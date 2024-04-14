package com.azry.library_management_system.service;

import com.azry.library_management_system.dto.ApplicationUserDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ApplicationUserService {
    List<ApplicationUserDTO> getAllUsers();
    ApplicationUserDTO getUserById(Long id);
    ApplicationUserDTO addUser(ApplicationUserDTO userDTO);
    ApplicationUserDTO updateUser(Long id, ApplicationUserDTO userDTO);
    void deleteUser(Long id);
}