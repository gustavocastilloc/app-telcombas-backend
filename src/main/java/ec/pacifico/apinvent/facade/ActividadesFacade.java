/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.pacifico.apinvent.facade;

import ec.pacifico.apinvent.entity.ActHeader;
import ec.pacifico.apinvent.entity.Actividades;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
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
public class ActividadesFacade {

    private Conexion con;

    public ActividadesFacade(Conexion con) {
        this.con = con;
    }

    public JSONArray obtenerListado(Actividades dato) {
        JSONArray lista = null;
        JSONParser parser = new JSONParser();
        String query
                = "Select Actividades.id,Actividades.abierto,\n"
                + "CONVERT(Varchar,Actividades.fecha,20) as fecha,\n"
                + "CONVERT(Varchar,Actividades.fecha2,20) as fecha2,\n"
                + "Actividades.usuario,Actividades.mins, Actividades.estado,\n"
                + "(Select CONCAT(CONVERT(CHAR(8),TTHeader.fecha,112),'-',TTHeader.conteo) \n"
                + "FROM TTHeader \n"
                + "Where TTHeader.id = ActHeader.idticket\n"
                + ") as ticket,"
                + "HeaderActConf.nombre as actividad,\n"
                + "ActHeader.comentario as comentario,ActHeader.id as idact,ActHeader.abierto,ActHeader.abbanco,\n"
                + "SubActConf.nombre as subactividad\n"
                + "FROM Actividades\n"
                + "FULL OUTER JOIN ActHeader                        on(Actividades.idheader=ActHeader.id)\n"
                + "FULL OUTER JOIN ConfigActividad as HeaderActConf on(ActHeader.actividad=HeaderActConf.id)\n"
                + "FULL OUTER JOIN ConfigActividad as SubActConf    on(Actividades.subactividad=SubActConf.id)\n"
                + "WHERE \n";
        if (dato.getEstado() != null) {
            query = query + "Actividades.estado=" + dato.getEstado() + "\n";
        } else {
            query = query + "Actividades.estado !=0 \n";
        }
        if (dato.getHeadab() != null) {
            query = query + "and ActHeader.abierto=" + dato.getHeadab() + "\n";
        }
        if (dato.getAbierto() != null) {
            query = query + "and Actividades.abierto=" + dato.getAbierto() + "\n";
        }
        if (dato.getFecha() != null) {
            query = query + "and fecha between  '" + dato.getFecha().trim() + " 00:00:00:00' and '" + dato.getFecha().trim() + " 23:59:59:59'\n";
        }
        if (dato.getTime0() != null && dato.getTime2() != null) {
            query = query + "and fecha between  '" + dato.getTime0().trim() + " 00:00:00:00' and '" + dato.getTime2().trim() + " 23:59:59:59'\n";
        }
        if (dato.getSubactividad() != null) {
            query = query + "and Actividades.subactividad=" + dato.getSubactividad() + "\n";
        }
        if (dato.getActividad() != null) {
            query = query + "and ActHeader.actividad=" + dato.getActividad() + "\n";
        }
        if (dato.getComentario() != null) {
            query = query + "and ActHeader.comentario like '%" + dato.getComentario().trim() + "%'\n";
        }
        if (dato.getHeadabbanco() != null) {
            query = query + "and ActHeader.abbanco=" + dato.getHeadabbanco() + "\n";
        }
        if (dato.getUsuario() != null) {
            query = query + "and Actividades.usuario like '%" + dato.getUsuario().trim() + "%'\n";
        }
        if (dato.getId() != null) {
            query = query + "and Actividades.id =" + dato.getId() + "\n";
        }

        if (dato.getFecha() == null) {
            query = query
                    + "ORDER BY Actividades.id desc\n";
        } else {
            query = query
                    + "ORDER BY Actividades.fecha asc\n";
        }
        query = query
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

    public Integer obtenerListadoSize(Actividades dato) {
        String query
                = "SELECT COUNT(Actividades.id)\n"
                + "FROM Actividades\n"
                + "FULL OUTER JOIN ActHeader                        on(Actividades.idheader=ActHeader.id)\n"
                + "FULL OUTER JOIN ConfigActividad as HeaderActConf on(ActHeader.actividad=HeaderActConf.id)\n"
                + "FULL OUTER JOIN ConfigActividad as SubActConf    on(Actividades.subactividad=SubActConf.id)\n"
                + "WHERE \n";
        if (dato.getEstado() != null) {
            query = query + "Actividades.estado=" + dato.getEstado() + "\n";
        } else {
            query = query + "Actividades.estado !=0 \n";
        }
        if (dato.getHeadab() != null) {
            query = query + "and ActHeader.abierto=" + dato.getHeadab() + "\n";
        }
        if (dato.getAbierto() != null) {
            query = query + "and Actividades.abierto=" + dato.getAbierto() + "\n";
        }
        if (dato.getFecha() != null) {
            query = query + "and fecha between  '" + dato.getFecha().trim() + " 00:00:00:00' and '" + dato.getFecha().trim() + " 23:59:59:59'\n";
        }
        if (dato.getTime0() != null && dato.getTime2() != null) {
            query = query + "and fecha between  '" + dato.getTime0().trim() + " 00:00:00:00' and '" + dato.getTime2().trim() + " 23:59:59:59'\n";
        }
        if (dato.getSubactividad() != null) {
            query = query + "and Actividades.subactividad=" + dato.getSubactividad() + "\n";
        }
        if (dato.getActividad() != null) {
            query = query + "and ActHeader.actividad=" + dato.getActividad() + "\n";
        }
        if (dato.getComentario() != null) {
            query = query + "and ActHeader.comentario like '%" + dato.getComentario().trim() + "%'\n";
        }
        if (dato.getHeadabbanco() != null) {
            query = query + "and ActHeader.abbanco=" + dato.getHeadabbanco() + "\n";
        }
        if (dato.getUsuario() != null) {
            query = query + "and Actividades.usuario like '%" + dato.getUsuario().trim() + "%'\n";
        }
        if (dato.getId() != null) {
            query = query + "and Actividades.id =" + dato.getId() + "\n";
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

    public JSONArray obtenerListado2(Actividades dato) {
        JSONArray lista = null;
        JSONParser parser = new JSONParser();
        String query
                = "Select ActHeader.id, HeaderActConf.nombre as actividad\n"
                + "FROM ActHeader\n"
                + "FULL OUTER JOIN ConfigActividad as HeaderActConf on(ActHeader.actividad=HeaderActConf.id)\n"
                + "WHERE ActHeader.estado =1 and ActHeader.abbanco = 0 and ActHeader.abierto = 1\n"
                + "ORDER BY ActHeader.id desc\n"
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
            System.out.println(this.getClass().toString() + ".obtenerListado2 " + e.getMessage());
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
                System.out.println(this.getClass().toString() + ".obtenerListado2 " + e.getMessage());
            }
        }
    }

    public Integer obtenerListadoSize2(Actividades dato) {
        String query
                = "SELECT COUNT(ActHeader.id)\n"
                + "FROM ActHeader\n"
                + "FULL OUTER JOIN ConfigActividad as HeaderActConf on(ActHeader.actividad=HeaderActConf.id)\n"
                + "WHERE ActHeader.estado =1 and ActHeader.abbanco = 0 and ActHeader.abierto = 1\n";
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
            System.out.println(this.getClass().toString() + ".obtenerListadoSize2 " + e.getMessage());
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
                System.out.println(this.getClass().toString() + ".obtenerListadoSize2 " + e.getMessage());
            }
        }
    }

    public JSONArray busquedaIdHeader(Actividades dato) {
        JSONArray lista = null;
        JSONParser parser = new JSONParser();
        String query
                = "Select ActHeader.estado,ActHeader.id,ActHeader.abbanco,ActHeader.subactop, ActHeader.modificacion,ActHeader.fechamod,ActHeader.comentario,\n"
                + " ConfigActividad.nombre as actividad,ConfigActividad.id as idactividad,\n"
                + " (SELECT Actividades.id,Actividades.fecha,Actividades.mins,Actividades.usuario,Actividades.estado,\n"
                + "    ConfigActividad.nombre as subactividad, ConfigActividad.id\n"
                + "    FROM Actividades \n"
                + "    FULL OUTER JOIN ConfigActividad on(Actividades.subactividad=ConfigActividad.id)\n"
                + "    WHERE Actividades.idheader=ActHeader.id and Actividades.estado="+dato.getEstado()+" \n"
                + "     FOR JSON AUTO) as Actividades\n"
                + "FROM ActHeader\n"
                + "FULL OUTER JOIN ConfigActividad on(ActHeader.actividad=ConfigActividad.id)\n"
                + "WHERE ActHeader.id = " + dato.getId() + "\n"
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

    public boolean actualizarActHeader(ActHeader dato) {
        String query
                = "UPDATE ActHeader\n"
                + "  SET  estado =       " + dato.getEstado() + "\n"
                + "      ,abbanco =      " + dato.getAbbanco() + "\n"
                + "      ,abierto =      " + dato.getCheck() + "\n"
                + "      ,comentario =  '" + dato.getComentario().trim() + "'\n"
                + "      ,fechamod = CURRENT_TIMESTAMP \n"
                + "      ,modificacion ='" + dato.getUsername().trim() + "'\n"
                + " WHERE id= " + dato.getId() + ";\n";

        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            con.getConnection().setAutoCommit(false);
            if (dato.getSubactividades().size() > 0) {
                for (Actividades act : dato.getSubactividades()) {
                    query = query
                            + "UPDATE Actividades\n"
                            + "  SET  fechamod   = CURRENT_TIMESTAMP \n"
                            + ",modificacion   ='" + dato.getUsername().trim() + "'\n";

                    if (act.getFecha() != null) {
                        query = query
                                + ",fecha          ='" + act.getFecha().replace("T", " ").trim() + "'\n";
                    }
                    if (act.getFecha2() != null) {
                        query = query
                                + ",fecha2          ='" + act.getFecha2().replace("T", " ").trim() + "'\n";
                    }
                    if (act.getUsuario() != null) {
                        query = query
                                + ",usuario         ='" + act.getUsuario().trim() + "'\n";
                    }
                    query = query
                            + ",abierto        = " + act.getAbierto() + "\n"
                            + ",estado        = " + act.getEstado() + "\n"
                            + ",mins        = " + act.getMins() + "\n"
                            + " WHERE id = " + act.getId() + ";\n";
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

    public Boolean existeEvento(Actividades dato) {
        String query
                = "  SELECT id\n"
                + "  FROM Actividades\n"
                + "  Where estado = 1 and usuario like '" + dato.getUsuario().trim() + "' and \n";
        if (dato.getId() != null) {
            query = query
                    + "    id != " + dato.getId() + " and\n";
        }
        query = query
                + "  ((	fecha between  '" + dato.getFecha().replace("T", " ").trim() + "' and '" + dato.getFecha2().replace("T", " ").trim() + "' ) or\n"
                + "   (	fecha2 between '" + dato.getFecha().replace("T", " ").trim() + "' and '" + dato.getFecha2().replace("T", " ").trim() + "' ) or\n"
                + "   (	'" + dato.getFecha().replace("T", " ").trim() + "' between fecha and fecha2 ) or\n"
                + "   (	'" + dato.getFecha2().replace("T", " ").trim() + "' between fecha and fecha2 ))\n"
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
            System.out.println(this.getClass().toString() + ".existeEvento " + e.getMessage());
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
                System.out.println(this.getClass().toString() + ".existeEvento " + e.getMessage());
            }
        }
    }

    public boolean crear(ActHeader dato) {
        String query
                = "INSERT INTO ActHeader\n"
                + "           (id\n"
                + "           ,subactop\n"
                + "           ,abierto\n"
                + "           ,actividad\n"
                + "           ,comentario\n"
                + "           ,abbanco\n"
                + "           ,fechamod\n"
                + "           ,modificacion)\n"
                + "            OUTPUT Inserted.id \n"
                + "            VALUES\n"
                + "            (NEXT VALUE FOR SeqActHeader\n"
                + "           , " + dato.getSubactop() + "\n"
                + "           , " + dato.getCheck() + "\n"
                + "           , " + dato.getActividad() + "\n"
                + "           ,'" + dato.getComentario() + "'\n"
                + "           , " + dato.getAbbanco() + "\n"
                + "           , CURRENT_TIMESTAMP \n"
                + "           ,'" + dato.getUsername().trim() + "');\n";
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

            String query2 = "";
            for (Actividades act : dato.getSubactividades()) {
                query2 = query2
                        + "           INSERT INTO Actividades\n"
                        + "           (id\n"
                        + "           ,idheader\n"
                        + "           ,subactividad\n";
                if (act.getFecha() != null) {
                    query2 = query2
                            + "           ,fecha\n";
                }
                if (act.getFecha2() != null) {
                    query2 = query2
                            + "           ,fecha2\n";
                }
                if (act.getUsuario() != null) {
                    query2 = query2
                            + "           ,usuario\n";
                }
                query2 = query2
                        + "           ,mins\n"
                        + "           ,abierto\n"
                        + "           ,fechamod\n"
                        + "           ,modificacion)\n"
                        + "            OUTPUT Inserted.id \n"
                        + "            VALUES\n"
                        + "            (NEXT VALUE FOR SeqActividades\n"
                        + "           , " + idheader + "\n"
                        + "           , " + act.getSubactividad() + "\n";
                if (act.getFecha() != null) {
                    query2 = query2
                            + "           ,'" + act.getFecha().replace('T', ' ').trim() + "'\n";
                }
                if (act.getFecha2() != null) {
                    query2 = query2
                            + "           ,'" + act.getFecha2().replace('T', ' ').trim() + "'\n";
                }
                if (act.getUsuario() != null) {
                    query2 = query2
                            + "           ,'" + act.getUsuario().trim() + "'\n";
                }
                query2 = query2
                        + "           , " + act.getMins() + "\n"
                        + "           , " + act.getAbierto() + "\n"
                        + "           , CURRENT_TIMESTAMP \n"
                        + "           ,'" + dato.getUsername().trim() + "');\n";

            }

            if (query2.length() > 0) {
                st = con.getConnection().prepareStatement(query2);
                st.executeQuery();
            } else {
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

    public JSONArray obtenerListadoExcel(Actividades dato) {
        JSONArray lista = null;
        JSONParser parser = new JSONParser();
        String query
                = "Select CONVERT(Varchar,Actividades.fecha,20) as fecha,\n"
                + "Actividades.id,Actividades.usuario,Actividades.mins, Actividades.estado,\n"
                + "ActHeader.comentario as comentario,\n"
                + "HeaderActConf.nombre as actividad,\n"
                + "SubActConf.nombre as subactividad\n"
                + "FROM Actividades\n"
                + "FULL OUTER JOIN ActHeader                        on(Actividades.idheader=ActHeader.id)\n"
                + "FULL OUTER JOIN ConfigActividad as HeaderActConf on(ActHeader.actividad=HeaderActConf.id)\n"
                + "FULL OUTER JOIN ConfigActividad as SubActConf    on(Actividades.subactividad=SubActConf.id)\n"
                + "WHERE Actividades.estado = 1 and Actividades.abierto = 1\n"
                + "and fecha between  '" + dato.getTime0().trim() + " 00:00:00:00' and '" + dato.getTime2().trim() + " 23:59:59:59'\n"
                + "ORDER BY CAST(Actividades.usuario as Varchar) asc, Actividades.fecha asc\n"
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

    public boolean createExcel(Actividades dato, String path) {
        try {
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Actividades");
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
            cellherderRef.setCellValue("Actividades");
            cellherderRef.setCellStyle(headStInventario);

            sheet.addMergedRegion(new CellRangeAddress(
                    0, //first row (0-based)
                    1, //last row  (0-based)
                    0, //first column (0-based)
                    7 //last column  (0-based)
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
            headerCell.setCellValue("id");
            headerCell.setCellStyle(headerStyle);

            headerCell = header.createCell(1);
            headerCell.setCellValue("Usuario");
            headerCell.setCellStyle(headerStyle);

            headerCell = header.createCell(2);
            headerCell.setCellValue("Actividades");
            headerCell.setCellStyle(headerStyle);

            headerCell = header.createCell(3);
            headerCell.setCellValue("Subactividades");
            headerCell.setCellStyle(headerStyle);

            headerCell = header.createCell(4);
            headerCell.setCellValue("Fecha - Hora Inicio");
            headerCell.setCellStyle(headerStyle);

            headerCell = header.createCell(5);
            headerCell.setCellValue("Fecha - Hora Fin");
            headerCell.setCellStyle(headerStyle);

            headerCell = header.createCell(6);
            headerCell.setCellValue("Minutos");
            headerCell.setCellStyle(headerStyle);

            headerCell = header.createCell(7);
            headerCell.setCellValue("Comentario");
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
                String fecha1 = inventario.get("fecha") == null ? "N/A" : inventario.get("fecha").toString();
                String minutos = inventario.get("mins") == null ? "N/A" : inventario.get("mins").toString();
                String usuario = inventario.get("usuario") == null ? "N/A" : inventario.get("usuario").toString();
                String id = inventario.get("id") == null ? "N/A" : inventario.get("id").toString();

                JSONArray headerarray = (JSONArray) parser.parse(inventario.get("ActHeader").toString());
                JSONObject headervar = (JSONObject) parser.parse(headerarray.get(0).toString());
                String comentario = headervar.get("comentario") == null ? "N/A" : headervar.get("comentario").toString();

                JSONArray actarray = (JSONArray) parser.parse(headervar.get("HeaderActConf").toString());
                JSONObject act = (JSONObject) parser.parse(actarray.get(0).toString());
                String actividades = act.get("actividad") == null ? "N/A" : act.get("actividad").toString();

                JSONArray subactarray = (JSONArray) parser.parse(act.get("SubActConf").toString());
                JSONObject subact = (JSONObject) parser.parse(subactarray.get(0).toString());
                String subactividades = subact.get("subactividad") == null ? "N/A" : subact.get("subactividad").toString();

                LocalDateTime endActual = LocalDateTime.parse((CharSequence) fecha1.replace(" ", "T"));
                LocalDateTime endActualp = endActual.plusMinutes(Long.parseLong(minutos));
                String fecha2 = endActualp.toString().replace("T", " ");

                row = sheet.createRow(fila);

                Cell cell = row.createCell(celda);
                cell.setCellValue(id);
                cell.setCellStyle(style);
                celda++;

                cell = row.createCell(celda);
                cell.setCellValue(usuario);
                cell.setCellStyle(style);
                celda++;

                cell = row.createCell(celda);
                cell.setCellValue(actividades);
                cell.setCellStyle(style);
                celda++;

                cell = row.createCell(celda);
                cell.setCellValue(subactividades);
                cell.setCellStyle(style);
                celda++;

                cell = row.createCell(celda);
                cell.setCellValue(fecha1);
                cell.setCellStyle(style);
                celda++;

                cell = row.createCell(celda);
                cell.setCellValue(fecha2);
                cell.setCellStyle(style);
                celda++;

                cell = row.createCell(celda);
                cell.setCellValue(minutos);
                cell.setCellStyle(style);
                celda++;

                cell = row.createCell(celda);
                cell.setCellValue(comentario);
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

}
