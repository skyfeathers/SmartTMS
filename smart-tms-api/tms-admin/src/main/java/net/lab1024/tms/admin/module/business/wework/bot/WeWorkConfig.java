package net.lab1024.tms.admin.module.business.wework.bot;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author yandy
 * @description:
 * @date 2025/12/26 9:26 上午
 */
@Component
@Data
public class WeWorkConfig {
    // 企业微信机器人配置（替换为你的实际值）
    public static final String CORP_ID = "wwa3756d84edb968b7"; // 企业微信后台「我的企业」获取
    public static final String TOKEN = "VLvyUGYxQdJ7"; // 机器人配置页的Token
    public static final String ENCODING_AES_KEY = "MTE0MzQ0MzkwZTgyNGQ4NThkZmUzNTVjZWE1NjUwNjE"; // 机器人配置页的EncodingAESKey（43位）
}