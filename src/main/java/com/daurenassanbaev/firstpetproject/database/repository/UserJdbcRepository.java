package com.daurenassanbaev.firstpetproject.database.repository;

import com.daurenassanbaev.firstpetproject.database.entity.Account;
import com.daurenassanbaev.firstpetproject.database.entity.User;
import com.daurenassanbaev.firstpetproject.service.AccountService;
import com.daurenassanbaev.firstpetproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class UserJdbcRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserService userService;

    public int saveUser(String username, String password) {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
        return jdbcTemplate.update(sql, username, password);
    }
    public User findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        User user = jdbcTemplate.queryForObject(sql, new Object[]{username}, new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                return user;
            }
        });
        List<Account> list = accountRepository.findByUserId(userService.findByUsername(username).get().getId());
        user.setAccounts(list);
        return user;
    }
}

