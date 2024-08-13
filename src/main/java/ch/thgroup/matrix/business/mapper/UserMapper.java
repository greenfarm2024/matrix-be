package ch.thgroup.matrix.business.mapper;

import ch.thgroup.matrix.business.dto.UserDTO;
import ch.thgroup.matrix.business.entity.UserEntity;

public class UserMapper {

    public static UserDTO toDTO(UserEntity userEntity) {
        return new UserDTO(
                userEntity.getUserId(),
                userEntity.getUserName(),
                userEntity.getFirstName(),
                userEntity.getLastName(),
                userEntity.getSex()
        );
    }

    public static UserEntity toEntity(UserDTO userDTO) {
        return new UserEntity(
                userDTO.getUserId(),
                userDTO.getUserName(),
                userDTO.getFirstName(),
                userDTO.getLastName(),
                userDTO.getSex()
        );
    }
}