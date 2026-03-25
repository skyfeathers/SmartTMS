package net.lab1024.tms.common.module.business.shipper.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.tms.common.constant.AuditStatusEnum;
import net.lab1024.tms.common.module.business.oa.enterprise.dao.CommonEnterpriseDao;
import net.lab1024.tms.common.module.business.shipper.constant.ShipperGradeEnum;
import net.lab1024.tms.common.module.business.shipper.constant.ShipperNatureEnum;
import net.lab1024.tms.common.module.business.shipper.constant.ShipperTagEnum;
import net.lab1024.tms.common.module.support.datatracer.annoation.DataTracerFieldBoolean;
import net.lab1024.tms.common.module.support.datatracer.annoation.DataTracerFieldDoc;
import net.lab1024.tms.common.module.support.datatracer.annoation.DataTracerFieldEnum;
import net.lab1024.tms.common.module.support.datatracer.annoation.DataTracerFieldSql;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * [ 货主 ]
 *
 * @author yandanyang
 * @date 2020/7/30 14:28
 */
@Data
@TableName("t_shipper")
public class ShipperEntity {

    @TableId(type = IdType.AUTO)
    private Long shipperId;

    /**
     * 货主性质 企业或个人
     */
    @DataTracerFieldDoc("性质")
    @DataTracerFieldEnum(enumClass = ShipperNatureEnum.class)
    private Integer shipperNature;

    /**
     * 货主名称
     */
    @DataTracerFieldDoc("名称")
    private String consignor;

    /**
     * 简称
     */
    @DataTracerFieldDoc("简称")
    private String shortName;

    /**
     * 是否需要开票
     */
    private Boolean makeInvoiceFlag;

    /**
     * 所属公司
     */
    @DataTracerFieldDoc("所属公司")
    @DataTracerFieldSql(relateColumn = "enterpriseId", relateDisplayColumn = "enterpriseName", relateMapper = CommonEnterpriseDao.class)
    private Long enterpriseId;

    /**
     * 税点
     */
    @DataTracerFieldDoc("税点")
    private BigDecimal taxPoint;

    /**
     * 是否扣税
     */
    @DataTracerFieldDoc("是否扣税")
    private Boolean deductTaxFlag;

    /**
     * 扣税比例
     */
    private BigDecimal deductTaxRatio;

    @DataTracerFieldDoc("等级")
    @DataTracerFieldEnum(enumClass = ShipperGradeEnum.class)
    private Integer grade;

    @DataTracerFieldDoc("标签")
    @DataTracerFieldEnum(enumClass = ShipperTagEnum.class)
    private Integer tagType;
    /**
     * 货主电话
     */
    @DataTracerFieldDoc("电话")
    private String shipperPhone;
    /**
     * 传真
     */
    @DataTracerFieldDoc("fax")
    private String fax;
    /**
     * 营业执照号
     */
    @DataTracerFieldDoc("社会统一信用代码")
    private String consignorId;
    /**
     * 营业执照图片
     */
    private String consignorImage;
    /**
     * 法人姓名
     */
    @DataTracerFieldDoc("法人姓名")
    private String legalPersonName;
    /**
     * 法人身份证号
     */
    @DataTracerFieldDoc("法人身份证号")
    private String legalPersonIdNumber;
    /**
     * 法人电话
     */
    @DataTracerFieldDoc("法人电话")
    private String legalPersonPhone;


    @DataTracerFieldDoc("所在地区")
    private String area;

    /**
     * 备注
     */
    private String remark;

    /**
     * 助记码
     */
    @DataTracerFieldDoc("助记码")
    private String mnemonicCode;

    /**
     * 账期
     */
    private Integer accountPeriod;

    /**
     * 校验状态
     */
    private Boolean verifyFlag;

    /**
     * 禁用状态
     */
    @DataTracerFieldDoc("禁用状态")
    @DataTracerFieldBoolean
    private Boolean disableFlag;

    /**
     * 附件信息
     */
    private String attachment;

    /**
     * 审核状态
     *
     * @see AuditStatusEnum
     */
    private Integer auditStatus;

    /**
     * 删除标识
     */
    private Boolean deletedFlag;

    private Long createUserId;

    private String createUserName;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
