package com.example.safecheck.repository;

import com.example.safecheck.entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LogRepository extends JpaRepository<Log, Integer> {

    List<Log> findByUser_Id(Long userId);
}
