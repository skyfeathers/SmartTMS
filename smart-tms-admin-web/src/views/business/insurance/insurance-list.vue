<!--
 * @Description: 保险管理列表
 * @version:
 * @Author: zhuoda
 * @Date: 2022-07-07 15:41:39
 * @LastEditors: zhuoda
 * @LastEditTime: 2022-08-21
-->
<template>
  <a-form class="smart-query-form" v-privilege="'insurance:query'">
    <a-row class="smart-query-form-row">
      <a-form-item label="保单号" class="smart-query-form-item">
        <a-input style="width: 180px" v-model:value="queryForm.keyWord" placeholder="保单号" />
      </a-form-item>

      <a-form-item label="保险类型" class="smart-query-form-item">
        <SmartEnumSelect width="100px" v-model:value="queryForm.insuranceType" placeholder="保险类型" enum-name="INSURANCE_TYPE_ENUM" />
      </a-form-item>

      <a-form-item label="保险对象" class="smart-query-form-item" v-if="!props.moduleType">
        <SmartEnumSelect width="100px" v-model:value="queryForm.moduleType" placeholder="保险对象" enum-name="INSURANCE_MODULE_TYPE_ENUM" />
      </a-form-item>

      <a-form-item label="保险公司" class="smart-query-form-item">
        <smart-dict-select width="200px" keyCode="insuranceCompanyCode" v-model:value="queryForm.insuranceCompanyCode" />
      </a-form-item>

      <a-form-item label="到期时间" class="smart-query-form-item">
        <a-range-picker @change="changeExipre" v-model:value="expireRange" :presets="defaultTimeRanges" style="width: 220px" />
      </a-form-item>

      <a-form-item class="smart-query-form-item">
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

  <a-card size="small" :bordered="false" :hoverable="true">
    <a-row class="smart-table-btn-block">
      <div class="smart-table-operate-block">
        <a-button @click="addInsurance" type="primary" size="small" v-privilege="'insurance:add'">
          <template #icon>
            <PlusOutlined/>
          </template>
          新建保险
        </a-button>
        <a-button @click="confirmDelete" v-privilege="'insurance:delete'" type="primary" danger  size="small" :disabled="selectedRowKeyList.length == 0"> 删除 </a-button>
        <a-button @click="exportExcel" v-privilege="'insurance:export'" type="primary" size="small"> 导出 </a-button>
      </div>
      <div>
        <SmartTableOperator v-model="columns" :tableId="TABLE_ID_CONST.INSURANCE" :refresh="ajaxQuery" />
      </div>
    </a-row>

    <a-table
      :scroll="{ x: '100%' }"
      size="small"
      bordered
      :dataSource="tableData"
      :columns="columns"
      rowKey="insuranceId"
      :pagination="false"
      :loading="tableLoading"
      :row-selection="{ selectedRowKeys: selectedRowKeyList, onChange: onSelectChange }"
    >
      <template #bodyCell="{ text, record, index, column }">
         <template v-if="column.dataIndex === 'moduleName'">
          <a v-if="$privilege('vehicle:detail')" @click="handleDetail(record)">{{ record.moduleName }}</a>
          <span v-else>{{ record.moduleName }}</span>
        </template>
        <template v-if="column.dataIndex === 'insuranceType'">
          <span>{{ $smartEnumPlugin.getDescByValue('INSURANCE_TYPE_ENUM', text) }}</span>
        </template>
        <template v-if="column.dataIndex === 'moduleType'">
          <span>{{ $smartEnumPlugin.getDescByValue('INSURANCE_MODULE_TYPE_ENUM', text) }}</span>
        </template>
        <template v-if="column.dataIndex === 'expireTime'">
          <span>
            {{ text }}
            <span v-show="record.outDated" style="color: red">(已过期)</span>
          </span>
        </template>
        <template v-if="column.dataIndex === 'attachment'">
          <file-preview :fileList="record.attachment"/>
        </template>
        <template v-if="column.dataIndex === 'action'">
          <div class="smart-table-operate">
            <a-button @click="updateInsurance(record)" v-privilege="'insurance:edit'" type="link">编辑</a-button>
          </div>
        </template>
      </template>
    </a-table>

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
  </a-card>

  <InsuranceForm ref="formRef" @reloadList="ajaxQuery"/>
</template>
<script setup>
import SmartEnumSelect from '/@/components/smart-enum-select/index.vue';
import SmartDictSelect from '/@/components/smart-dict-select/index.vue';
import FilePreview from '/@/components/file-preview/index.vue'
import SmartTableOperator from '/@/components/smart-table-operator/index.vue';
import InsuranceForm from './components/insurance-form.vue';

