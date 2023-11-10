package com.alseinn.socialmedia.service.comment;

import com.alseinn.socialmedia.dao.user.CommentRepository;
import com.alseinn.socialmedia.entity.comment.Comment;
import com.alseinn.socialmedia.entity.post.Post;
import com.alseinn.socialmedia.entity.user.User;
import com.alseinn.socialmedia.entity.user.enums.Gender;
import com.alseinn.socialmedia.entity.user.enums.Role;
import com.alseinn.socialmedia.request.comment.CreateCommentRequest;
import com.alseinn.socialmedia.request.comment.DeleteCommentRequest;
import com.alseinn.socialmedia.response.comment.CommentResponse;
import com.alseinn.socialmedia.service.comment.impl.CommentServiceImpl;
import com.alseinn.socialmedia.service.post.PostService;
import com.alseinn.socialmedia.service.user.UserService;
import com.alseinn.socialmedia.utils.UserUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Optional;

class CommentServiceTests {

    private CommentRepository commentRepository;
    private UserService userService;
    private UserUtils userUtils;
    private ObjectMapper mapper;
    private PostService postService;
    private CommentService commentService;

    @BeforeEach
    void setup() {
        commentRepository = Mockito.mock(CommentRepository.class);
        userService = Mockito.mock(UserService.class);
        userUtils = Mockito.mock(UserUtils.class);
        mapper = Mockito.mock(ObjectMapper.class);
        postService = Mockito.mock(PostService.class);

        commentService = new CommentServiceImpl(commentRepository, userService, userUtils, mapper, postService);
    }

    @Test
    void shouldReturnCommentResponseWithIsSuccessTrue_whenCreateCommentRequestParametersAreFillTrue() throws JsonProcessingException {
        Long postId = 1L;
        String username = "testUser";
        CreateCommentRequest createCommentRequest = new CreateCommentRequest(postId, "Content", "testUser");
        User user = new User("test", "user", Gender.MAN, "testUser@test.com", "1111111111", username, "123", Role.USER);
        Post post = new Post(postId, "firstPost", "firstPost", 0, 0, user, new ArrayList<>());
        Comment comment = new Comment(0, "Content", post, user);
        CommentResponse expectedResponse = new CommentResponse(true, "Comment created with success.");

        Mockito.when(userService.findByUsername(username)).thenReturn(user);
        Mockito.when(userUtils.isSessionUser(user)).thenReturn(true);
        Mockito.when(postService.findById(postId)).thenReturn(post);
        Mockito.when(commentRepository.save(comment)).thenReturn(comment);
        Mockito.when(mapper.writeValueAsString(comment)).thenReturn(Mockito.anyString());

        CommentResponse result = commentService.createComment(createCommentRequest);

        Assertions.assertEquals(expectedResponse, result);

        Mockito.verify(userService).findByUsername(username);
        Mockito.verify(userUtils).isSessionUser(user);
        Mockito.verify(postService).findById(postId);
        Mockito.verify(commentRepository).save(comment);
        Mockito.verify(mapper).writeValueAsString(comment);

    }

    @Test
    void shouldReturnCommentResponseWithIsSuccessFalse_whenUserNotFoundWithCreateCommentRequestUsername() throws JsonProcessingException {
        Long postId = 1L;
        String username = "testUser";
        CreateCommentRequest createCommentRequest = new CreateCommentRequest(postId, "Content", "testUser");
        CommentResponse expectedResponse = new CommentResponse(false, "User not found.");

        Mockito.when(userService.findByUsername(username)).thenReturn(null);
        Mockito.when(mapper.writeValueAsString(createCommentRequest)).thenReturn(Mockito.anyString());

        CommentResponse result = commentService.createComment(createCommentRequest);

        Assertions.assertEquals(expectedResponse, result);

        Mockito.verify(userService).findByUsername(username);
        Mockito.verify(mapper).writeValueAsString(createCommentRequest);

    }

    @Test
    void shouldReturnCommentResponseWithIsSuccessFalse_whenUserIsNotSessionUserWithCreateCommentRequestUsername() throws JsonProcessingException {
        Long postId = 1L;
        String username = "testUser";
        User user = new User("test", "user", Gender.MAN, "testUser@test.com", "1111111111", username, "123", Role.USER);
        CreateCommentRequest createCommentRequest = new CreateCommentRequest(postId, "Content", "testUser");
        CommentResponse expectedResponse = new CommentResponse(false, "This user is not session user.");

        Mockito.when(userService.findByUsername(username)).thenReturn(user);
        Mockito.when(userUtils.isSessionUser(user)).thenReturn(false);
        Mockito.when(mapper.writeValueAsString(createCommentRequest)).thenReturn(Mockito.anyString());

        CommentResponse result = commentService.createComment(createCommentRequest);

        Assertions.assertEquals(expectedResponse, result);

        Mockito.verify(userService).findByUsername(username);
        Mockito.verify(userUtils).isSessionUser(user);
        Mockito.verify(mapper).writeValueAsString(createCommentRequest);

    }

