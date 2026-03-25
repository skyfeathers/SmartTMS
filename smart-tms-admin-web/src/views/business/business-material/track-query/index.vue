<template>
  <a-form class="smart-query-form">
    <a-row class="smart-query-form-row">
      <a-form-item label="车牌号" class="smart-query-form-item">
        <a-input style="width: 120px" v-model:value.trim="queryForm.vehicleNumber" placeholder="车牌号" />
      </a-form-item>

      <a-form-item label="车牌颜色" class="smart-query-form-item">
        <SmartEnumSelect width="100px" v-model:value="queryForm.vehiclePlateColorCode" placeholder="车牌颜色" enum-name="VEHICLE_PLATE_COLOR_ENUM" />
      </a-form-item>
      <a-form-item label="运输时间" class="smart-query-form-item">
        <a-range-picker
          @change="changeTrackDay"
          v-model:value="trackDayRange"
          :presets="defaultTimeRanges"
          show-time
          format="YYYY-MM-DD HH:mm:ss"
        />
      </a-form-item>
    </a-row>

    <a-row>
      <a-form-item class="smart-query-form-item smart-margin-left10">
        <a-button type="primary" @click="search">
          <template #icon>
            <SearchOutlined />
          </template>
          查询
        </a-button>
        <a-button @click="resetQuery">
          <template #icon>
            <ReloadOutlined />
          </template>
          重置
        </a-button>
      </a-form-item>
    </a-row>
  </a-form>
  <a-card size="small">
    <BaiduMap ref="mapRef" :sinoiov="sinoiov" height="440px"/>
  </a-card>
</template>
<script setup>
import { reactive, ref, nextTick } from 'vue';
import SmartEnumSelect from '/@/components/smart-enum-select/index.vue';
import { defaultTimeRanges } from '/@/lib/default-time-ranges';
// import TkMap from '/@/components/tk-map/index.vue';
import BaiduMap from '/@/components/baidu-map/index.vue';
import { SmartLoading } from '/@/components/smart-loading';
import {waybillApi} from "/@/api/business/waybill/waybill-api";

const queryFormState = {
  vehicleNumber: null,
  vehiclePlateColorCode:null,
  //运输时间-开始时间
  startTime: null,
  //运输时间-结束时间
  endTime: null,
}

const queryForm = reactive({ ...queryFormState });

// 运输时间
const trackDayRange=ref()
function changeTrackDay(dates, dateStrings) {
  queryForm.startTime = dateStrings[0];
  queryForm.endTime = dateStrings[1];
}
function search(){
  queryVehicleSinoiov()
}
function resetQuery() {
  Object.assign(queryForm, queryFormState);
  trackDayRange.value=[]
}

// ----------------- 轨迹 --------------------
let sinoiov = ref([]);
const mapRef = ref();
async function queryVehicleSinoiov() {
  SmartLoading.show();
  try {
    let result = await waybillApi.queryVehicleSinoiov(queryForm);
    sinoiov.value = result.data;
    nextTick(() => {
      mapRef.value.initPosition();
    });
  } catch (error) {
    console.log('error', error);
  } finally {
    SmartLoading.hide();
  }
}
</script>
