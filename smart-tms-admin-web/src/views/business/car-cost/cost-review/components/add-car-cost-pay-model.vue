<!-- 录入etc支出的记录 -->
<template>
    <a-modal :title="form.moduleId ? '编辑费用' : '添加费用'" v-model:open="visible" cancel-text="取消" ok-text="确认" @cancel="onClose"
        @ok="onSubmit">
        <a-form ref="formRef" :label-col="{ span: 5 }" :model="form" :rules="rules" :wrapper-col="{ span: 12 }">
            <a-row>
                <a-col :span="24" v-if="showWaybillSelect">
                    <a-form-item class="smart-query-form-item" label="运单" name="waybillId" >
                        <a-tag v-for="item in waybillList" :key="item.waybillId" class="smart-margin-bottom5" closable
                            @close="removeWaybill(item.waybillId)">
                            {{ item.waybillNumber }}
                        </a-tag>
                        <a-button size="small" type="primary" @click="showWaybillModal">选择运单</a-button>
                    </a-form-item>
                </a-col>
                <a-col :span="24">
                    <a-form-item class="smart-query-form-item" label="费用类型" name="costType">
                        <SmartEnumSelect v-model:value="form.costType" enumName="CAR_COST_CATEGORY_TYPE_ENUM"
                            placeholder="费用类型" width="200px" />
                    </a-form-item>
                </a-col>
                <a-col :span="24">
                    <a-form-item label="费用项" name="categoryId">
                        <CarCostCategorySelect v-model:value="form.categoryId" @change="changeCategory"
                            :costType="form.costType" style="width: 200px" />
                    </a-form-item>
                </a-col>
                <a-col v-if="categoryPayType == CAR_COST_PAY_MODE_ENUM.OIL_CARD.value && form.waybillId" :span="24">
                    <a-form-item label="使用油卡" name="oilCardId">
                        <VehicleOilCardSelect v-model:value="form.oilCardId" :vehicleId="vehicleId" placeholder="请选择油卡" />
                    </a-form-item>
                </a-col>
                <a-col
                    v-if="form.costType == CAR_COST_CATEGORY_TYPE_ENUM.OIL_CARD.value && categoryPayType == CAR_COST_PAY_MODE_ENUM.CASH_COST.value"
                    :span="24">
                    <a-form-item label="加油类型" name="fuelType">
                        <a-select v-model:value="form.fuelType" placeholder="请选择加油类型" style="width:200px">
                            <a-select-option :label="OIL_CARD_FUEL_TYPE_ENUM.DIESEL_OIL_CARD.desc"
                                :value="OIL_CARD_FUEL_TYPE_ENUM.DIESEL_OIL_CARD.value">
                                柴油
                            </a-select-option>
                            <a-select-option :label="OIL_CARD_FUEL_TYPE_ENUM.GASOLINE_CARD.desc"
                                :value="OIL_CARD_FUEL_TYPE_ENUM.GASOLINE_CARD.value">
                                汽油
                            </a-select-option>
                        </a-select>
                    </a-form-item>
                </a-col>
                <template v-if="form.costType == CAR_COST_CATEGORY_TYPE_ENUM.OIL_CARD.value">
                    <a-col :span="24">
                        <a-form-item label="升数" name="oilConsumption">
                            <a-input-number v-model:value="form.oilConsumption" :min="0" :precision="2"
                                placeholder="请输入升数" style="width: 200px" />
                        </a-form-item>
                    </a-col>
                </template>
                <a-col :span="24">
                    <a-form-item label="费用金额" name="amount">
                        <a-input-number v-model:value="form.amount" :min="0" :precision="2" placeholder="请输入费用金额"
                            style="width: 200px" />
                    </a-form-item>
                </a-col>
                <a-col :span="24">
                    <a-form-item :rules="[{ required: requiredAttachmentFlag }]" label="附件" name="attachment">
                        <Upload :default-file-list="form.attachment" :folder="FILE_FOLDER_TYPE_ENUM.CAR_COST.value"
                            :maxUploadSize="10" :maxSize="100" accept=".jpg,.jpeg,.png,.gif,.mp4,.avi,.wmv"
                            buttonText="点击附件" @change="uploadAttachment" />
                    </a-form-item>
                </a-col>
                <a-col :span="24">
                    <a-form-item label="备注" name="remark">
                        <a-textarea v-model:value="form.remark" :min="0" :precision="2" placeholder="请输入备注"
                            style="width: 200px" />
                    </a-form-item>
                </a-col>
              <a-col :span="24">
                <a-form-item label="费用时间" name="createTime">
                  <a-date-picker v-model:value="form.createTime" :show-time="{ format: 'HH:mm' }"
                                 format="YYYY-MM-DD HH:mm:00"
                                 placeholder="请选择费用时间" valueFormat="YYYY-MM-DD HH:mm:00"/>
                </a-form-item>
              </a-col>

            </a-row>
        </a-form>
    </a-modal>
    <WaybillModalSelect ref="waybillModalRef" carCostFlag @changeWaybill="changeWaybill" />
