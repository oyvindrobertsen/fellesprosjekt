package no.ntnu.apotychia.service.security;


import no.ntnu.apotychia.service.UserService;
import no.ntnu.apotychia.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component("userDetailsService")
public class ApotychiaUserDetailsService implements UserDetailsService {

    private UserService userService;

    @Inject
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);
        return new ApotychiaUserDetails(user);
    }
}
