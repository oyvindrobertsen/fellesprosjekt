package no.ntnu.apotychia.controller;

import no.ntnu.apotychia.model.User;
import no.ntnu.apotychia.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthenticationController {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET, value="/register.html")
    public String register() {
        return "register";
    }

    @RequestMapping(method = RequestMethod.POST, value="/register")
    public String registerUser(@ModelAttribute("user") User user, ModelMap model) {
        user.setPasswordAndEncode(user.getEncodedPassword());
        this.userService.addNewUser(user);
        return "redirect:login";
    }
}