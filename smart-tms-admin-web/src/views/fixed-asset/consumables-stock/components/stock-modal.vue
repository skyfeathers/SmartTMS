<template>
  <a-modal width="600px" :open="visible" :title="form.consumablesId?'编辑':'新建'" ok-text="确定" cancel-text="取消"
           @ok="onSubmit" @cancel="onClose"
           :getContainer="getContainer">
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 5 }" :wrapper-col="{ span: 19 }">
      <a-form-item label="耗材名称" name="consumablesName">
        <a-input v-model:value="form.consumablesName" placeholder="请输入耗材名称 " style="width:200px"/>
      </a-form-item>
      <a-form-item label="所属分类" name="categoryId">
        <CategoryTreeSelect v-model:value="form.categoryId" :categoryType="CATEGORY_TYPE_ENUM.CONSUMABLES.value"
                            placeholder="请选择所属分类" style="width:200px"/>
      </a-form-item>
      <a-form-item label="库存预警值" name="stockWarningValue">
        <a-input-number v-model:value="form.stockWarningValue" :min="0" :precision="0" placeholder="请输入库存预警值"
                        style="width:200px"/>
      </a-form-item>
      <a-form-item label="备注" name="remark">
        <a-textarea :rows="4" v-model:value="form.remark" type="textarea" placeholder="请输入备注" style="width:200px"/>
      </a-form-item>
      <a-form-item label="附件" name="attachment">
        <Upload
            :maxUploadSize="5"
            buttonText="点击上传附件"
            :default-file-list="form.attachment || []"
            @change="changeAttachment"
            :folder="FILE_FOLDER_TYPE_ENUM.SHIPPER.value"
        />
      </a-form-item>
    </a-form>
  </a-modal>
</template>
<script setup>
import EnterpriseSelect from '/@/components/enterprise-select/index.vue';
import CategoryTreeSelect from '/@/components/fixed-asset/category-tree-select/index.vue';
import Upload from '/@/components/upload/index.vue';

import { FILE_FOLDER_TYPE_ENUM } from '/@/constants/business/file-const';
import { getContainer } from '/@/utils/container-util';
import { ref, reactive } from 'vue';
import { message } from 'ant-design-vue';
import { useSpinStore } from '/@/store/modules/system/spin';
import { consumablesStockApi } from '/@/api/fixed-asset/asset/consumables-stock-api';
import { CATEGORY_TYPE_ENUM } from '/@/constants/business/category-const';
// ----------------------- 以下是字段定义 emits props ------------------------
// emit
const emit = defineEmits(['reloadList']);

// ----------------------- 弹窗展示关闭 start ------------------------
// 是否展示
const visible = ref(false);

function showModal (rowData) {
  Object.assign(form, formDefault);
  if (rowData) {
    Object.assign(form, rowData);
  }
  visible.value = true;
}

function onClose () {
  Object.assign(form, formDefault);
  visible.value = false;
}

// ----------------------- 提交 ------------------------
//  组件
const formRef = ref();

const formDefault = {
  consumablesId: null,
  consumablesName: '',
  categoryId: null,
  remark: '',
  stockWarningValue: null,
  attachment: []
};
let form = reactive({ ...formDefault });
const rules = {
  consumablesName: [{ required: true, message: '请输入耗材名称' }],
  categoryId: [{ required: true, message: '请选择所属分类' }],
  stockWarningValue: [{ required: true, message: '请输入库存预警值' }],
};

function onSubmit () {
  formRef.value
      .validate()
      .then(async () => {
        useSpinStore().show();
        try {
          if (form.consumablesId) {
            await consumablesStockApi.update(form);
            message.success('更新成功');
          } else {
            await consumablesStockApi.add(form);
            message.success('添加成功');
          }
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

function changeAttachment (fileList) {
  form.attachment = fileList;
}

// ----------------------- 以下是暴露的方法内容 ------------------------
defineExpose({
  showModal,
});
</script>
