/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.pacifico.apinvent.facade;

import ec.pacifico.apinvent.entity.Tipo;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author Carolina
 */
public class TipoFacade {

    private Conexion con;

    public TipoFacade(Conexion con) {
        this.con = con;
    }

    public JSONArray obtenerListado(Tipo dato) {
        JSONArray lista = null;
        JSONParser parser = new JSONParser();
        String query
                = "SELECT *\n"
                + "FROM Tipo\n"
                + "WHERE nombre LIKE '%" + dato.getNombre().trim() + "%' \n";             
        if (dato.getEstado()!= null) {
            query = query
                + "and estado=" + dato.getEstado()+ "\n";
        }
        if (dato.getIdLink() != null) {
            query = query
                    + " and idciudad=" + dato.getIdLink() + "\n";
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

    public Integer obtenerListadoSize(Tipo dato) {
        String query
                = "SELECT COUNT(id)\n"
                + "FROM Tipo\n"
                + "WHERE nombre LIKE '%" + dato.getNombre().trim() + "%' \n";             
        if (dato.getEstado()!= null) {
            query = query
                + "and estado=" + dato.getEstado()+ "\n";
        }
        if (dato.getIdLink() != null) {
            query = query
                    + " and idciudad=" + dato.getIdLink() + "\n";
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

    public JSONArray busquedaId(Tipo dato) {
        JSONArray lista = null;
        JSONParser parser = new JSONParser();
        String query
                = "SELECT *\n"
                + "FROM Tipo\n"
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

    public JSONArray busquedaNombre(Tipo dato) {
        JSONArray lista = null;
        JSONParser parser = new JSONParser();
        String query
                = "SELECT id,nombre\n"
                + "FROM Tipo\n"
                + "WHERE nombre LIKE '%" + dato.getNombre().trim() + "%' and idciudad=" + dato.getIdLink() + " and estado=1\n"
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

    public JSONArray busquedaNombreExiste(Tipo dato,Integer state) {
        JSONArray lista = null;
        JSONParser parser = new JSONParser();
        String query
                = "SELECT id,nombre\n"
                + "FROM Tipo\n"
                + "WHERE nombre LIKE '" + dato.getNombre().trim() + "' and idciudad=" + dato.getIdLink() + " and estado="+state+"\n"
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

    public JSONArray busquedaCiudad(Tipo dato) {
        JSONArray lista = null;
        JSONParser parser = new JSONParser();
        String query
                = "SELECT Tipo.id,Tipo.nombre\n" +
                    "FROM Tipo\n" +
                    "FULL OUTER JOIN Ciudad ON (Ciudad.id=Tipo.idciudad)\n" +
                    "WHERE " ;
                    if (dato.getCiudades().contains(",")) {
                        query = query + "CAST(Ciudad.nombre as varchar) IN (";
                        String[] ldatos = dato.getCiudades().split(",");
                        query = query + "'" + ldatos[0].trim() + "'";
                        for (int l = 1; l < ldatos.length; l++) {
                            query = query + ",'" + ldatos[l].trim() + "'";
                        }
                        query = query + ")\n";
                    }
                query = query +
                    "and Tipo.estado=1\n" +
                    "ORDER BY CAST(Tipo.nombre as Varchar(1000)) asc\n"
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
            System.out.println(this.getClass().toString() + ".busquedaCiudad " + e.getMessage());
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
                System.out.println(this.getClass().toString() + ".busquedaCiudad " + e.getMessage());
            }
        }
    }

    public boolean actualizar(Tipo dato) {
        String query
                = "UPDATE Tipo\n"
                + "   SET nombre = '" + dato.getNombre().trim().toUpperCase() + "'\n"
                + "      ,estado = " + dato.getEstado() + "\n"
                + " WHERE id= " + dato.getId() + ";";
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

    public boolean crear(Tipo dato) {
        String query
                = "INSERT INTO Tipo\n"
                + "           (id\n"
                + "           ,nombre"
                + "           ,idciudad)\n"
                + "     VALUES\n"
                + "           (NEXT VALUE FOR SeqTipo\n"
                + "           ,'" + dato.getNombre().trim().toUpperCase() + "'\n"
                + "           ," + dato.getIdLink() + ")";
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
