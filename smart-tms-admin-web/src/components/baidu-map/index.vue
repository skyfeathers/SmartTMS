<template>
  <div id="container" :style="{'width':width,'height':height}"></div>
</template>
<script setup>
import {nextTick, onMounted, reactive, ref, watch} from "vue";
import _ from 'lodash';
import {WARN_TYPE_ENUM} from '/@/constants/business/warn-const';
import appMarkerForm from '/@/assets/images/map/app-marker-form.png'
import appMarkerTo from '/@/assets/images/map/app-marker-to.png'
import appMapPoint from '/@/assets/images/map/app-map-point.png'
import warnPoint from '/@/assets/images/map/icon-esc-yc-caozuodidian.png'
import appMarkerWay from '/@/assets/images/map/app-marker-way.png'
import carMarkerForm from '/@/assets/images/map/car-marker-form.png'
import carMarkerTo from '/@/assets/images/map/car-marker-to.png'
import carMapPoint from '/@/assets/images/map/car-map-point.png'
import carMarkerWay from '/@/assets/images/map/car-marker-way.png'
import {message} from "ant-design-vue";
import {WAYBILL_EXCEPTION_TYPE_ENUM} from "/@/constants/business/waybill-const";
// ----------------------- 以下是公用变量 emits props ----------------
let props = defineProps({
  appPosition: Array,// 司机app轨迹
  sinoiov: Array, //车辆中交轨迹
  wayBillTimeWarnList:Array, //车辆报警点
  exceptionList:Array, //车辆报警点
  // 轨迹是否已经结束了
  finishFlag: {
    type: Boolean,
    default: false
  },
  width: {
    type: String,
    default: '100%'
  },
  height: {
    type: String,
    default: '850px'
  },
  waybillId: {
    type: Number
  }
});
// ----------------------- 展开自定义组合式函数 -----------------------

// ----------------------- 以下是暴露的方法内容 -----------------------
defineExpose({
  initPosition,
});
// ----------------------- 以下是生命周期 ----------------------------
onMounted(() => {
  // 初始化地图
  initMap();
  // 初始化轨迹
  // initPosition();
})
// watch([() => props.appPosition, () => props.sinoiov,() => props.wayBillTimeWarnList], () => {
//   //initPosition();
// })

// ----------------------- 以下是业务内容 ----------------------------
let optsDefault = reactive({
  width: 420,     // 信息窗口宽度
  height: 150,    // 信息窗口高度
});
let map = null;
let zoom = ref(8);

/**
 * 初始化地图
 */
function initMap() {
  map = new BMapGL.Map("container");          // 创建地图实例
  let centerPoint = new BMapGL.Point(116.404, 39.915);  // 创建点坐标
  map.centerAndZoom(centerPoint, zoom.value);   // 初始化地图，设置中心点坐标和地图级别
  map.enableScrollWheelZoom(true);     //开启鼠标滚轮缩放
}

/**
 * 初始化app轨迹
 */
async function initPosition() {
  // 清除覆盖物
  map.clearOverlays();
  // 初始化中交车辆轨迹
  await initSinoiov();
  // 初始化app轨迹
  initAppPosition();
  initWarn();
}

function getInfo(exceptionType) {
  let appPosition = null
  let titleInfo = "";
  if (WAYBILL_EXCEPTION_TYPE_ENUM.LOAD_EXCEPTION.value == exceptionType) {
    appPosition = props.appPosition[0];
    titleInfo = WAYBILL_EXCEPTION_TYPE_ENUM.LOAD_EXCEPTION.desc;
  }
  if (WAYBILL_EXCEPTION_TYPE_ENUM.UNLOAD_EXCEPTION.value == exceptionType) {
    appPosition = props.appPosition[props.appPosition.length - 1];
    titleInfo = WAYBILL_EXCEPTION_TYPE_ENUM.UNLOAD_EXCEPTION.desc;
  }
  if (!appPosition) {
    return;
  }
  var point = new BMapGL.Point(appPosition.longitude, appPosition.latitude);
  let opts = {
    ...optsDefault,
    title: titleInfo  // 信息窗口标题
  }
  let infoWindow = new BMapGL.InfoWindow(getAppInfoWindowContent(appPosition, exceptionType), opts);  // 创建信息窗口对象
  map.openInfoWindow(infoWindow, point);
}

/**
 * 初始化app轨迹
 */

