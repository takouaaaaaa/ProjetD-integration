package com.projinteg.GesEvents.controller;

import com.projinteg.GesEvents.dto.SignInRequestDto;
import com.projinteg.GesEvents.security.UserDetailsImpl;
import com.projinteg.GesEvents.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;


    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody SignInRequestDto signInRequestDto) {
        // Authenticate the user using the provided username (or email) and password
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signInRequestDto.getUsername(), signInRequestDto.getPassword()));
        // Set the authentication context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate the JWT token for the authenticated user
        String jwt = jwtUtils.generateJwtToken(authentication);

        // Retrieve user details and return the JWT token in the response
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return ResponseEntity.ok(jwt);  // Return JWT in response
    }
}
