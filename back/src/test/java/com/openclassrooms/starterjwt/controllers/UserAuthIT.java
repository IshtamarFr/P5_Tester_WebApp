package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class UserAuthIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    final ObjectMapper mapper=new ObjectMapper();

    @Test
    public void testRegisterUser() throws Exception {
        SignupRequest signupRequest=new SignupRequest();
        signupRequest.setEmail("mockNewUser@test.com");
        signupRequest.setFirstName("mockFirstName");
        signupRequest.setLastName("mockLastName");
        signupRequest.setPassword("123456789");

        this.mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(signupRequest))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("mockNewUser@test.com")
    public void testDeleteUser() throws Exception {
        Optional<User> user=userRepository.findByEmail("mockNewUser@test.com");
        if (user.isPresent()) {
            Long userId=user.get().getId();
            this.mockMvc.perform(delete("/api/user/{id}",userId))
                    .andExpect(status().isOk());
        } else {
            throw new Exception("User has not been found");
        }
    }
}
