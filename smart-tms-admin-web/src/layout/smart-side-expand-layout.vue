<!--
 * @Author: zhuoda
 * @Date: 2021-08-25 21:52:23
 * @LastEditTime: 2022-08-26
 * @LastEditors: zhuoda
 * @Description:
 * @FilePath: /smart-admin/src/layout/smart-side-expand-layout.vue
-->
<template>
  <a-layout :class="['admin-layout', 'smart-scroll']" style="min-height: 100%">
    <!-- 侧边菜单 side-menu -->
    <a-layout-sider :theme="theme" :class="['side-menu', 'smart-scroll']" :collapsed="collapsed" :trigger="null">
      <!-- 左侧菜单 -->
      <side-expand-menu :collapsed="collapsed" />
    </a-layout-sider>

    <!--
  中间内容，一共三部分：
  1、顶部
  2、中间内容区域
  3、底部（一般是公司版权信息）
 -->
    <a-layout class="admin-layout-main smart-scroll" :style="`height: ${windowHeight}px`" id="smartAdminMain">
      <!-- 顶部头部信息 -->
      <a-layout-header class="smart-layout-header">
        <a-row justify="space-between" class="smart-layout-header-user">
          <a-col class="smart-layout-header-left">
            <span class="collapsed-button">
              <menu-unfold-outlined v-if="collapsed" class="trigger" @click="() => (collapsed = !collapsed)" />
              <menu-fold-outlined v-else class="trigger" @click="() => (collapsed = !collapsed)" />
            </span>
            <a-tooltip placement="bottom">
              <template #title>首页</template>
              <span class="home-button" @click="goHome">
                <home-outlined class="trigger" />
              </span>
            </a-tooltip>
            <span class="location-breadcrumb">
              <menu-location-breadcrumb />
            </span>
          </a-col>
          <!---用戶操作区域-->
          <a-col class="smart-layout-header-right">
            <header-user-space />
          </a-col>
        </a-row>
        <page-tag />
      </a-layout-header>

      <!--中间内容-->
      <a-layout-content class="admin-layout-content" id="smartAdminLayoutContent">
        <!--不keepAlive的iframe使用单个iframe组件-->
        <IframeIndex v-show="iframeNotKeepAlivePageFlag" :key="route.name" :name="route.name" :url="route.meta.frameUrl" />
        <!--keepAlive的iframe 每个页面一个iframe组件-->
        <IframeIndex
          v-for="item in keepAliveIframePages"
          v-show="route.name == item.name"
          :key="item.name"
          :name="item.name"
          :url="item.meta.frameUrl"
        />
        <!--非iframe使用router-view-->
        <router-view v-show="!iframeNotKeepAlivePageFlag && keepAliveIframePages.every((e) => route.name != e.name)" v-slot="{ Component }">
          <keep-alive :include="keepAliveIncludes">
            <div :key="route.name">
              <component :is="Component" />
            </div>
          </keep-alive>
        </router-view>
      </a-layout-content>
      <!-- footer 版权公司信息 -->
      <a-layout-footer class="smart-layout-footer"> <SmartFooter /></a-layout-footer>

      <a-back-top :target="backTopTarget" :visibilityHeight="80" />
    </a-layout>

    <!-- 右侧帮助文档 help-doc -->
    <!-- 右侧帮助文档 help-doc -->
    <a-layout-sider v-show="helpDocFlag" theme="light" :width="180" class="help-doc-sider smart-scroll" :trigger="null" style="min-height: 100%">
      <SideHelpDoc />
    </a-layout-sider>
  </a-layout>
</template>
<script setup>
import { computed, onMounted, ref } from 'vue';
import HeaderUserSpace from './components/header-user-space/index.vue';
import MenuLocationBreadcrumb from './components/menu-location-breadcrumb/index.vue';
import PageTag from './components/page-tag/index.vue';
import SideExpandMenu from './components/side-expand-menu/index.vue';
import SmartFooter from './components/smart-footer/index.vue';
import { smartKeepAlive } from './smart-keep-alive';
import IframeIndex from '/@/components/iframe/iframe-index.vue';
import watermark from '/@/lib/smart-wartermark';
import { useAppConfigStore } from '/@/store/modules/system/app-config';
import { useUserStore } from '/@/store/modules/system/user';
import SideHelpDoc from './components/side-help-doc/index.vue';
import { useRouter } from 'vue-router';
import { HOME_PAGE_NAME } from '/@/constants/system/home-const';

const windowHeight = ref(window.innerHeight);
const theme = computed(() => useAppConfigStore().$state.sideMenuTheme);
const pageTagFlag = computed(() => useAppConfigStore().$state.pageTagFlag);
const helpDocFlag = computed(() => useAppConfigStore().$state.helpDocFlag);
const collapsed = ref(false);

onMounted(() => {
  watermark.set('smartAdminLayoutContent', useUserStore().actualName);
});
const backTopTarget = () => {
  return document.getElementById('smartAdminMain');
};
// ----------------------- keep-alive相关 -----------------------
let { route, keepAliveIncludes, iframeNotKeepAlivePageFlag, keepAliveIframePages } = smartKeepAlive();

const router = useRouter();
function goHome() {
  router.push({ name: HOME_PAGE_NAME });
}

function initHeight () {
  windowHeight.value = window.innerHeight
}
window.addEventListener('resize', function () {
  initHeight();
});
</script>
<style scoped lang="less">
:deep(.ant-layout-header) {
  height: auto;
}
:deep(.layout-header) {
  height: auto;
}

.smart-layout-header {
  background: #fff;
  padding: 0;
  z-index: 999;
}

.smart-layout-header-user {
  height: @header-user-height;
  border-bottom: 1px solid #f6f6f6;
}

.smart-layout-header-left {
  display: flex;
  height: @header-user-height;

  .collapsed-button {
    margin-left: 10px;
    line-height: @header-user-height;
  }

  .home-button {
    margin-left: 15px;
    cursor: pointer;
    padding: 0 5px;
    line-height: @header-user-height;
  }

  .home-button:hover {
    background-color: #efefef;
  }

  .location-breadcrumb {
    margin-left: 15px;
    line-height: @header-user-height;
  }
}

.smart-layout-header-right {
  display: flex;
  height: @header-user-height;
}

.admin-layout {
  .side-menu {
    flex: 0 !important;
    min-width: inherit !important;
    max-width: none !important;
    width: auto !important;
    &.fixed-side {
      position: fixed;
      height: 100vh;
      left: 0;
      top: 0;
    }
  }

  .help-doc-sider {
    flex: 0 !important;
    min-width: 100px;
    height: 100vh;
    max-width: 100px;
    width: auto !important;
    &.fixed-side {
      position: fixed;
      height: 100vh;
      right: 0;
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
    overflow-x: hidden;
    overflow-y: scroll;
  }

  .admin-layout-content {
    min-height: auto;
    position: relative;
    padding: 10px 10px 0px 10px;
    height: v-bind('pageTagFlag ? "calc(100% - 80px)": "calc(100% - 40px)"');
    overflow-y: scroll;
    overflow-x: hidden;
  }
}

.smart-layout-footer {
  position: relative;
  padding: 10px 0px;
  display: flex;
  justify-content: center;
}
</style>
