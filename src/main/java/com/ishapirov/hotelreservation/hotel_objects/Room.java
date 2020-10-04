package com.ishapirov.hotelreservation.hotel_objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;

@Entity
@Table(name = "room")
@Getter
public class Room {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="room_number")
	private final Integer roomNumber;
	
	@ManyToOne
	@JoinColumn(name="room_type",referencedColumnName = "name")
	private final RoomType roomType;

	public Room(int roomNumber, RoomType typeOfRoom){
		this.roomNumber = roomNumber;
		this.roomType = typeOfRoom;
	}

}