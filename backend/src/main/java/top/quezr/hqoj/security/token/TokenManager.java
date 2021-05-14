package top.quezr.hqoj.security.token;


import top.quezr.hqoj.security.token.entity.CheckResult;
import top.quezr.hqoj.security.token.entity.SimpleUser;
import top.quezr.hqoj.security.token.entity.TokenObject;

public interface TokenManager {

    /**
     * 生成token（字符串组）
     */
    TokenObject generate(SimpleUser user);

    /**
     * 通过refreshToken进行刷新
     */
    TokenObject refresh(String refreshToken);

    /**
     * 检查accessToken
     */
    CheckResult checkAuthorization(String accessToken, int role, String addr);

}
