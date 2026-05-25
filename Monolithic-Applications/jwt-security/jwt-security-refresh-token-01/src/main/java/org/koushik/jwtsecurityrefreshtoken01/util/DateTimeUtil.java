package org.koushik.jwtsecurityrefreshtoken01.util;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class DateTimeUtil {
    private DateTimeUtil(){

    }
    public static Instant now() {
        return Instant.now();
    }
    public static Instant getAccessTokenExpiry(){
        return Instant.now().plus(3, ChronoUnit.MINUTES);
    }
    public static Instant getRefreshTokenExpiry(){
        return Instant.now().plus(7, ChronoUnit.DAYS);
    }
}
