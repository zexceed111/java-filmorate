package ru.yandex.practicum.filmorate.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class MyExceptionHandler {

    @ExceptionHandler({ValidationException.class, DuplicateDataException.class, NotFoundException.class})
    public ResponseEntity<Object> handler(ValidationException e) {
        StackTraceElement sElem = e.getStackTrace()[0];
        String className = sElem.getClassName();
        String str = className.substring(className.lastIndexOf(".") + 1);
        log.info("\nClass: {}; Method: {}; Line: {}; \nMessage: {}", str, sElem.getMethodName(), sElem.getLineNumber(), e.getMessage());
        return new ResponseEntity<>(e.getObjForBody(), e.getClass().equals(NotFoundException.class) ? HttpStatus.INTERNAL_SERVER_ERROR : HttpStatus.BAD_REQUEST);
    }
}
