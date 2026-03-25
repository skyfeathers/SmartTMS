<!--
 * @Author: zhuoda
 * @Date: 2021-08-03 10:27:11
 * @LastEditTime: 2022-08-19
 * @LastEditors: zhuoda
 * @Description:
 * @FilePath: /smart-admin/src/views/system/home/index.vue
-->
<template>
  <div class="home">
    <div class="home-left">
      <FunctionEntry class="smart-bottom-10"/>
      <Statistics class="smart-bottom-10"/>
      <div class="home-left-content">
          <HomeWaitHandle />
          <CredentialsCount />
      </div>
      
    </div>
    <div class="home-right">
      <HomeHeader class="smart-bottom-10" />
      <div class="home-right-content">
        <div class="home-right-content-notice">
            <HomeNotice title="公告" :noticeTypeId="1" class="smart-bottom-10" :icon="NoticeIcon" />
        </div>
        <div class="home-right-content-notice">
            <HomeNotice title="通知" :noticeTypeId="2" :icon="MessageIcon" class="smart-bottom-10" />
        </div>
        <div class="changelog-card-box">
          <ChangelogCard />
        </div>
        
        
      </div>
      
    </div>
    
    
  </div>
</template>
<script setup>
import {computed, onMounted, ref} from 'vue';
import {useRouter} from 'vue-router';
import HomeHeader from './home-header.vue';
import HomeNotice from './home-notice.vue';
import HomeWaitHandle from './home-wait-handle.vue';
import HomeWaitReceive from './home-wait-receive.vue';
import HeaderResetPassword from '/@/layout/components/header-user-space/header-reset-password-modal/index.vue';
import ChangelogCard from './components/changelog-card.vue';

import FunctionEntry from './components/function-entry.vue';
import Weather from './components/weather.vue';
import Statistics from './components/statistics.vue';
import CredentialsCount from './components/credentials-count.vue';
import NoticeIcon from "/@/assets/images/home/notice-icon.png";
import MessageIcon from "/@/assets/images/home/message-icon.png";


// ----------------------- 以下是字段定义 emits props ---------------------
let router = useRouter();
// ----------------------- 强制修改密码 ---------------------
const resetPasswordRef = ref();
onMounted(() => {
  if (window.history.state.forceEditPwdFlag) {
    resetPasswordRef.value.showModal(true);
  }
})

function forceEditPwdChange() {
  window.history.state.forceEditPwdFlag = false
}

// ----------------------- 以下是计算属性 watch监听 ------------------------
// 业绩完成百分比
const saleTargetPercent = computed(() => {
  return 75;
});
defineExpose({});
</script>
<style lang="less" scoped>
@import './index.less';

.smart-bottom-10 {
  margin-bottom: 10px;
}

.home {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: stretch;
  column-gap: 15px;
  height: calc(100vh - 115px);
  &-left {
    flex: 65%;
    display: flex;
    flex-direction: column;
    &-content {
      flex: 1;
      overflow: hidden;
      display: flex;
      flex-direction: row;
      column-gap: 10px;
      //直接子元素
      > * {
        flex: 1;
        overflow: hidden;
      }
    }
  }
  &-right {
    flex: 35%;
    display: flex;
    flex-direction: column;
    overflow: hidden;
    &-content {
      flex: 1;
      // 选择直接子元素
      display: flex;
      flex-direction: column;
      row-gap: 10px;
      overflow: hidden;
      &-notice {
        flex-shrink: 0;
        flex: 35%;
        display: flex;
        flex-direction: row;
        column-gap: 10px;
        overflow: hidden;
      }
      .changelog-card-box {
        flex: 30%;
        flex-shrink: 0;
        overflow: hidden;
      }
      
    }
    
  }
}
</style>
