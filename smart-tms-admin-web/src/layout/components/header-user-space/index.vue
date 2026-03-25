<!--
 * @Description:
 * @Author: zhuoda
 * @Date: 2021-08-03
 * @LastEditTime: 2022-08-25
 * @LastEditors: zhuoda
-->
<template>
  <a-space :size="10">
      <a-button class="user-space-item" title="快捷搜索" type="text" v-if="!show && userSpaceType == 'mini'" @click="showSearch">
        <template #icon><search-outlined/></template>
      </a-button>
      <HeaderAutoComplete @hideSearch="hideSearch" ref="autoCompleteRef" v-else/>
      <div class="user-space-item" @click="showQuickCreateModal" v-privilege="'vehicle:quickCreate'">
        <plus-square-outlined v-if="userSpaceType == 'mini'" title="快速新建" />
        <a-button v-else size="small" type="primary">
          <template #icon>
            <PlusOutlined />
          </template>
          快速新建
        </a-button>
      </div>
      <div class="user-space-item" @click="toAddOrder" v-privilege="'order:add'">
          <DiffOutlined v-if="userSpaceType == 'mini'" title="新建订单" />
          <a-button v-else size="small" type="primary">
              <template #icon>
                  <PlusOutlined />
              </template>
              新建订单
          </a-button>
      </div>
      <!---消息通知--->
      <HeaderNotice class="user-space-item" ref="headerNotice" @click="showNotice"/>
      <!---国际化--->
      <!-- <a-button type="text" @click="showSetting" class="operate-icon">
        <template #icon><switcher-outlined /></template>
        i18n
      </a-button> -->
      <!---设置--->
      <a-button class="user-space-item" title="设置" type="text" @click="showSetting">
        <template #icon><setting-outlined /></template>
      </a-button>
    <!---头像信息--->
    <div class="user-space-item" >
      <header-avatar />
    </div>

    <!---帮助文档--->
    <div class="user-space-item" @click="showHelpDoc">
      <question-circle-two-tone title="帮助文档" style="font-size: 12px; margin-right: 5px" />
      <span v-if="userSpaceType != 'mini'">帮助文档</span>
    </div>
    <HeaderSetting ref="headerSetting" />
    <VehicleQuickCreate ref="quickCreateRef" />
  </a-space>
</template>

<script setup>
import HeaderAvatar from './header-avatar.vue';
import HeaderSetting from './header-setting.vue';
import HeaderNotice from './header-notice.vue';
import HeaderAutoComplete from './header-auto-complete.vue';
import VehicleQuickCreate from '/@/components/vehicle-quick-create/index.vue';
import { ref, nextTick, computed } from 'vue';
import { useAppConfigStore } from '/@/store/modules/system/app-config';
import {router} from "/@/router";

const userSpaceType = computed(() => useAppConfigStore().$state.userSpaceType);

const quickCreateRef = ref();

function showQuickCreateModal () {
  quickCreateRef.value.showModal();
}


const show = ref(false)
const autoCompleteRef = ref()
function showSearch(){
  show.value = true;
  nextTick(()=>{
    autoCompleteRef.value.focus()
  })
}

function hideSearch(){
  show.value = false;
}

const headerSetting = ref();
function showSetting() {
  headerSetting.value.show();
}
const headerNotice = ref();
function showNotice() {
  headerNotice.value.showNotice();
}

function showHelpDoc() {
  useAppConfigStore().showHelpDoc();
}
function toAddOrder() {
    router.push({path: '/order/add'});
}
</script>

<style lang="less" scoped>
.user-space-item {
  height: 40px;
  color: inherit;
  padding: 0 5px;
  cursor: pointer;
  display: flex;
  align-items: center;
  font-size: 12px;
  a {
    color: inherit;

    i {
      font-size: 16px;
    }
  }
}



.user-space-item:hover {
  color: @primary-color;
  background: @hover-bg-color;
}

.setting {
  height: @header-user-height;
  line-height: @header-user-height;
  vertical-align: middle;
  display: flex;
  align-items: center;
}

.operate-icon {
  margin-left: 15px;
}
</style>
