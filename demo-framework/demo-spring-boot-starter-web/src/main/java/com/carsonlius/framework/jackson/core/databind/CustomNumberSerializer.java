package com.carsonlius.framework.jackson.core.databind;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

import java.io.IOException;

/**
 * @version V1.0
 * @author: carsonlius
 * @date: 2024/1/7 13:58
 * @company
 * @description 会将超长 long 值转换为 string，解决前端 JavaScript 最大安全整数是 2^53-1 的问题
 */
@JacksonStdImpl
public class CustomNumberSerializer extends NumberSerializer {

    public static final CustomNumberSerializer INSTANCE = new CustomNumberSerializer(Number.class);

    private static final long MAX_SAFE_INTEGER = 9007199254740991L;
    private static final long MIN_SAFE_INTEGER = -9007199254740991L;

    /**
     * @param rawType
     * @since 2.5
     */
    public CustomNumberSerializer(Class<? extends Number> rawType) {
        super(rawType);
    }

    @Override
    public void serialize(Number value, JsonGenerator g, SerializerProvider provider) throws IOException {
        if (value.longValue() > MIN_SAFE_INTEGER &&  value.longValue() < MAX_SAFE_INTEGER) {
            super.serialize(value, g, provider);
        } else {
            // 超出范围 序列化位字符串
            g.writeString(value.toString());
        }
    }
}


