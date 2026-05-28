package org.example.hotelapiproject.repository;

import org.example.hotelapiproject.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
}
