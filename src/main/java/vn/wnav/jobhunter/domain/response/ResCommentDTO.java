package vn.wnav.jobhunter.domain.response;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResCommentDTO {
     private long id;
    private String content;
    private Instant createdAt;
    private String createdBy;

    private Long userId;
    private String username;

}
