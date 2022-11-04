package by.kharchenko.intexsoftproject.model.repository;

import by.kharchenko.intexsoftproject.model.entity.Role;
import by.kharchenko.intexsoftproject.model.entity.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query("from Role r where r.role in ?1")
    Set<Role> findAllByNames(Set<RoleType> roleTypes);
}
