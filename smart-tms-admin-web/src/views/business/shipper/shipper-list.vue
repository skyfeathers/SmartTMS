<!--
 * @Description: 货主列表
 * @version:
 * @Author: lidoudou
 * @Date: 2022-07-05 15:41:39
 * @LastEditors: lidoudou
 * @LastEditTime: 2022-07-07 16:59:40
-->
<template>
  <a-form class="smart-query-form" v-privilege="'shipper:query'">
    <a-row class="smart-query-form-row">
      <a-form-item label="关键字" class="smart-query-form-item">
        <a-input style="width: 200px" v-model:value="queryForm.keyWords" placeholder="货主名称/货主简称/创建人" />
      </a-form-item>
      <a-form-item class="smart-query-form-item smart-margin-left10">
        <a-button type="primary" @click="search">
          <template #icon>
            <SearchOutlined />
          </template>
          查询
        </a-button>
        <a-button @click="resetQuery" class="smart-margin-left10">
          <template #icon>
            <ReloadOutlined />
          </template>
          重置
        </a-button>
      </a-form-item>
    </a-row>
  </a-form>

  <a-card size="small" :bordered="false" :hoverable="true">
    <a-row class="smart-table-btn-block">
      <div class="smart-table-operate-block">
        <a-button @click="operateShipper()" type="primary" size="small" v-privilege="'shipper:add'">
          <template #icon>
            <PlusOutlined />
          </template>
          新建货主
        </a-button>

        <a-button @click="showManagerModal" size="small" :disabled="selectedRowKeyList.length == 0"
          v-privilege="'shipper:updateManager'">
          分配业务负责人
        </a-button>

        <a-button @click="showPrincipalModal(PRINCIPAL_TYPE_ENUM.CUSTOMER_SERVICE.value)" size="small"
          :disabled="selectedRowKeyList.length == 0" v-privilege="'shipper:updateCustomerService'">
          分配客服负责人
        </a-button>


        <a-button @click="batchAudit" size="small" :disabled="selectedRowKeyList.length == 0"
          v-privilege="'shipper:batchAudit'"> 批量审核
        </a-button>

        <a-button @click="addTrack" size="small" :disabled="selectedRowKeyList.length == 0"
          v-privilege="'shipper:addTrack'"> 添加跟进记录
        </a-button>

        <a-button @click="batchUpdateVerify" size="small" :disabled="selectedRowKeyList.length == 0"
          v-privilege="'shipper:batchUpdateVerify'"> 校验信息
        </a-button>

        <a-button @click="confirmDelete" size="small" type="primary" danger :disabled="selectedRowKeyList.length == 0"
          v-privilege="'shipper:delete'"> 删除
        </a-button>

        <a-button v-privilege="'shipper:export'" size="small" @click="exportExcel"> 导出
        </a-button>
      </div>
      <div class="smart-table-setting-block"></div>
      <div>
        <SmartTableOperator v-model="columns" :tableId="TABLE_ID_CONST.SHIPPER" :refresh="ajaxQuery" />
      </div>
    </a-row>


    <a-tabs v-model:activeKey="activeKey" @tabClick="changeTab">
      <a-tab-pane key="all" tab="全部">
      </a-tab-pane>
      <a-tab-pane key="noManager" tab="无业务负责人">
      </a-tab-pane>
    </a-tabs>

    <a-table :scroll="{ x: tableWidth, y: 500 }" size="small" :dataSource="tableData" :columns="columns" rowKey="shipperId"
      :pagination="false" :row-selection="{ selectedRowKeys: selectedRowKeyList, onChange: onSelectChange }"
      :loading="tableLoading" bordered>
      <template #headerCell="{ column }">
        <SmartHeaderCell v-model:value="queryForm[column.filterOptions?.key || column.dataIndex]" :column="column"
          @change="change" />
      </template>
      <template #shortName="{ record, text }">
        <div style="display:flex;width:100%;">
          <div style="width:calc(100% - 32px)">
            <TextEllipsis v-if="$privilege('shipper:detail')" :text="text" classKey="shortName">
              <a @click="detailShipper(record.shipperId)">{{ text }}</a>
            </TextEllipsis>
            <TextEllipsis v-else :text="text" classKey="shortName">
              <span>{{ text }}</span>
            </TextEllipsis>
          </div>
          <SmartCopyIcon :value="text" />
        </div>
      </template>
      <template #consignor="{ record, text }">
        <div style="display:flex;width:100%;">
          <div style="width:calc(100% - 32px)">
            <TextEllipsis v-if="$privilege('shipper:detail')" :text="text" classKey="consignor">
              <a @click="detailShipper(record.shipperId)">{{ text }}</a>
            </TextEllipsis>
            <TextEllipsis v-else :text="text" classKey="consignor">
              <span>{{ text }}</span>
            </TextEllipsis>
          </div>
          <SmartCopyIcon :value="text" />
        </div>
      </template>
      <template #area="{ record }">
        <span>{{ getArea(record.area) }}</span>
      </template>

      <template #verifyFlag="{ text }">
        <span>{{ text ? '已校验' : '待校验' }}</span>
      </template>

      <template #shipperNature="{ text }">
        <span>{{ $smartEnumPlugin.getDescByValue('SHIPPER_NATURE_ENUM', text) }}</span>
      </template>

      <template #tagType="{ text }">
        <span>{{ $smartEnumPlugin.getDescByValue('SHIPPER_TAG_ENUM', text) }}</span>
      </template>

      <template #grade="{ text }">
        <span>{{ $smartEnumPlugin.getDescByValue('SHIPPER_GRADE_ENUM', text) }}</span>
      </template>

      <template #shipperTypeList="{ record }">
        <span>{{ getShipperTypeDesc(record.shipperTypeList) }}</span>
      </template>

      <template #managerName="{ record }">
        <span>{{ getNameByType(record.shipperPrincipalList, PRINCIPAL_TYPE_ENUM.MANAGER.value) }}</span>
      </template>

      <template #customerServiceList="{ record }">
        <TextEllipsis :text="getNameByType(record.shipperPrincipalList, PRINCIPAL_TYPE_ENUM.CUSTOMER_SERVICE.value)"
          classKey="customerServiceList">
          <span>{{ getNameByType(record.shipperPrincipalList, PRINCIPAL_TYPE_ENUM.CUSTOMER_SERVICE.value) }}</span>
        </TextEllipsis>
      </template>

      <template #auditStatus="{ text }">
        <a-tag v-show="text === AUDIT_STATUS_ENUM.AUDIT_PASS.value" color="success">{{
    AUDIT_STATUS_ENUM.AUDIT_PASS.desc
  }}
        </a-tag>
        <a-tag v-show="text === AUDIT_STATUS_ENUM.WAIT_AUDIT.value" color="warning">{{
    AUDIT_STATUS_ENUM.WAIT_AUDIT.desc
  }}
        </a-tag>
        <a-tag v-show="text === AUDIT_STATUS_ENUM.REJECT.value" color="error">{{
    AUDIT_STATUS_ENUM.REJECT.desc
  }}
        </a-tag>
      </template>

      <template #action="{ record }">
        <div class="smart-table-operate">
          <a-button v-privilege="'shipper:edit'" type="link" @click="operateShipper(record.shipperId)">编辑</a-button>
        </div>

      </template>
    </a-table>

    <div class="smart-query-table-page">
      <a-pagination showSizeChanger showQuickJumper show-less-items :pageSizeOptions="PAGE_SIZE_OPTIONS"
        :defaultPageSize="queryForm.pageSize" v-model:current="queryForm.pageNum" v-model:pageSize="queryForm.pageSize"
        :total="total" @change="ajaxQuery" @showSizeChange="ajaxQuery" :show-total="(total) => `共${total}条`" />
    </div>

    <update-manager-modal ref="updateManagerRef" :shipperIdList="selectedRowKeyList" @reloadList="ajaxQuery" />
    <UpdatePrincipalModal ref="updatePrincipalRef" :shipperIdList="selectedRowKeyList" @reloadList="ajaxQuery" />

    <add-track-modal ref="trackModalRef" @reloadList="ajaxQuery" />

    <AuditModal ref="auditModal" @refresh="auditHandleFinish" />
  </a-card>
