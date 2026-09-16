package com.practica;

import com.practica.EntropyCalculator.NodoDecision;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.router.Route;
import java.util.LinkedHashMap;
import java.util.Map;

@Route("")
public class MainView extends VerticalLayout {

    public MainView() {
        
    EntropyCalculator calc;
    try {
        calc = new EntropyCalculator("src/main/resources/peliculaR.arff");
    } catch (Exception e) {
        add(new Paragraph("Error cargando el dataset: " + e.getMessage()));
        return;
    }
                
        setPadding(true);
        setSpacing(true);

        add(new H1("Entropía y Ganancia de Información — peliculaR.arff"));

        // entropia total
        add(new H2("1. Entropía del conjunto total"));
        double entropiaTotal = calc.calcularEntropiaTotal();

        Paragraph infoTotal = new Paragraph(
            "Dataset: " + calc.getTotalInstancias() + " películas | "
            + "popular = yes: 10 | popular = no: 20"
        );
        Paragraph formulaTotal = new Paragraph(
            "H(S) = −(10/30)·log₂(10/30) − (20/30)·log₂(20/30)"
        );
        formulaTotal.getStyle().set("font-family", "monospace").set("background", "#f0f0f0").set("padding", "8px");

        H3 resultadoTotal = new H3(String.format("H(S) = %.4f bits", entropiaTotal));

        add(infoTotal, formulaTotal, resultadoTotal);

        // ganancia de información director_popularity
        add(new H2("2. Ganancia de información — director_popularity"));

        Map<String, double[]> detalle1 = calc.detalleAtributo(1); // índice 1 = director_popularity
        add(crearTablaDetalle(detalle1));

        double ganancia1 = calc.calcularGanancia(1);
        H3 resultadoGanancia1 = new H3(String.format("GI(S, director_popularity) = %.4f bits", ganancia1));
        add(resultadoGanancia1);

        // entropia 0
        add(new H2("3. ¿Qué significa entropía = 0?"));
        Paragraph explicacion = new Paragraph(
            "Significa que el subconjunto es completamente puro: todas sus instancias tienen la misma clase. "
            + "No hay incertidumbre. Ejemplo: todas las películas con director_popularity = low "
            + "tienen popular = no (10 de 10). Entropía = 0.0000."
        );
        add(explicacion);

        // otro atributo (budget)
        add(new H2("4. Ganancia de información — budget"));

        Map<String, double[]> detalle2 = calc.detalleAtributo(2); // índice 2 = budget
        add(crearTablaDetalle(detalle2));

        double ganancia2 = calc.calcularGanancia(2);
        H3 resultadoGanancia2 = new H3(String.format("GI(S, budget) = %.4f bits", ganancia2));
        add(resultadoGanancia2);
        
        // seccion recomendacion recusrsiva
        add(new H2("5. Recomendación por árbol de decisión (recursivo)"));

        NodoDecision arbol = calc.construirArbol();

        // pelicula de prueba
        Map<String, String> pelicula = new LinkedHashMap<>();
        pelicula.put("director_popularity", "high");
        pelicula.put("genre", "Action");
        pelicula.put("budget", "high");

        String resultado = calc.recomendar(arbol, pelicula);

        add(new Paragraph("Película de prueba: director_popularity=high, genre=Action, budget=high"));
        add(new H3("¿Recomendar? → " + resultado.toUpperCase()));
    }

    private Grid<String> crearTablaDetalle(Map<String, double[]> detalle) {
        Grid<String> grid = new Grid<>();
        grid.setItems(detalle.keySet());
        grid.addColumn(v -> v).setHeader("Valor");
        grid.addColumn(v -> (int) detalle.get(v)[0]).setHeader("yes");
        grid.addColumn(v -> (int) detalle.get(v)[1]).setHeader("no");
        grid.addColumn(v -> (int) detalle.get(v)[2]).setHeader("Total");
        grid.addColumn(v -> String.format("%.4f", detalle.get(v)[3])).setHeader("H(Sv)");
        grid.setHeight("200px");
        return grid;
    }
}