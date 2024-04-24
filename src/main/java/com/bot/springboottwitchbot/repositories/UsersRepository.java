package com.bot.springboottwitchbot.repositories;

import com.bot.springboottwitchbot.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface UsersRepository extends JpaRepository<User, Integer> {
    User findUserByLoginIgnoreCase(String login);

    ArrayList<User> findUsersByDateOfBirthIsNotNull();

    Page<User> findByLoginContainingIgnoreCase(String login, Pageable pageable);

    /*@Query("UPDATE User u SET u.active = :active WHERE u.id = :id")
    @Modifying
    public void updateActiveStatus(Integer id, boolean active);*/

}
