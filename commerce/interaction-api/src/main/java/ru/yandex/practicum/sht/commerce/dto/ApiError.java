package ru.yandex.practicum.sht.commerce.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.Builder;
import lombok.Value;
import org.springframework.http.HttpStatus;

import java.util.*;

@Value
@Builder
public class ApiError {

    ReasonError cause;
    List<StackTraceItem> stackTrace;
    HttpStatus httpStatus;
    String userMessage;
    String message;
    List<ReasonError> suppressed;
    String localizedMessage;

    public static ApiError from(Exception exception, HttpStatus httpStatus) {
        String message = exception.getMessage();
        if (exception instanceof ConstraintViolationException) {
            ConstraintViolationException constraintViolationException = (ConstraintViolationException) exception;
            Map<String, String> violations = new HashMap<>();
            for (ConstraintViolation<?> violation : constraintViolationException.getConstraintViolations()) {
                violations.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            message = violations.toString();
        }
        return ApiError.builder()
                .cause(ApiError.ReasonError.from(exception.getCause()))
                .stackTrace(ApiError.StackTraceItem.list(exception.getStackTrace()))
                .httpStatus(httpStatus)
                .userMessage(message)
                .suppressed(ApiError.ReasonError.from(exception.getSuppressed()))
                .localizedMessage(exception.getLocalizedMessage())
                .build();
    }

    @Value
    @Builder
    public static class ReasonError {
        List<StackTraceItem> stackTrace;
        String message;
        String localizedMessage;

        public static ReasonError from(Throwable cause) {
            if (Objects.isNull(cause)) {
                return null;
            }
            return ReasonError.builder()
                    .stackTrace(ApiError.StackTraceItem.list(cause.getStackTrace()))
                    .message(cause.getMessage())
                    .localizedMessage(cause.getLocalizedMessage())
                    .build();
        }

        public static List<ReasonError> from(Throwable[] suppressed) {
            if (Objects.isNull(suppressed)) {
                return List.of();
            }
            return Arrays.stream(suppressed)
                    .map(ReasonError::from)
                    .toList();
        }
    }

    @Value
    @Builder
    public static class StackTraceItem {
        String classLoaderName;
        String moduleName;
        String moduleVersion;
        String methodName;
        String fileName;
        Integer lineNumber;
        String className;
        Boolean nativeMethod;

        public static List<StackTraceItem> list(StackTraceElement[] stackTrace) {
            if (Objects.isNull(stackTrace)) {
                return List.of();
            }
            return Arrays.stream(stackTrace)
                    .map(element ->
                            StackTraceItem.builder()
                                    .classLoaderName(element.getClassLoaderName())
                                    .moduleName(element.getModuleName())
                                    .moduleVersion(element.getModuleVersion())
                                    .methodName(element.getMethodName())
                                    .fileName(element.getFileName())
                                    .lineNumber(element.getLineNumber())
                                    .className(element.getClassName())
                                    .nativeMethod(element.isNativeMethod())
                                    .build())
                    .toList();
        }
    }
}