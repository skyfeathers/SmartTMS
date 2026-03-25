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
    >
      <a-select-option v-for="item in bankList" :key="item.bankId" :value="item.bankId">
        {{ item.accountName }}/{{ item.bankAccount }}
      </a-select-option>
    </a-select>
    <a-button @click="addBankAccount" type="primary"> 新增账户 </a-button>
  </div>
  <FleetBankAddModal ref="fleetBankAddModalRef" @reloadList="query"/>
</template>

<script setup>
import {onMounted, ref, watch} from 'vue';
import { fleetApi } from '/@/api/business/fleet/fleet-api';
import FleetBankAddModal from "./fleet-bank-add-modal.vue";

const props = defineProps({
  fleetId: Number,
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

const bankList = ref([]);
async function query() {
  let res = await fleetApi.bankList(props.fleetId);
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
let fleetBankAddModalRef = ref();
function addBankAccount(){
  fleetBankAddModalRef.value.showModal(props.fleetId);
}

onMounted(query);

</script>

<style scoped lang="less">
.bank {
  display: flex;
}

</style>
