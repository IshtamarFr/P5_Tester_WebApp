package com.openclassrooms.starterjwt.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
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

    ObjectMapper mapper=new ObjectMapper();

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

    @AfterEach
    void clean() {
        sessionRepository.deleteAll();
        userRepository.deleteAll();
        teacherRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles="USER")
    @DisplayName("When auth user requests get all sessions, response is OK")
    public void testGetAllSessionsWorks() throws Exception {
        //Given
        userRepository.save(mockUser);
        teacherRepository.save(mockTeacher);
        sessionRepository.save(mockSession);

        //When
        this.mockMvc.perform(get("/api/session/"))

        //Then
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("mockest")))
                .andExpect(content().string(containsString("mock Session")));
    }

    @Test
    @WithMockUser(roles="USER")
    @DisplayName("When auth user requests get one session, response is OK and return data")
    public void testGetSessionByIdIsOk() throws Exception {
        //Given
        userRepository.save(mockUser);
        teacherRepository.save(mockTeacher);
        Long mockSessionId=sessionRepository.save(mockSession).getId();

        //When
        this.mockMvc.perform(get("/api/session/"+mockSessionId))

        //Then
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("mockest")))
                .andExpect(content().string(containsString("mock Session")));
    }

    @Test
    @WithMockUser(roles="USER")
    @DisplayName("When auth user requests delete one session, response is OK")
    public void testDeleteSessionIsOk() throws Exception {
        //Given
        userRepository.save(mockUser);
        teacherRepository.save(mockTeacher);
        Long mockSessionId=sessionRepository.save(mockSession).getId();

        //When
        this.mockMvc.perform(delete("/api/session/"+mockSessionId))

        //Then
                .andExpect(status().isOk());
        assertThat(sessionRepository.findById(mockSessionId).isPresent()).isFalse();
    }

    @Test
    @WithMockUser(roles="USER")
    @DisplayName("When auth user requests participate for session, response is OK")
    public void testAddUserToSessionIsOk() throws Exception {
        //Given
        Long mockUserId= userRepository.save(mockUser).getId();
        teacherRepository.save(mockTeacher);
        Long mockSessionId=sessionRepository.save(mockSession).getId();

        //When
        this.mockMvc.perform(post("/api/session/"+mockSessionId+"/participate/"+mockUserId))

        //Then
                .andExpect(status().isOk());
        assertThat(sessionRepository.findById(mockSessionId).isPresent()).isTrue();
        assertThat(sessionRepository.findById(mockSessionId).get().getUsers().contains(mockUser)).isTrue();
    }

    @Test
    @WithMockUser(roles="USER")
    @DisplayName("When auth user requests get NaN session, response is BadRequest")
    public void testGetSessionAsNanIsBadRequest() throws Exception {
        //Given
        userRepository.save(mockUser);
        teacherRepository.save(mockTeacher);
        sessionRepository.save(mockSession);

        //When
        this.mockMvc.perform(get("/api/session/Zza123"))

        //Then
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles="USER")
    @DisplayName("When auth user requests get inexistant session, response is NotFound")
    public void testGetSessionAsNullIsNotFound() throws Exception {
        //Given
        userRepository.save(mockUser);
        teacherRepository.save(mockTeacher);
        long mockSessionId=sessionRepository.save(mockSession).getId()+1;

        //When
        this.mockMvc.perform(get("/api/session/"+mockSessionId))

        //Then
                .andExpect(status().isNotFound());
    }
    @Test
    @WithMockUser(roles="USER")
    @DisplayName("When auth user requests delete NaN session, response is BadRequest")
    public void testDeleteSessionAsNaNIsBadRequest() throws Exception {
        //Given
        userRepository.save(mockUser);
        teacherRepository.save(mockTeacher);
        sessionRepository.save(mockSession);

        //When
        this.mockMvc.perform(delete("/api/session/Zvn98a"))

        //Then
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles="USER")
    @DisplayName("When auth requests delete inexistant session, response is NotFound")
    public void testDeleteSessionAsNullIsNotFound() throws Exception {
        //Given
        userRepository.save(mockUser);
        teacherRepository.save(mockTeacher);
        long mockSessionId=sessionRepository.save(mockSession).getId()+1;

        //When
        this.mockMvc.perform(delete("/api/session/"+mockSessionId))

        //Then
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles="USER")
    @DisplayName("When auth user requests participate NaN session, response is BadRequest")
    public void testParticipateSessionAsNaNIsBadRequest() throws Exception {
        //Given
        Long mockUserId=userRepository.save(mockUser).getId();
        teacherRepository.save(mockTeacher);
        Long mockSessionId=sessionRepository.save(mockSession).getId();

        //When
        this.mockMvc.perform(post("/api/session/"+mockSessionId+"/participate/Znv3"))

        //Then
                .andExpect(status().isBadRequest());

        //And When
        this.mockMvc.perform(post("/api/session/n378d/participate/"+mockUserId))

        //Then
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles="USER")
    @DisplayName("When auth user requests unParticipate NaN session, response is BadRequest")
    public void testUnParticipateSessionAsNaNIsBadRequest() throws Exception {
        //Given
        Long mockUserId=userRepository.save(mockUser).getId();
        teacherRepository.save(mockTeacher);
        Long mockSessionId=sessionRepository.save(mockSession).getId();

        //When
        this.mockMvc.perform(delete("/api/session/"+mockSessionId+"/participate/Znv3"))

        //Then
                .andExpect(status().isBadRequest());

        //Or When
        this.mockMvc.perform(delete("/api/session/n378d/participate/"+mockUserId))

        //Then
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("When auth user creates a new session, it is OK and returns created session")
    public void testCreateNewSessionWithSuccess() throws Exception {
        //Given
        userRepository.save(mockUser);
        teacherRepository.save(mockTeacher);
        sessionRepository.save(mockSession);

        SessionDto mockWannabeSession=SessionDto.builder()
                .name("Saucession")
                .date(new Date())
                .teacher_id(mockTeacher.getId())
                .description("I wanna be the very best like no one ever was")
                .build();

        System.out.println(mockWannabeSession);

        //When
        this.mockMvc.perform(post("/api/session").with(user("scp999@scpfundation.com"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(mockWannabeSession)))

        //Then
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("very best like")));
        assertThat(sessionRepository.findAll().size()).isEqualTo(2);
        assertThat(sessionRepository.findAll()).contains(mockSession);
    }

    @Test
    @DisplayName("When auth user modifies a new session, it is OK and overwrites initial session")
    public void testModifySessionWithSuccess() throws Exception {
        //Given
        userRepository.save(mockUser);
        teacherRepository.save(mockTeacher);
        long mockSessionId = sessionRepository.save(mockSession).getId();

        SessionDto mockWannabeSession = SessionDto.builder()
                .id(mockSessionId)
                .name("Saucession")
                .date(new Date())
                .teacher_id(mockTeacher.getId())
                .description("I wanna be the very best like no one ever was")
                .build();

        System.out.println(mockWannabeSession);

        //When
        this.mockMvc.perform(put("/api/session/" + mockSessionId).with(user("scp999@scpfundation.com"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(mockWannabeSession)))

                //Then
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("very best like")));
        assertThat(sessionRepository.findAll().size()).isEqualTo(1);
    }
}
