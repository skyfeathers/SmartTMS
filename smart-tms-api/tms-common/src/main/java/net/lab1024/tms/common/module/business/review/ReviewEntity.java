package net.lab1024.tms.common.module.business.review;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.tms.common.module.support.datatracer.annoation.DataTracerFieldDoc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/***
 * 审车表
 *
 * @author zhaoxinyang
 * @date 2023/10/18 09:38
 */
@Data
@TableName("t_review")
public class ReviewEntity {

    /**
     * 审车表ID
     */
    @TableId(type = IdType.AUTO)
    @DataTracerFieldDoc("审车ID")
    private Long reviewId;

    /**
     * 车辆ID
     */
    private Long vehicleId;

    /**
     * 所属企业ID
     */
    private Long enterpriseId;

    /**
     * 审车人
     */
    @DataTracerFieldDoc("审车人")
    private String reviewPerson;

    /**
     * 审车费用
     */
    @DataTracerFieldDoc("审车费用")
    private BigDecimal reviewAmount;

    /**
     * 审车地点
     */
    @DataTracerFieldDoc("审车地点")
    private String reviewPlant;

    /**
     * 备注
     */
    @DataTracerFieldDoc("备注")
    private String remark;

    /**
     * 审车时间
     */
    @DataTracerFieldDoc("审车时间")
    private LocalDate reviewDate;

    /**
     * 删除状态
     */
    private Boolean deletedFlag;

    /**
     * 创建人ID
     */
    private Long createUserId;

    /**
     * 创建人名字
     */
    private String createUserName;

    /**
     * 修改人ID
     */
    private Long updateUserId;

    /**
     * 修改人名字
     */
    private String updateUserName;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}