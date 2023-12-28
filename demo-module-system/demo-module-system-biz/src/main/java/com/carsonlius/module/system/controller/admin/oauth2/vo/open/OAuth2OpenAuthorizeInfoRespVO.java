package com.carsonlius.module.system.controller.admin.oauth2.vo.open;

import com.carsonlius.framework.common.core.KeyValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * @version V1.0
 * @author: carsonlius
 * @date: 2023/12/27 16:15
 * @company
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OAuth2OpenAuthorizeInfoRespVO {
    /**
     * 客户端
     */
    private Client client;

    @Schema(description = "scope 的选中信息,使用 List 保证有序性，Key 是 scope，Value 为是否选中", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<KeyValue<String, Boolean>> scopes;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Client {

        @Schema(description = "应用名", requiredMode = Schema.RequiredMode.REQUIRED, example = "土豆")
        private String name;

        @Schema(description = "应用图标", requiredMode = Schema.RequiredMode.REQUIRED, example = "https://www.iocoder.cn/xx.png")
        private String logo;

    }
}
