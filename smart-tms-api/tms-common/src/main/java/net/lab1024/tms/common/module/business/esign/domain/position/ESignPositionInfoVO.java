package net.lab1024.tms.common.module.business.esign.domain.position;

import lombok.Data;

import java.util.List;

/**
 * 位置列表
 *
 * @author lihaifan
 * @date 2022/7/18 19:04
 */
@Data
public class ESignPositionInfoVO {

    /**
     * 页码
     */
    private Integer pageIndex;

    /**
     * 坐标信息
     */
    private List<ESignCoordinateVO> coordinateList;
}
