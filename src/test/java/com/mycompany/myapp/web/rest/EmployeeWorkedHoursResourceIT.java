package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.EmployeeWorkedHours;
import com.mycompany.myapp.repository.EmployeeWorkedHoursRepository;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link EmployeeWorkedHoursResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EmployeeWorkedHoursResourceIT {

    private static final Integer DEFAULT_WORKED_HOURS = 1;
    private static final Integer UPDATED_WORKED_HOURS = 2;

    private static final LocalDate DEFAULT_WORKED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_WORKED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/employee-worked-hours";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EmployeeWorkedHoursRepository employeeWorkedHoursRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmployeeWorkedHoursMockMvc;

    private EmployeeWorkedHours employeeWorkedHours;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmployeeWorkedHours createEntity(EntityManager em) {
        EmployeeWorkedHours employeeWorkedHours = new EmployeeWorkedHours()
            .workedHours(DEFAULT_WORKED_HOURS)
            .workedDate(DEFAULT_WORKED_DATE);
        return employeeWorkedHours;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmployeeWorkedHours createUpdatedEntity(EntityManager em) {
        EmployeeWorkedHours employeeWorkedHours = new EmployeeWorkedHours()
            .workedHours(UPDATED_WORKED_HOURS)
            .workedDate(UPDATED_WORKED_DATE);
        return employeeWorkedHours;
    }

    @BeforeEach
    public void initTest() {
        employeeWorkedHours = createEntity(em);
    }

    @Test
    @Transactional
    void createEmployeeWorkedHours() throws Exception {
        int databaseSizeBeforeCreate = employeeWorkedHoursRepository.findAll().size();
        // Create the EmployeeWorkedHours
        restEmployeeWorkedHoursMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeeWorkedHours))
            )
            .andExpect(status().isCreated());

        // Validate the EmployeeWorkedHours in the database
        List<EmployeeWorkedHours> employeeWorkedHoursList = employeeWorkedHoursRepository.findAll();
        assertThat(employeeWorkedHoursList).hasSize(databaseSizeBeforeCreate + 1);
        EmployeeWorkedHours testEmployeeWorkedHours = employeeWorkedHoursList.get(employeeWorkedHoursList.size() - 1);
        assertThat(testEmployeeWorkedHours.getWorkedHours()).isEqualTo(DEFAULT_WORKED_HOURS);
        assertThat(testEmployeeWorkedHours.getWorkedDate()).isEqualTo(DEFAULT_WORKED_DATE);
    }

    @Test
    @Transactional
    void createEmployeeWorkedHoursWithExistingId() throws Exception {
        // Create the EmployeeWorkedHours with an existing ID
        employeeWorkedHours.setId(1L);

        int databaseSizeBeforeCreate = employeeWorkedHoursRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmployeeWorkedHoursMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeeWorkedHours))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeWorkedHours in the database
        List<EmployeeWorkedHours> employeeWorkedHoursList = employeeWorkedHoursRepository.findAll();
        assertThat(employeeWorkedHoursList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEmployeeWorkedHours() throws Exception {
        // Initialize the database
        employeeWorkedHoursRepository.saveAndFlush(employeeWorkedHours);

        // Get all the employeeWorkedHoursList
        restEmployeeWorkedHoursMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employeeWorkedHours.getId().intValue())))
            .andExpect(jsonPath("$.[*].workedHours").value(hasItem(DEFAULT_WORKED_HOURS)))
            .andExpect(jsonPath("$.[*].workedDate").value(hasItem(DEFAULT_WORKED_DATE.toString())));
    }

    @Test
    @Transactional
    void getEmployeeWorkedHours() throws Exception {
        // Initialize the database
        employeeWorkedHoursRepository.saveAndFlush(employeeWorkedHours);

        // Get the employeeWorkedHours
        restEmployeeWorkedHoursMockMvc
            .perform(get(ENTITY_API_URL_ID, employeeWorkedHours.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(employeeWorkedHours.getId().intValue()))
            .andExpect(jsonPath("$.workedHours").value(DEFAULT_WORKED_HOURS))
            .andExpect(jsonPath("$.workedDate").value(DEFAULT_WORKED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingEmployeeWorkedHours() throws Exception {
        // Get the employeeWorkedHours
        restEmployeeWorkedHoursMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEmployeeWorkedHours() throws Exception {
        // Initialize the database
        employeeWorkedHoursRepository.saveAndFlush(employeeWorkedHours);

        int databaseSizeBeforeUpdate = employeeWorkedHoursRepository.findAll().size();

        // Update the employeeWorkedHours
        EmployeeWorkedHours updatedEmployeeWorkedHours = employeeWorkedHoursRepository.findById(employeeWorkedHours.getId()).get();
        // Disconnect from session so that the updates on updatedEmployeeWorkedHours are not directly saved in db
        em.detach(updatedEmployeeWorkedHours);
        updatedEmployeeWorkedHours.workedHours(UPDATED_WORKED_HOURS).workedDate(UPDATED_WORKED_DATE);

        restEmployeeWorkedHoursMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEmployeeWorkedHours.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEmployeeWorkedHours))
            )
            .andExpect(status().isOk());

        // Validate the EmployeeWorkedHours in the database
        List<EmployeeWorkedHours> employeeWorkedHoursList = employeeWorkedHoursRepository.findAll();
        assertThat(employeeWorkedHoursList).hasSize(databaseSizeBeforeUpdate);
        EmployeeWorkedHours testEmployeeWorkedHours = employeeWorkedHoursList.get(employeeWorkedHoursList.size() - 1);
        assertThat(testEmployeeWorkedHours.getWorkedHours()).isEqualTo(UPDATED_WORKED_HOURS);
        assertThat(testEmployeeWorkedHours.getWorkedDate()).isEqualTo(UPDATED_WORKED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingEmployeeWorkedHours() throws Exception {
        int databaseSizeBeforeUpdate = employeeWorkedHoursRepository.findAll().size();
        employeeWorkedHours.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeWorkedHoursMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employeeWorkedHours.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeeWorkedHours))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeWorkedHours in the database
        List<EmployeeWorkedHours> employeeWorkedHoursList = employeeWorkedHoursRepository.findAll();
        assertThat(employeeWorkedHoursList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmployeeWorkedHours() throws Exception {
        int databaseSizeBeforeUpdate = employeeWorkedHoursRepository.findAll().size();
        employeeWorkedHours.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeWorkedHoursMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeeWorkedHours))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeWorkedHours in the database
        List<EmployeeWorkedHours> employeeWorkedHoursList = employeeWorkedHoursRepository.findAll();
        assertThat(employeeWorkedHoursList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmployeeWorkedHours() throws Exception {
        int databaseSizeBeforeUpdate = employeeWorkedHoursRepository.findAll().size();
        employeeWorkedHours.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeWorkedHoursMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeeWorkedHours))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmployeeWorkedHours in the database
        List<EmployeeWorkedHours> employeeWorkedHoursList = employeeWorkedHoursRepository.findAll();
        assertThat(employeeWorkedHoursList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmployeeWorkedHoursWithPatch() throws Exception {
        // Initialize the database
        employeeWorkedHoursRepository.saveAndFlush(employeeWorkedHours);

        int databaseSizeBeforeUpdate = employeeWorkedHoursRepository.findAll().size();

        // Update the employeeWorkedHours using partial update
        EmployeeWorkedHours partialUpdatedEmployeeWorkedHours = new EmployeeWorkedHours();
        partialUpdatedEmployeeWorkedHours.setId(employeeWorkedHours.getId());

        partialUpdatedEmployeeWorkedHours.workedHours(UPDATED_WORKED_HOURS);

        restEmployeeWorkedHoursMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployeeWorkedHours.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmployeeWorkedHours))
            )
            .andExpect(status().isOk());

        // Validate the EmployeeWorkedHours in the database
        List<EmployeeWorkedHours> employeeWorkedHoursList = employeeWorkedHoursRepository.findAll();
        assertThat(employeeWorkedHoursList).hasSize(databaseSizeBeforeUpdate);
        EmployeeWorkedHours testEmployeeWorkedHours = employeeWorkedHoursList.get(employeeWorkedHoursList.size() - 1);
        assertThat(testEmployeeWorkedHours.getWorkedHours()).isEqualTo(UPDATED_WORKED_HOURS);
        assertThat(testEmployeeWorkedHours.getWorkedDate()).isEqualTo(DEFAULT_WORKED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateEmployeeWorkedHoursWithPatch() throws Exception {
        // Initialize the database
        employeeWorkedHoursRepository.saveAndFlush(employeeWorkedHours);

        int databaseSizeBeforeUpdate = employeeWorkedHoursRepository.findAll().size();

        // Update the employeeWorkedHours using partial update
        EmployeeWorkedHours partialUpdatedEmployeeWorkedHours = new EmployeeWorkedHours();
        partialUpdatedEmployeeWorkedHours.setId(employeeWorkedHours.getId());

        partialUpdatedEmployeeWorkedHours.workedHours(UPDATED_WORKED_HOURS).workedDate(UPDATED_WORKED_DATE);

        restEmployeeWorkedHoursMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployeeWorkedHours.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmployeeWorkedHours))
            )
            .andExpect(status().isOk());

        // Validate the EmployeeWorkedHours in the database
        List<EmployeeWorkedHours> employeeWorkedHoursList = employeeWorkedHoursRepository.findAll();
        assertThat(employeeWorkedHoursList).hasSize(databaseSizeBeforeUpdate);
        EmployeeWorkedHours testEmployeeWorkedHours = employeeWorkedHoursList.get(employeeWorkedHoursList.size() - 1);
        assertThat(testEmployeeWorkedHours.getWorkedHours()).isEqualTo(UPDATED_WORKED_HOURS);
        assertThat(testEmployeeWorkedHours.getWorkedDate()).isEqualTo(UPDATED_WORKED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingEmployeeWorkedHours() throws Exception {
        int databaseSizeBeforeUpdate = employeeWorkedHoursRepository.findAll().size();
        employeeWorkedHours.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeWorkedHoursMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, employeeWorkedHours.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employeeWorkedHours))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeWorkedHours in the database
        List<EmployeeWorkedHours> employeeWorkedHoursList = employeeWorkedHoursRepository.findAll();
        assertThat(employeeWorkedHoursList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmployeeWorkedHours() throws Exception {
        int databaseSizeBeforeUpdate = employeeWorkedHoursRepository.findAll().size();
        employeeWorkedHours.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeWorkedHoursMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employeeWorkedHours))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeWorkedHours in the database
        List<EmployeeWorkedHours> employeeWorkedHoursList = employeeWorkedHoursRepository.findAll();
        assertThat(employeeWorkedHoursList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmployeeWorkedHours() throws Exception {
        int databaseSizeBeforeUpdate = employeeWorkedHoursRepository.findAll().size();
        employeeWorkedHours.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeWorkedHoursMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employeeWorkedHours))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmployeeWorkedHours in the database
        List<EmployeeWorkedHours> employeeWorkedHoursList = employeeWorkedHoursRepository.findAll();
        assertThat(employeeWorkedHoursList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmployeeWorkedHours() throws Exception {
        // Initialize the database
        employeeWorkedHoursRepository.saveAndFlush(employeeWorkedHours);

        int databaseSizeBeforeDelete = employeeWorkedHoursRepository.findAll().size();

        // Delete the employeeWorkedHours
        restEmployeeWorkedHoursMockMvc
            .perform(delete(ENTITY_API_URL_ID, employeeWorkedHours.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EmployeeWorkedHours> employeeWorkedHoursList = employeeWorkedHoursRepository.findAll();
        assertThat(employeeWorkedHoursList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
