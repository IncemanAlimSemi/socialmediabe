package com.alseinn.socialmedia.service.post;

import com.alseinn.socialmedia.dao.user.PostRepository;
import com.alseinn.socialmedia.entity.post.Post;
import com.alseinn.socialmedia.entity.user.User;
import com.alseinn.socialmedia.entity.user.enums.Gender;
import com.alseinn.socialmedia.entity.user.enums.Role;
import com.alseinn.socialmedia.request.post.CreatePostRequest;
import com.alseinn.socialmedia.request.post.DeletePostRequest;
import com.alseinn.socialmedia.response.post.PostResponse;
import com.alseinn.socialmedia.service.post.impl.PostServiceImpl;
import com.alseinn.socialmedia.service.user.UserService;
import com.alseinn.socialmedia.utils.UserUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

class PostServiceTests {

    private UserService userService;
    private PostRepository postRepository;
    private ObjectMapper mapper;
    private UserUtils userUtils;
    private PostService postService;

    @BeforeEach
    void setup() {
        userService = Mockito.mock(UserService.class);
        postRepository = Mockito.mock(PostRepository.class);
        mapper = Mockito.mock(ObjectMapper.class);
        userUtils = Mockito.mock(UserUtils.class);

        postService = new PostServiceImpl(userService, postRepository, mapper, userUtils);
    }

    @Test
    void shouldReturnPostResponseWithIsSuccessTrue_whenCreatePostRequestParametersAreFillTrue() throws JsonProcessingException {
        String username = "testUser";
        CreatePostRequest createPostRequest = new CreatePostRequest(username, "postTitle", "postContent");
        User user = new User("test", "user", Gender.MAN, "testUser@test.com", "1111111111", username, "123", Role.USER);
        Post post = new Post(0, createPostRequest.getPostTitle(), createPostRequest.getPostContent(), 0, 0, user, null);
        PostResponse expectedResponse = new PostResponse(true, "Post created with success");

        Mockito.when(userService.findByUsername(username)).thenReturn(user);
        Mockito.when(userUtils.isSessionUser(user)).thenReturn(true);
        Mockito.when(postRepository.save(post)).thenReturn(post);
        Mockito.when(mapper.writeValueAsString(post)).thenReturn(Mockito.anyString());

        PostResponse result = postService.createPost(createPostRequest);

        Assertions.assertEquals(expectedResponse, result);

        Mockito.verify(userService).findByUsername(username);
        Mockito.verify(userUtils).isSessionUser(user);
        Mockito.verify(postRepository).save(post);
        Mockito.verify(mapper).writeValueAsString(post);

    }

    @Test
    void shouldReturnPostResponseWithIsSuccessFalse_whenUserIsNotFoundWithCreatePostRequestUsername() throws JsonProcessingException {
        String username = "testUser";
        CreatePostRequest createPostRequest = new CreatePostRequest(username, "postTitle", "postContent");
        PostResponse expectedResponse = new PostResponse(false, "User not found");

        Mockito.when(userService.findByUsername(username)).thenReturn(null);
        Mockito.when(mapper.writeValueAsString(createPostRequest)).thenReturn(Mockito.anyString());

        PostResponse result = postService.createPost(createPostRequest);

        Assertions.assertEquals(expectedResponse, result);

        Mockito.verify(userService).findByUsername(username);
        Mockito.verify(mapper).writeValueAsString(createPostRequest);

    }

    @Test
    void shouldReturnPostResponseWithIsSuccessFalse_whenUserIsNotSessionUserWithCreatePostRequestUsername() throws JsonProcessingException {
        String username = "testUser";
        CreatePostRequest createPostRequest = new CreatePostRequest(username, "postTitle", "postContent");
        User user = new User("test", "user", Gender.MAN, "testUser@test.com", "1111111111", username, "123", Role.USER);
        PostResponse expectedResponse = new PostResponse(false, "This user is not session user.");

        Mockito.when(userService.findByUsername(username)).thenReturn(user);
        Mockito.when(userUtils.isSessionUser(user)).thenReturn(false);
        Mockito.when(mapper.writeValueAsString(createPostRequest)).thenReturn(Mockito.anyString());

        PostResponse result = postService.createPost(createPostRequest);

        Assertions.assertEquals(expectedResponse, result);

        Mockito.verify(userService).findByUsername(username);
        Mockito.verify(userUtils).isSessionUser(user);
        Mockito.verify(mapper).writeValueAsString(createPostRequest);

    }

