/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.pacifico.apinvent.ws;

import ec.pacifico.apinvent.entity.Temperatura;
import ec.pacifico.apinvent.facade.TemperaturaFacade;
import ec.pacifico.apinvent.facade.Conexion;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author Carolina
 */
@Stateless
@LocalBean
@Path("temperatura")
public class TemperaturaWS {

    @POST
    @Path("listfiltro")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response listfiltro(@Context HttpServletRequest request, Temperatura dato) {
        JSONObject resp = new JSONObject();

        if (dato.getFiltro0() == null) {
            resp.put("log", "Ingresar correctamente: filtro");
            resp.put("info", new JSONArray());
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }
        if (dato.getRack() == null) {
            resp.put("log", "Ingresar correctamente: rack");
            resp.put("info", new JSONArray());
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }
        if (dato.getTime0() == null || dato.getTime1() == null) {
            resp.put("log", "Ingresar correctamente: rango de tiempo");
            resp.put("info", new JSONArray());
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();

        }
        if (dato.getFiltro0().equals("halfhour")) {
            dato.setFiltroymd("year0,month0,day0,hour0,halfhour0");
            dato.setCastymd("CAST(year0 AS DECIMAL(10,2)) asc, CAST(month0 AS DECIMAL(10,2)) asc, CAST(day0 AS DECIMAL(10,2)) asc,CAST(hour0 AS DECIMAL(10,2)) asc,CAST(halfhour0 AS DECIMAL(10,2)) asc");
        } else if (dato.getFiltro0().equals("hour")) {
            dato.setFiltroymd("year0,month0,day0,hour0");
            dato.setCastymd("CAST(year0 AS DECIMAL(10,2)) asc, CAST(month0 AS DECIMAL(10,2)) asc, CAST(day0 AS DECIMAL(10,2)) asc,CAST(hour0 AS DECIMAL(10,2)) asc");
        } else if (dato.getFiltro0().equals("day")) {
            dato.setFiltroymd("year0,month0,day0");
            dato.setCastymd("CAST(year0 AS DECIMAL(10,2)) asc, CAST(month0 AS DECIMAL(10,2)) asc, CAST(day0 AS DECIMAL(10,2)) asc");
        } else if (dato.getFiltro0().equals("month")) {
            dato.setFiltroymd("year0,month0");
            dato.setCastymd("CAST(year0 AS DECIMAL(10,2)) asc, CAST(month0 AS DECIMAL(10,2)) asc");
        } else if (dato.getFiltro0().equals("year")) {
            dato.setFiltroymd("year0");
            dato.setCastymd("CAST(year0 AS DECIMAL(10,2)) asc");
        }
        //PROCESAMIENTO
        JSONArray lista = null;
        Conexion con = null;
        try {
            con = new Conexion();
            TemperaturaFacade consultaFacade = new TemperaturaFacade(con);
            lista = consultaFacade.obtenerListadofiltro(dato);
            con.closeConnection();
        } catch (Exception e) {
            System.out.println(this.getClass().toString() + " " + e.getMessage());
        } finally {
            con.closeConnection();
        }
        if (lista == null) { //|| size==0) {
            resp.put("log", "La consulta no retorna informacion");
            resp.put("info", new JSONArray()); //{}
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }

        resp.put("log", "consulta exitosa");
        resp.put("info", lista);
        return Response.status(Response.Status.OK).entity(resp.toString()).
                build();
    }

