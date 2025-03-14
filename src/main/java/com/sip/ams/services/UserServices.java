package com.sip.ams.services;

import com.sip.ams.entities.Role;
import com.sip.ams.entities.User;
import com.sip.ams.repositories.RoleRepository;
import com.sip.ams.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;

@Service("userService")
public class UserServices {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServices(
            UserRepository userRepository,
            RoleRepository roleRepository,
            BCryptPasswordEncoder bCryptPasswordEncoder
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(0);

        Role role = roleRepository.findByRole("USER");
        user.setRoles(new HashSet<Role>(Collections.singletonList(role)));

        userRepository.save(user);
    }

}
