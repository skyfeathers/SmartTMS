<!--
 * @Description: file content
 * @Author: yandy
 * @Date: 2022-08-03 11:30:09
 * @LastEditors:
 * @LastEditTime: 2022-08-03 11:30:09
-->
<template>
  <a-modal title="详情"
    :open="visibleFlag"
    :width="800"
    :maskClosable="false"
    forceRender
    :footer="null"
    @cancel="onClose">
    <div class="container">
      <a-spin :spinning="loading">
        <div class="title">{{detail.title}}</div>
        <div class="html-content"
             v-html="detail.contentHtml">
        </div>
      </a-spin>
    </div>
  </a-modal>
</template>
<script setup>
import { ref, reactive } from 'vue';
import { newsApi } from '/@/api/business/oa/news-api';

const visibleFlag = ref(false);

const defaultData = {
  title: undefined,
  content: undefined,
};

const detail = reactive({ ...defaultData });
const loading = ref(false);
// 查询详情
async function showModal(newsId) {
  try {
    loading.value = true;
    const result = await newsApi.newsViewDetail(newsId);
    const { title, contentHtml } = result.data;
    Object.assign(detail, { title, contentHtml });
    visibleFlag.value = true;
  } catch (e) {
    console.log(e);
  } finally {
    loading.value = false;
  }
}

// emit
const emit = defineEmits("closed");
// 关闭弹窗
function onClose() {
  visibleFlag.value = false;
  emit("closed");
}

// ----------------------- 以下是暴露的方法内容 ------------------------
defineExpose({
  showModal,
});
</script>


<style lang='less' scoped>
.container {
  overflow: hidden;
  overflow-y: auto;
  padding-bottom: 20px;
  .title {
    font-size: 20px;
    font-weight: bold;
    margin-bottom: 15px;
  }
  .html-content {
    line-height: 1.4;
  }
}
</style>
