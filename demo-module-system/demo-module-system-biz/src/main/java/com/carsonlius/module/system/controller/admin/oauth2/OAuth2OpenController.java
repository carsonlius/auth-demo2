package com.carsonlius.module.system.controller.admin.oauth2;

import com.carsonlius.framework.common.enums.UserTypeEnum;
import com.carsonlius.framework.common.pojo.CommonResult;
import com.carsonlius.framework.security.core.util.SecurityFrameworkUtils;
import com.carsonlius.module.system.controller.admin.oauth2.vo.open.OAuth2OpenAuthorizeInfoRespVO;
import com.carsonlius.module.system.convert.oauth2.OAuth2OpenConvert;
import com.carsonlius.module.system.dal.dataobject.oauth2.OAuth2ApproveDO;
import com.carsonlius.module.system.dal.dataobject.oauth2.OAuth2ClientDO;
import com.carsonlius.module.system.service.oauth2.OAuth2ApproveService;
import com.carsonlius.module.system.service.oauth2.OAuth2ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @version V1.0
 * @author: carsonlius
 * @date: 2023/12/27 16:01
 * @company
 * @description 提供给客户端的oauth2认证
 */
@RequestMapping("/system/oauth2")
public class OAuth2OpenController {

    @Autowired
    private OAuth2ClientService oAuth2ClientService;

    @Autowired
    private OAuth2ApproveService oAuth2ApproveService;

    /**
     * 对应 Spring Security OAuth 的 AuthorizationEndpoint 类的 authorize 方法
     * 获取认证信息, 比如: client名称,图标, scope列表
     */
    @GetMapping("/authorize")
    public CommonResult<OAuth2OpenAuthorizeInfoRespVO> authorize(@RequestParam("clientId") String clientId) {
        // 校验用户已登录, spring security已实现这个要求

        // 获取client信息
        OAuth2ClientDO clientDO = oAuth2ClientService.validOAuthClient(clientId);

        //  获取用户历史授权时 选定的权限范围
        List<OAuth2ApproveDO> approves = oAuth2ApproveService.getApproveList(SecurityFrameworkUtils.getLoginUserId(), getUserType(), clientId);

        // 拼接返回
        return CommonResult.success(OAuth2OpenConvert.INSTANCE.convert(clientDO, approves));
    }

    private Integer getUserType() {
        return UserTypeEnum.ADMIN.getValue();
    }
}
