package com.carsonlius.module.system.controller.admin.oauth2;

import cn.hutool.core.util.ObjectUtil;
import com.carsonlius.framework.common.enums.UserTypeEnum;
import com.carsonlius.framework.common.exception.enums.GlobalErrorCodeConstants;
import com.carsonlius.framework.common.exception.util.ServiceExceptionUtil;
import com.carsonlius.framework.common.pojo.CommonResult;
import com.carsonlius.framework.common.util.collection.CollectionUtils;
import com.carsonlius.framework.common.util.json.JsonUtils;
import com.carsonlius.framework.security.core.util.SecurityFrameworkUtils;
import com.carsonlius.module.system.controller.admin.oauth2.vo.open.OAuth2OpenAuthorizeInfoRespVO;
import com.carsonlius.module.system.convert.oauth2.OAuth2OpenConvert;
import com.carsonlius.module.system.dal.dataobject.oauth2.OAuth2ApproveDO;
import com.carsonlius.module.system.dal.dataobject.oauth2.OAuth2ClientDO;
import com.carsonlius.module.system.enums.oauth2.OAuth2GrantTypeEnum;
import com.carsonlius.module.system.service.oauth2.OAuth2ApproveService;
import com.carsonlius.module.system.service.oauth2.OAuth2ClientService;
import com.carsonlius.module.system.service.oauth2.OAuth2GrantService;
import com.carsonlius.module.system.util.OAuth2Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @version V1.0
 * @author: carsonlius
 * @date: 2023/12/27 16:01
 * @company
 * @description 提供给客户端的oauth2认证
 */
@RestController
@RequestMapping("/system/oauth2")
@Api(tags = "管理后台 - oauth2.0认证")
public class OAuth2OpenController {

    @Autowired
    private OAuth2ClientService oAuth2ClientService;

    @Autowired
    private OAuth2ApproveService oAuth2ApproveService;

    @Autowired
    private OAuth2GrantService oAuth2GrantService;

    /**
     * 对应 Spring Security OAuth 的 AuthorizationEndpoint 类的 authorize 方法
     * 获取认证信息, 比如: client名称,图标, scope列表
     */
    @GetMapping("/authorize")
    @ApiOperation(value = "获得授权信息", notes = "适合 code 授权码模式，或者 implicit 简化模式")
    @ApiImplicitParam(name = "clientId", value = "客户端ID", required = true, example = "yudao-sso-demo-by-code")
    public CommonResult<OAuth2OpenAuthorizeInfoRespVO> authorize(@RequestParam("clientId") String clientId) {
        // 校验用户已登录, spring security已实现这个要求

        // 获取client信息
        OAuth2ClientDO clientDO = oAuth2ClientService.validOAuthClient(clientId);

        //  获取用户历史授权时 选定的权限范围
        List<OAuth2ApproveDO> approves = oAuth2ApproveService.getApproveList(SecurityFrameworkUtils.getLoginUserId(), getUserType(), clientId);

        // 拼接返回
        return CommonResult.success(OAuth2OpenConvert.INSTANCE.convert(clientDO, approves));
    }


