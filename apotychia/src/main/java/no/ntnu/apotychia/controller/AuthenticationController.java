package no.ntnu.apotychia.controller;

import no.ntnu.apotychia.model.User;
import no.ntnu.apotychia.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/api/auth")
public class AuthenticationController {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.POST, value="/register")
    public String registerUser(@ModelAttribute User user, ModelMap model) {
        logger.info(user.toString());
        user.setPasswordAndEncode(user.getEncodedPassword());
        this.userService.addNewUser(user);
        return "redirect:/login";
    }

    @RequestMapping(method = RequestMethod.GET, value="/me")
    public ResponseEntity<User> getCurrentUser() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        user.setPassword(null);
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }
}