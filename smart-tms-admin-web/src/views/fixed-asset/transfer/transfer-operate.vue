<template>
  <div class="smart-form-wrapper">
    <a-form class="smart-form" labelAlign="right" ref="formRef" :model="form" :rules="rules">
      <a-card title="基本信息">
        <a-row>
          <a-col :lg="width.lg" :md="width.md" :sm="width.sm" :xl="width.xl" :xs="width.xs" :xxl="width.xxl">
            <a-form-item label="转出位置" name="fromLocationId">
              <LocationSelect v-model:value="form.fromLocationId" @change="changeFromLocation"  placeholder="请选择转出位置" style="width:200px"/>
            </a-form-item>
          </a-col>
          <a-col :lg="width.lg" :md="width.md" :sm="width.sm" :xl="width.xl" :xs="width.xs" :xxl="width.xxl">
            <a-form-item label="转入位置" name="toLocationId">
              <LocationSelect v-model:value="form.toLocationId" placeholder="请选择转入位置" style="width:200px"/>
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
      <AssetTableSelect ref="assetTableSelectRef" :operateFlag="true"/>
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
import LocationSelect from '/@/components/fixed-asset/locaton-select/index.vue';
import AssetTableSelect from '/@/components/fixed-asset/asset-table-select/index.vue';
import EnterpriseSelect from '/@/components/enterprise-select/index.vue';

import { FILE_FOLDER_TYPE_ENUM } from '/@/constants/business/file-const';
import { CATEGORY_TYPE_ENUM } from '/@/constants/business/category-const';
import { ASSET_STATUS_ENUM } from '/@/constants/fixed-asset/asset-const';
import { reactive, ref, watch, computed, onMounted, provide } from 'vue';
import { message } from 'ant-design-vue';
import { SmartLoading } from '/@/components/smart-loading';
import { formWidth } from '/@/views/business/hooks/form-hooks';
import { useAppConfigStore } from '/@/store/modules/system/app-config';
import { useRoute, useRouter } from 'vue-router';
import { useUserStore } from '/@/store/modules/system/user';
import _ from 'lodash';
import { assetTransferApi } from '/@/api/fixed-asset/asset/transfer-api';

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

// ---------------- 操作表单 ----------------

let route = useRoute();
let router = useRouter();

onMounted(() => {
  let assetId = route.query.assetId;
  if (assetId) {
    useUserStore().setTagName(route, '编辑转移');
  } else {
    useUserStore().setTagName(route, '新建转移');
  }
});

// ------------------------ 表单 ------------------------

// 组件ref
const formRef = ref();

const formDefault = {
  transferNo: undefined, //转拨
  fromLocationId: undefined, // 转出位置
  toLocationId: undefined, // 转入位置
  remark: null
};

let form = reactive({ ...formDefault });

const rules = {
  fromLocationId: [{ required: true, message: '请选择转出位置' }],
  toLocationId: [{ required: true, message: '请选择转入位置' }],
};

const assetTableSelectRef = ref();

function changeFromEnterprise () {
  form.fromLocationId = null;
  assetTableSelectRef.value.clear();
}

function changeFromLocation () {
  assetTableSelectRef.value.clear();
}

function changeToEnterprise () {
  form.toLocationId = null;
}


// 点击确定，验证表单
async function onSubmit () {
  try {
    await formRef.value.validateFields();
    let assetList = assetTableSelectRef.value.tableData;
    if (_.isEmpty(assetList)) {
      message.error('请选择转拨资产');
      return;
    }
    save(assetList.map(e => e.assetId));
  } catch (err) {
    console.log(err);
  }
}

// 新建、编辑API
async function save (assetIdList) {
  SmartLoading.show();
  try {
    let param = _.cloneDeep(form);
    param.assetIdList = assetIdList;
    await assetTransferApi.add(param);
    message.success('创建成功');
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

let customQueryForm = computed(() => {
  return {
    statusList: [
      ASSET_STATUS_ENUM.UNUSED.value
    ],
    locationId: form.fromLocationId
  };
});
provide('setCustomQueryForm', customQueryForm);

// 由于需要验证转出位置，所以增加自定义验证方法
function validateParam (param) {
  if (!param) {
    return false;
  }
  if(!param.value.locationId){
    message.error('请选择转出位置')
    return false;
  }
  return true;
}
provide('setValidateParam', validateParam)
</script>
