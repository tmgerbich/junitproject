package org.example;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

public class UserServiceTest {

    @Mock
    private Map<String, User> userDatabaseMock;
    private UserService userService;
    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userDatabaseMock);
    }

    //test to successfully register a user not in the database
    @Test
    public void testRegisterUserSuccess() {
        // Arrange
        user = new User("JohnDoe", "password", "jdoe@example.com");
        when(userDatabaseMock.containsKey(user.getUsername())).thenReturn(false);

        // Act
        boolean registrationSuccess = userService.registerUser(user);

        // Assert
        assertTrue(registrationSuccess);
    }

    //test to successfully fail registration when user exists
    @Test
    public void testRegisterUserFailure() {
        // Arrange
        user = new User("JohnDoe", "password", "jdoe@example.com");
        when(userDatabaseMock.containsKey(user.getUsername())).thenReturn(true);

        // Act
        boolean registrationSuccess = userService.registerUser(user);

        // Assert
        assertFalse(registrationSuccess);
    }

    //this edge case involves a space in the username at the end. this is currently allowed by the program and works correctly, if usernames are not intended to contain spaces then one would reverse the test and then go adjust the original code
    @Test
    public void testRegisterUserWithSpace() {
        // Arrange
        User user2 = new User("JohnDoe ", "password", "jdoe@example.com");
        when(userDatabaseMock.containsKey(user2.getUsername())).thenReturn(false);

        // Act
        boolean registrationSuccess = userService.registerUser(user2);

        // Assert
        assertTrue(registrationSuccess);
    }

    //test to log in successfully
    @Test
    public void testLoginUserSuccess() {
        // Arrange
        user = new User("JohnDoe", "password", "jdoe@example.com");
        when(userDatabaseMock.get(user.getUsername())).thenReturn(user);

        // Act
        User loggedInUser = userService.loginUser(user.getUsername(), user.getPassword());

        // Assert
        assertNotNull(loggedInUser);
        assertEquals(user.getUsername(), loggedInUser.getUsername());
    }

    //test for login failure if user not registered
    @Test
    public void testLoginUserFailureWhenUserNotFound() {
        // Arrange
        user = new User("JohnDoe", "password", "jdoe@example.com");
        when(userDatabaseMock.get(user.getUsername())).thenReturn(null);

        // Act
        User loggedInUser = userService.loginUser(user.getUsername(), user.getPassword());

        // Assert
        assertNull(loggedInUser);
    }

    //test for login failure when user types incorrect password
    @Test
    public void testLoginUserFailureWhenPasswordIncorrect() {
        // Arrange
        user = new User("JohnDoe", "password", "jdoe@example.com");
        when(userDatabaseMock.get(user.getUsername())).thenReturn(user);

        // Act
        User loggedInUser = userService.loginUser(user.getUsername(), "wrongPassword");

        // Assert
        assertNull(loggedInUser);
    }

    //test to successfully update user information
    @Test
    public void testUpdateUserProfileSuccess() {
        // Arrange
        user = new User("JohnDoe", "password", "jdoe@example.com");
        when(userDatabaseMock.containsKey("newUsername")).thenReturn(false);

        // Act
        boolean updateSuccess = userService.updateUserProfile(user, "newUsername", "newPassword", "newEmail");

        // Assert
        assertTrue(updateSuccess);
        verify(userDatabaseMock).put("newUsername", user);
        assertEquals("newUsername", user.getUsername());
        assertEquals("newPassword", user.getPassword());
        assertEquals("newEmail", user.getEmail());
    }

    //test to fail update information when username is taken
    @Test
    public void testUpdateUserProfileFailureWhenNewUsernameTaken() {
        // Arrange
        user = new User("JohnDoe", "password", "jdoe@example.com");
        when(userDatabaseMock.containsKey("newUsername")).thenReturn(true);

        // Act
        boolean updateSuccess = userService.updateUserProfile(user, "newUsername", "newPassword", "newEmail");

        // Assert
        assertFalse(updateSuccess);
        verify(userDatabaseMock, never()).put("newUsername", user);
    }

    //test to successfully update information even when password has been used already
    @Test
    public void testUpdateUserProfileDuplicatePassword() {
        // Arrange
        user = new User("JohnDoe", "password", "jdoe@example.com");
        User user2 = new User("JohnDeere", "samePassword", "jdeere@example.com");
        userDatabaseMock.put(user2.getUsername(), user2);
        when(userDatabaseMock.containsKey("newUsername")).thenReturn(true);

        // Act
        boolean updateSuccess = userService.updateUserProfile(user, "newUsername", "samePassword", "newEmail");

        // Assert
        assertFalse(updateSuccess);
        verify(userDatabaseMock, never()).put("newUsername", user);
    }

}
