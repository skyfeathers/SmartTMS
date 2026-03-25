<template>
  <a-modal width="950px" :open="visible" title="货物信息" ok-text="保存" cancel-text="取消" @ok="onClose" @cancel="onClose">
    <WaybillGoods :goods-list="goodsListRef" />
  </a-modal>
</template>
<script setup>
import { ref } from 'vue';
import WaybillGoods from './waybill-goods.vue';


let goodsListRef = ref([]);

// ----------------------- 显示/隐藏 ------------------------
const visible = ref(false);
function onClose() {
  goodsListRef.value = [];
  visible.value = false;
}

function showModal(goodsList) {
  goodsListRef.value = goodsList;
  visible.value = true;
}

defineExpose({
  showModal,
});

</script>
