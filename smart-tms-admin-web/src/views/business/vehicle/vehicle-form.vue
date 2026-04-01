<!--
 * @Description: 新建车辆
 * @Author: zhuoda
 * @Date: 2022-07-05
 * @LastEditTime: 2022-07-12
 * @LastEditors: zhuoda
-->
<template>
  <div class="smart-form-wrapper">
    <a-form class="smart-form" labelAlign="right" :label-col="labelStyle" ref="formRef" :model="formState"
      :rules="rules">
      <!---基础信息-->
      <a-card size="small" title="基础信息" class="smart-form-card">
        <a-descriptions size="small" bordered :column="{ lg: 2, md: 2, sm: 2, xl: 3, xs: 2, xxl: 5 }">
          <a-descriptions-item label="经营方式" v-if="!formState.vehicleId">
            <a-form-item name="businessMode">
              <SmartEnumSelect v-model:value="formState.businessMode" enumName="VEHICLE_BUSINESS_MODE_ENUM" width="180px"
                @change="changeBusinessMode" />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="车籍地">
            <a-form-item name="province">
              <smart-area-cascader type="province_city_district" style="width: 180px" v-model:value="registerArea"
                @change="changeRegisterArea" />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="绑定司机">
            <a-form-item name="driverIdList">
              <div class="form-width flex-center">
                <DriverSelect ref="driverSelectRef" v-model:value="formState.driverIdList" :multiple="true" width="140px" />
                <a @click="showQuickCreateModal(QUICK_CREATE_TYPE_ENUM.DRIVER.value)" style="margin-left: 5px;">新建</a>
              </div>
            </a-form-item>
          </a-descriptions-item>
          
          <a-descriptions-item label="绑定挂车">
            <a-form-item name="bracketId">
              <div class="form-width flex-center">
                <BracketSelect ref="bracketSelectRef" v-model:value="formState.bracketId" width="140px"/>
                <a @click="showQuickCreateModal(QUICK_CREATE_TYPE_ENUM.BRACKET.value)" style="margin-left: 5px;">新建</a>
              </div>
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="负责人">
            <a-form-item name="managerId">
              <employee-select v-model:value="formState.managerId" :leaveFlag="false" placeholder="请选择负责人"
                width="180px" />
            </a-form-item>
          </a-descriptions-item>
          
          <a-descriptions-item label="备注">
            <a-form-item name="remark">
              <a-textarea :auto-size="{ minRows: 1, maxRows: 5 }" v-model:value="formState.remark"
                style="width: 180px" />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item></a-descriptions-item>
        </a-descriptions>
      </a-card>

      <!---行驶证-正本-->
      <a-card size="small" title="行驶证" class="smart-margin-top10 smart-form-card">
        <a-descriptions size="small" bordered :column="{ lg: 2, md: 2, sm: 2, xl: 3, xs: 2, xxl: 5 }">
          <a-descriptions-item label="行驶证正本-附件">
            <a-form-item name="drivingLicenseAttachment">
              <Upload style="width: 100%" :default-file-list="formState.drivingLicenseAttachment"
                :folder="FILE_FOLDER_TYPE_ENUM.VEHICLE.value" :maxUploadSize="1" accept=".jpg,.jpeg,.png,.gif"
                listType="picture-card" buttonText="上传行驶证正本" @change="changeDrivingLicenseAttachment" />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="行驶证副本附件">
            <a-form-item name="drivingLicenseEctypeAttachment">
              <Upload style="width: 100%" :default-file-list="formState.drivingLicenseEctypeAttachment"
                :folder="FILE_FOLDER_TYPE_ENUM.VEHICLE.value" :maxUploadSize="1" accept=".jpg,.jpeg,.png,.gif"
                buttonText="上传行驶证副本" listType="picture-card" @change="changeDrivingLicenseEctypeAttachment" />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="车牌号" class="required">
            <a-form-item name="vehicleNumber">
              <a-input v-model:value="formState.vehicleNumber" @change="changeVehicleNumber" style="width: 180px" />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="车牌颜色">
            <a-form-item name="vehiclePlateColorCode">
              <SmartEnumSelect enumName="VEHICLE_PLATE_COLOR_ENUM" width="180px"
                v-model:value="formState.vehiclePlateColorCode" />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="车辆类型">
            <a-form-item name="vehicleType">
              <SmartDictSelect keyCode="vehicleType" v-model:value="formState.vehicleType" width="180px"
                :selectLabel="formState.selectVehicleTypeName" />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="机动车登记证书编号">
            <a-form-item name="vehicleRegistrationCertificateNo">
              <a-input v-model:value="formState.vehicleRegistrationCertificateNo" class="form-width"  />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="车辆识别代码(车架号)">
            <a-form-item name="vin">
              <a-input v-model:value="formState.vin" class="form-width" />
            </a-form-item>
          </a-descriptions-item>
          
          <a-descriptions-item label="发动机号">
            <a-form-item name="engineNumber">
              <a-input v-model:value="formState.engineNumber" class="form-width"  />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="品牌型号">
            <a-form-item name="model">
              <a-input v-model:value="formState.model" class="form-width"  />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="发证机关">
            <a-form-item name="issuingOrganizations">
              <a-input v-model:value="formState.issuingOrganizations" class="form-width" />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="注册日期">
            <a-form-item name="registerDate">
              <a-date-picker valueFormat="YYYY-MM-DD" v-model:value="formState.registerDate" class="form-width" />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="发证日期">
            <a-form-item name="issueDate">
              <a-date-picker valueFormat="YYYY-MM-DD" v-model:value="formState.issueDate" class="form-width" />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="车辆审验到期时间">
            <a-form-item name="vehicleAuditExpireDate">
              <a-date-picker valueFormat="YYYY-MM-DD" v-model:value="formState.vehicleAuditExpireDate"
                class="form-width" />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="总质量(kg)">
            <a-form-item name="grossMass">
              <a-input-number v-model:value="formState.grossMass" class="form-width" />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="整备质量(kg)">
            <a-form-item name="curbWeight">
              <a-input-number v-model:value="formState.curbWeight" :min="0" class="form-width" />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="核定载质量(载重kg)">
            <a-form-item name="vehicleTonnage">
              <a-input v-model:value="formState.vehicleTonnage" class="form-width" />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="牵引总质量(kg)">
            <a-form-item name="tractionWeight">
              <a-input-number v-model:value="formState.tractionWeight" :min="0" class="form-width" />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="外廓尺寸">
            <a-form-item name="gabarite">
              <a-input v-model:value="formState.gabarite" class="form-width" />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="能源类型">
            <a-form-item name="vehicleEnergyType">
              <SmartDictSelect keyCode="vehicleEnergyType" width="180px" v-model:value="formState.vehicleEnergyType" />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="所属人">
            <a-form-item name="owner">
              <a-input v-model:value="formState.owner" class="form-width" />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="使用性质">
            <a-form-item name="nature">
              <a-input v-model:value="formState.nature" class="form-width" />
            </a-form-item>
          </a-descriptions-item>
          
          
          
        </a-descriptions>
      </a-card>

      <!---道路运输证-->
      <a-card size="small" title="道路运输证" class="smart-margin-top10 smart-form-card">
        <a-alert message="由于道路运输证样式不一，可能存在部分证件无法识别【道路运输证号】或【道路运输经营许可证号】，
        请上传证件后仔细查看。" type="warning" show-icon class="smart-margin-top10 smart-margin-bottom5" />

        <a-descriptions size="small" bordered :column="{ lg: 2, md: 2, sm: 2, xl: 3, xs: 2, xxl: 5 }">
          <a-descriptions-item label="道路运输证-附件" :span="{ lg: 2, md: 2, sm: 2, xl: 3, xs: 2, xxl: 5 }">
            <a-form-item name="roadTransportCertificateAttachment">
              <Upload :default-file-list="formState.roadTransportCertificateAttachment"
                :folder="FILE_FOLDER_TYPE_ENUM.VEHICLE.value" :maxUploadSize="1" accept=".jpg,.jpeg,.png,.gif"
                listType="picture-card" buttonText="上传道路运输证" @change="changeRoadTransportCertificateAttachment" />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="道路运输证号">
            <a-form-item name="roadTransportCertificateNumber">
              <a-input v-model:value="formState.roadTransportCertificateNumber" class="form-width" />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="道路运输经营许可证号">
            <a-form-item name="roadTransportBusinessLicenseNumber">
              <div style="display: flex; align-items: center">
                <a-input v-model:value="formState.roadTransportBusinessLicenseNumber" class="form-width" />
                <a-tooltip placement="top">
                  <template #title>
                    <span>道路运输经营许可证号如无可使用车籍地地区编码+000000(当前车籍地：{{ formState.districtName }}
                      地区编码:{{ formState.district }})</span>
                  </template>
                  <question-circle-outlined />
                </a-tooltip>
              </div>
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="道运证有效期开始日期">
            <a-form-item name="roadTransportCertificateStartDate">
              <a-date-picker valueFormat="YYYY-MM-DD" v-model:value="formState.roadTransportCertificateStartDate"
              class="form-width" />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="道运证有效期结束日期">
            <a-form-item name="roadTransportCertificateExpireDate">
              <a-date-picker valueFormat="YYYY-MM-DD" v-model:value="formState.roadTransportCertificateExpireDate"
              class="form-width" />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item />
        </a-descriptions>
      </a-card>

      <!---挂靠信息-->
      <a-card size="small" title="挂靠信息" class="smart-margin-top10 smart-form-card">
        <a-descriptions size="small" bordered :column="{ lg: 2, md: 2, sm: 2, xl: 3, xs: 2, xxl: 5 }">
          <a-descriptions-item label="挂靠企业协议附件" :span="{ lg: 2, md: 2, sm: 2, xl: 3, xs: 2, xxl: 5 }">
            <a-form-item name="relyEnterpriseAttachment">
              <Upload :default-file-list="formState.relyEnterpriseAttachment"
                :folder="FILE_FOLDER_TYPE_ENUM.VEHICLE.value" :maxUploadSize="10" accept=".jpg,.jpeg,.png,.gif,.pdf"
                listType="picture-card" buttonText="上传挂靠协议" @change="changeRelyEnterpriseAttachment" />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="挂靠协议到期时间">
            <a-form-item name="relyEnterpriseExpireDate">
              <a-date-picker valueFormat="YYYY-MM-DD" v-model:value="formState.relyEnterpriseExpireDate"
              class="form-width" />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item />
        </a-descriptions>
      </a-card>

      <a-card class="smart-margin-top10 smart-form-card" size="small">
        <template #title>
          <a-tooltip>
            车主信息<question-circle-outlined />
            <template #title>
              挂靠车费用报表展示车辆的收款人信息
            </template>
          </a-tooltip>
        </template>
        <a-descriptions size="small" bordered :column="{ lg: 2, md: 2, sm: 2, xl: 3, xs: 2, xxl: 5 }">
          <a-descriptions-item label="车主">
            <a-form-item name="receiveUserName">
              <a-input v-model:value="formState.receiveUserName" class="form-width" placeholder="请输入车主" />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="车主银行">
            <a-form-item name="receiveBankName">
              <a-input v-model:value="formState.receiveBankName" class="form-width" placeholder="请输入车主银行" />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="车主银行账号">
            <a-form-item name="receiveBankNo">
              <a-input v-model:value="formState.receiveBankNo" class="form-width" placeholder="请输入车主银行账号" />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item />
        </a-descriptions>
      </a-card>
    </a-form>

    <a-card class="smart-margin-top5 smart-form-submit-btn-space end" size="small">
      <a-space>
        <a-button @click="onClose">取消</a-button>
        <a-button type="primary" @click="submitForm(false)">提交审核</a-button>
        <a-button type="primary" v-if="!formState.vehicleId" @click="submitForm(false,true)">提交并继续创建</a-button>
        <a-tooltip placement="top">
          <template #title>
            <span>校验车辆当前信息是否符合网络货运标准</span>
          </template>
          <a-button type="primary" @click="submitForm(true)">网络货运校验<question-circle-outlined />
          </a-button>
        </a-tooltip>
      </a-space>
    </a-card>
    <VehicleQuickCreate ref="quickCreateRef" @reloadList="reload"/>
  </div>
