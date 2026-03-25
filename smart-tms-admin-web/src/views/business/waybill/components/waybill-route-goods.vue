
<template>
  <a-row type="flex">
    <a-col flex="400px">
      <a-timeline mode="left" class="smart-margin-left20">
        <a-timeline-item :key="index" v-for="(item, index) in pathList" class="smart-margin-left5">
          <template #dot>{{ $smartEnumPlugin.getDescByValue('PATH_TYPE_ENUM', item.type) }}</template>
          <div class="smart-margin-left20">
            <h4>{{ item.provinceName }}{{ item.cityName }}{{ item.districtName }}{{ item.address }}</h4>
            <span>联系人：{{ item.contactName }}-{{ item.contactPhone }}</span>
          </div>
        </a-timeline-item>
      </a-timeline>
    </a-col>
    <a-col flex="auto">
      <WaybillGoods :goods-list="goodsList" />
    </a-col>
  </a-row>
</template>
<script setup>
import WaybillGoods from './waybill-goods.vue';

const props = defineProps({
  pathList: {
    type: Array,
  },
  goodsList: {
    type: Array,
  },
});

</script>
