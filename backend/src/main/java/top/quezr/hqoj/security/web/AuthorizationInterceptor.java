package top.quezr.hqoj.security.web;

import cn.hutool.extra.servlet.ServletUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import top.quezr.hqoj.security.token.TokenManager;
import top.quezr.hqoj.security.token.entity.CheckResult;
import top.quezr.hqoj.support.Response;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Component
public class AuthorizationInterceptor implements HandlerInterceptor {

    @Autowired
    private TokenManager tokenManager;

    ObjectMapper mapper = new ObjectMapper();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        //检查是否需要身份验证
        int atVal = needAuthorization(handler);
        CheckResult result;
        // 需要身份验证
        if (atVal >0) {
            //从header中得到token
            String authorization = request.getHeader("Authorization");

            //解析
            result = tokenManager.checkAuthorization(authorization, atVal, ServletUtil.getClientIP(request));

            //解析无误
            if(result.isSuccess()) {
                request.setAttribute(Authorization.USERID_ATTR,Integer.valueOf(result.getUser().getUserId()));
                return true;
            }

            //是rest接口，返回错误码
            if(isRestInterface(handler)){
                response.setContentType("text/html;charset=utf-8");
                final String s = mapper.writeValueAsString(Response.failure(result.getErrorCode()));
                response.getWriter().write(s);
                return false;
            }
            //不是rest接口，转发到/login
            else {
                response.sendRedirect(request.getContextPath()+"/login");
                return false;
            }
        }

        return true;
    }

    /**
     * 检查是否目标方法需要身份验证
     * @param handler 目标方法
     * @return 身份验证等级，0代表不需要
     */
    private int needAuthorization(Object handler){
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        //如果方法或类注明了@authorization
        Authorization ano = method.getAnnotation(Authorization.class);
        //value以方法上的注解为优先
        ano = ano != null ? ano : method.getDeclaringClass().getAnnotation(Authorization.class);
        return ano==null?0:ano.value();
    }

    /**
     * 检查是否目标方法为rest接口
     * @param handler
     * @return
     */
    private boolean isRestInterface(Object handler){
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        Object ano = method.getAnnotation(ResponseBody.class);
        ano = ano != null?ano:method.getDeclaringClass().getAnnotation(ResponseBody.class);
        ano = ano != null?ano:method.getDeclaringClass().getAnnotation(RestController.class);
        return ano!=null;
    }



}
