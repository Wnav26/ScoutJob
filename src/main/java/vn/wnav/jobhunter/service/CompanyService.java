package vn.wnav.jobhunter.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.wnav.jobhunter.domain.Company;
import vn.wnav.jobhunter.domain.Job;
import vn.wnav.jobhunter.domain.User;
import vn.wnav.jobhunter.domain.response.ResultPaginationDTO;
import vn.wnav.jobhunter.repository.CompanyRepository;
import vn.wnav.jobhunter.repository.JobRepository;
import vn.wnav.jobhunter.repository.UserRepository;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final JobService jobService;

    public CompanyService(CompanyRepository companyRepository, UserRepository userRepository,
            JobRepository jobRepository, JobService jobService) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
        this.jobService = jobService;
    }

    public Company handleSaveCompany(Company company) {
        Company wnav = this.companyRepository.save(company);
        return wnav;
    }

    public void deleteCompanyById(long id) {

        Optional<Company> comOptional = this.companyRepository.findById(id);
        if (comOptional.isPresent()) {
            Company com = comOptional.get();
            // Fetch tất cả job liên quan đến công ty này
            List<Job> jobs = this.jobRepository.findByCompany(com);
            jobs.forEach(job -> {
                // Sử dụng phương thức delete từ JobService để xóa các resume liên kết
                this.jobService.delete(job.getId()); // gọi phương thức delete từ JobService
            });
            // fetch all user belong to this company
            List<User> users = this.userRepository.findByCompany(com);
            this.userRepository.deleteAll(users);
        }

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

    public Optional<Company> getCompanyById(long id) {
        return this.companyRepository.findById(id);
    }

    public Company findCompanyByUserEmail(String email) {
        User user = this.userRepository.findByEmail(email);
        if (user != null) {
            return user.getCompany(); // Giả sử User có trường company
        }
        return null;
    }
    public Company handleUpdateCompany(Company c) {
        Optional<Company> companyOptional = this.companyRepository.findById(c.getId());
        if (companyOptional.isPresent()) {
            Company currentCompany = companyOptional.get();
            currentCompany.setLogo(c.getLogo());
            currentCompany.setName(c.getName());
            currentCompany.setDescription(c.getDescription());
            currentCompany.setAddress(c.getAddress());
            return this.companyRepository.save(currentCompany);
        }
        return null;
    }
}
