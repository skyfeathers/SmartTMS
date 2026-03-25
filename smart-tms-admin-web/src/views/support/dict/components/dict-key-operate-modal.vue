<!--
 * @Description:
 * @Author: zhuoda
 * @Date: 2022-06-24
 * @LastEditTime: 2022-06-24
 * @LastEditors: zhuoda
-->
<template>
  <a-modal :open="visible" :title="form.dictKeyId ? '编辑' : '添加'" ok-text="确认" cancel-text="取消" @ok="onSubmit" @cancel="onClose">
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 5 }" :wrapper-col="{ span: 12 }">
      <a-form-item label="编码" name="keyCode">
        <a-input v-model:value="form.keyCode" placeholder="请输入编码" />
      </a-form-item>
      <a-form-item label="名称" name="keyName">
        <a-input v-model:value="form.keyName" placeholder="请输入名称" />
      </a-form-item>

      <a-form-item label="备注" name="remark">
        <a-textarea v-model:value="form.remark" :rows="2"></a-textarea>
      </a-form-item>
    </a-form>
  </a-modal>
</template>
<script setup>
  import { ref, reactive } from 'vue';
  import { ValidateErrorEntity } from 'ant-design-vue/lib/form/interface';
  import { message } from 'ant-design-vue';
  import { useSpinStore } from '/@/store/modules/system/spin';
  import { dictApi } from '/@/api/support/dict/dict-api';

  // ----------------------- 以下是字段定义 emits props ------------------------
  // emit
  const emit = defineEmits(['reloadList']);

  //  组件
  const formRef = ref();

  const formDefault = {
    dictKeyId: undefined,
    keyCode: '',
    keyName: '',
    remark: '',
  };
  let form = reactive({ ...formDefault });
  const rules = {
    keyCode: [{ required: true, message: '请输入编码' }],
    keyName: [{ required: true, message: '请输入名称' }],
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
          if (form.dictKeyId) {
            await dictApi.keyEdit(form);
          } else {
            await dictApi.keyAdd(form);
          }
          message.success(`${form.dictKeyId ? '修改' : '添加'}成功`);
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
