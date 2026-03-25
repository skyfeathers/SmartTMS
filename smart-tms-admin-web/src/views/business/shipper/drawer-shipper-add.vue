<!--
 * @Description:  新增货主页面
 * @version:
 * @Author: lidoudou
 * @Date: 2022-07-05 17:50:17
 * @LastEditors: zhuoda
 * @LastEditTime: 2022-07-22
-->
<template>
  <div>
    <a-drawer title="新建货主" v-model:open="visible" width="80%">
      <a-form ref="formRef" :model="shipperForm" :rules="rules"
      labelAlign="right">
      <a-card size="small" title="基础信息">
        <a-descriptions size="small" bordered :column="{ lg: 2, md: 2, sm: 2, xl: 3, xs: 2, xxl: 5 }">
          <a-descriptions-item label="货主名称" class="required">
            <a-form-item name="consignor">
              <a-input v-model:value="shipperForm.consignor" placeholder="请输入货主名称" style="width: 180px"
                @change="queryByName" />
              <div v-if="existFlag" class="tips">货主名称已在当前公司存在</div>
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="货主简称" class="required">
            <a-form-item name="shortName">
              <a-input v-model:value="shipperForm.shortName" placeholder="请输入货主简称" style="width: 180px" />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="货主类型" class="required">
            <a-form-item name="shipperNature">
              <smart-enum-select width="180px" v-model:value="shipperForm.shipperNature" placeholder="请选择类型"
                enum-name="SHIPPER_NATURE_ENUM" />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="业务负责人" class="required">
            <a-form-item name="managerId">
              <employee-select ref="managerSelectRef" v-model:value="shipperForm.managerId"
                :systemConfigKey="SYSTEM_CONFIG_KEY_ENUM.SALES_ROLE_CODE.value" placeholder="请选择业务负责人" width="180px" />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="营业执照编号">
            <a-form-item name="consignorId">
              <a-input v-model:value="shipperForm.consignorId" placeholder="请输入营业执照编号" style="width: 180px" />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="是否开票">
            <a-form-item name="makeInvoiceFlag">
              <a-radio-group v-model:value="shipperForm.makeInvoiceFlag">
                <a-radio-button :value="FLAG_NUMBER_ENUM.TRUE.value">需要</a-radio-button>
                <a-radio-button :value="FLAG_NUMBER_ENUM.FALSE.value"> 不需要</a-radio-button>
              </a-radio-group>
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="税点(%)">
            <a-form-item name="taxPoint">
              <a-input-number v-model:value="shipperForm.taxPoint" :max="100" :min="0" :precision="2"
                placeholder="请输入税点" style="width: 180px" />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="所在地区">
            <a-form-item>
              <DictSelect width="180px" keyCode="AREA" v-model:value="shipperForm.area" />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="标签">
            <a-form-item name="tagType">
              <smart-enum-select width="180px" v-model:value="shipperForm.tagType" placeholder="请选择标签" enum-name="SHIPPER_TAG_ENUM" />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="等级">
            <a-form-item name="grade">
              <smart-enum-select width="180px" v-model:value="shipperForm.grade" placeholder="请选择等级" enum-name="SHIPPER_GRADE_ENUM" />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="业务关系">
            <a-form-item name="shipperTypeList">
              <smart-enum-select width="180px" v-model:value="shipperForm.shipperTypeList" placeholder="请选择类型"
                enum-name="SHIPPER_TYPE_ENUM" mode="multiple" />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="客服负责人">
            <a-form-item name="customerServiceIdList">
              <employee-select ref="customerServiceRef" v-model:value="shipperForm.customerServiceIdList"
                :systemConfigKey="SYSTEM_CONFIG_KEY_ENUM.CUSTOMER_SERVICE_ROLE_CODE.value" mode="multiple"
                placeholder="请选择客服负责人" width="180px" />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="账期(天)">
            <a-form-item name="accountPeriod">
              <a-input-number v-model:value="shipperForm.accountPeriod" :min="0" :max="99999" :precision="0"
                style="width: 180px" placeholder="请输入账期" />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="备注">
            <a-form-item name="remark">
              <a-input v-model:value="shipperForm.remark" style="width: 180px" placeholder="请输入备注" />
            </a-form-item>
          </a-descriptions-item>
        </a-descriptions>
      </a-card>
      <a-card class="smart-margin-top10" size="small" title="附件信息">
        <a-descriptions size="small" bordered :column="{ lg: 2, md: 2, sm: 2, xl: 3, xs: 2, xxl: 5 }">
          <a-descriptions-item label="营业执照">
            <a-form-item name="consignorId">
              <Upload accept=".jpg,.jpeg,.png,.gif" :maxUploadSize="1" buttonText="点击上传营业执照"
                :default-file-list="shipperForm.consignorImage || []" listType="picture-card" @change="changeConsignor"
                :folder="FILE_FOLDER_TYPE_ENUM.SHIPPER.value" />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="附件">
            <a-form-item name="attachment">
              <Upload :maxUploadSize="5" buttonText="点击上传附件" :default-file-list="shipperForm.attachment || []"
                @change="changeAttachment" :folder="FILE_FOLDER_TYPE_ENUM.SHIPPER.value" listType="picture-card"/>
            </a-form-item>
          </a-descriptions-item>
        </a-descriptions>
      </a-card>
    </a-form>

    <!-- 联系人 列表 -->
    <contact-manage ref="contactManageRef" :list="shipperForm.contactList" />

    <!-- 开票信息 列表 -->
    <invoice-manage ref="invoiceManageRef" :list="shipperForm.invoiceList" />

    <!-- 银行账户 列表 -->
    <payment-type-manage ref="paymentTypeManageRef" :list="shipperForm.paymentWayList" />

    <!-- 邮寄地址 列表 -->
    <mail-address-manage ref="mailAddressManageRef" :list="shipperForm.mailAddressList" />

    <a-card class="smart-margin-top5 end" size="small">
      <a-space>
        <a-button @click="onClose">取消</a-button>
        <a-button type="primary" @click="onSubmit()">保存</a-button>
      </a-space>
    </a-card>
    </a-drawer>
  </div>
