package com.alseinn.socialmedia.service.comment;

import com.alseinn.socialmedia.dao.user.CommentRepository;
import com.alseinn.socialmedia.entity.comment.Comment;
import com.alseinn.socialmedia.entity.post.Post;
import com.alseinn.socialmedia.entity.user.User;
import com.alseinn.socialmedia.entity.user.enums.Gender;
import com.alseinn.socialmedia.entity.user.enums.Role;
import com.alseinn.socialmedia.request.comment.CreateCommentRequest;
import com.alseinn.socialmedia.request.comment.DeleteCommentRequest;
import com.alseinn.socialmedia.response.general.GeneralInformationResponse;
import com.alseinn.socialmedia.service.comment.impl.CommentServiceImpl;
import com.alseinn.socialmedia.service.post.PostService;
import com.alseinn.socialmedia.service.user.UserService;
import com.alseinn.socialmedia.utils.ResponseUtils;
import com.alseinn.socialmedia.utils.UserUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

class CommentServiceTests {

    private CommentRepository commentRepository;
    private UserService userService;
    private UserUtils userUtils;
    private ObjectMapper mapper;
    private PostService postService;
    private CommentService commentService;
    private ResponseUtils responseUtils;


    @BeforeEach
    void setup() {
        commentRepository = Mockito.mock(CommentRepository.class);
        userService = Mockito.mock(UserService.class);
        userUtils = Mockito.mock(UserUtils.class);
        mapper = Mockito.mock(ObjectMapper.class);
        postService = Mockito.mock(PostService.class);
        responseUtils = Mockito.mock(ResponseUtils.class);

        commentService = new CommentServiceImpl(commentRepository, userService, userUtils, mapper, postService, responseUtils);
    }

    @Test
    void shouldReturnGeneralInformationResponseWithIsSuccessTrue_whenCreateCommentRequestParametersAreFillTrue() throws IOException {
        Long postId = 1L;
        String username = "testUser";
        Boolean isSuccess = true;
        String message = "Comment created with success.";
        CreateCommentRequest createCommentRequest = new CreateCommentRequest(postId, "Content", "testUser");
        User user = new User("test", "user", Gender.MAN, "testUser@test.com", "1111111111", username, "123", Role.USER, null);
        Post post = new Post(postId, "firstPost", "firstPost", 0, 0, user, new ArrayList<>(), null);
        Comment comment = new Comment(0, "Content", post, user, null);
        GeneralInformationResponse expectedResponse = new GeneralInformationResponse(isSuccess, message);

        Mockito.when(userService.findByUsername(username)).thenReturn(user);
        Mockito.when(userUtils.isSessionUser(user)).thenReturn(true);
        Mockito.when(postService.findById(postId)).thenReturn(post);
        Mockito.when(commentRepository.save(comment)).thenReturn(comment);
        Mockito.when(mapper.writeValueAsString(comment)).thenReturn("String");
        Mockito.when(responseUtils.createGeneralInformationResponse(Mockito.eq(isSuccess), Mockito.eq(message)))
                .thenReturn(expectedResponse);

        GeneralInformationResponse result = commentService.createComment(createCommentRequest);

        Assertions.assertEquals(expectedResponse, result);

        Mockito.verify(userService).findByUsername(username);
        Mockito.verify(userUtils).isSessionUser(user);
        Mockito.verify(postService).findById(postId);
        Mockito.verify(commentRepository).save(comment);
        Mockito.verify(mapper).writeValueAsString(comment);
        Mockito.verify(responseUtils).createGeneralInformationResponse(Mockito.eq(isSuccess), Mockito.eq(message));

    }

    @Test
    void shouldReturnGeneralInformationResponseWithIsSuccessFalse_whenUserNotFoundWithCreateCommentRequestUsername() throws IOException {
        Long postId = 1L;
        String username = "testUser";
        Boolean isSuccess = false;
        String message = "User not found.";
        CreateCommentRequest createCommentRequest = new CreateCommentRequest(postId, "Content", "testUser");
        GeneralInformationResponse expectedResponse = new GeneralInformationResponse(isSuccess, message);

        Mockito.when(userService.findByUsername(username)).thenReturn(null);
        Mockito.when(mapper.writeValueAsString(createCommentRequest)).thenReturn("String");
        Mockito.when(responseUtils.createGeneralInformationResponse(Mockito.eq(isSuccess), Mockito.eq(message)))
                .thenReturn(expectedResponse);

        GeneralInformationResponse result = commentService.createComment(createCommentRequest);

        Assertions.assertEquals(expectedResponse, result);

        Mockito.verify(userService).findByUsername(username);
        Mockito.verify(mapper).writeValueAsString(createCommentRequest);
        Mockito.verify(responseUtils).createGeneralInformationResponse(Mockito.eq(isSuccess), Mockito.eq(message));

    }

