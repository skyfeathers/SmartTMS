<template>
  <div>
    <a-form ref="formRef" :model="form" :rules="rules" labelAlign="left">
      <a-card size="small" title="基础信息">
        <a-descriptions size="small" bordered :column="{ lg: 2, md: 2, sm: 2, xl: 3, xs: 2, xxl: 5 }">
          <a-descriptions-item label="司机电话" class="required">
            <a-form-item name="telephone">
              <a-input v-model:value="form.telephone" class="form-width" placeholder="请输入司机电话" />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="经营方式" v-if="!form.driverId">
            <a-form-item name="businessMode">
              <SmartEnumSelect v-model:value="form.businessMode" width="180px" enumName="DRIVER_BUSINESS_MODE_ENUM" />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="备注">
            <a-form-item name="remark">
              <a-input v-model:value="form.remark" class="form-width" placeholder="请输入备注" />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="关联车辆">
            <a-form-item name="vehicleIdList">
              <div class="form-width flex-center">
                <VehicleSelect width="140px" ref="vehicleSelectRef" placeholder="请选择车辆" :multiple="true" v-model:value="form.vehicleIdList" />
                <a @click="showQuickCreateModal(QUICK_CREATE_TYPE_ENUM.VEHICLE.value)" style="margin-left: 5px;">新建</a>
              </div>
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="负责人">
            <a-form-item name="managerId">
              <employee-select width="180px" v-model:value="form.managerId" :leaveFlag="false"
                placeholder="请选择负责人" />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item :span="5">
          </a-descriptions-item>
          <a-descriptions-item label="司机大头照">
            <a-form-item name="photo">
              <Upload :default-file-list="form.photo" :folder="FILE_FOLDER_TYPE_ENUM.DRIVER.value" :maxUploadSize="1"
                accept="jpg, jpeg, png" buttonText="点击上传图片" listType="picture-card" @change="photoPicChange" />
            </a-form-item>
          </a-descriptions-item>
        </a-descriptions>
      </a-card>
      <a-card class="smart-margin-top10" size="small" title="司机身份证信息">
        <a-descriptions size="small" bordered :column="{ lg: 2, md: 2, sm: 2, xl: 3, xs: 2, xxl: 5 }">
          <a-descriptions-item label="身份证（人像面）">
            <a-form-item name="idCardBackPic">
              <Upload :default-file-list="form.idCardBackPic" :folder="FILE_FOLDER_TYPE_ENUM.DRIVER.value"
                :maxUploadSize="1" accept="jpg, jpeg, png" buttonText="点击上传图片" listType="picture-card"
                @change="idCardBackPicChange" />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="身份证（国徽面）">
            <a-form-item name="idCardFrontPic">
              <Upload :default-file-list="form.idCardFrontPic" :folder="FILE_FOLDER_TYPE_ENUM.DRIVER.value"
                :maxUploadSize="1" accept="jpg, jpeg, png" buttonText="点击上传图片" listType="picture-card"
                @change="idCardFrontPicChange" />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item :span="3">
          </a-descriptions-item>
          <a-descriptions-item label="司机姓名" class="required">
            <a-form-item name="driverName">
              <a-input v-model:value="form.driverName" class="form-width" placeholder="请输入司机姓名"
                @change="changeDriverName" />
              <DriverNameList ref="driverNameRef" />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="身份证号">
            <a-form-item name="drivingLicense">
              <a-input v-model:value="form.drivingLicense" class="form-width" placeholder="请输入身份证号" />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="身份证地址">
            <a-form-item name="homeAddress">
              <a-input v-model:value="form.homeAddress" class="form-width" placeholder="请输入身份证地址" />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="身份证有效期开始时间">
            <a-form-item name="idCardEffectiveStartDate">
              <a-date-picker v-model:value="form.idCardEffectiveStartDate" valueFormat="YYYY-MM-DD"
                class="form-width" />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="身份证有效期结束时间">
            <a-form-item name="idCardEffectiveEndDate">
              <a-date-picker :disabled="form.idCardEndlessFlag" v-model:value="form.idCardEffectiveEndDate"
                valueFormat="YYYY-MM-DD" class="form-width" />
              <a-checkbox class="flex-end" v-model:checked="form.idCardEndlessFlag">长期有效</a-checkbox>
            </a-form-item>
          </a-descriptions-item>
        </a-descriptions>
      </a-card>
      <a-card class="smart-margin-top10" size="small" title="驾驶证信息">
        <a-descriptions size="small" bordered :column="{ lg: 2, md: 2, sm: 2, xl: 3, xs: 2, xxl: 5 }">
          <a-descriptions-item label="驾驶证正本图">
            <a-form-item name="drivingLicenseFrontPic">
              <Upload :default-file-list="form.drivingLicenseFrontPic" :folder="FILE_FOLDER_TYPE_ENUM.DRIVER.value"
                :maxUploadSize="1" accept="jpg, jpeg, png" buttonText="点击上传图片" listType="picture-card"
                @change="drivingLicenseFrontPicChange" />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="驾驶证副本图">
            <a-form-item name="drivingLicenseBackPic">
              <Upload :default-file-list="form.drivingLicenseBackPic" :folder="FILE_FOLDER_TYPE_ENUM.DRIVER.value"
                :maxUploadSize="1" accept="jpg, jpeg, png" buttonText="点击上传图片" listType="picture-card"
                @change="drivingLicenseBackPicChange" />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item :span="3">

          </a-descriptions-item>
          <a-descriptions-item label="驾驶证号">
            <a-form-item name="drivingLicenseNo">
              <a-input v-model:value="form.drivingLicenseNo" class="form-width" placeholder="请输入驾驶证号" />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="准驾车型">
            <a-form-item name="vehicleClass">
              <smart-enum-select v-model:value="form.vehicleClass" enum-name="VEHICLE_CLASS_ENUM" placeholder="请选择准驾车型"
                width="180px" />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="发证机关">
            <a-form-item name="issuingOrganizations">
              <a-input v-model:value="form.issuingOrganizations" class="form-width" placeholder="请输入发证机关" />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="首次驾照签发日期">
            <a-form-item name="licenseFirstGetDate">
              <a-date-picker v-model:value="form.licenseFirstGetDate" valueFormat="YYYY-MM-DD" class="form-width" />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="驾驶证有效开始日期">
            <a-form-item name="validPeriodFrom">
              <a-date-picker v-model:value="form.validPeriodFrom" valueFormat="YYYY-MM-DD" class="form-width" />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="驾驶证有效结束日期">
            <a-form-item name="validPeriodTo">
              <a-form-item-rest>
                <a-date-picker :disabled="form.drivingLicenseEndlessFlag" v-model:value="form.validPeriodTo"
                  valueFormat="YYYY-MM-DD" class="form-width" />
              </a-form-item-rest>
              <a-checkbox class="flex-end" v-model:checked="form.drivingLicenseEndlessFlag">长期有效</a-checkbox>
            </a-form-item>
          </a-descriptions-item>
        </a-descriptions>
      </a-card>
      <a-card class="smart-margin-top10" size="small" title="从业资格证（道路运输从业资格证）">
        <a-descriptions size="small" bordered :column="{ lg: 2, md: 2, sm: 2, xl: 3, xs: 2, xxl: 5 }">
          <a-descriptions-item label="从业资格证图" :span="5">
            <a-form-item name="qualificationCertificatePic">
              <Upload :default-file-list="form.qualificationCertificatePic" :folder="FILE_FOLDER_TYPE_ENUM.DRIVER.value"
                :maxUploadSize="1" accept="jpg, jpeg, png" buttonText="点击上传图片" listType="picture-card"
                @change="qualificationCertificatePicChange" />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="从业资格证号">
            <a-form-item name="qualificationCertificate">
              <a-input v-model:value="form.qualificationCertificate" class="form-width" placeholder="请输入从业资格证号" />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="从业资格证有效期开始日期">
            <a-form-item name="qualificationCertificateStartDate">
              <a-date-picker v-model:value="form.qualificationCertificateStartDate" valueFormat="YYYY-MM-DD"
                class="form-width" />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="从业资格证有效期结束日期">
            <a-form-item name="qualificationCertificateEndDate">
              <a-date-picker v-model:value="form.qualificationCertificateEndDate" valueFormat="YYYY-MM-DD"
                class="form-width" />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item>
          </a-descriptions-item>
        </a-descriptions>
      </a-card>
      <a-card class="smart-margin-top10" size="small" title="税务登记人">
          <a-descriptions size="small" bordered :column="{ lg: 2, md: 2, sm: 2, xl: 3, xs: 2, xxl: 5 }">
            <a-descriptions-item label="纳税人身份证正面" :span="5">
              <a-form-item name="idCardFrontAttachment">
                <Upload :default-file-list="form.taxRegister.idCardFrontAttachment"
                  :folder="FILE_FOLDER_TYPE_ENUM.DRIVER.value" :maxUploadSize="5" buttonText="点击上传纳税人身份证正面"
                  accept="jpg, jpeg, png" listType="picture-card"
                  @change="idCardFrontAttachmentChange($event, index)" />
              </a-form-item>
            </a-descriptions-item>
            <a-descriptions-item label="姓名">
              <a-form-item name="name">
                <a-input v-model:value="form.taxRegister.name" class="form-width" placeholder="请输入姓名" />
              </a-form-item>
            </a-descriptions-item>
            <a-descriptions-item label="纳税人身份证号">
              <a-form-item name="idCard">
                <a-input v-model:value="form.taxRegister.idCard" class="form-width" placeholder="请输入纳税人身份证号" />
              </a-form-item>
            </a-descriptions-item>
            <a-descriptions-item label="纳税人手机号">
              <a-form-item name="phone">
                <a-input v-model:value="form.taxRegister.phone" class="form-width" placeholder="请输入纳税人手机号" />
              </a-form-item>
            </a-descriptions-item>
            <a-descriptions-item label="纳税人地址">
              <a-form-item name="address">
                <a-input v-model:value="form.taxRegister.address" class="form-width" placeholder="请输入纳税人地址" />
              </a-form-item>
            </a-descriptions-item>
          </a-descriptions>
      </a-card>
      <a-card class="smart-margin-top10" size="small" title="银行卡信息">
        <template v-for="(item, index) in form.bankList" :key="index">
          <a-form :ref="setBankRef" :model="item" :rules="bankRules">
            <a-descriptions size="small" bordered :column="{ lg: 2, md: 2, sm: 2, xl: 3, xs: 2, xxl: 5 }">
              <a-descriptions-item label="银行卡正面" :span="4">
                <a-form-item name="bankCardFrontAttachment">
                  <Upload :default-file-list="item.bankCardFrontAttachment" :folder="FILE_FOLDER_TYPE_ENUM.DRIVER.value"
                    :maxUploadSize="5" buttonText="点击上传银行卡正面" accept="jpg, jpeg, png" listType="picture-card"
                    @change="bankAttachmentChange($event, index)" />
                </a-form-item>
              </a-descriptions-item>
              <a-descriptions-item :span="1">
              </a-descriptions-item>
              <a-descriptions-item label="银行类型">
                <a-form-item name="bankType">
                  <SmartDictSelect keyCode="BANK-TYPE" v-model:value="item.bankType" :select-label="item.bankName"
                    width="180px" />
                </a-form-item>
              </a-descriptions-item>
              <a-descriptions-item label="开户名">
                <a-form-item name="accountName">
                  <a-input v-model:value="item.accountName" class="form-width" placeholder="请输入开户名" />
                </a-form-item>
              </a-descriptions-item>
              <a-descriptions-item label="银行账号">
                <a-form-item name="bankAccount">
                  <a-input v-model:value="item.bankAccount" class="form-width" placeholder="请输入银行账号" />
                </a-form-item>
              </a-descriptions-item>
              <a-descriptions-item label="支行名称">
                <a-form-item name="bankBranchName">
                  <a-input v-model:value="item.bankBranchName" class="form-width" placeholder="请输入支行名称" />
                </a-form-item>
              </a-descriptions-item>
              <a-descriptions-item label="是否默认">
                <a-form-item name="defaultFlag">
                  <a-radio-group v-model:value="item.defaultFlag" name="radioGroup"
                    @change="e => changeDefaultFlag(e, index)">
                    <a-radio :value="FLAG_NUMBER_ENUM.TRUE.value">是</a-radio>
                    <a-radio :value="FLAG_NUMBER_ENUM.FALSE.value">否</a-radio>
                  </a-radio-group>
                </a-form-item>
              </a-descriptions-item>
              <a-descriptions-item label="是否为纳税人信息">
                <a-form-item name="taxpayerFlag">
                  <a-radio-group v-model:value="item.taxpayerFlag" name="radioGroup"
                    @change="e => changeTaxpayerFlag(e, index)">
                    <a-radio :value="FLAG_NUMBER_ENUM.TRUE.value">是</a-radio>
                    <a-radio :value="FLAG_NUMBER_ENUM.FALSE.value">否</a-radio>
                  </a-radio-group>
                </a-form-item>
              </a-descriptions-item>
              <a-descriptions-item :span="1">
              </a-descriptions-item>
            </a-descriptions>
            <a-row :gutter="16">
              <a-col :span="24" style="margin-bottom: 10px">
                <a-button block type="dashed" @click="addBankItem">
                  <template #icon>
                    <plus-outlined />
                  </template>
                  新增银行卡信息
                </a-button>
                <a-button v-if="form.bankList.length > 1" block class="smart-margin-top5" type="dashed"
                  @click="deleteBankItem(index)">
                  <template #icon>
                    <minus-outlined />
                  </template>
                  删除银行卡信息
                </a-button>
              </a-col>
            </a-row>
          </a-form>
        </template>
      </a-card>
      <a-card class="smart-margin-top5 flex-end end" size="small">
        <a-space>
          <a-button @click="onClose">取消</a-button>
          <a-button type="primary" @click="onSubmit(false)">保存并提交审核</a-button>
          <a-button type="primary" @click="onSubmit(false,true)" v-if="!form.driverId">保存并继续创建</a-button>

          <a-tooltip placement="top">
            <template #title>
              <span>校验司机当前信息是否符合网络货运标准</span>
            </template>
            <a-button type="primary" @click="onSubmit(true)">网络货运校验<question-circle-outlined />
            </a-button>
          </a-tooltip>
        </a-space>
      </a-card>
    </a-form>
    <VehicleQuickCreate ref="quickCreateRef" @reloadList="reload"/>
  </div>