    @Test
    void shouldReturnCommentResponseWithIsSuccessFalse_whenPostIsNotFoundWithCreateCommentRequestPostId() throws JsonProcessingException {
        Long postId = 1L;
        String username = "testUser";
        CreateCommentRequest createCommentRequest = new CreateCommentRequest(postId, "Content", "testUser");
        User user = new User("test", "user", Gender.MAN, "testUser@test.com", "1111111111", username, "123", Role.USER);
        CommentResponse expectedResponse = new CommentResponse(false, "Post not found.");

        Mockito.when(userService.findByUsername(username)).thenReturn(user);
        Mockito.when(userUtils.isSessionUser(user)).thenReturn(true);
        Mockito.when(postService.findById(postId)).thenReturn(null);
        Mockito.when(mapper.writeValueAsString(createCommentRequest)).thenReturn(Mockito.anyString());

        CommentResponse result = commentService.createComment(createCommentRequest);

        Assertions.assertEquals(expectedResponse, result);

        Mockito.verify(userService).findByUsername(username);
        Mockito.verify(userUtils).isSessionUser(user);
        Mockito.verify(postService).findById(postId);
        Mockito.verify(mapper).writeValueAsString(createCommentRequest);

    }

    @Test
    void shouldReturnCommentResponseWithIsSuccessFalse_whenCommentCannotSaveDatabase() throws JsonProcessingException {
        Long postId = 1L;
        String username = "testUser";
        CreateCommentRequest createCommentRequest = new CreateCommentRequest(postId, "Content", "testUser");
        User user = new User("test", "user", Gender.MAN, "testUser@test.com", "1111111111", username, "123", Role.USER);
        Post post = new Post(postId, "firstPost", "firstPost", 0, 0, user, new ArrayList<>());
        Comment comment = new Comment(0, "Content", post, user);
        CommentResponse expectedResponse = new CommentResponse(false, "Comment could not be created.");

        Mockito.when(userService.findByUsername(username)).thenReturn(user);
        Mockito.when(userUtils.isSessionUser(user)).thenReturn(true);
        Mockito.when(postService.findById(postId)).thenReturn(post);
        Mockito.when(commentRepository.save(comment)).thenThrow(new NullPointerException("Exception"));
        Mockito.when(mapper.writeValueAsString(post)).thenReturn(Mockito.anyString());

        CommentResponse result = commentService.createComment(createCommentRequest);

        Assertions.assertEquals(expectedResponse, result);

        Mockito.verify(userService).findByUsername(username);
        Mockito.verify(userUtils).isSessionUser(user);
        Mockito.verify(postService).findById(postId);
        Mockito.verify(commentRepository).save(comment);
        Mockito.verify(mapper).writeValueAsString(post);

    }

    @Test
    void shouldReturnCommentResponseWithIsSuccessTrue_whenDeleteCommentRequestParametersAreFillTrue() throws JsonProcessingException {
        Long commentId = 1L;
        String username = "testUser";
        DeleteCommentRequest deleteCommentRequest = new DeleteCommentRequest(commentId, username);
        User user = new User("test", "user", Gender.MAN, "testUser@test.com", "1111111111", username, "123", Role.USER);
        Post post = new Post(1L, "firstPost", "firstPost", 0, 0, user, new ArrayList<>());
        Comment comment = new Comment(commentId, "commentContent", post, user);
        CommentResponse expectedResponse = new CommentResponse(true, "Comment deleted with success.");

        Mockito.when(userService.findByUsername(username)).thenReturn(user);
        Mockito.when(userUtils.isSessionUser(user)).thenReturn(true);
        Mockito.when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        Mockito.when(mapper.writeValueAsString(comment)).thenReturn(Mockito.anyString());

        CommentResponse result = commentService.deleteComment(deleteCommentRequest);

        Assertions.assertEquals(expectedResponse, result);

        Mockito.verify(userService).findByUsername(username);
        Mockito.verify(userUtils).isSessionUser(user);
        Mockito.verify(commentRepository).findById(commentId);
        Mockito.verify(commentRepository).delete(comment);
        Mockito.verify(mapper).writeValueAsString(comment);

    }

