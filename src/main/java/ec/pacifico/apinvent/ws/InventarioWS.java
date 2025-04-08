/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.pacifico.apinvent.ws;

import ec.pacifico.apinvent.entity.Inventario;
import ec.pacifico.apinvent.facade.Conexion;
import ec.pacifico.apinvent.facade.EnlaceFacade;
import ec.pacifico.apinvent.facade.InventarioFacade;
import ec.pacifico.apinvent.facade.UsuarioFacade;
import java.io.File;
import java.io.FileInputStream;
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
@Path("inventario")
public class InventarioWS {

    @POST
    @Path("list")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response list(@Context HttpServletRequest request, Inventario dato) {
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
        if (dato.getSerie() == null) {
            dato.setSerie("");
        }
        if (dato.getUbicacion() != null) {
            dato.setUbicacion(dato.getUbicacion().toUpperCase());
            //solo es numero
            if (!dato.getUbicacion().contains("P") && !dato.getUbicacion().contains("R")) {
                resp.put("log", "Ingresar correctamente: filtro de ubicacion ej:P1-R1,P1,R1");
                resp.put("info", new JSONArray());
                return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                        header("totalpaginas", 0).
                        header("totalresultados", 0).
                        build();
            }
            //tiene -
            if (dato.getUbicacion().contains("-")) {
                String l[] = dato.getUbicacion().split("-");
                for (int i = 0; i < l.length; i++) {
                    if (l[i].contains("P") && !l[i].contains("R")) {
                        String p[] = l[i].split("P");
                        if (p.length > 1) {
                            for (int j = 0; j < p[1].toCharArray().length; j++) {
                                if (!Character.isDigit(p[1].charAt(j))) {
                                    resp.put("log", "Ingresar correctamente: filtro de ubicacion ej:P1-R1,P1,R1");
                                    resp.put("info", new JSONArray());
                                    return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                                            header("totalpaginas", 0).
                                            header("totalresultados", 0).
                                            build();
                                }
                            }
                            dato.setPiso(Integer.valueOf(p[1]));
                        } else {
                            resp.put("log", "Ingresar correctamente: filtro de ubicacion ej:P1-R1,P1,R1");
                            resp.put("info", new JSONArray());
                            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                                    header("totalpaginas", 0).
                                    header("totalresultados", 0).
                                    build();
                        }

                    } else if (l[i].contains("R") && !l[i].contains("P")) {
                        String r[] = l[i].split("R");
                        if (r.length > 1) {
                            for (int j = 0; j < r[1].toCharArray().length; j++) {
                                if (!Character.isDigit(r[1].charAt(j))) {
                                    resp.put("log", "Ingresar correctamente: filtro de ubicacion ej:P1-R1,P1,R1");
                                    resp.put("info", new JSONArray());
                                    return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                                            header("totalpaginas", 0).
                                            header("totalresultados", 0).
                                            build();
                                }
                            }
                            dato.setRack(Integer.valueOf(r[1]));
                        } else {
                            resp.put("log", "Ingresar correctamente: filtro de ubicacion ej:P1-R1,P1,R1");
                            resp.put("info", new JSONArray());
                            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                                    header("totalpaginas", 0).
                                    header("totalresultados", 0).
                                    build();
                        }
                    } else {
                        resp.put("log", "Ingresar correctamente: filtro de ubicacion ej:P1-R1,P1,R1");
                        resp.put("info", new JSONArray());
                        return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                                header("totalpaginas", 0).
                                header("totalresultados", 0).
                                build();
                    }
                }
                //no tiene -
            } else {
                // para PX
                if (dato.getUbicacion().contains("P") && !dato.getUbicacion().contains("R")) {
                    String p[] = dato.getUbicacion().split("P");
                    if (p.length > 1) {
                        for (int j = 0; j < p[1].toCharArray().length; j++) {
                            if (!Character.isDigit(p[1].charAt(j))) {
                                resp.put("log", "Ingresar correctamente: filtro de ubicacion ej:P1-R1,P1,R1");
                                resp.put("info", new JSONArray());
                                return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                                        header("totalpaginas", 0).
                                        header("totalresultados", 0).
                                        build();
                            }
                        }
                        dato.setPiso(Integer.valueOf(p[1]));
                    } else {
                        resp.put("log", "Ingresar correctamente: filtro de ubicacion ej:P1-R1,P1,R1");
                        resp.put("info", new JSONArray());
                        return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                                header("totalpaginas", 0).
                                header("totalresultados", 0).
                                build();
                    }
                    //PARA RX
                } else if (dato.getUbicacion().contains("R") && !dato.getUbicacion().contains("P")) {
                    String r[] = dato.getUbicacion().split("R");
                    if (r.length > 1) {
                        for (int j = 0; j < r[1].toCharArray().length; j++) {
                            if (!Character.isDigit(r[1].charAt(j))) {
                                resp.put("log", "Ingresar correctamente: filtro de ubicacion ej:P1-R1,P1,R1");
                                resp.put("info", new JSONArray());
                                return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                                        header("totalpaginas", 0).
                                        header("totalresultados", 0).
                                        build();
                            }
                        }
                        dato.setRack(Integer.valueOf(r[1]));
                    } else {
                        resp.put("log", "Ingresar correctamente: filtro de ubicacion ej:P1-R1,P1,R1");
                        resp.put("info", new JSONArray());
                        return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                                header("totalpaginas", 0).
                                header("totalresultados", 0).
                                build();
                    }
                    //PARA PXRX
                } else {
                    resp.put("log", "Ingresar correctamente: filtro de ubicacion ej:P1-R1,P1,R1");
                    resp.put("info", new JSONArray());
                    return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                            header("totalpaginas", 0).
                            header("totalresultados", 0).
                            build();
                }
            }

        }
        /*else {
            switch (dato.getSerie().trim().length()) {
                case 0:
                    dato.setSerie("");
                    break;
                case 1:
                    dato.setSerie(dato.getSerie().trim().toUpperCase());
                    break;
                default:
                    String name = dato.getSerie();
                    name = name.trim();
                    String firstLtr = name.substring(0, 1);
                    String restLtrs = name.substring(1, name.length());
                    firstLtr = firstLtr.toUpperCase();
                    name = firstLtr + restLtrs;
                    dato.setSerie(name);
                    break;
            }
        }*/

        //PROCESAMIENTO
        JSONArray lista = null;
        int size = 0;
        Conexion con = null;
        try {
            con = new Conexion();
            InventarioFacade consultaFacade = new InventarioFacade(con);
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
    public Response porId(@Context HttpServletRequest request, Inventario dato) {
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
            InventarioFacade consultaFacade = new InventarioFacade(con);
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
    public Response porNombre(@Context HttpServletRequest request, Inventario dato) {
        JSONObject resp = new JSONObject();
        if (request.getContentType().isEmpty() || request.getContentType() == null || request.getContentType().isBlank()
                || !request.getContentType().contains("application/json") || request.getContentLength() == 0) {
            resp.put("log", "No ha enviado datos");
            resp.put("info", new JSONArray());
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }
        //VERIFICAR DATOS  
        if (dato.getSerie() == null) {
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
            InventarioFacade consultaFacade = new InventarioFacade(con);
            lista = consultaFacade.busquedaNombre(dato);
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
    public Response crear(@Context HttpServletRequest request, Inventario dato) {
        JSONObject resp = new JSONObject();
        if (request.getContentType().isEmpty() || request.getContentType() == null || request.getContentType().isBlank()
                || !request.getContentType().contains("application/json") || request.getContentLength() == 0) {
            resp.put("log", "No ha enviado datos");
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }
        //VERIFICAR DATOS  
        if (dato.getNombre() == null || dato.getNombre().trim().length() == 0) {
            resp.put("log", "Ingresar correctamente: nombre");
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }

        if (dato.getSerie() == null || dato.getSerie().trim().length() == 0) {
            resp.put("log", "Ingresar correctamente: serie");
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }

        if (dato.getAgencia() == null) {
            resp.put("log", "Ingresar correctamente: agencia");
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }

        if (dato.getCritico() == null) {
            resp.put("log", "Ingresar correctamente: critico");
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }

        if (dato.getIdAmbiente() == null) {
            resp.put("log", "Ingresar correctamente: ambiente");
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }
        if (dato.getIdModelo() == null) {
            resp.put("log", "Ingresar correctamente: modelo");
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }

        if (dato.getIdPropietario() == null) {
            resp.put("log", "Ingresar correctamente: propietario");
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }

        if (dato.getIdAmbiente() != null) {
            if (dato.getIdAmbiente() == 3) {
                dato.setUtil(1);
            }
            if (dato.getIdAmbiente() == 4) {
                dato.setUtil(0);
                dato.setOpmger(null);
                dato.setIdOrion(null);
                dato.setCritico(null);
                dato.setBpac(null);
                dato.setPiso(null);
                dato.setRack(null);
            }
            if (dato.getIdAmbiente() == 5) {
                dato.setUtil(null);
                dato.setOpmger(null);
                dato.setIdOrion(null);
                dato.setCritico(null);
                dato.setBpac(null);
                dato.setPiso(null);
                dato.setRack(null);
            }

            if (dato.getIdAmbiente() == 1) {
                dato.setOpmger(0);
                dato.setBpac(1);
                dato.setUtil(1);
                dato.setIdOrion(null);
            }
            if (dato.getIdAmbiente() == 2) {

                if (dato.getMarca().trim().contains("CISCO")) {
                    dato.setOpmger(1);
                    dato.setBpac(1);
                    dato.setUtil(1);

                    if (dato.getIdOrion() == null) {
                        resp.put("log", "Ingresar correctamente: Carpeta Orion");
                        return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                                build();
                    }
                } else {
                    dato.setUtil(1);
                }

            }

            if (dato.getIdAmbiente() != 4 && dato.getIdAmbiente() != 5) {
                if (dato.getPiso() == null || dato.getRack() == null) {
                    resp.put("log", "Ingresar correctamente: Piso y Rack");
                    return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                            build();
                }
            }if (dato.getIdAmbiente() == 2 || dato.getIdAmbiente() == 3) {
                if (dato.getIdOrion()== null) {
                    resp.put("log", "Ingresar correctamente: Orion");
                    return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                            build();
                }
            }

        }

        if (dato.getPiso() != null && dato.getRack() == null) {
            resp.put("log", "Ingresar correctamente: Rack");
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }
        if (dato.getPiso() == null && dato.getRack() != null) {
            resp.put("log", "Ingresar correctamente: Piso");
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }
        if (dato.getPiso() != null && dato.getPiso() <= 0) {
            resp.put("log", "Ingresar correctamente: Piso debe ser mayor a cero");
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }
        if (dato.getRack() != null && dato.getRack() <= 0) {
            resp.put("log", "Ingresar correctamente: Rack debe ser mayor a cero");
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
            JSONObject obj = (JSONObject) parser.parse(lista2.get(0).toString());
            if (lista2 != null) {
                if (!(boolean) obj.get("informacion")) {
                    resp.put("log", "Usuario no tiene permisos, revisar con usuario de Inventario Outsourcing ");
                    return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                            build();
                }
            }

            InventarioFacade consultaFacade = new InventarioFacade(con);
            if (consultaFacade.busquedaNombre(dato) != null) {
                resp.put("log", "Ya existe serie");
                return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                        build();
            }
            if (dato.getInventario() != null) {
                if (consultaFacade.busquedaNombreInventario(dato) != null) {
                    resp.put("log", "Ya existe inventario");
                    return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                            build();
                }
            }
            boolean tf = consultaFacade.crear(dato);
            if (!tf) {
                resp.put("log", "No se pudo crear nuevo inventario");
                return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                        build();
            }
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
    public Response download(@Context HttpServletRequest request, Inventario dato) {
        //DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //FileWriter fileWriter = null;
        //Date date = new Date();
        Conexion con = null;
        String fileName = "Equipos.xlsx";
        String path = "../temp/Inventario" + fileName;

        try {
            //fileWriter = new FileWriter("MainReport_" + dateFormat.format(date) + ".csv");
            //fileWriter.append(csvService.mainReport(dateFormat.parse(param.getStartDate()), dateFormat.parse(param.getEndDate())));
            //fileWriter.flush();
            //fileWriter.close();
            con = new Conexion();
            InventarioFacade consultaFacade = new InventarioFacade(con);
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
    public Response actualizar(@Context HttpServletRequest request, Inventario dato) {
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
        if (dato.getSerie() == null || dato.getSerie().trim().length() == 0) {
            resp.put("log", "Ingresar correctamente: serie");
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }
        if (dato.getAgencia() == null) {
            resp.put("log", "Ingresar correctamente: agencia");
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }
        if (dato.getIdAmbiente() == null) {
            resp.put("log", "Ingresar correctamente: ambiente");
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }
        if (dato.getIdModelo() == null) {
            resp.put("log", "Ingresar correctamente: modelo");
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }

        if (dato.getIdPropietario() == null) {
            resp.put("log", "Ingresar correctamente: propietario");
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }

        if (dato.getInventario() != null) {
            dato.setInventario(dato.getInventario().trim());
        }

        if (dato.getNombre() != null) {
            dato.setNombre(dato.getNombre().trim());
        }

        if (dato.getOpmger() == 0) {
            dato.setOpmger(null);
        }

        if (dato.getOpmger() != null) {
            if (dato.getOpmger() == 1) {
                dato.setBpac(1);
            } else {
                dato.setBpac(0);
            }
        }

        if (dato.getIdAmbiente() != null) {
            if (dato.getIdAmbiente() == 3) {

                dato.setUtil(1);
            }
            if (dato.getIdAmbiente() == 4) {
                dato.setUtil(0);
                dato.setOpmger(null);
                dato.setIdOrion(null);
                dato.setCritico(null);
                dato.setBpac(null);
                dato.setPiso(null);
                dato.setRack(null);
            }
            if (dato.getIdAmbiente() == 5) {
                dato.setUtil(null);
                dato.setOpmger(null);
                dato.setIdOrion(null);
                dato.setCritico(null);
                dato.setBpac(null);
                dato.setPiso(null);
                dato.setRack(null);
            }
            if (dato.getIdAmbiente() == 1) {
                dato.setOpmger(0);
                dato.setBpac(1);
                dato.setUtil(1);
                dato.setIdOrion(null);
            }
            if (dato.getIdAmbiente() == 2) {

                if (dato.getMarca().trim().contains("CISCO")) {
                    dato.setOpmger(1);
                    dato.setBpac(1);
                    dato.setUtil(1);

                    if (dato.getIdOrion() == null) {
                        resp.put("log", "Ingresar correctamente: Carpeta Orion");
                        return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                                build();
                    }
                } else {
                    dato.setUtil(1);
                }

            }
            
            if (dato.getIdAmbiente() != 4 && dato.getIdAmbiente() != 5) {
                if (dato.getPiso() == null || dato.getRack() == null) {
                    resp.put("log", "Ingresar correctamente: Piso y Rack");
                    return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                            build();
                }
            }if (dato.getIdAmbiente() == 2 || dato.getIdAmbiente() == 3) {
                if (dato.getIdOrion()== null) {
                    resp.put("log", "Ingresar correctamente: Orion");
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
                if ((!(boolean) obj.get("eliminar")) && dato.getEstado() == 0) {
                    resp.put("log", "Usuario no puede desactivar inventario, revisar con usuario de Admin Banco ");
                    return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                            build();
                }

                if ((!(boolean) obj.get("informacion")) && dato.getEstado() == 1) {
                    resp.put("log", "Usuario no tiene permisos, revisar con usuario de Inventario Outsourcing ");
                    return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                            build();
                }
            }
            InventarioFacade consultaFacade = new InventarioFacade(con);
            if (consultaFacade.busquedaId(dato) == null) {
                resp.put("log", "No existe");
                return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                        build();
            }
            if (dato.getSerie() != null) {
                JSONArray bni = consultaFacade.busquedaNombre(dato);
                if (bni != null) {
                    JSONParser parser2 = new JSONParser();
                    JSONObject json = (JSONObject) parser2.parse(bni.get(0).toString());
                    if (dato.getId().toString().equals(json.get("id").toString())) {
                    } else {
                        resp.put("log", "Ya existe serie");
                        return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                                build();
                    }

                }
            }
            if (dato.getInventario() != null) {
                JSONArray bni = consultaFacade.busquedaNombreInventario(dato);
                if (bni != null) {
                    JSONParser parser2 = new JSONParser();
                    JSONObject json = (JSONObject) parser2.parse(bni.get(0).toString());
                    if (dato.getId().toString().equals(json.get("id").toString())) {
                    } else {
                        resp.put("log", "Ya existe inventario");
                        return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                                build();
                    }

                }
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
    @Path("dashboard")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response dashboard(@Context HttpServletRequest request, Inventario dato) {
        JSONObject resp = new JSONObject();
        if (request.getContentType().isEmpty() || request.getContentType() == null || request.getContentType().isBlank()
                || !request.getContentType().contains("application/json") || request.getContentLength() == 0) {
            resp.put("log", "No ha enviado datos");
            resp.put("total", 0);
            resp.put("tfecha", 0);
            resp.put("tfechaprox", 0);
            resp.put("tcritico", 0);
            resp.put("tbpac", 0);
            resp.put("topmger", 0);
            resp.put("tutil", 0);
            resp.put("tufecha", 0);
            resp.put("tufechaprox", 0);
            resp.put("tbodega", 0);
            resp.put("tbfecha", 0);
            resp.put("tbfechaprox", 0);
            resp.put("atmint",0);
            resp.put("totalenlacesprov", 0);
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }
        //PROCESAMIENTO
        JSONObject total = null;
        JSONObject tfecha = null;
        JSONObject tfechaprox = null;
        //int tcritico = 0;
        //int tbpac = 0;
        //int topmger = 0;
        JSONObject tutil = null;
        JSONObject tufecha = null;
        JSONObject tufechaprox = null;
        JSONObject tbodega = null;
        JSONObject tbfecha = null;
        JSONObject tbfechaprox = null;
        JSONObject atmint = null;
        JSONObject totalenlacesprov = null;
        Conexion con = null;
        try {
            con = new Conexion();
            InventarioFacade consultaFacade = new InventarioFacade(con);
            EnlaceFacade consultaEnlaceFac = new EnlaceFacade(con);
            total = consultaFacade.dashboardTotal();
            tfecha = consultaFacade.dashboardTotalFecha();
            tfechaprox = consultaFacade.dashboardTotalFechaProximo();
            //tcritico = consultaFacade.dashboardTotalCritico();
            //tbpac = consultaFacade.dashboardTotalBpac();
            //topmger = consultaFacade.dashboardTotalOpmger();
            tutil = consultaFacade.dashboardTotalUtil();
            tufecha = consultaFacade.dashboardUtilFecha();
            tufechaprox = consultaFacade.dashboardUtilFechaProximo();
            tbodega = consultaFacade.dashboardBodega();
            tbfecha = consultaFacade.dashboardBodegaFecha();
            tbfechaprox = consultaFacade.dashboardBodegaFechaProximo();
            atmint = consultaEnlaceFac.dashboardATMINT();
            totalenlacesprov = consultaEnlaceFac.dashboardCantEnlaceProveedor();
            con.closeConnection();
        } catch (Exception e) {
            System.out.println(this.getClass().toString() + " " + e.getMessage());
        } finally {
            con.closeConnection();
        }

        resp.put("log", "consulta exitosa");
        resp.put("total", total);
        resp.put("tfecha", tfecha);
        resp.put("tfechaprox", tfechaprox);
        //resp.put("tcritico", tcritico);
        //resp.put("tbpac", tbpac);
        //resp.put("topmger", topmger);
        resp.put("tutil", tutil);
        resp.put("tufecha", tufecha);
        resp.put("tufechaprox", tufechaprox);
        resp.put("tbodega", tbodega);
        resp.put("tbfecha", tbfecha);
        resp.put("tbfechaprox", tbfechaprox);
        resp.put("atmint", atmint);
        resp.put("totalenlacesprov", totalenlacesprov);
        return Response.status(Response.Status.OK).entity(resp.toString()).
                build();
    }

    @POST
    @Path("logs")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response logs(@Context HttpServletRequest request, Inventario dato) {
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
            InventarioFacade consultaFacade = new InventarioFacade(con);
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

}
