package no.ntnu.apotychia.service;

import no.ntnu.apotychia.model.User;
import no.ntnu.apotychia.service.repository.UserRepository;
import no.ntnu.apotychia.service.security.ApotychiaUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public User findByUsername(String username) {
          return userRepository.findOne(username);
    }

    public void addNewUser(User user) {
        userRepository.insert(user);
    }
}