</template>
<script setup>
import SmartEnumSelect from '/@/components/smart-enum-select/index.vue';
import SmartDictSelect from '/@/components/smart-dict-select/index.vue';
import Upload from '/@/components/upload/index.vue';
import DriverSelect from '/@/components/driver-select/index.vue';
import BracketSelect from '/@/components/bracket-select/index.vue';
import EmployeeSelect from "/@/components/employee-select/index.vue";
import EnterpriseSelect from '/@/components/enterprise-select/index.vue';
import SmartAreaCascader from '/@/components/smart-area-cascader/index.vue';
import VehicleQuickCreate from '/@/components/vehicle-quick-create/index.vue';
import { computed, reactive, ref, onMounted, watch, nextTick } from 'vue';
import { FILE_FOLDER_TYPE_ENUM } from '/@/constants/business/file-const';
import { message } from 'ant-design-vue';
import { useRoute, useRouter } from 'vue-router';
import { useUserStore } from '/@/store/modules/system/user';
import { VEHICLE_BUSINESS_MODE_ENUM, QUICK_CREATE_TYPE_ENUM } from '/@/constants/business/vehicle-const';
import { SmartLoading } from '/@/components/smart-loading';
import { vehicleApi } from '/@/api/business/vehicle/vehicle-api';
import _ from 'lodash';
import { ocrSetup } from "/@/views/support/ocr/ocr-setup";
import dayjs from "dayjs";
import { regular } from '/@/constants/regular-const';
import { useAppConfigStore } from '/@/store/modules/system/app-config';
import { formWidth } from '/@/views/business/hooks/form-hooks';
// ---------------- 页面加载 ----------------

