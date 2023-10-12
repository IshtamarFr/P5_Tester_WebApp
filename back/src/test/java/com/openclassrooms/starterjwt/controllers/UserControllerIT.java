package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class UserControllerIT {
    @Autowired
    private MockMvc mockMvc;

    final ObjectMapper mapper=new ObjectMapper();

    @Test
    @WithUserDetails("yoga@studio.com")
    public void testGetUserById() throws Exception {
        this.mockMvc.perform(get("/api/user/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("dmin")));
    }

    @Test
    public void testDeleteUserByIdIsRejected() throws Exception {
        this.mockMvc.perform(delete("/api/user/2"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails("yoga@studio.com")
    public void testDeleteUserById() throws Exception {
        this.mockMvc.perform(delete("/api/user/1"))
                .andExpect(status().isOk());
    }
}
