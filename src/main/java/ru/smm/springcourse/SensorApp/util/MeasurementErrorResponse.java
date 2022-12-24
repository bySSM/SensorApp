package ru.smm.springcourse.SensorApp.util;

import java.time.LocalDateTime;

public class MeasurementErrorResponse {

    private String message; // Сообщение об ошибке
    private LocalDateTime localDateTime; // отметка времени, в который произошла эта ошибка, мс

    public MeasurementErrorResponse(String message, LocalDateTime localDateTime) {
        this.message = message;
        this.localDateTime = localDateTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }
}
