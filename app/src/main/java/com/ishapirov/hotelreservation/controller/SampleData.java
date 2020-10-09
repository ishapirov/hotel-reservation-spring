package com.ishapirov.hotelreservation.controller;

import com.ishapirov.hotelreservation.hotel_objects.Room;
import com.ishapirov.hotelreservation.hotel_objects.RoomType;
import com.ishapirov.hotelreservation.repositories.RoomRepository;
import com.ishapirov.hotelreservation.repositories.RoomTypeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

@Controller
public class SampleData {
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private RoomTypeRepository roomTypeRepository;

    // @EventListener
    public void appReady(ApplicationReadyEvent event){
        RoomType singleType = new RoomType("Single",180.00);
        RoomType doubleType = new RoomType("Double",220.00);
        RoomType suiteType = new RoomType("Suite",380.00);
        RoomType penthouseType = new RoomType("Penthouse",700.00);
        roomTypeRepository.save(singleType);
        roomTypeRepository.save(doubleType);
        roomTypeRepository.save(suiteType);
        roomTypeRepository.save(penthouseType);

        //Creating 100 rooms (25 of each type for now)
        for(int i=0;i<25;i++){
            Room room = new Room(i,singleType,Math.round((Math.random()*100+120)*100)/100);
            roomRepository.save(room);
        }
        for(int i=25;i<50;i++){
            Room room = new Room(i,doubleType,Math.round((Math.random()*120+180)*100)/100);
            roomRepository.save(room);
        }
        for(int i=50;i<75;i++){
            Room room = new Room(i,suiteType,Math.round((Math.random()*200+350)*100)/100);
            roomRepository.save(room);
        }
        for(int i=75;i<100;i++){
            Room room = new Room(i,penthouseType,Math.round((Math.random()*400+600)*100)/100);
            roomRepository.save(room);
        }
    }
}
