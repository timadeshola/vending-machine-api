package com.timadeshola.vendingmachine.core;

import com.timadeshola.vendingmachine.core.exceptions.CustomException;
import com.timadeshola.vendingmachine.core.exceptions.model.ErrorDetail;
import com.timadeshola.vendingmachine.core.exceptions.model.ValidationError;
import com.timadeshola.vendingmachine.model.response.AppResponse;
import org.modelmapper.MappingException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.NestedServletException;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Project title: vending-machine
 *
 * @author johnadeshola
 * Date: 9/16/21
 * Time: 1:00 PM
 */
@RestControllerAdvice
public class ResourceAdvice {

    @ExceptionHandler(CustomException.class)
    public final ResponseEntity<AppResponse<ErrorDetail>> handleCustomException(CustomException ex, WebRequest request) {
        ErrorDetail errorDetails = ErrorDetail.builder()
                .message(ex.getMessage())
                .code(ex.getStatus().value() != 0 ? ex.getStatus().value() : HttpStatus.PRECONDITION_FAILED.value())
                .details(request.getDescription(true))
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(errorDetails.getCode()).body(AppResponse.<ErrorDetail>builder()
                .message(errorDetails.getMessage())
                .status(errorDetails.getCode())
                .error(errorDetails)
                .build());

    }

    @ExceptionHandler(EntityNotFoundException.class)
    public final ResponseEntity<AppResponse<ErrorDetail>> handleEntityNotFoundException(EntityNotFoundException ex, WebRequest request) {
        ErrorDetail errorDetails = ErrorDetail.builder()
                .message(ex.getMessage())
                .code(HttpStatus.NOT_FOUND.value())
                .details(request.getDescription(true))
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(errorDetails.getCode()).body(AppResponse.<ErrorDetail>builder()
                .message(errorDetails.getMessage())
                .status(errorDetails.getCode())
                .error(errorDetails)
                .build());

    }

    @ExceptionHandler(ResponseStatusException.class)
    public final ResponseEntity<AppResponse<ErrorDetail>> handleResponseStatusException(ResponseStatusException ex, WebRequest request) {
        ErrorDetail errorDetails = ErrorDetail.builder()
                .message(ex.getMessage())
                .code(ex.getStatus().value())
                .details(request.getDescription(true))
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(errorDetails.getCode()).body(AppResponse.<ErrorDetail>builder()
                .message(errorDetails.getMessage())
                .status(errorDetails.getCode())
                .error(errorDetails)
                .build());

    }

    @ExceptionHandler(NoResultException.class)
    public final ResponseEntity<AppResponse<ErrorDetail>> handleNoResultException(NoResultException ex, WebRequest request) {
        ErrorDetail errorDetails = ErrorDetail.builder()
                .message(ex.getMessage())
                .code(HttpStatus.NOT_FOUND.value())
                .details(request.getDescription(true))
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(errorDetails.getCode()).body(AppResponse.<ErrorDetail>builder()
                .message(errorDetails.getMessage())
                .status(errorDetails.getCode())
                .error(errorDetails)
                .build());

    }

    @ExceptionHandler({MappingException.class})
    public final ResponseEntity<AppResponse<ErrorDetail>> handleMappingException(MappingException ex, WebRequest request) {
        ErrorDetail errorDetails = ErrorDetail.builder()
                .message(ex.getMessage())
                .code(HttpStatus.BAD_REQUEST.value())
                .details(request.getDescription(true))
                .timestamp(new Date())
                .build();
        return ResponseEntity.ok().body(AppResponse.<ErrorDetail>builder()
                .message(ex.getMessage())
                .status(errorDetails.getCode())
                .error(errorDetails)
                .build());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<AppResponse<ErrorDetail>> handleUsernameNotFoundException(UsernameNotFoundException ex, WebRequest request) {
        ErrorDetail errorDetails = ErrorDetail.builder()
                .message("Invalid username or password credentials")
                .code(HttpStatus.NO_CONTENT.value())
                .details(request.getDescription(true))
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(AppResponse.<ErrorDetail>builder()
                .message(ex.getMessage())
                .status(errorDetails.getCode())
                .error(errorDetails)
                .build());
    }

    @ExceptionHandler({AuthenticationCredentialsNotFoundException.class, BadCredentialsException.class, ClientRegistrationException.class})
    public ResponseEntity<AppResponse<ErrorDetail>> handleAuthenticationCredentialsNotFoundException(AuthenticationCredentialsNotFoundException ex, WebRequest request) {
        ErrorDetail errorDetails = ErrorDetail.builder()
                .message("Invalid authorization / authentication")
                .code(HttpStatus.UNAUTHORIZED.value())
                .details(request.getDescription(true))
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(AppResponse.<ErrorDetail>builder()
                .message(ex.getMessage())
                .status(errorDetails.getCode())
                .error(errorDetails)
                .build());
    }

    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class, NestedServletException.class})
    public ResponseEntity<AppResponse<ErrorDetail>> handleIllegalArgumentException(RuntimeException ex, WebRequest request) {
        ErrorDetail errorDetails = ErrorDetail.builder()
                .message(ex.getMessage())
                .code(HttpStatus.BAD_REQUEST.value())
                .details(request.getDescription(true))
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(AppResponse.<ErrorDetail>builder()
                .message(ex.getMessage())
                .status(errorDetails.getCode())
                .error(errorDetails)
                .build());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<AppResponse<ErrorDetail>> handleDataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request) {
        ErrorDetail errorDetails = ErrorDetail.builder()
                .message(ex.getMessage())
                .code(HttpStatus.BAD_REQUEST.value())
                .details(request.getDescription(true))
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(AppResponse.<ErrorDetail>builder()
                .message(ex.getMessage())
                .status(errorDetails.getCode())
                .error(errorDetails)
                .build());
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<AppResponse<ErrorDetail>> handleHttpMediaTypeNotAcceptableException(HttpMediaTypeNotAcceptableException ex, WebRequest request) {
        ErrorDetail errorDetails = ErrorDetail.builder()
                .message(ex.getMessage())
                .code(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value())
                .details(request.getDescription(true))
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(AppResponse.<ErrorDetail>builder()
                .message(ex.getMessage())
                .status(errorDetails.getCode())
                .error(errorDetails)
                .build());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<AppResponse<ErrorDetail>> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex, WebRequest request) {
        ErrorDetail errorDetails = ErrorDetail.builder()
                .message(ex.getMessage())
                .code(HttpStatus.BAD_REQUEST.value())
                .details(request.getDescription(true))
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(AppResponse.<ErrorDetail>builder()
                .message(ex.getMessage())
                .status(errorDetails.getCode())
                .error(errorDetails)
                .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<AppResponse<List<ErrorDetail>>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest request) {
        List<ValidationError> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) ->
                errors.add(ValidationError.builder()
                        .field(((FieldError) error).getField())
                        .rejectedValue(((FieldError) error).getRejectedValue())
                        .objectName(error.getObjectName())
                        .build()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(AppResponse.<List<ErrorDetail>>builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error(ErrorDetail.builder()
                        .message("Validation Error")
                        .code(HttpStatus.BAD_REQUEST.value())
                        .details(request.getDescription(true))
                        .timestamp(new Date())
                        .validation(errors)
                        .build())
                .message("Validation error")
                .build());

    }
}