<template>
  <default-home-card icon="StarTwoTone" title="已办待办">
    <a-row :gutter="[10, 10]">
      <a-col span="11">
        <div class="center column">
          <a-typography-title :level="3">待办</a-typography-title>
          <a-space direction="vertical" style="width: 100%">
            <div class="add-to-do">
              <a-input-search v-model:value="taskTitle" placeholder="添加任务" enter-button="添加" @search="addTask" />
            </div>
            <div v-for="(item, index) in toDoList" :key="index" :class="['to-do', { done: item.doneFlag }]">
              <a-checkbox v-model:checked="item.doneFlag">
                <span class="task">{{ item.title }}</span>
              </a-checkbox>
              <div class="star-icon" @click="itemStar(item)">
                <StarFilled v-if="item.starFlag" style="color: #ff8c00" />
                <StarOutlined v-else style="color: #c0c0c0" />
              </div>
            </div>
          </a-space>
        </div>
      </a-col>
      <a-col span="1">
        <div class="center">
          <a-divider style="height: 100%" type="vertical" />
        </div>
      </a-col>
      <a-col span="11">
        <div class="center column">
          <a-typography-title :level="4">已办</a-typography-title>
          <a-space direction="vertical" style="width: 100%">
            <div v-for="(item, index) in doneList" :key="index" :class="['to-do', { done: item.doneFlag }]">
              <a-checkbox v-model:checked="item.doneFlag">
                <span class="task">{{ item.title }}</span>
              </a-checkbox>
              <div class="star-icon" @click="itemStar(item)">
                <StarFilled v-if="item.starFlag" style="color: #ff8c00" />
                <StarOutlined v-else style="color: #c0c0c0" />
              </div>
            </div>
          </a-space>
        </div>
      </a-col>
    </a-row>
  </default-home-card>
</template>
<script setup>
import DefaultHomeCard from '/@/views/system/home/components/default-home-card.vue';
import { computed, ref } from 'vue';
import dayjs from 'dayjs';
import { message } from 'ant-design-vue';

let taskList = ref([
  {
    title: '周四下午提交周报',
    doneFlag: false,
    starFlag: false,
    starTime: 0,
  },
  {
    title: '周三晚上和老丁吃饭',
    doneFlag: false,
    starFlag: false,
    starTime: 0,
  },
  {
    title: '开发任务分配',
    doneFlag: false,
    starFlag: false,
    starTime: 0,
  },
  {
    title: '跟进团建事宜',
    doneFlag: false,
    starFlag: false,
    starTime: 0,
  },
  {
    title: '下个版本的需求确认',
    doneFlag: false,
    starFlag: false,
    starTime: 0,
  },
  {
    title: '线上版本发布',
    doneFlag: true,
    starFlag: true,
    starTime: dayjs().unix(),
  },
  {
    title: '周一财务报销',
    doneFlag: true,
    starFlag: false,
    starTime: 0,
  },
  {
    title: '抢消费券',
    doneFlag: true,
    starFlag: false,
    starTime: 0,
  },
]);

let toDoList = computed(() => {
  return taskList.value.filter((e) => !e.doneFlag).sort((a, b) => b.starTime - a.starTime);
});

let doneList = computed(() => {
  return taskList.value.filter((e) => e.doneFlag);
});

function itemStar(item) {
  item.starFlag = !item.starFlag;
  if (item.starFlag) {
    item.starTime = dayjs().unix();
  }
}

//-------------------------任务新建-----------------------
let taskTitle = ref('');
function addTask() {
  if (!taskTitle.value) {
    message.warn('请输入任务标题');
    return;
  }
  let data = {
    title: taskTitle.value,
    doneFlag: false,
    starFlag: false,
    starTime: 0,
  };
  taskList.value.unshift(data);
  taskTitle.value = '';
}
</script>
<style lang="less" scoped>
.center {
  display: flex;
  justify-content: center;
  height: 100%;

  &.column {
    flex-direction: column;
    justify-content: flex-start;
  }
}

.to-do {
  width: 100%;
  border: 1px solid #d3d3d3;
  border-radius: 4px;
  padding: 8px;
  display: flex;
  align-items: center;

  .star-icon {
    margin-left: auto;
    cursor: pointer;
  }

  &.done {
    text-decoration: line-through;
    color: #8c8c8c;

    .task {
      color: #8c8c8c;
    }
  }
}
</style>
