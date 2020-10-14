package com.ishapirov.hotelreservation.hotelclasses;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "room")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Room {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="room_number")
	private Integer roomNumber;
	
	@ManyToOne
	@JoinColumn(name="room_type")
	private RoomType roomType;

	private Double roomPrice;

	public Double getPrice(){
		if(this.roomPrice == null){
			return roomType.getDefaultPrice();
		}
		return roomPrice;
	}


}