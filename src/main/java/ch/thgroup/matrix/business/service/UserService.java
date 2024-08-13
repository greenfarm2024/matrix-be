package ch.thgroup.matrix.business.service;

import ch.thgroup.matrix.business.dto.UserDTO;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserDTO> fetchAllUsers();

    Optional<UserDTO> fetchUserById(Long userId);

    void addUser(UserDTO userDTO);

    Optional<UserDTO> updateUser(Long userId, UserDTO userDTO);

    void removeUser(Long userId);
}