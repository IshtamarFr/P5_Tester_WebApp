package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock UserRepository userRepository;

    @InjectMocks UserService userService;

    @Test
    @DisplayName("When I findById valid user, it should return mock and call userRepo")
    public void testFindValidUserById() {
        //Given
        User mockUser=User.builder()
                .email("test@test.com")
                .firstName("Prenom")
                .lastName("Nom")
                .password("123456")
                .admin(true)
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        //When
        User user=userService.findById(1L);

        //Then
        assertThat(user).isEqualTo(mockUser);
        verify(userRepository,times(1)).findById(1L);
    }

    @Test
    @DisplayName("When I delete user, it should call user repo")
    public void testDeleteExistingUserById() {
        //Given

        //When
        userService.delete(3L);

        //Then
        verify(userRepository,times(1)).deleteById(3L);
    }
}
