<template>
  <div>
    <a-descriptions :column="2" size="small">
      <a-descriptions-item label="银行名称">{{ paymentDetail.payBankName }}</a-descriptions-item>
      <a-descriptions-item label="开户名">{{ paymentDetail.payAccountName }}</a-descriptions-item>
      <a-descriptions-item label="银行账号">{{ paymentDetail.payBankAccount }}</a-descriptions-item>
      <a-descriptions-item label="创建人">{{ paymentDetail.createUserName }}</a-descriptions-item>
      <a-descriptions-item label="创建时间">{{ paymentDetail.createTime }}</a-descriptions-item>
    </a-descriptions>
  </div>
</template>
<script setup>
const props = defineProps({
  paymentDetail: {
    type: Object,
    default: () => {
      return {};
    }
  },
});

</script>
