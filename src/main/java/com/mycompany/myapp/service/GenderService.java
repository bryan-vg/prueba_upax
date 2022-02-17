package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Gender;
import com.mycompany.myapp.repository.GenderRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Gender}.
 */
@Service
@Transactional
public class GenderService {

    private final Logger log = LoggerFactory.getLogger(GenderService.class);

    private final GenderRepository genderRepository;

    public GenderService(GenderRepository genderRepository) {
        this.genderRepository = genderRepository;
    }

    /**
     * Save a gender.
     *
     * @param gender the entity to save.
     * @return the persisted entity.
     */
    public Gender save(Gender gender) {
        log.debug("Request to save Gender : {}", gender);
        return genderRepository.save(gender);
    }

    /**
     * Partially update a gender.
     *
     * @param gender the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Gender> partialUpdate(Gender gender) {
        log.debug("Request to partially update Gender : {}", gender);

        return genderRepository
            .findById(gender.getId())
            .map(existingGender -> {
                if (gender.getName() != null) {
                    existingGender.setName(gender.getName());
                }

                return existingGender;
            })
            .map(genderRepository::save);
    }

    /**
     * Get all the genders.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Gender> findAll() {
        log.debug("Request to get all Genders");
        return genderRepository.findAll();
    }

    /**
     * Get one gender by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Gender> findOne(Long id) {
        log.debug("Request to get Gender : {}", id);
        return genderRepository.findById(id);
    }

    /**
     * Delete the gender by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Gender : {}", id);
        genderRepository.deleteById(id);
    }
}
