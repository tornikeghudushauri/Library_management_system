package com.azry.library_management_system.service.impl;

import com.azry.library_management_system.dto.ApplicationUserDTO;
import com.azry.library_management_system.exception.UserNotFoundException;
import com.azry.library_management_system.exception.UserRoleNotFoundException;
import com.azry.library_management_system.model.ApplicationUser;
import com.azry.library_management_system.model.Role;
import com.azry.library_management_system.repository.RoleRepository;
import com.azry.library_management_system.repository.UserRepository;
import com.azry.library_management_system.service.ApplicationUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ApplicationUserServiceImpl implements ApplicationUserService, UserDetailsService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;


    @Autowired
    public ApplicationUserServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepository, RoleRepository roleRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public List<ApplicationUserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(ApplicationUserDTO::new).toList();
    }

    @Override
    public ApplicationUserDTO getUserById(Long id) {
        ApplicationUser applicationUser = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));

        return new ApplicationUserDTO(applicationUser);
    }

    @Override
    public ApplicationUserDTO addUser(ApplicationUserDTO userDTO) {
        Role userRole = roleRepository.findByAuthority("USER").orElseThrow(()-> new UserRoleNotFoundException("USER Role not found"));
        Set<Role> authorities = new HashSet<>();
        authorities.add(userRole);

        ApplicationUser applicationUser = new ApplicationUser(userDTO.getUsername(), passwordEncoder.encode(userDTO.getPassword()));
        applicationUser.setAuthorities(authorities);

        return new ApplicationUserDTO(userRepository.save(applicationUser));
    }

    @Override
    public ApplicationUserDTO updateUser(Long id, ApplicationUserDTO userDTO) {
        ApplicationUser applicationUser = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        applicationUser.setUsername(userDTO.getUsername());
        applicationUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        return new ApplicationUserDTO(userRepository.save(applicationUser));
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found"));
    }
}