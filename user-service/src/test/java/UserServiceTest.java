import common.model.Role;
import common.model.UserDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.util.ObjectUtils;
import user.UserServerApplication;
import user.model.User;
import user.repository.UserRepository;
import user.service.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = UserServerApplication.class)
@ActiveProfiles("test")
public class UserServiceTest {
    @MockitoSpyBean
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private CacheManager cacheManager;

    private final String CACHE_NAME = "users";

    private Cache getCache(){
        Cache cache = cacheManager.getCache(CACHE_NAME);
        assertNotNull(cache);
        return cache;
    }
    @BeforeEach
    @AfterEach
    void setupAndTearDown(){
        Cache cache = getCache();
        if(!ObjectUtils.isEmpty(cache)){
            cache.clear();
        }

        reset(userRepository);
        userRepository.deleteAll();
    }

    private User saveTestUser(String username, String email){
        User user = new User();
        user.setUsername(username);
        user.setPassword( passwordEncoder.encode("password"));
        user.setEmail(email);
        user.setFirstName("Cache");
        user.setLastName("Test");
        user.setRole(Role.USER);
        return userRepository.saveAndFlush(user);
    }

    @Test
    @DisplayName("Make sure redis cache stored user after fetch user by id")
    public void testGetUserByIdCaching(){
        User savedUser = saveTestUser("test", "test@email.com");
        long userId = savedUser.getId();
        UserDto firstCall = userService.getUserById(userId);
        UserDto secondCall = userService.getUserById(userId);

        verify(userRepository, times(1)).findById(userId);

        assertEquals(firstCall, secondCall);
    }
}
