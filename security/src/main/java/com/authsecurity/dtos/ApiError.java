package com.authsecurity.dtos;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public record ApiError(
    int status,
    String error,
    String message,
    String path,
    String timestamp
) {
    public static ApiError of(int status, String error, String message, String path) {
        String ts = OffsetDateTime.now(ZoneOffset.UTC)
                                  .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        return new ApiError(status, error, message, path, ts);
    }
}
