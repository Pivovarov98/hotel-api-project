package org.example.hotelapiproject.repository;

import org.example.hotelapiproject.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteRoomRepository extends JpaRepository<FavoriteRoom, Long> {

    boolean existsByAccountAndRoom(
            Account account,
            Room room
    );

    void deleteByAccountAndRoom(
            Account account,
            Room room
    );

    List<FavoriteRoom> findAllByAccount(Account account);
}
