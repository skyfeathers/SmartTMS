<template>
  <div>
    <a-form class="smart-query-form" v-privilege="'system:menu:query'">
      <a-row class="smart-query-form-row">
        <a-form-item label="关键字" class="smart-query-form-item">
          <a-input style="width: 300px" v-model:value="queryForm.keywords" placeholder="菜单名称/路由地址/组件路径/权限字符串" />
        </a-form-item>

        <a-form-item label="类型" class="smart-query-form-item">
          <smart-enum-select width="120px" v-model:value="queryForm.menuType" placeholder="请选择类型" enum-name="MENU_TYPE_ENUM" />
        </a-form-item>

        <a-form-item label="禁用" class="smart-query-form-item">
          <smart-enum-select width="120px" enum-name="FLAG_NUMBER_ENUM" v-model:value="queryForm.disabledFlag" />
        </a-form-item>

        <a-form-item class="smart-query-form-item smart-margin-left10">
          <a-button type="primary" @click="query">
            <template #icon>
              <SearchOutlined />
            </template>
            查询
          </a-button>

          <a-button @click="resetQuery">
            <template #icon>
              <ReloadOutlined />
            </template>
            重置
          </a-button>
          <a-button class="smart-margin-left20" @click="moreQueryConditionFlag = !moreQueryConditionFlag">
            <template #icon>
              <MoreOutlined />
            </template>
            {{ moreQueryConditionFlag ? '收起' : '展开' }}
          </a-button>
        </a-form-item>
      </a-row>

      <a-row class="smart-query-form-row" v-show="moreQueryConditionFlag">
        <a-form-item label="外链" class="smart-query-form-item">
          <smart-enum-select width="120px" enum-name="FLAG_NUMBER_ENUM" v-model:value="queryForm.frameFlag" />
        </a-form-item>

        <a-form-item label="缓存" class="smart-query-form-item">
          <smart-enum-select width="120px" enum-name="FLAG_NUMBER_ENUM" v-model:value="queryForm.cacheFlag" />
        </a-form-item>

        <a-form-item label="显示" class="smart-query-form-item">
          <smart-enum-select width="120px" enum-name="FLAG_NUMBER_ENUM" v-model:value="queryForm.visibleFlag" />
        </a-form-item>
      </a-row>
    </a-form>

    <a-card size="small" :bordered="false" :hoverable="true">
      <a-row class="smart-table-btn-block">
        <div class="smart-table-operate-block">
          <a-button type="primary" size="small" @click="showDrawer" v-privilege="'system:menu:Add'">
            <template #icon>
              <PlusOutlined />
            </template>
            添加菜单
          </a-button>

          <a-button type="primary" danger size="small" @click="batchDelete" :disabled="!hasSelected" v-privilege="'system:menu:batch:Delete'">
            <template #icon>
              <DeleteOutlined />
            </template>
            批量删除
          </a-button>
        </div>
        <div class="smart-table-setting-block"></div>
      </a-row>

      <a-table
        :row-selection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
        size="small"
        :defaultExpandAllRows="true"
        :dataSource="tableData"
        bordered
        :columns="columns"
        :loading="tableLoading"
        rowKey="menuId"
        :pagination="false"
      >
        <template #bodyCell="{ text, record, index, column }">
          <template v-if="column.dataIndex === 'menuType'">
            <a-tag :color="menuTypeColorArray[text]">{{ $smartEnumPlugin.getDescByValue('MENU_TYPE_ENUM', text) }}</a-tag>
          </template>
          <template v-if="column.dataIndex === 'platformFlag'">
            <a-tag v-if="text" color="green">平台</a-tag>
            <a-tag v-else>租户</a-tag>
          </template>
          <template v-if="column.dataIndex === 'component'">
            <span>{{ record.frameFlag ? record.frameUrl : record.component }}</span>
          </template>

          <template v-if="column.dataIndex === 'frameFlag'">
            <span>{{ $smartEnumPlugin.getDescByValue('FLAG_NUMBER_ENUM', text) }}</span>
          </template>

          <template v-if="column.dataIndex === 'cacheFlag'">
            <span>{{ $smartEnumPlugin.getDescByValue('FLAG_NUMBER_ENUM', text) }}</span>
          </template>

          <template v-if="column.dataIndex === 'disabledFlag'">
            <span>{{ $smartEnumPlugin.getDescByValue('FLAG_NUMBER_ENUM', text) }}</span>
          </template>

          <template v-if="column.dataIndex === 'icon'">
            <component :is="$antIcons[text]" />
          </template>

          <template v-if="column.dataIndex === 'operate'">
            <div class="smart-table-operate">
              <a-button v-privilege="'system:menu:Update'" type="link" size="small" @click="showDrawer(record)">编辑</a-button>
              <a-button v-privilege="'system:menu:Delete'" danger type="link" @click="singleDelete(record)">删除</a-button>
            </div>
          </template>
        </template>
      </a-table>
    </a-card>

    <MenuOperateModal ref="menuOperateModal" @reloadList="query" />
  </div>
