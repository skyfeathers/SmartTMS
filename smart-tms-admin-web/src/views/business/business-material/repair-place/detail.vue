<template>
    <a-card class="smart-margin-top10 content-card" size="small">
          <RepairList/>
    </a-card>
  </template>
  <script setup>
  import { repairPlantApi } from '/@/api/business/business-material/repair-plant';
  import { onMounted, reactive, ref, computed } from 'vue';
  import { useRoute, useRouter } from 'vue-router';
  import { useUserStore } from '/@/store/modules/system/user';
  import { SmartLoading } from '/@/components/smart-loading';
  import RepairList from './components/repair-place-list.vue';
  
  // --------------------- 详情数据 ---------------------
  let router = useRouter();
  let route = useRoute();

  function onClose() {
    useUserStore().closePage(route, router);
  }
  
  </script>
  <style lang="less" scoped>
  .content-card {
    ::v-deep(.ant-card-body) {
      padding-top: 0px;
    }
  }
  </style>
  