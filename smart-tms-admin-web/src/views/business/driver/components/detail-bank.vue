<!--
 * @Description:
 * @Author: zhuoda
 * @Date: 2022-07-09
 * @LastEditTime: 2022-08-19
 * @LastEditors: zhuoda
-->
<template>
  <div>
    <template v-for="item in detail.bankList" :key="item.bankId">
      <a-typography-title :level="5" class="smart-margin-top10">银行卡信息</a-typography-title>
      <a-descriptions :column="3" size="small">
        <a-descriptions-item label="开户名">{{ item.accountName }}</a-descriptions-item>
        <a-descriptions-item label="银行账号">{{ item.bankAccount }}</a-descriptions-item>
        <a-descriptions-item label="银行名称">{{ item.bankName }}</a-descriptions-item>
        <a-descriptions-item label="支行名称">{{ item.bankBranchName }}</a-descriptions-item>
        <a-descriptions-item label="是否默认">{{ item.defaultFlag ? '是' : '否' }}</a-descriptions-item>
        <a-descriptions-item label="是否为纳税人信息">{{ item.taxpayerFlag ? '是' : '否' }}</a-descriptions-item>
        <a-descriptions-item label="银行卡正面">
          <file-preview type="picture" :fileList="item.bankCardFrontAttachment" />
        </a-descriptions-item>
      </a-descriptions>
    </template>
  </div>
</template>
<script setup>
import FilePreview from '/@/components/file-preview/index.vue';
const props = defineProps({
  detail: {
    type: Object,
    default: () => {
      return {};
    },
  },
});
</script>
<style lang="scss" scoped></style>
