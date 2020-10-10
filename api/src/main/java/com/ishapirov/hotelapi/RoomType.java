package com.ishapirov.hotelapi;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roomtype")
public class RoomType implements Serializable{

    private static final long serialVersionUID = -2584875412146575093L;
    @Id
    private String name;
    @Column(name = "default_price")
    private double defaultPrice;
}