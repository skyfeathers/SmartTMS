package net.lab1024.tms.common.module.business.repair.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.tms.common.module.business.material.repairplant.dao.CommonRepairPlantDao;
import net.lab1024.tms.common.module.support.datatracer.annoation.DataTracerFieldDoc;
import net.lab1024.tms.common.module.support.datatracer.annoation.DataTracerFieldSql;

import java.time.LocalDate;
import java.time.LocalDateTime;

/***
 * 车辆维修
 *
 * @author lidoudou
 * @date 2022/6/24 下午4:55
 */
@Data
@TableName("t_repair")
public class RepairEntity {

    /**
     * 维修ID
     */
    @TableId(type = IdType.AUTO)
    private Long repairId;

    /**
     * 模块ID
     */
    private Long moduleId;

    /**
     * 所属企业ID
     */
    private Long enterpriseId;

    /**
     * 模块类型
     */
    private Integer moduleType;

    /**
     * 维修厂家
     */
    @DataTracerFieldDoc("维修厂家")
    @DataTracerFieldSql(relateColumn = "repairPlantId",relateDisplayColumn = "repairPlantName", relateMapper = CommonRepairPlantDao.class)
    private Integer repairPlantId;

    /**
     * 维修人
     */
    @DataTracerFieldDoc("维修人")
    private String repairPerson;

    /**
     * 维修时间
     */
    @DataTracerFieldDoc("维修时间")
    private LocalDate repairDate;

    /**
     * 备注
     */
    @DataTracerFieldDoc("备注")
    private String remark;

    /**
     * 附件
     */
    @DataTracerFieldDoc("附件")
    private String attachment;

    /**
     * 删除标识
     */
    private Boolean deletedFlag;

    private Long createUserId;

    private String createUserName;

    private Long updateUserId;

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
