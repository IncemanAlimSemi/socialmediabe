package com.alseinn.socialmedia.service.like;

import com.alseinn.socialmedia.dao.user.LikeActionRepository;
import com.alseinn.socialmedia.entity.comment.Comment;
import com.alseinn.socialmedia.entity.like.LikeAction;
import com.alseinn.socialmedia.entity.like.LikeActionKey;
import com.alseinn.socialmedia.entity.like.enums.ActionObjectEnum;
import com.alseinn.socialmedia.entity.post.Post;
import com.alseinn.socialmedia.entity.user.User;
import com.alseinn.socialmedia.entity.user.enums.Gender;
import com.alseinn.socialmedia.entity.user.enums.Role;
import com.alseinn.socialmedia.request.like.LikeActionRequest;
import com.alseinn.socialmedia.response.like.ActionResponse;
import com.alseinn.socialmedia.service.comment.CommentService;
import com.alseinn.socialmedia.service.like.impl.LikeActionServiceImpl;
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

public class LikeActionServiceTests {

    private UserService userService;
    private UserUtils userUtils;
    private ObjectMapper mapper;
    private PostService postService;
    private CommentService commentService;
    private LikeActionRepository likeActionRepository;

    private LikeActionService likeActionService;

    @BeforeEach
    void setup() {
        userService = Mockito.mock(UserService.class);
        userUtils = Mockito.mock(UserUtils.class);
        mapper = Mockito.mock(ObjectMapper.class);
        postService = Mockito.mock(PostService.class);
        commentService = Mockito.mock(CommentService.class);
        likeActionRepository = Mockito.mock(LikeActionRepository.class);

        likeActionService = new LikeActionServiceImpl(userService, userUtils, mapper, postService,
                commentService, likeActionRepository);
    }

    @Test
    void shouldReturnActionResponseWithIsSuccessTrue_whenLikeActionRequestParametersAreFillTrueAndTypeSendCommentAndUserIsLikedFirstTimeTheComment() throws JsonProcessingException {
        String username = "testUser";
        ActionObjectEnum actionObjectType = ActionObjectEnum.COMMENT;
        LikeActionRequest likeActionRequest = new LikeActionRequest(0, actionObjectType, username);
        User user = new User("test", "user", Gender.MAN, "aaa@gmail.com", "5555555555", username, "123", Role.USER);
        Post post = new Post(0, "title", "content", 0, 0, user, new ArrayList<>());
        Comment comment = new Comment(0, "content", post, user);
        LikeActionKey likeActionKey = new LikeActionKey(username, actionObjectType, 0);
        LikeAction likeAction = new LikeAction(likeActionKey, true);
        ActionResponse expectedResponse = new ActionResponse(true,likeActionRequest.getType().toString() + " liked.");

        Mockito.when(userService.findByUsername(username)).thenReturn(user);
        Mockito.when(userUtils.isSessionUser(user)).thenReturn(true);
        Mockito.when(commentService.findById(likeActionRequest.getId())).thenReturn(comment);
        Mockito.when(likeActionRepository.findById(likeActionKey)).thenReturn(Optional.empty());
        Mockito.when(likeActionRepository.save(likeAction)).thenReturn(likeAction);
        Mockito.when(mapper.writeValueAsString(likeActionRequest)).thenReturn(Mockito.anyString());

        ActionResponse result = likeActionService.like(likeActionRequest);

        Assertions.assertEquals(expectedResponse, result);

        Mockito.verify(userService).findByUsername(username);
        Mockito.verify(userUtils).isSessionUser(user);
        Mockito.verify(commentService).findById(likeActionRequest.getId());
        Mockito.verify(likeActionRepository).findById(likeActionKey);
        Mockito.verify(likeActionRepository).save(likeAction);
        Mockito.verify(mapper).writeValueAsString(likeActionRequest);

    }

