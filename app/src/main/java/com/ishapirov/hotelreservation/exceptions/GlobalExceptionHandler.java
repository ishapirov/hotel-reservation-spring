package com.ishapirov.hotelreservation.exceptions;

import com.ishapirov.hotelapi.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
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

    @ExceptionHandler(DatesInvalidException.class)
    public ResponseEntity<ExceptionResponse> datesInvalid(DatesInvalidException datesInvalidException){
        ExceptionResponse response = new ExceptionResponse();
        response.setTimestamp(LocalDateTime.now());
        response.setStatus(400);
        response.setError("BAD_REQUEST");
        response.setMessage(datesInvalidException.getMessage());

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

    @ExceptionHandler(ReservationCreationError.class)
    public ResponseEntity<ExceptionResponse> reservationCreationError(ReservationCreationError reservationCreationError){
        ExceptionResponse response = new ExceptionResponse();
        response.setTimestamp(LocalDateTime.now());
        response.setStatus(500);
        response.setError("INTERNAL_SERVER_ERROR");
        response.setMessage(reservationCreationError.getMessage());

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
}
