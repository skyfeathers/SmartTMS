<!-- 录入etc支出的记录 -->
<template>
    <a-modal title="关联运单" :visible="visible" cancel-text="取消" ok-text="确认" @cancel="onClose" @ok="onSubmit">
        <a-form ref="formRef" :label-col="{ span: 5 }" :model="form" :rules="rules" :wrapper-col="{ span: 12 }">
            <a-row>
                <a-col :span="24">
                    <a-form-item class="smart-query-form-item" label="运单" name="waybillId">
                        <a-tag v-for="item in waybillList" :key="item.waybillId" class="smart-margin-bottom5" closable
                            @close="removeWaybill(item.waybillId)">
                            {{ item.waybillNumber }}
                        </a-tag>
                        <a-button size="small" type="primary" @click="showWaybillModal">选择运单</a-button>
                    </a-form-item>
                </a-col>
            </a-row>
        </a-form>
    </a-modal>
    <WaybillModalSelect ref="waybillModalRef" carCostFlag @changeWaybill="changeWaybill" />
</template>
<script setup>
import { ref, reactive } from 'vue';
import { message } from 'ant-design-vue';
import { useSpinStore } from '/@/store/modules/system/spin';
import _ from 'lodash';
import { useRoute } from 'vue-router';
import { carCostTabulationApi } from '/@/api/business/car-cost/car-cost-tabulation-api';
import WaybillModalSelect from '/@/components/waybill-modal-select/index.vue';
let route = useRoute();
// ----------------------- 以下是字段定义 emits props ------------------------
// emit
const emit = defineEmits('reloadList');

//  组件
const formRef = ref();

const formDefault = {
    waybillId: null,
    tabulationId: null
};
let form = reactive({ ...formDefault });
const rules = {
    waybillId: [{ required: true, message: '请选择运单' }],
};

// ----------------------- 抽屉的展示隐藏 start ------------------------
// 是否展示抽屉
const visible = ref(false);

function showModal(tabulationId) {
    Object.assign(form, formDefault);
    form.tabulationId = tabulationId
    visible.value = true;
}

function onClose() {
    Object.assign(form, formDefault);
    formRef.value.resetFields();
    visible.value = false;
}

// ----------------------- 抽屉的展示隐藏 end ------------------------

let waybillId = Number(route.query.waybillId);

function onSubmit() {
    formRef.value
        .validate()
        .then(async () => {
            useSpinStore().show();
            try {
                await carCostTabulationApi.relate(form.tabulationId,form.waybillId);
                message.success('添加成功');
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


const waybillModalRef = ref();
function showWaybillModal() {
    waybillModalRef.value.showModal({}, (waybillList.value || []).map(e => e.waybillId), waybillList.value);
}


const waybillList = ref([])
function changeWaybill(list) {
    waybillList.value = list;
    form.waybillId = list[0].waybillId
    formRef.value.validate('waybillId')
}

function removeWaybill(waybillId) {
    let findIndex = waybillList.value.findIndex(e => e.waybillId == waybillId);
    if (findIndex == -1) {
        return;
    }
    waybillList.value.splice(findIndex, 1);
}

// ----------------------- 以下是暴露的方法内容 ------------------------
defineExpose({
    showModal,
});
</script>