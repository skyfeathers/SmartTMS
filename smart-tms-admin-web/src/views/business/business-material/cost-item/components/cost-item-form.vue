<!--
 * @Description:费用表单
 * @version:
 * @Author: zhuoda
 * @Date: 2021-09-01 20:58:51
 * @LastEditors: zhuoda
 * @LastEditTime: 2022-07-18
-->
<template>
  <a-modal
      :width="500"
      :open="visible"
      :title="form.insuranceId ? '编辑' : '添加'"
      ok-text="保存"
      cancel-text="取消"
      @ok="onSubmit"
      @cancel="onClose"
  >
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 4 }">
      <a-row>
        <a-col :span="24">
          <a-form-item label="类型" name="type">
            <SmartEnumSelect width="100%" v-model:value="form.type" placeholder="请选择类型"
                             enum-name="COST_ITEM_TYPE_ENUM"/>
          </a-form-item>
        </a-col>

        <a-col :span="24">
          <a-form-item label="分类" name="category">
            <SmartEnumSelect width="100%" v-model:value="form.category" placeholder="请选择分类"
                             enum-name="COST_ITEM_CATEGORY_ENUM"/>
          </a-form-item>
        </a-col>

        <a-col :span="24">
          <a-form-item label="费用名称" name="name">
            <a-input v-model:value="form.name" placeholder="请输入费用名称" style="width: 100%"/>
          </a-form-item>
        </a-col>

        <a-col :span="24">
          <a-form-item label="排序" name="seq">
            <a-input-number v-model:value="form.seq" style="width: 100%"/>
          </a-form-item>
        </a-col>

        <a-col :span="24">
          <a-form-item label="备注" name="remark">
            <a-input v-model:value="form.remark" placeholder="请输入备注" style="width: 100%"/>
          </a-form-item>
        </a-col>
      </a-row>
    </a-form>
  </a-modal>
</template>
<script setup>
import SmartEnumSelect from '/@/components/smart-enum-select/index.vue';
import {ref, reactive, nextTick} from 'vue';
import {message} from 'ant-design-vue';
import _ from 'lodash';
import {SmartLoading} from '/@/components/smart-loading';
import {costItemApi} from '/@/api/business/cost-item/cost-item-api';

// ----------------------- 对外暴露 ------------------------
const emit = defineEmits('reloadList');
defineExpose({
  showModal,
});

// ----------------------- 显示/隐藏 ------------------------
const visible = ref(false);

function onClose() {
  Object.assign(form, formDefault);
  visible.value = false;
}

function showModal(rowData) {
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
  type: null,
  category: null,
  name: null,
  seq: 0,
  remark: undefined,
  costItemId: undefined,
};
let form = reactive({...formDefault});
const rules = {
  name: [{required: true, message: '请输入费用名称'}],
  type: [{required: true, message: '请选择类型'}],
  category: [{required: true, message: '请选择分类'}],
};

// ----------------------- 提交表单 ------------------------
const formRef = ref();

function onSubmit() {
  formRef.value
      .validate()
      .then(async () => {
        SmartLoading.show();
        try {
          if (form.costItemId) {
            await costItemApi.update(form);
          } else {
            await costItemApi.save(form);
          }
          message.success(`${form.costItemId ? '修改' : '添加'}成功`);
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
