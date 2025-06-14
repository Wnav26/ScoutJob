package vn.wnav.jobhunter.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import vn.wnav.jobhunter.domain.Comment;
import vn.wnav.jobhunter.domain.Job;
import vn.wnav.jobhunter.domain.User;
import vn.wnav.jobhunter.domain.response.ResCommentDTO;
import vn.wnav.jobhunter.service.CommentService;
import vn.wnav.jobhunter.service.JobService;
import vn.wnav.jobhunter.service.UserService;
import vn.wnav.jobhunter.util.SecurityUtil;
import vn.wnav.jobhunter.util.annotation.ApiMessage;

@RestController
@RequestMapping("/api/v1")
public class CommentController {
    private final CommentService commentService;
    private final JobService jobService;
    private final UserService userService;

    public CommentController(CommentService commentService, JobService jobService, UserService userService) {
        this.commentService = commentService;
        this.jobService = jobService;
        this.userService = userService;
    }

    @PostMapping("/comments/{id}")
    @ApiMessage("create comment")
    public ResponseEntity<Comment> createComment(@PathVariable("id") long id, @RequestBody String content) {

        Job job = jobService.fetchJobById(id).orElseThrow(() -> new RuntimeException("Job not found"));
        String email = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        User currentUser = this.userService.handelGetUserByUsername(email);

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setJob(job);
        comment.setUser(currentUser);

        return ResponseEntity.ok(commentService.save(comment));
    }

    @PutMapping("/comments/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable("id") long id, @RequestBody String content) {
        Comment comment = commentService.findById(id);
        comment.setContent(content);
        return ResponseEntity.ok(commentService.save(comment));
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable("id") long id) {
        commentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/comments/{id}")
    @ApiMessage("fetch comment by job")
    public ResponseEntity<List<ResCommentDTO>> getCommentsByJobId(@PathVariable("id") long id) {
        List<Comment> comments = commentService.findByJobId(id);
        return ResponseEntity.ok(commentService.toDTOList(comments));
    }

}
