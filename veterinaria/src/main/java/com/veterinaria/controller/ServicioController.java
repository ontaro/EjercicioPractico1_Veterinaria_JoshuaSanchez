/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.veterinaria.controller;

import com.veterinaria.domain.Servicio;
import com.veterinaria.domain.TipoServicio;
import com.veterinaria.repository.TipoServicioRepository;
import com.veterinaria.service.ServicioService;
import java.util.List;
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
@RequestMapping("/servicios")
public class ServicioController {

    private final ServicioService servicioService;
    private final TipoServicioRepository tipoServicioRepository;

    @GetMapping
    public String listado(Model model) {
        model.addAttribute("servicios", servicioService.getServicios());
        return "servicios/listado";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("servicio", new Servicio());
        cargarTiposServicio(model);
        return "servicios/formulario";
    }

    @GetMapping("/editar/{id}")
    public String editar(
            @PathVariable Integer id,
            Model model,
            RedirectAttributes redirectAttributes) {

        return servicioService.getServicio(id).map(servicio -> {
            model.addAttribute("servicio", servicio);
            cargarTiposServicio(model);
            return "servicios/formulario";
        }).orElseGet(() -> {
            redirectAttributes.addFlashAttribute(
                    "error", "No se encontró el servicio.");
            return "redirect:/servicios";
        });
    }

    @PostMapping("/guardar")
    public String guardar(
            @ModelAttribute("servicio") Servicio servicio,
            RedirectAttributes redirectAttributes) {

        try {
            servicioService.save(servicio);
            redirectAttributes.addFlashAttribute(
                    "todoOk", "El servicio se guardó correctamente.");
        } catch (Exception e) {
            log.error("Error al guardar el servicio", e);
            redirectAttributes.addFlashAttribute(
                    "error", "No se pudo guardar el servicio. Verifique los datos.");
        }

        return "redirect:/servicios";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(
            @PathVariable Integer id,
            RedirectAttributes redirectAttributes) {

        try {
            servicioService.delete(id);
            redirectAttributes.addFlashAttribute(
                    "todoOk", "El servicio se eliminó correctamente.");
        } catch (DataIntegrityViolationException e) {
            log.error("No se pudo eliminar el servicio {}", id, e);
            redirectAttributes.addFlashAttribute(
                    "error", "No se puede eliminar el servicio porque tiene citas asociadas.");
        } catch (Exception e) {
            log.error("Error al eliminar el servicio {}", id, e);
            redirectAttributes.addFlashAttribute(
                    "error", "No se pudo eliminar el servicio.");
        }

        return "redirect:/servicios";
    }

    private void cargarTiposServicio(Model model) {
        List<TipoServicio> tipos = tipoServicioRepository.findAll();
        model.addAttribute("tiposServicio", tipos);
    }
}