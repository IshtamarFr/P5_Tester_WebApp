package com.openclassrooms.starterjwt.controllers.it;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

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

    private final BCryptPasswordEncoder passwordEncoder=new BCryptPasswordEncoder();

    final ObjectMapper mapper=new ObjectMapper();

    final User testUser=User.builder()
            .email("987654321@test.com")
            .password(passwordEncoder.encode("Aa123456!"))
            .lastName("MockLN")
            .firstName("MockFN")
            .build();

    @AfterEach
    void clean(){
        userRepository.findByEmail("987654321@test.com").ifPresent(user -> userRepository.deleteById(user.getId()));
    }

    @Test
    public void testLoginUserWorksWithCorrectCredential() throws Exception {
        //Given
        try {
            userRepository.save(testUser);
        } catch (Exception e) {
            //ignore
        }

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
    }

    @Test
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
    }
}
