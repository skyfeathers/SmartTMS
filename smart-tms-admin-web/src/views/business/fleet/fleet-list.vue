<template>
  <a-form class="smart-query-form" v-privilege="'fleet:query'">
    <a-row class="smart-query-form-row">
      <a-form-item class="smart-query-form-item" label="关键字">
        <a-input
            v-model:value="queryForm.keyWords"
            class="form-width"
            placeholder="车队名称/车队长/联系方式/创建人"
        />
      </a-form-item>

      <a-form-item class="smart-query-form-item" label="注册地址">
        <smart-area-cascader width="240px" v-model:value="queryForm.registerArea"/>
      </a-form-item>

      <a-form-item class="smart-query-form-item" label="审核状态">
        <smart-enum-select v-model:value="queryForm.auditStatus" enumName="AUDIT_STATUS_ENUM"/>
      </a-form-item>

      <a-form-item class="smart-query-form-item" label="创建时间">
        <a-range-picker v-model:value="queryForm.createTime" :presets="defaultTimeRanges" class="form-width"
                        @change="changeCreateTime"/>
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

  <a-card :bordered="false" :hoverable="true" size="small">
    <a-row class="smart-table-btn-block">
      <div class="smart-table-operate-block">
        <a-button size="small" type="primary" @click="addFleet()" v-privilege="'fleet:add'">
          <template #icon>
            <PlusOutlined/>
          </template>
          新建车队
        </a-button>

        <a-button
            :disabled="selectedRowKeyList.length == 0"
            size="small"
            @click="auditFleet()"
            v-privilege="'fleet:batchAudit'">
          <template #icon>
            <AuditOutlined/>
          </template>
          批量审核
        </a-button>

        <a-button
            :disabled="selectedRowKeyList.length == 0"
            size="small"
            @click="showUpdateManagerModal()"
            v-privilege="'fleet:updateManager'">
          <template #icon>
            <AuditOutlined/>
          </template>
          分配负责人
        </a-button>

        <a-button @click="confirmDelete(selectedRowKeyList,ajaxQuery)" :disabled="selectedRowKeyList.length == 0"
                  v-privilege="'fleet:delete'" size="small" type="primary" danger >删除
        </a-button>
      </div>
      <div class="smart-table-setting-block"></div>
      <div>
        <SmartTableOperator v-model="columns" :tableId="TABLE_ID_CONST.FLEET" :refresh="ajaxQuery" />
      </div>
    </a-row>

    <a-table
        :columns="columns"
        :dataSource="tableData"
        :pagination="false"
        :row-selection="{ selectedRowKeys: selectedRowKeyList, onChange: onSelectChange }"
        :scroll="{ x: 1300, y: 500 }"
        :loading="tableLoading"
        rowKey="fleetId"
        size="small"
        bordered
    >
      <template #bodyCell="{ text, record, index, column }">
        <template v-if="column.dataIndex === 'fleetName'">
          <a @click="fleetDetail(record.fleetId)" v-if="$privilege('fleet:detail')">{{ record.fleetName }}</a>
          <span v-else>{{ record.fleetName }}</span>
        </template>
        <template v-if="column.dataIndex === 'captainName'">
          {{ record.captainName }} - {{ record.captainPhone }}
        </template>
        <template v-if="column.dataIndex === 'registerArea'">
          {{ record.provinceName }}{{ record.cityName }}{{ record.districtName }}
        </template>
        <template v-if="column.dataIndex === 'auditStatus'">
          <a-tag v-show="text === AUDIT_STATUS_ENUM.AUDIT_PASS.value" color="success">{{ AUDIT_STATUS_ENUM.AUDIT_PASS.desc }}</a-tag>
          <a-tag v-show="text === AUDIT_STATUS_ENUM.WAIT_AUDIT.value" color="warning">{{ AUDIT_STATUS_ENUM.WAIT_AUDIT.desc }}</a-tag>
          <a-tag v-show="text === AUDIT_STATUS_ENUM.REJECT.value" color="error">{{ AUDIT_STATUS_ENUM.REJECT.desc }}</a-tag>
        </template>
        <template v-if="column.dataIndex === 'action'">
          <div class="smart-table-operate">
            <a-button @click="addFleet(record.fleetId)" v-privilege="'fleet:edit'" type="link">编辑</a-button>
          </div>
        </template>
      </template>

    </a-table>

    <div class="smart-query-table-page">
      <a-pagination
          v-model:current="queryForm.pageNum"
          v-model:pageSize="queryForm.pageSize"
          :defaultPageSize="queryForm.pageSize"
          :pageSizeOptions="PAGE_SIZE_OPTIONS"
          :show-total="(total) => `共${total}条`"
          :total="total"
          show-less-items
          showQuickJumper
          showSizeChanger
          @change="ajaxQuery"
          @showSizeChange="ajaxQuery"
      />
    </div>
    <AuditModal ref="auditModal" @refresh="handleFinish"/>
    <UpdateManagerModal ref="updateManagerRef" @refresh="handleFinish"/>
  </a-card>
