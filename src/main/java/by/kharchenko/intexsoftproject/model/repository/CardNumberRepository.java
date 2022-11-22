package by.kharchenko.intexsoftproject.model.repository;

import by.kharchenko.intexsoftproject.model.entity.CardNumber;
import by.kharchenko.intexsoftproject.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CardNumberRepository extends JpaRepository<CardNumber, Long> {

    @Query("from CardNumber c where c.user = ?1")
    Optional<CardNumber> findByUser(User user);
}
