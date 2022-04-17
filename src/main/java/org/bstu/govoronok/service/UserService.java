package org.bstu.govoronok.service;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.bstu.govoronok.model.Role;
import org.bstu.govoronok.model.User;
import org.bstu.govoronok.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender emailSender;
    private final JedisPool jedisPool;

    public User getUserById(Long id) {
        Optional<User> userById = Optional.ofNullable(userRepository.getUserById(id));
        return userById.orElse(null);
    }

    public void addNewUser(User user) {
        UUID code = UUID.randomUUID();
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.set(code.toString(), user.getEmail());
            jedis.expire(code.toString(), 86400);
            sendConfirmMessageToUserByEmail(user.getEmail(), code);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
        }
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }

    public void confirmUser(UUID code) {
        try (Jedis jedis = jedisPool.getResource()) {
            String email = jedis.get(code.toString());
            User user = findByUsername(email);
            if (user != null) {
                user.setApproved(Boolean.TRUE);
                userRepository.save(user);
                jedis.del(code.toString());
                logger.info("User confirmed");
            } else {
                logger.error("User not found");
            }
        }
    }

    public void resetPasswordForUserByEmail(String email) {
        logger.info("Attempt to reset password by email: {}", email);
        UUID code = UUID.randomUUID();
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.set(code.toString(), email);
            jedis.expire(code.toString(), 3600);
            sendMessageForResetPasswordToUserByEmail(email, code);
        }
    }

    public void setNewPasswordWithCode(UUID code, String password) {
        logger.info("Attempt to set new password");
        try (Jedis jedis = jedisPool.getResource()) {
            String email = jedis.get(code.toString());
            User user = findByUsername(email);
            if (user != null) {
                user.setPassword(passwordEncoder.encode(password));
                userRepository.save(user);
                jedis.del(code.toString());
            } else {
                logger.error("User not found");
            }
        }
    }

    public User findByUsername(String username) {
        return userRepository.getUserByEmail(username);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = findByUsername(email);
        List<Role> roleList = new ArrayList<>();
        if (user == null) {
            logger.error("User with email {} not found", email);
        } else {
            roleList.add(user.getRole());
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), mapRolesToAuthorities(roleList));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getRole())).collect(Collectors.toList());
    }

    public void sendConfirmMessageToUserByEmail(String email, UUID code) {
        logger.info("Sending email confirm message to {}", email);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("hovor007@gmail.com");
        message.setTo(email);
        message.setSubject("Confirm registration");
        message.setText("Follow this link to continue registration: http://localhost:8087/authentication/confirm/" +
                code);
        emailSender.send(message);
    }

    public void sendMessageForResetPasswordToUserByEmail(String email, UUID code) {
        logger.info("Sending email reset password message");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("hovor007@gmail.com");
        message.setTo(email);
        message.setSubject("Reset password");
        message.setText("Follow this link if you'd like to reset your password: http://localhost:8087/authentication/reset"
                + "\n Your code: " + code +
                "\n This code will expire in 1 hour" +
                "\n If you don't want to reset your password, please ignore this message and your password will not be changed");
        emailSender.send(message);
    }

    public void deposit(BigDecimal amount, Principal principal) {
        User user = userRepository.getUserByEmail(principal.getName());
        user.setBalance(user.getBalance().add(amount));
        userRepository.save(user);
    }
}
