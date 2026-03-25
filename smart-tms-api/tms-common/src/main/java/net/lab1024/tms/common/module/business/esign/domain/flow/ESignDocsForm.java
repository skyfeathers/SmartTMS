package net.lab1024.tms.common.module.business.esign.domain.flow;

import lombok.Data;

/**
 * 待签署文件信息
 *
 * @author lihaifan
 * @date 2022/7/18 18:07
 */
@Data
public class ESignDocsForm {

    /**
     * 待签署文件id
     */
    private String fileId;

    /**
     * 文件名称
     *
     * （签署界面展示的文件名称与下载时的文件名称）
     *
     * （1）名称不传扩展名时，例如传：xx，默认展示的文件名为 xx，下载时自动添加后缀 xx.pdf。
     *
     * （2）文件扩展名仅限传pdf格式，例如传 xx.pdf，展示的文件名即为所传值 xx.pdf，下载文件也为xx.pdf。
     *
     * （3）传入空值，取文件的原始名称，扩展名皆为.pdf。
     *
     * （4）文件名称不支持以下9个字符：
     *
     * / \ : * " < > | ？
     */
    private String fileName;

    /**
     * 文档是否设置编辑密码
     *
     * 0-未设置，1-已设置，默认值为0
     *
     * 注：
     *
     * （1）设置编辑密码的PDF文件需要输入密码才有权限进行盖章操作。
     *
     * （2）encryption填写1时，同时需要向filePassword参数填写编辑密码。
     *
     * （3）支持自动盖章场景。
     *
     * （4）不支持手动盖章场景。
     */
    private Integer encryption;

    /**
     * 文档密码, 如果encryption值为1, 文档密码不能为空，默认没有密码
     */
    private String filePassword;
}
