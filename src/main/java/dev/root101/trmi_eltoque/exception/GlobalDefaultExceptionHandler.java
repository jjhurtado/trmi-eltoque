package dev.root101.trmi_eltoque.exception;

import dev.root101.commons.exceptions.ApiException;
import dev.root101.commons.exceptions.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalDefaultExceptionHandler extends ResponseEntityExceptionHandler {

    //------------------- Error logger UC -------------------\\
    private void logException(Exception exc, HttpStatus status) {
    }

    //---------------------- END ----------------------\\
    /**
     * <pre>
     * Flujo:
     * 1 - Obtengo el HttpStatus de la excepcion.
     * 2 - Hago el log de pepper(almaceno el error en la BD).
     * 3 - Creo y devuelvo el 'ResponseEntity' con body:
     *          validationExc.getMessages(). => Lista de todas las validaciones que se rompieron.
     * </pre>
     *
     * @param validationExc
     * @param request
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value
            = {ValidationException.class})
    protected ResponseEntity handleValidationException(
            ValidationException validationExc,
            WebRequest request
    ) throws Exception {
        //Step 1
        HttpStatus status = validationExc.getStatusCode();

        //Step 2
        logException(validationExc, status);

        //Step 3
        return new ResponseEntity(
                validationExc.getMessages(),
                status
        );
    }

    /**
     * <pre>
     * Flujo:
     * 1 - Obtengo el HttpStatus de la excepcion.
     * 2 - Hago el log(almaceno el error en la BD).
     * 3 - Creo y devuelvo el 'ResponseEntity' con body 'apiExc.getMessage()'
     * </pre>
     *
     * @param apiExc
     * @param request
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value
            = {ApiException.class})
    protected ResponseEntity handleApiException(
            ApiException apiExc,
            WebRequest request
    ) throws Exception {
        //Step 1
        HttpStatus status = apiExc.status();

        //Step 2
        logException(apiExc, status);

        //Step 3
        return new ResponseEntity(
                apiExc.getMessage(),
                status
        );
    }

    /**
     *
     * <pre>
     * Flujo:
     * 1 - Obtengo el HttpStatus de la excepcion.
     * 2 - Hago el log de pepper(almaceno el error en la BD).
     * 3 - Creo y devuelvo el 'ResponseEntity' con body 'ex.getResponseBodyAsString()'
     * </pre>
     *
     * @param ex
     * @param request
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value
            = {HttpClientErrorException.class})
    protected ResponseEntity handleHttpClientError(
            HttpClientErrorException ex,
            WebRequest request
    ) throws Exception {
        //Step 1
        HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());

        //Step 2
        logException(ex, status);

        //Step 3
        return new ResponseEntity(
                ex.getResponseBodyAsString(),
                status
        );
    }

    /**
     *
     * <pre>
     * Flujo:
     * 1 - Compruebo si la excepcion esta anotada con 'ResponseStatus', lo que la convierte en una excepcion preparada para que la procese Spring.
     *  1.1 - Si esta anotada con 'ResponseStatus' hago el 'rethrow' para que la procese Spring.
     * Si no es una excepcion de Spting:
     * 2 - Como es 'Exception', no se ha procesado por mas nadie, el status se definio como INTERNAL_SERVER_ERROR(500)
     * 3 - Hago el log de pepper(almaceno el error en la BD).
     * 4 - Creo y devuelvo el 'ResponseEntity' con body 'ex.getMessage()'
     * </pre>
     *
     * @param ex
     * @param request
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value
            = {Exception.class})
    protected ResponseEntity handleGeneric(
            Exception ex,
            WebRequest request
    ) throws Exception {
        //Step 1
        if (AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class) != null) {
            //Step 1.1
            throw ex;
        } else {
            //Step 2
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

            //Step 3
            logException(ex, status);

            //Step 4
            return new ResponseEntity(
                    ex.getMessage(),
                    status
            );
        }
    }

    /**
     *
     * <pre>
     * Flujo:
     * 1 - Si es un Access Denied es un 403.
     * 2 - Hago el log de pepper(almaceno el error en la BD).
     * 3 - Creo y devuelvo el 'ResponseEntity' con body 'ApiResponse' y propiedades:
     *  3.1 - status: String.valueOf(status.value()),
     *  3.2 - msg: ex.getMessage()
     *  3.3 - data: null.
     * </pre>
     *
     * @param ex
     * @param request
     * @return
     * @throws Exception
     *
     * @ExceptionHandler(value = {AccessDeniedException.class}) protected
     * ResponseEntity handleAccessDenied( AccessDeniedException ex, WebRequest
     * request ) throws Exception { HttpStatus status = HttpStatus.FORBIDDEN;
     *
     * pepperExceptionLogger.log(ex, status);
     *
     * return new ResponseEntity( ex.getMessage(), status ); }
     */
}
