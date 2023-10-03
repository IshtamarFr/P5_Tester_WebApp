package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class SessionServiceTest {
    @Mock SessionRepository sessionRepository;
    @Mock UserRepository userRepository;
    @InjectMocks SessionService sessionService;

    @Test
    public void testNewSession() {
        Session session=new Session();
        sessionService.create(session);
        verify(sessionRepository,times(1)).save(session);
    }

    @Test void testKillSession() {
        sessionService.delete(1L);
        verify(sessionRepository,times(1)).deleteById(1L);
    }

    //TODO: Finish all tests for sessionService
}
