package com.carsonlius.framework.common.util.date;

import java.time.LocalDateTime;

/**
 * @version V1.0
 * @author: carsonlius
 * @date: 2023/12/21 10:08
 * @company
 * @description 时间工具类
 */
public class DateUtils {
    public static boolean isExpired(LocalDateTime time) {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(time);
    }
}
