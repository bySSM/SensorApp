package ru.smm.springcourse.SensorApp.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.smm.springcourse.SensorApp.dto.MeasurementDTO;
import ru.smm.springcourse.SensorApp.dto.MeasurementsResponse;
import ru.smm.springcourse.SensorApp.models.Measurement;
import ru.smm.springcourse.SensorApp.services.MeasurementsService;
import ru.smm.springcourse.SensorApp.util.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

import static ru.smm.springcourse.SensorApp.util.ErrorsUtils.returnErrorsToClient;

@RestController
@RequestMapping("/measurement")
public class MeasurementsController {

    private final MeasurementsService measurementsService;
    private final MeasurementValidator measurementValidator;
    private final ModelMapper modelMapper;

    @Autowired
    public MeasurementsController(MeasurementsService measurementsService, ModelMapper modelMapper,
                                  MeasurementValidator measurementValidator) {
        this.measurementsService = measurementsService;
        this.modelMapper = modelMapper;
        this.measurementValidator = measurementValidator;
    }

    @PostMapping("/add")
    public ResponseEntity<HttpStatus> add(@RequestBody @Valid MeasurementDTO measurementDTO,
                                                      BindingResult bindingResult) {

        Measurement measurementToAdd = convertToMeasurement(measurementDTO);

        // Валидируем название сенсора, проверяем, чтобы такое название было в БД
        measurementValidator.validate(measurementToAdd, bindingResult);

        // Если есть ошибки
        if (bindingResult.hasErrors())
            returnErrorsToClient(bindingResult);

        // Если ошибок не было, то регистрируем сенсор и его сохраняем в БД
        measurementsService.addMeasurement(measurementToAdd);

        // Возвращаем HTTP ответ с пустым телом и со статусом - 200
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping()
    public MeasurementsResponse getMeasurements() {
        // Обычно список из элементов оборачивается в один объект для пересылки
        return new MeasurementsResponse(measurementsService.findAll().stream().map(this::convertToMeasurementDTO)
                .collect(Collectors.toList()));
    }

    @GetMapping("/rainyDaysCount")
    public Long getRainyDaysCount(){
        return measurementsService.findAll().stream().filter(Measurement::getRaining).count();
    }

    // Метод для конвертации MeasurementDTO, который отправляем клиенту в сущность Measurement.
    // Чтобы не назначать все поля вручную (measurement.setName(measurementDTO.getName());) используем ModelMapper
    private Measurement convertToMeasurement(MeasurementDTO measurementDTO) {
        // Bean ModelMapper создали в конфигурационном классе SensorAppApplication и добавили его в конструктор

        // Задаем исходный объект и целевой класс (к которому мы хотим преобразовать)
        // Возвращаем объект класса Measurement
        return modelMapper.map(measurementDTO, Measurement.class);
    }

    // Метод для конвертации Measurement, который отправляем клиенту в сущность MeasurementDTO.
    // Чтобы не назначать все поля вручную (measurement.setName(measurementDTO.getName());) используем ModelMapper
    private MeasurementDTO convertToMeasurementDTO(Measurement measurement) {
        // Bean ModelMapper создали в конфигурационном классе SensorAppApplication и добавили его в конструктор

        // Задаем исходный объект и целевой класс (к которому мы хотим преобразовать)
        // Возвращаем объект класса SensorDTO
        return modelMapper.map(measurement, MeasurementDTO.class);
    }

    @ExceptionHandler // Помечаем метод, который ловит исключение и помещает в себя необходимый JSON.
    // арг - исключение, которое ловим в этом методе
    private ResponseEntity<MeasurementErrorResponse> handleException(MeasurementException e) {
        MeasurementErrorResponse response = new MeasurementErrorResponse(
                e.getMessage(), // сообщение, которое создалось в методе createMeasurements, если не прошла валидация данных
                LocalDateTime.now()
        );

        // 1 арг - тело ответа; 2 арг - статус ответа в заголовке
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); // BAD_REQUEST - 400 статус (скорее всего)
    }
}
