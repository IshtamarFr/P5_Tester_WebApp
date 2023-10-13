package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class SessionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    SessionController sessionController;

    @MockBean
    SessionService sessionService;

    Session mockSession;

    @BeforeEach
    public void init() {
        Teacher mockTeacher=new Teacher();
        mockTeacher.setId(99L);
        mockTeacher.setFirstName("mockFN");
        mockTeacher.setLastName("mockLN");

        mockSession = new Session();
        mockSession.setId(42L);
        mockSession.setName("mock Session");
        mockSession.setDate(new Date());
        mockSession.setTeacher(mockTeacher);
        mockSession.setDescription("The mockest session");

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
}
