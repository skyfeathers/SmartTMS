<template>
  <div class="weather">
          <iframe
            width="600"
            scrolling="no"
            height="50"
            frameborder="0"
            allowtransparency="true"
            src="//i.tianqi.com/index.php?c=code&id=12&icon=1&num=5&site=12"
          ></iframe>
        </div>
</template>
<script setup>
import { ref } from 'vue';

</script>
<style lang="less" scoped> 
.weather {
    width: 100%;
    height: 100%;
    display: flex;
    flex-direction: row;
    justify-content: center;
    align-items: center;
    background-color: #fff;
    border-radius: 5px;
    padding: 10px 20px;
}
</style>
