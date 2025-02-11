/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.pacifico.apinvent.ws;

import ec.pacifico.apinvent.entity.ActHeader;
import ec.pacifico.apinvent.entity.Actividades;
import ec.pacifico.apinvent.entity.TTHeader;
import ec.pacifico.apinvent.entity.Ticket;
import ec.pacifico.apinvent.facade.ActividadesFacade;
import ec.pacifico.apinvent.facade.Conexion;
import ec.pacifico.apinvent.facade.TicketFacade;
import ec.pacifico.apinvent.facade.UsuarioFacade;
import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
@Path("ticket")
public class TicketWS {

    @POST
    @Path("list")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response list(@Context HttpServletRequest request, Ticket dato) {
        JSONObject resp = new JSONObject();
        if (request.getContentType().isEmpty() || request.getContentType() == null || request.getContentType().isBlank()
                || !request.getContentType().contains("application/json") || request.getContentLength() == 0) {
            resp.put("log", "No ha enviado datos");
            resp.put("info", new JSONArray());
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    header("totalpaginas", 0).
                    header("totalresultados", 0).
                    build();
        }
        //VERIFICAR DATOS  
        if (dato.getPageIndex() == null) {
            resp.put("log", "Ingresar correctamente: index");
            resp.put("info", new JSONArray());
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    header("totalpaginas", 0).
                    header("totalresultados", 0).
                    build();
        }
        if (dato.getPageSize() == null) {
            resp.put("log", "Ingresar correctamente: tamaño de página");
            resp.put("info", new JSONArray());
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    header("totalpaginas", 0).
                    header("totalresultados", 0).
                    build();
        }

        //PROCESAMIENTO
        JSONArray lista = null;
        int size = 0;
        Conexion con = null;
        try {
            con = new Conexion();
            TicketFacade consultaFacade = new TicketFacade(con);
            lista = consultaFacade.obtenerListado(dato);
            size = consultaFacade.obtenerListadoSize(dato);
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
                    header("totalpaginas", 0).
                    header("totalresultados", 0).
                    build();
        }

        int residuo = size % dato.getPageSize();
        int paginas = 0;
        if (residuo == 0) {
            paginas = (int) size / dato.getPageSize();
        } else {
            paginas = (int) (size / dato.getPageSize()) + 1;
        }
        resp.put("log", "consulta exitosa");
        resp.put("info", lista);
        return Response.status(Response.Status.OK).entity(resp.toString()).
                header("totalpaginas", paginas).
                header("totalresultados", size).
                build();
    }

    @POST
    @Path("id")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response porIdTT(@Context HttpServletRequest request, TTHeader dato) {
        JSONObject resp = new JSONObject();
        if (request.getContentType().isEmpty() || request.getContentType() == null || request.getContentType().isBlank()
                || !request.getContentType().contains("application/json") || request.getContentLength() == 0) {
            resp.put("log", "No ha enviado datos");
            resp.put("info", null);
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }
        //VERIFICAR DATOS  
        if (dato.getId() == null) {
            resp.put("log", "Ingresar correctamente: id");
            resp.put("info", null);
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }

        //PROCESAMIENTO
        JSONArray lista = null;
        Conexion con = null;
        try {
            con = new Conexion();
            TicketFacade consultaFacade = new TicketFacade(con);
            lista = consultaFacade.busquedaIdTTHeader(dato);
            con.closeConnection();
        } catch (Exception e) {
            System.out.println(this.getClass().toString() + " " + e.getMessage());
        } finally {
            con.closeConnection();
        }
        if (lista == null) { //|| size==0) {
            resp.put("log", "La consulta no retorna informacion");
            resp.put("info", null); //{}
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }
        resp.put("log", "consulta exitosa");
        resp.put("info", lista.get(0));
        return Response.status(Response.Status.OK).entity(resp.toString()).
                build();
    }

