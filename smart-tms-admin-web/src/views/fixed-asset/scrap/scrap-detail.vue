<!--
  * 固定资产-报废
  *
  * @Author:    卓大
  * @Date:      2023-03-23 15:01:51
  * @Copyright  1024创新实验室 （ https://1024lab.net ）
-->
<template>
  <a-modal title="报废详情" width="1000px" :open="visibleFlag" @cancel="onClose" :maskClosable="false" :destroyOnClose="true">
    <a-form ref="formRef" :model="form" :label-col="{ span: 6 }">
      <a-row>
        <a-col :span="8">
          <a-form-item label="报废时间" name="scrapTime">
            {{ form.scrapTime }}
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="创建时间" name="scrapTime">
            {{ form.createTime }}
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="申请人" name="scrapTime">
            {{ form.applyUserName }}
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <div style="padding:0px 10px">
            <div>报废说明：</div>
            <div>
              {{ form.scrapExplain }}
            </div>
          </div>
        </a-col>
      </a-row>

      <a-card class="smart-margin-top10" size="small">
        <a-tabs>
          <a-tab-pane key="supplier" tab="资产列表">
            <AssetTableSelect :assetList="form.assetList" ref="assetTableSelectRef" :operateFlag="false" />
          </a-tab-pane>
          <a-tab-pane key="operateLog" tab="操作记录">
            <DataTracerList
                :business-id="form.scrapId"
                :business-type="DATA_TRACER_BUSINESS_TYPE_ENUM.ASSET_CLEAR.value"/>
          </a-tab-pane>
        </a-tabs>
      </a-card>

    </a-form>

    <template #footer>
      <a-space>
        <a-button v-if="form.status === ASSET_SCRAP_STATUS_ENUM.AUDIT.value" type="primary" @click="pass">审核通过</a-button>
        <a-button class="smart-margin-left20" v-if="form.status === ASSET_SCRAP_STATUS_ENUM.AUDIT.value" type="primary" @click="reject" danger
          >审核驳回</a-button
        >
        <a-button class="smart-margin-left20" @click="onClose">关闭</a-button>
      </a-space>
    </template>
  </a-modal>
</template>
<script setup>
import { reactive, ref, nextTick } from 'vue';
import AssetTableSelect from '/@/components/fixed-asset/asset-table-select/index.vue';
import DataTracerList from '/@/components/data-tracer/index.vue';
import { DATA_TRACER_BUSINESS_TYPE_ENUM } from '/@/constants/support/data-tracer-const';

import _ from 'lodash';
import { message, Modal } from 'ant-design-vue';
import { SmartLoading } from '/@/components/smart-loading';
import { scrapApi } from '/@/api/fixed-asset/asset/scrap-api';
import { smartSentry } from '/@/lib/smart-sentry';
import { ASSET_SCRAP_STATUS_ENUM } from '/@/constants/fixed-asset/scrap-const';

// ------------------------ 事件 ------------------------

const emits = defineEmits(['reloadList']);

// ------------------------ 显示与隐藏 ------------------------
// 是否显示
const visibleFlag = ref(false);

function show(scrapId) {
  Object.assign(form, formDefault);
  visibleFlag.value = true;
  getDetail(scrapId);
}

function onClose() {
  Object.assign(form, formDefault);
  visibleFlag.value = false;
  emits('reloadList');
}

// ------------------------ 详情 ------------------------
async function getDetail(scrapId) {
  SmartLoading.show();
  try {
    let detail = await scrapApi.getDetail(scrapId);
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
  scrapId: undefined,
  scrapTime: undefined, //报废日期
  explain: undefined, //维修内容
  applyUserId: undefined, //申请报废人员
  applyUserName: undefined, //申请报废人员
  createTime: undefined, //创建时间
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
        await scrapApi.pass([form.scrapId]);
        message.success('操作成功');
        getDetail(form.scrapId);
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
        await scrapApi.reject([form.scrapId]);
        message.success('操作成功');
        getDetail(form.scrapId);
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
