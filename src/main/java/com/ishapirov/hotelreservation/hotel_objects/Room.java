package com.ishapirov.hotelreservation.hotel_objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "room")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Room {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="room_number")
	private Integer roomNumber;
	
	@ManyToOne
	@JoinColumn(name="room_type")
	private RoomType roomType;

	private double roomPrice;

	public Room(int roomNumber, RoomType typeOfRoom, double roomPrice){
		this.roomNumber = roomNumber;
		this.roomType = typeOfRoom;
		this.roomPrice = roomPrice;	
	}

}