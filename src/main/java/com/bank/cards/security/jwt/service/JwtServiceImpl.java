package com.bank.cards.security.jwt.service;

import com.bank.cards.exception.exception.UnauthorizedException;
import com.bank.cards.security.jwt.dto.JwtRequest;
import com.bank.cards.security.jwt.dto.JwtResponse;
import com.bank.cards.service.user.UserService;
import com.bank.cards.util.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
    private final UserService userService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;

    @Override
    public JwtResponse createToken(JwtRequest jwtRequest) {
        try {
          authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                  jwtRequest.getUsername(),
                  jwtRequest.getPassword()));

          UserDetails userDetails = userService.loadUserByUsername(jwtRequest.getUsername());
          String token = jwtTokenUtils.generateToken(userDetails);

          return new JwtResponse(token);
        } catch (BadCredentialsException e) {
            throw new UnauthorizedException("Invalid username or password.");
        }
    }
}