    @Test
    void shouldReturnGeneralInformationResponseWithIsSuccessFalse_whenUserIsNotSessionUserWithCreateCommentRequestUsername() throws IOException {
        Long postId = 1L;
        String username = "testUser";
        Boolean isSuccess = false;
        String message = "This user is not session user.";
        User user = new User("test", "user", Gender.MAN, "testUser@test.com", "1111111111", username, "123", Role.USER, null);
        CreateCommentRequest createCommentRequest = new CreateCommentRequest(postId, "Content", "testUser");
        GeneralInformationResponse expectedResponse = new GeneralInformationResponse(isSuccess, message);

        Mockito.when(userService.findByUsername(username)).thenReturn(user);
        Mockito.when(userUtils.isSessionUser(user)).thenReturn(false);
        Mockito.when(mapper.writeValueAsString(createCommentRequest)).thenReturn("String");
        Mockito.when(responseUtils.createGeneralInformationResponse(Mockito.eq(isSuccess), Mockito.eq(message)))
                .thenReturn(expectedResponse);

        GeneralInformationResponse result = commentService.createComment(createCommentRequest);

        Assertions.assertEquals(expectedResponse, result);

        Mockito.verify(userService).findByUsername(username);
        Mockito.verify(userUtils).isSessionUser(user);
        Mockito.verify(mapper).writeValueAsString(createCommentRequest);
        Mockito.verify(responseUtils).createGeneralInformationResponse(Mockito.eq(isSuccess), Mockito.eq(message));

    }

    @Test
    void shouldReturnGeneralInformationResponseWithIsSuccessFalse_whenPostIsNotFoundWithCreateCommentRequestPostId() throws IOException {
        Long postId = 1L;
        String username = "testUser";
        Boolean isSuccess = false;
        String message = "Post not found.";
        CreateCommentRequest createCommentRequest = new CreateCommentRequest(postId, "Content", "testUser");
        User user = new User("test", "user", Gender.MAN, "testUser@test.com", "1111111111", username, "123", Role.USER, null);
        GeneralInformationResponse expectedResponse = new GeneralInformationResponse(isSuccess, message);

        Mockito.when(userService.findByUsername(username)).thenReturn(user);
        Mockito.when(userUtils.isSessionUser(user)).thenReturn(true);
        Mockito.when(postService.findById(postId)).thenReturn(null);
        Mockito.when(mapper.writeValueAsString(createCommentRequest)).thenReturn("String");
        Mockito.when(responseUtils.createGeneralInformationResponse(Mockito.eq(isSuccess), Mockito.eq(message)))
                .thenReturn(expectedResponse);

        GeneralInformationResponse result = commentService.createComment(createCommentRequest);

        Assertions.assertEquals(expectedResponse, result);

        Mockito.verify(userService).findByUsername(username);
        Mockito.verify(userUtils).isSessionUser(user);
        Mockito.verify(postService).findById(postId);
        Mockito.verify(mapper).writeValueAsString(createCommentRequest);
        Mockito.verify(responseUtils).createGeneralInformationResponse(Mockito.eq(isSuccess), Mockito.eq(message));

    }

    @Test
    void shouldReturnGeneralInformationResponseWithIsSuccessFalse_whenCommentCannotSaveDatabase() throws IOException {
        Long postId = 1L;
        String username = "testUser";
        Boolean isSuccess = false;
        String message = "Comment could not be created.";
        CreateCommentRequest createCommentRequest = new CreateCommentRequest(postId, "Content", "testUser");
        User user = new User("test", "user", Gender.MAN, "testUser@test.com", "1111111111", username, "123", Role.USER, null);
        Post post = new Post(postId, "firstPost", "firstPost", 0, 0, user, new ArrayList<>(), null);
        Comment comment = new Comment(0, "Content", post, user, null);
        GeneralInformationResponse expectedResponse = new GeneralInformationResponse(isSuccess, message);

        Mockito.when(userService.findByUsername(username)).thenReturn(user);
        Mockito.when(userUtils.isSessionUser(user)).thenReturn(true);
        Mockito.when(postService.findById(postId)).thenReturn(post);
        Mockito.when(commentRepository.save(comment)).thenThrow(new NullPointerException("Exception"));
        Mockito.when(mapper.writeValueAsString(post)).thenReturn("String");
        Mockito.when(responseUtils.createGeneralInformationResponse(Mockito.eq(isSuccess), Mockito.eq(message)))
                .thenReturn(expectedResponse);

        GeneralInformationResponse result = commentService.createComment(createCommentRequest);

        Assertions.assertEquals(expectedResponse, result);

        Mockito.verify(userService).findByUsername(username);
        Mockito.verify(userUtils).isSessionUser(user);
        Mockito.verify(postService).findById(postId);
        Mockito.verify(commentRepository).save(comment);
        Mockito.verify(mapper).writeValueAsString(post);
        Mockito.verify(responseUtils).createGeneralInformationResponse(Mockito.eq(isSuccess), Mockito.eq(message));

    }

