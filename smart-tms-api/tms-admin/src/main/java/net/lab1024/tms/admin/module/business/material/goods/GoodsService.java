package net.lab1024.tms.admin.module.business.material.goods;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.admin.module.business.material.goods.domain.GoodsCreateForm;
import net.lab1024.tms.admin.module.business.material.goods.domain.GoodsQueryForm;
import net.lab1024.tms.admin.module.business.material.goods.domain.GoodsUpdateForm;
import net.lab1024.tms.admin.module.business.material.goods.domain.GoodsVO;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartPageUtil;
import net.lab1024.tms.common.module.business.material.goods.GoodsEntity;
import net.lab1024.tms.common.module.support.dict.DictCacheService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 业务资料-货物管理
 *
 * @author lihaifan
 * @date 2022/6/24 11:32
 */
@Service
@Slf4j
public class GoodsService {

    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private DictCacheService dictCacheService;
    /**
     * 分页查询货物模块
     *
     * @param queryDTO
     * @return
     */
    public ResponseDTO<PageResult<GoodsVO>> queryByPage(GoodsQueryForm queryDTO) {
        queryDTO.setDeletedFlag(Boolean.FALSE);
        Page<?> page = SmartPageUtil.convert2PageQuery(queryDTO);
        List<GoodsVO> goodsVOS = goodsDao.queryPage(page, queryDTO);
        if(CollectionUtils.isNotEmpty(goodsVOS)) {
            goodsVOS.forEach(e->{
                e.setGoodsTypeName(dictCacheService.selectValueNameByValueCode(e.getGoodsType()));
            });
        }
        PageResult<GoodsVO> pageResult = SmartPageUtil.convert2PageResult(page, goodsVOS);
        return ResponseDTO.ok(pageResult);
    }

    public ResponseDTO<List<GoodsVO>> queryList(Long enterpriseId) {
        List<GoodsVO> goodsVOS = goodsDao.queryList(enterpriseId, Boolean.FALSE);
        if(CollectionUtils.isNotEmpty(goodsVOS)) {
            goodsVOS.forEach(e->{
                e.setGoodsTypeName(dictCacheService.selectValueNameByValueCode(e.getGoodsType()));
            });
        }
        return ResponseDTO.ok(goodsVOS);
    }

    /**
     * 查询货物详情
     *
     * @param goodsId
     * @return
     */
    public ResponseDTO<GoodsVO> getDetail(Long goodsId) {
        // 校验货物是否存在
        GoodsVO goodsVO = goodsDao.getDetail(goodsId, Boolean.FALSE);
        if (Objects.isNull(goodsVO)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "站点不存在");
        }
        return ResponseDTO.ok(goodsVO);
    }

    /**
     * 新建货物
     *
     * @param createVO
     * @return
     */
    public ResponseDTO<String> createGoods(GoodsCreateForm createVO, Long enterpriseId) {
        // 数据插入
        GoodsEntity goodsEntity = SmartBeanUtil.copy(createVO, GoodsEntity.class);
        goodsEntity.setEnterpriseId(enterpriseId);
        goodsDao.insert(goodsEntity);
        return ResponseDTO.ok();
    }

    /**
     * 编辑货物
     *
     * @param updateVO
     * @return
     */
    public ResponseDTO<String> updateGoods(GoodsUpdateForm updateVO) {
        Long goodsId = updateVO.getGoodsId();
        // 校验堆场是否存在
        GoodsEntity goodsEntity = goodsDao.selectById(goodsId);
        if (Objects.isNull(goodsEntity) || goodsEntity.getDeletedFlag()) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "站点不存在");
        }
        // 数据编辑
        GoodsEntity updateEntity = SmartBeanUtil.copy(updateVO, GoodsEntity.class);
        goodsDao.updateById(updateEntity);
        return ResponseDTO.ok();
    }


    /**
     * 删除货物
     *
     * @param goodsId
     * @return
     */
    public ResponseDTO<String> deleteGoods(Long goodsId) {
        // 校验货物是否存在
        GoodsEntity goodsEntity = goodsDao.selectById(goodsId);
        if (Objects.isNull(goodsEntity) || goodsEntity.getDeletedFlag()) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "站点不存在");
        }
        goodsDao.deleteGoods(goodsId, Boolean.TRUE);
        return ResponseDTO.ok();
    }
}