import { defaultTimeRanges } from '/@/lib/default-time-ranges';
import {reactive, ref, onMounted, computed, inject} from 'vue';
import { message, Modal } from 'ant-design-vue';
import { PAGE_SIZE, PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
import { useSpinStore } from '/@/store/modules/system/spin';
import { insuranceApi } from '/@/api/business/insurance/insurance-api';
import dayjs from 'dayjs';
import { INSURANCE_MODULE_TYPE_ENUM } from '/@/constants/business/insurance-const';
import { useRouter } from 'vue-router';
import { TABLE_ID_CONST } from '/@/constants/table-id-const';

const props = defineProps({
  //保险对象类型
  moduleType: Number,
  //保险对象ID
  moduleId: Number,
});

const columns = ref([
  {
    title: '保单号',
    dataIndex: 'encryptPolicyNumber',
    width: 150
  },
  {
    title: '保险公司',
    dataIndex: 'insuranceCompanyName',
    width: 260,
    ellipsis: true,
  },
  {
    title: '保险类型',
    dataIndex: 'insuranceType',
    width: 120
  },
  {
    title: '保险对象类型',
    dataIndex: 'moduleType',
    width: 120
  },
  {
    title: '保险对象',
    dataIndex: 'moduleName',
    width: 120
  },
  {
    title: '保额(万)',
    dataIndex: 'insuranceAmount',
    width: 100
  },
  {
    title: '保险费用（元）',
    dataIndex: 'policyInsuranceAmount',
    width: 120
  },
  {
    title: '投保时间',
    dataIndex: 'effectDate',
    width: 120
  },
  {
    title: '到期时间',
    dataIndex: 'expireDate',
    width: 120
  },
  {
    title: '登记日期',
    dataIndex: 'enrollDate',
    width: 120
  },
  {
    title: '备注',
    dataIndex: 'remark',
    width: 100,
    ellipsis: true,
  },
  {
    title: '附件',
    dataIndex: 'attachment',
    width: 120,
  },
  {
    title: '创建人',
    dataIndex: 'createUserName',
    width: 100,
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
    width: 70,
  },
]);

const queryFormState = {
  pageNum: 1,
  pageSize: PAGE_SIZE,
  //保单号/保险对象名称
  keyWord: null,
  //保险类型
  insuranceType: null,
  //保险对象类型
  moduleType: props.moduleType,
  //保险对象id
  moduleId: props.moduleId,
  //保险公司
  insuranceCompanyCode: null,
  //有效期开始时间
  startDate: null,
  //有效期结束时间
  endDate: null,
};

// ----------- table 批量操作 start -----------
const selectedRowKeyList = ref([]);
const hasSelected = computed(() => selectedRowKeyList.value.length > 0);

function onSelectChange(keyArray) {
  selectedRowKeyList.value = keyArray;
}

const queryForm = reactive({ ...queryFormState });
const tableLoading = ref(false);
const tableData = ref([]);
const total = ref(0);

function resetQuery() {
  expireRange.value = [];
  Object.assign(queryForm, queryFormState);
  selectedRowKeyList.value = [];
  ajaxQuery();
}

function search () {
  queryForm.pageNum = 1;
  ajaxQuery();
}

onMounted(ajaxQuery);
async function ajaxQuery() {
  try {
    tableLoading.value = true;
    let responseModel = await insuranceApi.queryByPage(queryForm);
    const list = responseModel.data.list;
    for (const item of list) {
      item.outDated = dayjs(item.expireTime, 'YYYY-MM-DD') <= dayjs();
    }
    total.value = responseModel.data.total;
    tableData.value = list;
  } catch (e) {
    console.log(e);
  } finally {
    tableLoading.value = false;
  }
}

//过期时间
const expireRange = ref([]);
function changeExipre(dates, dateStrings) {
  queryForm.startDate = dateStrings[0];
  queryForm.endDate = dateStrings[1];
}

// ----------- 新建/更新/删除-----------
const formRef = ref();
function addInsurance() {
  let params = {};
  if (props.moduleType && props.moduleId) {
    params = {
      moduleType: props.moduleType,
      moduleId: props.moduleId,
      disabled: true
    };
  }
  formRef.value.showModal(params);
}

function updateInsurance (insurance) {
  if (props.moduleType && props.moduleId) {
    insurance.disabled = true;
  }
  formRef.value.showModal(insurance);
}

function confirmDelete() {
  if (!hasSelected.value) {
    message.warning('请先选择车辆');
    return;
  }
  Modal.confirm({
    title: '提示',
    content: '确认要删除吗？',
    okText: '删除',
    okType: 'danger',
    onOk: () => {
      deleteInsurance();
    },
  });
}

const deleteInsurance = async () => {
  try {
    useSpinStore().show();
    await insuranceApi.batchDelete(selectedRowKeyList.value);
    message.success('删除成功');
    ajaxQuery();
  } catch (e) {
    console.log(e);
  } finally {
    useSpinStore().hide();
  }
};

//保险对象详情
const router = useRouter();
const privilegePlugin = inject('privilegePlugin');
function moduleDetail(moduleType, moduleId) {
  if (moduleType == INSURANCE_MODULE_TYPE_ENUM.VEHICLE.value && privilegePlugin("vehicle:detail")) {
    router.push({
      path: '/vehicle/vehicle-detail',
      query: {
        vehicleId: moduleId,
      },
    });
    return;
  }
  if (moduleType == INSURANCE_MODULE_TYPE_ENUM.BRACKET.value && privilegePlugin("bracket:detail")) {
    router.push({
      path: '/bracket/detail',
      query: {
        bracketId: moduleId,
      },
    });
    return;
  }
}

function exportExcel(){
  insuranceApi.exportExcel('保险列表.xlsx', queryForm)
}

function handleDetail(record) {
  let path = '/vehicle/vehicle-detail';
  let query = {
    vehicleId: record.moduleId,
  }
  if(record.moduleType == INSURANCE_MODULE_TYPE_ENUM.BRACKET.value) {
    path = '/bracket/detail';
    query = {
      bracketId: record.moduleId,
    }
  }
  router.push({
    path,
    query,
  })
}
</script>
