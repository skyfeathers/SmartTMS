<template>
  <div>
    <a-descriptions :column="2" size="small">
      <a-descriptions-item label="备注">{{ verificationDetail.remark }}</a-descriptions-item>
      <a-descriptions-item label="附件信息">
        <file-preview :fileList="verificationDetail.attachment"/>
      </a-descriptions-item>
      <a-descriptions-item label="创建人">{{ verificationDetail.createUserName }}</a-descriptions-item>
      <a-descriptions-item label="创建时间">{{ verificationDetail.createTime }}</a-descriptions-item>
    </a-descriptions>
  </div>
</template>
<script setup>
import FilePreview from '/@/components/file-preview/index.vue'

const props = defineProps({
  verificationDetail: {
    type: Object,
    default: () => {
      return {};
    }
  },
});

</script>
