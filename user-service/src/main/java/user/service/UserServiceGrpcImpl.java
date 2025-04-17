package user.service;

import com.google.protobuf.Timestamp;
import common.model.UserDto;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import user.grpc.proto.GetUserRequest;
import user.grpc.proto.UserResponse;
import user.grpc.proto.UserServiceGrpc;

@GrpcService
@RequiredArgsConstructor
@Slf4j
public class UserServiceGrpcImpl extends UserServiceGrpc.UserServiceImplBase{
    private final UserService userService;

    @Override
    public void getUserById(GetUserRequest request, StreamObserver<UserResponse> responseObserver){
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
}
