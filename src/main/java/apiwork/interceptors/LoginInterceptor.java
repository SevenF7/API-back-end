package apiwork.interceptors;

import apiwork.utils.JwtUtil;
import apiwork.utils.ThreadLocalUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    //    //Redis相关内容，为了方便测试，最后一次版本取消注释即可
//    @Autowired
//    private StringRedisTemplate stringRedisTemplate;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //令牌验证
        String tocken = request.getHeader("Authorization");
        //验证tocken
        //Redis相关内容，为了方便测试，最后一次版本取消注释即可
        try {
//            //Redis相关内容，为了方便测试，最后一次版本取消注释即可
//            ValueOperations<String,String>operations=stringRedisTemplate.opsForValue();
//            String redisToken =operations.get(tocken);
//            if (redisToken==null){
//                throw  new RuntimeException();
//            }
            Map<String, Object> claims = JwtUtil.parseToken(tocken);
            ThreadLocalUtil.set(claims);
            return true;
        } catch (Exception e) {
            response.setStatus(401);
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        ThreadLocalUtil.remove();
    }
}
