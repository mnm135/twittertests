package helper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Datehelper {

    public static String getCurrentDate() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("MMM d, yyyy", Locale.ENGLISH);
        return formatter.format(date);
    }
}
