package com.openclassrooms.starterjwt.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ObjectMapper mapper;

    final private BCryptPasswordEncoder passwordEncoder=new BCryptPasswordEncoder();

    final User testUser=User.builder()
            .email("987654321@test.com")
            .password(passwordEncoder.encode("Aa123456!"))
            .lastName("MockLN")
            .firstName("MockFN")
            .build();

    @AfterEach
    void clean(){
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("When I request login with correct credentials, response is OK and returns correct data")
    public void testLoginUserWorksWithCorrectCredential() throws Exception {
        //Given
        userRepository.save(testUser);

        LoginRequest loginRequest=LoginRequest.builder()
                .email("987654321@test.com")
                .password("Aa123456!")
                .build();

        //When
        this.mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(loginRequest)))

        //Then
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("LN")))
                .andExpect(content().string(containsString("false")));

        assertThat(userRepository.findByEmail("987654321@test.com").isPresent()).isTrue();
    }

    @Test
    @DisplayName("When I request register with correct data, response is OK")
    public void testRegisterUserWorks() throws Exception {
        //Given
        SignupRequest signupRequest= SignupRequest.builder()
                .email("987654321@test.com")
                .password("Aa123456!")
                .lastName("MockLN")
                .firstName("MockFN")
                .build();

        //When
        this.mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(signupRequest)))

        //Then
                .andExpect(status().isOk());
        assertThat(userRepository.findByEmail("987654321@test.com").isPresent()).isTrue();
    }

    @Test
    @DisplayName("When I request register but email is already taken, response is BadRequest and returns message")
    public void testRegisterUserDontWorkIfEmailIsAlreadyTaken() throws Exception {
        //Given
        userRepository.save(testUser);

        SignupRequest signupRequest= SignupRequest.builder()
                .email("987654321@test.com")
                .password("Aa123456!")
                .lastName("MockLN")
                .firstName("MockFN")
                .build();

        //When
        this.mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(signupRequest)))

        //Then
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Email is already taken")));
    }
}
