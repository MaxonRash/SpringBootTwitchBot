package com.bot.springboottwitchbot.utilities;

import com.bot.springboottwitchbot.models.User;

import java.util.Comparator;

public class UserDOBComparator implements Comparator<User> {
    @Override
    public int compare(User o1, User o2) {
        return o1.getDateOfBirth().getMonth() - o2.getDateOfBirth().getMonth();
    }
}