function initAppPosition() {
  // app轨迹
  if (_.isEmpty(props.appPosition)) {
    return;
  }
  let waybillId = props.appPosition[0].waybillId
  // 创建轨迹、中间点
  let appMiddleIcon = new BMapGL.Icon(appMapPoint, new BMapGL.Size(10, 10));
  let appPolylineList = props.appPosition.map((e, i) => {
    let appMiddlePoint = new BMapGL.Point(e.longitude, e.latitude);
    // 第一点与最后一点不设置圆点图标
    if (i === 0 || i === props.appPosition.length - 1) {
      return appMiddlePoint;
    }
    let appMiddleMarker = new BMapGL.Marker(appMiddlePoint, {icon: appMiddleIcon});
    appMiddleMarker.addEventListener("click", function () {
      let opts = {
        ...optsDefault,
        title: "司机中间位置"  // 信息窗口标题
      }
      let infoWindow = new BMapGL.InfoWindow(getAppInfoWindowContent(e), opts);  // 创建信息窗口对象
      map.openInfoWindow(infoWindow, appMiddlePoint);
    });
    if (waybillId == props.waybillId) {
      map.addOverlay(appMiddleMarker);
    }
    return appMiddlePoint;
  });
  // 创建轨迹
  // console.log('app')
  // console.log(appPolylineList)
  // console.log('app')
  let appPolyline = new BMapGL.Polyline(appPolylineList, {strokeColor: "#f96f2c", strokeWeight: 4, strokeOpacity: 1});
  if (waybillId == props.waybillId) {
    map.addOverlay(appPolyline);
  }
  nextTick(() => {
    // 地图增加起始点
    let appStartPosition = props.appPosition[0];
    let appStartPoint = new BMapGL.Point(appStartPosition.longitude, appStartPosition.latitude)
    // 有中交轨迹则以中交第一个点为中间点
    if (_.isEmpty(props.sinoiov)) {
      // 初始化地图中心点坐标和地图级别
      map.centerAndZoom(appStartPoint, zoom.value);
    }
    let appStartIcon = new BMapGL.Icon(appMarkerForm, new BMapGL.Size(25, 25));
    let appStartMarker = new BMapGL.Marker(appStartPoint, {icon: appStartIcon});
    appStartMarker.addEventListener("click", function () {
      let opts = {
        ...optsDefault,
        title: "司机开始位置"  // 信息窗口标题
      }
      let infoWindow = new BMapGL.InfoWindow(getAppInfoWindowContent(appStartPosition, WAYBILL_EXCEPTION_TYPE_ENUM.LOAD_EXCEPTION.value), opts);  // 创建信息窗口对象
      map.openInfoWindow(infoWindow, appStartPoint);
    });
    if (waybillId == props.waybillId) {
      map.addOverlay(appStartMarker);
    }
    // 地图增加结束点
    let appEndPosition = props.appPosition[props.appPosition.length - 1];
    let appEndPoint = new BMapGL.Point(appEndPosition.longitude, appEndPosition.latitude)
    let appEndIcon = new BMapGL.Icon(props.finishFlag ? appMarkerTo : appMarkerWay, new BMapGL.Size(25, 25));
    let appEndMarker = new BMapGL.Marker(appEndPoint, {icon: appEndIcon});
    appEndMarker.addEventListener("click", function () {
      let opts = {
        ...optsDefault,
        title: `司机${props.finishFlag ? '结束' : '当前'}位置`  // 信息窗口标题
      }
      let infoWindow = new BMapGL.InfoWindow(getAppInfoWindowContent(appEndPosition, WAYBILL_EXCEPTION_TYPE_ENUM.UNLOAD_EXCEPTION.value), opts);  // 创建信息窗口对象
      map.openInfoWindow(infoWindow, appEndPoint);
    });
    if (waybillId == props.waybillId) {
      map.addOverlay(appEndMarker);
    }
  })
}

/**
 * 初始化中交车辆轨迹
 */
