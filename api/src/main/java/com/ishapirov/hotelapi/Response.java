package com.ishapirov.hotelapi;

import lombok.Data;

@Data
public class Response {
    private String message;
    public Response(String message){
        this.message = message;
    }
}
