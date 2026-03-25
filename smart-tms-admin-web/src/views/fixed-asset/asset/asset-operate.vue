<!--
  * 固定资产
  *
  * @Author:    lidoudou
  * @Date:      2023-03-15 14:15:14
  * @Copyright  1024创新实验室
-->
<template>
  <div class="smart-form-wrapper">
    <a-form class="smart-form" labelAlign="right" ref="formRef" :model="form" :rules="rules">
      <a-card title="基本信息">
        <a-row>
          <a-col :lg="width.lg" :md="width.md" :sm="width.sm" :xl="width.xl" :xs="width.xs" :xxl="width.xxl">
            <a-form-item label="所属分类" name="categoryId">
              <CategoryTreeSelect v-model:value="form.categoryId" :categoryType="CATEGORY_TYPE_ENUM.FIXED_ASSET.value"
                                 placeholder="请选择所属分类" style="width:200px"/>
            </a-form-item>
          </a-col>
          <a-col :lg="width.lg" :md="width.md" :sm="width.sm" :xl="width.xl" :xs="width.xs" :xxl="width.xxl">
            <a-form-item label="资产名称" name="assetName">
              <a-input v-model:value="form.assetName" placeholder="请输入资产名称" style="width:200px"/>
            </a-form-item>
          </a-col>
          <a-col :lg="width.lg" :md="width.md" :sm="width.sm" :xl="width.xl" :xs="width.xs" :xxl="width.xxl">
            <a-form-item label="资产编号" name="assetNo">
              <a-input v-model:value="form.assetNo" placeholder="请输入资产编号" style="width:200px"/>
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
              <SmartDictSelect keyCode="ASSET-SOURCE" v-model:value="form.sourceId" placeholder="请选择资产来源" style="width:200px"/>
            </a-form-item>
          </a-col>
          <a-col :lg="width.lg" :md="width.md" :sm="width.sm" :xl="width.xl" :xs="width.xs" :xxl="width.xxl">
            <a-form-item label="品牌" name="brand">
              <a-input v-model:value="form.brand" placeholder="请输入品牌" style="width:200px"/>
            </a-form-item>
          </a-col>

          <a-col :lg="width.lg" :md="width.md" :sm="width.sm" :xl="width.xl" :xs="width.xs" :xxl="width.xxl">
            <a-form-item label="规格型号" name="model">
              <a-input v-model:value="form.model" placeholder="请输入规格型号" style="width:200px"/>
            </a-form-item>
          </a-col>
          <a-col :lg="width.lg" :md="width.md" :sm="width.sm" :xl="width.xl" :xs="width.xs" :xxl="width.xxl">
            <a-form-item label="设备序列号" name="serialId">
              <a-input v-model:value="form.serialId" placeholder="请输入设备序列号" style="width:200px"/>
            </a-form-item>
          </a-col>
          <a-col :lg="width.lg" :md="width.md" :sm="width.sm" :xl="width.xl" :xs="width.xs" :xxl="width.xxl">
            <a-form-item label="计量单位" name="unit">
              <a-input v-model:value="form.unit" placeholder="请输入计量单位" style="width:200px"/>
            </a-form-item>
          </a-col>

          <a-col :lg="width.lg" :md="width.md" :sm="width.sm" :xl="width.xl" :xs="width.xs" :xxl="width.xxl">
            <a-form-item label="预计使用期限（月）" name="monthCount">
              <a-input-number v-model:value="form.monthCount" placeholder="请输入使用期限" style="width: 200px" :min="0"/>
            </a-form-item>
          </a-col>
          <a-col :lg="width.lg" :md="width.md" :sm="width.sm" :xl="width.xl" :xs="width.xs" :xxl="width.xxl">
            <a-form-item label="购入日期" name="purchaseTime">
              <a-date-picker v-model:value="form.purchaseTime" valueFormat="YYYY-MM-DD" placeholder="请选择购入日期"
                             style="width:200px"/>
            </a-form-item>
          </a-col>
          <a-col :lg="width.lg" :md="width.md" :sm="width.sm" :xl="width.xl" :xs="width.xs" :xxl="width.xxl">
            <a-form-item label="到期时间" name="expireTime">
              <a-date-picker v-model:value="form.expireTime" valueFormat="YYYY-MM-DD" placeholder="请选择到期日期"
                             style="width:200px"/>
            </a-form-item>
          </a-col>
          <a-col :lg="width.lg" :md="width.md" :sm="width.sm" :xl="width.xl" :xs="width.xs" :xxl="width.xxl">
            <a-form-item label="购入价格" name="price">
              <a-input-number v-model:value="form.price" :min="0" :max="99999999" :precision="2" style="width: 200px" placeholder="请输入购入价格"/>
            </a-form-item>
          </a-col>
          <a-col :lg="width.lg" :md="width.md" :sm="width.sm" :xl="width.xl" :xs="width.xs" :xxl="width.xxl">
            <a-form-item label="残值率" name="residualValueRate">
              <a-input-number v-model:value="form.residualValueRate" :min="0" :max="100" :precision="0" style="width: 200px" placeholder="请输入残值率"/>
            </a-form-item>
          </a-col>

          <a-col :lg="width.lg" :md="width.md" :sm="width.sm" :xl="width.xl" :xs="width.xs" :xxl="width.xxl">
            <a-form-item label="存放位置" name="locationId">
              <LocationSelect v-model:value="form.locationId" placeholder="请选择存放位置" style="width:200px"/>
            </a-form-item>
          </a-col>
          <a-col :lg="width.lg" :md="width.md" :sm="width.sm" :xl="width.xl" :xs="width.xs" :xxl="width.xxl">
            <a-form-item label="备注" name="remark">
              <a-input v-model:value="form.remark" style="width:200px" placeholder="请输入备注"/>
            </a-form-item>
          </a-col>


          <a-col :lg="width.lg" :md="width.md" :sm="width.sm" :xl="width.xl" :xs="width.xs" :xxl="width.xxl">
            <a-form-item label="使用部门">
              <DepartmentTreeSelect ref="departmentTreeSelect" width="100%" :init="false" placeholder="请选择使用部门"
                                    v-model:value="form.departmentId" style="width:200px"/>
            </a-form-item>
          </a-col>
          <a-col :lg="width.lg" :md="width.md" :sm="width.sm" :xl="width.xl" :xs="width.xs" :xxl="width.xxl">
            <a-form-item label="使用人">
              <employee-select v-model:value="form.userId" :leaveFlag="false" :disabledFlag="0"
                               placeholder="请选择使用人" style="width:200px"/>
            </a-form-item>
          </a-col>

          <a-col :lg="width.lg" :md="width.md" :sm="width.sm" :xl="width.xl" :xs="width.xs" :xxl="width.xxl">
            <a-form-item label="物品照片" name="attachment">
              <Upload
                  :maxUploadSize="5"
                  buttonText="点击上传物品照片"
                  :default-file-list="form.attachment || []"
                  @change="changeAttachment"
                  :folder="FILE_FOLDER_TYPE_ENUM.FIXED_ASSET.value"
              />
            </a-form-item>
          </a-col>
        </a-row>
      </a-card>

      <a-card title="维保信息" class="smart-margin-top5">
        <a-row>
          <a-col :lg="width.lg" :md="width.md" :sm="width.sm" :xl="width.xl" :xs="width.xs" :xxl="width.xxl">
            <a-form-item label="供应商" name="supplierName">
              <a-input v-model:value="form.supplierName" placeholder="请输入供应商" style="width:200px"/>
            </a-form-item>
          </a-col>
          <a-col :lg="width.lg" :md="width.md" :sm="width.sm" :xl="width.xl" :xs="width.xs" :xxl="width.xxl">
            <a-form-item label="供应商联系人" name="supplierContactName">
              <a-input v-model:value="form.supplierContactName" placeholder="请输入供应商联系人" style="width:200px"/>
            </a-form-item>
          </a-col>
          <a-col :lg="width.lg" :md="width.md" :sm="width.sm" :xl="width.xl" :xs="width.xs" :xxl="width.xxl">
            <a-form-item label="供应商联系方式" name="supplierContactPhone">
              <a-input v-model:value="form.supplierContactPhone" placeholder="请输入供应商联系方式" style="width:200px"/>
            </a-form-item>
          </a-col>
          <a-col :lg="width.lg" :md="width.md" :sm="width.sm" :xl="width.xl" :xs="width.xs" :xxl="width.xxl">
            <a-form-item label="负责人" name="managerId">
              <employee-select v-model:value="form.managerId" :leaveFlag="false" :disabledFlag="0"
                               placeholder="请选择负责人" style="width:200px"/>
            </a-form-item>
          </a-col>
          <a-col :lg="width.lg" :md="width.md" :sm="width.sm" :xl="width.xl" :xs="width.xs" :xxl="width.xxl">
            <a-form-item label="维保到期" name="repairExpireTime">
              <a-date-picker v-model:value="form.repairExpireTime" valueFormat="YYYY-MM-DD" placeholder="请选择维保到期"
                             style="width:200px"/>
            </a-form-item>
          </a-col>
          <a-col :lg="width.lg" :md="width.md" :sm="width.sm" :xl="width.xl" :xs="width.xs" :xxl="width.xxl">
            <a-form-item label="维保说明" name="supplierRemark">
              <a-input v-model:value="form.supplierRemark" placeholder="请输入维保说明" style="width:200px"/>
            </a-form-item>
          </a-col>
        </a-row>
      </a-card>
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
import LocationSelect from '/@/components/fixed-asset/locaton-select/index.vue';
import CategoryTreeSelect from '/@/components/fixed-asset/category-tree-select/index.vue';
import SmartDictSelect from '/@/components/smart-dict-select/index.vue';
import Upload from '/@/components/upload/index.vue';
import EmployeeSelect from "/@/components/employee-select/index.vue";
import DepartmentTreeSelect from '/@/components/department-tree-select/index.vue';

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
    useUserStore().setTagName(route, '编辑资产');
    getDetail(assetId);
  } else {
    useUserStore().setTagName(route, '新建资产');
  }
});

