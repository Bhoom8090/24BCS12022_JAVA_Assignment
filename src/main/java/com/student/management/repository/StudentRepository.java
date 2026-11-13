package com.student.management.repository;

import com.student.management.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    // derived query -> select * from students where lower(course) = lower(?)
    List<Student> findByCourseIgnoreCase(String course);

    Optional<Student> findByEmail(String email);
}
