package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Test
    @WithMockUser(roles="USER")
    @DisplayName("When auth user requests get one user, request is OK and userService is called")
    public void testGetUserByIdNullUserIsNotFound() throws Exception {
        //Given
        when(userService.findById(42L)).thenReturn(null);

        //When
        this.mockMvc.perform(get("/api/user/42"))

        //Then
                .andExpect(status().isNotFound());
        verify(userService,times(1)).findById(42L);
    }

    @Test
    @WithMockUser(roles="USER")
    @DisplayName("When auth user requests get a NaN user, request is BadRequest and no service is called")
    public void testGetUserNaNIsBadRequestAndDontCallService() throws Exception {
        //Given

        //When
        this.mockMvc.perform(get("/api/user/AAA"))

        //Then
                .andExpect(status().isBadRequest());
        verify(userService,times(0)).findById(any());
    }

    @Test
    @WithMockUser(roles="USER")
    @DisplayName("When auth user requests delete one inexistant user, request is NotFound and userService is called for find only")
    public void testDeleteUserByIdNullUserIsNotFoundAndDontCallDeleteService() throws Exception {
        //Given
        when(userService.findById(42L)).thenReturn(null);

        //When
        this.mockMvc.perform(delete("/api/user/42"))

        //Then
                .andExpect(status().isNotFound());
        verify(userService,times(1)).findById(42L);
        verify(userService,times(0)).delete(any());
    }

    @Test
    @WithMockUser(roles="USER")
    @DisplayName("When auth user requests delete NaN user, request is BadRequest and no service is called")
    public void testDeleteUserNaNIsBadRequestAndDontCallServices() throws Exception {
        //Given

        //When
        this.mockMvc.perform(delete("/api/user/AAA"))

        //Then
                .andExpect(status().isBadRequest());
        verify(userService,times(0)).findById(any());
        verify(userService,times(0)).delete(any());
    }
}
