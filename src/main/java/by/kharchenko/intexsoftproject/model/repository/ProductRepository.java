package by.kharchenko.intexsoftproject.model.repository;

import by.kharchenko.intexsoftproject.model.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = "select p from Product p where p.availability = true order by p.name")
    List<Product> findByCurrentPage(Pageable pageable);
}
