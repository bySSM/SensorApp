package ru.smm.springcourse.SensorApp.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.smm.springcourse.SensorApp.dto.SensorDTO;
import ru.smm.springcourse.SensorApp.models.Sensor;
import ru.smm.springcourse.SensorApp.services.SensorsService;
import ru.smm.springcourse.SensorApp.util.MeasurementErrorResponse;
import ru.smm.springcourse.SensorApp.util.MeasurementException;
import ru.smm.springcourse.SensorApp.util.SensorValidator;

import javax.validation.Valid;
import java.time.LocalDateTime;

import static ru.smm.springcourse.SensorApp.util.ErrorsUtils.returnErrorsToClient;


@RestController
@RequestMapping("/sensors")
public class SensorsController {

    private final SensorsService sensorsService;
    private final ModelMapper modelMapper;
    private final SensorValidator sensorValidator;

    @Autowired
    public SensorsController(SensorsService sensorsService, ModelMapper modelMapper, SensorValidator sensorValidator) {
        this.sensorsService = sensorsService;
        this.modelMapper = modelMapper;
        this.sensorValidator = sensorValidator;
    }

    // Метод для регистрации датчика. Возвращает http ответ (можем возвращать любой объект, например Sensor)
    // Когда мы пришлем JSON объект в этот метод, то @RequestBody сконвертирует его в объект SensorDTO
    @PostMapping("/registration")
    public ResponseEntity<HttpStatus> registration(@RequestBody @Valid SensorDTO sensorDTO,
                                                   BindingResult bindingResult) {

        Sensor sensorToAdd = convertToSensor(sensorDTO);

        // Валидируем поле name, проверяем, чтобы был уникальными ФИО
        sensorValidator.validate(sensorToAdd, bindingResult);

        // Если есть ошибки
        if (bindingResult.hasErrors())
            returnErrorsToClient(bindingResult);

        // Если ошибок не было, то регистрируем сенсор и его сохраняем в БД
        sensorsService.registrationSensor(sensorToAdd);

        // Возвращаем HTTP ответ с пустым телом и со статусом - 200
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler // Помечаем метод, который ловит исключение и помещает в себя необходимый JSON.
    // арг - исключение, которое ловим в этом методе
    private ResponseEntity<MeasurementErrorResponse> handleException(MeasurementException e) {
        MeasurementErrorResponse response = new MeasurementErrorResponse(
                e.getMessage(), // сообщение, которое создалось в методе registration, если не прошла валидация данных
                LocalDateTime.now()
        );

        // 1 арг - тело ответа; 2 арг - статус ответа в заголовке
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); // BAD_REQUEST - 400 статус (скорее всего)
    }


    // Метод для конвертации SensorDTO, который отправляем клиенту в сущность Sensor.
    // Чтобы не назначать все поля вручную (sensor.setName(sensorDTO.getName());) используем ModelMapper
    private Sensor convertToSensor(SensorDTO sensorDTO) {
        // Bean ModelMapper создали в конфигурационном классе SensorAppApplication и добавили его в конструктор

        // Задаем исходный объект и целевой класс (к которому мы хотим преобразовать)
        // Возвращаем объект класса Sensor
        return modelMapper.map(sensorDTO, Sensor.class);
    }

}
