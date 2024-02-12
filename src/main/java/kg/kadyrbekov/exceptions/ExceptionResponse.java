package kg.kadyrbekov.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
public class ExceptionResponse  {

    private HttpStatus httpStatus;

    private String exceptionName;

    private String message;



}

