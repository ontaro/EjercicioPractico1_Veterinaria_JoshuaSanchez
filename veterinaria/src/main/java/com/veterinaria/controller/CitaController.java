/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.veterinaria.controller;

import com.veterinaria.domain.Cita;
import com.veterinaria.domain.Servicio;
import com.veterinaria.repository.ServicioRepository;
import com.veterinaria.service.CitaService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author os990
 */

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/citas")
public class CitaController {

    private final CitaService citaService;
    private final ServicioRepository servicioRepository;

    @GetMapping
    public String listado(Model model) {
        model.addAttribute("citas", citaService.getCitas());
        return "citas/listado";
    }

    @GetMapping("/nuevo")
    public String nueva(Model model) {
        model.addAttribute("cita", new Cita());
        cargarServicios(model);
        return "citas/formulario";
    }

    @GetMapping("/editar/{id}")
    public String editar(
            @PathVariable Integer id,
            Model model,
            RedirectAttributes redirectAttributes) {

        return citaService.getCita(id).map(cita -> {
            model.addAttribute("cita", cita);
            cargarServicios(model);
            return "citas/formulario";
        }).orElseGet(() -> {
            redirectAttributes.addFlashAttribute(
                    "error", "No se encontró la cita.");
            return "redirect:/citas";
        });
    }

    @PostMapping("/guardar")
    public String guardar(
            @ModelAttribute("cita") Cita cita,
            RedirectAttributes redirectAttributes) {

        try {
            citaService.save(cita);
            redirectAttributes.addFlashAttribute(
                    "todoOk", "La cita se guardó correctamente.");
        } catch (Exception e) {
            log.error("Error al guardar la cita", e);
            redirectAttributes.addFlashAttribute(
                    "error", "No se pudo guardar la cita. Verifique los datos.");
        }

        return "redirect:/citas";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(
            @PathVariable Integer id,
            RedirectAttributes redirectAttributes) {

        try {
            Cita cita = citaService.getCita(id)
                    .orElseThrow(() ->
                            new IllegalArgumentException("Cita no encontrada."));

            citaService.delete(id);

            redirectAttributes.addFlashAttribute(
                    "todoOk", "La cita se eliminó correctamente.");

        } catch (Exception e) {
            log.error("Error al eliminar la cita {}", id, e);
            redirectAttributes.addFlashAttribute(
                    "error", "No se pudo eliminar la cita.");
        }

        return "redirect:/citas";
    }

    private void cargarServicios(Model model) {
        List<Servicio> servicios = servicioRepository.findAll();
        model.addAttribute("servicios", servicios);
    }
}