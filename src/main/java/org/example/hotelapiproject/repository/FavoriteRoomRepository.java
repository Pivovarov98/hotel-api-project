package org.example.hotelapiproject.repository;

import org.example.hotelapiproject.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRoomRepository extends JpaRepository<FavoriteRoom, Long> {

    Optional<FavoriteRoom> findByAccountAndRoom(Account account, Room room);

    boolean existsByAccountAndRoom(Account account, Room room);

    List<FavoriteRoom> findAllByAccount(Account account);
}
