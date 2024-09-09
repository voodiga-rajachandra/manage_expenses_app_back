package com.daurenassanbaev.firstpetproject.service;

import com.daurenassanbaev.firstpetproject.database.repository.UserJdbcRepository;
import com.daurenassanbaev.firstpetproject.database.repository.UserRepository;
import com.daurenassanbaev.firstpetproject.dto.UserDto;
import com.daurenassanbaev.firstpetproject.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ModelMapper modelMapper;
    private final UserJdbcRepository userJdbcRepository;

    @Autowired
    public UserService(UserRepository userRepository,
                       UserMapper userMapper,
                       ModelMapper modelMapper,
                       @Lazy UserJdbcRepository userJdbcRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.modelMapper = modelMapper;
        this.userJdbcRepository = userJdbcRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Loading user by username {}", username);
        return userRepository.findByUsername(username)
                .map(user -> new User(
                        user.getUsername(),
                        user.getPassword(),
                        Collections.emptyList()
                )).orElseThrow(() -> {
                    log.warn("User not found with username {}", username);
                    return new UsernameNotFoundException("Failed to retrieve user: " + username);});
    }
    @Transactional
    public void create(UserDto userDto) {
        log.info("Creating user {}", userDto.getUsername());
        com.daurenassanbaev.firstpetproject.database.entity.User user = userMapper.map(userDto);
        try {
            userJdbcRepository.saveUser(user.getUsername(), user.getPassword());
            log.info("Created user {}", user.getUsername());
        } catch (Exception e) {
            log.error("Failed to create user {}: {}", user.getUsername(), e.getMessage());
            throw e;
        }
    }
    @Transactional
    public void delete(Integer id) {
        log.info("Deleting user {}", id);
        userRepository.deleteById(id);
    }

    public Optional<UserDto> findByUsername(String username) {
        log.info("Search user by username: {}", username);
        Optional<com.daurenassanbaev.firstpetproject.database.entity.User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            log.info("Found user: {}", user.get().getUsername());
            return Optional.of(modelMapper.map(user.get(), UserDto.class));
        } else {
            log.warn("User not found: {}", username);
            return Optional.empty();
        }
    }
}
