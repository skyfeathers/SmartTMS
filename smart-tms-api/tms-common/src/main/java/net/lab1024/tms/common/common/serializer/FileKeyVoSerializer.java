package net.lab1024.tms.common.common.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.google.common.collect.Lists;
import net.lab1024.tms.common.common.constant.StringConst;
import net.lab1024.tms.common.common.util.SmartStringUtil;
import net.lab1024.tms.common.module.support.file.domain.vo.FileVO;
import net.lab1024.tms.common.module.support.file.service.FileService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;

/**
 * [  ]
 *
 * @author 罗伊
 * @date 2020/8/15 15:06
 */
public class FileKeyVoSerializer extends JsonSerializer<String> {

    @Autowired
    private FileService fileService;


    @Override
    public void serialize(String value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (StringUtils.isEmpty(value)) {
            jsonGenerator.writeObject(Lists.newArrayList());
            return;
        }
        if(fileService == null){
            jsonGenerator.writeString(value);
            return;
        }
        List<String> list = SmartStringUtil.split(value, StringConst.SEPARATOR);
        List<FileVO> fileKeyVOList = fileService.getFileList(list);
        jsonGenerator.writeObject(fileKeyVOList);
    }
}
