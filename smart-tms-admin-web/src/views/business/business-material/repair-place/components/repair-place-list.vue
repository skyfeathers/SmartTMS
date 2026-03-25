<template>
    <a-form class="smart-query-form">
      <a-row class="smart-query-form-row">
        <a-form-item label="创建时间" class="smart-query-form-item">
          <a-space direction="vertical" :size="12">
            <a-range-picker v-model:value="searchDate" @change="dateChange"/>
          </a-space>
        </a-form-item>
        <a-form-item class="smart-query-form-item smart-margin-left10">
          <a-button type="primary" @click="search">
            <template #icon>
              <SearchOutlined/>
            </template>
            查询
          </a-button>
          <a-button @click="resetQuery">
            <template #icon>
              <ReloadOutlined/>
            </template>
            重置
          </a-button>
        </a-form-item>
      </a-row>
    </a-form>
    
    <a-card size="small" :bordered="false" :hoverable="true">
      <div class="amount-sum">维修厂家金额总计：<span>{{ amountSum || 0 }}</span> 元</div>
      <a-table
          :scroll="{ x: 1300 }"
          size="small"
          bordered
          :dataSource="tableData"
          :columns="columns"
          rowKey="repairId"
          :pagination="false"
      >
        <template #bodyCell="{ text, record, index, column }">
          <template v-if="column.dataIndex === 'contentVOList'">
              <div  v-for="(item,index) in record.contentVOList" :key="index">
                <div v-if="index<2" style="line-height: 20px;">
                  <span>{{ item.repairContent }}</span>,
                  <span>￥{{ item.repairAmount?.toFixed(1) }}</span>
                </div>
              </div>
                <div v-if="record.contentVOList?.length>2">
                  <a-button type="link" :size="size" @click="showContentVOListModal(record.contentVOList)">更多></a-button>
                </div>
          </template>
          <template v-else-if="column.dataIndex === 'attachment'">
            <file-preview :fileList="record.attachment" :width="50"/>
          </template>
        </template>
      </a-table>
  
      <!-- 维修内容弹出框 -->
      <a-modal :open="openContentVOListModal" title="维修内容" @ok="contentVOListModalOk" @cancel="contentVOListModalCancel">
        <a-table
        :columns="columnsContentVOList"
        :data-source="dataContentVOList"
        bordered
        :pagination="false"
        >
          <template #bodyCell="{ column, text }">
            <template v-if="column.dataIndex === 'repairAmount'">
              <span>￥{{ text?.toFixed(2) }}</span>
            </template>
          </template>
        </a-table>
      </a-modal>
  
      <div class="smart-query-table-page">
        <a-pagination
            showSizeChanger
            showQuickJumper
            show-less-items
            :pageSizeOptions="PAGE_SIZE_OPTIONS"
            :defaultPageSize="queryForm.pageSize"
            v-model:current="queryForm.pageNum"
            v-model:pageSize="queryForm.pageSize"
            :total="total"
            @change="ajaxQuery"
            @showSizeChange="ajaxQuery"
            :show-total="(total) => `共${total}条`"
        />
      </div>
    </a-card>
  </template>
  <script setup>
  import FilePreview from '/@/components/file-preview/index.vue';
  import {reactive, ref, onMounted} from 'vue';
  import { repairPlantApi } from '/@/api/business/business-material/repair-plant';
  import {PAGE_SIZE, PAGE_SIZE_OPTIONS} from '/@/constants/common-const';
  import { useRoute } from 'vue-router';
  // 维修弹出框相关
  const columnsContentVOList = [
    {
      title: '维修内容',
      dataIndex: 'repairContent',
      align:'center'
    },
    {
      title: '费用',
      dataIndex: 'repairAmount',
      align:'center'
    },
  ]
  const dataContentVOList=ref([])
  const openContentVOListModal = ref(false);
  
  function showContentVOListModal(contentVOList){
    openContentVOListModal.value=true
    dataContentVOList.value=contentVOList
  }
  function contentVOListModalOk(){
    openContentVOListModal.value = false;
  }
  function contentVOListModalCancel(){
    openContentVOListModal.value = false;
  }
  
  
  // table 相关
  const tableLoading = ref(false);
  const tableData = ref([]);
  const total = ref(0);
  const columns = reactive([
    {
      title: 'ID',
      width: 50,
      dataIndex: 'repairId',
    },
    {
      title: '维修车辆',
      dataIndex: 'vehicleNumber',
    },
    {
      title: '维修人',
      dataIndex: 'repairPerson',
    },
    {
      title: '维修时间',
      width: 100,
      dataIndex: 'repairDate',
    },
    {
      title: '维修内容',
      dataIndex: 'contentVOList',
    },
    {
      title: '金额合计',
      dataIndex: 'sumAmount',
    },
    {
      title: '维修配件附件',
      width: 120,
      dataIndex: 'attachment',
    },
    {
      title: '创建人',
      width: 100,
      dataIndex: 'createUserName',
    },
    {
      title: '创建时间',
      dataIndex: 'createTime',
    }
  ]);
  // 日期选择
  let searchDate = ref();
  
  function dateChange(dates, dateStrings) {
    queryForm.createStartTime = dateStrings[0];
    queryForm.createEndTime = dateStrings[1];
  }
  
  const route = useRoute();
  // 查询相关
  const queryFormState = {
    repairPlantId: route.query.repairPlantId,
    createEndTime: null,
    createStartTime: null,
    pageNum: 1,
    pageSize: PAGE_SIZE,
    searchCount: true
  };
  const queryForm = reactive({...queryFormState});
  
  async function ajaxQuery() {
    try {
      tableLoading.value = true;
      let responseModel = await repairPlantApi.queryRepairPlant(queryForm);
      const list = responseModel.data.list;
      total.value = responseModel.data.total;
      tableData.value = list;
    } catch (e) {
      console.log(e);
    } finally {
      tableLoading.value = false;
    }
  }

  const amountSum = ref();
  async function getRepairPlantAmountSum() {
    try {
      let res = await repairPlantApi.getRepairPlantAmountSum(route.query.repairPlantId);
      amountSum.value = res.data;
    } catch (e) {
      console.log(e);
    }
  }
  getRepairPlantAmountSum();
  function search () {
    queryForm.pageNum = 1;
    ajaxQuery();
  }
  
  function resetQuery() {
    searchDate.value = [];
    Object.assign(queryForm, queryFormState);
    ajaxQuery();
  }

  onMounted(ajaxQuery);
  </script>
  <style lang="less" scoped>
        .amount-sum{
            span{
                color: red;
            }
        }
  </style>