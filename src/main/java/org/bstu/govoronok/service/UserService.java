package org.bstu.govoronok.service;

import org.bstu.govoronok.model.Role;
import org.bstu.govoronok.model.User;
import org.bstu.govoronok.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JavaMailSender emailSender;
    private JedisPool jedisPool;


    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JavaMailSender emailSender, JedisPool jedisPool){
        this.jedisPool = jedisPool;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.emailSender = emailSender;
    }

    public void addNewUser(User user){
        UUID code = UUID.randomUUID();
        try(Jedis jedis = jedisPool.getResource()){
            jedis.set(code.toString() ,user.getEmail());
            jedis.expire(code.toString(), 86400);
            sendConfirmMessageToUserByEmail(user.getEmail(), code);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
        }
    }

    public void confirmUser(UUID code){
        try(Jedis jedis = jedisPool.getResource()){
            String email = jedis.get(code.toString());
            User user = findByUsername(email);
            if(user!=null){
                user.setApproved(Boolean.TRUE);
                userRepository.save(user);
                jedis.del(code.toString());
            }
            else{
                System.out.println("USER NOT FOUND");
            }
        }
    }

    public void resetPasswordForUserByEmail(String email){
        UUID code = UUID.randomUUID();
        try(Jedis jedis = jedisPool.getResource()){
            jedis.set(code.toString() , email);
            jedis.expire(code.toString(), 3600);
            sendMessageForResetPasswordToUserByEmail(email, code);
        }
    }

    public Optional<User> getUserById(Long id){
        return userRepository.findById(id);
    }

    public void deleteUserById(Long id){
        userRepository.deleteById(id);
    }

    public void setNewPasswordWithCode(UUID code, String password){
        try(Jedis jedis = jedisPool.getResource()){
            String email = jedis.get(code.toString());
            User user = findByUsername(email);
            if(user!=null){
                user.setPassword(passwordEncoder.encode(password));
                userRepository.save(user);
                jedis.del(code.toString());
            }
            else{
                System.out.println("USER NOT FOUND");
            }
        }
    }

    public User findByUsername(String username){
        return userRepository.getUserByEmail(username);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = findByUsername(email);
        if(user == null){
            throw new UsernameNotFoundException(String.format("User with email '%s' not found", email));
        }
        List<Role> roleList = new ArrayList<>();
        roleList.add(user.getRole());

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), mapRolesToAuthorities(roleList));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles){
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getRole())).collect(Collectors.toList());
    }

    public void sendConfirmMessageToUserByEmail(String email, UUID code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("hovor007@gmail.com");
        message.setTo(email);
        message.setSubject("Confirm registration");
        message.setText("Follow this link to continue registration: http://localhost:8087/authentication/confirm/" +
                        code);
        emailSender.send(message);
    }

    public void sendMessageForResetPasswordToUserByEmail(String email, UUID code) {
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

}
