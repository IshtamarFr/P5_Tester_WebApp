package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
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

    final Session mockSession=Session.builder()
            .id(42L)
            .name("mock Session")
            .date(new Date())
            .description("The mockest session")
            .users(new ArrayList<>())
            .build();
    final User mockUser=User.builder()
            .id(999L)
            .firstName("Tickle")
            .lastName("Monster")
            .email("scp999@scpfundation.com")
            .admin(false)
            .password("999999")
            .build();

    @Test
    @DisplayName("When I create a new session, it should call sessionRepo")
    public void testCreateNewSessionIsSaved() {
        //Given
        Session session=new Session();

        //When
        sessionService.create(session);

        //Then
        verify(sessionRepository,times(1)).save(session);
    }

    @Test
    @DisplayName("When I delete a session, it should call sessionRepo")
    public void testKillSessionIsDeleted() {
        //Given

        //When
        sessionService.delete(1L);

        //Then
        verify(sessionRepository,times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("When I findAll sessions, it should call sessionRepo")
    public void testGetAllSessions() {
        //Given

        //When
        List<Session> sessions=sessionService.findAll();

        //Then
        verify(sessionRepository,times(1)).findAll();
    }

    @Test
    @DisplayName("When I try to participate for not found session, it should throw NotFoundException")
    public void testParticipateThrowsExceptionWhenNotFound() {
        //Given

        //When

        //Then
        assertThatThrownBy(()->sessionService.participate(1L,42L)).isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("When I update a session, it should call sessionRepo")
    public void testUpdateSessionSavesSession() {
        //Given

        //When
        Session session=sessionService.update(73L,mockSession);

        //Then
        verify(sessionRepository,times(1)).save(mockSession);
    }

    @Test
    @DisplayName("When valid user tries to participate for valid session, it should call sessionRepo and userSession")
    public void testParticipateIsOkWhenUserAndSessionAreValid() {
        //Given
        when(sessionRepository.findById(42L)).thenReturn(Optional.of(mockSession));
        when(userRepository.findById(999L)).thenReturn(Optional.of(mockUser));

        //When
        sessionService.participate(42L,999L);

        //Then
        verify(sessionRepository,times(1)).findById(42L);
        verify(userRepository,times(1)).findById(999L);
    }

    @Test
    @DisplayName("When I try to noLongerParticipate for not found session, it should throw NotFoundException")
    public void testNoLongerParticipateNotFoundThrowsException() {
        //Given

        //When

        //Then
        assertThatThrownBy(()->sessionService.noLongerParticipate(1L,42L)).isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("When invalid user try to noLongerParticipate for valid session, it should throw BadRequestException")
    public void testNoLongerParticipateThrowsBadRequestException() {
        //Given
        when(sessionRepository.findById(42L)).thenReturn(Optional.of(mockSession));

        //When

        //Then
        assertThatThrownBy(()->sessionService.noLongerParticipate(42L,999L)).isInstanceOf(BadRequestException.class);
    }

    @Test
    @DisplayName("WhenI try to noLongerParticipate valid session, it should call sessionRepo")
    public void testNoLongerParticipateIsOkWithCorrectCredential() {
        //Given
        List<User>users=new ArrayList<>();
        users.add(mockUser);
        mockSession.setUsers(users);

        when(sessionRepository.findById(42L)).thenReturn(Optional.of(mockSession));

        //When
        sessionService.noLongerParticipate(42L,999L);

        //Then
        verify(sessionRepository,times(1)).save(mockSession);
    }
}
