package vn.wnav.jobhunter.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.wnav.jobhunter.domain.Company;
import vn.wnav.jobhunter.domain.Skill;
import vn.wnav.jobhunter.domain.response.ResultPaginationDTO;
import vn.wnav.jobhunter.repository.SkillRepository;

@Service
public class SkillService {
    private final SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public boolean isSkillExist(String name) {
        return this.skillRepository.existsByName(name);
    }

    public Skill handleSaveSkill(Skill skill) {
        Skill wnav = this.skillRepository.save(skill);
        return wnav;
    }

    public Skill updateSkill(Skill s) {
        return this.skillRepository.save(s);
    }

    public Skill getSkillById(long id) {
        Optional<Skill> skillOptional = this.skillRepository.findById(id);
        if (skillOptional.isPresent()) {
            return skillOptional.get();
        }
        return null;
    }

    public ResultPaginationDTO getAllSkills(Specification<Skill> spec, Pageable pageable) {

        Page<Skill> pageSkill = this.skillRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pageSkill.getTotalPages());
        mt.setTotal(pageSkill.getTotalElements());
        rs.setMeta(mt);
        rs.setResult(pageSkill.getContent());
        return rs;
    }

    public void deleteSkill(long id) {
        // delete job (inside job_skill table)
        Optional<Skill> skillOptional = this.skillRepository.findById(id);
        Skill currentSkill = skillOptional.get();
        currentSkill.getJobs().forEach(job -> job.getSkills().remove(currentSkill));

        // delete subscriber (inside subscriber_skill table)
        currentSkill.getSubscribers().forEach(subs -> subs.getSkills().remove(currentSkill));

        // delete skill
        this.skillRepository.delete(currentSkill);
    }
}