</template>
<script setup>
import Upload from '/@/components/upload/index.vue';
import EmployeeSelect from "/@/components/employee-select/index.vue";
import VehicleSelect from '/@/components/vehicle-select/index.vue';
import SmartEnumSelect from '/@/components/smart-enum-select/index.vue';
import DriverNameList from './components/driver-name-list.vue';
import SmartDictSelect from '/@/components/smart-dict-select/index.vue';
import VehicleQuickCreate from '/@/components/vehicle-quick-create/index.vue';
import { computed, nextTick, onMounted, reactive, ref, watch } from 'vue';
import { FILE_FOLDER_TYPE_ENUM } from '/@/constants/business/file-const';
import { FLAG_NUMBER_ENUM } from '/@/constants/common-const';
import { QUICK_CREATE_TYPE_ENUM } from '/@/constants/business/vehicle-const';
import { DRIVER_BUSINESS_MODE_ENUM } from '/@/constants/business/driver-const';
import { message } from 'ant-design-vue';
import { useSpinStore } from '/@/store/modules/system/spin';
import { driverApi } from '/@/api/business/driver/driver-api';
import { useRoute, useRouter } from 'vue-router';
import { useUserStore } from '/@/store/modules/system/user';
import _ from 'lodash';
import { ocrSetup } from '/@/views/support/ocr/ocr-setup';
import dayjs from 'dayjs';
import { regular } from '/@/constants/regular-const';
import { formWidth } from '/@/views/business/hooks/form-hooks';
import { useAppConfigStore } from '/@/store/modules/system/app-config';

