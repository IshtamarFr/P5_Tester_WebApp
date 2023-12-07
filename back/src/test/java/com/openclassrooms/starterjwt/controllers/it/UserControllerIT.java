package com.openclassrooms.starterjwt.controllers.it;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder=new BCryptPasswordEncoder();

    final ObjectMapper mapper=new ObjectMapper();
    private Long testUserId;

    final private User testUser=User.builder()
            .email("987654321@test.com")
            .password(passwordEncoder.encode("Aa123456!"))
            .lastName("MockLN")
            .firstName("MockFN")
            .build();

    @BeforeEach
    void init() {
        try {
            userRepository.save(testUser);
            userRepository.findByEmail("987654321@test.com").ifPresent(user->testUserId=user.getId());
        } catch (Exception e) {
            //ignore
        }
    }

    @AfterEach
    void clean(){
        userRepository.findByEmail("987654321@test.com").ifPresent(user -> userRepository.deleteById(user.getId()));
    }

    @Test
    @WithMockUser(roles="USER")
    public void testGetUserByIdFindsValidUser() throws Exception {
        //When
        this.mockMvc.perform(get("/api/user/"+testUserId))

        //Then
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("MockFN")));
    }

    @Test
    public void testDeleteUserByIdIsRejected() throws Exception {
        //When
        this.mockMvc.perform(delete("/api/user/"+testUserId))

        //Then
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testDeleteUserByIdIsRejectedWhenUserIsNotValid() throws Exception {
        //When
        this.mockMvc.perform(delete("/api/user/"+testUserId).with(user("yoga@studio.com")))

        //Then
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testDeleteUserByIdWorksWhenUserIsValid() throws Exception {
        //When
        this.mockMvc.perform(delete("/api/user/"+testUserId).with(user("987654321@test.com")))

        //Then
                .andExpect(status().isOk());
    }
}
