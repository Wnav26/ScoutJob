package vn.wnav.jobhunter.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import vn.wnav.jobhunter.domain.Comment;
import vn.wnav.jobhunter.domain.response.ResCommentDTO;
import vn.wnav.jobhunter.repository.CommentRepository;

@Service
public class CommentService {
     private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    public void deleteById(Long id) {
        commentRepository.deleteById(id);
    }

    public List<Comment> findByJobId(Long jobId) {
        return commentRepository.findByJobId(jobId);
    }

    public List<Comment> findByUserId(Long userId) {
        return commentRepository.findByUserId(userId);
    }
    public Comment findById(long id){
    	return commentRepository.findById(id).orElse(null);
    }
    public static ResCommentDTO toDTO(Comment comment) {
        return new ResCommentDTO(
            comment.getId(),
            comment.getContent(),
            comment.getCreatedAt(),
            comment.getCreatedBy(),
            comment.getUser() != null ? comment.getUser().getId() : null,
            comment.getUser() != null ? comment.getUser().getName() : null
        );
    }

    public List<ResCommentDTO> toDTOList(List<Comment> comments) {
        return comments.stream()
                       .map(CommentService::toDTO)
                       .collect(Collectors.toList());
    }
}
