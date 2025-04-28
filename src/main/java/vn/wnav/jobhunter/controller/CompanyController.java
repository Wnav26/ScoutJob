package vn.wnav.jobhunter.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.wnav.jobhunter.domain.Company;
import vn.wnav.jobhunter.domain.Job;
import vn.wnav.jobhunter.domain.User;
import vn.wnav.jobhunter.domain.response.ResultPaginationDTO;
import vn.wnav.jobhunter.service.CompanyService;
import vn.wnav.jobhunter.util.annotation.ApiMessage;
import vn.wnav.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class CompanyController {
    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/companies")
    @ApiMessage("create company")

    public ResponseEntity<Company> createNewCompany(@Valid @RequestBody Company postManCompany) {
        Company company = this.companyService.handleSaveCompany(postManCompany);
        return ResponseEntity.status(HttpStatus.CREATED).body(company);
    }

    @DeleteMapping("/companies/{id}")
    @ApiMessage("delete company")

    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
        Optional<Company> currentComp = this.companyService.getCompanyById(id);
        if (!currentComp.isPresent()) {
            throw new IdInvalidException("Company not found");
        }
        this.companyService.deleteCompanyById(id);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/companies")
    @ApiMessage("fetch all company")

    public ResponseEntity<ResultPaginationDTO> getAllCompany(
            @Filter Specification<Company> spec, Pageable pageable) {

        return ResponseEntity.ok(this.companyService.getAllCompanies(spec, pageable));
    }

    @GetMapping("/companies/{id}")
    @ApiMessage("fetch company")

    public ResponseEntity<Company> getCompanyById(@PathVariable("id") long id) {
        Optional<Company> company = this.companyService.getCompanyById(id);
        return ResponseEntity.ok(company.get());
    }

    @PutMapping("/companies")
    @ApiMessage("update company")

    public ResponseEntity<Company> updateCompany(@Valid @RequestBody Company postManCompany) {

        Company company = this.companyService.handleUpdateCompany(postManCompany);
        return ResponseEntity.ok(company);
    }
}
