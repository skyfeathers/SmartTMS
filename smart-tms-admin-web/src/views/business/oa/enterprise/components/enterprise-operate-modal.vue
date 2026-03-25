<template>
  <a-drawer v-model:open="visible" title="添加" :width="850">
    <a-card class="box-card">
      <a-form ref="formRef" :model="form" :rules="rules" >
        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="企业logo" name="enterpriseLogo">
              <Upload accept=".jpg,.jpeg,.png,.gif"
                      listType="picture-card"
                      :maxUploadSize="1"
                      buttonText="点击上传企业logo"
                      :default-file-list="form.enterpriseLogo"
                      @change="enterpriseLogoChange"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="营业执照" name="businessLicense">
              <Upload accept=".jpg,.jpeg,.png,.gif"
                      listType="picture-card"
                      :maxUploadSize="1"
                      buttonText="点击上传营业执照"
                      :default-file-list="form.businessLicense"
                      @change="businessLicenseChange"></Upload>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="企业名称" name="enterpriseName">
              <a-input v-model:value="form.enterpriseName" placeholder="请输入企业名称"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="企业简称" name="enterpriseShortName">
              <a-input v-model:value="form.enterpriseShortName" placeholder="请输入企业简称"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="统一社会信用代码" name="unifiedSocialCreditCode">
              <a-input v-model:value="form.unifiedSocialCreditCode" placeholder="请输入统一社会信用代码"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="道路运输经营许可证" name="networkFreightTransportCode">
              <a-input v-model:value="form.networkFreightTransportCode" placeholder="请输入道路运输经营许可证"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="类型" name="type">
              <SmartEnumSelect v-model:value="form.type" placeholder="请选择类型" style="width: 100%"
                               enum-name="ENTERPRISE_TYPE_ENUM"/>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </a-card>

    <a-card class="box-card smart-margin-top5">
      <a-form ref="formRef" :model="form" :rules="rules" >
        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="联系人" name="contact">
              <a-input v-model:value="form.contact" placeholder="请输入联系人"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="联系人电话" name="contactPhone">
              <a-input v-model:value="form.contactPhone" placeholder="请输入联系人电话"/>
            </a-form-item>
          </a-col>

          <a-col :span="12">
            <a-form-item label="所在城市" name="provinceCityDistrict">
              <smart-area-cascader type="province_city_district" style="width:100%"
                                   v-model:value="area" placeholder="请选择所在城市"
                                   @change="changeArea"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="详细地址" name="address">
              <a-input v-model:value="form.address" placeholder="请输入详细地址"/>
            </a-form-item>
          </a-col>

          <a-col :span="12">
            <a-form-item label="邮箱" name="email">
              <a-input v-model:value="form.email" placeholder="请输入邮箱"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="状态" name="disabledFlag">
              <a-switch v-model:checked="enabledChecked" @change="enabledCheckedChange" checked-children="禁用"
                        un-checked-children="启用"/>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </a-card>

    <a-card class="box-card smart-margin-top5">
      <a-form ref="formRef" :model="form" :rules="rules" >
        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="网站名称" name="websiteName">
              <a-input v-model:value="form.websiteName" placeholder="请输入网站名称"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="备案号" name="beiAnNo">
              <a-input v-model:value="form.beiAnNo" placeholder="请输入备案号"/>
            </a-form-item>
          </a-col>
          <a-col :span="12" v-if="platformOperate">
            <a-form-item label="平台标识" name="platformFlag">
              <a-switch v-model:checked="platformFlagChecked" @change="platformFlagCheckedChange" checked-children="平台"
                        un-checked-children="租户"/>
            </a-form-item>
          </a-col>

          <a-col :span="24" v-for="(item, index) in form.domainNameList" style="display: flex">
            <a-form-item :label="`域名${index + 1}`">
              <a-input v-model:value="form.domainNameList[index]" placeholder="请输入域名"  style="width: 400px"/>
            </a-form-item>

            <a-button @click="addDomain()" type="link">添加域名</a-button>
            <a-button @click="removeDomain(index)" type="link">删除域名</a-button>
          </a-col>
        </a-row>
      </a-form>
    </a-card>

    <a-card class="smart-margin-top5 end" size="small">
      <a-space>
        <a-button @click="onClose">取消</a-button>
        <a-button type="primary" @click="onSubmit()">保存</a-button>
      </a-space>
    </a-card>
  </a-drawer>
  <InitAccountDialog ref="initAccountDialogRef"/>
</template>

<script setup>

import { ref, reactive, nextTick } from 'vue';
import { message } from 'ant-design-vue';
import SmartAreaCascader from '/@/components/smart-area-cascader/index.vue';
import Upload from '/@/components/upload/index.vue';
import SmartEnumSelect from '/@/components/smart-enum-select/index.vue';
import InitAccountDialog from './enterprise-init-account-dialog.vue';
import { useSpinStore } from '/@/store/modules/system/spin';
import { enterpriseApi } from '/@/api/business/oa/enterprise-api';
import { useRoute, useRouter } from 'vue-router';
import _ from 'lodash';
import { regular } from '/@/constants/regular-const';

