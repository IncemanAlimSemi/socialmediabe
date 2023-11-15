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
import com.alseinn.socialmedia.response.general.GeneralInformationResponse;
import com.alseinn.socialmedia.service.comment.CommentService;
import com.alseinn.socialmedia.service.like.impl.LikeActionServiceImpl;
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

public class LikeActionServiceTests {

    private UserService userService;
    private UserUtils userUtils;
    private ObjectMapper mapper;
    private PostService postService;
    private CommentService commentService;
    private LikeActionRepository likeActionRepository;
    private LikeActionService likeActionService;
    private ResponseUtils responseUtils;


    @BeforeEach
    void setup() {
        userService = Mockito.mock(UserService.class);
        userUtils = Mockito.mock(UserUtils.class);
        mapper = Mockito.mock(ObjectMapper.class);
        postService = Mockito.mock(PostService.class);
        commentService = Mockito.mock(CommentService.class);
        likeActionRepository = Mockito.mock(LikeActionRepository.class);
        responseUtils = Mockito.mock(ResponseUtils.class);

        likeActionService = new LikeActionServiceImpl(userService, userUtils, mapper, postService,
                commentService, likeActionRepository, responseUtils);
    }

    @Test
    void shouldReturnGeneralInformationResponseWithIsSuccessTrue_whenLikeActionRequestParametersAreFillTrueAndTypeSendCommentAndUserIsLikedFirstTimeTheComment() throws IOException {
        String username = "testUser";
        ActionObjectEnum actionObjectType = ActionObjectEnum.COMMENT;
        Boolean isSuccess = true;
        String message = actionObjectType + " liked.";
        LikeActionRequest likeActionRequest = new LikeActionRequest(0, actionObjectType, username);
        User user = new User("test", "user", Gender.MAN, "aaa@gmail.com", "5555555555", username, "123", Role.USER);
        Post post = new Post(0, "title", "content", 0, 0, user, new ArrayList<>());
        Comment comment = new Comment(0, "content", post, user);
        LikeActionKey likeActionKey = new LikeActionKey(username, actionObjectType, 0);
        LikeAction likeAction = new LikeAction(likeActionKey, true);
        GeneralInformationResponse expectedResponse = new GeneralInformationResponse(isSuccess, message);

        Mockito.when(userService.findByUsername(username)).thenReturn(user);
        Mockito.when(userUtils.isSessionUser(user)).thenReturn(true);
        Mockito.when(commentService.findById(likeActionRequest.getId())).thenReturn(comment);
        Mockito.when(likeActionRepository.findById(likeActionKey)).thenReturn(Optional.empty());
        Mockito.when(likeActionRepository.save(likeAction)).thenReturn(likeAction);
        Mockito.when(mapper.writeValueAsString(likeActionRequest)).thenReturn("String");
        Mockito.when(responseUtils.createGeneralInformationResponse(Mockito.eq(isSuccess), Mockito.eq(message)))
                .thenReturn(expectedResponse);

        GeneralInformationResponse result = likeActionService.like(likeActionRequest);

        Assertions.assertEquals(expectedResponse, result);

        Mockito.verify(userService).findByUsername(username);
        Mockito.verify(userUtils).isSessionUser(user);
        Mockito.verify(commentService).findById(likeActionRequest.getId());
        Mockito.verify(likeActionRepository).findById(likeActionKey);
        Mockito.verify(likeActionRepository).save(likeAction);
        Mockito.verify(mapper).writeValueAsString(likeActionRequest);
        Mockito.verify(responseUtils).createGeneralInformationResponse(Mockito.eq(isSuccess), Mockito.eq(message));

    }

