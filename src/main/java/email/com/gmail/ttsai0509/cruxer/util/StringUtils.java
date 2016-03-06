package email.com.gmail.ttsai0509.cruxer.util;


import java.util.Random;

public final class StringUtils {

    private static Random random = new Random();

    private StringUtils() {
        throw new RuntimeException("Ahhh, shit. No!");
    }

    public static final String ALPHA = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    public static final String NUM = "0123456789";
    public static final String SYM = "!@#$%^&*()_+-=[]|;':<>?,./`~";
    public static final String ALL = ALPHA + NUM + SYM;

    public static String randomString(int length, String charset) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++)
            sb.append(charset.charAt(random.nextInt(charset.length())));
        return sb.toString();
    }

}
