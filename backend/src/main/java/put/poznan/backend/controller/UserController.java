package put.poznan.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import put.poznan.backend.dto.users.AllUsersResponse;
import put.poznan.backend.service.UserService;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping(path = "/all")
    public ResponseEntity<List<AllUsersResponse>> getAllUsers() {
        return ResponseEntity.ok( userService.getAllUsers() );
    }

    @GetMapping(path = "/me")
    public ResponseEntity<AllUsersResponse> whoIam() {
        return ResponseEntity.ok( userService.whoAmI() );
    }
}
