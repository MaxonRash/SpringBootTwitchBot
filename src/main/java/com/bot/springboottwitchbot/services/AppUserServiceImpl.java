package com.bot.springboottwitchbot.services;

import com.bot.springboottwitchbot.models.AppUser;
import com.bot.springboottwitchbot.repositories.AppUsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class AppUserServiceImpl implements AppUserService{
    private AppUsersRepository usersRepository;
    @Autowired
    public AppUserServiceImpl(AppUsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

//    @Override
//    public List<AppUser> getAll() {
//        return usersRepository.findAll();
//    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AppUser> user = usersRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException
                    ("Invalid username or password.");
        }
        AppUser presentUser = user.get();
        return new org.springframework.security
                .core.userdetails.User(presentUser.getUsername(),
                presentUser.getPassword(),
//                mapRolesToAuthorities(user.getRoles()));
                Collections.emptyList());
    }

//    private Collection<? extends GrantedAuthority>
//    mapRolesToAuthorities(Collection<Role> roles) {
//
//        return roles.stream()
//                .map(role -> new SimpleGrantedAuthority
//                        (role.getName()))
//                .collect(Collectors.toList());
//    }
}