    /**
     * 对应spring security oauth的AuthorizationEndpoint 类的 approveOrDeny 方法
     * 场景一:  [自动授权 autoApprove=true]
     * 刚进入 sso.vue 界面，调用该接口，用户历史已经给该应用做过对应的授权，或者 OAuth2Client 支持该 scope 的自动授权
     * 场景二: [手动授权 autoApprove=true]
     * 在 sso.vue 界面，用户选择好 scope 授权范围，调用该接口，进行授权。此时，approved 为 true 或者 false
     * 因为前后端分离，Axios 无法很好的处理 302 重定向，所以和 Spring Security OAuth 略有不同，返回结果是重定向的 URL，剩余交给前端处理
     */
//    @PostMapping("/authorize")
//    @Operation(summary = "申请授权", description = "适合 code 授权码模式，或者 implicit 简化模式；在 sso.vue 单点登录界面被【提交】调用")
//    @Parameters({
//            @Parameter(name = "response_type", required = true, description = "响应类型", example = "code"),
//            @Parameter(name = "client_id", required = true, description = "客户端编号", example = "tudou"),
//            @Parameter(name = "scope", description = "授权范围", example = "userinfo.read"), // 使用 Map<String, Boolean> 格式，Spring MVC 暂时不支持这么接收参数
//            @Parameter(name = "redirect_uri", required = true, description = "重定向 URI", example = "https://www.iocoder.cn"),
//            @Parameter(name = "auto_approve", required = true, description = "用户是否接受", example = "true"),
//            @Parameter(name = "state", example = "1")
//    })
//    @OperateLog(enable = false) // 避免 Post 请求被记录操作日志
    @PostMapping("/authorize")
    @ApiOperation(value = "申请授权", notes = "适合 code 授权码模式，或者 implicit 简化模式；在 sso.vue 单点登录界面被【提交】调用")
    @ApiImplicitParams({
            @ApiImplicitParam(name="response_type", required=true, value="响应类型", example = "code"),
            @ApiImplicitParam(name="client_id", required=true, value="客户端编号", example = "yudao-sso-demo-by-code"),
            @ApiImplicitParam(name="scope", required=false, value="授权范围", example = "{\"user.read\":true,\"user.write\":false}"),
            @ApiImplicitParam(name="redirect_uri", required=true, value="重定向 URI", example = "http://localhost:18080"),
            @ApiImplicitParam(name="auto_approve", required=true, value="是否自动授权", example = "false"),
            @ApiImplicitParam(name="state", required=true, value="自定义标识,会远洋返回", example = "clientID1"),
    })
    public CommonResult<String> approveOrDeny(@RequestParam("response_type") String responseType,
                                              @RequestParam("client_id") String clientId,
                                              @RequestParam(value = "scope", required = false) String scope,
                                              @RequestParam(value = "redirect_uri") String redirectUri,
                                              @RequestParam("auto_approve") Boolean autoApprove,
                                              @RequestParam(value = "state", required = true) String state) {
        Map<String, Boolean> scopes = JsonUtils.parseObject(scope, Map.class);
        scopes = ObjectUtil.defaultIfNull(scopes, Collections.emptyMap());

        //  1.0 校验用户已登录, spring security已完成这一步

        // 1.1 校验响应responseType是否是code
        OAuth2GrantTypeEnum grantTypeEnum = getGrantTypeEnum(responseType);

        // 1.2 校验redirectUri重定向域名是否合法 + 校验scope 是否在client授权范围内
        OAuth2ClientDO client = oAuth2ClientService.validOAuthClient(clientId, null, grantTypeEnum.getGrantType(), scopes.keySet(), redirectUri);

        // todo 2.0 场景1(自动授权) 自动授权范围是否合法,如果不合法返回null,不跳转
        if (Boolean.TRUE.equals(autoApprove)) {


        } else {
            //  2.1 场景2, 计算授权范围是否合法， 不合法则跳转到一个错误链接
            if (!oAuth2ApproveService.updateAfterApproval(SecurityFrameworkUtils.getLoginUserId(), getUserType(), client.getClientId(), scopes)) {
                return CommonResult.success(OAuth2Utils.buildUnsuccessfulRedirect(redirectUri, responseType, state, "access_denied", "User denied access"));
            }
        }

        // 发放code 并重定向
        List<String> approveScopes = CollectionUtils.convertList(scopes.entrySet(), Map.Entry::getKey, Map.Entry::getValue);

        return CommonResult.success(getAuthorizationCodeRedirect(SecurityFrameworkUtils.getLoginUserId(), client, approveScopes, redirectUri, state));
    }

    /**
     * 下发code码
     */
    private String getAuthorizationCodeRedirect(Long userId, OAuth2ClientDO client, List<String> scopes, String redirectUri, String state) {
        // 1. 创建code授权码
        String code = oAuth2GrantService.grantAuthorizationCodeForCode(userId, getUserType(), client.getClientId(), scopes, redirectUri, state);

        // 2. 拼接重定向的url
        return OAuth2Utils.buildAuthorizationCodeRedirectUri(redirectUri, code, state);
    }

    private OAuth2GrantTypeEnum getGrantTypeEnum(String responseType) {
        if ("code".equals(responseType)) {
            return OAuth2GrantTypeEnum.AUTHORIZATION_CODE;
        }

        throw ServiceExceptionUtil.exception(GlobalErrorCodeConstants.BAD_REQUEST, "response_type 参数值只允许 code");
    }

    private Integer getUserType() {
        return UserTypeEnum.ADMIN.getValue();
    }
}
