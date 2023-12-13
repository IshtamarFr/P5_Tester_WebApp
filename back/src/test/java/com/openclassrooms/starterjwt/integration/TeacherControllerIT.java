package com.openclassrooms.starterjwt.integration;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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

    @AfterEach
    void clean() {
        teacherRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles="USER")
    @DisplayName("When auth user requests get all teachers, response is OK")
    public void testGetAllTeachersIsOkAndCallService() throws Exception {
        //Given
        teacherRepository.save(testTeacher);

        //When
        this.mockMvc.perform(get("/api/teacher"))

        //Then
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("MockLN")));
    }

    @Test
    @WithMockUser(roles="USER")
    @DisplayName("When auth user requests get one teacher, response is OK")
    public void testGetTeacherByIdIsOkAndCallService() throws Exception {
        //Given
        Long testTeacherId=teacherRepository.save(testTeacher).getId();

        //When
        this.mockMvc.perform(get("/api/teacher/"+testTeacherId))

        //Then
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("MockLN")));
    }

    @Test
    @DisplayName("When unAuthorized user requests get one teacher, response is unAuthorized")
    public void testGetTeacherByIdIsUnauthorized() throws Exception {
        //Given

        //When
        this.mockMvc.perform(get("/api/teacher/682"))

        //Then
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles="USER")
    @DisplayName("When auth user requests get inexistant teacher, response is NotFound")
    public void testGetTeacherIsNotFound() throws Exception {
        //Given

        //When
        this.mockMvc.perform(get("/api/teacher/999999999"))

        //Then
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles="USER")
    @DisplayName("When auth user requests get NaN teacher, response is BadRequest")
    public void testGetTeacherAsNaNIsBadRequest() throws Exception {
        //Given

        //When
        this.mockMvc.perform(get("/api/teacher/7a2D@!-1"))

                //Then
                .andExpect(status().isBadRequest());
    }
}