</template>
<script setup>
import UpdateManagerModal from './components/update-manager-modal.vue';
import UpdatePrincipalModal from './components/update-principal-modal.vue';
import AddTrackModal from './components/add-track-modal.vue';
import AuditModal from '/@/components/audit-modal/index.vue';
import SmartTableOperator from '/@/components/smart-table-operator/index.vue';
import SmartHeaderCell from '/@/components/smart-table-header-cell/index.vue'
import { TABLE_ID_CONST } from '/@/constants/table-id-const';
import { SYSTEM_CONFIG_KEY_ENUM } from '/@/constants/system/system-config-const';
import { reactive, ref, inject, onMounted } from 'vue';
import { message, Modal } from 'ant-design-vue';
import { useSpinStore } from '/@/store/modules/system/spin';
import { shipperApi } from '/@/api/business/shipper/shipper-api';
import { AUDIT_STATUS_ENUM, PAGE_SIZE, PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
import { PRINCIPAL_TYPE_ENUM } from '/@/constants/business/shipper-const';
import { router } from '/@/router/index';
import _ from 'lodash';
import { SmartLoading } from '/@/components/smart-loading';
import useDragTable from '/@/hook/use-drag-table/index';
import TextEllipsis from '/@/components/text-ellipsis/index.vue'
let smartEnumPlugin = inject('smartEnumPlugin');
// ----------- table 查询操作 start -----------
const { columnsData:columns, tableWidth } = useDragTable([
  {
    title: '货主简称',
    dataIndex: 'shortName',
    width: 150,
    fixed: 'left',
    ellipsis: true,
    slots: { customRender: 'shortName' },
    filterOptions: {
      type: 'input',
    }
  },
  {
    title: '货主名称',
    dataIndex: 'consignor',
    slots: { customRender: 'consignor' },
    width: 150,
    ellipsis: true,
    filterOptions: {
      type: 'input',
    }
  },
  {
    title: '所在地区',
    dataIndex: 'area',
    width: 150,
    slots: { customRender: 'area' },
    filterOptions: {
      type: 'dict-select',
      keyCode: 'AREA',
      multiple: true,
      key: 'areaList'
    }
  },
  {
    title: '货主类型',
    dataIndex: 'shipperNature',
    width: 150,
    slots: { customRender: 'shipperNature' },
    filterOptions: {
      type: 'enum-select',
      enumName: 'SHIPPER_NATURE_ENUM',
    }
  },
  {
    title: '校验状态',
    dataIndex: 'verifyFlag',
    width: 150,
    slots: { customRender: 'verifyFlag' },
    filterOptions: {
      type: 'boolean-select',
    }
  },
  {
    title: '标签',
    dataIndex: 'tagType',
    slots: { customRender: 'tagType' },
    width: 150,
    filterOptions: {
      type: 'enum-select',
      enumName: 'SHIPPER_TAG_ENUM',
      multiple: true,
      key: 'tagTypeList'
    }
  },
  {
    title: '等级',
    dataIndex: 'grade',
    width: 150,
    slots: { customRender: 'grade' },
    filterOptions: {
      type: 'enum-select',
      enumName: 'SHIPPER_GRADE_ENUM',
      multiple: true,
      key: 'gradeList'
    }
  },
  {
    title: '业务关系',
    dataIndex: 'shipperTypeList',
    width: 150,
    slots: { customRender: 'shipperTypeList' },
    filterOptions: {
      type: 'enum-select',
      enumName: 'SHIPPER_TYPE_ENUM',
      multiple: true,
      key: 'shipperTypeList'
    }
  },
  {
    title: '税点',
    dataIndex: 'taxPoint',
    width: 150,
  },
  {
    title: '业务负责人',
    dataIndex: 'managerName',
    width: 150,
    slots: { customRender: 'managerName' },
    filterOptions: {
      type: 'role-employee-select',
      systemConfigKey: SYSTEM_CONFIG_KEY_ENUM.SALES_ROLE_CODE.value,
      key: 'managerId'
    }
  },
  {
    title: '客服负责人',
    dataIndex: 'customerServiceList',
    slots: { customRender: 'customerServiceList' },
    width: 150,
    ellipsis: true,
    filterOptions: {
      type: 'role-employee-select',
      systemConfigKey: SYSTEM_CONFIG_KEY_ENUM.CUSTOMER_SERVICE_ROLE_CODE.value,
      key: 'customerServiceId'
    }
  },
  {
    title: '最近跟进记录',
    dataIndex: 'lastTrackContent',
    width: 150,
    ellipsis: true,
  },
  {
    title: '最近跟进时间',
    dataIndex: 'lastTrackTime',
    width: 200,
  },
  {
    title: '审核状态',
    dataIndex: 'auditStatus',
    width: 150,
    slots: { customRender: 'auditStatus' },
    filterOptions: {
      type: 'enum-select',
      enumName: 'AUDIT_STATUS_ENUM'
    }
  },
  {
    title: '创建人',
    dataIndex: 'createUserName',
    width: 150,
    filterOptions: {
      type: 'input',
    }
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    width: 200,
    filterOptions: {
      type: 'date-range',
      ranges: true
    }
  },
  {
    title: '操作',
    dataIndex: 'action',
    fixed: 'right',
    slots: { customRender: 'action' },
    width: 100,
    filterOptions: {
      type: 'submit',
      btnType: 'link'
    }
  },
], TABLE_ID_CONST.SHIPPER);

function change(obj) {
  if (obj.key == 'createTime') {
    queryForm.startTime = obj.value[0];
    queryForm.endTime = obj.value[1];
    search()
    return
  }
  if (obj.search) {
    search()
  }
}

const queryFormState = {
  pageNum: 1,
  pageSize: PAGE_SIZE,
  keyWords: '',
  gradeList: [], // 等级
  tagTypeList: [], // 标签
  areaList: [],
  shipperTypeList: [], // 业务关系
  shipperNature: null, // 业务关系
  startTime: '',
  endTime: '',
  customerServiceId: null,
  managerId: null,
  enterpriseId: null,
  noManagerFlag: null,
  //审核状态
  auditStatus: null,
  shortName: undefined,
  consignor: undefined,
  verifyFlag: undefined,
  createUserName: undefined,
  createTime: []
};
const queryForm = reactive({ ...queryFormState });
const createDateRange = ref([]);
const tableLoading = ref(false);
const tableData = ref([]);
const total = ref(0);

function resetQuery() {
  Object.assign(queryForm, queryFormState);
  createDateRange.value = [];
  ajaxQuery();
}

function search() {
  queryForm.pageNum = 1;
  ajaxQuery();
}

const activeKey = ref('all');

function changeTab (value) {
  if (value == 'noManager'){
    queryForm.noManagerFlag = true;
  }else {
    queryForm.noManagerFlag = null;
  }
  ajaxQuery();
}

async function ajaxQuery() {
  try {
    tableLoading.value = true;
    let responseModel = await shipperApi.queryShipper(queryForm);
    const list = responseModel.data.list;
    total.value = responseModel.data.total;
    tableData.value = list;
    selectedRowKeyList.value = [];
  } catch (e) {
    console.log(e);
  } finally {
    tableLoading.value = false;
  }
}

function changeDate(dates, dateStrings) {
  queryForm.startTime = dateStrings[0];
  queryForm.endTime = dateStrings[1];
}

// 获取业务类型的描述信息
function getShipperTypeDesc(shipperTypeList) {
  if (!shipperTypeList) {
    return '';
  }
  let descList = [];
  shipperTypeList.forEach((item) => {
    let desc = smartEnumPlugin.getDescByValue('SHIPPER_TYPE_ENUM', item);
    descList.push(desc);
  });
  return descList.join('，');
}

function getArea(area = []) {
  if (_.isEmpty(area)) {
    return '';
  }
  return area.map(e => e.valueName).join('，');
}

// 根据类型获取负责人的姓名
function getNameByType(list, type) {
  if (_.isEmpty(list)) {
    return '';
  }
  return list.filter(e => e.type == type).map((e) => e.employeeName).join('，');
}

// ----------- table 批量操作 start -----------
const selectedRowKeyList = ref([]);

function onSelectChange(selectedRowKeys) {
  selectedRowKeyList.value = selectedRowKeys;
}

function clearSelected() {
  selectedRowKeyList.value = [];
}

const updateManagerRef = ref();

// 显示修改货主业务负责人弹窗
function showManagerModal() {
  updateManagerRef.value.showModal();
}

// 显示修改货主客服负责人弹窗
const updatePrincipalRef = ref();

function showPrincipalModal(type) {
  updatePrincipalRef.value.showModal(type);
}

// ----------- table 数据操作 start -----------
function confirmDelete() {
  Modal.confirm({
    title: '提示',
    content: `确认要删除${selectedRowKeyList.value.length}个货主吗？`,
    okText: '删除',
    okType: 'danger',
    onOk: () => {
      deleteShipper();
    },
  });
}

async function deleteShipper() {
  try {
    useSpinStore().show();
    await shipperApi.deleteShipper(selectedRowKeyList.value);
    message.success('删除成功');
    ajaxQuery();
  } catch (e) {
    console.log(e);
  } finally {
    useSpinStore().hide();
  }
}

// ----------------- 审核 --------------------
let auditModal = ref();

function batchAudit() {
  for (const item of tableData.value) {
    if (_.includes(selectedRowKeyList.value, item.shipperId)) {
      if (item.auditStatus !== AUDIT_STATUS_ENUM.WAIT_AUDIT.value) {
        message.warning('请选择待审核的货主！请移除【' + item.consignor + '】');
        return;
      }
    }
  }

  let params = {
    shipperIdList: selectedRowKeyList.value,
  };
  auditModal.value.showModal(params, shipperApi.batchAudit);
}

function auditHandleFinish() {
  clearSelected();
  ajaxQuery();
}

// ----------- 添加跟进记录 -----------
const trackModalRef = ref();

function addTrack() {
  if (selectedRowKeyList.value.length > 1) {
    message.warning('每次只能跟进一个货主哦');
    return;
  }
  trackModalRef.value.showModal(selectedRowKeyList.value[0]);
}

// ----------- 更新 校验状态 -----------
function batchUpdateVerify() {
  for (const item of tableData.value) {
    if (_.includes(selectedRowKeyList.value, item.shipperId)) {
      if (item.verifyFlag) {
        message.warning('请选择待校验的货主！请移除【' + item.consignor + '】');
        return;
      }
    }
  }
  Modal.confirm({
    title: '提示',
    content: '货主信息是否确认无误？',
    okText: '确认',
    onOk: async () => {
      await shipperApi.batchUpdateVerifyFlag(selectedRowKeyList.value);
      message.info('确认成功')
      ajaxQuery();
    },
  });
}

// ------------ 导出 Excel -----------
function exportExcel() {
  SmartLoading.show();
  let params = _.cloneDeep(queryForm);
  params.gradeList = (params.gradeList || []).join(',');
  params.tagTypeList = (params.tagTypeList || []).join(',');
  params.shipperTypeList = (params.shipperTypeList || []).join(',');
  params.areaList = (params.areaList || []).join(',');
  shipperApi.downloadExcel(params);
  SmartLoading.hide();
}

// ----------- 页面跳转 start -----------
// 货主新建
function operateShipper(shipperId) {
  router.push({
    path: '/shipper/operate',
    query: {
      shipperId
    }
  });
}

function detailShipper(shipperId) {
  router.push({
    path: '/shipper/detail',
    query: {
      shipperId
    }
  });
}

onMounted(ajaxQuery);
</script>
