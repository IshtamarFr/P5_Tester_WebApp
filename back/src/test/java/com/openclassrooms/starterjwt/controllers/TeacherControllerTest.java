package com.openclassrooms.starterjwt.controllers.tests;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TeacherControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    TeacherService teacherService;

    final Teacher mockTeacher1=Teacher.builder()
            .id(106L)
            .firstName("TheOld")
            .lastName("Man")
            .build();
    final Teacher mockTeacher2=Teacher.builder()
            .id(682L)
            .firstName("HardToDestroy")
            .lastName("Reptile")
            .build();
    final List<Teacher> teachers=Arrays.asList(mockTeacher1, mockTeacher2);

    @Test
    @WithMockUser(roles="USER")
    public void testGetAllTeachersIsOkAndCallService() throws Exception {
        //Given
        when(teacherService.findAll()).thenReturn(teachers);

        //When
        this.mockMvc.perform(get("/api/teacher"))

        //Then
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("HardToDestroy")))
                .andExpect(content().string(containsString("TheOld")));
        verify(teacherService,times(1)).findAll();
    }

    @Test
    @WithMockUser(roles="USER")
    public void testGetTeacherByIdIsOkAndCallService() throws Exception {
        //Given
        when(teacherService.findById(682L)).thenReturn(mockTeacher2);

        //When
        this.mockMvc.perform(get("/api/teacher/682"))

        //Then
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Reptile")));
        verify(teacherService,times(1)).findById(682L);
    }

    @Test
    public void testGetTeacherByIdIsUnauthorized() throws Exception {
        //Given

        //When
        this.mockMvc.perform(get("/api/teacher/682"))

        //Then
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles="USER")
    public void testGetTeacherAsNullIsNotFoundAndDontCallService() throws Exception {
        //Given
        when(teacherService.findById(9999L)).thenReturn(null);

        //When
        this.mockMvc.perform(get("/api/teacher/9999"))

        //Then
                .andExpect(status().isNotFound());
        verify(teacherService,times(1)).findById(9999L);
    }

    @Test
    @WithMockUser(roles="USER")
    public void testGetTeacherAsNaNIsBadRequestAndDontCallService() throws Exception {
        //Given

        //When
        this.mockMvc.perform(get("/api/teacher/1ABCD"))

        //Then
                .andExpect(status().isBadRequest());
        verify(teacherService,times(0)).findById(any());
    }
}
