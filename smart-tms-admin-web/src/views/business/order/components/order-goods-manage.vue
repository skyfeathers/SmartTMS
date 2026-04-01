<!--
 * @Description:
 * @version:
 * @Author: lidoudou
 * @Date: 2022-07-15 19:26:11
 * @LastEditors: zhuoda
 * @LastEditTime: 2022-07-22
-->
<template>
  <a-card class="smart-margin-top10" size="small">
    <template #title>
      <div style="color:red">货物信息</div>
    </template>
    <a-table :columns="columns" :dataSource="tableData" :pagination="false" :scroll="{ x: 800 }" bordered size="small">
      <template #headerCell="{ column }">
        <template v-if="column.dataIndex == 'action'">
          <a @click="showModal()">添加货物</a>
        </template>
      </template>
      <template #bodyCell="{ text, record, index, column }">
        <template v-if="column.dataIndex === 'action'">
          <a @click="showModal(record)" style="margin-right:10px" type="link">编辑</a>
          <a @click="deleteItem(index)" type="link">删除</a>
        </template>
        <template v-if="column.dataIndex === 'goodsType'">
          {{record.goodsTypeName}}
        </template>
        <template v-if="column.dataIndex === 'goodsUnit'">
          {{$smartEnumPlugin.getDescByValue('GOODS_UNIT_TYPE_ENUM', record.goodsUnit)}}
        </template>
      </template>
      <template #emptyText>
          <div>
              暂无数据
          </div>
      </template>
    </a-table>

    <a-modal title="货物信息" v-model:open="visible" :width="760" @cancel="onClose">
      <a-form ref="formRef" :model="goodsForm" :rules="rules" :label-col="{ span: 5 }" :wrapper-col="{ span: 12 }">
        <a-form-item label="货物名称" name="orderGoodsId">
          <div style="display: flex; align-items: center;">
            <a-select v-model:value="goodsForm.orderGoodsId" placeholder="请选择货物名称" :fieldNames="{label: 'goodsName', value: 'goodsId'}" :options="goodsNameList" showSearch @change="changeGoodsName" />
            <a-button class="smart-margin-left10" type="link" @click="handleAddGoodsBtnClick">新增</a-button>
          </div>
        </a-form-item>
        <div class="goods-add" v-if="addGoodsTag">
          <a-form  layout="inline" ref="goodsAddFormRef" :model="goodsAddForm" :rules="goodsAddRules">
            <a-form-item label="" name="goodsName">
              <a-input v-model:value="goodsAddForm.goodsName" placeholder="货物名称" style="width: 100px"/>
            </a-form-item>
            <a-form-item label="" name="goodsType">
              <smart-dict-select
                style="width: 120px"
                keyCode="cargoTypeClassificationCode"
                v-model:value="goodsAddForm.goodsType"
                placeholder="货物类型"
            />
            </a-form-item>
            <a-form-item label="" name="sort">
              <a-input v-model:value="goodsAddForm.sort" placeholder="排序" style="width: 80px" type="number" />
            </a-form-item>
            <a-form-item>
              <a-button class="smart-margin-left10" type="primary" @click="handleAddGoods">提交</a-button>
              <a-button class="smart-margin-left10" @click="handleCancel">取消</a-button>
            </a-form-item>
            
          </a-form>
        </div>
        <a-form-item label="货物类型" name="goodsType">
          <smart-dict-select
              style="width: 100%"
              keyCode="cargoTypeClassificationCode"
              v-model:value="goodsForm.goodsType"
              @change="changeType"
              placeholder="请选择货物类型"
              disabled
          />
        </a-form-item>
        
        <a-form-item label="货物单位" name="goodsUnit">
          <smart-enum-select style="width: 100%" v-model:value="goodsForm.goodsUnit" placeholder="请选择货物单位"
                             enum-name="GOODS_UNIT_TYPE_ENUM"/>
        </a-form-item>
<!--        <a-form-item label="货物单价" name="goodsUnitPrice">
          <a-input-number v-model:value="goodsForm.goodsUnitPrice" :min="0" :precision="4" placeholder="请输入货物单价"
                          style="width: 100%"/>
        </a-form-item>-->
        <a-form-item label="货物量" name="goodsQuantity">
          <a-input-number v-model:value="goodsForm.goodsQuantity" :min="0" :precision="2" placeholder="请输入总数量"
                          style="width: 100%"/>
        </a-form-item>
        <a-form-item label="备注" name="remark">
          <a-input v-model:value="goodsForm.remark" placeholder="请输入备注"/>
        </a-form-item>
<!--        <a-form-item label="总计金额">
          {{getGoodsTotalAmount(goodsForm)}}
          <span v-if="GOODS_UNIT_TYPE_ENUM.UNIT_TOM.value == goodsForm.goodsUnit">（总计金额 = 货物重量 * 货物单价）</span>
          <span v-else>（总计金额 = 货物数量 * 货物单价 ）</span>
        </a-form-item>
        <a-form-item  label="总计金额说明">
          <span style="color: red"> 总金额根据货物单位的不同计算方式不同，除吨是按照毛重计算，其余全部按照数量计算</span>
        </a-form-item>-->

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
        <a-button type="primary" @click="onSubmit">提交</a-button>
      </div>
    </a-modal>
  </a-card>
</template>
<script setup>
import { reactive, ref, onMounted, watch, computed } from 'vue';
import { message } from 'ant-design-vue';
import { useSpinStore } from '/@/store/modules/system/spin';
import {GOODS_UNIT_TYPE_ENUM} from "/@/constants/business/order-const";
import SmartDictSelect from '/@/components/smart-dict-select/index.vue';
import SmartEnumSelect from '/@/components/smart-enum-select/index.vue';
import _ from 'lodash';
import {Decimal} from "decimal.js";
import { TRANSPORTATION_TYPE_ENUM } from '/@/constants/business/transport-route-const';
import { goodsApi } from '/@/api/business/business-material/goods-api';

