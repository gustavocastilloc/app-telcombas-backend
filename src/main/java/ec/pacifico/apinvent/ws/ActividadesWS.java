/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.pacifico.apinvent.ws;

import ec.pacifico.apinvent.entity.ActHeader;
import ec.pacifico.apinvent.entity.Actividades;
import ec.pacifico.apinvent.facade.ActividadesFacade;
import ec.pacifico.apinvent.facade.Conexion;
import ec.pacifico.apinvent.facade.UsuarioFacade;
import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDateTime;
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
@Path("actividades")
public class ActividadesWS {

    @POST
    @Path("list")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response list(@Context HttpServletRequest request, Actividades dato) {
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

        if (dato.getTime0() != null && dato.getTime2() == null) {
            resp.put("log", "Ingresar correctamente: rango de tiempo");
            resp.put("info", new JSONArray());
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    header("totalpaginas", 0).
                    header("totalresultados", 0).
                    build();
        }
        if (dato.getTime0() == null && dato.getTime2() != null) {
            resp.put("log", "Ingresar correctamente: rango de tiempo");
            resp.put("info", new JSONArray());
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    header("totalpaginas", 0).
                    header("totalresultados", 0).
                    build();
        }
        if (dato.getUsuario() != null && dato.getUsuario().trim().length() == 0) {
            dato.setUsuario(null);
        }

        //PROCESAMIENTO
        JSONArray lista = null;
        int size = 0;
        Conexion con = null;
        try {
            con = new Conexion();
            ActividadesFacade consultaFacade = new ActividadesFacade(con);
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
            return Response.status(Response.Status.OK).entity(resp.toString()).
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
    @Path("listacts")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response listacts(@Context HttpServletRequest request, Actividades dato) {
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
        
        if (dato.getUsuario() != null && dato.getUsuario().trim().length() == 0) {
            dato.setUsuario(null);
        }

        //PROCESAMIENTO
        JSONArray lista = null;
        int size = 0;
        Conexion con = null;
        try {
            con = new Conexion();
            ActividadesFacade consultaFacade = new ActividadesFacade(con);
            lista = consultaFacade.obtenerListado2(dato);
            size = consultaFacade.obtenerListadoSize2(dato);
            con.closeConnection();
        } catch (Exception e) {
            System.out.println(this.getClass().toString() + " " + e.getMessage());
        } finally {
            con.closeConnection();
        }
        if (lista == null) { //|| size==0) {
            resp.put("log", "La consulta no retorna informacion");
            resp.put("info", new JSONArray()); //{}
            return Response.status(Response.Status.OK).entity(resp.toString()).
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
    public Response porIdAct(@Context HttpServletRequest request, Actividades dato) {
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
            ActividadesFacade consultaFacade = new ActividadesFacade(con);
            lista = consultaFacade.busquedaIdHeader(dato);
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
    public Response crear(@Context HttpServletRequest request, ActHeader dato) {
        JSONObject resp = new JSONObject();
        if (request.getContentType().isEmpty() || request.getContentType() == null || request.getContentType().isBlank()
                || !request.getContentType().contains("application/json") || request.getContentLength() == 0) {
            resp.put("log", "No ha enviado datos");
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }

        //VERIFICAR DATOS  
        if (dato.getActividad() == null) {
            resp.put("log", "Ingresar correctamente: Actividad");
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }

        if (dato.getComentario() == null || dato.getComentario().trim().length() == 0) {
            resp.put("log", "Ingresar correctamente: Comentario");
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }

        if (dato.getSubactividades() == null) {
            resp.put("log", "Ingresar correctamente: Subactividades");
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }
        if (dato.getSubactividades() != null && dato.getSubactividades().size() == 0) {
            resp.put("log", "Ingresar correctamente: Subactividades");
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }
        
        dato.setComentario(dato.getComentario().replace("'", " "));
        dato.setCheck(1);

        for (Actividades act : dato.getSubactividades()) {
            act.setAbierto(1);
            if (act.getFecha() != null && act.getFecha().trim().length() == 0) {
                act.setFecha(null);
                act.setFecha2(null);
            }
            if (act.getSubactividad() == null) {
                resp.put("log", "Ingresar correctamente: Subactividad");
                return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                        build();
            }

            if (act.getFecha() != null && act.getMins() == null) {
                resp.put("log", "Ingresar correctamente: Minutos");
                return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                        build();
            }
            if (act.getFecha() != null && act.getMins() == 0) {
                resp.put("log", "Ingresar correctamente: Minutos");
                return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                        build();
            }

            if (act.getUsuario() != null) {
                if (act.getFecha() == null || act.getFecha().trim().length() == 0) {
                    resp.put("log", "Ingresar correctamente: Fecha");
                    return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                            build();
                }
            } else {
                act.setAbierto(0);
            }
            if (act.getFecha() == null) {
                act.setAbierto(0);
            }
            if (act.getMins() != null && act.getMins() == 0) {
                act.setAbierto(0);
            }

            if (act.getFecha() != null) {
                LocalDateTime endActual = LocalDateTime.parse((CharSequence) act.getFecha().replace(" ", "T"));
                LocalDateTime endActualp = endActual.plusMinutes(Long.parseLong(act.getMins().toString()));
                String fecha2 = endActualp.toString();
                act.setFecha2(fecha2);
            }

            if (act.getAbierto() == 0) {
                dato.setCheck(0);
            }

            if (act.getSubactividad() == 5l) {
                dato.setAbbanco(1);
            } else {
                dato.setAbbanco(0);
            }

        }
        for (Actividades act1 : dato.getSubactividades()) {
            if (act1.getUsuario() != null && act1.getFecha() != null) {
                LocalDateTime fechai1 = LocalDateTime.parse((CharSequence) act1.getFecha().replace(" ", "T"));
                for (Actividades act2 : dato.getSubactividades()) {
                    if (act2.getUsuario() != null && act2.getFecha() != null) {
                        LocalDateTime fechai2 = LocalDateTime.parse((CharSequence) act2.getFecha().replace(" ", "T"));
                        LocalDateTime fechae2 = LocalDateTime.parse((CharSequence) act2.getFecha2().replace(" ", "T"));
                        if (act1.getSubactividad().compareTo(act2.getSubactividad()) != 0 && act1.getUsuario().equals(act2.getUsuario())) {
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
        JSONArray lista2 = null;
        //PROCESAMIENTO
        Conexion con = null;
        try {
            con = new Conexion();
            ActividadesFacade consultaFacade = new ActividadesFacade(con);
            for (Actividades act : dato.getSubactividades()) {
                if (act.getFecha() != null) {
                    if (consultaFacade.existeEvento(act)) {
                        resp.put("log", "Existe una actividad ya ingresado");
                        return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                                build();
                    }
                }

            }
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

    @POST
    @Path("actualizarheader")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response actualizarHeader(@Context HttpServletRequest request, ActHeader dato) {
        JSONObject resp = new JSONObject();
        if (request.getContentType().isEmpty() || request.getContentType() == null || request.getContentType().isBlank()
                || !request.getContentType().contains("application/json") || request.getContentLength() == 0) {
            resp.put("log", "No ha enviado datos");
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }
        //VERIFICAR DATOS  
        if (dato.getActividad() == null) {
            resp.put("log", "Ingresar correctamente: Actividad");
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }

        if (dato.getComentario() == null || dato.getComentario().trim().length() == 0) {
            resp.put("log", "Ingresar correctamente: Comentario");
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }

        if (dato.getSubactividades() == null) {
            resp.put("log", "Ingresar correctamente: Subactividades");
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }
        if (dato.getSubactividades() != null && dato.getSubactividades().size() == 0) {
            resp.put("log", "Ingresar correctamente: Subactividades");
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }

        if (dato.getEstado() == null) {
            resp.put("log", "Ingresar correctamente: estado");
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }
        
        dato.setComentario(dato.getComentario().replace("'", " "));

        dato.setCheck(1);
        for (Actividades act : dato.getSubactividades()) {
            if(dato.getEstado()==0){
                act.setEstado(0);
            }
            act.setAbierto(1);
            if (act.getFecha() != null && act.getFecha().trim().length() == 0) {
                act.setFecha(null);
                act.setFecha2(null);
            }
            if (act.getSubactividad() == null) {
                resp.put("log", "Ingresar correctamente: Subactividad");
                return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                        build();
            }

            if (act.getFecha() != null && act.getMins() == null) {
                resp.put("log", "Ingresar correctamente: Minutos");
                return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                        build();
            }
            if (act.getFecha() != null && act.getMins() == 0) {
                resp.put("log", "Ingresar correctamente: Minutos");
                return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                        build();
            }

            if (act.getEstado() == 1) {
                if (act.getUsuario() != null) {
                    if (act.getFecha() == null || act.getFecha().trim().length() == 0) {
                        resp.put("log", "Ingresar correctamente: Fecha");
                        return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                                build();
                    }
                } else {
                    act.setAbierto(0);
                }

                if (act.getFecha() == null) {
                    act.setAbierto(0);
                }

                if (act.getMins() != null && act.getMins() == 0) {
                    act.setAbierto(0);
                }
            }

            if (act.getFecha() != null) {
                LocalDateTime endActual = LocalDateTime.parse((CharSequence) act.getFecha().replace(" ", "T"));
                LocalDateTime endActualp = endActual.plusMinutes(Long.parseLong(act.getMins().toString()));
                String fecha2 = endActualp.toString();
                act.setFecha2(fecha2);
            }

            if (act.getAbierto() == 0) {
                dato.setCheck(0);
            }
            
            if (act.getSubactividad() == 5l) {
                dato.setAbbanco(1);
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
                if ((boolean) obj.get("eliminar") && dato.getAbbanco() == 0) {
                    resp.put("log", "Usuario no puede editar la actividad");
                    return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                            build();
                }

            }

            ActividadesFacade consultaFacade = new ActividadesFacade(con);
            for (Actividades act : dato.getSubactividades()) {
                if (act.getFecha() != null) {
                    if (consultaFacade.existeEvento(act)) {
                        resp.put("log", "Existe una actividad ya ingresado");
                        return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                                build();
                    }
                }

            }
            consultaFacade.actualizarActHeader(dato);
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
    @Path("/download")
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response download(@Context HttpServletRequest request, ec.pacifico.apinvent.entity.Actividades dato) {
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
        String fileName = "Actividades.xlsx";
        String path = "../temp/Inventario" + fileName;

        try {
            con = new Conexion();
            ActividadesFacade consultaFacade = new ActividadesFacade(con);
            consultaFacade.createExcel(dato, path);

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
