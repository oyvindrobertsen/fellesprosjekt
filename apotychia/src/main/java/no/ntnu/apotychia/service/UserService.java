package no.ntnu.apotychia.service;

import no.ntnu.apotychia.model.User;
import no.ntnu.apotychia.service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public User findByUsername(String username) {
          return userRepository.findOne(username);
    }
}
