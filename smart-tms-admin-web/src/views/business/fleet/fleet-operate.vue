<template>
  <div>
    <a-form ref="formRef" :model="form" :rules="rules" labelAlign="left">
      <a-card size="small" title="车队信息">
        <a-row :gutter="16">
          <a-col :lg="width.lg" :md="width.md" :sm="width.sm" :xl="width.xl" :xs="width.xs" :xxl="width.xxl">
            <a-form-item label="速记码" name="shorthandCode">
              <a-input v-model:value="form.shorthandCode" class="form-width" placeholder="请输入速记码"/>
            </a-form-item>
          </a-col>
          <a-col :lg="width.lg" :md="width.md" :sm="width.sm" :xl="width.xl" :xs="width.xs" :xxl="width.xxl">
            <a-form-item label="车队名称" name="fleetName">
              <a-input v-model:value="form.fleetName" class="form-width" placeholder="请输入车队名称"/>
            </a-form-item>
          </a-col>
          <a-col :lg="width.lg" :md="width.md" :sm="width.sm" :xl="width.xl" :xs="width.xs" :xxl="width.xxl">
            <a-form-item label="车队长" name="captainName">
              <a-input v-model:value="form.captainName" class="form-width" placeholder="请输入车队长"/>
            </a-form-item>
          </a-col>
          <a-col :lg="width.lg" :md="width.md" :sm="width.sm" :xl="width.xl" :xs="width.xs" :xxl="width.xxl">
            <a-form-item label="车队长联系方式" name="captainPhone">
              <a-input v-model:value="form.captainPhone" class="form-width" placeholder="请输入车队长联系方式"/>
            </a-form-item>
          </a-col>
          <a-col :lg="width.lg" :md="width.md" :sm="width.sm" :xl="width.xl" :xs="width.xs" :xxl="width.xxl">
            <a-form-item label="负责人" name="managerId">
              <employee-select v-model:value="form.managerId" :leaveFlag="false" class="form-width"
                               placeholder="请选择负责人"/>
            </a-form-item>
          </a-col>
          <a-col :lg="width.lg" :md="width.md" :sm="width.sm" :xl="width.xl" :xs="width.xs" :xxl="width.xxl">
            <a-form-item label="备注" name="remark">
              <a-input v-model:value="form.remark" class="form-width" placeholder="请输入备注"/>
            </a-form-item>
          </a-col>
        </a-row>
      </a-card>

      <a-card size="small" title="车队长信息" class="smart-margin-top10">
        <a-row :gutter="16">
          <a-col :lg="width.lg" :md="width.md" :sm="width.sm" :xl="width.xl" :xs="width.xs" :xxl="width.xxl">
            <a-form-item label="身份证（人像面）" name="captainIdCardBackPic">
              <Upload
                  :default-file-list="form.captainIdCardBackPic"
                  :folder="FILE_FOLDER_TYPE_ENUM.FLEET.value"
                  :maxUploadSize="1"
                  accept="jpg, jpeg, png"
                  buttonText="点击上传图片"
                  listType="picture-card"
                  @change="idCardBackPicChange"
              />
            </a-form-item>
          </a-col>
          <a-col :lg="width.lg" :md="width.md" :sm="width.sm" :xl="width.xl" :xs="width.xs" :xxl="width.xxl">
            <a-form-item label="身份证（国徽面）" name="captainIdCardFrontPic">
              <Upload
                  :default-file-list="form.captainIdCardFrontPic"
                  :folder="FILE_FOLDER_TYPE_ENUM.FLEET.value"
                  :maxUploadSize="1"
                  accept="jpg, jpeg, png"
                  buttonText="点击上传图片"
                  listType="picture-card"
                  @change="idCardFrontPicChange"
              />
            </a-form-item>
          </a-col>
          <a-col :lg="width.lg" :md="width.md" :sm="width.sm" :xl="width.xl" :xs="width.xs" :xxl="width.xxl">
            <a-form-item label="身份证号" name="captainIdCard">
              <a-input v-model:value="form.captainIdCard" class="form-width" placeholder="请输入身份证号" />
            </a-form-item>
          </a-col>
        </a-row>
      </a-card>

      <a-card class="smart-margin-top10" size="small" title="银行卡信息">
        <template v-for="(item, index) in form.bankList" :key="index">
          <a-form :ref="setBankRef" :model="item" :rules="bankRules" labelAlign="left">
            <a-row :gutter="16">
              <a-col :lg="width.lg" :md="width.md" :sm="width.sm" :xl="width.xl" :xs="width.xs" :xxl="width.xxl">
                <a-form-item label="银行类型" name="bankType">
                  <SmartDictSelect keyCode="BANK-TYPE" v-model:value="item.bankType" :select-label="item.bankName" width="100%" />
                </a-form-item>
              </a-col>
              <a-col :lg="width.lg" :md="width.md" :sm="width.sm" :xl="width.xl" :xs="width.xs" :xxl="width.xxl">
                <a-form-item label="开户名" name="accountName">
                  <a-input v-model:value="item.accountName" class="form-width" placeholder="请输入开户名" />
                </a-form-item>
              </a-col>
              <a-col :lg="width.lg" :md="width.md" :sm="width.sm" :xl="width.xl" :xs="width.xs" :xxl="width.xxl">
                <a-form-item label="银行账号" name="bankAccount">
                  <a-input v-model:value="item.bankAccount" class="form-width" placeholder="请输入银行账号" />
                </a-form-item>
              </a-col>
