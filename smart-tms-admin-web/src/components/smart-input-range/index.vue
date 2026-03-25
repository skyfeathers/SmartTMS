<!--
 * @Description:
 * @Author: zhuoda
 * @Date: 2021-08-03
 * @LastEditTime: 2022-07-09
 * @LastEditors: zhuoda
-->
<template>
    <div class="container" :style="{
        'width': `${width}`
    }">
        <div><a-input type="number" v-model:value="startValue" placeholder="请输入" @change="changeDebounce"></a-input></div>
        <span>-</span>
        <div><a-input type="number" v-model:value="endValue" placeholder="请输入" @change="changeDebounce"></a-input></div>
    </div>
</template>

<script setup>
import { ref, watch } from 'vue';
import lodash from 'lodash'
const props = defineProps({
    enumName: String,
    value: Array,
    width: {
        type: String,
        default: '100%',
    },
});
const emit = defineEmits(['update:value', 'change']);

const startValue = ref()
const endValue = ref()

watch(() => props.value, (newVal) => {
    if (Array.isArray(newVal) && newVal.length == 2) {
        startValue.value = props.value[0]
        endValue.value = props.value[1]
    }else{
        startValue.value = undefined
        endValue.value = undefined
    }
})

const changeDebounce = lodash.debounce(change,500)
function change(){
    if((startValue.value || startValue.value === 0) && (endValue.value || endValue.value === 0)){
        if(startValue.value > endValue.value){
            emit('update:value',[endValue.value, startValue.value])
            emit('change',[endValue.value, startValue.value])
        }else{
            
            emit('update:value',[startValue.value, endValue.value])
            emit('change',[startValue.value, endValue.value])
        }
    }else if(!startValue.value && !endValue.value){
        emit('update:value',[endValue.value, startValue.value])
        emit('change',[endValue.value, startValue.value])
    }
}


</script>

<style lang="less" scoped>
.container {
    display: flex;
    align-items: center;
    div{
        flex: 1;
    }
    span {
        margin: 0 5px;
    }
}
</style>