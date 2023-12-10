package com.openclassrooms.starterjwt.integration;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TeacherControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TeacherRepository teacherRepository;

    final Teacher testTeacher= Teacher.builder()
            .lastName("MockLN")
            .firstName("MockFN")
            .build();

    private Long testTeacherId;

    @BeforeEach
    void init() {
        try {
            testTeacherId = teacherRepository.save(testTeacher).getId();
        } catch (Exception e) {
            //ignore
        }
    }

    @AfterEach
    void clean() {
        try {
            teacherRepository.deleteById(testTeacherId);
        } catch (Exception e) {
            //ignore
        }
    }

    @Test
    @WithMockUser(roles="USER")
    public void testGetAllTeachersIsOkAndCallService() throws Exception {
        //When
        this.mockMvc.perform(get("/api/teacher"))

        //Then
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles="USER")
    public void testGetTeacherByIdIsOkAndCallService() throws Exception {
        //When
        this.mockMvc.perform(get("/api/teacher/"+testTeacherId))

        //Then
                .andExpect(status().isOk());
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
    public void testGetTeacherIsNotFound() throws Exception {
        //When
        this.mockMvc.perform(get("/api/teacher/999999999"))

        //Then
                .andExpect(status().isNotFound());
    }
}
