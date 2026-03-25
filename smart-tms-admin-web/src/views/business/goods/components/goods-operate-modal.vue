<template>
  <a-drawer
    :title="form.goodsId ? '编辑' : '添加'"
    :width="720"
    :open="visible"
    :body-style="{ paddingBottom: '80px' }"
    @close="onClose"
  >
    <a-form
      ref="formRef"
      :model="form"
      :rules="rules"
      :label-col="{ span: 5 }"
      :wrapper-col="{ span: 12 }"
    >
      <a-form-item label="商品类型" name="goodsType">
        <smart-enum-select
          style="width: 100%"
          v-model:value="form.goodsType"
          placeholder="请选择类型"
          enum-name="GOODS_TYPE_ENUM"
        />
      </a-form-item>
      <a-form-item label="商品分类" name="categoryId">
        <category-tree
          v-model:value="form.categoryId"
          placeholder="请选择商品分类"
          :categoryType="CATEGORY_TYPE_ENUM.GOODS.value"
        />
      </a-form-item>
      <a-form-item label="商品名称" name="goodsName">
        <a-input v-model:value="form.goodsName" placeholder="请输入商品名称" />
      </a-form-item>
      <a-form-item label="商品状态" name="shelvesFlag">
        <a-radio-group v-model:value="form.shelvesFlag">
          <a-radio :value="true">上架</a-radio>
          <a-radio :value="false">下架</a-radio>
        </a-radio-group>
      </a-form-item>
      <a-form-item label="商品价格" name="price">
        <a-input-number
          style="width: 100%"
          placeholder="请输入商品价格"
          v-model:value="form.price"
          :min="0"
          :max="9999"
        />
      </a-form-item>
      <a-form-item label="备注" name="remark">
        <a-input
          style="width: 100%"
          placeholder="请输入备注"
          v-model:value="form.remark"
        />
      </a-form-item>
    </a-form>
    <div
      :style="{
        position: 'absolute',
        right: 0,
        bottom: 0,
        width: '100%',
        borderTop: '1px solid #e9e9e9',
        padding: '10px 16px',
        background: '#fff',
        textAlign: 'right',
        zIndex: 1,
      }"
    >
      <a-button style="margin-right: 8px" @click="onClose">取消</a-button>
      <a-button type="primary" @click="onSubmit">提交</a-button>
    </div>
  </a-drawer>
</template>
<script setup>
import { ref, nextTick, reactive, createVNode } from "vue";
import SmartEnumSelect from "/@/components/smart-enum-select/index.vue";
import CategoryTree from "/@/components/category-tree-select/index.vue";
import { GOODS_TYPE_ENUM } from "/@/constants/business/goods-const";
import { CATEGORY_TYPE_ENUM } from "/@/constants/business/category-const";
import { ValidateErrorEntity } from "ant-design-vue/lib/form/interface";
import { message } from "ant-design-vue";
import { useSpinStore } from "/@/store/modules/system/spin";
import _ from "lodash";
import { goodsApi } from "/@/api/business/goods/goods-api";

// ----------------------- 以下是字段定义 emits props ------------------------
// emit
const emit = defineEmits("reloadList");

// 组件ref
const formRef = ref();

const formDefault = {
  categoryId: undefined,
  coverPic: "",
  goodsIntro: "",
  goodsName: "",
  goodsType: GOODS_TYPE_ENUM.BOOK.value,
  price: undefined,
  shelvesFlag: true,
  goodsId: undefined,
  // remark: "",
};
let form = reactive({ ...formDefault });
const rules = {
  categoryId: [{ required: true, message: "请选择商品分类" }],
  goodsType: [{ required: true, message: "请选择商品分组" }],
  goodsName: [{ required: true, message: "商品名称不能为空" }],
  price: [{ required: true, message: "商品价格不能为空" }],
};
// 是否展示抽屉
const visible = ref(false);

// ----------------------- 以下是生命周期 ------------------------

// ----------------------- 以下是方法 ------------------------
function showDrawer(rowData) {
  Object.assign(form, formDefault);
  if (rowData && !_.isEmpty(rowData)) {
    Object.assign(form, rowData);
  }
  console.log(form);
  visible.value = true;
  nextTick(() => {});
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
        if (form.goodsId) {
          await goodsApi.updateGoods(form);
        } else {
          await goodsApi.addGoods(form);
        }
        message.success(`${form.goodsId ? "修改" : "添加"}成功`);
        onClose();
        emit("reloadList");
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
  showDrawer,
});
</script>
