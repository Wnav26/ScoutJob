package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User handleSaveUser(User user) {
        User wnav = this.userRepository.save(user);
        return wnav;
    }

    public User handleUpdateUser(User reqUser) {
        User wnav = this.getUserById(reqUser.getId());
        if (wnav != null) {
            wnav.setName(reqUser.getName());
            wnav.setEmail(reqUser.getEmail());
            wnav.setPassword(reqUser.getPassword());
            wnav = this.userRepository.save(wnav);
        }
        return wnav;
    }

    public void deleteUserById(long id) {

        this.userRepository.deleteById(id);
    }

    public List<User> getAllUsers() {

        return this.userRepository.findAll();
    }

    public User getUserById(long id) {
        Optional<User> userOptional = this.userRepository.findById(id);
        if (userOptional.isPresent()) {
            return userOptional.get();
        }
        return null;
    }

    public User handelGetUserByUsername(String name) {
        return this.userRepository.findByEmail(name);
    }
}
