package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SessionServiceTest {
    @Mock SessionRepository sessionRepository;
    @Mock UserRepository userRepository;
    @InjectMocks SessionService sessionService;

    Session mockSession;
    User mockUser;

    @BeforeEach
    public void init() {
        mockUser=new User();
        mockUser.setId(999L);
        mockUser.setFirstName("Tickle");
        mockUser.setLastName("Monster");
        mockUser.setEmail("scp999@scpfundation.com");
        mockUser.setAdmin(false);
        mockUser.setPassword("999999");

        mockSession = new Session();
        mockSession.setId(42L);
        mockSession.setName("mock Session");
        mockSession.setDate(new Date());
        mockSession.setDescription("The mockest session");
        mockSession.setUsers(new ArrayList<>());
    }

    @Test
    public void testNewSession() {
        Session session=new Session();
        sessionService.create(session);
        verify(sessionRepository,times(1)).save(session);
    }

    @Test
    public void testKillSession() {
        sessionService.delete(1L);
        verify(sessionRepository,times(1)).deleteById(1L);
    }

    @Test
    public void testGetAllSessions() {
        List<Session> sessions=sessionService.findAll();
        verify(sessionRepository,times(1)).findAll();
    }

    @Test
    public void testParticipateThrowsException() {
        assertThatThrownBy(()->sessionService.participate(1L,42L)).isInstanceOf(NotFoundException.class);
    }

    @Test
    public void testUpdate() {
        Session session=sessionService.update(73L,mockSession);
        verify(sessionRepository,times(1)).save(mockSession);
    }

    @Test
    public void testParticipateIsValid() {

        when(sessionRepository.findById(42L)).thenReturn(Optional.of(mockSession));
        when(userRepository.findById(999L)).thenReturn(Optional.of(mockUser));

        sessionService.participate(42L,999L);
        verify(sessionRepository,times(1)).findById(42L);
        verify(userRepository,times(1)).findById(999L);
    }

    @Test
    public void testNoLongerParticipateNotFoundThrowsException() {
        assertThatThrownBy(()->sessionService.participate(1L,42L)).isInstanceOf(NotFoundException.class);
    }

    @Test
    public void testNoLongerParticipateThrowsBadRequestException() {
        when(sessionRepository.findById(42L)).thenReturn(Optional.of(mockSession));

        assertThatThrownBy(()->sessionService.noLongerParticipate(42L,999L)).isInstanceOf(BadRequestException.class);
    }

    @Test
    public void testNoLongerParticipate() {
        List<User>users=new ArrayList<>();
        users.add(mockUser);
        mockSession.setUsers(users);

        when(sessionRepository.findById(42L)).thenReturn(Optional.of(mockSession));

        sessionService.noLongerParticipate(42L,999L);
        verify(sessionRepository,times(1)).save(mockSession);
    }
}
