<!--
 * @Description:
 * @Author: zhuoda
 * @Date: 2022-07-18
 * @LastEditTime: 2022-08-02
 * @LastEditors: zhuoda
-->
<template>
  <a-row type="flex" class="row-box">
    <a-col flex="400px">
      <h4 style="margin-bottom: 20px" v-if="detail.transportRouteName">路线名称：{{ detail.transportRouteName }}</h4>
      <a-timeline mode="left" class="margin-left30">
        <a-timeline-item :key="index" v-for="(item, index) in detail.pathList" style="margin-left:30px">
          <template #dot>{{ $smartEnumPlugin.getDescByValue('PATH_TYPE_ENUM', item.type) }}</template>
          <div class="smart-margin-left20">
            <h4>{{ item.provinceName }}{{ item.cityName }}{{ item.districtName }}{{ item.address }}</h4>
            <span>联系人：{{ item.contactName }}-{{ item.contactPhone }}</span>
          </div>
        </a-timeline-item>
      </a-timeline>
    </a-col>
    <a-col flex="auto" style="overflow: hidden;">
        <a-table :columns="columns" :dataSource="detail.goodsList" bordered :pagination="false" size="small"  :scroll="{ x: '100%' }">
          <template #bodyCell="{ text, record, index, column }">
            <template v-if="column.dataIndex === 'goodsUnit'">
              {{$smartEnumPlugin.getDescByValue('GOODS_UNIT_TYPE_ENUM', record.goodsUnit)}}
            </template>
            <template v-else-if="column.dataIndex === 'goodsQuantity'">
              <span>{{ record.goodsQuantity }}/</span>
              <span style="color: red;">{{ record.remainGoodsQuantity }}</span>
            </template>
          </template>
        </a-table>
    </a-col>
  </a-row>
</template>
<script setup>
import { computed, reactive } from 'vue';

const props = defineProps({
  detail: {
    type: Object,
  },
});
const columns = reactive([
  {
    title: '货物类型',
    dataIndex: 'goodsTypeName',
    fixed: 'left',
    width: 150,
    ellipsis: true,
  },
  {
    title: '货物名称',
    dataIndex: 'goodsName',
    width: 120,
    ellipsis: true,
  },
  {
    title: '货物单位',
    dataIndex: 'goodsUnit',
    width: 80,
  },
  {
    title: '货物量(总量/剩余量)',
    dataIndex: 'goodsQuantity',
    width: 160,
  },
  {
    title: '备注',
    dataIndex: 'remark',
    width: 100,
    ellipsis: true,
  },
]);
</script>

<style lang="less" scoped>
.row-box {
  flex-flow: nowrap;
}
</style>
