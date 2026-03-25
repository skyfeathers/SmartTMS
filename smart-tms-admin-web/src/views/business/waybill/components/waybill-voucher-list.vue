<!--
 * @Description: 凭证/磅单
 * @Author: zhuoda
 * @Date: 2022-07-15
 * @LastEditTime: 2022-07-16
 * @LastEditors: zhuoda
-->
<template>
  <div>
    <a-button type="primary" size="small" @click="showModal(null)">补录 磅单/凭证</a-button>
    <div style="display: flex;gap: 20px;">
      <div style="width: 40%">
        <a-collapse v-model:activeKey="activeKey" class="smart-margin-top10">
          <a-collapse-panel v-for="item in voucherList" :key="item.waybillVoucherId" :header="buildHeader(item)">
            <div>
              <file-preview :fileList="item.attachment" type="picture" />
            </div>
            <template #extra>
              <a-button type="link" size="small" @click.stop="handleUpdate(item)">编辑</a-button>
              <a-button type="link" size="small" danger @click.stop="handleDelete(item.waybillVoucherId)">删除</a-button>
            </template>
          </a-collapse-panel>
        </a-collapse>
      </div>
      
      <BaiduMap
          style="width: 60%"
          height="600px"
          ref="baiduMapRef"
          :waybillId="waybillId"
          :appPosition="[]"
          :sinoiov="[]"
          :wayBillTimeWarnList="[]"
          :finishFlag="true"
      />
    </div>
    
    <WaybillVoucherUploadModal ref="waybillVoucherUploadModalRef" @success="refresh" />
  </div>
</template>
<script setup>
import { onMounted, ref, getCurrentInstance, nextTick, reactive, h } from 'vue';
import _ from 'lodash';
import { waybillApi } from '/@/api/business/waybill/waybill-api';
import { WAYBILL_VOUCHER_TYPE_ENUM } from '/@/constants/business/waybill-const';
import { SmartLoading } from '/@/components/smart-loading';
import { message, Modal } from 'ant-design-vue';
import FilePreview from '/@/components/file-preview/index.vue';
import WaybillVoucherUploadModal from './waybill-voucher-upload-modal.vue';
import BaiduMap from '/@/components/baidu-map/index.vue';
const props = defineProps({
  waybillId: Number,
});
const emit = defineEmits(['refresh']);

const voucherList = ref([]);
const activeKey = ref([]);
onMounted(query);

// 刷新凭证/磅单列表
function refresh() {
  query();
  emit('refresh');
}

let baiduMapRef = ref(null);

async function query() {
  let res = await waybillApi.getVoucherListByWaybillId(props.waybillId);
  voucherList.value = res.data;
  let keyArray = [];
  for (let i = 0; i < res.data.length; i++) {
    keyArray.push(i);
  }
  activeKey.value = keyArray;
  baiduMapRef.value.initPosition();
}

function buildHeader(voucher) {
  const internalInstance = getCurrentInstance();
  const smartEnumPlugin = internalInstance.appContext.config.globalProperties.$smartEnumPlugin;
  let typeName = smartEnumPlugin.getDescByValue('WAYBILL_VOUCHER_TYPE_ENUM', voucher.type);
  let headerContent = `【${typeName}】， 时间：${voucher.createTime}`;
  if (voucher.location) {
    headerContent += '，位置：' + voucher.location;
  }

  if (voucher.latitude) {
    headerContent += '， 经度 ：' + voucher.latitude + ',纬度  :' + voucher.longitude;
  }
  return headerContent;
}

const waybillVoucherUploadModalRef = ref(null);
function showModal() {
  waybillVoucherUploadModalRef.value.showModal({
    waybillId: props.waybillId
  });
}

// 更新运单凭证
async function handleUpdate(item) {
  waybillVoucherUploadModalRef.value.showModal({
    waybillId: props.waybillId,
    ...item

  });
}

// 删除凭证/磅单
async function handleDelete(waybillVoucherId) {
  Modal.confirm({
    title: '确认删除该凭证吗？',
    okText: '确认',
    okType: 'danger',
    onOk: async () => {
      await waybillApi.deleteVoucher(waybillVoucherId);
      refresh();
      message.success('删除成功');
    }
  });
}
</script>
<style lang="less" scoped>
::v-deep .ant-collapse-extra {
  flex-shrink: 0;
}
</style>
