package es.imh.error.model;

import es.statplans.error.model.impl.ApiErrorImpl;
import es.statplans.error.model.impl.ApiValidationSubError;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Por defecto los errores se presentan en un formato JSON con los siguientes campos:
 * "timestamp": "2021-06-30T10:00:00.000+00:00" --> La fecha no se entiende bien
 * "status": 400
 * "error": "Bad Request"
 * "exception": "org.springframework.web.bind.MethodArgumentNotValidException" --> No interesa al cliente
 * "message": "Validation failed for object='userDto'. Error count: 1" --> Demasiados detalles para el cliente
 * "path": "/api/v1/users"
 *
 * Se va a cambiar por un formato de tipo
 * "status": "BAD_REQUEST"
 * "message": "Validation failed for object='userDto'. Error count: 1"
 * "path": "/api/v1/users"
 * "date": "06/07/2022 11:58:31"
 * "subErrors": [{
 *    "object": "userDto",
 *    "field": "name",
 *    "message": "Name must be between 2 and 50 characters"},
 * "statusCode": 400
 *
 */
public interface ApiError {
    HttpStatus getStatus();
    int getStatusCode();
    String getMessage();
    String getPath();
    LocalDateTime getDate();
    List<ApiSubError> getSubErrors();

    static ApiError fromErrorAttributes(Map<String, Object> defaultErrorAttributesMap) {
        int statusCode = ((Integer)defaultErrorAttributesMap.get("status")).intValue();
        ApiErrorImpl result =
          ApiErrorImpl.builder()
                  .status(HttpStatus.valueOf(statusCode))
                  .message((String)defaultErrorAttributesMap.getOrDefault("message", "No message available"))
                  .path((String)defaultErrorAttributesMap.getOrDefault("path", "No path available"))
                  .build();
        if (defaultErrorAttributesMap.containsKey("errors")) {
            List<ObjectError> errors = (List<ObjectError>) defaultErrorAttributesMap.get("errors");
            List<ApiSubError> subErrors = errors.stream()
                    .map(ApiValidationSubError::fromObjectError)
                    .collect(Collectors.toList());
            result.setSubErrors(subErrors);
        }
        return result;
    }
}