    @Test
    void shouldReturnActionResponseWithIsSuccessTrue_whenLikeActionRequestParametersAreFillTrueAndTypeSendPostAndUserIsLikedFirstTimeThePost() throws JsonProcessingException {
        String username = "testUser";
        ActionObjectEnum actionObjectType = ActionObjectEnum.POST;
        LikeActionRequest likeActionRequest = new LikeActionRequest(0, actionObjectType, username);
        User user = new User("test", "user", Gender.MAN, "aaa@gmail.com", "5555555555", username, "123", Role.USER);
        Post post = new Post(0, "title", "content", 0, 0, user, new ArrayList<>());
        LikeActionKey likeActionKey = new LikeActionKey(username, actionObjectType, 0);
        LikeAction likeAction = new LikeAction(likeActionKey, true);
        ActionResponse expectedResponse = new ActionResponse(true,likeActionRequest.getType().toString() + " liked.");

        Mockito.when(userService.findByUsername(username)).thenReturn(user);
        Mockito.when(userUtils.isSessionUser(user)).thenReturn(true);
        Mockito.when(postService.findById(likeActionRequest.getId())).thenReturn(post);
        Mockito.when(likeActionRepository.findById(likeActionKey)).thenReturn(Optional.empty());
        Mockito.when(likeActionRepository.save(likeAction)).thenReturn(likeAction);
        Mockito.when(mapper.writeValueAsString(likeActionRequest)).thenReturn(Mockito.anyString());

        ActionResponse result = likeActionService.like(likeActionRequest);

        Assertions.assertEquals(expectedResponse, result);

        Mockito.verify(userService).findByUsername(username);
        Mockito.verify(userUtils).isSessionUser(user);
        Mockito.verify(postService).findById(likeActionRequest.getId());
        Mockito.verify(likeActionRepository).findById(likeActionKey);
        Mockito.verify(likeActionRepository).save(likeAction);
        Mockito.verify(mapper).writeValueAsString(likeActionRequest);

    }

    @Test
    void shouldReturnActionResponseWithIsSuccessTrue_whenLikeActionRequestParametersAreFillTrueAndTypeSendCommentAndUserIsUnlikedTheComment() throws JsonProcessingException {
        String username = "testUser";
        ActionObjectEnum actionObjectType = ActionObjectEnum.COMMENT;
        LikeActionRequest likeActionRequest = new LikeActionRequest(0, actionObjectType, username);
        User user = new User("test", "user", Gender.MAN, "aaa@gmail.com", "5555555555", username, "123", Role.USER);
        Post post = new Post(0, "title", "content", 0, 0, user, new ArrayList<>());
        Comment comment = new Comment(0, "content", post, user);
        LikeActionKey likeActionKey = new LikeActionKey(username, actionObjectType, 0);
        LikeAction likeAction = new LikeAction(likeActionKey, true);
        ActionResponse expectedResponse = new ActionResponse(true,likeActionRequest.getType().toString() + " unliked.");

        Mockito.when(userService.findByUsername(username)).thenReturn(user);
        Mockito.when(userUtils.isSessionUser(user)).thenReturn(true);
        Mockito.when(commentService.findById(likeActionRequest.getId())).thenReturn(comment);
        Mockito.when(likeActionRepository.findById(likeActionKey)).thenReturn(Optional.of(likeAction));
        Mockito.when(mapper.writeValueAsString(likeActionRequest)).thenReturn(Mockito.anyString());

        ActionResponse result = likeActionService.like(likeActionRequest);

        Assertions.assertEquals(expectedResponse, result);

        Mockito.verify(userService).findByUsername(username);
        Mockito.verify(userUtils).isSessionUser(user);
        Mockito.verify(commentService).findById(likeActionRequest.getId());
        Mockito.verify(likeActionRepository).findById(likeActionKey);
        Mockito.verify(likeActionRepository).delete(likeAction);
        Mockito.verify(mapper).writeValueAsString(likeActionRequest);

    }

    @Test
    void shouldReturnActionResponseWithIsSuccessTrue_whenLikeActionRequestParametersAreFillTrueAndTypeSendPostAndUserIsUnlikedThePost() throws JsonProcessingException {
        String username = "testUser";
        ActionObjectEnum actionObjectType = ActionObjectEnum.POST;
        LikeActionRequest likeActionRequest = new LikeActionRequest(0, actionObjectType, username);
        User user = new User("test", "user", Gender.MAN, "aaa@gmail.com", "5555555555", username, "123", Role.USER);
        Post post = new Post(0, "title", "content", 0, 0, user, new ArrayList<>());
        LikeActionKey likeActionKey = new LikeActionKey(username, actionObjectType, 0);
        LikeAction likeAction = new LikeAction(likeActionKey, true);
        ActionResponse expectedResponse = new ActionResponse(true,likeActionRequest.getType().toString() + " unliked.");

        Mockito.when(userService.findByUsername(username)).thenReturn(user);
        Mockito.when(userUtils.isSessionUser(user)).thenReturn(true);
        Mockito.when(postService.findById(likeActionRequest.getId())).thenReturn(post);
        Mockito.when(likeActionRepository.findById(likeActionKey)).thenReturn(Optional.of(likeAction));
        Mockito.when(mapper.writeValueAsString(likeActionRequest)).thenReturn(Mockito.anyString());

        ActionResponse result = likeActionService.like(likeActionRequest);

        Assertions.assertEquals(expectedResponse, result);

        Mockito.verify(userService).findByUsername(username);
        Mockito.verify(userUtils).isSessionUser(user);
        Mockito.verify(postService).findById(likeActionRequest.getId());
        Mockito.verify(likeActionRepository).findById(likeActionKey);
        Mockito.verify(likeActionRepository).delete(likeAction);
        Mockito.verify(mapper).writeValueAsString(likeActionRequest);

    }

