package org.koushik.authappbackend.helper;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

public class ProjectHelper {
    public static UUID parseUUID(String uuid){
        return UUID.fromString(uuid);
    }

    public static ZonedDateTime getCurrentIndiaTime(){
        return ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));
    }
}
