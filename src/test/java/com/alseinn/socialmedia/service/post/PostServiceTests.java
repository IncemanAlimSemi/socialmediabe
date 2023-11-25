package com.alseinn.socialmedia.service.post;

import com.alseinn.socialmedia.dao.user.PostRepository;
import com.alseinn.socialmedia.entity.post.Post;
import com.alseinn.socialmedia.entity.user.User;
import com.alseinn.socialmedia.entity.user.enums.Gender;
import com.alseinn.socialmedia.entity.user.enums.Role;
import com.alseinn.socialmedia.request.post.CreatePostRequest;
import com.alseinn.socialmedia.request.post.DeletePostRequest;
import com.alseinn.socialmedia.response.general.GeneralInformationResponse;
import com.alseinn.socialmedia.service.post.impl.PostServiceImpl;
import com.alseinn.socialmedia.service.user.UserService;
import com.alseinn.socialmedia.utils.ResponseUtils;
import com.alseinn.socialmedia.utils.UserUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

class PostServiceTests {

    private UserService userService;
    private PostRepository postRepository;
    private ObjectMapper mapper;
    private UserUtils userUtils;
    private PostService postService;
    private ResponseUtils responseUtils;

    @BeforeEach
    void setup() {
        userService = Mockito.mock(UserService.class);
        postRepository = Mockito.mock(PostRepository.class);
        mapper = Mockito.mock(ObjectMapper.class);
        userUtils = Mockito.mock(UserUtils.class);
        responseUtils = Mockito.mock(ResponseUtils.class);

        postService = new PostServiceImpl(userService, postRepository, mapper, userUtils, responseUtils);
    }

    @Test
    void shouldReturnGeneralInformationResponseWithIsSuccessTrue_whenCreatePostRequestParametersAreFillTrue() throws IOException {
        String username = "testUser";
        Boolean isSuccess = true;
        String message = "Post created with success.";
        CreatePostRequest createPostRequest = new CreatePostRequest(username, "postTitle", "postContent");
        User user = new User("test", "user", Gender.MAN, "testUser@test.com", "1111111111", username, "123", Role.USER, null);
        Post post = new Post(0, createPostRequest.getTitle(), createPostRequest.getContent(), 0, 0, user, null, null);
        GeneralInformationResponse expectedResponse = new GeneralInformationResponse(isSuccess, message);

        Mockito.when(userService.findByUsername(username)).thenReturn(user);
        Mockito.when(userUtils.isSessionUser(user)).thenReturn(true);
        Mockito.when(postRepository.save(post)).thenReturn(post);
        Mockito.when(mapper.writeValueAsString(post)).thenReturn("String");
        Mockito.when(responseUtils.createGeneralInformationResponse(Mockito.eq(isSuccess), Mockito.eq(message)))
                .thenReturn(expectedResponse);

        GeneralInformationResponse result = postService.createPost(createPostRequest);

        Assertions.assertEquals(expectedResponse, result);

        Mockito.verify(userService).findByUsername(username);
        Mockito.verify(userUtils).isSessionUser(user);
        Mockito.verify(postRepository).save(post);
        Mockito.verify(mapper).writeValueAsString(post);
        Mockito.verify(responseUtils).createGeneralInformationResponse(Mockito.eq(isSuccess), Mockito.eq(message));

    }

    @Test
    void shouldReturnGeneralInformationResponseWithIsSuccessFalse_whenUserIsNotFoundWithCreatePostRequestUsername() throws IOException {
        String username = "testUser";
        Boolean isSuccess = false;
        String message = "User not found.";
        CreatePostRequest createPostRequest = new CreatePostRequest(username, "postTitle", "postContent");
        GeneralInformationResponse expectedResponse = new GeneralInformationResponse(isSuccess, message);

        Mockito.when(userService.findByUsername(username)).thenReturn(null);
        Mockito.when(mapper.writeValueAsString(createPostRequest)).thenReturn("String");
        Mockito.when(responseUtils.createGeneralInformationResponse(Mockito.eq(isSuccess), Mockito.eq(message)))
                .thenReturn(expectedResponse);

        GeneralInformationResponse result = postService.createPost(createPostRequest);

        Assertions.assertEquals(expectedResponse, result);

        Mockito.verify(userService).findByUsername(username);
        Mockito.verify(mapper).writeValueAsString(createPostRequest);
        Mockito.verify(responseUtils).createGeneralInformationResponse(Mockito.eq(isSuccess), Mockito.eq(message));

    }

