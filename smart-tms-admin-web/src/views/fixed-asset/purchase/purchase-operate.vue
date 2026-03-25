<template>
  <div class="smart-form-wrapper">
    <a-form class="smart-form" labelAlign="right" ref="formRef" :model="form" :rules="rules">
      <a-card title="基本信息">
        <a-row>
          <a-col :lg="width.lg" :md="width.md" :sm="width.sm" :xl="width.xl" :xs="width.xs" :xxl="width.xxl">
            <a-form-item label="采购编号" name="purchaseNo">
              <a-input v-model:value="form.purchaseNo" placeholder="请输入采购编号" style="width:200px"/>
              <a-tooltip placement="top">
                <template #title>
                  <span>为空，则由系统自动生成</span>
                </template>
                <question-circle-outlined />
              </a-tooltip>
            </a-form-item>
          </a-col>
          <a-col :lg="width.lg" :md="width.md" :sm="width.sm" :xl="width.xl" :xs="width.xs" :xxl="width.xxl">
            <a-form-item label="资产来源" name="sourceId">
              <SmartDictSelect keyCode="ASSET-SOURCE" v-model:value="form.sourceId" placeholder="请选择资产来源"
                               style="width:200px"/>
            </a-form-item>
          </a-col>
          <a-col :lg="width.lg" :md="width.md" :sm="width.sm" :xl="width.xl" :xs="width.xs" :xxl="width.xxl">
            <a-form-item label="备注" name="remark">
              <a-input v-model:value="form.remark" placeholder="请输入备注" style="width:200px"/>
            </a-form-item>
          </a-col>
        </a-row>
      </a-card>
      <!-- 资产列表 -->
      <AssetTable ref="assetTableRef"/>
    </a-form>

    <a-card class="smart-margin-top5 flex-end" size="small">
      <a-space>
        <a-button @click="onClose">取消</a-button>
        <a-button type="primary" @click="onSubmit()">保存</a-button>
      </a-space>
    </a-card>
  </div>
</template>
<script setup>
import AssetTable from './components/purchase-asset-table.vue';
import SmartDictSelect from '/@/components/smart-dict-select/index.vue';
import EnterpriseSelect from '/@/components/enterprise-select/index.vue';

import { FILE_FOLDER_TYPE_ENUM } from '/@/constants/business/file-const';
import { CATEGORY_TYPE_ENUM } from '/@/constants/business/category-const';
import { reactive, ref, watch, computed, onMounted, nextTick } from 'vue';
import { message } from 'ant-design-vue';
import { SmartLoading } from '/@/components/smart-loading';
import { assetApi } from '/@/api/fixed-asset/asset/asset-api';
import { formWidth } from '/@/views/business/hooks/form-hooks';
import { useAppConfigStore } from '/@/store/modules/system/app-config';
import { useRoute, useRouter } from 'vue-router';
import { useUserStore } from '/@/store/modules/system/user';
import _ from 'lodash';
import { assetPurchaseApi } from '/@/api/fixed-asset/asset/purchase-api';

// ------------------------ 事件 ------------------------
const helpDocFlag = computed(() => useAppConfigStore().$state.helpDocFlag);
let { initWidth } = formWidth();
let width = ref({});
width.value = initWidth();
watch(
    () => helpDocFlag.value,
    () => {
      width.value = initWidth();
    }
);
// ------------------------ 事件 ------------------------

const emits = defineEmits(['reloadList']);

// ---------------- 操作表单 ----------------
let route = useRoute();
let router = useRouter();

onMounted(() => {
  let assetId = route.query.assetId;
  if (assetId) {
    useUserStore().setTagName(route, '编辑资产采购');
    getDetail(assetId);
  } else {
    useUserStore().setTagName(route, '新建资产采购');
  }
});

// ------------------------ 表单 ------------------------

// 组件ref
const formRef = ref();

const formDefault = {
  purchaseNo: undefined, //资产编号
  sourceId: undefined, //资产名称
  remark: null,
};

let form = reactive({ ...formDefault });

const rules = {
  // purchaseNo: [{ required: true, message: '请输入资产编号' }],
  sourceId: [{ required: true, message: '请选择资产来源' }],
};

function changeAttachment (fileList) {
  form.attachment = fileList;
}

const assetTableRef = ref();
// 点击确定，验证表单
async function onSubmit () {
  try {
    await formRef.value.validateFields();
    let assetList = await assetTableRef.value.validate();
    save(assetList);
  } catch (err) {
    console.log(err)
  }
}

// 新建、编辑API
async function save (assetList) {
  SmartLoading.show();
  try {
    let param = _.cloneDeep(form);
    param.assetList = assetList;
    await assetPurchaseApi.add(param);
    message.success('操作成功');
    onClose();
  } catch (err) {
    console.log(err);
  } finally {
    SmartLoading.hide();
  }
}


// 关闭
function onClose () {
  useUserStore().closePage(route, router);
}

</script>