</template>
<script setup>
import SmartEnumSelect from "/@/components/smart-enum-select/index.vue";
import UpdateManagerModal from "/@/components/update-manager-modal/index.vue";
import SmartAreaCascader from "/@/components/smart-area-cascader/index.vue"
import AuditModal from '/@/components/audit-modal/index.vue'
import SmartTableOperator from '/@/components/smart-table-operator/index.vue';

import { TABLE_ID_CONST } from '/@/constants/table-id-const';
import { onMounted, reactive, ref } from 'vue';
import { message } from "ant-design-vue";
import { AUDIT_STATUS_ENUM, PAGE_SIZE, PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
import { defaultTimeRanges } from '/@/lib/default-time-ranges';
import { fleetApi } from '/@/api/business/fleet/fleet-api';
import { useRouter } from 'vue-router';
import { useFleetDelete } from '/@/views/business/fleet/hooks/use-fleet-delete';
import _ from 'lodash';
// --------------------- 列表查询 ------------------------
const columns = ref([
  {
    title: "车队名称",
    dataIndex: "fleetName",
    width:180,
    fixed: "left",
  },
  {
    title: "车队长-联系方式",
    width:180,
    dataIndex: "captainName",
  },
  {
    title: "身份证号",
    dataIndex: "captainIdCard",
    width:180,
  },
  {
    title: "注册地址",
    width:100,
    dataIndex: "registerArea",
  },
  {
    title: "审核状态",
    dataIndex: "auditStatus",
    width:100,
  },
  {
    title: "创建人",
    width:100,
    dataIndex: "createUserName",
  },
  {
    title: "创建时间",
    width:180,
    dataIndex: "createTime",
  },
  {
    title: "操作",
    dataIndex: "action",
    fixed: "right",
    width: 50,
  },
]);

const queryFormState = {
  keyWords: "",
  auditStatus: undefined,
  createStartTime: undefined,
  createEndTime: undefined,
  createTime: [],
  registerAreaCode:undefined,
  registerArea:[],
  pageNum: 1,
  pageSize: PAGE_SIZE,
};
const queryForm = reactive({...queryFormState});
const tableLoading = ref(false);
const selectedRowKeyList = ref([]);
const tableData = ref([]);
const total = ref(0);

function changeCreateTime(dates, dateStrings) {
  queryForm.createStartTime = dateStrings[0];
  queryForm.createEndTime = dateStrings[1];
}

function onSelectChange(selectedRowKeys) {
  selectedRowKeyList.value = selectedRowKeys;
}

function resetQuery() {
  Object.assign(queryForm, queryFormState);
  selectedRowKeyList.value = [];
  ajaxQuery();
}

function search () {
  queryForm.pageNum = 1;
  ajaxQuery();
}

async function ajaxQuery() {
  try {
    tableLoading.value = true;
    if(_.isEmpty(queryForm.registerArea)){
      queryForm.registerAreaCode = null;
    }else {
      queryForm.registerAreaCode = queryForm.registerArea[queryForm.registerArea.length - 1].value;
    }
    let responseModel = await fleetApi.queryFleet(queryForm);
    const list = responseModel.data.list;
    total.value = responseModel.data.total;
    tableData.value = list;
  } catch (e) {
    console.log(e);
  } finally {
    tableLoading.value = false;
  }
}

// ----------------------- 车队操作 -----------------------

let { confirmDelete }  = useFleetDelete();

// ----------------- 审核 --------------------
let auditModal = ref();

function auditFleet() {
  for (const item of tableData.value) {
    if (_.includes(selectedRowKeyList.value, item.fleetId)) {
      if (item.auditStatus !== AUDIT_STATUS_ENUM.WAIT_AUDIT.value) {
        message.warning('请选择待审核的车队！请移除【' + item.fleetName + '】');
        return;
      }
    }
  }

  let fleetIdList = selectedRowKeyList.value;
  let params = { fleetIdList };
  auditModal.value.showModal(params,fleetApi.batchAuditFleet);
}

function handleFinish(){
  selectedRowKeyList.value = [];
  ajaxQuery();
}

const updateManagerRef = ref();

function showUpdateManagerModal () {
  let fleetIdList = selectedRowKeyList.value;
  let params = { fleetIdList };
  updateManagerRef.value.showModal(params, fleetApi.batchUpdateManager);
}

// ----------------- 跳转 --------------------
let router = useRouter();
function addFleet(fleetId) {
  router.push({path:'/fleet/operate',query:{fleetId}})
}
function fleetDetail(fleetId){
  router.push({path:'/fleet/detail',query:{fleetId}})
}

onMounted(ajaxQuery);
</script>
<style lang="less" scoped>
.form-width {
  width: 240px;
}

</style>
