package ru.smm.springcourse.SensorApp.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.smm.springcourse.SensorApp.models.Sensor;
import ru.smm.springcourse.SensorApp.services.SensorsService;

@Component
public class SensorValidator implements Validator {

    private final SensorsService sensorsService;

    @Autowired
    public SensorValidator(SensorsService sensorsService) {
        this.sensorsService = sensorsService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Sensor.class.equals(clazz);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Sensor sensor = (Sensor) o;

        // 1ая проверка, есть ли сенсор с таким же названием
        // isPresent() - метод обертки Optional, который проверяет, существует ли объект в этом Optional
        if (sensorsService.findByName(sensor.getName()).isPresent())
            // 1 арг - на каком поле произошла ошибка
            // 2 арг - код ошибки (пока нас не интересует)
            // 3 арг - сообщение об ошибке
            errors.rejectValue("name", "", "Сенсор с таким именем уже есть!");
    }
}
