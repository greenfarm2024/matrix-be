package ch.thgroup.matrix.business.admin.service;

import ch.thgroup.matrix.business.admin.dto.UserDTO;

public interface UserService {

    UserDTO register(UserDTO registrationRequest);

    UserDTO login(UserDTO loginRequest);

    UserDTO refreshToken(UserDTO refreshTokenRequest);

    UserDTO getAllUsers();

    UserDTO getUsersById(Long userId);

    UserDTO deleteUser(Long userId);

    UserDTO updateUser(UserDTO updatedUser);

    UserDTO getMyInfo(String userName);
}
