<template>
    <a-form class="smart-query-form" v-privilege="'businessType:query'">
        <a-row class="smart-query-form-row">
            <a-form-item class="smart-query-form-item" label="运单号">
                <a-input v-model:value="queryForm.waybillNumber" style="width: 200px;" placeholder="运单号"></a-input>
            </a-form-item>
            <a-form-item class="smart-query-form-item" label="司机">
                <DriverSelect v-model:value="queryForm.driverId" width="200px" />
            </a-form-item>
            <a-form-item class="smart-query-form-item" label="车辆">
                <VehicleSelect v-model:value="queryForm.vehicleId" width="200px" />
            </a-form-item>
            <a-form-item class="smart-query-form-item" label="箱号">
                <a-input v-model:value="queryForm.containerNumber" style="width: 200px;" placeholder="请输入"></a-input>
            </a-form-item>
            <a-form-item class="smart-query-form-item smart-margin-left10">
                <a-button type="primary" @click="quickQuery">
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
                <a-button @click="addCarCostBasic()" type="primary" size="small">
                    <template #icon>
                        <PlusOutlined />
                    </template>
                    新增工资
                </a-button>
            </div>
            <div class="smart-table-setting-block"></div>
        </a-row>

        <a-table :scroll="{ x: '100%' }" size="small" :dataSource="tableData" :columns="columns" :pagination="false" bordered>
            <template #bodyCell="{ text, record, index, column }">
                <template v-if="column.dataIndex === 'waybillNumber'">
                    <a @click="waybillDetail(record.waybillId)">{{ text }}</a>
                </template>
                <template v-else-if="column.dataIndex === 'confirmFlag'">
                    <a-tag v-show="record.confirmFlag" color="success">已确认</a-tag>
                    <a-tag v-show="!record.confirmFlag" color="error">未确认</a-tag>
                </template>
                <template v-else-if="column.dataIndex === 'action'">
                    <div class="smart-table-operate">
                        <a-button @click="confirm(record)"
                            type="link">{{ record.confirmFlag ? '反确认' : '确认'}}</a-button>
                        <a-button @click="confirmDelete(record.basicInfoId)"
                            type="link">删除
                        </a-button>
                        <a-button @click="edit(record)"
                            type="link">修改
                        </a-button>
                    </div>
                </template>
            </template>
        </a-table>

        <div class="smart-query-table-page">
            <a-pagination showSizeChanger showQuickJumper show-less-items :pageSizeOptions="PAGE_SIZE_OPTIONS"
                :defaultPageSize="queryForm.pageSize" v-model:current="queryForm.pageNum"
                v-model:pageSize="queryForm.pageSize" :total="total" @change="ajaxQuery" @showSizeChange="ajaxQuery"
                :show-total="(total) => `共${total}条`" />
        </div>
    </a-card>
    <SalaryModal ref="salaryModalRef" @reloadList="ajaxQuery"/>
