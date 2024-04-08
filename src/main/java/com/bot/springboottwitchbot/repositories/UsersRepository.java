package com.bot.springboottwitchbot.repositories;

import com.bot.springboottwitchbot.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<User, Integer> {
    public User findUserByLoginIgnoreCase(String login);
}
