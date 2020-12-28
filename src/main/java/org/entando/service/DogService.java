package org.entando.service;

import org.entando.domain.Dog;
import org.entando.repository.DogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Dog}.
 */
@Service
@Transactional
public class DogService {

    private final Logger log = LoggerFactory.getLogger(DogService.class);

    private final DogRepository dogRepository;

    public DogService(DogRepository dogRepository) {
        this.dogRepository = dogRepository;
    }

    /**
     * Save a dog.
     *
     * @param dog the entity to save.
     * @return the persisted entity.
     */
    public Dog save(Dog dog) {
        log.debug("Request to save Dog : {}", dog);
        return dogRepository.save(dog);
    }

    /**
     * Get all the dogs.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Dog> findAll() {
        log.debug("Request to get all Dogs");
        return dogRepository.findAll();
    }


    /**
     * Get one dog by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Dog> findOne(Long id) {
        log.debug("Request to get Dog : {}", id);
        return dogRepository.findById(id);
    }

    /**
     * Delete the dog by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Dog : {}", id);

        dogRepository.deleteById(id);
    }
}
