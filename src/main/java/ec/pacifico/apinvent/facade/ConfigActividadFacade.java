/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.pacifico.apinvent.facade;

import ec.pacifico.apinvent.entity.ConfigActividad;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author Carolina
 */
public class ConfigActividadFacade {

    private Conexion con;

    public ConfigActividadFacade(Conexion con) {
        this.con = con;
    }

    public JSONArray obtenerListado(ConfigActividad dato) {
        JSONArray lista = null;
        JSONParser parser = new JSONParser();
        String query
                = "SELECT *\n"
                + "FROM ConfigActividad\n"
                + "WHERE nombre LIKE '%" + dato.getNombre().trim() + "%' and estado = " + dato.getEstado() + "\n"
                + "ORDER BY issub asc, CAST(nombre as Varchar(1000)) asc\n"
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

    public Integer obtenerListadoSize(ConfigActividad dato) {
        String query
                = "SELECT COUNT(id)\n"
                + "FROM ConfigActividad\n"
                + "WHERE nombre LIKE '%" + dato.getNombre().trim() + "%' and estado = " + dato.getEstado() + ";";
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

    public String obtenerOpciones(ConfigActividad dato) {
        String query
                = "SELECT ";
        if (dato.getSubactop() == 0) {
            query = query
                    + "subact0 \n";
        } else if (dato.getSubactop() == 1) {
            query = query
                    + "subact1\n";
        } else if (dato.getSubactop() == 2) {
            query = query
                    + "subact2\n";
        }
        if (dato.getSubactop() == 3) {
            query = query
                    + "subact3\n";
        }
        query = query
                + "FROM ConfigActividad\n"
                + "WHERE id = " + dato.getId() + " and estado = 1;";
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = con.getConnection().prepareStatement(query);
            rs = st.executeQuery();
            String op = "";
            while (rs.next()) {
                op = rs.getString(1);
            }
            return op;
        } catch (Exception e) {
            System.out.println(this.getClass().toString() + ".obtenerOpciones " + e.getMessage());
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
                System.out.println(this.getClass().toString() + ".obtenerOpciones " + e.getMessage());
            }
        }
    }

    public JSONArray busquedaId(ConfigActividad dato) {
        JSONArray lista = null;
        JSONParser parser = new JSONParser();
        String query
                = "SELECT *\n"
                + "FROM ConfigActividad\n"
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

    public JSONArray busquedaNombre(ConfigActividad dato, Integer issub) {
        JSONArray lista = null;
        JSONParser parser = new JSONParser();
        String query
                = "SELECT *\n"
                + "FROM ConfigActividad \n"
                + "WHERE nombre LIKE '%" + dato.getNombre().trim() + "%'  and estado=1 and issub=" + issub + "\n"
                + "ORDER BY CAST(nombre as Varchar(1000)) asc\n"
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

    public boolean actualizar(ConfigActividad dato) {
        String query
                = "UPDATE ConfigActividad\n"
                + "   SET nombre = '" + dato.getNombre() + "'\n"
                + "      ,mins =      " + dato.getMins() + "\n"
                + "      ,editmins =  " + dato.getEditmins() + "\n"
                + "      ,hassub =    " + dato.getHassub() + "\n"
                + "      ,issub =     " + dato.getIssub() + "\n"
                + "      ,estado =    " + dato.getEstado() + "\n";
        if (dato.getSubact0() != null && dato.getSubact0().trim().length() > 0) {
            query = query
                    + "      ,subact0 =   '" + dato.getSubact0() + "'\n";
        } else {
            query = query
                    + "      ,subact0 =   NULL\n";
        }
        if (dato.getSubact1() != null && dato.getSubact1().trim().length() > 0) {
            query = query
                    + "      ,subact1 =   '" + dato.getSubact1() + "'\n";
        } else {
            query = query
                    + "      ,subact1 =   NULL\n";
        }
        if (dato.getSubact2() != null && dato.getSubact2().trim().length() > 0) {
            query = query
                    + "      ,subact2 =   '" + dato.getSubact2() + "'\n";
        } else {
            query = query
                    + "      ,subact2 =   NULL\n";
        }
        if (dato.getSubact3() != null && dato.getSubact3().trim().length() > 0) {
            query = query
                    + "      ,subact3 =   '" + dato.getSubact3() + "'\n";
        } else {
            query = query
                    + "      ,subact3 =   NULL\n";
        }
        query = query
                + " WHERE id= " + dato.getId() + ";\n";
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

    public boolean crear(ConfigActividad dato) {
        String query
                = "INSERT INTO ConfigActividad\n"
                + "           (id\n"
                + "           ,nombre\n"
                + "           ,editmins\n"
                + "           ,hassub\n"
                + "           ,issub\n";
        if (dato.getSubact0() != null && dato.getSubact0().trim().length() > 0) {
            query = query
                    + "           ,subact0";
        }
        if (dato.getSubact1() != null && dato.getSubact1().trim().length() > 0) {
            query = query
                    + "           ,subact1";
        }
        if (dato.getSubact2() != null && dato.getSubact2().trim().length() > 0) {
            query = query
                    + "           ,subact2";
        }
        if (dato.getSubact3() != null && dato.getSubact3().trim().length() > 0) {
            query = query
                    + "           ,subact3";
        }
        query = query
                + "           ,mins)\n"
                + "            OUTPUT Inserted.id \n"
                + "     VALUES\n"
                + "           (NEXT VALUE FOR SeqConfigActividad\n"
                + "           ,'" + dato.getNombre() + "'\n"
                + "           ," + dato.getEditmins() + "\n"
                + "           ," + dato.getHassub() + "\n"
                + "           ," + dato.getIssub() + "\n";
        if (dato.getSubact0() != null && dato.getSubact0().trim().length() > 0) {
            query = query
                    + "           ,'" + dato.getSubact0().trim() + "'\n";
        }
        if (dato.getSubact1() != null && dato.getSubact1().trim().length() > 0) {
            query = query
                    + "           ,'" + dato.getSubact1().trim() + "'\n";
        }
        if (dato.getSubact2() != null && dato.getSubact2().trim().length() > 0) {
            query = query
                    + "           ,'" + dato.getSubact2().trim() + "'\n";
        }
        if (dato.getSubact3() != null && dato.getSubact3().trim().length() > 0) {
            query = query
                    + "           ,'" + dato.getSubact3().trim() + "'\n";
        }
        query = query
                + "           ," + dato.getMins() + ");\n";
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            con.getConnection().setAutoCommit(false);
            st = con.getConnection().prepareStatement(query);
            st.executeQuery();
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

}
