package net.lab1024.tms.common.module.support.table;

import com.alibaba.fastjson.JSONArray;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.module.support.table.domain.TableColumnEntity;
import net.lab1024.tms.common.module.support.table.domain.TableColumnUpdateForm;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zhuoda
 * @date 2022-08-20 17:37
 */
@Service
public class TableColumnService {

    @Autowired
    private TableColumnDao tableColumnDao;

    /**
     * 获取 - 表格列
     * @return
     */
    public String getTableColumns(Long requestUserId, Integer tableId) {
        TableColumnEntity tableColumnEntity = tableColumnDao.selectByUserIdAndTableId(requestUserId, tableId);
        return tableColumnEntity == null ? null : tableColumnEntity.getColumns();
    }

    /**
     * 更新表格列
     * @param userId
     * @param updateForm
     * @return
     */
    public ResponseDTO<String> updateTableColumns(Long userId, TableColumnUpdateForm updateForm) {
        if (CollectionUtils.isEmpty(updateForm.getColumnList())) {
            return ResponseDTO.ok();
        }
        Integer tableId = updateForm.getTableId();
        TableColumnEntity tableColumnEntity = tableColumnDao.selectByUserIdAndTableId(userId, tableId);
        if (tableColumnEntity == null) {
            tableColumnEntity = new TableColumnEntity();
            tableColumnEntity.setTableId(tableId);
            tableColumnEntity.setUserId(userId);
            tableColumnEntity.setColumns(JSONArray.toJSONString(updateForm.getColumnList()));
            tableColumnDao.insert(tableColumnEntity);
        } else {
            tableColumnEntity.setColumns(JSONArray.toJSONString(updateForm.getColumnList()));
            tableColumnDao.updateById(tableColumnEntity);
        }
        return ResponseDTO.ok();
    }

    /**
     * 删除表格列
     * @param requestUserId
     * @param tableId
     * @return
     */
    public ResponseDTO<String> deleteTableColumn(Long requestUserId, Integer tableId) {
        tableColumnDao.delete(requestUserId,tableId);
        return ResponseDTO.ok();
    }
}
