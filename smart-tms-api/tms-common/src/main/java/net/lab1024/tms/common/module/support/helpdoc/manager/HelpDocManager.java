package net.lab1024.tms.common.module.support.helpdoc.manager;

import net.lab1024.tms.common.module.support.helpdoc.dao.HelpDocDao;
import net.lab1024.tms.common.module.support.helpdoc.domain.entity.HelpDocEntity;
import net.lab1024.tms.common.module.support.helpdoc.domain.form.HelpDocRelationForm;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 帮助文档 manager
 *
 * @author zhuoda
 * @date 2022/08/14 21:21
 */
@Service
public class HelpDocManager {

    @Autowired
    private HelpDocDao helpDocDao;

    /**
     * 保存
     *
     * @param helpDocEntity
     * @param relationList
     */
    @Transactional(rollbackFor = Throwable.class)
    public void save(HelpDocEntity helpDocEntity, List<HelpDocRelationForm> relationList) {
        helpDocDao.insert(helpDocEntity);
        Long helpDocId = helpDocEntity.getHelpDocId();
        // 保存关联
        if (CollectionUtils.isNotEmpty(relationList)) {
            helpDocDao.insertRelation(helpDocId, relationList);
        }
    }

    /**
     * 更新
     *
     * @param helpDocEntity
     * @param relationList
     */
    @Transactional(rollbackFor = Throwable.class)
    public void update(HelpDocEntity helpDocEntity, List<HelpDocRelationForm> relationList) {
        helpDocDao.updateById(helpDocEntity);
        Long helpDocId = helpDocEntity.getHelpDocId();
        // 保存关联
        if (CollectionUtils.isNotEmpty(relationList)) {
            helpDocDao.deleteRelation(helpDocId);
            helpDocDao.insertRelation(helpDocId, relationList);
        }
    }
}
