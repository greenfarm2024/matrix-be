package ch.thgroup.matrix.business.admin.service;

import ch.thgroup.matrix.business.admin.dto.UserDTO;
import ch.thgroup.matrix.business.admin.entity.UserEntity;
import ch.thgroup.matrix.business.admin.dto.UserMapper;
import ch.thgroup.matrix.business.admin.repo.UserRepository;
import ch.thgroup.matrix.business.common.JWTUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JWTUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDTO register(UserDTO registrationRequest) {
        UserDTO userDTO = new UserDTO();
        try {
            UserEntity userEntity = new UserEntity();
            userEntity.setUserName(registrationRequest.getUserName());
            userEntity.setFirstName(registrationRequest.getFirstName());
            userEntity.setLastName(registrationRequest.getLastName());
            userEntity.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            userEntity.setRole(registrationRequest.getRole());
            userEntity.setGroupe(registrationRequest.getGroupe());
            userEntity.setSex(registrationRequest.getSex());
            userEntity.setCreatedBy((short) 0);
            UserEntity userEntityResult = userRepository.save(userEntity);
            if (userEntityResult.getUserId() > 0) {
                userDTO.setUser(userEntityResult);
                userDTO.setMessage("User Saved Successfully");
                userDTO.setStatusCode(200);
            }
        } catch (Exception e) {
            userDTO.setStatusCode(500);
            userDTO.setError(e.getMessage());
        }
        return userDTO;
    }

    @Override
    public UserDTO login(UserDTO loginRequest) {
        UserDTO response = new UserDTO();
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword()));
            UserEntity userEntity = userRepository.findByUserName(loginRequest.getUserName())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            String jwt = jwtUtils.generateToken(userEntity);
            String refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), userEntity);
            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRole(userEntity.getRole());
            response.setRefreshToken(refreshToken);
            response.setExpirationTime("24Hrs");
            response.setMessage("Successfully Logged In");
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public UserDTO refreshToken(UserDTO refreshTokenRequest) {
        UserDTO response = new UserDTO();
        try {
            String userName = jwtUtils.extractUsername(refreshTokenRequest.getToken());
            UserEntity userEntity = userRepository.findByUserName(userName)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            if (jwtUtils.isTokenValid(refreshTokenRequest.getToken(), userEntity)) {
                String jwt = jwtUtils.generateToken(userEntity);
                response.setStatusCode(200);
                response.setToken(jwt);
                response.setRefreshToken(refreshTokenRequest.getToken());
                response.setExpirationTime("24Hr");
                response.setMessage("Successfully Refreshed Token");
            }
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public UserDTO getAllUsers() {
        UserDTO userDTO = new UserDTO();
        try {
            List<UserEntity> allUsers = userRepository.findAll();
            if (!allUsers.isEmpty()) {
                userDTO.setUserList(allUsers);
                userDTO.setStatusCode(200);
                userDTO.setMessage("Successful");
            } else {
                userDTO.setStatusCode(404);
                userDTO.setMessage("No users found");
            }
        } catch (Exception e) {
            userDTO.setStatusCode(500);
            userDTO.setMessage("Error occurred: " + e.getMessage());
        }
        return userDTO;
    }

    @Override
    public UserDTO getUsersById(Long userId) {
        UserDTO userDTO = new UserDTO();
        try {
            UserEntity userEntity = userRepository.findByUserId(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            userDTO = UserMapper.toDTO(userEntity);
            userDTO.setStatusCode(200);
            userDTO.setMessage("User with id '" + userId + "' found successfully");
        } catch (Exception e) {
            userDTO.setStatusCode(500);
            userDTO.setMessage("Error occurred: " + e.getMessage());
        }
        return userDTO;
    }

    @Override
    public UserDTO deleteUser(Long userId) {
        UserDTO userDTO = new UserDTO();
        try {
            Optional<UserEntity> userEntityOptional = userRepository.findByUserId(userId);
            if (userEntityOptional.isPresent()) {
                userRepository.deleteById(userId);
                userDTO.setStatusCode(200);
                userDTO.setMessage("User deleted successfully");
            } else {
                userDTO.setStatusCode(404);
                userDTO.setMessage("User not found for deletion");
            }
        } catch (Exception e) {
            userDTO.setStatusCode(500);
            userDTO.setMessage("Error occurred while deleting user: " + e.getMessage());
        }
        return userDTO;
    }

    @Override
    public UserDTO updateUser(UserDTO updatedUser) {
        UserDTO userDTO = new UserDTO();
        try {
            UserEntity existingUser = userRepository.findById(updatedUser.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            existingUser.setUserName(updatedUser.getUserName());
            existingUser.setFirstName(updatedUser.getFirstName());
            existingUser.setLastName(updatedUser.getLastName());
            existingUser.setRole(updatedUser.getRole());
            existingUser.setGroupe(updatedUser.getGroupe());
            existingUser.setSex(updatedUser.getSex());

            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            }

            UserEntity savedUser = userRepository.save(existingUser);
            userDTO.setUser(savedUser);
            userDTO.setStatusCode(200);
            userDTO.setMessage("User updated successfully");
        } catch (Exception e) {
            userDTO.setStatusCode(500);
            userDTO.setMessage("Error occurred while updating user: " + e.getMessage());
        }
        return userDTO;
    }

    @Override
    public UserDTO getMyInfo(String userName) {
        UserDTO userDTO = new UserDTO();
        try {
            UserEntity userEntity = userRepository.findByUserName(userName)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            userDTO.setUser(userEntity);
            userDTO.setStatusCode(200);
            userDTO.setMessage("successful");
        } catch (Exception e) {
            userDTO.setStatusCode(500);
            userDTO.setMessage("Error occurred while getting user info: " + e.getMessage());
        }
        return userDTO;
    }
}