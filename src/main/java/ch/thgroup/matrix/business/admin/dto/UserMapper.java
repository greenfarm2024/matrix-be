package ch.thgroup.matrix.business.admin.dto;

import ch.thgroup.matrix.business.admin.entity.UserEntity;

public class UserMapper {

    public static UserDTO toDTO(UserEntity userEntity) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(userEntity.getUserId());
        userDTO.setUserName(userEntity.getUsername());
        userDTO.setFirstName(userEntity.getFirstName());
        userDTO.setLastName(userEntity.getLastName());
        userDTO.setRole(userEntity.getRole());
        userDTO.setSex(userEntity.getSex());
        userDTO.setGroupe(userEntity.getGroupe());
        userDTO.setPassword(userEntity.getPassword());
        return userDTO;
    }

    public static UserEntity toEntity(UserDTO userDTO) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(userDTO.getUserId());
        userEntity.setUserName(userDTO.getUserName());
        userEntity.setFirstName(userDTO.getFirstName());
        userEntity.setLastName(userDTO.getLastName());
        userEntity.setRole(userDTO.getRole());
        userEntity.setSex(userDTO.getSex());
        userEntity.setGroupe(userDTO.getGroupe());
        userEntity.setPassword(userDTO.getPassword());
        return userEntity;
    }
}