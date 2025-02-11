/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.pacifico.apinvent.facade;

import ec.pacifico.apinvent.entity.Inventario;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
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
import org.json.simple.JSONObject;

/**
 *
 * @author Carolina
 */
public class InventarioFacade {

    private Conexion con;

    public InventarioFacade(Conexion con) {
        this.con = con;
    }

    public JSONArray obtenerListado(Inventario dato) {
        JSONArray lista = null;
        JSONParser parser = new JSONParser();

        String query
                = "SELECT  Inventario.id,Inventario.ip,\n" //listo
                + "Inventario.piso,Inventario.rack,Inventario.estado,Inventario.util,\n"
                + "Inventario.serie,Inventario.so,Inventario.inventario,Inventario.fechabanco,\n"//listo
                + "Inventario.nombre,Modelo.fechafin,\n"//listo
                + "Modelo.nombre as nombremodelo,\n"//listo
                + "Ambiente.nombre as nombreambiente,\n"//listo
                + "Ciudad.nombre as nombreciudad,\n"//listo
                + "Tipo.nombre as nombretipo,\n"//listo
                + "Agencia.nombre as nombreagencia,\n"//listo
                + "Orion.nombre as nombreorion,\n"//listo
                + "Hardware.nombre as nombrehardware,\n"
                + "Propietario.nombre as nombrepropietario\n"//listo
                + "FROM Inventario \n"
                + "FULL OUTER JOIN Orion on(Orion.id=Inventario.idorion)\n"
                + "FULL OUTER JOIN Ambiente on(Ambiente.id=Inventario.idambiente)\n"
                + "FULL OUTER JOIN Agencia on(Inventario.idagencia=Agencia.id)\n"
                + "FULL OUTER JOIN Tipo on(Tipo.id=Agencia.idtipo)\n"
                + "FULL OUTER JOIN Ciudad on(Ciudad.id=Tipo.idciudad)\n"
                + "FULL OUTER JOIN Modelo on(Inventario.idmodelo=Modelo.id)\n"
                + "FULL OUTER JOIN Hardware on(Modelo.idequipo=Hardware.id)\n"
                + "FULL OUTER JOIN Propietario on(Inventario.idpropietario=Propietario.id)\n"
                + "WHERE Inventario.estado=" + dato.getEstado() + "\n";
        if (dato.getIp() != null) {
            //query = query
            //        + " and Inventario.ip LIKE '%" + dato.getIp().trim() + "%'\n";
            if (dato.getIp().contains(",")) {
                query = query + "and CAST(Inventario.ip as Varchar) IN (";
                String[] lip = dato.getIp().split(",");
                query = query + "'" + lip[0].trim() + "'";
                for (int l = 1; l < lip.length; l++) {
                    query = query + ",'" + lip[l].trim() + "'";
                }
                query = query + ")\n";
            } else {
                query = query
                        + " and Inventario.ip LIKE '%" + dato.getIp().trim() + "%'\n";
            }
        }
        if (dato.getSo() != null) {
            //query = query
            //        + " and Inventario.so LIKE '%" + dato.getSo().trim() + "%'\n";
            if (dato.getSo().contains(",")) {
                query = query + "and CAST(Inventario.so as Varchar) IN (";
                String[] lso = dato.getSo().split(",");
                query = query + "'" + lso[0].trim() + "'";
                for (int l = 1; l < lso.length; l++) {
                    query = query + ",'" + lso[l].trim() + "'";
                }
                query = query + ")\n";
            } else {
                query = query
                        + " and Inventario.so LIKE '%" + dato.getSo().trim() + "%'\n";
            }
        }
        if (dato.getInventario() != null) {
            if (dato.getInventario().contains(",")) {
                query = query + "and CAST(Inventario.inventario as Varchar) IN (";
                String[] linv = dato.getInventario().split(",");
                query = query + "'" + linv[0].trim() + "'";
                for (int l = 1; l < linv.length; l++) {
                    query = query + ",'" + linv[l].trim() + "'";
                }
                query = query + ")\n";
            } else {
                query = query
                        + " and Inventario.inventario LIKE '%" + dato.getInventario().trim() + "%'\n";
            }
        }
        if (dato.getNombre() != null) {
            //query = query
            //        + " and Inventario.nombre LIKE '%" + dato.getNombre().trim() + "%'\n";
            if (dato.getNombre().contains(",")) {
                query = query + "and CAST(Inventario.nombre as Varchar) IN (";
                String[] lnombre = dato.getNombre().split(",");
                query = query + "'" + lnombre[0].trim() + "'";
                for (int l = 1; l < lnombre.length; l++) {
                    query = query + ",'" + lnombre[l].trim() + "'";
                }
                query = query + ")\n";
            } else {
                query = query
                        + " and Inventario.nombre LIKE '%" + dato.getNombre().trim() + "%'\n";
            }
        }
        if (dato.getFecha() != null) {
            //query = query
            //        + " and Modelo.fechafin LIKE '%" + dato.getFecha().trim() + "%'\n";
            if (dato.getFecha().contains(",")) {
                query = query + "and CAST(Modelo.fechafin as Varchar) IN (";
                String[] lfecha = dato.getFecha().split(",");
                query = query + "'" + lfecha[0].trim() + "'";
                for (int l = 1; l < lfecha.length; l++) {
                    query = query + ",'" + lfecha[l].trim() + "'";
                }
                query = query + ")\n";
            } else {
                query = query
                        + " and Modelo.fechafin LIKE '%" + dato.getFecha().trim() + "%'\n";
            }
        }
        if (dato.getnModelo() != null) {
            //query = query1
            //        + " and Modelo.nombre LIKE '%" + dato.getnModelo().trim() + "%'\n";
            if (dato.getnModelo().contains(",")) {
                query = query + "and CAST(Modelo.nombre as Varchar) IN (";
                String[] lmodelo = dato.getnModelo().split(",");
                query = query + "'" + lmodelo[0].trim() + "'";
                for (int l = 1; l < lmodelo.length; l++) {
                    query = query + ",'" + lmodelo[l].trim() + "'";
                }
                query = query + ")\n";
            } else {
                query = query
                        + " and Modelo.nombre LIKE '%" + dato.getnModelo().trim() + "%'\n";
            }
        }
        if (dato.getnAmbiente() != null) {
            //query = query
            //        + " and Ambiente.nombre LIKE '%" + dato.getnAmbiente() + "%'\n";
            if (dato.getnAmbiente().contains(",")) {
                query = query + "and CAST(Ambiente.nombre as Varchar) IN (";
                String[] lnambiente = dato.getnAmbiente().split(",");
                query = query + "'" + lnambiente[0].trim() + "'";
                for (int l = 1; l < lnambiente.length; l++) {
                    query = query + ",'" + lnambiente[l].trim() + "'";
                }
                query = query + ")\n";
            } else {
                query = query
                        + " and Ambiente.nombre LIKE '%" + dato.getnAmbiente().trim() + "%'\n";
            }
        }
        if (dato.getNciudad() != null) {
            //query = query
            //        + " and Ciudad.nombre LIKE '%" + dato.getNciudad() + "%'\n";
            if (dato.getNciudad().contains(",")) {
                query = query + "and CAST(Ciudad.nombre as Varchar) IN (";
                String[] lnciudad = dato.getNciudad().split(",");
                query = query + "'" + lnciudad[0].trim() + "'";
                for (int l = 1; l < lnciudad.length; l++) {
                    query = query + ",'" + lnciudad[l].trim() + "'";
                }
                query = query + ")\n";
            } else {
                query = query
                        + " and Ciudad.nombre LIKE '%" + dato.getNciudad().trim() + "%'\n";
            }
        }
        if (dato.getNtipo() != null) {
            //query = query
            //        + " and Tipo.nombre LIKE '%" + dato.getNtipo().trim() + "%'\n";
            if (dato.getNtipo().contains(",")) {
                query = query + "and CAST(Tipo.nombre as Varchar) IN (";
                String[] lntipo = dato.getNtipo().split(",");
                query = query + "'" + lntipo[0].trim() + "'";
                for (int l = 1; l < lntipo.length; l++) {
                    query = query + ",'" + lntipo[l].trim() + "'";
                }
                query = query + ")\n";
            } else {
                query = query
                        + " and Tipo.nombre LIKE '%" + dato.getNtipo().trim() + "%'\n";
            }
        }
        if (dato.getNagencia() != null) {
            //query = query
            //        + " and Agencia.nombre LIKE '%" + dato.getNagencia().trim() + "%'\n";
            if (dato.getNagencia().contains(",")) {
                query = query + "and CAST(Agencia.nombre as Varchar) IN (";
                String[] lnagencia = dato.getNagencia().split(",");
                query = query + "'" + lnagencia[0].trim() + "'";
                for (int l = 1; l < lnagencia.length; l++) {
                    query = query + ",'" + lnagencia[l].trim() + "'";
                }
                query = query + ")\n";
            } else {
                query = query
                        + " and Agencia.nombre LIKE '%" + dato.getNagencia().trim() + "%'\n";
            }
        }
        if (dato.getnorion() != null) {
            //query = query
            //        + " and Orion.nombre LIKE '%" + dato.getnorion().trim() + "%'\n";
            if (dato.getnorion().contains(",")) {
                query = query + "and CAST(Orion.nombre as Varchar) IN (";
                String[] lnorion = dato.getnorion().split(",");
                query = query + "'" + lnorion[0].trim() + "'";
                for (int l = 1; l < lnorion.length; l++) {
                    query = query + ",'" + lnorion[l].trim() + "'";
                }
                query = query + ")\n";
            } else {
                query = query
                        + " and Orion.nombre LIKE '%" + dato.getnorion().trim() + "%'\n";
            }
        }
        if (dato.getSerie() != null) {
            if (dato.getSerie().contains(",")) {
                query = query + "and CAST(Inventario.serie as Varchar) IN (";
                String[] lserie = dato.getSerie().split(",");
                query = query + "'" + lserie[0].trim() + "'";
                for (int l = 1; l < lserie.length; l++) {
                    query = query + ",'" + lserie[l].trim() + "'";
                }
                query = query + ")\n";
            } else {
                query = query
                        + " and Inventario.serie LIKE '%" + dato.getSerie().trim() + "%'\n";
            }
        }
        if (dato.getnEquipo() != null) {
            if (dato.getnEquipo().contains(",")) {
                query = query + "and CAST(Hardware.nombre as Varchar) IN (";
                String[] lequipo = dato.getnEquipo().split(",");
                query = query + "'" + lequipo[0].trim() + "'";
                for (int l = 1; l < lequipo.length; l++) {
                    query = query + ",'" + lequipo[l].trim() + "'";
                }
                query = query + ")\n";
            } else {
                query = query
                        + " and Hardware.nombre LIKE '%" + dato.getnEquipo().trim() + "%'\n";
            }
        }
        if (dato.getnPropietario() != null) {
            if (dato.getnPropietario().contains(",")) {
                query = query + "and CAST(Propietario.nombre as Varchar) IN (";
                String[] lprop = dato.getnPropietario().split(",");
                query = query + "'" + lprop[0].trim() + "'";
                for (int l = 1; l < lprop.length; l++) {
                    query = query + ",'" + lprop[l].trim() + "'";
                }
                query = query + ")\n";
            } else {
                query = query
                        + " and Propietario.nombre LIKE '%" + dato.getnPropietario().trim() + "%'\n";
            }
        }
        if (dato.getPiso() != null) {
            query = query
                    + " and Inventario.piso = " + dato.getPiso() + "\n";
        }
        if (dato.getRack() != null) {
            query = query
                    + " and Inventario.rack = " + dato.getRack() + "\n";
        }
        if (dato.getUtil() != null) {
            query = query
                    + " and Inventario.util = " + dato.getUtil() + "\n";
        }

        if (dato.getFechafin() != null) {
            if (dato.getFechaini() != null) {
                query = query
                        + " and CAST( Modelo.fechafin as DATETIME)  >= CONVERT(DATETIME, '" + dato.getFechaini().trim() + "')\n"
                        + " and CAST( Modelo.fechafin as DATETIME)  <= CONVERT(DATETIME, '" + dato.getFechafin().trim() + "')\n";
            } else {
                query = query
                        + " and CAST( Modelo.fechafin as DATETIME)  <= CONVERT(DATETIME, '" + dato.getFechafin().trim() + "')\n";
            }
        }
        query = query
                + "ORDER BY CAST(Inventario.fecha as DATETIME) desc\n"
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

    public JSONArray obtenerListadoExcel(Inventario dato) {
        JSONArray lista = null;
        JSONParser parser = new JSONParser();
        String query
                = "SELECT  Inventario.id,Inventario.ip,\n"
                + "Inventario.piso,Inventario.rack,\n"
                + "Inventario.inventario, Inventario.fechabanco,Inventario.serie,\n"
                + "Inventario.util, Inventario.bpac,Inventario.so,\n"
                + "Inventario.critico, Inventario.opmger,Inventario.serie,\n"
                + "Inventario.nombre,Modelo.fechafin,\n"
                + "Modelo.nombre as nombremodelo,\n"
                + "Ambiente.nombre as nombreambiente,\n"
                + "Ciudad.nombre as nombreciudad,\n"
                + "Tipo.nombre as nombretipo,\n"
                + "Agencia.nombre as nombreagencia,\n"
                + "Orion.nombre as nombreorion,\n"
                + "Propietario.nombre as nombrepropietario,\n"
                + "Marca.nombre as marca,\n"
                + "Equipo.nombre as equipo,\n"
                + "Flash.nombre as flash,\n"
                + "Ram.nombre as ram\n"
                + "FROM Inventario \n"
                + "FULL OUTER JOIN Propietario       on(Inventario.idpropietario=Propietario.id)\n"
                + "FULL OUTER JOIN Orion             on(Inventario.idorion=Orion.id)\n"
                + "FULL OUTER JOIN Ambiente          on(Ambiente.id=Inventario.idambiente)\n"
                + "FULL OUTER JOIN Agencia           on(Inventario.idagencia=Agencia.id)\n"
                + "FULL OUTER JOIN Tipo              on(Tipo.id=Agencia.idtipo)\n"
                + "FULL OUTER JOIN Ciudad            on(Ciudad.id=Tipo.idciudad)\n"
                + "FULL OUTER JOIN Modelo            on(Inventario.idmodelo=Modelo.id)\n"
                + "FULL OUTER JOIN Hardware as Marca  on(Marca.id=Modelo.idmarca)\n"
                + "FULL OUTER JOIN Hardware as Equipo on(Equipo.id=Modelo.idequipo)\n"
                + "FULL OUTER JOIN Hardware as Flash  on(Flash.id=Modelo.idflash)\n"
                + "FULL OUTER JOIN Hardware as Ram    on(Ram.id=Modelo.idram)\n"
                + "WHERE Inventario.estado=" + dato.getEstado() + "\n";
        if (dato.getIp() != null) {
            //query = query
            //        + " and Inventario.ip LIKE '%" + dato.getIp().trim() + "%'\n";
            if (dato.getIp().contains(",")) {
                query = query + "and CAST(Inventario.ip as Varchar) IN (";
                String[] lip = dato.getIp().split(",");
                query = query + "'" + lip[0].trim() + "'";
                for (int l = 1; l < lip.length; l++) {
                    query = query + ",'" + lip[l].trim() + "'";
                }
                query = query + ")\n";
            } else {
                query = query
                        + " and Inventario.ip LIKE '%" + dato.getIp().trim() + "%'\n";
            }
        }
        if (dato.getSo() != null) {
            //query = query
            //        + " and Inventario.so LIKE '%" + dato.getSo().trim() + "%'\n";
            if (dato.getSo().contains(",")) {
                query = query + "and CAST(Inventario.so as Varchar) IN (";
                String[] lso = dato.getSo().split(",");
                query = query + "'" + lso[0].trim() + "'";
                for (int l = 1; l < lso.length; l++) {
                    query = query + ",'" + lso[l].trim() + "'";
                }
                query = query + ")\n";
            } else {
                query = query
                        + " and Inventario.so LIKE '%" + dato.getSo().trim() + "%'\n";
            }
        }
        if (dato.getInventario() != null) {
            if (dato.getInventario().contains(",")) {
                query = query + "and CAST(Inventario.inventario as Varchar) IN (";
                String[] linv = dato.getInventario().split(",");
                query = query + "'" + linv[0].trim() + "'";
                for (int l = 1; l < linv.length; l++) {
                    query = query + ",'" + linv[l].trim() + "'";
                }
                query = query + ")\n";
            } else {
                query = query
                        + " and Inventario.inventario LIKE '%" + dato.getInventario().trim() + "%'\n";
            }
        }
        if (dato.getNombre() != null) {
            //query = query
            //        + " and Inventario.nombre LIKE '%" + dato.getNombre().trim() + "%'\n";
            if (dato.getNombre().contains(",")) {
                query = query + "and CAST(Inventario.nombre as Varchar) IN (";
                String[] lnombre = dato.getNombre().split(",");
                query = query + "'" + lnombre[0].trim() + "'";
                for (int l = 1; l < lnombre.length; l++) {
                    query = query + ",'" + lnombre[l].trim() + "'";
                }
                query = query + ")\n";
            } else {
                query = query
                        + " and Inventario.nombre LIKE '%" + dato.getNombre().trim() + "%'\n";
            }
        }
        if (dato.getFecha() != null) {
            //query = query
            //        + " and Modelo.fechafin LIKE '%" + dato.getFecha().trim() + "%'\n";
            if (dato.getFecha().contains(",")) {
                query = query + "and CAST(Modelo.fechafin as Varchar) IN (";
                String[] lfecha = dato.getFecha().split(",");
                query = query + "'" + lfecha[0].trim() + "'";
                for (int l = 1; l < lfecha.length; l++) {
                    query = query + ",'" + lfecha[l].trim() + "'";
                }
                query = query + ")\n";
            } else {
                query = query
                        + " and Modelo.fechafin LIKE '%" + dato.getFecha().trim() + "%'\n";
            }
        }
        if (dato.getnModelo() != null) {
            //query = query1
            //        + " and Modelo.nombre LIKE '%" + dato.getnModelo().trim() + "%'\n";
            if (dato.getnModelo().contains(",")) {
                query = query + "and CAST(Modelo.nombre as Varchar) IN (";
                String[] lmodelo = dato.getnModelo().split(",");
                query = query + "'" + lmodelo[0].trim() + "'";
                for (int l = 1; l < lmodelo.length; l++) {
                    query = query + ",'" + lmodelo[l].trim() + "'";
                }
                query = query + ")\n";
            } else {
                query = query
                        + " and Modelo.nombre LIKE '%" + dato.getnModelo().trim() + "%'\n";
            }
        }
        if (dato.getnAmbiente() != null) {
            //query = query
            //        + " and Ambiente.nombre LIKE '%" + dato.getnAmbiente() + "%'\n";
            if (dato.getnAmbiente().contains(",")) {
                query = query + "and CAST(Ambiente.nombre as Varchar) IN (";
                String[] lnambiente = dato.getnAmbiente().split(",");
                query = query + "'" + lnambiente[0].trim() + "'";
                for (int l = 1; l < lnambiente.length; l++) {
                    query = query + ",'" + lnambiente[l].trim() + "'";
                }
                query = query + ")\n";
            } else {
                query = query
                        + " and Ambiente.nombre LIKE '%" + dato.getnAmbiente().trim() + "%'\n";
            }
        }
        if (dato.getNciudad() != null) {
            //query = query
            //        + " and Ciudad.nombre LIKE '%" + dato.getNciudad() + "%'\n";
            if (dato.getNciudad().contains(",")) {
                query = query + "and CAST(Ciudad.nombre as Varchar) IN (";
                String[] lnciudad = dato.getNciudad().split(",");
                query = query + "'" + lnciudad[0].trim() + "'";
                for (int l = 1; l < lnciudad.length; l++) {
                    query = query + ",'" + lnciudad[l].trim() + "'";
                }
                query = query + ")\n";
            } else {
                query = query
                        + " and Ciudad.nombre LIKE '%" + dato.getNciudad().trim() + "%'\n";
            }
        }
        if (dato.getNtipo() != null) {
            //query = query
            //        + " and Tipo.nombre LIKE '%" + dato.getNtipo().trim() + "%'\n";
            if (dato.getNtipo().contains(",")) {
                query = query + "and CAST(Tipo.nombre as Varchar) IN (";
                String[] lntipo = dato.getNtipo().split(",");
                query = query + "'" + lntipo[0].trim() + "'";
                for (int l = 1; l < lntipo.length; l++) {
                    query = query + ",'" + lntipo[l].trim() + "'";
                }
                query = query + ")\n";
            } else {
                query = query
                        + " and Tipo.nombre LIKE '%" + dato.getNtipo().trim() + "%'\n";
            }
        }
        if (dato.getNagencia() != null) {
            //query = query
            //        + " and Agencia.nombre LIKE '%" + dato.getNagencia().trim() + "%'\n";
            if (dato.getNagencia().contains(",")) {
                query = query + "and CAST(Agencia.nombre as Varchar) IN (";
                String[] lnagencia = dato.getNagencia().split(",");
                query = query + "'" + lnagencia[0].trim() + "'";
                for (int l = 1; l < lnagencia.length; l++) {
                    query = query + ",'" + lnagencia[l].trim() + "'";
                }
                query = query + ")\n";
            } else {
                query = query
                        + " and Agencia.nombre LIKE '%" + dato.getNagencia().trim() + "%'\n";
            }
        }
        if (dato.getnorion() != null) {
            //query = query
            //        + " and Orion.nombre LIKE '%" + dato.getnorion().trim() + "%'\n";
            if (dato.getnorion().contains(",")) {
                query = query + "and CAST(Orion.nombre as Varchar) IN (";
                String[] lnorion = dato.getnorion().split(",");
                query = query + "'" + lnorion[0].trim() + "'";
                for (int l = 1; l < lnorion.length; l++) {
                    query = query + ",'" + lnorion[l].trim() + "'";
                }
                query = query + ")\n";
            } else {
                query = query
                        + " and Orion.nombre LIKE '%" + dato.getnorion().trim() + "%'\n";
            }
        }
        if (dato.getSerie() != null) {
            if (dato.getSerie().contains(",")) {
                query = query + "and CAST(Inventario.serie as Varchar) IN (";
                String[] lserie = dato.getSerie().split(",");
                query = query + "'" + lserie[0].trim() + "'";
                for (int l = 1; l < lserie.length; l++) {
                    query = query + ",'" + lserie[l].trim() + "'";
                }
                query = query + ")\n";
            } else {
                query = query
                        + " and Inventario.serie LIKE '%" + dato.getSerie().trim() + "%'\n";
            }
        }
        if (dato.getnEquipo() != null) {
            if (dato.getnEquipo().contains(",")) {
                query = query + "and CAST(Hardware.nombre as Varchar) IN (";
                String[] lequipo = dato.getnEquipo().split(",");
                query = query + "'" + lequipo[0].trim() + "'";
                for (int l = 1; l < lequipo.length; l++) {
                    query = query + ",'" + lequipo[l].trim() + "'";
                }
                query = query + ")\n";
            } else {
                query = query
                        + " and Hardware.nombre LIKE '%" + dato.getnEquipo().trim() + "%'\n";
            }
        }
        if (dato.getnPropietario() != null) {
            if (dato.getnPropietario().contains(",")) {
                query = query + "and CAST(Propietario.nombre as Varchar) IN (";
                String[] lprop = dato.getnPropietario().split(",");
                query = query + "'" + lprop[0].trim() + "'";
                for (int l = 1; l < lprop.length; l++) {
                    query = query + ",'" + lprop[l].trim() + "'";
                }
                query = query + ")\n";
            } else {
                query = query
                        + " and Propietario.nombre LIKE '%" + dato.getnPropietario().trim() + "%'\n";
            }
        }
        if (dato.getPiso() != null) {
            query = query
                    + " and Inventario.piso = " + dato.getPiso() + "\n";
        }
        if (dato.getRack() != null) {
            query = query
                    + " and Inventario.rack = " + dato.getRack() + "\n";
        }
        if (dato.getUtil() != null) {
            query = query
                    + " and Inventario.util = " + dato.getUtil() + "\n";
        }

        if (dato.getFechafin() != null) {
            if (dato.getFechaini() != null) {
                query = query
                        + " and CAST( Modelo.fechafin as DATETIME)  >= CONVERT(DATETIME, '" + dato.getFechaini().trim() + "')\n"
                        + " and CAST( Modelo.fechafin as DATETIME)  <= CONVERT(DATETIME, '" + dato.getFechafin().trim() + "')\n";
            } else {
                query = query
                        + " and CAST( Modelo.fechafin as DATETIME)  <= CONVERT(DATETIME, '" + dato.getFechafin().trim() + "')\n";
            }
        }

        query = query
                + "ORDER BY CAST(Inventario.nombre as Varchar(1000)) asc\n"
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

    public Integer obtenerListadoSize(Inventario dato) {
        String query
                = "SELECT COUNT(Inventario.id)\n"
                + "FROM Inventario \n"
                + "FULL OUTER JOIN Orion on(Orion.id=Inventario.idorion)\n"
                + "FULL OUTER JOIN Ambiente on(Ambiente.id=Inventario.idambiente)\n"
                + "FULL OUTER JOIN Agencia on(Inventario.idagencia=Agencia.id)\n"
                + "FULL OUTER JOIN Tipo on(Tipo.id=Agencia.idtipo)\n"
                + "FULL OUTER JOIN Ciudad on(Ciudad.id=Tipo.idciudad)\n"
                + "FULL OUTER JOIN Modelo on(Inventario.idmodelo=Modelo.id)"
                + "WHERE Inventario.estado=" + dato.getEstado() + "\n";
        if (dato.getIp() != null) {
            //query = query
            //        + " and Inventario.ip LIKE '%" + dato.getIp().trim() + "%'\n";
            if (dato.getIp().contains(",")) {
                query = query + "and CAST(Inventario.ip as Varchar) IN (";
                String[] lip = dato.getIp().split(",");
                query = query + "'" + lip[0].trim() + "'";
                for (int l = 1; l < lip.length; l++) {
                    query = query + ",'" + lip[l].trim() + "'";
                }
                query = query + ")\n";
            } else {
                query = query
                        + " and Inventario.ip LIKE '%" + dato.getIp().trim() + "%'\n";
            }
        }
        if (dato.getSo() != null) {
            //query = query
            //        + " and Inventario.so LIKE '%" + dato.getSo().trim() + "%'\n";
            if (dato.getSo().contains(",")) {
                query = query + "and CAST(Inventario.so as Varchar) IN (";
                String[] lso = dato.getSo().split(",");
                query = query + "'" + lso[0].trim() + "'";
                for (int l = 1; l < lso.length; l++) {
                    query = query + ",'" + lso[l].trim() + "'";
                }
                query = query + ")\n";
            } else {
                query = query
                        + " and Inventario.so LIKE '%" + dato.getSo().trim() + "%'\n";
            }
        }
        if (dato.getInventario() != null) {
            if (dato.getInventario().contains(",")) {
                query = query + "and CAST(Inventario.inventario as Varchar) IN (";
                String[] linv = dato.getInventario().split(",");
                query = query + "'" + linv[0].trim() + "'";
                for (int l = 1; l < linv.length; l++) {
                    query = query + ",'" + linv[l].trim() + "'";
                }
                query = query + ")\n";
            } else {
                query = query
                        + " and Inventario.inventario LIKE '%" + dato.getInventario().trim() + "%'\n";
            }
        }
        if (dato.getNombre() != null) {
            //query = query
            //        + " and Inventario.nombre LIKE '%" + dato.getNombre().trim() + "%'\n";
            if (dato.getNombre().contains(",")) {
                query = query + "and CAST(Inventario.nombre as Varchar) IN (";
                String[] lnombre = dato.getNombre().split(",");
                query = query + "'" + lnombre[0].trim() + "'";
                for (int l = 1; l < lnombre.length; l++) {
                    query = query + ",'" + lnombre[l].trim() + "'";
                }
                query = query + ")\n";
            } else {
                query = query
                        + " and Inventario.nombre LIKE '%" + dato.getNombre().trim() + "%'\n";
            }
        }
        if (dato.getFecha() != null) {
            //query = query
            //        + " and Modelo.fechafin LIKE '%" + dato.getFecha().trim() + "%'\n";
            if (dato.getFecha().contains(",")) {
                query = query + "and CAST(Modelo.fechafin as Varchar) IN (";
                String[] lfecha = dato.getFecha().split(",");
                query = query + "'" + lfecha[0].trim() + "'";
                for (int l = 1; l < lfecha.length; l++) {
                    query = query + ",'" + lfecha[l].trim() + "'";
                }
                query = query + ")\n";
            } else {
                query = query
                        + " and Modelo.fechafin LIKE '%" + dato.getFecha().trim() + "%'\n";
            }
        }
        if (dato.getnModelo() != null) {
            //query = query1
            //        + " and Modelo.nombre LIKE '%" + dato.getnModelo().trim() + "%'\n";
            if (dato.getnModelo().contains(",")) {
                query = query + "and CAST(Modelo.nombre as Varchar) IN (";
                String[] lmodelo = dato.getnModelo().split(",");
                query = query + "'" + lmodelo[0].trim() + "'";
                for (int l = 1; l < lmodelo.length; l++) {
                    query = query + ",'" + lmodelo[l].trim() + "'";
                }
                query = query + ")\n";
            } else {
                query = query
                        + " and Modelo.nombre LIKE '%" + dato.getnModelo().trim() + "%'\n";
            }
        }
        if (dato.getnAmbiente() != null) {
            //query = query
            //        + " and Ambiente.nombre LIKE '%" + dato.getnAmbiente() + "%'\n";
            if (dato.getnAmbiente().contains(",")) {
                query = query + "and CAST(Ambiente.nombre as Varchar) IN (";
                String[] lnambiente = dato.getnAmbiente().split(",");
                query = query + "'" + lnambiente[0].trim() + "'";
                for (int l = 1; l < lnambiente.length; l++) {
                    query = query + ",'" + lnambiente[l].trim() + "'";
                }
                query = query + ")\n";
            } else {
                query = query
                        + " and Ambiente.nombre LIKE '%" + dato.getnAmbiente().trim() + "%'\n";
            }
        }
        if (dato.getNciudad() != null) {
            //query = query
            //        + " and Ciudad.nombre LIKE '%" + dato.getNciudad() + "%'\n";
            if (dato.getNciudad().contains(",")) {
                query = query + "and CAST(Ciudad.nombre as Varchar) IN (";
                String[] lnciudad = dato.getNciudad().split(",");
                query = query + "'" + lnciudad[0].trim() + "'";
                for (int l = 1; l < lnciudad.length; l++) {
                    query = query + ",'" + lnciudad[l].trim() + "'";
                }
                query = query + ")\n";
            } else {
                query = query
                        + " and Ciudad.nombre LIKE '%" + dato.getNciudad().trim() + "%'\n";
            }
        }
        if (dato.getNtipo() != null) {
            //query = query
            //        + " and Tipo.nombre LIKE '%" + dato.getNtipo().trim() + "%'\n";
            if (dato.getNtipo().contains(",")) {
                query = query + "and CAST(Tipo.nombre as Varchar) IN (";
                String[] lntipo = dato.getNtipo().split(",");
                query = query + "'" + lntipo[0].trim() + "'";
                for (int l = 1; l < lntipo.length; l++) {
                    query = query + ",'" + lntipo[l].trim() + "'";
                }
                query = query + ")\n";
            } else {
                query = query
                        + " and Tipo.nombre LIKE '%" + dato.getNtipo().trim() + "%'\n";
            }
        }
        if (dato.getNagencia() != null) {
            //query = query
            //        + " and Agencia.nombre LIKE '%" + dato.getNagencia().trim() + "%'\n";
            if (dato.getNagencia().contains(",")) {
                query = query + "and CAST(Agencia.nombre as Varchar) IN (";
                String[] lnagencia = dato.getNagencia().split(",");
                query = query + "'" + lnagencia[0].trim() + "'";
                for (int l = 1; l < lnagencia.length; l++) {
                    query = query + ",'" + lnagencia[l].trim() + "'";
                }
                query = query + ")\n";
            } else {
                query = query
                        + " and Agencia.nombre LIKE '%" + dato.getNagencia().trim() + "%'\n";
            }
        }
        if (dato.getnorion() != null) {
            //query = query
            //        + " and Orion.nombre LIKE '%" + dato.getnorion().trim() + "%'\n";
            if (dato.getnorion().contains(",")) {
                query = query + "and CAST(Orion.nombre as Varchar) IN (";
                String[] lnorion = dato.getnorion().split(",");
                query = query + "'" + lnorion[0].trim() + "'";
                for (int l = 1; l < lnorion.length; l++) {
                    query = query + ",'" + lnorion[l].trim() + "'";
                }
                query = query + ")\n";
            } else {
                query = query
                        + " and Orion.nombre LIKE '%" + dato.getnorion().trim() + "%'\n";
            }
        }
        if (dato.getSerie() != null) {
            if (dato.getSerie().contains(",")) {
                query = query + "and CAST(Inventario.serie as Varchar) IN (";
                String[] lserie = dato.getSerie().split(",");
                query = query + "'" + lserie[0].trim() + "'";
                for (int l = 1; l < lserie.length; l++) {
                    query = query + ",'" + lserie[l].trim() + "'";
                }
                query = query + ")\n";
            } else {
                query = query
                        + " and Inventario.serie LIKE '%" + dato.getSerie().trim() + "%'\n";
            }
        }
        if (dato.getPiso() != null) {
            query = query
                    + " and Inventario.piso = " + dato.getPiso() + "\n";
        }
        if (dato.getRack() != null) {
            query = query
                    + " and Inventario.rack = " + dato.getRack() + "\n";
        }
        if (dato.getUtil() != null) {
            query = query
                    + " and Inventario.util = " + dato.getUtil() + "\n";
        }

        if (dato.getFechafin() != null) {
            if (dato.getFechaini() != null) {
                query = query
                        + " and CAST( Modelo.fechafin as DATETIME)  >= CONVERT(DATETIME, '" + dato.getFechaini().trim() + "')\n"
                        + " and CAST( Modelo.fechafin as DATETIME)  <= CONVERT(DATETIME, '" + dato.getFechafin().trim() + "')\n";
            } else {
                query = query
                        + " and CAST( Modelo.fechafin as DATETIME)  <= CONVERT(DATETIME, '" + dato.getFechafin().trim() + "')\n";
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

    public JSONArray busquedaId(Inventario dato) {
        JSONArray lista = null;
        JSONParser parser = new JSONParser();
        String query
                = "SELECT id,nombre,serie, piso,rack,inventario,ip,so,estado,critico,opmger,util,bpac,usuario,fechabanco,fecha,usuarioc,fechac,\n"
                + "(SELECT id,nombre\n"
                + "FROM Ambiente \n"
                + "WHERE Ambiente.id=Inventario.idambiente\n"
                + "FOR JSON AUTO) as Ambiente,\n"
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
                + "WHERE Agencia.id=Inventario.idagencia\n"
                + "FOR JSON AUTO) as Agencia\n,"
                + "(SELECT id,nombre\n"
                + "FROM Modelo \n"
                + "WHERE Modelo.id=Inventario.idmodelo\n"
                + "FOR JSON AUTO) as Modelo,\n"
                + "(SELECT id,nombre\n"
                + "FROM Orion \n"
                + "WHERE Orion.id=Inventario.idorion\n"
                + "FOR JSON AUTO) as Orion,\n"
                + "(SELECT id,nombre\n"
                + "FROM Propietario \n"
                + "WHERE Propietario.id=Inventario.idpropietario\n"
                + "FOR JSON AUTO) as Propietario\n"
                + "FROM Inventario\n"
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

    public JSONArray busquedaNombre(Inventario dato) {
        JSONArray lista = null;
        JSONParser parser = new JSONParser();
        String query
                = "SELECT id, nombre\n"
                + "FROM Inventario\n"
                + "WHERE serie LIKE '" + dato.getSerie().trim() + "'  and estado=1\n"
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

    public JSONArray busquedaNombreInventario(Inventario dato) {
        JSONArray lista = null;
        JSONParser parser = new JSONParser();
        String query
                = "SELECT id, nombre\n"
                + "FROM Inventario\n"
                + "WHERE inventario LIKE '" + dato.getInventario() + "'  and estado=1\n"
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

    public boolean actualizar(Inventario dato) {

        String query
                = "UPDATE Inventario\n"
                + "   SET ip = '" + dato.getIp().trim() + "'\n";
        if (dato.getInventario() != null) {
            query = query
                    + "      ,inventario = '" + dato.getInventario().trim() + "'\n";
        } else {
            query = query
                    + "      ,inventario = " + dato.getInventario() + "\n";
        }
        if (dato.getFechabanco() != null) {
            query = query
                    + "      ,fechabanco = '" + dato.getFechabanco().trim() + "'\n";
        } else {
            query = query
                    + "      ,fechabanco = " + dato.getFechabanco() + "\n";
        }
        query = query
                + "      ,nombre = '" + dato.getNombre().trim() + "'\n"
                + "      ,serie = '" + dato.getSerie().trim() + "'\n"
                + "      ,so = '" + dato.getSo().trim() + "'\n"
                + "      ,usuario = '" + dato.getUsername().trim() + "'\n"
                + "      ,fecha = CURRENT_TIMESTAMP \n"
                + "      ,idambiente = " + dato.getIdAmbiente() + "\n"
                + "      ,idagencia = " + dato.getAgencia() + "\n"
                + "      ,idmodelo = " + dato.getIdModelo() + "\n"
                + "      ,idorion = " + dato.getIdOrion() + "\n"
                + "      ,idpropietario = " + dato.getIdPropietario() + "\n"
                + "      ,critico = " + dato.getCritico() + "\n"
                + "      ,opmger = " + dato.getOpmger() + "\n"
                + "      ,bpac = " + dato.getBpac() + "\n"
                + "      ,piso = " + dato.getPiso() + "\n"
                + "      ,rack = " + dato.getRack() + "\n"
                + "      ,util = " + dato.getUtil() + "\n"
                + "      ,estado = " + dato.getEstado() + " \n"
                + " WHERE id= " + dato.getId() + ";";
        String query2
                = "INSERT INTO logsinventario\n"
                + "           (idambiente\n"
                + "           ,idagencia\n"
                + "           ,idmodelo\n"
                + "           ,idorion\n"
                + "           ,idpropietario"
                + "           ,idinventario"
                + "           ,usuario"
                + "           ,fecha"
                + "           ,adicional)\n"
                + "     VALUES\n"
                + "           (\n"
                + "            " + dato.getIdAmbiente() + "\n"
                + "           ," + dato.getAgencia() + "\n"
                + "           ," + dato.getIdModelo() + "\n"
                + "           ," + dato.getIdOrion() + "\n"
                + "           ," + dato.getIdPropietario() + "\n"
                + "           ," + dato.getId() + "\n"
                + "           ,'" + dato.getUsername() + "'\n"
                + "           , CURRENT_TIMESTAMP \n"
                + "           ,'Nombre: " + dato.getNombre() + "\n"
                + "Ip: " + dato.getIp() + "\n"
                + "Serie: " + dato.getSerie() + "\n"
                + "Inventario: " + dato.getInventario() + "\n"
                + "Fecha Inventario: " + dato.getFechabanco() + "\n"
                + "So: " + dato.getSo() + "\n";
        if (dato.getPiso() == null) {
            query2 = query2 + "Piso:  \n";
        } else {
            query2 = query2 + "Piso: " + dato.getPiso() + "\n";
        }
        if (dato.getRack() == null) {
            query2 = query2 + "Rack:  \n";
        } else {
            query2 = query2 + "Rack: " + dato.getRack() + "\n";
        }
        if (dato.getUtil() == null) {
            query2 = query2 + "Utilizado:  \n";
        } else {
            query2 = query2 + "Utilizado: " + switchIntValue(dato.getUtil()) + "\n";
        }
        if (dato.getCritico() == null) {
            query2 = query2 + "Critico:  \n";
        } else {
            query2 = query2 + "Critico: " + switchIntValue(dato.getCritico()) + "\n";
        }
        if (dato.getOpmger() == null) {
            query2 = query2 + "NCM Opmger:  \n";
        } else {
            query2 = query2 + "NCM Opmger: " + switchIntValue(dato.getOpmger()) + "\n";
        }
        if (dato.getBpac() == null) {
            query2 = query2 + "BPAC:  \n";
        } else {
            query2 = query2 + "BPAC: " + switchIntValue(dato.getBpac()) + "\n";
        }
        query2 = query2
                + "Activado: " + switchIntValue(dato.getEstado()) + ""
                + "           ');";
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            con.getConnection().setAutoCommit(false);
            st = con.getConnection().prepareStatement(query);
            st.executeUpdate();
            st = con.getConnection().prepareStatement(query2);
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

    public boolean crear(Inventario dato) {
        String query
                = "INSERT INTO Inventario\n"
                + "           (id\n"
                + "           ,nombre\n";
        if (dato.getInventario() != null) {
            query = query
                    + "         ,inventario\n";
        }
        if (dato.getFechabanco() != null) {
            query = query
                    + "         ,fechabanco\n";
        }
        query = query
                + "           ,so\n"
                + "           ,serie\n"
                + "           ,ip\n"
                + "           ,usuario\n"
                + "           ,fecha\n"
                + "           ,usuarioc\n"
                + "           ,fechac\n"
                + "           ,critico\n"
                + "           ,opmger\n"
                + "           ,bpac\n"
                + "           ,idambiente\n"
                + "           ,idagencia\n"
                + "           ,idmodelo\n"
                + "           ,idorion\n"
                + "           ,piso\n"
                + "           ,rack\n"
                + "           ,util\n"
                + "           ,idpropietario)\n"
                + "     VALUES\n"
                + "           (NEXT VALUE FOR SeqInventario\n"
                + "           ,'" + dato.getNombre().trim() + "'\n";
        if (dato.getInventario() != null) {
            query = query
                    + ",'" + dato.getInventario().trim() + "'\n";
        }
        if (dato.getFechabanco() != null) {
            query = query
                    + ",'" + dato.getFechabanco().trim() + "'\n";
        }
        query = query
                + "           ,'" + dato.getSo().trim() + "'\n"
                + "           ,'" + dato.getSerie().trim() + "'\n"
                + "           ,'" + dato.getIp().trim() + "'\n"
                + "           ,'" + dato.getUsername().trim() + "'\n"
                + "           , CURRENT_TIMESTAMP \n"
                + "           ,'" + dato.getUsername().trim() + "'\n"
                + "           , CURRENT_TIMESTAMP \n"
                + "           ," + dato.getCritico() + "\n"
                + "           ," + dato.getOpmger() + "\n"
                + "           ," + dato.getBpac() + "\n"
                + "           ," + dato.getIdAmbiente() + "\n"
                + "           ," + dato.getAgencia() + "\n"
                + "           ," + dato.getIdModelo() + "\n"
                + "           ," + dato.getIdOrion() + "\n"
                + "           ," + dato.getPiso() + "\n"
                + "           ," + dato.getRack() + "\n"
                + "           ," + dato.getUtil() + "\n"
                + "           ," + dato.getIdPropietario() + ");";
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
                return false;
            }
        }
    }

    public boolean createExcel(Inventario dato, String path) {
        try {
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Inventario");
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
            cellherderRef.setCellValue("Listado de Equipos");
            cellherderRef.setCellStyle(headStInventario);

            sheet.addMergedRegion(new CellRangeAddress(
                    0, //first row (0-based)
                    1, //last row  (0-based)
                    0, //first column (0-based)
                    21 //last column  (0-based)
            ));

            Row header = sheet.createRow(2);
            XSSFFont font = ((XSSFWorkbook) workbook).createFont();
            font.setFontName("Calibri");
            font.setFontHeightInPoints((short) 10);
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
            headerCell.setCellValue("Agencia");
            headerCell.setCellStyle(headerStyle);

            headerCell = header.createCell(2);
            headerCell.setCellValue("Ubicacion");
            headerCell.setCellStyle(headerStyle);

            headerCell = header.createCell(3);
            headerCell.setCellValue("Ambiente ");
            headerCell.setCellStyle(headerStyle);

            headerCell = header.createCell(4);
            headerCell.setCellValue("Nombre del Equipo");
            headerCell.setCellStyle(headerStyle);

            headerCell = header.createCell(5);
            headerCell.setCellValue("Equipo");
            headerCell.setCellStyle(headerStyle);

            headerCell = header.createCell(6);
            headerCell.setCellValue("Marca");
            headerCell.setCellStyle(headerStyle);

            headerCell = header.createCell(7);
            headerCell.setCellValue("Modelo");
            headerCell.setCellStyle(headerStyle);

            headerCell = header.createCell(8);
            headerCell.setCellValue("Version del SO");
            headerCell.setCellStyle(headerStyle);

            headerCell = header.createCell(9);
            headerCell.setCellValue("Memoria Flash");
            headerCell.setCellStyle(headerStyle);

            headerCell = header.createCell(10);
            headerCell.setCellValue("Memoria RAM");
            headerCell.setCellStyle(headerStyle);

            headerCell = header.createCell(11);
            headerCell.setCellValue("Inventario Banco");
            headerCell.setCellStyle(headerStyle);

            headerCell = header.createCell(12);
            headerCell.setCellValue("Fecha de Adquisicion");
            headerCell.setCellStyle(headerStyle);

            headerCell = header.createCell(13);
            headerCell.setCellValue("Serie");
            headerCell.setCellStyle(headerStyle);

            headerCell = header.createCell(14);
            headerCell.setCellValue("IP Principal");
            headerCell.setCellStyle(headerStyle);

            headerCell = header.createCell(15);
            headerCell.setCellValue("Carpeta Orion");
            headerCell.setCellStyle(headerStyle);

            headerCell = header.createCell(16);
            headerCell.setCellValue("NCM OPManager");
            headerCell.setCellStyle(headerStyle);

            headerCell = header.createCell(17);
            headerCell.setCellValue("Propietario");
            headerCell.setCellStyle(headerStyle);

            headerCell = header.createCell(18);
            headerCell.setCellValue("Gestion BPAC");
            headerCell.setCellStyle(headerStyle);

            headerCell = header.createCell(19);
            headerCell.setCellValue("Utilizado");
            headerCell.setCellStyle(headerStyle);

            headerCell = header.createCell(20);
            headerCell.setCellValue("Equipos Criticos");
            headerCell.setCellStyle(headerStyle);

            headerCell = header.createCell(21);
            headerCell.setCellValue("Fecha Fin Soporte");
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

                String util = inventario.get("util") == null ? "No" : ((Boolean) inventario.get("util") ? "Si" : "No");
                String bpac = inventario.get("bpac") == null ? "No" : ((Boolean) inventario.get("bpac") ? "Si" : "No");
                String critico = inventario.get("critico") == null ? "No" : ((Boolean) inventario.get("critico") ? "Si" : "No");
                String opmger = inventario.get("opmger") == null ? "No" : ((Boolean) inventario.get("opmger") ? "Si" : "No");
                String ip = inventario.get("ip") == null ? "N/A" : inventario.get("ip").toString();
                String nombre = inventario.get("nombre") == null ? "N/A" : inventario.get("nombre").toString();
                String inv = inventario.get("inventario") == null ? "N/A" : inventario.get("inventario").toString();
                String fechabanco = inventario.get("fechabanco") == null ? "N/A" : inventario.get("fechabanco").toString();
                String serie = inventario.get("serie") == null ? "N/A" : inventario.get("serie").toString();
                String so = inventario.get("so") == null ? "N/A" : inventario.get("so").toString();
                JSONArray modelarray = (JSONArray) parser.parse(inventario.get("Modelo").toString());
                JSONObject model = (JSONObject) parser.parse(modelarray.get(0).toString());
                String modelo = model.get("nombremodelo") == null ? "N/A" : model.get("nombremodelo").toString();
                String fechafin = model.get("fechafin") == null ? "NO ANUNCIADO" : model.get("fechafin").toString();
                JSONArray ambientearray = (JSONArray) parser.parse(model.get("Ambiente").toString());
                JSONObject ambi = (JSONObject) parser.parse(ambientearray.get(0).toString());
                String ambiente = ambi.get("nombreambiente") == null ? "N/A" : ambi.get("nombreambiente").toString();

                JSONArray ciudadarray = (JSONArray) parser.parse(ambi.get("Ciudad").toString());
                JSONObject ciudad = (JSONObject) parser.parse(ciudadarray.get(0).toString());
                String cd = ciudad.get("nombreciudad") == null ? "N/A" : ciudad.get("nombreciudad").toString();
                JSONArray tipoarray = (JSONArray) parser.parse(ciudad.get("Tipo").toString());
                JSONObject tipo = (JSONObject) parser.parse(tipoarray.get(0).toString());
                String tp = tipo.get("nombretipo") == null ? "N/A" : tipo.get("nombretipo").toString();
                JSONArray agarray = (JSONArray) parser.parse(tipo.get("Agencia").toString());
                JSONObject agencia = (JSONObject) parser.parse(agarray.get(0).toString());
                String ag = agencia.get("nombreagencia") == null ? "N/A" : agencia.get("nombreagencia").toString();
                JSONArray orionarray = (JSONArray) parser.parse(agencia.get("Orion").toString());
                JSONObject orion = (JSONObject) parser.parse(orionarray.get(0).toString());
                String or = orion.get("nombreorion") == null ? "N/A" : orion.get("nombreorion").toString();
                JSONArray proparray = (JSONArray) parser.parse(orion.get("Propietario").toString());
                JSONObject propietario = (JSONObject) parser.parse(proparray.get(0).toString());
                String prp = propietario.get("nombrepropietario") == null ? "N/A" : propietario.get("nombrepropietario").toString();
                JSONArray marcaarray = (JSONArray) parser.parse(propietario.get("Marca").toString());
                JSONObject marca = (JSONObject) parser.parse(marcaarray.get(0).toString());
                String mc = marca.get("marca") == null ? "N/A" : marca.get("marca").toString();
                JSONArray equipoqarray = (JSONArray) parser.parse(marca.get("Equipo").toString());
                JSONObject equipo = (JSONObject) parser.parse(equipoqarray.get(0).toString());
                String eq = equipo.get("equipo") == null ? "N/A" : equipo.get("equipo").toString();
                JSONArray flasharray = (JSONArray) parser.parse(equipo.get("Flash").toString());
                JSONObject flash = (JSONObject) parser.parse(flasharray.get(0).toString());
                String fsh = flash.get("flash") == null ? "N/A" : flash.get("flash").toString();
                JSONArray ramarray = (JSONArray) parser.parse(flash.get("Ram").toString());
                JSONObject ram = (JSONObject) parser.parse(ramarray.get(0).toString());
                String rm = ram.get("ram") == null ? "N/A" : ram.get("ram").toString();

                String ubicacion;
                if (inventario.get("piso") == null) {
                    ubicacion = ambiente;
                } else {
                    if (inventario.get("rack") != null) {
                        ubicacion = "P" + inventario.get("piso").toString() + "-R" + inventario.get("rack").toString();
                    } else {
                        ubicacion = "P" + inventario.get("piso").toString() + "-R1";
                    }
                }

                row = sheet.createRow(fila);

                Cell cell = row.createCell(celda);
                cell.setCellValue(cd);
                cell.setCellStyle(style);
                celda++;

                cell = row.createCell(celda);
                cell.setCellValue(ag);
                cell.setCellStyle(style);
                celda++;

                cell = row.createCell(celda);
                cell.setCellValue(ubicacion);
                cell.setCellStyle(style);
                celda++;

                cell = row.createCell(celda);
                cell.setCellValue(ambiente);
                cell.setCellStyle(style);
                celda++;

                cell = row.createCell(celda);
                cell.setCellValue(nombre);
                cell.setCellStyle(style);
                celda++;

                cell = row.createCell(celda);
                cell.setCellValue(eq);
                cell.setCellStyle(style);
                celda++;

                cell = row.createCell(celda);
                cell.setCellValue(mc);
                cell.setCellStyle(style);
                celda++;

                cell = row.createCell(celda);
                cell.setCellValue(modelo);
                cell.setCellStyle(style);
                celda++;

                cell = row.createCell(celda);
                cell.setCellValue(so);
                cell.setCellStyle(style);
                celda++;

                cell = row.createCell(celda);
                cell.setCellValue(fsh);
                cell.setCellStyle(style);
                celda++;

                cell = row.createCell(celda);
                cell.setCellValue(rm);
                cell.setCellStyle(style);
                celda++;

                cell = row.createCell(celda);
                cell.setCellValue(inv);
                cell.setCellStyle(style);
                celda++;

                cell = row.createCell(celda);
                cell.setCellValue(fechabanco);
                cell.setCellStyle(style);
                celda++;

                cell = row.createCell(celda);
                cell.setCellValue(serie);
                cell.setCellStyle(style);
                celda++;

                cell = row.createCell(celda);
                cell.setCellValue(ip);
                cell.setCellStyle(style);
                celda++;

                cell = row.createCell(celda);
                cell.setCellValue(or);
                cell.setCellStyle(style);
                celda++;

                cell = row.createCell(celda);
                cell.setCellValue(opmger);
                cell.setCellStyle(style);
                celda++;

                cell = row.createCell(celda);
                cell.setCellValue(prp);
                cell.setCellStyle(style);
                celda++;

                cell = row.createCell(celda);
                cell.setCellValue(bpac);
                cell.setCellStyle(style);
                celda++;

                cell = row.createCell(celda);
                cell.setCellValue(util);
                cell.setCellStyle(style);
                celda++;

                cell = row.createCell(celda);
                cell.setCellValue(critico);
                cell.setCellStyle(style);
                celda++;

                cell = row.createCell(celda);
                cell.setCellValue(fechafin);
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
            sheet.autoSizeColumn(11);
            sheet.autoSizeColumn(12);
            sheet.autoSizeColumn(13);
            sheet.autoSizeColumn(14);
            sheet.autoSizeColumn(15);
            sheet.autoSizeColumn(16);
            sheet.autoSizeColumn(17);
            sheet.autoSizeColumn(18);
            sheet.autoSizeColumn(19);
            sheet.autoSizeColumn(20);
            sheet.autoSizeColumn(21);

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

    public String switchIntValue(Integer v) {
        String res = "NULL";
        switch (v) {
            case 0:
                return "NO";
            case 1:
                return "SI";
        }
        return res;
    }

    public JSONArray obtenerListadoLogs(Inventario dato) {
        JSONArray lista = null;
        JSONParser parser = new JSONParser();

        String query
                = "SELECT  logsinventario.usuario,logsinventario.fecha,logsinventario.adicional,\n"
                + "Modelo.nombre as nombremodelo,\n"//listo
                + "Ambiente.nombre as nombreambiente,\n"//listo
                + "Agencia.nombre as nombreagencia,\n"//listo
                + "Orion.nombre as nombreorion,\n"//listo
                + "Propietario.nombre as nombrepropietario\n"//listo
                + "FROM logsinventario \n"
                + "FULL OUTER JOIN Orion on(Orion.id=logsinventario.idorion)\n"
                + "FULL OUTER JOIN Ambiente on(Ambiente.id=logsinventario.idambiente)\n"
                + "FULL OUTER JOIN Agencia on(logsinventario.idagencia=Agencia.id)\n"
                + "FULL OUTER JOIN Modelo on(logsinventario.idmodelo=Modelo.id)\n"
                + "FULL OUTER JOIN Propietario on(logsinventario.idpropietario=Propietario.id)\n"
                + "WHERE logsinventario.idinventario=" + dato.getId() + "\n";

        query = query
                + "ORDER BY CAST(logsinventario.fecha as DATETIME) desc\n"
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

    public Integer obtenerListadoLogsSize(Inventario dato) {
        String query
                = "SELECT COUNT(logsinventario.idagencia)\n"
                + "FROM logsinventario \n"
                + "FULL OUTER JOIN Orion on(Orion.id=logsinventario.idorion)\n"
                + "FULL OUTER JOIN Ambiente on(Ambiente.id=logsinventario.idambiente)\n"
                + "FULL OUTER JOIN Agencia on(logsinventario.idagencia=Agencia.id)\n"
                + "FULL OUTER JOIN Modelo on(logsinventario.idmodelo=Modelo.id)\n"
                + "FULL OUTER JOIN Propietario on(logsinventario.idpropietario=Propietario.id)\n"
                + "WHERE logsinventario.idinventario=" + dato.getId() + "\n;";
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

    //DISPOSITIVOS EN INVNETARIO
    public JSONObject dashboardTotal() {
        JSONObject obj = null;
        JSONParser parser = new JSONParser();
        String query
                = "SELECT COUNT(id) as c,\n"
                + "(Select Count(id)FROM Inventario WHERE estado = 1 and idpropietario=4 ) as b,\n"
                + "(Select Count(id)FROM Inventario WHERE estado = 1 and idpropietario!=4 ) as n\n"
                + "FROM Inventario \n"
                + "WHERE estado = 1\n"
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

            while (rs.next()) {
                rsst = rsst + rs.getString(1);
            }

            obj = rsst.length() <= 0 ? null : (JSONObject) ((JSONArray) parser.parse(rsst)).get(0);
            return obj;
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

    public JSONObject dashboardTotalFecha() {
        JSONObject obj = null;
        JSONParser parser = new JSONParser();
        String query
                = "SELECT COUNT(Inventario.id) as c,\n"
                + "(SELECT COUNT(Inventario.id)FROM Inventario INNER JOIN Modelo ON Modelo.id = Inventario.idmodelo WHERE Modelo.fechafin <= CURRENT_TIMESTAMP and Inventario.estado =1 and Inventario.idpropietario=4) as b,\n"
                + "(SELECT COUNT(Inventario.id)FROM Inventario INNER JOIN Modelo ON Modelo.id = Inventario.idmodelo WHERE Modelo.fechafin <= CURRENT_TIMESTAMP and Inventario.estado =1 and Inventario.idpropietario!=4) as n \n"
                + "FROM Inventario\n"
                + "INNER JOIN Modelo\n"
                + "ON Modelo.id = Inventario.idmodelo\n"
                + "WHERE Modelo.fechafin <= CURRENT_TIMESTAMP and Inventario.estado =1\n"
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

            while (rs.next()) {
                rsst = rsst + rs.getString(1);
            }

            obj = rsst.length() <= 0 ? null : (JSONObject) ((JSONArray) parser.parse(rsst)).get(0);
            return obj;
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

    public JSONObject dashboardTotalFechaProximo() {
        JSONObject obj = null;
        JSONParser parser = new JSONParser();
        String query
                = "SELECT COUNT(Inventario.id) as c,\n"
                + "(SELECT COUNT(Inventario.id) FROM Inventario INNER JOIN Modelo ON Modelo.id = Inventario.idmodelo WHERE Modelo.fechafin <= DATEADD(YEAR,1,CURRENT_TIMESTAMP) and Modelo.fechafin >= CURRENT_TIMESTAMP  and Inventario.estado =1 and Inventario.idpropietario=4 ) as b, \n"
                + "(SELECT COUNT(Inventario.id) FROM Inventario INNER JOIN Modelo ON Modelo.id = Inventario.idmodelo WHERE Modelo.fechafin <= DATEADD(YEAR,1,CURRENT_TIMESTAMP) and Modelo.fechafin >= CURRENT_TIMESTAMP  and Inventario.estado =1 and Inventario.idpropietario!=4 ) as n \n"
                + "FROM Inventario INNER JOIN Modelo ON Modelo.id = Inventario.idmodelo\n"
                + "WHERE Modelo.fechafin <= DATEADD(YEAR,1,CURRENT_TIMESTAMP) and "
                + "Modelo.fechafin >= CURRENT_TIMESTAMP  and Inventario.estado =1\n"
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

            while (rs.next()) {
                rsst = rsst + rs.getString(1);
            }

            obj = rsst.length() <= 0 ? null : (JSONObject) ((JSONArray) parser.parse(rsst)).get(0);
            return obj;
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

    //DISPOSITIVOS UTILZADOS en Inventario
    public JSONObject dashboardTotalCritico() {
        JSONObject obj = null;
        JSONParser parser = new JSONParser();
        String query
                = "SELECT COUNT(id) as c,\n"
                + "(SELECT COUNT(id) FROM Inventario WHERE critico = 1 and util = 1 and estado = 1 and idpropietario=4) as b,\n"
                + "(SELECT COUNT(id) FROM Inventario WHERE critico = 1 and util = 1 and estado = 1 and idpropietario!=4) as n\n"
                + "FROM Inventario WHERE critico = 1 and util = 1 and estado = 1\n"
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

            while (rs.next()) {
                rsst = rsst + rs.getString(1);
            }

            obj = rsst.length() <= 0 ? null : (JSONObject) ((JSONArray) parser.parse(rsst)).get(0);
            return obj;
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

    public JSONObject dashboardTotalBpac() {
        JSONObject obj = null;
        JSONParser parser = new JSONParser();
        String query
                = "SELECT COUNT(id) as c,\n"
                + "(SELECT COUNT(id) FROM Inventario WHERE bpac = 1 and util = 1 and estado = 1 and idpropietario =4)as b,\n"
                + "(SELECT COUNT(id) FROM Inventario WHERE bpac = 1 and util = 1 and estado = 1 and idpropietario !=4)as n \n"
                + "FROM Inventario WHERE bpac = 1 and util = 1 and estado = 1\n"
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

            while (rs.next()) {
                rsst = rsst + rs.getString(1);
            }

            obj = rsst.length() <= 0 ? null : (JSONObject) ((JSONArray) parser.parse(rsst)).get(0);
            return obj;
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

    public JSONObject dashboardTotalOpmger() {
        JSONObject obj = null;
        JSONParser parser = new JSONParser();
        String query
                = "SELECT COUNT(id) as c,\n"
                + "(SELECT COUNT(id) FROM Inventario WHERE opmger = 1 and util = 1 and estado =1 and idpropietario = 4) as b,\n"
                + "(SELECT COUNT(id) FROM Inventario WHERE opmger = 1 and util = 1 and estado =1 and idpropietario = 4) as n\n"
                + "FROM Inventario WHERE opmger = 1 and util = 1 and estado =1\n"
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

            while (rs.next()) {
                rsst = rsst + rs.getString(1);
            }

            obj = rsst.length() <= 0 ? null : (JSONObject) ((JSONArray) parser.parse(rsst)).get(0);
            return obj;
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

    public JSONObject dashboardTotalUtil() {
        JSONObject obj = null;
        JSONParser parser = new JSONParser();
        String query
                = "SELECT COUNT(id) as c,\n"
                + "(SELECT COUNT(id) FROM Inventario WHERE util = 1 and util = 1 and estado = 1 and idpropietario = 4) as b, \n"
                + "(SELECT COUNT(id) FROM Inventario WHERE util = 1 and util = 1 and estado = 1 and idpropietario!=4) as n \n"
                + "FROM Inventario WHERE util = 1 and util = 1 and estado = 1\n"
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

            while (rs.next()) {
                rsst = rsst + rs.getString(1);
            }

            obj = rsst.length() <= 0 ? null : (JSONObject) ((JSONArray) parser.parse(rsst)).get(0);
            return obj;
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

    public JSONObject dashboardUtilFechaProximo() {
        JSONObject obj = null;
        JSONParser parser = new JSONParser();
        String query
                = "SELECT COUNT(Inventario.id) as c, \n"
                + "(SELECT COUNT(Inventario.id) FROM Inventario INNER JOIN Modelo ON Modelo.id = Inventario.idmodelo WHERE Modelo.fechafin <= DATEADD(YEAR,1,CURRENT_TIMESTAMP) and Modelo.fechafin >= CURRENT_TIMESTAMP  and Inventario.util = 1 and Inventario.estado =1 and Inventario.idpropietario =4) as b, \n"
                + "(SELECT COUNT(Inventario.id) FROM Inventario INNER JOIN Modelo ON Modelo.id = Inventario.idmodelo WHERE Modelo.fechafin <= DATEADD(YEAR,1,CURRENT_TIMESTAMP) and Modelo.fechafin >= CURRENT_TIMESTAMP  and Inventario.util = 1 and Inventario.estado =1 and Inventario.idpropietario !=4) as n \n"
                + "FROM Inventario INNER JOIN Modelo ON Modelo.id = Inventario.idmodelo\n"
                + "WHERE Modelo.fechafin <= DATEADD(YEAR,1,CURRENT_TIMESTAMP) and "
                + "Modelo.fechafin >= CURRENT_TIMESTAMP  and Inventario.util = 1 and Inventario.estado =1\n"
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

            while (rs.next()) {
                rsst = rsst + rs.getString(1);
            }

            obj = rsst.length() <= 0 ? null : (JSONObject) ((JSONArray) parser.parse(rsst)).get(0);
            return obj;
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

    public JSONObject dashboardUtilFecha() {
        JSONObject obj = null;
        JSONParser parser = new JSONParser();
        String query
                = "SELECT COUNT(Inventario.id) as c,\n"
                + "(SELECT COUNT(Inventario.id) FROM Inventario INNER JOIN Modelo ON Modelo.id = Inventario.idmodelo WHERE Modelo.fechafin <= CURRENT_TIMESTAMP and  Inventario.util = 1 and  Inventario.estado =1 and Inventario.idpropietario = 4) as b,\n"
                + "(SELECT COUNT(Inventario.id) FROM Inventario INNER JOIN Modelo ON Modelo.id = Inventario.idmodelo WHERE Modelo.fechafin <= CURRENT_TIMESTAMP and  Inventario.util = 1 and  Inventario.estado =1 and Inventario.idpropietario != 4) as n \n"                
                + "FROM Inventario INNER JOIN Modelo ON Modelo.id = Inventario.idmodelo\n"
                + "WHERE Modelo.fechafin <= CURRENT_TIMESTAMP and  Inventario.util = 1 and  Inventario.estado =1\n"
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

            while (rs.next()) {
                rsst = rsst + rs.getString(1);
            }

            obj = rsst.length() <= 0 ? null : (JSONObject) ((JSONArray) parser.parse(rsst)).get(0);
            return obj;
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

    //DISPOSITIVOS EN BODEGA
    public JSONObject dashboardBodega() {
        JSONObject obj = null;
        JSONParser parser = new JSONParser();
        String query
                = "SELECT COUNT(id) as c,\n"
                + "(SELECT COUNT(id) FROM Inventario WHERE idambiente = 4 and estado =1 and idpropietario =4 ) as b,\n"
                + "(SELECT COUNT(id) FROM Inventario WHERE idambiente = 4 and estado =1 and idpropietario !=4 ) as n\n"
                + "FROM Inventario WHERE idambiente = 4 and estado =1\n"
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

            while (rs.next()) {
                rsst = rsst + rs.getString(1);
            }

            obj = rsst.length() <= 0 ? null : (JSONObject) ((JSONArray) parser.parse(rsst)).get(0);
            return obj;
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

    public JSONObject dashboardBodegaFechaProximo() {
        JSONObject obj = null;
        JSONParser parser = new JSONParser();
        String query
                = "SELECT COUNT(Inventario.id) as c,\n"
                + "(SELECT COUNT(Inventario.id) FROM Inventario INNER JOIN Modelo ON Modelo.id = Inventario.idmodelo WHERE Modelo.fechafin <= DATEADD(YEAR,1,CURRENT_TIMESTAMP) and Modelo.fechafin >= CURRENT_TIMESTAMP and Inventario.idambiente =4 and Inventario.estado =1 and Inventario.idpropietario = 4) as b,\n"
                + "(SELECT COUNT(Inventario.id) FROM Inventario INNER JOIN Modelo ON Modelo.id = Inventario.idmodelo WHERE Modelo.fechafin <= DATEADD(YEAR,1,CURRENT_TIMESTAMP) and Modelo.fechafin >= CURRENT_TIMESTAMP and Inventario.idambiente =4 and Inventario.estado =1 and Inventario.idpropietario != 4) as n\n"
                + "FROM Inventario INNER JOIN Modelo ON Modelo.id = Inventario.idmodelo\n"
                + "WHERE Modelo.fechafin <= DATEADD(YEAR,1,CURRENT_TIMESTAMP) and \n"
                + "Modelo.fechafin >= CURRENT_TIMESTAMP and Inventario.idambiente =4 and Inventario.estado =1\n"
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

            while (rs.next()) {
                rsst = rsst + rs.getString(1);
            }

            obj = rsst.length() <= 0 ? null : (JSONObject) ((JSONArray) parser.parse(rsst)).get(0);
            return obj;
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

    public JSONObject dashboardBodegaFecha() {
        JSONObject obj = null;
        JSONParser parser = new JSONParser();
        String query
                = "SELECT COUNT(Inventario.id) as c,\n"
                + "(SELECT COUNT(Inventario.id) FROM Inventario INNER JOIN Modelo ON Modelo.id = Inventario.idmodelo WHERE Modelo.fechafin <= CURRENT_TIMESTAMP and Inventario.idambiente =4 and Inventario.estado =1 and Inventario.idpropietario =4) as b,\n"
                + "(SELECT COUNT(Inventario.id) FROM Inventario INNER JOIN Modelo ON Modelo.id = Inventario.idmodelo WHERE Modelo.fechafin <= CURRENT_TIMESTAMP and Inventario.idambiente =4 and Inventario.estado =1 and Inventario.idpropietario !=4) as n\n"
                + "FROM Inventario INNER JOIN Modelo ON Modelo.id = Inventario.idmodelo\n"
                + "WHERE Modelo.fechafin <= CURRENT_TIMESTAMP and Inventario.idambiente =4 and Inventario.estado =1\n"
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

            while (rs.next()) {
                rsst = rsst + rs.getString(1);
            }

            obj = rsst.length() <= 0 ? null : (JSONObject) ((JSONArray) parser.parse(rsst)).get(0);
            return obj;
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