</template>
<script setup>
import { ref, reactive, provide, nextTick } from 'vue';
import { message } from 'ant-design-vue';
import { useSpinStore } from '/@/store/modules/system/spin';
import _ from 'lodash';
import { useRoute } from 'vue-router';
import { CAR_COST_CATEGORY_TYPE_ENUM, CAR_COST_PAY_MODE_ENUM } from '/@/constants/business/car-cost-const';
import { FILE_FOLDER_TYPE_ENUM } from '/@/constants/business/file-const';
import SmartEnumSelect from '/@/components/smart-enum-select/index.vue';
import Upload from '/@/components/upload/index.vue';
import CarCostCategorySelect from '../car-cost-category-select.vue';
import { carCostPayApi } from '/@/api/business/waybill/car-cost-pay-api';
import VehicleOilCardSelect from '/@/components/vehicle-oil-card-select/index.vue';
import { OIL_CARD_FUEL_TYPE_ENUM } from '/@/constants/business/car-cost-const';
import WaybillModalSelect from '/@/components/waybill-modal-select/index.vue';
import { carCostTabulationApi } from '/@/api/business/car-cost/car-cost-tabulation-api';
import dayjs from "dayjs";
let route = useRoute();
// ----------------------- 以下是字段定义 emits props ------------------------
// emit
const emit = defineEmits(['reloadList']);
//  组件
const formRef = ref();

const formDefault = {
    waybillId: null,
    categoryId: null,
    amount: null,
    attachment: null,
    remark: null,
    costType: null,
    etcPayId: null,
    oilCardId: undefined,
    fuelType: undefined,
    oilConsumption: undefined,
    createTime: dayjs().format('YYYY-MM-DD HH:mm:ss'),
};
let form = reactive({ ...formDefault });
const rules = {
    waybillId: [{ required: true, message: '请选择运单' }],
    costType: [{ required: true, message: '请选择费用类型' }],
    categoryId: [{ required: true, message: '请选择费用项' }],
    amount: [{ required: true, message: '请输入费用金额' }],
    oilCardId: [{ required: true, message: '请选择油卡' }],
    oilConsumption: [{ required: true, message: '请选择加油升数' }],
    fuelType: [{ required: true, message: '请选择加油类型' }],
    createTime: [{ required: true, message: '请选择费用时间' }],
};
// 是否展示运单选择
const showWaybillSelect = ref(true);

const vehicleId = ref();
provide('vehicleId', vehicleId.value)
const waybillList = ref([])
function changeWaybill(list) {
    waybillList.value = list;
    form.waybillId = list[0].waybillId
    vehicleId.value = list[0].vehicleId
    if(formRef.value) {
        formRef.value.validate('waybillId')
    }
}

function removeWaybill(waybillId) {
    let findIndex = waybillList.value.findIndex(e => e.waybillId == waybillId);
    if (findIndex == -1) {
        return;
    }
    waybillList.value.splice(findIndex, 1);
}

