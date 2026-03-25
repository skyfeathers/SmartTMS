<!--
 * @Description:科目表单
 * @version:
 * @Author: zhuoda
 * @Date: 2021-09-01 20:58:51
 * @LastEditors: zhuoda
 * @LastEditTime: 2022-07-18
-->
<template>
  <a-modal
      :title="form.categoryId ? '编辑' : '添加'"
      :open="visible"
      :width="500"
      cancel-text="取消"
      ok-text="保存"
      @cancel="onClose"
      @ok="onSubmit"
  >
    <a-form ref="formRef" :label-col="{ span: 6 }" :model="form" :rules="rules">
      <a-row>
        <a-col :span="24">
          <a-form-item label="类型" name="costType">
            <SmartEnumSelect v-model:value="form.costType" enum-name="CAR_COST_CATEGORY_TYPE_ENUM" placeholder="请选择类型"
                             width="100%"/>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="支付方式" name="payMode">
            <SmartEnumSelect v-model:value="form.payMode" enum-name="CAR_COST_PAY_MODE_ENUM" placeholder="请选择支付方式"
                             width="100%"/>
          </a-form-item>
        </a-col>

        <a-col :span="24">
          <a-form-item label="科目名称" name="categoryName">
            <a-input v-model:value="form.categoryName" placeholder="请输入科目名称" style="width: 100%"/>
          </a-form-item>
        </a-col>

        <a-col :span="24">
          <a-form-item label="排序" name="sort">
            <a-input-number v-model:value="form.sort" style="width: 100%"/>
          </a-form-item>
        </a-col>
      </a-row>
    </a-form>
  </a-modal>
</template>
<script setup>
import SmartEnumSelect from '/@/components/smart-enum-select/index.vue';
import { ref, reactive, nextTick } from 'vue';
import { message } from 'ant-design-vue';
import _ from 'lodash';
import { SmartLoading } from '/@/components/smart-loading';
import { carCostCategoryApi } from '/@/api/business/waybill/car-cost-category-api';

// ----------------------- 对外暴露 ------------------------
const emit = defineEmits('reloadList');
defineExpose({
  showModal,
});

// ----------------------- 显示/隐藏 ------------------------
const visible = ref(false);

function onClose () {
  Object.assign(form, formDefault);
  visible.value = false;
}

function showModal (rowData) {
  Object.assign(form, formDefault);
  if (rowData && !_.isEmpty(rowData)) {
    Object.assign(form, rowData);
  }
  visible.value = true;
  nextTick(() => {
    formRef.value.clearValidate();
  });
}

// ----------------------- 表单 ------------------------
const formDefault = {
  sort: 0,
  categoryName: null,
  costType: null,
  payMode: null
};
let form = reactive({ ...formDefault });
const rules = {
  categoryName: [{ required: true, message: '请输入科目名称' }],
  costType: [{ required: true, message: '请选择类型' }],
  payMode: [{ required: true, message: '请选择支付方式' }],
};

// ----------------------- 提交表单 ------------------------
const formRef = ref();

function onSubmit () {
  formRef.value
      .validate()
      .then(async () => {
        SmartLoading.show();
        try {
          if (form.categoryId) {
            await carCostCategoryApi.update(form);
          } else {
            await carCostCategoryApi.save(form);
          }
          message.success(`${form.categoryId ? '修改' : '添加'}成功`);
          emit('reloadList');
          onClose();
        } catch (error) {
          console.log(error);
        } finally {
          SmartLoading.hide();
        }
      })
      .catch((error) => {
        console.log('error', error);
        message.error('参数验证错误，请仔细填写表单数据!');
      });
}
</script>
