package com.student.management.service;

import com.student.management.entity.Student;
import com.student.management.exception.DuplicateEmailException;
import com.student.management.exception.ResourceNotFoundException;
import com.student.management.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public Student addStudent(Student student) {
        // email should be unique
        if (studentRepository.findByEmail(student.getEmail()).isPresent()) {
            throw new DuplicateEmailException("Email already exists: " + student.getEmail());
        }
        student.setId(null); // make sure it is always an insert
        return studentRepository.save(student);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
    }

    public Student updateStudent(Long id, Student newData) {
        Student existing = getStudentById(id);

        // check that the new email is not used by some other student
        Optional<Student> sameEmail = studentRepository.findByEmail(newData.getEmail());
        if (sameEmail.isPresent() && !sameEmail.get().getId().equals(id)) {
            throw new DuplicateEmailException("Email already exists: " + newData.getEmail());
        }

        existing.setName(newData.getName());
        existing.setEmail(newData.getEmail());
        existing.setCourse(newData.getCourse());
        existing.setAge(newData.getAge());

        return studentRepository.save(existing);
    }

    public void deleteStudent(Long id) {
        Student existing = getStudentById(id); // throws 404 if not found
        studentRepository.delete(existing);
    }

    public List<Student> getStudentsByCourse(String course) {
        return studentRepository.findByCourseIgnoreCase(course);
    }
}
