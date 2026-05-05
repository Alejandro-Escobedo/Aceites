package com.inventario.aceites;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@Controller
public class InventarioController {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private InventarioRepository inventarioRepository;

    // 🔥 MOSTRAR INVENTARIO CON STOCK SISTEMA
    @GetMapping("/inventario")
    public String verInventario(Model model) {

        List<Producto> productos = productoRepository.findAll();

        for (Producto p : productos) {

            Inventario ultimo = inventarioRepository
                    .findTopByProducto_IdAndStockFisicoIsNullOrderByFechaDesc(p.getId())
                    .orElse(null);

            if (ultimo != null) {
                p.setStockSistema(ultimo.getStockSistema());
            } else {
                p.setStockSistema(0.0);
            }
        }

        model.addAttribute("productos", productos);
        return "inventario";
    }

    // 🔥 GUARDAR INVENTARIO
    @PostMapping("/guardar")
    public String guardar(@RequestParam List<Integer> id,
                          @RequestParam List<Double> stockFisico) {

        for (int i = 0; i < id.size(); i++) {

            Producto p = productoRepository.findById(id.get(i)).orElse(null);

            if (p != null) {

                Inventario ultimo = inventarioRepository
                        .findTopByProducto_IdAndStockFisicoIsNullOrderByFechaDesc(p.getId())
                        .orElse(null);

                double sistema = (ultimo != null) ? ultimo.getStockSistema() : 0;
                double fisico = stockFisico.get(i);
                double diferencia = Math.round((fisico - sistema) * 100.0) / 100.0;

                String estado;
                if (fisico > sistema) estado = "Sobra";
                else if (fisico < sistema) estado = "Falta";
                else estado = "Igual";

                Inventario inv = inventarioRepository
                        .findTopByProducto_IdAndStockFisicoIsNullOrderByFechaDesc(p.getId())
                        .orElse(null);

                if (inv != null) {
                    // 👉 actualizar el registro existente
                    inv.setStockFisico(fisico);
                    inv.setDiferencia(diferencia);
                    inv.setEstado(estado);

                    inventarioRepository.save(inv);
                }
            }
        }

        return "redirect:/reporte";
    }

    // 🔥 REPORTE SOLO ÚLTIMO POR PRODUCTO
    @GetMapping("/reporte")
    public String reporte(Model model) {

        List<Inventario> todos = inventarioRepository.findAll();

        List<Inventario> ultimos = new ArrayList<>();
        Set<Integer> usados = new HashSet<>();

        Collections.reverse(todos);

        for (Inventario inv : todos) {
            Integer idProducto = inv.getProducto().getId();
            if (!usados.contains(idProducto)) {
                ultimos.add(inv);
                usados.add(idProducto);
            }
        }

        model.addAttribute("inventarios", ultimos);

        return "reporte";
    }
}