async function initSinoiov() {
  // 中交车辆轨迹
  if (_.isEmpty(props.sinoiov)) {
    return;
  }
  // WGS84坐标轨迹点 批量转为BD09 单次只能转10条
  let count = props.sinoiov.length;
  let pages = Math.ceil(count / 10); //向上取整
  // 分页处理
  let convertor = new BMapGL.Convertor();
  // 总转换数据
  let translatePointList = [];
  for (let i = 0; i < pages; i++) {
    let WGS84PointList = props.sinoiov.slice(i * 10, (i + 1) * 10);
    let baiduPointList = await translate(convertor, WGS84PointList);
    translatePointList.push(...baiduPointList);
  }
  let waybillId = translatePointList[0].waybillId;
  // console.log(translatePointList)
  // 创建轨迹、中间点
  let carMiddleIcon = new BMapGL.Icon(carMapPoint, new BMapGL.Size(10, 10));
  let carPolylineList = translatePointList.map((e, i) => {
    let carMiddlePoint = new BMapGL.Point(e.lon, e.lat);
    // 第一点与最后一点不设置圆点图标
    if (i === 0 || i === translatePointList.length - 1) {
      return carMiddlePoint;
    }
    let carMiddleMarker = new BMapGL.Marker(carMiddlePoint, {icon: carMiddleIcon});
    carMiddleMarker.addEventListener("click", function () {
      let opts = {
        ...optsDefault,
        title: "车辆中间位置"  // 信息窗口标题
      }
      let infoWindow = new BMapGL.InfoWindow(getCarInfoWindowContent(e), opts);  // 创建信息窗口对象
      map.openInfoWindow(infoWindow, carMiddlePoint);
    });
    if (waybillId == props.waybillId) {
      map.addOverlay(carMiddleMarker);
    }
    return carMiddlePoint;
  });


  // 创建轨迹
  let carPolyline = new BMapGL.Polyline(carPolylineList, {strokeColor: "#357df7", strokeWeight: 4, strokeOpacity: 1});
  if (waybillId == props.waybillId) {
    map.addOverlay(carPolyline);
  }
  nextTick(() => {
    // 地图增加起始点
    let carStartPosition = translatePointList[0];
    let carStartPoint = new BMapGL.Point(carStartPosition.lon, carStartPosition.lat);
    map.centerAndZoom(carStartPoint, zoom.value);
    let carStartIcon = new BMapGL.Icon(carMarkerForm, new BMapGL.Size(25, 25));
    let carStartMarker = new BMapGL.Marker(carStartPoint, {icon: carStartIcon});
    carStartMarker.addEventListener("click", function () {
      let opts = {
        ...optsDefault,
        title: "车辆开始位置"  // 信息窗口标题
      }
      let infoWindow = new BMapGL.InfoWindow(getCarInfoWindowContent(carStartPosition), opts);  // 创建信息窗口对象
      map.openInfoWindow(infoWindow, carStartPoint);
    });
    if (waybillId == props.waybillId) {
      map.addOverlay(carStartMarker);
    }
    // 地图增加结束点
    let carEndPosition = translatePointList[translatePointList.length - 1];
    let carEndPoint = new BMapGL.Point(carEndPosition.lon, carEndPosition.lat);
    let carEndIcon = new BMapGL.Icon(props.finishFlag ? carMarkerTo : carMarkerWay, new BMapGL.Size(25, 25));
    let carEndMarker = new BMapGL.Marker(carEndPoint, {icon: carEndIcon});
    carEndMarker.addEventListener("click", function () {
      let opts = {
        ...optsDefault,
        title: `车辆${props.finishFlag ? '结束' : '当前'}位置`  // 信息窗口标题
      }
      let infoWindow = new BMapGL.InfoWindow(getCarInfoWindowContent(carEndPosition), opts);  // 创建信息窗口对象
      map.openInfoWindow(infoWindow, carEndPoint);
    });
    if (waybillId == props.waybillId) {
      map.addOverlay(carEndMarker);
    }
  })
}
/**
 * 初始化预警点
 */
//  watch(props.wayBillTimeWarnList, () => {
//   // initWarn();
// })
 async function initWarn() {

  if (_.isEmpty(props.wayBillTimeWarnList)) {
    return;
  }

 // 创建报警中间点
  let carWarnIcon = new BMapGL.Icon(warnPoint, new BMapGL.Size(40, 40)
    , {
      anchor: new BMapGL.Size(20, 36)
  });
    let carWarnPointList = props.wayBillTimeWarnList.map((e, i) => {
      let carWarnPoint = new BMapGL.Point(e.lon, e.lat);
      let carWarnPointMarker = new BMapGL.Marker(carWarnPoint, {icon: carWarnIcon});
      carWarnPointMarker.addEventListener("click", function () {
        let opts = {
          ...optsDefault,
          title: "车辆报警位置"  // 信息窗口标题
        }
        let infoWindow = new BMapGL.InfoWindow(getWarnInfoWindowContent(e), opts);  // 创建信息窗口对象
        map.openInfoWindow(infoWindow, carWarnPoint);
      });
      let waybillId = props.wayBillTimeWarnList[0].waybillId;
      if (waybillId == props.waybillId) {
        map.addOverlay(carWarnPointMarker);
      }
      return carWarnPoint;
    });
 }


