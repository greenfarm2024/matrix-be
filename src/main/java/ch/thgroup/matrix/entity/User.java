package ch.thgroup.matrix.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users", schema = "matrix")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    public User(Long userId, String firstName) {
        this.userId = userId;
        this.firstName = firstName;
    }

    public User() {

    }
}