const helpDocFlag = computed(() => useAppConfigStore().$state.helpDocFlag);
// ----- 加载form宽度
let { initWidth } = formWidth();
let width = ref({});
width.value = initWidth();
watch(
  () => helpDocFlag.value,
  () => {
    width.value = initWidth();
  }
);

const quickCreateRef = ref()
function showQuickCreateModal (typeValue) {
  quickCreateRef.value.showModal(typeValue);
}

const vehicleSelectRef = ref()
function reload(){
  vehicleSelectRef.value.queryData()
}
// ---------------- 操作表单 ----------------

const defaultRules = {
  telephone: [
    { required: true, message: '请输入司机电话' },
    { pattern: regular.phone, message: '司机电话格式不正确', trigger: 'blur' }
  ],
  driverName: [{ required: true, message: '请输入司机姓名' }],
  drivingLicense: [{ pattern: regular.isIdentityCard, message: '身份证号格式不正确', trigger: 'blur' }],
};

let validatePeriodTo = async (rule, value) => {
  if (!form.drivingLicenseEndlessFlag && !value) {
    return Promise.reject('请输入驾驶证有效结束日期');
  }
  return Promise.resolve();
};

const nftRules = {
  telephone: [
    { required: true, message: '请输入司机电话' },
    { pattern: regular.phone, message: '司机电话格式不正确', trigger: 'blur' }
  ],
  businessMode: [{ required: true, message: '请选择经营方式' }],
  driverName: [{ required: true, message: '请输入司机姓名' }],
  drivingLicense: [{ required: true, message: '请输入司机身份证号' },
  { pattern: regular.isIdentityCard, message: '身份证号格式不正确', trigger: 'blur' }],
  vehicleClass: [{ required: true, message: '请输入准驾车型' }],
  issuingOrganizations: [{ required: true, message: '请输入发证机关' }],
  validPeriodFrom: [{ required: true, message: '请输入驾驶证有效开始日期' }],
  validPeriodTo: [{ required: true, validator: validatePeriodTo }],
  qualificationCertificate: [{ required: true, message: '请输入从业资格证号' }],
  idCardFrontPic: [{ required: true, message: '请上传身份证国徽面' }],
  idCardBackPic: [{ required: true, message: '请上传身份证人像面' }],
  drivingLicenseFrontPic: [{ required: true, message: '请上传驾驶证正本图' }],
  drivingLicenseBackPic: [{ required: true, message: '请上传驾驶证副本图' }],
  drivingLicenseNo: [{ required: true, message: '请输入驾驶证号' }],
  qualificationCertificatePic: [{ required: true, message: '请上传从业资格证' }],
}

