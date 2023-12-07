package com.openclassrooms.starterjwt.controllers.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.SessionService;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SessionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    SessionService sessionService;
    @MockBean
    UserService userService;

    final ObjectMapper mapper=new ObjectMapper();

    @Autowired
    SessionMapper sessionMapper;

    final Teacher mockTeacher=Teacher.builder()
            .id(1L)
            .firstName("mockFN")
            .lastName("mockLN")
            .build();

    final User mockUser=User.builder()
            .id(999L)
                .firstName("Tickle")
                .lastName("Monster")
                .email("scp999@scpfundation.com")
                .admin(false)
                .password("999999")
                .build();

    final Session mockSession = Session.builder()
            .id(42L)
            .name("mock Session")
            .date(new Date())
            .teacher(mockTeacher)
            .description("The mockest session")
            .build();

    @Test
    @WithMockUser(roles="USER")
    public void testGetAllSessionsWorksAndCallService() throws Exception {
        //Given
        List<Session>sessions=new ArrayList<>();
        sessions.add(mockSession);
        when(sessionService.findAll()).thenReturn(sessions);

        //When
        this.mockMvc.perform(get("/api/session/"))

        //Then
                .andExpect(status().isOk());
        verify(sessionService,times(1)).findAll();
    }

    @Test
    @WithMockUser(roles="USER")
    public void testGetSessionByIdIsOkAndCallService() throws Exception {
        //Given
        when(sessionService.getById(42L)).thenReturn(mockSession);

        //When
        this.mockMvc.perform(get("/api/session/42"))

        //Then
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("mockest")));
        verify(sessionService,times(1)).getById(42L);
    }

    @Test
    @WithMockUser(roles="USER")
    public void testCreateSessionIsOkAndCallService() throws Exception {
        //Given
        when(sessionService.create(mockSession)).thenReturn(mockSession);

        //When
        this.mockMvc.perform(post("/api/session/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(sessionMapper.toDto(mockSession))))
        //Then
                .andExpect(status().isOk());
        verify(sessionService,times(1)).create(mockSession);
    }

    @Test
    @WithMockUser(roles="USER")
    public void testModifySessionIsOkAndCallService() throws Exception {
        //Given
        when(sessionService.update(42L, mockSession)).thenReturn(mockSession);

        //When
        this.mockMvc.perform(put("/api/session/42")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(sessionMapper.toDto(mockSession)))
                .accept(MediaType.APPLICATION_JSON))

        //Then
                .andExpect(status().isOk());
        verify(sessionService,times(1)).update(42L,mockSession);
    }

    @Test
    @WithMockUser(roles="USER")
    public void testModifySessionWithNaNIsBadRequestDontCallService() throws Exception {
        //Given

        //When
        this.mockMvc.perform(put("/api/session/42A")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(sessionMapper.toDto(mockSession)))
                .accept(MediaType.APPLICATION_JSON))

        //Then
                .andExpect(status().isBadRequest());
        verify(sessionService,times(0)).update(42L,mockSession);
    }

    @Test
    @WithMockUser(roles="USER")
    public void testDeleteSessionIsOkAndCallService() throws Exception {
        //Given
        when(sessionService.getById(42L)).thenReturn(mockSession);

        //When
        this.mockMvc.perform(delete("/api/session/42"))

        //Then
                .andExpect(status().isOk());
        verify(sessionService,times(1)).delete(42L);
    }

    @Test
    @WithMockUser(roles="USER")
    public void testAddUserToSessionIsOkAndCallService() throws Exception {
        //Given
        when(sessionService.getById(42L)).thenReturn(mockSession);
        when(userService.findById(999L)).thenReturn(mockUser);

        //When
        this.mockMvc.perform(post("/api/session/42/participate/999"))

        //Then
                .andExpect(status().isOk());
        verify(sessionService,times(1)).participate(42L,999L);
    }

    @Test
    @WithMockUser(roles="USER")
    public void testRemoveUserToSessionIsOkAndCallService() throws Exception {
        //Given
        when(sessionService.getById(42L)).thenReturn(mockSession);
        when(userService.findById(999L)).thenReturn(mockUser);

        //When
        this.mockMvc.perform(delete("/api/session/42/participate/999"))

        //Then
                .andExpect(status().isOk());
        verify(sessionService,times(1)).noLongerParticipate(42L,999L);
    }

    @Test
    @WithMockUser(roles="USER")
    public void testDeleteSessionAsNullIsNotFoundAndDontCallService() throws Exception {
        //Given
        when(sessionService.getById(682L)).thenReturn(null);

        //When
        this.mockMvc.perform(delete("/api/session/682"))

        //Then
                .andExpect(status().isNotFound());
        verify(sessionService,times(0)).delete(682L);
    }

    @Test
    @WithMockUser(roles="USER")
    public void testDeleteParticipationAsNaNIsBadRequestAndDontCallService() throws Exception {
        //Given

        //When
        this.mockMvc.perform(delete("/api/session/8/participate/*68847--"))

        //Then
                .andExpect(status().isBadRequest());
        verify(sessionService,times(0)).delete(any());
    }

    @Test
    @WithMockUser(roles="USER")
    public void testPostParticipationAsNaNIsBadRequestAndDontCallService() throws Exception {
        //Given

        //When

        this.mockMvc.perform(post("/api/session/*68847--/participate/5"))

        //Then
                .andExpect(status().isBadRequest());
        verify(sessionService,times(0)).participate(any(),any());
    }

    @Test
    @WithMockUser(roles="USER")
    public void testGetSessionAsNaNIsBadRequestAndDontCallService() throws Exception {
        //Given

        //When
        this.mockMvc.perform(get("/api/session/*68847--"))

        //Then
                .andExpect(status().isBadRequest());
        verify(sessionService,times(0)).getById(any());
    }

    @Test
    @WithMockUser(roles="USER")
    public void testDeleteSessionAsNaNIsBadRequestAndDontCallService() throws Exception {
        //Given

        //When
        this.mockMvc.perform(delete("/api/session/*68847--"))

        //Then
                .andExpect(status().isBadRequest());
        verify(sessionService,times(0)).delete(any());
    }

    @Test
    @WithMockUser(roles="USER")
    public void testGetSessionAsNotFundIsNotFound() throws Exception {
        //Given

        //When
        this.mockMvc.perform(get("/api/session/9999999999"))

        //Then
                .andExpect(status().isNotFound());
        verify(sessionService,times(1)).getById(9999999999L);
    }
}
