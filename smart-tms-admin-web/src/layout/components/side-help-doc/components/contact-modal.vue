<!--
 * @Description:保险表单
 * @version:
 * @Author: zhuoda
 * @Date: 2021-09-01 20:58:51
 * @LastEditors: zhuoda
 * @LastEditTime: 2022-08-25
-->
<template>
  <a-modal :open="visible" title="联系客服人员" :closable="false" :maskClosable="true">
    <div>
      <a-result>
        <template #icon>
          <img :src="wechat" />
        </template>
<!--        <template #title>
          <h4>微信号：xxxxxxxx</h4>
          <h4>电话：13123123123</h4>
        </template>
        <template #extra>
          <p>ps: 如果没人接电话，请去xxxxx找xxxxx</p>
        </template>-->
      </a-result>
    </div>
    <template #footer>
      <a-button type="primary" @click="hide">知道了</a-button>
    </template>
  </a-modal>
</template>
<script setup>
import { ref, reactive, nextTick, computed } from 'vue';
import wechat from '/@/assets/images/1024lab/wechat.png';

defineExpose({
  show,
});

const visible = ref(false);
function show() {
  visible.value = true;
}
function hide() {
  visible.value = false;
}
</script>
<style scoped>
img{
  width: 300px;
  height: 300px;
}
</style>
