<!--
 * @Description: file content
 * @Author: yandy
 * @Date: 2022-07-14 21:13:34
 * @LastEditors:
 * @LastEditTime: 2022-07-14 21:13:34
-->
<template>
  <div class="bank">
    <a-select
        v-model:value="selectValue"
        :style="`width: ${width}`"
        :placeholder="props.placeholder"
        :allowClear="true"
        :size="size"
        @change="handleChange"
        :disabled="disabled"
        optionFilterProp="label"
    >
      <a-select-option v-for="item in bankList" :key="item.bankId" :label="`${item.accounttName}/${item.bankAccount}`"
                       :value="item.bankId">
        {{ item.accountName }}/{{ item.bankAccount }}
      </a-select-option>
    </a-select>
    <a-button @click="refresh" type="primary"> 刷新 </a-button>
    <a-button @click="addBankAccount" type="primary" class="smart-margin-left10"> 新增账户 </a-button>
  </div>
  <DriverBankAddModal ref="driverBankAddModalRef" @reloadList="query"/>
</template>

<script setup>
import {computed, onMounted, ref, watch} from 'vue';
import { driverApi } from '/@/api/business/driver/driver-api';
import DriverBankAddModal from "./driver-bank-add-modal.vue";

const props = defineProps({
  driverId: Number,
  value: [Number, String, Object],
  width: {
    type: String,
    default: '200px',
  },
  placeholder: {
    type: String,
    default: '请选择',
  },
  size: {
    type: String,
    default: 'default',
  },
  disabled: {
    type: Boolean,
    default: false,
  },
});


const selectValue = ref(props.value);
// 箭头value变化
watch(
    () => props.value,
    (newValue) => {
      selectValue.value = newValue;
    }
);


watch(
    () => props.driverId,
    (newValue) => {
      console.log("driver watch", newValue)
      query();
    }
);


const bankList = ref([]);
async function query() {
  let res = await driverApi.bankList(props.driverId);
  bankList.value = res.data;
}

const emit = defineEmits(['update:value', 'change']);
function handleChange(value) {
  let obj = null;
  if(value){
    obj = bankList.value.find(e => e.bankId == value);
  }
  emit('update:value', value);
  emit('change', value, obj);
}

// 添加新账户
let driverBankAddModalRef = ref();
function addBankAccount(){
  driverBankAddModalRef.value.showModal(props.driverId);
}

function refresh(){
  query();
}

onMounted(query);

</script>

<style scoped lang="less">
.bank {
  display: flex;
}

</style>