const quickCreateRef = ref()
function showQuickCreateModal (typeValue) {
  quickCreateRef.value.showModal(typeValue);
}

const driverSelectRef = ref()
const bracketSelectRef = ref()
function reload(){
  driverSelectRef.value.queryList()
  bracketSelectRef.value.queryList()
}

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
  let vehicleId = route.query.vehicleId;
  if (vehicleId) {
    useUserStore().setTagName(route, '编辑车辆');
    getDetail(vehicleId);
  } else {
    useUserStore().setTagName(route, '新建车辆');
  }
});

// ----------------------- 详情查询 ----------------------------
async function getDetail(vehicleId) {
  SmartLoading.show();
  try {
    let response = await vehicleApi.getDetail(vehicleId);
    let detail = response.data;
    detail.driverIdList = detail.driverVehicleList.map(e => e.driverId);
    if (!_.isEmpty(detail.vehicleType)) {
      detail.vehicleType = detail.vehicleType[0].valueCode;
    }
    Object.assign(formState, detail);
    nextTick(() => {
      registerArea.value = [
        {
          value: detail.province,
          label: detail.provinceName,
        },
        {
          value: detail.city,
          label: detail.cityName,
        },
        {
          value: detail.district,
          label: detail.districtName,
        },
      ]
    });
  } catch (error) {
    console.log('error', error);
  } finally {
    SmartLoading.hide();
  }
}

