<template>
  <div class="smart-form-wrapper">
    <a-form class="smart-form" labelAlign="right" ref="formRef" :model="form" :rules="rules">
      <a-card title="基本信息">
        <a-row>
          <a-col :span="12">
            <a-form-item label="计提日期" name="depreciationDate">
              <a-date-picker v-model:value="form.depreciationDate" @change="queryAsset" valueFormat="YYYY-MM-01"
                            picker="month" style="width:200px"/>
            </a-form-item>
          </a-col>
          <a-col>
            <a-form-item label="备注" name="remark">
              <a-textarea v-model:value="form.remark" :rows="4" placeholder="请输入备注"/>
            </a-form-item>
          </a-col>
        </a-row>
      </a-card>
      <!-- 资产列表 -->
      <AssetList :assetList="assetList"/>
    </a-form>

    <a-card class="smart-margin-top5 flex-end" size="small">
      <a-space>
        <a-button @click="onClose">取消</a-button>
        <a-button type="primary" @click="onSubmit()">保存</a-button>
      </a-space>
    </a-card>
  </div>
</template>
<script setup>
import EnterpriseSelect from '/@/components/enterprise-select/index.vue';
import AssetList from './components/depreciation-asset-list.vue';

import { FILE_FOLDER_TYPE_ENUM } from '/@/constants/business/file-const';
import { CATEGORY_TYPE_ENUM } from '/@/constants/business/category-const';
import { ASSET_STATUS_ENUM } from '/@/constants/fixed-asset/asset-const';
import { reactive, ref, watch, computed, onMounted, provide } from 'vue';
import { message } from 'ant-design-vue';
import { SmartLoading } from '/@/components/smart-loading';
import { formWidth } from '/@/views/business/hooks/form-hooks';
import { useAppConfigStore } from '/@/store/modules/system/app-config';
import { useRoute, useRouter } from 'vue-router';
import { useUserStore } from '/@/store/modules/system/user';
import _ from 'lodash';
import { assetDepreciationApi } from '/@/api/fixed-asset/asset/depreciation-api';

// ---------------- 操作表单 ----------------
let route = useRoute();
let router = useRouter();

onMounted(() => {
  let assetId = route.query.assetId;
  if (assetId) {
    useUserStore().setTagName(route, '编辑归还');
    getDetail(assetId);
  } else {
    useUserStore().setTagName(route, '新建折旧单');
  }
});

// ------------------------ 表单 ------------------------

// 组件ref
const formRef = ref();

const formDefault = {
  depreciationDate: null,
  remark: null
};

let form = reactive({ ...formDefault });

const rules = {
  depreciationDate: [{ required: true, message: '请选择计提日期' }],
};

// 点击确定，验证表单
async function onSubmit () {
  try {
    await formRef.value.validateFields();
    save();
  } catch (err) {
    console.log(err);
  }
}

// 新建、编辑API
async function save () {
  SmartLoading.show();
  try {
    let param = _.cloneDeep(form);
    await assetDepreciationApi.add(param);
    message.success('创建成功');
    onClose();
  } catch (err) {
    console.log(err);
  } finally {
    SmartLoading.hide();
  }
}

// 关闭
function onClose () {
  useUserStore().closePage(route, router);
}

const assetList = ref([]);

async function queryAsset () {
  if (!form.depreciationDate) {
    assetList.value = [];
    return;
  }
  SmartLoading.show();
  try {
    const { data } = await assetDepreciationApi.queryAsset(_.cloneDeep(form));
    assetList.value = data;
    if(_.isEmpty(data)){
      message.error('计提日期下不存在资产')
    }
  } catch (err) {
    console.log(err);
  } finally {
    SmartLoading.hide();
  }
}

</script>
