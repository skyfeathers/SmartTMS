<!--
 * @Author: lp
 * @Date: 2022-07-13 17:59:31
 * @LastEditors: zhuoda
 * @LastEditTime: 2022-07-20
 * @Description: 挂车新建编辑
 * @FilePath: \nft-admin-web\src\views\business\bracket\bracket-operate.vue
-->
<template>
  <div>
    <a-form ref="formRef" :model="infoForm" :rules="infoRules" labelAlign="left">
      <a-card size="small" title="基础信息">
        <a-descriptions size="small" bordered :column="{ lg: 2, md: 2, sm: 2, xl: 3, xs: 2, xxl: 5 }">
          <a-descriptions-item label="经营方式"  v-if="!bracketId">
            <a-form-item name="businessMode">
              <SmartEnumSelect v-model:value="infoForm.businessMode" enumName="VEHICLE_BUSINESS_MODE_ENUM"
                               style="width:100%"/>
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="负责人">
            <a-form-item name="managerId">
              <employee-select v-model:value="infoForm.managerId" :leaveFlag="false" :disabledFlag="false" placeholder="请选择负责人" class="form-width"/>
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item />
        </a-descriptions>
      </a-card>
      <a-card class="smart-margin-top10" size="small" title="挂车行驶证正本信息">
        <a-descriptions size="small" bordered :column="{ lg: 2, md: 2, sm: 2, xl: 3, xs: 2, xxl: 5 }">
          <a-descriptions-item label="行驶证正本">
            <a-form-item name="drivingLicensePic">
              <Upload
                :default-file-list="infoForm.drivingLicensePic"
                :folder="FILE_FOLDER_TYPE_ENUM.BRACKET.value"
                :maxUploadSize="1"
                accept="jpg, jpeg, png"
                buttonText="点击上传图片"
                listType="picture-card"
                @change="drivingLicensePicChange"
              />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="行驶证副本">
            <a-form-item name="drivingLicenseEctypePic">
              <Upload
                :default-file-list="infoForm.drivingLicenseEctypePic"
                :folder="FILE_FOLDER_TYPE_ENUM.BRACKET.value"
                :maxUploadSize="1"
                accept="jpg, jpeg, png"
                buttonText="点击上传图片"
                listType="picture-card"
                @change="drivingLicenseEctypePicChange"
              />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="挂车车牌号" class="required">
            <a-form-item name="bracketNo">
              <a-input v-model:value="infoForm.bracketNo" @change="changeBracketNo" class="form-width" placeholder="请输入挂车车牌号" />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="实际所属人">
            <a-form-item name="owner">
              <a-input v-model:value="infoForm.owner" class="form-width" placeholder="请输入实际所属人" />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="使用性质">
            <a-form-item name="nature">
              <a-input v-model:value="infoForm.nature" class="form-width" placeholder="请输入使用性质" />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="车辆识别代号">
            <a-form-item name="vin">
              <a-input v-model:value="infoForm.vin" class="form-width" placeholder="请输入车辆识别代号" />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="车辆类型">
            <a-form-item name="type">
              <a-input v-model:value="infoForm.type" class="form-width" placeholder="请选择车辆类型" />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="车牌颜色">
            <a-form-item name="plateColorCode">
              <smart-enum-select
                v-model:value="infoForm.plateColorCode"
                enum-name="VEHICLE_PLATE_COLOR_ENUM"
                placeholder="请选择车牌颜色"
                width="100%"
              />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="载重（吨）">
            <a-form-item name="tonnage">
              <a-input-number
                class="form-width"
                placeholder="请输入载重（吨）"
                v-model:value="infoForm.tonnage"
                :min="0"
                :max="9999"
                :step="0.001"
                string-mode
              />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="重量（吨）">
            <a-form-item name="weight">
              <a-input-number
                class="form-width"
                placeholder="请输入重量（吨）"
                v-model:value="infoForm.weight"
                :min="0"
                :max="9999"
                :step="0.001"
                string-mode
              />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="注册日期">
            <a-form-item name="registerTime">
              <a-date-picker v-model:value="infoForm.registerTime" valueFormat="YYYY-MM-DD" class="form-width" placeholder="请输入注册日期" />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="发证日期">
            <a-form-item name="issueTime">
              <a-date-picker v-model:value="infoForm.issueTime" valueFormat="YYYY-MM-DD" class="form-width" placeholder="请输入发证日期" />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item>

          </a-descriptions-item>
        </a-descriptions>
      </a-card>
      <a-card class="smart-margin-top5 flex-end" size="small">
        <a-space>
          <a-button @click="onClose">取消</a-button>
          <a-button type="primary" @click="onSubmit(false)">保存并提交审核</a-button>
          <a-button type="primary" v-if="!bracketId" @click="onSubmit(false,true)">保存并继续创建</a-button>
