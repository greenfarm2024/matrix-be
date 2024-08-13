package ch.thgroup.matrix.business.service;

import ch.thgroup.matrix.business.dto.UserDTO;
import ch.thgroup.matrix.business.entity.UserEntity;
import ch.thgroup.matrix.business.mapper.UserMapper;
import ch.thgroup.matrix.business.repository.UserRepository;
import ch.thgroup.matrix.common.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }

    public UserDTO getUserById(long userId) {
        return userRepository.findById(userId)
                .map(UserMapper::toDTO)
                .orElseThrow(() -> new NotFoundException("No user found with ID = " + userId));
    }

    public void createUser(UserDTO userDTO) {
        UserEntity userEntity = UserMapper.toEntity(userDTO);
        userRepository.save(userEntity);
    }

    public UserDTO updateUser(Long userId, UserDTO userDTO) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userEntity.setUserName(userDTO.getUserName());
        userEntity.setFirstName(userDTO.getFirstName());
        userEntity.setLastName(userDTO.getLastName());
        userEntity.setSex(userDTO.getSex());
        return UserMapper.toDTO(userRepository.save(userEntity));
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}