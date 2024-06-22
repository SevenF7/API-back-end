package apiwork;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
/**
 * Hello world!
 *
 */
@SpringBootApplication
@EnableAspectJAutoProxy
@MapperScan("apiwork.mapper")
public class APIBackEndApplication
{
    public static void main( String[] args )
    {
        SpringApplication.run(APIBackEndApplication.class,args);
    }
}
