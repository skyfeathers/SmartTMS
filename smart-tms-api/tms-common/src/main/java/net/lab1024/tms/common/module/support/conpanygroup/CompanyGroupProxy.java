package net.lab1024.tms.common.module.support.conpanygroup;

import cn.hutool.core.bean.BeanUtil;
import org.springframework.cglib.beans.BeanGenerator;

public class CompanyGroupProxy {

    public static Object withCompanyId(Object arg, Long companyId) {
        BeanGenerator beanGenerator = new BeanGenerator();
        beanGenerator.setSuperclass(arg.getClass());
        beanGenerator.addProperty("companyId", Long.class);
        Object proxyObject = beanGenerator.create();
        try {
            BeanUtil.copyProperties(arg, proxyObject);
            proxyObject.getClass().getMethod("setCompanyId", Long.class).invoke(proxyObject, companyId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return proxyObject;
    }

}
