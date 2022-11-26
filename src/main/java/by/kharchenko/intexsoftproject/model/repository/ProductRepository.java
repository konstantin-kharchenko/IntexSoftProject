package by.kharchenko.intexsoftproject.model.repository;

import by.kharchenko.intexsoftproject.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    @Query(value = "select p from Product p where p.availability = true order by p.name")
    Page<Product> findByCurrentPage(Pageable pageable);
}
