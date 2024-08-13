package ch.thgroup.matrix.business.controller;

import ch.thgroup.matrix.business.dto.UserDTO;
import ch.thgroup.matrix.business.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping(ApplicationPaths.API_PATH + "/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/fetchallusers")
    public List<UserDTO> fetchAllUsers() {
        return userService.fetchAllUsers();
    }

    @GetMapping("/fetchuserbyid/{userId}")
    public ResponseEntity<UserDTO> fetchUserById(@PathVariable Long userId) {
        return userService.fetchUserById(userId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/adduser")
    public ResponseEntity<Void> addUser(@Valid @RequestBody UserDTO userDTO) {
        userService.addUser(userDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/updateuser/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        return userService.updateUser(id, userDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/deleteuser/{userId}")
    public ResponseEntity<Void> removeUser(@PathVariable Long userId) {
        userService.removeUser(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}