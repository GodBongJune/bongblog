package shop.mtcoding.blog.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import shop.mtcoding.blog.dto.ReplyWriteDTO;
import shop.mtcoding.blog.model.User;
import shop.mtcoding.blog.repository.ReplyRepository;

@Controller
public class ReplyController {
    @Autowired
    private HttpSession session;
    @Autowired
    private ReplyRepository replyRepository;

    @PostMapping("/reply/save")
    public String save(ReplyWriteDTO replyWriteDTO) {
        // comment 유효성 검사 (boardId null인지 공백인지 comment null,공백)
        if (replyWriteDTO.getComment() == null || replyWriteDTO.getComment().isEmpty()) {
            return "redirect:/40x";
        }
        if (replyWriteDTO.getBoardId() == null) {
            return "redirect:/40x";
        }
        // 인증검사
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/loginForm";
        }
        // 댓글 작성
        replyRepository.save(replyWriteDTO, sessionUser.getId());

        // 댓글을 작성했으니 상세보기 페이지로 돌려준다
        return "redirect:/board/" + replyWriteDTO.getBoardId();
    }

}
