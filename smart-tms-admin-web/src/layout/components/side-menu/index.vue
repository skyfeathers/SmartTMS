<!--
 * @Description:
 * @Author: zhuoda
 * @Date: 2021-11-05
 * @LastEditTime: 2022-08-26
 * @LastEditors: zhuoda
-->

<template>
  <!--
    左侧菜单分为两部分：
    1、顶部logo区域，包含 logo和名称
    2、下方菜单区域
   -->

  <!-- 顶部logo区域 -->
  <div class="logo" @click="goHome" v-if="!collapsed">
    <img v-if="appDefaultConfig.logoUrl" class="logo-img" :src="appDefaultConfig.logoUrl" />
    <div class="title">{{ websiteName }}</div>
  </div>
  <div class="min-logo" @click="goHome" v-if="collapsed">
    <img v-if="appDefaultConfig.logoUrl" class="logo-img" :src="appDefaultConfig.logoUrl" />
  </div>

  <!-- 下方菜单区域： 这里使用一个递归菜单解决 -->
  <recursion-menu :collapsed="collapsed" ref="menu" />
</template>

<script setup>
import { computed, ref, watch } from 'vue';
import { useRouter } from 'vue-router';
import RecursionMenu from './recursion-menu.vue';
import { HOME_PAGE_NAME } from '/@/constants/system/home-const';
import { useAppConfigStore } from '/@/store/modules/system/app-config';

let appDefaultConfig = useAppConfigStore();
const sideMenuWidth = computed(() => 'width:' + useAppConfigStore().sideMenuWidth + 'px');
const sideMenuTheme = computed(() => useAppConfigStore().sideMenuTheme);
const websiteName = computed(() => useAppConfigStore().websiteName);

const props = defineProps({
  collapsed: {
    type: Boolean,
    required: false,
    default: false,
  },
});

const menu = ref();

watch(
  () => props.collapsed,
  (newValue, oldValue) => {
    // 如果是展开菜单的话，重新获取更新菜单的展开项: openkeys和selectKeys
    if (!newValue) {
      menu.value.updateOpenKeysAndSelectKeys();
    }
  }
);

const router = useRouter();
function goHome() {
  router.push({ name: HOME_PAGE_NAME });
}
</script>

<style lang="less" scoped>
.shadow {
  box-shadow: 2px 0 6px rgba(0, 21, 41, 0.35);
}
.side-menu {
  min-height: 100vh;
  overflow-y: auto;
  z-index: 10;

  .min-logo {
    height: @header-user-height;
    line-height: @header-user-height;
    padding: 0px 15px 0px 15px;
    width: 100%;
    z-index: 9999;
    display: flex;
    justify-content: center;
    .logo-img {
      width: 32px;
      height: 32px;
    }
  }

  .logo {
    width: 100%;
    height: @header-user-height;
    line-height: @header-user-height;
    padding: 0px 15px 0px 15px;
    z-index: 9999;
    display: flex;
    cursor: pointer;
    justify-content: space-around;

    .logo-img {
      width: 40px;
      height: 40px;
    }

    .title {
      font-size: 16px;
      font-weight: 600;
      text-align: center;
      color: v-bind('sideMenuTheme === "light" ? "#001529": "#ffffff"');
    }
  }
}
.menu {
  padding: 16px 0;
}
</style>
