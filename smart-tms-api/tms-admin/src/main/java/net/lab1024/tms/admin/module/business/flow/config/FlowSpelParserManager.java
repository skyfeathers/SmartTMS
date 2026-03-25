package net.lab1024.tms.admin.module.business.flow.config;

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2021/8/17 16:44
 */
public class FlowSpelParserManager {

    private ExpressionParser expressionParser;
    private static final FlowSpelParserManager INSTANCE = new FlowSpelParserManager();

    private FlowSpelParserManager() {
        this.expressionParser = new SpelExpressionParser();
    }

    public static final FlowSpelParserManager getInstance() {
        return INSTANCE;
    }

    public ExpressionParser getSpelParser() {
        return expressionParser;
    }

}
