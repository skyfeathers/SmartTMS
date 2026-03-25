<!--
 * @Description: file content
 * @Author: yandy
 * @Date: 2022-07-13 15:13:06
 * @LastEditors:
 * @LastEditTime: 2022-07-13 15:13:06
-->
<template>
    <a-modal title="提交" :width="720" v-model:open="modalVisible" @cancel="onClose" :getContainer="getContainer">
      <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 6 }" :wrapper-col="{ span: 12 }">
        <a-form-item label="审核结果" name="auditStatus">
          <a-radio-group v-model:value="form.auditStatus">
            <a-radio :value="AUDIT_STATUS_ENUM.AUDIT_PASS.value">审核通过</a-radio>
            <a-radio :value="AUDIT_STATUS_ENUM.REJECT.value">审核驳回</a-radio>
          </a-radio-group>
        </a-form-item>
        <a-form-item label="审核备注" name="auditRemark">
          <a-input style="width: 100%" placeholder="请输入备注" v-model:value="form.auditRemark"/>
          <span style="color: red">审核驳回时必须填写备注信息</span>
        </a-form-item>
      </a-form>

      <div
          :style="{
          position: 'absolute',
          right: 0,
          bottom: 0,
          width: '100%',
          borderTop: '1px solid #e9e9e9',
          padding: '10px 16px',
          background: '#fff',
          textAlign: 'right',
          zIndex: 1,
        }"
      >
        <a-button style="margin-right: 8px" @click="onClose">取消</a-button>
        <a-button type="primary" @click="submit">提交</a-button>
      </div>
    </a-modal>
  </template>
  <script setup>
  import {reactive, ref} from 'vue';
  import {message} from 'ant-design-vue';
  import {useSpinStore} from '/@/store/modules/system/spin';
  import { carCostTabulationApi } from '/@/api/business/car-cost/car-cost-tabulation-api';
  import { AUDIT_STATUS_ENUM } from '/@/constants/common-const';
  import { getContainer } from '/@/utils/container-util';

  const formRef = ref();
  const formDefault = {
    tabulationId: undefined,
    tabulationIdList: undefined,
    auditStatus: undefined,
    auditRemark: '',
  };
  let form = reactive({...formDefault});
  const rules = {auditStatus: [{required: true, message: '请选择审核结果'}]};

  const modalVisible = ref(false);
  const batchAuditFlag = ref(false);
  let extData = null;
  // 显示
  function showModal (tabulationId, extDataStr) {
    form.tabulationId = tabulationId;
    extData = extDataStr;
    modalVisible.value = true;
    batchAuditFlag.value = false;
  }
  function showBatchModal(tabulationIdList) {
    form.tabulationIdList = tabulationIdList;
    modalVisible.value = true;
    batchAuditFlag.value = true;
  }
  // 关闭
  function onClose() {
    Object.assign(form, formDefault)
    modalVisible.value = false;
    batchAuditFlag.value = false;
  }

  // 点击审核，验证表单
  function submit() {
    formRef.value
        .validate()
        .then(() => {
          if (form.auditStatus == AUDIT_STATUS_ENUM.REJECT.value && !form.auditRemark) {
            message.error('请输入审核备注');
            return;
          }
          if(form.auditStatus == AUDIT_STATUS_ENUM.AUDIT_PASS.value){
            Object.assign(form, { extData})
          }
          auditFlow();
        })
        .catch((error) => {
          console.log('error', error);
          message.error('参数验证错误，请仔细填写表单数据!');
        });
  }

  // 审核
  const emit = defineEmits(['refresh']);
  async function auditFlow() {
    try {
      useSpinStore().show();
      if(batchAuditFlag.value) {
        await carCostTabulationApi.auditBatch(form);
      }else{
        await carCostTabulationApi.audit(form);
      }
      message.info('提交成功');
      emit('refresh');
      onClose();
    } catch (e) {
      console.log(e);
    } finally {
      useSpinStore().hide();
    }
  }

  defineExpose({
    showModal,
    showBatchModal
  });
  </script>

  <style lang="less" scoped>
  .receive-time-tips {
    padding-top: 5px;
    line-height: 1.46;
    color: #f00;
  }
  </style>
