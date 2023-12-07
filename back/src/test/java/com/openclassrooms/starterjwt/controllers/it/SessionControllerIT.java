package com.openclassrooms.starterjwt.controllers.it;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
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

import java.time.LocalDateTime;
import java.util.Date;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SessionControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    TeacherRepository teacherRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    SessionRepository sessionRepository;
    @Autowired
    SessionMapper sessionMapper;

    private final BCryptPasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
    final ObjectMapper mapper=new ObjectMapper();

    Long mockTeacherId;
    Long mockUserId;
    Long mockSessionId;

    final Teacher mockTeacher=Teacher.builder()
            .firstName("mockFN")
            .lastName("mockLN")
            .build();

    final User mockUser=User.builder()
            .firstName("Tickle")
            .lastName("Monster")
            .email("scp999@scpfundation.com")
            .admin(false)
            .password(passwordEncoder.encode("999999"))
            .build();

    final Session mockSession = Session.builder()
            .name("mock Session")
            .date(new Date())
            .teacher(mockTeacher)
            .description("The mockest session")
            .createdAt(LocalDateTime.now())
            .build();

    @BeforeEach
    void init() {
        try {
           mockUserId= userRepository.save(mockUser).getId();
           mockTeacherId=teacherRepository.save(mockTeacher).getId();
           mockSessionId=sessionRepository.save(mockSession).getId();
        } catch (Exception e) {
            //ignore
        }
    }

    @AfterEach
    void clean() {
        try {
            sessionRepository.deleteById(mockSessionId);
            userRepository.deleteById(mockUserId);
            teacherRepository.deleteById(mockTeacherId);
        } catch (Exception e) {
            //ignore
        }
    }

    @Test
    @WithMockUser(roles="USER")
    public void testGetAllSessionsWorks() throws Exception {
        //When
        this.mockMvc.perform(get("/api/session/"))

        //Then
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles="USER")
    public void testGetSessionByIdIsOk() throws Exception {
        //When
        this.mockMvc.perform(get("/api/session/"+mockSessionId))

        //Then
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("mockest")));
    }

    @Test
    @WithMockUser(roles="USER")
    public void testDeleteSessionIsOk() throws Exception {
        //When
        this.mockMvc.perform(delete("/api/session/"+mockSessionId))

        //Then
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles="USER")
    public void testAddUserToSessionIsOk() throws Exception {
        //When
        this.mockMvc.perform(post("/api/session/"+mockSessionId+"/participate/"+mockUserId))

        //Then
                .andExpect(status().isOk());
    }
}
