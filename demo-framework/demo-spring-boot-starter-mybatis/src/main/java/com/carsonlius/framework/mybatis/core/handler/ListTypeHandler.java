package com.carsonlius.framework.mybatis.core.handler;

import cn.hutool.core.util.StrUtil;
import com.carsonlius.framework.common.util.json.JsonUtils;
import org.apache.ibatis.type.*;
import org.springframework.util.CollectionUtils;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @version V1.0
 * @author: carsonlius
 * @date: 2024/1/8 11:25
 * @company
 * @description List泛型处理器
 */

public abstract class ListTypeHandler<T> extends BaseTypeHandler<List<T>> {
    /**
     * 具体类型,由子类实现
     * @return 具体类型
     * */
    protected abstract Class<T> getType();

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<T> parameter, JdbcType jdbcType) throws SQLException {
        String context = CollectionUtils.isEmpty(parameter) ? null : JsonUtils.toJsonString(parameter);
        ps.setString(i, context);
    }

    @Override
    public List<T> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return getResult(rs.getString( columnName));
    }

    @Override
    public List<T> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return getResult(rs.getString( columnIndex));
    }

    @Override
    public List<T> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return getResult(cs.getString( columnIndex));
    }

    /**
     * 根据json字符串格式化成List
     * */
    private List<T> getResult(String context) {
        return StrUtil.isBlank(context) ? new ArrayList<>() : JsonUtils.parseArray(context, getType());
    }
}
