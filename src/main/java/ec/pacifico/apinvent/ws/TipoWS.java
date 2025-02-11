/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.pacifico.apinvent.ws;

import ec.pacifico.apinvent.entity.Tipo;
import ec.pacifico.apinvent.facade.TipoFacade;
import ec.pacifico.apinvent.facade.Conexion;
import ec.pacifico.apinvent.facade.UsuarioFacade;
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
@Path("tipo")
public class TipoWS {
    @POST
    @Path("list")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response list(@Context HttpServletRequest request, Tipo dato) {
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
        }/*
        if (dato.getEstado() == null) {
            resp.put("log", "Ingresar correctamente: estado");
            resp.put("info", new JSONArray());
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    header("totalpaginas", 0).
                    header("totalresultados", 0).
                    build();
        }*/
        if (dato.getNombre() == null) {
            dato.setNombre("");
        } /*else {
            switch (dato.getNombre().trim().length()) {
                case 0:
                    dato.setNombre("");
                    break;
                case 1:
                    dato.setNombre(dato.getNombre().trim().toUpperCase());
                    break;
                default:
                    String name = dato.getNombre();
                    name = name.trim();
                    String firstLtr = name.substring(0, 1);
                    String restLtrs = name.substring(1, name.length());
                    firstLtr = firstLtr.toUpperCase();
                    name = firstLtr + restLtrs;
                    dato.setNombre(name);
                    break;
            }
        }*/

        //PROCESAMIENTO
        JSONArray lista = null;
        int size = 0;
        Conexion con = null;
        try {
            con = new Conexion();
            TipoFacade consultaFacade = new TipoFacade(con);
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
    public Response porId(@Context HttpServletRequest request, Tipo dato) {
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
            TipoFacade consultaFacade = new TipoFacade(con);
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
    public Response porNombre(@Context HttpServletRequest request, Tipo dato) {
        JSONObject resp = new JSONObject();
        if (request.getContentType().isEmpty() || request.getContentType() == null || request.getContentType().isBlank()
                || !request.getContentType().contains("application/json") || request.getContentLength() == 0) {
            resp.put("log", "No ha enviado datos");
            resp.put("info", new JSONArray());
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }
        //VERIFICAR DATOS  
        if (dato.getNombre() == null) {
            resp.put("log", "Ingresar correctamente: nombre");
            resp.put("info", new JSONArray());
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }
        dato.setEstado(1);
        dato.setPageIndex(1);
        dato.setPageSize(1000);
        //PROCESAMIENTO
        JSONArray lista = null;
        Conexion con = null;
        try {
            con = new Conexion();
            TipoFacade consultaFacade = new TipoFacade(con);
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
    @Path("ciudades")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response porCiudades(@Context HttpServletRequest request, Tipo dato) {
        JSONObject resp = new JSONObject();
        if (request.getContentType().isEmpty() || request.getContentType() == null || request.getContentType().isBlank()
                || !request.getContentType().contains("application/json") || request.getContentLength() == 0) {
            resp.put("log", "No ha enviado datos");
            resp.put("info", new JSONArray());
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }
        //VERIFICAR DATOS  
        if (dato.getCiudades()== null) {
            resp.put("log", "Ingresar correctamente: ciudades");
            resp.put("info", new JSONArray());
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }if (!dato.getCiudades().contains(",")) {
            resp.put("log", "Ingresar correctamente: ciudades");
            resp.put("info", new JSONArray());
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }
        
        
        //PROCESAMIENTO
        JSONArray lista = null;
        Conexion con = null;
        try {
            con = new Conexion();
            TipoFacade consultaFacade = new TipoFacade(con);
            lista = consultaFacade.busquedaCiudad(dato);
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
    public Response crear(@Context HttpServletRequest request, Tipo dato) {
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
        
        if (dato.getIdLink()== null) {
            resp.put("log", "Ingresar correctamente: tipo");
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }

        if (dato.getNombre().trim().length() == 1) {
            dato.setNombre(dato.getNombre().trim().toUpperCase());
        } else {
            String name = dato.getNombre().trim();
            String firstLtr = name.substring(0, 1);
            String restLtrs = name.substring(1, name.length());
            firstLtr = firstLtr.toUpperCase();
            name = firstLtr + restLtrs;
            dato.setNombre(name);
        }
        JSONArray lista2 = null;
        JSONArray lista3 = null;

        //PROCESAMIENTO
        Conexion con = null;
        try {
            con = new Conexion();
            JSONParser parser = new JSONParser();
            UsuarioFacade consultaFacade2 = new UsuarioFacade(con);
            lista2 = consultaFacade2.busquedaPermisos(dato.getUsername());
            if (lista2 != null) {
                JSONObject obj = (JSONObject) parser.parse(lista2.get(0).toString());
                if (!(boolean) obj.get("informacion") && !(boolean) obj.get("enlaces")) {
                    resp.put("log", "Usuario no tiene permisos, revisar con usuario de Inventario/Enlaces Outsourcing ");
                    return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                            build();
                }
            }
            TipoFacade consultaFacade = new TipoFacade(con);
            if (consultaFacade.busquedaNombreExiste(dato,1)!=null) {
                resp.put("log", "Ya existe");
                return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                        build();
            }
            lista3 = consultaFacade.busquedaNombreExiste(dato,0);
            if (lista3!=null) {
                JSONObject obj = (JSONObject) parser.parse(lista3.get(0).toString());
                Long id = (Long)obj.get("id");
                dato.setId(id);
                dato.setEstado(1);
                consultaFacade.actualizar(dato);
            }else{
                consultaFacade.crear(dato);
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
    @Path("actualizar")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response actualizar(@Context HttpServletRequest request, Tipo dato) {
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
        if (dato.getNombre() == null || dato.getNombre().trim().length() == 0) {
            resp.put("log", "Ingresar correctamente: nombre");
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }
        if (dato.getEstado() == null) {
            resp.put("log", "Ingresar correctamente: estado");
            return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                    build();
        }
        if (dato.getNombre().trim().length() == 1) {
            dato.setNombre(dato.getNombre().trim().toUpperCase());
        } else  {
            String name = dato.getNombre().trim();
            String firstLtr = name.substring(0, 1);
            String restLtrs = name.substring(1, name.length());
            firstLtr = firstLtr.toUpperCase();
            name = firstLtr + restLtrs;
            dato.setNombre(name);
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
                if (!(boolean) obj.get("informacion") && !(boolean) obj.get("enlaces")) {
                    resp.put("log", "Usuario no tiene permisos, revisar con usuario de Inventario/Enlaces Outsourcing ");
                    return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                            build();
                }
                /*
                if ((!(boolean) obj.get("eliminar")) && dato.getEstado()==0) {
                    resp.put("log", "Usuario no puede eliminar dato");
                    return Response.status(Response.Status.BAD_REQUEST).entity(resp.toString()).
                            build();
                }*/
            }
            TipoFacade consultaFacade = new TipoFacade(con);
            if (consultaFacade.busquedaId(dato)==null) {
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

}
