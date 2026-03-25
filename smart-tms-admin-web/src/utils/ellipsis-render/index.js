import { h } from 'vue';
import TextE from '/@/components/text-ellipsis/index.vue';

//使用需设置 width 以及 ellipsis 否则单元格将自动扩展 无法达到预期效果

export default function ellipsisRender({ text,column }){
    return h(TextE, {
        text,
        classKey:column.dataIndex
    })
}