// 内管车、挂靠车显示所属公司
const showEnterpriseFlag = computed(() => {
  let typeList = [
    VEHICLE_BUSINESS_MODE_ENUM.INNER_MANAGEMENT.value,
    VEHICLE_BUSINESS_MODE_ENUM.RELY.value
  ];
  return typeList.includes(formState.businessMode);
})
// ---------------- 表单 ----------------
const labelStyle = {};
const formDefault = {
  enterpriseId: null,
  businessMode: VEHICLE_BUSINESS_MODE_ENUM.INNER_MANAGEMENT.value, //经营方式
  vehicleAuditExpireDate: null, //车辆审验到期时间
  bracketId: null, //绑定挂车
  driverIdList: [], //绑定司机
  remark: null, //备注
  drivingLicenseAttachment: [], //行驶证-正本-附件
  vehicleNumber: null, //车牌号
  vehiclePlateColorCode: null, //车牌颜色
  vehicleType: undefined, //车辆类型
  vin: null, //车辆识别代码(车架号)
  owner: null, //所属人
  ownerType: null, //所属人性质
  licenseNo: null, //车辆行驶证档案编号
  issuingOrganizations: null, //发证机关
  registerDate: null, //注册日期
  issueDate: null, //发证日期
  expireDate: null, //到期时间
  drivingLicenseEctypeAttachment: [], //行驶证副本附件
  grossMass: null, //总质量
  vehicleTonnage: null, //核定载质量(载重)
  gabarite: null, //外廓尺寸
  vehicleEnergyType: null, //能源类型
  roadTransportCertificateAttachment: [], //道路运输证-附件
  roadTransportCertificateNumber: null, //道路运输证号
  roadTransportCertificateStartDate: null, //道运证有效期开始日期
  roadTransportCertificateExpireDate: null, //道运证有效期结束日期
  relyEnterpriseAttachment: [], //挂靠企业协议附件
  relyEnterpriseExpireDate: null, //挂靠协议到期时间
  relyEnterpriseBusinessLicenseAttachment: [], //挂靠企业营业执照
  relyEnterpriseName: null, //挂靠企业名称
  relyEnterpriseUnifiedSocialCreditCode: null, //挂靠企业统一社会信用代码
  relyEnterpriseRoadTransportBusinessLicenseAttachment: [], //道路运输经营许可证
  relyEnterpriseRoadTransportBusinessLicenseNumber: null, //道路运输经营许可证号
  relyEnterpriseRoadTransportBusinessLicenseIssueDate: null, //发证日期
  relyEnterpriseRoadTransportBusinessLicenseExpireDate: null, //结束日期
  engineNumber: null, // 发动机号
  vehicleRegistrationCertificateNo: null, // 机动车登记证书编号
  roadTransportBusinessLicenseNumber: null,
  selectVehicleTypeName: null, //选中的车辆类型名称
  receiveUserName: null, // 车主
  receiveBankName: null, // 车主银行
  receiveBankNo: null, // 车主银行账号
};
const formState = reactive({ ...formDefault });