const rules = ref(defaultRules)
let formRef = ref();

const bankForm = {
  accountName: '',
  bankAccount: '',
  bankName: '',
  bankType: undefined,
  bankBranchName: '',
  attachment: undefined,
  defaultFlag: FLAG_NUMBER_ENUM.FALSE.value,
  taxpayerFlag: FLAG_NUMBER_ENUM.FALSE.value,
};
const form = reactive({
  enterpriseId: null,
  businessMode: DRIVER_BUSINESS_MODE_ENUM.INNER_MANAGEMENT.value, //经营方式
  idCardEndlessFlag: false,
  photo: [],
  bankList: [],
  taxRegister: {}
});

let route = useRoute();
let router = useRouter();
onMounted(() => {
  let driverId = route.query.driverId;
  if (!driverId) {
    addBankItem();
    return;
  }
  useUserStore().setTagName(route, '编辑司机');
  getDetail(driverId);
});

// ----------------------- 详情查询 ----------------------------
async function getDetail(driverId) {
  useSpinStore().show();
  try {
    let responseModel = await driverApi.getDriver(driverId);
    let detail = responseModel.data;
    (detail.bankList || []).forEach(item => {
      item.defaultFlag = item.defaultFlag ? FLAG_NUMBER_ENUM.TRUE.value : FLAG_NUMBER_ENUM.FALSE.value;
      item.taxpayerFlag = item.taxpayerFlag ? FLAG_NUMBER_ENUM.TRUE.value : FLAG_NUMBER_ENUM.FALSE.value;
    });
    detail.taxRegister = detail.taxRegister || {};
    Object.assign(form, detail);
    form.vehicleIdList = (form.driverVehicleList || []).map((e) => e.vehicleId);
    if (_.isEmpty(form.bankList)) {
      addBankItem();
    }
  } catch (error) {
    console.log('error', error);
  } finally {
    useSpinStore().hide();
  }
}

