package top.quezr.hqoj.security.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.quezr.hqoj.security.token.entity.CheckResult;
import top.quezr.hqoj.security.token.entity.SimpleUser;
import top.quezr.hqoj.security.token.entity.TokenObject;
import top.quezr.hqoj.security.token.entity.TokenErrorCode;

import java.util.*;

@Component("tokenManager")
@Slf4j
public class DefaultTokenManager implements TokenManager {

    public static final int MAX_COUNT = 5;

    /**
     * accessToken 过期时间 60min
     */
    private static final long EXPIRE_TIME = 4*60*60 *1000;

    /**
     * refreshToken 过期时间 90min
     */
    private static final long REFRESH_EXPIRE_TIME= 8 * 90 * 60 *1000;

    /**
     * 加密秘钥，使用它生成token
     */
    private static final String TOKEN_SECRET="4kd2js1kl4mmc5kd4sa4x54e8w/d18as-+asc2DDX2D8gf";

    private static final String REFRESH_TOKEN_SECRET="SOFG,ZX9-S8Q2+X5R4+672FS9MPV;Z.PQ*/91`SA56CZ5@$^%&(@sgjxasfw";


    private SimpleUser analysisToken(String token, boolean flag){
        try{

            Algorithm algorithm ;
            if(flag){
                algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            }else{
                algorithm = Algorithm.HMAC256(REFRESH_TOKEN_SECRET);
            }
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedjwt = verifier.verify(token);
            return new SimpleUser(
                    decodedjwt.getClaim("uid").asString()
                    , decodedjwt.getClaim("addr").asString()
                    ,decodedjwt.getClaim("role").asInt()
                    ,decodedjwt.getClaim("count").asInt()
            );
        }catch (TokenExpiredException e){
            return new SimpleUser(null,null,0,-1);
        }catch (JWTDecodeException e){
            System.out.println("token解析失败");
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public TokenObject generate(SimpleUser user) {
        String acc = generateAToken(user,true);
        String ref = generateAToken(user,false);
        return new TokenObject(acc,ref);
    }

    private String generateAToken(SimpleUser user, boolean isAccess){
        try{
            Date date;
            Algorithm algorithm ;
            if(isAccess){
                date = new Date(System.currentTimeMillis()+EXPIRE_TIME);
                algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            }else{
                date = new Date(System.currentTimeMillis()+REFRESH_EXPIRE_TIME);
                algorithm = Algorithm.HMAC256(REFRESH_TOKEN_SECRET);
            }
            Map<String, Object> header = new HashMap<>();
            header.put("typ","JWT");
            header.put("alg","HS256");

            return JWT.create()
                    .withHeader(header)
                    .withClaim("uid",user.getUserId())
                    .withClaim("role",user.getRole())
                    .withClaim("addr",user.getAddr())
                    .withClaim("count",user.getCount())
                    .withExpiresAt(date)
                    .sign(algorithm);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public TokenObject refresh(String refreshToken) {
        CheckResult result = checkAuthorizationImpl(refreshToken,0,null,false);
        if(!result.isSuccess()){
            log.debug("refresh failed");
            return null;
        }
        SimpleUser user = result.getUser();
        user.setCount(user.getCount()+1);
        if(user.getCount()>MAX_COUNT){
            log.debug("over the max refresh times");
            return null;
        }
        return new TokenObject(generateAToken(user,true),generateAToken(user,false));

    }

    @Override
    public CheckResult checkAuthorization(String accessToken, int role, String addr) {
        return checkAuthorizationImpl(accessToken,role,addr,true);
    }

    private CheckResult checkAuthorizationImpl(String authorization, int role, String addr, boolean isAccess){
        CheckResult result = new CheckResult();
        result.setSuccess(false);

        //token是否为空
        if(authorization==null) {
            result.setErrorCode(TokenErrorCode.DID_NOT_LOGIN);
            return result;
        }

        //获取token解析结果
        SimpleUser user;
        user = analysisToken(authorization,isAccess);

        if(user==null) {
            result.setErrorCode(TokenErrorCode.WRONG_TOKEN);
            return result;
        }

        //令牌需要刷新
        if(user.getCount()==-1) {
            result.setErrorCode(TokenErrorCode.NEED_REFRESH);
            return result;
        }

        //需要重新登录
        if(user.getCount()== DefaultTokenManager.MAX_COUNT) {
            result.setErrorCode(TokenErrorCode.NEED_LOGIN);
            return result;
        }

        if(isAccess){
            //检查地址是否一致
            if(!addr.equals(user.getAddr())) {
                result.setErrorCode(TokenErrorCode.WRONG_ADDR);
                return result;
            }

            //检查接口权限
            if (user.getRole()<role) {
                result.setErrorCode(TokenErrorCode.INSUFFICIENT_AUTHORITY);
                return result;
            }
        }

        result.setSuccess(true);
        result.setUser(user);
        return result;
    }
}
