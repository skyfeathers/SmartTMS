<template>
  <a-form class="smart-query-form" v-privilege="'transportRoute:query'">
    <a-row class="smart-query-form-row">
      <a-form-item label="关键字" class="smart-query-form-item">
        <a-input style="width: 300px" v-model:value="queryForm.keywords" placeholder="路线名称/创建人"/>
      </a-form-item>

      <a-form-item label="创建时间" class="smart-query-form-item">
        <a-space direction="vertical" :size="12">
          <a-range-picker v-model:value="searchDate" @change="dateChange"/>
        </a-space>
      </a-form-item>

      <a-form-item label="路线状态" class="smart-query-form-item">
        <a-radio-group v-model:value="queryForm.disabledFlag" @change="search">
          <a-radio-button :value="null">全部</a-radio-button>
          <a-radio-button value="false">启用</a-radio-button>
          <a-radio-button value="true">禁用</a-radio-button>
        </a-radio-group>
      </a-form-item>

      <a-form-item class="smart-query-form-item smart-margin-left10">
        <a-button type="primary" @click="search">
          <template #icon>
            <SearchOutlined/>
          </template>
          查询
        </a-button>
        <a-button @click="resetQuery">
          <template #icon>
            <ReloadOutlined/>
          </template>
          重置
        </a-button>
      </a-form-item>
    </a-row>
  </a-form>

  <a-card size="small" :bordered="false" :hoverable="true">
    <a-row class="smart-table-btn-block">
      <div class="smart-table-operate-block">
        <a-button @click="addOrUpdate()" v-privilege="'transportRoute:add'" type="primary" size="small">
          <template #icon>
            <PlusOutlined/>
          </template>
          新建运输路线
        </a-button>
      </div>
      <div class="smart-table-setting-block"></div>
    </a-row>
    <a-tabs v-model:activeKey="activeKey"  @change="changeTabs">
      <a-tab-pane v-for="item in $smartEnumPlugin.getValueDescList('TRANSPORTATION_TYPE_ENUM')"
                  :key="item.value"
                  :tab="item.desc" />
    </a-tabs>
    <TransportRouteTable :tableData="tableData" :transportRouteType="activeKey" @update="addOrUpdate" @delete="del"/>
    <div class="smart-query-table-page">
      <a-pagination
          showSizeChanger
          showQuickJumper
          show-less-items
          :pageSizeOptions="PAGE_SIZE_OPTIONS"
          :defaultPageSize="queryForm.pageSize"
          v-model:current="queryForm.pageNum"
          v-model:pageSize="queryForm.pageSize"
          :total="total"
          @change="ajaxQuery"
          @showSizeChange="ajaxQuery"
          :show-total="(total) => `共${total}条`"
      />
    </div>
    <!--新建编辑modal-->
    <TransportRouteOperateModal ref="operateModal" @reloadList="ajaxQuery"/>
  </a-card>
</template>
<script setup>
import TransportRouteOperateModal from './components/transport-route-operate-modal.vue';
import TransportRouteTable from './components/transport-route-table.vue';
import { ref, onMounted, onActivated, reactive, computed, nextTick } from 'vue';
import { TRANSPORTATION_TYPE_ENUM } from '/@/constants/business/transport-route-const';
import { PAGE_SIZE } from '/@/constants/common-const';
import { transportRouteApi } from '/@/api/business/business-material/transport-route-api';
import { useSpinStore } from '/@/store/modules/system/spin';
import { message } from 'ant-design-vue';
import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';

// 默认运输类型-集装箱运输
let activeKey = ref(TRANSPORTATION_TYPE_ENUM.CONTAINER_TRANSPORT.value);

// 查询条件
const queryFormState = {
  keywords: '',
  disabledFlag: null,
  endTime: null,
  startTime: null,
  transportRouteType: null,
  pageNum: 1,
  pageSize: PAGE_SIZE,
  searchCount: true
};

const queryForm = reactive({...queryFormState});
// 日期选择
let searchDate = ref();
function dateChange(dates, dateStrings) {
  queryForm.startTime = dateStrings[0];
  queryForm.endTime = dateStrings[1];
}

const tableData = ref([]);
const total = ref(0);

function search() {
  queryForm.pageNum = 1;
  ajaxQuery();
}

// 查询
async function ajaxQuery() {
  try {
    useSpinStore().show();
    queryForm.transportRouteType = activeKey.value;
    let responseModel = await transportRouteApi.pageQuery(queryForm);
    const list = responseModel.data.list;
    total.value = responseModel.data.total;
    tableData.value = list;
  } catch (e) {
    console.log(e);
  } finally {
    useSpinStore().hide();
  }
}

// 重置查询
function resetQuery() {
  searchDate.value = [];
  Object.assign(queryForm, queryFormState);
  ajaxQuery();
}

// 删除
async function del(transportRouteId) {
  try {
    useSpinStore().show();
    await transportRouteApi.delete(transportRouteId);
    message.success('删除成功');
    ajaxQuery();
  } catch (e) {
    console.log(e);
  } finally {
    useSpinStore().hide();
  }
}

// 编辑或新增
const operateModal = ref();
function addOrUpdate(rowData) {
  operateModal.value.showModal(rowData, activeKey.value);
}

// 重置查询条件
function changeTabs() {
  nextTick(() => {
    ajaxQuery()
  });
}

onMounted(() => {
  ajaxQuery();
});


onMounted(ajaxQuery);
</script>
