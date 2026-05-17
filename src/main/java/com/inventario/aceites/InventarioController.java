package com.inventario.aceites;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.*;

@Controller
public class InventarioController {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private InventarioRepository inventarioRepository;

    // =========================
    // INVENTARIO
    // =========================

    @GetMapping("/inventario")
    public String verInventario(Model model) {

        List<Producto> productos =
                productoRepository.findAll();

        for (Producto p : productos) {

            Inventario ultimo =
                    inventarioRepository
                            .findTopByProducto_IdOrderByFechaDesc(p.getId())
                            .orElse(null);

            if (ultimo != null) {

                p.setStockSistema(
                        ultimo.getStockSistema()
                );

            } else {

                p.setStockSistema(null);
            }
        }

        model.addAttribute(
                "productos",
                productos
        );

        return "inventario";
    }

    // =========================
    // GUARDAR
    // =========================

    @PostMapping("/guardar")
    public String guardar(@RequestParam List<Integer> id,
                          @RequestParam(required = false) List<Double> stockSistema,
                          @RequestParam(required = false) List<Double> stockFisico,
                          Principal principal) {

        boolean esAdmin =
                principal.getName().equalsIgnoreCase("Alejandro");

        for (int i = 0; i < id.size(); i++) {

            Producto p = productoRepository.findById(id.get(i)).orElse(null);

            if (p == null) continue;

            // ==========================================
            // ADMIN
            // ==========================================
            if (esAdmin) {

                Inventario inv = inventarioRepository
                        .findTopByProducto_IdOrderByFechaDesc(p.getId())
                        .orElse(null);

                if (inv == null) {

                    inv = new Inventario();
                    inv.setProducto(p);
                    inv.setFecha(LocalDate.now());
                }

                if (stockSistema != null &&
                        stockSistema.size() > i &&
                        stockSistema.get(i) != null) {

                    inv.setStockSistema(stockSistema.get(i));
                }

                inventarioRepository.save(inv);
            }

            // ==========================================
            // OPERADOR
            // ==========================================
            else {

                Inventario inv = inventarioRepository
                        .findTopByProducto_IdAndStockFisicoIsNullOrderByFechaDesc(p.getId())
                        .orElse(null);

                // SI YA FUE LLENADO → NO MODIFICAR
                if (inv == null) {
                    continue;
                }

                if (stockFisico != null &&
                        stockFisico.size() > i &&
                        stockFisico.get(i) != null) {

                    inv.setStockFisico(stockFisico.get(i));

                    double diferencia =
                            inv.getStockFisico() - inv.getStockSistema();

                    inv.setDiferencia(diferencia);

                    if (diferencia > 0)
                        inv.setEstado("Sobra");
                    else if (diferencia < 0)
                        inv.setEstado("Falta");
                    else
                        inv.setEstado("Igual");

                    inventarioRepository.save(inv);
                }
            }
        }

        // ADMIN SE QUEDA EN INVENTARIO
        if (esAdmin) {
            return "redirect:/inventario";
        }

        // OPERADOR VA A REPORTE
        return "redirect:/reporte";
    }

    // =========================
    // ACTUALIZAR
    // =========================

    @PostMapping("/actualizar")
    public String actualizar(

            @RequestParam Integer id,

            @RequestParam Double stockSistema,

            @RequestParam Double stockFisico) {

        Inventario inv =
                inventarioRepository
                        .findById(id)
                        .orElse(null);

        if (inv != null) {

            inv.setStockSistema(
                    stockSistema
            );

            inv.setStockFisico(
                    stockFisico
            );

            double diferencia =
                    Math.round(
                            (stockFisico - stockSistema)
                                    * 100.0
                    ) / 100.0;

            inv.setDiferencia(
                    diferencia
            );

            if (stockFisico > stockSistema) {

                inv.setEstado(
                        "Sobra"
                );

            } else if (stockFisico < stockSistema) {

                inv.setEstado(
                        "Falta"
                );

            } else {

                inv.setEstado(
                        "Igual"
                );
            }

            inventarioRepository.save(
                    inv
            );
        }

        return "redirect:/reporte";
    }

    // =========================
    // REPORTE
    // =========================

    @GetMapping("/reporte")
    public String reporte(
            @RequestParam(required = false)
            String fecha,

            Model model) {

        List<Inventario> inventarios;

        if (fecha != null &&
                !fecha.isEmpty()) {

            LocalDate f =
                    LocalDate.parse(fecha);

            inventarios =
                    inventarioRepository
                            .findByFecha(f);

        } else {

            inventarios =
                    inventarioRepository
                            .findAll();
        }

        Collections.reverse(
                inventarios
        );

        model.addAttribute(
                "inventarios",
                inventarios
        );

        return "reporte";
    }
}