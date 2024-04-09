package com.bot.springboottwitchbot.services;

import com.bot.springboottwitchbot.DTOs.get_user_DTOs.GetUserDTO;
import com.bot.springboottwitchbot.DTOs.utilities_for_DTOs.GetUserDTOToUserConverter;
import com.bot.springboottwitchbot.models.User;
import com.bot.springboottwitchbot.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UsersService {
    private final UsersRepository usersRepository;

    @Autowired
    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public User findOne(String login) {
//        Optional<User> user = Optional.of(new User());
        User user = new User();
//        user = usersRepository.findById(id);
        user = usersRepository.findUserByLoginIgnoreCase(login);
        System.out.println(user.getLogin());
        return user;
    }

    @Transactional
    public void save(User user) throws ParseException {
//        user.setTwitchId(Integer.parseInt(userDTO.getData().get(0).getId()));
//        user.setLogin(userDTO.getData().get(0).getLogin());
//        user.setCreatedAt(null);
//        user.setFollowingSince(null);
//        user.setDateOfBirth(null);


//        user.setDateOfBirth(new Date(100, Calendar.AUGUST, 12));
        System.out.println(user);
        usersRepository.save(user);
    }


}
