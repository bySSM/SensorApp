package ru.smm.springcourse.SensorApp.util;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

/**
 *  Кастомный класс для отправки ошибок клиенту
 */

public class ErrorsUtils {

    public static void returnErrorsToClient(BindingResult bindingResult) {

        // Совмещаем ошибки валидации и отправляем ответ обратно
        StringBuilder errorMsg = new StringBuilder();

        // Получим ошибки и положим их в список
        List<FieldError> errors = bindingResult.getFieldErrors();
        for (FieldError error : errors) {
            errorMsg.append(error.getField())
                    .append(" - ").append(error.getDefaultMessage() == null ? error.getCode() : error.getDefaultMessage())
                    .append(";");
        }

        throw new MeasurementException(errorMsg.toString());
    }
}