<!--          <a-tooltip placement="top">
            <template #title>
              <span>校验挂车当前信息是否符合网络货运标准</span>
            </template>
            <a-button type="primary" @click="onSubmit(true)">校验并保存<question-circle-outlined/>
            </a-button>
          </a-tooltip>-->
        </a-space>
      </a-card>
    </a-form>
  </div>
</template>
<script setup>
import { computed, nextTick, onMounted, reactive, ref, watch } from 'vue';
import { message, Modal } from 'ant-design-vue';
import { useRoute, useRouter } from 'vue-router';
import { useSpinStore } from '/@/store/modules/system/spin';
import { useUserStore } from '/@/store/modules/system/user';
import _ from 'lodash';
import { FILE_FOLDER_TYPE_ENUM } from '/@/constants/business/file-const';
import { VEHICLE_BUSINESS_MODE_ENUM } from '/@/constants/business/vehicle-const';
import { bracketApi } from '/@/api/business/bracket/bracket-api';
import Upload from '/@/components/upload/index.vue';
import SmartEnumSelect from '/@/components/smart-enum-select/index.vue';
import EmployeeSelect from "/@/components/employee-select/index.vue";
import EnterpriseSelect from '/@/components/enterprise-select/index.vue';
import { ocrSetup } from '/@/views/support/ocr/ocr-setup';
import { useAppConfigStore } from '/@/store/modules/system/app-config';
import { formWidth } from '/@/views/business/hooks/form-hooks';
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
let formRef = ref();

// 挂车数据
const infoFormDefault = {
  enterpriseId: null,
  drivingLicensePic: [], // 行驶证图片 ,
  drivingLicenseEctypePic: [], // 行驶证副本 ,
  owner: undefined, // 实际所属人 ,
  ownerType: undefined, // 所属人性质
  address: undefined, // 住址 ,
  nature: undefined, // 使用性质 ,
  type: undefined, // 型号
  vin: undefined, // 车辆识别代号 ,
  bracketNo: undefined, // 挂车车牌号
  plateColorCode: undefined, // 车牌颜色 ,
  tonnage: undefined, // 载重（吨） ,
  weight: undefined, // 重量
  registerTime: undefined, // 注册日期 ,
  issueTime: undefined, // 发证日期 ,
  issuingOrganizations: undefined, // 发证机关 ,
  expireTime: undefined, // 到期日期 ,
  remark: undefined, // 备注 ,
  drivingLicenseAttachment: undefined, // 行驶证附件
  drivingLicenseEctypeAttachment: undefined, // 行驶证副本附件
  businessMode: VEHICLE_BUSINESS_MODE_ENUM.INNER_MANAGEMENT.value, // 经营方式
};

let infoForm = reactive({ ...infoFormDefault });

let bracketId = ref();

const defaultRules = {
  bracketNo: [{ required: true, message: '请输入挂车车牌号' }],
};

// 验证表单
const nftRules = {
  bracketNo: [{ required: true, message: '请输入挂车车牌号' }],
  tonnage: [{ required: true, message: '请输入核定载质量' }],
  weight: [{ required: true, message: '请输入总质量' }],
};
// 验证表单
const infoRules = ref(defaultRules)

// ----------------- ocr 识别   start -----------------
let { recognizeVehicleLicenseFace, recognizeVehicleLicenseBack } = ocrSetup();
// 行驶证正本
function drivingLicensePicChange(fileList) {
  infoForm.drivingLicensePic = fileList;
  formRef.value.validateFields('drivingLicensePic');
  if (_.isEmpty(fileList)) {
    return;
  }
  let file = fileList[0];
  let fileUrl = file.fileUrl;
  recognizeVehicleLicenseFace(fileUrl, recognizeVehicleLicenseAfter);
}

