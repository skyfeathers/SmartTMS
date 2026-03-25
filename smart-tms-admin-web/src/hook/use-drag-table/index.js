import { computed, ref } from 'vue'
import { tableColumnApi } from '/@/api/support/table/table-column-api';
import { mergeColumn } from './smart-table-column-merge';
import _ from 'lodash';

function getColumns(columns, tableId) {
    return new Promise((resolve,reject)=>{
        if(!tableId){
            resolve(mergeColumn(columns, []));
        }
        let userTableColumnArray = [];
        try {
            tableColumnApi.getColumns(tableId).then(res => {
                if (res.data) {
                    userTableColumnArray = JSON.parse(res.data);
                    resolve(mergeColumn(columns, userTableColumnArray));
                }else{
                    resolve(mergeColumn(columns, []));
                }
            })
        } catch (e) {
            resolve(mergeColumn(columns, userTableColumnArray))
        }
    })
}

export default function useDragTable(columns, tableId, isDrag = true) {
    const columnsData = ref([])
    const tableWidth = computed(()=>{
        let width = 0;
        for(var i = 0; i < columnsData.value.length;i++){
            width = width + (columnsData.value[i].width ? columnsData.value[i].width : 150)
        }
        return width
    })
    let oldIndex = undefined;
    let newIndex = undefined;
    let scurceOldIndex = undefined;
    let scurceNewIndex = undefined;

    async function save(scurceColumns) {
        try {
            let columnList = [];
            for (let index = 0; index < scurceColumns.length; index++) {
                let item = scurceColumns[index];
                let column = {
                    columnKey: item.columnKey,
                    sort: index + 1,
                };
                if (item.width) {
                    column.width = item.width;
                }
                let selectedRowKeyList = columnsData.value.map(e=>e.dataIndex)
                column.showFlag = selectedRowKeyList.indexOf(item.dataIndex) > -1 ? true : false;
                columnList.push(column);
            }
            columnList = _.sortBy(columnList, (e) => e.sort);
            await tableColumnApi.updateTableColumn({
                tableId,
                columnList,
            });
        } catch (e) {
            console.log(e);
        } finally {
        }
    }

    function moveTableData(oldIndex, newIndex) {
        let scurceColumns = []
        getColumns(columns,tableId).then(res=>{
            scurceColumns = res;
            const currRow = scurceColumns.splice(oldIndex, 1)[0];
            scurceColumns.splice(newIndex, 0, currRow);
            save(scurceColumns);
        })
    }

    function customHeaderCell(record) {
        return {
            style: {
                cursor: 'pointer'
            },
            onMouseenter: (event) => {
                const ev = event || window.event
                const target = ev.target
                target.draggable = true
            },
            onMouseleave: () => {
                const ev = event || window.event
                const target = ev.target
                target.draggable = false
            },
            // 开始拖拽
            onDragstart: (event) => {
                const ev = event || window.event
                ev.stopPropagation()
                oldIndex = columnsData.value.findIndex(e => {
                    return e.dataIndex == record.dataIndex
                });
                if (tableId) {
                    getColumns(columns,tableId).then(res=>{
                        scurceOldIndex = res.findIndex(e => {
                            return e.dataIndex == record.dataIndex
                        })
                    })
                }
            },
            onDragover: (event) => {
                const ev = event || window.event
                ev.preventDefault()
                ev.dataTransfer.dropEffect = 'move'
                newIndex = columnsData.value.findIndex(e => {
                    return e.dataIndex || "" == record.dataIndex
                });
            },
            onDrop: (event) => {
                const ev = event || window.event
                ev.stopPropagation()
                newIndex = columnsData.value.findIndex(e => {
                    return e.dataIndex == record.dataIndex
                });
                if (newIndex === oldIndex) return
                let temp = columnsData.value[oldIndex];
                columnsData.value.splice(oldIndex, 1)
                columnsData.value.splice(newIndex, 0, temp)
                if (tableId) {
                    getColumns(columns,tableId).then(res=>{
                        scurceNewIndex = res.findIndex(e => {
                            return e.dataIndex == record.dataIndex
                        })
                        if (scurceNewIndex === scurceOldIndex) return
                        moveTableData(scurceOldIndex, scurceNewIndex);
                    })
                }
            },
        }
    }
    
    columnsData.value = isDrag ? columns.map(e => {
        return e.fixed ? e : {
            ...e,
            customHeaderCell: customHeaderCell
        }
    }) : columns

    return { columnsData, tableWidth }
}