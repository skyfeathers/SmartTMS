<template>
    <a-form class="smart-query-form" v-privilege="'car-cost:cost-review:query'">
        <a-row class="smart-query-form-row">
            <a-form-item class="smart-query-form-item" label="运单号">
                <a-input v-model:value="queryForm.waybillNumber" style="width: 150px;" placeholder="运单号"></a-input>
            </a-form-item>
            <a-form-item class="smart-query-form-item" label="箱号">
                <a-input v-model:value="queryForm.containerNumber" style="width: 150px;" placeholder="箱号"></a-input>
            </a-form-item>
            <a-form-item class="smart-query-form-item" label="审核状态">
                <SmartEnumSelect v-model:value="queryForm.auditStatus" enumName="AUDIT_STATUS_ENUM" placeholder="审核状态"
                    width="200px" />
            </a-form-item>
            <a-form-item class="smart-query-form-item" label="司机">
                <DriverSelect v-model:value="queryForm.driverId" width="150px" />
            </a-form-item>
            <a-form-item class="smart-query-form-item" label="车辆">
                <VehicleSelect v-model:value="queryForm.vehicleId" width="150px" />
            </a-form-item>
            <a-form-item class="smart-query-form-item" label="费用类型">
                <SmartEnumSelect v-model:value="queryForm.costType" enumName="CAR_COST_CATEGORY_TYPE_ENUM"
                    placeholder="费用类型" width="200px" />
            </a-form-item>
            <a-form-item class="smart-query-form-item" label="费用项">
                <CarCostCategorySelect v-model:value="queryForm.categoryId" :costType="queryForm.costType"
                    style="width: 200px" />
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
                <a-button @click="addCarCostPay()" type="primary" size="small" v-privilege="'car-cost:cost-review:add'">
                    <template #icon>
                        <PlusOutlined />
                    </template>
                    新增费用
                </a-button>
                <a-button @click="addCarCostReceive()" type="primary" size="small" v-privilege="'car-cost:cost-review:apply'">
                    <template #icon>
                        <PlusOutlined />
                    </template>
                    申请出车款
                </a-button>
                <a-button @click="addCarCostOilReceive()" type="primary" size="small" v-privilege="'car-cost:cost-review:oil-recharge'">
                    <template #icon>
                        <PlusOutlined />
                    </template>
                    油卡充值
                </a-button>
                <a-button @click="showBatchAudit()" type="default" size="small" v-privilege="'car-cost:cost-review:batch-audit'">
                    批量审核
                </a-button>
                <a-button @click="associationWaybill()" type="default" size="small" v-privilege="'car-cost:cost-review:association'">
                    关联运单
                </a-button>
                <a-button @click="unAssociation()" type="default" size="small" v-privilege="'car-cost:cost-review:un-association'">
                    取消关联
                </a-button>
            </div>
            <div class="smart-table-setting-block"></div>
        </a-row>

        <a-table :row-selection="{ selectedRowKeys: selectedRowKeyList, onChange: onSelectChange }"
            :scroll="{ x: '100%' }" size="small" :dataSource="tableData" :columns="columns" rowKey="tabulationId"
            :pagination="false" bordered>
            <template #bodyCell="{ text, record, index, column }">
                <template v-if="column.dataIndex === 'waybillNumber'">
                    <a @click="waybillDetail(record.waybillId)">{{ text }}</a>
                  <SmartCopyIcon v-if="text" :value="text" />
                </template>
                <template v-if="column.dataIndex === 'attachment'">
                    <file-preview :fileList="text" />
                </template>
                <template v-if="column.dataIndex === 'auditStatus'">
                    <a-tag size="small" :color="$smartEnumPlugin.getFieldByValue('AUDIT_STATUS_ENUM', text, 'color')">
                        {{ $smartEnumPlugin.getDescByValue('AUDIT_STATUS_ENUM', text) }}
                    </a-tag>
                </template>
                <template v-if="column.dataIndex === 'moduleType'">
                    {{ $smartEnumPlugin.getDescByValue('CAR_COST_MODULE_TYPE_ENUM', text) }}
                </template>
                <template v-else-if="column.dataIndex === 'action'">
                    <div class="smart-table-operate">
                        <a-button @click="showAudit(record)" type="link" v-privilege="'car-cost:cost-review:audit'">审核</a-button>
                        <a-button @click="edit(record)" type="link" v-privilege="'car-cost:cost-review:edit'">修改</a-button>
                        <a-button @click="confirmDelete(record.tabulationId)" type="link" v-privilege="'car-cost:cost-review:delete'">删除
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
    <AddCarCostPayModel ref="addCarCostPayModelRef" @reloadList="ajaxQuery" />
    <CarCostCashReceiveModal ref="carCostCashReceiveModalRef" @reloadList="ajaxQuery" />
    <CarCostOilReceiveModal ref="carCostOilReceiveModalRef" @reloadList="ajaxQuery" />
    <AuditModal ref="auditModalRef" @refresh="ajaxQuery" />
    <WaybillModalSelect ref="waybillModalRef" carCostFlag @changeWaybill="changeWaybill" />
