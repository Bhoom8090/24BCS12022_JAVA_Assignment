package com.student.management.controller;

import com.student.management.entity.Student;
import com.student.management.exception.DuplicateEmailException;
import com.student.management.exception.ResourceNotFoundException;
import com.student.management.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

// Controller for the frontend (Thymeleaf pages). Runs on the servlet container (Tomcat).
@Controller
public class StudentViewController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/")
    public String home() {
        return "redirect:/students";
    }

    // show all students (optionally filtered by course)
    @GetMapping("/students")
    public String listStudents(@RequestParam(required = false) String course, Model model) {
        List<Student> students;

        if (course != null && !course.trim().isEmpty()) {
            students = studentService.getStudentsByCourse(course.trim());
        } else {
            students = studentService.getAllStudents();
        }

        model.addAttribute("students", students);
        model.addAttribute("course", course);
        return "students";
    }

    // open empty form
    @GetMapping("/students/new")
    public String showAddForm(Model model) {
        model.addAttribute("student", new Student());
        model.addAttribute("title", "Add Student");
        return "student-form";
    }

    // open form with existing data
    @GetMapping("/students/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes ra) {
        try {
            model.addAttribute("student", studentService.getStudentById(id));
            model.addAttribute("title", "Edit Student");
            return "student-form";
        } catch (ResourceNotFoundException e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/students";
        }
    }

    // save (add or update depending on id)
    @PostMapping("/students/save")
    public String saveStudent(@Valid @ModelAttribute("student") Student student,
                              BindingResult result, Model model, RedirectAttributes ra) {

        boolean isNew = (student.getId() == null);
        model.addAttribute("title", isNew ? "Add Student" : "Edit Student");

        if (result.hasErrors()) {
            return "student-form";
        }

        try {
            if (isNew) {
                studentService.addStudent(student);
                ra.addFlashAttribute("success", "Student added successfully!");
            } else {
                studentService.updateStudent(student.getId(), student);
                ra.addFlashAttribute("success", "Student updated successfully!");
            }
        } catch (DuplicateEmailException e) {
            result.rejectValue("email", "duplicate", e.getMessage());
            return "student-form";
        } catch (ResourceNotFoundException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/students";
    }

    @PostMapping("/students/delete/{id}")
    public String deleteStudent(@PathVariable Long id, RedirectAttributes ra) {
        try {
            studentService.deleteStudent(id);
            ra.addFlashAttribute("success", "Student deleted successfully!");
        } catch (ResourceNotFoundException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/students";
    }
}