let formRef = ref();
const defaultRules = {
  businessMode: [{ required: true, message: '请选择经营方式' }],
  vehicleNumber: [{ required: true, message: '请输入车牌号' }],
  relyEnterpriseUnifiedSocialCreditCode: [{
    pattern: regular.unifiedSocialCreditCode,
    message: '统一社会信用代码不正确',
    trigger: 'blur'
  }]
};

const rules = ref({ ...defaultRules });

const validateBracket = async (rule, value) => {
  if (formState.vehicleType && (formState.vehicleType || '').startsWith('Q') && !formState.bracketId) {
    return Promise.reject('牵引车需要绑定挂车');
  }
  return Promise.resolve();
};

const validateTractionWeight = async (rule, value) => {
  if (formState.vehicleType && (formState.vehicleType || '').startsWith('Q') && !formState.tractionWeight) {
    return Promise.reject('牵引车请输入牵引总质量');
  }
  return Promise.resolve();
};

const nftRules = {
  businessMode: [{ required: true, message: '请选择经营方式' }],
  province: [{ required: true, message: '请选择车籍地' }],
  vehicleNumber: [{ required: true, message: '请输入车牌号' }],
  enterpriseId: [{ required: true, message: '请输入所属企业' }],
  bracketId: [{ validator: validateBracket }],
  vehiclePlateColorCode: [{ required: true, message: '请输入车牌颜色' }],
  vehicleType: [{ required: true, message: '请输入车辆类型' }],
  vin: [{ required: true, message: '请输入车辆识别代码' }],
  issuingOrganizations: [{ required: true, message: '请输入发证机关' }],
  registerDate: [{ required: true, message: '请输入注册日期' }],
  issueDate: [{ required: true, message: '请输入发证日期' }],
  grossMass: [{ required: true, message: '请输入总质量' }],
  vehicleTonnage: [{ required: true, message: '请输入核定载质量' }],
  curbWeight: [{ required: true, message: '请输入整备质量' }],
  tractionWeight: [{ validator: validateTractionWeight }],
  vehicleEnergyType: [{ required: true, message: '请输入能源类型' }],
  owner: [{ required: true, message: '请输入所属人' }],
  nature: [{ required: true, message: '请输入使用性质' }],
  roadTransportCertificateNumber: [{ required: true, message: '请输入道路运输证号' }],
  roadTransportBusinessLicenseNumber: [{ required: true, message: '请输入道路运输经营许可证号' }],
  relyEnterpriseUnifiedSocialCreditCode: [{ pattern: regular.unifiedSocialCreditCode, message: '统一社会信用代码不正确', trigger: 'blur' }],
  roadTransportCertificateAttachment: [{ required: true, message: '请输入道路运输证-附件' }],
  drivingLicenseEctypeAttachment: [{ required: true, message: '请上传行驶证副本附件' }],
  drivingLicenseAttachment: [{ required: true, message: '请上传行驶证正本附件' }],
}

