package ru.kataaas.kaflent.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ru.kataaas.kaflent.payload.EmotionResponse;
import ru.kataaas.kaflent.payload.PostDTO;
import ru.kataaas.kaflent.payload.PostResponse;
import ru.kataaas.kaflent.entity.PostEntity;
import ru.kataaas.kaflent.service.CommentService;
import ru.kataaas.kaflent.service.EmotionService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PostMapper {

    private final EmotionService emotionService;

    private final CommentService commentService;

    @Autowired
    public PostMapper(EmotionService emotionService, CommentService commentService) {
        this.emotionService = emotionService;
        this.commentService = commentService;
    }

    public PostResponse toPostResponse(Page<PostEntity> posts) {
        PostResponse postResponse = new PostResponse();
        List<PostDTO> content = new ArrayList<>();
        // get post content from page and convert to postDTO
        posts.getContent().forEach(post -> content.add(toPostDTO(post)));

        postResponse.setPosts(content);
        postResponse.setPageNo(posts.getNumber());
        postResponse.setPageSize(posts.getSize());
        postResponse.setTotalElements(posts.getTotalElements());
        postResponse.setTotalPages(posts.getTotalPages());
        postResponse.setLast(posts.isLast());

        return postResponse;
    }

    public PostDTO toPostDTO(PostEntity post) {
        PostDTO postDTO = new PostDTO();
        Set<String> files = new HashSet<>();
        post.getFiles().forEach(file -> files.add(file.getFilename()));

        postDTO.setId(post.getId());
        postDTO.setContent(post.getContent());
        postDTO.setFiles(files);
        postDTO.setCreatedAt(post.getCreatedAt());
        postDTO.setGroupId(post.getGroupId());
        postDTO.setCountOfEmotions(emotionService.getCountOfEmotions(post.getId()));
        postDTO.setCountOfComments(commentService.getCountOfComments(post.getId()));

        return postDTO;
    }

    public EmotionResponse toEmotionResponse(Page<String> emotions) {
        EmotionResponse emotionResponse = new EmotionResponse();
        emotionResponse.setEmotions(emotions.getContent());
        emotionResponse.setPageNo(emotions.getNumber());
        emotionResponse.setPageSize(emotions.getSize());
        emotionResponse.setTotalElements(emotions.getTotalElements());
        emotionResponse.setTotalPages(emotions.getTotalPages());
        emotionResponse.setLast(emotions.isLast());

        return emotionResponse;
    }

}
