<template>

    <a-modal :width="700" :open="visible" :title="title" ok-text="保存" cancel-text="取消" @ok="onSubmit" @cancel="onClose">
      <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 6 }">
        <a-row>
          <a-col :span="24">
            <a-form-item label="类型" name="type">
              <SmartEnumSelect width="200px" v-model:value="form.type" placeholder="类型" enum-name="WAYBILL_VOUCHER_TYPE_ENUM" :disabled="typeDisable" />
            </a-form-item>
          </a-col>
           <a-col :span="24">
            <a-form-item label="凭证图片" name="attachment">
              <Upload
                :default-file-list="form.attachment"
                :folder="FILE_FOLDER_TYPE_ENUM.WAYBILL_VOUCHER.value"
                :maxUploadSize="3"
                accept="jpg, jpeg, png"
                listType="picture-card"
                buttonText="上传凭证"
                @change="(fileList) => form.attachment = fileList"
              />
            </a-form-item>
          </a-col>
           <a-col :span="24">
            <a-form-item label="人车合影" name="sceneAttachment">
              <Upload
                :default-file-list="form.sceneAttachment"
                :folder="FILE_FOLDER_TYPE_ENUM.WAYBILL_VOUCHER.value"
                :maxUploadSize="3"
                accept="jpg, jpeg, png"
                listType="picture-card"
                buttonText="上传人车合影"
                @change="(fileList) => form.sceneAttachment = fileList"
              />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="磅单" name="poundListAttachment">
              <Upload
                :default-file-list="form.poundListAttachment"
                :folder="FILE_FOLDER_TYPE_ENUM.WAYBILL_VOUCHER.value"
                :maxUploadSize="3"
                accept="jpg, jpeg, png"
                listType="picture-card"
                buttonText="上传磅单"
                @change="(fileList) => form.poundListAttachment = fileList"
              />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="磅重" name="poundListQuantity">
              <a-input-number v-model:value="form.poundListQuantity" :min="0" precision="2" style="width: 200px" placeholder="请输入磅重" />
            </a-form-item>
          </a-col>
         

          <a-col :span="24">
            <a-form-item label="位置">
              <a-input v-model:value="form.location" placeholder="请输入位置" style="width: 200px" />
              <a-button type="link" class="smart-margin-left10" @click="showMapModal">地图选点</a-button>
            </a-form-item>
            
          </a-col>

          <a-col :span="24">
            <a-form-item label="纬度">
              <a-input-number v-model:value="form.longitude" style="width: 200px" placeholder="请输入纬度" />
            </a-form-item>
          </a-col>

          <a-col :span="24">
            <a-form-item label="经度">
              <a-input-number v-model:value="form.latitude" style="width: 200px" placeholder="请输入经度" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
          <a-form-item label="上传时间" name="createTime">
            <a-date-picker v-model:value="form.createTime" valueFormat="YYYY-MM-DD HH:mm:ss" :showTime="true" style="width: 300px" />
          </a-form-item>
        </a-col>
        </a-row>
      </a-form>
    </a-modal>
    <MapModal ref="mapRef" pathTypeEnumName="WAYBILL_VOUCHER_TYPE_ENUM" @confirmSelect="selectAddressByMap"/>
</template>
<script setup>
import { onMounted, ref, getCurrentInstance, nextTick, reactive } from 'vue';
import _ from 'lodash';
import { message } from 'ant-design-vue';
import { waybillApi } from '/@/api/business/waybill/waybill-api';
import { FILE_FOLDER_TYPE_ENUM } from '/@/constants/business/file-const';

import SmartEnumSelect from '/@/components/smart-enum-select/index.vue';
import Upload from '/@/components/upload/index.vue';
import { WAYBILL_VOUCHER_TYPE_ENUM } from '/@/constants/business/waybill-const';
import dayjs from 'dayjs';

import MapModal from '/@/views/business/order/components/map-modal.vue';
import { baiduMapApi } from '/@/api/support/baidumap/baidu-map-api';

const smartEnumsPlugin = getCurrentInstance().appContext.config.globalProperties.$smartEnumPlugin;

const emit = defineEmits(['success']);

let visible = ref(false);

let title = ref('添加');

let typeDisable = ref(false);

function onClose() {
  Object.assign(form, formDefault);
  typeDisable.value = false;
  visible.value = false;
}

function showModal(rowData) {
  title.value = '添加';
  typeDisable.value = false;
  Object.assign(form, formDefault);
  if (rowData && !_.isEmpty(rowData)) {
    if (rowData.type) {
      title.value = smartEnumsPlugin.getFieldByValue('WAYBILL_VOUCHER_TYPE_ENUM', rowData.type, 'desc');
      typeDisable.value = true;
    }
    Object.assign(form, rowData);
    console.log('form', form);
  }
  form.createTime = dayjs().format('YYYY-MM-DD HH:mm:ss');
  visible.value = true;
  nextTick(() => {
    formRef.value.clearValidate();
  });
}


// ----------------------- 表单 ------------------------
const formDefault = {
  waybillId: null,
  type: null,
  attachment: null,
  location: null,
  longitude: undefined,
  latitude: undefined,
  createTime: null,
  poundListQuantity: null,
  poundListAttachment: null,
  sceneAttachment: null,
  waybillVoucherId: null,
};
let form = reactive({ ...formDefault });
const rules = {
  type: [{ required: true, message: '请选择类型' }],
  attachment: [{ required: true, message: '请上传凭证' }],
  createTime: [{ required: true, message: '请选择操作时间' }],
};

// ----------------------- 提交表单 ------------------------
const formRef = ref();
function onSubmit() {
  formRef.value
    .validate()
    .then(async () => {
      try {
        let param = _.cloneDeep(form);
        if (param.waybillVoucherId) {
          await waybillApi.updateVoucher(param);
          message.success('更新成功');
         }else {
          await waybillApi.addVoucher(param);
          message.success('添加成功');
         }
        
        emit('success');
        onClose();
      } catch (error) {
        console.log(error);
      }
    })
    .catch((error) => {
      console.log('error', error);
      message.error('参数验证错误，请仔细填写表单数据!');
    });
}

// ----------------------- 地图 ------------------------
const mapRef = ref();
function showMapModal() {
  mapRef.value.showModal(form.type, null, {
    longitude: form.longitude, // 经度
    latitude: form.latitude, // 纬度
    province: form.provinceName, // 省
    city: form.cityName, // 市
    district: form.districtName, // 区
    address: form.location, // 详细地址
  });
}
async function selectAddressByMap (selectedAddress, pathType, pathIndex) {
  try {
    let { latitude, longitude } = selectedAddress;
    const { data } = await baiduMapApi.reverseGeocoding({ latitude, longitude });
    let { adcode, province, city, district, address } = data;
    form.location = address;
    form.longitude = longitude;
    form.latitude = latitude;
  } catch (error) {
    console.log('error', error);
  }
}



defineExpose({
  showModal,
});


</script>