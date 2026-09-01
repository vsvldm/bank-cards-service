package com.bank.cards.infrastructure.security.jwt.service;

import com.bank.cards.domain.exception.UnauthorizedException;
import com.bank.cards.presentation.dto.request.JwtRequest;
import com.bank.cards.application.dto.output.JwtResponse;
import com.bank.cards.infrastructure.util.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
    private final UserDetailsService userDetailsService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;

    @Override
    public JwtResponse createToken(JwtRequest jwtRequest) {
        try {
          authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                  jwtRequest.getUsername(),
                  jwtRequest.getPassword()));

          UserDetails userDetails = userDetailsService.loadUserByUsername(jwtRequest.getUsername());
          String token = jwtTokenUtils.generateToken(userDetails);

          return new JwtResponse(token);
        } catch (BadCredentialsException e) {
            throw new UnauthorizedException("Invalid username or password.");
        }
    }
}
