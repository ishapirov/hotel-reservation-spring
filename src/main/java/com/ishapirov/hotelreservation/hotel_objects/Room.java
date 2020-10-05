package com.ishapirov.hotelreservation.hotel_objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Getter;

@Entity
@Table(name = "room")
@Getter
// @NamedQuery(name = "findAvailableRooms", query = "SELECT * FROM ")
public class Room {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="room_number")
	private final Integer roomNumber;
	
	@ManyToOne
	@JoinColumn(name="room_type")
	private final RoomType roomType;

	private double room_price;

	public Room(int roomNumber, RoomType typeOfRoom, double room_price){
		this.roomNumber = roomNumber;
		this.roomType = typeOfRoom;
		this.room_price = room_price;	
	}

}