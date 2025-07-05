package com.example.security;

import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(appUser -> new User(
                        appUser.getUsername(),
                        appUser.getPassword(),
                        Collections.singletonList(new SimpleGrantedAuthority(appUser.getRole().name()))
                ))
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé avec le nom : " + username));
    }
}