// ----------------------- 文件上传 ----------------------------
// ocr 识别
let { recognizeIdCardFace, recognizeIdCardBack, recognizeDrivingLicense, recognizeBankCard } = ocrSetup();
function idCardBackPicChange(fileList) {
  form.idCardBackPic = fileList;
  if (_.isEmpty(fileList)) {
    return;
  }
  let file = fileList[0];
  let fileUrl = file.fileUrl;
  recognizeIdCardFace(fileUrl, recognizeIdCardFaceAfter);
}

function recognizeIdCardFaceAfter(data) {
  if (!data) {
    return;
  }
  // 司机姓名
  form.driverName = data.name;
  // 身份证号
  form.drivingLicense = data.idNumber;
  // 身份证地址
  form.homeAddress = data.address;
}

function photoPicChange(fileList) {
  form.photo = fileList;
}

function idCardFrontPicChange(fileList) {
  form.idCardFrontPic = fileList;
  if (_.isEmpty(fileList)) {
    return;
  }
  let file = fileList[0];
  let fileUrl = file.fileUrl;
  recognizeIdCardBack(fileUrl, recognizeIdCardBackAfter);
}

function recognizeIdCardBackAfter(data) {
  if (!data) {
    return;
  }
  // 身份证有效期开始时间
  form.idCardEffectiveStartDate = data.validPeriodStartDate;
  // 身份证有效期结束时间
  form.idCardEffectiveEndDate = data.validPeriodEndDate;
  form.idCardEndlessFlag = data.idCardEndlessFlag;
}