    @Test
    void shouldReturnGeneralInformationResponseWithIsSuccessTrue_whenLikeActionRequestParametersAreFillTrueAndTypeSendPostAndUserIsLikedFirstTimeThePost() throws IOException {
        String username = "testUser";
        ActionObjectEnum actionObjectType = ActionObjectEnum.POST;
        Boolean isSuccess = true;
        String message = actionObjectType + " liked.";
        LikeActionRequest likeActionRequest = new LikeActionRequest(0, actionObjectType, username);
        User user = new User("test", "user", Gender.MAN, "aaa@gmail.com", "5555555555", username, "123", Role.USER);
        Post post = new Post(0, "title", "content", 0, 0, user, new ArrayList<>());
        LikeActionKey likeActionKey = new LikeActionKey(username, actionObjectType, 0);
        LikeAction likeAction = new LikeAction(likeActionKey, true);
        GeneralInformationResponse expectedResponse = new GeneralInformationResponse(isSuccess, message);

        Mockito.when(userService.findByUsername(username)).thenReturn(user);
        Mockito.when(userUtils.isSessionUser(user)).thenReturn(true);
        Mockito.when(postService.findById(likeActionRequest.getId())).thenReturn(post);
        Mockito.when(likeActionRepository.findById(likeActionKey)).thenReturn(Optional.empty());
        Mockito.when(likeActionRepository.save(likeAction)).thenReturn(likeAction);
        Mockito.when(mapper.writeValueAsString(likeActionRequest)).thenReturn("String");
        Mockito.when(responseUtils.createGeneralInformationResponse(Mockito.eq(isSuccess), Mockito.eq(message)))
                .thenReturn(expectedResponse);

        GeneralInformationResponse result = likeActionService.like(likeActionRequest);

        Assertions.assertEquals(expectedResponse, result);

        Mockito.verify(userService).findByUsername(username);
        Mockito.verify(userUtils).isSessionUser(user);
        Mockito.verify(postService).findById(likeActionRequest.getId());
        Mockito.verify(likeActionRepository).findById(likeActionKey);
        Mockito.verify(likeActionRepository).save(likeAction);
        Mockito.verify(mapper).writeValueAsString(likeActionRequest);
        Mockito.verify(responseUtils).createGeneralInformationResponse(Mockito.eq(isSuccess), Mockito.eq(message));

    }

    @Test
    void shouldReturnGeneralInformationResponseWithIsSuccessTrue_whenLikeActionRequestParametersAreFillTrueAndTypeSendCommentAndUserIsUnlikedTheComment() throws IOException {
        String username = "testUser";
        ActionObjectEnum actionObjectType = ActionObjectEnum.COMMENT;
        Boolean isSuccess = true;
        String message = actionObjectType + " unliked.";
        LikeActionRequest likeActionRequest = new LikeActionRequest(0, actionObjectType, username);
        User user = new User("test", "user", Gender.MAN, "aaa@gmail.com", "5555555555", username, "123", Role.USER);
        Post post = new Post(0, "title", "content", 0, 0, user, new ArrayList<>());
        Comment comment = new Comment(0, "content", post, user);
        LikeActionKey likeActionKey = new LikeActionKey(username, actionObjectType, 0);
        LikeAction likeAction = new LikeAction(likeActionKey, true);
        GeneralInformationResponse expectedResponse = new GeneralInformationResponse(isSuccess, message);

        Mockito.when(userService.findByUsername(username)).thenReturn(user);
        Mockito.when(userUtils.isSessionUser(user)).thenReturn(true);
        Mockito.when(commentService.findById(likeActionRequest.getId())).thenReturn(comment);
        Mockito.when(likeActionRepository.findById(likeActionKey)).thenReturn(Optional.of(likeAction));
        Mockito.when(mapper.writeValueAsString(likeActionRequest)).thenReturn("String");
        Mockito.when(responseUtils.createGeneralInformationResponse(Mockito.eq(isSuccess), Mockito.eq(message)))
                .thenReturn(expectedResponse);

        GeneralInformationResponse result = likeActionService.like(likeActionRequest);

        Assertions.assertEquals(expectedResponse, result);

        Mockito.verify(userService).findByUsername(username);
        Mockito.verify(userUtils).isSessionUser(user);
        Mockito.verify(commentService).findById(likeActionRequest.getId());
        Mockito.verify(likeActionRepository).findById(likeActionKey);
        Mockito.verify(likeActionRepository).delete(likeAction);
        Mockito.verify(mapper).writeValueAsString(likeActionRequest);
        Mockito.verify(responseUtils).createGeneralInformationResponse(Mockito.eq(isSuccess), Mockito.eq(message));

    }

