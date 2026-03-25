package net.lab1024.tms.common.module.business.esign.domain.flow;

import lombok.Data;

/**
 * 签署区位置信息
 *
 * @author lihaifan
 * @date 2022/7/18 18:02
 */
@Data
public class ESignPosBeanForm {

    /**
     * 页码信息，
     *
     * 当签署区signType为2时, 页码可以'-'分割指定页码范围, 传all代表全部页码。
     *
     * 其他情况只能是数字
     */
    private String posPage;

    /**
     * x坐标，坐标为印章中心点
     */
    private Float posX;

    /**
     * y坐标，坐标为印章中心点
     */
    private Float posY;
}
