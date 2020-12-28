package org.entando.repository;

import org.entando.domain.Dog;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Dog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DogRepository extends JpaRepository<Dog, Long> {
}
