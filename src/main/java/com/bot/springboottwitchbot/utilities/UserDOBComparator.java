package com.bot.springboottwitchbot.utilities;

import com.bot.springboottwitchbot.models.User;

import java.util.Comparator;

public class UserDOBComparator implements Comparator<User> {
    @Override
    public int compare(User o1, User o2) {
        if (o1.getDateOfBirth() == null)
            return 100;
        if (o2.getDateOfBirth() == null)
            return -100;
        return o1.getDateOfBirth().getMonth() - o2.getDateOfBirth().getMonth();
    }
}
