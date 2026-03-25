<template>
  <div class="smart-form-wrapper">
    <a-form class="smart-form" labelAlign="right" ref="formRef" :model="form" :rules="rules">
      <a-card title="基本信息">
        <a-row>
          <a-col :lg="width.lg" :md="width.md" :sm="width.sm" :xl="width.xl" :xs="width.xs" :xxl="width.xxl">
            <a-form-item label="领用日期" name="useTime">
              <a-date-picker v-model:value="form.useTime" valueFormat="YYYY-MM-DD" style="width:200px"/>
            </a-form-item>
          </a-col>
          <a-col :lg="width.lg" :md="width.md" :sm="width.sm" :xl="width.xl" :xs="width.xs" :xxl="width.xxl">
            <a-form-item label="所属位置" name="locationId">
              <LocationSelect v-model:value="form.locationId" placeholder="请选择所属位置" style="width:200px"/>
            </a-form-item>
          </a-col>
          <a-col :lg="width.lg" :md="width.md" :sm="width.sm" :xl="width.xl" :xs="width.xs" :xxl="width.xxl">
            <a-form-item label="领用部门" name="departmentId">
              <DepartmentTreeSelect ref="departmentTreeSelect" width="100%" :init="false" placeholder="请选择使用部门"
                                    v-model:value="form.departmentId" style="width:200px"/>
            </a-form-item>
          </a-col>
          <a-col :lg="width.lg" :md="width.md" :sm="width.sm" :xl="width.xl" :xs="width.xs" :xxl="width.xxl">
            <a-form-item label="领用人" name="userId">
              <employee-select v-model:value="form.userId" :leaveFlag="false" :disabledFlag="0"
                               placeholder="请选择使用人" style="width:200px"/>
            </a-form-item>
          </a-col>
          <a-col :lg="width.lg" :md="width.md" :sm="width.sm" :xl="width.xl" :xs="width.xs" :xxl="width.xxl">
            <a-form-item label="申请说明" name="remark">
              <a-input v-model:value="form.remark" placeholder="请输入申请说明" style="width:200px"/>
            </a-form-item>
          </a-col>
        </a-row>
      </a-card>
      <!-- 耗材列表 -->
      <AssetTable ref="assetTableRef" :locationId="form.locationId"/>
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
import EnterpriseSelect from '/@/components/enterprise-select/index.vue';
import EmployeeSelect from '/@/components/employee-select/index.vue';
import DepartmentTreeSelect from '/@/components/department-tree-select/index.vue';
import AssetTable from './components/requisition-asset-table.vue';
import LocationSelect from '/@/components/fixed-asset/locaton-select/index.vue';

import { ASSET_STATUS_ENUM } from '/@/constants/fixed-asset/asset-const';
import { reactive, ref, watch, computed, onMounted, provide } from 'vue';
import { message } from 'ant-design-vue';
import { SmartLoading } from '/@/components/smart-loading';
import { formWidth } from '/@/views/business/hooks/form-hooks';
import { useAppConfigStore } from '/@/store/modules/system/app-config';
import { useRoute, useRouter } from 'vue-router';
import { useUserStore } from '/@/store/modules/system/user';
import _ from 'lodash';
import { consumablesRequisitionApi } from '/@/api/fixed-asset/asset/consumables-requisition-api';

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
    useUserStore().setTagName(route, '编辑领用');
    getDetail(assetId);
  } else {
    useUserStore().setTagName(route, '新建领用');
  }
});

// ------------------------ 表单 ------------------------

// 组件ref
const formRef = ref();

const formDefault = {
  requisitionRevertNo: undefined, //资产编号
  sourceId: undefined, //资产名称
  remark: null
};

let form = reactive({ ...formDefault });

const rules = {
  useTime: [{ required: true, message: '请选择领用日期' }],
  locationId: [{ required: true, message: '请选择所属位置' }],
  departmentId: [{ required: true, message: '请选择领用部门' }],
  userId: [{ required: true, message: '请选择领用人' }],
};

const assetTableRef = ref();

// 点击确定，验证表单
async function onSubmit () {
  try {
    await formRef.value.validateFields();
    let consumablesList = assetTableRef.value.consumablesList;
    if (_.isEmpty(consumablesList)) {
      message.error('请选择领用资产');
      return;
    }
    save(consumablesList.map(e => {
      return {
        count: e.count,
        consumablesId: e.consumablesId
      };
    }));
  } catch (err) {
    console.log(err);
  }
}

// 新建、编辑API
async function save (itemList) {
  SmartLoading.show();
  try {
    let param = _.cloneDeep(form);
    param.itemList = itemList;
    await consumablesRequisitionApi.add(param);
    message.success('创建成功');
    onClose();
  } catch (err) {
    console.log(err);
  } finally {
    SmartLoading.hide();
  }
}

function changeEnterprise () {
  form.locationId = null;
  assetTableRef.value.clear();
}

function changeLocation(){
  assetTableRef.value.clear();
}

// 关闭
function onClose () {
  useUserStore().closePage(route, router);
}

</script>
