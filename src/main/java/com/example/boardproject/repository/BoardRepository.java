package com.example.boardproject.repository;

import com.example.boardproject.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface BoardRepository extends CrudRepository <Board, Long> {
    Page<Board> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
