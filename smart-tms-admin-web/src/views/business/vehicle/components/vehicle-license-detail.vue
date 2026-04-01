<!--
 * @Description:
 * @Author: zhuoda
 * @Date: 2022-07-11
 * @LastEditTime: 2022-07-12
 * @LastEditors: zhuoda
-->
<template>
  <div>
    <a-typography-title :level="5" class="smart-margin-top10">行驶证信息</a-typography-title>
    <a-descriptions :column="4" size="small">
      <a-descriptions-item label="行驶证正本">
        <file-preview type="picture" :fileList="detail.drivingLicenseAttachment" />
      </a-descriptions-item>
      <a-descriptions-item label="车牌号">{{ detail.vehicleNumber }}</a-descriptions-item>
      <a-descriptions-item label="车牌颜色">
        {{ $smartEnumPlugin.getDescByValue('VEHICLE_PLATE_COLOR_ENUM', detail.vehiclePlateColorCode) }}
      </a-descriptions-item>
      <a-descriptions-item label="车辆类型">{{vehicleTypeDesc}}
      </a-descriptions-item>
      <a-descriptions-item label="机动车登记证书编号">{{ detail.vehicleRegistrationCertificateNo }}</a-descriptions-item>
      <a-descriptions-item label="车辆识别代码(车架号)">{{ detail.vin }}</a-descriptions-item>
      <a-descriptions-item label="发动机号">{{ detail.engineNumber }}</a-descriptions-item>
      <a-descriptions-item label="品牌型号">{{ detail.model }}</a-descriptions-item>
      <a-descriptions-item label="所属人">{{ detail.owner }}</a-descriptions-item>
      <a-descriptions-item label="使用性质">{{ detail.nature }}</a-descriptions-item>
<!--      <a-descriptions-item label="所属人性质">
        {{ $smartEnumPlugin.getDescByValue('VEHICLE_OWNER_TYPE_ENUM', detail.ownerType) }}
      </a-descriptions-item>
      <a-descriptions-item label="车辆行驶证档案编号">{{ detail.licenseNo }}</a-descriptions-item>-->
      <a-descriptions-item label="发证机关">{{ detail.issuingOrganizations }}</a-descriptions-item>
      <a-descriptions-item label="注册日期">{{ detail.registerDate }}</a-descriptions-item>
      <a-descriptions-item label="发证日期">{{ detail.issueDate }}</a-descriptions-item>
       <a-descriptions-item label="车辆审验到期时间">{{ detail.vehicleAuditExpireDate }}</a-descriptions-item>
<!--      <a-descriptions-item label="到期时间">{{ detail.expireDate }}</a-descriptions-item>-->
      <a-descriptions-item label="行驶证副本">
        <file-preview type="picture" :fileList="detail.drivingLicenseEctypeAttachment" />
      </a-descriptions-item>
      <a-descriptions-item label="总质量(kg)">{{ detail.grossMass }}</a-descriptions-item>
      <a-descriptions-item label="核定载质量(载重kg)">{{ detail.vehicleTonnage }}</a-descriptions-item>
      <a-descriptions-item label="外廓尺寸">{{ detail.gabarite }}</a-descriptions-item>
      <a-descriptions-item label="能源类型">{{ detail.vehicleEnergyTypeName }}</a-descriptions-item>
    </a-descriptions>
    <a-divider />
    <a-typography-title :level="5" class="smart-margin-top10">道路运输证</a-typography-title>
    <a-descriptions :column="4" size="small">
      <a-descriptions-item label="道路运输证">
        <file-preview type="picture" :fileList="detail.roadTransportCertificateAttachment" />
      </a-descriptions-item>
      <a-descriptions-item label="道路运输证号">{{ detail.roadTransportCertificateNumber }}</a-descriptions-item>
      <a-descriptions-item label="道路运输经营许可证">{{ detail.roadTransportBusinessLicenseNumber }}</a-descriptions-item>
      <a-descriptions-item label="道运证有效期开始日期">{{ detail.roadTransportCertificateStartDate }}</a-descriptions-item>
      <a-descriptions-item label="道运证有效期结束日期">{{ detail.roadTransportCertificateExpireDate }}</a-descriptions-item>
    </a-descriptions>
    <a-divider />
    <a-typography-title :level="5" class="smart-margin-top10">挂靠信息</a-typography-title>
    <a-descriptions :column="2" size="small">
      <a-descriptions-item label="挂靠企业协议">
<!--        <file-preview :fileList="detail.relyEnterpriseAttachment" />-->
        <Upload :showUploadBtn="false"
            :default-file-list="detail.relyEnterpriseAttachment"
            listType="picture-card"
        />
      </a-descriptions-item>
      <a-descriptions-item label="挂靠协议到期时间">{{ detail.relyEnterpriseExpireDate }}</a-descriptions-item>
<!--      <a-descriptions-item label="挂靠企业营业执照">
        <file-preview :fileList="detail.relyEnterpriseBusinessLicenseAttachment" />
      </a-descriptions-item>
      <a-descriptions-item label="挂靠企业名称">{{ detail.relyEnterpriseName }}</a-descriptions-item>
      <a-descriptions-item label="统一社会信用代码">{{ detail.relyEnterpriseUnifiedSocialCreditCode }}</a-descriptions-item>
      <a-descriptions-item label="道路运输经营许可证">
        <file-preview :fileList="detail.relyEnterpriseRoadTransportBusinessLicenseAttachment" />
      </a-descriptions-item>
      <a-descriptions-item label="道路运输经营许可证号">{{ detail.relyEnterpriseRoadTransportBusinessLicenseNumber }}</a-descriptions-item>
      <a-descriptions-item label="发证日期">{{ detail.relyEnterpriseRoadTransportBusinessLicenseIssueDate }}</a-descriptions-item>
      <a-descriptions-item label="结束日期">{{ detail.relyEnterpriseRoadTransportBusinessLicenseExpireDate }}</a-descriptions-item>-->
    </a-descriptions>
    <a-divider />
    <a-typography-title :level="5" class="smart-margin-top10">车主信息</a-typography-title>
    <a-descriptions :column="2" size="small">
      <a-descriptions-item label="车主">
        {{detail.receiveUserName}}
      </a-descriptions-item>
      <a-descriptions-item label="车主银行">
        {{detail.receiveBankName}}
      </a-descriptions-item>
      <a-descriptions-item label="车主银行账号">
        {{detail.receiveBankNo}}
      </a-descriptions-item>
    </a-descriptions>
  </div>
</template>
<script setup>
import FilePreview from '/@/components/file-preview/index.vue';
import Upload from '/@/components/upload/index.vue';

import { computed } from 'vue';
import _ from "lodash"
const props = defineProps({
  detail: {
    type: Object,
    default: () => {
      return {};
    },
  },
});

const vehicleTypeDesc = computed(() => {
  if(_.isEmpty(props.detail.vehicleType)) {
    return '';
  }
  return props.detail.vehicleType[0].valueName
});
// 有效期
</script>
<style lang="less" scoped>
:deep(.ant-divider-horizontal) {
  margin: 0;
}
</style>