    @POST
    @Path("idticket")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response porIdTicket(@Context HttpServletRequest request, Ticket dato) {
        JSONObject resp = new JSONObject();
        if (request.getContentType().isEmpty() || request.getContentType() == null || request.getContentType().isBlank()
                || !request.getContentType().contains("application/json") || request.getContentLength() == 0) {
            resp.put("log", "No ha enviado datos");
            resp.put("info", null);
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }
        //VERIFICAR DATOS  
        if (dato.getId() == null) {
            resp.put("log", "Ingresar correctamente: id");
            resp.put("info", null);
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }

        //PROCESAMIENTO
        JSONArray lista = null;
        Conexion con = null;
        try {
            con = new Conexion();
            TicketFacade consultaFacade = new TicketFacade(con);
            lista = consultaFacade.busquedaIdTicket(dato);
            con.closeConnection();
        } catch (Exception e) {
            System.out.println(this.getClass().toString() + " " + e.getMessage());
        } finally {
            con.closeConnection();
        }
        if (lista == null) { //|| size==0) {
            resp.put("log", "La consulta no retorna informacion");
            resp.put("info", null); //{}
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }
        resp.put("log", "consulta exitosa");
        resp.put("info", lista.get(0));
        return Response.status(Response.Status.OK).entity(resp.toString()).
                build();
    }