// ------------------------ 表单 ------------------------

// 组件ref
const formRef = ref();

const formDefault = {
  assetId: undefined, //资产ID
  categoryId: undefined, //所属分类
  assetNo: undefined, //资产编号
  assetName: undefined, //资产名称
  price: 0,// 购入价格
  residualValueRate: 5 ,// 残值率
  monthCount: 0
};

let form = reactive({ ...formDefault });

const rules = {
  categoryId: [{ required: true, message: '请选择所属分类' }],
  // assetNo: [{ required: true, message: '请输入资产编号' }],
  assetName: [{ required: true, message: '请输入资产名称' }],
  sourceId: [{ required: true, message: '请选择资产来源' }],
  locationId: [{ required: true, message: '请选择存放位置' }],
  date: [{ required: true, message: '请选择购入日期' }],
  price: [{ required: true, message: '请输入购入价格' }],
  residualValueRate: [{ required: true, message: '请输入残值率' }],
  monthCount: [
    { required: true, message: '请输入预计使用期限' }
  ],
};

function changeAttachment (fileList) {
  form.attachment = fileList;
}

// 点击确定，验证表单
async function onSubmit () {
  try {
    await formRef.value.validateFields();
    save();
  } catch (err) {
    message.error('参数验证错误，请仔细填写表单数据!');
  }
}

// 新建、编辑API
async function save () {
  SmartLoading.show();
  try {
    if (form.assetId) {
      await assetApi.update(form);
      message.success('编辑成功');
    } else {
      await assetApi.add(form);
      message.success('新建成功');
    }
    onClose();
  } catch (err) {
    console.log(e);
  } finally {
    SmartLoading.hide();
  }
}

// ------------------------ 获取详情 ------------------------
async function getDetail (orderId) {
  SmartLoading.show();
  try {
    let response = await assetApi.getDetail(orderId);
    let detail = response.data;
    detail.sourceId = detail.sourceId[0]?.valueCode;
    Object.assign(form, detail);
  } catch (error) {
    console.log('error', error);
  } finally {
    SmartLoading.hide();
  }
}


// 关闭
function onClose () {
  useUserStore().closePage(route, router);
}

</script>
<style scoped lang="less">
.explain{
  color: #333;
}
</style>