    @Test
    void shouldReturnGeneralInformationResponseWithIsSuccessTrue_whenLikeActionRequestParametersAreFillTrueAndTypeSendPostAndUserIsUnlikedThePost() throws IOException {
        String username = "testUser";
        ActionObjectEnum actionObjectType = ActionObjectEnum.POST;
        Boolean isSuccess = true;
        String message = actionObjectType + " unliked.";
        LikeActionRequest likeActionRequest = new LikeActionRequest(0, actionObjectType, username);
        User user = new User("test", "user", Gender.MAN, "aaa@gmail.com", "5555555555", username, "123", Role.USER);
        Post post = new Post(0, "title", "content", 0, 0, user, new ArrayList<>());
        LikeActionKey likeActionKey = new LikeActionKey(username, actionObjectType, 0);
        LikeAction likeAction = new LikeAction(likeActionKey, true);
        GeneralInformationResponse expectedResponse = new GeneralInformationResponse(isSuccess, message);

        Mockito.when(userService.findByUsername(username)).thenReturn(user);
        Mockito.when(userUtils.isSessionUser(user)).thenReturn(true);
        Mockito.when(postService.findById(likeActionRequest.getId())).thenReturn(post);
        Mockito.when(likeActionRepository.findById(likeActionKey)).thenReturn(Optional.of(likeAction));
        Mockito.when(mapper.writeValueAsString(likeActionRequest)).thenReturn("String");
        Mockito.when(responseUtils.createGeneralInformationResponse(Mockito.eq(isSuccess), Mockito.eq(message)))
                .thenReturn(expectedResponse);

        GeneralInformationResponse result = likeActionService.like(likeActionRequest);

        Assertions.assertEquals(expectedResponse, result);

        Mockito.verify(userService).findByUsername(username);
        Mockito.verify(userUtils).isSessionUser(user);
        Mockito.verify(postService).findById(likeActionRequest.getId());
        Mockito.verify(likeActionRepository).findById(likeActionKey);
        Mockito.verify(likeActionRepository).delete(likeAction);
        Mockito.verify(mapper).writeValueAsString(likeActionRequest);
        Mockito.verify(responseUtils).createGeneralInformationResponse(Mockito.eq(isSuccess), Mockito.eq(message));

    }

    @Test
    void shouldReturnGeneralInformationResponseWithIsSuccessFalse_whenUserIsNotFoundWithLikeActionRequestUsername() throws IOException {
        String username = "testUser";
        ActionObjectEnum actionObjectType = ActionObjectEnum.POST;
        Boolean isSuccess = false;
        String message = "User not found.";
        LikeActionRequest likeActionRequest = new LikeActionRequest(0, actionObjectType, username);
        GeneralInformationResponse expectedResponse = new GeneralInformationResponse(isSuccess, message);

        Mockito.when(userService.findByUsername(username)).thenReturn(null);
        Mockito.when(mapper.writeValueAsString(likeActionRequest)).thenReturn("String");
        Mockito.when(responseUtils.createGeneralInformationResponse(Mockito.eq(isSuccess), Mockito.eq(message)))
                .thenReturn(expectedResponse);

        GeneralInformationResponse result = likeActionService.like(likeActionRequest);

        Assertions.assertEquals(expectedResponse, result);

        Mockito.verify(userService).findByUsername(username);
        Mockito.verify(mapper).writeValueAsString(likeActionRequest);
        Mockito.verify(responseUtils).createGeneralInformationResponse(Mockito.eq(isSuccess), Mockito.eq(message));

    }

    @Test
    void shouldReturnGeneralInformationResponseWithIsSuccessFalse_whenUserIsNotSessionUserWithLikeActionRequestUsername() throws IOException {
        String username = "testUser";
        ActionObjectEnum actionObjectType = ActionObjectEnum.POST;
        Boolean isSuccess = false;
        String message = "This user is not session user.";
        LikeActionRequest likeActionRequest = new LikeActionRequest(0, actionObjectType, username);
        User user = new User("test", "user", Gender.MAN, "aaa@gmail.com", "5555555555", username, "123", Role.USER);
        GeneralInformationResponse expectedResponse = new GeneralInformationResponse(isSuccess, message);

        Mockito.when(userService.findByUsername(username)).thenReturn(user);
        Mockito.when(userUtils.isSessionUser(user)).thenReturn(false);
        Mockito.when(mapper.writeValueAsString(likeActionRequest)).thenReturn("String");
        Mockito.when(responseUtils.createGeneralInformationResponse(Mockito.eq(isSuccess), Mockito.eq(message)))
                .thenReturn(expectedResponse);

        GeneralInformationResponse result = likeActionService.like(likeActionRequest);

        Assertions.assertEquals(expectedResponse, result);

        Mockito.verify(userService).findByUsername(username);
        Mockito.verify(userUtils).isSessionUser(user);
        Mockito.verify(mapper).writeValueAsString(likeActionRequest);
        Mockito.verify(responseUtils).createGeneralInformationResponse(Mockito.eq(isSuccess), Mockito.eq(message));

    }

