<template>
  <a-modal :close-on-click-modal="true" v-model:open="visible" width="80%" @ok="handleOk" @cancel="visible = false"
           ok-text="确认地点" :closable="true" :destroyOnClose="true" :maskClosable="false">

    <div class="smart-margin-top10" id="baidu-map-container"></div>
    <div class="smart-margin-top10">
      {{ addressInfo.province }}{{ addressInfo.city }}{{ addressInfo.district }}{{ addressInfo.address }}
      <a-input size="mini" type="text" id="searchAddres" v-model:value="searchAddresKeywords"
               placeholder="请输入查找的地点" style="width:50%"/>
    </div>
    <template #footer>
      <a-button @click="visible = false">取消</a-button>
      <a-button type="primary" @click="handleOk">设为{{$smartEnumPlugin.getDescByValue(pathTypeEnumName, pathType) }}</a-button>
    </template>
  </a-modal>
</template>
<script setup>
import { onMounted, ref, nextTick } from 'vue';
import { message } from 'ant-design-vue';
import { PATH_TYPE_ENUM } from '/@/constants/business/transport-route-const';
import localKey from '/@/constants/local-storage-key-const';
import { localRead } from '/@/utils/local-util';

const emit = defineEmits(['confirmSelect']);

const props = defineProps({
  pathTypeEnumName: {
    type: String,
    default: 'PATH_TYPE_ENUM',
  },
});

let visible = ref(false);

let searchAddresKeywords = ref('');
let addressInfo = ref({
  // 地址信息
  longitude: '', // 经度
  latitude: '', // 纬度
  province: '', // 省
  city: '', // 市
  district: '', // 区
  address: '', // 详细地址
});

let pathType = ref('');
let pathIndex = ref(null);

function showModal (type, index, pathInfo) {
  pathType.value = type;
  pathIndex.value = index;
  visible.value = true;
  addressInfo.value = {
    // 地址信息
    longitude: '', // 经度
    latitude: '', // 纬度
    province: '', // 省
    city: '', // 市
    district: '', // 区
    address: '', // 详细地址
  };
  if (pathInfo && (pathInfo.longitude || pathInfo.provinceName)) {
    addressInfo.value = {
      // 地址信息
      longitude: pathInfo.longitude, // 经度
      latitude: pathInfo.latitude, // 纬度
      province: pathInfo.provinceName, // 省
      city: pathInfo.cityName, // 市
      district: pathInfo.districtName, // 区
      address: pathInfo.address, // 详细地址
    };
  }
  nextTick(async() => {
    try {
      let longitude = pathInfo?.longitude;
      let latitude = pathInfo?.latitude;

      if (!pathInfo?.longitude && !pathInfo?.latitude) {
        let position = await getLocation();
        console.log('position', position);
        longitude = position.coords.longitude;
        latitude = position.coords.latitude;
      }
      
      initBaiduMap(longitude, latitude);
    } catch (error) {
      console.error('获取位置失败');
      initBaiduMap(pathInfo?.longitude, pathInfo?.latitude);
    }
  });
}

