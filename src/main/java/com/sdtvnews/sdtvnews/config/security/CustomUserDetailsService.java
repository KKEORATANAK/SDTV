package com.sdtvnews.sdtvnews.config.security;


import com.sdtvnews.sdtvnews.config.EncryptionUtil;
import com.sdtvnews.sdtvnews.config.RoleConstants;
import com.sdtvnews.sdtvnews.entity.User;
import com.sdtvnews.sdtvnews.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EncryptionUtil encryptionUtil; // Inject the EncryptionUtil

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserNameLogin(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        // Map integer roles to GrantedAuthority
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (user.getRoleId() == 2) { // Assuming 2 is USER
            authorities.add(new SimpleGrantedAuthority(RoleConstants.ROLE_USER));
        } else if (user.getRoleId() == 1) { // Assuming 1 is ADMIN
            authorities.add(new SimpleGrantedAuthority(RoleConstants.ROLE_ADMIN));
        }

        // Return CustomUserDetails including user ID
        return new CustomUserDetails(user.getId(), user.getUserName(), user.getPassWord(), authorities);
    }

}
