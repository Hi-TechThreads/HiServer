package user.service;


import common.exceptions.ResourceNotFoundException;
import common.model.Role;
import common.model.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import user.exception.UserAlreadyExistsException;
import user.model.CreateUserRequest;
import user.model.UpdateUserRequest;
import user.model.User;
import user.repository.UserRepository;
import user.utils.UserMapper;

import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly=true)
@CacheConfig(cacheNames = "users")
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @CacheEvict(allEntries = true)
    public UserDto createUser(CreateUserRequest request){
        log.info("Attempting to create user with username: {}", request.getUsername());

        if (userRepository.existsByEmail(request.getEmail()))
            throw new UserAlreadyExistsException("Email already exists: " + request.getEmail());
        if (userRepository.existsByUsername(request.getUsername()))
            throw new UserAlreadyExistsException("User name already exists: " + request.getUsername());

        User user = userMapper.createUserRequestToUser(request);

        // Set Role default as USER
        user.setRole(Role.USER);

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        User savedUser = userRepository.save(user);
        log.info("User created successfully with ID: {}", user.getId());
        return userMapper.toUserDto(savedUser);
    }

    @Cacheable(key = "#id")
    public UserDto getUserById(long id){
        log.info("Fetching user by ID: {}", id);
        return userRepository.findById(id)
                .map(userMapper::toUserDto)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }

    @Cacheable(key = "#username")
    public UserDto getUserByUsername(String username){
        log.info("Fetching user by username: {}", username);
        return userRepository.findByUsername(username)
                .map(userMapper::toUserDto)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
    }

    public Page<UserDto> getAllUsers(Pageable pageable){
        log.info("Fetching all users page:{}, size:{}", pageable.getPageNumber(), pageable.getPageSize());
        Page<User> userPage = userRepository.findAll(pageable);
        return userPage.map(userMapper::toUserDto);
    }

    @Transactional
    @CacheEvict(key = "#id")
    public UserDto updateUser(long id, UpdateUserRequest request){
        log.info("Attempting to update user with ID:{}", id);
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        if(!request.getEmail().equalsIgnoreCase(existingUser.getEmail())){
            if(userRepository.existsByEmail(request.getEmail())){
                throw new UserAlreadyExistsException("Email already exists");
            }
        }

        userMapper.updateUserFromRequest(request, existingUser);

        User updateUser = userRepository.save(existingUser);
        log.info("User updated sucessfully with ID: {}", id);
        return userMapper.toUserDto(updateUser);
    }

    @Transactional
    @CacheEvict(key = "#id")
    public void deleteUser(long id){
        log.info("Attempting to delete user with ID: {}", id);

        if(!userRepository.existsById(id)){
            throw new ResourceNotFoundException("User", "id", id);
        }

        userRepository.deleteById(id);
        log.info("User deleted successfully with ID: {}", id);
    }

}
