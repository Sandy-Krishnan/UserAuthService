package com.sandy.userauthservice.services;

import com.sandy.userauthservice.dtos.UserToken;
import com.sandy.userauthservice.exception.IncorrectPasswordException;
import com.sandy.userauthservice.exception.UserAlreadyNotExistException;
import com.sandy.userauthservice.exception.UserNotRegisteredException;
import com.sandy.userauthservice.model.Role;
import com.sandy.userauthservice.model.Session;
import com.sandy.userauthservice.model.State;
import com.sandy.userauthservice.model.User;
import com.sandy.userauthservice.repositories.RoleRepository;
import com.sandy.userauthservice.repositories.SessionRepository;
import com.sandy.userauthservice.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService implements IAuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private SecretKey secretKey;

    @Override
    public User signUp(String name, String email, String password) {
        //uniqueness
        Role role = null;
        Optional<User> optionalUser  = userRepository.findByEmail(email);

        if(optionalUser.isPresent()) {
            throw new UserAlreadyNotExistException("User with this email " + email + " already exist");
        }
        //create user
        User user = new User();
        user.setEmail(email);
        user.setUserName(name);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setCreatedAt(System.currentTimeMillis());
        user.setUpdateAt(System.currentTimeMillis());
        user.setState(State.ACTIVE);

        //assign roles

        Optional<Role> optionalRole = roleRepository.findByValue("DEFAULT");

        if(optionalRole.isEmpty()) {
            role = new Role();
            role.setValue("DEFAULT");
            role.setCreatedAt(System.currentTimeMillis());
            role.setUpdateAt(System.currentTimeMillis());
            role.setState(State.ACTIVE);
            roleRepository.save(role);
        } else {
            role = optionalRole.get();
        }

        List<Role> roles = List.of(role);

        user.setRoles(roles);

        return userRepository.save(user);
    }

    @Override
    public UserToken login(String email, String password) {

        Optional<User> optionalUser = userRepository.findByEmail(email);

        if(optionalUser.isEmpty() ) {
            throw new UserNotRegisteredException("Uer with email " + email + " does not exist");
        }

        User user = optionalUser.get();

        if(!bCryptPasswordEncoder.matches(password, user.getPassword())) {
            throw new IncorrectPasswordException("Invalid credentials");
        }

        Map<String,Object> payload = new HashMap<>();
        Long nowInMillis = System.currentTimeMillis(); // gets us timestamp in epoch
        System.out.println("nowInMillis = " + nowInMillis);
        payload.put("iat",nowInMillis); //Jan 16 58392
        payload.put("exp",nowInMillis+100000); //100 seconds
        payload.put("userId",user.getId());
        payload.put("iss","scaler");
        payload.put("scope",user.getRoles()); // payload

        /*MacAlgorithm macAlgorithm = Jwts.SIG.HS256; //header
        SecretKey secretKey = macAlgorithm.key().build();//secret*/

        String token = Jwts.builder()
                .claims(payload)
                .signWith(secretKey)
                .compact();//signed with

        Session session = new Session();
        session.setToken(token);
        session.setUser(user);
        session.setState(State.ACTIVE);
        sessionRepository.save(session);

        return new UserToken(user, token);
    }

    public Boolean validateToken(String token) {
        Optional<Session> optionalSession = sessionRepository.findByToken(token);

        if(optionalSession.isEmpty()) {
            return false;
        }

        JwtParser jwtParser =  Jwts.parser().verifyWith(secretKey).build();
        Claims claims = jwtParser.parseEncryptedClaims(token).getPayload();

        Long tokenExpiry = (Long) claims.get("exp");
        Long currentTime = System.currentTimeMillis();

        if(currentTime > tokenExpiry) {
            return false;
        } else {
            return true;
        }
    }
}
