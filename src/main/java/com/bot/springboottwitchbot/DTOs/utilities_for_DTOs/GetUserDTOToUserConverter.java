package com.bot.springboottwitchbot.DTOs.utilities_for_DTOs;

import com.bot.springboottwitchbot.DTOs.get_user_DTOs.GetUserDTO;
import com.bot.springboottwitchbot.models.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GetUserDTOToUserConverter {
    public static User ConvertUserFromDTO(GetUserDTO userDTO) throws ParseException {
        User user = new User();
        user.setTwitchId(Integer.parseInt(userDTO.getData().get(0).getId()));
        user.setLogin(userDTO.getData().get(0).getLogin());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date date = simpleDateFormat.parse(userDTO.getData().get(0).getCreated_at());
        user.setCreatedAt(date);
        return user;
    }
}
