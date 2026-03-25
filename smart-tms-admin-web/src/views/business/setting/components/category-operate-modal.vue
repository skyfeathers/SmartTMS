<!--
 * @Description:
 * @version:
 * @Author: zhuoda
 * @Date: 2021-09-01 20:58:51
 * @LastEditors: zhuoda
 * @LastEditTime: 2022-06-02
-->
<template>
  <a-modal
    :open="visible"
    :title="form.categoryId ? '编辑' : '添加'"
    ok-text="确认"
    cancel-text="取消"
    @ok="onSubmit"
    @cancel="onClose"
  >
    <a-form
      ref="formRef"
      :model="form"
      :rules="rules"
      :label-col="{ span: 5 }"
      :wrapper-col="{ span: 12 }"
    >
      <a-form-item label="分类名称" name="categoryName">
        <a-input v-model:value="form.categoryName" placeholder="请输入分类名称" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>
<script setup>
import { ref, reactive } from "vue";
import { ValidateErrorEntity } from "ant-design-vue/lib/form/interface";
import { message } from "ant-design-vue";
import { useSpinStore } from "/@/store/modules/system/spin";
import _ from "lodash";
import { categoryApi } from "/@/api/business/category/category-api";

// ----------------------- 以下是字段定义 emits props ------------------------
// emit
const emit = defineEmits("reloadList");

//  组件
const formRef = ref();

const formDefault = {
  categoryId: undefined,
  categoryName: "",
  categoryType: 1,
  parentId: undefined,
  disabledFlag: false,
};
let form = reactive({ ...formDefault });
const rules = {
  categoryName: [{ required: true, message: "请输入分类名称" }],
};
// 是否展示抽屉
const visible = ref(false);

// ----------------------- 以下是生命周期 ------------------------

// ----------------------- 以下是方法 ------------------------
function showModal(categoryType, parentId, rowData) {
  Object.assign(form, formDefault);
  form.categoryType = categoryType;
  form.parentId = parentId;
  if (rowData && !_.isEmpty(rowData)) {
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
        if (form.categoryId) {
          await categoryApi.updateCategory(form);
        } else {
          await categoryApi.addCategory(form);
        }
        message.success(`${form.categoryId ? "修改" : "添加"}成功`);
        emit("reloadList", form.parentId);
        onClose();
      } catch (error) {
        console.log(error);
      } finally {
        useSpinStore().hide();
      }
    })
    .catch((error) => {
      console.log("error", error);
      message.error("参数验证错误，请仔细填写表单数据!");
    });
}

// ----------------------- 以下是暴露的方法内容 ------------------------
defineExpose({
  showModal,
});
</script>
