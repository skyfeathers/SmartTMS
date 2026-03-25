<template>
  <a-card size="small" :bordered="false" :hoverable="true">
    <a-alert>
      <template v-slot:message>
        <h4>SerialNumber 单号生成器介绍：</h4>
      </template>
      <template v-slot:description>
        <pre>
简介：SerialNumber是一个可以根据不同的日期、规则生成一系列特别单号的功能，比如订单号、合同号、采购单号等等。
原理：内部有三种实现方式： 1) 基于内存锁实现 （不支持分布式和集群）；  2) 基于redis锁实现 ；  3) 基于Mysql 锁for update 实现
- 支持随机生成和查询生成记录
- 支持动态配置
</pre
        >
      </template>
    </a-alert>

    <a-table size="small" bordered class="smart-margin-top10" :dataSource="tableData" :columns="columns" rowKey="tag" :pagination="false">
      <template #bodyCell="{ text, record, index, column }">
        <template v-if="column.dataIndex === 'action'">
          <div class="smart-table-operate">
            <a-button @click="generate(record)" type="link">生成</a-button>
            <a-button @click="showRecord(record.serialNumberId)" type="link">查看记录</a-button>
          </div>
        </template>
      </template>
    </a-table>
  </a-card>

  <!---生成表单--->
  <SerialNumberGenerateFormModal ref="generateForm" @refresh="ajaxQuery" />
  <!---生成记录--->
  <SerialNumberRecordList ref="recordList" />
</template>
<script setup>
  import { onMounted, reactive, ref } from 'vue';
  import { serialNumberApi } from '/@/api/support/serial-number/serial-number-api';
  import { defaultTimeRanges } from '/@/lib/default-time-ranges';
  import SerialNumberGenerateFormModal from './serial-number-generate-form-modal.vue';
  import SerialNumberRecordList from './serial-number-record-list.vue';

  //------------------------ 表格渲染 ---------------------

  const columns = reactive([
    {
      title: 'ID',
      dataIndex: 'serialNumberId',
    },
    {
      title: '业务',
      dataIndex: 'businessName',
    },
    {
      title: '格式',
      dataIndex: 'format',
    },
    {
      title: '循环周期',
      dataIndex: 'ruleType',
    },
    {
      title: '初始值',
      dataIndex: 'initNumber',
    },
    {
      title: '随机增量',
      dataIndex: 'stepRandomRange',
    },
    {
      title: '备注',
      dataIndex: 'remark',
    },
    {
      title: '上次产生单号',
      dataIndex: 'lastNumber',
    },
    {
      title: '上次产生时间',
      dataIndex: 'lastTime',
    },
    {
      title: '操作',
      dataIndex: 'action',
      fixed: 'right',
      width: 160,
    },
  ]);

  const tableLoading = ref(false);
  const tableData = ref([]);

  async function ajaxQuery() {
    try {
      tableLoading.value = true;
      let res = await serialNumberApi.getAll();
      tableData.value = res.data;
    } catch (e) {
      console.log(e);
    } finally {
      tableLoading.value = false;
    }
  }

  onMounted(ajaxQuery);

  // ------------------------------ 表格操作列： 生成 ------------------------------
  const generateForm = ref();
  function generate(record) {
    generateForm.value.showModal(record);
  }

  // ------------------------------ 表格操作列： 查看结果 ------------------------------

  const recordList = ref();
  function showRecord(serialNumberId) {
    recordList.value.showModal(serialNumberId);
  }
</script>
