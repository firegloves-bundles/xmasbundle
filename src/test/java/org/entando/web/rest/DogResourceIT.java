package org.entando.web.rest;

import org.entando.XmasbundleApp;
import org.entando.config.TestSecurityConfiguration;
import org.entando.domain.Dog;
import org.entando.repository.DogRepository;
import org.entando.service.DogService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link DogResource} REST controller.
 */
@SpringBootTest(classes = { XmasbundleApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class DogResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private DogRepository dogRepository;

    @Autowired
    private DogService dogService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDogMockMvc;

    private Dog dog;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dog createEntity(EntityManager em) {
        Dog dog = new Dog()
            .name(DEFAULT_NAME);
        return dog;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dog createUpdatedEntity(EntityManager em) {
        Dog dog = new Dog()
            .name(UPDATED_NAME);
        return dog;
    }

    @BeforeEach
    public void initTest() {
        dog = createEntity(em);
    }

    @Test
    @Transactional
    public void createDog() throws Exception {
        int databaseSizeBeforeCreate = dogRepository.findAll().size();
        // Create the Dog
        restDogMockMvc.perform(post("/api/dogs").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dog)))
            .andExpect(status().isCreated());

        // Validate the Dog in the database
        List<Dog> dogList = dogRepository.findAll();
        assertThat(dogList).hasSize(databaseSizeBeforeCreate + 1);
        Dog testDog = dogList.get(dogList.size() - 1);
        assertThat(testDog.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createDogWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = dogRepository.findAll().size();

        // Create the Dog with an existing ID
        dog.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDogMockMvc.perform(post("/api/dogs").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dog)))
            .andExpect(status().isBadRequest());

        // Validate the Dog in the database
        List<Dog> dogList = dogRepository.findAll();
        assertThat(dogList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllDogs() throws Exception {
        // Initialize the database
        dogRepository.saveAndFlush(dog);

        // Get all the dogList
        restDogMockMvc.perform(get("/api/dogs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dog.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
    
    @Test
    @Transactional
    public void getDog() throws Exception {
        // Initialize the database
        dogRepository.saveAndFlush(dog);

        // Get the dog
        restDogMockMvc.perform(get("/api/dogs/{id}", dog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dog.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }
    @Test
    @Transactional
    public void getNonExistingDog() throws Exception {
        // Get the dog
        restDogMockMvc.perform(get("/api/dogs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDog() throws Exception {
        // Initialize the database
        dogService.save(dog);

        int databaseSizeBeforeUpdate = dogRepository.findAll().size();

        // Update the dog
        Dog updatedDog = dogRepository.findById(dog.getId()).get();
        // Disconnect from session so that the updates on updatedDog are not directly saved in db
        em.detach(updatedDog);
        updatedDog
            .name(UPDATED_NAME);

        restDogMockMvc.perform(put("/api/dogs").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedDog)))
            .andExpect(status().isOk());

        // Validate the Dog in the database
        List<Dog> dogList = dogRepository.findAll();
        assertThat(dogList).hasSize(databaseSizeBeforeUpdate);
        Dog testDog = dogList.get(dogList.size() - 1);
        assertThat(testDog.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingDog() throws Exception {
        int databaseSizeBeforeUpdate = dogRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDogMockMvc.perform(put("/api/dogs").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dog)))
            .andExpect(status().isBadRequest());

        // Validate the Dog in the database
        List<Dog> dogList = dogRepository.findAll();
        assertThat(dogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDog() throws Exception {
        // Initialize the database
        dogService.save(dog);

        int databaseSizeBeforeDelete = dogRepository.findAll().size();

        // Delete the dog
        restDogMockMvc.perform(delete("/api/dogs/{id}", dog.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Dog> dogList = dogRepository.findAll();
        assertThat(dogList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
