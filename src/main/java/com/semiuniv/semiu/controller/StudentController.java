package com.semiuniv.semiu.controller;

import com.semiuniv.semiu.dto.StudentDto;
import com.semiuniv.semiu.entity.Department;
import com.semiuniv.semiu.entity.Student;
import com.semiuniv.semiu.repository.DepartmentRepository;
import com.semiuniv.semiu.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/semi/student")
public class StudentController {

    private final DepartmentRepository departmentRepository;
    private final StudentService studentService;

    public StudentController(StudentService studentService, DepartmentRepository departmentRepository) {
        this.studentService = studentService;
        this.departmentRepository = departmentRepository;
    }

    //등록
    @GetMapping("/insertForm")
    public String insertForm(Model model) {
        List<Department> departments = departmentRepository.findAll();
        model.addAttribute("departments", departments);
        model.addAttribute("studentDto", new StudentDto());
        return "students/insertStudent";
    }

    @PostMapping("/insert")
    public String insert(@Valid @ModelAttribute("studentDto") StudentDto dto,
                         BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "students/insertStudent";
        }

        studentService.insertStudent(dto);
        return "redirect:/semi/student/show";
    }

    //조회
    @GetMapping("/show")
    public String showAll(Model model, @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<StudentDto> studentDto = studentService.showAllStudents(pageable);
        model.addAttribute("studentDto", studentDto);
        return "students/showStudents";
    }

    //수정
    @GetMapping("/updateForm/{updateId}")
    public String updateForm(@PathVariable("updateId") Integer id, Model model) {
        List<Department> departments = departmentRepository.findAll();
        model.addAttribute("departments", departments);

        StudentDto studentDto = studentService.showOneStudent(id);
        model.addAttribute("studentDto", studentDto);
        return "students/updateStudent";
    }

    @PostMapping("/update")
    public String update(@Valid @ModelAttribute("studentDto") StudentDto dto,
                         BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "students/updateStudent";
        }

        studentService.updateStudent(dto);
        return "redirect:/semi/student/show";
    }

    //삭제
    @PostMapping("/delete/{deleteId}")
    public String delete(@PathVariable("deleteId") Integer id) {
        studentService.deleteStudent(id);
        return "redirect:/semi/student/show";
    }

}
