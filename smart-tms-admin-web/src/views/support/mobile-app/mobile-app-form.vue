<template>
  <a-modal
    :closable="true"
    :destroyOnClose="true"
    :maskClosable="false"
    :onCancel="onClose"
    :title="form.changeLogId ? '编辑' : '添加'"
    :open="visibleFlag"
    width="600px"
    @close="onClose"
  >
    <a-form ref="formRef" :label-col="{ span: 5 }" :model="form" :rules="rules">
      <a-form-item label="平台" name="platformType">
        <SmartEnumSelect v-model:value="form.platformType" enumName="MOBILE_APP_PLATFORM_TYPE_ENUM" placeholder="平台类型" width="100%" />
      </a-form-item>
      <a-form-item label="版本号" name="versionNo">
        <a-input v-model:value="form.versionNo" placeholder="版本号" style="width: 100%" />
        </a-form-item>
      <a-form-item label="强制更新" name="forceUpdateFlag">
        <a-switch v-model:checked="forceUpdateFlagChecked" checked-children="强制" un-checked-children="非强制" @change="forceUpdateFlagCheckedChange" />
      </a-form-item>
      <a-form-item label="最新版" name="newestFlag">
        <a-switch v-model:checked="newestFlagChecked" checked-children="最新" un-checked-children="过时" @change="newestFlagCheckedChange" />
      </a-form-item>
      <a-form-item label="APP包文件" name="appFile">
        <Upload
            :defaultFileList="form.appFile"
            :folder="FILE_FOLDER_TYPE_ENUM.MOBILE_APP.value"
            :maxSize="300"
            :maxUploadSize="1"
            :multiple="true"
            buttonText="点击上传文件"
            @change="changeAttachment"
        />
      </a-form-item>
      <a-form-item label="更新内容" name="updateDesc">
        <a-textarea v-model:value="form.updateDesc" :rows="5"  placeholder="更新内容" style="width: 100%" />
      </a-form-item>
    </a-form>

    <template #footer>
      <a-space>
        <a-button @click="onClose">取消</a-button>
        <a-button type="primary" @click="onSubmit">保存</a-button>
      </a-space>
    </template>
  </a-modal>
</template>
<script setup>
  import { reactive, ref, nextTick } from 'vue';
  import _ from 'lodash';
  import { message } from 'ant-design-vue';
  import { SmartLoading } from '/@/components/smart-loading';
  import { mobileAppApi } from '/@/api/support/mobile-app/mobile-app-api';
  import { smartSentry } from '/@/lib/smart-sentry';
  import { FILE_FOLDER_TYPE_ENUM } from '/@/constants/business/file-const';
  import Upload from '/@/components/upload/index.vue';
  import SmartEnumSelect from '/@/components/framework/smart-enum-select/index.vue';
  // ------------------------ 事件 ------------------------

  const emits = defineEmits(['reloadList']);

  // ------------------------ 显示与隐藏 ------------------------
  // 是否显示
  const visibleFlag = ref(false);

  function show(rowData) {
    Object.assign(form, formDefault);
    if (rowData && !_.isEmpty(rowData)) {
      Object.assign(form, rowData);
    }
    visibleFlag.value = true;
    nextTick(() => {
      formRef.value.clearValidate();
    });
  }

  function onClose() {
    Object.assign(form, formDefault);
    visibleFlag.value = false;
  }

  // ------------------------ 表单 ------------------------

  // 组件ref
  const formRef = ref();

  const formDefault = {
    id: undefined,
    platformType: undefined, //平台类型
    versionNo: undefined, //版本号
    updateDesc: undefined, //版本更新描述
    forceUpdateFlag: false, //是否强制更新
    newestFlag: true, //是否是最新版
    appFile: undefined, //文件
  };

  let form = reactive({ ...formDefault });

  const rules = {
    platformType: [{ required: true, message: '平台类型 必填' }],
    versionNo: [{ required: true, message: '版本号 必填' }],
    forceUpdateFlag: [{ required: true, message: '是否强制更新 必填' }],
    newestFlag: [{ required: true, message: '是否是最新版 必填' }],
  };

  const forceUpdateFlagChecked = ref(false);
  const newestFlagChecked = ref(true);

  function forceUpdateFlagCheckedChange(checked) {
    form.forceUpdateFlag = checked;
  }
  function newestFlagCheckedChange(checked) {
    form.newestFlag = !checked;
  }

  function changeAttachment (fileList) {
    form.appFile = fileList;
  }

  // 点击确定，验证表单
  async function onSubmit() {
    try {
      await formRef.value.validateFields();
      save();
    } catch (err) {
      message.error('参数验证错误，请仔细填写表单数据!');
    }
  }

  // 新建、编辑API
  async function save() {
    SmartLoading.show();
    try {
      if (form.id) {
        await mobileAppApi.update(form);
      } else {
        await mobileAppApi.add(form);
      }
      message.success('操作成功');
      emits('reloadList');
      onClose();
    } catch (err) {
      smartSentry.captureError(err);
    } finally {
      SmartLoading.hide();
    }
  }

  defineExpose({
    show,
  });
</script>
