<!--
 * @Author: lp
 * @Date: 2022-07-13 17:58:53
 * @LastEditors: lp
 * @LastEditTime: 2022-07-17 16:51:55
 * @Description: 挂车管理
 * @FilePath: \nft-admin-web\src\views\business\bracket\bracket-list.vue
-->
<template>
  <div>
    <a-form class="smart-query-form" v-privilege="'bracket:query'">
      <a-row class="smart-query-form-row">
        <a-form-item class="smart-query-form-item" label="关键字">
          <a-input v-model:value="queryForm.keyWords" class="form-width" placeholder="挂车车牌号/创建人" allowClear/>
        </a-form-item>

        <!-- <a-form-item class="smart-query-form-item" label="型号">
          <a-input v-model:value="queryForm.type" class="form-width" placeholder="请输入型号" allowClear/>
        </a-form-item>

        <a-form-item class="smart-query-form-item" label="审核状态">
          <smart-enum-select v-model:value="queryForm.auditStatus" enumName="AUDIT_STATUS_ENUM" allowClear/>
        </a-form-item> -->

<!--        <a-form-item class="smart-query-form-item" label="交强险到期时间">-->
<!--          <a-range-picker v-model:value="compulsoryTrafficTimeRef" :ranges="defaultTimeRanges" class="form-width" @change="changeCompulsoryTrafficTime" allowClear/>-->
<!--        </a-form-item>-->

