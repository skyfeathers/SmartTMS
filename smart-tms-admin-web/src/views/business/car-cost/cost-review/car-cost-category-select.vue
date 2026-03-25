<!-- 支出方式 -->
<template>
  <a-select
      v-model:value="selectValue"
      :allowClear="true"
      :disabled="disabled"
      :mode="multiple ? 'multiple' : ''"
      :placeholder="props.placeholder"
      :showSearch="true"
      :size="size"
      :style="`width: ${width}`"
      optionFilterProp="label"
      @change="handleChange"
  >
    <a-select-option v-for="item in categoryList" :key="item.categoryId"
                     :label="`${item.categoryName}`">
      {{item.categoryName }}
    </a-select-option>
  </a-select>
</template>

<script setup>
import { onMounted, ref, watch } from 'vue';
import { carCostCategoryApi } from '/@/api/business/waybill/car-cost-category-api';

const props = defineProps({
  value: [Number, String, Object, Array],
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
  multiple: {
    type: Boolean,
    default: false,
  },
  costType: {
    type: [Number, null],
    required: true,
  }
});
const emit = defineEmits(['update:value', 'change']);

const selectValue = ref(props.value);

onMounted(()=>{
  if(props.costType){
    queryList();
  }
})

watch(()=>props.costType,(val)=>{
  if(val){
    console.log(val);
    queryList();
    emit('update:value', undefined);
  }else{
    selectValue.value = undefined;
    emit('update:value', undefined);
  }
})

// 箭头value变化
watch(
    () => props.value,
    (newValue) => {
      selectValue.value = newValue;
      handleChange(newValue)
    }
);

function handleChange (value) {
  let find = categoryList.value.find(e=>e.categoryId == value);
  emit('update:value', value);
  emit('change', value, find || {});
}

const categoryList = ref([]);


async function queryList () {
  let res = await carCostCategoryApi.queryByCostType(props.costType);
  categoryList.value = res.data;
}

</script>
