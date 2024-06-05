package apiwork;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.ibatis.annotations.Mapper;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtTest {
    @Test
    public void testGen(){
        Map<String,Object> claims=new HashMap<>();
        claims.put("id",1);
        claims.put("username","张三");
        //生成jwt的代码
        String tocken = JWT.create()
                .withClaim("user",claims)
                .withExpiresAt(new Date(System.currentTimeMillis()+1000*60*60*12))
                .sign(Algorithm.HMAC256("itheima"));
        System.out.println(tocken);

    }

    @Test
    public void testParse(){
        String tocken="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9" +
                ".eyJ1c2VyIjp7ImlkIjoxLCJ1c2VybmFtZSI6IuW8oOS4iSJ9LCJleHAiOjE3MTAxOTY5ODN9" +
                ".MCyLWscKhBJDZTBT2i36JVAD6ugOgLRk9zAn68kGwgQ";
        JWTVerifier jwtVerifier =JWT.require(Algorithm.HMAC256("itheima")).build();

        DecodedJWT decodedJWT =jwtVerifier.verify(tocken);
        Map<String, Claim> claims=decodedJWT.getClaims();
        System.out.println(claims.get("user"));
    }
}
//