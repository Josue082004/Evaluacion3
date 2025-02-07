package com.example.controller.tda.list.graph;

import com.example.controller.excepcion.LabelException;
import com.example.controller.tda.list.LinkedList;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Set;

import com.example.controller.CoordenadasDispositivo;

public class GraphLabelNoDirect<E> extends GraphLabelDirect<E> {

    

    public GraphLabelNoDirect(Integer nro_vertices, Class<E> clazz, CoordenadasDispositivo coordenadas) {
        super(nro_vertices, clazz, coordenadas); // Llamada al constructor de la clase base
    }
    //Adyacencias Manuales 
    
    public void insertEdgeLD(E v1, E v2) throws Exception {
        if (isLabelsGraph()) {
            // Obtener la velocidad geográfica entre las dos redes usando el HashMap de coordenadas
            double velocidad = coordenadas.calcularCosteTransmision(v1.toString(), v2.toString());

            // Si la velocidad es válida, insertar la arista con la velocidad calculada
            if (velocidad >= 0) {
                // Se inserta la arista en ambas direcciones para grafo no dirigido
                add_edge(getVerticeL(v1), getVerticeL(v2), (float) velocidad);
                add_edge(getVerticeL(v2), getVerticeL(v1), (float) velocidad);

            }
        } else {
            throw new LabelException("Grafo no etiquetado");
        }
        System.err.println(coordenadas.calcularCosteTransmision(v1.toString(), v2.toString()));
    }

    @Override
    public E[] getLabels() {
        return labels; // Devuelve el array de etiquetas
    }

    @Override
    public LinkedList<Adycencia> adycencias(Integer vertex) {
        try {
            return super.adycencias(vertex); // Llama al método de la superclase
        } catch (RuntimeException e) {
            throw e; // Re-lanza la excepción no comprobada si es crítico
        } catch (Exception e) {
            throw new RuntimeException("Error obteniendo adyacencias", e);
        }
    }

    public Float getWeight(Integer from, Integer to) {
        try {
            return weight_edge(from, to); // Obtiene el peso de la arista
        } catch (RuntimeException e) {
            throw e; // Re-lanza la excepción no comprobada si es crítico
        } catch (Exception e) {
            throw new RuntimeException("Error obteniendo peso de la arista", e);
        }
    }

    public void generarAdyacenciasAleatorias() throws Exception {
    Random random = new Random();

    for (int i = 1; i <= this.nro_vertices(); i++) {
        Set<Integer> conexiones = new HashSet<>(); // Usamos un Set para evitar duplicados

        while (conexiones.size() < 3) {
            int destino = random.nextInt(this.nro_vertices()) + 1; // Genera un nodo aleatorio entre 1 y nro_vertices

            // Asegurarse de que no sea el mismo nodo y que no haya conexión previa
            if (destino != i && !conexiones.contains(destino)) {
                conexiones.add(destino);

                // Insertar arista bidireccional
                add_edge(i, destino, (float) coordenadas.calcularCosteTransmision(getLabelL(i).toString(), getLabelL(destino).toString()));
                add_edge(destino, i, (float) coordenadas.calcularCosteTransmision(getLabelL(destino).toString(), getLabelL(i).toString()));
            }
        }

        // Imprimir las conexiones generadas para cada nodo
        System.out.println("Nodo " + i + " conectado a: " + conexiones);
       }
    }

    // Método para conectar todos los vértices usando Prim con Adycencia
    public void conectarTodosPrim() throws Exception {
        int numVertices = this.nro_vertices();
        if (numVertices <= 1) return; // No hay suficientes nodos para conectar

        PriorityQueue<Adycencia> minHeap = new PriorityQueue<>(Comparator.comparingDouble(Adycencia::getWeight));
        boolean[] visitado = new boolean[numVertices + 1]; // Controla qué nodos han sido visitados
        List<Adycencia> aristasMST = new ArrayList<>();

        // Iniciar desde el primer nodo (ID = 1)
        visitado[1] = true;
        for (int i = 2; i <= numVertices; i++) {
            float peso = (float) coordenadas.calcularCosteTransmision(getLabelL(1).toString(), getLabelL(i).toString());
            if (peso >= 0) {
                minHeap.add(new Adycencia(i, peso));
            }
        }

        while (!minHeap.isEmpty() && aristasMST.size() < numVertices - 1) {
            Adycencia menor = minHeap.poll();
            int destino = menor.getDestination();

            if (!visitado[destino]) {
                visitado[destino] = true;
                aristasMST.add(menor);


                add_edge(1, destino, menor.getWeight());
                add_edge(destino, 1, menor.getWeight());
                for (int i = 1; i <= numVertices; i++) {
                    if (!visitado[i]) {
                        float peso = (float) coordenadas.calcularCosteTransmision(getLabelL(destino).toString(), getLabelL(i).toString());
                        if (peso >= 0) {
                            minHeap.add(new Adycencia(i, peso));
                        }
                    }
                }
            }
        }

        System.out.println("Conexiones generadas con Prim:");
        for (Adycencia ady : aristasMST) {
            System.out.println(getLabelL(1) + " <---> " + getLabelL(ady.getDestination()) + " [Costo: " + ady.getWeight() + "]");
        }
    }


    
}


