package com.example.boardproject.service;

import com.example.boardproject.domain.Board;
import com.example.boardproject.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    public void createBoard(Board board) {
        boardRepository.save(board);
    }

    public Iterable<Board> getAllBoards(Pageable pageable) {
        return boardRepository.findAllByOrderByCreatedAtDesc(pageable).getContent();
    }

    public Board getBoardById(Long id) {
        return boardRepository.findById(id).orElse(null);
    }

    public void updateBoard(Board board) {
        boardRepository.save(board);
    }

    public void deleteBoard(Long id) {
        boardRepository.deleteById(id);
    }
}
