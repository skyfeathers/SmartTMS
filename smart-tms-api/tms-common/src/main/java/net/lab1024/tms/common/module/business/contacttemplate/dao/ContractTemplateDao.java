package net.lab1024.tms.common.module.business.contacttemplate.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.common.module.business.contacttemplate.domain.entity.ContractTemplateEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2021/8/13 8:40
 */
@Mapper
@Component
public interface ContractTemplateDao extends BaseMapper<ContractTemplateEntity> {

    /**
     * 查询模板列表
     * @param disableFlag
     * @return
     */
    List<ContractTemplateEntity> selectByDisableFlag(@Param("disableFlag") Boolean disableFlag);
}
