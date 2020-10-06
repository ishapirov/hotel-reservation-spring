package com.ishapirov.hotelreservation.controller_objects;

import java.util.Date;

import lombok.Data;

@Data
public class BookRoom {
    
    private Integer roomNumber;
    private String checkInDate;
    private String checkOutDate;
}