    @Test
    void shouldReturnCommentResponseWithIsSuccessTrue_whenPostOwnerUserTryToDeleteOtherUserComment() throws JsonProcessingException {
        Long commentId = 1L;
        String ownerOfPostUsername = "testUser";
        String ownerOfCommentUsername = "ownerComment";
        DeleteCommentRequest deleteCommentRequest = new DeleteCommentRequest(commentId, ownerOfPostUsername);
        User ownerOfPostUser = new User("owner post", "user", Gender.MAN, "testUser@test.com", "1111111111", ownerOfPostUsername, "123", Role.USER);
        User ownerOfCommentUser = new User("owner comment", "user", Gender.MAN, "testUser@test.com", "1111111111", ownerOfCommentUsername, "123", Role.USER);
        Post post = new Post(1L, "firstPost", "firstPost", 0, 0, ownerOfPostUser, new ArrayList<>());
        Comment comment = new Comment(commentId, "commentContent", post, ownerOfCommentUser);
        CommentResponse expectedResponse = new CommentResponse(true, "Comment deleted with success.");

        Mockito.when(userService.findByUsername(ownerOfPostUsername)).thenReturn(ownerOfPostUser);
        Mockito.when(userUtils.isSessionUser(ownerOfPostUser)).thenReturn(true);
        Mockito.when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        Mockito.when(mapper.writeValueAsString(comment)).thenReturn(Mockito.anyString());

        CommentResponse result = commentService.deleteComment(deleteCommentRequest);

        Assertions.assertEquals(expectedResponse, result);

        Mockito.verify(userService).findByUsername(ownerOfPostUsername);
        Mockito.verify(userUtils).isSessionUser(ownerOfPostUser);
        Mockito.verify(commentRepository).findById(commentId);
        Mockito.verify(commentRepository).delete(comment);
        Mockito.verify(mapper).writeValueAsString(comment);

    }


    @Test
    void shouldReturnCommentResponseWithIsSuccessFalse_whenUserNotFoundWithDeleteCommentRequestUsername() throws JsonProcessingException {
        Long commentId = 1L;
        String username = "testUser";
        DeleteCommentRequest deleteCommentRequest = new DeleteCommentRequest(commentId, username);
        CommentResponse expectedResponse = new CommentResponse(false, "User not found.");

        Mockito.when(userService.findByUsername(username)).thenReturn(null);
        Mockito.when(mapper.writeValueAsString(deleteCommentRequest)).thenReturn(Mockito.anyString());

        CommentResponse result = commentService.deleteComment(deleteCommentRequest);

        Assertions.assertEquals(expectedResponse, result);

        Mockito.verify(userService).findByUsername(username);
        Mockito.verify(mapper).writeValueAsString(deleteCommentRequest);

    }

    @Test
    void shouldReturnCommentResponseWithIsSuccessFalse_whenUserIsNotSessionUserWithDeleteCommentRequestUsername() throws JsonProcessingException {
        Long commentId = 1L;
        String username = "testUser";
        DeleteCommentRequest deleteCommentRequest = new DeleteCommentRequest(commentId, username);
        User user = new User("test", "user", Gender.MAN, "testUser@test.com", "1111111111", username, "123", Role.USER);
        CommentResponse expectedResponse = new CommentResponse(false, "This user is not session user.");

        Mockito.when(userService.findByUsername(username)).thenReturn(user);
        Mockito.when(userUtils.isSessionUser(user)).thenReturn(false);
        Mockito.when(mapper.writeValueAsString(deleteCommentRequest)).thenReturn(Mockito.anyString());

        CommentResponse result = commentService.deleteComment(deleteCommentRequest);

        Assertions.assertEquals(expectedResponse, result);

        Mockito.verify(userService).findByUsername(username);
        Mockito.verify(userUtils).isSessionUser(user);
        Mockito.verify(mapper).writeValueAsString(deleteCommentRequest);

    }

