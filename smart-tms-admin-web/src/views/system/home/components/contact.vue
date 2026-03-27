<template>
    <div class="code-info">
        <div class="code-info-item">
          <img src="/@/assets/images/login/code-h5.png" alt="" class="code-info-item-img" />
          <div class="code-info-item-text">移动端</div>
        </div>
        <div class="code-info-item">
          <img src="/@/assets/images/login/code-mp.png" alt="" class="code-info-item-img" />
          <div class="code-info-item-text">微信号</div>
        </div>
    </div>
</template>

<style lang="less" scoped>

.code-info {
    margin-top: 10px;
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: space-around;
    background: #fff;
    box-shadow: 0 1px 2px 0 rgba(0, 0, 0, 0.03), 0 1px 6px -1px rgba(0, 0, 0, 0.02), 0 2px 4px 0 rgba(0, 0, 0, 0.02);
    padding: 20px;
    border-radius: 8px;
    &-item {
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      &-img {
        width: 112px;
        height: 112px;
      }
      &-text {
        font-size: 12px;
        font-weight: 500;
        color: #333;
        margin-top: 10px;
      }
    }
    
  }

</style>