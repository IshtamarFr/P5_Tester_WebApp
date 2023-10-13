package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class TeacherControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    TeacherService teacherService;

    Teacher mockTeacher1;
    Teacher mockTeacher2;
    List<Teacher> teachers;

    @BeforeEach
    public void init() {
        mockTeacher1=new Teacher();
        mockTeacher1.setId(106L);
        mockTeacher1.setFirstName("TheOld");
        mockTeacher1.setLastName("Man");

        mockTeacher2=new Teacher();
        mockTeacher2.setId(682L);
        mockTeacher2.setFirstName("HardToDestroy");
        mockTeacher2.setLastName("Reptile");

        teachers=new ArrayList<>();
        teachers.add(mockTeacher1);
        teachers.add(mockTeacher2);
    }

    @Test
    @WithUserDetails("yoga@studio.com")
    public void testGetAllTeachers() throws Exception {
        when(teacherService.findAll()).thenReturn(teachers);

        this.mockMvc.perform(get("/api/teacher"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("HardToDestroy")))
                .andExpect(content().string(containsString("TheOld")));
    }

    @Test
    @WithUserDetails("yoga@studio.com")
    public void testGetTeacherById() throws Exception {
        when(teacherService.findById(682L)).thenReturn(mockTeacher2);

        this.mockMvc.perform(get("/api/teacher/682"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Reptile")));
    }

    @Test
    public void testGetTeacherByIdIsUnauthorized() throws Exception {
        this.mockMvc.perform(get("/api/teacher/682"))
                .andExpect(status().isUnauthorized());
    }
}
