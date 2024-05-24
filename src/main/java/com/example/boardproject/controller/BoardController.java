package com.example.boardproject.controller;

import com.example.boardproject.domain.Board;
import com.example.boardproject.service.BoardService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @GetMapping("/list")
    public String getAllBoards(@RequestParam(defaultValue = "1") int page,
                               @RequestParam(defaultValue = "5") int pageSize,
                               Model model) {
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by("id").ascending());
        Iterable<Board> boards = boardService.getAllBoards(pageable);
        model.addAttribute("boards", boards);
        model.addAttribute("page", page);

        return "board/list";
    }

    @GetMapping("/view")
    public String viewPostDetails(@RequestParam(name = "id") Long id, Model model) {
        Board board = boardService.getBoardById(id);
        model.addAttribute("board", board);
        return "board/view";
    }

    @GetMapping("/writeform")
    public String showWriteForm() {
        return "board/writeform";
    }

    @PostMapping("/write")
    public String createBoard(@RequestParam("name") String name,
                              @RequestParam("title") String title,
                              @RequestParam("content") String content,
                              @RequestParam("password") String password,
                              RedirectAttributes redirectAttributes) {
        Board board = new Board();
        board.setName(name);
        board.setTitle(title);
        board.setContent(content);
        board.setPassword(password);
        board.setCreatedAt(LocalDateTime.now());

        boardService.createBoard(board);
        redirectAttributes.addFlashAttribute("message", "Board created successfully!");
        return "redirect:/list";
    }

    @GetMapping("/deleteform")
    public String showDeleteForm(@RequestParam("id") Long id, Model model) {
        // 게시글 ID를 이용하여 해당 게시글을 찾아 모델에 추가
        Board board = boardService.getBoardById(id);
        model.addAttribute("board", board);
        return "board/deleteform";
    }

    @PostMapping("/delete")
    public String deleteBoard(@RequestParam("id") Long id,
                              @RequestParam("password") String password) {
        // 게시글 가져오기
        Board board = boardService.getBoardById(id);
        // 암호 검증
        if (board != null && board.getPassword().equals(password)) {
            // 삭제
            boardService.deleteBoard(id);
            // 삭제 후 리스트로 리다이렉트
            return "redirect:/list";
        } else {
            return "redirect:/deleteform?id=" + id;
        }
    }

    @GetMapping("/updateform")
    public String showUpdateForm(@RequestParam("id") Long id, Model model) {
        Board board = boardService.getBoardById(id);
        model.addAttribute("board", board);
        return "board/updateform";
    }

    @PostMapping("/update")
    public String updateBoard(@RequestParam("id") Long id,
                              @RequestParam("title") String title,
                              @RequestParam("content") String content,
                              @RequestParam("password") String password,
                              RedirectAttributes redirectAttributes) {
        Board board = boardService.getBoardById(id);
        if(board != null && board.getPassword().equals(password)) {
            board.setTitle(title);
            board.setContent(content);
            boardService.updateBoard(board);

            redirectAttributes.addFlashAttribute("message", "board update success");
            return "redirect:/view?id=" + id;
        } else {
            redirectAttributes.addFlashAttribute("error", "invaild passwore");
            return "redirect:/updateform?id=" + id;
        }
    }
}