function recognizeVehicleLicenseAfter(data) {
  if (!data) {
    return;
  }
  // 挂车号(挂车车牌号)
  infoForm.bracketNo = data.licensePlateNumber;
  // 车型
  infoForm.type = data.vehicleType;
  // 车辆识别代码(车架号)
  infoForm.vin = data.vinCode;
  // 所属人
  infoForm.owner = data.owner;
  // 使用性质
  infoForm.nature = data.useNature;
  // 发证日期
  infoForm.issueTime = data.issueDate;
  // 注册日期
  infoForm.registerTime = data.registrationDate;
}

// 行驶证副本
function drivingLicenseEctypePicChange(fileList) {
  infoForm.drivingLicenseEctypePic = fileList;
  formRef.value.validateFields('drivingLicenseEctypePic');
  if (_.isEmpty(fileList)) {
    return;
  }
  let file = fileList[0];
  let fileUrl = file.fileUrl;
  recognizeVehicleLicenseBack(fileUrl, recognizeVehicleLicenseBackAfter);
}

function recognizeVehicleLicenseBackAfter (data) {
  if (!data) {
    return;
  }
  // 总质量
  infoForm.tonnage = data.totalWeight;
  let regexNumber = /[1-9][0-9]*/g;
  // 总质量
  if (data.totalWeight) {
    let totalWeight = data.totalWeight.match(regexNumber);
    if (totalWeight) {
      infoForm.tonnage = totalWeight.join('');
    }
  }
  // 核定载质量(载重kg)
  if (data.permittedWeight) {
    let permittedWeight = data.permittedWeight.match(regexNumber);
    if (permittedWeight) {
      infoForm.weight = permittedWeight.join('');
    }
  }
}

function changeBracketNo () {
  infoForm.bracketNo = (infoForm.bracketNo || '').replace(/\s*/g, '').toUpperCase();
}

// 获取详情
async function getDetail() {
  try {
    useSpinStore().show();
    let result = await bracketApi.bracketDetail(bracketId.value);
    let detail = result.data;
    Object.assign(infoForm, detail);
    delete infoForm.insuranceList;
    delete infoForm.repairList;
  } catch (error) {
    console.log('error', error);
  } finally {
    useSpinStore().hide();
  }
}

function reset(){
  formRef.value.resetFields()
}

// 保存操作
async function onSubmit(validateNftFlag,next) {
  cancelValidate();
  if (validateNftFlag) {
    infoRules.value = _.cloneDeep(nftRules);
  } else {
    infoRules.value = _.cloneDeep(defaultRules);
  }
  nextTick(async () => {
    let validatePass = false;
    try {
      // 主表单验证
      await formRef.value.validate();
      validatePass = true;
    } catch (error) {
      console.log('error', error);
      message.error('参数验证错误，请仔细填写表单数据!');
    }
    // 方法调用
    if (validatePass) {
      await submitInfo(validateNftFlag,next);
    }
  });
}

// 保存信息
async function submitInfo(validateNftFlag,next) {
  try {
    useSpinStore().show();
    let params = Object.assign({}, infoForm, { validateNftFlag });
    if (bracketId.value) {
      await bracketApi.bracketUpdate(params);
    } else {
      await bracketApi.bracketSave(params);
    }
    message.success(`${bracketId.value ? '修改' : '添加'}成功`);
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

let route = useRoute();
let router = useRouter();

// 关闭
function onClose() {
  useUserStore().closePage(route, router);
}

function cancelValidate () {
  formRef.value.clearValidate();
  infoRules.value = _.cloneDeep(defaultRules);
}

// 加载
onMounted(() => {
  bracketId.value = route.query.bracketId;
  if (!bracketId.value) {
    return;
  }
  // 修改标签名称
  useUserStore().setTagName(route, '编辑挂车');
  getDetail();
});
</script>
<style lang="less" scoped>
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
.flex-end {
  display: flex;
  align-items: center;
  justify-content: flex-end;
}
::v-deep .ant-form-item-required{
  color: #ff4d4f !important;
}

.end{
  position: sticky;
  bottom: 0;
}
</style>
