package com.sharsheev.ewarehouse.service.impl;

import com.sharsheev.ewarehouse.model.Order;
import com.sharsheev.ewarehouse.model.User;
import com.sharsheev.ewarehouse.model.exceptions.InvalidUsernameOrPasswordException;
import com.sharsheev.ewarehouse.model.exceptions.PasswordsDoNotMatchException;
import com.sharsheev.ewarehouse.model.exceptions.UsernameAlreadyExistsException;
import com.sharsheev.ewarehouse.repository.UserRepository;
import com.sharsheev.ewarehouse.service.UserService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(s).get();
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(), Stream.of(new SimpleGrantedAuthority(user.getRole())).collect(Collectors.toList()));
        return userDetails;
    }

    @Override
    public User register(String username, String password, String repeatPassword, String role) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty())
            throw new InvalidUsernameOrPasswordException();
        if (!password.equals(repeatPassword))
            throw new PasswordsDoNotMatchException();
        if (this.userRepository.findByUsername(username).isPresent())
            throw new UsernameAlreadyExistsException(username);
        User user = new User(username, passwordEncoder.encode(password), role);
        return userRepository.save(user);
    }

    @Override
    public User login(String username, String password) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            throw new InvalidUsernameOrPasswordException();
        }
        return userRepository.findByUsernameAndPassword(username,
                password).orElseThrow(InvalidUsernameOrPasswordException::new);
    }

    @Override
    public User updateUser(String username, String password, String firstName, String lastName, String address, String email) {
        User user = userRepository.findByUsername(username).orElseThrow(InvalidUsernameOrPasswordException::new);
        if (passwordEncoder.matches(password, user.getPassword())) {
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setAddress(address);
            user.setEmail(email);
            user.setEmail(email);
            this.userRepository.save(user);
        }
        return user;
    }

    @Override
    public User adminUserUpdate(String username, String firstName, String lastName, String address, String email) {
        User user = userRepository.findByUsername(username).orElseThrow(InvalidUsernameOrPasswordException::new);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setAddress(address);
        user.setEmail(email);
        user.setEmail(email);
        this.userRepository.save(user);
        return user;
    }

    @Override
    public User getUser(String username) {
        return this.userRepository.findById(username).orElseThrow(InvalidUsernameOrPasswordException::new);
    }

    @Override
    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    @Override
    public User findUserById(String username) {
        return this.userRepository.findById(username).orElseThrow(InvalidUsernameOrPasswordException::new);
    }

    @Override
    public void deleteUser(String username) {
        this.userRepository.delete(this.userRepository.findById(username).orElseThrow(InvalidUsernameOrPasswordException::new));
    }

    @Override
    public List<Order> listAllUserOrders(String username) {
        User user = this.userRepository.findById(username).orElseThrow(InvalidUsernameOrPasswordException::new);
        return user.getOrders();
    }
}
