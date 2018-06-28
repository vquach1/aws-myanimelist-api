package utils;

import java.util.Calendar;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MiscUtils {
    private HashMap<String, String> monthMap;

    private String malMonthDayYearDateRegex = "([a-zA-Z]+) (\\d+), (\\d{4})";
    private String malYearDateRegex = "(\\d{4})";
    private String malTimeDurationRegex = "((\\d+) hr. )?(\\d+) min.";

    private Pattern malMonthDayYearDatePattern;
    private Pattern malYearDatePattern;
    private Pattern malTimeDurationPattern;

    public MiscUtils() {
        malMonthDayYearDatePattern = Pattern.compile(malMonthDayYearDateRegex);
        malYearDatePattern = Pattern.compile(malYearDateRegex);
        malTimeDurationPattern = Pattern.compile(malTimeDurationRegex);

        initializeMonthMap();
    }

    private void initializeMonthMap() {
        monthMap = new HashMap<>();

        monthMap.put("Jan", "01");
        monthMap.put("Feb", "02");
        monthMap.put("Mar", "03");
        monthMap.put("Apr", "04");
        monthMap.put("May", "05");
        monthMap.put("Jun", "06");
        monthMap.put("Jul", "07");
        monthMap.put("Aug", "08");
        monthMap.put("Sep", "09");
        monthMap.put("Oct", "10");
        monthMap.put("Nov", "11");
        monthMap.put("Dec", "12");
    }


    // Honestly, I wouldn't be surprised if there were already a library out there
    // that does this stuff. If there is, we can get rid of a lot of code
    public Calendar convertDate(String malDate) {
        if (malDate.equals("?")) {
            return null;
        }

        Calendar date = convertMonthDayYearDate(malDate);
        if (date == null) {
            date = convertYearDate(malDate);
        }

        return date;
    }

    private Calendar convertMonthDayYearDate(String malDate) {
        try {
            Matcher matcher = malMonthDayYearDatePattern.matcher(malDate);
            matcher.find();

            int monthNum = Integer.parseInt(monthMap.get(matcher.group(1)));
            int dayNum = Integer.parseInt(matcher.group(2));
            int yearNum = Integer.parseInt(matcher.group(3));

            Calendar malDateDB = Calendar.getInstance();
            malDateDB.set(yearNum, monthNum, dayNum);
            return malDateDB;
        } catch (IllegalStateException e) {
            return null;
        }
    }

    private Calendar convertYearDate(String malDate) {
        try {
            Matcher matcher = malYearDatePattern.matcher(malDate);
            matcher.find();

            int yearNum = Integer.parseInt(matcher.group(1));

            Calendar malDateDB = Calendar.getInstance();
            malDateDB.set(yearNum, 1, 1);
            return malDateDB;
        } catch (IllegalStateException e) {
            return null;
        }
    }

    public int convertTimeDuration(String malTimeDuration) {
        Matcher matcher = malTimeDurationPattern.matcher(malTimeDuration);
        matcher.find();

        if (matcher.groupCount() != 3) {
            return 0;
        }

        int hours = matcher.group(2) == null ? 0 : Integer.valueOf(matcher.group(2));
        int minutes = matcher.group(3) == null ? 0 : Integer.valueOf(matcher.group(3));

        return hours * 60 + minutes;
    }
}
