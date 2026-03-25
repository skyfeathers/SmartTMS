<template>
  <div>
    <a-typography-title :level="5" class="smart-margin-top10">身份证信息</a-typography-title>
    <a-descriptions :column="3" size="small">
      <a-descriptions-item label="司机姓名">{{ detail.driverName }}</a-descriptions-item>
      <a-descriptions-item label="身份证号码">{{ detail.drivingLicense }}<SmartCopyIcon v-if="detail.drivingLicense" :value="detail.drivingLicense" /></a-descriptions-item>
      <a-descriptions-item label="身份证地址">{{ detail.homeAddress }}</a-descriptions-item>
      <a-descriptions-item label="身份证有效期开始时间">{{ detail.idCardEffectiveStartDate }}</a-descriptions-item>
      <a-descriptions-item label="身份证有效期结束日期">{{ detail.idCardEndlessFlag ? '长期有效' :detail.idCardEffectiveEndDate }}</a-descriptions-item>
      <a-descriptions-item label="正面图">
        <file-preview type="picture" :fileList="detail.idCardFrontPic"/>
      </a-descriptions-item>
      <a-descriptions-item label="反面图">
        <file-preview type="picture" :fileList="detail.idCardBackPic"/>
      </a-descriptions-item>
    </a-descriptions>
    <a-typography-title :level="5" class="smart-margin-top10">驾驶证信息</a-typography-title>
    <a-descriptions :column="3" size="small">
      <a-descriptions-item label="驾驶证号">{{ detail.drivingLicenseNo }}</a-descriptions-item>
      <a-descriptions-item label="准驾车型">{{  $smartEnumPlugin.getDescByValue('VEHICLE_CLASS_ENUM', detail.vehicleClass) }}</a-descriptions-item>
      <a-descriptions-item label="发证机关">{{ detail.issuingOrganizations }}</a-descriptions-item>
      <a-descriptions-item label="首次驾照签发日期">{{ detail.licenseFirstGetDate }}</a-descriptions-item>
      <a-descriptions-item label="驾驶证有效期开始时间">{{ detail.validPeriodFrom }}</a-descriptions-item>
      <a-descriptions-item label="驾驶证有效期结束时间">{{ detail.drivingLicenseEndlessFlag ? '长期有效' : detail.validPeriodTo }}</a-descriptions-item>
      <a-descriptions-item label="驾驶证正本图">
        <file-preview type="picture" :fileList="detail.drivingLicenseFrontPic"/>
      </a-descriptions-item>
      <a-descriptions-item label="驾驶证副本图">
        <file-preview type="picture" :fileList="detail.drivingLicenseBackPic"/>
      </a-descriptions-item>
    </a-descriptions>
    <a-typography-title :level="5" class="smart-margin-top10">从业资格证信息</a-typography-title>
    <a-descriptions :column="3" size="small">
      <a-descriptions-item label="从业资格证号">{{ detail.qualificationCertificate }}</a-descriptions-item>
      <a-descriptions-item label="从业资格证有效期开始日期">{{ detail.qualificationCertificateStartDate }}</a-descriptions-item>
      <a-descriptions-item label="从业资格证有效期结束日期">{{ detail.qualificationCertificateEndDate }}</a-descriptions-item>
      <a-descriptions-item label="从业资格证件图">
        <file-preview type="picture" :fileList="detail.qualificationCertificatePic"/>
      </a-descriptions-item>
    </a-descriptions>
    <a-typography-title :level="5" class="smart-margin-top10">税务登记人</a-typography-title>
    <a-descriptions :column="3" size="small">
      <a-descriptions-item label="姓名">{{ detail.taxRegister?.name }}</a-descriptions-item>
      <a-descriptions-item label="纳税人身份证号">{{ detail.taxRegister?.idCard }}</a-descriptions-item>
      <a-descriptions-item label="纳税人手机号">{{ detail.taxRegister?.address }}</a-descriptions-item>
      <a-descriptions-item label="纳税人地址">{{ detail.taxRegister?.address }}</a-descriptions-item>
      <a-descriptions-item label="纳税人身份证正面">
        <file-preview type="picture" :fileList="detail.taxRegister?.idCardFrontAttachment"/>
      </a-descriptions-item>
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