defineExpose({
  showModal
});
var map = null; // 百度地图对象
// 初始化百度地图
function initBaiduMap (longitude = 116.404, latitude = 39.915) {
  /** 初始化地图Start */
  map = new BMapGL.Map('baidu-map-container'); // 创建地图实例
  var point = new BMapGL.Point(longitude, latitude); // 设置中心点坐标
  map.centerAndZoom(point, 13); // 地图初始化，同时设置地图展示级别
  map.enableScrollWheelZoom(true); //开启鼠标滚轮缩放
  map.clearOverlays(); // 移除地图上的标注
  var marker = new BMapGL.Marker({ lng: longitude, lat: latitude }); // 创建标注
  map.addOverlay(marker); // 将标注添加到地图中
  /** 初始化地图End */

  /** 点击地图创建坐标事件Start */
  // 添加地图点击事件
  map.addEventListener('click', function (e) {
    var clickpt = e.latlng; // 点击的坐标
    map.clearOverlays(); // 移除地图上的标注
    var marker = new BMapGL.Marker(clickpt); // 创建标注
    map.addOverlay(marker); // 将标注添加到地图中
    // 逆地址解析
    geocAddress(clickpt);
  });
  /** 点击地图创建坐标事件End */

  if (addressInfo.value.province && !addressInfo.value.longitude) {
    searchAddress(addressInfo.value);
  }

  /** 搜索地址Start */
      // 建立一个自动完成的对象
  var ac = new BMapGL.Autocomplete({
        input: 'searchAddres',
        location: map,
      });
  // 鼠标点击下拉列表后的事件
  ac.addEventListener('onconfirm', function (e) {
    // 搜索词
    var searchValue = e.item.value;
    map.clearOverlays(); // 移除地图上的标注
    searchAddress(searchValue);
  });
  /** 搜索地址End */
  nextTick(() => {
    setTimeout(() => {
      let mainList = document.getElementsByClassName('tangram-suggestion-main');
      for (let i = 0; i < mainList.length; i++) {
        let mainElement = mainList[i];
        mainElement.style.zIndex = 1000 + i;
      }
    },1000);
  })

  try {
    console.log('!addressInfo.value.province && !addressInfo.value.longitude', !addressInfo.value.province && !addressInfo.value.longitude);
    if(!addressInfo.value.province && !addressInfo.value.longitude) {
      console.log(new Date());
      let locationLatLon = localRead(localKey.LOCATION);
      let r = null;
      if (locationLatLon) {
        r = JSON.parse(locationLatLon);
      }
      if (r && r.point) {
        var point = new BMapGL.Point(r.point.lng, r.point.lat); // 设置中心点坐标
        map.centerAndZoom(point, 13); // 地图初始化，同时设置地图展示级别
        map.clearOverlays(); // 移除地图上的标注
        var marker = new BMapGL.Marker({ lng: r.point.lng, lat: r.point.lat }); // 创建标注
        map.addOverlay(marker); // 将标注添加到地图中
        addressInfo.value = {
          // 地址信息
          longitude: r.longitude, // 经度
          latitude: r.latitude, // 纬度
          province: r.address.province, // 省
          city: r.address.city, // 市
          district: r.address.district, // 区
          address: r.address.street, // 详细地址
        };
      }
    }
  } catch (e) {
    console.log(e);
  }

}

function searchAddress (searchValue) {
  var local = new BMapGL.LocalSearch(map, {
    //智能搜索
    onSearchComplete: function (results) {
      let poi = results.getPoi(0); //获取第一个智能搜索的结果
      var searchpt = poi.point; // 获取坐标
      map.centerAndZoom(searchpt, 16);
      map.addOverlay(new BMapGL.Marker(searchpt)); //添加标注
      geocAddress(searchpt); // 逆地址解析
    },
  });
  local.search(
      (searchValue.province || '') +
      (searchValue.city || '') +
      (searchValue.district || '') +
      (searchValue.street || searchValue.address || '') +
      (searchValue.business || '')
  );
}

/** 逆向解析地址 point */
function geocAddress (point) {
  var geoc = new BMapGL.Geocoder();
  geoc.getLocation(point, function (geocInfo) {
    // 设置基本信息
    var addressComponents = geocInfo.addressComponents;
    addressInfo.value.longitude = point.lng;
    addressInfo.value.latitude = point.lat;
    addressInfo.value.province = addressComponents.province;
    addressInfo.value.city = addressComponents.city;
    addressInfo.value.district = addressComponents.district;
    let address = addressComponents.street + addressComponents.streetNumber;
    if (geocInfo.surroundingPois.length > 0) {
      address = address + geocInfo.surroundingPois[0].title;
    }
    addressInfo.value.address = address;
  });
}


/**
 * 确认选择
 */
function handleOk () {
  if (!addressInfo.value.latitude) {
    message.error('请选择地址');
    return;
  }
  emit('confirmSelect', addressInfo.value, pathType.value, pathIndex.value);
  searchAddresKeywords.value = '';
  visible.value = false;
}

async function getMapShowFlag () {

}



function getLocation() {
  return new Promise(function(resolve, reject) {
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(resolve, reject);
    } else {
      reject("Geolocation不支持该浏览器");
    }
  });
}

</script>

<style scoped>
#baidu-map-container {
  height: 360px;
  width: 100%;
}

:deep(.tangram-suggestion-main) {
  z-index: 2000
}

</style>