    @POST
    @Path("crear")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response crear(@Context HttpServletRequest request, TTHeader dato) {
        JSONObject resp = new JSONObject();
        if (request.getContentType().isEmpty() || request.getContentType() == null || request.getContentType().isBlank()
                || !request.getContentType().contains("application/json") || request.getContentLength() == 0) {
            resp.put("log", "No ha enviado datos");
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }

        if (dato.getFecha() == null || dato.getFecha().trim().length() == 0 || dato.getFecha().contains("echa") || dato.getFecha().contains("ate")) {
            resp.put("log", "Ingresar correctamente: Fecha de Ticket");
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }

        if (dato.getTecnicorespon() == null || dato.getTecnicorespon().trim().length() == 0) {
            resp.put("log", "Ingresar correctamente: Tecnico Responsable");
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }

        if (dato.getTecnicoreporte() == null || dato.getTecnicoreporte().trim().length() == 0) {
            resp.put("log", "Ingresar correctamente: Reportado por");
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }

        for (ActHeader act : dato.getActividades()) {
            if (act.getSubactividades() == null) {
                resp.put("log", "No registra subactividad");
                return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                        build();
            }
            if (act.getActividad() == null) {
                resp.put("log", "No registra identificador");
                return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                        build();
            }
            for (Actividades subact : act.getSubactividades()) {
                if (subact.getFecha() != null && subact.getFecha().length() == 0) {
                    subact.setFecha(null);
                    subact.setFecha2(null);
                }
                if ((subact.getMins() != null && subact.getMins() != 0)) {
                    act.setCheck(1);
                    subact.setAbierto(1);
                } else {
                    act.setCheck(0);
                    subact.setAbierto(0);
                }
                if (act.getNameact().contains("ngres")) {
                    subact.setUsuario(dato.getTecnicorespon());
                } else {
                    subact.setUsuario(dato.getTecnicoreporte());
                }
                if (subact.getFecha() != null) {
                    LocalDateTime endActual = LocalDateTime.parse((CharSequence) subact.getFecha().replace(" ", "T"));
                    LocalDateTime endActualp = endActual.plusMinutes(Long.parseLong(subact.getMins().toString()));
                    String fecha2 = endActualp.toString();
                    subact.setFecha2(fecha2);

                }

            }

            

        }
        
        for (ActHeader act : dato.getActividades()) {
            for (Actividades subact1 : act.getSubactividades()) {
                Long idact1 = act.getActividad();
                if (subact1.getUsuario() != null && subact1.getFecha() != null) {
                    LocalDateTime fechai1 = LocalDateTime.parse((CharSequence) subact1.getFecha().replace(" ", "T"));
                    for (ActHeader act2 : dato.getActividades()) {
                        Long idact2 = act2.getActividad();
                        for (Actividades subact2 : act2.getSubactividades()) {
                            if (subact2.getUsuario() != null && subact2.getFecha() != null) {
                                LocalDateTime fechai2 = LocalDateTime.parse((CharSequence) subact2.getFecha().replace(" ", "T"));
                                LocalDateTime fechae2 = LocalDateTime.parse((CharSequence) subact2.getFecha2().replace(" ", "T"));
                                if (idact1.compareTo(idact2) != 0 && subact1.getUsuario().equals(subact2.getUsuario())) {
                                    if ((fechai1.isAfter(fechai2) || fechai1.isEqual(fechai2)) && (fechai1.isBefore(fechae2) || fechai1.isEqual(fechae2))) {
                                        resp.put("log", "Se chocan los tiempos entre actividades");
                                        return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                                                build();
                                    }

                                }
                            }

                        }
                    }

                }
            }
        }
        
        

        //VERIFICAR DATOS  
        if (dato.getIdproblema() == null) {
            resp.put("log", "Ingresar correctamente: problema");
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }

        if (dato.getTickets() == null || dato.getTickets().isEmpty()) {
            resp.put("log", "Ingresar correctamente: Eventos");
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }

        if (dato.getIdproveedor() != null && dato.getIdproveedor() == 15l) {
            dato.setIdproveedor(null);
        }

        if (dato.getIdproblema() == 2 && dato.getIdproveedor() == null) {
            resp.put("log", "Ingresar correctamente: Proveedor para perdida de enlace");
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }

        if (dato.getIdproblema() == 10 && dato.getIdproveedor() == null) {
            resp.put("log", "Ingresar correctamente: Proveedor para degradacion de enlace");
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }

        if (dato.getSoporte() == null || dato.getSoporte().trim().length() == 0) {
            resp.put("log", "Ingresar correctamente: Soporte");
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }

        if (dato.getDescripcion() == null || dato.getDescripcion().trim().length() == 0) {
            resp.put("log", "Ingresar correctamente: Descripcion");
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }

        if (dato.getSoporte() == null
                || dato.getSoporte().trim().length() == 0
                || dato.getDescripcion() == null
                || dato.getDescripcion().trim().length() == 0) {
            dato.setTthabierto(0); //ABIERTO
        } else {
            dato.setTthabierto(1);
        }

        for (Ticket tt : dato.getTickets()) {
            if (tt.getIdagencia() == null) {
                resp.put("log", "Ingresar correctamente: Agencia");
                return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                        build();
            }
            if (tt.getTcompleto() == null || tt.getTcompleto().trim().length() == 0) {
                resp.put("log", "Ingresar correctamente: tiempo completo");
                return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                        build();
            }
            if (tt.getTime0() == null || tt.getTime0().trim().length() == 0) {
                resp.put("log", "Ingresar correctamente: Tiempo Inicial");
                return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                        build();
            }
            if (tt.getTcompleto().trim().toLowerCase().equals("no")) {
                if (tt.getTime1() == null || tt.getTime1().trim().length() == 0) {
                    resp.put("log", "Ingresar correctamente: Tiempo cargado");
                    return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                            build();
                }
            }

            if (tt.getTcompleto().equals("NO APLICA") && dato.getIdproblema() == 2) {
                resp.put("log", "Ingresar correctamente: tiempo completo para perdida de enlace");
                return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                        build();
            }
            if (tt.getTcompleto().equals("NO APLICA") && dato.getIdproblema() == 10) {
                resp.put("log", "Ingresar correctamente: tiempo completo para degradacion de enlace");
                return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                        build();
            }

            if (dato.getIdproblema() == 2 && tt.getIdenlace() == null) {
                resp.put("log", "Ingresar correctamente:Ticket (Agencia no tiene asociado enlace, registrar nuevamente)");
                return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                        build();
            }

            if (dato.getIdproblema() == 10 && tt.getIdenlace() == null) {
                resp.put("log", "Ingresar correctamente:Ticket (Agencia no tiene asociado enlace, registrar nuevamente)");
                return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                        build();
            }

            if (tt.getTime2() == null || tt.getTime2().trim().length() == 0) {
                tt.setTmins(0l);
                tt.setAbierto(0);//ABIERTO
                tt.setTime2(null);
            } else {
                tt.setAbierto(1);//CERRADO
            }
            

        }

        if (dato.getActividades() == null || dato.getActividades().size() == 0) {
            resp.put("log", "No registra actividades");
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }

        JSONArray lista2 = null;
        //PROCESAMIENTO
        Conexion con = null;
        try {
            con = new Conexion();
            JSONParser parser = new JSONParser();
            UsuarioFacade consultaFacade2 = new UsuarioFacade(con);
            lista2 = consultaFacade2.busquedaPermisos(dato.getUsername());
            if (lista2 != null) {
                JSONObject obj = (JSONObject) parser.parse(lista2.get(0).toString());

            }
            ActividadesFacade consultaFacade0 = new ActividadesFacade(con);
            for (ActHeader act : dato.getActividades()) {
                for (Actividades subact : act.getSubactividades()) {
                    if (subact.getMins() > 0) {
                        if (consultaFacade0.existeEvento(subact)) {
                            resp.put("log", "Existe una actividad ya ingresado");
                            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                                    build();
                        }
                    }
                }
            }

            TicketFacade consultaFacade = new TicketFacade(con);

            for (Ticket tt : dato.getTickets()) {
                if(!consultaFacade.validarTiempos(tt.getTime0(), tt.getTime1(), tt.getTime2())){
                    resp.put("log", "Validar tiempos");
                    return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                            build();
                }
                if (consultaFacade.isBaja(dato, tt)) {
                    resp.put("log", "Evento tiene enlace en Baja");
                    return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                            build();
                }
                if (consultaFacade.existeEvento(tt)) {
                    resp.put("log", "Existe un evento ya ingresado");
                    return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                            build();
                }
                

            }
            boolean b = consultaFacade.crear(dato);
            con.closeConnection();

            if (!b) {
                resp.put("log", "No se pudo realizar esta accion");
                return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                        build();
            }
        } catch (Exception e) {
            System.out.println(this.getClass().toString() + " " + e.getMessage());
        } finally {
            con.closeConnection();
        }

        resp.put("log", "consulta exitosa");
        return Response.status(Response.Status.OK).entity(resp.toString()).
                build();
    }

