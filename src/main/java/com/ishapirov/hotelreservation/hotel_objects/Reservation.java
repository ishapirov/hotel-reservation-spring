package com.ishapirov.hotelreservation.hotel_objects;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Entity
@Table(name = "reservation")
public class Reservation implements Serializable{
    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private Integer registrationNumber;
    @ManyToOne
    @JoinColumn(name="c_id",referencedColumnName = "customer_id")
    private Customer customer;
    @ManyToOne
    @JoinColumn(name="r_num",referencedColumnName = "room_number")
    private Room room;
}