</template>
<script setup>
import DriverSelect from '/@/components/driver-select/index.vue';
import VehicleSelect from '/@/components/vehicle-select/index.vue';
import { reactive, ref, onMounted } from 'vue';
import { message, Modal } from 'ant-design-vue';
import { useSpinStore } from '/@/store/modules/system/spin';
import { basicInfoApi } from '/@/api/business/basicInfo/basicInfo-api'
import { PAGE_SIZE, PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
import SalaryModal from './components/salary-modal.vue';
import { SmartLoading } from '/@/components/smart-loading';
import { useRouter } from 'vue-router'
import useDragTable from '/@/hook/use-drag-table/index'
const router = useRouter();
function waybillDetail(waybillId) {
  router.push({ path: '/waybill/waybill-detail', query: { waybillId } });
}

const salaryModalRef = ref()
function addCarCostBasic(){
  salaryModalRef.value.showModal()
}
const { columnsData:columns, tableWidth } = useDragTable([
    {
        title: '司机',
        dataIndex: 'driverName',
        width: 100,
        ellipsis: true,
    },
    {
        title: '车辆',
        dataIndex: 'vehicleNumber',
        width: 120,
    },
    {
        title: '运单',
        dataIndex: 'waybillNumber',
        width: 150
    },
    {
        title: '箱号',
        dataIndex: 'containerNumber',
        width: 120,
    },
    {
        title: '高速里程',
        dataIndex: 'highSpeedMileage',
        width: 100,
    },
    {
        title: '低速里程',
        dataIndex: 'lowSpeedMileage',
        width: 100,
    },
    {
        title: 'GPS公里数',
        dataIndex: 'gpsMileage',
        width: 100,
    },
    {
        title: '用油量',
        dataIndex: 'oilConsumption',
        width: 100
    },
    {
        title: '出勤天数',
        dataIndex: 'attendanceDays',
        width: 100
    },
    {
        title: '提成工资',
        dataIndex: 'commissionSalary',
        width: 100
    },
    {
        title: '基本工资',
        dataIndex: 'basicSalary',
        width: 100
    },
    {
        title: '确认状态',
        dataIndex: 'confirmFlag',
        width: 100
    },
    {
        title: '操作',
        dataIndex: 'action',
        fixed: 'right',
        width: 140
    },
]);

const queryFormState = {
    keywords: '',
    disabledFlag: null,
    endTime: null,
    startTime: null,
    pageNum: 1,
    pageSize: PAGE_SIZE,
    searchCount: true,
    tripType: null
};
const queryForm = reactive({ ...queryFormState });
const tableData = ref([]);
const total = ref(0);
const operateModal = ref();

// 日期选择
let searchDate = ref();

function dateChange(dates, dateStrings) {
    queryForm.startTime = dateStrings[0];
    queryForm.endTime = dateStrings[1];
}

function resetQuery() {
    searchDate.value = [];
    Object.assign(queryForm, queryFormState);
    ajaxQuery();
}

function quickQuery() {
    queryForm.pageNum = 1;
    ajaxQuery();
}

function edit(record){
  salaryModalRef.value.showModal(record)
}

const carCostCashReceiveModalRef = ref();

function addCarCostReceive(){
    carCostCashReceiveModalRef.value.showModal();
}

const carCostOilReceiveModalRef = ref()
function addCarCostOilReceive(){
    carCostOilReceiveModalRef.value.showModal();
}

const auditModalRef = ref();
function showAudit(){
    auditModalRef.value.showModal();
}

const associationWaybillRef = ref()

function associationWaybill(){
    associationWaybillRef.value.showModal();
}

function unAssociation(){
    Modal.confirm({
    title: '取消关联',
    content: '确定要取消关联吗',
    okText: '确认',
    onOk() {
    },
    cancelText: '取消',
    onCancel() {},
  });
}

async function ajaxQuery() {
    try {
        SmartLoading.show();
        let responseModel = await basicInfoApi.page(queryForm);
        const list = responseModel.data.list;
        total.value = responseModel.data.total;
        tableData.value = list;
    } catch (e) {
        console.log(e);
    } finally {
        SmartLoading.hide();
    }
}


function confirmDelete(basicInfoId) {
    Modal.confirm({
        title: "确定要删除吗？",
        content: "删除后，该信息将不可恢复",
        okText: "删除",
        okType: "danger",
        onOk() {
            del(basicInfoId);
        },
        cancelText: "取消",
        onCancel() {
        },
    });
}
async function del(basicInfoId) {
    try {
        useSpinStore().show();
        await basicInfoApi.del(basicInfoId);
        message.success('删除成功');
        ajaxQuery();
    } catch (e) {
        console.log(e);
    } finally {
        useSpinStore().hide();
    }
};

async function confirm(rowData) {
    try {
        await basicInfoApi.confirm(rowData.basicInfoId,!rowData.confirmFlag);
        ajaxQuery();
    } catch (error) {

    }
}

onMounted(ajaxQuery);

</script>
