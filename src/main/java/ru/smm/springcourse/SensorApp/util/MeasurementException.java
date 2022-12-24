package ru.smm.springcourse.SensorApp.util;

/**
 *  Кастомное исключение валидации измерений погоды
 */

public class MeasurementException extends RuntimeException {
    public MeasurementException(String msg) {
        super(msg);
    }
}
