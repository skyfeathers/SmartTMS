<template>
  <a-dropdown trigger="click" v-model:open="show">
    <div style="width: 28px;display: flex;justify-content: center;">
      <a-badge :count="unreadCount">
        <a-button title="消息通知" type="text">
          <template #icon><BellOutlined :style="{ fontSize: '12px' }" /></template>
        </a-button>
      </a-badge>
    </div>

    <template #overlay>
      <div>
        <a-card title="消息通知" class="content-card" size="small">
          <template #extra>
            <div class="notice-operate">
              <a @click="readAll()" v-if="unreadCount > 0">全部已读</a>
              <a @click="changePage()">更多</a>
            </div>
          </template>
          <a-spin :spinning="loading">
            <a-list item-layout="horizontal" :data-source="msgList">
              <template #renderItem="{ item }">
                <a-list-item @click="handle(item)">
                  <a-list-item-meta :description="item.content">
                    <template #title>
                      {{ item.title }}
                    </template>
                  </a-list-item-meta>
                </a-list-item>
              </template>
            </a-list>
          </a-spin>
        </a-card>
      </div>
    </template>
  </a-dropdown>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue';
import { messageApi } from '/@/api/business/message/message-api';
import { useUserStore } from '/@/store/modules/system/user';
import { MSG_RECEIVE_TYPE_ENUM } from '/@/constants/business/message-const';
import { PAGE_SIZE } from '/@/constants/common-const';
import { messageSetup } from '/@/views/business/oa/message/message-handle-setup';
import { message } from 'ant-design-vue';

defineExpose({ showNotice });

const loading = ref(false);
const show = ref(false);
let unreadCount = ref(0);
let msgList = ref([]);
let pages = ref(0);

function showNotice() {
  show.value = true;
  queryMsg();
}

let interval = null;

onMounted(() => {
  queryUnreadCount();
  if (interval == null) {
    //定时查询未读消息数
    interval = setInterval(() => {
      queryUnreadCount();
    }, 3 * 60 * 1000);
  }
});

// 查询未读消息数
async function queryUnreadCount() {
  try {
    let responseModel = await messageApi.queryUnreadCount();
    unreadCount.value = responseModel.data;
  } catch (e) {
    console.log(e);
  } finally {
  }
}

const queryFormState = {
  readFlag: false,
  pageNum: 1,
  pageSize: 5,
};
const queryForm = reactive({ ...queryFormState });

// 查询未读消息数
async function queryMsg() {
  try {
    loading.value = true;
    let responseModel = await messageApi.queryMessage(queryForm);
    msgList.value = responseModel.data.list;
    unreadCount.value = responseModel.data.total;
    pages.value = responseModel.data.pages;
  } catch (e) {
    console.log(e);
  } finally {
    loading.value = false;
  }
}

// 换一批
function changePage() {
  if (queryForm.pageNum === pages.value) {
    queryForm.pageNum = 1;
  } else {
    queryForm.pageNum++;
  }
  queryMsg();
}

// 全部已读
async function readAll () {
  try {
    if(unreadCount.value == 0){
      message.success('没有未读的消息了～')
      return;
    }
    await messageApi.updateAllReadFlag();
    show.value = false;
    queryUnreadCount();
  } catch (e) {
    console.log(e);
  }
}

let { toHandle } = messageSetup();

function handle(item) {
  toHandle(item);
  show.value = false;
}
</script>

<style lang="less" scoped>
.content-card {
  width: 350px;
  ::v-deep(.ant-card-body) {
    padding-top: 0px;
  }
}
ul li :hover {
  cursor: pointer;
  color: @primary-color;
}

.notice-operate {
  a {
    margin-right: 15px;
  }
}
</style>
