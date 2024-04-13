package com.bot.springboottwitchbot.services;

import com.bot.springboottwitchbot.models.User;
import com.bot.springboottwitchbot.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

@Service
@Transactional(readOnly = true)
public class UsersService {
    private final UsersRepository usersRepository;

    @Autowired
    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public User findOne(String login) {
        User user = new User();
        user = usersRepository.findUserByLoginIgnoreCase(login);
        return user;
    }

    @Transactional
    public void save(User user) throws ParseException {
        usersRepository.save(user);
    }

    public ArrayList<User> findAllUsersWithDOBIsToday() {
        ArrayList<User> allUsersWithDOBNotNull = usersRepository.findUsersByDateOfBirthIsNotNull();
        Date todayDate = new Date();
        ArrayList<User> usersWithTodayDOB = new ArrayList<>();
        for (User user : allUsersWithDOBNotNull) {
            if ( (user.getDateOfBirth().getDate() == todayDate.getDate()) && (user.getDateOfBirth().getMonth() == todayDate.getMonth()) ) {
                usersWithTodayDOB.add(user);
            }
        }
        return usersWithTodayDOB;
    }


}