    @Test
    void shouldReturnCommentResponseWithIsSuccessFalse_whenCommentIsNotFoundWithDeleteCommentRequestCommentId() throws JsonProcessingException {
        Long commentId = 1L;
        String username = "testUser";
        DeleteCommentRequest deleteCommentRequest = new DeleteCommentRequest(commentId, username);
        User user = new User("test", "user", Gender.MAN, "testUser@test.com", "1111111111", username, "123", Role.USER);
        CommentResponse expectedResponse = new CommentResponse(false, "Comment not found.");

        Mockito.when(userService.findByUsername(username)).thenReturn(user);
        Mockito.when(userUtils.isSessionUser(user)).thenReturn(true);
        Mockito.when(mapper.writeValueAsString(deleteCommentRequest)).thenReturn(Mockito.anyString());

        CommentResponse result = commentService.deleteComment(deleteCommentRequest);

        Assertions.assertEquals(expectedResponse, result);

        Mockito.verify(userService).findByUsername(username);
        Mockito.verify(userUtils).isSessionUser(user);
        Mockito.verify(commentRepository).findById(commentId);
        Mockito.verify(mapper).writeValueAsString(deleteCommentRequest);

    }

    @Test
    void shouldReturnCommentResponseWithIsSuccessFalse_whenUserIsNotOwnerOfCommentOrPostWithDeleteCommentRequestUsername() throws JsonProcessingException {
        Long commentId = 1L;
        String username = "testUser";
        String notOwnerUsername = "notOwnerUser";
        DeleteCommentRequest deleteCommentRequest = new DeleteCommentRequest(commentId, notOwnerUsername);
        User user = new User("test", "user", Gender.MAN, "testUser@test.com", "1111111111", username, "123", Role.USER);
        User notOwnerUser = new User("not owner", "user", Gender.MAN, "testUser2@test.com", "2222222222", notOwnerUsername, "123", Role.USER);
        Post post = new Post(1L, "firstPost", "firstPost", 0, 0, user, new ArrayList<>());
        Comment comment = new Comment(commentId, "commentContent", post, user);
        CommentResponse expectedResponse = new CommentResponse(false, "This user is not owner of post or comment.");

        Mockito.when(userService.findByUsername(notOwnerUsername)).thenReturn(notOwnerUser);
        Mockito.when(userUtils.isSessionUser(notOwnerUser)).thenReturn(true);
        Mockito.when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        Mockito.when(mapper.writeValueAsString(comment)).thenReturn(Mockito.anyString());

        CommentResponse result = commentService.deleteComment(deleteCommentRequest);

        Assertions.assertEquals(expectedResponse, result);

        Mockito.verify(userService).findByUsername(notOwnerUsername);
        Mockito.verify(userUtils).isSessionUser(notOwnerUser);
        Mockito.verify(commentRepository).findById(commentId);
        Mockito.verify(mapper).writeValueAsString(comment);

    }

    @Test
    void shouldReturnCommentResponseWithIsSuccessFalse_whenCommentCannotDelete() throws JsonProcessingException {
        Long commentId = 1L;
        String username = "testUser";
        DeleteCommentRequest deleteCommentRequest = new DeleteCommentRequest(commentId, username);
        User user = new User("test", "user", Gender.MAN, "testUser@test.com", "1111111111", username, "123", Role.USER);
        Post post = new Post(1L, "firstPost", "firstPost", 0, 0, user, new ArrayList<>());
        Comment comment = new Comment(commentId, "commentContent", post, user);
        CommentResponse expectedResponse = new CommentResponse(false, "Comment could not be deleted.");

        Mockito.when(userService.findByUsername(username)).thenReturn(user);
        Mockito.when(userUtils.isSessionUser(user)).thenReturn(true);
        Mockito.when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        Mockito.doThrow(new NullPointerException("Exception")).when(commentRepository).delete(comment);
        Mockito.when(mapper.writeValueAsString(comment)).thenReturn(Mockito.anyString());

        CommentResponse result = commentService.deleteComment(deleteCommentRequest);

        Assertions.assertEquals(expectedResponse, result);

        Mockito.verify(userService).findByUsername(username);
        Mockito.verify(userUtils).isSessionUser(user);
        Mockito.verify(commentRepository).findById(commentId);
        Mockito.verify(commentRepository).delete(comment);
        Mockito.verify(mapper).writeValueAsString(comment);

    }

    @Test
    void shouldReturnComment_whenIdFindInDatabase() {
        Long commentId = 1L;
        String username = "testUser";
        User user = new User("test", "user", Gender.MAN, "testUser@test.com", "1111111111", username, "123", Role.USER);
        Post post = new Post(1L, "firstPost", "firstPost", 0, 0, user, new ArrayList<>());
        Comment comment = new Comment(commentId, "commentContent", post, user);

        Mockito.when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        Comment result = commentService.findById(commentId);

        Assertions.assertEquals(comment, result);

        Mockito.verify(commentRepository).findById(commentId);

    }
}
