package com.example.rest;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.example.controller.CoordenadasDispositivo;
import com.example.controller.dao.services.DispositivoService;
import com.example.controller.tda.list.LinkedList;
import com.example.controller.tda.list.graph.GraphLabelNoDirect;
import com.example.controller.tda.recoridos.BellmanFord;
import com.example.models.Dispositivo;
import com.google.gson.Gson;


@Path("Dispositivo")
public class DispositivoApi {

    private GraphLabelNoDirect<Dispositivo> graph;
    private CoordenadasDispositivo coordenadas = new CoordenadasDispositivo();

    @Path("/list")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPersons() {
        HashMap map = new HashMap<>();
        DispositivoService ps = new DispositivoService();
        map.put("msg", "OK");
        map.put("data", ps.listAll().toArray());
        if (ps.listAll().isEmpty()) {
            map.put("data", new Object[] {});
        }

        return Response.ok(map).build();
    }

    @Path("/get/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPerson(@PathParam("id") Integer id) {
        HashMap map = new HashMap<>();
        DispositivoService ps = new DispositivoService();
        try {
            ps.setDispositivo(ps.get(id));
        } catch (Exception e) {
            // TODO: handle exception
        }
        map.put("msg", "OK");
        map.put("data", ps.getDispositivo());
        if (ps.getDispositivo().getId() == null) {
            map.put("data", "No existe la Dispositivo con ese identificador");
            return Response.status(Status.BAD_REQUEST).entity(map).build();
        }
        return Response.ok(map).build();
    }

    @Path("/save")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response save(HashMap map) {
        // TODO
        // VALIDATION ---- BAD REQUEST

        HashMap res = new HashMap<>();
        Gson g = new Gson();
        String a = g.toJson(map);
        System.out.println("******* " + a);
        try {
            DispositivoService ps = new DispositivoService();
            ps.getDispositivo().setNombre(map.get(("nombre")).toString());
            ps.getDispositivo().setMarca(map.get("marca").toString());
            ps.getDispositivo().setIp(map.get("ip").toString());
            ps.getDispositivo().setVelocidadAlta(Double.parseDouble(map.get("velocidadAlta").toString()));
            ps.getDispositivo().setVelocidadBaja(Double.parseDouble(map.get("velocidadBaja").toString()));

            ps.save();
            res.put("msg", "OK");
            res.put("data", "Dispositivo registrada correctamente");
            return Response.ok(res).build();
        } catch (Exception e) {
            System.out.println("Error en sav data " + e.toString());
            res.put("msg", "Error");
            // res.put("msg", "ERROR");
            res.put("data", e.toString());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(res).build();
            // TODO: handle exception
        }

    }

    @Path("/grafo")
    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response getGrafoHtml() {
        // Ruta absoluta al archivo grafo.html
        String filePath = "resources\\graph\\grafo.html";

        File file = new File(filePath);

        if (!file.exists()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Archivo grafo.html no encontrado").build();
        }

        try {
            // Leer el contenido del archivo
            String content = Files.readString(file.toPath(), StandardCharsets.UTF_8);
            return Response.ok(content).build();
        } catch (IOException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al leer el archivo grafo.html: " + e.getMessage()).build();
        }
    }

    @GET
    @Path("/bellman/{origen}/{destino}")
    public Response ejecutarBellmanFord(@PathParam("origen") int origen, @PathParam("destino") int destino) {
        HashMap<String, Object> mapa = new HashMap<>();
        try {

            // Validación de nodos
            System.out.println("Número de vértices: " + graph.nro_vertices());
            if (origen <= 0 || destino <= 0 || origen > graph.nro_vertices() || destino > graph.nro_vertices()) {
                mapa.put("msg", "Error: Nodos fuera de rango.");
                return Response.status(Response.Status.BAD_REQUEST).entity(mapa).build();
            }

            Map<String, Object> resultado = BellmanFord.ejecutarBellmanFord(graph, origen, destino);
            System.out.println("Resultado de Bellman-Ford: " + resultado); // Verificar resultado
            mapa.put("msg", "OK");
            mapa.put("data", resultado);
            return Response.ok(mapa).build();

        } catch (Exception e) {
            mapa.put("msg", "Error");
            mapa.put("data", e.toString());
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(mapa).build();
        }
    }

    @Path("/caminoCorto/{origen}/{destino}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response calcularCaminoCorto(@PathParam("origen") int origen, @PathParam("destino") int destino) {
        HashMap<String, Object> res = new HashMap<>();
        try {

            DispositivoService ps = new DispositivoService();
            LinkedList<Dispositivo> lp = ps.listAll();

            // Verificar que la lista no esté vacía
            if (lp.isEmpty()) {
                res.put("msg", "Error");
                res.put("data", "No se encontraron Dispositivo en la base de datos.");
                return Response.status(Response.Status.BAD_REQUEST).entity(res).build();
            }

            // Agregar las Dispositivo HashMap de coordenadas
            for (Dispositivo p : lp.toArray()) {
                coordenadas.agregarDispositivo(p);
            }

            for (Dispositivo p : lp.toArray()) {
                System.out.println(p.getNombre());
            }

            graph = new GraphLabelNoDirect<>(lp.getSize(), Dispositivo.class, coordenadas);
            Dispositivo[] m = lp.toArray();

            for (int i = 0; i < lp.getSize(); i++) {
                graph.labelsVerticeL((i + 1), m[i]);
            }

            // Generar adyacencias aleatorias
            graph.conectarTodosPrim();
		
		       
        
            // Guardar el grafo en formato JSON usando el método de GraphLabelDirect
            graph.guardarGrafoEnJson();
    
            // Dibujar el grafo (opcional)
            graph.drawGraph();



            // Instanciar el objeto BellmanFord y calcular el camino corto
            BellmanFord bellmanFord = new BellmanFord();
            String resultado = bellmanFord.caminoCorto(graph, origen, destino);

            // Verificar si el resultado contiene algún error (camino no encontrado o ciclo
            // negativo)
            if (resultado.contains("No existe camino") || resultado.contains("El grafo contiene un ciclo negativo")) {
                res.put("msg", "Error");
                res.put("data", resultado);
                return Response.status(Response.Status.BAD_REQUEST).entity(res).build();
            }

            // Si todo salió bien, devolver el camino y la distancia
            res.put("msg", "Camino corto calculado exitosamente");
            res.put("resultado", resultado);
            return Response.ok(res).build();

        } catch (Exception e) {
            // Capturar cualquier excepción que ocurra
            res.put("msg", "Error");
            res.put("data", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(res).build();
        }

    }

}
