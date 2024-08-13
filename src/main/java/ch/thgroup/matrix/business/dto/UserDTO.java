package ch.thgroup.matrix.business.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class UserDTO {
    private Long userId;
    private String userName;
    private String firstName;
    private String lastName;
    private String sex;
}
