package com.carsonlius.framework.mybatis.core.handler;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.util.List;

/**
 * @version V1.0
 * @author: carsonlius
 * @date: 2024/1/8 13:57
 * @company
 * @description List<String>和Json之间的转化
 */
@MappedTypes(List.class)
@MappedJdbcTypes(JdbcType.VARCHAR)
public class StringListTypeHandler extends ListTypeHandler<String> {
    @Override
    protected Class<String> getType() {
        return String.class;
    }
}
