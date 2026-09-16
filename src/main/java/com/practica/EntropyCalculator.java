package com.practica;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import java.util.*;

public class EntropyCalculator {

    private Instances data;
    private int classIndex;

    public EntropyCalculator(String arffPath) throws Exception {
        System.out.println("Buscando archivo en: " + arffPath);
        DataSource source = new DataSource(arffPath);
        data = source.getDataSet();
        if (data == null) throw new Exception("El archivo se encontró pero Weka no pudo leerlo");
        data.setClassIndex(data.numAttributes() - 1);
        classIndex = data.classIndex();
    }

    public double calcularEntropiaTotal() {
        Map<String, Integer> conteo = new HashMap<>();
        for (int i = 0; i < data.numInstances(); i++) {
            String clase = data.instance(i).stringValue(classIndex);
            conteo.merge(clase, 1, Integer::sum);
        }
        return calcularEntropia(conteo, data.numInstances());
    }

    public double calcularGanancia(int atributoIndex) {
        double entropiaTotal = calcularEntropiaTotal();
        Map<String, List<String>> grupos = new LinkedHashMap<>();

        for (int i = 0; i < data.numInstances(); i++) {
            String val = data.instance(i).stringValue(atributoIndex);
            String clase = data.instance(i).stringValue(classIndex);
            grupos.computeIfAbsent(val, k -> new ArrayList<>()).add(clase);
    }

    double suma = 0.0;
        for (List<String> clases : grupos.values()) {
            Map<String, Integer> conteo = new HashMap<>();
            for (String c : clases) conteo.merge(c, 1, Integer::sum);
            suma += ((double) clases.size() / data.numInstances()) * calcularEntropia(conteo, clases.size());
        }
        return entropiaTotal - suma;
    }

    public Map<String, double[]> detalleAtributo(int atributoIndex) {
        Map<String, List<String>> grupos = new LinkedHashMap<>();
        for (int i = 0; i < data.numInstances(); i++) {
            String val = data.instance(i).stringValue(atributoIndex);
            String clase = data.instance(i).stringValue(classIndex);
            grupos.computeIfAbsent(val, k -> new ArrayList<>()).add(clase);
    }

    Map<String, double[]> resultado = new LinkedHashMap<>();
        for (Map.Entry<String, List<String>> entry : grupos.entrySet()) {
            List<String> clases = entry.getValue();
            long yes = clases.stream().filter(c -> c.equals("yes")).count();
            long no  = clases.stream().filter(c -> c.equals("no")).count();
            Map<String, Integer> conteo = new HashMap<>();
            for (String c : clases) conteo.merge(c, 1, Integer::sum);
            resultado.put(entry.getKey(), new double[]{yes, no, clases.size(), calcularEntropia(conteo, clases.size())});
        }
        return resultado;
    }
    
    public String getAtributoNombre(int index) {
        return data.attribute(index).name();
    }

    public int getTotalInstancias() { return data.numInstances(); }

    private double calcularEntropia(Map<String, Integer> conteo, int total) {
        double entropia = 0.0;
        for (int count : conteo.values()) {
            if (count > 0) {
                double p = (double) count / total;
                entropia -= p * (Math.log(p) / Math.log(2));
            }
        }
        return entropia;
    }
    
    // adiciones para entregable
    
    //nodo del arbol de decision
    public static class NodoDecision {
        String atributo;        // atributo a evaluar (null si es hoja)
        String clase;           // clase resultado si es hoja ("yes"/"no")
        Map<String, NodoDecision> ramas; // valor = subnodo

        //constructor q decide si es hoja o nodo itnerno
        NodoDecision(String valor, boolean esHoja) {
            if (esHoja) {
                this.clase = valor;
                this.ramas = null;
            } else {
                this.atributo = valor;
                this.ramas = new LinkedHashMap<>();
            }
        }
    }
    
    public NodoDecision construirArbol() {
        NodoDecision raiz = new NodoDecision("director_popularity", false);
        raiz.ramas.put("low",    new NodoDecision("no", true));
        raiz.ramas.put("medium", new NodoDecision("no", true));
        raiz.ramas.put("high",   new NodoDecision("yes", true));
        return raiz;
    }
    
    // rrecursiva: recorre el arbol y retorna la recomendación
    public String recomendar(NodoDecision nodo, Map<String, String> pelicula) {
        // Caso base: llegamos a una hoja
        if (nodo.ramas == null) {
            return nodo.clase;
        }
        // Caso recursivo: buscar el valor del atributo y bajar por la rama
        String valorAtributo = pelicula.getOrDefault(nodo.atributo, "medium");
        NodoDecision siguiente = nodo.ramas.getOrDefault(valorAtributo,
                                    nodo.ramas.values().iterator().next());
        return recomendar(siguiente, pelicula);
    }
}