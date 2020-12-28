package org.entando.repository;

import org.entando.domain.Conference;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Conference entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConferenceRepository extends JpaRepository<Conference, Long> {
}