    @Test
    void shouldReturnGeneralInformationResponseWithIsSuccessFalse_whenUserIsNotSessionUserWithCreatePostRequestUsername() throws IOException {
        String username = "testUser";
        Boolean isSuccess = false;
        String message = "This user is not session user.";
        CreatePostRequest createPostRequest = new CreatePostRequest(username, "postTitle", "postContent");
        User user = new User("test", "user", Gender.MAN, "testUser@test.com", "1111111111", username, "123", Role.USER, null);
        GeneralInformationResponse expectedResponse = new GeneralInformationResponse(isSuccess, message);

        Mockito.when(userService.findByUsername(username)).thenReturn(user);
        Mockito.when(userUtils.isSessionUser(user)).thenReturn(false);
        Mockito.when(mapper.writeValueAsString(createPostRequest)).thenReturn("String");
        Mockito.when(responseUtils.createGeneralInformationResponse(Mockito.eq(isSuccess), Mockito.eq(message)))
                .thenReturn(expectedResponse);

        GeneralInformationResponse result = postService.createPost(createPostRequest);

        Assertions.assertEquals(expectedResponse, result);

        Mockito.verify(userService).findByUsername(username);
        Mockito.verify(userUtils).isSessionUser(user);
        Mockito.verify(mapper).writeValueAsString(createPostRequest);
        Mockito.verify(responseUtils).createGeneralInformationResponse(Mockito.eq(isSuccess), Mockito.eq(message));

    }

    @Test
    void shouldReturnGeneralInformationResponseWithIsSuccessFalse_whenPostCannotSaveDatabase() throws IOException {
        String username = "testUser";
        Boolean isSuccess = false;
        String message = "Post could not be created.";
        CreatePostRequest createPostRequest = new CreatePostRequest(username, "postTitle", "postContent");
        User user = new User("test", "user", Gender.MAN, "testUser@test.com", "1111111111", username, "123", Role.USER, null);
        Post post = new Post(0, createPostRequest.getTitle(), createPostRequest.getContent(), 0, 0, user, null, new Date(System.currentTimeMillis()));
        GeneralInformationResponse expectedResponse = new GeneralInformationResponse(isSuccess, message);

        Mockito.when(userService.findByUsername(username)).thenReturn(user);
        Mockito.when(userUtils.isSessionUser(user)).thenReturn(true);
        Mockito.when(postRepository.save(post)).thenThrow(new NullPointerException("Exception"));
        Mockito.when(mapper.writeValueAsString(createPostRequest)).thenReturn("String");
        Mockito.when(responseUtils.createGeneralInformationResponse(Mockito.eq(isSuccess), Mockito.eq(message)))
                .thenReturn(expectedResponse);

        GeneralInformationResponse result = postService.createPost(createPostRequest);

        Assertions.assertEquals(expectedResponse, result);

        Mockito.verify(userService).findByUsername(username);
        Mockito.verify(userUtils).isSessionUser(user);
        Mockito.verify(postRepository).save(post);
        Mockito.verify(mapper).writeValueAsString(post);
        Mockito.verify(responseUtils).createGeneralInformationResponse(Mockito.eq(isSuccess), Mockito.eq(message));

    }

    @Test
    void shouldReturnGeneralInformationResponseWithIsSuccessTrue_whenDeletePostRequestParametersAreFillTrue() throws IOException {
        Long postId = 0L;
        String username = "testUser";
        Boolean isSuccess = true;
        String message = "Post deleted with success.";
        DeletePostRequest deletePostRequest = new DeletePostRequest(postId, username);
        User user = new User("test", "user", Gender.MAN, "testUser@test.com", "1111111111", username, "123", Role.USER, null);
        Post post = new Post(postId, "postTitle", "postContent", 0, 0, user, null, null);
        GeneralInformationResponse expectedResponse = new GeneralInformationResponse(isSuccess, message);

        Mockito.when(userService.findByUsername(username)).thenReturn(user);
        Mockito.when(userUtils.isSessionUser(user)).thenReturn(true);
        Mockito.when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        Mockito.when(mapper.writeValueAsString(post)).thenReturn("String");
        Mockito.when(responseUtils.createGeneralInformationResponse(Mockito.eq(isSuccess), Mockito.eq(message)))
                .thenReturn(expectedResponse);

        GeneralInformationResponse result = postService.deletePost(deletePostRequest);

        Assertions.assertEquals(expectedResponse, result);

        Mockito.verify(userService).findByUsername(username);
        Mockito.verify(userUtils).isSessionUser(user);
        Mockito.verify(postRepository).findById(postId);
        Mockito.verify(postRepository).delete(post);
        Mockito.verify(mapper).writeValueAsString(post);
        Mockito.verify(responseUtils).createGeneralInformationResponse(Mockito.eq(isSuccess), Mockito.eq(message));

    }

