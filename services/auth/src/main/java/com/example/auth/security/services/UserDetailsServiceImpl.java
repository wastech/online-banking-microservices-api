package com.example.auth.security.services;

import com.example.auth.entity.User;
import com.example.auth.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    @Cacheable(value = "userDetails", key = "#username", unless = "#result == null")
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Loading user details from database for username: {}", username);
        User user = userRepository.findByUserName(username)
            .orElseThrow(() -> {
                logger.warn("User not found with username: {}", username);
                return new UsernameNotFoundException("User Not Found with username: " + username);
            });

        logger.info("User found, building UserDetails object for: {}", username);
        return UserDetailsImpl.build(user);
    }
}