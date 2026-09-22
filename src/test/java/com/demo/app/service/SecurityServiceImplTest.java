```java
package com.demo.app.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecurityServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private UserDetails userDetails;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private SecurityServiceImpl securityService;

    @BeforeEach
    void setUp() {
        // Clear any existing security context to ensure test isolation
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Given a logged-in user in SecurityContext, when findLoggedInUsername is called, then return the username")
    void givenLoggedInUserInSecurityContext_whenFindLoggedInUsername_thenReturnUsername() {
        // Arrange
        String expectedUsername = "testUser";
        when(SecurityContextHolder.getContext()).thenReturn(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(expectedUsername);

        // Act
        String result = securityService.findLoggedInUsername();

        // Assert
        assertEquals(expectedUsername, result);
        verify(securityContext, times(1)).getAuthentication();
        verify(authentication, times(1)).getName();
    }

    @Test
    @DisplayName("Given no logged-in user in SecurityContext, when findLoggedInUsername is called, then return null")
    void givenNoLoggedInUserInSecurityContext_whenFindLoggedInUsername_thenReturnNull() {
        // Arrange
        when(SecurityContextHolder.getContext()).thenReturn(securityContext);
        when(securityContext.getAuthentication()).thenReturn(null);

        // Act
        String result = securityService.findLoggedInUsername();

        // Assert
        assertNull(result);
        verify(securityContext, times(1)).getAuthentication();
        verify(authentication, never()).getName();
    }

    @Test
    @DisplayName("Given valid credentials, when autologin is called, then set authentication in SecurityContext")
    void givenValidCredentials_whenAutologin_thenSetAuthenticationInSecurityContext() {
        // Arrange
        String username = "validUser";
        String password = "validPassword";
        List<GrantedAuthority> authorities = Collections.singletonList(mock(GrantedAuthority.class));

        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(userDetails.getAuthorities()).thenReturn(authorities);
        when(userDetails.getUsername()).thenReturn(username);
        when(userDetails.getPassword()).thenReturn(password);
        when(userDetails.isEnabled()).thenReturn(true);
        when(userDetails.isAccountNonExpired()).thenReturn(true);
        when(userDetails.isAccountNonLocked()).thenReturn(true);