<!--
 * @Description: file content
 * @Author: yandy
 * @Date: 2022-07-07 10:18:53
 * @LastEditors:
 * @LastEditTime: 2022-07-07 10:18:53
-->
<template>
  <a-modal :open="visible" :width="1000" title="修改路线" ok-text="确认" cancel-text="取消" @ok="onSubmit" @cancel="onClose">
    <a-alert
      v-for="(item, index) in waybillRef.exceptionList"
      :key="index"
      :message="item.exceptionMessage"
      type="error"
      class="smart-margin-bottom5"
    />

    <a-form ref="formRef" :model="form" :rules="rules">
      <a-space v-for="(item, index) in form.pathList" :key="index" style="display: flex; margin-bottom: 8px" align="baseline">
        <a-form-item
          :name="['pathList', index, 'provinceName']"
          :rules="{ required: true, message: '请选择地区' }"
          :label="$smartEnumPlugin.getDescByValue('PATH_TYPE_ENUM', item.type)"
        >
          <smart-area-cascader v-model:value="item.area" type="province_city_district" @change="(value, select) => changeArea(value, select, item)" />
        </a-form-item>
        <a-form-item :name="['pathList', index, 'address']" :rules="{ required: true, message: '请输入详细地址' }">
          <a-input v-model:value="item.address" placeholder="请输入详细地址" @change="getDistance" style="width: 200px" class="smart-margin-left5" />
        </a-form-item>
        <a-form-item :name="['pathList', index, 'contactName']" :rules="{ required: true, message: '请输入联系人' }">
          <a-input v-model:value="item.contactName" placeholder="请输入联系人" style="width: 150px" class="smart-margin-left5" />
        </a-form-item>
        <a-form-item :name="['pathList', index, 'contactPhone']">
          <a-input v-model:value="item.contactPhone" placeholder="请输入联系电话" style="width: 150px" class="smart-margin-left5" />
        </a-form-item>
        <a-button @click="showAddressSelectModal(item.type,index,item)" type="link">地图选点</a-button>
      </a-space>
      <a-form-item label="运输距离(公里)" name="distance">
        {{form.distance}}
        <a-button @click="getDistance" type="primary" class="smart-margin-left5">距离计算</a-button>
      </a-form-item>
      <MapModal ref="mapRef" @confirmSelect="selectAddressByMap"/>
    </a-form>
  </a-modal>
</template>
<script setup>
import { ref, reactive, nextTick } from 'vue';
import { message } from 'ant-design-vue';
import SmartAreaCascader from '/@/components/smart-area-cascader/index.vue';
import { useSpinStore } from '/@/store/modules/system/spin';
import { PATH_TYPE_ENUM } from '/@/constants/business/transport-route-const';
import _ from 'lodash';
import { waybillApi } from '/@/api/business/waybill/waybill-api';
import { baiduMapApi } from '/@/api/support/baidumap/baidu-map-api';
import MapModal from '/src/views/business/order/components/map-modal.vue';
import { getCodeByAdcode } from '/@/lib/area-util';
import { SmartLoading } from '/@/components/smart-loading';


//  组件
const formRef = ref();
// 校验规则
const rules = {
  distance: [{ required: true, message: '请输入运输距离' }],
};

// form
const formDefault = {
  waybillId: undefined,
  distance: undefined,
  pathList: [],
};
let form = reactive({ ...formDefault });
// 地址默认值
const pathDefault = {
  contactName: '',
  contactPhone: '',
  area: [],
  province: undefined,
  provinceName: undefined,
  city: undefined,
  cityName: undefined,
  district: undefined,
  districtName: undefined,
  address: undefined,
  type: undefined,
};
let path = reactive({ ...pathDefault });
// 是否展示
const visible = ref(false);

// 显示modal
const waybillRef = ref({});
async function showModal(id) {
  try {
    console.log(id);
  
  SmartLoading.show();
  let res = await waybillApi.getDetail(id);
  Object.assign(form, formDefault);
  form.waybillId = res.data.waybillId;
  form.distance = res.data.distance;
  form.pathList = [];
  if (!_.isEmpty(res.data.pathList)) {
    res.data.pathList.forEach((e) => {
      form.pathList.push(e);
    });
  }
  await nextTick();
  updateArea(form.pathList);
  visible.value = true;
  } catch (error) {
    console.log(error);
  } finally {
    SmartLoading.hide();
  }
}

