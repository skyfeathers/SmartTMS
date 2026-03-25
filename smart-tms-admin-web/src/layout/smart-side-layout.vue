<template>
  <a-layout :class="['admin-layout', 'smart-scroll']" style="min-height: 100%">
    <!-- 侧边菜单 side-menu -->
    <a-layout-sider :class="['side-menu', 'smart-scroll']" :width="sideMenuWidth" :collapsed="collapsed" :theme="theme">
      <!-- 左侧菜单 -->
      <side-menu :collapsed="collapsed" />
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
        <a-row class="layout-header-user" justify="space-between">
          <a-col class="layout-header-left" :style="{
            'max-width': `calc(100% - ${rightWidth}px)`
          }">
            <div class="layout-header-box">
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
                <page-tag v-if="pageTagLocation == 'top'"/>
                <MenuLocationBreadcrumb v-else/>
              </span>
            </div>
            
          </a-col>
          <!---用戶操作区域-->
          <a-col class="layout-header-right">
            <header-user-space />
          </a-col>
        </a-row>
        <page-tag v-if="pageTagLocation == 'center'"/>
      </a-layout-header>

      <!--中间内容-->
      <a-layout-content id="smartAdminLayoutContent" class="admin-layout-content">
        <!--不keepAlive的iframe使用单个iframe组件-->
        <IframeIndex v-if="iframeNotKeepAlivePageFlag" :key="route.name" :name="route.name" :url="route.meta.frameUrl" />
        <!--keepAlive的iframe 每个页面一个iframe组件-->
        <IframeIndex
          v-for="item in keepAliveIframePages"
          v-show="route.name == item.name"
          :key="item.name"
          :name="item.name"
          :url="item.meta.frameUrl"
        />
        <!--非iframe使用router-view-->
        <div
          v-show="!iframeNotKeepAlivePageFlag && keepAliveIframePages.every((e) => route.name != e.name)"
          style="height: 100%"
        >
           <router-view v-slot="{ Component }">
            <keep-alive :include="keepAliveIncludes">
              <component :is="Component"/>
            </keep-alive>
          </router-view>
        </div>
      </a-layout-content>

      <!-- footer 版权公司信息 -->
      <a-layout-footer class="layout-footer">
        <smart-footer />
      </a-layout-footer>

      <a-back-top :target="backTopTarget" :visibilityHeight="80" />
    </a-layout>

    <!-- 右侧帮助文档 help-doc -->
    <a-layout-sider v-show="helpDocFlag" theme="light" :width="180" class="help-doc-sider smart-scroll" :trigger="null" style="min-height: 100%">
      <SideHelpDoc />
    </a-layout-sider>
  </a-layout>
</template>

<script setup>
import { computed, onMounted, ref, watch, nextTick } from 'vue';
import { useAppConfigStore } from '../store/modules/system/app-config';
import HeaderUserSpace from './components/header-user-space/index.vue';
import MenuLocationBreadcrumb from './components/menu-location-breadcrumb/index.vue';
import PageTag from './components/page-tag/index.vue';
import SideMenu from './components/side-menu/index.vue';
import SmartFooter from './components/smart-footer/index.vue';
import { smartKeepAlive } from './smart-keep-alive';
import IframeIndex from '/@/components/iframe/iframe-index.vue';
import watermark from '/@/lib/smart-wartermark';
import { useUserStore } from '/@/store/modules/system/user';
import SideHelpDoc from './components/side-help-doc/index.vue';
import { useRouter } from 'vue-router';
import { HOME_PAGE_NAME } from '/@/constants/system/home-const';

const sideMenuWidth = computed(() => useAppConfigStore().$state.sideMenuWidth);
const pageTagFlag = computed(() => useAppConfigStore().$state.pageTagFlag);
const theme = computed(() => useAppConfigStore().$state.sideMenuTheme);
const helpDocFlag = computed(() => useAppConfigStore().$state.helpDocFlag);
const pageTagLocation = computed(() => useAppConfigStore().$state.pageTagLocation);
const windowHeight = ref(window.innerHeight);
const collapsed = ref(false);
const appConfigStore = useAppConfigStore();

watch(pageTagLocation,(newVal)=>{
  if(newVal == 'top'){
    nextTick(()=>{
      sizeComputed();
    })
  }
},{
  immediate:true
})

onMounted(() => {
  watermark.set('smartAdminLayoutContent', useUserStore().actualName);
});
const rightWidth = ref(0)
let rightMaxWidth = 0;
let maxWidth = 0
let tagsWidth = 0
function sizeComputed(){
  const tagParentElement = document.querySelector('.location-breadcrumb')
  const tagsElement = tagParentElement.querySelector('.ant-tabs-nav-list')
  const parentElement = document.querySelector('.layout-header-user')
  maxWidth = parentElement.offsetWidth
  const rightElement = document.querySelector('.layout-header-right')
  rightWidth.value = rightElement.offsetWidth
  let ro = new ResizeObserver((e) => {
    let result = e.find(j=>{
      return j.target.className === 'ant-tabs-nav-list'
    })
    maxWidth = parentElement.offsetWidth
    rightWidth.value = rightElement.offsetWidth + 10
    if(rightWidth.value > rightMaxWidth){
      rightMaxWidth = rightWidth.value
    }
    if(result){
      tagsWidth = result.contentRect.width
    }else{
      tagsWidth = tagsElement.offsetWidth;
    }
    if(tagsWidth + rightMaxWidth + 160 > maxWidth){
      appConfigStore.$patch({
        userSpaceType: 'mini',
      });
    }else{
      appConfigStore.$patch({
        userSpaceType: 'default',
      });
    }
  })
  ro.observe(rightElement)
  ro.observe(tagsElement)
  ro.observe(parentElement)
}



const backTopTarget = () => {
  return document.getElementById('smartAdminMain');
};

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
// ----------------------- keep-alive相关 -----------------------
let { route, keepAliveIncludes, iframeNotKeepAlivePageFlag, keepAliveIframePages } = smartKeepAlive();
</script>

<style lang="less" scoped>
:deep(.ant-layout-header) {
  height: auto;
}
:deep(.layout-header) {
  height: auto;
}

.layout-header {
  background: #fff;
  padding: 0;
  z-index: 999;
}

.layout-header-user {
  height: @header-user-height;
  border-bottom: 1px solid #f6f6f6;
}

.layout-header-left {
  display: flex;
  height: @header-user-height;

  .layout-header-box{
    width: 100%;
    overflow: hidden;
    display: flex;
  }

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
    width: calc(100% - 56px);
    margin-left: 15px;
    line-height: @header-user-height;
    display: flex;
    align-items: center;
  }
}

.layout-header-right {
  display: flex;
  height: @header-user-height;
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
