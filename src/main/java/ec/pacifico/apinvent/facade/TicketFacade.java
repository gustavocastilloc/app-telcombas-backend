/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.pacifico.apinvent.facade;

import ec.pacifico.apinvent.entity.ActHeader;
import ec.pacifico.apinvent.entity.Actividades;
import ec.pacifico.apinvent.entity.RangoTiempo;
import ec.pacifico.apinvent.entity.TTHeader;
import ec.pacifico.apinvent.entity.Ticket;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xddf.usermodel.PresetColor;
import org.apache.poi.xddf.usermodel.XDDFColor;
import org.apache.poi.xddf.usermodel.XDDFShapeProperties;
import org.apache.poi.xddf.usermodel.XDDFSolidFillProperties;
import org.apache.poi.xddf.usermodel.chart.AxisCrosses;
import org.apache.poi.xddf.usermodel.chart.AxisPosition;
import org.apache.poi.xddf.usermodel.chart.BarDirection;
import org.apache.poi.xddf.usermodel.chart.ChartTypes;
import org.apache.poi.xddf.usermodel.chart.LegendPosition;
import org.apache.poi.xddf.usermodel.chart.XDDFBarChartData;
import org.apache.poi.xddf.usermodel.chart.XDDFCategoryAxis;
import org.apache.poi.xddf.usermodel.chart.XDDFChartData;
import org.apache.poi.xddf.usermodel.chart.XDDFChartLegend;
import org.apache.poi.xddf.usermodel.chart.XDDFDataSource;
import org.apache.poi.xddf.usermodel.chart.XDDFDataSourcesFactory;
import org.apache.poi.xddf.usermodel.chart.XDDFNumericalDataSource;
import org.apache.poi.xddf.usermodel.chart.XDDFValueAxis;
import org.apache.poi.xssf.usermodel.XSSFChart;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
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
public class TicketFacade {

    private Conexion con;

    public TicketFacade(Conexion con) {
        this.con = con;
    }

