package net.lab1024.tms.common.module.business.esign.domain.flow;

import lombok.Data;

import java.util.List;

/**
 * 下载已签文件
 *
 * @author lihaifan
 * @date 2022/9/16 15:47
 */
@Data
public class ESignDocumentsVO {

    /**
     * 文档列表数据
     */
    private List<ESignDocumentsItemVO> docs;
}