function updateArea(pathList) {
  pathList.forEach((e) => {
    if (!e.province) {
      return;
    }
    e.area = [
      {
        value: e.province,
        label: e.provinceName,
      },
      {
        value: e.city,
        label: e.cityName,
      },
      {
        value: e.district,
        label: e.districtName,
      },
    ];
  });
}

//关闭modal
function onClose() {
  Object.assign(form, formDefault);
  form.pathList = [];
  formRef.value.clearValidate();
  visible.value = false;
}

// emit
const emit = defineEmits(['reloadList']);

function onSubmit() {
  console.log(form);
  formRef.value
    .validate()
    .then(async () => {
      useSpinStore().show();
      try {
        await waybillApi.waybillPathUpdate(form);
        message.success('修改成功');
        emit('reloadList', form);
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

async function getDistance() {
  if (_.isEmpty(form.pathList)) {
    message.error('请先选择运输路线');
    return;
  }
  let source = form.pathList.find((e) => PATH_TYPE_ENUM.PLACING_LOADING.value == e.type);
  if (!source) {
    message.error('装货地址不能为空');
    return;
  }
  let dest = form.pathList.find((e) => PATH_TYPE_ENUM.UNLOADING_LOCATION.value == e.type);
  if (!dest) {
    message.error('卸货地址不能为空');
    return;
  }
  let sourceAddress = getLocation(source);
  let destAddress = getLocation(dest);
  try {
    let param = {
      sourceCity: source.cityName,
      sourceDistrict: source.districtName,
      sourceProvince: source.provinceName,
      sourceAddress: sourceAddress,
      destAddress: destAddress,
      destCity: dest.cityName,
      destDistrict: dest.districtName,
      destProvince: dest.provinceName,
    };
    let response = await baiduMapApi.distanceQuery(param);
    form.distance = response.data;
  } catch (error) {
    console.log(error);
  } finally {
    useSpinStore().hide();
  }
}

function getLocation(path) {
  let address = '';
  if (path.provinceName) {
    address = address + path.provinceName;
  }
  if (path.cityName) {
    address = address + path.cityName;
  }
  if (path.districtName) {
    address = address + path.districtName;
  }
  if (path.address) {
    address = address + path.address;
  }
  return address;
}

// 地址变化
function changeArea(value, selectedOptions, path) {
  if (_.isEmpty(selectedOptions)) {
    path.area = [];
    path.province = undefined;
    path.provinceName = undefined;
    path.city = undefined;
    path.cityName = undefined;
    path.district = undefined;
    path.districtName = undefined;

    path.longitude = undefined;
    path.latitude = undefined;

    path.address = undefined;
    return;
  }
  // 地区信息
  path.province = selectedOptions[0].value;
  path.provinceName = selectedOptions[0].label;

  path.city = selectedOptions[1].value;
  path.cityName = selectedOptions[1].label;
  if (selectedOptions[2]) {
    path.district = selectedOptions[2].value;
    path.districtName = selectedOptions[2].label;
  }else {
    path.district = undefined;
    path.districtName = undefined;
  }
  getDistance();
}

// 地图选点

const mapRef = ref();

function showAddressSelectModal (type, index, pathInfo) {
  mapRef.value.showModal(type, index, pathInfo);
}

async function selectAddressByMap (selectedAddress, pathType, pathIndex) {
  try {
    let { latitude, longitude } = selectedAddress;
    const { data } = await baiduMapApi.reverseGeocoding({ latitude, longitude });
    let { adcode, province, city, district, address } = data;
    let codeInfo = getCodeByAdcode(adcode);
    address = address.replace(data.province, '');
    address = address.replace(data.city, '');
    address = address.replace(data.district, '');
    let pathList = form.pathList;
    let pathInfo = pathList[pathIndex];
    let area = [
      {
        value: codeInfo.province,
        label: province,
      },
      {
        value: codeInfo.city,
        label: city,
      },
    ];
    if (district) {
      area.push({
        value: codeInfo.district,
        label: district,
      });
    }
    Object.assign(pathInfo, {
      provinceName: province,
      cityName: city,
      districtName: district,
      address,
      longitude,
      latitude,
      area
    }, codeInfo);
    await formRef.value.validateFields([['pathList', pathIndex, 'provinceName']]);
  } catch (error) {
    console.log('error', error);
  }
}


// ----------------------- 以下是暴露的方法内容 ------------------------
defineExpose({
  showModal,
});
</script>
<style lang="less" scoped>
.form-button {
  font-size: 20px;
  margin-left: 5px;
}
</style>
