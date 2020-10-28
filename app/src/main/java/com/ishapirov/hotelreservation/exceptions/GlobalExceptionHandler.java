package com.ishapirov.hotelreservation.exceptions;

import com.ishapirov.hotelapi.authenticationservice.exceptions.InvalidUsernameOrPasswordException;
import com.ishapirov.hotelapi.exceptionresponse.ExceptionResponse;
import com.ishapirov.hotelapi.exceptionresponse.Violation;
import com.ishapirov.hotelapi.generalexceptions.*;
import com.ishapirov.hotelapi.reservationservice.exceptions.*;
import com.ishapirov.hotelapi.roomservice.exceptions.RoomNotFoundException;
import com.ishapirov.hotelapi.roomservice.exceptions.RoomTypeNotFoundException;
import com.ishapirov.hotelapi.userservice.exceptions.CustomerNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ExceptionResponse> customerNotFound(CustomerNotFoundException customerNotFoundException){
        ExceptionResponse response = new ExceptionResponse();
        response.setTimestamp(LocalDateTime.now());
        response.setStatus(404);
        response.setError("NOT_FOUND");
        response.setMessage(customerNotFoundException.getMessage());

        return new ResponseEntity<ExceptionResponse>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<ExceptionResponse> unauthorizedAccess(UnauthorizedAccessException unauthorizedAccessException){
        ExceptionResponse response = new ExceptionResponse();
        response.setTimestamp(LocalDateTime.now());
        response.setStatus(403);
        response.setError("FORBIDDEN");
        response.setMessage(unauthorizedAccessException.getMessage());

        return new ResponseEntity<ExceptionResponse>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(DatesInvalidException.class)
    public ResponseEntity<ExceptionResponse> datesInvalid(DatesInvalidException datesInvalidException){
        ExceptionResponse response = new ExceptionResponse();
        response.setTimestamp(LocalDateTime.now());
        response.setStatus(400);
        response.setError("BAD_REQUEST");
        response.setMessage(datesInvalidException.getMessage());

        return new ResponseEntity<ExceptionResponse>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ExceptionResponse> badRequest(BadRequestException badRequestException){
        ExceptionResponse response = new ExceptionResponse();
        response.setTimestamp(LocalDateTime.now());
        response.setStatus(400);
        response.setError("BAD_REQUEST");
        response.setMessage(badRequestException.getMessage());

        return new ResponseEntity<ExceptionResponse>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidUsernameOrPasswordException.class)
    public ResponseEntity<ExceptionResponse> invalidUsernameOrPassword(InvalidUsernameOrPasswordException invalidUsernameOrPasswordException){
        ExceptionResponse response = new ExceptionResponse();
        response.setTimestamp(LocalDateTime.now());
        response.setStatus(401);
        response.setError("UNAUTHORIZED");
        response.setMessage(invalidUsernameOrPasswordException.getMessage());

        return new ResponseEntity<ExceptionResponse>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ReservationDeletionError.class)
    public ResponseEntity<ExceptionResponse> reservationCreationError(ReservationDeletionError reservationDeletionError){
        ExceptionResponse response = new ExceptionResponse();
        response.setTimestamp(LocalDateTime.now());
        response.setStatus(500);
        response.setError("INTERNAL_SERVER_ERROR");
        response.setMessage(reservationDeletionError.getMessage());

        return new ResponseEntity<ExceptionResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ReservationNotFoundException.class)
    public ResponseEntity<ExceptionResponse> reservationNotFoundException(ReservationNotFoundException reservationNotFoundException){
        ExceptionResponse response = new ExceptionResponse();
        response.setTimestamp(LocalDateTime.now());
        response.setStatus(404);
        response.setError("NOT_FOUND");
        response.setMessage(reservationNotFoundException.getMessage());

        return new ResponseEntity<ExceptionResponse>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ReservationOverlapException.class)
    public ResponseEntity<ExceptionResponse> reservationOverlapException(ReservationOverlapException reservationOverlapException){
        ExceptionResponse response = new ExceptionResponse();
        response.setTimestamp(LocalDateTime.now());
        response.setStatus(409);
        response.setError("CONFLICT");
        response.setMessage(reservationOverlapException.getMessage());

        return new ResponseEntity<ExceptionResponse>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ReservationAlreadyCancelledException.class)
    public ResponseEntity<ExceptionResponse> reservationAlreadyCancelledException(ReservationAlreadyCancelledException reservationAlreadyCancelledException){
        ExceptionResponse response = new ExceptionResponse();
        response.setTimestamp(LocalDateTime.now());
        response.setStatus(409);
        response.setError("CONFLICT");
        response.setMessage(reservationAlreadyCancelledException.getMessage());

        return new ResponseEntity<ExceptionResponse>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ReservationUsernamesDoNotMatchException.class)
    public ResponseEntity<ExceptionResponse> reservationUsernamesDoNotMatchException(ReservationUsernamesDoNotMatchException reservationUsernamesDoNotMatchException){
        ExceptionResponse response = new ExceptionResponse();
        response.setTimestamp(LocalDateTime.now());
        response.setStatus(403);
        response.setError("FORBIDDEN");
        response.setMessage(reservationUsernamesDoNotMatchException.getMessage());

        return new ResponseEntity<ExceptionResponse>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(RoomNotFoundException.class)
    public ResponseEntity<ExceptionResponse> roomNotFoundException(RoomNotFoundException roomNotFoundException){
        ExceptionResponse response = new ExceptionResponse();
        response.setTimestamp(LocalDateTime.now());
        response.setStatus(404);
        response.setError("NOT_FOUND");
        response.setMessage(roomNotFoundException.getMessage());

        return new ResponseEntity<ExceptionResponse>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RoomTypeNotFoundException.class)
    public ResponseEntity<ExceptionResponse>  roomTypeNotFoundException(RoomTypeNotFoundException roomTypeNotFoundException){
        ExceptionResponse response = new ExceptionResponse();
        response.setTimestamp(LocalDateTime.now());
        response.setStatus(404);
        response.setError("NOT_FOUND");
        response.setMessage(roomTypeNotFoundException.getMessage());

        return new ResponseEntity<ExceptionResponse>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotImplementedException.class)
    public ResponseEntity<ExceptionResponse>  notImplementedException(NotImplementedException notImplementedException){
        ExceptionResponse response = new ExceptionResponse();
        response.setTimestamp(LocalDateTime.now());
        response.setStatus(501);
        response.setError("NOT_IMPLEMENTED");
        response.setMessage(notImplementedException.getMessage());

        return new ResponseEntity<ExceptionResponse>(response, HttpStatus.NOT_IMPLEMENTED);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ExceptionResponse onConstraintValidationException(
            ConstraintViolationException e) {
        ExceptionResponse response = new ExceptionResponse();
        response.setTimestamp(LocalDateTime.now());
        response.setStatus(400);
        response.setError("BAD_REQUEST");
        response.setMessage("One or more constraints were violated");
        for (ConstraintViolation violation : e.getConstraintViolations()) {
            response.getInputViolations().add(
                    new Violation(violation.getPropertyPath().toString(), violation.getMessage()));
        }
        return response;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ExceptionResponse onMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {
        ExceptionResponse response = new ExceptionResponse();
        response.setTimestamp(LocalDateTime.now());
        response.setStatus(400);
        response.setError("BAD_REQUEST");
        response.setMessage("One or more constraints were violated");
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            response.getInputViolations().add(
                    new Violation(fieldError.getField(), fieldError.getDefaultMessage()));
        }
        return response;
    }
}
