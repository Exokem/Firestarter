package xkv.util;

import java.util.List;

public class Time
{
    public static final double YEAR = 3.1536e10, DAY = 8.64e7, HOUR = 3.6e6, MINUTE = 6e4, SECOND = 1000;

    public static final List<Double> SCALES = List.of(YEAR, DAY, HOUR, MINUTE, SECOND);

    public static final List<String> SC_FUL = List.of("year", "day", "hour", "minute", "second");
    public static final List<String> SC_SRT = List.of("y", "d", "h", "m", "s");

    public static String longToString(long millis, boolean longForm)
    {
        StringBuilder dataBuilder = new StringBuilder();

        List<String> units = longForm ? SC_FUL : SC_SRT;

        for (int indx = 0; indx < SCALES.size(); indx++)
        {
            double scale = SCALES.get(indx);

            long value = (long) (millis / scale);

            millis -= value * scale;

            if (0 < value)
            {
                String unit = units.get(indx);

                if (1 < value && longForm)
                {
                    unit += "s";
                }

                String format = longForm ? "%s %s" : "%s%s";

                dataBuilder.append(String.format(format, value, unit));

                if (indx != SCALES.size() - 1)
                {
                    dataBuilder.append(" ");
                }
            }

        }

        return dataBuilder.toString();
    }
}
