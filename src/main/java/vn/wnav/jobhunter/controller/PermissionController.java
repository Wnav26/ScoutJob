package vn.wnav.jobhunter.controller;

import java.util.Optional;

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
import vn.wnav.jobhunter.domain.Job;
import vn.wnav.jobhunter.domain.Permission;
import vn.wnav.jobhunter.domain.response.ResultPaginationDTO;
import vn.wnav.jobhunter.domain.response.job.ResUpdateJobDTO;
import vn.wnav.jobhunter.service.CompanyService;
import vn.wnav.jobhunter.service.PermissionService;
import vn.wnav.jobhunter.util.annotation.ApiMessage;
import vn.wnav.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class PermissionController {
    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PostMapping("/permissions")
    @ApiMessage("create permission")

    public ResponseEntity<Permission> createNewPermission(@Valid @RequestBody Permission postManPer)
            throws IdInvalidException {
        if (this.permissionService.isPermissionExist(postManPer)) {
            throw new IdInvalidException("Permission đã tồn tại.");
        }
        Permission permission = this.permissionService.handleSavePermission(postManPer);
        return ResponseEntity.status(HttpStatus.CREATED).body(permission);
    }

    @PutMapping("/permissions")
    @ApiMessage("Update a permission")
    public ResponseEntity<Permission> update(@Valid @RequestBody Permission permission) throws IdInvalidException {
        if (this.permissionService.fetchById(permission.getId()) == null) {
            throw new IdInvalidException("Permission với id = " + permission.getId() + " không tồn tại.");
        }

        if (this.permissionService.isPermissionExist(permission)) {
            // check name
            if (this.permissionService.isSameName(permission)) {
                throw new IdInvalidException("Permission đã tồn tại.");
            }
        }

        return ResponseEntity.ok()
                .body(this.permissionService.update(permission));
    }

    @DeleteMapping("/permissions/{id}")
    @ApiMessage("delete a permission")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
        // check exist by id
        if (this.permissionService.fetchById(id) == null) {
            throw new IdInvalidException("Permission với id = " + id + " không tồn tại.");
        }
        this.permissionService.delete(id);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/permissions")
    @ApiMessage("Fetch permissions")
    public ResponseEntity<ResultPaginationDTO> getPermissions(
            @Filter Specification<Permission> spec, Pageable pageable) {

        return ResponseEntity.ok(this.permissionService.getPermissions(spec, pageable));
    }
}
