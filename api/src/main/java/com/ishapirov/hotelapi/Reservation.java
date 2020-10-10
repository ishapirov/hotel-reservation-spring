package com.ishapirov.hotelapi;

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
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reservation")
@EqualsAndHashCode(callSuper = true)
public class Reservation extends DatesInterval implements Serializable{
    private static final long serialVersionUID = -5960526503459035821L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private Integer reservationNumber;
    @ManyToOne
    @JoinColumn(name="c_id")
    private Customer customer;
    @ManyToOne
    @JoinColumn(name="r_num")
    private Room room;

}
