<!--
 * @Description: file content
 * @Author: yandy
 * @Date: 2022-07-07 10:18:53
 * @LastEditors:
 * @LastEditTime: 2022-07-07 10:18:53
-->
<template>
  <a-modal v-model:open="visible" :width="1000" :title="updateFlag ? '编辑' : '添加'" ok-text="确认" cancel-text="取消" @ok="onSubmit"
    @cancel="onClose">
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ style: { width: '90px' } }" >
      <a-row>
        <a-col :span="12">
          <a-form-item label="运输类型" name="transportRouteType">
            <SmartEnumSelect v-model:value="form.transportRouteType" placeholder="请选择运输类型"
              enum-name="TRANSPORTATION_TYPE_ENUM" @change="transportRouteTypeChange" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="路线名称" name="transportRouteName">
            <a-input v-model:value="form.transportRouteName" placeholder="请输入路线名称" />
          </a-form-item>
        </a-col>
      </a-row>

      <a-row>
        <a-col :span="12">
          <a-form-item label="里程（千米）" name="mileage">
            <a-input-number v-model:value="form.mileage" :min="0" :max="999999" :precision="2" placeholder="请输入里程（千米）"
              style="width: 200px" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-button @click="getDistance" type="primary" class="smart-margin-left5">距离计算</a-button>
        </a-col>
        <a-col :span="12">
          <a-form-item label="路线状态" name="disabledFlag">
            <a-switch v-model:checked="enabledChecked" @change="enabledCheckedChange" />
          </a-form-item>
        </a-col>
      </a-row>
      <a-card>

        <a-space style="display: flex; justify-content: space-between;flex-direction: row-reverse; margin-bottom: 8px"
          align="baseline">
          <a-button type="link" block @click="addAddress(PATH_TYPE_ENUM.UNLOADING_LOCATION.value)">
            新增卸货地址
          </a-button>
          <a-button type="link" block @click="addAddress(PATH_TYPE_ENUM.PLACING_LOADING.value)">
            新增装货地址
          </a-button>
          <a-form-item v-if="form.transportRouteType == TRANSPORTATION_TYPE_ENUM.CONTAINER_TRANSPORT.value && !updateFlag"
            label="还柜堆场" name="returnContainerYardId">
            <YardSelect placeholder="请选择还柜堆场"
              @change="(value, yard) => yardChange(value, yard, PATH_TYPE_ENUM.RETURN_CONTAINER_LOCATION.value)" />
          </a-form-item>
          <a-form-item v-if="form.transportRouteType == TRANSPORTATION_TYPE_ENUM.CONTAINER_TRANSPORT.value && !updateFlag"
            label="提柜堆场">
            <YardSelect placeholder="请选择提柜堆场"
              @change="(value, yard) => yardChange(value, yard, PATH_TYPE_ENUM.CONTAINER_LOCATION.value)" />
          </a-form-item>
        </a-space>
        <div v-if="!$lodash.isEmpty(form.containerPathList)">
          <a-space v-for="(item, index) in form.containerPathList" :key="index" style="display: flex; margin-bottom: 8px"
            align="baseline">
            <a-form-item :name="['containerPathList', index, 'provinceName']"
              :rules="{ required: true, message: '请选择地区' }"
              :label="$smartEnumPlugin.getDescByValue('PATH_TYPE_ENUM', item.type)">
              <smart-area-cascader v-model:value="item.area" type="province_city_district"
                @change="(value, select) => changeArea(value, select, item)" />
            </a-form-item>
            <a-form-item :name="['containerPathList', index, 'address']" :rules="{ required: true, message: '请输入详细地址', }">
              <a-input v-model:value="item.address" placeholder="请输入详细地址" 
                class="smart-margin-left5" />
            </a-form-item>
            <a-form-item :name="['containerPathList', index, 'contactName']">
              <a-input v-model:value="item.contactName" placeholder="请输入联系人" style="width: 150px"
                class="smart-margin-left5" />
            </a-form-item>
            <a-form-item :name="['containerPathList', index, 'contactPhone']">
              <a-input v-model:value="item.contactPhone" placeholder="请输入联系电话" style="width: 150px"
                class="smart-margin-left5" />
            </a-form-item>
            <a-button type="link" block @click="showAddressSelectModal(item.type, index, item)">
            地图选点
          </a-button>
          </a-space>
        </div>
        <a-space v-for="(item, index) in form.loadPathList" :key="index" style="display: flex; margin-bottom: 8px"
          align="baseline">
          <a-form-item :name="['loadPathList', index, 'provinceName']"
            :label="$smartEnumPlugin.getDescByValue('PATH_TYPE_ENUM', item.type)">
            <smart-area-cascader v-model:value="item.area" type="province_city_district"
              @change="(value, select) => changeArea(value, select, item)" />
          </a-form-item>
          <a-form-item :name="['loadPathList', index, 'address']">
            <a-input v-model:value="item.address" placeholder="请输入详细地址" class="smart-margin-left5" />
          </a-form-item>
          <a-form-item :name="['loadPathList', index, 'contactName']">
            <a-input v-model:value="item.contactName" placeholder="请输入联系人" style="width: 150px"
              class="smart-margin-left5" />
          </a-form-item>
          <a-form-item :name="['loadPathList', index, 'contactPhone']">
            <a-input v-model:value="item.contactPhone" placeholder="请输入联系电话" style="width: 150px"
              class="smart-margin-left5" />
          </a-form-item>
          
          <a-button type="link" block @click="showAddressSelectModal(item.type, index, item)">
            地图选点
          </a-button>
          <a-button type="link" block @click="removeAddress(item.type, index)">
            移除
          </a-button>
        </a-space>
      </a-card>

    </a-form>
  </a-modal>
  <MapModal ref="mapRef" @confirmSelect="selectAddressByMap" />
