<!--
 * @Description: 文件预览弹窗
 * @version: 
 * @Author: 李云飞 qq:23983208
 * @Date: 2022-07-18 15:20:14
 * @LastEditors: 李云飞 qq:23983208
 * @LastEditTime: 2022-07-18 15:43:10
-->
<template>
  <a-modal title="文件预览"
    v-model:open="visibleFlag"
    :width="768"
    @cancel="onClose">
    <div class="container">
      <img class="img-prev"
        :src="previewUrl" />
    </div>
    <template #footer>
      <a-button @click="onClose">关闭</a-button>
    </template>
  </a-modal>
</template>

<script setup>
import { ref } from 'vue';
import { download } from '/@/lib/axios';

const visibleFlag = ref(false);
const imgFileType = ['jpg', 'jpeg', 'png', 'gif'];
const previewUrl = ref();

function showPreview(fileItem) {
  if (isImg(fileItem.fileType)) {
    previewUrl.value = fileItem.fileUrl;
    visibleFlag.value = true;
    return;
  }
  download(fileItem.fileName, fileItem.fileUrl);
}

// 判断图片类型
function isImg(fileType) {
  return imgFileType.includes(fileType);
}

function onClose() {
  visibleFlag.value = false;
}

// ----------------------- 以下是暴露的方法内容 ------------------------
defineExpose({
  showPreview,
});
</script>

<style lang='less' scoped>
.container {
  display: flex;
  justify-content: center;
  align-items: center;
  .img-prev {
    display: block;
    width: 100%;
    height: 600px;
    object-fit: contain;
  }
}
</style>