package com.daurenassanbaev.firstpetproject.mapper;

import com.daurenassanbaev.firstpetproject.database.entity.User;
import com.daurenassanbaev.firstpetproject.database.repository.UserRepository;
import com.daurenassanbaev.firstpetproject.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserMapper implements Mapper<UserDto, User> {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User map(UserDto object) {
        User user = new User();
        copy(object, user);
        return user;
    }

    private void copy(UserDto object, User user) {
        user.setUsername(object.getUsername());
        Optional.ofNullable(object.getRawPassword()).filter(StringUtils::hasText).map(passwordEncoder::encode).ifPresent(user::setPassword);
    }
}
