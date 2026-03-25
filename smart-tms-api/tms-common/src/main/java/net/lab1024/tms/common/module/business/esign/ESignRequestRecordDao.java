package net.lab1024.tms.common.module.business.esign;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.common.module.business.esign.domain.entity.ESignRequestRecordEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * e签宝请求记录
 *
 * @author lihaifan
 * @date 2022/9/16 16:22
 */
@Mapper
@Component
public interface ESignRequestRecordDao extends BaseMapper<ESignRequestRecordEntity> {
}
