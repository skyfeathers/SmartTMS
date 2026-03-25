<template>
  <a-modal width="1300px" :open="visible" title="分段信息" ok-text="提交" cancel-text="取消" @ok="onClose" @cancel="onClose">
    <WaybillSplitTransportList :waybillId="waybillIdRef"/>
  </a-modal>
</template>
<script setup>
import { ref} from 'vue';
import WaybillSplitTransportList from './waybill-split-transport-list.vue';

// ----------------------- 对外暴露 ------------------------
defineExpose({
  showModal,
});

// ----------------------- 显示/隐藏 ------------------------
const visible = ref(false);
function onClose() {
  visible.value = false;
  waybillIdRef.value = 0;
}

const waybillIdRef = ref(0);
function showModal(waybillId) {
  waybillIdRef.value = waybillId;
  visible.value = true;
}

</script>
<style scoped lang="less">
</style>
