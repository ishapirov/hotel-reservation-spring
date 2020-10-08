package com.ishapirov.hotelreservation.controller.classes;

import java.util.Date;

import lombok.Data;

@Data
public class BookRoom {
    
    private Integer roomNumber;
    private Date checkInDate;
    private Date checkOutDate;
}
