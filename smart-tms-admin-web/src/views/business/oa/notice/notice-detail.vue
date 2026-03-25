<!--
 * @Description: 公告详情
 * @version:
 * @Author: zhuoda
 * @Date: 2022-08-18 21:30:30
 * @LastEditors: zhuoda
 * @LastEditTime: 2022-08-19
-->
<template>
  <a-card style="margin-bottom: 15px" size="small">
    <a-descriptions :title="noticeDetail.title" :columns="4" size="small">
      <template #extra>
        <a-button v-if="!noticeDetail.publishFlag" type="primary" size="small" @click="onEdit">编辑</a-button>
      </template>
      <a-descriptions-item label="分类">{{ noticeDetail.noticeTypeName }}</a-descriptions-item>
      <a-descriptions-item label="文号">{{ noticeDetail.documentNumber }}</a-descriptions-item>
      <a-descriptions-item label="来源">{{ noticeDetail.source }}</a-descriptions-item>
      <a-descriptions-item label="作者">{{ noticeDetail.author }}</a-descriptions-item>
      <a-descriptions-item label="推送对象">{{ $smartEnumPlugin.getDescByValue('NOTICE_PUSH_TYPE_ENUM', noticeDetail.noticePushType) }}</a-descriptions-item>
      <a-descriptions-item label="页面浏览量">{{ noticeDetail.pageViewCount }}</a-descriptions-item>
      <a-descriptions-item label="用户浏览量">{{ noticeDetail.userViewCount }}</a-descriptions-item>
      <a-descriptions-item label="创建时间">{{ noticeDetail.createTime }}</a-descriptions-item>
      <a-descriptions-item label="发布时间">{{ noticeDetail.publishTime }}</a-descriptions-item>
      <a-descriptions-item label="定时发布">{{ noticeDetail.publishFlag ? '已发布' : '待发布' }}</a-descriptions-item>
      <a-descriptions-item v-if="!$lodash.isEmpty(noticeDetail.attachment)" label="附件" :span="2">
        <FilePreview :fileList="noticeDetail.attachment"/>
      </a-descriptions-item>
      <a-descriptions-item v-if="platformFlag" :span="2" label="可见范围">
        <template v-if="noticeDetail.allVisibleFlag">全部可见</template>
        <div class="visible-list">
          <div class="visible-item" v-for="item in noticeDetail.visibleRangeList" :key="item.dataId">
            {{ item.dataName }}
          </div>
        </div>
      </a-descriptions-item>
    </a-descriptions>
  </a-card>

  <a-card size="small">
    <a-tabs v-model:activeKey="activeKey" size="small">
      <a-tab-pane :key="1" tab="内容">
        <div class="content-html" v-html="noticeDetail.contentHtml"></div>
      </a-tab-pane>
      <a-tab-pane :key="2" tab="查看记录" force-render>
        <NoticeViewRecordList ref="noticeViewRecordList" :noticeId="route.query.noticeId" />
      </a-tab-pane>
      <a-tab-pane :key="3" tab="操作记录" />
    </a-tabs>
  </a-card>

  <!-- 编辑 -->
  <NoticeFormDrawer ref="noticeFormDrawerRef" @reloadList="queryNoticeDetail" />

</template>

<script setup>
import { reactive, ref, onMounted, computed } from 'vue';
import { useRoute } from 'vue-router';
import { SmartLoading } from '/@/components/smart-loading';
import { noticeApi } from '/@/api/business/oa/notice-api';
import { PAGE_SIZE, PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
import NoticeFormDrawer from './components/notice-form-drawer.vue';
import FilePreview from '/@/components/file-preview/index.vue';
import DepartmentTreeSelect from '/@/components/department-tree-select/index.vue';
import NoticeViewRecordList from './components/notice-view-record-list.vue';
import { NOTICE_PUSH_TYPE_ENUM } from '/@/constants/business/notice-const';

const route = useRoute();

const platformFlag = computed(()=>{
  return noticeDetail.value.noticePushType === NOTICE_PUSH_TYPE_ENUM.PLATFORM_PUSH.value;
})

const props = defineProps({
  newsType: {
    type: Number,
  },
});

const activeKey = ref(1);

const noticeDetail = ref({});
const noticeViewRecordList = ref();

onMounted(() => {
  if (route.query.noticeId) {
    queryNoticeDetail();
    noticeViewRecordList.value.onSearch();
  }
});

// 查询详情
async function queryNoticeDetail() {
  try {
    SmartLoading.show();
    const result = await noticeApi.getUpdateNoticeInfo(route.query.noticeId);
    noticeDetail.value = result.data;
  } catch (err) {
    console.log(err);
  } finally {
    SmartLoading.hide();
  }
}

// 点击编辑
const noticeFormDrawerRef = ref();
function onEdit() {
  noticeFormDrawerRef.value.showModal(noticeDetail.value.noticeId);
}

</script>

<style lang="less" scoped>
:deep(.ant-descriptions-item-content) {
  flex: 1;
  overflow: hidden;
}
.file-list {
  width: 100%;
  display: flex;
  flex-wrap: wrap;
  .file-item {
    display: block;
    margin-right: 10px;
  }
}
.visible-list {
  display: flex;
  flex-wrap: wrap;
  .visible-item {
    margin-right: 10px;
    color: #666;
  }
}
.content-html {
  img {
    max-width: 100%;
  }
}
</style>
