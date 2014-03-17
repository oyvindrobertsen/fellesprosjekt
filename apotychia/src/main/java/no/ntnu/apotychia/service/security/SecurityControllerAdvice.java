package no.ntnu.apotychia.service.security;

import no.ntnu.apotychia.model.User;
import no.ntnu.apotychia.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class SecurityControllerAdvice {

    @Autowired
    UserService userService;

    @ModelAttribute
    public User currentUser(Authentication authentication) {
        if (authentication == null) {
            return null;
        }
        ApotychiaUserDetails apotychiaUserDetails = (ApotychiaUserDetails) authentication.getPrincipal();
        String username = apotychiaUserDetails.getUsername();
        return userService.findByUsername(username);
    }
}