<!--        <a-form-item class="smart-query-form-item" label="商业险到期时间">-->
<!--          <a-range-picker v-model:value="commercialInsuranceTimeRef" :ranges="defaultTimeRanges" class="form-width" @change="changeCommercialInsuranceTime" allowClear/>-->
<!--        </a-form-item>-->

        <!-- <a-form-item class="smart-query-form-item" label="创建时间">
          <a-range-picker v-model:value="createTimeRef" :ranges="defaultTimeRanges" class="form-width" @change="changeCreateTime" allowClear/>
        </a-form-item> -->

        <a-form-item class="smart-query-form-item smart-margin-left10">
          <a-button type="primary" @click="search">
            <template #icon>
              <SearchOutlined />
            </template>
            查询
          </a-button>
          <a-button @click="resetQuery">
            <template #icon>
              <ReloadOutlined />
            </template>
            重置
          </a-button>
        </a-form-item>
      </a-row>
    </a-form>

    <a-card :bordered="false" :hoverable="true" size="small">
      <a-row class="smart-table-btn-block">
        <div class="smart-table-operate-block">
          <a-button size="small" type="primary" @click="addBracket()" v-privilege="'bracket:add'">
            <template #icon>
              <PlusOutlined />
            </template>
            新建挂车
          </a-button>
          <a-button
            :disabled="selectedRowKeyList.length == 0"
            size="small"
            @click="batchAuditBracket"
            v-privilege="'bracket:batchAudit'">
            <template #icon>
              <AuditOutlined/>
            </template>
            批量审核
          </a-button>
          <a-button
              :disabled="selectedRowKeyList.length == 0"
              size="small"
              v-privilege="'bracket:updateManager'"
              @click="showUpdateManagerModal()">
            <template #icon>
              <AuditOutlined/>
            </template>
            分配负责人
          </a-button>

          <a-button
              :disabled="selectedRowKeyList.length == 0"
              size="small"
              @click="showUpdateBusinessModeModal">
            <template #icon>
              <AuditOutlined/>
            </template>
            修改经营方式
          </a-button>

          <a-button @click="confirmDelete()" v-privilege="'bracket:delete'" :disabled="selectedRowKeyList.length == 0"
                    type="primary" danger size="small">删除
          </a-button>

          <a-button v-privilege="'bracket:quickCreate'" size="small" type="primary" @click="showQuickCreateModal()">
            快速新建
          </a-button>

          <a-button @click="exportExcel()" v-privilege="'bracket:export'" size="small">导出</a-button>
        </div>
        <div class="smart-table-setting-block"></div>
        <div>
          <SmartTableOperator v-model="columns" :tableId="TABLE_ID_CONST.BRACKET" :refresh="ajaxQuery" />
        </div>
      </a-row>

      <a-tabs @tabClick="changeTab" v-model:activeKey="activeKey">
        <a-tab-pane key="" tab="全部">
        </a-tab-pane>
        <a-tab-pane key="1" tab="内管">
        </a-tab-pane>
        <a-tab-pane key="2" tab="挂靠">
        </a-tab-pane>
        <a-tab-pane key="3" tab="外派">
        </a-tab-pane>
      </a-tabs>

      <a-table
        :columns="columns"
        :dataSource="tableData"
        :pagination="false"
        :row-selection="{ selectedRowKeys: selectedRowKeyList, onChange: onSelectChange }"
        rowKey="bracketId"
        :scroll="{ x: '100%' }"
        :loading="tableLoading"
        size="small"
        bordered
      >
        <template #headerCell="{ column }">
            <SmartHeaderCell v-model:value="queryForm[column.filterOptions?.key || column.dataIndex]" :column="column"
              @change="change" />
        </template>

        <template #bodyCell="{ text, record, index, column }">
          <template v-if="column.dataIndex === 'bracketNo'">
            <a v-if="$privilege('bracket:detail')" @click="bracketDetail(record.bracketId)">{{ record.bracketNo }}</a>
            <span v-else>{{record.bracketNo}}</span>
          </template>
          <template v-if="column.dataIndex === 'businessMode'">
            <span>{{ $smartEnumPlugin.getDescByValue('VEHICLE_BUSINESS_MODE_ENUM', text) }}</span>
          </template>
          <template v-if="column.dataIndex === 'createUserType'">
            {{ $smartEnumPlugin.getDescByValue('USER_TYPE_ENUM', text) }}
          </template>
          <template v-if="column.dataIndex === 'expireTime'">
            <span :class="{ expired: dateExpired(record.expireTime) }">{{
              dateHandle(record.expireTime)
            }}</span>
          </template>
          <template v-if="column.dataIndex === 'roadTransportCertificateExpireTime'">
            <span :class="{ expired: dateExpired(record.roadTransportCertificateExpireTime) }">{{
              dateHandle(record.roadTransportCertificateExpireTime)
            }}</span>
          </template>
          <template v-if="column.dataIndex === 'compulsoryTrafficExpireTime'">
            <span :class="{ expired: dateExpired(record.compulsoryTrafficExpireTime) }">{{
              dateHandle(record.compulsoryTrafficExpireTime)
            }}</span>
          </template>
          <template v-if="column.dataIndex === 'commercialExpireTime'">
            <span :class="{ expired: dateExpired(record.commercialExpireTime) }">{{
              dateHandle(record.commercialExpireTime)
            }}</span>
          </template>
          <template v-if="column.dataIndex === 'auditStatus'">
            <a-tag v-show="text === AUDIT_STATUS_ENUM.AUDIT_PASS.value" color="success">{{ AUDIT_STATUS_ENUM.AUDIT_PASS.desc }}</a-tag>
            <a-tag v-show="text === AUDIT_STATUS_ENUM.WAIT_AUDIT.value" color="warning">{{ AUDIT_STATUS_ENUM.WAIT_AUDIT.desc }}</a-tag>
            <a-tag v-show="text === AUDIT_STATUS_ENUM.REJECT.value" color="error">{{ AUDIT_STATUS_ENUM.REJECT.desc }}</a-tag>
          </template>
           <template v-if="column.dataIndex === 'action'">
            <div class="smart-table-operate">
              <a-button type="link" @click="addBracket(record.bracketId)" v-privilege="'bracket:edit'">编辑</a-button>
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
    </a-card>
    <AuditModal ref="auditModal" @refresh="handleFinish"/>
    <UpdateManagerModal ref="updateManagerRef" @refresh="handleFinish"/>
    <UpdateBusinessModeModal ref="updateBusinessModeRef" @refresh="handleFinish"/>
    <!-- 快速创建司机、车辆、挂车 -->
    <VehicleQuickCreate ref="quickCreateRef" @reloadList="handleFinish" />
  </div>
