package com.ishapirov.hotelreservation.retrievefilter;

import com.ishapirov.hotelapi.services.reservation.domain.CancelReservation;
import com.ishapirov.hotelapi.services.reservation.domain.ReservationUpdate;
import com.ishapirov.hotelapi.services.reservation.domain.admin.BookReservationForUser;
import com.ishapirov.hotelapi.services.reservation.exceptions.ReservationAlreadyCancelledException;
import com.ishapirov.hotelapi.services.reservation.exceptions.ReservationDeletionError;
import com.ishapirov.hotelapi.services.reservation.exceptions.ReservationNotFoundException;
import com.ishapirov.hotelapi.services.reservation.exceptions.ReservationUsernamesDoNotMatchException;
import com.ishapirov.hotelreservation.domain.Reservation;
import com.ishapirov.hotelreservation.domain.Room;
import com.ishapirov.hotelreservation.domain.User;
import com.ishapirov.hotelreservation.repositories.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReservationUtil {
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private RoomUtil roomUtil;
    @Autowired
    private UserUtil userUtil;
    @Autowired
    private AuthenticationUtil authenticationUtil;

    public Reservation getReservationByReservationNumber(Integer reservationNumber){
        Optional<Reservation> reservation = reservationRepository.findByReservationNumber(reservationNumber);
        if(reservation.isEmpty())
            throw new ReservationNotFoundException("A reservation with the given reservation number was not found");
        if(!authenticationUtil.isAdmin()){
            if(!authenticationUtil.getUsernameOfCurrentUser().equals(reservation.get().getUser().getUserSecurity().getUsername()))
                throw new ReservationUsernamesDoNotMatchException("The username on the reservation does not match the username of the logged in user");
        }
        return reservation.get();
    }

    public Page<Reservation> getReservationsByUsername(String username, Pageable pageable){
        return reservationRepository.findAllByUser_Username(username,pageable);
    }

    public Reservation bookReservation(BookReservationForUser bookReservationForUser){
        User user = userUtil.getUserByUsername(bookReservationForUser.getUsername());
        Room room = roomUtil.getRoomByRoomNumberAndDates(bookReservationForUser.getRoomNumber(),bookReservationForUser.getCheckInDate(),bookReservationForUser.getCheckOutDate());
        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setRoom(room);
        reservation.setCheckInDate(bookReservationForUser.getCheckInDate());
        reservation.setCheckOutDate(bookReservationForUser.getCheckOutDate());
        reservationRepository.save(reservation);
        return reservation;
    }

    public Reservation updateReservation(Integer reservationNumber, ReservationUpdate reservationUpdate){
        Reservation reservation = getReservationByReservationNumber(reservationNumber);
        Room room = roomUtil.getRoomExcludingReservation(reservationUpdate.getRoomNumber(),reservationUpdate.getCheckInDate(),reservationUpdate.getCheckOutDate(),reservationNumber);
        reservation.setRoom(room);
        reservation.setCheckInDate(reservationUpdate.getCheckInDate());
        reservation.setCheckOutDate(reservationUpdate.getCheckOutDate());
        reservationRepository.save(reservation);
        return reservation;
    }


    public void deleteReservation(Integer reservationNumber){
        Reservation reservation = getReservationByReservationNumber(reservationNumber);
        reservationRepository.delete(reservation);
    }

    public Reservation cancelReservation(CancelReservation cancelReservation){
        Reservation reservation = getReservationByReservationNumber(cancelReservation.getReservationNumber());
        String username = authenticationUtil.getUsernameOfCurrentUser();
        if(!reservation.getUser().getUsername().equals(username))
            throw new ReservationUsernamesDoNotMatchException("The username on the reservation does not match the username of the logged in user");
        setCancelledToTrue(reservation);
        return reservation;
    }

    public Reservation cancelReservationForUser(CancelReservation cancelReservation){
        Reservation reservation = getReservationByReservationNumber(cancelReservation.getReservationNumber());
        setCancelledToTrue(reservation);
        return reservation;
    }

    private void setCancelledToTrue(Reservation reservation){
        if(reservation.isCancelled())
            throw new ReservationAlreadyCancelledException("This reservation has already been cancelled");
        reservation.setCancelled(true);
        reservationRepository.save(reservation);
    }

}