</template>
<script setup>
import SmartEnumSelect from '/@/components/smart-enum-select/index.vue';
import { SmartLoading } from '/@/components/smart-loading';
import DriverSelect from '/@/components/driver-select/index.vue';
import VehicleSelect from '/@/components/vehicle-select/index.vue';
import SmartCopyIcon from '/@/components/smart-copy-icon/index.vue'
import { reactive, ref, onMounted } from 'vue';
import { message, Modal } from 'ant-design-vue';
import { useSpinStore } from '/@/store/modules/system/spin';
import { carCostTabulationApi } from '/@/api/business/car-cost/car-cost-tabulation-api';
import { PAGE_SIZE, PAGE_SIZE_OPTIONS, AUDIT_STATUS_ENUM } from '/@/constants/common-const';
import CarCostCategorySelect from './car-cost-category-select.vue';
import AddCarCostPayModel from './components/add-car-cost-pay-model.vue'
import CarCostCashReceiveModal from './components/car-cost-cash-receive-modal.vue'
import CarCostOilReceiveModal from './components/car-cost-oil-receive-modal.vue'
import AuditModal from './components/audit-modal.vue'
import WaybillModalSelect from '/@/components/waybill-modal-select/index.vue';
import FilePreview from '/@/components/file-preview/index.vue';
import { CAR_COST_MODULE_TYPE_ENUM } from '/@/constants/business/car-cost-const';
import { useRouter } from 'vue-router'
import useDragTable from '/@/hook/use-drag-table/index'
const router = useRouter();
function waybillDetail(waybillId) {
  router.push({ path: '/waybill/waybill-detail', query: { waybillId } });
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
        width: 100,
    },
    {
        title: '运单',
        dataIndex: 'waybillNumber',
        width: 180,
    },
    {
        title: '状态',
        width: 100,
        dataIndex: 'auditStatus',
    },
    {
        title: '模块类型',
        width: 100,
        dataIndex: 'moduleType',
    },
    {
        title: '费用项',
        width: 100,
        dataIndex: 'categoryName',
    },
    {
        title: '油卡',
        dataIndex: 'oilCardNo',
        width: 100,
    },
    {
        title: '金额',
        dataIndex: 'amount',
        width: 100,
    },
    {
        title: '加油升数',
        dataIndex: 'oilConsumption',
        width: 100,
    },
    {
        title: '凭证',
        dataIndex: 'attachment',
        width: 180,
    },
    {
        title: '备注',
        dataIndex: 'remark',
        width: 100,
    },
    {
        title: '上报时间',
        dataIndex: 'createTime',
        width: 180,
    },
    {
        title: '操作',
        dataIndex: 'action',
        fixed: 'right',
        width: 140
    },
]);

const queryFormState = {
    waybillNumber: undefined,
    containerNumber: undefined,
    auditStatus: null,
    pageNum: 1,
    pageSize: PAGE_SIZE,
    categoryId: null,
    driverId: null,
    vehicleId: null,
    costType: null
};
const queryForm = reactive({ ...queryFormState });
const tableData = ref([]);
const total = ref(0);
const operateModal = ref();

function resetQuery() {
    Object.assign(queryForm, queryFormState);
    ajaxQuery();
}

function quickQuery() {
    ajaxQuery();
}

const addCarCostPayModelRef = ref()
function addCarCostPay() {
    addCarCostPayModelRef.value.showModal()
}

const carCostCashReceiveModalRef = ref();

function addCarCostReceive() {
    carCostCashReceiveModalRef.value.showModal();
}