    @Test
    void shouldReturnPostResponseWithIsSuccessFalse_whenPostCannotSaveDatabase() throws JsonProcessingException {
        String username = "testUser";
        CreatePostRequest createPostRequest = new CreatePostRequest(username, "postTitle", "postContent");
        User user = new User("test", "user", Gender.MAN, "testUser@test.com", "1111111111", username, "123", Role.USER);
        Post post = new Post(0, createPostRequest.getPostTitle(), createPostRequest.getPostContent(), 0, 0, user, null);
        PostResponse expectedResponse = new PostResponse(false, "Post could not be created");

        Mockito.when(userService.findByUsername(username)).thenReturn(user);
        Mockito.when(userUtils.isSessionUser(user)).thenReturn(true);
        Mockito.when(postRepository.save(post)).thenThrow(new NullPointerException("Exception"));
        Mockito.when(mapper.writeValueAsString(createPostRequest)).thenReturn(Mockito.anyString());

        PostResponse result = postService.createPost(createPostRequest);

        Assertions.assertEquals(expectedResponse, result);

        Mockito.verify(userService).findByUsername(username);
        Mockito.verify(userUtils).isSessionUser(user);
        Mockito.verify(postRepository).save(post);
        Mockito.verify(mapper).writeValueAsString(post);

    }

    @Test
    void shouldReturnPostResponseWithIsSuccessTrue_whenDeletePostRequestParametersAreFillTrue() throws JsonProcessingException {
        Long postId = 0L;
        String username = "testUser";
        DeletePostRequest deletePostRequest = new DeletePostRequest(postId, username);
        User user = new User("test", "user", Gender.MAN, "testUser@test.com", "1111111111", username, "123", Role.USER);
        Post post = new Post(postId, "postTitle", "postContent", 0, 0, user, null);
        PostResponse expectedResponse = new PostResponse(true, "Post deleted with success");

        Mockito.when(userService.findByUsername(username)).thenReturn(user);
        Mockito.when(userUtils.isSessionUser(user)).thenReturn(true);
        Mockito.when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        Mockito.when(mapper.writeValueAsString(post)).thenReturn(Mockito.anyString());

        PostResponse result = postService.deletePost(deletePostRequest);

        Assertions.assertEquals(expectedResponse, result);

        Mockito.verify(userService).findByUsername(username);
        Mockito.verify(userUtils).isSessionUser(user);
        Mockito.verify(postRepository).findById(postId);
        Mockito.verify(postRepository).delete(post);
        Mockito.verify(mapper).writeValueAsString(post);

    }

    @Test
    void shouldReturnPostResponseWithIsSuccessFalse_whenUserIsNotFoundWithDeletePostRequestUsername() throws JsonProcessingException {
        Long postId = 0L;
        String username = "testUser";
        DeletePostRequest deletePostRequest = new DeletePostRequest(postId, username);
        PostResponse expectedResponse = new PostResponse(false, "User not found");

        Mockito.when(userService.findByUsername(username)).thenReturn(null);
        Mockito.when(mapper.writeValueAsString(deletePostRequest)).thenReturn(Mockito.anyString());

        PostResponse result = postService.deletePost(deletePostRequest);

        Assertions.assertEquals(expectedResponse, result);

        Mockito.verify(userService).findByUsername(username);
        Mockito.verify(mapper).writeValueAsString(deletePostRequest);

    }

    @Test
    void shouldReturnPostResponseWithIsSuccessFalse_whenUserIsNotSessionUserWithDeletePostRequestUsername() throws JsonProcessingException {
        Long postId = 0L;
        String username = "testUser";
        DeletePostRequest deletePostRequest = new DeletePostRequest(postId, username);
        User user = new User("test", "user", Gender.MAN, "testUser@test.com", "1111111111", username, "123", Role.USER);
        PostResponse expectedResponse = new PostResponse(false, "This user is not session user.");

        Mockito.when(userService.findByUsername(username)).thenReturn(user);
        Mockito.when(userUtils.isSessionUser(user)).thenReturn(false);
        Mockito.when(mapper.writeValueAsString(deletePostRequest)).thenReturn(Mockito.anyString());

        PostResponse result = postService.deletePost(deletePostRequest);

        Assertions.assertEquals(expectedResponse, result);

        Mockito.verify(userService).findByUsername(username);
        Mockito.verify(userUtils).isSessionUser(user);
        Mockito.verify(mapper).writeValueAsString(deletePostRequest);

    }

