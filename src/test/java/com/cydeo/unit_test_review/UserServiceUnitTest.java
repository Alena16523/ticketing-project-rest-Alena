package com.cydeo.unit_test_review;

import com.cydeo.dto.RoleDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.Role;
import com.cydeo.entity.User;
import com.cydeo.mapper.UserMapper;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.KeycloakService;
import com.cydeo.service.ProjectService;
import com.cydeo.service.TaskService;
import com.cydeo.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProjectService projectService;

    @Mock
    private TaskService taskService;

    @Mock
    private KeycloakService keycloakService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService; //we need to inject mocks into this bean,
    // because that's the class we are testing
    //@InjectMocks will go to real UserServiceImpl class and replace real beans to mocks we created above.

    @Spy //runs real userMapper but you still gonna be able to see if userMapper converts objects
    private UserMapper userMapper=new UserMapper(new ModelMapper());

    User user;
    UserDTO userDTO;

    @BeforeEach //will run before each of the methods
    void setUp(){
        user=new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setUserName("user");
        user.setPassWord("Abc1");
        user.setEnabled(true);
        user.setRole(new Role("Manager"));

        userDTO=new UserDTO();
        userDTO.setId(1L);
        userDTO.setFirstName("John");
        userDTO.setLastName("Doe");
        userDTO.setUserName("user");
        userDTO.setPassWord("Abc1");
        userDTO.setEnabled(true);

        RoleDTO roleDTO=new RoleDTO();
        roleDTO.setDescription("Manager");
        userDTO.setRole(roleDTO);

    }

    private List<User> getUsers(){
        User user2=new User();
        user2.setId(2L);
        user2.setFirstName("Emily");
        return List.of(user, user2); //returns some immutable list
    }

    private List<UserDTO> getUsersDTO(){
        UserDTO userDTO2=new UserDTO();
        userDTO2.setId(2L);
        userDTO2.setFirstName("Emily");
        return List.of(userDTO, userDTO2); //returns some immutable list
    }

    @Test
    void should_list_all_users(){
        //stub
        when(userRepository.findAllByIsDeletedOrderByFirstNameDesc(false)).thenReturn(getUsers());
        //when that real method is called, we need to return list of users entity, and we created that method above

        List<UserDTO> expectedList=getUsersDTO();
//        expectedList.sort(Comparator.comparing(UserDTO::getFirstName).reversed());

        List<UserDTO> actualList=userService.listAllUsers();

       // Assertions.assertEquals(expectedList, actualList);

        //AssertJ
        assertThat(actualList).usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedList);

    }

    @Test
    void should_find_user_by_username(){
        when(userRepository.findByUserNameAndIsDeleted(anyString(), anyBoolean())).thenReturn(user);

        UserDTO actual=userService.findByUserName("user");
        Assertions.assertEquals(userDTO, actual);
    }

    @Test
    void should_throw_exception_when_user_not_found(){
//        Throwable throwable=catchThrowable(()->userService.findByUserName("SomeUserName"));
//        Assertions.assertInstanceOf(NoSuchElementException.class, throwable);
//        Assertions.assertEquals("User not found", throwable.getMessage());

        Assertions.assertThrowsExactly(NoSuchElementException.class, ()->userService.findByUserName("SomeUsername"), "User not found");
    }

    @Test
    void should_save_user(){
        when(userRepository.save(any())).thenReturn(user);

        UserDTO actualDTO=userService.save(userDTO);

        assertThat(actualDTO).usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(userDTO);

        verify(passwordEncoder).encode(anyString());
    }

    @Test
    void should_update_user(){
        when(userRepository.findByUserNameAndIsDeleted(anyString(), anyBoolean())).thenReturn(user);
        when(userRepository.save(any())).thenReturn(user);
        UserDTO actualDTO=userService.update(userDTO);
        verify(passwordEncoder).encode(anyString());
        assertThat(actualDTO).usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(userDTO);

    }











}