    public JSONArray obtenerListado(Ticket dato) {
        JSONArray lista = null;
        JSONParser parser = new JSONParser();
        String query
                = "Select \n"
                + "CONVERT(Varchar,Ticket.time0,20) as time0,CONVERT(Varchar,Ticket.time1,20) as time1,CONVERT(Varchar,Ticket.time2,20) as time2,\n"
                + "Ticket.id,Ticket.estado,Ticket.LAN,Ticket.tcompleto,Ticket.tdias,Ticket.tmins,Ticket.abierto,\n"
                + "Agencia.nombre as agencia,\n"
                + "Tipo.nombre as tipo,\n"
                + "Ciudad.nombre as ciudad,\n"
                + "TTHeader.id,TTHeader.ttproveedor,TTHeader.tthabierto,TTHeader.conteo,CONVERT(CHAR(8),TTHeader.fecha,112) as fecha,TTHeader.tecnicorespon,TTHeader.tecnicoreporte,\n"
                + "Propietario.nombre as proveedor,\n"
                + "Problema.nombre as problema\n"
                + "FROM Ticket\n"
                + "FULL OUTER JOIN Agencia on(Ticket.idagencia=Agencia.id)\n"
                + "FULL OUTER JOIN Tipo on(Tipo.id=Agencia.idtipo)\n"
                + "FULL OUTER JOIN Ciudad on(Ciudad.id=Tipo.idciudad)\n"
                + "FULL OUTER JOIN TTHeader on(TTHeader.id=Ticket.idheader)\n"
                + "FULL OUTER JOIN Problema on(TTHeader.idproblema=Problema.id)\n"
                + "FULL OUTER JOIN Propietario on(TTHeader.idproveedor=Propietario.id)\n"
                + "WHERE \n";
        if (dato.getEstado() != null) {
            query = query + "Ticket.estado=" + dato.getEstado() + "\n";
        } else {
            query = query + "Ticket.estado !=0 \n";
        }

        if (dato.getLan() != null) {
            query = query
                    + " and Ticket.LAN = " + dato.getLan() + "\n";
        }

        if (dato.getTecnicoreporte() != null) {
            query = query
                    + " and TTHeader.tecnicoreporte LIKE '%" + dato.getTecnicoreporte() + "%'\n";
        }
        if (dato.getTecnicorespon() != null) {
            query = query
                    + " and TTHeader.tecnicorespon LIKE '%" + dato.getTecnicorespon() + "%'\n";
        }

        if (dato.getTime0() != null && dato.getTime2() != null) {
            query = query
                    + " and TTHeader.fecha between '" + dato.getTime0() + "' and '" + dato.getTime2() + "'\n";
        }

        if (dato.getMin() != null) {
            query = query
                    + " and Ticket.tmins >= " + dato.getMin() + "\n";
        }

        if (dato.getMax() != null) {
            query = query
                    + " and Ticket.tmins <= " + dato.getMax() + "\n";
        }

        if (dato.getAbierto() != null) {
            if (dato.getAbierto() == 1) {
                query = query
                        + " and (TTHeader.tthabierto = " + dato.getAbierto() + " and Ticket.abierto = " + dato.getAbierto() + ")\n";
            }
            if (dato.getAbierto() == 0) {
                query = query
                        + " and (TTHeader.tthabierto = " + dato.getAbierto() + " or Ticket.abierto = " + dato.getAbierto() + ")\n";
            }
        }

        if (dato.getTcompleto() != null) {
            query = query
                    + " and Ticket.tcompleto LIKE '" + dato.getTcompleto().trim() + "'\n";
        }

        if (dato.getTicket() != null) {
            if (dato.getTicket().contains(",")) {
                query = query + "and CAST(TTHeader.fecha as Varchar) IN (";
                String[] lfecha = dato.getTicket().split(",");
                query = query + "'" + lfecha[0].trim() + "'";
                for (int l = 1; l < lfecha.length; l++) {
                    query = query + ",'" + lfecha[l].trim() + "'";
                }
                query = query + ")\n";
            } else {
                query = query
                        + " and TTHeader.fecha LIKE '%" + dato.getTicket().trim() + "%'\n";
            }
        }

        if (dato.getNciudad() != null) {
            if (dato.getNciudad().contains(",")) {
                query = query + "and CAST(Ciudad.nombre as Varchar(100)) IN (";
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
            if (dato.getNtipo().contains(",")) {
                query = query + "and CAST(Tipo.nombre as Varchar(100)) IN (";
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
            if (dato.getNagencia().contains(",")) {
                query = query + "and CAST(Agencia.nombre as Varchar(100)) IN (";
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
        if (dato.getProblema() != null) {
            if (dato.getProblema().contains(",")) {
                query = query + "and CAST(Problema.nombre as Varchar(100)) IN (";
                String[] lntipo = dato.getProblema().split(",");
                query = query + "'" + lntipo[0].trim() + "'";
                for (int l = 1; l < lntipo.length; l++) {
                    query = query + ",'" + lntipo[l].trim() + "'";
                }
                query = query + ")\n";
            } else {
                query = query
                        + " and Problema.nombre LIKE '%" + dato.getProblema().trim() + "%'\n";
            }
        }

        if (dato.getProveedor() != null) {
            if (dato.getProveedor().contains("N/A")) {
                query = query
                        + " and TTHeader.idproveedor IS NULL\n";
            } else {
                if (dato.getProveedor().contains(",")) {
                    query = query + "and CAST(Propietario.nombre as Varchar(100)) IN (";
                    String[] lntipo = dato.getProveedor().split(",");
                    query = query + "'" + lntipo[0].trim() + "'";
                    for (int l = 1; l < lntipo.length; l++) {
                        query = query + ",'" + lntipo[l].trim() + "'";
                    }
                    query = query + ")\n";
                } else {
                    query = query
                            + " and Propietario.nombre LIKE '%" + dato.getProveedor().trim() + "%'\n";
                }
            }
        }

        query = query
                + "ORDER BY Ticket.idheader desc\n"
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

    public Integer obtenerListadoSize(Ticket dato) {
        String query
                = "SELECT COUNT(Ticket.id)\n"
                + "FROM Ticket\n"
                + "FULL OUTER JOIN Agencia on(Ticket.idagencia=Agencia.id)\n"
                + "FULL OUTER JOIN Tipo on(Tipo.id=Agencia.idtipo)\n"
                + "FULL OUTER JOIN Ciudad on(Ciudad.id=Tipo.idciudad)\n"
                + "FULL OUTER JOIN TTHeader on(TTHeader.id=Ticket.idheader)\n"
                + "FULL OUTER JOIN Problema on(TTHeader.idproblema=Problema.id)\n"
                + "FULL OUTER JOIN Propietario on(TTHeader.idproveedor=Propietario.id)\n"
                + "WHERE \n";

        if (dato.getEstado() != null) {
            query = query + "Ticket.estado=" + dato.getEstado() + "\n";
        } else {
            query = query + "Ticket.estado !=0 \n";
        }

        if (dato.getLan() != null) {
            query = query
                    + " and Ticket.LAN = " + dato.getLan() + "\n";
        }

        if (dato.getTecnicoreporte() != null) {
            query = query
                    + " and TTHeader.tecnicoreporte LIKE '%" + dato.getTecnicoreporte() + "%'\n";
        }
        if (dato.getTecnicorespon() != null) {
            query = query
                    + " and TTHeader.tecnicorespon LIKE '%" + dato.getTecnicorespon() + "%'\n";
        }

        if (dato.getTime0() != null && dato.getTime2() != null) {
            query = query
                    + " and TTHeader.fecha between '" + dato.getTime0() + "' and '" + dato.getTime2() + "'\n";
        }

        if (dato.getMin() != null) {
            query = query
                    + " and Ticket.tmins >= " + dato.getMin() + "\n";
        }

        if (dato.getMax() != null) {
            query = query
                    + " and Ticket.tmins <= " + dato.getMax() + "\n";
        }

        if (dato.getAbierto() != null) {
            if (dato.getAbierto() == 1) {
                query = query
                        + " and (TTHeader.tthabierto = " + dato.getAbierto() + " and Ticket.abierto = " + dato.getAbierto() + ")\n";
            }
            if (dato.getAbierto() == 0) {
                query = query
                        + " and (TTHeader.tthabierto = " + dato.getAbierto() + " or Ticket.abierto = " + dato.getAbierto() + ")\n";
            }
        }

        if (dato.getTcompleto() != null) {
            query = query
                    + " and Ticket.tcompleto LIKE '" + dato.getTcompleto().trim() + "'\n";
        }

        if (dato.getTicket() != null) {
            if (dato.getTicket().contains(",")) {
                query = query + "and CAST(TTHeader.fecha as Varchar) IN (";
                String[] lfecha = dato.getTicket().split(",");
                query = query + "'" + lfecha[0].trim() + "'";
                for (int l = 1; l < lfecha.length; l++) {
                    query = query + ",'" + lfecha[l].trim() + "'";
                }
                query = query + ")\n";
            } else {
                query = query
                        + " and TTHeader.fecha LIKE '%" + dato.getTicket().trim() + "%'\n";
            }
        }

        if (dato.getNciudad() != null) {
            if (dato.getNciudad().contains(",")) {
                query = query + "and CAST(Ciudad.nombre as Varchar(100)) IN (";
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
            if (dato.getNtipo().contains(",")) {
                query = query + "and CAST(Tipo.nombre as Varchar(100)) IN (";
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
            if (dato.getNagencia().contains(",")) {
                query = query + "and CAST(Agencia.nombre as Varchar(100)) IN (";
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
        if (dato.getProblema() != null) {
            if (dato.getProblema().contains(",")) {
                query = query + "and CAST(Problema.nombre as Varchar(100)) IN (";
                String[] lntipo = dato.getProblema().split(",");
                query = query + "'" + lntipo[0].trim() + "'";
                for (int l = 1; l < lntipo.length; l++) {
                    query = query + ",'" + lntipo[l].trim() + "'";
                }
                query = query + ")\n";
            } else {
                query = query
                        + " and Problema.nombre LIKE '%" + dato.getProblema().trim() + "%'\n";
            }

        }

        if (dato.getProveedor() != null) {
            if (dato.getProveedor().contains("N/A")) {
                query = query
                        + " and TTHeader.idproveedor IS NULL\n";
            } else {
                if (dato.getProveedor().contains(",")) {
                    query = query + "and CAST(Propietario.nombre as Varchar(100)) IN (";
                    String[] lntipo = dato.getProveedor().split(",");
                    query = query + "'" + lntipo[0].trim() + "'";
                    for (int l = 1; l < lntipo.length; l++) {
                        query = query + ",'" + lntipo[l].trim() + "'";
                    }
                    query = query + ")\n";
                } else {
                    query = query
                            + " and Propietario.nombre LIKE '%" + dato.getProveedor().trim() + "%'\n";
                }
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

    public JSONArray busquedaIdTicket(Ticket dato) {
        JSONArray lista = null;
        JSONParser parser = new JSONParser();
        String query
                = "Select Ticket.estado,Ticket.id,Ticket.idheader,Ticket.idenlace,Ticket.adicional,Ticket.modificacion,Ticket.fechamod,\n"
                + "Ticket.tcompleto,Ticket.time0,Ticket.time1, Ticket.time2,Ticket.tmins,\n"
                + " (SELECT Agencia.id,Agencia.nombre as nombreagencia\n"
                + "  FROM Agencia \n"
                + "  WHERE Agencia.id=Ticket.idagencia\n"
                + "  FOR JSON AUTO) as Agencia\n"
                + "FROM Ticket\n"
                + "WHERE Ticket.id = " + dato.getId() + "\n"
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

    public JSONArray busquedaIdTTHeader(TTHeader dato) {
        JSONArray lista = null;
        JSONParser parser = new JSONParser();
        String query
                = "Select TTHeader.estado,TTHeader.id,TTHeader.modificacion,TTHeader.fechamod,\n"
                + "TTHeader.conteo,TTHeader.fecha,TTHeader.soporte,TTHeader.descripcion,\n"
                + "TTHeader.tecnicorespon,TTHeader.tecnicoreporte,TTHeader.ttproveedor,\n"
                + " (SELECT id,nombre\n"
                + "  FROM Propietario \n"
                + "  WHERE Propietario.id=TTHeader.idproveedor\n"
                + "  FOR JSON AUTO) as Proveedor,\n"
                + " (SELECT id,nombre\n"
                + "  FROM Problema \n"
                + "  WHERE Problema.id=TTHeader.idproblema\n"
                + "  FOR JSON AUTO) as Problema,\n"
                + " (SELECT Ticket.id,Ticket.idagencia,Ticket.tcompleto,Ticket.tmins,\n"
                + "  CONVERT(Varchar,Ticket.time0,20) as time0,CONVERT(Varchar,Ticket.time1,20) as time1, CONVERT(Varchar,Ticket.time2,20) as time2,\n"
                + "   (SELECT id,nombre\n"
                + "    FROM Agencia \n"
                + "    WHERE Agencia.id=Ticket.idagencia\n"
                + "    FOR JSON AUTO) as Agencia\n"
                + "    FROM Ticket \n"
                + "    WHERE Ticket.idheader=TTHeader.id and Ticket.estado=1\n"
                + "     FOR JSON AUTO) as Ticket\n"
                + "FROM TTHeader\n"
                + "FULL OUTER JOIN Ticket on(Ticket.idheader=TTHeader.id)\n"
                + "WHERE TTHeader.id = " + dato.getId() + "\n"
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

    public boolean actualizarTicket(Ticket dato) {

        long dias = (Long) (dato.getTmins() / 1440);
        long dias_r = dato.getTmins() - (1440 * dias);
        long mins = (Long) (dias_r / 60);
        long mins_r = dias_r - (60 * mins);

        dato.setTdias(dias + "d " + mins + "h " + mins_r + "m");

        String query
                = "UPDATE Ticket\n"
                + "  SET  idagencia=    " + dato.getIdagencia() + "\n"
                + "      ,estado =      " + dato.getEstado() + "\n"
                + "      ,abierto =     " + dato.getAbierto() + "\n"
                + "      ,fechamod = CURRENT_TIMESTAMP \n"
                + "      ,modificacion='" + dato.getUsername().trim() + "'\n"
                + "      ,tmins =       " + dato.getTmins() + "\n"
                + "      ,time0 =      '" + dato.getTime0().trim() + "'\n"
                + "      ,time2 =      '" + dato.getTime2().trim() + "'\n";
        if (dato.getTime1() != null) {
            query = query + "      ,time1 =      '" + dato.getTime1().trim() + "'\n";
        } else {
            query = query + "      ,time1 =      " + dato.getTime1() + "\n";
        }

        if (dato.getAdicional() != null) {
            query = query
                    + "      ,adicional =  '" + dato.getAdicional().trim() + "'\n";
        } else {
            query = query
                    + "      ,adicional =   " + dato.getAdicional() + "\n";
        }
        query = query
                + "      ,tcompleto =  '" + dato.getTcompleto().trim() + "'\n"
                + "      ,tdias =      '" + dato.getTdias().trim() + "'\n"
                + " WHERE id= " + dato.getId() + ";\n";
        Ticket tactual = TicketActual(dato);
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            con.getConnection().setAutoCommit(false);

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

    public boolean actualizarTTHeader(TTHeader dato) {
        String query
                = "UPDATE TTHeader\n"
                + "  SET  estado =         " + dato.getEstado() + "\n"
                + "      ,tthabierto =     " + dato.getTthabierto() + "\n"
                + "      ,tecnicoreporte ='" + dato.getTecnicoreporte().trim() + "'\n"
                + "      ,fechamod = CURRENT_TIMESTAMP \n"
                + "      ,modificacion   ='" + dato.getUsername().trim() + "'\n";
        if (dato.getDescripcion() != null) {
            query = query
                    + "      ,descripcion =   '" + dato.getDescripcion().trim() + "'\n";
        }
        if (dato.getSoporte() != null) {
            query = query
                    + "      ,soporte =       '" + dato.getSoporte().trim() + "'\n";
        }
        if (dato.getIdproveedor() != null) {
            query = query
                    + "      ,idproveedor =    " + dato.getIdproveedor() + "\n";
        }
        if (dato.getTtproveedor() != null) {
            query = query
                    + "      ,ttproveedor =   '" + dato.getTtproveedor().trim() + "'\n";
        } else {
            query = query
                    + "      ,ttproveedor =    " + dato.getTtproveedor() + "\n";
        }
        query = query
                + " WHERE id= " + dato.getId() + ";\n";

        if (dato.getEstado() == 0) {
            query = query
                    + "UPDATE Ticket\n"
                    + "  SET  estado   = " + dato.getEstado() + "\n"
                    + "    ,fechamod   = CURRENT_TIMESTAMP \n"
                    + ",modificacion   ='" + dato.getUsername().trim() + "'\n"
                    + " WHERE idheader = " + dato.getId() + ";\n";

            query = query
                    + "UPDATE TTHeader\n"
                    + "  SET  estado   = " + dato.getEstado() + "\n"
                    + "    ,fechamod   = CURRENT_TIMESTAMP \n"
                    + ",modificacion   ='" + dato.getUsername().trim() + "'\n"
                    + " WHERE idheaderlan = " + dato.getId() + ";\n";

            query = query
                    + "UPDATE Ticket\n"
                    + "  SET  estado   = " + dato.getEstado() + "\n"
                    + "    ,fechamod   = CURRENT_TIMESTAMP \n"
                    + ",modificacion   ='" + dato.getUsername().trim() + "'\n"
                    + " WHERE idheaderlan = " + dato.getId() + ";\n";
        }

        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            con.getConnection().setAutoCommit(false);

            String query2 = "";
            Long problema = 0l;
            String queryproblema = "SELECT lan FROM Problema WHERE id =" + dato.getIdproblema() + "\n";
            st = con.getConnection().prepareStatement(queryproblema);
            rs = st.executeQuery();
            while (rs.next()) {
                problema = rs.getLong(1);
            };

            for (Ticket tt : dato.getTickets()) {
                int cenlaces = 0;
                if (dato.getIdproveedor() == null) {//SI NO TIENE PROVEEDOR, NO REVISAR LAN
                    tt.setLan(0);
                } else if (dato.getIdproblema() == 2l) {//VERIFICAR LAN
                    String querylan = queryContarEnlaces(tt);
                    st = con.getConnection().prepareStatement(querylan);
                    rs = st.executeQuery();
                    while (rs.next()) {
                        cenlaces = rs.getInt(1);
                    };

                }
                int countbaja = 0;
                if (cenlaces > 1) {
                    String baja = "SELECT Count(Enlace.id)\n"
                            + "FROM Enlace\n"
                            + "FULL OUTER JOIN Fechabaja ON(Fechabaja.idenlace=Enlace.id)\n"
                            + "Where Enlace.estado !=0 and Enlace.idagencia = " + tt.getIdagencia() + " and Enlace.id !=" + tt.getIdenlace() + "\n"
                            + "and '" + dato.getFecha() + "' between Fechabaja.inicio and Fechabaja.fin ;";
                    st = con.getConnection().prepareStatement(baja);
                    rs = st.executeQuery();
                    while (rs.next()) {
                        countbaja = rs.getInt(1);
                    };
                    if (countbaja > 0) {
                        cenlaces = cenlaces - 1;
                    }

                }

                if (cenlaces > 1) { //NO SE AFECTA LAN
                    tt.setLan(0);
                } else { // SE AFECTA LAN
                    tt.setLan(1);
                }

                if (tt.getTcompleto().toUpperCase().trim().equals("NO APLICA")) {
                    tt.setLan(0);
                }
                if (!tt.getTcompleto().trim().toLowerCase().equals("no")) {
                    tt.setTime1(null);
                }

                if (problema.equals(0l)) {
                    tt.setLan(0);
                }

                query2 = query2 + queryCrearTicket(tt, dato.getId(), 0, dato.getUsername());
                if (tt.getLan() == 0 && cenlaces > 1 && !tt.getTcompleto().toUpperCase().trim().equals("NO APLICA")) {
                    //VERIFICAR SI HAY OTROS EVENTOS QUE COINCIDEN POR CADA TICKET PARA HACER LAN
                    String queryeventos = queryEventosExistentes(tt);
                    st = con.getConnection().prepareStatement(queryeventos);
                    rs = st.executeQuery();
                    String eventos = "";
                    while (rs.next()) {
                        eventos = rs.getString(1);
                    };
                    List<RangoTiempo> rtArrayFinal = eventosLan(eventos, tt);

                    //PROCESOO DE LAN.
                    Long idheaderlan = 0l;
                    if (rtArrayFinal.size() > 0) { //DEBO CREAR TICKETS LAN
                        String headerRT = queryCrearHeaderLan(dato, dato.getId()); //CREAR TTHEADER DE LAN
                        st = con.getConnection().prepareStatement(headerRT);
                        rs = st.executeQuery();
                        while (rs.next()) {
                            idheaderlan = rs.getLong(1);
                        };
                        if (idheaderlan == 0) { //SI NO HAY HEADER DE LAN EXISTE UN ERROR
                            con.getConnection().rollback();
                            return false;
                        }

                        for (int n = 0; n < rtArrayFinal.size(); n++) { // TICKETS DE EVENTOS DE LAN POR CADA HEADER
                            query2 = query2 + queryCrearTicketLan(tt, idheaderlan, rtArrayFinal.get(n).getIni(), rtArrayFinal.get(n).getEnd(), dato.getUsername(), dato.getId());
                        }
                    }

                }
                if (tt.getLan() == 1 && cenlaces == 1 && !tt.getTcompleto().toUpperCase().trim().equals("NO APLICA")) {
                    Long idheaderlan = 0l;
                    String headerRT = queryCrearHeaderLan(dato, dato.getId()); //CREAR TTHEADER DE LAN
                    st = con.getConnection().prepareStatement(headerRT);
                    rs = st.executeQuery();
                    while (rs.next()) {
                        idheaderlan = rs.getLong(1);
                    };
                    if (idheaderlan == 0) { //SI NO HAY HEADER DE LAN EXISTE UN ERROR
                        con.getConnection().rollback();
                        return false;
                    }
                    LocalDateTime iniActual = LocalDateTime.parse((CharSequence) tt.getTime0().replace(" ", "T"));
                    LocalDateTime endActual = LocalDateTime.parse((CharSequence) tt.getTime2().replace(" ", "T"));
                    query2 = query2 + queryCrearTicketLan(tt, idheaderlan, iniActual, endActual, dato.getUsername(), dato.getId());

                }

            }
            if (query2.length() > 0) {
                st = con.getConnection().prepareStatement(query2);
                rs = st.executeQuery();
                Long idticket = 0l;
                while (rs.next()) {
                    idticket = rs.getLong(1);
                };

                if (idticket.equals(0l)) {
                    con.getConnection().rollback();
                    return false;
                }
            }

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

    public boolean crear(TTHeader dato) {
        String query
                = "INSERT INTO TTHeader\n"
                + "           (id\n"
                + "           ,conteo\n"
                + "           ,fecha\n"
                + "           ,fechamod\n"
                + "           ,modificacion\n"
                + "           ,idproblema\n"
                + "           ,idproveedor\n"
                + "           ,tthabierto\n"
                + "           ,tecnicorespon\n";
        if (dato.getDescripcion() != null) {
            query = query
                    + "           ,descripcion\n";
        }
        if (dato.getSoporte() != null) {
            query = query
                    + "           ,soporte\n";
        }
        if (dato.getTtproveedor() != null) {
            query = query
                    + "           ,ttproveedor\n";
        } else {
            query = query
                    + "           ,ttproveedor\n";
        }
        query = query
                + "           ,tecnicoreporte)\n"
                + "            OUTPUT Inserted.id \n"
                + "            VALUES\n"
                + "            (NEXT VALUE FOR SeqTTHeader,\n"
                + "            ISNULL((SELECT TOP 1 conteo\n"
                + "            FROM TTHeader\n"
                + "            WHERE fecha ='" + dato.getFecha() + "' \n"
                + "            ORDER BY id desc) +1,1)\n"
                + "           ,'" + dato.getFecha() + "'\n"
                + "           , CURRENT_TIMESTAMP \n"
                + "           ,'" + dato.getUsername().trim() + "'\n"
                + "           , " + dato.getIdproblema() + "\n"
                + "           , " + dato.getIdproveedor() + "\n"
                + "           , " + dato.getTthabierto() + "\n"
                + "           ,'" + dato.getTecnicorespon().trim() + "'\n";
        if (dato.getDescripcion() != null) {
            query = query
                    + "           ,'" + dato.getDescripcion().trim() + "'\n";
        }
        if (dato.getSoporte() != null) {
            query = query
                    + "           ,'" + dato.getSoporte().trim() + "'\n";
        }
        if (dato.getTtproveedor() != null) {
            query = query
                    + "           ,'" + dato.getTtproveedor().trim() + "'\n";
        } else {
            query = query
                    + "           , " + dato.getTtproveedor() + "\n";
        }
        query = query
                + "           ,'" + dato.getTecnicoreporte().trim() + "');\n";
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            con.getConnection().setAutoCommit(false);

            st = con.getConnection().prepareStatement(query);
            Long idheader = 0l;
            rs = st.executeQuery();
            while (rs.next()) {
                idheader = rs.getLong(1);

            };
            if (idheader == 0l) {
                con.getConnection().rollback();
                return false;
            }
            Long problema = 0l;
            String queryproblema = "SELECT lan FROM Problema WHERE id =" + dato.getIdproblema() + "\n";
            st = con.getConnection().prepareStatement(queryproblema);
            rs = st.executeQuery();
            while (rs.next()) {
                problema = rs.getLong(1);
            };

            String query2 = "";

            for (Ticket tt : dato.getTickets()) {
                int cenlaces = 0;
                if (dato.getIdproveedor() == null) {//SI NO TIENE PROVEEDOR, NO REVISAR LAN
                    tt.setLan(0);
                } else if (dato.getIdproblema() == 2l) {//VERIFICAR LAN
                    String querylan = queryContarEnlaces(tt);
                    st = con.getConnection().prepareStatement(querylan);

                    rs = st.executeQuery();
                    while (rs.next()) {
                        cenlaces = rs.getInt(1);
                    };

                }
                int countbaja = 0;
                if (cenlaces > 1) {
                    String baja = "SELECT Count(Enlace.id)\n"
                            + "FROM Enlace\n"
                            + "FULL OUTER JOIN Fechabaja ON(Fechabaja.idenlace=Enlace.id)\n"
                            + "Where Enlace.estado !=0 and Enlace.idagencia = " + tt.getIdagencia() + " and Enlace.id !=" + tt.getIdenlace() + "\n"
                            + "and '" + dato.getFecha() + "' between Fechabaja.inicio and Fechabaja.fin ;";
                    st = con.getConnection().prepareStatement(baja);
                    rs = st.executeQuery();
                    while (rs.next()) {
                        countbaja = rs.getInt(1);
                    };
                    if (countbaja > 0) {
                        cenlaces = cenlaces - 1;
                    }

                }

                if (cenlaces > 1) { //NO SE AFECTA LAN
                    tt.setLan(0);
                } else { // SE AFECTA LAN
                    tt.setLan(1);
                }

                if (tt.getTcompleto().toUpperCase().trim().equals("NO APLICA")) {
                    tt.setLan(0);
                }
                if (!tt.getTcompleto().trim().toLowerCase().equals("no")) {
                    tt.setTime1(null);
                }

                if (problema.equals(0l)) {
                    tt.setLan(0);
                }

                query2 = query2 + queryCrearTicket(tt, idheader, 0, dato.getUsername());
                if (tt.getLan() == 0 && cenlaces > 1 && !tt.getTcompleto().toUpperCase().trim().equals("NO APLICA")) {
                    //VERIFICAR SI HAY OTROS EVENTOS QUE COINCIDEN POR CADA TICKET PARA HACER LAN
                    String queryeventos = queryEventosExistentes(tt);
                    st = con.getConnection().prepareStatement(queryeventos);
                    rs = st.executeQuery();
                    String eventos = "";
                    while (rs.next()) {
                        eventos = rs.getString(1);
                    };

                    List<RangoTiempo> rtArrayFinal = eventosLan(eventos, tt);

                    //PROCESOO DE LAN.
                    Long idheaderlan = 0l;
                    if (rtArrayFinal.size() > 0) { //DEBO CREAR TICKETS LAN
                        String headerRT = queryCrearHeaderLan(dato, idheader); //CREAR TTHEADER DE LAN
                        st = con.getConnection().prepareStatement(headerRT);
                        rs = st.executeQuery();
                        while (rs.next()) {
                            idheaderlan = rs.getLong(1);
                        };
                        if (idheaderlan == 0) { //SI NO HAY HEADER DE LAN EXISTE UN ERROR
                            con.getConnection().rollback();
                            return false;
                        }

                        for (int n = 0; n < rtArrayFinal.size(); n++) { // TICKETS DE EVENTOS DE LAN POR CADA HEADER
                            query2 = query2 + queryCrearTicketLan(tt, idheaderlan, rtArrayFinal.get(n).getIni(), rtArrayFinal.get(n).getEnd(), dato.getUsername(), idheader);
                        }
                    }

                }

                if (tt.getLan() == 1 && cenlaces == 1 && !tt.getTcompleto().toUpperCase().trim().equals("NO APLICA")) {
                    Long idheaderlan = 0l;
                    String headerRT = queryCrearHeaderLan(dato, idheader); //CREAR TTHEADER DE LAN
                    st = con.getConnection().prepareStatement(headerRT);
                    rs = st.executeQuery();
                    while (rs.next()) {
                        idheaderlan = rs.getLong(1);
                    };
                    if (idheaderlan == 0) { //SI NO HAY HEADER DE LAN EXISTE UN ERROR
                        con.getConnection().rollback();
                        return false;
                    }
                    LocalDateTime iniActual = LocalDateTime.parse((CharSequence) tt.getTime0().replace(" ", "T"));
                    LocalDateTime endActual = LocalDateTime.parse((CharSequence) tt.getTime2().replace(" ", "T"));
                    query2 = query2 + queryCrearTicketLan(tt, idheaderlan, iniActual, endActual, dato.getUsername(), idheader);

                }
            }
            st = con.getConnection().prepareStatement(query2);
            rs = st.executeQuery();
            Long idticket = 0l;
            while (rs.next()) {
                idticket = rs.getLong(1);
            };

            for (ActHeader act : dato.getActividades()) {
                String query3
                        = "INSERT INTO ActHeader\n"
                        + "           (id\n"
                        + "           ,abierto\n"
                        + "           ,idticket\n"
                        + "           ,actividad\n"
                        + "           ,comentario\n"
                        + "           ,abbanco\n"
                        + "           ,fechamod\n"
                        + "           ,modificacion)\n"
                        + "            OUTPUT Inserted.id \n"
                        + "            VALUES\n"
                        + "            (NEXT VALUE FOR SeqActHeader\n"
                        + "           , " + act.getCheck() + "\n"
                        + "           , " + idheader + "\n"
                        + "           , " + act.getActividad() + "\n"
                        + "           ,'" + act.getComentario() + "'\n"
                        + "           , 1 \n"
                        + "           , CURRENT_TIMESTAMP \n"
                        + "           ,'" + dato.getUsername().trim() + "');\n";
                st = con.getConnection().prepareStatement(query3);
                Long idheaderact = 0l;
                rs = st.executeQuery();
                while (rs.next()) {
                    idheaderact = rs.getLong(1);

                };
                if (idheaderact == 0l) {
                    con.getConnection().rollback();
                    return false;
                }
                for (Actividades subact : act.getSubactividades()) {
                    String query4
                            = "           INSERT INTO Actividades\n"
                            + "           (id\n"
                            + "           ,idheader\n"
                            + "           ,subactividad\n";
                    if (subact.getFecha() != null) {
                        query4 = query4
                                + "           ,fecha\n";
                    }
                    if (subact.getFecha2() != null) {
                        query4 = query4
                                + "           ,fecha2\n";
                    }
                    if (subact.getUsuario() != null) {
                        query4 = query4
                                + "           ,usuario\n";
                    }
                    query4 = query4
                            + "           ,mins\n"
                            + "           ,abierto\n"
                            + "           ,fechamod\n"
                            + "           ,modificacion)\n"
                            + "            OUTPUT Inserted.id \n"
                            + "            VALUES\n"
                            + "            (NEXT VALUE FOR SeqActividades\n"
                            + "           , " + idheaderact + "\n"
                            + "           , 5\n";
                    if (subact.getFecha() != null) {
                        query4 = query4
                                + "           ,'" + subact.getFecha().replace('T', ' ').trim() + "'\n";
                    }
                    if (subact.getFecha2() != null) {
                        query4 = query4
                                + "           ,'" + subact.getFecha2().replace('T', ' ').trim() + "'\n";
                    }
                    if (subact.getUsuario() != null) {
                        query4 = query4
                                + "           ,'" + subact.getUsuario().trim() + "'\n";
                    }
                    query4 = query4
                            + "           , " + subact.getMins() + "\n"
                            + "           , " + subact.getAbierto() + "\n"
                            + "           , CURRENT_TIMESTAMP \n"
                            + "           ,'" + dato.getUsername().trim() + "');\n";
                    if (query4.length() > 0) {
                        st = con.getConnection().prepareStatement(query4);
                        st.executeQuery();
                    } else {
                        con.getConnection().rollback();
                        return false;
                    }
                }

            }

            if (idticket.equals(0l)) {
                con.getConnection().rollback();
                return false;
            }

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

    public Boolean existeEvento(Ticket tt) {
        String query
                = "  SELECT id\n"
                + "      ,time0\n"
                + "      ,time2\n"
                + "  FROM Ticket\n"
                + "  Where estado = 1 and idagencia=" + tt.getIdagencia() + " and \n";
        if (tt.getId() != null) {
            query = query
                    + "    id != " + tt.getId() + " and  LAN =1 and \n";
        } else if (tt.getIdenlace() != null) {
            query = query
                    + "    idenlace=" + tt.getIdenlace() + " and \n";
        }
        query = query
                + "  ((	time0 between '" + tt.getTime0() + "' and '" + tt.getTime2() + "' ) or\n"
                + "   (	time2 between '" + tt.getTime0() + "' and '" + tt.getTime2() + "' ) or\n"
                + "   (	'" + tt.getTime0() + "' between time0 and time2 ) or\n"
                + "   (	'" + tt.getTime2() + "' between time0 and time2 ))\n"
                + "  FOR JSON AUTO;";
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            con.getConnection().setAutoCommit(false);
            st = con.getConnection().prepareStatement(query);
            rs = st.executeQuery();
            con.getConnection().commit();
            String rsst = "";
            while (rs.next()) {
                rsst = rsst + rs.getString(1);
            }
            if (rsst.length() > 0) {
                return true;
            }
            return false;

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

    public Boolean isBaja(TTHeader dato, Ticket tt) {
        String query
                = "SELECT Fechabaja.id\n"
                + "FROM Enlace\n"
                + "FULL OUTER JOIN Fechabaja ON(Fechabaja.idenlace=Enlace.id)\n"
                + "Where Enlace.estado !=0 and Enlace.idagencia = " + tt.getIdagencia() + " and Enlace.id =" + tt.getIdenlace() + "\n"
                + "and ( '" + tt.getTime0() + "' between Fechabaja.inicio and Fechabaja.fin\n"
                + "and  '" + tt.getTime2() + "' between Fechabaja.inicio and Fechabaja.fin )\n"
                + "  FOR JSON AUTO;";
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            con.getConnection().setAutoCommit(false);
            st = con.getConnection().prepareStatement(query);
            rs = st.executeQuery();
            con.getConnection().commit();
            int rsst = 0;
            while (rs.next()) {
                rsst = rs.getInt(1);
            }
            if (rsst > 0) {
                return true;
            }
            return false;

        } catch (Exception e) {
            System.out.println(this.getClass().toString() + ".isbaja " + e.getMessage());
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
                System.out.println(this.getClass().toString() + ".isbaja " + e.getMessage());
            }
        }
    }

    public Ticket TicketActual(Ticket tt) {
        JSONArray array = null;
        JSONParser parser = new JSONParser();
        String query
                = "  SELECT * \n"
                + "  FROM Ticket\n"
                + "  Where id =" + tt.getId() + "\n"
                + "  FOR JSON AUTO;";
        PreparedStatement st = null;
        ResultSet rs = null;
        Ticket tta = new Ticket();
        try {
            con.getConnection().setAutoCommit(false);
            st = con.getConnection().prepareStatement(query);
            String rsst = "";
            rs = st.executeQuery();
            //con.getConnection().commit();
            if (rs.next()) {
                rsst = rsst + rs.getString(1);
            }
            if (rsst.length() > 0) {
                array = (JSONArray) parser.parse(rsst);
            }
            JSONObject obj = (JSONObject) array.get(0);
            tta.setLan(((Boolean) obj.get("LAN")) == true ? 1 : 0);
            tta.setTime0((String) obj.get("time0"));
            tta.setTime1((String) obj.get("time1"));
            tta.setTime2((String) obj.get("time2"));

            return tta;

        } catch (Exception e) {
            System.out.println(this.getClass().toString() + ".actualizar " + e.getMessage());
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
                System.out.println(this.getClass().toString() + ".actualizar " + e.getMessage());
            }
        }
    }

    public TTHeader TTHeaderActual(Ticket tt) {
        JSONArray array = null;
        JSONParser parser = new JSONParser();
        String query
                = "  SELECT * \n"
                + "  FROM TTHeader\n"
                + "  Where id =" + tt.getIdheader() + "\n"
                + "  FOR JSON AUTO;";
        PreparedStatement st = null;
        ResultSet rs = null;
        TTHeader tta = new TTHeader();
        try {
            con.getConnection().setAutoCommit(false);
            st = con.getConnection().prepareStatement(query);
            String rsst = "";
            rs = st.executeQuery();
            con.getConnection().commit();
            if (rs.next()) {
                rsst = rsst + rs.getString(1);
            }
            if (rsst.length() > 0) {
                array = (JSONArray) parser.parse(rsst);
            }
            JSONObject obj = (JSONObject) array.get(0);
            tta.setFecha((String) obj.get("fecha"));
            tta.setIdproblema((Long) obj.get("idproblema"));
            tta.setTecnicoreporte((String) obj.get("tecnicorespon"));
            tta.setTecnicorespon((String) obj.get("tecnicoreporte"));

            return tta;

        } catch (Exception e) {
            System.out.println(this.getClass().toString() + ".actualizar " + e.getMessage());
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
                System.out.println(this.getClass().toString() + ".actualizar " + e.getMessage());
            }
        }
    }

    //QUERYS
    public String queryContarEnlaces(Ticket tt) {
        String query = "SELECT count(id) as c\n"
                + "  FROM Enlace\n"
                + "  Where estado !=0 and idagencia=" + tt.getIdagencia() + ";";

        return query;
    }

    public String queryCrearTicket(Ticket tt, Long idheader, Integer idheaderlan, String usuario) {
        //CREACION DE  TICKETS

        long dias = (Long) (tt.getTmins() / 1440);
        long dias_r = tt.getTmins() - (1440 * dias);
        long mins = (Long) (dias_r / 60);
        long mins_r = dias_r - (60 * mins);
        tt.setTdias(dias + "d " + mins + "h " + mins_r + "m");

        String query2 = "INSERT INTO Ticket\n"
                + "           (id\n"
                + "           ,idagencia\n"
                + "           ,idheader\n"
                + "           ,LAN\n"
                + "           ,abierto\n"
                + "           ,tcompleto\n"
                + "           ,fechamod\n"
                + "           ,modificacion\n"
                + "           ,tmins\n"
                + "           ,tdias\n"
                + "           ,time0\n"
                + "           ,time1\n";
        if (idheaderlan != 0) {
            query2 = query2
                    + "           ,idlan\n";
        }
        if (tt.getIdenlace() != null) {
            query2 = query2
                    + "           ,idenlace\n";
        }
        if (tt.getTime2() != null) {
            query2 = query2
                    + "           ,time2\n";
        }
        if (tt.getAdicional() != null) {
            query2 = query2
                    + "           ,adicional\n";
        }
        query2 = query2
                + ")\n"
                + "     OUTPUT Inserted.id \n"
                + "     VALUES\n"
                + "           (NEXT VALUE FOR SeqTicket\n"
                + "           , " + tt.getIdagencia() + "\n"
                + "           , " + idheader + "\n"
                + "           , 0\n"
                + "           , " + tt.getAbierto() + "\n"
                + "           ,'" + tt.getTcompleto().trim() + "'\n"
                + "           , CURRENT_TIMESTAMP \n"
                + "           ,'" + usuario.trim() + "'\n"
                + "           , " + tt.getTmins() + "\n"
                + "           ,'" + tt.getTdias().trim() + "'\n"
                + "           ,'" + tt.getTime0().trim() + "'\n";
        if (tt.getTime1() != null) {
            query2 = query2
                    + "           ,'" + tt.getTime1().trim() + "'\n";
        } else {
            query2 = query2
                    + "           ," + tt.getTime1() + "\n";
        }
        if (idheaderlan != 0) {
            query2 = query2
                    + "           ," + idheaderlan + "\n";
        }

        if (tt.getIdenlace() != null) {
            query2 = query2
                    + "           ," + tt.getIdenlace() + "\n";
        }
        if (tt.getTime2() != null) {
            query2 = query2
                    + "           ,'" + tt.getTime2().trim() + "'\n";
        }
        if (tt.getAdicional() != null) {
            query2 = query2
                    + "           ,'" + tt.getAdicional().trim() + "'\n";
        }
        query2 = query2
                + ");\n";

        return query2;
    }

    public String queryEventosExistentes(Ticket tt) {
        String queryeventos
                = "  SELECT id\n"
                + "      ,time0\n"
                + "      ,time2\n"
                + "  FROM Ticket\n"
                + "  Where estado = 1 and idagencia=" + tt.getIdagencia() + " \n";
        if (tt.getIdenlace() != null) {
            queryeventos = queryeventos
                    + " and \n"
                    + "idenlace!=" + tt.getIdenlace();
        }
        queryeventos = queryeventos
                + " and  LAN = 0 and \n"
                + "  ((	time0 between '" + tt.getTime0() + "' and '" + tt.getTime2() + "' ) or\n"
                + "   (	time2 between '" + tt.getTime0() + "' and '" + tt.getTime2() + "' ) or\n"
                + "   (	'" + tt.getTime0() + "' between time0 and time2 ) or\n"
                + "   (	'" + tt.getTime2() + "' between time0 and time2 ))\n"
                + "  FOR JSON AUTO;";
        return queryeventos;
    }

    public String queryCrearTicketLan(Ticket tt, Long idheaderlan, LocalDateTime ini, LocalDateTime end, String usuario, Long idheaderlan2) {
        //CREACION DE  TICKETS
        long minutoslan = ChronoUnit.MINUTES.between(ini, end);
        //int minutoslan = end.getMinute() - ini.getMinute();
        long diaslan = (Long) (minutoslan / 1440);
        long dias_rlan = minutoslan - (1440 * diaslan);
        long minslan = (Long) (dias_rlan / 60);
        long mins_rlan = dias_rlan - (60 * minslan);

        tt.setTdias(diaslan + "d " + minslan + "h " + mins_rlan + "m");
        String query
                = "INSERT INTO Ticket\n"
                + "           (id\n"
                + "           ,idagencia\n"
                + "           ,idheader\n"
                + "           ,idheaderlan\n"
                + "           ,LAN\n"
                + "           ,abierto\n"
                + "           ,tcompleto\n"
                + "           ,fechamod \n"
                + "           ,modificacion\n"
                + "           ,tmins\n"
                + "           ,tdias\n"
                + "           ,time0\n"
                + "           ,time2)\n"
                + "     OUTPUT Inserted.id \n"
                + "     VALUES\n"
                + "           (NEXT VALUE FOR SeqTicket\n"
                + "           , " + tt.getIdagencia() + "\n"
                + "           , " + idheaderlan + "\n"
                + "           , " + idheaderlan2 + "\n"
                + "           , 1\n"
                + "           , " + tt.getAbierto() + "\n"
                + "           ,'SI'\n"
                + "           ,CURRENT_TIMESTAMP \n"
                + "           ,'" + usuario.trim() + "'\n"
                + "           , " + minutoslan + "\n"
                + "           ,'" + tt.getTdias().trim() + "'\n"
                + "           ,'" + ini.toString().replace("T", " ") + "'\n"
                + "           ,'" + end.toString().replace("T", " ") + "');\n";

        return query;
    }

    public String queryCrearHeaderLan(TTHeader dato, Long idHeader) {
        //CREACION DE  TICKETS
        String headerRT
                = "INSERT INTO TTHeader\n"
                + "           (id\n"
                + "           ,conteo\n"
                + "           ,fecha\n"
                + "           ,idproblema\n"
                + "           ,idheaderlan\n"
                + "           ,tthabierto\n"
                + "           ,soporte\n"
                + "           ,descripcion\n"
                + "           ,fechamod \n"
                + "           ,modificacion\n"
                + "           ,tecnicorespon\n"
                + "           ,tecnicoreporte)\n"
                + "            OUTPUT Inserted.id \n"
                + "            VALUES\n"
                + "            (NEXT VALUE FOR SeqTTHeader,\n"
                + "            ISNULL((SELECT TOP 1 conteo\n"
                + "            FROM TTHeader\n"
                + "            WHERE fecha ='" + dato.getFecha() + "' \n"
                + "            ORDER BY id desc) +1,1)\n"
                + "           ,'" + dato.getFecha().trim() + "'\n"
                + "           , " + dato.getIdproblema() + "\n"
                + "           , " + idHeader + "\n"
                + "           , 1\n"
                + "           ,'LAN automatico'\n"
                + "           ,'Afectación de la LAN'\n"
                + "           ,CURRENT_TIMESTAMP \n"
                + "           ,'" + dato.getUsername().trim() + "'\n"
                + "           ,'" + dato.getTecnicorespon().trim() + "'\n"
                + "           ,'" + dato.getTecnicoreporte().trim() + "');\n";
        return headerRT;
    }

    //PROCESO DE EVENTOS LAN
    public List<RangoTiempo> eventosLan(String eventos, Ticket tt) {
        List<RangoTiempo> rtArrayFinal = null;
        try {
            JSONArray lista = null;
            JSONParser parser = new JSONParser();
            if (eventos.length() > 0) {
                lista = (JSONArray) parser.parse(eventos);

            }

            List<RangoTiempo> rtArray = new ArrayList<RangoTiempo>();
            rtArrayFinal = new ArrayList<RangoTiempo>();

            if (lista != null) {
                for (int n = 0; n < lista.size(); n++) {
                    JSONObject ev = (JSONObject) lista.get(n);
                    LocalDateTime ini = LocalDateTime.parse((CharSequence) ev.get("time0"));
                    LocalDateTime end = LocalDateTime.parse((CharSequence) ev.get("time2"));
                    RangoTiempo rt = new RangoTiempo();
                    rt.setIni(ini);
                    rt.setEnd(end);
                    rtArray.add(rt);
                }
            }
            LocalDateTime iniActual = LocalDateTime.parse((CharSequence) tt.getTime0().replace(" ", "T"));
            LocalDateTime endActual = LocalDateTime.parse((CharSequence) tt.getTime2().replace(" ", "T"));

            for (int n = 0; n < rtArray.size(); n++) {
                RangoTiempo rt = new RangoTiempo();
                if (iniActual.isAfter(((RangoTiempo) rtArray.get(n)).getIni())) {
                    rt.setIni(iniActual);
                } else {
                    rt.setIni(((RangoTiempo) rtArray.get(n)).getIni());
                }
                if (endActual.isAfter(((RangoTiempo) rtArray.get(n)).getEnd())) {
                    rt.setEnd(((RangoTiempo) rtArray.get(n)).getEnd());
                } else {
                    rt.setEnd(endActual);
                }
                rtArrayFinal.add(rt);
            }
            //eliminar periodos continuos
            if (rtArrayFinal.size() > 1) {
                for (int n = (rtArrayFinal.size() - 1); n >= 1; n--) {
                    if (!(rtArrayFinal.get(n).getIni().isAfter(rtArrayFinal.get(n - 1).getEnd()))) {
                        rtArrayFinal.get(n - 1).setEnd(rtArrayFinal.get(n).getEnd());
                        rtArrayFinal.remove(n);
                    }

                }
            }

            if (rtArrayFinal.size() > 1) {
                for (int n = (rtArrayFinal.size() - 1); n >= 1; n--) {
                    int minutos = rtArrayFinal.get(n).getEnd().getMinute() - rtArrayFinal.get(n).getIni().getMinute();
                    if (minutos < 5) {
                        rtArrayFinal.remove(n);
                    }

                }
            }

            return rtArrayFinal;
        } catch (Exception e) {
            System.out.println(this.getClass().toString() + ".eventosLan " + e.getMessage());
            return null;
        }
    }

    public List<RangoTiempo> eventosLanUpdate(String eventos, String eventoslan, Ticket tt) {
        List<RangoTiempo> rtArrayFinal = null;
        try {
            JSONArray lista = null;
            JSONParser parser = new JSONParser();
            if (eventos.length() > 0) {
                lista = (JSONArray) parser.parse(eventos);

            }

            JSONArray listalan = null;
            if (eventoslan.length() > 0) {
                listalan = (JSONArray) parser.parse(eventoslan);

            }

            List<RangoTiempo> rtArray = new ArrayList<RangoTiempo>();
            rtArrayFinal = new ArrayList<RangoTiempo>();
            if (lista != null) {
                for (int n = 0; n < lista.size(); n++) {
                    JSONObject ev = (JSONObject) lista.get(n);
                    LocalDateTime ini = LocalDateTime.parse((CharSequence) ev.get("time0"));
                    LocalDateTime end = LocalDateTime.parse((CharSequence) ev.get("time2"));
                    RangoTiempo rt = new RangoTiempo();
                    rt.setIni(ini);
                    rt.setEnd(end);
                    rtArray.add(rt);
                }
            }
            LocalDateTime iniActual = LocalDateTime.parse((CharSequence) tt.getTime0().replace(" ", "T"));
            LocalDateTime endActual = LocalDateTime.parse((CharSequence) tt.getTime2().replace(" ", "T"));
            for (int n = 0; n < rtArray.size(); n++) {
                RangoTiempo rt = new RangoTiempo();
                if (iniActual.isAfter(((RangoTiempo) rtArray.get(n)).getIni())) {
                    rt.setIni(iniActual);
                } else {
                    rt.setIni(((RangoTiempo) rtArray.get(n)).getIni());
                }
                if (endActual.isAfter(((RangoTiempo) rtArray.get(n)).getEnd())) {
                    rt.setEnd(((RangoTiempo) rtArray.get(n)).getEnd());
                } else {
                    rt.setEnd(endActual);
                }
                rtArrayFinal.add(rt);
            }
            //eliminar periodos continuos
            if (rtArrayFinal.size() > 1) {
                for (int n = (rtArrayFinal.size() - 1); n >= 1; n--) {
                    if (!(rtArrayFinal.get(n).getIni().isAfter(rtArrayFinal.get(n - 1).getEnd()))) {
                        rtArrayFinal.get(n - 1).setEnd(rtArrayFinal.get(n).getEnd());
                        rtArrayFinal.remove(n);
                    }

                }
            }

            if (rtArrayFinal.size() > 1) {
                for (int n = (rtArrayFinal.size() - 1); n >= 1; n--) {
                    int minutos = rtArrayFinal.get(n).getEnd().getMinute() - rtArrayFinal.get(n).getIni().getMinute();
                    if (minutos < 5) {
                        rtArrayFinal.remove(n);
                    }

                }
            }
            String query = "";

            if (listalan != null) {
                for (int n = 0; n < listalan.size(); n++) {

                    JSONObject ev = (JSONObject) listalan.get(n);
                    rtArrayFinal.get(n).setId((Long) ev.get("id"));
                }
            }

            return rtArrayFinal;
        } catch (Exception e) {
            System.out.println(this.getClass().toString() + ".eventosLan " + e.getMessage());
            return null;
        }
    }

    public String queryUpdateTicketLan(Ticket tt, Long idheaderlan, LocalDateTime ini, LocalDateTime end, String usuario) {
        //CREACION DE  TICKETS
        long minutoslan = ChronoUnit.MINUTES.between(ini, end);
        long diaslan = (Long) (minutoslan / 1440);
        long dias_rlan = minutoslan - (1440 * diaslan);
        long minslan = (Long) (dias_rlan / 60);
        long mins_rlan = dias_rlan - (60 * minslan);

        tt.setTmins(minutoslan);
        tt.setTdias(diaslan + "d " + minslan + "h " + mins_rlan + "m");
        String query
                = "UPDATE Ticket\n"
                + "  SET  idagencia=    " + tt.getIdagencia() + "\n"
                + "      ,estado =      1\n"
                + "      ,abierto =     1\n"
                + "      ,LAN =         1\n"
                + "      ,fechamod = CURRENT_TIMESTAMP \n"
                + "      ,modificacion   ='" + usuario.trim() + "'\n"
                + "      ,idheader =    " + idheaderlan + "\n"
                + "      ,tmins =       " + tt.getTmins() + "\n"
                + "      ,time0 =      '" + ini.toString().replace("T", " ") + "'\n"
                + "      ,time2 =      '" + end.toString().replace("T", " ") + "'\n"
                + "      ,tcompleto =  '" + tt.getTcompleto().trim() + "'\n"
                + "      ,tdias =      '" + tt.getTdias().trim() + "'\n";
        if (tt.getAdicional() != null) {
            query = query
                    + "      ,adicional =  '" + tt.getAdicional().trim() + "'\n";
        } else {
            query = query
                    + "      ,adicional =   " + tt.getAdicional() + "\n";
        }
        query = query
                + " WHERE id= " + tt.getId() + ";";

        return query;
    }

    //EXCEL
    public JSONArray obtenerEstadisticaExcel(Ticket dato) {
        JSONArray lista = null;
        JSONParser parser = new JSONParser();
        String query
                = "Select Propietario.nombre,\n"
                + "	(Select Count(Ticket.id)\n"
                + "		FROM Ticket \n"
                + "		FULL OUTER JOIN TTHeader ON TTHeader.id = Ticket.idheader \n"
                + "		WHERE TTHeader.idproveedor = Propietario.id and Ticket.estado = 1 and TTHeader.idproblema=2\n"
                + "             and Ticket.time0 between '" + dato.getTime0() + " 00:00' and '" + dato.getTime2() + " 23:59'\n"
                + "	) as Ticket,\n"
                + "	(Select Sum(Ticket.tmins)\n"
                + "		FROM Ticket \n"
                + "		FULL OUTER JOIN TTHeader ON TTHeader.id = Ticket.idheader\n"
                + "		WHERE TTHeader.idproveedor = Propietario.id and Ticket.estado = 1  and TTHeader.idproblema=2\n"
                + "             and Ticket.time0 between '" + dato.getTime0() + " 00:00' and '" + dato.getTime2() + " 23:59'\n"
                + "	) as Tiempo,\n"
                + "	(Select Count(Enlace.id)\n"
                + "		From Enlace \n"
                + "		Where Enlace.idproveedor= Propietario.id and Enlace.estado !=0\n"
                + "	) as Enlace,\n"
                + "	(Select Count(Enlace.id)\n"
                + "		From Enlace \n"
                + "		FULL OUTER JOIN Agencia on (Agencia.id= Enlace.idagencia)\n"
                + "		FULL OUTER JOIN Tipo on (Tipo.id = Agencia.idtipo)\n"
                + "		Where Enlace.idproveedor= Propietario.id  and Enlace.estado !=0 \n"
                + "		and ( Tipo.nombre like '%CIAC%' OR Tipo.nombre like '%CIN%' OR Tipo.nombre like '%VES%')\n"
                + "	) as CINCIACVES,\n"
                + "	(Select Count(Enlace.id)\n"
                + "		From Enlace \n"
                + "		FULL OUTER JOIN Agencia on (Agencia.id= Enlace.idagencia)\n"
                + "		FULL OUTER JOIN Tipo on (Tipo.id = Agencia.idtipo)\n"
                + "		Where Enlace.idproveedor= Propietario.id  and Enlace.estado !=0 \n"
                + "		and ( Tipo.nombre like '%ATM%' OR Tipo.nombre like '%INT%')\n"
                + "	) as ATMINT,\n"
                + "	(Select Count(Enlace.id)\n"
                + "		From Enlace \n"
                + "		FULL OUTER JOIN Agencia on (Agencia.id= Enlace.idagencia)\n"
                + "		FULL OUTER JOIN Tipo on (Tipo.id = Agencia.idtipo)\n"
                + "		Where Enlace.idproveedor= Propietario.id and Enlace.estado !=0 \n"
                + "		and ( Tipo.nombre like '%EXT%')\n"
                + "	) as EXT\n"
                + "FROM Propietario\n"
                + "ORDER BY CAST(Propietario.nombre as Varchar) asc\n"
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
            lista = (JSONArray) parser.parse(rsst);

            return lista;
        } catch (Exception e) {
            System.out.println(this.getClass().toString() + ".obtenerEstadisticaExcel " + e.getMessage());
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
                System.out.println(this.getClass().toString() + ".obtenerEstadisticaExcel " + e.getMessage());
            }
        }
    }

    public JSONArray obtenerEstadisticaExcel2(Ticket dato) {
        JSONArray lista = null;
        JSONParser parser = new JSONParser();
        String query
                = "SELECT Problema.nombre, \n"
                + "(Select count(TTHeader.id) FROM TTHeader \n"
                + "WHERE TTHeader.idproblema = Problema.id and TTHeader.estado=1  and (TTHeader.soporte is null or TTHeader.soporte != 'LAN automatico')\n"
                + "and TTHeader.fecha between '" + dato.getTime0() + "' and '" + dato.getTime2() + "'\n"
                + ") as sumticket\n"
                + "FROM Problema\n"
                + "Where Problema.estado=1\n"
                + "ORDER BY CAST(Problema.nombre as Varchar) asc\n"
                + "FOR JSON AUTO;";
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = con.getConnection().prepareStatement(query);
            rs = st.executeQuery();
            String rsst = "";
            while (rs.next()) {
                rsst = rsst + rs.getString(1);
            }
            lista = (JSONArray) parser.parse(rsst);

            return lista;
        } catch (Exception e) {
            System.out.println(this.getClass().toString() + ".obtenerEstadisticaExcel2 " + e.getMessage());
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
                System.out.println(this.getClass().toString() + ".obtenerEstadisticaExcel2 " + e.getMessage());
            }
        }
    }

    public JSONArray obtenerListadoExcel(Ticket dato) {
        JSONArray lista = null;
        JSONParser parser = new JSONParser();
        String query
                = "Select \n"
                + "(Select TTHeader.id,TTHeader.soporte, TTHeader.tthabierto,TTHeader.descripcion,TTHeader.ttproveedor,\n"
                + "     TTHeader.conteo,CONVERT(CHAR(8),TTHeader.fecha,112) as fecha,\n"
                + "     TTHeader.tecnicorespon,TTHeader.tecnicoreporte,\n"
                + "	Propietario.nombre as proveedor,\n"
                + "	Problema.nombre as problema\n"
                + "	FROM TTHeader\n"
                + "	FULL OUTER JOIN Problema on(TTHeader.idproblema=Problema.id)\n"
                + "	FULL OUTER JOIN Propietario on(TTHeader.idproveedor=Propietario.id)\n"
                + "	WHERE TTHeader.id=Ticket.idheader\n"
                + "	FOR JSON AUTO\n"
                + " ) as TTHeader,\n"
                + "CONVERT(Varchar,Ticket.time0,20) as time0,\n"
                + "CONVERT(Varchar,Ticket.time1,20) as time1,\n"
                + "CONVERT(Varchar,Ticket.time2,20) as time2,\n"
                + "Ticket.id,Ticket.estado,Ticket.LAN,Ticket.tcompleto,Ticket.tdias,Ticket.tmins,Ticket.abierto,Ticket.adicional,\n"
                + "Agencia.nombre as agencia,\n"
                + "Tipo.nombre as tipo,\n"
                + "Ciudad.nombre as ciudad\n"
                + "FROM Ticket\n"
                + "FULL OUTER JOIN Agencia on(Ticket.idagencia=Agencia.id)\n"
                + "FULL OUTER JOIN Tipo on(Tipo.id=Agencia.idtipo)\n"
                + "FULL OUTER JOIN Ciudad on(Ciudad.id=Tipo.idciudad)\n"
                + "WHERE Ticket.estado=1\n";

        if (dato.getTime0() != null && dato.getTime2() != null) {
            query = query
                    + " and Ticket.time0 between '" + dato.getTime0() + " 00:00' and '" + dato.getTime2() + " 23:59'\n";
        }

        query = query
                + "ORDER BY Ticket.idheader desc\n"
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
            lista = (JSONArray) parser.parse(rsst);

            return lista;
        } catch (Exception e) {
            System.out.println(this.getClass().toString() + ".obtenerListadoExcel " + e.getMessage());
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
                System.out.println(this.getClass().toString() + ".obtenerListadoExcel " + e.getMessage());
            }
        }
    }

    public JSONArray obtenerDisponibilidadExcel(Ticket dato) {
        JSONArray lista = null;
        JSONParser parser = new JSONParser();
        String query
                = "Select \n"
                + "(Select sum(Ticket.tmins) \n"
                + "	FROM Ticket\n"
                + "	Where Ticket.idagencia = Agencia.id\n"
                + "	and Ticket.LAN = 1\n"
                + "	and Ticket.estado = 1 \n"
                + "	and Ticket.time0 between '" + dato.getTime0() + " 00:00' and '" + dato.getTime2() + " 23:59'\n"
                + "   ) as LAN,\n"
                + "Agencia.nombre as agencia,\n"
                + "Agencia.id as idagencia,\n"
                + "Tipo.nombre as tipo,\n"
                + "Ciudad.nombre as ciudad,\n"
                + "(Select Propietario.nombre as proveedor ,\n"
                + "  (Select sum(Ticket.tmins) \n"
                + "	FROM Ticket\n"
                + "     FULL OUTER JOIN TTHeader ON(TTHeader.id = Ticket.idheader)\n"
                + "	FULL OUTER JOIN Problema ON (Problema.id = TTHeader.idproblema)\n"
                + "	Where Ticket.idenlace = Enlace.id\n"
                + "	and Ticket.estado = 1 \n"
                + "     and Ticket.tcompleto NOT LIKE 'NO APLICA'\n"
                + "	and Ticket.time0 between '" + dato.getTime0() + " 00:00' and '" + dato.getTime2() + " 23:59'\n"
                + "     and Problema.id = 2 \n"
                + "   ) as sumtmins,\n"
                + "   Entidades.nombre as propietario,\n"
                + "   Agencia.nombre as puntob\n"
                + "  From Propietario  \n"
                + "  FULL OUTER JOIN Enlace on(Enlace.idagencia = Agencia.id) \n"
                + "  FULL OUTER JOIN Agencia on(Enlace.punto = Agencia.id) \n"
                + "  FULL OUTER JOIN Entidades on(Enlace.idpropiedad = Entidades.id) \n"
                + "  Where Propietario.id=Enlace.idproveedor and Enlace.estado !=0\n"
                + "  FOR JSON AUTO) as Proveedores\n"
                + "FROM Agencia\n"
                + "FULL OUTER JOIN Tipo on (Agencia.idtipo  = Tipo.id)\n"
                + "FULL OUTER JOIN Ciudad on (Ciudad.id  = Tipo.idciudad)\n"
                + "Where Agencia.estado = 1\n"
                + "and (SELECT Count(Enlace.id) FROM Enlace WHERE Enlace.idagencia = Agencia.id and Enlace.estado !=0 )!=0\n"
                + "ORDER BY Ciudad.nombre asc, Tipo.nombre asc, CAST(Agencia.nombre as Varchar) asc\n"
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
            lista = (JSONArray) parser.parse(rsst);

            return lista;
        } catch (Exception e) {
            System.out.println(this.getClass().toString() + ".obtenerDisponibilidadExcel " + e.getMessage());
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
                System.out.println(this.getClass().toString() + ".obtenerDisponibilidadExcel " + e.getMessage());
            }
        }
    }

    public boolean createExcel(Ticket dato, String path) {
        try {
            XSSFWorkbook workbook = new XSSFWorkbook();

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

            XSSFSheet sheet = workbook.createSheet("Ticket");
            sheet.setDisplayGridlines(false);
            sheet.setDefaultColumnWidth(15);

            XSSFSheet sheetTOTAL = workbook.createSheet("DISPONIBILIDAD");
            sheetTOTAL.setDisplayGridlines(false);
            sheetTOTAL.setDefaultColumnWidth(15);

            XSSFSheet sheetEstad = workbook.createSheet("AUX");
            sheet.setDisplayGridlines(false);
            sheet.setDefaultColumnWidth(15);

            Row herderRef = sheet.createRow(0);
            Cell cellherderRef = herderRef.createCell(0);
            cellherderRef.setCellValue("Tickets");
            cellherderRef.setCellStyle(headStInventario);

            Row herderRefTOTAL = sheetTOTAL.createRow(0);
            Cell cellherderRefTOTAL = herderRefTOTAL.createCell(0);
            cellherderRefTOTAL.setCellValue("DISPONIBILIDAD");
            cellherderRefTOTAL.setCellStyle(headStInventario);

            Row herderRefEstad = sheetEstad.createRow(0);
            Cell cellherderRefEstad = herderRefEstad.createCell(0);
            cellherderRefEstad.setCellValue("Cantidad de enlaces por proveedor");
            cellherderRefEstad.setCellStyle(headStInventario);

            sheet.addMergedRegion(new CellRangeAddress(
                    0, //first row (0-based)
                    1, //last row  (0-based)
                    0, //first column (0-based)
                    22 //last column  (0-based)
            ));

            sheetTOTAL.addMergedRegion(new CellRangeAddress(
                    0, //first row (0-based)
                    1, //last row  (0-based)
                    0, //first column (0-based)
                    7 //last column  (0-based)
            ));

            sheetTOTAL.setAutoFilter(new CellRangeAddress(
                    2, //first row (0-based)
                    2, //last row  (0-based)
                    0, //first column (0-based)
                    7 //last column  (0-based)
            ));
            sheetEstad.addMergedRegion(new CellRangeAddress(
                    0, //first row (0-based)
                    1, //last row  (0-based)
                    0, //first column (0-based)
                    4 //last column  (0-based)
            ));

            Row header = sheet.createRow(2);
            Row headerTOTAL = sheetTOTAL.createRow(2);
            Row headerEstad = sheetEstad.createRow(2);

            XSSFFont font = ((XSSFWorkbook) workbook).createFont();
            font.setFontName("Calibri");
            font.setFontHeightInPoints((short) 10);
            font.setBold(true);
            font.setColor(IndexedColors.WHITE.getIndex());

            CellStyle style = workbook.createCellStyle();
            style.setBorderBottom(BorderStyle.MEDIUM);
            style.setBorderLeft(BorderStyle.MEDIUM);
            style.setBorderRight(BorderStyle.MEDIUM);
            style.setBorderTop(BorderStyle.MEDIUM);

            CellStyle gris = workbook.createCellStyle();
            gris.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            gris.setWrapText(true);
            gris.setBorderBottom(BorderStyle.MEDIUM);
            gris.setBorderLeft(BorderStyle.MEDIUM);
            gris.setBorderRight(BorderStyle.MEDIUM);
            gris.setBorderTop(BorderStyle.MEDIUM);
            gris.setVerticalAlignment(VerticalAlignment.CENTER);
            gris.setAlignment(HorizontalAlignment.CENTER);
            gris.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());

            CellStyle dhm = workbook.createCellStyle();
            DataFormat format = workbook.createDataFormat();
            dhm.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            dhm.setWrapText(true);
            dhm.setBorderBottom(BorderStyle.MEDIUM);
            dhm.setBorderLeft(BorderStyle.MEDIUM);
            dhm.setBorderRight(BorderStyle.MEDIUM);
            dhm.setBorderTop(BorderStyle.MEDIUM);
            dhm.setVerticalAlignment(VerticalAlignment.CENTER);
            dhm.setAlignment(HorizontalAlignment.CENTER);
            dhm.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            dhm.setDataFormat(format.getFormat("d\"d\" hh\"h\" mm\"m\""));

            CellStyle dhm2 = workbook.createCellStyle();
            dhm2.setBorderBottom(BorderStyle.MEDIUM);
            dhm2.setBorderLeft(BorderStyle.MEDIUM);
            dhm2.setBorderRight(BorderStyle.MEDIUM);
            dhm2.setBorderTop(BorderStyle.MEDIUM);
            dhm2.setDataFormat(format.getFormat("d\"d\" hh\"h\" mm\"m\""));

            CellStyle azul = workbook.createCellStyle();
            azul.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            azul.setWrapText(true);
            azul.setBorderBottom(BorderStyle.MEDIUM);
            azul.setBorderLeft(BorderStyle.MEDIUM);
            azul.setBorderRight(BorderStyle.MEDIUM);
            azul.setBorderTop(BorderStyle.MEDIUM);
            azul.setVerticalAlignment(VerticalAlignment.CENTER);
            azul.setAlignment(HorizontalAlignment.CENTER);
            azul.setFont(font);
            azul.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());

            CellStyle rojo = workbook.createCellStyle();
            rojo.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            rojo.setWrapText(true);
            rojo.setBorderBottom(BorderStyle.MEDIUM);
            rojo.setBorderLeft(BorderStyle.MEDIUM);
            rojo.setBorderRight(BorderStyle.MEDIUM);
            rojo.setBorderTop(BorderStyle.MEDIUM);
            rojo.setVerticalAlignment(VerticalAlignment.CENTER);
            rojo.setAlignment(HorizontalAlignment.CENTER);
            rojo.setFont(font);
            rojo.setFillForegroundColor(IndexedColors.RED1.getIndex());

            Cell headerCell = header.createCell(0);
            headerCell.setCellValue("Ticket");
            headerCell.setCellStyle(azul);

            headerCell = header.createCell(1);
            headerCell.setCellValue("Localidad (afeccion)");
            headerCell.setCellStyle(rojo);

            headerCell = header.createCell(2);
            headerCell.setCellValue("Tecnico Responsable");
            headerCell.setCellStyle(azul);

            headerCell = header.createCell(3);
            headerCell.setCellValue("Reportado por");
            headerCell.setCellStyle(azul);

            headerCell = header.createCell(4);
            headerCell.setCellValue("Sucursal");
            headerCell.setCellStyle(azul);

            headerCell = header.createCell(5);
            headerCell.setCellValue("Tipo");
            headerCell.setCellStyle(azul);

            headerCell = header.createCell(6);
            headerCell.setCellValue("Localidad");
            headerCell.setCellStyle(azul);

            headerCell = header.createCell(7);
            headerCell.setCellValue("Tiempo Completo");
            headerCell.setCellStyle(azul);

            headerCell = header.createCell(8);
            headerCell.setCellValue("Fecha Inicio de Incidencia");
            headerCell.setCellStyle(azul);

            headerCell = header.createCell(9);
            headerCell.setCellValue("Hora Inicio de Incidencia");
            headerCell.setCellStyle(azul);

            headerCell = header.createCell(10);
            headerCell.setCellValue("Fecha cargada al proveedor");
            headerCell.setCellStyle(azul);

            headerCell = header.createCell(11);
            headerCell.setCellValue("Hora cargada al proveedor");
            headerCell.setCellStyle(azul);

            headerCell = header.createCell(12);
            headerCell.setCellValue("Fecha cierre de incidencia");
            headerCell.setCellStyle(azul);

            headerCell = header.createCell(13);
            headerCell.setCellValue("Hora cierre de incidencia");
            headerCell.setCellStyle(azul);

            headerCell = header.createCell(14);
            headerCell.setCellValue("Tiempo (dias horas)");
            headerCell.setCellStyle(rojo);

            headerCell = header.createCell(15);
            headerCell.setCellValue("Tiempo (m)");
            headerCell.setCellStyle(rojo);

            headerCell = header.createCell(16);
            headerCell.setCellValue("Estado de Ticket");
            headerCell.setCellStyle(rojo);

            headerCell = header.createCell(17);
            headerCell.setCellValue("Afección LAN");
            headerCell.setCellStyle(azul);

            headerCell = header.createCell(18);
            headerCell.setCellValue("Tipo de Problema");
            headerCell.setCellStyle(azul);

            headerCell = header.createCell(19);
            headerCell.setCellValue("Proveedor");
            headerCell.setCellStyle(azul);

            headerCell = header.createCell(20);
            headerCell.setCellValue("Persona que dio soporte");
            headerCell.setCellStyle(azul);
            
            headerCell = header.createCell(21);
            headerCell.setCellValue("Proveedor Ticket");
            headerCell.setCellStyle(azul);

            headerCell = header.createCell(22);
            headerCell.setCellValue("Descripcion del problema");
            headerCell.setCellStyle(azul);

            Cell headerCellTOTAL = headerTOTAL.createCell(0);
            headerCellTOTAL.setCellValue("Sucursal");
            headerCellTOTAL.setCellStyle(azul);

            headerCellTOTAL = headerTOTAL.createCell(1);
            headerCellTOTAL.setCellValue("Tipo");
            headerCellTOTAL.setCellStyle(azul);

            headerCellTOTAL = headerTOTAL.createCell(2);
            headerCellTOTAL.setCellValue("Localidad");
            headerCellTOTAL.setCellStyle(azul);

            headerCellTOTAL = headerTOTAL.createCell(3);
            headerCellTOTAL.setCellValue("Proveedores");
            headerCellTOTAL.setCellStyle(azul);

            headerCellTOTAL = headerTOTAL.createCell(4);
            headerCellTOTAL.setCellValue("Tiempo down(m)");
            headerCellTOTAL.setCellStyle(rojo);

            headerCellTOTAL = headerTOTAL.createCell(5);
            headerCellTOTAL.setCellValue("Tiempo de disponibilidad");
            headerCellTOTAL.setCellStyle(rojo);

            headerCellTOTAL = headerTOTAL.createCell(6);
            headerCellTOTAL.setCellValue("Propietario");
            headerCellTOTAL.setCellStyle(azul);

            headerCellTOTAL = headerTOTAL.createCell(7);
            headerCellTOTAL.setCellValue("Punto B (LLEGADA)");
            headerCellTOTAL.setCellStyle(azul);

            Cell headerCellEstad = headerEstad.createCell(0);
            headerCellEstad.setCellValue("Proveedor");
            headerCellEstad.setCellStyle(azul);

            headerCellEstad = headerEstad.createCell(1);
            headerCellEstad.setCellValue("VES/CIN/CIAC");
            headerCellEstad.setCellStyle(azul);

            headerCellEstad = headerEstad.createCell(2);
            headerCellEstad.setCellValue("ATM/INT");
            headerCellEstad.setCellStyle(azul);

            headerCellEstad = headerEstad.createCell(3);
            headerCellEstad.setCellValue("EEXT");
            headerCellEstad.setCellStyle(azul);

            headerCellEstad = headerEstad.createCell(4);
            headerCellEstad.setCellValue("TOTAL");
            headerCellEstad.setCellStyle(azul);

            long minutosmes = 0l;
            if (dato.getTime0() != null && dato.getTime2() != null) {
                LocalDateTime ini = LocalDateTime.parse((CharSequence) dato.getTime0() + "T00:00");
                LocalDateTime end = LocalDateTime.parse((CharSequence) dato.getTime2() + "T23:59");
                minutosmes = ChronoUnit.MINUTES.between(ini, end);

            }

            JSONArray lista = obtenerListadoExcel(dato);
            JSONArray lista2 = obtenerDisponibilidadExcel(dato);
            JSONArray lista3 = obtenerEstadisticaExcel(dato);
            JSONArray lista4 = obtenerEstadisticaExcel2(dato);

            JSONParser parser = new JSONParser();

            int celda = 0;
            int fila = 3;
            Row row;

            int celdaTOTAL = 0;
            int filaTOTAL = 3;
            Row rowTOTAL;

            int celdaEstad = 0;
            int filaEstad = 3;
            Row rowEstad;

            int frfg1 = filaEstad;
            int lrfg1 = filaEstad;

            int frfg2 = filaEstad;
            int lrfg2 = filaEstad;

            int frfg3 = filaEstad;
            int lrfg3 = filaEstad;

            int frfg4 = filaEstad;
            int lrfg4 = filaEstad;

            for (Object item : lista) {
                JSONObject inventario = (JSONObject) parser.parse(item.toString());

                JSONArray ttheaderarray = (JSONArray) parser.parse(inventario.get("TTHeader").toString());
                JSONObject ttheader = (JSONObject) parser.parse(ttheaderarray.get(0).toString());

                JSONArray proveeodrarray = (JSONArray) parser.parse(ttheader.get("Propietario").toString());
                JSONObject proveedorobj = (JSONObject) parser.parse(proveeodrarray.get(0).toString());

                JSONArray problemaarray = (JSONArray) parser.parse(proveedorobj.get("Problema").toString());
                JSONObject problemaobj = (JSONObject) parser.parse(problemaarray.get(0).toString());

                JSONArray agenciararray = (JSONArray) parser.parse(inventario.get("Agencia").toString());
                JSONObject agenciaobj = (JSONObject) parser.parse(agenciararray.get(0).toString());

                JSONArray tiporarray = (JSONArray) parser.parse(agenciaobj.get("Tipo").toString());
                JSONObject tipoobj = (JSONObject) parser.parse(tiporarray.get(0).toString());

                JSONArray ciudadararray = (JSONArray) parser.parse(tipoobj.get("Ciudad").toString());
                JSONObject ciudadobj = (JSONObject) parser.parse(ciudadararray.get(0).toString());

                String tcompleto = inventario.get("tcompleto") == null ? "" : inventario.get("tcompleto").toString();
                String tmins = inventario.get("tmins") == null ? "" : inventario.get("tmins").toString();
                String tdias = inventario.get("tdias") == null ? "" : inventario.get("tdias").toString();
                String time0 = inventario.get("time0") == null ? "" : inventario.get("time0").toString();
                String time1 = inventario.get("time1") == null ? "" : inventario.get("time1").toString();
                String time2 = inventario.get("time2") == null ? "" : inventario.get("time2").toString();
                String adicional = inventario.get("adicional") == null ? "" : inventario.get("adicional").toString();
                String LAN = (Boolean) inventario.get("LAN") ? "SI" : "NO";
                Boolean tthabierto = (Boolean) ttheader.get("tthabierto");
                Boolean tabierto = (Boolean) inventario.get("abierto");
                String tecnicorespon = ttheader.get("tecnicorespon") == null ? "" : ttheader.get("tecnicorespon").toString();
                String descripcion = ttheader.get("descripcion") == null ? "" : ttheader.get("descripcion").toString();
                String ttprov = ttheader.get("ttproveedor") == null ? "" : ttheader.get("ttproveedor").toString();
                String tecnicoreporte = ttheader.get("tecnicoreporte") == null ? "" : ttheader.get("tecnicoreporte").toString();
                String soporte = ttheader.get("soporte") == null ? "" : ttheader.get("soporte").toString();
                String fechai = ttheader.get("fecha") == null ? "" : ttheader.get("fecha").toString();
                String conteo = ttheader.get("conteo") == null ? "" : ttheader.get("conteo").toString();
                String ciudad = ciudadobj.get("ciudad") == null ? "" : ciudadobj.get("ciudad").toString();
                String tipo = tipoobj.get("tipo") == null ? "" : tipoobj.get("tipo").toString();
                String agencia = agenciaobj.get("agencia") == null ? "" : agenciaobj.get("agencia").toString();
                String proveedor = proveedorobj.get("proveedor") == null ? "-" : proveedorobj.get("proveedor").toString();
                String problema = problemaobj.get("problema") == null ? "" : problemaobj.get("problema").toString();
                String dfinal = descripcion + " " + ttprov + " " + adicional;
                String abierto = (tthabierto && tabierto) ? "CERRADO" : "ABIERTO";
                String t0 = "";
                String h0 = "";
                String t1 = "";
                String h1 = "";
                String t2 = "";
                String h2 = "";

                if (time0.length() > 0) {
                    String[] tarr = time0.split(" ");
                    if (tarr[0] != null) {
                        t0 = tarr[0];
                    }
                    if (tarr[1] != null) {
                        h0 = tarr[1];
                    }
                }
                if (time1.length() > 1) {
                    String[] tarr = time1.split(" ");
                    if (tarr[0] != null) {
                        t1 = tarr[0];
                    }
                    if (tarr[1] != null) {
                        h1 = tarr[1];
                    }
                }
                if (time2.length() > 1) {
                    String[] tarr = time2.split(" ");
                    if (tarr[0] != null) {
                        t2 = tarr[0];
                    }
                    if (tarr[1] != null) {
                        h2 = tarr[1];
                    }
                }

                String localidad = agencia;

                if (proveedor.length() > 1) {
                    localidad = localidad + "(" + proveedor + ")";
                } else {
                    localidad = localidad + "(LAN)";
                }
                row = sheet.createRow(fila);

                Cell cell = row.createCell(celda);
                cell.setCellValue(fechai + "-" + conteo);
                cell.setCellStyle(style);
                celda++;

                cell = row.createCell(celda);
                cell.setCellValue(localidad);
                cell.setCellStyle(style);
                celda++;

                cell = row.createCell(celda);
                cell.setCellValue(tecnicorespon);
                cell.setCellStyle(style);
                celda++;

                cell = row.createCell(celda);
                cell.setCellValue(tecnicoreporte);
                cell.setCellStyle(style);
                celda++;

                cell = row.createCell(celda);
                cell.setCellValue(ciudad);
                cell.setCellStyle(style);
                celda++;

                cell = row.createCell(celda);
                cell.setCellValue(tipo);
                cell.setCellStyle(style);
                celda++;

                cell = row.createCell(celda);
                cell.setCellValue(agencia);
                cell.setCellStyle(style);
                celda++;

                cell = row.createCell(celda);
                cell.setCellValue(tcompleto);
                cell.setCellStyle(style);
                celda++;

                cell = row.createCell(celda);
                cell.setCellValue(t0);
                cell.setCellStyle(style);
                celda++;

                cell = row.createCell(celda);
                cell.setCellValue(h0);
                cell.setCellStyle(style);
                celda++;

                cell = row.createCell(celda);
                cell.setCellValue(t1);
                cell.setCellStyle(style);
                celda++;

                cell = row.createCell(celda);
                cell.setCellValue(h1);
                cell.setCellStyle(style);
                celda++;

                cell = row.createCell(celda);
                cell.setCellValue(t2);
                cell.setCellStyle(style);
                celda++;

                cell = row.createCell(celda);
                cell.setCellValue(h2);
                cell.setCellStyle(style);
                celda++;

                cell = row.createCell(celda);
                cell.setCellValue(tdias);
                cell.setCellStyle(style);
                celda++;

                cell = row.createCell(celda);
                cell.setCellValue(tmins);
                cell.setCellStyle(style);
                celda++;

                cell = row.createCell(celda);
                cell.setCellValue(abierto);
                cell.setCellStyle(gris);
                celda++;

                cell = row.createCell(celda);
                cell.setCellValue(LAN);
                cell.setCellStyle(style);
                celda++;

                cell = row.createCell(celda);
                cell.setCellValue(problema);
                cell.setCellStyle(style);
                celda++;

                cell = row.createCell(celda);
                cell.setCellValue(proveedor);
                cell.setCellStyle(style);
                celda++;

                cell = row.createCell(celda);
                cell.setCellValue(soporte);
                cell.setCellStyle(style);
                celda++;
                
                cell = row.createCell(celda);
                cell.setCellValue(ttprov);
                cell.setCellStyle(style);
                celda++;

                cell = row.createCell(celda);
                cell.setCellValue(dfinal.trim());
                cell.setCellStyle(style);
                celda++;

                fila++;
                celda = 0;

            }

            for (Object item : lista2) {
                JSONObject inventario = (JSONObject) parser.parse(item.toString());

                JSONArray tipoarray = (JSONArray) parser.parse(inventario.get("Tipo").toString());
                JSONObject tipoobj = (JSONObject) parser.parse(tipoarray.get(0).toString());

                JSONArray ciudadaarray = (JSONArray) parser.parse(tipoobj.get("Ciudad").toString());
                JSONObject ciudadobj = (JSONObject) parser.parse(ciudadaarray.get(0).toString());

                JSONArray proveedorarray = (JSONArray) parser.parse(ciudadobj.get("Proveedores").toString());

                String agencia = inventario.get("agencia") == null ? "" : inventario.get("agencia").toString();
                int lan = inventario.get("LAN") == null ? 0 : Integer.parseInt(inventario.get("LAN").toString());
                String tipo = tipoobj.get("tipo") == null ? "" : tipoobj.get("tipo").toString();
                String ciudad = ciudadobj.get("ciudad") == null ? "" : ciudadobj.get("ciudad").toString();

                float perdida = (lan * 100) / (float) minutosmes;
                BigDecimal bd = new BigDecimal(100 - perdida);
                bd = bd.setScale(2, RoundingMode.HALF_UP);

                rowTOTAL = sheetTOTAL.createRow(filaTOTAL);
                Cell cellTotal = rowTOTAL.createCell(celdaTOTAL);
                cellTotal.setCellValue(ciudad);
                cellTotal.setCellStyle(style);
                celdaTOTAL++;

                cellTotal = rowTOTAL.createCell(celdaTOTAL);
                cellTotal.setCellValue(tipo);
                cellTotal.setCellStyle(style);
                celdaTOTAL++;

                cellTotal = rowTOTAL.createCell(celdaTOTAL);
                cellTotal.setCellValue(agencia);
                cellTotal.setCellStyle(style);
                celdaTOTAL++;

                cellTotal = rowTOTAL.createCell(celdaTOTAL);
                cellTotal.setCellValue("LAN");
                cellTotal.setCellStyle(style);
                celdaTOTAL++;

                cellTotal = rowTOTAL.createCell(celdaTOTAL);
                cellTotal.setCellValue(lan);
                cellTotal.setCellStyle(style);
                celdaTOTAL++;

                cellTotal = rowTOTAL.createCell(celdaTOTAL);
                cellTotal.setCellValue(bd.doubleValue());
                cellTotal.setCellStyle(style);
                celdaTOTAL++;

                cellTotal = rowTOTAL.createCell(celdaTOTAL);
                cellTotal.setCellValue("-");
                cellTotal.setCellStyle(style);
                celdaTOTAL++;

                cellTotal = rowTOTAL.createCell(celdaTOTAL);
                cellTotal.setCellValue("-");
                cellTotal.setCellStyle(style);
                celdaTOTAL++;

                filaTOTAL++;

                celdaTOTAL = 0;

                for (Object item2 : proveedorarray) {
                    JSONObject proveedorobj = (JSONObject) parser.parse(item2.toString());
                    String proveedor = proveedorobj.get("proveedor") == null ? "" : proveedorobj.get("proveedor").toString();
                    JSONArray propietarioarray = (JSONArray) parser.parse(proveedorobj.get("Entidades").toString());
                    JSONObject propietarioobj = (JSONObject) parser.parse(propietarioarray.get(0).toString());
                    String propietario = propietarioobj.get("propietario") == null ? "" : propietarioobj.get("propietario").toString();
                    JSONArray puntobarray = (JSONArray) parser.parse(propietarioobj.get("Agencia").toString());
                    JSONObject puntobobj = (JSONObject) parser.parse(puntobarray.get(0).toString());
                    String puntob = puntobobj.get("puntob") == null ? "" : puntobobj.get("puntob").toString();

                    int tmins = proveedorobj.get("sumtmins") == null ? 0 : Integer.parseInt(proveedorobj.get("sumtmins").toString());
                    perdida = (tmins * 100) / (float) minutosmes;
                    bd = new BigDecimal(100 - perdida);
                    bd = bd.setScale(2, RoundingMode.HALF_UP);

                    rowTOTAL = sheetTOTAL.createRow(filaTOTAL);
                    cellTotal = rowTOTAL.createCell(celdaTOTAL);
                    cellTotal.setCellValue(ciudad);
                    cellTotal.setCellStyle(style);
                    celdaTOTAL++;

                    cellTotal = rowTOTAL.createCell(celdaTOTAL);
                    cellTotal.setCellValue(tipo);
                    cellTotal.setCellStyle(style);
                    celdaTOTAL++;

                    cellTotal = rowTOTAL.createCell(celdaTOTAL);
                    cellTotal.setCellValue(agencia);
                    cellTotal.setCellStyle(style);
                    celdaTOTAL++;

                    cellTotal = rowTOTAL.createCell(celdaTOTAL);
                    cellTotal.setCellValue(proveedor);
                    cellTotal.setCellStyle(style);
                    celdaTOTAL++;

                    cellTotal = rowTOTAL.createCell(celdaTOTAL);
                    cellTotal.setCellValue(tmins);
                    cellTotal.setCellStyle(style);
                    celdaTOTAL++;

                    cellTotal = rowTOTAL.createCell(celdaTOTAL);
                    cellTotal.setCellValue(bd.doubleValue());
                    cellTotal.setCellStyle(style);
                    celdaTOTAL++;

                    cellTotal = rowTOTAL.createCell(celdaTOTAL);
                    cellTotal.setCellValue(propietario);
                    cellTotal.setCellStyle(style);
                    celdaTOTAL++;

                    cellTotal = rowTOTAL.createCell(celdaTOTAL);
                    cellTotal.setCellValue(puntob);
                    cellTotal.setCellStyle(style);
                    celdaTOTAL++;

                    filaTOTAL++;
                    celdaTOTAL = 0;

                }

            }

            rowTOTAL = sheetTOTAL.createRow(filaTOTAL);
            Cell cellTotal = rowTOTAL.createCell(celdaTOTAL);
            cellTotal.setCellValue("");
            cellTotal.setCellStyle(style);
            celdaTOTAL++;

            cellTotal = rowTOTAL.createCell(celdaTOTAL);
            cellTotal.setCellValue("");
            cellTotal.setCellStyle(style);
            celdaTOTAL++;

            cellTotal = rowTOTAL.createCell(celdaTOTAL);
            cellTotal.setCellValue("");
            cellTotal.setCellStyle(style);
            celdaTOTAL++;

            cellTotal = rowTOTAL.createCell(celdaTOTAL);
            cellTotal.setCellValue("");
            cellTotal.setCellStyle(style);
            celdaTOTAL++;

            cellTotal = rowTOTAL.createCell(celdaTOTAL);
            cellTotal.setCellValue("");
            cellTotal.setCellStyle(style);
            celdaTOTAL++;

            cellTotal = rowTOTAL.createCell(celdaTOTAL);
            cellTotal.setCellValue("");
            cellTotal.setCellStyle(style);
            celdaTOTAL++;

            cellTotal = rowTOTAL.createCell(celdaTOTAL);
            cellTotal.setCellValue("");
            cellTotal.setCellStyle(style);
            celdaTOTAL++;

            cellTotal = rowTOTAL.createCell(celdaTOTAL);
            cellTotal.setCellValue("");
            cellTotal.setCellStyle(style);
            celdaTOTAL++;

            String formula = "ROUND(SUBTOTAL(101,F4:F" + filaTOTAL + "),2)";

            filaTOTAL++;
            celdaTOTAL = 0;

            rowTOTAL = sheetTOTAL.createRow(filaTOTAL);
            cellTotal = rowTOTAL.createCell(celdaTOTAL);
            cellTotal.setCellValue("Total");
            cellTotal.setCellStyle(gris);
            celdaTOTAL++;

            cellTotal = rowTOTAL.createCell(celdaTOTAL);
            cellTotal.setCellValue("");
            cellTotal.setCellStyle(gris);
            celdaTOTAL++;

            cellTotal = rowTOTAL.createCell(celdaTOTAL);
            cellTotal.setCellValue("");
            cellTotal.setCellStyle(gris);
            celdaTOTAL++;

            cellTotal = rowTOTAL.createCell(celdaTOTAL);
            cellTotal.setCellValue("");
            cellTotal.setCellStyle(gris);
            celdaTOTAL++;

            cellTotal = rowTOTAL.createCell(celdaTOTAL);
            cellTotal.setCellValue("");
            cellTotal.setCellStyle(gris);
            celdaTOTAL++;

            cellTotal = rowTOTAL.createCell(celdaTOTAL);
            cellTotal.setCellFormula(formula);
            cellTotal.setCellStyle(gris);
            celdaTOTAL++;

            cellTotal = rowTOTAL.createCell(celdaTOTAL);
            cellTotal.setCellValue("");
            cellTotal.setCellStyle(gris);
            celdaTOTAL++;

            cellTotal = rowTOTAL.createCell(celdaTOTAL);
            cellTotal.setCellValue("");
            cellTotal.setCellStyle(gris);
            celdaTOTAL++;

            filaTOTAL++;
            celdaTOTAL = 0;

            frfg1 = filaEstad;

            for (Object item : lista3) {
                JSONObject inventario = (JSONObject) parser.parse(item.toString());
                String ext = inventario.get("EXT") == null ? "" : inventario.get("EXT").toString();
                String cinciacves = inventario.get("CINCIACVES") == null ? "0" : inventario.get("CINCIACVES").toString();
                String atmint = inventario.get("ATMINT") == null ? "0" : inventario.get("ATMINT").toString();
                String nombre = inventario.get("nombre") == null ? "0" : inventario.get("nombre").toString();
                int enlace = inventario.get("Enlace") == null ? 0 : Integer.parseInt(inventario.get("Enlace").toString());

                if (enlace != 0) {
                    rowEstad = sheetEstad.createRow(filaEstad);
                    Cell cellEstad = rowEstad.createCell(celdaEstad);
                    cellEstad.setCellValue(nombre);
                    cellEstad.setCellStyle(style);
                    celdaEstad++;

                    cellEstad = rowEstad.createCell(celdaEstad);
                    cellEstad.setCellValue(Integer.parseInt(cinciacves));
                    cellEstad.setCellStyle(style);
                    celdaEstad++;

                    cellEstad = rowEstad.createCell(celdaEstad);
                    cellEstad.setCellValue(Integer.parseInt(atmint));
                    cellEstad.setCellStyle(style);
                    celdaEstad++;

                    cellEstad = rowEstad.createCell(celdaEstad);
                    cellEstad.setCellValue(Integer.parseInt(ext));
                    cellEstad.setCellStyle(style);
                    celdaEstad++;

                    cellEstad = rowEstad.createCell(celdaEstad);
                    cellEstad.setCellValue(enlace);
                    cellEstad.setCellStyle(style);
                    celdaEstad++;

                    filaEstad++;
                    celdaEstad = 0;

                }

            }

            lrfg1 = filaEstad;

            String formulaCCV = "SUM(B4:B" + filaEstad + ")";
            String formulaAI = "SUM(C4:C" + filaEstad + ")";
            String formulaEXT = "SUM(D4:D" + filaEstad + ")";
            String formulaEnlaces = "SUM(E4:E" + filaEstad + ")";

            rowEstad = sheetEstad.createRow(filaEstad);
            Cell cellEstad = rowEstad.createCell(celdaEstad);
            cellEstad.setCellValue("Total");
            cellEstad.setCellStyle(gris);
            celdaEstad++;

            cellEstad = rowEstad.createCell(celdaEstad);
            cellEstad.setCellFormula(formulaCCV);
            cellEstad.setCellStyle(gris);
            celdaEstad++;

            cellEstad = rowEstad.createCell(celdaEstad);
            cellEstad.setCellFormula(formulaAI);
            cellEstad.setCellStyle(gris);
            celdaEstad++;

            cellEstad = rowEstad.createCell(celdaEstad);
            cellEstad.setCellFormula(formulaEXT);
            cellEstad.setCellStyle(gris);
            celdaEstad++;

            cellEstad = rowEstad.createCell(celdaEstad);
            cellEstad.setCellFormula(formulaEnlaces);
            cellEstad.setCellStyle(gris);
            celdaEstad++;

            filaEstad++;
            celdaEstad = 0;

            //ENLACES - INCIDENTE
            filaEstad++;
            filaEstad++;
            filaEstad++;
            Row headerEstad2 = sheetEstad.createRow(filaEstad);
            Cell headerCellEstad2 = headerEstad2.createCell(0);
            headerCellEstad2.setCellValue("Proveedor");
            headerCellEstad2.setCellStyle(azul);

            headerCellEstad2 = headerEstad2.createCell(1);
            headerCellEstad2.setCellValue("Enlaces");
            headerCellEstad2.setCellStyle(azul);

            headerCellEstad2 = headerEstad2.createCell(2);
            headerCellEstad2.setCellValue("Incidencias");
            headerCellEstad2.setCellStyle(azul);

            headerCellEstad2 = headerEstad2.createCell(3);
            headerCellEstad2.setCellValue("Indice");
            headerCellEstad2.setCellStyle(azul);

            filaEstad++;
            frfg2 = filaEstad;

            for (Object item : lista3) {
                JSONObject inventario = (JSONObject) parser.parse(item.toString());
                int ticket = inventario.get("Ticket") == null ? 0 : Integer.parseInt(inventario.get("Ticket").toString());
                String nombre = inventario.get("nombre") == null ? "0" : inventario.get("nombre").toString();
                int enlace = inventario.get("Enlace") == null ? 0 : Integer.parseInt(inventario.get("Enlace").toString());

                if (enlace != 0) {

                    float calticket = (float) ticket / (float) enlace;
                    BigDecimal indiceticket = new BigDecimal(calticket);
                    indiceticket = indiceticket.setScale(2, RoundingMode.HALF_UP);

                    rowEstad = sheetEstad.createRow(filaEstad);
                    cellEstad = rowEstad.createCell(celdaEstad);
                    cellEstad.setCellValue(nombre);
                    cellEstad.setCellStyle(style);
                    celdaEstad++;

                    cellEstad = rowEstad.createCell(celdaEstad);
                    cellEstad.setCellValue(enlace);
                    cellEstad.setCellStyle(style);
                    celdaEstad++;

                    cellEstad = rowEstad.createCell(celdaEstad);
                    cellEstad.setCellValue(ticket);
                    cellEstad.setCellStyle(style);
                    celdaEstad++;

                    cellEstad = rowEstad.createCell(celdaEstad);
                    cellEstad.setCellValue(indiceticket.doubleValue());
                    cellEstad.setCellStyle(style);
                    celdaEstad++;

                    filaEstad++;
                    celdaEstad = 0;

                }

            }

            lrfg2 = filaEstad;
            filaEstad++;
            filaEstad++;
            filaEstad++;

            Row headerEstad3 = sheetEstad.createRow(filaEstad);
            Cell headerCellEstad3 = headerEstad3.createCell(0);
            headerCellEstad3.setCellValue("Proveedor");
            headerCellEstad3.setCellStyle(azul);

            headerCellEstad3 = headerEstad3.createCell(1);
            headerCellEstad3.setCellValue("Tiempo de respuesta");
            headerCellEstad3.setCellStyle(azul);

            headerCellEstad3 = headerEstad3.createCell(2);
            headerCellEstad3.setCellValue("Incidencias");
            headerCellEstad3.setCellStyle(azul);

            headerCellEstad3 = headerEstad3.createCell(3);
            headerCellEstad3.setCellValue("Indice");
            headerCellEstad3.setCellStyle(azul);

            filaEstad++;
            frfg3 = filaEstad;

            for (Object item : lista3) {
                JSONObject inventario = (JSONObject) parser.parse(item.toString());
                int ticket = inventario.get("Ticket") == null ? 0 : Integer.parseInt(inventario.get("Ticket").toString());
                int tiempo = inventario.get("Tiempo") == null ? 0 : Integer.parseInt(inventario.get("Tiempo").toString());
                String nombre = inventario.get("nombre") == null ? "0" : inventario.get("nombre").toString();
                int enlace = inventario.get("Enlace") == null ? 0 : Integer.parseInt(inventario.get("Enlace").toString());

                if (enlace != 0) {

                    float tiempodhm = (float) tiempo / (float) 1440;
                    float caltiempo = tiempo == 0 ? (float) 0 : (float) ticket / tiempodhm;

                    BigDecimal indicetiempo = new BigDecimal(caltiempo);
                    BigDecimal tdhm = new BigDecimal(tiempodhm);

                    rowEstad = sheetEstad.createRow(filaEstad);
                    cellEstad = rowEstad.createCell(celdaEstad);
                    cellEstad.setCellValue(nombre);
                    cellEstad.setCellStyle(style);
                    celdaEstad++;

                    cellEstad = rowEstad.createCell(celdaEstad);
                    cellEstad.setCellValue(tdhm.doubleValue());
                    cellEstad.setCellStyle(dhm2);
                    celdaEstad++;

                    cellEstad = rowEstad.createCell(celdaEstad);
                    cellEstad.setCellValue(ticket);
                    cellEstad.setCellStyle(style);
                    celdaEstad++;

                    cellEstad = rowEstad.createCell(celdaEstad);
                    cellEstad.setCellValue(indicetiempo.doubleValue());
                    cellEstad.setCellStyle(dhm2);
                    celdaEstad++;

                    filaEstad++;
                    celdaEstad = 0;

                }

            }

            lrfg3 = filaEstad;

            filaEstad++;
            filaEstad++;
            filaEstad++;

            Row headerEstad4 = sheetEstad.createRow(filaEstad);
            Cell headerCellEstad4 = headerEstad4.createCell(0);
            headerCellEstad4.setCellValue("Estadística de Incidentes");
            headerCellEstad4.setCellStyle(azul);

            headerCellEstad4 = headerEstad4.createCell(1);
            headerCellEstad4.setCellValue("Suma CUENTA de Tickets");
            headerCellEstad4.setCellStyle(azul);

            filaEstad++;
            frfg4 = filaEstad;

            String formulaP = "SUM(B" + filaEstad + ":B";

            for (Object item : lista4) {
                JSONObject inventario = (JSONObject) parser.parse(item.toString());

                String nombre = inventario.get("nombre") == null ? "" : inventario.get("nombre").toString();
                int sumticket = inventario.get("sumticket") == null ? 0 : Integer.parseInt(inventario.get("sumticket").toString());
                if (sumticket > 0) {
                    rowEstad = sheetEstad.createRow(filaEstad);
                    cellEstad = rowEstad.createCell(celdaEstad);
                    cellEstad.setCellValue(nombre);
                    cellEstad.setCellStyle(style);
                    celdaEstad++;

                    cellEstad = rowEstad.createCell(celdaEstad);
                    cellEstad.setCellValue(sumticket);
                    cellEstad.setCellStyle(style);
                    celdaEstad++;

                    filaEstad++;
                    celdaEstad = 0;
                }

            }

            formulaP = formulaP + filaEstad + ")";

            rowEstad = sheetEstad.createRow(filaEstad);
            cellEstad = rowEstad.createCell(celdaEstad);
            cellEstad.setCellValue("Total");
            cellEstad.setCellStyle(gris);
            celdaEstad++;

            cellEstad = rowEstad.createCell(celdaEstad);
            cellEstad.setCellFormula(formulaP);
            cellEstad.setCellStyle(gris);
            celdaEstad++;

            lrfg4 = filaEstad;
            filaEstad++;
            celdaEstad = 0;

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
            sheet.autoSizeColumn(22);

            sheetTOTAL.autoSizeColumn(0);
            sheetTOTAL.autoSizeColumn(1);
            sheetTOTAL.autoSizeColumn(2);
            sheetTOTAL.autoSizeColumn(3);
            sheetTOTAL.autoSizeColumn(4);
            sheetTOTAL.autoSizeColumn(5);
            sheetTOTAL.autoSizeColumn(6);
            sheetTOTAL.autoSizeColumn(7);

            sheetEstad.autoSizeColumn(0);
            sheetEstad.autoSizeColumn(1);
            sheetEstad.autoSizeColumn(2);
            sheetEstad.autoSizeColumn(3);
            sheetEstad.autoSizeColumn(4);
            sheetEstad.autoSizeColumn(5);
            sheetEstad.autoSizeColumn(6);
            sheetEstad.autoSizeColumn(7);
            sheetEstad.autoSizeColumn(8);
            sheetEstad.autoSizeColumn(9);

            String arr1[] = {"VES/CIN/CIAC", "ATM/INTERNET", "EEXT", "TOTAL"};
            PresetColor c1[] = {PresetColor.DARK_BLUE, PresetColor.LIGHT_GRAY, PresetColor.LIGHT_BLUE, PresetColor.GRAY};
            String arr2[] = {"Enlaces", "Incidencias", "Indice"};
            PresetColor c2[] = {PresetColor.DARK_BLUE, PresetColor.LIGHT_GRAY, PresetColor.LIGHT_BLUE};
            String arr3[] = {"Tiempo de respuesta", "Incidencias", "Indice"};
            PresetColor c3[] = {PresetColor.DARK_BLUE, PresetColor.LIGHT_GRAY, PresetColor.LIGHT_BLUE};
            String arr4[] = {"Suma CUENTA de Tickets"};

            drawCol(frfg1, lrfg1, 0, 4, "", "Proveedores", "", sheetEstad, arr1, c1);
            drawCol(frfg2, lrfg2 - 1, 0, 3, "", "Proveedores", "", sheetEstad, arr2, c2);
            drawCol(frfg3, lrfg3 - 1, 0, 3, "", "Proveedores", "", sheetEstad, arr3, c3);
            drawPie(frfg4, lrfg4, 0, 1, "Estadística de Incidentes", sheetEstad, arr4);

            File f = new File(path);
            if (f.exists()) {
                f.delete();

            }
            try (FileOutputStream outputStream = new FileOutputStream(new File(path))) {
                workbook.write(outputStream);
                outputStream.close();
            }

            return true;
        } catch (Exception e) {
            System.out.println(this.getClass().toString() + ".crearExcel " + e.getMessage());
            return false;
        }
    }

    public void drawCol(int fr, int lr, int fc, int lc, String title, String xtitle, String ytitle, XSSFSheet sheetEstad, String[] subtitles, PresetColor[] color) {
        try {
            XSSFDrawing drawing = sheetEstad.createDrawingPatriarch();
            XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 6, fr, 16, fr + 11);

            XSSFChart chart = drawing.createChart(anchor);
            //chart.setTitleText(title);
            //chart.setTitleOverlay(false);

            XDDFChartLegend legend = chart.getOrAddLegend();
            legend.setPosition(LegendPosition.BOTTOM);

            XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
            //bottomAxis.setTitle(xtitle);
            bottomAxis.setCrosses(AxisCrosses.MAX);
            XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
            //leftAxis.setTitle(ytitle);
            leftAxis.setCrosses(AxisCrosses.AUTO_ZERO);

            XDDFChartData data = chart.createData(ChartTypes.BAR, bottomAxis, leftAxis);

            XDDFDataSource<String> source = XDDFDataSourcesFactory.fromStringCellRange(sheetEstad,
                    new CellRangeAddress(fr, lr + 1, fc, fc));

            for (int i = 1; i <= lc; i++) {
                XDDFNumericalDataSource<Double> values = XDDFDataSourcesFactory.fromNumericCellRange(sheetEstad,
                        new CellRangeAddress(fr, lr + 1, i, i));
                XDDFChartData.Series series1 = data.addSeries(source, values);
                series1.setTitle(subtitles[i - 1], null);

                XDDFSolidFillProperties fill = new XDDFSolidFillProperties(XDDFColor.from(color[i - 1]));
                XDDFShapeProperties properties = series1.getShapeProperties();
                if (properties == null) {
                    properties = new XDDFShapeProperties();
                }

                properties.setFillProperties(fill);
                series1.setShapeProperties(properties);
            }

            data.setVaryColors(Boolean.TRUE);

            chart.plot(data);

            XDDFBarChartData bar = (XDDFBarChartData) data;
            bar.setBarDirection(BarDirection.COL);

        } catch (Exception e) {

        }

    }

    public void drawPie(int fr, int lr, int fc, int lc, String title, XSSFSheet sheetEstad, String[] subtitles) {
        try {
            XSSFDrawing drawing = sheetEstad.createDrawingPatriarch();
            XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 6, fr, 16, fr + 20);

            XSSFChart chart = drawing.createChart(anchor);
            chart.setTitleText(title);
            chart.setTitleOverlay(false);

            XDDFChartData data = chart.createData(ChartTypes.PIE3D, null, null);

            XDDFDataSource<String> source = XDDFDataSourcesFactory.fromStringCellRange(sheetEstad,
                    new CellRangeAddress(fr, lr - 1, fc, fc));

            XDDFNumericalDataSource<Double> values = XDDFDataSourcesFactory.fromNumericCellRange(sheetEstad,
                    new CellRangeAddress(fr, lr - 1, 1, 1));
            XDDFChartData.Series series1 = data.addSeries(source, values);

            chart.plot(data);

            if (!chart.getCTChart().getPlotArea().getPie3DChartArray(0).getSerArray(0).isSetDLbls()) {
                chart.getCTChart().getPlotArea().getPie3DChartArray(0).getSerArray(0).addNewDLbls();
            }
            chart.getCTChart().getPlotArea().getPie3DChartArray(0).getSerArray(0).getDLbls().addNewShowVal().setVal(false);
            chart.getCTChart().getPlotArea().getPie3DChartArray(0).getSerArray(0).getDLbls().addNewShowSerName().setVal(false);
            chart.getCTChart().getPlotArea().getPie3DChartArray(0).getSerArray(0).getDLbls().addNewShowCatName().setVal(true);
            chart.getCTChart().getPlotArea().getPie3DChartArray(0).getSerArray(0).getDLbls().addNewShowPercent().setVal(true);
            chart.getCTChart().getPlotArea().getPie3DChartArray(0).getSerArray(0).getDLbls().addNewShowLegendKey().setVal(false);

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public boolean validarTiempos(String time0, String time1, String time2) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime t0 = LocalDateTime.parse(time0, formatter);
        LocalDateTime t2 = LocalDateTime.parse(time2, formatter);

        if (time1 == null) {
            return t2.isAfter(t0);
        } else {
            LocalDateTime  t1 = LocalDateTime .parse(time1, formatter);
            return t2.isAfter(t1) && t1.isAfter(t0);
        }
    }
}
