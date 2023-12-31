package com.carsonlius.framework.mybatis.core.dataobject;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @version V1.0
 * @author: carsonlius
 * @date: 2023/12/20 15:18
 * @company
 * @description
 */
@Data
@Accessors(chain = true)
public class BaseTenantDO extends BaseDO {
    /**
     * 多租户编号
     */
    private Long tenantId;
}
