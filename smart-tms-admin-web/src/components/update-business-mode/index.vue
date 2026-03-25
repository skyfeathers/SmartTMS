<template>
  <a-modal v-model:open="visible" :width="450" title="经营方式" @close="onClose">
    <a-form ref="formRef">
      <a-form-item label="经营方式">
        <SmartEnumSelect v-model:value="form.businessMode" enumName="VEHICLE_BUSINESS_MODE_ENUM"
                         style="width:100%"/>
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
import SmartEnumSelect from '/@/components/smart-enum-select/index.vue';

defineExpose({
  showModal
});

const emit = defineEmits('refresh');
// form组件与参数
const formRef = ref();
let form = ref({
  businessMode: null
});
// 是否展示与方法
const visible = ref(false);
let updateHandle = null;

function showModal (updateForm, updateFunction) {
  form.value = Object.assign(updateForm, form);
  updateHandle = updateFunction;
  visible.value = true;
}

// 操作
async function onSubmit () {
  if (!form.value.businessMode) {
    message.error('请选择经营方式');
    return;
  }
  if (!updateHandle) {
    message.error('未设置更新方法');
    return;
  }
  try {
    useSpinStore().show();
    await updateHandle(form.value);
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