// ----------------------- 抽屉的展示隐藏 start ------------------------
// 是否展示抽屉
const visible = ref(false);
const moduleId = ref(null)
function showModal(rowData, waybillSelect = true) {
    showWaybillSelect.value = waybillSelect;
    waybillList.value = [];
    vehicleId.value = undefined;
    Object.assign(form, formDefault);
    if (rowData && rowData.moduleId) {
        moduleId.value = rowData.moduleId
        if(rowData.waybillId){
            waybillList.value = [{
                waybillId: rowData.waybillId,
                waybillNumber: rowData.waybillNumber
            }]
        }
        form.costType = rowData.costType;
        vehicleId.value = rowData.vehicleId
        nextTick(() => {
            getDetail(rowData);
        })
    }

    if(!waybillSelect){
       changeWaybill([
        {
            waybillId: rowData.waybillId,
            vehicleId: rowData.vehicleId,
        }
       ])
    }
    
    visible.value = true;
    
}

async function getDetail(rowData) {
    const res = await carCostTabulationApi.detail(rowData.moduleId, rowData.moduleType);
    let data = res.data;
    Object.assign(form, data);
    form.oilCardId = data.oilCardId ? Number(data.oilCardId) : undefined
    form.categoryId = data.categoryId ? Number(data.categoryId) : undefined
    form.moduleId = moduleId.value
}

function onClose() {
    Object.assign(form, formDefault);
    categoryPayType.value = null;
    moduleId.value = null;
    form.moduleId = undefined;
    requiredAttachmentFlag.value = false;
    formRef.value.resetFields();
    visible.value = false;
}

function onSubmit() {
    formRef.value
        .validate()
        .then(async () => {
            useSpinStore().show();
            try {
                await add();
                emit('reloadList');
                onClose();
            } catch (error) {
                console.log(error);
            } finally {
                useSpinStore().hide();
            }
        })
        .catch((error) => {
            console.log('error', error);
            message.error('参数验证错误，请仔细填写表单数据!');
        });
}

async function add() {
    switch (form.costType) {
        case CAR_COST_CATEGORY_TYPE_ENUM.CASH_COST.value:
            if (form.moduleId) {
                form.cashPayId = form.moduleId
                await carCostPayApi.updateCashPay(form);
                message.success('编辑成功');
            } else {
                await carCostPayApi.addCashPay(form);
                message.success('添加成功');
            }
            break;
        case CAR_COST_CATEGORY_TYPE_ENUM.OIL_CARD.value:
            if (form.moduleId) {
                form.oilPayId = form.moduleId;
                await carCostPayApi.updateOilPay(form);
                message.success('编辑成功');
            }else{
                await carCostPayApi.addOilPay(form);
                message.success('添加成功');
            }

            break;
        case CAR_COST_CATEGORY_TYPE_ENUM.ETC_COST.value:
            if (form.moduleId) {
                form.etcPayId = form.moduleId;
                await carCostPayApi.updateEtcPay(form);
                message.success('编辑成功');
            }else{
                await carCostPayApi.addEtcPay(form);
                message.success('添加成功');
            }
            break;
        case CAR_COST_CATEGORY_TYPE_ENUM.UREA_COST.value:
            if (form.moduleId) {
                form.ureaPayId = form.moduleId;
                await carCostPayApi.updateUreaPay(form);
                message.success('编辑成功');
            }else{
                await carCostPayApi.addUreaPay(form);
                message.success('添加成功');
            }
            break;
        default:
            break;
    }
}

// 上传附件
function uploadAttachment(fileList) {
    form.attachment = fileList;
    formRef.value.validateFields('attachment');
}

const waybillModalRef = ref();
function showWaybillModal() {
    waybillModalRef.value.showModal({}, (waybillList.value || []).map(e => e.waybillId), waybillList.value);
}

let requiredAttachmentFlag = ref(true);

let categoryPayType = ref(null);

function changeCategory(categoryId, category) {
    categoryPayType.value = category.payMode;
    requiredAttachmentFlag.value = category.payMode !== CAR_COST_PAY_MODE_ENUM.ETC_CARD.value;
}
// ----------------------- 以下是暴露的方法内容 ------------------------
defineExpose({
    showModal,
});
</script>