<!--              <a-col :lg="width.lg" :md="width.md" :sm="width.sm" :xl="width.xl" :xs="width.xs" :xxl="width.xxl">-->
<!--                <a-form-item label="银行名称" name="bankName">-->
<!--                  <a-input v-model:value="item.bankName" class="form-width" placeholder="请输入银行名称" />-->
<!--                </a-form-item>-->
<!--              </a-col>-->
              <a-col :lg="width.lg" :md="width.md" :sm="width.sm" :xl="width.xl" :xs="width.xs" :xxl="width.xxl">
                <a-form-item label="支行名称" name="bankBranchName">
                  <a-input v-model:value="item.bankBranchName" class="form-width" placeholder="请输入支行名称" />
                </a-form-item>
              </a-col>
              <a-col :lg="width.lg" :md="width.md" :sm="width.sm" :xl="width.xl" :xs="width.xs" :xxl="width.xxl">
                <a-form-item label="是否默认" name="defaultFlag">
                  <a-radio-group v-model:value="item.defaultFlag" name="radioGroup"
                                 @change="e=>changeDefaultFlag(e,index)">
                    <a-radio :value="FLAG_NUMBER_ENUM.TRUE.value">是</a-radio>
                    <a-radio :value="FLAG_NUMBER_ENUM.FALSE.value">否</a-radio>
                  </a-radio-group>
                </a-form-item>
              </a-col>
              <a-col :lg="width.lg" :md="width.md" :sm="width.sm" :xl="width.xl" :xs="width.xs" :xxl="width.xxl">
                <a-form-item label="附件信息" name="attachment">
                  <Upload
                    :default-file-list="item.attachment"
                    :folder="FILE_FOLDER_TYPE_ENUM.DRIVER.value"
                    :maxUploadSize="5"
                    buttonText="点击上传附件"
                    @change="bankAttachmentChange($event, index)"
                  />
                </a-form-item>
              </a-col>
              <a-col :span="24" style="margin-bottom: 10px">
                <a-button block type="dashed" @click="addBankItem">
                  <template #icon>
                    <plus-outlined />
                  </template>
                  新增银行卡信息
                </a-button>
                <a-button v-if="form.bankList.length > 1" block class="smart-margin-top5" type="dashed" @click="deleteBankItem(index)">
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
      <a-card class="smart-margin-top5 flex-end" size="small">
        <a-space>
          <a-button @click="onClose">取消</a-button>
          <a-button type="primary" @click="onSubmit()">保存并提交审核</a-button>
        </a-space>
      </a-card>
    </a-form>
  </div>
