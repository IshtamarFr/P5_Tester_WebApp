package com.openclassrooms.starterjwt.controllers.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.SessionService;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class SessionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    SessionService sessionService;
    @MockBean
    UserService userService;

    Session mockSession;
    User mockUser;

    final ObjectMapper mapper=new ObjectMapper();

    @Autowired
    SessionMapper sessionMapper;

    @BeforeEach
    public void init() {
        Teacher mockTeacher=Teacher.builder()
                .id(1L)
                .firstName("mockFN")
                .lastName("mockLN")
                .build();

        mockUser=User.builder()
                .id(999L)
                .firstName("Tickle")
                .lastName("Monster")
                .email("scp999@scpfundation.com")
                .admin(false)
                .password("999999")
                .build();

        mockSession = Session.builder()
                .id(42L)
                .name("mock Session")
                .date(new Date())
                .teacher(mockTeacher)
                .description("The mockest session")
                .build();
    }

    @Test
    @WithUserDetails("yoga@studio.com")
    public void testGetAllSessions() throws Exception {
        List<Session>sessions=new ArrayList<>();
        sessions.add(mockSession);
        when(sessionService.findAll()).thenReturn(sessions);

        this.mockMvc.perform(get("/api/session/"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("yoga@studio.com")
    public void testGetSessionById() throws Exception {
        when(sessionService.getById(42L)).thenReturn(mockSession);

        this.mockMvc.perform(get("/api/session/42"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("mockest")));
    }

    @Test
    @WithUserDetails("yoga@studio.com")
    public void testCreateSession() throws Exception {
        when(sessionService.create(mockSession)).thenReturn(mockSession);

        this.mockMvc.perform(post("/api/session/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(sessionMapper.toDto(mockSession)))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(sessionService,times(1)).create(mockSession);
    }

    @Test
    @WithUserDetails("yoga@studio.com")
    public void testModifySession() throws Exception {
        when(sessionService.update(42L, mockSession)).thenReturn(mockSession);

        this.mockMvc.perform(put("/api/session/42")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(sessionMapper.toDto(mockSession)))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(sessionService,times(1)).update(42L,mockSession);
    }

    @Test
    @WithUserDetails("yoga@studio.com")
    public void testModifySessionBadRequest() throws Exception {
        this.mockMvc.perform(put("/api/session/42A")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(sessionMapper.toDto(mockSession)))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithUserDetails("yoga@studio.com")
    public void testDeleteSession() throws Exception {
        when(sessionService.getById(42L)).thenReturn(mockSession);

        this.mockMvc.perform(delete("/api/session/42"))
                .andExpect(status().isOk());
        verify(sessionService,times(1)).delete(42L);
    }

    @Test
    @WithUserDetails("yoga@studio.com")
    public void testAddUserToSession() throws Exception {
        when(sessionService.getById(42L)).thenReturn(mockSession);
        when(userService.findById(999L)).thenReturn(mockUser);

        this.mockMvc.perform(post("/api/session/42/participate/999"))
                .andExpect(status().isOk());
        verify(sessionService,times(1)).participate(42L,999L);
    }

    @Test
    @WithUserDetails("yoga@studio.com")
    public void testRemoveUserToSession() throws Exception {
        when(sessionService.getById(42L)).thenReturn(mockSession);
        when(userService.findById(999L)).thenReturn(mockUser);

        this.mockMvc.perform(delete("/api/session/42/participate/999"))
                .andExpect(status().isOk());
        verify(sessionService,times(1)).noLongerParticipate(42L,999L);
    }

    @Test
    @WithUserDetails("yoga@studio.com")
    public void testDeleteSessionAsNull() throws Exception {
        when(sessionService.getById(682L)).thenReturn(null);

        this.mockMvc.perform(delete("/api/session/682"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails("yoga@studio.com")
    public void testDeleteParticipationAsNaN() throws Exception {
        this.mockMvc.perform(delete("/api/session/8/participate/*68847--"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithUserDetails("yoga@studio.com")
    public void testPostParticipationAsNaN() throws Exception {
        this.mockMvc.perform(post("/api/session/*68847--/participate/5"))
                .andExpect(status().isBadRequest());
    }


}