    @Test
    void shouldReturnGeneralInformationResponseWithIsSuccessFalse_whenUserIsNotFoundWithDeletePostRequestUsername() throws IOException {
        Long postId = 0L;
        String username = "testUser";
        Boolean isSuccess = false;
        String message = "User not found.";
        DeletePostRequest deletePostRequest = new DeletePostRequest(postId, username);
        GeneralInformationResponse expectedResponse = new GeneralInformationResponse(isSuccess, message);

        Mockito.when(userService.findByUsername(username)).thenReturn(null);
        Mockito.when(mapper.writeValueAsString(deletePostRequest)).thenReturn("String");
        Mockito.when(responseUtils.createGeneralInformationResponse(Mockito.eq(isSuccess), Mockito.eq(message)))
                .thenReturn(expectedResponse);

        GeneralInformationResponse result = postService.deletePost(deletePostRequest);

        Assertions.assertEquals(expectedResponse, result);

        Mockito.verify(userService).findByUsername(username);
        Mockito.verify(mapper).writeValueAsString(deletePostRequest);
        Mockito.verify(responseUtils).createGeneralInformationResponse(Mockito.eq(isSuccess), Mockito.eq(message));

    }

    @Test
    void shouldReturnGeneralInformationResponseWithIsSuccessFalse_whenUserIsNotSessionUserWithDeletePostRequestUsername() throws IOException {
        Long postId = 0L;
        String username = "testUser";
        Boolean isSuccess = false;
        String message = "This user is not session user.";
        DeletePostRequest deletePostRequest = new DeletePostRequest(postId, username);
        User user = new User("test", "user", Gender.MAN, "testUser@test.com", "1111111111", username, "123", Role.USER, null);
        GeneralInformationResponse expectedResponse = new GeneralInformationResponse(false, "This user is not session user.");

        Mockito.when(userService.findByUsername(username)).thenReturn(user);
        Mockito.when(userUtils.isSessionUser(user)).thenReturn(false);
        Mockito.when(mapper.writeValueAsString(deletePostRequest)).thenReturn("String");
        Mockito.when(responseUtils.createGeneralInformationResponse(Mockito.eq(isSuccess), Mockito.eq(message)))
                .thenReturn(expectedResponse);

        GeneralInformationResponse result = postService.deletePost(deletePostRequest);

        Assertions.assertEquals(expectedResponse, result);

        Mockito.verify(userService).findByUsername(username);
        Mockito.verify(userUtils).isSessionUser(user);
        Mockito.verify(mapper).writeValueAsString(deletePostRequest);
        Mockito.verify(responseUtils).createGeneralInformationResponse(Mockito.eq(isSuccess), Mockito.eq(message));

    }

    @Test
    void shouldReturnGeneralInformationResponseWithIsSuccessFalse_whenPostIsNotFoundWithDeletePostRequestPostId() throws IOException {
        Long postId = 0L;
        String username = "testUser";
        Boolean isSuccess = false;
        String message = "Post not found.";
        DeletePostRequest deletePostRequest = new DeletePostRequest(postId, username);
        User user = new User("test", "user", Gender.MAN, "testUser@test.com", "1111111111", username, "123", Role.USER, null);
        GeneralInformationResponse expectedResponse = new GeneralInformationResponse(false, "Post not found.");

        Mockito.when(userService.findByUsername(username)).thenReturn(user);
        Mockito.when(userUtils.isSessionUser(user)).thenReturn(true);
        Mockito.when(postRepository.findById(postId)).thenReturn(Optional.empty());
        Mockito.when(mapper.writeValueAsString(deletePostRequest)).thenReturn("String");
        Mockito.when(responseUtils.createGeneralInformationResponse(Mockito.eq(isSuccess), Mockito.eq(message)))
                .thenReturn(expectedResponse);

        GeneralInformationResponse result = postService.deletePost(deletePostRequest);

        Assertions.assertEquals(expectedResponse, result);

        Mockito.verify(userService).findByUsername(username);
        Mockito.verify(userUtils).isSessionUser(user);
        Mockito.verify(postRepository).findById(postId);
        Mockito.verify(mapper).writeValueAsString(deletePostRequest);
        Mockito.verify(responseUtils).createGeneralInformationResponse(Mockito.eq(isSuccess), Mockito.eq(message));

    }

