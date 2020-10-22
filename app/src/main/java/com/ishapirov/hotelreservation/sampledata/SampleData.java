package com.ishapirov.hotelreservation.sampledata;

import com.ishapirov.hotelreservation.domain.Room;
import com.ishapirov.hotelreservation.domain.RoomType;
import com.ishapirov.hotelreservation.repositories.RoleRepository;
import com.ishapirov.hotelreservation.repositories.RoomRepository;
import com.ishapirov.hotelreservation.repositories.RoomTypeRepository;

import com.ishapirov.hotelreservation.domain.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class SampleData {
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private RoomTypeRepository roomTypeRepository;
    @Autowired
    private RoleRepository roleRepository;

//    @EventListener
    public void appReady(ApplicationReadyEvent event){
        Role admin = new Role();
        admin.setName("ROLE_ADMIN");
        Role user = new Role();
        user.setName("ROLE_USER");
        roleRepository.save(admin);
        roleRepository.save(user);

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
            Room room = new Room(i,singleType, (double) (Math.round((Math.random()*100+120)*100)/100));
            roomRepository.save(room);
        }
        for(int i=25;i<50;i++){
            Room room = new Room(i,doubleType, (double) (Math.round((Math.random()*120+180)*100)/100));
            roomRepository.save(room);
        }
        for(int i=50;i<75;i++){
            Room room = new Room(i,suiteType, (double) (Math.round((Math.random()*200+350)*100)/100));
            roomRepository.save(room);
        }
        for(int i=75;i<100;i++){
            Room room = new Room(i,penthouseType, (double) (Math.round((Math.random()*400+600)*100)/100));
            roomRepository.save(room);
        }
    }
}
