package com.daurenassanbaev.firstpetproject.database.repository;

import com.daurenassanbaev.firstpetproject.database.entity.User;

public interface CustomUserRepository {
    User saveUser(User user);
}
