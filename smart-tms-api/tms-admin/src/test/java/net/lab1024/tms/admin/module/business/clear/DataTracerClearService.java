package net.lab1024.tms.admin.module.business.clear;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerBusinessTypeEnum;
import net.lab1024.tms.common.module.support.datatracer.dao.DataTracerDao;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * @author lidoudou
 * @description:
 * @date 2023/5/29 下午5:46
 */
@Service
public class DataTracerClearService {
    @Autowired
    private DataTracerDao dataTracerDao;

    public void deleteByBusinessIdAndType(DataTracerBusinessTypeEnum dataTracerBusinessTypeEnum, Collection<Long> businessIdList) {
        if (null == dataTracerBusinessTypeEnum || CollectionUtils.isEmpty(businessIdList)) {
            return;
        }
        QueryWrapper qw = new QueryWrapper();
        qw.eq("business_type", dataTracerBusinessTypeEnum.getValue());
        qw.in("business_id", businessIdList);
        dataTracerDao.delete(qw);

    }
}