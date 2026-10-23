/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.veterinaria.service;

import com.veterinaria.domain.Servicio;
import com.veterinaria.domain.TipoServicio;
import com.veterinaria.repository.ServicioRepository;
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
public class ServicioService {

    private final ServicioRepository servicioRepository;
    private final TipoServicioRepository tipoServicioRepository;

    @Transactional(readOnly = true)
    public List<Servicio> getServicios() {
        return servicioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Servicio> getServicio(Integer id) {
        return servicioRepository.findById(id);
    }

    @Transactional
    public void save(Servicio servicio) {
        Integer idTipo = servicio.getTipoServicio().getId();

        TipoServicio tipoServicio = tipoServicioRepository
                .findById(idTipo)
                .orElseThrow(() -> new IllegalArgumentException(
                        "El tipo de servicio con ID " + idTipo + " no existe."
                ));

        servicio.setTipoServicio(tipoServicio);
        servicioRepository.save(servicio);
    }

    @Transactional
    public void delete(Integer id) {
        if (!servicioRepository.existsById(id)) {
            throw new IllegalArgumentException(
                    "El servicio con ID " + id + " no existe."
            );
        }

        try {
            servicioRepository.deleteById(id);
            servicioRepository.flush();

        } catch (DataIntegrityViolationException e) {
            log.warn("No se puede eliminar el servicio {}", id, e);

            throw new IllegalStateException(
                    "No se puede eliminar el servicio porque tiene citas asociadas.",
                    e
            );
        }
    }
}
