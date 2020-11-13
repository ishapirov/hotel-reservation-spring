package com.ishapirov.hotelreservation.controllers.user;

import com.ishapirov.hotelapi.generalexceptions.NotImplementedException;
import com.ishapirov.hotelapi.pagination.HotelPage;
import com.ishapirov.hotelapi.services.user.UserService;
import com.ishapirov.hotelapi.services.user.domain.UserInformation;
import com.ishapirov.hotelapi.services.user.domain.UserSignupInformation;
import com.ishapirov.hotelapi.services.user.domain.UserUpdate;
import com.ishapirov.hotelapi.services.user.paramvalidation.UsersCriteria;
import com.ishapirov.hotelreservation.domain.User;
import com.ishapirov.hotelreservation.mapper.DomainToApiMapper;
import com.ishapirov.hotelreservation.retrievefilter.RoleUtil;
import com.ishapirov.hotelreservation.retrievefilter.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import javax.validation.Valid;


@RestController
@Validated
public class UserController implements UserService {
    @Autowired
    private UserUtil userUtil;
    @Autowired
    private RoleUtil roleUtil;
    @Autowired
    private DomainToApiMapper domainToApiMapper;

    @Override
    public HotelPage<UserInformation> getUsers(UsersCriteria usersCriteria) {
        Pageable pageable = PageRequest.of(usersCriteria.getPageNumber(), usersCriteria.getSize(), Sort.by("userID"));
        return domainToApiMapper.getUserInformationPage(userUtil.getUsers(pageable),pageable);
    }

    @Override
    @Transactional
    public UserInformation newUser(UserSignupInformation userSignupInformation) {
        User user = userUtil.createNewUser(userSignupInformation);
        return domainToApiMapper.getUserInformation(user);
    }

    @Override
    public UserInformation getUser(String username) {
        User user = userUtil.getUserByUsername(username);
        return domainToApiMapper.getUserInformation(user);
    }

    @Override
    public UserInformation updateUser(String username, UserUpdate userUpdate) {
        User user = userUtil.updateUser(username,userUpdate);
        return domainToApiMapper.getUserInformation(user);
    }

    @Override
    public void deleteUser(String username) {
        throw new NotImplementedException("This operation is not yet implemented");
    }

    @Override
    public UserInformation newAdmin(@Valid UserSignupInformation userSignupInformation) {
        User user = userUtil.createNewAdmin(userSignupInformation);
        return domainToApiMapper.getUserInformation(user);
    }
}
