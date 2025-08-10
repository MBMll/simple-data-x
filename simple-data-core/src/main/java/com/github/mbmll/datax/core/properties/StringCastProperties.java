package com.github.mbmll.datax.core.properties;


import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import java.util.Collections;
import java.util.List;
import java.util.TimeZone;

/**
 * @Author xlc
 * @Description
 * @Date 2025/8/11 00:29:13
 */

public class StringCastProperties {
    String datetimeFormat = "yyyy-MM-dd HH:mm:ss";
    String dateFormat = "yyyy-MM-dd";
    String timeFormat = "HH:mm:ss";
    List<String> extraFormats = Collections.emptyList();
    String timeZone = "GMT+8";
    String encoding = "UTF-8";
    FastDateFormat dateFormatter;
    FastDateFormat timeFormatter;
    FastDateFormat datetimeFormatter;
    TimeZone timeZoner;

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        if (StringUtils.isNotBlank(dateFormat)) {
            this.dateFormat = dateFormat;
        }
    }

    public FastDateFormat getDateFormatter() {
        return dateFormatter;
    }

    public void setDateFormatter(FastDateFormat dateFormatter) {
        this.dateFormatter = dateFormatter;
    }

    public String getDatetimeFormat() {
        return datetimeFormat;
    }

    public void setDatetimeFormat(String datetimeFormat) {
        if (StringUtils.isNotBlank(datetimeFormat)) {
            this.datetimeFormat = datetimeFormat;
        }
    }

    public FastDateFormat getDatetimeFormatter() {
        return datetimeFormatter;
    }

    public void setDatetimeFormatter(FastDateFormat datetimeFormatter) {
        this.datetimeFormatter = datetimeFormatter;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        if (StringUtils.isNotBlank(encoding)) {
            this.encoding = encoding;
        }
    }

    public List<String> getExtraFormats() {
        return extraFormats;
    }

    public void setExtraFormats(List<String> extraFormats) {
        if (extraFormats != null) {
            this.extraFormats = extraFormats;
        }
    }

    public String getTimeFormat() {
        return timeFormat;
    }

    public void setTimeFormat(String timeFormat) {
        if (StringUtils.isNotBlank(timeFormat)) {
            this.timeFormat = timeFormat;
        }
    }

    public FastDateFormat getTimeFormatter() {
        return timeFormatter;
    }

    public void setTimeFormatter(FastDateFormat timeFormatter) {
        this.timeFormatter = timeFormatter;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        if (StringUtils.isNotBlank(timeZone)) {
            this.timeZone = timeZone;
        }
    }

    public TimeZone getTimeZoner() {
        return timeZoner;
    }

    public void setTimeZoner(TimeZone timeZoner) {
        this.timeZoner = timeZoner;
    }
}
