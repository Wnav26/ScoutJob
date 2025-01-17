package vn.hoidanit.jobhunter.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.hoidanit.jobhunter.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User save(User wnav);

    void deleteById(long id);

    List<User> findAll();

    Optional<User> findById(long id);

    User findByEmail(String email);
}