const carCostOilReceiveModalRef = ref()
function addCarCostOilReceive() {
    carCostOilReceiveModalRef.value.showModal();
}

const auditModalRef = ref();
function showAudit(item) {
    auditModalRef.value.showModal(item.tabulationId);
}

function showBatchAudit() {
    if (selectedRowKeyList.value.length > 0) {
        auditModalRef.value.showBatchModal(selectedRowKeyList.value);
    } else {
        message.error('请选择费用数据')
    }
}

const waybillModalRef = ref();
function associationWaybill() {
    if (selectedRowKeyList.value.length !== 0) {
        if (!selectedRowList.value.some(e => e.waybillNumber)) {
            waybillModalRef.value.showModal({}, [], []);
        } else {
            message.error('请选择未关联运单的费用数据')
        }
    } else {
        message.error('请选择费用数据')
    }
}

async function changeWaybill(list) {
    useSpinStore().show();
    try {
        await carCostTabulationApi.relate({
            tabulationIdList:selectedRowKeyList.value,
            waybillId: list[0].waybillId
        });
        message.success('添加成功');
        ajaxQuery();
    } catch (error) {
        console.log(error);
    } finally {
        useSpinStore().hide();
    }
}

function unAssociation() {
    if (selectedRowKeyList.value.length !== 0) {
        if (selectedRowList.value.some(e => e.waybillNumber)) {
            Modal.confirm({
                bodyStyle:{
                    padding:'20px !important'
                },
                title: '取消关联',
                content: '确定要取消关联吗',
                okText: '确认',
                async onOk() {
                    try {
                        await carCostTabulationApi.cancelRelate({tabulationIdList: selectedRowKeyList.value})
                        message.success('取消成功')
                        ajaxQuery();
                    } catch (error) {

                    }
                },
                cancelText: '取消',
                onCancel() { },
            });
        } else {
            message.error('请选择关联运单的费用数据')
        }
    } else {
        message.error('请选择一条费用数据')
    }

}

async function ajaxQuery() {
    try {
        SmartLoading.show();
        let responseModel = await carCostTabulationApi.page(queryForm);
        const list = responseModel.data.list;
        total.value = responseModel.data.total;
        tableData.value = list;
        selectedRowKeyList.value = [];
        selectedRowList.value = [];
    } catch (e) {
        console.log(e);
    } finally {
        SmartLoading.hide();
    }
}


function confirmDelete(tabulationId) {
    Modal.confirm({
        title: "确定要删除吗？",
        content: "删除后，该信息将不可恢复",
        okText: "删除",
        okType: "danger",
        onOk() {
            del(tabulationId);
        },
        cancelText: "取消",
        onCancel() {
        },
    });
}
async function del(tabulationId) {
    try {
        useSpinStore().show();
        await carCostTabulationApi.delete(tabulationId);
        message.success('删除成功');
        ajaxQuery();
    } catch (e) {
        console.log(e);
    } finally {
        useSpinStore().hide();
    }
}

function edit(rowData) {
    switch(rowData.moduleType){
        case CAR_COST_MODULE_TYPE_ENUM.CASH_RECEIVE.value:
            carCostCashReceiveModalRef.value.showModal(rowData);
        break;
        case CAR_COST_MODULE_TYPE_ENUM.OIL_CARD_RECEIVE.value:
            carCostOilReceiveModalRef.value.showModal(rowData);
        break;
        case CAR_COST_MODULE_TYPE_ENUM.CASH_PAY.value:
            addCarCostPayModelRef.value.showModal(rowData)
        break;
        case CAR_COST_MODULE_TYPE_ENUM.OIL_PAY.value:
        addCarCostPayModelRef.value.showModal(rowData)
        break;
        case CAR_COST_MODULE_TYPE_ENUM.ETC_PAY.value:
        addCarCostPayModelRef.value.showModal(rowData)
        break;
        case CAR_COST_MODULE_TYPE_ENUM.UREA_PAY.value:
        addCarCostPayModelRef.value.showModal(rowData)
        break;
    }
}

onMounted(ajaxQuery);

const selectedRowKeyList = ref([]);
const selectedRowList = ref([]);

function onSelectChange(selectedRowKeys, selectedRows) {
    selectedRowKeyList.value = selectedRowKeys;
    selectedRowList.value = selectedRows;
}
</script>
