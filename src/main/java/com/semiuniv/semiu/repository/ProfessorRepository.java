package com.semiuniv.semiu.repository;

import com.semiuniv.semiu.entity.Professor;
import com.semiuniv.semiu.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Integer> {
}
