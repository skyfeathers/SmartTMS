<!--
 * @Description: 
 * @Author: zhuoda
 * @Date: 2022-07-14
 * @LastEditTime: 2022-08-02
 * @LastEditors: zhuoda
-->
<template>
  <div class="comment-item" v-for="(item, index) in commentList" :key="index">
    <div class="comment-header">
      <span>{{ item.creatorName }}添加的评论</span>
      <span>{{ item.createTime }}</span>
    </div>
    <div class="comment-content">
      <p>{{ item.content }}</p>
    </div>
  </div>
</template>
<script setup>
// ========= 定义 props ===============
const props = defineProps({
  commentList: Array,
});
</script>
<style lang="less" scoped>
.comment-item {
  width: 350px;
  background: #f7f7f9;
  border-radius: 6px;
  margin-top: 8px;
  font-family: PingFang SC, PingFang SC-Regular;
  font-weight: 400;
  text-align: left;
  .comment-header {
    display: flex;
    justify-content: space-between;
    padding: 2px 12px 0px 12px;
    span {
      font-size: 12px;
      color: #70829a;
      line-height: 23px;
      letter-spacing: 0.24px;
    }
  }
  .comment-content {
    padding: 2px 12px 2px 12px;
    span {
      font-size: 14px;
      color: #000000;
      line-height: 23px;
      letter-spacing: 0.28px;
    }
  }
}
</style>
