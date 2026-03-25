<template>
  <a-modal v-model:open="recordModal" :width="700" title="操作记录" :footer="null" @close="onClose">
    <a-table
        :columns="recordColumns"
        :dataSource="recordList"
        :pagination="false"
        size="small"
    />
  </a-modal>
</template>
<script setup>
import {ref} from "vue";
import {useSpinStore} from "/@/store/modules/system/spin";
import {contractApi} from "/@/api/business/contract/contract-api";
// ----------------------- 以下是公用变量 emits props ----------------
// ----------------------- 展开自定义组合式函数 -----------------------

// ----------------------- 以下是暴露的方法内容 -----------------------
defineExpose({
  showModal
});
// ----------------------- 以下是生命周期 ----------------------------

// ----------------------- 以下是业务内容 ----------------------------
// 查询线上合同操作记录
let recordColumns = ref([
  {
    title: "事件代码",
    dataIndex: "action",
  },
  {
    title: "事件名称",
    dataIndex: "actionDesc",
  },
  {
    title: "操作时间",
    dataIndex: "createTime",
  },
]);
let recordList = ref([]);
let recordModal = ref(false);

function showModal(contractId){
  getOnlineRecords(contractId);
}

async function getOnlineRecords(contractId) {
  useSpinStore().show();
  try {
    let res = await contractApi.getOnlineRecords(contractId);
    recordList.value = res.data;
    recordModal.value = true;
  } catch (error) {
    console.error(error);
  } finally {
    useSpinStore().hide();
  }
}

function onClose() {
  recordList.value = [];
  recordModal.value = false;
}
</script>
<style lang='scss' scoped></style>
