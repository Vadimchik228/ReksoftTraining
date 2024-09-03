package com.rntgroup.web.security;

import com.rntgroup.database.entity.User;
import com.rntgroup.database.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(final String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.map(JwtEntityFactory::create).orElse(null);
    }

}
