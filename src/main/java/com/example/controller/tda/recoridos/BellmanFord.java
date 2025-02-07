package com.example.controller.tda.recoridos;

import java.util.HashMap;
import java.util.Map;

import com.example.controller.tda.list.graph.Adycencia;
import com.example.controller.tda.list.graph.GraphLabelNoDirect;

public class BellmanFord {

    public static Map<String, Object> ejecutarBellmanFord(GraphLabelNoDirect<?> graph, int source, int destino) {
        int n = graph.nro_vertices();
        float[] dist = new float[n];
        Integer[] predecesor = new Integer[n];

        for (int i = 0; i < n; i++) {
            dist[i] = Float.MAX_VALUE;
            predecesor[i] = null;
        }
        dist[source - 1] = 0;

        long startTime = System.currentTimeMillis(); 

        for (int i = 1; i < n; i++) { 
            for (int u = 1; u <= n; u++) {
                for (Adycencia edge : (Iterable<Adycencia>) graph.adycencias(u)) {
                    int v = edge.getDestination();
                    float peso = edge.getWeight();
                    if (dist[u - 1] != Float.MAX_VALUE && dist[u - 1] + peso < dist[v - 1]) {
                        dist[v - 1] = dist[u - 1] + peso;
                        predecesor[v - 1] = u - 1; 
                    }
                }
            }
        }

   
        for (int u = 1; u <= n; u++) {
            for (Adycencia edge : (Iterable<Adycencia>) graph.adycencias(u)) {
                int v = edge.getDestination();
                float peso = edge.getWeight();
                if (dist[u - 1] != Float.MAX_VALUE && dist[u - 1] + peso < dist[v - 1]) {
                    System.err.println("El grafo contiene un ciclo negativo");
                    return Map.of("error", "El grafo contiene un ciclo negativo");
                }
            }
        }

        long endTime = System.currentTimeMillis(); 
        System.out.println("Tiempo de ejecución de Bellman-Ford: " + (endTime - startTime) + " ms");
        imprimirDistancias(dist, source);
        String camino = reconstruirCamino(predecesor, source - 1, destino - 1);
        Map<String, Object> resultado = new HashMap<>();
        if (camino.equals("No existe camino")) {
            resultado.put("mensaje", camino);
            System.out.println(camino); 
        } else {
            resultado.put("camino", camino);
            resultado.put("Velocidad de transmicion", dist[destino - 1]);
            System.out.println("Camino mínimo: " + camino); 
            System.out.println("Velocidad de transmicion: " + dist[destino - 1]); 
        }
        return resultado;

    }

    private static String reconstruirCamino(Integer[] predecesor, int source, int destino) {
        if (predecesor[destino] == null) {
            return "No existe camino";
        }
        StringBuilder camino = new StringBuilder();
        int actual = destino;
        while (actual != source) {
            camino.insert(0, " -> " + (actual + 1));
            actual = predecesor[actual];
        }
        camino.insert(0, (source + 1));
        return camino.toString();
    }

    private static void imprimirDistancias(float[] dist, int source) {
        System.out.println("Velocidades mínimas desde el vértice " + source + ":");
        for (int i = 0; i < dist.length; i++) {
            System.out.println("Vértice " + (i + 1) + ": " + (dist[i] == Float.MAX_VALUE ? "INF" : dist[i]));
        }
    }

    public String caminoCorto(GraphLabelNoDirect<?> graph, int origen, int destino) throws Exception {

        if (graph == null) {
            throw new Exception("Grafo no existe");
        }

        System.out.println("Calculando camino corto desde " + origen + " hasta " + destino);

        Map<String, Object> resultado = BellmanFord.ejecutarBellmanFord(graph, origen, destino);

        if (resultado.containsKey("error")) {
            return resultado.get("error").toString();
        } else {
            // Obtener el camino y la velocidad
            String camino = (String) resultado.get("camino");
            Float velocidad = (Float) resultado.get("Velocidad de transmicion");

            // Imprimir el resultado
            System.out.println("Camino corto calculado: " + camino);
            System.out.println("Velocidad de transmicion: " + velocidad);

            return "Camino: " + camino + " | Velocidad de transmicion: " + velocidad;
        }
    }

}
