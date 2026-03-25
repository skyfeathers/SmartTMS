package net.lab1024.tms.admin.module.business.receive.domain.vo;

import lombok.Data;

import java.time.LocalDate;

@Data
public class WaybillBusinessDateVO {

    private Long waybillId;

    private LocalDate businessDate;
}
