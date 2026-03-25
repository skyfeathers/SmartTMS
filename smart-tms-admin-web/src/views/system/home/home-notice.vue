<!--
 * @Description:首页通知
 * @Author: zhuoda
 * @Date: 2022-06-25
 * @LastEditTime: 2022-08-19
 * @LastEditors: zhuoda
-->
<template>
  <default-home-card extra="更多" :icon="icon" title="通知公告" @extraClick="onMore" class="home-notice-card">
    <a-spin :spinning="loading">
      <a-empty v-if="$lodash.isEmpty(data)" />
      <ul v-else class="notice-list">
        <li v-for="(item, index) in data" :key="index" :class="[item.viewFlag ? 'read' : 'un-read']">
          <a-tooltip placement="top">
            <template #title>
              <span>{{ item.title }}</span>
            </template>
            <a class="content" @click="toDetail(item.noticeId)">
              <a-badge :status="item.viewFlag ? 'default' : 'error'" />
              <span class="title">{{ item.title }}</span>
            </a>
          </a-tooltip>
          <span class="time"> {{ item.publishDate }}</span>
        </li>
      </ul>
    </a-spin>
  </default-home-card>
</template>
<script setup>
import { onMounted, reactive, ref } from 'vue';
import DefaultHomeCard from '/@/views/system/home/components/default-home-card.vue';
import { useRouter } from 'vue-router';
import { PAGE_SIZE } from '/@/constants/common-const';
import { noticeApi } from '/@/api/business/oa/notice-api';

const props = defineProps({
  noticeTypeId: {
    type: Number,
    default: 1,
  },
  icon: {
    type: String,
  },
});

const queryForm = {
  noticeTypeId: props.noticeTypeId,
  pageNum: 1,
  pageSize: 5,
  searchCount: false,
};

let data = ref([]);

const loading = ref(false);
// 查询列表
async function queryNoticeList() {
  try {
    loading.value = true;
    const result = await noticeApi.queryEmployeeNotice(queryForm);
    data.value = result.data.list;
  } catch (err) {
    console.log(err);
  } finally {
    loading.value = false;
  }
}

onMounted(() => {
  queryNoticeList();
});

// 查看更多
function onMore() {
  router.push({
    path: '/notice/notice-employee-list',
  });
}

// 进入详情
const router = useRouter();
function toDetail(noticeId) {
  router.push({
    path: '/notice/notice-employee-detail',
    query: { noticeId },
  });
}
</script>
<style lang="less" scoped>
@read-color: #666;
ul li {
  display: flex;
  justify-content: space-between;
  margin-bottom: 4px;

  .content {
    white-space: nowrap;
    text-overflow: ellipsis;
    overflow: hidden;
    word-break: break-all;
    margin-right: 5px;
  }

  .time {
    flex-shrink: 0;
    color: @read-color;
    min-width: 75px;
  }
}

ul li :hover {
  color: @primary-color;
}

.un-read a {
  color: @text-color;
}

.read a {
  color: @read-color;
}
.home-notice-card {
  width: 100%;
}

.notice-list {
  height: 100%;
  overflow: auto;
  .title {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    word-break: break-all;
  }
}
</style>
