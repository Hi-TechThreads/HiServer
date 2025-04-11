package user.service;


import common.exceptions.ResourceNotFoundException;
import common.model.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import user.model.User;
import user.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly=true)
public class UserService {
    private final UserRepository userRepository;

    @Cacheable(value="userCache", key="#id")
    public UserDto getUserById(Long id){
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id ));
        return mapToDto(user);
    }

    public UserDto getUserByUsername(String username){
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        return mapToDto(user);
    }

    @Transactional
    public UserDto createUser(UserDto userDto){
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());  // TODO: need to encrypt the password
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());

        User savedUser = userRepository.save(user);
        return mapToDto(savedUser);
    }



    private UserDto mapToDto(User user){
        return new UserDto(user.getId(), user.getUsername(), user.getPassword(), user.getEmail(), user.getFirstName(), user.getLastName());
    }

}