/**
 * 批量转换
 * @param convertor
 * @param sinoiovList
 * @returns {Promise<unknown>}
 */
function translate(convertor, sinoiovList) {
  return new Promise((resolve, reject) => {
    let copySinoiovList = _.cloneDeep(sinoiovList);
    let WGS84PointList = copySinoiovList.map(e => new BMapGL.Point(e.lon, e.lat));
    /**
     * 坐标常量说明：
     * COORDINATES_WGS84 = 1, WGS84坐标
     * COORDINATES_WGS84_MC = 2, WGS84的平面墨卡托坐标
     * COORDINATES_GCJ02 = 3，GCJ02坐标
     * COORDINATES_GCJ02_MC = 4, GCJ02的平面墨卡托坐标
     * COORDINATES_BD09 = 5, 百度bd09经纬度坐标
     * COORDINATES_BD09_MC = 6，百度bd09墨卡托坐标
     * COORDINATES_MAPBAR = 7，mapbar地图坐标
     * COORDINATES_51 = 8，51地图坐标
     */
    convertor.translate(WGS84PointList, 1, 5, (data) => {
      if (data.status !== 0) {
        //message.error("中交车辆轨迹转换错误，请联系管理员");
        resolve(copySinoiovList);
      }
      copySinoiovList.forEach((e, i) => {
        let point = data.points[i];
        e.lon = point.lng;
        e.lat = point.lat;
      })
      resolve(copySinoiovList);
    })
  })
}

/**
 * 生成app提示窗口文案
 * @param appPosition
 * @returns {string}
 */
function getAppInfoWindowContent(appPosition, exceptionType) {
  let exceptionMsg = "";
  if (!_.isEmpty(props.exceptionList)) {
    let exception = props.exceptionList.find(e=>e.exceptionType == exceptionType);
    if (exception) {
      exceptionMsg = exception.exceptionMessage;
    }
  }
  return `<div><span>经度：${appPosition.longitude}，纬度：${appPosition.latitude}</span><br/><span>地址：${appPosition.address}</span><br/><span>时间：${appPosition.uploadTime}</span><br/><span>异常：${exceptionMsg}</span></div>`;
}

/**
 * 生成车辆提示窗口文案
 * @param appPosition
 * @returns {string}
 */
function getCarInfoWindowContent(carPosition) {
  return `<div><span>经度：${carPosition.lon}，纬度：${carPosition.lat}</span><br/><span>时间：${carPosition.gtm}</span></div>`;
}

/**
 * 生成车辆提示窗口文案
 * @param wayBillInfo
 * @returns {string}
 */
function getWarnInfoWindowContent(wayBillInfo) {
  if (wayBillInfo.warnType == WARN_TYPE_ENUM.SPEEDING.value) {
    wayBillInfo.warnType=WARN_TYPE_ENUM.SPEEDING.desc
  }else if (wayBillInfo.warnType == WARN_TYPE_ENUM.FATIGUE.value) {
    wayBillInfo.warnType=WARN_TYPE_ENUM.FATIGUE.desc
  }else if (wayBillInfo.warnType == WARN_TYPE_ENUM.VEHICLE_OFFLINE.value) {
    wayBillInfo.warnType=WARN_TYPE_ENUM.VEHICLE_OFFLINE.desc
  }else if (wayBillInfo.warnType == WARN_TYPE_ENUM.PARKING_VIOLATION.value) {
    wayBillInfo.warnType=WARN_TYPE_ENUM.PARKING_VIOLATION.desc
  }else if (wayBillInfo.warnType == WARN_TYPE_ENUM.LOW_SPEED.value) {
    wayBillInfo.warnType=WARN_TYPE_ENUM.LOW_SPEED.desc
  }
  return `<div>
        <p>${wayBillInfo.driverName}-${wayBillInfo.telephone}-${wayBillInfo.vehicleNumber}</p>
        <p>报警类型：${ wayBillInfo.warnType}/报警等级：${wayBillInfo.warnLevel}</p>
        <span>${wayBillInfo.sendTime}</span>
        </div>`;
}
</script>
<style lang='less' scoped>
#container {
  position: relative;
}
ul li {
  list-style: none;
}

.exception-wrap {
  z-index: 999;
  position: absolute;
  top: 50px;
  right: 10px;
  display: flex;
  .item {
    margin-left: 10px;
    width: 180px;
    height: 50px;
    padding: 1rem 1rem;
    border-radius: .25rem;
    background-color: #fff;
    box-shadow: 0 2px 6px 0 rgba(27, 142, 236, 0.5);
  }
}
</style>
