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

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserDTO> fetchAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserDTO> fetchUserById(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        return userRepository.findById(userId)
                .map(UserMapper::toDTO);
    }

    @Override
    public void addUser(UserDTO userDTO) {
        UserEntity userEntity = UserMapper.toEntity(userDTO);
        userRepository.save(userEntity);
    }

    @Override
    public Optional<UserDTO> updateUser(Long userId, UserDTO userDTO) {
        return userRepository.findById(userId)
                .map(userEntity -> {
                    userEntity.setUserName(userDTO.getUserName());
                    userEntity.setFirstName(userDTO.getFirstName());
                    userEntity.setLastName(userDTO.getLastName());
                    userEntity.setSex(userDTO.getSex());
                    return UserMapper.toDTO(userRepository.save(userEntity));
                });
    }

    @Override
    public void removeUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User not found with ID = " + userId);
        }
        userRepository.deleteById(userId);
    }
}