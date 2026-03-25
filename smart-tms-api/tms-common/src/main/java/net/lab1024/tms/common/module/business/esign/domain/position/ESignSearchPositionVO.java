package net.lab1024.tms.common.module.business.esign.domain.position;

import lombok.Data;

import java.util.List;

/**
 * 搜索关键字坐标
 *
 * @author lihaifan
 * @date 2022/7/18 19:01
 */
@Data
public class ESignSearchPositionVO {

    /**
     * 文档id
     */
    private String fileId;

    /**
     * 关键字
     */
    private String keyword;

    /**
     * 位置列表
     */
    private List<ESignPositionInfoVO> positionList;
}
