package com.semiuniv.semiu.controller;

import com.semiuniv.semiu.dto.ProfessorDto;
import com.semiuniv.semiu.entity.Department;
import com.semiuniv.semiu.repository.DepartmentRepository;
import com.semiuniv.semiu.service.ProfessorService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/semi/admin/professor")
public class ProfessorController {

    private final DepartmentRepository departmentRepository;
    private final ProfessorService professorService;

    public ProfessorController(DepartmentRepository departmentRepository, ProfessorService professorService) {
        this.departmentRepository = departmentRepository;
        this.professorService = professorService;
    }

    //등록
    @GetMapping("/insertForm")
    public String insertForm(Model model) {
        List<Department> departments = departmentRepository.findAll();
        model.addAttribute("departments", departments);
        model.addAttribute("professorDto", new ProfessorDto());
        return  "professors/insertProfessorForm";
    }

    @PostMapping("/insert")
    public String insert(@Valid @ModelAttribute("professorDto") ProfessorDto dto,
                         BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "professors/insertProfessorForm";
        }

        professorService.insertProfessor(dto);
        return "redirect:/semi/admin/professor/show";
    }

    //조회 + 검색
    @GetMapping("/show")
    public String showAll(Model model,
                          @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
                          @RequestParam(value = "keyword", defaultValue = "") String keyword) {

        Page<ProfessorDto> professorDto = null;

        if (keyword == null || keyword.isEmpty()) {
            professorDto = professorService.showAllProfessors(pageable);
        } else {
            try {
                int id = Integer.parseInt(keyword);
                // 키워드가 숫자로 변환될 수 있으면 ID로 검색
                professorDto = professorService.searchProfessorById(id, pageable);
            } catch (NumberFormatException e) {
                // 숫자로 변환되지 않는 경우 이름으로 검색
                professorDto = professorService.searchProfessorByName(keyword, pageable);
            }
        }

        model.addAttribute("professorDto", professorDto);
        return "professors/showProfessors";
    }

    //수정
    @GetMapping("/updateForm/{updateId}")
    public String updateForm(@PathVariable("updateId") Integer id, Model model) {
        List<Department> departments = departmentRepository.findAll();
        model.addAttribute("departments", departments);

        ProfessorDto professorDto = professorService.showOneProfessor(id);
        model.addAttribute("professorDto", professorDto);
        return "professors/updateProfessorForm";
    }

    @PostMapping("/update")
    public String update(@Valid @ModelAttribute("professorDto") ProfessorDto dto,
                         BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "professors/updateProfessorForm";
        }

        professorService.updateProfessor(dto);
        return "redirect:/semi/admin/professor/show";
    }

    //삭제
    @PostMapping("/delete/{deleteId}")
    public String delete(@PathVariable("deleteId") Integer id) {
        professorService.deleteProfessor(id);
        return "redirect:/semi/admin/professor/show";
    }
}
