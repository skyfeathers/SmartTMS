<template>
    <div class="code-info">
        
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