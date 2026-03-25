package net.lab1024.tms.common.module.business.pic;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.module.business.pic.dao.CommonPicDao;
import net.lab1024.tms.common.module.business.pic.domain.entity.PicEntity;
import net.lab1024.tms.common.module.business.pic.domain.vo.PicSimpleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhuoda
 * @Date 2021/1/22
 */

@Service
public class CommonPicService {

    @Autowired
    private CommonPicDao commonPicDao;

    /**
     * 根据投放类型
     *
     * @param type
     * @return
     */
    public List<PicSimpleVO> selectByType(Integer type, Long enterpriseId) {
        List<PicEntity> picList = commonPicDao.selectByType(enterpriseId, type, Boolean.TRUE);
        return SmartBeanUtil.copyList(picList, PicSimpleVO.class);
    }

}