function drivingLicenseFrontPicChange(fileList) {
  form.drivingLicenseFrontPic = fileList;
  if (_.isEmpty(fileList)) {
    return;
  }
  let file = fileList[0];
  let fileUrl = file.fileUrl;
  recognizeDrivingLicense(fileUrl, recognizeDrivingLicenseAfter);
}

function recognizeDrivingLicenseAfter(data) {
  if (!data) {
    return;
  }
  // 驾驶证号
  form.drivingLicenseNo = data.licenseNumber;
  // 准驾车型
  form.vehicleClass = data.approvedType;
  // 发证机关
  form.issuingOrganizations = data.issueAuthority;
  // 驾驶证有效开始日期
  if (data.validFromDate) {
    form.validPeriodFrom = dayjs(data.validFromDate).format('YYYY-MM-DD');
  }
  // 驾驶证有效截止日期
  if (data.validToDate) {
    form.validPeriodTo = dayjs(data.validToDate).format('YYYY-MM-DD');
  }
}

function drivingLicenseBackPicChange(fileList) {
  form.drivingLicenseBackPic = fileList;
}

function qualificationCertificatePicChange(fileList) {
  form.qualificationCertificatePic = fileList;
}

function bankAttachmentChange(fileList, index) {
  form.bankList[index].bankCardFrontAttachment = fileList;
  if (_.isEmpty(fileList)) {
    return;
  }
  let file = fileList[0];
  let fileUrl = file.fileUrl;
  recognizeBankCard(fileUrl, bankAttachmentChangeAfter, index);
}

function bankAttachmentChangeAfter(data, index) {
  if (!data) {
    return;
  }
  form.bankList[index].bankAccount = data.cardNumber;
  form.bankList[index].bankName = data.bankName;

}


function idCardFrontAttachmentChange(fileList) {
  form.taxRegister.idCardFrontAttachment = fileList;
}

