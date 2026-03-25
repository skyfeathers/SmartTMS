package net.lab1024.tms.common.common.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * [  ]
 *
 * @author 罗伊
 * @date 2020/8/20 16:04
 */
public class BigDecimalNullZeroSerializer extends JsonSerializer<BigDecimal> {

    @Override
    public void serialize(BigDecimal value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (value == null) {
            jsonGenerator.writeNumber(BigDecimal.ZERO);
            return;
        }
        jsonGenerator.writeNumber(value);
    }
}