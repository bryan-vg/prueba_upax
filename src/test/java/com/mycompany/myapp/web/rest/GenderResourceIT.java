package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Gender;
import com.mycompany.myapp.repository.GenderRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link GenderResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GenderResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/genders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GenderRepository genderRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGenderMockMvc;

    private Gender gender;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Gender createEntity(EntityManager em) {
        Gender gender = new Gender().name(DEFAULT_NAME);
        return gender;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Gender createUpdatedEntity(EntityManager em) {
        Gender gender = new Gender().name(UPDATED_NAME);
        return gender;
    }

    @BeforeEach
    public void initTest() {
        gender = createEntity(em);
    }

    @Test
    @Transactional
    void createGender() throws Exception {
        int databaseSizeBeforeCreate = genderRepository.findAll().size();
        // Create the Gender
        restGenderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gender)))
            .andExpect(status().isCreated());

        // Validate the Gender in the database
        List<Gender> genderList = genderRepository.findAll();
        assertThat(genderList).hasSize(databaseSizeBeforeCreate + 1);
        Gender testGender = genderList.get(genderList.size() - 1);
        assertThat(testGender.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createGenderWithExistingId() throws Exception {
        // Create the Gender with an existing ID
        gender.setId(1L);

        int databaseSizeBeforeCreate = genderRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGenderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gender)))
            .andExpect(status().isBadRequest());

        // Validate the Gender in the database
        List<Gender> genderList = genderRepository.findAll();
        assertThat(genderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllGenders() throws Exception {
        // Initialize the database
        genderRepository.saveAndFlush(gender);

        // Get all the genderList
        restGenderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gender.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getGender() throws Exception {
        // Initialize the database
        genderRepository.saveAndFlush(gender);

        // Get the gender
        restGenderMockMvc
            .perform(get(ENTITY_API_URL_ID, gender.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(gender.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingGender() throws Exception {
        // Get the gender
        restGenderMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewGender() throws Exception {
        // Initialize the database
        genderRepository.saveAndFlush(gender);

        int databaseSizeBeforeUpdate = genderRepository.findAll().size();

        // Update the gender
        Gender updatedGender = genderRepository.findById(gender.getId()).get();
        // Disconnect from session so that the updates on updatedGender are not directly saved in db
        em.detach(updatedGender);
        updatedGender.name(UPDATED_NAME);

        restGenderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedGender.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedGender))
            )
            .andExpect(status().isOk());

        // Validate the Gender in the database
        List<Gender> genderList = genderRepository.findAll();
        assertThat(genderList).hasSize(databaseSizeBeforeUpdate);
        Gender testGender = genderList.get(genderList.size() - 1);
        assertThat(testGender.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingGender() throws Exception {
        int databaseSizeBeforeUpdate = genderRepository.findAll().size();
        gender.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGenderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, gender.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gender))
            )
            .andExpect(status().isBadRequest());

        // Validate the Gender in the database
        List<Gender> genderList = genderRepository.findAll();
        assertThat(genderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGender() throws Exception {
        int databaseSizeBeforeUpdate = genderRepository.findAll().size();
        gender.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGenderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gender))
            )
            .andExpect(status().isBadRequest());

        // Validate the Gender in the database
        List<Gender> genderList = genderRepository.findAll();
        assertThat(genderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGender() throws Exception {
        int databaseSizeBeforeUpdate = genderRepository.findAll().size();
        gender.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGenderMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gender)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Gender in the database
        List<Gender> genderList = genderRepository.findAll();
        assertThat(genderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGenderWithPatch() throws Exception {
        // Initialize the database
        genderRepository.saveAndFlush(gender);

        int databaseSizeBeforeUpdate = genderRepository.findAll().size();

        // Update the gender using partial update
        Gender partialUpdatedGender = new Gender();
        partialUpdatedGender.setId(gender.getId());

        partialUpdatedGender.name(UPDATED_NAME);

        restGenderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGender.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGender))
            )
            .andExpect(status().isOk());

        // Validate the Gender in the database
        List<Gender> genderList = genderRepository.findAll();
        assertThat(genderList).hasSize(databaseSizeBeforeUpdate);
        Gender testGender = genderList.get(genderList.size() - 1);
        assertThat(testGender.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateGenderWithPatch() throws Exception {
        // Initialize the database
        genderRepository.saveAndFlush(gender);

        int databaseSizeBeforeUpdate = genderRepository.findAll().size();

        // Update the gender using partial update
        Gender partialUpdatedGender = new Gender();
        partialUpdatedGender.setId(gender.getId());

        partialUpdatedGender.name(UPDATED_NAME);

        restGenderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGender.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGender))
            )
            .andExpect(status().isOk());

        // Validate the Gender in the database
        List<Gender> genderList = genderRepository.findAll();
        assertThat(genderList).hasSize(databaseSizeBeforeUpdate);
        Gender testGender = genderList.get(genderList.size() - 1);
        assertThat(testGender.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingGender() throws Exception {
        int databaseSizeBeforeUpdate = genderRepository.findAll().size();
        gender.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGenderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, gender.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gender))
            )
            .andExpect(status().isBadRequest());

        // Validate the Gender in the database
        List<Gender> genderList = genderRepository.findAll();
        assertThat(genderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGender() throws Exception {
        int databaseSizeBeforeUpdate = genderRepository.findAll().size();
        gender.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGenderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gender))
            )
            .andExpect(status().isBadRequest());

        // Validate the Gender in the database
        List<Gender> genderList = genderRepository.findAll();
        assertThat(genderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGender() throws Exception {
        int databaseSizeBeforeUpdate = genderRepository.findAll().size();
        gender.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGenderMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(gender)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Gender in the database
        List<Gender> genderList = genderRepository.findAll();
        assertThat(genderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGender() throws Exception {
        // Initialize the database
        genderRepository.saveAndFlush(gender);

        int databaseSizeBeforeDelete = genderRepository.findAll().size();

        // Delete the gender
        restGenderMockMvc
            .perform(delete(ENTITY_API_URL_ID, gender.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Gender> genderList = genderRepository.findAll();
        assertThat(genderList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
