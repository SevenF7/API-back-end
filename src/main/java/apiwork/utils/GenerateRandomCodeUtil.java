package apiwork.utils;

import org.springframework.stereotype.Service;

import java.util.Random;
@Service
public class GenerateRandomCodeUtil {
    public String getRandomCode() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <6; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}
