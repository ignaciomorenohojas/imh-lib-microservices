package es.imh.error.model.impl;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonInclude;
import es.statplans.error.model.ApiError;
import es.statplans.error.model.ApiSubError;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiErrorImpl implements ApiError {

    private HttpStatus status;

    private String message;

    private String path;

    @JsonFormat(shape = Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    @Builder.Default
    private LocalDateTime date = LocalDateTime.now();

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ApiSubError> subErrors;

    @Override
    public int getStatusCode() {
        return status!=null?status.value():0;
    }


}
