<!--
 * @Description: 
 * @Author: zhuoda
 * @Date: 2022-07-05
 * @LastEditTime: 2022-07-07
 * @LastEditors: zhuoda
-->
<template>
  <div class="card-container">
    <a-card size="small" :bordered="false">
      <template #title>
        <div class="title">
            <div class="title-icon">
                <img :src="props.icon" alt="" />
            </div>
          <slot name="title"></slot>
          <span v-if="!$slots.title" class="title-text">{{ props.title }}</span>
        </div>
      </template>
      <template v-if="props.extra" #extra>
        <slot name="extra"></slot>
        <a v-if="!$slots.extra" @click="extraClick">{{ props.extra }}</a>
      </template>
      <slot></slot>
    </a-card>
  </div>
</template>
<script setup>
let props = defineProps({
  icon: String,
  title: String,
  extra: String,
});
let emits = defineEmits(['extraClick']);

function extraClick() {
  emits('extraClick');
}
</script>
<style lang="less" scoped>
.card-container {
  background-color: #fff;
  height: 100%;
  border-radius: 5px;
  overflow: hidden;
  ::v-deep .ant-card {
    height: 100%;
    .ant-card-head {
      padding: 0 20px;
    }
    .ant-card-body {
      height: 100%;
      .ant-spin-container {
        display: flex;
        flex-direction: column;
        .ant-empty {
          margin: auto;
        }
      }
    }
  }
  .title {
    display: flex;
    flex-direction: row;
    justify-content: flex-start;
    align-items: center;
    column-gap: 10px;
    padding: 5px 0;
    &-icon {
        width: 20px;
        height: 20px;
        background-color: #3886fb;
        display: flex;
        flex-direction: row;
        justify-content: center;
        align-items: center;
        border-radius: 5px;
        padding: 4px;
        box-sizing: border-box;
        // 蓝色阴影
        box-shadow: 0px 0px 5px 0px rgba(56, 134, 251, 0.5);
        img {       
            width: 100%;
            height: 100%;
        }
    }
    .title-text {
      font-size: 14px;
      color: #333;
      font-weight: bold;
    }
  }
}
</style>
