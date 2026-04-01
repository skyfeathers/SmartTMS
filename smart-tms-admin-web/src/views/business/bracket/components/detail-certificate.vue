<!--
 * @Date: 2022-07-17 17:30:41
 * @LastEditors: lp
 * @LastEditTime: 2022-07-17 18:27:32
 * @FilePath: \nft-admin-web\src\views\business\bracket\components\detail-certificate.vue
-->
<template>
  <div>
    <a-descriptions :column="3" size="small">
      <a-descriptions-item label="行驶证正本">
        <file-preview type="picture" :fileList="detail.drivingLicensePic"/>
      </a-descriptions-item>
      <a-descriptions-item label="行驶证副本">
        <file-preview type="picture" :fileList="detail.drivingLicenseEctypePic"/>
      </a-descriptions-item>
      <a-descriptions-item label="挂车车牌号">{{ detail.bracketNo }}</a-descriptions-item>
      <a-descriptions-item label="实际所属人">{{ detail.owner }}</a-descriptions-item>
      <a-descriptions-item label="车辆类型">{{ detail.type }}</a-descriptions-item>
      <a-descriptions-item label="车辆识别代号">{{ detail.vin }}</a-descriptions-item>
      <a-descriptions-item label="车牌颜色">{{  $smartEnumPlugin.getDescByValue('VEHICLE_PLATE_COLOR_ENUM', detail.plateColorCode) }}</a-descriptions-item>
      <a-descriptions-item label="载重（kg）">{{ detail.tonnage }}</a-descriptions-item>
      <a-descriptions-item label="重量（kg）">{{ detail.weight }}</a-descriptions-item>
      <a-descriptions-item label="注册日期">{{ detail.registerTime }}</a-descriptions-item>
      <a-descriptions-item label="发证日期">{{ detail.issueTime }}</a-descriptions-item>
    </a-descriptions>
  </div>
</template>
<script setup>
import FilePreview from '/@/components/file-preview/index.vue'
const props = defineProps({
  detail: {
    type: Object,
    default: () => {
      return {};
    }
  },
});
</script>
<style lang='less' scoped></style>
