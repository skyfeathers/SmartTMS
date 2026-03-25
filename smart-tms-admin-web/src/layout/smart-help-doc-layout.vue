<template>
  <a-layout :class="['admin-layout', 'smart-scroll']" style="min-height: 100%">
    <!-- 侧边目录 side-menu -->
    <a-layout-sider :class="['side-menu', 'smart-scroll']" :collapsed="false" theme="light" :width="250">
      <div class="help-doc-tree">
        <!-- 头部 logo -->
        <div class="logo" @click="goHome" style="width: 100%" v-if="!collapsed">
          <img class="logo-img" :src="logoImg" />
          <div class="title">{{websiteName}}</div>
        </div>

        <!-- 目录内容 -->
        <div>
          <a-directory-tree
            v-model:expandedKeys="expandedKeys"
            v-model:selectedKeys="selectedKeys"
            :tree-data="helpDocTreeData"
            @select="selectHelpDoc"
          />
        </div>
      </div>
    </a-layout-sider>

    <!--
      中间内容，一共三部分：
      1、顶部
      2、中间内容区域
      3、底部（一般是公司版权信息）
     -->
    <a-layout id="smartAdminMain" :style="`height: ${windowHeight}px`" class="admin-layout-main smart-scroll">
      <!-- 顶部头部信息 -->
      <a-layout-header class="layout-header">
        <a-row class="layout-header-user">
          <a-col class="layout-header-right">
            <strong>帮助文档</strong>
          </a-col>
        </a-row>
      </a-layout-header>

      <!--中间内容-->
      <a-layout-content id="smartAdminLayoutContent" class="admin-layout-content">
        <router-view v-slot="{ Component }">
          <div :key="route.fullPath">
            <component :is="Component" />
          </div>
        </router-view>
      </a-layout-content>

      <!-- footer 版权公司信息 -->
      <a-layout-footer class="layout-footer">
        <smart-footer />
      </a-layout-footer>

      <a-back-top :target="backTopTarget" :visibilityHeight="80" />
    </a-layout>
  </a-layout>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue';
import { useAppConfigStore } from '../store/modules/system/app-config';
import SmartFooter from './components/smart-footer/index.vue';
import watermark from '/@/lib/smart-wartermark';
import { useUserStore } from '/@/store/modules/system/user';
import logoImg from '/@/assets/images/logo/logo.png';
import { HOME_PAGE_NAME } from '/@/constants/system/home-const';
import { useRouter, useRoute } from 'vue-router';
import { helpDocApi } from '/@/api/support/help-doc/help-doc-api';
import { helpDocCatalogApi } from '/@/api/support/help-doc/help-doc-catalog-api';
import { SmartLoading } from '/@/components/smart-loading';
import _ from 'lodash';

const pageTagFlag = computed(() => useAppConfigStore().$state.pageTagFlag);
const theme = computed(() => useAppConfigStore().$state.sideMenuTheme);
const websiteName = computed(() => useAppConfigStore().websiteName);
const windowHeight = window.innerHeight;
const collapsed = ref(false);
onMounted(() => {
  watermark.set('smartAdminLayoutContent', useUserStore().actualName);
});
const backTopTarget = () => {
  return document.getElementById('smartAdminMain');
};
const router = useRouter();
const route = useRoute();
function goHome() {
  router.push({ name: HOME_PAGE_NAME });
}

// ----------------------- 选中 节点 -----------------------------
const expandedKeys = ref([]);
const selectedKeys = ref([]);

function selectHelpDoc(selectedKeys) {
  let key = selectedKeys[0];
  if (key.indexOf(TYPE_CATALOG_PREFIX) > -1) {
    return;
  }

  let helpDocId = key.substr(TYPE_HELP_DOC_PREFIX.length);
  router.push({ path: '/help-doc/detail', query: { helpDocId } });
}

// 更新展开节点
function updateExpandedKeys(helpDocId, helpDocList, catalogList) {
  expandedKeys.value = [TYPE_HELP_DOC_PREFIX + helpDocId];
  selectedKeys.value = [TYPE_HELP_DOC_PREFIX + helpDocId];

  let helpDoc = helpDocList.filter((e) => e.helpDocId == helpDocId);
  let catalogId = null;
  if (helpDoc.length > 0) {
    catalogId = helpDoc[0].helpDocCatalogId;
  }

  if (catalogId) {
    expandedKeys.value.push(TYPE_CATALOG_PREFIX + catalogId);
  }

  let parentId = catalogId;
  while (parentId !== 0) {
    let catalog = catalogList.filter((e) => e.helpDocCatalogId == parentId);
    if (catalog.length > 0) {
      parentId = catalog[0].parentId;
      expandedKeys.value.push(TYPE_CATALOG_PREFIX + catalog[0].helpDocCatalogId);
    } else {
      parentId = 0;
    }
  }
}

