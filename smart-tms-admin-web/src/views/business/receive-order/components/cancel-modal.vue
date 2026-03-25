<template>
  <a-modal width="600px" :open="visible" :title="props.title" ok-text="确认" cancel-text="取消" @ok="onSubmit"
           @cancel="onClose">
    <a-textarea :rows="2" v-model:value="remark" type="textarea" placeholder="请输入作废备注" style="width:80%"/>
  </a-modal>
</template>
<script setup>
import { ref } from 'vue';

// ----------------------- 以下是字段定义 emits props ------------------------
const props = {
  title: {
    type: String,
  }
};
// emit
const emit = defineEmits(['onSubmit']);

// 是否展示
const visible = ref(false);

// ----------------------- 以下是生命周期 ------------------------
let receiveOrderId = null;
let remark = ref('');

function showModal (updateId) {
  receiveOrderId = updateId;
  remark.value = '';
  visible.value = true;
}

function onClose () {
  visible.value = false;
}

function onSubmit () {
  onClose();
  emit('onSubmit', { remark: remark.value }, receiveOrderId);
}

// ----------------------- 以下是暴露的方法内容 ------------------------
defineExpose({
  showModal,
});
</script>
