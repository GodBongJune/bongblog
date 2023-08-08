package shop.mtcoding.blog.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReplyWriteDTO {
    private Integer boardId; // Integer을 해야 null
    private String comment;
}
