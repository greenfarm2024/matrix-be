package ch.thgroup.matrix.business.service;

import ch.thgroup.matrix.business.dto.UserDTO;
import ch.thgroup.matrix.business.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserDTO> getAllUsers();

    UserDTO getUserById(long userId);

    void createUser(UserDTO userDTO);

    UserDTO updateUser(Long userId, UserDTO userDTO);

    void deleteUser(Long userId);
}