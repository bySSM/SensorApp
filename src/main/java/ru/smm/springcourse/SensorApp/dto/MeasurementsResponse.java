package ru.smm.springcourse.SensorApp.dto;

import java.util.List;

/**
 * Объект (список, который обернули в данный объект) для отправки клиенту на запрос
 */
public class MeasurementsResponse {
    private List<MeasurementDTO> measurements;

    public MeasurementsResponse(List<MeasurementDTO> measurements) {
        this.measurements = measurements;
    }

    public List<MeasurementDTO> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(List<MeasurementDTO> measurements) {
        this.measurements = measurements;
    }
}