    @POST
    @Path("listfiltroequipo")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response listfiltroequipo(@Context HttpServletRequest request, Temperatura dato) {
        JSONObject resp = new JSONObject();

        if (dato.getFiltro0() == null) {
            resp.put("log", "Ingresar correctamente: filtro");
            resp.put("info", new JSONArray());
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }
        if (dato.getRack() == null) {
            resp.put("log", "Ingresar correctamente: rack");
            resp.put("info", new JSONArray());
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }
        if (dato.getHostname()== null) {
            resp.put("log", "Ingresar correctamente: hostname");
            resp.put("info", new JSONArray());
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }
        if (dato.getSerie()== null) {
            resp.put("log", "Ingresar correctamente: serie");
            resp.put("info", new JSONArray());
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }
        if (dato.getTime0() == null || dato.getTime1() == null) {
            resp.put("log", "Ingresar correctamente: rango de tiempo");
            resp.put("info", new JSONArray());
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();

        }
        if (dato.getFiltro0().equals("halfhour")) {
            dato.setFiltroymd("year0,month0,day0,hour0,halfhour0");
            dato.setCastymd("CAST(year0 AS DECIMAL(10,2)) asc, CAST(month0 AS DECIMAL(10,2)) asc, CAST(day0 AS DECIMAL(10,2)) asc,CAST(hour0 AS DECIMAL(10,2)) asc,CAST(halfhour0 AS DECIMAL(10,2)) asc");
        } else if (dato.getFiltro0().equals("hour")) {
            dato.setFiltroymd("year0,month0,day0,hour0");
            dato.setCastymd("CAST(year0 AS DECIMAL(10,2)) asc, CAST(month0 AS DECIMAL(10,2)) asc, CAST(day0 AS DECIMAL(10,2)) asc,CAST(hour0 AS DECIMAL(10,2)) asc");
        } else if (dato.getFiltro0().equals("day")) {
            dato.setFiltroymd("year0,month0,day0");
            dato.setCastymd("CAST(year0 AS DECIMAL(10,2)) asc, CAST(month0 AS DECIMAL(10,2)) asc, CAST(day0 AS DECIMAL(10,2)) asc");
        } else if (dato.getFiltro0().equals("month")) {
            dato.setFiltroymd("year0,month0");
            dato.setCastymd("CAST(year0 AS DECIMAL(10,2)) asc, CAST(month0 AS DECIMAL(10,2)) asc");
        } else if (dato.getFiltro0().equals("year")) {
            dato.setFiltroymd("year0");
            dato.setCastymd("CAST(year0 AS DECIMAL(10,2)) asc");
        }
        //PROCESAMIENTO
        JSONArray lista = null;
        Conexion con = null;
        try {
            con = new Conexion();
            TemperaturaFacade consultaFacade = new TemperaturaFacade(con);
            lista = consultaFacade.obtenerListadofiltrobyequipo(dato);
            con.closeConnection();
        } catch (Exception e) {
            System.out.println(this.getClass().toString() + " " + e.getMessage());
        } finally {
            con.closeConnection();
        }
        if (lista == null) { //|| size==0) {
            resp.put("log", "La consulta no retorna informacion");
            resp.put("info", new JSONArray()); //{}
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }

        resp.put("log", "consulta exitosa");
        resp.put("info", lista);
        return Response.status(Response.Status.OK).entity(resp.toString()).
                build();
    }

    @POST
    @Path("listequipos")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response listequipos(@Context HttpServletRequest request, Temperatura dato) {
        JSONObject resp = new JSONObject();
        if (dato.getRack() == null) {
            resp.put("log", "Ingresar correctamente: rack");
            resp.put("info", new JSONArray());
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }
        if (dato.getTime0() == null || dato.getTime1() == null) {
            resp.put("log", "Ingresar correctamente: rango de tiempo");
            resp.put("info", new JSONArray());
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();

        }

        //PROCESAMIENTO
        JSONArray lista = null;
        Conexion con = null;
        try {
            con = new Conexion();
            TemperaturaFacade consultaFacade = new TemperaturaFacade(con);
            lista = consultaFacade.obtenerEquipos(dato);
            con.closeConnection();
        } catch (Exception e) {
            System.out.println(this.getClass().toString() + " " + e.getMessage());
        } finally {
            con.closeConnection();
        }
        if (lista == null) { //|| size==0) {
            resp.put("log", "La consulta no retorna informacion");
            resp.put("info", new JSONArray()); //{}
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }

        resp.put("log", "consulta exitosa");
        resp.put("info", lista);
        return Response.status(Response.Status.OK).entity(resp.toString()).
                build();
    }

    @POST
    @Path("crear")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response crear(@Context HttpServletRequest request, Temperatura dato
    ) {
        JSONObject resp = new JSONObject();
        if (request.getContentType().isEmpty() || request.getContentType() == null || request.getContentType().isBlank()
                || !request.getContentType().contains("application/json") || request.getContentLength() == 0) {
            resp.put("log", "No ha enviado datos");
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }
        //VERIFICAR DATOS  
        if (dato.getArrtemp().isEmpty()) {
            resp.put("log", "No ha enviado datos");
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }
        //PROCESAMIENTO
        Conexion con = null;
        try {
            con = new Conexion();
            TemperaturaFacade consultaFacade = new TemperaturaFacade(con);
            consultaFacade.crear(dato);
            con.closeConnection();
        } catch (Exception e) {
            System.out.println(this.getClass().toString() + " " + e.getMessage());
        } finally {
            con.closeConnection();
        }

        resp.put("log", "consulta exitosa");
        return Response.status(Response.Status.OK).entity(resp.toString()).
                build();
    }

}
