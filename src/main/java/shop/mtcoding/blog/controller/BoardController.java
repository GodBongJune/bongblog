package shop.mtcoding.blog.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import shop.mtcoding.blog.dto.UpdateDTO;
import shop.mtcoding.blog.dto.WriteDTO;
import shop.mtcoding.blog.model.Board;
import shop.mtcoding.blog.model.Reply;
import shop.mtcoding.blog.model.User;
import shop.mtcoding.blog.repository.BoardRepository;
import shop.mtcoding.blog.repository.ReplyRepository;

@Controller
public class BoardController {

    @Autowired
    private HttpSession session;

    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private ReplyRepository replyRepository;

    @ResponseBody
    @GetMapping("/test/reply")
    public List<Reply> test2() {
        List<Reply> replys = replyRepository.findByBoardId(1);
        return replys;
    }

    @ResponseBody
    @GetMapping("/test/board/1")
    public Board test() {
        Board board = boardRepository.findById(1);
        return board;
    }

    @PostMapping("/board/{id}/update")
    public String update(@PathVariable Integer id, UpdateDTO updateDTO) {
        // 1. 인증권한

        // 2. 권한 체크

        // 3. 핵심로직
        // update board_tb set title = :title,content=:content where id = :id
        boardRepository.update(updateDTO, id);

        return "redirect:/board/" + id;
    }

    @GetMapping("/board/{id}/updateForm")
    public String updateForm(@PathVariable Integer id, HttpServletRequest request) {
        // 1.인증검사

        // 2.권한체크

        // 3.핵심로직

        Board board = boardRepository.findById(id);
        request.setAttribute("board", board);
        return "board/updateForm";
    }

    @PostMapping("/board/{id}/delete")
    public String delete(@PathVariable Integer id) {// 1.PathVariable값 받기
        // 2.인증검사(포스트맨으로 비정상)(로그인페이지보내기)
        // session에 접근해서 sessionUser키값을 가져오세요
        // null이면 로그인페이지로 보내고
        // null이 아니면 3번을 실행
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/loginForm";
        }

        // 3.권한검사
        Board board = boardRepository.findById(id);
        if (board.getUser().getId() != sessionUser.getId()) {
            return "redirect:/40x"; // 403 403권한없음
        }
        // 4.모델에 접근해서 삭제
        // boardRepository.deleteById(id); 호출 ->리턴받지마
        // delete from board_tb where id = :id
        boardRepository.deleteById(id);

        return "redirect:/";
    }

    @GetMapping({ "/", "/board" })
    // 리퀘스트 파람은 페이지 요청이 null이기에 기본값을 0으로 바꿔야함
    public String index(@RequestParam(defaultValue = "0") Integer page, HttpServletRequest request) {
        // 1.유효성검사x
        // 2.인증검사x

        List<Board> boardList = boardRepository.findAll(page);
        int totalCount = boardRepository.count();
        int totalPage = totalCount / 3;
        if (totalCount % 3 > 0) {
            totalPage = totalPage + 1;
        }
        boolean last = totalPage - 1 == page;

        // System.out.println("테스트 :" + boardList.size());
        // System.out.println("테스트 :" + boardList.get(0).getTitle());

        // 모든언어든 request에 담는다
        request.setAttribute("boardList", boardList);
        request.setAttribute("prevPage", page - 1);
        request.setAttribute("nextPage", page + 1);
        request.setAttribute("first", page == 0 ? true : false);
        request.setAttribute("last", last);
        request.setAttribute("totalPage", totalPage);
        request.setAttribute("totalCount", totalCount);

        return "index";
    }

    // 핵심로직은 인서트만하면되는데 부가로직이 많음
    @PostMapping("/board/save")
    public String save(WriteDTO writeDTO) {
        // validation check(유효성 검사))
        if (writeDTO.getTitle() == null || writeDTO.getTitle().isEmpty()) {
            return "redirect:/40x";
        }
        if (writeDTO.getContent() == null || writeDTO.getContent().isEmpty()) {
            return "redirect:/40x";
        }
        // 인증체크
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/loginForm";
        }
        boardRepository.save(writeDTO, sessionUser.getId());
        return "redirect:/";
    }

    @GetMapping("/board/saveForm")
    public String saveForm() {
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/loginForm";
        }
        return "board/saveForm";
    }

    // localhost:8080/board/1
    // localhost:8080/board/50
    @GetMapping("/board/{id}")
    public String detail(@PathVariable Integer id, HttpServletRequest request) { // Controller
        User sessionUser = (User) session.getAttribute("sessionUser"); // 세션접근
        List<Reply> replys = replyRepository.findByBoardId(id);

        boolean pageOwner = false;
        if (sessionUser != null) {
            pageOwner = sessionUser.getId() == replys.get(0).getUser().getId();
        } else {

        }

        // request.setAttribute("board", board);
        request.setAttribute("pageOwner", pageOwner);
        return "/board/detail"; // View 리턴
    }

}