    @Test
    void shouldReturnPostResponseWithIsSuccessFalse_whenPostIsNotFoundWithDeletePostRequestPostId() throws JsonProcessingException {
        Long postId = 0L;
        String username = "testUser";
        DeletePostRequest deletePostRequest = new DeletePostRequest(postId, username);
        User user = new User("test", "user", Gender.MAN, "testUser@test.com", "1111111111", username, "123", Role.USER);
        PostResponse expectedResponse = new PostResponse(false, "Post not found");

        Mockito.when(userService.findByUsername(username)).thenReturn(user);
        Mockito.when(userUtils.isSessionUser(user)).thenReturn(true);
        Mockito.when(postRepository.findById(postId)).thenReturn(Optional.empty());
        Mockito.when(mapper.writeValueAsString(deletePostRequest)).thenReturn(Mockito.anyString());

        PostResponse result = postService.deletePost(deletePostRequest);

        Assertions.assertEquals(expectedResponse, result);

        Mockito.verify(userService).findByUsername(username);
        Mockito.verify(userUtils).isSessionUser(user);
        Mockito.verify(postRepository).findById(postId);
        Mockito.verify(mapper).writeValueAsString(deletePostRequest);

    }

    @Test
    void shouldReturnPostResponseWithIsSuccessFalse_whenUserIsNotOwnerOfPostWithDeletePostRequestUsername() throws JsonProcessingException {
        Long postId = 0L;
        String username = "testUser";
        String ownerOfPostUsername = "ownerOfPostUser";
        DeletePostRequest deletePostRequest = new DeletePostRequest(postId, ownerOfPostUsername);
        User user = new User("test", "user", Gender.MAN, "testUser@test.com", "1111111111", username, "123", Role.USER);
        User ownerOfPostUser = new User("test", "user", Gender.MAN, "testUser@test.com", "1111111111", ownerOfPostUsername, "123", Role.USER);
        Post post = new Post(postId, "postTitle", "postContent", 0, 0, user, null);
        PostResponse expectedResponse = new PostResponse(false, "This user is not owner this post.");

        Mockito.when(userService.findByUsername(ownerOfPostUsername)).thenReturn(ownerOfPostUser);
        Mockito.when(userUtils.isSessionUser(ownerOfPostUser)).thenReturn(true);
        Mockito.when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        Mockito.when(mapper.writeValueAsString(deletePostRequest)).thenReturn(Mockito.anyString());

        PostResponse result = postService.deletePost(deletePostRequest);

        Assertions.assertEquals(expectedResponse, result);

        Mockito.verify(userService).findByUsername(ownerOfPostUsername);
        Mockito.verify(userUtils).isSessionUser(ownerOfPostUser);
        Mockito.verify(postRepository).findById(postId);
        Mockito.verify(mapper).writeValueAsString(post);

    }

    @Test
    void shouldReturnPostResponseWithIsSuccessFalse_whenPostCannotDeleteFromDatabase() throws JsonProcessingException {
        Long postId = 0L;
        String username = "testUser";
        DeletePostRequest deletePostRequest = new DeletePostRequest(postId, username);
        User user = new User("test", "user", Gender.MAN, "testUser@test.com", "1111111111", username, "123", Role.USER);
        Post post = new Post(postId, "postTitle", "postContent", 0, 0, user, null);
        PostResponse expectedResponse = new PostResponse(false, "Post could not be deleted");

        Mockito.when(userService.findByUsername(username)).thenReturn(user);
        Mockito.when(userUtils.isSessionUser(user)).thenReturn(true);
        Mockito.when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        Mockito.doThrow(new NullPointerException("Exception")).when(postRepository).delete(post);
        Mockito.when(mapper.writeValueAsString(deletePostRequest)).thenReturn(Mockito.anyString());

        PostResponse result = postService.deletePost(deletePostRequest);

        Assertions.assertEquals(expectedResponse, result);

        Mockito.verify(userService).findByUsername(username);
        Mockito.verify(userUtils).isSessionUser(user);
        Mockito.verify(postRepository).findById(postId);
        Mockito.verify(postRepository).delete(post);
        Mockito.verify(mapper).writeValueAsString(post);

    }

    @Test
    void shouldReturnPost_whenPostFindWithIdInDatabase() {
        long postId = 0L;
        String username = "testUser";
        User user = new User("test", "user", Gender.MAN, "testUser@test.com", "1111111111", username, "123", Role.USER);
        Post post = new Post(postId, "postTitle", "postContent", 0, 0, user, null);

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
