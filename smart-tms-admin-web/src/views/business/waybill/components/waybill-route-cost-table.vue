<template>
    <a-card size="small" :bordered="false" :hoverable="true">
        <a-row class="smart-table-btn-block smart-margin-top10">
            <div class="smart-table-operate-block">
                <a-button @click="addCarCostPay()" type="primary" size="small">
                    <template #icon>
                        <PlusOutlined />
                    </template>
                    新增费用
                </a-button>
                <a-button @click="addCarCostReceive()" type="primary" size="small">
                    <template #icon>
                        <PlusOutlined />
                    </template>
                    申请出车款
                </a-button>
                <a-button @click="addCarCostOilReceive()" type="primary" size="small">
                    <template #icon>
                        <PlusOutlined />
                    </template>
                    油卡充值
                </a-button>
            </div>
            <div class="smart-table-setting-block"></div>
        </a-row>

        <a-table
            :scroll="{ x: tableWidth, y: 500 }" size="small" :dataSource="tableData" :columns="columns" rowKey="tabulationId"
            :pagination="false" bordered>
            <template #bodyCell="{ text, record, index, column }">
                <template v-if="column.dataIndex === 'attachment'">
                    <file-preview :fileList="text" />
                </template>
                <template v-if="column.dataIndex === 'auditStatus'">
                    {{ $smartEnumPlugin.getDescByValue('AUDIT_STATUS_ENUM', text) }}
                </template>
                <template v-if="column.dataIndex === 'moduleType'">
                    {{ $smartEnumPlugin.getDescByValue('CAR_COST_MODULE_TYPE_ENUM', text) }}
                </template>
                <template v-else-if="column.dataIndex === 'action'">
                    <div class="smart-table-operate">
                        <a-button @click="edit(record)" type="link">修改</a-button>
                        <a-button @click="confirmDelete(record.tabulationId)" type="link">删除
                        </a-button>
                    </div>
                </template>
            </template>
        </a-table>

    </a-card>
    <AddCarCostPayModel ref="addCarCostPayModelRef" @reloadList="ajaxQuery" />
    <CarCostCashReceiveModal ref="carCostCashReceiveModalRef" @reloadList="ajaxQuery" />
    <CarCostOilReceiveModal ref="carCostOilReceiveModalRef" @reloadList="ajaxQuery" />
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
import CarCostCategorySelect from '/@/views/business/car-cost/cost-review/car-cost-category-select.vue';
import AddCarCostPayModel from '/@/views/business/car-cost/cost-review/components/add-car-cost-pay-model.vue'
import CarCostCashReceiveModal from '/@/views/business/car-cost/cost-review/components/car-cost-cash-receive-modal.vue'
import CarCostOilReceiveModal from '/@/views/business/car-cost/cost-review/components/car-cost-oil-receive-modal.vue'
import FilePreview from '/@/components/file-preview/index.vue';
import { CAR_COST_MODULE_TYPE_ENUM } from '/@/constants/business/car-cost-const';
import { useRouter } from 'vue-router'
import useDragTable from '/@/hook/use-drag-table/index'
const router = useRouter();

const props = defineProps({
    waybillId: {
        type: [Number, null],
        default: null
    },
    vehicleId: {
        type: [Number, null],
        default: null
    }
})

const { columnsData:columns, tableWidth } = useDragTable([
    
    
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
        title: '状态',
        width: 100,
        dataIndex: 'auditStatus',
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
        width: 100
    },
]);

const tableData = ref([]);

const addCarCostPayModelRef = ref()
function addCarCostPay() {
    addCarCostPayModelRef.value.showModal({
        waybillId: props.waybillId,
        vehicleId: props.vehicleId,
    }, false)
}

const carCostCashReceiveModalRef = ref();

function addCarCostReceive() {
    carCostCashReceiveModalRef.value.showModal({
        waybillId: props.waybillId,
    }, false)
}

const carCostOilReceiveModalRef = ref()
function addCarCostOilReceive() {
    carCostOilReceiveModalRef.value.showModal({
        waybillId: props.waybillId,
    }, false)
}

async function ajaxQuery() {
    try {
        SmartLoading.show();
        let responseModel = await carCostTabulationApi.listByWaybillId(props.waybillId);
        const list = responseModel.data;
        tableData.value = list;
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
</script>
