package net.lab1024.tms.common.module.business.esign.domain.file;

import lombok.Data;

/**
 * 获取文件上传地址
 *
 * @author lihaifan
 * @date 2022/7/16 15:24
 */
@Data
public class ESignFileUploadForm {

    /**
     * 先获取文件MD5的二进制数组（128位），再对这个二进制进行base64编码。
     */
    private String contentMd5;

    /**
     * 目标文件的MIME类型，支持：
     *
     * （1）application/octet-stream
     *
     * （2）application/pdf
     *
     * 注意，后面文件流上传的Content-Type参数要和这里一致，不然就会有403的报错
     */
    private String contentType;

    /**
     * 文件名称（必须带上文件扩展名，不然会导致后续发起流程校验过不去 示例：合同.pdf ）；
     *
     * 注意：
     *
     * （1）该字段的文件后缀名称和真实的文件后缀需要一致。比如上传的文件类型是word文件，那该参数需要传“xxx.docx”，不能是“xxx.pdf”
     *
     * （2）该字段建议直接传入pdf文件，其他类型文件建议本地自行转换成pdf，避免通过接口格式转换引起的格式错误、耗时久等问题。
     *
     * （3）文件名称不支持以下9个字符：/ \ : * " < > | ？
     */
    private String fileName;

    /**
     * 文件大小，单位byte
     */
    private long fileSize;

    /**
     * 是否转换成pdf文档，默认false，代表不做转换。转换是异步行为，如果指定要转换，需要调用查询文件信息接口查询状态，转换完成后才可使用。
     *
     * 注意：如果本身就是PDF文件，该参数必须是false，可以直接默认不传。
     */
    private boolean convert2Pdf;
}