</template>
<script setup>
import { computed, onMounted, reactive, ref, watch } from 'vue';
import Upload from '/@/components/upload/index.vue';
import EmployeeSelect from "/@/components/employee-select/index.vue";
import SmartDictSelect from '/@/components/smart-dict-select/index.vue';
import { FILE_FOLDER_TYPE_ENUM } from '/@/constants/business/file-const';
import { FLAG_NUMBER_ENUM } from '/@/constants/common-const';
import { message } from 'ant-design-vue';
import { useSpinStore } from '/@/store/modules/system/spin';
import { fleetApi } from '/@/api/business/fleet/fleet-api';
import { useRoute, useRouter } from 'vue-router';
import { useUserStore } from '/@/store/modules/system/user';
import _ from 'lodash';
import SmartAreaCascader from '/@/components/smart-area-cascader/index.vue';
import { ocrSetup } from '/@/views/support/ocr/ocr-setup';
import { regular } from '/@/constants/regular-const';
import { formWidth } from '/@/views/business/hooks/form-hooks';
import { useAppConfigStore } from '/@/store/modules/system/app-config';
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
let formRef = ref();
const rules = {
  shorthandCode: [{ required: true, message: '请输入速记码' }],
  fleetName: [{ required: true, message: '请输入车队名称' }],
  captainPhone: [{ pattern: regular.phone, message: '车队长联系方式格式不正确', trigger: 'blur' }],
  captainIdCard: [{ pattern: regular.isIdentityCard, message: '身份证号格式不正确', trigger: 'blur' }]
};
const bankForm = {
  accountName: '',
  bankAccount: '',
  bankName: '',
  bankType: '',
  bankBranchName: '',
  attachment: undefined,
  defaultFlag: FLAG_NUMBER_ENUM.FALSE.value
};
const form = reactive({
  bankList: [],
});

onMounted(() => {
  let fleetId = route.query.fleetId;
  if (!fleetId) {
    addBankItem();
    return;
  }
  useUserStore().setTagName(route, '编辑车队');
  getDetail(fleetId);
});

// ----------------------- 详情查询 ----------------------------
async function getDetail(fleetId) {
  useSpinStore().show();
  try {
    let responseModel = await fleetApi.getFleet(fleetId);
    let detail = responseModel.data;
    (detail.bankList || []).forEach(item => {
      item.defaultFlag = item.defaultFlag ? FLAG_NUMBER_ENUM.TRUE.value : FLAG_NUMBER_ENUM.FALSE.value;
    });
    Object.assign(form, detail);d
    if (detail.provinceName) {
      form.registerArea = [
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
      ];
    }
    if (_.isEmpty(form.bankList)) {
      addBankItem();
    }
  } catch (error) {
    console.log('error', error);
  } finally {
    useSpinStore().hide();
  }
}
// ----------------------- 地区选择 ----------------------------
function setArea(params) {
  if (!_.isEmpty(form.registerArea)) {
    // 地区信息
    params.province = form.registerArea[0].value;
    params.provinceName = form.registerArea[0].label;

    params.city = form.registerArea[1].value;
    params.cityName = form.registerArea[1].label;
    if (form.registerArea[2]) {
      params.district = form.registerArea[2].value;
      params.districtName = form.registerArea[2].label;
    }
  }
}
// ----------------------- 文件上传 ----------------------------
// ocr 识别
let { recognizeIdCardFace, recognizeIdCardBack } = ocrSetup();
function idCardBackPicChange(fileList) {
  form.captainIdCardBackPic = fileList;
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
  // 车队长
  form.captainName = data.name;
  // 身份证号
  form.captainIdCard = data.idNumber;
}

function idCardFrontPicChange(fileList) {
  form.captainIdCardFrontPic = fileList;
}

function bankAttachmentChange(fileList, index) {
  form.bankList[index].attachment = fileList;
}

// ----------------------- 银行卡信息 ----------------------------
const bankRules = {
  bankType: [{ required: true, message: '请选择银行类型' }],
  accountName: [{ required: true, message: '请输入开户名' }],
  bankAccount: [{ required: true, message: '请输入银行账号' }],
  bankBranchName: [{ required: true, message: '请输入支行名称' }],
  captainName: [{ required: true, message: '请输入车队长' }],
  captainPhone: [{ required: true, message: '请输入车队长联系方式' }],
};
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

function changeDefaultFlag (e, changeIndex) {
  form.bankList.forEach((item, index) => {
    item.defaultFlag = FLAG_NUMBER_ENUM.FALSE.value;
    if (index == changeIndex) {
      item.defaultFlag = e.target.value;
    }
    console.log(item)
  });
}

// ----------------------- 提交取消 ----------------------
async function onSubmit () {
  let validatePass = false;
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
    await operateForm();
  }
}

async function operateForm() {
  useSpinStore().show();
  try {
    let params = _.cloneDeep(form);
    setArea(params);
    if (params.fleetId) {
      await fleetApi.updateFleet(params);
    } else {
      await fleetApi.saveFleet(params);
    }
    message.success(`${params.fleetId ? '修改' : '添加'}成功`);
    onClose();
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
  width: 100%;
}

.flex-end {
  display: flex;
  align-items: center;
  justify-content: flex-end;
}
</style>