// ----------------------- 帮助文档 目录 树 -----------------------------
onMounted(queryHelpDocTree);

const helpDocTreeData = ref([]);

const TYPE_CATALOG_PREFIX = 'catalog_';
const TYPE_HELP_DOC_PREFIX = 'help_doc_';
//目录默认id为0
const HELP_DOC_CATALOG_PARENT_ID = 0;
//查询帮助文档树形结构
async function queryHelpDocTree() {
  SmartLoading.show();
  try {
    let { data: catalogList } = await helpDocCatalogApi.getAll();
    let { data: helpDocList } = await helpDocApi.getAllHelpDocList();

    //设置前缀
    for (const item of catalogList) {
      item.key = TYPE_CATALOG_PREFIX + item.helpDocCatalogId;
      item.title = item.name;
    }
    //转为map，供递归使用
    let helpDocMap = new Map();
    for (const item of helpDocList) {
      item.key = TYPE_HELP_DOC_PREFIX + item.helpDocId;
      let list = helpDocMap.get(item.helpDocCatalogId);
      if (!list) {
        list = [];
        helpDocMap.set(item.helpDocCatalogId, list);
      }
      list.push(item);
    }

    helpDocTreeData.value = buildHelpDocCatalogTree(catalogList, 0, helpDocMap);
    if (!route.query.helpDocId && firstHelpDocId) {
      selectHelpDoc([TYPE_HELP_DOC_PREFIX + firstHelpDocId]);
      return;
    }

    //更新展开节点
    updateExpandedKeys(route.query.helpDocId, helpDocList, catalogList);
  } catch (e) {
    console.log(e);
  } finally {
    SmartLoading.hide();
  }
}

// 记录第一个树
let firstHelpDocId = null;
// 构建目录树
function buildHelpDocCatalogTree(data, parentId, helpDocMap) {
  let children = data.filter((e) => e.parentId === parentId) || [];
  //排序
  children = _.sortBy(children, (e) => e.sort);

  let helpDocList = helpDocMap.get(parentId);
  if (helpDocList) {
    //排序
    helpDocList = _.sortBy(helpDocList, (e) => e.sort);
    if (!firstHelpDocId) {
      firstHelpDocId = helpDocList[0].helpDocId;
    }
    children.push(...helpDocList);
  }

  for (const e of children) {
    if (e.key.indexOf(TYPE_HELP_DOC_PREFIX) > -1) {
      continue;
    }
    e.children = buildHelpDocCatalogTree(data, e.helpDocCatalogId, helpDocMap);
  }

  return children;
}
</script>

<style lang="less" scoped>
:deep(.ant-layout-header) {
  height: auto;
}
:deep(.layout-header) {
  height: auto;
}
:deep(.ant-tree-treenode) {
  margin: 2px 0px;
}

.layout-header {
  background: #fff;
  padding: 0;
  z-index: 999;
}

.layout-header-user {
  height: @header-user-height;
  display: flex;
  justify-content: flex-end;
  border-bottom: 1px solid #f6f6f6;
}

.layout-header-right {
  display: flex;
  height: @header-user-height;
  line-height: @header-user-height;
  margin-right: 20px;
  font-size: 16px;
}

.layout-container {
  height: calc(100vh - @header-height);
  overflow-x: hidden;
  overflow-y: auto;
}

.admin-layout {
  .side-menu {
    height: 100vh;
    overflow: scroll;
    .help-doc-tree {
      color: #001529;
    }

    .logo {
      height: @header-user-height;
      line-height: @header-user-height;
      padding: 0px 15px 0px 15px;
      z-index: 9999;
      display: flex;
      cursor: pointer;
      justify-content: flex-start;
      margin-bottom: 10px;

      .logo-img {
        width: 40px;
        height: 40px;
      }

      .title {
        font-size: 16px;
        font-weight: 600;
        text-align: center;
        color: '#001529';
      }
    }

    &.fixed-side {
      position: fixed;
      height: 100vh;
      left: 0;
      top: 0;
    }
  }

  .virtual-side {
    transition: all 0.2s;
  }

  .virtual-header {
    transition: all 0.2s;
    opacity: 0;

    &.fixed-tabs.multi-page:not(.fixed-header) {
      height: 0;
    }
  }

  .admin-layout-main {
    overflow-y: scroll;
    overflow-x: hidden;
  }

  .admin-layout-content {
    min-height: auto;
    position: relative;
    overflow-y: scroll;
    overflow-x: hidden;
    padding: 10px 10px 0px 10px;
    height: v-bind('pageTagFlag ? "calc(100% - 80px)": "calc(100% - 40px)"');
  }
}

.layout-footer {
  position: relative;
  padding: 10px 0px;
  display: flex;
  justify-content: center;
}
</style>
