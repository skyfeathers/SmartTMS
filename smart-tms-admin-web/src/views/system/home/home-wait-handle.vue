<!--
 * @Description: file content
 * @Author: yandy
 * @Date: 2022-08-03 15:19:11
 * @LastEditors:
 * @LastEditTime: 2022-08-03 15:19:11
-->
<template>
  <default-home-card extra="换一批" :icon="WaitHandleIcon" title="待办事项" @extraClick="onChange" class="home-wait-handle">

    <a-spin :spinning="loading">
      <a-empty v-if="$lodash.isEmpty(data)"/>
      <a-list v-else item-layout="horizontal" :data-source="data">
        <template #renderItem="{ item }">
          <a-list-item>
            <a-list-item-meta :description="item.content" @click="toHandle(item)">
              <template #title>
                <div class="title-container">
                  <a-badge :status="item.readFlag ? 'default' : 'error'"/>
                  <span class="title">{{ item.title }}</span>
                </div>
              </template>
            </a-list-item-meta>
            <template #extra>
              <div class="time"> {{ item.createTime }}</div>
            </template>
          </a-list-item>
        </template>
      </a-list>
    </a-spin>
  </default-home-card>
</template>
<script setup>
import {onMounted, reactive, ref} from "vue";
import DefaultHomeCard from "/@/views/system/home/components/default-home-card.vue";
import {PAGE_SIZE} from "/@/constants/common-const";
import {messageApi} from "/@/api/business/message/message-api";
import {messageSetup} from "/@/views/business/oa/message/message-handle-setup";
import WaitHandleIcon from "/@/assets/images/home/wait-handle.png";

const queryFormState = {
  readFlag: false,
  pageNum: 1,
  pageSize: 5,
  searchCount: true
};
const queryForm = reactive({...queryFormState});

let data = ref([])
let pages = ref(0);

const loading = ref(false);

// 查询 管理员查全部
async function ajaxQuery() {
  try {
    loading.value = true;
    let result = await messageApi.queryMessage(queryForm);
    data.value = result.data.list;
    pages.value = result.data.pages;
  } catch (e) {
    console.log(e);
  } finally {
    loading.value = false;
  }
}

onMounted(() => {
  ajaxQuery();
});


// 换一批
function onChange() {
  if (queryForm.pageNum === pages.value) {
    queryForm.pageNum = 1;
  } else {
    queryForm.pageNum++;
  }
  ajaxQuery();
}

// 标记已读
async function readHandle(msgType) {
  try {
    await messageApi.updateReadFlagByMsgType(msgType);
  } catch (e) {
    console.log(e);
  } finally {

  }
}

let {toHandle} = messageSetup();


</script>
<style lang="less" scoped>
ul li {
height: 55px;
  .time {
    flex-shrink: 0;
    color: @text-color-secondary;
    min-width: 75px;
  }
}

ul li :hover {
  cursor: pointer;
  color: @primary-color;
}

.home-wait-handle {
  height: 100%;
  overflow: hidden;
  ::v-deep .ant-list {
    height: 100%;
    overflow: hidden;
    .ant-list-items {
      height: 100%;
      overflow: scroll;
      .ant-list-item-meta-description {
        width: 100%;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
      }
    }
    .title-container {
      display: flex;
      flex-direction: row;
      align-items: center;
      margin-right: 5px;
      .title {
        flex-shrink: 0;
        width: 100%;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
      } 
    }
    
    
  }
}



</style>