    @Test
    void shouldReturnActionResponseWithIsSuccessFalse_whenUserIsNotFoundWithLikeActionRequestUsername() throws JsonProcessingException {
        String username = "testUser";
        ActionObjectEnum actionObjectType = ActionObjectEnum.POST;
        LikeActionRequest likeActionRequest = new LikeActionRequest(0, actionObjectType, username);
        ActionResponse expectedResponse = new ActionResponse(false,"User not found.");

        Mockito.when(userService.findByUsername(username)).thenReturn(null);
        Mockito.when(mapper.writeValueAsString(likeActionRequest)).thenReturn(Mockito.anyString());

        ActionResponse result = likeActionService.like(likeActionRequest);

        Assertions.assertEquals(expectedResponse, result);

        Mockito.verify(userService).findByUsername(username);
        Mockito.verify(mapper).writeValueAsString(likeActionRequest);

    }

    @Test
    void shouldReturnActionResponseWithIsSuccessFalse_whenUserIsNotSessionUserWithLikeActionRequestUsername() throws JsonProcessingException {
        String username = "testUser";
        ActionObjectEnum actionObjectType = ActionObjectEnum.POST;
        LikeActionRequest likeActionRequest = new LikeActionRequest(0, actionObjectType, username);
        User user = new User("test", "user", Gender.MAN, "aaa@gmail.com", "5555555555", username, "123", Role.USER);
        ActionResponse expectedResponse = new ActionResponse(false,"This user is not session user.");

        Mockito.when(userService.findByUsername(username)).thenReturn(user);
        Mockito.when(userUtils.isSessionUser(user)).thenReturn(false);
        Mockito.when(mapper.writeValueAsString(likeActionRequest)).thenReturn(Mockito.anyString());

        ActionResponse result = likeActionService.like(likeActionRequest);

        Assertions.assertEquals(expectedResponse, result);

        Mockito.verify(userService).findByUsername(username);
        Mockito.verify(userUtils).isSessionUser(user);
        Mockito.verify(mapper).writeValueAsString(likeActionRequest);

    }

    @Test
    void shouldReturnActionResponseWithIsSuccessFalse_whenCommentNotFoundWithLikeActionRequestId() throws JsonProcessingException {
        String username = "testUser";
        ActionObjectEnum actionObjectType = ActionObjectEnum.COMMENT;
        LikeActionRequest likeActionRequest = new LikeActionRequest(0, actionObjectType, username);
        User user = new User("test", "user", Gender.MAN, "aaa@gmail.com", "5555555555", username, "123", Role.USER);
        ActionResponse expectedResponse = new ActionResponse(false,"This action id is not found in database.");

        Mockito.when(userService.findByUsername(username)).thenReturn(user);
        Mockito.when(userUtils.isSessionUser(user)).thenReturn(true);
        Mockito.when(commentService.findById(likeActionRequest.getId())).thenReturn(null);
        Mockito.when(mapper.writeValueAsString(likeActionRequest)).thenReturn(Mockito.anyString());

        ActionResponse result = likeActionService.like(likeActionRequest);

        Assertions.assertEquals(expectedResponse, result);

        Mockito.verify(userService).findByUsername(username);
        Mockito.verify(userUtils).isSessionUser(user);
        Mockito.verify(commentService).findById(likeActionRequest.getId());
        Mockito.verify(mapper).writeValueAsString(likeActionRequest);

    }