// ----------------- ocr 识别   start -----------------
let { recognizeBusinessLicense, recognizeVehicleLicenseFace, recognizeVehicleLicenseBack, recognizeRoadTransportCertificate } = ocrSetup();

function changeDrivingLicenseAttachment(fileList) {
  formState.drivingLicenseAttachment = fileList;
  if (_.isEmpty(fileList)) {
    return;
  }
  let file = fileList[0];
  let fileUrl = file.fileUrl;
  recognizeVehicleLicenseFace(fileUrl, recognizeVehicleLicenseAfter)
}

function recognizeVehicleLicenseAfter(data) {
  if (!data) {
    return;
  }
  // 车牌号
  formState.vehicleNumber = data.licensePlateNumber;
  // 车型
  formState.selectVehicleTypeName = data.vehicleType;
  // 车辆识别代码(车架号)
  formState.vin = data.vinCode;
  // 所属人
  formState.owner = data.owner;
  // 使用性质
  formState.nature = data.useNature;
  // 发证机关
  formState.issuingOrganizations = data.issueAuthority
  // 注册日期
  if (data.registrationDate) {
    formState.registerDate = dayjs(data.registrationDate).format('YYYY-MM-DD');
  }
  // 发证日期
  if (data.issueDate) {
    formState.issueDate = dayjs(data.issueDate).format('YYYY-MM-DD');
  }
  console.log(formState);
}

function changeDrivingLicenseEctypeAttachment(fileList) {
  formState.drivingLicenseEctypeAttachment = fileList;
  if (_.isEmpty(fileList)) {
    return;
  }
  let file = fileList[0];
  let fileUrl = file.fileUrl;
  recognizeVehicleLicenseBack(fileUrl, recognizeVehicleLicenseBackAfter);
}

function recognizeVehicleLicenseBackAfter(data) {
  if (!data) {
    return;
  }
  let regexNumber = /[1-9][0-9]*/g;
  // 总质量
  if (data.totalWeight) {
    let totalWeight = data.totalWeight.match(regexNumber);
    if (totalWeight) {
      formState.grossMass = totalWeight.join('');
    }
  }

  // 核定载质量(载重kg)
  if (data.permittedWeight) {
    let permittedWeight = data.permittedWeight.match(regexNumber);
    if (permittedWeight) {
      formState.vehicleTonnage = permittedWeight.join('');
    }
  }
  // 整备质量
  if (data.curbWeight) {
    let curbWeight = data.curbWeight.match(regexNumber);
    if (curbWeight) {
      formState.curbWeight = curbWeight.join('');
    }
  }
  // 牵引总质量
  if (data.tractionWeight) {
    let tractionWeight = data.tractionWeight.match(regexNumber);
    if (tractionWeight) {
      formState.tractionWeight = tractionWeight.join('');
    }
  }
  // 外廓尺寸
  if (data.overallDimension) {
    let overallDimension = data.overallDimension.match(regexNumber);
    if (overallDimension) {
      formState.gabarite = overallDimension.join('');
    }
  }
}
// 车籍地
const registerArea = ref([]);
function changeRegisterArea(value, selectedOptions) {
  if (!_.isEmpty(selectedOptions)) {
    // 地区信息
    formState.province = registerArea.value[0].value;
    formState.provinceName = registerArea.value[0].label;

    formState.city = registerArea.value[1].value;
    formState.cityName = registerArea.value[1].label;
    if (registerArea.value[2]) {
      formState.district = registerArea.value[2].value;
      formState.districtName = registerArea.value[2].label;
    }
  }
}

function changeVehicleNumber() {
  formState.vehicleNumber = (formState.vehicleNumber || '').replace(/\s*/g, '').toUpperCase();
}
// ----------------- ocr 识别   end -----------------

function changeBusinessMode(businessMode) {
  if (businessMode == VEHICLE_BUSINESS_MODE_ENUM.ASSIGNMENT.value) {
    formState.enterpriseId = null;
  }
}

