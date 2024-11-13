package me.khruslan.spotifyreleasenotifier.user;

import org.easymock.EasyMockExtension;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static me.khruslan.spotifyreleasenotifier.fixture.TelegramFixtures.TELEGRAM_USER_ID;
import static me.khruslan.spotifyreleasenotifier.fixture.UserFixtures.USER_DTO;
import static me.khruslan.spotifyreleasenotifier.fixture.UserFixtures.mockUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.easymock.EasyMock.expect;

@ExtendWith(EasyMockExtension.class)
public class UserServiceTest extends EasyMockSupport {
    @Mock
    private UserDao userDao;

    @Mock
    private UserMapper userMapper;

    private UserService userService;

    @BeforeEach
    public void init() {
        userService = new UserService(userDao, userMapper);
    }

    @Test
    public void givenUsersFound_whenGetAllUsers_thenReturnUsers() {
        expect(userDao.getAllUsers()).andReturn(List.of(USER_DTO));
        expect(userMapper.mapFromDto(List.of(USER_DTO))).andReturn(List.of(mockUser()));
        replayAll();

        assertThat(userService.getAllUsers()).isEqualTo(List.of(mockUser()));
        verifyAll();
    }

    @Test
    public void givenUserFound_whenUserExists_thenReturnTrue() {
        expect(userDao.userExists(TELEGRAM_USER_ID)).andReturn(true);
        replayAll();

        assertThat(userService.userExists(TELEGRAM_USER_ID)).isTrue();
        verifyAll();
    }

    @Test
    public void givenUserNotFound_whenUserExists_thenReturnFalse() {
        expect(userDao.userExists(TELEGRAM_USER_ID)).andReturn(false);
        replayAll();

        assertThat(userService.userExists(TELEGRAM_USER_ID)).isFalse();
        verifyAll();
    }

    @Test
    public void givenUserSaveable_whenCreateUser_thenReturnTrue() {
        expect(userMapper.mapToDto(mockUser())).andReturn(USER_DTO);
        expect(userDao.createUser(USER_DTO)).andReturn(true);
        replayAll();

        assertThat(userService.createUser(mockUser())).isTrue();
        verifyAll();
    }

    @Test
    public void givenUserNotSaveable_whenCreateUser_thenReturnFalse() {
        expect(userMapper.mapToDto(mockUser())).andReturn(USER_DTO);
        expect(userDao.createUser(USER_DTO)).andReturn(false);
        replayAll();

        assertThat(userService.createUser(mockUser())).isFalse();
        verifyAll();
    }

    @Test
    public void givenUserSaveable_whenUpdateUser_thenReturnTrue() {
        expect(userMapper.mapToDto(mockUser())).andReturn(USER_DTO);
        expect(userDao.updateUser(USER_DTO)).andReturn(true);
        replayAll();

        assertThat(userService.updateUser(mockUser())).isTrue();
        verifyAll();
    }

    @Test
    public void givenUserNotSaveable_whenUpdateUser_thenReturnFalse() {
        expect(userMapper.mapToDto(mockUser())).andReturn(USER_DTO);
        expect(userDao.updateUser(USER_DTO)).andReturn(false);
        replayAll();

        assertThat(userService.updateUser(mockUser())).isFalse();
        verifyAll();
    }

    @Test
    public void givenUserDeletable_whenDeleteUser_thenReturnTrue() {
        expect(userDao.deleteUser(TELEGRAM_USER_ID)).andReturn(true);
        replayAll();

        assertThat(userService.deleteUser(TELEGRAM_USER_ID)).isTrue();
        verifyAll();
    }

    @Test
    public void givenUserNotDeletable_whenDeleteUser_thenReturnTrue() {
        expect(userDao.deleteUser(TELEGRAM_USER_ID)).andReturn(false);
        replayAll();

        assertThat(userService.deleteUser(TELEGRAM_USER_ID)).isFalse();
        verifyAll();
    }
}
