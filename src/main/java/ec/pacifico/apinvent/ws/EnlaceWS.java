/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.pacifico.apinvent.ws;

import ec.pacifico.apinvent.facade.Conexion;
import ec.pacifico.apinvent.facade.EnlaceFacade;
import ec.pacifico.apinvent.facade.UsuarioFacade;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
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
@Path("enlace")
public class EnlaceWS {

    @POST
    @Path("list")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response list(@Context HttpServletRequest request, ec.pacifico.apinvent.entity.Enlace dato) {
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
            EnlaceFacade consultaFacade = new EnlaceFacade(con);
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
    public Response porId(@Context HttpServletRequest request, ec.pacifico.apinvent.entity.Enlace dato) {
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
            EnlaceFacade consultaFacade = new EnlaceFacade(con);
            lista = consultaFacade.busquedaId(dato);
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
    @Path("nombre")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response porNombre(@Context HttpServletRequest request, ec.pacifico.apinvent.entity.Enlace dato) {
        JSONObject resp = new JSONObject();
        if (request.getContentType().isEmpty() || request.getContentType() == null || request.getContentType().isBlank()
                || !request.getContentType().contains("application/json") || request.getContentLength() == 0) {
            resp.put("log", "No ha enviado datos");
            resp.put("info", new JSONArray());
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }
        //VERIFICAR DATOS  
        if (dato.getAgencia() == null) {
            resp.put("log", "Ingresar correctamente: nombre");
            resp.put("info", new JSONArray());
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }
        dato.setEstado(1);
        dato.setPageIndex(1);
        dato.setPageSize(15);
        //PROCESAMIENTO
        JSONArray lista = null;
        Conexion con = null;
        try {
            con = new Conexion();
            EnlaceFacade consultaFacade = new EnlaceFacade(con);
            lista = consultaFacade.busquedaAgenciaEnlace(dato);
            con.closeConnection();
        } catch (Exception e) {
            System.out.println(this.getClass().toString() + " " + e.getMessage());
        } finally {
            con.closeConnection();
        }
        if (lista == null) { //|| size==0) {
            resp.put("log", "La consulta no retorna informacion");
            resp.put("info", new JSONArray());
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
    public Response crear(@Context HttpServletRequest request, ec.pacifico.apinvent.entity.Enlace dato) {
        JSONObject resp = new JSONObject();
        if (request.getContentType().isEmpty() || request.getContentType() == null || request.getContentType().isBlank()
                || !request.getContentType().contains("application/json") || request.getContentLength() == 0) {
            resp.put("log", "No ha enviado datos");
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }
        //VERIFICAR DATOS  
        if (dato.getTunel() == null || dato.getTunel().trim().length() == 0) {
            resp.put("log", "Ingresar correctamente: Ip del tunnel");
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }
        if (dato.getIdmedio() == null) {
            resp.put("log", "Ingresar correctamente: medio");
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }
        if (dato.getIdpunto() == null) {
            resp.put("log", "Ingresar correctamente: punto");
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }
        if (dato.getBw() == null) {
            resp.put("log", "Ingresar correctamente: bw");
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }
        if (dato.getIdpropiedad() == null) {
            resp.put("log", "Ingresar correctamente: propiedad");
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }
        if (dato.getIdproveedor() == null) {
            resp.put("log", "Ingresar correctamente: proveedor");
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
                if (!(boolean) obj.get("enlaces")) {
                    resp.put("log", "Usuario no tiene permisos, revisar con usuario de Enlaces Outsourcing ");
                    return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                            build();
                }
            }

            EnlaceFacade consultaFacade = new EnlaceFacade(con);
            if (dato.getTunel().trim().length() == 0 || dato.getTunel().trim().equals("N/A")) {
                dato.setTunel("N/A");
            } else {
                if (consultaFacade.busquedaIP(dato) != null) {
                    resp.put("log", "Ya existe direccion de Tunel");
                    return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                            build();
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
    @Path("/download")
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response download(@Context HttpServletRequest request, ec.pacifico.apinvent.entity.Enlace dato) {
        //DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //FileWriter fileWriter = null;
        //Date date = new Date();
        Conexion con = null;
        String fileName = "enlaces.xlsx";
        String path = "../temp/Inventario" + fileName;

        try {
            //fileWriter = new FileWriter("MainReport_" + dateFormat.format(date) + ".csv");
            //fileWriter.append(csvService.mainReport(dateFormat.parse(param.getStartDate()), dateFormat.parse(param.getEndDate())));
            //fileWriter.flush();
            //fileWriter.close();
            con = new Conexion();
            EnlaceFacade consultaFacade = new EnlaceFacade(con);
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
            return Response.status(Response.Status.CONFLICT).entity("No se pudo crear excel").build();
        } finally {
            con.closeConnection();
        }
    }

    @POST
    @Path("actualizar")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response actualizar(@Context HttpServletRequest request, ec.pacifico.apinvent.entity.Enlace dato) {
        JSONObject resp = new JSONObject();
        if (request.getContentType().isEmpty() || request.getContentType() == null || request.getContentType().isBlank()
                || !request.getContentType().contains("application/json") || request.getContentLength() == 0) {
            resp.put("log", "No ha enviado datos");
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }
        //VERIFICAR DATOS  
        if (dato.getTunel() == null || dato.getTunel().trim().length() == 0) {
            resp.put("log", "Ingresar correctamente: Ip del tunnel");
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }
        if (dato.getId() == null) {
            resp.put("log", "Ingresar correctamente: id");
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }
        if (dato.getIdmedio() == null) {
            resp.put("log", "Ingresar correctamente: medio");
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }
        if (dato.getIdpunto() == null) {
            resp.put("log", "Ingresar correctamente: punto");
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }
        if (dato.getBw() == null) {
            resp.put("log", "Ingresar correctamente: bw");
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }
        if (dato.getIdpropiedad() == null) {
            resp.put("log", "Ingresar correctamente: propiedad");
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }
        if (dato.getIdproveedor() == null) {
            resp.put("log", "Ingresar correctamente: proveedor");
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }

        if (dato.getIdbaja() != null) { //ACTUALMENTE ESTA DE BAJA
            if (dato.getEstado() != 2) {//INGRESO O ELIMINADO
                //dato.setIdbaja(null);
                //dato.setFechabajai(null);
                //dato.setFechabaja(null);
                //ACTUALIZAR FECHA DEL ACTUAL ID DE BAJA
                if (dato.getFechainicio() == null) {
                    resp.put("log", "Ingresar correctamente: Fecha de Inicio de Baja");
                    return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                            build();
                }
                if (dato.getFechabaja() == null) {
                    resp.put("log", "Ingresar correctamente: Fecha de fin de Baja");
                    return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                            build();
                }
                Date baja = new Date();
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    baja = dateFormat.parse(dato.getFechabaja());
                } catch (Exception e) {
                    resp.put("log", "Ingresar correctamente: Fecha de Fin de Baja");
                    return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                            build();
                }
                Date today = new Date();
                if (baja.after(today)) {
                    resp.put("log", "Ingresar correctamente: No se puede remover de Baja por  la fecha de fin");
                    return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                            build();
                }
            } else {//SIGUE EN BAJA
                if (dato.getFechainicio() == null) {
                    resp.put("log", "Ingresar correctamente: Fecha de Inicio de Baja");
                    return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                            build();
                }
            }
        } else {//NO ESTA DE BAJA
            if (dato.getEstado() != 2) {//INGRESO O ELIMINADO
                dato.setIdbaja(null);
            } else {//INGRESAR NUEVO BAJA
                if (dato.getFechainicio() == null) {
                    resp.put("log", "Ingresar correctamente: Fecha de Inicio de Baja");
                    return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                            build();
                }
                if (dato.getFechabaja() == null) {
                    resp.put("log", "Ingresar correctamente: Fecha de fin de Baja");
                    return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                            build();
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
                if (!(boolean) obj.get("enlaces")) {
                    resp.put("log", "Usuario no tiene permisos, revisar con usuario de Enlaces Outsourcing ");
                    return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                            build();
                }
            }
            EnlaceFacade consultaFacade = new EnlaceFacade(con);
            if (dato.getTunel().trim().equals("N/A") || dato.getTunel().trim().contains("-")) {
                dato.setTunel("N/A");
            } else {
                if (dato.getEstado() == 1) {
                    JSONArray bi = consultaFacade.busquedaIP(dato);
                    if (bi != null) {
                        JSONParser parser2 = new JSONParser();
                        JSONObject json = (JSONObject) parser2.parse(bi.get(0).toString());
                        if (dato.getId().toString().equals(json.get("id").toString())) {
                        } else {
                            resp.put("log", "Ya existe direccion de Tunel");
                            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                                    build();
                        }
                    }
                }
            }

            if (consultaFacade.busquedaId(dato) == null) {
                resp.put("log", "No existe");
                return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                        build();
            }
            consultaFacade.actualizar(dato);
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
    @Path("logs")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response logs(@Context HttpServletRequest request, ec.pacifico.apinvent.entity.Enlace dato) {
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
            EnlaceFacade consultaFacade = new EnlaceFacade(con);
            lista = consultaFacade.obtenerListadoLogs(dato);
            size = consultaFacade.obtenerListadoLogsSize(dato);
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
    @Path("baja")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response baja(@Context HttpServletRequest request, ec.pacifico.apinvent.entity.Enlace dato) {
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
            EnlaceFacade consultaFacade = new EnlaceFacade(con);
            lista = consultaFacade.obtenerListadoBaja(dato);
            size = consultaFacade.obtenerListadoBajaSize(dato);
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

}
