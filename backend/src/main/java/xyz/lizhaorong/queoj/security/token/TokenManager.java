package xyz.lizhaorong.queoj.security.token;


import xyz.lizhaorong.queoj.security.token.entity.CheckResult;
import xyz.lizhaorong.queoj.security.token.entity.SimpleUser;
import xyz.lizhaorong.queoj.security.token.entity.TokenObject;
import xyz.lizhaorong.queoj.support.ErrorCode;

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
