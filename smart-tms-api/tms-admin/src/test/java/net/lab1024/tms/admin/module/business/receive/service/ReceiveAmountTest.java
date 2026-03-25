package net.lab1024.tms.admin.module.business.receive.service;

import cn.hutool.core.collection.CollectionUtil;
import com.google.common.collect.Lists;
import net.lab1024.tms.admin.TmsAdminApplicationTest;
import net.lab1024.tms.admin.module.business.receive.dao.ReceiveOrderWaybillDao;
import net.lab1024.tms.admin.module.business.receive.dao.ReceiveOrderDao;
import net.lab1024.tms.admin.module.business.receive.manager.ReceiveOrderManage;
import net.lab1024.tms.common.module.business.material.cost.constants.CostItemCategoryEnum;
import net.lab1024.tms.common.module.business.material.cost.constants.CostItemTypeEnum;
import net.lab1024.tms.common.module.business.receive.domain.ReceiveOrderWaybillEntity;
import net.lab1024.tms.common.module.business.receive.domain.ReceiveOrderEntity;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReceiveAmountTest extends TmsAdminApplicationTest {

    @Resource
    private ReceiveOrderDao receiveOrderDao;

    @Resource
    private ReceiveOrderWaybillDao receiveOrderWaybillDao;

    @Resource
    private ReceiveOrderManage receiveOrderManage;

    /**
     * 测试营收总额是否等于运费和异常费用之和
     */
    @Test
    public void amountContrast() {

    }



}
