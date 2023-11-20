package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
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
    public void testFindUser() {
        User mockUser=new User("test@test.com","Nom","Prenom","123456",true);

        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        User user=userService.findById(1L);
        assertThat(user).isEqualTo(mockUser);
        verify(userRepository,times(1)).findById(1L);
    }

    @Test
    public void testDeleteUser() {
        userService.delete(3L);
        verify(userRepository,times(1)).deleteById(3L);
    }
}
