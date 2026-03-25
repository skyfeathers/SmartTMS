package net.lab1024.tms.common.common.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import net.lab1024.tms.common.module.support.file.domain.vo.FileVO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 由于前端接收到的是序列化过的字段，
 * 这边入库需要进行反序列化操作比较方便处理
 *
 * @author lemon
 * @version 1.0.0
 * @date 2021年11月24日 17:15:00
 */
public class FileKeyVoDeserializer extends JsonDeserializer<String> {
    
    @Override
    public String deserialize (JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        List<FileVO> list = new ArrayList<>();
        ObjectCodec objectCodec = jsonParser.getCodec();
        JsonNode listOrObjectNode = objectCodec.readTree(jsonParser);
        String deserialize = "";
        try {
            if (listOrObjectNode.isArray()) {
                for (JsonNode node : listOrObjectNode) {
                    list.add(objectCodec.treeToValue(node, FileVO.class));
                }
            } else {
                list.add(objectCodec.treeToValue(listOrObjectNode, FileVO.class));
            }
            deserialize = list.stream().map(FileVO::getFileKey).collect(Collectors.joining(","));
        }catch (Exception e){
            deserialize = listOrObjectNode.asText();
        }
        return deserialize;
    }

    
}