    @POST
    @Path("actualizar")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response actualizarTicket(@Context HttpServletRequest request, Ticket dato) {
        JSONObject resp = new JSONObject();
        if (request.getContentType().isEmpty() || request.getContentType() == null || request.getContentType().isBlank()
                || !request.getContentType().contains("application/json") || request.getContentLength() == 0) {
            resp.put("log", "No ha enviado datos");
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }
        //VERIFICAR DATOS  
        if (dato.getId() == null) {
            resp.put("log", "Ingresar correctamente: id");
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }

        if (dato.getEstado() == null) {
            resp.put("log", "Ingresar correctamente: estado");
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }
        if (dato.getIdagencia() == null) {
            resp.put("log", "Ingresar correctamente: Agencia");
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }
        if (dato.getTcompleto() == null || dato.getTcompleto().trim().length() == 0) {
            resp.put("log", "Ingresar correctamente: tiempo completo");
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }
        if (dato.getTime0() == null || dato.getTime0().trim().length() == 0) {
            resp.put("log", "Ingresar correctamente: Tiempo Inicial");
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }
        if (dato.getTcompleto().trim().toLowerCase().equals("no")) {
            if (dato.getTime1() == null || dato.getTime1().trim().length() == 0) {
                resp.put("log", "Ingresar correctamente: Tiempo cargado");
                return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                        build();
            }
        } else {
            dato.setTime1(null);
        }

        if (dato.getTime2() == null || dato.getTime2().trim().length() == 0) {
            dato.setTmins(0l);
            dato.setAbierto(0);//ABIERTO
            dato.setTime2(null);
        } else {
            dato.setAbierto(1);//CERRADO
        }

        long minutos = 0l;
        if (dato.getTmins() == null && dato.getTime2() != null) {
            LocalDateTime iniActual;
            LocalDateTime endActual = LocalDateTime.parse((CharSequence) dato.getTime2().replace(" ", "T"));

            if (dato.getTcompleto().trim().toLowerCase().equals("no")) {
                iniActual = LocalDateTime.parse((CharSequence) dato.getTime1().replace(" ", "T"));
            } else {
                iniActual = LocalDateTime.parse((CharSequence) dato.getTime0().replace(" ", "T"));
            }
            minutos = ChronoUnit.MINUTES.between(iniActual, endActual);

            dato.setTmins(minutos);
        }
        if (dato.getTmins().compareTo(0l) <= 0) {
            resp.put("log", "Verificar fecha, los minutos son  menor a cero");
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }
        //PROCESAMIENTO
        Conexion con = null;
        JSONArray lista2 = null;
        try {

            con = new Conexion();
            JSONParser parser = new JSONParser();
            UsuarioFacade consultaFacade2 = new UsuarioFacade(con);
            lista2 = consultaFacade2.busquedaPermisos(dato.getUsername());
            if (lista2 != null) {
                JSONObject obj = (JSONObject) parser.parse(lista2.get(0).toString());

            }
            TicketFacade consultaFacade = new TicketFacade(con);
            if (consultaFacade.busquedaIdTicket(dato) == null) {
                resp.put("log", "No existe");
                return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                        build();
            }

            if (dato.getEstado() == 0) {
                if (consultaFacade.existeEvento(dato)) {
                    resp.put("log", "Existe un evento ingresado de LAN");
                    return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                            build();
                }
            }

            /*
            if (consultaFacade.existeEventoId(dato)) {
                resp.put("log", "Existe un evento ya ingresado");
                return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                        build();
            }*/
            consultaFacade.actualizarTicket(dato);
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

    @POST
    @Path("actualizarheader")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response actualizarHeader(@Context HttpServletRequest request, TTHeader dato) {
        JSONObject resp = new JSONObject();
        if (request.getContentType().isEmpty() || request.getContentType() == null || request.getContentType().isBlank()
                || !request.getContentType().contains("application/json") || request.getContentLength() == 0) {
            resp.put("log", "No ha enviado datos");
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }
        //VERIFICAR DATOS  
        if (dato.getIdproblema() == null) {
            resp.put("log", "Ingresar correctamente: problema");
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }

        if (dato.getFecha() == null || dato.getFecha().trim().length() == 0 || dato.getFecha().contains("echa") || dato.getFecha().contains("ate")) {
            resp.put("log", "Ingresar correctamente: Fecha de Ticket");
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }

        if (dato.getTecnicorespon() == null || dato.getTecnicorespon().trim().length() == 0) {
            resp.put("log", "Ingresar correctamente: Tecnico Responsable");
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }

        if (dato.getTecnicoreporte() == null || dato.getTecnicoreporte().trim().length() == 0) {
            resp.put("log", "Ingresar correctamente: Reportado por");
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }

        if (dato.getIdproveedor() != null && dato.getIdproveedor() == 15l) {
            dato.setIdproveedor(null);
        }

        if (dato.getSoporte() == null || dato.getSoporte().trim().length() == 0) {
            resp.put("log", "Ingresar correctamente: Soporte");
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }

        if (dato.getDescripcion() == null || dato.getDescripcion().trim().length() == 0) {
            resp.put("log", "Ingresar correctamente: Descripcion");
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }

        if (dato.getIdproveedor() == null) {
            if (dato.getDescripcion() == null
                    || dato.getDescripcion().trim().length() == 0) {
                dato.setTthabierto(0); //ABIERTO
            } else {
                dato.setTthabierto(1);
            }
        } else {
            if (dato.getSoporte() == null
                    || dato.getSoporte().trim().length() == 0
                    || dato.getDescripcion() == null
                    || dato.getDescripcion().trim().length() == 0) {
                dato.setTthabierto(0); //ABIERTO
            } else {
                dato.setTthabierto(1);
            }
        }

        if (dato.getTickets() != null) {
            for (Ticket tt : dato.getTickets()) {
                if (tt.getIdagencia() == null) {
                    resp.put("log", "Ingresar correctamente: Agencia");
                    return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                            build();
                }

                if (tt.getTcompleto() == null || tt.getTcompleto().trim().length() == 0) {
                    resp.put("log", "Ingresar correctamente: tiempo completo");
                    return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                            build();
                }

                if (tt.getTcompleto().equals("NO APLICA") && dato.getIdproblema() == 2) {
                    resp.put("log", "Ingresar correctamente: tiempo completo para perdida de enlace");
                    return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                            build();
                }
                if (tt.getTcompleto().equals("NO APLICA") && dato.getIdproblema() == 10) {
                    resp.put("log", "Ingresar correctamente: tiempo completo para degradacion de enlace");
                    return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                            build();
                }

                if (tt.getTime0() == null || tt.getTime0().trim().length() == 0) {
                    resp.put("log", "Ingresar correctamente: Tiempo Inicial");
                    return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                            build();
                }
                if (tt.getTcompleto().trim().toLowerCase().equals("no")) {
                    if (tt.getTime1() == null || tt.getTime1().trim().length() == 0) {
                        resp.put("log", "Ingresar correctamente: Tiempo cargado");
                        return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                                build();
                    }
                }
                if (tt.getTime2() == null || tt.getTime2().trim().length() == 0) {
                    tt.setTmins(0l);
                    tt.setAbierto(0);//ABIERTO
                    tt.setTime2(null);
                } else {
                    tt.setAbierto(1);//CERRADO
                }

            }
        }
        JSONArray lista2 = null;
        //PROCESAMIENTO
        Conexion con = null;
        try {
            con = new Conexion();
            JSONParser parser = new JSONParser();
            UsuarioFacade consultaFacade2 = new UsuarioFacade(con);
            lista2 = consultaFacade2.busquedaPermisos(dato.getUsername());
            if (lista2 != null) {
                JSONObject obj = (JSONObject) parser.parse(lista2.get(0).toString());

            }

            TicketFacade consultaFacade = new TicketFacade(con);

            for (Ticket tt : dato.getTickets()) {
                if (consultaFacade.existeEvento(tt)) {
                    resp.put("log", "Existe un evento ya ingresado");
                    return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                            build();
                }
            }
            boolean b = consultaFacade.actualizarTTHeader(dato);
            con.closeConnection();

            if (!b) {
                resp.put("log", "No se pudo realizar esta accion");
                return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                        build();
            }
        } catch (Exception e) {
            System.out.println(this.getClass().toString() + " " + e.getMessage());
        } finally {
            con.closeConnection();
        }

        resp.put("log", "consulta exitosa");
        return Response.status(Response.Status.OK).entity(resp.toString()).
                build();
    }

    @POST
    @Path("/download")
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")

    public Response download(@Context HttpServletRequest request, ec.pacifico.apinvent.entity.Ticket dato) {
        //DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //FileWriter fileWriter = null;
        //Date date = new Date();

        if (dato.getTime0() == null) {
            System.out.println(this.getClass().toString() + ".createExcel ");
            return Response.status(Response.Status.CONFLICT).entity("Ingresar Rango de Fecha").build();
        }

        if (dato.getTime2() == null) {
            System.out.println(this.getClass().toString() + ".createExcel ");
            return Response.status(Response.Status.CONFLICT).entity("Ingresar Rango de Fecha").build();
        }
        Conexion con = null;
        String fileName = "Tickets.xlsx";
        String path = "../temp/Inventario" + fileName;

        try {
            //fileWriter = new FileWriter("MainReport_" + dateFormat.format(date) + ".csv");
            //fileWriter.append(csvService.mainReport(dateFormat.parse(param.getStartDate()), dateFormat.parse(param.getEndDate())));
            //fileWriter.flush();
            //fileWriter.close();
            con = new Conexion();
            TicketFacade consultaFacade = new TicketFacade(con);
            consultaFacade.createExcel(dato, path);

            //ResponseBuilder response = Response.ok((Object) fileWriter);
            //response.header("Content-Disposition", "attachment; filename=\"MainReport_" + dateFormat.format(date) + ".csv\"");
            FileInputStream input = new FileInputStream(new File(path));

            return Response.status(Response.Status.OK)//.type("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                    .entity((Object) input)
                    .header("Content-Disposition", "attachment; filename=")
                    .build();
        } catch (Exception e) {
            System.out.println(this.getClass().toString() + ".createExcel " + e.getMessage());
            return Response.status(Response.Status.CONFLICT).header("totalresultados", "No se pudo crear excel").build();

        } finally {
            con.closeConnection();
        }
    }

}
