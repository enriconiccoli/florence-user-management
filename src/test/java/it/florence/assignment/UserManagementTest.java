package it.florence.assignment;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.mock.PanacheMock;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import it.florence.assignment.entity.UserEntity;
import it.florence.assignment.model.MultipartBody;
import it.florence.assignment.model.UserDTO;
import it.florence.assignment.service.UserService;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import org.hibernate.Session;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
class UserManagementTest {

    @Inject
    UserService userService;

    @InjectMock
    Session session;

    PanacheQuery query = Mockito.mock(PanacheQuery.class);
    UserEntity userEntity = new UserEntity(1000L, "testname", "testsurname", "mail", "address");
    List<UserEntity> userEntityList = List.of(userEntity);


    @Test
    void getAllUsers(){

        PanacheMock.mock(UserEntity.class);
        Mockito.when(UserEntity.findAll())
                .thenReturn(query);
        Mockito.when(query.list())
                .thenReturn(userEntityList);

        List<UserDTO> userDTOList = userService.getAllUsers();

        assertEquals(1, userDTOList.size(),"Check size");
        assertEquals(userEntity.getId(), userDTOList.get(0).getId(), "Check entity");
    }

    @Test
    void getUser(){

        PanacheMock.mock(UserEntity.class);
        Mockito.when(UserEntity.findById(Mockito.any()))
                .thenReturn(userEntity);

        UserDTO userDTO = userService.getUser(1000L);

        assertEquals(userEntity.getId(), userDTO.getId(), "Check entity");
    }

    @Test
    void getUserWrongId(){

        PanacheMock.mock(UserEntity.class);
        Mockito.when(UserEntity.findById(Mockito.any()))
                .thenReturn(null);

        WebApplicationException e =
                Assertions.assertThrows(WebApplicationException.class, ()-> userService.getUser(2000L));

        assertEquals(404, e.getResponse().getStatus(), "Check status");
        assertEquals("User not found", e.getMessage(), "Check error message");
    }

    @Test
    void findUsersByNameSurname(){

        PanacheMock.mock(UserEntity.class);
        Mockito.when(UserEntity.findUsersByNameSurname(Mockito.any(), Mockito.any()))
                .thenReturn(userEntityList);

        List<UserDTO> userDTOList = userService.findUsersByNameSurname("testname", "testsurname");

        assertEquals(1, userDTOList.size(),"Check size");
        assertEquals(userEntity.getId(), userDTOList.get(0).getId(), "Check entity");
    }

    @Test
    void findUsersByNameSurnameWrongIdentity(){

        PanacheMock.mock(UserEntity.class);
        Mockito.when(UserEntity.findUsersByNameSurname(Mockito.any(), Mockito.any()))
                .thenReturn(Collections.emptyList());

        List<UserDTO> userDTOList = userService.findUsersByNameSurname("", "");

        assertEquals(0, userDTOList.size(),"Check size");
    }

    @Test
    void createUser(){

        Mockito.doNothing().when(session).persist(Mockito.any());

        assertDoesNotThrow(() -> userService.createUser(new UserDTO()));
    }

    @Test
    void updateUser(){

        Mockito.doNothing().when(session).persist(Mockito.any());

        PanacheMock.mock(UserEntity.class);
        Mockito.when(UserEntity.findById(Mockito.any()))
                .thenReturn(userEntity);

        assertDoesNotThrow(() -> userService.updateUser(1000L, new UserDTO()));
    }

    @Test
    void updateUserWrongId(){

        PanacheMock.mock(UserEntity.class);
        Mockito.when(UserEntity.findById(Mockito.any()))
                .thenReturn(null);

        WebApplicationException e =
                Assertions.assertThrows(WebApplicationException.class, ()-> userService.updateUser(2000L, new UserDTO()));

        assertEquals(404, e.getResponse().getStatus(), "Check status");
        assertEquals("User not found", e.getMessage(), "Check error message");
    }

    @Test
    void deleteUser(){

        PanacheMock.mock(UserEntity.class);
        Mockito.when(UserEntity.deleteById(Mockito.any())).thenReturn(true);

        assertDoesNotThrow(() -> userService.deleteUser(1000L));
    }

    @Test
    void deleteAll(){
        PanacheMock.mock(UserEntity.class);
        Mockito.when(UserEntity.deleteAll()).thenReturn(1L);

        assertDoesNotThrow(() -> userService.deleteAll());
    }

    @Test
    void uploadCSV(){

        String csvContent = """
            id,name,surname,address,mail
            ,Enrico,Niccoli,Via Fittizia 65,enriconiccoli@mail.com
            """;
        InputStream csvStream = new ByteArrayInputStream(csvContent.getBytes());


        Mockito.doNothing().when(session).persist(Mockito.any());

        PanacheMock.mock(UserEntity.class);
        Mockito.when(UserEntity.findById(Mockito.any()))
                .thenReturn(null);

        assertDoesNotThrow(() ->userService.saveFromCSV(new MultipartBody(csvStream)));
    }

    @Test
    void uploadCSVUpdate(){

        String csvContent = """
            id,name,surname,address,mail
            1000,Enrico,Niccoli,Via Fittizia 65,enriconiccoli@mail.com
            """;
        InputStream csvStream = new ByteArrayInputStream(csvContent.getBytes());

        Mockito.doNothing().when(session).persist(Mockito.any());

        PanacheMock.mock(UserEntity.class);
        Mockito.when(UserEntity.findById(Mockito.any()))
                .thenReturn(userEntity);

        assertDoesNotThrow(() ->userService.saveFromCSV(new MultipartBody(csvStream)));
    }

    @Test
    void uploadCSVMalformedContent(){

        String csvContent = """
                This is not a valid CSV!
                ,id,nome,
                0,0,0,0,
                """;
        InputStream csvStream = new ByteArrayInputStream(csvContent.getBytes());

        WebApplicationException e = Assertions.assertThrows(
                WebApplicationException.class,
                () ->userService.saveFromCSV(new MultipartBody(csvStream))
        );

        assertEquals(400, e.getResponse().getStatus(), "Check error code");
    }

}