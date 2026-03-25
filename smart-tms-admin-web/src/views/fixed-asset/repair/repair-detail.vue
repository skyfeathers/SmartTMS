<!--
  * 固定资产-维修登记
  *
  * @Author:    卓大
  * @Date:      2023-03-23 15:01:51
  * @Copyright  1024创新实验室 （ https://1024lab.net ）
-->
<template>
  <a-modal title="维修登记" width="1000px" :open="visibleFlag" @cancel="onClose" :maskClosable="false" :destroyOnClose="true">
    <a-form ref="formRef" :model="form" :label-col="{ span: 7 }">
      <a-row>
        <a-col :span="8">
          <a-form-item label="业务时间" name="businessDate">
            {{ form.businessDate }}
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="维修厂家" name="repairCompany">
            {{ form.repairCompany ? form.repairCompany[0].valueName :''}}
          </a-form-item>
        </a-col>

        <a-col :span="8">
          <a-form-item label="维修内容" name="content">
            {{ form.content }}
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="申请人员" name="applyUserId">
            {{ form.applyUserName }}
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="维修花费" name="repairCost">
            {{ form.repairCost }}
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="备注" name="remark">
            {{ form.remark }}
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="创建日期" name="remark">
            {{ form.createTime }}
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="维修配件附件">
            <file-preview :fileList="form.mountingsFiles" />
          </a-form-item>
        </a-col>

        <a-col :span="8">
          <a-form-item label="发票附件">
            <file-preview :fileList="form.invoiceFiles" />
          </a-form-item>
        </a-col>
      </a-row>

      <a-card class="smart-margin-top10" size="small">
        <a-tabs>
          <a-tab-pane key="supplier" tab="资产列表">
            <AssetTableSelect :assetList="form.assetList" ref="assetTableSelectRef" :operateFlag="false" />
          </a-tab-pane>
          <a-tab-pane key="operateLog" tab="操作记录">
            <DataTracerList
                :business-id="form.repairId"
                :business-type="DATA_TRACER_BUSINESS_TYPE_ENUM.ASSET_REPAIR.value"/>
          </a-tab-pane>
        </a-tabs>
      </a-card>

    </a-form>

    <template #footer>
      <a-space>
        <a-button v-if="form.status === ASSET_REPAIR_STATUS_ENUM.AUDIT.value" type="primary" @click="pass">审核通过</a-button>
        <a-button class="smart-margin-left20" v-if="form.status === ASSET_REPAIR_STATUS_ENUM.AUDIT.value" type="primary" @click="reject"  danger>审核驳回</a-button>
        <a-button class="smart-margin-left20" v-if="form.status === ASSET_REPAIR_STATUS_ENUM.REPAIRING.value" type="primary" @click="finish">完成维修</a-button>
        <a-button class="smart-margin-left20" @click="onClose">关闭</a-button>
      </a-space>
    </template>
  </a-modal>
</template>
<script setup>
import FilePreview from '/@/components/file-preview/index.vue';
import AssetTableSelect from '/@/components/fixed-asset/asset-table-select/index.vue';
import DataTracerList from '/@/components/data-tracer/index.vue';
import { DATA_TRACER_BUSINESS_TYPE_ENUM } from '/@/constants/support/data-tracer-const';

import { reactive, ref, nextTick } from 'vue';

import _ from 'lodash';
import { message,Modal } from 'ant-design-vue';
import { SmartLoading } from '/@/components/smart-loading';
import { repairApi } from '/@/api/fixed-asset/asset/repair-api';
import { smartSentry } from '/@/lib/smart-sentry';
import { ASSET_REPAIR_STATUS_ENUM } from '/@/constants/fixed-asset/repair-const';

// ------------------------ 事件 ------------------------

const emits = defineEmits(['reloadList']);

// ------------------------ 显示与隐藏 ------------------------
// 是否显示
const visibleFlag = ref(false);

function show(repairId) {
  Object.assign(form, formDefault);
  visibleFlag.value = true;
  getDetail(repairId);
}

function onClose() {
  Object.assign(form, formDefault);
  visibleFlag.value = false;
  emits('reloadList');
}

// ------------------------ 详情 ------------------------
async function getDetail(repairId) {
  SmartLoading.show();
  try {
    let detail = await repairApi.getDetail(repairId);
    Object.assign(form, detail.data);
  } catch (e) {
    smartSentry.captureError(e);
  } finally {
    SmartLoading.hide();
  }
}

// ------------------------ 表单 ------------------------

// 组件ref
const formRef = ref();
// 选择固定资产ref
const assetTableSelectRef = ref();

const formDefault = {
  repairId: undefined,
  repairCompany: undefined, //维修厂家
  businessDate: undefined, //业务日期
  content: undefined, //维修内容
  applyUserId: undefined, //申请维修人员
  applyUserName: undefined, //申请维修人员
  repairCost: undefined, //维修花费
  remark: undefined, //备注
  mountingsFiles: undefined, //维修配件附件
  invoiceFiles: undefined, //发票附件
  assetList: [], //资产id列表
};

let form = reactive({ ...formDefault });

// ---------------------------- 审核通过 ----------------------------
function pass() {

  Modal.confirm({
    title: `确认审核通过吗？`,
    closable: true,
    okText: '审核通过',
    okType: 'primary',
    onOk: async () => {
      try {
        SmartLoading.show();
        await repairApi.pass([form.repairId]);
        message.success('操作成功');
        getDetail(form.repairId);
      } catch (e) {
        smartSentry.captureError(e);
      } finally {
        SmartLoading.hide();
      }
    },
    cancelText: '取消',
    cancelType: 'warning',
  });
}
// ---------------------------- 审核驳回 ----------------------------
function reject() {

  Modal.confirm({
    title: `确认审核驳回吗？`,
    okText: '审核驳回',
    okType: 'primary',
    onOk: async () => {
      try {
        SmartLoading.show();
        await repairApi.reject([form.repairId]);
        message.success('操作成功');
        getDetail(form.repairId);
      } catch (e) {
        smartSentry.captureError(e);
      } finally {
        SmartLoading.hide();
      }
    },
    cancelText: '取消',
  });
}

// ---------------------------- 完成维修 ----------------------------
function finish() {
  Modal.confirm({
    title: `确认完成维修吗？`,
    okText: '维修完成',
    okType: 'primary',
    onOk: async () => {
      try {
        SmartLoading.show();
        await repairApi.finish([form.repairId]);
        message.success('操作成功');
        getDetail(form.repairId);
      } catch (e) {
        smartSentry.captureError(e);
      } finally {
        SmartLoading.hide();
      }
    },
    cancelText: '取消',
  });
}


defineExpose({
  show,
});
</script>
