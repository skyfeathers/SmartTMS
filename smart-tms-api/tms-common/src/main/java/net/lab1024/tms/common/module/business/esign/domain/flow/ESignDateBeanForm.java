package net.lab1024.tms.common.module.business.esign.domain.flow;

import lombok.Data;

/**
 * 签署日期信息
 *
 * @author lihaifan
 * @date 2022/7/18 18:04
 */
@Data
public class ESignDateBeanForm {

    /**
     * 签章日期字体大小（默认值12px）
     */
    private Integer fontSize;

    /**
     * 签章日期格式，支持的格式：
     *
     * yyyy年MM月dd日（默认值）
     *
     * yyyy-MM-dd
     *
     * yyyy/MM/dd
     */
    private String format;

    /**
     * 页码信息
     *
     * 注：autoExecute是否自动执行为true时，并且需要展示签署日期，则需要指定日期盖章页码 ，默认当前页
     */
    private Integer posPage;

    /**
     * x坐标 ，坐标原点为页面左下角
     *
     * 注：
     *
     * （1）autoExecute是否自动执行为true时，
     *
     * 且必须添加签署日期，若需指定日期位置，可传入x坐标值，不传x坐标值默认为0；
     *
     * （2）autoExecute是否自动执行为false时（页面手动签署），若需指定日期位置，posPage需与posX、posY三个参数同时传入，坐标值才会生效。
     */
    private Float posX;

    /**
     * y坐标 ，坐标原点为页面左下角
     *
     * 注：
     *
     * （1）autoExecute是否自动执行为true时，
     *
     * 且必须添加签署日期，若需指定日期位置，可传入y坐标值，不传y坐标值默认为0；
     *
     * （2）autoExecute是否自动执行为false时（页面手动签署），若需指定日期位置，posPage需与posX、posY三个参数同时传入，坐标值才会生效。
     */
    private Float posY;

}
