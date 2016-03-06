package email.com.gmail.ttsai0509.cruxer.util;

import java.util.Calendar;
import java.util.Date;

/**
 * Another java.util.Date utility class. Mostly to wrap
 * magic numbers into a type-safe enum.
 *
 */
public class DateUtils {

    public static enum Field {
        SECOND(Calendar.SECOND),
        MINUTE(Calendar.MINUTE),
        HOUR(Calendar.HOUR),
        DAY(Calendar.DATE),
        WEEK(Calendar.WEEK_OF_YEAR),
        MONTH(Calendar.MONTH),
        YEAR(Calendar.YEAR);

        private final int magic;

        Field(int magic) {
            this.magic = magic;
        }
    }

    public static Date addNow(Field field, int amount) {
        Calendar c = Calendar.getInstance();
        c.add(field.magic, amount);
        return c.getTime();
    }

    public static Date add(Date date, Field field, int amount) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(field.magic, amount);
        return c.getTime();
    }

}