</template>
<script setup>
import { ref, reactive, nextTick } from 'vue';
import { message } from 'ant-design-vue';
import SmartAreaCascader from '/@/components/smart-area-cascader/index.vue';
import SmartEnumSelect from '/@/components/smart-enum-select/index.vue';
import YardSelect from '/@/components/yard-select/index.vue';
import { useSpinStore } from '/@/store/modules/system/spin';
import { transportRouteApi } from "/@/api/business/business-material/transport-route-api";
import { PATH_TYPE_ENUM, TRANSPORTATION_TYPE_ENUM } from "/@/constants/business/transport-route-const";
import _ from 'lodash';
import { baiduMapApi } from '/@/api/support/baidumap/baidu-map-api';
import MapModal from '/@/views/business/order/components/map-modal.vue';
import { getCodeByAdcode } from '/@/lib/area-util.js';

//  组件
const formRef = ref();
// 校验规则
const rules = {
  transportRouteType: [{ required: true, message: '请选择运输类型' }],
  transportRouteName: [{ required: true, message: '请输入路线名称' }],
  mileage: [{ required: true, message: '请输入里程' }],
};


// form
const formDefault = {
  transportRouteId: undefined,
  transportRouteType: undefined,
  transportRouteName: undefined,
  mileage: undefined,
  disabledFlag: false,
  containerPathList: [],
  loadPathList: [],
  pathList: []
};
let form = reactive({ ...formDefault });

// 地址默认值
const pathDefault = {
  contactName: '',
  contactPhone: '',
  area: [],
  province: '',
  provinceName: '',
  city: '',
  cityName: '',
  district: '',
  districtName: '',
  address: '',
  type: undefined,
}
let path = reactive({ ...pathDefault });

// 路线状态
const enabledChecked = ref(true);
function enabledCheckedChange(checked) {
  form.disabledFlag = !checked;
}

// 是否展示
const visible = ref(false);
// 是否是编辑
const updateFlag = ref(false);

// 显示modal
function showModal(rowData, transportRouteType) {
  Object.assign(form, formDefault);
  form.transportRouteType = transportRouteType;
  if (rowData) {
    updateFlag.value = true;
    if (!_.isEmpty(rowData.pathList)) {
      form.containerPathList = rowData.pathList.filter(e => e.type == PATH_TYPE_ENUM.CONTAINER_LOCATION.value || e.type == PATH_TYPE_ENUM.RETURN_CONTAINER_LOCATION.value);
      form.loadPathList = rowData.pathList.filter(e => e.type == PATH_TYPE_ENUM.UNLOADING_LOCATION.value || e.type == PATH_TYPE_ENUM.PLACING_LOADING.value);
      nextTick(() => {
        let containerPathList = updateArea(form.containerPathList);
        let loadPathList = updateArea(form.loadPathList);
        Object.assign(form, { containerPathList, loadPathList })
      })

    }
    Object.assign(form, rowData);
    form.pathList = [];
    enabledChecked.value = !rowData.disabledFlag;
  } else {
    initPath()
  }
  console.log(form);
  visible.value = true;
}

