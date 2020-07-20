package com.beetrack.bitcoin_wallet.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatUtil {

    public static String formatDate(final String date) {

        if (date.contains("Z")) {
            String dateTransform = date.replace("Z", "");

            String patternCurrent = "yyyy-MM-dd'T'HH:mm:ss";
            String patternRequired = "dd/MM/yyyy HH:mm:ss";

            SimpleDateFormat formatterCurrent = new SimpleDateFormat(patternCurrent);
            SimpleDateFormat formatterRequired = new SimpleDateFormat(patternRequired);

            try {

                Date turnedDate = formatterCurrent.parse(dateTransform);

                return formatterRequired.format(turnedDate);

            } catch (ParseException e) {
                e.printStackTrace();

            }
        }
        return date;
    }
}
