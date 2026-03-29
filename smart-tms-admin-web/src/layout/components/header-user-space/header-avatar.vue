<!--
 * @Author: zhuoda
 * @Date: 2021-08-03 10:27:11
 * @LastEditTime: 2022-06-24
 * @LastEditors: zhuoda
 * @Description:
 * @FilePath: /smart-admin/src/layout/components/smart-header-user-space/header-avatar.vue
-->

<template>
  <a-dropdown class="header-trigger">
    <div class="wrapper">
      <a-avatar style="margin: 0 5px" :size="compactFlag ? 17 : 20" id="smartAdminAvatar">
        {{ avatarName }}
      </a-avatar>
      <span v-if="userSpaceType != 'mini'" class="name">{{ actualName }}</span>
    </div>
    <template #overlay>
      <a-menu :class="['avatar-menu']">
        <a-menu-item @click="refresh">
          <span>刷新权限</span>
        </a-menu-item>
        <a-menu-item @click="showUpdatePwdModal">
          <span>修改密码</span>
        </a-menu-item>
        <a-menu-item @click="onLogout">
          <span>退出登录</span>
        </a-menu-item>
      </a-menu>
    </template>
  </a-dropdown>
  <header-reset-password ref="resetPasswordRef"/>
</template>
<script setup>
  import { onMounted, computed, ref } from 'vue';
  import { useRouter } from 'vue-router';
  import { loginApi } from '/@/api/system/login/login-api';
  import { useUserStore } from '/@/store/modules/system/user';
  import { clearAllCoolies } from '/@/utils/cookie-util';
  import { localClear } from '/@/utils/local-util';
  import headerResetPassword from "/@/components/header-reset-password-modal/index.vue";
  import { useAppConfigStore } from '/@/store/modules/system/app-config';

  const userSpaceType = computed(() => useAppConfigStore().$state.userSpaceType);

  // 是否紧凑
  const compactFlag = computed(() => useAppConfigStore().compactFlag);
  // 头像背景颜色
  const AVATAR_BACKGROUND_COLOR_ARRAY = ['#87d068', '#00B853', '#f56a00', '#1890ff'];

  const avatarName = ref('');
  const router = useRouter();
  // ----------------------- 以下是计算属性 watch监听 ------------------------
  const actualName = computed(() => useUserStore().actualName);

  async function onLogout() {
    localClear();
    clearAllCoolies();
    useUserStore().logout();
    try {
      await loginApi.logout();
    } catch (e) {
    } finally {
      location.reload();
    }
  }

  async function refresh() {
    await loginApi.refresh();
    location.reload();
  }

  function updateAvatar() {
    if (useUserStore().actualName) {
      avatarName.value = useUserStore().actualName.substr(0, 1);
      const avatar = document.getElementById('smartAdminAvatar');
      if (avatar) {
        avatar.style.backgroundColor = AVATAR_BACKGROUND_COLOR_ARRAY[hashcode(avatarName.value) % 4];
      }
    }
  }

  // 修改密码 组件
  const resetPasswordRef = ref();

  function showUpdatePwdModal () {
    resetPasswordRef.value.showModal();
  }

  function hashcode(str) {
    let hash = 1,
      i,
      chr;
    if (str.length === 0) return hash;
    for (i = 0; i < str.length; i++) {
      chr = str.charCodeAt(i);
      hash = (hash << 5) - hash + chr;
      hash |= 0; // Convert to 32bit integer
    }
    return hash;
  }

  onMounted(updateAvatar);
</script>
<style lang="less" scoped>
  .wrapper {
    cursor: pointer;
    display: flex;
    align-items: center;
  }
  .header-trigger {
    height: @header-user-height;
    line-height: @header-user-height;

    .avatar {
      vertical-align: middle;
    }

    .name {
      margin-left: 5px;
      font-weight: 500;
    }
  }
</style>
