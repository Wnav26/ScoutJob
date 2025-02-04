package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.CompanyRepository;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company handleSaveCompany(Company company) {
        Company wnav = this.companyRepository.save(company);
        return wnav;
    }

    public void deleteCompanyById(long id) {

        this.companyRepository.deleteById(id);
    }

    public ResultPaginationDTO getAllCompanies(Specification<Company> spec, Pageable pageable) {

        Page<Company> pageCompany = this.companyRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pageCompany.getTotalPages());
        mt.setTotal(pageCompany.getTotalElements());
        rs.setMeta(mt);
        rs.setResult(pageCompany.getContent());
        return rs;
    }

    public Company getCompanyById(long id) {
        Optional<Company> companyOptional = this.companyRepository.findById(id);
        if (companyOptional.isPresent()) {
            return companyOptional.get();
        }
        return null;
    }

    public Company handleUpdateCompany(Company reqCompany) {
        Company wnav = this.getCompanyById(reqCompany.getId());
        if (wnav != null) {
            wnav.setName(reqCompany.getName());
            wnav.setAddress(reqCompany.getAddress());
            wnav.setDescription(reqCompany.getDescription());
            wnav.setLogo(reqCompany.getLogo());
            wnav.setUpdatedAt(reqCompany.getUpdatedAt());
            wnav.setUpdatedBy(reqCompany.getUpdatedBy());
            wnav = this.companyRepository.save(wnav);

        }
        return wnav;
    }
}
