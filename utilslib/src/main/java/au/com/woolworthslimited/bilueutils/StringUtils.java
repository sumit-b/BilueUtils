package au.com.woolworthslimited.bilueutils;

import android.text.TextUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.security.InvalidParameterException;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Billy Chang on 4/06/15.
 */
public class StringUtils {
    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

    /**
     * Originally from RoboGuice:
     * https://github.com/roboguice/roboguice/blob/master/roboguice/src/main/java/roboguice/util/Strings.java
     * Like join, but allows for a distinct final delimiter.  For english sentences such
     * as "Alice, Bob and Charlie" use ", " and " and " as the delimiters.
     *
     * @param delimiter     usually ", "
     * @param lastDelimiter usually " and "
     * @param objs          the objects
     * @param <T>           the type
     * @return a string
     */
    public static <T> String joinAnd(final String delimiter, final String lastDelimiter,
                                     final Collection<T> objs) {
        if (objs == null || objs.isEmpty())
            return "";

        final Iterator<T> iter = objs.iterator();
        final StringBuilder buffer = new StringBuilder(StringUtils.toString(iter.next()));
        int i = 1;
        while (iter.hasNext()) {
            final T obj = iter.next();
            if (notEmpty(obj))
                buffer.append(++i == objs.size() ? lastDelimiter : delimiter).
                        append(StringUtils.toString(obj));
        }
        return buffer.toString();
    }

    public static <T> String joinAnd(final String delimiter, final String lastDelimiter,
                                     final T... objs) {
        return joinAnd(delimiter, lastDelimiter, Arrays.asList(objs));
    }

    public static <T> String join(final String delimiter, final Collection<T> objs) {
        if (objs == null || objs.isEmpty())
            return "";

        final Iterator<T> iter = objs.iterator();
        final StringBuilder buffer = new StringBuilder(StringUtils.toString(iter.next()));

        while (iter.hasNext()) {
            final T obj = iter.next();
            if (notEmpty(obj)) buffer.append(delimiter).append(StringUtils.toString(obj));
        }
        return buffer.toString();
    }

    public static <T> String join(final String delimiter, final T... objects) {
        return join(delimiter, Arrays.asList(objects));
    }

    public static String toString(InputStream input) {
        StringWriter sw = new StringWriter();
        copy(new InputStreamReader(input), sw);
        return sw.toString();
    }

    public static String toString(Reader input) {
        StringWriter sw = new StringWriter();
        copy(input, sw);
        return sw.toString();
    }

    public static int copy(Reader input, Writer output) {
        long count = copyLarge(input, output);
        return count > Integer.MAX_VALUE ? -1 : (int) count;
    }

