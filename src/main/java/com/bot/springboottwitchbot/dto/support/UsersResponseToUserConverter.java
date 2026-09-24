package com.bot.springboottwitchbot.dto.support;

import com.bot.springboottwitchbot.dto.user.UsersResponse;
import com.bot.springboottwitchbot.models.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UsersResponseToUserConverter {
    public static User ConvertUserFromDTO(UsersResponse userDTO) throws ParseException {
        User user = new User();
        user.setTwitchId(Integer.parseInt(userDTO.getData().get(0).getId()));
        user.setLogin(userDTO.getData().get(0).getLogin());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date date = simpleDateFormat.parse(userDTO.getData().get(0).getCreated_at());
        user.setCreatedAt(date);
        return user;
    }
}
