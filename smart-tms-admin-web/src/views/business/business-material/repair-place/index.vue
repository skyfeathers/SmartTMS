<!--
 * @Description:  维修地点
 * @version:
 * @Author: 李云飞 qq:23983208
 * @Date: 2022-07-13 17:09:59
 * @LastEditors: 李云飞 qq:23983208
 * @LastEditTime: 2022-07-19 10:24:12
-->
<template>
  <a-form class="smart-query-form" v-privilege="'repairPlace:query'">
    <a-row class="smart-query-form-row">
      <a-form-item label="关键字"
        class="smart-query-form-item">
        <a-input style="width: 300px"
          v-model:value="queryForm.searchWord"
          placeholder="简称/联系人/创建人" />
      </a-form-item>

      <a-form-item label="创建时间"
        class="smart-query-form-item">
        <a-range-picker v-model:value="searchDate"
          @change="dateChange" />
      </a-form-item>

      <a-form-item label="状态"
        class="smart-query-form-item">
        <a-radio-group v-model:value="queryForm.disabledFlag" @change="onSearch">
          <a-radio-button :value="null">全部</a-radio-button>
          <a-radio-button :value="false">启用</a-radio-button>
          <a-radio-button :value="true">禁用</a-radio-button>
        </a-radio-group>
      </a-form-item>

      <a-form-item class="smart-query-form-item smart-margin-left10">
        <a-button-group>
          <a-button type="primary"
            @click="onSearch">
            <template #icon>
              <SearchOutlined />
            </template>
            查询
          </a-button>
          <a-button @click="onReload">
            <template #icon>
              <ReloadOutlined />
            </template>
            重置
          </a-button>
        </a-button-group>
      </a-form-item>
    </a-row>
  </a-form>

  <a-card size="small"
    :bordered="false">
    <a-row class="smart-table-btn-block">
      <div class="smart-table-operate-block">
        <a-button type="primary"
                  size="small"
                  @click="addOrUpdate()"
                  v-privilege="'repairPlace:add'">
          <template #icon>
            <PlusOutlined />
          </template>
          新建维修地点
        </a-button>
      </div>
    </a-row>

    <a-table rowKey="repairPlantId"
      :columns="tableColumns"
      :dataSource="tableData"
      :scroll="{ x: '100%' }"
      :pagination="false"
      :loading="tableLoading"
      size="small"
      bordered>
      <template #bodyCell="{ column, record }">
        <template v-if="column.dataIndex === 'action'">
          <div class="smart-table-operate">
            <a-button type="link"
                      @click="addOrUpdate(record)" v-privilege="'repairPlace:edit'">编辑
            </a-button>
            <a-button type="link" v-privilege="'repairPlace:delete'"
                      @click="onDelete(record.repairPlantId)">删除
            </a-button>
          </div>
        </template>
        <template v-else-if="column.dataIndex == 'repairPlantName'">
          <a-button @click="toDetail(record)" type="link" v-if="$privilege('repairPlace:detail')">{{ record.repairPlantName }}</a-button>
          <span v-else>{{ record.repairPlantName }}</span>
        </template>
        <template v-else-if="column.dataIndex === 'disabledFlag'">
          <a-tag v-show="record.disabledFlag" color="error">禁用</a-tag>
          <a-tag v-show="!record.disabledFlag" color="success">启用</a-tag>
        </template>
      </template>
    </a-table>

    <div class="smart-query-table-page">
      <a-pagination showSizeChanger
        showQuickJumper
        show-less-items
        :pageSizeOptions="PAGE_SIZE_OPTIONS"
        :defaultPageSize="queryForm.pageSize"
        v-model:current="queryForm.pageNum"
        v-model:pageSize="queryForm.pageSize"
        :total="total"
        @change="queryRepairPlantList"
        @showSizeChange="queryRepairPlantList"
        :show-total="(total) => `共${total}条`" />
    </div>
  </a-card>

  <!-- 新建、编辑弹窗 -->
  <RepairPlaceOperateModal ref="repairPlaceOperateRef"
    @reloadList="onReload" />
</template>
<script setup>
import { reactive, ref, onMounted } from 'vue';
import { message, Modal } from 'ant-design-vue';
import { PAGE_SIZE, PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
import { repairPlantApi } from '/@/api/business/business-material/repair-plant';
import BooleanFlagSelect from '/@/components/boolean-flag-select/index.vue';
import RepairPlaceOperateModal from './components/repair-place-operate-modal.vue';
import { useRouter } from 'vue-router'
const tableColumns = reactive([
  {
    title: '车辆维修厂家简称',
    dataIndex: 'repairPlantName',
    width: 260,
    ellipsis: true,
  },
  {
    title: '维修厂地址',
    dataIndex: 'addressDetail',
    width: 300,
    ellipsis: true,
    customRender: ({ record }) => {
      return `${record.addressArea || ''}${record.addressDetail||''}`;
    },
  },
  {
    title: '联系人',
    dataIndex: 'contactName',
    width: 120,
    ellipsis: true,
  },
  {
    title: '联系方式',
    dataIndex: 'contactPhone',
    width: 130,
  },
  {
    title: '状态',
    dataIndex: 'disabledFlag',
    width: 100,
  },
  {
    title: '创建人',
    dataIndex: 'createUserName',
    width: 120,
    ellipsis: true,
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    width: 170,
  },
  {
    title: '操作',
    dataIndex: 'action',
    fixed: 'right',
    width: 100,
  },
]);

const tableData = ref([]);

const total = ref(0);

const queryFormState = {
  searchWord: '',
  startDate: null,
  endDate: null,
  disabledFlag: null,
  pageNum: 1,
  pageSize: PAGE_SIZE,
};
const queryForm = reactive({ ...queryFormState });

const tableLoading = ref(false);

onMounted(() => {
  queryRepairPlantList();
});

// 查询列表
async function queryRepairPlantList() {
  try {
    tableLoading.value = true;
    const result = await repairPlantApi.queryRepairPlantList(queryForm);
    tableData.value = result.data.list;
    total.value = result.data.total;
  } catch (err) {
    console.log(err);
  } finally {
    tableLoading.value = false;
  }
}

// 点击查询
function onSearch() {
  queryForm.pageNum = 1;
  queryRepairPlantList();
}

// 点击重置
function onReload() {
  Object.assign(queryForm, queryFormState);
  searchDate.value = [];
  queryRepairPlantList();
}

// 日期选择
const searchDate = ref([]);
function dateChange(dates, dateStrings) {
  queryForm.startDate = dateStrings[0];
  queryForm.endDate = dateStrings[1];
}

// 新建、编辑
const repairPlaceOperateRef = ref();
function addOrUpdate(rowData = null) {
  repairPlaceOperateRef.value.showModal(rowData);
}

// 删除
function onDelete(repairPlantId) {
  Modal.confirm({
    title: '提示',
    content: '确认删除此数据吗?',
    onOk() {
      delRepairPlant(repairPlantId);
    },
  });
}

async function delRepairPlant(repairPlantId) {
  try {
    tableLoading.value = true;
    await repairPlantApi.delRepairPlant(repairPlantId);
    message.success('删除成功');
    if (tableData.value.length === 1 && queryForm.pageNum > 1) {
      queryForm.pageNum -= 1;
    }
    queryRepairPlantList();
  } catch (err) {
    console.log(err);
  } finally {
    tableLoading.value = false;
  }
}

const router = useRouter()
function toDetail({repairPlantId}){
  router.push({
    path:'/business/business-material/repair-place-detail',
    query:{
      repairPlantId
    }
  })
}
</script>
