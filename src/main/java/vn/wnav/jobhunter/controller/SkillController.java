package vn.wnav.jobhunter.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.wnav.jobhunter.domain.Company;
import vn.wnav.jobhunter.domain.Skill;
import vn.wnav.jobhunter.domain.User;
import vn.wnav.jobhunter.domain.response.ResUpdateUserDTO;
import vn.wnav.jobhunter.domain.response.ResultPaginationDTO;
import vn.wnav.jobhunter.service.SkillService;
import vn.wnav.jobhunter.util.annotation.ApiMessage;
import vn.wnav.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class SkillController {
    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @PostMapping("/skills")
    @ApiMessage("Create a skill")
    public ResponseEntity<Skill> create(@Valid @RequestBody Skill s) throws IdInvalidException {
        // check name
        if (s.getName() != null && this.skillService.isSkillExist(s.getName())) {
            throw new IdInvalidException("Skill name = " + s.getName() + " đã tồn tại");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(this.skillService.handleSaveSkill(s));
    }

    @PutMapping("/skills")
    @ApiMessage("Update a skill")
    public ResponseEntity<Skill> update(@Valid @RequestBody Skill s) throws IdInvalidException {
        // check id
        Skill currentSkill = this.skillService.getSkillById(s.getId());
        if (currentSkill == null) {
            throw new IdInvalidException("Skill id = " + s.getId() + " không tồn tại");
        }

        // check name
        if (s.getName() != null && this.skillService.isSkillExist(s.getName())) {
            throw new IdInvalidException("Skill name = " + s.getName() + " đã tồn tại");
        }

        currentSkill.setName(s.getName());
        return ResponseEntity.ok().body(this.skillService.updateSkill(currentSkill));
    }

    @GetMapping("/skills")
    @ApiMessage("fetch all skill")

    public ResponseEntity<ResultPaginationDTO> getAllSkill(
            @Filter Specification<Skill> spec, Pageable pageable) {

        return ResponseEntity.ok(this.skillService.getAllSkills(spec, pageable));
    }

    @DeleteMapping("/skills/{id}")
    @ApiMessage("Delete a skill")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
        // check id
        Skill currentSkill = this.skillService.getSkillById(id);
        if (currentSkill == null) {
            throw new IdInvalidException("Skill id = " + id + " không tồn tại");
        }
        this.skillService.deleteSkill(id);
        return ResponseEntity.ok().body(null);
    }
}
