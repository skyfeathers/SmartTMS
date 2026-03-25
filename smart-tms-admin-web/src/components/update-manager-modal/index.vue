<template>
  <a-modal v-model:open="visible" :width="700" title="审核" @close="onClose">
    <a-form ref="formRef">
      <a-form-item label="负责人">
        <employee-select v-model:value="form.managerId" :leaveFlag="false" placeholder="请选择负责人" style="width: 80%"/>
      </a-form-item>
    </a-form>
    <template #footer>
      <a-button @click="onClose">取消</a-button>
      <a-button type="primary" @click="onSubmit">确定</a-button>
    </template>
  </a-modal>
</template>
<script setup>
import { ref } from 'vue';
import { message } from 'ant-design-vue';
import { useSpinStore } from '/@/store/modules/system/spin';
import EmployeeSelect from '/@/components/employee-select/index.vue';

defineExpose({
  showModal
});

const emit = defineEmits('refresh');
// form组件与参数
const formRef = ref();
let form = ref({
  managerId: null
});
// 是否展示与方法
const visible = ref(false);
let auditHandle = null;

function showModal (auditForm, auditFunction) {
  form.value = Object.assign(auditForm, form);
  auditHandle = auditFunction;
  visible.value = true;
}

// 操作
async function onSubmit () {
  if (!form.value.managerId) {
    message.error('请选择负责人');
    return;
  }
  if (!auditHandle) {
    message.error('未设置更新方法');
    return;
  }
  try {
    useSpinStore().show();
    await auditHandle(form.value);
    message.success('操作成功');
    onClose();
    emit('refresh');
  } catch (e) {
    console.log(e);
  } finally {
    useSpinStore().hide();
  }
}

function onClose () {
  visible.value = false;
}

</script>
<style lang='less' scoped></style>