    @Test
    void shouldReturnGeneralInformationResponseWithIsSuccessTrue_whenDeleteCommentRequestParametersAreFillTrue() throws IOException {
        Long commentId = 1L;
        String username = "testUser";
        Boolean isSuccess = true;
        String message = "Comment deleted with success.";
        DeleteCommentRequest deleteCommentRequest = new DeleteCommentRequest(commentId, username);
        User user = new User("test", "user", Gender.MAN, "testUser@test.com", "1111111111", username, "123", Role.USER, null);
        Post post = new Post(1L, "firstPost", "firstPost", 0, 0, user, new ArrayList<>(), null);
        Comment comment = new Comment(commentId, "commentContent", post, user, null);
        GeneralInformationResponse expectedResponse = new GeneralInformationResponse(isSuccess, message);

        Mockito.when(userService.findByUsername(username)).thenReturn(user);
        Mockito.when(userUtils.isSessionUser(user)).thenReturn(true);
        Mockito.when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        Mockito.when(mapper.writeValueAsString(comment)).thenReturn("String");
        Mockito.when(responseUtils.createGeneralInformationResponse(Mockito.eq(isSuccess), Mockito.eq(message)))
                .thenReturn(expectedResponse);

        GeneralInformationResponse result = commentService.deleteComment(deleteCommentRequest);

        Assertions.assertEquals(expectedResponse, result);

        Mockito.verify(userService).findByUsername(username);
        Mockito.verify(userUtils).isSessionUser(user);
        Mockito.verify(commentRepository).findById(commentId);
        Mockito.verify(commentRepository).delete(comment);
        Mockito.verify(mapper).writeValueAsString(comment);
        Mockito.verify(responseUtils).createGeneralInformationResponse(Mockito.eq(isSuccess), Mockito.eq(message));

    }

    @Test
    void shouldReturnGeneralInformationResponseWithIsSuccessTrue_whenPostOwnerUserTryToDeleteOtherUserComment() throws IOException {
        Long commentId = 1L;
        String ownerOfPostUsername = "testUser";
        String ownerOfCommentUsername = "ownerComment";
        Boolean isSuccess = true;
        String message = "Comment deleted with success.";
        DeleteCommentRequest deleteCommentRequest = new DeleteCommentRequest(commentId, ownerOfPostUsername);
        User ownerOfPostUser = new User("owner post", "user", Gender.MAN, "testUser@test.com", "1111111111", ownerOfPostUsername, "123", Role.USER, null);
        User ownerOfCommentUser = new User("owner comment", "user", Gender.MAN, "testUser@test.com", "1111111111", ownerOfCommentUsername, "123", Role.USER, null);
        Post post = new Post(1L, "firstPost", "firstPost", 0, 0, ownerOfPostUser, new ArrayList<>(), null);
        Comment comment = new Comment(commentId, "commentContent", post, ownerOfCommentUser, null);
        GeneralInformationResponse expectedResponse = new GeneralInformationResponse(isSuccess, message);

        Mockito.when(userService.findByUsername(ownerOfPostUsername)).thenReturn(ownerOfPostUser);
        Mockito.when(userUtils.isSessionUser(ownerOfPostUser)).thenReturn(true);
        Mockito.when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        Mockito.when(mapper.writeValueAsString(comment)).thenReturn("String");
        Mockito.when(responseUtils.createGeneralInformationResponse(Mockito.eq(isSuccess), Mockito.eq(message)))
                .thenReturn(expectedResponse);

        GeneralInformationResponse result = commentService.deleteComment(deleteCommentRequest);

        Assertions.assertEquals(expectedResponse, result);

        Mockito.verify(userService).findByUsername(ownerOfPostUsername);
        Mockito.verify(userUtils).isSessionUser(ownerOfPostUser);
        Mockito.verify(commentRepository).findById(commentId);
        Mockito.verify(commentRepository).delete(comment);
        Mockito.verify(mapper).writeValueAsString(comment);
        Mockito.verify(responseUtils).createGeneralInformationResponse(Mockito.eq(isSuccess), Mockito.eq(message));

    }


