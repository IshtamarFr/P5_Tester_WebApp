package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeacherServiceTest {

    @Mock TeacherRepository teacherRepository;

    @InjectMocks TeacherService teacherService;

    List<Teacher> teachers;
    @BeforeEach
    public void init() {
        teachers= new ArrayList<>();
        Teacher teacherOne=Teacher.builder().firstName("Prenom").lastName("Nom").build();
        Teacher teacherTwo=Teacher.builder().firstName("Prenom2").lastName("Nom2").build();
        teachers.add(teacherOne);
        teachers.add(teacherTwo);
    }

    @Test
    public void testFindAllTeachers() {
        when(teacherRepository.findAll()).thenReturn(teachers);

        List<Teacher> teachersList=teacherService.findAll();
        assertThat(teachersList.size()).isEqualTo(2);
        verify(teacherRepository,times(1)).findAll();
    }

    @Test
    public void testFindTeacher() {
        Teacher mockTeacher=Teacher.builder().firstName("Prenom").lastName("Nom").build();
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teachers.get(1)));

        Teacher teacher=teacherService.findById(1L);
        assertThat(teacher).isEqualTo(mockTeacher);
        verify(teacherRepository,times(1)).findById(1L);
    }
}
