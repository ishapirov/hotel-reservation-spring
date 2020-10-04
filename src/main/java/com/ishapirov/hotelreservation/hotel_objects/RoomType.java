package com.ishapirov.hotelreservation.hotel_objects;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "roomtype")
public class RoomType implements Serializable{
    @Id
    private String name;
    private double price;

}