function changeRoadTransportCertificateAttachment(fileList) {
  formState.roadTransportCertificateAttachment = fileList;
  if (_.isEmpty(fileList)) {
    return;
  }
  let file = fileList[0];
  let fileUrl = file.fileUrl;
  recognizeRoadTransportCertificate(fileUrl, recognizeRoadTransportCertificateAfter);
}

function recognizeRoadTransportCertificateAfter(data) {
  if (!data) {
    return;
  }
  formState.roadTransportBusinessLicenseNumber = data.roadTransportBusinessLicenseNumber;
}

function changeRelyEnterpriseAttachment(e) {
  formState.relyEnterpriseAttachment = e;
}

// 挂靠企业营业执照
function changeRelyEnterpriseBusinessLicenseAttachment(fileList) {
  formState.relyEnterpriseBusinessLicenseAttachment = fileList;
  if (_.isEmpty(fileList)) {
    return;
  }
  let file = fileList[0];
  let fileUrl = file.fileUrl;
  recognizeBusinessLicense(fileUrl, recognizeBusinessLicenseAfter)
}

function recognizeBusinessLicenseAfter(data) {
  if (!data) {
    return;
  }
  formState.relyEnterpriseName = data.companyName;
  formState.relyEnterpriseUnifiedSocialCreditCode = data.creditCode;
}


function changeRelyEnterpriseRoadTransportBusinessLicenseAttachment(e) {
  formState.relyEnterpriseRoadTransportBusinessLicenseAttachment = e;
}

function joinFileStr(array, separator) {
  if (!array || array.length === 0) {
    return null;
  }
  return _.join(
    array.map((e) => e.fileKey),
    separator
  );
}

function reset() {
  formRef.value.resetFields();
  formState.vehicleAuditExpireDate = null
  registerArea.value = []
}

async function submitForm(validateNftFlag,next) {
  cancelValidate();
  if (validateNftFlag) {
    rules.value = _.cloneDeep(nftRules);
  } else {
    rules.value = _.cloneDeep(defaultRules);
  }
  nextTick(() => {
    formRef.value
      .validate()
      .then(async () => {
        SmartLoading.show();
        try {
          let formData = _.cloneDeep(formState);
          formData.drivingLicenseAttachment = joinFileStr(formState.drivingLicenseAttachment, ',');
          formData.drivingLicenseEctypeAttachment = joinFileStr(formState.drivingLicenseEctypeAttachment, ',');
          formData.roadTransportCertificateAttachment = joinFileStr(formState.roadTransportCertificateAttachment, ',');
          formData.relyEnterpriseAttachment = joinFileStr(formState.relyEnterpriseAttachment, ',');
          formData.relyEnterpriseBusinessLicenseAttachment = joinFileStr(formState.relyEnterpriseBusinessLicenseAttachment, ',');
          formData.relyEnterpriseRoadTransportBusinessLicenseAttachment = joinFileStr(
            formState.relyEnterpriseRoadTransportBusinessLicenseAttachment,
            ','
          );
          formData.validateNftFlag = validateNftFlag;
          //牵引车 必须绑定挂车
          // if(formState.vehicleType && ( formState.vehicleType || '').startsWith("Q") && !formData.bracketId){
          //   message.error('牵引车需要绑定挂车');
          //   return;
          // }
          if (formData.vehicleId) {
            await vehicleApi.update(formData);
          } else {
            await vehicleApi.save(formData);
          }
          message.success('操作成功');
          if(next){
            reset();
          }else{
            onClose();
          }
        } catch (error) {
          console.log(error);
        } finally {
          SmartLoading.hide();
        }
      })
      .catch((error) => {
        console.log('error', error);
        message.error('参数验证错误，请仔细填写表单数据!');
      });
  })
}

// 关闭
function onClose() {
  useUserStore().closePage(route, router);
}

function cancelValidate() {
  formRef.value.clearValidate();
  rules.value = _.cloneDeep(defaultRules);
}

</script>
<style lang="less" scoped>
.smart-form-card {
  :deep(.ant-card-head-title) {
    &::before {
      content: '';
      position: absolute;
      top: 3px;
      left: 0;
      width: 3px;
      height: 30px;
      background-color: @primary-color;
    }
  }
}

.smart-form-submit-btn-space {
  width: 100%;
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

.form-width {
  width: 180px;
}
</style>
