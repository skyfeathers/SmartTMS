<template>
  <default-home-card icon="SmileTwoTone" title="关注公众号">
    <div class="app-qr-box">
      <div class="app-qr">
        <img :src="gongzhonghao" />
        <span class="qr-desc"> 关注公众号 </span>
      </div>
      <div class="app-qr">
        <img :src="zhuoda" />
        <span class="qr-desc"> 扫码 骚扰卓大 :) </span>
      </div>
    </div>
  </default-home-card>
</template>
<script setup>
import DefaultHomeCard from "/@/views/system/home/components/default-home-card.vue";
import gongzhonghao from '/@/assets/images/1024lab/1024lab-gzh.jpg';
import zhuoda from '/@/assets/images/1024lab/zhuoda-wechat.jpg';
</script>
<style lang='less' scoped>
.app-qr-box {
  display: flex;
  align-items: center;
  justify-content: space-around;
  margin-top: 20px;
  .app-qr {
    display: flex;
    align-items: center;
    justify-content: center;
    flex-direction: column;
    > img {
      width: 112px;
      height: 112px;
    }
    .qr-desc {
      display: flex;
      align-items: center;
      margin-top: 11px;
      font-size: 12px;
      font-weight: 500;
      text-align: center;
      > img {
        width: 15px;
        height: 18px;
        margin-right: 9px;
      }
    }
  }
}
</style>
