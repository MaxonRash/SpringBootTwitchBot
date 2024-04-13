package com.bot.springboottwitchbot.repositories;

import com.bot.springboottwitchbot.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface UsersRepository extends JpaRepository<User, Integer> {
    User findUserByLoginIgnoreCase(String login);

    ArrayList<User> findUsersByDateOfBirthIsNotNull();

}
