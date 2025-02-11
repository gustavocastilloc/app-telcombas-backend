/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.pacifico.apinvent.facade;

import ec.pacifico.apinvent.entity.Problema;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author Carolina
 */
public class ProblemaFacade {
    private Conexion con;

    public ProblemaFacade(Conexion con) {
        this.con = con;
    }

    public JSONArray obtenerListado(Problema dato) {
        JSONArray lista = null;
        JSONParser parser = new JSONParser();
        String query
                = "SELECT *\n"
                + "FROM Problema\n"
                + "WHERE nombre LIKE '%" + dato.getNombre().trim() + "%' and estado = " + dato.getEstado() + "\n";
        if(dato.getLan()!=null){
            query = query 
                + "and lan = "+dato.getLan()+"\n";
        }
        query = query 
                + "ORDER BY CAST(nombre as Varchar(1000)) asc\n"
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

    public Integer obtenerListadoSize(Problema dato) {
        String query
                = "SELECT COUNT(id)\n"
                + "FROM Problema\n"
                + "WHERE nombre LIKE '%" + dato.getNombre().trim() + "%' and estado = " + dato.getEstado() + "\n";
        if(dato.getLan()!=null){
            query = query 
                + "and lan = "+dato.getLan()+"\n";
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

    public JSONArray busquedaId(Problema dato) {
        JSONArray lista = null;
        JSONParser parser = new JSONParser();
        String query
                = "SELECT *\n"
                + "FROM Problema\n"
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
            if(rsst.length()>0){
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

    public JSONArray busquedaNombre(Problema dato) {
        JSONArray lista = null;
        JSONParser parser = new JSONParser();
        String query
                = "SELECT id, nombre,lan\n"
                + "FROM Problema\n"
                + "WHERE nombre LIKE '%" + dato.getNombre().trim() + "%'  and estado=1\n"
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
            if(rsst.length()>0){
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

    public boolean actualizar(Problema dato) {
        String query
                = "UPDATE Problema\n"
                + "   SET nombre = '"+dato.getNombre()+"'\n"
                + "      ,estado = "+dato.getEstado()+"\n"
                + "      ,lan = "+dato.getLan()+"\n"
                + " WHERE id= "+dato.getId()+";";
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

    public boolean crear(Problema dato) {
        String query
                = "INSERT INTO Problema\n"
                + "           (id\n"
                + "            ,lan\n"
                + "           ,nombre)\n"
                + "     VALUES\n"
                + "           (NEXT VALUE FOR SeqProblema\n"
                + "            , "+dato.getLan()+"\n"
                + "           ,'" +dato.getNombre() + "')";
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
}
