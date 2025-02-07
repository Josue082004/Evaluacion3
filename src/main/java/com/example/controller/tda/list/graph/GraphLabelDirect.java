package com.example.controller.tda.list.graph;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.HashMap;

import com.example.controller.CoordenadasDispositivo;
import com.example.controller.excepcion.LabelException;
import com.example.controller.tda.list.LinkedList;
import com.example.models.Dispositivo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


public class GraphLabelDirect<E> extends GraphDirect {
    protected E labels[];
    protected HashMap<E, Integer> dictVertices;
    private Class<E> clazz;
    public CoordenadasDispositivo coordenadas;

    public GraphLabelDirect(Integer nro_vertices, Class<E> clazz, CoordenadasDispositivo coordenadas) {
        super(nro_vertices);
        this.coordenadas = coordenadas;
        labels = (E[]) Array.newInstance(clazz, nro_vertices + 1);
        dictVertices = new HashMap<>();
    }

    public Boolean is_edgeL(E v1, E v2) throws Exception {
        if (isLabelsGraph()) {
            return is_edge(getVerticeL(v1), getVerticeL(v2));
        } else {
            throw new LabelException("Grafo no etiquetado");
        }
    }

    public void insertEdgeL(E v1, E v2, Float weight) throws Exception {
        if (isLabelsGraph()) {
            add_edge(getVerticeL(v1), getVerticeL(v2), weight);
        } else {
            throw new LabelException("Grafo no etiquetado");
        }
    }

    public void insertEdgeL(E v1, E v2) throws Exception {
        if (isLabelsGraph()) {
            insertEdgeL(v1, v2, Float.NaN);

        } else {
            throw new LabelException("Grafo no etiquetado");
        }
    }

    public LinkedList<Adycencia> adycenciasL(E v) throws Exception {
        if (isLabelsGraph()) {
            return adycencias(getVerticeL(v));
        } else {
            throw new LabelException("Grafo no etiquetado");
        }
    }

    public void labelsVerticeL(Integer v, E data) {
        labels[v] = data;
        dictVertices.put(data, v);
    }

    public Boolean isLabelsGraph() {
        Boolean band = true;
        for (int i = 1; i < labels.length; i++) {
            if (labels[i] == null) {
                band = false;
                break;
            }
        }
        return band;
    }

    public Integer getVerticeL(E label) {
        return dictVertices.get(label);
    }

    public E getLabelL(Integer v1) {
        return labels[v1];
    }

    @Override
    public String toString() {
        String grafo = "";
        try {
            for (int i = 1; i <= this.nro_vertices(); i++) {
                grafo += "V" + i + " [" + getLabelL(i).toString() + "]" + "\n";
                LinkedList<Adycencia> lista = adycencias(i);
                if (!lista.isEmpty()) {
                    Adycencia[] ady = lista.toArray();
                    for (int j = 0; j < ady.length; j++) {
                        Adycencia a = ady[j];
                        grafo += "ady " + "V" + a.getDestination() + " weight: " + a.getWeight() + " ["
                                + getLabelL(a.getDestination()).toString() + "]" + "\n";
                    }
                }
            }
        } catch (Exception e) {
        }
        return grafo;
    }

    public String drawGraph() {
        String grafo = "var nodes = new vis.DataSet([" + "\n";
        try {
            for (int i = 1; i <= this.nro_vertices(); i++) {
                grafo += "{id: " + i + ", label:" + '"' + getLabelL(i).toString() + '"' + "}," + "\n";
            }
            grafo += "]);" + "\n";

            grafo += "var edges = new vis.DataSet([" + "\n";
            for (int i = 1; i <= this.nro_vertices(); i++) {
                LinkedList<Adycencia> lista = adycencias(i);
                if (!lista.isEmpty()) {
                    Adycencia[] ady = lista.toArray();
                    for (int j = 0; j < ady.length; j++) {
                        Adycencia a = ady[j];
                        grafo += "{from: " + i + ", to: " + a.getDestination() + ", label: " + '"' + a.getWeight() + '"'
                                + "}," + "\n";
                    }
                }
            }
            grafo += "]);" + "\n";


            grafo += "var container = document.getElementById(\"mynetwork\");" + "\n";
            grafo += "var data = {" + "\n";
            grafo += "nodes: nodes," + "\n";
            grafo += "edges: edges," + "\n";
            grafo += "};" + "\n";
            grafo += "var options = {" + "\n";
            grafo += "  edges: {" + "\n";
            grafo += "    font: { align: 'middle' }," + "\n"; 
            grafo += "    arrows: 'to' // Mostrar flechas en las aristas\n";
            grafo += "  }" + "\n";
            grafo += "};" + "\n";
            grafo += "var network = new vis.Network(container, data, options);" + "\n";

            FileWriter file = new FileWriter(
                    "resources" + File.separatorChar + "graph" + File.separatorChar + "graph.js");
            file.write(grafo);
            file.flush();
            file.close();
        } catch (Exception e) {
            e.printStackTrace(); 
        }
        return grafo;
    }

    public E[] getLabels() {
        return labels;
    }

    public HashMap<E, Integer> getDictVertices() {
        return dictVertices;
    }

    public void guardarGrafoEnJson() {
        String rutaArchivo = "media\\grafo.json";
        try {
            JsonObject jsonGrafo = new JsonObject();

            JsonArray jsonLabels = new JsonArray();
            for (int i = 1; i <= this.nro_vertices(); i++) {
                JsonObject vertice = new JsonObject();
                vertice.addProperty("id", i);
                vertice.addProperty("label", getLabelL(i).toString());
                jsonLabels.add(vertice);
            }
            jsonGrafo.add("etiquetas", jsonLabels);

            JsonArray jsonAristas = new JsonArray();
            for (int i = 1; i <= this.nro_vertices(); i++) {
                LinkedList<Adycencia> lista = adycencias(i);
                if (!lista.isEmpty()) {
                    Adycencia[] ady = lista.toArray();
                    for (int j = 0; j < ady.length; j++) {
                        Adycencia a = ady[j];
                        JsonObject arista = new JsonObject();
                        arista.addProperty("from", i);
                        arista.addProperty("to", a.getDestination());
                        arista.addProperty("weight", a.getWeight());
                        jsonAristas.add(arista);
                    }
                }
            }
            jsonGrafo.add("aristas", jsonAristas);

            try (FileWriter file = new FileWriter(rutaArchivo)) {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                file.write(gson.toJson(jsonGrafo));
                file.flush();
            }

            System.out.println("Grafo guardado en el archivo " + rutaArchivo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createGraph(LinkedList<Dispositivo> lista) {
        if (lista.isEmpty()) {
            System.out.println("No hay redes para crear el grafo.");
            return;
        }

        this.nro_vertices = lista.getSize();
        labels = (E[]) Array.newInstance(clazz, nro_vertices + 1);
        dictVertices = new HashMap<>();

        Dispositivo[] arreglo = lista.toArray();

        for (int i = 0; i < arreglo.length; i++) {
            if (arreglo[i] != null) {
                labelsVerticeL(i + 1, (E) arreglo[i]);
            }
        }

        System.out.println("Grafo creado con " + lista.getSize() + " vértices, pero sin adyacencias.");
    }

}