function updateArea(pathList) {
  pathList.forEach(e => {
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
    ]
  })
  return pathList;
}

//关闭modal
function onClose() {
  Object.assign(form, formDefault);
  formRef.value.clearValidate();
  visible.value = false;
}

async function getDistance() {
  if (!form.transportRouteName) {
    message.error('请先输入运输路线名称');
    return;
  }
  let source = form.loadPathList.find(e => PATH_TYPE_ENUM.PLACING_LOADING.value == e.type);
  console.log(source);
  if (!source.area) {
    message.error('装货地址不能为空');
    return;
  }
  let dest = form.loadPathList.find(e => PATH_TYPE_ENUM.UNLOADING_LOCATION.value == e.type);
  if (!dest.area) {
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
    form.mileage = response.data;
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

// emit
const emit = defineEmits(['reloadList']);
function onSubmit() {
  console.log(form)
  formRef.value
    .validate()
    .then(async () => {
      useSpinStore().show();
      try {
        form.pathList = [...form.containerPathList, ...form.loadPathList];
        form.pathList = form.pathList.filter(e => e.provinceName);
        if (_.isEmpty(form.pathList)) {
          message.error('请输入路线信息');
          return;
        }
        if (form.transportRouteId) {
          await transportRouteApi.update(form);
        } else {
          let result = await transportRouteApi.create(form);
          form.transportRouteId = result.data;
        }
        message.success(`${form.yardId ? '修改' : '添加'}成功`);
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

// 移除地址
function removeAddress(type, index) {
  let loadPathList = form.loadPathList;
  loadPathList.splice(index, 1);
  Object.assign(form, { loadPathList });
}

// 添加地址
function addAddress(type) {
  let path = Object.assign({}, pathDefault);
  path.type = type;
  if (PATH_TYPE_ENUM.UNLOADING_LOCATION.value == type || PATH_TYPE_ENUM.PLACING_LOADING.value == type) {
    form.loadPathList.push(path);
  } else {
    form.containerPathList.push(path);
  }
}

// 运输类型变动
function transportRouteTypeChange(value) {
  formRef.value.clearValidate();
  initPath(value);

}

// 修改堆场
function yardChange(value, yard, pathType) {
  let containerPathList = form.containerPathList;
  let path = containerPathList.find(e => e.type == pathType);
  if (!path) {
    return;
  }
  Object.assign(path, yard);
  path.contactName = yard.contact;
  path.area = [
    {
      value: path.province,
      label: path.provinceName,
    },
    {
      value: path.city,
      label: path.cityName,
    },
    {
      value: path.district,
      label: path.districtName,
    },
  ]
  Object.assign(form, { containerPathList })
}

// 新增是 初始化地址
function initPath() {
  Object.assign(form, { containerPathList: [], loadPathList: [] })
  if (form.transportRouteType == TRANSPORTATION_TYPE_ENUM.CONTAINER_TRANSPORT.value) {
    addAddress(PATH_TYPE_ENUM.CONTAINER_LOCATION.value);
    addAddress(PATH_TYPE_ENUM.RETURN_CONTAINER_LOCATION.value);
    addAddress(PATH_TYPE_ENUM.PLACING_LOADING.value);
    addAddress(PATH_TYPE_ENUM.UNLOADING_LOCATION.value,);
  } else {
    addAddress(PATH_TYPE_ENUM.PLACING_LOADING.value);
    addAddress(PATH_TYPE_ENUM.UNLOADING_LOCATION.value);
  }
}

// 地址变化
function changeArea(value, selectedOptions, path) {
  if (_.isEmpty(selectedOptions)) {
    return
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

    let pathList = pathType == PATH_TYPE_ENUM.CONTAINER_LOCATION.value || pathType == PATH_TYPE_ENUM.RETURN_CONTAINER_LOCATION.value ? form.containerPathList : form.loadPathList;
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
  } catch (error) {
    console.log(error);
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
