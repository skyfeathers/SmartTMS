<!--
 * @Description:
 * @version:
 * @Author: zhuoda
 * @Date: 2021-09-01 20:58:51
 * @LastEditors: zhuoda
 * @LastEditTime: 2022-06-23
-->
<template>
  <a-modal :open="visible" :title="form.systemConfigId ? '编辑' : '添加'" ok-text="确认" cancel-text="取消" @ok="onSubmit" @cancel="onClose">
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 5 }">
      <a-form-item label="参数Key" name="configKey">
        <a-input v-model:value="form.configKey" placeholder="请输入参数Key" />
      </a-form-item>
      <a-form-item label="参数名称" name="configName">
        <a-input v-model:value="form.configName" placeholder="请输入参数名称" />
      </a-form-item>
      <a-form-item label="参数值" name="configValue">
        <a-textarea v-model:value="form.configValue" placeholder="请输入参数值" />
      </a-form-item>
      <a-form-item label="备注" name="remark">
        <a-textarea v-model:value="form.remark" :rows="2"></a-textarea>
      </a-form-item>
    </a-form>
  </a-modal>
</template>
<script setup>
  import { message } from 'ant-design-vue';
  import { reactive, ref } from 'vue';
  import { configApi } from '../../../api/support/config/config-api';
  import { useSpinStore } from '/@/store/modules/system/spin';

  // ----------------------- 以下是字段定义 emits props ------------------------
  // emit
  const emit = defineEmits('reloadList');

  //  组件
  const formRef = ref();

  const formDefault = {
    systemConfigId: undefined,
    configKey: '',
    configName: '',
    configValue: '',
    remark: '',
  };
  let form = reactive({ ...formDefault });
  const rules = {
    configKey: [{ required: true, message: '请输入参数key' }],
    configName: [{ required: true, message: '请输入参数名称' }],
    configValue: [{ required: true, message: '请输入参数值' }],
  };
  // 是否展示
  const visible = ref(false);

  // ----------------------- 以下是生命周期 ------------------------

  // ----------------------- 以下是方法 ------------------------
  function showModal(rowData) {
    Object.assign(form, formDefault);
    if (rowData) {
      Object.assign(form, rowData);
    }
    visible.value = true;
  }

  function onClose() {
    Object.assign(form, formDefault);
    visible.value = false;
  }

  function onSubmit() {
    formRef.value
      .validate()
      .then(async () => {
        useSpinStore().show();
        try {
          if (form.systemConfigId) {
            await configApi.updateConfig(form);
          } else {
            await configApi.addConfig(form);
          }
          message.success(`${form.systemConfigId ? '修改' : '添加'}成功`);
          emit('reloadList');
          onClose();
        } catch (error) {
          console.log(error);
        } finally {
          useSpinStore().hide();
        }
      })
      .catch((error) => {
        console.log('error', error);
        message.error('参数验证错误，请仔细填写表单数据!');
      });
  }

  // ----------------------- 以下是暴露的方法内容 ------------------------
  defineExpose({
    showModal,
  });
</script>