</template>
<script setup>
import { reactive, ref, onMounted, computed, watch } from 'vue';
import { message } from 'ant-design-vue';
import { useSpinStore } from '/@/store/modules/system/spin';
import { useRoute, useRouter } from 'vue-router';
import _ from 'lodash';
import SmartEnumSelect from '/@/components/smart-enum-select/index.vue';
import BooleanFlagSelect from '/@/components/boolean-flag-select/index.vue';
import EnterpriseSelect from '/@/components/enterprise-select/index.vue';
import Upload from '/@/components/upload/index.vue';
import DictSelect from '/@/components/dict-select/index.vue';
import EmployeeSelect from '/@/components/role-employee-select/index.vue';
import ContactManage from './components/contact-manage.vue';
import InvoiceManage from './components/invoice-manage.vue';
import PaymentTypeManage from './components/payment-type-manage.vue';
import MailAddressManage from './components/mail-address-manage.vue';
import { FILE_FOLDER_TYPE_ENUM } from '/@/constants/business/file-const';
import { SYSTEM_CONFIG_KEY_ENUM } from '/@/constants/system/system-config-const';
import { FLAG_NUMBER_ENUM } from '/@/constants/common-const';
import { configApi } from '/@/api/support/config/config-api';
import { useUserStore } from '/@/store/modules/system/user';
import { shipperApi } from '/@/api/business/shipper/shipper-api';
import { ocrSetup } from '/@/views/support/ocr/ocr-setup';
import { PRINCIPAL_TYPE_ENUM } from '/@/constants/business/shipper-const';
import { driverApi } from '/@/api/business/driver/driver-api';
const route = useRoute();
const router = useRouter();
const visible = ref(false)

