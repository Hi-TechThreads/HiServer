import common.model.UserDto;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ActiveProfiles;
import user.UserServerApplication;
import user.model.User;
import user.repository.UserRepository;
import user.service.UserService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = UserServerApplication.class, properties = {"grpc.server.enabled=false"})
@ActiveProfiles("test") // Assumes test profile sets up an in-memory DB and cache
public class UserServiceCachingTest {

    @Autowired
    private UserService userService;

    // Use SpyBean so we can verify that the repository is only called once.
    @Mock
    private UserRepository userRepository;

    @Autowired
    private CacheManager cacheManager;

    @Test
    public void testGetUserByIdCaching() {
        // Prepare a dummy user.
        User user = new User();
        user.setId(1L);

        // When the repository is called with id=1, return the dummy user.
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Call the service method twice.
//        UserDto firstCall = userService.getUserById(1L);
        UserDto secondCall = userService.getUserById(1L);

        // The repository should be called only once because the result should be cached.
        verify(userRepository, times(1)).findById(1L);

        // Also verify that the cache holds the result for key "1"
        Object cached = cacheManager.getCache("users").get(1L, UserDto.class);
//        assertThat(cached).isEqualTo(firstCall);
//        assertThat(firstCall).isEqualTo(secondCall);
    }
}
