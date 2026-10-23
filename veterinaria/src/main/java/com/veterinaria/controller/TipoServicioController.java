/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.veterinaria.controller;

import com.veterinaria.domain.TipoServicio;
import com.veterinaria.service.TipoServicioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
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
@RequestMapping("/tiposervicio")
public class TipoServicioController {

    private final TipoServicioService tipoServicioService;

    @GetMapping
    public String listado(Model model) {
        model.addAttribute("tipos", tipoServicioService.getTiposServicio());
        return "tiposervicio/listado";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("tipoServicio", new TipoServicio());
        return "tiposervicio/formulario";
    }

    @GetMapping("/editar/{id}")
    public String editar(
            @PathVariable Integer id,
            Model model,
            RedirectAttributes redirectAttributes) {

        return tipoServicioService.getTipoServicio(id)
                .map(tipo -> {
                    model.addAttribute("tipoServicio", tipo);
                    return "tiposervicio/formulario";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute(
                            "error", "No se encontró el tipo de servicio.");
                    return "redirect:/tiposervicio";
                });
    }

    @PostMapping("/guardar")
    public String guardar(
            @ModelAttribute("tipoServicio") TipoServicio tipoServicio,
            RedirectAttributes redirectAttributes) {

        try {
            tipoServicioService.save(tipoServicio);

            redirectAttributes.addFlashAttribute(
                    "todoOk", "El tipo de servicio se guardó correctamente.");

        } catch (Exception e) {
            log.error("Error al guardar el tipo de servicio", e);

            redirectAttributes.addFlashAttribute(
                    "error", "No se pudo guardar el tipo de servicio.");
        }

        return "redirect:/tiposervicio";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(
            @PathVariable Integer id,
            RedirectAttributes redirectAttributes) {

        try {
            tipoServicioService.delete(id);

            redirectAttributes.addFlashAttribute(
                    "todoOk", "El tipo de servicio se eliminó correctamente.");

        } catch (IllegalStateException | DataIntegrityViolationException e) {
            log.error("No se pudo eliminar el tipo de servicio {}", id, e);

            redirectAttributes.addFlashAttribute(
                    "error",
                    "No se puede eliminar este tipo porque tiene servicios asociados.");
        }

        return "redirect:/tiposervicio";
    }
}