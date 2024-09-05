package ch.thgroup.matrix.business.admin.dto;

import ch.thgroup.matrix.business.admin.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDTO {
    private Long userId;
    private int statusCode;
    private String error;
    private String message;
    private String token;
    private String refreshToken;
    private String expirationTime;
    private String userName;
    private String firstName;
    private String lastName;
    private String role;
    private String sex;
    private String groupe;
    private String password;
    private UserEntity user;
    private List<UserEntity> userList;
}