    @Test
    void shouldReturnActionResponseWithIsSuccessFalse_whenPostNotFoundWithLikeActionRequestId() throws JsonProcessingException {
        String username = "testUser";
        ActionObjectEnum actionObjectType = ActionObjectEnum.POST;
        LikeActionRequest likeActionRequest = new LikeActionRequest(0, actionObjectType, username);
        User user = new User("test", "user", Gender.MAN, "aaa@gmail.com", "5555555555", username, "123", Role.USER);
        ActionResponse expectedResponse = new ActionResponse(false,"This action id is not found in database.");

        Mockito.when(userService.findByUsername(username)).thenReturn(user);
        Mockito.when(userUtils.isSessionUser(user)).thenReturn(true);
        Mockito.when(postService.findById(likeActionRequest.getId())).thenReturn(null);
        Mockito.when(mapper.writeValueAsString(likeActionRequest)).thenReturn(Mockito.anyString());

        ActionResponse result = likeActionService.like(likeActionRequest);

        Assertions.assertEquals(expectedResponse, result);

        Mockito.verify(userService).findByUsername(username);
        Mockito.verify(userUtils).isSessionUser(user);
        Mockito.verify(postService).findById(likeActionRequest.getId());
        Mockito.verify(mapper).writeValueAsString(likeActionRequest);

    }

    @Test
    void shouldReturnActionResponseWithIsSuccessFalse_whenPostOrCommentCannotSaveInDatabase() throws JsonProcessingException {
        String username = "testUser";
        ActionObjectEnum actionObjectType = ActionObjectEnum.POST;
        LikeActionRequest likeActionRequest = new LikeActionRequest(0, actionObjectType, username);
        User user = new User("test", "user", Gender.MAN, "aaa@gmail.com", "5555555555", username, "123", Role.USER);
        Post post = new Post(0, "title", "content", 0, 0, user, new ArrayList<>());
        LikeActionKey likeActionKey = new LikeActionKey(username, actionObjectType, 0);
        LikeAction likeAction = new LikeAction(likeActionKey, true);
        ActionResponse expectedResponse = new ActionResponse(false,likeActionRequest.getType().toString() + " not liked/unliked.");

        Mockito.when(userService.findByUsername(username)).thenReturn(user);
        Mockito.when(userUtils.isSessionUser(user)).thenReturn(true);
        Mockito.when(postService.findById(likeActionRequest.getId())).thenReturn(post);
        Mockito.when(likeActionRepository.findById(likeActionKey)).thenReturn(Optional.empty());
        Mockito.when(likeActionRepository.save(likeAction)).thenThrow(new NullPointerException("Exception"));
        Mockito.when(mapper.writeValueAsString(likeActionRequest)).thenReturn(Mockito.anyString());

        ActionResponse result = likeActionService.like(likeActionRequest);

        Assertions.assertEquals(expectedResponse, result);

        Mockito.verify(userService).findByUsername(username);
        Mockito.verify(userUtils).isSessionUser(user);
        Mockito.verify(postService).findById(likeActionRequest.getId());
        Mockito.verify(likeActionRepository).findById(likeActionKey);
        Mockito.verify(likeActionRepository).save(likeAction);
        Mockito.verify(mapper).writeValueAsString(likeActionRequest);

    }

    @Test
    void shouldReturnActionResponseWithIsSuccessFalse_whenPostOrCommentCannotDeleteInDatabase() throws JsonProcessingException {
        String username = "testUser";
        ActionObjectEnum actionObjectType = ActionObjectEnum.POST;
        LikeActionRequest likeActionRequest = new LikeActionRequest(0, actionObjectType, username);
        User user = new User("test", "user", Gender.MAN, "aaa@gmail.com", "5555555555", username, "123", Role.USER);
        Post post = new Post(0, "title", "content", 0, 0, user, new ArrayList<>());
        LikeActionKey likeActionKey = new LikeActionKey(username, actionObjectType, 0);
        LikeAction likeAction = new LikeAction(likeActionKey, true);
        ActionResponse expectedResponse = new ActionResponse(false,likeActionRequest.getType().toString() + " not liked/unliked.");

        Mockito.when(userService.findByUsername(username)).thenReturn(user);
        Mockito.when(userUtils.isSessionUser(user)).thenReturn(true);
        Mockito.when(postService.findById(likeActionRequest.getId())).thenReturn(post);
        Mockito.when(likeActionRepository.findById(likeActionKey)).thenReturn(Optional.of(likeAction));
        Mockito.doThrow(new NullPointerException("Exception")).when(likeActionRepository).delete(likeAction);
        Mockito.when(mapper.writeValueAsString(likeActionRequest)).thenReturn(Mockito.anyString());

        ActionResponse result = likeActionService.like(likeActionRequest);

        Assertions.assertEquals(expectedResponse, result);

        Mockito.verify(userService).findByUsername(username);
        Mockito.verify(userUtils).isSessionUser(user);
        Mockito.verify(postService).findById(likeActionRequest.getId());
        Mockito.verify(likeActionRepository).findById(likeActionKey);
        Mockito.verify(likeActionRepository).delete(likeAction);
        Mockito.verify(mapper).writeValueAsString(likeActionRequest);

    }
}
