<!--
  * 固定资产-维修登记
  *
  * @Author:    卓大
  * @Date:      2023-03-23 15:01:51
  * @Copyright  1024创新实验室 （ https://1024lab.net ）
-->
<template>
  <a-modal
    :title="form.repairId ? '编辑' : '新建维修登记'"
    width="1000px"
    :open="visibleFlag"
    @cancel="onClose"
    :maskClosable="false"
    :destroyOnClose="true"
  >
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 7 }">
      <a-row>

        <a-col :span="8">
          <a-form-item label="业务时间" name="businessDate">
            <a-date-picker valueFormat="YYYY-MM-DD" v-model:value="form.businessDate" style="width: 200px" placeholder="业务日期" />
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="维修厂家" name="repairCompany">
            <DictSelect width="75%" v-model:value="form.repairCompany" keyCode="ASSET-REPAIR-COMPANY" placeholder="维修厂家" />
          </a-form-item>
        </a-col>

        <a-col :span="8">
          <a-form-item label="维修内容" name="content">
            <a-textarea style="width: 90%" v-model:value="form.content" placeholder="维修内容" />
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="申请人员" name="applyUserId">
            <employee-select v-model:value="form.applyUserId" :leaveFlag="false" placeholder="申请维修人员" style="width: 90%"/>
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="维修花费" name="repairCost">
            <a-input-number style="width: 90%" v-model:value="form.repairCost" placeholder="维修花费" :min="0" />
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="备注" name="remark">
            <a-textarea style="width: 90%" v-model:value="form.remark" placeholder="备注" />
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="维修配件附件" >
            <Upload
                  :maxUploadSize="20"
                  buttonText="点击上传附件"
                  :default-file-list="form.mountingsFiles || []"
                  @change="changeMountingAttachment"
                  :folder="FILE_FOLDER_TYPE_ENUM.FIXED_ASSET.value"
              />
          </a-form-item>
        </a-col>

        <a-col :span="8">
          <a-form-item label="发票附件">
            <Upload
                  :maxUploadSize="20"
                  buttonText="点击上传附件"
                  :default-file-list="form.invoiceFiles || []"
                  @change="changeInvoiceFiles"
                  :folder="FILE_FOLDER_TYPE_ENUM.FIXED_ASSET.value"
              />
          </a-form-item>
        </a-col>
      </a-row>

        <!-- 资产列表 -->
        <AssetTableSelect ref="assetTableSelectRef" :operateFlag="true"/>
    </a-form>

    <template #footer>
      <a-space>
        <a-button @click="onClose">取消</a-button>
        <a-button type="primary" @click="onSubmit">保存</a-button>
      </a-space>
    </template>
  </a-modal>
</template>
<script setup>
import { reactive, ref, nextTick, computed, provide } from 'vue';
import AssetTableSelect from '/@/components/fixed-asset/asset-table-select/index.vue';
import _ from 'lodash';
import { message } from 'ant-design-vue';
import { SmartLoading } from '/@/components/smart-loading';
import { repairApi } from '/@/api/fixed-asset/asset/repair-api';
import { smartSentry } from '/@/lib/smart-sentry';
import Upload from '/@/components/upload/index.vue';
import DictSelect from '/@/components/dict-select/index.vue';
import { FILE_FOLDER_TYPE_ENUM } from '/@/constants/business/file-const';
import EmployeeSelect from '/@/components/employee-select/index.vue';
import EnterpriseSelect from '/@/components/enterprise-select/index.vue';
import { ASSET_STATUS_ENUM } from '/@/constants/fixed-asset/asset-const';

// ------------------------ 事件 ------------------------

const emits = defineEmits(['reloadList']);

// ------------------------ 显示与隐藏 ------------------------
// 是否显示
const visibleFlag = ref(false);

function show(rowData) {
  Object.assign(form, formDefault);
  if (rowData && !_.isEmpty(rowData)) {
    Object.assign(form, rowData);
  }
  visibleFlag.value = true;
  nextTick(() => {
    formRef.value.clearValidate();
  });
}

function onClose() {
  Object.assign(form, formDefault);
  visibleFlag.value = false;
}

// ------------------------ 表单 ------------------------

// 组件ref
const formRef = ref();
// 选择固定资产ref
const assetTableSelectRef = ref();

const formDefault = {
  repairId: undefined,
  repairCompany: undefined, //维修厂家
  businessDate: undefined, //业务日期
  content: undefined, //维修内容
  applyUserId: undefined, //申请维修人员
  repairCost: undefined, //维修花费
  remark: undefined, //备注
  mountingsFiles: undefined, //维修配件附件
  invoiceFiles: undefined, //发票附件
  assetIdList:[],//资产id列表
};

let form = reactive({ ...formDefault });

const rules = {
  repairCompany: [{ required: true, message: '维修厂家 必填' }],
  businessDate: [{ required: true, message: '业务日期 必填' }],
  applyUserId: [{ required: true, message: '申请维修人员 必填' }],
};

function changeMountingAttachment (fileList) {
  form.mountingsFiles = fileList;
}

function changeInvoiceFiles (fileList) {
  form.invoiceFiles = fileList;
}

// 点击确定，验证表单
async function onSubmit() {
  try {
    await formRef.value.validateFields();

    let assetList = assetTableSelectRef.value.tableData;
    if (_.isEmpty(assetList)) {
      message.error('请选择需要维修登记的固定资产');
      return;
    }
    form.assetIdList = assetList.map( e => e.assetId);
    save();
  } catch (err) {
    message.error('参数验证错误，请仔细填写表单数据!');
  }
}

// 新建、编辑API
async function save() {
  SmartLoading.show();
  try {
    if (form.repairId) {
      await repairApi.update(form);
    } else {
      await repairApi.add(form);
    }
    message.success('操作成功');
    emits('reloadList');
    onClose();
  } catch (err) {
    smartSentry.captureError(err);
  } finally {
    SmartLoading.hide();
  }
}

let customQueryForm = computed(() => {
  return {};
});
provide('setCustomQueryForm', customQueryForm);

// 由于需要验证调出位置，所以增加自定义验证方法
function validateParam (param) {
  if (!param) {
    return false;
  }
  return true;
}

provide('setValidateParam', validateParam);

defineExpose({
  show,
});
</script>
