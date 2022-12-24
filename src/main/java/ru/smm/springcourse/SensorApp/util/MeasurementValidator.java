package ru.smm.springcourse.SensorApp.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.smm.springcourse.SensorApp.models.Measurement;
import ru.smm.springcourse.SensorApp.services.SensorsService;

@Component
public class MeasurementValidator implements Validator {

    private final SensorsService sensorsService;

    @Autowired
    public MeasurementValidator(SensorsService sensorsService) {
        this.sensorsService = sensorsService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Measurement.class.equals(clazz);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Measurement measurement = (Measurement) o;

        // 1ая проверка, проверяем есть ли вообще сенсор у этого измерения
        if (measurement.getSensor() == null)
            return;

        // 2ая проверка, есть ли сенсор с таким названием
        if (sensorsService.findByName(measurement.getSensor().getName()).isEmpty())
            // 1 арг - на каком поле произошла ошибка
            // 2 арг - код ошибки (пока нас не интересует)
            // 3 арг - сообщение об ошибке
            errors.rejectValue("sensor", "", "С таким именем нет зарегистрированного сенсора");
    }
}
