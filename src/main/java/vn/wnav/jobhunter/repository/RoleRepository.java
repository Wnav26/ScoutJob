package vn.wnav.jobhunter.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import vn.wnav.jobhunter.domain.Job;
import vn.wnav.jobhunter.domain.Permission;
import vn.wnav.jobhunter.domain.Role;
import vn.wnav.jobhunter.domain.Skill;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>,
        JpaSpecificationExecutor<Role> {
    boolean existsByName(String name);

    Role findByName(String name);
}
