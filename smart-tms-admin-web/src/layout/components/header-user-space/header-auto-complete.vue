<template>
    <div style="width: 200px;display: flex;align-items: center;">
        <a-auto-complete  v-model:value="text" @select="onSelect" @search="onSearch" style="width: 100%;" :options="options">
            <a-input ref="input" allowClear placeholder="请输入" @blur="blur">
                <template #prefix>
                    <search-outlined />
                </template>
            </a-input>
        </a-auto-complete>
    </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { QUICK_SEARCH_TYPE_ENUM } from '/@/constants/business/quick-const'
import { quickApi } from '/@/api/business/quick/quick-api'
import { message } from 'ant-design-vue'
//optionsDefault 数据格式 {value:搜索展示前缀,path:点击跳转路径,searchKey:搜索key}

const optionsDefault = ref([{
  value: QUICK_SEARCH_TYPE_ENUM.ORDER.desc,
  searchType: QUICK_SEARCH_TYPE_ENUM.ORDER.value,
  path: '/order/detail',
  searchKey: 'orderId'
},{
  value: QUICK_SEARCH_TYPE_ENUM.WAYBILL.desc,
  searchType: QUICK_SEARCH_TYPE_ENUM.WAYBILL.value,
  path: '/waybill/waybill-detail',
  searchKey: 'waybillId'
},{
  value: QUICK_SEARCH_TYPE_ENUM.PAY_ORDER.desc,
  searchType: QUICK_SEARCH_TYPE_ENUM.PAY_ORDER.value,
  path: '/pay/pay-order-detail',
  searchKey: 'payOrderId'
},{
  value: QUICK_SEARCH_TYPE_ENUM.RECEIVE_ORDER.desc,
  searchType: QUICK_SEARCH_TYPE_ENUM.RECEIVE_ORDER.value,
  path: '/receive-order/detail',
  searchKey: 'receiveOrderId'
}])

const emits = defineEmits(['hideSearch'])

const options = ref([])
const text = ref('')

const router = useRouter();
async function onSelect(e, row) {
    let keywords;
    if(text.value.includes(':')){
        keywords = text.value.split(':')[1]
    }else{
        keywords = text.value
    }
    try {
        const res = await quickApi.search({
            keywords,
            searchType:row.searchType
        })
        if(!res.data.successFlag){
            message.error('未找到对应数据!')
            return
        }
        router.push({
            path: row.path,
            query: {
                [row.searchKey]:res.data.id
            }
        })
    } catch (error) {
        
    }
}


function onSearch() {
    if(text.value.includes(':')){
        text.value = text.value.split(':')[1]
    }
    if(text.value){
        options.value = optionsDefault.value.map(e => {
            return {
                ...e,
                value: e.value + ':' + text.value
            }
        })
    }else{
        options.value = []
    } 
}

function blur(){
    if(!text.value){
        emits('hideSearch')
    }
}

const input = ref()
function focus(){
    input.value.focus()
}

defineExpose({
    focus
})
</script>

<style lang="less" scoped>
.content-card {
    width: 350px;

    ::v-deep(.ant-card-body) {
        padding-top: 0px;
    }
}

ul li :hover {
    cursor: pointer;
    color: @primary-color;
}
</style>