    @Test
    void shouldReturnGeneralInformationResponseWithIsSuccessFalse_whenCommentNotFoundWithLikeActionRequestId() throws IOException {
        String username = "testUser";
        ActionObjectEnum actionObjectType = ActionObjectEnum.COMMENT;
        Boolean isSuccess = false;
        String message = "This action id is not found in database.";
        LikeActionRequest likeActionRequest = new LikeActionRequest(0, actionObjectType, username);
        User user = new User("test", "user", Gender.MAN, "aaa@gmail.com", "5555555555", username, "123", Role.USER);
        GeneralInformationResponse expectedResponse = new GeneralInformationResponse(isSuccess, message);

        Mockito.when(userService.findByUsername(username)).thenReturn(user);
        Mockito.when(userUtils.isSessionUser(user)).thenReturn(true);
        Mockito.when(commentService.findById(likeActionRequest.getId())).thenReturn(null);
        Mockito.when(mapper.writeValueAsString(likeActionRequest)).thenReturn("String");
        Mockito.when(responseUtils.createGeneralInformationResponse(Mockito.eq(isSuccess), Mockito.eq(message)))
                .thenReturn(expectedResponse);

        GeneralInformationResponse result = likeActionService.like(likeActionRequest);

        Assertions.assertEquals(expectedResponse, result);

        Mockito.verify(userService).findByUsername(username);
        Mockito.verify(userUtils).isSessionUser(user);
        Mockito.verify(commentService).findById(likeActionRequest.getId());
        Mockito.verify(mapper).writeValueAsString(likeActionRequest);
        Mockito.verify(responseUtils).createGeneralInformationResponse(Mockito.eq(isSuccess), Mockito.eq(message));

    }

    @Test
    void shouldReturnGeneralInformationResponseWithIsSuccessFalse_whenPostNotFoundWithLikeActionRequestId() throws IOException {
        String username = "testUser";
        ActionObjectEnum actionObjectType = ActionObjectEnum.POST;
        Boolean isSuccess = false;
        String message = "This action id is not found in database.";
        LikeActionRequest likeActionRequest = new LikeActionRequest(0, actionObjectType, username);
        User user = new User("test", "user", Gender.MAN, "aaa@gmail.com", "5555555555", username, "123", Role.USER);
        GeneralInformationResponse expectedResponse = new GeneralInformationResponse(isSuccess, message);

        Mockito.when(userService.findByUsername(username)).thenReturn(user);
        Mockito.when(userUtils.isSessionUser(user)).thenReturn(true);
        Mockito.when(postService.findById(likeActionRequest.getId())).thenReturn(null);
        Mockito.when(mapper.writeValueAsString(likeActionRequest)).thenReturn(Mockito.anyString());
        Mockito.when(mapper.writeValueAsString(likeActionRequest)).thenReturn("String");
        Mockito.when(responseUtils.createGeneralInformationResponse(Mockito.eq(isSuccess), Mockito.eq(message)))
                .thenReturn(expectedResponse);

        GeneralInformationResponse result = likeActionService.like(likeActionRequest);

        Assertions.assertEquals(expectedResponse, result);

        Mockito.verify(userService).findByUsername(username);
        Mockito.verify(userUtils).isSessionUser(user);
        Mockito.verify(postService).findById(likeActionRequest.getId());
        Mockito.verify(mapper).writeValueAsString(likeActionRequest);
        Mockito.verify(responseUtils).createGeneralInformationResponse(Mockito.eq(isSuccess), Mockito.eq(message));

    }

