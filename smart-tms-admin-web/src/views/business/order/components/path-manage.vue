<!--
 * @Description:
 * @version:
 * @Author: lidoudou
 * @Date: 2022-07-15 20:07:11
 * @LastEditors: lidoudou
 * @LastEditTime: 2022-07-15 20:32:14
-->
<template>
  <a-card class="smart-margin-top10">
    <template #title>
      <div class="path-manage-header">
        <div style="color:red">路线信息</div>
        <div class="path-manage-distance">
          <span>运输距离：</span>
          <span style="color:red">{{form.distance}}</span>
          <span>公里</span>
          
        </div>
      </div>
      
    </template>
    <a-form ref="formRef" :model="form" :rules="rules">
      <a-descriptions size="small" bordered :column="1">
        <a-descriptions-item label="运输路线">
          <a-form-item>
            <TransportRouteSelect v-model:value="form.transportRouteId" :transportRouteType="transportRouteType"
              @change="changeTransportRoute" ref="transportRouteSelectRef" />
            <a-button @click="addTransportRoute" type="link">添加路线</a-button>
            <a-button @click="addAddress(PATH_TYPE_ENUM.PLACING_LOADING.value)" type="link">添加装货地址</a-button>
            <a-button @click="addAddress(PATH_TYPE_ENUM.UNLOADING_LOCATION.value)" type="link">添加卸货地址</a-button>
          </a-form-item>
        </a-descriptions-item>
        <template v-for="(item, index) in form.pathList" :key="index">
            <a-descriptions-item :label="$smartEnumPlugin.getDescByValue('PATH_TYPE_ENUM', item.type)">
              <a-form-item>
            <smart-area-cascader v-model:value="item.area" type="province_city_district" :key="index"
              @change="(value, select) => changeArea(value, select, item)" />
            <a-input v-model:value="item.address" placeholder="请输入详细地址" style="width: 200px" class="smart-margin-left5" />
            <a-input v-model:value="item.contactName" placeholder="请输入联系人" style="width: 150px" class="smart-margin-left5"
              @change="(e) => changeContactName(e, item)" />
            <a-input v-model:value="item.contactPhone" placeholder="请输入联系电话" style="width: 150px"
              class="smart-margin-left5" />
            
              <a-button @click="showAddressSelectModal(item.type, index, item)" type="link">地图选点</a-button>
              <a-button
              v-if="item.type != PATH_TYPE_ENUM.CONTAINER_LOCATION.value && item.type != PATH_TYPE_ENUM.RETURN_CONTAINER_LOCATION.value"
              @click="removeAddress(item.type, index)" type="link">移除</a-button>
          </a-form-item>
            </a-descriptions-item>
        </template>
       
      </a-descriptions>
    </a-form>
    <TransportRouteOperateModal ref="transportRouteRef" @reloadList="reloadTransportRoute" />
    <MapModal ref="mapRef" @confirmSelect="selectAddressByMap" />
  </a-card>
</template>
<script setup>
import { ref, reactive, nextTick, watch, onMounted } from 'vue';
import { message } from 'ant-design-vue';
import { useSpinStore } from '/@/store/modules/system/spin';
import _ from 'lodash';

import SmartAreaCascader from '/@/components/smart-area-cascader/index.vue';
import TransportRouteSelect from '/@/components/transport-route-select/index.vue';
import TransportRouteOperateModal
  from '/@/views/business/business-material/transport-route/components/transport-route-operate-modal.vue';
import { PATH_TYPE_ENUM, TRANSPORTATION_TYPE_ENUM } from '/@/constants/business/transport-route-const';
import { ORDER_TYPE_ENUM } from '/@/constants/business/order-const';
import { baiduMapApi } from '/@/api/support/baidumap/baidu-map-api';
import MapModal from '/@/views/business/order/components/map-modal.vue';
import { getCodeByAdcode } from '/@/lib/area-util.js';

const props = defineProps({
  // 运输类型
  transportRouteType: {
    type: [Number, String]
  },
  // 订单类型
  orderType: {
    type: [Number, String]
  }
});

watch(
  () => props.transportRouteType,
  (newValue) => {
    initPath();
  }
);

watch(
  () => props.orderType,
  (newValue) => {
    initPath();
  }
);

//  组件
const formRef = ref();
// 校验规则
const rules = {
  transportRouteName: [{ required: true, message: '请输入路线名称' }],
};

// form
const formDefault = {
  transportRouteId: undefined,
  transportRouteName: undefined,
};
let form = reactive({ ...formDefault });

watch(
  () => form.pathList,
  (newValue) => {
    if(!newValue || newValue.length == 0) {
      return;
    }
    let source = newValue.find(e => PATH_TYPE_ENUM.PLACING_LOADING.value == e.type);
    let dest = newValue.find(e => PATH_TYPE_ENUM.UNLOADING_LOCATION.value == e.type);
    if (source && dest && source.province && dest.province) {
      getDistance();
    }
  }, 
  {
    deep: true,
    immediate: true,
  }
);

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
let pathList = ref([]);
let path = reactive({ ...pathDefault });

// 是否是编辑
const updateFlag = ref(false);