</template>
<script setup>
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
import { message, Modal } from 'ant-design-vue';
import _ from 'lodash';
import { computed, createVNode, onMounted, reactive, ref } from 'vue';
import MenuOperateModal from './components/menu-operate-modal.vue';
import { buildMenuTableTree, filterMenuByQueryForm } from './menu-data-handler';
import { columns } from './menu-list-table-columns';
import { menuApi } from '/@/api/system/menu/menu-api';
import SmartEnumSelect from '/@/components/smart-enum-select/index.vue';
import { SmartLoading } from '/@/components/smart-loading';

// ------------------------ 表格渲染 ------------------------
const menuTypeColorArray = ['red', 'blue', 'orange', 'green'];

// ------------------------ 查询表单 ------------------------
const queryFormState = {
  keywords: '',
  menuType: undefined,
  frameFlag: undefined,
  cacheFlag: undefined,
  visibleFlag: undefined,
  disabledFlag: undefined,
};
const queryForm = reactive({ ...queryFormState });
//展开更多查询参数
const moreQueryConditionFlag = ref(true);

// ------------------------ table表格数据和查询方法 ------------------------

const tableLoading = ref(false);
const tableData = ref([]);

function resetQuery() {
  Object.assign(queryForm, queryFormState);
  query();
}

onMounted(query);

async function query() {
  try {
    tableLoading.value = true;
    let responseModel = await menuApi.queryMenu();
    // 过滤搜索条件
    const filtedMenuList = filterMenuByQueryForm(responseModel.data, queryForm);
    // 递归构造树形结构，并付给 TableTree组件
    tableData.value = buildMenuTableTree(filtedMenuList);
  } catch (e) {
    console.log(e);
  } finally {
    tableLoading.value = false;
  }
}

// -------------- 多选操作 --------------
const selectedRowKeys = ref([]);
let selectedRows = [];
const hasSelected = computed(() => selectedRowKeys.value.length > 0);

function onSelectChange(keyArray, selectRows) {
  selectedRowKeys.value = keyArray;
  selectedRows = selectRows;
}

function singleDelete(record) {
  confirmBatchDelete([record]);
}

function batchDelete() {
  confirmBatchDelete(selectedRows);
}

function confirmBatchDelete(menuArray) {
  const menuNameArray = menuArray.map((e) => e.menuName);
  Modal.confirm({
    title: '确定要删除如下菜单吗?',
    icon: createVNode(ExclamationCircleOutlined),
    content: _.join(menuNameArray, '、'),
    okText: '删除',
    okType: 'danger',
    onOk() {
      console.log('OK');
      const menuIdList = menuArray.map((e) => e.menuId);
      requestDatchDelete(menuIdList);
      selectedRows = [];
    },
    cancelText: '取消',
    onCancel() {},
  });

  async function requestDatchDelete(menuIdList) {
    SmartLoading.show();
    try {
      await menuApi.batchDeleteMenu(menuIdList);
      message.success('删除成功!');
      query();
    } catch (e) {
      console.log(e);
    } finally {
      SmartLoading.hide();
    }
  }
}

// -------------- 添加、修改 右侧抽屉 --------------
const menuOperateModal = ref();
function showDrawer(rowData) {
  menuOperateModal.value.showDrawer(rowData);
}
</script>
