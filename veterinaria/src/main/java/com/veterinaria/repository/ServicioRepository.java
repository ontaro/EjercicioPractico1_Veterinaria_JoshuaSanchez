/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.veterinaria.repository;

import com.veterinaria.domain.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author os990
 */
public interface ServicioRepository extends JpaRepository<Servicio, Integer> {
    
}
