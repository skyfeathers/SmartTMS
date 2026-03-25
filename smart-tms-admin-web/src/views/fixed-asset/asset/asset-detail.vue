<!--
 * @Description:
 * @version:
 * @Author: lidoudou
 * @Date: 2022-07-16 10:05:46
 * @LastEditors: zhuoda
 * @LastEditTime: 2022-08-02
-->
<template>
  <a-card size="small" title="基本信息">
    <a-page-header>
      <div class="content">
        <div class="main">
          <a-descriptions :column="3" size="small">
            <a-descriptions-item label="所属分类">{{ assetInfo.categoryName }}</a-descriptions-item>
            <a-descriptions-item label="资产编号">{{ assetInfo.assetNo }}</a-descriptions-item>
            <a-descriptions-item label="资产名称">{{ assetInfo.assetName }}</a-descriptions-item>

            <a-descriptions-item label="资产来源">{{ assetInfo.sourceId[0]?.valueName }}</a-descriptions-item>
            <a-descriptions-item label="品牌">
              {{ assetInfo.brand }}
            </a-descriptions-item>
            <a-descriptions-item label="规格型号">{{ assetInfo.model }}</a-descriptions-item>

            <a-descriptions-item label="设备序列号">{{ assetInfo.serialId }}</a-descriptions-item>
            <a-descriptions-item label="计量单位">{{ assetInfo.unit }}</a-descriptions-item>
            <a-descriptions-item label="存放位置">{{ assetInfo.locationName }}</a-descriptions-item>


            <a-descriptions-item label="所属公司">{{ assetInfo.enterpriseName }}</a-descriptions-item>
            <a-descriptions-item label="使用部门">{{assetInfo.departmentName}}</a-descriptions-item>
            <a-descriptions-item label="使用人">{{assetInfo.userName}}</a-descriptions-item>
            <a-descriptions-item label="使用期限（月）">{{ assetInfo.monthCount }}</a-descriptions-item>
            <a-descriptions-item label="购入日期">{{ assetInfo.purchaseTime }}</a-descriptions-item>
            <a-descriptions-item label="到期日期">{{ assetInfo.expireTime }}</a-descriptions-item>
            <a-descriptions-item label="备注">{{ assetInfo.remark }}</a-descriptions-item>
            <a-descriptions-item label="创建人">{{ assetInfo.createUserName }}</a-descriptions-item>
            <a-descriptions-item label="创建时间">{{ assetInfo.createTime }}</a-descriptions-item>
            <a-descriptions-item label="物品照片">
              <Upload
                  :default-file-list="assetInfo.attachment || []"
                  :showUploadBtn="false"
              />
            </a-descriptions-item>
          </a-descriptions>
        </div>
        <div class="extra">
          <div class="status">
            <div class="label">状态</div>
            <div class="value">{{ $smartEnumPlugin.getDescByValue('ASSET_STATUS_ENUM',assetInfo.status) }}</div>
          </div>
        </div>
      </div>
    </a-page-header>

  </a-card>
  <a-card class="smart-margin-top10" size="small">
    <a-tabs>
      <a-tab-pane key="supplier" tab="维保信息">
        <a-page-header>
          <div class="content">
            <div class="main">
              <a-descriptions :column="4" size="small">
                <a-descriptions-item label="供应商">{{ assetInfo.supplierName }}</a-descriptions-item>
                <a-descriptions-item label="供应商联系人">{{ assetInfo.supplierContactName }}</a-descriptions-item>
                <a-descriptions-item label="供应商联系方式">{{ assetInfo.supplierContactPhone }}</a-descriptions-item>
                <a-descriptions-item label="负责人">{{ assetInfo.supplierManagerName }}</a-descriptions-item>
                <a-descriptions-item label="维保到期">{{ assetInfo.repairExpireTime }}</a-descriptions-item>
                <a-descriptions-item label="维保说明">{{ assetInfo.supplierRemark }}</a-descriptions-item>
              </a-descriptions>
            </div>
          </div>
        </a-page-header>
      </a-tab-pane>
      <a-tab-pane key="operateLog" tab="操作记录">
        <DataTracerList
            :business-id="assetId"
            :business-type="DATA_TRACER_BUSINESS_TYPE_ENUM.ASSET.value"/>
      </a-tab-pane>
    </a-tabs>
  </a-card>

</template>
<script setup>
import Upload from '/@/components/upload/index.vue';
import DataTracerList from '/@/components/data-tracer/index.vue';

import { DATA_TRACER_BUSINESS_TYPE_ENUM } from '/@/constants/support/data-tracer-const';
import { ref, onMounted } from 'vue';
import { useSpinStore } from '/@/store/modules/system/spin';
import { useRoute, useRouter } from 'vue-router';
import { assetApi } from '/@/api/fixed-asset/asset/asset-api';

const route = useRoute();
const router = useRouter();

// -------------- 获取订单详情 --------------
let assetId = route.query.assetId;
let assetInfo = ref({
  sourceId: []
});

async function getDetail () {
  if (!assetId) {
    return;
  }
  try {
    useSpinStore().show();
    const { data } = await assetApi.getDetail(assetId);
    assetInfo.value = data;
  } catch (e) {
    console.log(e);
  } finally {
    useSpinStore().hide();
  }
}

onMounted(() => {
  getDetail();
});
</script>
<style lang="less" scoped>
.ant-page-header {
  padding: 0;
}

.content-card {
  ::v-deep(.ant-card-body) {
    padding-top: 0px;
  }
}
.content {
  display: flex;
}

.status {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  width: max-content;
  margin-right: 30px;

  .label {
    font-size: 15px;
    color: #8c8c8c;
  }

  .value {
    font-size: 25px;
    color: #1f1f1f;
  }
}

</style>