// --------- 表单逻辑 ---
const rules = {
  consignor: [{ required: true, message: '请输入货主名称' }],
  shortName: [{ required: true, message: '请输入货主简称' }],
  shipperNature: [{ required: true, message: '请选择货主类型' }],
  managerId: [{ required: true, message: '请选择业务负责人' }],
};
const shipperForm = reactive({
  makeInvoiceFlag: FLAG_NUMBER_ENUM.TRUE.value,
  taxPoint: 9,
  deductTaxFlag: false,
  deductTaxRatio: 0,
  accountPeriod: 30,
  managerIdList: [],
  managerId: null
});
let area = ref([]);
const contactManageRef = ref();
const invoiceManageRef = ref();
const paymentTypeManageRef = ref();
const mailAddressManageRef = ref();
const formRef = ref();

// 提交货主信息
async function onSubmit() {
  formRef.value
    .validate()
    .then(async () => {
      let contactList = contactManageRef.value.tableData;
      if (_.isEmpty(contactList)) {
        message.error('最少添加一条联系人信息')
        return;
      }
      useSpinStore().show();
      try {
        let param = Object.assign({}, shipperForm, {
          contactList,
          invoiceList: invoiceManageRef.value.tableData,
          paymentWayList: paymentTypeManageRef.value.tableData,
          mailAddressList: mailAddressManageRef.value.tableData,
          managerIdList: shipperForm.managerId ? [shipperForm.managerId] : []
        });

        if (param.shipperId) {
          await shipperApi.updateShipper(param);
          message.success('更新成功');
        } else {
          await shipperApi.saveShipper(param);
          message.success('创建成功');
        }
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

function reset(){
  formRef.value.resetFields();
  shipperForm.area = []
  shipperForm.consignorImage = []
  shipperForm.contactList = []
  shipperForm.invoiceList = []
  shipperForm.paymentWayList = []
  shipperForm.mailAddressList = []
}

function changeArea(value, selectedOptions) {
  if (!_.isEmpty(selectedOptions)) {
    // 更新所选择地区
    shipperForm.provinceCode = area.value[0].value;
    shipperForm.provinceName = area.value[0].label;

    shipperForm.cityCode = area.value[1].value;
    shipperForm.cityName = area.value[1].label;
  }
}

// ocr 识别
let { recognizeBusinessLicense } = ocrSetup();

// ------- 文件上传 start
function changeConsignor(fileList) {
  shipperForm.consignorImage = fileList;
  if (_.isEmpty(fileList)) {
    return;
  }
  let file = fileList[0];
  let fileUrl = file.fileUrl;
  recognizeBusinessLicense(fileUrl, recognizeConsignorAfter);
}

function recognizeConsignorAfter(data) {
  if (!data) {
    return;
  }
  shipperForm.consignor = data.companyName;
  shipperForm.consignorId = data.creditCode;
}

function changeAttachment(fileList) {
  shipperForm.attachment = fileList;
}

const emits = defineEmits(['reloadShipper'])

// 关闭
function onClose() {
  emits('reloadShipper');
  reset();
  visible.value = false
}

// --------- 判断货主名称是否重复 ------------------------
let existFlag = ref(false);
async function queryByName() {
  try {
    if (!shipperForm.consignor) {
      existFlag.value = false;
      return;
    }
    let params = {
      consignor: shipperForm.consignor,
      shipperId: shipperForm.shipperId
    };
    let responseModel = await shipperApi.queryByName(params);
    existFlag.value = responseModel.data;
  } catch (e) {
    console.log(e);
  }
}

function showDrawer(){
  visible.value = true;
}

defineExpose({
  showDrawer
})

</script>
<style lang="less" scoped>
::v-deep .ant-form-item-required{
  color: #ff4d4f !important;
}

.tips {
  color: #ff4d4f
}


.end{
  position: sticky;
  bottom: 0;
}

::v-deep .ant-form-item {
  margin-bottom: 0 !important;
}

::v-deep .ant-descriptions-row>th {
  width: 100px;
}

::v-deep .ant-descriptions-row>td {
  width: 200px;
}

::v-deep .ant-descriptions-item-content {
  padding: 2px 16px !important;
}

::v-deep .ant-upload-select {
  margin-bottom: 0px !important;
}

::v-deep th.required {
  color: red;
}

.form-width {
  width: 180px;
}
</style>
