package user.service;

import com.google.protobuf.Timestamp;
import common.grpc.user.proto.*;
import common.model.UserDto;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import user.model.CreateUserRequest;

@GrpcService
@RequiredArgsConstructor
@Slf4j
public class UserServiceGrpcImpl extends UserServiceGrpc.UserServiceImplBase{
    private final UserService userService;

    @Override
    public void getUserById(GetUserByIdRequest request, StreamObserver<UserResponse> responseObserver){
        UserDto userDto = userService.getUserById(request.getId());
        Timestamp createdAt = Timestamp.newBuilder()
                .setSeconds(userDto.getCreatedAt().getEpochSecond())
                .setNanos(userDto.getCreatedAt().getNano())
                .build();

        Timestamp updatedAt = Timestamp.newBuilder()
                .setSeconds(userDto.getCreatedAt().getEpochSecond())
                .setNanos(userDto.getCreatedAt().getNano())
                .build();

        UserResponse response = UserResponse.newBuilder()
                .setId(userDto.getId())
                .setUsername(userDto.getUsername())
                .setEmail(userDto.getEmail())
                .setFirstName(userDto.getFirstName())
                .setLastName(userDto.getLastName())
                .setCreatedAt(createdAt)
                .setUpdatedAt(updatedAt)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void registerUser(RegistrationGrpcRequest request, StreamObserver<RegistrationGrpcResponse> responseObserver){
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setEmail(request.getEmail());
        createUserRequest.setUsername(request.getUsername());
        createUserRequest.setPassword(request.getPassword());
        createUserRequest.setFirstName(request.getFirstName());
        createUserRequest.setLastName(request.getLastName());
        UserDto userDto = userService.createUser(createUserRequest);
        RegistrationGrpcResponse response = RegistrationGrpcResponse.newBuilder()
                        .setEmail(userDto.getEmail())
                        .setCreatedUserId(userDto.getId())
                        .setUsername(userDto.getUsername())
                        .setRoleValue(userDto.getRole().ordinal())
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