function updatePathList(transportPathList, distance) {
  form.pathList = [];
  form.distance = distance || 0;
  if (!_.isEmpty(transportPathList)) {
    transportPathList.forEach(e => {
      Object.assign(path, e);
      form.pathList.push(e);
    });
    nextTick(() => {
      form.pathList.forEach(e => {
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
    });
  } else {
    initPath();
  }
}

function onSubmit() {
  formRef.value
    .validate()
    .then(async () => {
      useSpinStore().show();
      try {
        form.pathList = form.pathList;
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

// 移除地址
function removeAddress(type, index) {
  form.pathList.splice(index, 1);
}

// 添加地址
function addAddress(type) {
  let list = _.cloneDeep(form.pathList);
  let path = Object.assign({}, pathDefault);
  path.type = type;
  list.push(path);
  list.sort((a, b) => { return a.type - b.type; });
  updatePathList(list);
}

// 新增是 初始化地址
function initPath() {
  form.pathList = [];
  if (props.transportRouteType == TRANSPORTATION_TYPE_ENUM.CONTAINER_TRANSPORT.value && props.orderType != ORDER_TYPE_ENUM.NETWORK_FREIGHT.value) {
    addAddress(PATH_TYPE_ENUM.CONTAINER_LOCATION.value);
    addAddress(PATH_TYPE_ENUM.PLACING_LOADING.value);
    addAddress(PATH_TYPE_ENUM.UNLOADING_LOCATION.value,);
    addAddress(PATH_TYPE_ENUM.RETURN_CONTAINER_LOCATION.value);
  } else {
    addAddress(PATH_TYPE_ENUM.PLACING_LOADING.value);
    addAddress(PATH_TYPE_ENUM.UNLOADING_LOCATION.value);
  }
}

// 地址变化
function changeArea(value, selectedOptions, path) {
  console.log(value, selectedOptions, path);
  if (_.isEmpty(selectedOptions)) {
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
  }
}

const emit = defineEmits(['changeUnloadingLocationContactName']);

function changeContactName(e, item) {
  console.log(e);
  let contactName = item.contactName;
  if (!contactName) {
    return;
  }
  if (PATH_TYPE_ENUM.UNLOADING_LOCATION.value != item.type) {
    return;
  }
  emit('changeUnloadingLocationContactName', item.contactName);
}

function changeTransportRoute(value, item) {
  let contactName = '';
  if (item) {
    // 如果是网络货运，只加入装货、卸货地点
    let pathList = item.pathList;
    if (props.orderType == ORDER_TYPE_ENUM.NETWORK_FREIGHT.value) {
      let pathTypeList = [
        PATH_TYPE_ENUM.PLACING_LOADING.value,
        PATH_TYPE_ENUM.UNLOADING_LOCATION.value
      ];
      pathList = pathList.filter(e => pathTypeList.includes(e.type));
    }
    updatePathList(pathList);
    let find = item.pathList.find(e => PATH_TYPE_ENUM.UNLOADING_LOCATION.value == e.type && e.contactName);
    if (find) {
      contactName = find.contactName;
    }
  } else {
    updatePathList([]);
  }
  emit('changeUnloadingLocationContactName', contactName);
}

// 新增运输路线
const transportRouteRef = ref();
function addTransportRoute() {
  transportRouteRef.value.showModal(null, props.transportRouteType);
}

// 运输路线下拉选择框
const transportRouteSelectRef = ref();

function reloadTransportRoute(transportRoute) {
  transportRouteSelectRef.value.queryData();
  // 如果已经选择过运输路线，则不处理
  if (form.transportRouteId) {
    return;
  }
  Object.assign(form, { transportRouteId: transportRoute.transportRouteId })
  changeTransportRoute(transportRoute.transportRouteId, transportRoute);
}

async function getDistance() {
  if (_.isEmpty(form.pathList)) {
    message.error('请先选择运输路线');
    return;
  }
  let source = form.pathList.find(e => PATH_TYPE_ENUM.PLACING_LOADING.value == e.type);
  if (!source.province) {
    message.error('装货地址不能为空');
    return;
  }
  let dest = form.pathList.find(e => PATH_TYPE_ENUM.UNLOADING_LOCATION.value == e.type);
  if (!dest.province) {
    message.error('卸货地址不能为空');
    return;
  }
  let sourceAddress = getLocation(source);
  let destAddress = getLocation(dest);
  try {
    let param = {
      sourceAddress: sourceAddress,
      destAddress: destAddress,
    }
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
  return address;
}

onMounted(() => {
  initPath();
});


function resetForm(){
  initPath();
  transportRouteSelectRef.value.selectValue = undefined
}

// ------------------------ 地图选点 ------------------------
const mapRef = ref();

function showAddressSelectModal (type, index, pathInfo) {
  mapRef.value.showModal(type, index, pathInfo);
}

async function selectAddressByMap (selectedAddress, pathType, pathIndex) {
  try {
    console.log(selectedAddress, pathType, pathIndex);
  let { latitude, longitude } = selectedAddress;
  const { data } = await baiduMapApi.reverseGeocoding({ latitude, longitude });
    let { adcode, province, city, district, address } = data;
    let codeInfo = getCodeByAdcode(adcode);
    address = address.replace(data.province, '');
    address = address.replace(data.city, '');
    address = address.replace(data.district, '');
    let pathInfo = form.pathList[pathIndex];
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
  } catch (error) {
    console.log(error);
  }
}

defineExpose({
  form,
  formRef,
  updatePathList,
  resetForm
});
</script>
<style lang="less" scoped>

::v-deep .ant-descriptions-row>th {
  width: 7% !important;
}

::v-deep .ant-descriptions-row>td {
  width: 93% !important;
}

.path-manage-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.path-manage-distance {
  font-weight: 500;
}



</style>