    @Test
    void shouldReturnGeneralInformationResponseWithIsSuccessFalse_whenUserNotFoundWithDeleteCommentRequestUsername() throws IOException {
        Long commentId = 1L;
        String username = "testUser";
        Boolean isSuccess = false;
        String message = "User not found.";
        DeleteCommentRequest deleteCommentRequest = new DeleteCommentRequest(commentId, username);
        GeneralInformationResponse expectedResponse = new GeneralInformationResponse(isSuccess, message);

        Mockito.when(userService.findByUsername(username)).thenReturn(null);
        Mockito.when(mapper.writeValueAsString(deleteCommentRequest)).thenReturn("String");
        Mockito.when(responseUtils.createGeneralInformationResponse(Mockito.eq(isSuccess), Mockito.eq(message)))
                .thenReturn(expectedResponse);

        GeneralInformationResponse result = commentService.deleteComment(deleteCommentRequest);

        Assertions.assertEquals(expectedResponse, result);

        Mockito.verify(userService).findByUsername(username);
        Mockito.verify(mapper).writeValueAsString(deleteCommentRequest);
        Mockito.verify(responseUtils).createGeneralInformationResponse(Mockito.eq(isSuccess), Mockito.eq(message));

    }

    @Test
    void shouldReturnGeneralInformationResponseWithIsSuccessFalse_whenUserIsNotSessionUserWithDeleteCommentRequestUsername() throws IOException {
        Long commentId = 1L;
        String username = "testUser";
        Boolean isSuccess = false;
        String message = "This user is not session user.";
        DeleteCommentRequest deleteCommentRequest = new DeleteCommentRequest(commentId, username);
        User user = new User("test", "user", Gender.MAN, "testUser@test.com", "1111111111", username, "123", Role.USER, null);
        GeneralInformationResponse expectedResponse = new GeneralInformationResponse(isSuccess, message);

        Mockito.when(userService.findByUsername(username)).thenReturn(user);
        Mockito.when(userUtils.isSessionUser(user)).thenReturn(false);
        Mockito.when(mapper.writeValueAsString(deleteCommentRequest)).thenReturn("String");
        Mockito.when(responseUtils.createGeneralInformationResponse(Mockito.eq(isSuccess), Mockito.eq(message)))
                .thenReturn(expectedResponse);

        GeneralInformationResponse result = commentService.deleteComment(deleteCommentRequest);

        Assertions.assertEquals(expectedResponse, result);

        Mockito.verify(userService).findByUsername(username);
        Mockito.verify(userUtils).isSessionUser(user);
        Mockito.verify(mapper).writeValueAsString(deleteCommentRequest);
        Mockito.verify(responseUtils).createGeneralInformationResponse(Mockito.eq(isSuccess), Mockito.eq(message));

    }

    @Test
    void shouldReturnGeneralInformationResponseWithIsSuccessFalse_whenCommentIsNotFoundWithDeleteCommentRequestCommentId() throws IOException {
        Long commentId = 1L;
        String username = "testUser";
        Boolean isSuccess = false;
        String message = "Comment not found.";
        DeleteCommentRequest deleteCommentRequest = new DeleteCommentRequest(commentId, username);
        User user = new User("test", "user", Gender.MAN, "testUser@test.com", "1111111111", username, "123", Role.USER, null);
        GeneralInformationResponse expectedResponse = new GeneralInformationResponse(isSuccess, message);

        Mockito.when(userService.findByUsername(username)).thenReturn(user);
        Mockito.when(userUtils.isSessionUser(user)).thenReturn(true);
        Mockito.when(mapper.writeValueAsString(deleteCommentRequest)).thenReturn("String");
        Mockito.when(responseUtils.createGeneralInformationResponse(Mockito.eq(isSuccess), Mockito.eq(message)))
                .thenReturn(expectedResponse);

        GeneralInformationResponse result = commentService.deleteComment(deleteCommentRequest);

        Assertions.assertEquals(expectedResponse, result);

        Mockito.verify(userService).findByUsername(username);
        Mockito.verify(userUtils).isSessionUser(user);
        Mockito.verify(commentRepository).findById(commentId);
        Mockito.verify(mapper).writeValueAsString(deleteCommentRequest);
        Mockito.verify(responseUtils).createGeneralInformationResponse(Mockito.eq(isSuccess), Mockito.eq(message));

    }