defineExpose({
  showModal
})
const emit = defineEmits(["refresh"]);

// ---------------- 弹窗操作 ----------------
// 是否展示
const visible = ref(false);
// 是否平台操作
const platformOperate = ref(true);

function showModal (enterpriseId, operateType) {
  Object.assign(form, formDefault);
  area.value = [];
  if (enterpriseId) {
    detail(enterpriseId);
  }
  visible.value = true;
  platformOperate.value = operateType;
}

function onClose() {
  visible.value = false;
}

//  组件
const formRef = ref();

const formDefault = {
  enterpriseId: undefined,
  enterpriseName: undefined,
  unifiedSocialCreditCode: undefined,
  networkFreightTransportCode:undefined,
  businessLicense: undefined,
  contact: undefined,
  contactPhone: undefined,
  email: undefined,
  province: undefined,
  provinceName: undefined,
  city: undefined,
  cityName: undefined,
  district: undefined,
  districtName: undefined,
  address: undefined,
  disabledFlag: false,
  platformFlag: false,
  websiteName: '',
  websiteDesc: '',
  beiAnNo:'',
  enterpriseShortName: null,
  domainNameList: [''],
  domainName: '',
};
let form = reactive({ ...formDefault });
const rules = {
  enterpriseName: [{ required: true, message: '请输入企业名称' }],
  enterpriseShortName: [{ required: true, message: '请输入企业简称' }],
  unifiedSocialCreditCode: [{ required: true, message: '请输入统一社会信用代码' }],
  // networkFreightTransportCode: [{ required: true, message: '请输入道路运输经营许可证' }],
  contact: [{ required: true, message: '请输入联系人' }],
  contactPhone: [{ required: true, message: '请输入联系人电话' },
    { pattern: regular.isLandlineOrPhone, message: '请输入正确的联系人电话', trigger: 'blur' }
  ],
  type: [{ required: true, message: '请选择类型' }],
};

//-------------回显账号密码信息----------
let initAccountDialogRef = ref();
function showAccount(accountName, password) {
  initAccountDialogRef.value.showModal(accountName, password);
}

function onSubmit() {
  console.log(form.domainNameList);
  formRef.value
      .validate()
      .then(async () => {
        useSpinStore().show();
        try {
          form.domainName = form.domainNameList.join(",");
          if (form.enterpriseId) {
            await enterpriseApi.update(form);
          } else {
            await enterpriseApi.create(form);
            showAccount(form.contactPhone, form.contactPhone)
          }
          message.success(`${form.enterpriseId ? '修改' : '添加'}成功`);
          emit('refresh');
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

// 状态
const enabledChecked = ref(true);

function enabledCheckedChange (checked) {
  form.disabledFlag = checked;
}

const platformFlagChecked =  ref(false);

function platformFlagCheckedChange (checked) {
  console.log("platformFlagCheckedChange", checked);
  form.platformFlag = checked;
}

// 地区
const area = ref([]);

function changeArea(value, selectedOptions) {
  Object.assign(form, {
    province: '',
    provinceName: '',
    city: '',
    cityName: '',
    district: '',
    districtName: ''
  });
  if (!_.isEmpty(selectedOptions)) {
    // 地区信息
    form.province = area.value[0].value;
    form.provinceName = area.value[0].label;

    form.city = area.value[1].value;
    form.cityName = area.value[1].label;
    if (area.value[2]) {
      form.district = area.value[2].value;
      form.districtName = area.value[2].label;
    }
  }
}

function enterpriseLogoChange(fileList){
  form.enterpriseLogo = fileList;
}

function businessLicenseChange(fileList){
  form.businessLicense = fileList;
}


// ----------------------- 以下是方法 ------------------------
const route = useRoute();
const router = useRouter();

async function detail(enterpriseId) {
  try {
    let result = await enterpriseApi.detail(enterpriseId);
    let data = result.data;
    Object.assign(form, data);
    form.domainNameList = data.domainName.split(",");
    enabledChecked.value = data.disabledFlag;
    platformFlagChecked.value = data.platformFlag;
    nextTick(() => {
      // 省市区不存在，不需要赋值
      if (!data.provinceName) {
        return;
      }
      area.value = [
        {
          value: data.province,
          label: data.provinceName,
        },
        {
          value: data.city,
          label: data.cityName,
        },
        {
          value: data.district,
          label: data.districtName,
        },
      ]
    });
  } catch (error) {
    console.log(error);
  } finally {
    useSpinStore().hide();
  }
}

function addDomain(){
  form.domainNameList.push('');
}
function removeDomain(index){
  form.domainNameList.splice(index, 1);
}
</script>

<style lang='less' scoped>
.form-width {
  width: 100% !important;
}

.footer {
  width: 100%;
  display: flex;
  justify-content: flex-end;
}

:deep(.ant-card-body) {
  padding: 10px;
}
</style>
