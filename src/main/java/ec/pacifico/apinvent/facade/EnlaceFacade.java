/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.pacifico.apinvent.facade;

import ec.pacifico.apinvent.entity.Enlace;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author Carolina
 */
public class EnlaceFacade {

    private Conexion con;

    public EnlaceFacade(Conexion con) {
        this.con = con;
    }

    public JSONArray obtenerListado(Enlace dato) {
        JSONArray lista = null;
        JSONParser parser = new JSONParser();
        String query
                = "SELECT  Enlace.id,Enlace.bw,Enlace.estado,\n"
                + "Enlace.tunel,\n"
                + "Proveedor.nombre as nombreproveedor,\n"
                + "Propiedad.nombre as nombrepropiedad,\n"
                + "Ciudad.nombre as nombreciudad,\n"
                + "Tipo.nombre as nombretipo,\n"
                + "Agencia.nombre as nombreagencia,\n"
                + "Punto.nombre as nombrepunto,\n"
                + "Hardware.nombre as nombremedio\n"
                + "FROM Enlace \n"
                + "FULL OUTER JOIN Propietario as Proveedor   on(Proveedor.id=Enlace.idproveedor)\n"
                + "FULL OUTER JOIN Entidades as Propiedad     on(Propiedad.id=Enlace.idpropiedad)\n"
                + "FULL OUTER JOIN Hardware                   on(Enlace.idmedio=Hardware.id)\n"
                + "FULL OUTER JOIN Agencia                    on(Enlace.idagencia=Agencia.id)\n"
                + "FULL OUTER JOIN Agencia as Punto           on(Enlace.punto=Punto.id)\n"
                + "FULL OUTER JOIN Tipo                       on(Tipo.id=Agencia.idtipo)\n"
                + "FULL OUTER JOIN Ciudad                     on(Ciudad.id=Tipo.idciudad)\n"
                + "WHERE \n";

        if (dato.getEstado() != null) {
            query = query + "Enlace.estado=" + dato.getEstado() + "\n";
        } else {
            query = query + "Enlace.estado !=0 \n";
        }

        if (dato.getTunel() != null) {
            if (dato.getTunel().contains(",")) {
                query = query + "and CAST(Enlace.tunel as Varchar) IN (";
                String[] lprop = dato.getTunel().split(",");
                query = query + "'" + lprop[0].trim() + "'";
                for (int l = 1; l < lprop.length; l++) {
                    query = query + ",'" + lprop[l].trim() + "'";
                }
                query = query + ")\n";
            } else {
                query = query
                        + " and Enlace.tunel LIKE '%" + dato.getTunel().trim() + "%'\n";
            }
        }

        if (dato.getMedio() != null) {
            if (dato.getMedio().contains(",")) {
                query = query + "and CAST(Hardware.nombre as Varchar) IN (";
                String[] lprop = dato.getMedio().split(",");
                query = query + "'" + lprop[0].trim() + "'";
                for (int l = 1; l < lprop.length; l++) {
                    query = query + ",'" + lprop[l].trim() + "'";
                }
                query = query + ")\n";
            } else {
                query = query
                        + " and Hardware.nombre LIKE '%" + dato.getMedio().trim() + "%'\n";
            }
        }

        if (dato.getCiudad() != null) {
            if (dato.getCiudad().contains(",")) {
                query = query + "and CAST(Ciudad.nombre as Varchar) IN (";
                String[] lnciudad = dato.getCiudad().split(",");
                query = query + "'" + lnciudad[0].trim() + "'";
                for (int l = 1; l < lnciudad.length; l++) {
                    query = query + ",'" + lnciudad[l].trim() + "'";
                }
                query = query + ")\n";
            } else {
                query = query
                        + " and Ciudad.nombre LIKE '%" + dato.getCiudad().trim() + "%'\n";
            }
        }

        if (dato.getTipo() != null) {
            if (dato.getTipo().contains(",")) {
                query = query + "and CAST(Tipo.nombre as Varchar) IN (";
                String[] lntipo = dato.getTipo().split(",");
                query = query + "'" + lntipo[0].trim() + "'";
                for (int l = 1; l < lntipo.length; l++) {
                    query = query + ",'" + lntipo[l].trim() + "'";
                }
                query = query + ")\n";
            } else {
                query = query
                        + " and Tipo.nombre LIKE '%" + dato.getTipo().trim() + "%'\n";
            }
        }

        if (dato.getPunto() != null) {
            if (dato.getPunto().contains(",")) {
                query = query + "and CAST(Punto.nombre as Varchar) IN (";
                String[] lprop = dato.getPunto().split(",");
                query = query + "'" + lprop[0].trim() + "'";
                for (int l = 1; l < lprop.length; l++) {
                    query = query + ",'" + lprop[l].trim() + "'";
                }
                query = query + ")\n";
            } else {
                query = query
                        + " and Punto.nombre LIKE '%" + dato.getPunto().trim() + "%'\n";
            }
        }

        if (dato.getAgencia() != null) {
            if (dato.getAgencia().contains(",")) {
                query = query + "and CAST(Agencia.nombre as Varchar) IN (";
                String[] lnagencia = dato.getAgencia().split(",");
                query = query + "'" + lnagencia[0].trim() + "'";
                for (int l = 1; l < lnagencia.length; l++) {
                    query = query + ",'" + lnagencia[l].trim() + "'";
                }
                query = query + ")\n";
            } else {
                query = query
                        + " and Agencia.nombre LIKE '%" + dato.getAgencia().trim() + "%'\n";
            }
        }

        if (dato.getPropiedad() != null) {
            if (dato.getPropiedad().contains(",")) {
                query = query + "and CAST(Propiedad.nombre as Varchar) IN (";
                String[] lprop = dato.getPropiedad().split(",");
                query = query + "'" + lprop[0].trim() + "'";
                for (int l = 1; l < lprop.length; l++) {
                    query = query + ",'" + lprop[l].trim() + "'";
                }
                query = query + ")\n";
            } else {
                query = query
                        + " and Propiedad.nombre LIKE '%" + dato.getPropiedad().trim() + "%'\n";
            }
        }

        if (dato.getProveedor() != null) {
            if (dato.getProveedor().contains(",")) {
                query = query + "and CAST(Proveedor.nombre as Varchar) IN (";
                String[] lprop = dato.getProveedor().split(",");
                query = query + "'" + lprop[0].trim() + "'";
                for (int l = 1; l < lprop.length; l++) {
                    query = query + ",'" + lprop[l].trim() + "'";
                }
                query = query + ")\n";
            } else {
                query = query
                        + " and Proveedor.nombre LIKE '%" + dato.getProveedor().trim() + "%'\n";
            }

        }
        query = query
                + "ORDER BY CAST(Enlace.fecha as DATETIME) desc\n"
                + "OFFSET ? ROWS\n"
                + "FETCH NEXT ? ROWS ONLY\n"
                + "FOR JSON AUTO; \n";
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = con.getConnection().prepareStatement(query);
            st.setInt(2, dato.getPageSize());
            st.setInt(1, (dato.getPageIndex() - 1) * dato.getPageSize());
            rs = st.executeQuery();
            String rsst = "";
            while (rs.next()) {
                rsst = rsst + rs.getString(1);
            }
            lista = (JSONArray) parser.parse(rsst);

            return lista;
        } catch (Exception e) {
            System.out.println(this.getClass().toString() + ".obtenerListado " + e.getMessage());
            return null;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
            } catch (Exception e) {
                System.out.println(this.getClass().toString() + ".obtenerListado " + e.getMessage());
            }
        }
    }

    public JSONArray obtenerListadoExcel(Enlace dato) {
        JSONArray lista = null;
        JSONParser parser = new JSONParser();
        String query
                = "SELECT  Enlace.id,\n"
                + "Enlace.bw,Enlace.tunel, Enlace.estado,Enlace.identificador,\n"
                + "Proveedor.nombre as nombreproveedor,\n"
                + "Propiedad.nombre as nombrepropiedad,\n"
                + "Ciudad.nombre as nombreciudad,\n"
                + "Tipo.nombre as nombretipo,\n"
                + "Agencia.nombre as nombreagencia,\n"
                + "Punto.nombre as nombrepunto,\n"
                + "Hardware.nombre as nombremedio\n"
                + "FROM Enlace \n"
                + "FULL OUTER JOIN Propietario as Proveedor   on(Proveedor.id=Enlace.idproveedor)\n"
                + "FULL OUTER JOIN Entidades as Propiedad     on(Propiedad.id=Enlace.idpropiedad)\n"
                + "FULL OUTER JOIN Hardware                   on(Enlace.idmedio=Hardware.id)\n"
                + "FULL OUTER JOIN Agencia                    on(Enlace.idagencia=Agencia.id)\n"
                + "FULL OUTER JOIN Agencia as Punto           on(Enlace.punto=Punto.id)\n"
                + "FULL OUTER JOIN Tipo                       on(Tipo.id=Agencia.idtipo)\n"
                + "FULL OUTER JOIN Ciudad                     on(Ciudad.id=Tipo.idciudad)\n"
                + "WHERE Enlace.estado != 0\n";
        query = query
                + "ORDER BY CAST(Agencia.nombre as Varchar(1000)) asc\n"
                + "FOR JSON AUTO; \n";
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = con.getConnection().prepareStatement(query);
            //st.setInt(2, dato.getPageSize());
            //st.setInt(1, (dato.getPageIndex() - 1) * dato.getPageSize());
            rs = st.executeQuery();
            String rsst = "";
            while (rs.next()) {
                rsst = rsst + rs.getString(1);
            }
            lista = (JSONArray) parser.parse(rsst);

            return lista;
        } catch (Exception e) {
            System.out.println(this.getClass().toString() + ".obtenerListado " + e.getMessage());
            return null;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
            } catch (Exception e) {
                System.out.println(this.getClass().toString() + ".obtenerListado " + e.getMessage());
            }
        }
    }

    public Integer obtenerListadoSize(Enlace dato) {
        String query
                = "SELECT COUNT(Enlace.id)\n"
                + "FROM Enlace \n"
                + "FULL OUTER JOIN Propietario as Proveedor   on(Proveedor.id=Enlace.idproveedor)\n"
                + "FULL OUTER JOIN Entidades as Propiedad     on(Propiedad.id=Enlace.idpropiedad)\n"
                + "FULL OUTER JOIN Hardware                   on(Enlace.idmedio=Hardware.id)\n"
                + "FULL OUTER JOIN Agencia                    on(Enlace.idagencia=Agencia.id)\n"
                + "FULL OUTER JOIN Agencia as Punto           on(Enlace.punto=Punto.id)\n"
                + "FULL OUTER JOIN Tipo                       on(Tipo.id=Agencia.idtipo)\n"
                + "FULL OUTER JOIN Ciudad                     on(Ciudad.id=Tipo.idciudad)\n"
                + "WHERE \n";

        if (dato.getEstado() != null) {
            query = query + "Enlace.estado=" + dato.getEstado() + "\n";
        } else {
            query = query + "Enlace.estado !=0 \n";
        }

        if (dato.getTunel() != null) {
            if (dato.getTunel().contains(",")) {
                query = query + "and CAST(Enlace.tunel as Varchar) IN (";
                String[] lprop = dato.getTunel().split(",");
                query = query + "'" + lprop[0].trim() + "'";
                for (int l = 1; l < lprop.length; l++) {
                    query = query + ",'" + lprop[l].trim() + "'";
                }
                query = query + ")\n";
            } else {
                query = query
                        + " and Enlace.tunel LIKE '%" + dato.getTunel().trim() + "%'\n";
            }
        }
        //if (dato.getBw()!= null) {
        //    query = query
        //            + " and Enlace.bw LIKE '%" + dato.getBw()+ "%'\n";
        //}
        if (dato.getMedio() != null) {
            if (dato.getMedio().contains(",")) {
                query = query + "and CAST(Hardware.nombre as Varchar) IN (";
                String[] lprop = dato.getMedio().split(",");
                query = query + "'" + lprop[0].trim() + "'";
                for (int l = 1; l < lprop.length; l++) {
                    query = query + ",'" + lprop[l].trim() + "'";
                }
                query = query + ")\n";
            } else {
                query = query
                        + " and Hardware.nombre LIKE '%" + dato.getMedio().trim() + "%'\n";
            }
        }

        if (dato.getCiudad() != null) {
            if (dato.getCiudad().contains(",")) {
                query = query + "and CAST(Ciudad.nombre as Varchar) IN (";
                String[] lnciudad = dato.getCiudad().split(",");
                query = query + "'" + lnciudad[0].trim() + "'";
                for (int l = 1; l < lnciudad.length; l++) {
                    query = query + ",'" + lnciudad[l].trim() + "'";
                }
                query = query + ")\n";
            } else {
                query = query
                        + " and Ciudad.nombre LIKE '%" + dato.getCiudad().trim() + "%'\n";
            }
        }

        if (dato.getTipo() != null) {
            if (dato.getTipo().contains(",")) {
                query = query + "and CAST(Tipo.nombre as Varchar) IN (";
                String[] lntipo = dato.getTipo().split(",");
                query = query + "'" + lntipo[0].trim() + "'";
                for (int l = 1; l < lntipo.length; l++) {
                    query = query + ",'" + lntipo[l].trim() + "'";
                }
                query = query + ")\n";
            } else {
                query = query
                        + " and Tipo.nombre LIKE '%" + dato.getTipo().trim() + "%'\n";
            }
        }

        if (dato.getPunto() != null) {
            if (dato.getPunto().contains(",")) {
                query = query + "and CAST(Punto.nombre as Varchar) IN (";
                String[] lprop = dato.getPunto().split(",");
                query = query + "'" + lprop[0].trim() + "'";
                for (int l = 1; l < lprop.length; l++) {
                    query = query + ",'" + lprop[l].trim() + "'";
                }
                query = query + ")\n";
            } else {
                query = query
                        + " and Punto.nombre LIKE '%" + dato.getPunto().trim() + "%'\n";
            }
        }

        if (dato.getAgencia() != null) {
            if (dato.getAgencia().contains(",")) {
                query = query + "and CAST(Agencia.nombre as Varchar) IN (";
                String[] lnagencia = dato.getAgencia().split(",");
                query = query + "'" + lnagencia[0].trim() + "'";
                for (int l = 1; l < lnagencia.length; l++) {
                    query = query + ",'" + lnagencia[l].trim() + "'";
                }
                query = query + ")\n";
            } else {
                query = query
                        + " and Agencia.nombre LIKE '%" + dato.getAgencia().trim() + "%'\n";
            }
        }

        if (dato.getPropiedad() != null) {
            if (dato.getPropiedad().contains(",")) {
                query = query + "and CAST(Propiedad.nombre as Varchar) IN (";
                String[] lprop = dato.getPropiedad().split(",");
                query = query + "'" + lprop[0].trim() + "'";
                for (int l = 1; l < lprop.length; l++) {
                    query = query + ",'" + lprop[l].trim() + "'";
                }
                query = query + ")\n";
            } else {
                query = query
                        + " and Propiedad.nombre LIKE '%" + dato.getPropiedad().trim() + "%'\n";
            }
        }

        if (dato.getProveedor() != null) {
            if (dato.getProveedor().contains(",")) {
                query = query + "and CAST(Proveedor.nombre as Varchar) IN (";
                String[] lprop = dato.getProveedor().split(",");
                query = query + "'" + lprop[0].trim() + "'";
                for (int l = 1; l < lprop.length; l++) {
                    query = query + ",'" + lprop[l].trim() + "'";
                }
                query = query + ")\n";
            } else {
                query = query
                        + " and Proveedor.nombre LIKE '%" + dato.getProveedor().trim() + "%'\n";
            }

        }
        query = query + ";";
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = con.getConnection().prepareStatement(query);
            rs = st.executeQuery();
            int size = 0;
            while (rs.next()) {
                size = rs.getInt(1);
            }
            return size;
        } catch (Exception e) {
            System.out.println(this.getClass().toString() + ".obtenerListadoSize " + e.getMessage());
            return null;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
            } catch (Exception e) {
                System.out.println(this.getClass().toString() + ".obtenerListadoSize " + e.getMessage());
            }
        }
    }

    public JSONArray obtenerListadoLogs(Enlace dato) {
        JSONArray lista = null;
        JSONParser parser = new JSONParser();

        String query
                = "SELECT  logsenlace.id,logsenlace.bw,logsenlace.estado,\n"
                + "logsenlace.tunel,logsenlace.fecha,logsenlace.usuario,\n"
                + "Proveedor.nombre as nombreproveedor,\n"
                + "Propiedad.nombre as nombrepropiedad,\n"
                + "Agencia.nombre as nombreagencia,\n"
                + "Punto.nombre as nombrepunto,\n"
                + "Hardware.nombre as nombremedio\n"
                + "FROM logsenlace \n"
                + "FULL OUTER JOIN Propietario as Proveedor   on(Proveedor.id=logsenlace.idproveedor)\n"
                + "FULL OUTER JOIN Entidades as Propiedad     on(Propiedad.id=logsenlace.idpropiedad)\n"
                + "FULL OUTER JOIN Hardware                   on(logsenlace.idmedio=Hardware.id)\n"
                + "FULL OUTER JOIN Agencia                    on(logsenlace.idagencia=Agencia.id)\n"
                + "FULL OUTER JOIN Agencia as Punto           on(logsenlace.punto=Punto.id)\n"
                + "WHERE logsenlace.id=" + dato.getId() + "\n";

        query = query
                + "ORDER BY CAST(logsenlace.fecha as DATETIME) desc\n"
                + "OFFSET ? ROWS\n"
                + "FETCH NEXT ? ROWS ONLY\n"
                + "FOR JSON AUTO; \n";
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = con.getConnection().prepareStatement(query);
            st.setInt(2, dato.getPageSize());
            st.setInt(1, (dato.getPageIndex() - 1) * dato.getPageSize());
            rs = st.executeQuery();
            String rsst = "";
            while (rs.next()) {
                rsst = rsst + rs.getString(1);
            }
            lista = (JSONArray) parser.parse(rsst);

            return lista;
        } catch (Exception e) {
            System.out.println(this.getClass().toString() + ".obtenerListadoLogs " + e.getMessage());
            return null;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
            } catch (Exception e) {
                System.out.println(this.getClass().toString() + ".obtenerListadoLogs " + e.getMessage());
            }
        }
    }

    public Integer obtenerListadoLogsSize(Enlace dato) {
        String query
                = "SELECT COUNT(logsenlace.id)\n"
                + "FROM logsenlace \n"
                + "FULL OUTER JOIN Propietario as Proveedor   on(Proveedor.id=logsenlace.idproveedor)\n"
                + "FULL OUTER JOIN Entidades as Propiedad     on(Propiedad.id=logsenlace.idpropiedad)\n"
                + "FULL OUTER JOIN Hardware                   on(logsenlace.idmedio=Hardware.id)\n"
                + "FULL OUTER JOIN Agencia                    on(logsenlace.idagencia=Agencia.id)\n"
                + "FULL OUTER JOIN Agencia as Punto           on(logsenlace.punto=Punto.id)\n"
                + "WHERE logsenlace.id=" + dato.getId() + "\n";
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = con.getConnection().prepareStatement(query);
            rs = st.executeQuery();
            int size = 0;
            while (rs.next()) {
                size = rs.getInt(1);
            }
            return size;
        } catch (Exception e) {
            System.out.println(this.getClass().toString() + ".obtenerListadoLogsSize" + e.getMessage());
            return null;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
            } catch (Exception e) {
                System.out.println(this.getClass().toString() + ".obtenerListadoLogsSize " + e.getMessage());
            }
        }
    }

    public JSONArray obtenerListadoBaja(Enlace dato) {
        JSONArray lista = null;
        JSONParser parser = new JSONParser();

        String query
                = "SELECT inicio,fin, usuario, fecha, usuarioc, fechac\n"
                + "FROM Fechabaja \n"
                + "WHERE idenlace=" + dato.getId() + "\n"
                + "ORDER BY CAST(fechac as DATETIME) desc\n"
                + "OFFSET ? ROWS\n"
                + "FETCH NEXT ? ROWS ONLY\n"
                + "FOR JSON AUTO; \n";
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = con.getConnection().prepareStatement(query);
            st.setInt(2, dato.getPageSize());
            st.setInt(1, (dato.getPageIndex() - 1) * dato.getPageSize());
            rs = st.executeQuery();
            String rsst = "";
            while (rs.next()) {
                rsst = rsst + rs.getString(1);
            }
            lista = (JSONArray) parser.parse(rsst);

            return lista;
        } catch (Exception e) {
            System.out.println(this.getClass().toString() + ".obtenerListadoBaja " + e.getMessage());
            return null;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
            } catch (Exception e) {
                System.out.println(this.getClass().toString() + ".obtenerListadoBaja " + e.getMessage());
            }
        }
    }

    public Integer obtenerListadoBajaSize(Enlace dato) {
        String query
                = "SELECT COUNT(id)\n"
                + "FROM Fechabaja \n"
                + "WHERE idenlace=" + dato.getId() + "\n";
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = con.getConnection().prepareStatement(query);
            rs = st.executeQuery();
            int size = 0;
            while (rs.next()) {
                size = rs.getInt(1);
            }
            return size;
        } catch (Exception e) {
            System.out.println(this.getClass().toString() + ".obtenerListadoBajaSize" + e.getMessage());
            return null;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
            } catch (Exception e) {
                System.out.println(this.getClass().toString() + ".obtenerListadoBajaSize " + e.getMessage());
            }
        }
    }

    public JSONArray busquedaId(Enlace dato) {
        JSONArray lista = null;
        JSONParser parser = new JSONParser();
        String query
                = "SELECT id,bw,tunel,identificador,estado,usuario,fecha,usuarioc,fechac,\n"
                + "(SELECT id,nombre,\n"
                + "(SELECT id,nombre,\n"
                + "(SELECT id,nombre\n"
                + "FROM Ciudad \n"
                + "WHERE Ciudad.id=Tipo.idciudad\n"
                + "FOR JSON AUTO) as Ciudad\n"
                + "FROM Tipo \n"
                + "WHERE Tipo.id=Agencia.idtipo\n"
                + "FOR JSON AUTO) as Tipo\n"
                + "FROM Agencia \n"
                + "WHERE Agencia.id=Enlace.idagencia\n"
                + "FOR JSON AUTO) as Agencia\n,"
                + "(SELECT id,inicio,fin\n"
                + "FROM Fechabaja \n"
                + "WHERE Fechabaja.id=Enlace.idbaja\n"
                + "FOR JSON AUTO) as Baja,\n"
                + "(SELECT id,nombre\n"
                + "FROM Agencia \n"
                + "WHERE Agencia.id=Enlace.punto\n"
                + "FOR JSON AUTO) as Punto,\n"
                + "(SELECT id,nombre\n"
                + "FROM Propietario \n"
                + "WHERE Propietario.id=Enlace.idproveedor\n"
                + "FOR JSON AUTO) as Proveedor,\n"
                + "(SELECT id,nombre\n"
                + "FROM Hardware \n"
                + "WHERE Hardware.id=Enlace.idmedio\n"
                + "FOR JSON AUTO) as Medio,\n"
                + "(SELECT id,nombre\n"
                + "FROM Entidades \n"
                + "WHERE Entidades.id=Enlace.idpropiedad\n"
                + "FOR JSON AUTO) as Propiedad\n"
                + "FROM Enlace\n"
                + "WHERE id = " + dato.getId() + "\n"
                + "FOR JSON AUTO; \n";
        PreparedStatement st = null;

        ResultSet rs = null;
        try {
            st = con.getConnection().prepareStatement(query);
            rs = st.executeQuery();
            String rsst = "";
            while (rs.next()) {
                rsst = rsst + rs.getString(1);
            }
            if (rsst.length() > 0) {
                lista = (JSONArray) parser.parse(rsst);
            }
            return lista;
        } catch (Exception e) {
            System.out.println(this.getClass().toString() + ".busquedaId " + e.getMessage());
            return null;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
            } catch (Exception e) {
                System.out.println(this.getClass().toString() + ".busquedaId " + e.getMessage());
            }
        }
    }

    public JSONArray busquedaAgenciaEnlace(Enlace dato) {
        JSONArray lista = null;
        JSONParser parser = new JSONParser();
        String query
                = "SELECT id, nombre\n"
                + "FROM Enlace\n"
                + "LEFT JOIN Agencia on(Enlace.idagencia=Agencia.id)\n"
                + "WHERE Agencia.nombre LIKE '" + dato.getAgencia() + "'  and Enlace.estado != 0\n"
                + "FOR JSON AUTO; \n";
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = con.getConnection().prepareStatement(query);
            rs = st.executeQuery();
            String rsst = "";
            while (rs.next()) {
                rsst = rsst + rs.getString(1);

            }
            if (rsst.length() > 0) {
                lista = (JSONArray) parser.parse(rsst);
            }

            return lista;
        } catch (Exception e) {
            System.out.println(this.getClass().toString() + ".busquedaNombre " + e.getMessage());
            return null;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
            } catch (Exception e) {
                System.out.println(this.getClass().toString() + ".busquedaNombre " + e.getMessage());
            }
        }
    }

    public JSONArray busquedaIP(Enlace dato) {
        JSONArray lista = null;
        JSONParser parser = new JSONParser();
        String query
                = "SELECT id\n"
                + "FROM Enlace\n"
                + "WHERE tunel LIKE '" + dato.getTunel().trim() + "'  and estado = 1\n"
                + "FOR JSON AUTO; \n";
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = con.getConnection().prepareStatement(query);
            rs = st.executeQuery();
            String rsst = "";
            while (rs.next()) {
                rsst = rsst + rs.getString(1);

            }
            if (rsst.length() > 0) {
                lista = (JSONArray) parser.parse(rsst);
            }

            return lista;
        } catch (Exception e) {
            System.out.println(this.getClass().toString() + ".busquedaIP " + e.getMessage());
            return null;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
            } catch (Exception e) {
                System.out.println(this.getClass().toString() + ".busquedaIP " + e.getMessage());
            }
        }
    }

    public boolean actualizar(Enlace dato) {
        String baja = "";
        String query = "";
        if (dato.getIdbaja() != null) { //ACTUALMENTE ESTA DE BAJA
            baja = "UPDATE Fechabaja SET \n"
                    + "inicio   = '" + dato.getFechainicio() + "'\n"
                    + ",usuario = '" + dato.getUsername().trim() + "'\n"
                    + ",fecha = CURRENT_TIMESTAMP \n";
            if (dato.getFechabaja() != null) {
                baja = baja
                        + ",fin      = '" + dato.getFechabaja() + "'\n";
            }
            baja = baja
                    + "Where id = " + dato.getIdbaja() + ";\n";
            query = query + baja;
            if (dato.getEstado() != 2) {//INGRESO O ELIMINADO
                //ACTUALIZADO FECHA DEL ACTUAL ID DE BAJA SI NO ES NULL
                dato.setIdbaja(null);
            } else {//SIGUE EN BAJA

            }
        } else {//NO ESTA DE BAJA
            if (dato.getEstado() == 2) {//NUEVO BAJA
                baja = "INSERT INTO Fechabaja\n"
                        + "(id\n"
                        + ",usuario\n"
                        + ",fecha\n"
                        + ",usuarioc\n"
                        + ",fechac\n"
                        + ",inicio\n";
                if (dato.getFechabaja() != null) {
                    baja = baja
                            + ",fin\n";
                }
                baja = baja
                        + ",idenlace)"
                        + " OUTPUT Inserted.id \n"
                        + " VALUES\n"
                        + "(NEXT VALUE FOR SeqFechabaja\n"
                        + ",'" + dato.getUsername().trim() + "'\n"
                        + ", CURRENT_TIMESTAMP \n"
                        + ",'" + dato.getUsername().trim() + "'\n"
                        + ", CURRENT_TIMESTAMP \n"
                        + ",'" + dato.getFechainicio() + "'\n";
                if (dato.getFechabaja() != null) {
                    baja = baja
                            + ",'" + dato.getFechabaja() + "'\n";
                }
                baja = baja
                        + "," + dato.getId() + ");\n";
            }

        }
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            con.getConnection().setAutoCommit(false);

            if (dato.getIdbaja() == null && dato.getEstado() == 2) {//NUEVO BAJA
                st = con.getConnection().prepareStatement(baja);
                rs = st.executeQuery();
                Long idbaja = 0l;
                while (rs.next()) {
                    idbaja = rs.getLong(1);
                };

                if (idbaja.equals(0l)) {
                    con.getConnection().rollback();
                    return false;
                }

                dato.setIdbaja(idbaja);
            }

            query = query
                    + "UPDATE Enlace SET\n";
            if (dato.getIdentificador() != null) {
                query = query
                        + "      identificador = '" + dato.getIdentificador().trim() + "'\n";
            } else {
                query = query
                        + "      identificador = " + dato.getIdentificador() + "\n";
            }
            query = query
                    + "      ,tunel = '" + dato.getTunel().trim() + "'\n"
                    + "      ,punto = " + dato.getIdpunto() + "\n"
                    + "      ,estado = " + dato.getEstado() + "\n"
                    + "      ,idbaja = " + dato.getIdbaja() + "\n"
                    + "      ,usuario = '" + dato.getUsername().trim() + "'\n"
                    + "      ,fecha = CURRENT_TIMESTAMP \n"
                    + "      ,bw = " + dato.getBw() + "\n"
                    + "      ,idmedio = " + dato.getIdmedio() + "\n"
                    + "      ,idproveedor = " + dato.getIdproveedor() + "\n"
                    + "      ,idagencia = " + dato.getIdagencia() + "\n"
                    + "      ,idpropiedad = " + dato.getIdpropiedad() + " \n"
                    + " WHERE id= " + dato.getId() + ";";

            query = query
                    + "INSERT INTO logsenlace\n"
                    + "           (id\n";
            if (dato.getIdentificador() != null) {
                query = query
                        + "         ,identificador\n";
            }
            query = query
                    + "           ,idproveedor\n"
                    + "           ,idagencia\n"
                    + "           ,idbaja\n"
                    + "           ,idpropiedad\n"
                    + "           ,usuario\n"
                    + "           ,fecha\n"
                    + "           ,bw\n"
                    + "           ,estado\n"
                    + "           ,tunel\n"
                    + "           ,punto\n"
                    + "           ,idmedio)\n"
                    + "     VALUES\n"
                    + "           (" + dato.getId() + "\n";
            if (dato.getIdentificador() != null) {
                query = query
                        + ",'" + dato.getIdentificador().trim() + "'\n";
            }
            query = query
                    + "           ," + dato.getIdproveedor() + "\n"
                    + "           ," + dato.getIdagencia() + "\n"
                    + "           ," + dato.getIdbaja() + "\n"
                    + "           ," + dato.getIdpropiedad() + "\n"
                    + "           ,'" + dato.getUsername().trim() + "'\n"
                    + "           , CURRENT_TIMESTAMP \n"
                    + "           ," + dato.getBw() + "\n"
                    + "           ," + dato.getEstado() + "\n"
                    + "           ,'" + dato.getTunel().trim() + "'\n"
                    + "           ," + dato.getIdpunto() + "\n"
                    + "           ," + dato.getIdmedio() + ");\n";
            st = con.getConnection().prepareStatement(query);
            st.executeUpdate();
            con.getConnection().commit();
            return true;
        } catch (Exception e) {
            System.out.println(this.getClass().toString() + ".actualizar " + e.getMessage());
            return false;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
            } catch (Exception e) {
                System.out.println(this.getClass().toString() + ".actualizar " + e.getMessage());
            }
        }
    }

    public boolean crear(Enlace dato) {
        String query
                = "INSERT INTO Enlace\n"
                + "           (id\n";
        if (dato.getIdentificador() != null) {
            query = query
                    + "         ,identificador\n";
        }
        query = query
                + "           ,idproveedor\n"
                + "           ,idagencia\n"
                + "           ,idpropiedad\n"
                + "           ,usuario\n"
                + "           ,fecha\n"
                + "           ,usuarioc\n"
                + "           ,fechac\n"
                + "           ,bw\n"
                + "           ,tunel\n"
                + "           ,punto\n"
                + "           ,idmedio)\n"
                + "     VALUES\n"
                + "           (NEXT VALUE FOR SeqEnlace\n";
        if (dato.getIdentificador() != null) {
            query = query
                    + ",'" + dato.getIdentificador().trim() + "'\n";
        }
        query = query
                + "           ," + dato.getIdproveedor() + "\n"
                + "           ," + dato.getIdagencia() + "\n"
                + "           ," + dato.getIdpropiedad() + "\n"
                + "           ,'" + dato.getUsername().trim() + "'\n"
                + "           , CURRENT_TIMESTAMP \n"
                + "           ,'" + dato.getUsername().trim() + "'\n"
                + "           , CURRENT_TIMESTAMP \n"
                + "           ," + dato.getBw() + "\n"
                + "           ,'" + dato.getTunel().trim() + "'\n"
                + "           ," + dato.getIdpunto() + "\n"
                + "           ," + dato.getIdmedio() + ");\n";

        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            con.getConnection().setAutoCommit(false);
            st = con.getConnection().prepareStatement(query);
            st.executeUpdate();
            con.getConnection().commit();
            return true;
        } catch (Exception e) {
            System.out.println(this.getClass().toString() + ".crear " + e.getMessage());
            return false;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
            } catch (Exception e) {
                System.out.println(this.getClass().toString() + ".crear " + e.getMessage());
            }
        }
    }

    //HAY QUE EDITAR
    public boolean createExcel(Enlace dato, String path) {
        try {
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Enlaces");
            sheet.setDisplayGridlines(false);
            sheet.setDefaultColumnWidth(15);

            XSSFFont fonts = ((XSSFWorkbook) workbook).createFont();
            fonts.setFontName("Calibri");
            fonts.setBold(true);
            fonts.setColor(IndexedColors.WHITE.getIndex());
            fonts.setFontHeightInPoints((short) 18);

            CellStyle headStInventario = workbook.createCellStyle();
            headStInventario.setAlignment(HorizontalAlignment.CENTER);
            headStInventario.setVerticalAlignment(VerticalAlignment.TOP);
            headStInventario.setFont(fonts);
            headStInventario.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());

            headStInventario.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            Row herderRef = sheet.createRow(0);
            Cell cellherderRef = herderRef.createCell(0);
            cellherderRef.setCellValue("Listado de Enlaces");
            cellherderRef.setCellStyle(headStInventario);

            sheet.addMergedRegion(new CellRangeAddress(
                    0, //first row (0-based)
                    1, //last row  (0-based)
                    0, //first column (0-based)
                    9 //last column  (0-based)
            ));

            Row header = sheet.createRow(2);
            XSSFFont font = ((XSSFWorkbook) workbook).createFont();
            font.setFontName("Calibri");
            font.setFontHeightInPoints((short) 11);
            font.setBold(true);
            font.setColor(IndexedColors.BLACK.getIndex());

            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setWrapText(true);
            headerStyle.setBorderBottom(BorderStyle.MEDIUM);
            headerStyle.setBorderLeft(BorderStyle.MEDIUM);
            headerStyle.setBorderRight(BorderStyle.MEDIUM);
            headerStyle.setBorderTop(BorderStyle.MEDIUM);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setFont(font);

            Cell headerCell = header.createCell(0);
            headerCell.setCellValue("Ciudad");
            headerCell.setCellStyle(headerStyle);

            headerCell = header.createCell(1);
            headerCell.setCellValue("Punto A");
            headerCell.setCellStyle(headerStyle);

            headerCell = header.createCell(2);
            headerCell.setCellValue("Tipo");
            headerCell.setCellStyle(headerStyle);

            headerCell = header.createCell(3);
            headerCell.setCellValue("Punto B ");
            headerCell.setCellStyle(headerStyle);

            headerCell = header.createCell(4);
            headerCell.setCellValue("Proveedor ");
            headerCell.setCellStyle(headerStyle);

            headerCell = header.createCell(5);
            headerCell.setCellValue("Medio");
            headerCell.setCellStyle(headerStyle);

            headerCell = header.createCell(6);
            headerCell.setCellValue("BW(Kbps)");
            headerCell.setCellStyle(headerStyle);

            headerCell = header.createCell(7);
            headerCell.setCellValue("IP Tunnel");
            headerCell.setCellStyle(headerStyle);

            headerCell = header.createCell(8);
            headerCell.setCellValue("Propiedad");
            headerCell.setCellStyle(headerStyle);
            
            headerCell = header.createCell(9);
            headerCell.setCellValue("Identificador");
            headerCell.setCellStyle(headerStyle);
            
            headerCell = header.createCell(10);
            headerCell.setCellValue("Estado");
            headerCell.setCellStyle(headerStyle);

            JSONArray lista = obtenerListadoExcel(dato);
            JSONParser parser = new JSONParser();
            //final ObjectMapper objectMapper = new ObjectMapper();
            //Inventario[] reporte = objectMapper.readValue(lista.toString(), Inventario[].class);

            CellStyle style = workbook.createCellStyle();
            style.setBorderBottom(BorderStyle.MEDIUM);
            style.setBorderLeft(BorderStyle.MEDIUM);
            style.setBorderRight(BorderStyle.MEDIUM);
            style.setBorderTop(BorderStyle.MEDIUM);

            int celda = 0;
            int fila = 3;
            Row row;
            for (Object item : lista) {
                JSONObject inventario = (JSONObject) parser.parse(item.toString());
                //String medio = inventario.get("medio")==null? "N/A":inventario.get("medio").toString();
                String bw = inventario.get("bw") == null ? "N/A" : inventario.get("bw").toString();
                String tunel = inventario.get("tunel") == null ? "N/A" : inventario.get("tunel").toString();
                String identificador = inventario.get("identificador") == null ? "N/A" : inventario.get("identificador").toString();
                JSONArray provrray = (JSONArray) parser.parse(inventario.get("Proveedor").toString());
                JSONObject prov = (JSONObject) parser.parse(provrray.get(0).toString());
                String proveedor = prov.get("nombreproveedor") == null ? "N/A" : prov.get("nombreproveedor").toString();
                JSONArray propddarray = (JSONArray) parser.parse(prov.get("Propiedad").toString());
                JSONObject propdd = (JSONObject) parser.parse(propddarray.get(0).toString());
                String propiedad = propdd.get("nombrepropiedad") == null ? "N/A" : propdd.get("nombrepropiedad").toString();
                JSONArray cdarray = (JSONArray) parser.parse(propdd.get("Ciudad").toString());
                JSONObject cd = (JSONObject) parser.parse(cdarray.get(0).toString());
                String ciudad = cd.get("nombreciudad") == null ? "N/A" : cd.get("nombreciudad").toString();
                JSONArray tparray = (JSONArray) parser.parse(cd.get("Tipo").toString());
                JSONObject tp = (JSONObject) parser.parse(tparray.get(0).toString());
                String tipo = tp.get("nombretipo") == null ? "N/A" : tp.get("nombretipo").toString();
                JSONArray agarray = (JSONArray) parser.parse(tp.get("Agencia").toString());
                JSONObject ag = (JSONObject) parser.parse(agarray.get(0).toString());
                String agencia = ag.get("nombreagencia") == null ? "N/A" : ag.get("nombreagencia").toString();
                JSONArray ptarray = (JSONArray) parser.parse(ag.get("Punto").toString());
                JSONObject pt = (JSONObject) parser.parse(ptarray.get(0).toString());
                String punto = pt.get("nombrepunto") == null ? "N/A" : pt.get("nombrepunto").toString();
                JSONArray mdarray = (JSONArray) parser.parse(pt.get("Hardware").toString());
                JSONObject md = (JSONObject) parser.parse(mdarray.get(0).toString());
                String medio = md.get("nombremedio") == null ? "N/A" : md.get("nombremedio").toString();

                String estado = inventario.get("estado") == null ? "Eliminado" : inventario.get("estado").toString();
                int intestado = Integer.parseInt(estado);
                if (intestado == 0) {
                    estado = "Eliminado";
                }
                if (intestado == 1) {
                    estado = "Ingresado";
                }
                if (intestado == 2) {
                    estado = "Baja";
                }
                row = sheet.createRow(fila);

                Cell cell = row.createCell(celda);
                cell.setCellValue(ciudad);
                cell.setCellStyle(style);
                celda++;

                cell = row.createCell(celda);
                cell.setCellValue(agencia);
                cell.setCellStyle(style);
                celda++;

                cell = row.createCell(celda);
                cell.setCellValue(tipo);
                cell.setCellStyle(style);
                celda++;

                cell = row.createCell(celda);
                cell.setCellValue(punto);
                cell.setCellStyle(style);
                celda++;

                cell = row.createCell(celda);
                cell.setCellValue(proveedor);
                cell.setCellStyle(style);
                celda++;

                cell = row.createCell(celda);
                cell.setCellValue(medio);
                cell.setCellStyle(style);
                celda++;

                cell = row.createCell(celda);
                cell.setCellValue(bw);
                cell.setCellStyle(style);
                celda++;

                cell = row.createCell(celda);
                cell.setCellValue(tunel);
                cell.setCellStyle(style);
                celda++;

                cell = row.createCell(celda);
                cell.setCellValue(propiedad);
                cell.setCellStyle(style);
                celda++;
                
                cell = row.createCell(celda);
                cell.setCellValue(identificador);
                cell.setCellStyle(style);
                celda++;
                
                cell = row.createCell(celda);
                cell.setCellValue(estado);
                cell.setCellStyle(style);
                celda++;

                fila++;
                celda = 0;
            }

            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
            sheet.autoSizeColumn(3);
            sheet.autoSizeColumn(4);
            sheet.autoSizeColumn(5);
            sheet.autoSizeColumn(6);
            sheet.autoSizeColumn(7);
            sheet.autoSizeColumn(8);
            sheet.autoSizeColumn(9);
            sheet.autoSizeColumn(10);
            
            File f = new File(path);
            if (f.exists()) {
                f.delete();

            }
            try (FileOutputStream outputStream = new FileOutputStream(new File(path))) {
                workbook.write(outputStream);
                outputStream.close();
            }
            //FileInputStream input = new FileInputStream(new File(path)); 

            return true;
        } catch (Exception e) {
            System.out.println(this.getClass().toString() + ".crearExcel " + e.getMessage());
            return false;
        }
    }

    public Integer dashboardTotal() {
        String query
                = "SELECT COUNT(id) as c\n"
                + "FROM Enlace\n";
        query = query + ";";
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = con.getConnection().prepareStatement(query);
            rs = st.executeQuery();
            int size = 0;
            while (rs.next()) {
                size = rs.getInt(1);
            }
            return size;
        } catch (Exception e) {
            System.out.println(this.getClass().toString() + ".dashboardTotal " + e.getMessage());
            return null;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
            } catch (Exception e) {
                System.out.println(this.getClass().toString() + ".dashboardTotal " + e.getMessage());
            }
        }
    }

}
