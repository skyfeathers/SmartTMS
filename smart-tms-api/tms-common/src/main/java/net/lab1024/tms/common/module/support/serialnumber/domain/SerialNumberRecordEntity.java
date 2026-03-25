package net.lab1024.tms.common.module.support.serialnumber.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @Auther: 罗伊
 * @Date: 2018/8/7 0007 13:33
 * @Description:
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_serial_number_record")
public class SerialNumberRecordEntity {

    /**
     * 单号id
     */
    private Integer serialNumberId;

    /**
     * 记录日期
     */
    private LocalDate recordDate;

    /**
     * 最后更新值
     */
    private Long lastNumber;

    /**
     * 上次生成时间
     */
    private LocalDateTime lastTime;

    /**
     * 数量
     */
    private Long count;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;

}
