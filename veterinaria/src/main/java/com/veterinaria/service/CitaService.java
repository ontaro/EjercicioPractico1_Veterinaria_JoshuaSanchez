/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.veterinaria.service;

import com.veterinaria.domain.Cita;
import com.veterinaria.domain.Servicio;
import com.veterinaria.repository.CitaRepository;
import com.veterinaria.repository.ServicioRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author os990
 */
@Service
@RequiredArgsConstructor
public class CitaService {

    private final CitaRepository citaRepository;
    private final ServicioRepository servicioRepository;

    @Transactional(readOnly = true)
    public List<Cita> getCitas() {
        return citaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Cita> getCita(Integer id) {
        return citaRepository.findById(id);
    }

    @Transactional
    public void save(Cita cita) {
        Integer idServicio = cita.getServicio().getId();

        Servicio servicio = servicioRepository.findById(idServicio)
                .orElseThrow(() -> new IllegalArgumentException(
                        "El servicio seleccionado no existe."));

        cita.setServicio(servicio);
        citaRepository.save(cita);
    }

    @Transactional
    public void delete(Integer id) {
        if (!citaRepository.existsById(id)) {
            throw new IllegalArgumentException(
                    "No se encontró la cita con ID: " + id);
        }

        citaRepository.deleteById(id);
    }

}
