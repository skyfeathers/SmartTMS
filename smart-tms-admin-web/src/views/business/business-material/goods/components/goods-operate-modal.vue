<!--
 * @Description: file content
 * @Author: yandy
 * @Date: 2022-07-07 10:18:53
 * @LastEditors:
 * @LastEditTime: 2022-07-07 10:18:53
-->
<template>
  <a-modal :open="visible" :title="form.goodsId ? '编辑' : '添加'" ok-text="确认" cancel-text="取消" @ok="onSubmit" @cancel="onClose">
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 5 }" :wrapper-col="{ span: 18 }">
      <a-form-item label="货物名称" name="goodsName">
        <a-input v-model:value="form.goodsName" placeholder="请输入货物名称" />
      </a-form-item>
      <a-form-item label="货物类型" name="goodsType">
        <smart-dict-select style="width: 100%" keyCode="cargoTypeClassificationCode" v-model:value="form.goodsType" placeholder="请选择货物类型" />
      </a-form-item>

      <a-form-item label="排序" name="sort">
        <a-input v-model:value="form.sort" placeholder="请输入排序" />
        <h6 style="color: #ababab">值越小越靠前</h6>
      </a-form-item>
    </a-form>
  </a-modal>
</template>
<script setup>
import { ref, reactive, nextTick } from 'vue';
import { message } from 'ant-design-vue';
import { useSpinStore } from '/@/store/modules/system/spin';
import { goodsApi } from '/@/api/business/business-material/goods-api';
import SmartDictSelect from '/@/components/smart-dict-select/index.vue';
import emitter from '/@/utils/mitt-util';
import _ from 'lodash';

// ----------------------- 以下是字段定义 emits props ------------------------
// emit
const emit = defineEmits(['reloadList']);

//  组件
const formRef = ref();

const formDefault = {
  goodsId: undefined,
  goodsName: '',
  goodsType: null,
  sort: null,
};
let form = reactive({ ...formDefault });

const rules = {
  goodsName: [{ required: true, message: '请输入货物名称' }],
  goodsType: [{ required: true, message: '请输入货物类型' }],
  sort: [{ required: true, message: '请输入排序' }],
};
// 是否展示
const visible = ref(false);
// 地区
const area = ref([]);

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
  formRef.value.resetFields();
  visible.value = false;
}

function onSubmit() {
  formRef.value
    .validate()
    .then(async () => {
      useSpinStore().show();
      try {
        if (form.goodsId) {
          await goodsApi.update(form);
        } else {
          await goodsApi.create(form);
        }
        message.success(`${form.goodsId ? '修改' : '添加'}成功`);
        emit('reloadList');
        emitter.emit('createGoods');
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
