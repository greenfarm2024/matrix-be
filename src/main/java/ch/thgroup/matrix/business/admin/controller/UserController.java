package ch.thgroup.matrix.business.admin.controller;

import ch.thgroup.matrix.business.common.ApplicationPaths;
import ch.thgroup.matrix.business.admin.dto.UserDTO;
import ch.thgroup.matrix.business.admin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApplicationPaths.API_PATH + "/admin")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    // to register a user we have to be logged in as an admin
    public ResponseEntity<UserDTO> register(@RequestBody UserDTO reg){
        return ResponseEntity.ok(userService.register(reg));
    }

    @PostMapping("/auth/login")
    // to login we don't need to be logged in
    public ResponseEntity<UserDTO> login(@RequestBody UserDTO req){
        return ResponseEntity.ok(userService.login(req));
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<UserDTO> refreshToken(@RequestBody UserDTO req){
        return ResponseEntity.ok(userService.refreshToken(req));
    }

    @GetMapping("/getallusers")
    // to get all users we have to be logged in as an admin
    public ResponseEntity<UserDTO> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());

    }

    @GetMapping("/getuserbyid/{userId}")
    public ResponseEntity<UserDTO> getUserByID(@PathVariable Long userId){
        return ResponseEntity.ok(userService.getUsersById(userId));

    }

    @PutMapping("/updateuser")
    // to update a user we have to be logged in as an admin
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDTO){
        return ResponseEntity.ok(userService.updateUser(userDTO));
    }

    @GetMapping("/getprofile")
    public ResponseEntity<UserDTO> getMyProfile(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        UserDTO response = userService.getMyInfo(email);
        return  ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/deleteuser/{userId}")
    public ResponseEntity<UserDTO> deleteUser(@PathVariable Long userId){
        return ResponseEntity.ok(userService.deleteUser(userId));
    }

}
