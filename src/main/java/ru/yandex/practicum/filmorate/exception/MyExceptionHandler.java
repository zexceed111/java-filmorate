package ru.yandex.practicum.filmorate.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class MyExceptionHandler {

    @ExceptionHandler({ValidationException.class, DuplicateDataException.class})
    public ResponseEntity<Object> handler(ValidationException e) {
        StackTraceElement sElem = e.getStackTrace()[0];
        String className = sElem.getClassName();
        String str = className.substring(className.lastIndexOf(".") + 1);
        log.info("\nClass: {}; Method: {}; Line: {}; \nMessage: {}",
                str, sElem.getMethodName(), sElem.getLineNumber(), e.getMessage());
        return new ResponseEntity<>(e.getObjForBody(), HttpStatus.BAD_REQUEST);
    }

    //Решил разнести в разные методы, так вроде нагляднее
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handler(NotFoundException e) {
        StackTraceElement sElem = e.getStackTrace()[0];
        String className = sElem.getClassName();
        String str = className.substring(className.lastIndexOf(".") + 1);
        log.info("\nClass: {}; Method: {}; Line: {}; \nMessage: {}",
                str, sElem.getMethodName(), sElem.getLineNumber(), e.getMessage());
        return new ResponseEntity<>(e.getObjForBody(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<Object> handler(InternalServerException e) {
        StackTraceElement sElem = e.getStackTrace()[0];
        String className = sElem.getClassName();
        String str = className.substring(className.lastIndexOf(".") + 1);
        log.info("\nClass: {}; Method: {}; Line: {}; \nMessage: {}",
                str, sElem.getMethodName(), sElem.getLineNumber(), e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