</template>
<script setup>
import SmartEnumSelect from '/@/components/smart-enum-select/index.vue';
import AuditModal from '/@/components/audit-modal/index.vue'
import UpdateManagerModal from "/@/components/update-manager-modal/index.vue";
import UpdateBusinessModeModal from "/@/components/update-business-mode/index.vue";
import SmartTableOperator from '/@/components/smart-table-operator/index.vue';
import VehicleQuickCreate from '/@/components/vehicle-quick-create/index.vue';
import { QUICK_CREATE_TYPE_ENUM } from '/@/constants/business/vehicle-const';
import { TABLE_ID_CONST } from '/@/constants/table-id-const';
import { onMounted, reactive, ref } from 'vue';
import { message, Modal } from 'ant-design-vue';
import { useSpinStore } from '/@/store/modules/system/spin';
import { AUDIT_STATUS_ENUM, PAGE_SIZE, PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
import { useRouter } from 'vue-router';
import { bracketApi } from '/@/api/business/bracket/bracket-api';
import { useDriverCertificateValidity } from '/@/views/business/driver/hooks/use-driver-certificate-validity';
import _ from 'lodash';
import {defaultTimeRanges} from "/@/lib/default-time-ranges"
import { SmartLoading } from '/@/components/smart-loading';
import { driverApi } from '/@/api/business/driver/driver-api';
import useDragTable from '/@/hook/use-drag-table/index';
import SmartHeaderCell from '/@/components/smart-table-header-cell/index.vue'
// --------------------- 列表查询 ------------------------//
const queryFormDefault = {
  commercialInsuranceEndTime: undefined,
  commercialInsuranceStartTime: undefined,
  compulsoryTrafficEndTime: undefined,
  compulsoryTrafficStartTime: undefined,
  drivingLicenseEndTime: undefined,
  drivingLicenseStartTime: undefined,
  roadTransportCertificateEndTime: undefined,
  roadTransportCertificateStartTime: undefined,
  vehicleAuditEndTime: undefined,
  vehicleAuditStartTime: undefined,
  createStartTime: undefined,
  createEndTime: undefined,
  searchCount: true,
  keyWords: '',
  type: undefined,
  auditStatus: undefined,
  pageNum: 1,
  pageSize: PAGE_SIZE,
  bracketNo:undefined,
  managerId:undefined,
  enterpriseId:undefined,
  tonnage:[],
  tonnageStart:undefined,
  tonnageEnd:undefined,
  weight:[],
  weightStart:undefined,
  weightEnd:undefined,
  createUserType:undefined,
  createUserName:undefined,
  createTime:[],
  businessMode: undefined,
};
const queryForm = reactive({ ...queryFormDefault });
const tableLoading = ref(false);
const selectedRowKeyList = ref([]);
const tableData = ref([]);
const total = ref(0);

// const compulsoryTrafficTimeRef = ref([]); // 交强险到期时间
// const commercialInsuranceTimeRef = ref([]); // 商业险到期时间
// const createTimeRef = ref([]); // 创建时间

// 列表名称
const { columnsData:columns, tableWidth } = useDragTable([
  {
    title: '挂车车牌号',
    dataIndex: 'bracketNo',
    fixed: 'left',
    width: 150,
    filterOptions:{
        type:'input',
        key:'bracketNo'
    }
  },
  {
    title: '负责人',
    dataIndex: 'managerName',
    width: 150,
    filterOptions:{
        type:'employee-select',
        key:'managerId',
        leaveFlag:false
    }
  },
  {
    title: '经营方式',
    dataIndex: 'businessMode',
    width: 150,
    filterOptions:{
      type:'enum-select',
      enumName:'VEHICLE_BUSINESS_MODE_ENUM'
    }
  },
  {
    title: '重量（吨）',
    dataIndex: 'weight',
    width: 150,
    filterOptions:{
        type:'number-range',
    },
  },
  {
    title: '载重（吨）',
    dataIndex: 'tonnage',
    width: 150,
    filterOptions:{
        type:'number-range',
    },
  },
  {
    title: '型号',
    dataIndex: 'type',
    filterOptions:{
        type:'input',
    },
    width: 150,
  },
  {
    title: '交强险到期时间',
    dataIndex: 'compulsoryTrafficExpireTime',
    width: 200,
  },
  {
    title: '商业险到期时间',
    dataIndex: 'commercialExpireTime',
    width: 200,
  },
  {
    title: '审核状态',
    dataIndex: 'auditStatus',
    width: 150,
    filterOptions:{
        type:'enum-select',
        enumName:'AUDIT_STATUS_ENUM'
    }
  },
  {
    title: '创建人',
    dataIndex: 'createUserName',
    width: 150,
    filterOptions:{
        type:'input',
    },
  },
  {
    title: '创建人类型',
    dataIndex: 'createUserType',
    width: 150,
    filterOptions:{
        type:'enum-select',
        enumName:'USER_TYPE_ENUM'
    },
  },
  {
    title: '创建时间',
    width: 200,
    dataIndex: 'createTime',
    filterOptions:{
      type:'date-range',
      ranges:true
    }
  },
  {
    title: '操作',
    dataIndex: 'action',
    fixed: 'right',
    width: 70,
    filterOptions:{
      type:'submit',
      btnType:'link'
    }
  },
],TABLE_ID_CONST.BRACKET);


const activeKey = ref('');
function changeTab (value) {
  queryForm.businessMode = value;
  ajaxQuery();
}


function change(obj){
  if(obj.key == 'createTime'){
    queryForm.createStartTime = obj.value[0];
    queryForm.createEndTime = obj.value[1];
    search()
    return
  }
  if(obj.key == 'weight'){
    queryForm.weightStart = obj.value[0];
    queryForm.weightEnd = obj.value[1];
    search()
    return
  }
  if(obj.key == 'tonnage'){
    queryForm.tonnageStart = obj.value[0];
    queryForm.tonnageEnd = obj.value[1];
    search()
    return
  }
  if(obj.search){
    search()
  }
}

// 有效期校验
let { dateHandle, dateExpired } = useDriverCertificateValidity();

// 审核弹窗引用
let auditModal = ref();

// 列表选择
function onSelectChange(selectedRowKeys) {
  selectedRowKeyList.value = selectedRowKeys;
}

// 重置操作
function resetQuery() {
  Object.assign(queryForm, queryFormDefault);
  selectedRowKeyList.value = [];
  ajaxQuery();
}

function search () {
  queryForm.pageNum = 1;
  ajaxQuery();
}

// 获取列表数据
async function ajaxQuery() {
  try {
    tableLoading.value = true;
    let responseModel = await bracketApi.queryBracketList(queryForm);
    const list = responseModel.data.list;
    total.value = responseModel.data.total;
    tableData.value = list;
  } catch (e) {
    console.log(e);
  } finally {
    tableLoading.value = false;
  }
}

// // ----------------------- 挂车操作 -----------------------
// 删除操作
function confirmDelete() {
  Modal.confirm({
      title: `确认要删除${selectedRowKeyList.value.length}个挂车吗？`,
      content: "删除后，该信息将不可恢复",
      okText: "删除",
      okType: "danger",
      onOk() {
          deleteBracket(selectedRowKeyList.value);
      },
      cancelText: "取消",
      onCancel() {
      },
  });
}

// 删除
async function deleteBracket(bracketId) {
  try {
      useSpinStore().show();
      await bracketApi.bracketBatchDelete(bracketId);
      message.success('删除成功');
      ajaxQuery();
  } catch (e) {
      console.log(e);
  } finally {
      useSpinStore().hide();
  }
}

// 批量审核
function batchAuditBracket() {
  for (const item of tableData.value) {
    if (_.includes(selectedRowKeyList.value, item.bracketId)) {
      if (item.auditStatus !== AUDIT_STATUS_ENUM.WAIT_AUDIT.value) {
        message.warning('请选择待审核的挂车！请移除【' + item.bracketNo + '】');
        return;
      }
    }
  }
  let params = {
    bracketIdList: selectedRowKeyList.value
  };
  auditModal.value.showModal(params, bracketApi.batchAudit);
}

// 审核
function auditBracket(bracketId) {
  let params = { bracketIdList: [bracketId] };
  auditModal.value.showModal(params, bracketApi.batchAudit);
}


// 修改负责人
const updateManagerRef = ref();
function showUpdateManagerModal () {
  let bracketIdList = selectedRowKeyList.value;
  let params = { bracketIdList };
  updateManagerRef.value.showModal(params, bracketApi.batchUpdateManager);
}

// 修改经营方式
const updateBusinessModeRef = ref();
function showUpdateBusinessModeModal () {
  if (selectedRowKeyList.value.length > 1) {
    message.error('请选择一条单据');
    return;
  }
  let bracketId =  selectedRowKeyList.value[0];
  let params = { bracketId };
  updateBusinessModeRef.value.showModal(params, bracketApi.businessModeUpdate);
}

function handleFinish(){
  selectedRowKeyList.value = [];
  ajaxQuery();
}

// ----------------- 显示快速创建弹窗 --------------------
const quickCreateRef = ref();

function showQuickCreateModal () {
  quickCreateRef.value.showModal(QUICK_CREATE_TYPE_ENUM.BRACKET.value);
}


// ----------------- 跳转 -------------------- //
let router = useRouter();

// 挂车添加编辑
function addBracket(bracketId) {
  router.push({ path: '/bracket/operate', query: { bracketId } });
}

// 挂车详情
function bracketDetail(bracketId) {
  router.push({ path: '/bracket/detail', query: { bracketId } });
}

// ------------ 导出 Excel -----------
function exportExcel () {
  SmartLoading.show();
  let params = _.cloneDeep(queryForm);
  bracketApi.downloadExcel('挂车列表.xlsx', params);
  SmartLoading.hide();
}

onMounted(()=> {
    ajaxQuery();
});
</script>
<style lang="less" scoped>
.form-width {
  width: 240px;
}

.expired {
  color: red;
}
</style>