const props = defineProps({
  // 是否显示操作按钮
  actionFlag: {
    type: Boolean,
    default: true,
  },
  // 货主详情
  detail: {
    type: Object,
  },
});


let tableData = ref([]);
// ------- 新增、编辑商品 start --------
const defaultForm = {
  goodsType: null,
  goodsTypeName: null,
  goodsName: null,
  goodsQuantity: 0,
  goodsUnit: null,
  // goodsUnitPrice: 0,
  // goodsTotalPrice: 0,
  index: null,
  orderGoodsId: null,
  remark: null,
};
let goodsForm = reactive({ ...defaultForm });

const rules = {
  goodsType: [{ required: true, message: '请选择货物类型', trigger: 'change' }],
  goodsQuantity: [
    { required: true, message: '请输入总数量' },
    { min: 1, message: '总数量必须大于0' , type: 'number' },
  ],
  orderGoodsId: [{ required: true, message: '请选择货物名称' }],
  goodsUnit: [{ required: true, message: '请选择货物单位' }],
};
const formRef = ref();


// 保存联系人
function onSubmit () {
  formRef.value
      .validate()
      .then(async () => {
        useSpinStore().show();
        try {
          let params = Object.assign({}, goodsForm);
          console.log(params);
          tableData.value[goodsForm.index] = params;
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

// 是否展示弹窗
const visible = ref(false);

// 展示修改联系人弹窗
function showModal (itemInfo, index) {
  // 默认设置index
  if (!itemInfo) {
    goodsForm.index = tableData.value.length;
  } else {
    Object.assign(goodsForm, itemInfo);
  }
  getGoodsList();
  visible.value = true;
  
}

function onClose () {
  Object.assign(goodsForm, defaultForm);
  visible.value = false;
}

function changeType (value, item) {
  console.log('changeType', value, item);
  goodsForm.goodsTypeName = item.valueName;
  goodsForm.goodsName = item.valueName;
}

// ------- 新增、编辑联系人 end --------
// 删除联系人
function deleteItem (index) {
  tableData.value.splice(index, 1);
}


// 货物名称下拉列表
let goodsNameList = ref([]);
async function getGoodsList() {
  try {
    let responseModel = await goodsApi.list();
    goodsNameList.value = responseModel.data;
    
  } catch (e) {
    console.log(e);
  }
}

// 货物名称下拉列表选择事件
function changeGoodsName (value, item) {
  goodsForm.goodsName = item.goodsName;
  goodsForm.goodsType = item.goodsType;
  goodsForm.goodsTypeName = item.goodsTypeName;
  formRef.value.validateFields(['goodsType']);
}

onMounted(() => {
  if (!props.actionFlag) {
    let findIndex = columns.findIndex((e) => e.dataIndex == 'action');
    if (findIndex != -1) {
      columns.splice(findIndex, 1);
    }
  }
});

function resetTable(){
  tableData.value = []
}

defineExpose({
  resetTable,
  tableData
});

watch(
    () => props.detail,
    (e) => {
      if (!e) {
        return;
      }
      if (!_.isEmpty(e.goodsList)) {
        e.goodsList.forEach((item, index) => {
          item.index = index;
        });
        tableData.value = e.goodsList;
      }
      goodsForm.goodsUnit = GOODS_UNIT_TYPE_ENUM.UNIT_TOM.value;
      if (props.detail.businessTypeCode == TRANSPORTATION_TYPE_ENUM.CONTAINER_TRANSPORT.value) {
        goodsForm.goodsUnit = GOODS_UNIT_TYPE_ENUM.UNIT_CAR.value;
      }
    },
    {
      immediate: true,
      deep: true,
    }
);

// ------- 列表相关 start --------
const columns = reactive([
  {
    title: '操作',
    dataIndex: 'action',
  },
  {
    title: '货物类型',
    dataIndex: 'goodsTypeName',
  },
  {
    title: '货物名称',
    dataIndex: 'goodsName',
  },
  {
    title: '货物单位',
    dataIndex: 'goodsUnit',
  },
  {
    title: '货物量',
    dataIndex: 'goodsQuantity',
  },
  {
    title: '备注',
    dataIndex: 'remark',
  },
]);
// ------- 列表相关 start --------


// ------- 新增货物 --------

let addGoodsTag = ref(false);

const goodsAddFormRef = ref();

const defaultGoodsForm = {
  goodsName: '',
  goodsType: null,
  sort: null,
};
let goodsAddForm = reactive({ ...defaultGoodsForm });
const goodsAddRules = {
  goodsName: [{ required: true, message: '请输入货物名称' }],
  goodsType: [{ required: true, message: '请选择货物类型' }],
  sort: [{ required: true, message: '请输入排序' }],
};
// 新增货物按钮点击事件
function handleAddGoodsBtnClick() {
  goodsAddForm = Object.assign(goodsAddForm, defaultGoodsForm);
  addGoodsTag.value = true;
}


async function handleAddGoods() {
  try {
    await goodsAddFormRef.value.validate();
    await goodsApi.create({
      ...goodsAddForm
    });
    message.success('新增成功');
    addGoodsTag.value = false;
    getGoodsList();
  } catch (error) {
    console.log(error);
    message.error('参数验证错误，请仔细填写表单数据!');
    return;
  }
}

// 取消新增货物
function handleCancel() {
  goodsAddForm = Object.assign(goodsAddForm, defaultGoodsForm);
  addGoodsTag.value = false;
}



</script>
<style lang="less" scoped>
.goods-add {
  margin: 10px 0 10px 140px;
}
</style>