package com.azry.library_management_system.security.service;

import com.azry.library_management_system.dto.AuthResponseDTO;
import com.azry.library_management_system.exception.AuthenticationFailedException;
import com.azry.library_management_system.exception.UserRoleNotFoundException;
import com.azry.library_management_system.model.ApplicationUser;
import com.azry.library_management_system.model.Role;
import com.azry.library_management_system.repository.RoleRepository;
import com.azry.library_management_system.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
@Transactional
public class AuthService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final TokenService tokenService;

    @Autowired
    public AuthService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            TokenService tokenService
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    public ApplicationUser registerUser(String username, String password) {

        String encodedPassword = passwordEncoder.encode(password);

        Role userRole = roleRepository.findByAuthority("USER").orElseThrow(()-> new UserRoleNotFoundException("Role not found"));
        Set<Role> authorities = new HashSet<>();
        authorities.add(userRole);

        ApplicationUser applicationUser = new ApplicationUser(null, username, encodedPassword, null, authorities);

        return userRepository.save(applicationUser);
    }

    public AuthResponseDTO loginUser(String username, String password) {

        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            String token = tokenService.generateJwt(auth);

            return new AuthResponseDTO(token);

        } catch (AuthenticationException e) {
            log.error("Authentication failed for user: {}", username, e);
            throw new AuthenticationFailedException("Invalid username or password");
        }
    }

}