    @Test
    void shouldReturnGeneralInformationResponseWithIsSuccessFalse_whenUserIsNotOwnerOfPostWithDeletePostRequestUsername() throws IOException {
        Long postId = 0L;
        String username = "testUser";
        String ownerOfPostUsername = "ownerOfPostUser";
        Boolean isSuccess = false;
        String message = "This user is not owner this post.";
        DeletePostRequest deletePostRequest = new DeletePostRequest(postId, ownerOfPostUsername);
        User user = new User("test", "user", Gender.MAN, "testUser@test.com", "1111111111", username, "123", Role.USER, null);
        User ownerOfPostUser = new User("test", "user", Gender.MAN, "testUser@test.com", "1111111111", ownerOfPostUsername, "123", Role.USER, null);
        Post post = new Post(postId, "postTitle", "postContent", 0, 0, user, null, null);
        GeneralInformationResponse expectedResponse = new GeneralInformationResponse(isSuccess, message);

        Mockito.when(userService.findByUsername(ownerOfPostUsername)).thenReturn(ownerOfPostUser);
        Mockito.when(userUtils.isSessionUser(ownerOfPostUser)).thenReturn(true);
        Mockito.when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        Mockito.when(mapper.writeValueAsString(deletePostRequest)).thenReturn("String");
        Mockito.when(responseUtils.createGeneralInformationResponse(Mockito.eq(isSuccess), Mockito.eq(message)))
                .thenReturn(expectedResponse);

        GeneralInformationResponse result = postService.deletePost(deletePostRequest);

        Assertions.assertEquals(expectedResponse, result);

        Mockito.verify(userService).findByUsername(ownerOfPostUsername);
        Mockito.verify(userUtils).isSessionUser(ownerOfPostUser);
        Mockito.verify(postRepository).findById(postId);
        Mockito.verify(mapper).writeValueAsString(post);
        Mockito.verify(responseUtils).createGeneralInformationResponse(Mockito.eq(isSuccess), Mockito.eq(message));

    }

    @Test
    void shouldReturnGeneralInformationResponseWithIsSuccessFalse_whenPostCannotDeleteFromDatabase() throws IOException {
        Long postId = 0L;
        String username = "testUser";
        Boolean isSuccess = false;
        String message = "Post could not be deleted.";
        DeletePostRequest deletePostRequest = new DeletePostRequest(postId, username);
        User user = new User("test", "user", Gender.MAN, "testUser@test.com", "1111111111", username, "123", Role.USER, null);
        Post post = new Post(postId, "postTitle", "postContent", 0, 0, user, null, null);
        GeneralInformationResponse expectedResponse = new GeneralInformationResponse(isSuccess, message);

        Mockito.when(userService.findByUsername(username)).thenReturn(user);
        Mockito.when(userUtils.isSessionUser(user)).thenReturn(true);
        Mockito.when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        Mockito.doThrow(new NullPointerException("Exception")).when(postRepository).delete(post);
        Mockito.when(mapper.writeValueAsString(deletePostRequest)).thenReturn("String");
        Mockito.when(responseUtils.createGeneralInformationResponse(Mockito.eq(isSuccess), Mockito.eq(message)))
                .thenReturn(expectedResponse);

        GeneralInformationResponse result = postService.deletePost(deletePostRequest);

        Assertions.assertEquals(expectedResponse, result);

        Mockito.verify(userService).findByUsername(username);
        Mockito.verify(userUtils).isSessionUser(user);
        Mockito.verify(postRepository).findById(postId);
        Mockito.verify(postRepository).delete(post);
        Mockito.verify(mapper).writeValueAsString(post);
        Mockito.verify(responseUtils).createGeneralInformationResponse(Mockito.eq(isSuccess), Mockito.eq(message));

    }

    @Test
    void shouldReturnPost_whenPostFindWithIdInDatabase() {
        long postId = 0L;
        String username = "testUser";
        User user = new User("test", "user", Gender.MAN, "testUser@test.com", "1111111111", username, "123", Role.USER, null);
        Post post = new Post(postId, "postTitle", "postContent", 0, 0, user, null, null);

        Mockito.when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        Post result = postService.findById(postId);

        Assertions.assertEquals(post, result);

        Mockito.verify(postRepository).findById(postId);
    }

    @Test
    void shouldReturnPost_whenPostIsNotFindWithIdInDatabase() {
        long postId = 0L;

        Post result = postService.findById(postId);

        Assertions.assertNull(result);

        Mockito.verify(postRepository).findById(postId);
    }
}
