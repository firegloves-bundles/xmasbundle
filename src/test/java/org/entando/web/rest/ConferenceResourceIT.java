package org.entando.web.rest;

import org.entando.XmasbundleApp;
import org.entando.config.TestSecurityConfiguration;
import org.entando.domain.Conference;
import org.entando.repository.ConferenceRepository;
import org.entando.service.ConferenceService;

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
 * Integration tests for the {@link ConferenceResource} REST controller.
 */
@SpringBootTest(classes = { XmasbundleApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class ConferenceResourceIT {

    private static final String DEFAULT_STREET = "AAAAAAAAAA";
    private static final String UPDATED_STREET = "BBBBBBBBBB";

    @Autowired
    private ConferenceRepository conferenceRepository;

    @Autowired
    private ConferenceService conferenceService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restConferenceMockMvc;

    private Conference conference;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Conference createEntity(EntityManager em) {
        Conference conference = new Conference()
            .street(DEFAULT_STREET);
        return conference;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Conference createUpdatedEntity(EntityManager em) {
        Conference conference = new Conference()
            .street(UPDATED_STREET);
        return conference;
    }

    @BeforeEach
    public void initTest() {
        conference = createEntity(em);
    }

    @Test
    @Transactional
    public void createConference() throws Exception {
        int databaseSizeBeforeCreate = conferenceRepository.findAll().size();
        // Create the Conference
        restConferenceMockMvc.perform(post("/api/conferences").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(conference)))
            .andExpect(status().isCreated());

        // Validate the Conference in the database
        List<Conference> conferenceList = conferenceRepository.findAll();
        assertThat(conferenceList).hasSize(databaseSizeBeforeCreate + 1);
        Conference testConference = conferenceList.get(conferenceList.size() - 1);
        assertThat(testConference.getStreet()).isEqualTo(DEFAULT_STREET);
    }

    @Test
    @Transactional
    public void createConferenceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = conferenceRepository.findAll().size();

        // Create the Conference with an existing ID
        conference.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restConferenceMockMvc.perform(post("/api/conferences").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(conference)))
            .andExpect(status().isBadRequest());

        // Validate the Conference in the database
        List<Conference> conferenceList = conferenceRepository.findAll();
        assertThat(conferenceList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllConferences() throws Exception {
        // Initialize the database
        conferenceRepository.saveAndFlush(conference);

        // Get all the conferenceList
        restConferenceMockMvc.perform(get("/api/conferences?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(conference.getId().intValue())))
            .andExpect(jsonPath("$.[*].street").value(hasItem(DEFAULT_STREET)));
    }
    
    @Test
    @Transactional
    public void getConference() throws Exception {
        // Initialize the database
        conferenceRepository.saveAndFlush(conference);

        // Get the conference
        restConferenceMockMvc.perform(get("/api/conferences/{id}", conference.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(conference.getId().intValue()))
            .andExpect(jsonPath("$.street").value(DEFAULT_STREET));
    }
    @Test
    @Transactional
    public void getNonExistingConference() throws Exception {
        // Get the conference
        restConferenceMockMvc.perform(get("/api/conferences/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateConference() throws Exception {
        // Initialize the database
        conferenceService.save(conference);

        int databaseSizeBeforeUpdate = conferenceRepository.findAll().size();

        // Update the conference
        Conference updatedConference = conferenceRepository.findById(conference.getId()).get();
        // Disconnect from session so that the updates on updatedConference are not directly saved in db
        em.detach(updatedConference);
        updatedConference
            .street(UPDATED_STREET);

        restConferenceMockMvc.perform(put("/api/conferences").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedConference)))
            .andExpect(status().isOk());

        // Validate the Conference in the database
        List<Conference> conferenceList = conferenceRepository.findAll();
        assertThat(conferenceList).hasSize(databaseSizeBeforeUpdate);
        Conference testConference = conferenceList.get(conferenceList.size() - 1);
        assertThat(testConference.getStreet()).isEqualTo(UPDATED_STREET);
    }

    @Test
    @Transactional
    public void updateNonExistingConference() throws Exception {
        int databaseSizeBeforeUpdate = conferenceRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConferenceMockMvc.perform(put("/api/conferences").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(conference)))
            .andExpect(status().isBadRequest());

        // Validate the Conference in the database
        List<Conference> conferenceList = conferenceRepository.findAll();
        assertThat(conferenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteConference() throws Exception {
        // Initialize the database
        conferenceService.save(conference);

        int databaseSizeBeforeDelete = conferenceRepository.findAll().size();

        // Delete the conference
        restConferenceMockMvc.perform(delete("/api/conferences/{id}", conference.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Conference> conferenceList = conferenceRepository.findAll();
        assertThat(conferenceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
