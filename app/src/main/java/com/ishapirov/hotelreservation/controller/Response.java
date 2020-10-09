package com.ishapirov.hotelreservation.controller;

import lombok.Data;

@Data
public class Response {
    private String message;
    public Response(String message){
        this.message = message;
    }
}