    @Test
    void shouldReturnGeneralInformationResponseWithIsSuccessFalse_whenPostOrCommentCannotSaveInDatabase() throws IOException {
        String username = "testUser";
        ActionObjectEnum actionObjectType = ActionObjectEnum.POST;
        Boolean isSuccess = false;
        String message = actionObjectType + " not liked/unliked.";
        LikeActionRequest likeActionRequest = new LikeActionRequest(0, actionObjectType, username);
        User user = new User("test", "user", Gender.MAN, "aaa@gmail.com", "5555555555", username, "123", Role.USER);
        Post post = new Post(0, "title", "content", 0, 0, user, new ArrayList<>());
        LikeActionKey likeActionKey = new LikeActionKey(username, actionObjectType, 0);
        LikeAction likeAction = new LikeAction(likeActionKey, true);
        GeneralInformationResponse expectedResponse = new GeneralInformationResponse(isSuccess, message);

        Mockito.when(userService.findByUsername(username)).thenReturn(user);
        Mockito.when(userUtils.isSessionUser(user)).thenReturn(true);
        Mockito.when(postService.findById(likeActionRequest.getId())).thenReturn(post);
        Mockito.when(likeActionRepository.findById(likeActionKey)).thenReturn(Optional.empty());
        Mockito.when(likeActionRepository.save(likeAction)).thenThrow(new NullPointerException("Exception"));
        Mockito.when(mapper.writeValueAsString(likeActionRequest)).thenReturn("String");
        Mockito.when(responseUtils.createGeneralInformationResponse(Mockito.eq(isSuccess), Mockito.eq(message)))
                .thenReturn(expectedResponse);

        GeneralInformationResponse result = likeActionService.like(likeActionRequest);

        Assertions.assertEquals(expectedResponse, result);

        Mockito.verify(userService).findByUsername(username);
        Mockito.verify(userUtils).isSessionUser(user);
        Mockito.verify(postService).findById(likeActionRequest.getId());
        Mockito.verify(likeActionRepository).findById(likeActionKey);
        Mockito.verify(likeActionRepository).save(likeAction);
        Mockito.verify(mapper).writeValueAsString(likeActionRequest);
        Mockito.verify(responseUtils).createGeneralInformationResponse(Mockito.eq(isSuccess), Mockito.eq(message));

    }

    @Test
    void shouldReturnGeneralInformationResponseWithIsSuccessFalse_whenPostOrCommentCannotDeleteInDatabase() throws IOException {
        String username = "testUser";
        ActionObjectEnum actionObjectType = ActionObjectEnum.POST;
        Boolean isSuccess = false;
        String message = actionObjectType + " not liked/unliked.";
        LikeActionRequest likeActionRequest = new LikeActionRequest(0, actionObjectType, username);
        User user = new User("test", "user", Gender.MAN, "aaa@gmail.com", "5555555555", username, "123", Role.USER);
        Post post = new Post(0, "title", "content", 0, 0, user, new ArrayList<>());
        LikeActionKey likeActionKey = new LikeActionKey(username, actionObjectType, 0);
        LikeAction likeAction = new LikeAction(likeActionKey, true);
        GeneralInformationResponse expectedResponse = new GeneralInformationResponse(isSuccess, message);

        Mockito.when(userService.findByUsername(username)).thenReturn(user);
        Mockito.when(userUtils.isSessionUser(user)).thenReturn(true);
        Mockito.when(postService.findById(likeActionRequest.getId())).thenReturn(post);
        Mockito.when(likeActionRepository.findById(likeActionKey)).thenReturn(Optional.of(likeAction));
        Mockito.doThrow(new NullPointerException("Exception")).when(likeActionRepository).delete(likeAction);
        Mockito.when(mapper.writeValueAsString(likeActionRequest)).thenReturn("String");
        Mockito.when(responseUtils.createGeneralInformationResponse(Mockito.eq(isSuccess), Mockito.eq(message)))
                .thenReturn(expectedResponse);

        GeneralInformationResponse result = likeActionService.like(likeActionRequest);

        Assertions.assertEquals(expectedResponse, result);

        Mockito.verify(userService).findByUsername(username);
        Mockito.verify(userUtils).isSessionUser(user);
        Mockito.verify(postService).findById(likeActionRequest.getId());
        Mockito.verify(likeActionRepository).findById(likeActionKey);
        Mockito.verify(likeActionRepository).delete(likeAction);
        Mockito.verify(mapper).writeValueAsString(likeActionRequest);
        Mockito.verify(responseUtils).createGeneralInformationResponse(Mockito.eq(isSuccess), Mockito.eq(message));

    }
}
