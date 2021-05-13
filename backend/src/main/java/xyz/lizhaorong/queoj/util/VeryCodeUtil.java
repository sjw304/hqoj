package xyz.lizhaorong.queoj.util;

import java.util.Random;

/**
 * @author que
 * @version 1.0
 * @date 2021/3/18 22:38
 */
public class VeryCodeUtil {

    private static final Random random = new Random();

    public static String generateCode(int length){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            stringBuilder.append(random.nextInt(10));
        }
        return stringBuilder.toString();
    }

}