function changeDefaultFlag(e, changeIndex) {
  form.bankList.forEach((item, index) => {
    item.defaultFlag = FLAG_NUMBER_ENUM.FALSE.value;
    if (index == changeIndex) {
      item.defaultFlag = e.target.value;
    }
  });
}

function changeTaxpayerFlag(e, changeIndex) {
  form.bankList.forEach((item, index) => {
    item.taxpayerFlag = FLAG_NUMBER_ENUM.FALSE.value;
    if (index == changeIndex) {
      item.taxpayerFlag = e.target.value;
    }
  });
}

const driverNameRef = ref();
function changeDriverName(e) {
  if (form.driverId) {
    return;
  }
  driverNameRef.value.ajaxQuery(e.target.value);
}
// ----------------------- 银行卡信息 ----------------------------
const nftBankRules = {
  accountName: [{ required: true, message: '请输入开户名' }],
  bankAccount: [{ required: true, message: '请输入银行账号' }],
  bankType: [{ required: true, message: '请选择银行类型' }],
  bankBranchName: [{ required: true, message: '请输入支行名称' }],
  taxpayerFlag: [{ required: true, message: '请选择是否为纳税人信息' }],
}
const bankRules = ref([]);
let bankFormRefs = ref([]);

function addBankItem() {
  form.bankList.push(_.cloneDeep(bankForm));
}

function deleteBankItem(index) {
  form.bankList.splice(index, 1);
}

function setBankRef(el) {
  if (el) {
    bankFormRefs.value.push(el);
  }
}

function cancelValidate() {
  formRef.value.clearValidate();
  rules.value = _.cloneDeep(defaultRules);
}

function reset(){
  formRef.value.resetFields();
  form.taxRegister = {}
  form.bankList = [{
  accountName: '',
  bankAccount: '',
  bankName: '',
  bankType: undefined,
  bankBranchName: '',
  attachment: undefined,
  defaultFlag: FLAG_NUMBER_ENUM.FALSE.value,
  taxpayerFlag: FLAG_NUMBER_ENUM.FALSE.value,
}]
}

// ----------------------- 提交取消 ----------------------
async function onSubmit(validateNftFlag,next) {
  cancelValidate();
  if (validateNftFlag) {
    bankRules.value = _.cloneDeep(nftBankRules);
    rules.value = _.cloneDeep(nftRules);
  } else {
    bankRules.value = [];
    rules.value = _.cloneDeep(defaultRules);
  }
  let validatePass = false;
  nextTick(async () => {
    try {
      // 主表单验证
      await formRef.value.validate();
      // 银行信息表单验证
      for (let bankFormRef of bankFormRefs.value) {
        await bankFormRef.validate();
      }
      validatePass = true;
    } catch (error) {
      console.log('error', error);
      message.error('参数验证错误，请仔细填写表单数据!');
    }
    // 方法调用
    if (validatePass) {
      await operateForm(validateNftFlag,next);
    }
  });
}

async function operateForm(validateNftFlag,next) {
  useSpinStore().show();
  try {
    let params = _.cloneDeep(form);
    params.idCardEndlessFlag = params.idCardEndlessFlag ? 1 : 0;
    params.drivingLicenseEndlessFlag = params.drivingLicenseEndlessFlag ? 1 : 0;
    params.validateNftFlag = validateNftFlag;
    if (params.driverId) {
      await driverApi.updateDriver(params);
    } else {
      await driverApi.saveDriver(params);
    }
    message.success(`${params.driverId ? '修改' : '添加'}成功`);
    if(next){
      reset()
    }else{
      onClose();
    }
  } catch (error) {
    console.log('error', error);
  } finally {
    useSpinStore().hide();
  }
}

// 关闭
function onClose() {
  useUserStore().closePage(route, router);
}
</script>
<style lang="less" scoped>
.form-width {
  width: 180px;
}

.flex-end {
  display: flex;
  align-items: center;
  justify-content: flex-end;
}

::v-deep .ant-form-item-required {
  color: #ff4d4f !important;
}

.end {
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
</style>
