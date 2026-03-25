<template>
  <div class="content">
    <div class="main">
      <a-descriptions :column="3" size="small">
        <a-descriptions-item label="货主简称">{{ props.detail?.shortName }}<SmartCopyIcon v-if="props.detail?.shortName" :value="props.detail.shortName" /></a-descriptions-item>
        <a-descriptions-item label="是否需要开票">{{ props.detail?.makeInvoiceFlag?'需要':'不需要' }}</a-descriptions-item>
        <a-descriptions-item v-if="props.detail?.makeInvoiceFlag" label="税点(%)">{{ props.detail?.taxPoint }}</a-descriptions-item>
        <a-descriptions-item>
          所在地区： {{ getArea(props.detail?.area) }}
        </a-descriptions-item>
<!--        <a-descriptions-item>
          是否扣税：
          {{ props.detail?.deductTaxFlag ? '是' : '否'}}
        </a-descriptions-item>-->

        <a-descriptions-item>
          货主类型：
          {{
            $smartEnumPlugin.getDescByValue(
                'SHIPPER_NATURE_ENUM',
                props.detail?.shipperNature
            )
          }}
        </a-descriptions-item>
        <a-descriptions-item>
          标签：
          {{
            $smartEnumPlugin.getDescByValue('SHIPPER_TAG_ENUM', props.detail?.tagType)
          }}
        </a-descriptions-item>
<!--        <a-descriptions-item> 扣税比例： {{ props.detail?.deductTaxRatio }}</a-descriptions-item>-->

        <a-descriptions-item>
          等级：
          {{
            $smartEnumPlugin.getDescByValue('SHIPPER_GRADE_ENUM', props.detail?.grade)
          }}
        </a-descriptions-item>
        <a-descriptions-item> 所属公司： {{ props.detail?.enterpriseName }}</a-descriptions-item>
        <a-descriptions-item> 业务关系： {{ getShipperTypeDesc() }}</a-descriptions-item>
        <a-descriptions-item> 业务负责人： {{ getManagerName() }}</a-descriptions-item>

        <a-descriptions-item> 客服负责人： {{ getCustomerService() }}</a-descriptions-item>
        <a-descriptions-item> 账期(天)： {{ props.detail?.accountPeriod }}</a-descriptions-item>
        <a-descriptions-item> 营业执照编号： {{ props.detail?.consignorId }}</a-descriptions-item>
        <a-descriptions-item> 税点： {{ props.detail?.taxPoint }}</a-descriptions-item>
        <a-descriptions-item> 备注： {{ props.detail?.remark }}</a-descriptions-item>
        <a-descriptions-item> 营业执照：
          <file-preview type="picture" name="营业执照"
                        :fileList="props.detail?.consignorImage"/>
        </a-descriptions-item>
        <a-descriptions-item> 附件：
          <file-preview :fileList="props.detail?.attachment" name="附件"
                        :maxUploadSize="3"/>
        </a-descriptions-item>

        <a-descriptions-item> 创建人： {{ props.detail?.createUserName }}</a-descriptions-item>
        <a-descriptions-item> 创建时间： {{ props.detail?.createTime }}</a-descriptions-item>
      </a-descriptions>
    </div>
  </div>
</template>
<script setup>
import {inject} from 'vue';
import {useRouter} from 'vue-router';

let smartEnumPlugin = inject('smartEnumPlugin');

import Upload from '/@/components/upload/index.vue';
import AddTrackModal from '../components/add-track-modal.vue';
import FilePreview from '/@/components/file-preview/index.vue';
import _ from 'lodash';
import { PRINCIPAL_TYPE_ENUM } from '/@/constants/business/shipper-const';
// ----------------------- 以下是字段定义 emits props ---------------------

const props = defineProps({
  detail: {
    type: Object,
    default: () => {
      return {
        consignorImage: []
      };
    }
  },
});
const emit = defineEmits(['refresh']);
const router = useRouter();

// 获取业务类型的描述信息
function getShipperTypeDesc() {
  let shipperTypeList = props.detail?.shipperTypeList;
  if (!shipperTypeList) {
    return '';
  }
  let descList = [];
  shipperTypeList.forEach((item) => {
    let desc = smartEnumPlugin.getDescByValue('SHIPPER_TYPE_ENUM', item);
    descList.push(desc);
  });
  return descList.join('，');
}

function getCustomerService() {
  if (!props.detail) {
    return '';
  }
  if (_.isEmpty(props.detail.shipperPrincipalList)) {
    return '';
  }
  let list = props.detail.shipperPrincipalList.filter(e => e.type == PRINCIPAL_TYPE_ENUM.CUSTOMER_SERVICE.value);
  return list.map(e => e.employeeName).join('，');
}

function getManagerName() {
  if (!props.detail) {
    return '';
  }
  if (_.isEmpty(props.detail.shipperPrincipalList)) {
    return '';
  }
  let list = props.detail.shipperPrincipalList.filter(e => e.type == PRINCIPAL_TYPE_ENUM.MANAGER.value);
  return list.map(e => e.employeeName).join('，');
}

function getArea(area = []) {
  if (_.isEmpty(area)) {
    return '';
  }
  return area.map(e => e.valueName).join('，');
}

</script>
<style scoped lang="less">
</style>
