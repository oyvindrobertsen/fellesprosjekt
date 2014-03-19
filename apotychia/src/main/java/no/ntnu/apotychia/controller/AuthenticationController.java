package no.ntnu.apotychia.controller;

import no.ntnu.apotychia.model.Group;
import no.ntnu.apotychia.model.Participant;
import no.ntnu.apotychia.model.User;
import no.ntnu.apotychia.service.GroupService;
import no.ntnu.apotychia.service.UserService;
import no.ntnu.apotychia.service.security.ApotychiaUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/api/auth")
public class AuthenticationController {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    UserService userService;
    @Autowired
    GroupService groupService;

    @RequestMapping(method = RequestMethod.POST, value="/register")
    public String registerUser(@ModelAttribute User user, ModelMap model) {
        logger.info(user.toString());
        user.setPasswordAndEncode(user.getEncodedPassword());
        this.userService.addNewUser(user);
        return "redirect:/login";
    }

    @RequestMapping(method = RequestMethod.GET, value="/me")
    public ResponseEntity<User> getCurrentUser() {
        ApotychiaUserDetails apotychiaUserDetails =
                (ApotychiaUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userService.findByUsername(apotychiaUserDetails.getUsername());
        currentUser.setPassword(null);
        return new ResponseEntity<User>(currentUser, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value="/users")
    public ResponseEntity<List<Participant>> getAllUsersAndGroups() {
        List<Participant> ret = new ArrayList<Participant>();
        ret.addAll(userService.findAllUsers());
        ret.addAll(groupService.getAllGroups());
        return new ResponseEntity<List<Participant>>(ret, HttpStatus.OK);
    }
}