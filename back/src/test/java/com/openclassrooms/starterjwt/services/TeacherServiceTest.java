package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeacherServiceTest {

    @Mock TeacherRepository teacherRepository;

    @InjectMocks TeacherService teacherService;

    final Teacher teacherOne=Teacher.builder()
            .firstName("Prenom")
            .lastName("Nom")
            .build();
    final Teacher teacherTwo=Teacher.builder()
            .firstName("Prenom2")
            .lastName("Nom2")
            .build();
    final List<Teacher> teachers= Arrays.asList(teacherOne,teacherTwo);

    @Test
    @DisplayName("When I findAll teachers, it should return mock list and call teacherRepo")
    public void testFindAllTeachers() {
        //Given
        when(teacherRepository.findAll()).thenReturn(teachers);

        //When
        List<Teacher> teachersList=teacherService.findAll();

        //Then
        assertThat(teachersList.size()).isEqualTo(2);
        verify(teacherRepository,times(1)).findAll();
    }

    @Test
    @DisplayName("When I findById valid teacher, it should return mock teacher and call teacherRepo")
    public void testFindValidTeacherById() {
        //Given
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teachers.get(1)));

        //When
        Teacher teacher=teacherService.findById(1L);

        //Then
        assertThat(teacher).isEqualTo(teacherOne);
        verify(teacherRepository,times(1)).findById(1L);
    }
}
