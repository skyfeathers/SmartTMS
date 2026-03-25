<template>
  <div>
    <a-table
        :scroll="{ x:3300}"
        size="small"
        :dataSource="tableData"
        :columns="columns"
        :pagination="false"
        bordered
    >
      <template #bodyCell="{ text, record, index, column }">
        <template v-if="column.dataIndex === 'index'">
          {{ index + 1 }}
        </template>
        <template v-if="column.dataIndex === 'errorMsg'">
          <div class="error-msg" v-for="(item,index) in record.errorMsgList">{{ index + 1 }}、{{ item }}</div>
        </template>
        <template v-if="column.dataIndex === 'consignor'">
          <span>{{ text }}</span>
          <div v-if="!record.shipperId" class="error-msg">
            未匹配到现有货主，将新建
          </div>
        </template>
        <template v-if="column.dataIndex === 'scheduleName'">
          <span>{{ text }}</span>
          <div v-if="!record.scheduleId && text" class="error-msg">未匹配</div>
        </template>
        <template v-if="column.dataIndex === 'transportationTypeName'">
          <span>{{ text }}</span>
          <div v-if="!record.businessTypeCode && text" class="error-msg">未匹配</div>
        </template>
        <template v-if="column.dataIndex === 'transportModeName'">
          <span>{{ text }}</span>
        </template>
        <template v-if="column.dataIndex === 'settleModeName'">
          <span>{{ text }}</span>
        </template>
        <template v-if="column.dataIndex === 'containerBusinessTypeName'">
          <template v-if="record.businessTypeCode == TRANSPORTATION_TYPE_ENUM.CONTAINER_TRANSPORT.value">
            <span>{{ text }}</span>
            <div v-if="!record.containerBusinessTypeId && text" class="error-msg">未匹配</div>
          </template>
          <template v-else>
            <span></span>
          </template>
        </template>
        <template v-if="column.dataIndex === 'cabinetName'">
          <template v-if="record.businessTypeCode == TRANSPORTATION_TYPE_ENUM.CONTAINER_TRANSPORT.value">
            <span>{{ text }}</span>
            <div v-if="!record.cabinetId && text" class="error-msg">未匹配</div>
          </template>
          <template v-else>
            <span></span>
          </template>
        </template>
        <template v-if="column.dataIndex === 'goodsTypeName'">
          <span>{{ text }}</span>
          <div v-if="!record.goodsType && text" class="error-msg">未匹配</div>
        </template>
        <template v-if="column.dataIndex === 'goodsName'">
          <span>{{ text }}</span>
        </template>
        <template v-if="column.dataIndex === 'goodsUnitName'">
          <span>{{ text }}</span>
          <div v-if="!record.goodsUnit && text" class="error-msg">未匹配</div>
        </template>
        <template v-if="column.dataIndex === 'goodsWeight'">
          <span>{{ text }}</span>
        </template>
        <template v-if="column.dataIndex === 'goodsCount'">
          <span>{{ text }}</span>
        </template>
        <template v-if="column.dataIndex === 'deliverGoodsTime'">
          <span>{{ text }}</span>
        </template>
        <template v-if="column.dataIndex === 'createUserName'">
          <span>{{ text }}</span>
          <div v-if="!record.createUserId && text" class="error-msg">不存在</div>
        </template>
        <template v-if="column.dataIndex === 'transportRouteName'">
          <span>{{ text }}</span>
          <div v-if="!record.transportRouteId && text" class="error-msg">不存在</div>
        </template>
        <template v-if="column.dataIndex === 'driverName'">
          <span>{{ text }}</span>
        </template>
        <template v-if="column.dataIndex === 'driverPhone'">
          <span>{{ text }}</span>
          <div v-if="!record.driverId && text" class="error-msg">
            未匹配到现有司机，将新建
          </div>
        </template>
        <template v-if="column.dataIndex === 'vehicleNumber'">
          <span>{{ text }}</span>
          <div v-if="!record.vehicleId && text" class="error-msg">
            未匹配到现有车辆，将新建
          </div>
        </template>
        <template v-if="column.dataIndex === 'containerNumber'">
          <span>{{ text }}</span>
        </template>
        <template v-if="column.dataIndex === 'remark'">
          <span>{{ text }}</span>
        </template>
        <template v-if="column.dataIndex === 'receiveAmount'">
          <span>{{ text }}</span>
        </template>
        <template v-if="column.dataIndex === 'payAmount'">
          <span>{{ text }}</span>
        </template>
        <template v-if="column.dataIndex === 'oilCardAmount'">
          <span>{{ text }}</span>
        </template>
        <template v-if="column.dataIndex === 'businessDate'">
          {{ (text || '').substring(0, 7) }}
        </template>
      </template>
    </a-table>
  </div>
</template>
<script setup>
import { computed } from 'vue';
import { defaultColumns } from './import-columns';
import { TRANSPORTATION_TYPE_ENUM } from '/@/constants/business/transport-route-const';

const props = defineProps({
  tableData: {
    type: Array
  },
  // 是否为异常数据
  errorFlag: {
    type: Boolean,
    default: false
  }
});

const columns = computed(() => {
  if (props.errorFlag) {
    let errorMsgColumns = [
      {
        title: '序号',
        dataIndex: 'index',
        width: 50,
        fixed: 'left'
      },
      {
        title: '错误信息',
        dataIndex: 'errorMsg',
        width: 200,
        fixed: 'left'
      }
    ];
    return errorMsgColumns.concat(defaultColumns);
  }
  return defaultColumns;
});
</script>

<style lang="less" scoped>
.error-msg {
  color: #ff0000;
}
</style>
