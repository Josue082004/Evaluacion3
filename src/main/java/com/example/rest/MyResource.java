package com.example.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import com.example.controller.CoordenadasDispositivo;
import com.example.controller.dao.services.DispositivoService;
import com.example.controller.tda.list.LinkedList;
import com.example.controller.tda.list.graph.GraphLabelNoDirect;
import com.example.controller.tda.recoridos.BellmanFord;
import com.example.models.Dispositivo;



@Path("myresource")
public class MyResource {

}