    @Test
    void shouldReturnGeneralInformationResponseWithIsSuccessFalse_whenUserIsNotOwnerOfCommentOrPostWithDeleteCommentRequestUsername() throws IOException {
        Long commentId = 1L;
        String username = "testUser";
        String notOwnerUsername = "notOwnerUser";
        Boolean isSuccess = false;
        String message = "This user is not owner of post or comment.";
        DeleteCommentRequest deleteCommentRequest = new DeleteCommentRequest(commentId, notOwnerUsername);
        User user = new User("test", "user", Gender.MAN, "testUser@test.com", "1111111111", username, "123", Role.USER, null);
        User notOwnerUser = new User("not owner", "user", Gender.MAN, "testUser2@test.com", "2222222222", notOwnerUsername, "123", Role.USER, null);
        Post post = new Post(1L, "firstPost", "firstPost", 0, 0, user, new ArrayList<>(), null);
        Comment comment = new Comment(commentId, "commentContent", post, user, null);
        GeneralInformationResponse expectedResponse = new GeneralInformationResponse(isSuccess, message);

        Mockito.when(userService.findByUsername(notOwnerUsername)).thenReturn(notOwnerUser);
        Mockito.when(userUtils.isSessionUser(notOwnerUser)).thenReturn(true);
        Mockito.when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        Mockito.when(mapper.writeValueAsString(deleteCommentRequest)).thenReturn("String");
        Mockito.when(responseUtils.createGeneralInformationResponse(Mockito.eq(isSuccess), Mockito.eq(message)))
                .thenReturn(expectedResponse);

        GeneralInformationResponse result = commentService.deleteComment(deleteCommentRequest);

        Assertions.assertEquals(expectedResponse, result);

        Mockito.verify(userService).findByUsername(notOwnerUsername);
        Mockito.verify(userUtils).isSessionUser(notOwnerUser);
        Mockito.verify(commentRepository).findById(commentId);
        Mockito.verify(mapper).writeValueAsString(comment);
        Mockito.verify(responseUtils).createGeneralInformationResponse(Mockito.eq(isSuccess), Mockito.eq(message));

    }

    @Test
    void shouldReturnGeneralInformationResponseWithIsSuccessFalse_whenCommentCannotDelete() throws IOException {
        Long commentId = 1L;
        String username = "testUser";
        Boolean isSuccess = false;
        String message = "Comment could not be deleted.";
        DeleteCommentRequest deleteCommentRequest = new DeleteCommentRequest(commentId, username);
        User user = new User("test", "user", Gender.MAN, "testUser@test.com", "1111111111", username, "123", Role.USER, null);
        Post post = new Post(1L, "firstPost", "firstPost", 0, 0, user, new ArrayList<>(), null);
        Comment comment = new Comment(commentId, "commentContent", post, user, null);
        GeneralInformationResponse expectedResponse = new GeneralInformationResponse(isSuccess, message);

        Mockito.when(userService.findByUsername(username)).thenReturn(user);
        Mockito.when(userUtils.isSessionUser(user)).thenReturn(true);
        Mockito.when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        Mockito.doThrow(new NullPointerException("Exception")).when(commentRepository).delete(comment);
        Mockito.when(mapper.writeValueAsString(comment)).thenReturn("String");
        Mockito.when(responseUtils.createGeneralInformationResponse(Mockito.eq(isSuccess), Mockito.eq(message)))
                .thenReturn(expectedResponse);

        GeneralInformationResponse result = commentService.deleteComment(deleteCommentRequest);

        Assertions.assertEquals(expectedResponse, result);

        Mockito.verify(userService).findByUsername(username);
        Mockito.verify(userUtils).isSessionUser(user);
        Mockito.verify(commentRepository).findById(commentId);
        Mockito.verify(commentRepository).delete(comment);
        Mockito.verify(mapper).writeValueAsString(comment);
        Mockito.verify(responseUtils).createGeneralInformationResponse(Mockito.eq(isSuccess), Mockito.eq(message));

    }

    @Test
    void shouldReturnComment_whenIdFindInDatabase() {
        Long commentId = 1L;
        String username = "testUser";
        User user = new User("test", "user", Gender.MAN, "testUser@test.com", "1111111111", username, "123", Role.USER, null);
        Post post = new Post(1L, "firstPost", "firstPost", 0, 0, user, new ArrayList<>(), null);
        Comment comment = new Comment(commentId, "commentContent", post, user, null);

        Mockito.when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        Comment result = commentService.findById(commentId);

        Assertions.assertEquals(comment, result);

        Mockito.verify(commentRepository).findById(commentId);

    }
}