    public static long copyLarge(Reader input, Writer output) throws RuntimeException {
        try {
            char[] buffer = new char[DEFAULT_BUFFER_SIZE];
            long count = 0;
            int n;
            while (-1 != (n = input.read(buffer))) {
                output.write(buffer, 0, n);
                count += n;
            }
            return count;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String toString(final Object o) {
        return toString(o, "");
    }

    public static String toString(final Object o, final String def) {
        return o == null ? def :
                o instanceof InputStream ? toString((InputStream) o) :
                        o instanceof Reader ? toString((Reader) o) :
                                o instanceof Object[] ? StringUtils.join(", ", (Object[]) o) :
                                        o instanceof Collection ? StringUtils.join(", ", (Collection<?>) o) : o.toString();
    }

    public static boolean isEmpty(final Object o) {
        return toString(o).trim().length() == 0;
    }

    public static boolean notEmpty(final Object o) {
        return toString(o).trim().length() != 0;
    }

    public static String md5(String s) {
        // http://stackoverflow.com/questions/1057041/difference-between-java-and-php5-md5-hash
        // http://code.google.com/p/roboguice/issues/detail?id=89
        try {

            final byte[] hash = MessageDigest.getInstance("MD5").digest(s.getBytes("UTF-8"));
            final StringBuilder hashString = new StringBuilder();

            for (byte aHash : hash) {
                String hex = Integer.toHexString(aHash);

                if (hex.length() == 1) {
                    hashString.append('0');
                    hashString.append(hex.charAt(hex.length() - 1));
                } else {
                    hashString.append(hex.substring(hex.length() - 2));
                }
            }

            return hashString.toString();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String capitalize(String s) {
        final String c = StringUtils.toString(s);
        return c.length() >= 2 ? c.substring(0, 1).toUpperCase() + c.substring(1) :
                c.length() >= 1 ? c.toUpperCase() : c;
    }

    public static boolean equals(Object a, Object b) {
        return StringUtils.toString(a).equals(StringUtils.toString(b));
    }

    public static boolean equalsIgnoreCase(Object a, Object b) {
        return StringUtils.toString(a).toLowerCase().equals(StringUtils.toString(b).toLowerCase());
    }

    public static String[] chunk(String str, int chunkSize) {
        if (isEmpty(str) || chunkSize == 0)
            return new String[0];

        final int len = str.length();
        final int arrayLen = ((len - 1) / chunkSize) + 1;
        final String[] array = new String[arrayLen];
        for (int i = 0; i < arrayLen; ++i)
            array[i] = str.substring(i * chunkSize, (i * chunkSize) + chunkSize < len
                    ? (i * chunkSize) + chunkSize : len);

        return array;
    }

    public static String flatChunk(String str, int chunkSize, int dontChunkUnlessSize) {
        if (str.length() < dontChunkUnlessSize) {
            return str;
        }
        return join(" ", Arrays.asList(chunk(str, chunkSize)));
    }

    public static String namedFormat(String str, Map<String, String> substitutions) {
        for (String key : substitutions.keySet())
            str = str.replace('$' + key, substitutions.get(key));

        return str;
    }

    public static String namedFormat(String str, Object... nameValuePairs) {
        if (nameValuePairs.length % 2 != 0)
            throw new InvalidParameterException("You must include one value for each parameter");

        final HashMap<String, String> map = new HashMap<String, String>(nameValuePairs.length / 2);
        for (int i = 0; i < nameValuePairs.length; i += 2)
            map.put(StringUtils.toString(nameValuePairs[i]), StringUtils.toString(nameValuePairs[i + 1]));

        return namedFormat(str, map);
    }

    private static final int MAX_NUM_DIGIT = 7;
    private static final int MAX_NUM_DECIMAL_DIGIT = 2;

    public static String currencyFormat(String str) {
        String intPart;
        String decimalPart;

        // 3/2/2015 crash fix
        //in case just '.' is put, treat it as '0.'
        if (".".equals(str)) {
            str = "0.";
        }

        String[] splittedInput = str.split("[.]");

        if (splittedInput.length > 1) {
            //has decimal point
            intPart = splittedInput[0];
            decimalPart = splittedInput[1];
        } else {
            intPart = str;
            decimalPart = null;
        }

        //clean up - remove currency symbol, comma
        String cleanString = intPart.replaceAll("[$,]", "");

        //double zeros are weird
        if (cleanString.startsWith("00")) {
            cleanString = "0";
        }

        //check if decimal point is at the end
        boolean hasDecimalPointAtEnd = false;
        if (cleanString.length() > 0) {
            hasDecimalPointAtEnd = '.' == cleanString.charAt(cleanString.length() - 1);
        }

        //max digit check for integer part
        if (hasDecimalPointAtEnd) {
            //this length should include decimal point
            if (cleanString.length() > (MAX_NUM_DIGIT + 1)) {
                cleanString = cleanString.substring(0, MAX_NUM_DIGIT + 1);
            }
        } else {
            //it has only decimal
            if (cleanString.length() > MAX_NUM_DIGIT) {
                cleanString = cleanString.substring(0, MAX_NUM_DIGIT);
            }
        }

        //max digit check for decimal part
        if (decimalPart != null && decimalPart.length() > MAX_NUM_DECIMAL_DIGIT) {
            decimalPart = decimalPart.substring(0, MAX_NUM_DECIMAL_DIGIT);
        }

        //add currency sign & format with commas
        String formatted = "";
        if (!"".equals(cleanString)) {
            DecimalFormat df = new DecimalFormat("###,###,###,###");
            formatted = "$" + df.format(Double.parseDouble(cleanString));

            //restore decimal point if ended with decimal point
            if (hasDecimalPointAtEnd) {
                formatted += ".";
            }
        }

        //deal decimal values
        if (decimalPart != null) {
            formatted += "." + decimalPart;
        }

        return formatted;
    }

    public static String getHashedWithLastDigitsIntact(String text, int numLastDigit) {
        if (text == null)
            return text;

        int totalLen = text.length();

        if (numLastDigit > totalLen)
            return text;

        StringBuffer buffer = new StringBuffer();
        int numberOfHash = totalLen - numLastDigit;
        for (int i = 0; i < numberOfHash; i++) {
            buffer.append("#");
        }

        for (int charAt = totalLen - numLastDigit; charAt < totalLen; charAt++) {
            buffer.append(text.charAt(charAt));
        }

        return buffer.toString();
    }

    public static String getDollarCurrenyFormattedNumber(double amount) {
        //old way using currency format - could be problematic for formatting may vary depending on locale setting or os version
        //return NumberFormat.getCurrencyInstance().format(amount);

        DecimalFormat format = new DecimalFormat("$#,###,###,##0.00;-$#,###,###,##0.00");
        return format.format(amount);
    }

    public static boolean isPostCode(String text) {
        return (TextUtils.isDigitsOnly(text) && text.length() == 4);
    }
}
