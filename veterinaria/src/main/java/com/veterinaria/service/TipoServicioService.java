/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.veterinaria.service;

import com.veterinaria.domain.TipoServicio;
import com.veterinaria.repository.TipoServicioRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author os990
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class TipoServicioService {

    private final TipoServicioRepository tipoServicioRepository;

    @Transactional(readOnly = true)
    public List<TipoServicio> getTiposServicio() {
        return tipoServicioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<TipoServicio> getTipoServicio(Integer id) {
        return tipoServicioRepository.findById(id);
    }

    @Transactional
    public void save(TipoServicio tipoServicio) {
        tipoServicioRepository.save(tipoServicio);
    }

    @Transactional
    public void delete(Integer id) {
        if (!tipoServicioRepository.existsById(id)) {
            throw new IllegalArgumentException(
                    "El tipo de servicio con ID " + id + " no existe."
            );
        }

        try {
            tipoServicioRepository.deleteById(id);
            tipoServicioRepository.flush();

        } catch (DataIntegrityViolationException e) {
            log.warn("No se puede eliminar el tipo de servicio {}", id, e);

            throw new IllegalStateException(
                    "No se puede eliminar el tipo de servicio porque tiene servicios asociados.",
                    e
            );
        }
    }
}