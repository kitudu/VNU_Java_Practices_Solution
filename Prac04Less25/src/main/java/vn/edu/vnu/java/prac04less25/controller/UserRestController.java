package vn.edu.vnu.java.prac04less25.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import vn.edu.vnu.java.prac04less25.entity.User;
import vn.edu.vnu.java.prac04less25.entity.UserRequest;
import vn.edu.vnu.java.prac04less25.entity.UserResponse;
import vn.edu.vnu.java.prac04less25.service.IUserService;
import vn.edu.vnu.java.prac04less25.util.JWTUtil;


@Controller
@RequestMapping("/user")
public class UserRestController {

    @Autowired
    private IUserService userService;
    @Autowired
    private JWTUtil util;
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/saveUser")
    public ResponseEntity<String> saveUser(@RequestBody User user) {

        Integer id = userService.saveUser(user);
        String message= "User with id '"+id+"' saved succssfully!";
        //return new ResponseEntity<String>(message, HttpStatus.OK);
        return ResponseEntity.ok(message);
    }

    @PostMapping("/loginUser")
    public ResponseEntity<UserResponse> login(@RequestBody UserRequest request){

        //Validate username/password with DB(required in case of Stateless Authentication)
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(), request.getPassword()));
        String token =util.generateToken(request.getUsername());
        return ResponseEntity.ok(new UserResponse(token,"Token generated successfully!"));
    }

    @PostMapping("/getData")
    public ResponseEntity<String> testAfterLogin(Principal p){
        return ResponseEntity.ok("You are accessing data after a valid Login. You are :" +p.getName());
    }
}
