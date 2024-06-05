package apiwork;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;
/**
 * Hello world!
 *
 */
@SpringBootApplication
@MapperScan("apiwork.mapper")
public class APIBackEndApplication
{
    public static void main( String[] args )
    {
        SpringApplication.run(APIBackEndApplication.class,args);
    }
}
