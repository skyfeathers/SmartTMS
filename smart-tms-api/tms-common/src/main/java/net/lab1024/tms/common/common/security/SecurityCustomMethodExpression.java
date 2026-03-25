package net.lab1024.tms.common.common.security;

import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

/**
 * @author yandy
 * @description:
 * @date 2022/5/16 9:39 上午
 */
public class SecurityCustomMethodExpression  extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {
    private Object filterObject;
    private Object returnObject;
    private Object target;

    SecurityCustomMethodExpression(Authentication a) {
        super(a);
    }


    @Override
    public void setFilterObject(Object filterObject) {
        this.filterObject = filterObject;
    }
    @Override
    public Object getFilterObject() {
        return this.filterObject;
    }
    @Override
    public void setReturnObject(Object returnObject) {
        this.returnObject = returnObject;
    }
    @Override
    public Object getReturnObject() {
        return this.returnObject;
    }

    void setThis(Object target) {
        this.target = target;
    }
    @Override
    public Object getThis() {
        return this.target;
    }
}