package com.carsonlius.module.system.controller.admin.oauth2;

import cn.hutool.core.util.ObjectUtil;
import com.carsonlius.framework.common.enums.UserTypeEnum;
import com.carsonlius.framework.common.exception.enums.GlobalErrorCodeConstants;
import com.carsonlius.framework.common.exception.util.ServiceExceptionUtil;
import com.carsonlius.framework.common.pojo.CommonResult;
import com.carsonlius.framework.common.util.collection.CollectionUtils;
import com.carsonlius.framework.common.util.http.HttpUtils;
import com.carsonlius.framework.common.util.json.JsonUtils;
import com.carsonlius.framework.security.core.util.SecurityFrameworkUtils;
import com.carsonlius.module.system.controller.admin.oauth2.vo.open.OAuth2OpenAccessTokenRespVO;
import com.carsonlius.module.system.controller.admin.oauth2.vo.open.OAuth2OpenAuthorizeInfoRespVO;
import com.carsonlius.module.system.convert.oauth2.OAuth2OpenConvert;
import com.carsonlius.module.system.dal.dataobject.oauth2.OAuth2AccessTokenDO;
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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
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
     * 对应 Spring Security OAuth 的 TokenEndpoint 类的 postAccessToken 方法
     * 授权码 authorization_code 模式: code redirectUri + state
     * 密码 password 模式: username + password + scope参数
     * 刷新 refresh_token模式时： refreshToken参数
     * 客户端 client_credentials 模式：scope 参数 不支持
     * 简化 implicit 模式时：不支持
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "grant_type", value = "授权类型", required = true, example = "authorization_code"),
            @ApiImplicitParam(name = "code", value = "授权码认证第一步获取的code", required = false, example = "1fa0225b70a94f43a5977dcc472787f0"),
            @ApiImplicitParam(name = "redirect_uri", value = "重定向URI", required = false, example = "http://127.0.0.1:18080/callback.html"),
            @ApiImplicitParam(name = "state", value = "状态", required = false, example = ""),
            @ApiImplicitParam(name = "username", value = "用户名", required = false, example = ""),
            @ApiImplicitParam(name = "password", value = "密码", required = false, example = ""),
            @ApiImplicitParam(name = "scope", value = "授权范围", required = false, example = "user.read user.write"),
            @ApiImplicitParam(name = "refresh_token", value = "刷新token", required = false, example = "123424233"),
    })
    @PostMapping("token")
    @PermitAll
    @ApiOperation(value = "获得访问令牌", notes = "适合 code 授权码模式，或者 implicit 简化模式；在 sso.vue 单点登录界面被【获取】调用" +
            "1.授权码 authorization_code 模式: code redirectUri + state 2.密码 password 模式: username + password + scope参数" +
            "3.刷新 refresh_token模式时： refreshToken参数  4. 客户端 client_credentials 模式：scope 参数 不支持 5. 简化 implicit 模式时：不支持 默认在header中传递clientId和clientSecret")
    public CommonResult<OAuth2OpenAccessTokenRespVO> postAccessToken(HttpServletRequest request,
                                                                     @RequestParam("grant_type") String grantType,
                                                                     @RequestParam("code") String code,
                                                                     @RequestParam(value = "redirect_uri", required = false) String redirectUri,
                                                                     @RequestParam(value = "state", required = false) String state,
                                                                     @RequestParam(value = "username", required = false) String username,
                                                                     @RequestParam(value = "password", required = false) String password,
                                                                     @RequestParam(value = "scope", required = false) String scope,
                                                                     @RequestParam(value = "refresh_token", required = false) String refreshToken) {

        List<String> scopes = OAuth2Utils.buildScopes(scope);

        // 1.0 校验授权类型
        OAuth2GrantTypeEnum grantTypeEnum = OAuth2GrantTypeEnum.getByGranType(grantType);
        if (grantTypeEnum == null) {
            throw ServiceExceptionUtil.exception(GlobalErrorCodeConstants.BAD_REQUEST, "未知授权类型");
        }

        if (!(grantTypeEnum == OAuth2GrantTypeEnum.AUTHORIZATION_CODE || grantTypeEnum == OAuth2GrantTypeEnum.REFRESH_TOKEN)) {
            throw ServiceExceptionUtil.exception(GlobalErrorCodeConstants.BAD_REQUEST, "只支持authorization_code模式");
        }

        // 1.1 校验客户端
        List<String> clientIdAndSecret = obtainBasicAuthorization(request);
        OAuth2ClientDO client = oAuth2ClientService.validOAuthClient(clientIdAndSecret.get(0), clientIdAndSecret.get(1), grantType, scopes, redirectUri);
        OAuth2AccessTokenDO accessTokenDo = null;
        switch (grantTypeEnum) {
            case AUTHORIZATION_CODE:
                accessTokenDo = oAuth2GrantService.grantAuthorizationCodeForAccessToken(client.getClientId(), code, redirectUri, state);
                break;
            case REFRESH_TOKEN:
                accessTokenDo = oAuth2GrantService.grantRefreshToken(refreshToken, client.getClientId());
                break;
            default:
                throw ServiceExceptionUtil.exception(GlobalErrorCodeConstants.BAD_REQUEST, "不支持的授权模式");

        }


        // 1.2 根据授权模式 获取访问令牌
        return CommonResult.success(OAuth2OpenConvert.INSTANCE.convert(accessTokenDo));
    }


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
            @ApiImplicitParam(name = "response_type", required = true, value = "响应类型", example = "code"),
            @ApiImplicitParam(name = "client_id", required = true, value = "客户端编号", example = "yudao-sso-demo-by-code"),
            @ApiImplicitParam(name = "scope", required = false, value = "授权范围", example = "{\"user.read\":true,\"user.write\":false}"),
            @ApiImplicitParam(name = "redirect_uri", required = true, value = "重定向 URI", example = "http://127.0.0.1:18080/callback.html"),
            @ApiImplicitParam(name = "auto_approve", required = true, value = "是否自动授权", example = "false"),
            @ApiImplicitParam(name = "state", required = true, value = "自定义标识,会远洋返回", example = "clientID1"),
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

    @DeleteMapping("/token")
    @ApiOperation(value = "删除访问令牌")
    @PermitAll
    @ApiImplicitParams({@ApiImplicitParam(name = "token", required = true, value = "")})
    public CommonResult<Boolean> revokeToken(HttpServletRequest request, @RequestParam(value = "token") String token) {
        // 校验客户端
        List<String> clientIdAndSecret = obtainBasicAuthorization(request);
        OAuth2ClientDO clientDO = oAuth2ClientService.validOAuthClient(clientIdAndSecret.get(0), clientIdAndSecret.get(1), null, null, null);

        return CommonResult.success(oAuth2GrantService.revokeToken(clientDO.getClientId(), token));
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

    /**
     * 从header中获取clientId和secret
     */
    private List<String> obtainBasicAuthorization(HttpServletRequest request) {
        List<String> clientIdAndSecret = HttpUtils.obtainBasicAuthorization(request);
        if (clientIdAndSecret == null) {
            throw ServiceExceptionUtil.exception(GlobalErrorCodeConstants.BAD_REQUEST, "client_id 或 client_secret 未正确传递");
        }
        return clientIdAndSecret;
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
