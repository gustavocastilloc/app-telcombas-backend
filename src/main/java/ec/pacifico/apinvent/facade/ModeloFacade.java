/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.pacifico.apinvent.facade;

import ec.pacifico.apinvent.entity.Modelo;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author Carolina
 */
public class ModeloFacade {

    private Conexion con;

    public ModeloFacade(Conexion con) {
        this.con = con;
    }

    public JSONArray obtenerListado(Modelo dato) {
        JSONArray lista = null;
        JSONParser parser = new JSONParser();
        String query
                = "SELECT id, nombre,fechafin,estado,\n"
                + "(SELECT id,nombre\n"
                + "FROM Hardware \n"
                + "WHERE Hardware.id=Modelo.idmarca\n"
                + "FOR JSON AUTO) as Marca,\n"
                + "(SELECT id,nombre\n"
                + "FROM Hardware \n"
                + "WHERE Hardware.id=Modelo.idequipo\n"
                + "FOR JSON AUTO) as Equipo,\n"
                + "(SELECT id,nombre\n"
                + "FROM Hardware \n"
                + "WHERE Hardware.id=Modelo.idflash\n"
                + "FOR JSON AUTO) as Flash,\n"
                + "(SELECT id,nombre\n"
                + "FROM Hardware \n"
                + "WHERE Hardware.id=Modelo.idram\n"
                + "FOR JSON AUTO) as Ram\n"
                + "FROM Modelo\n"
                + "WHERE nombre LIKE '%" + dato.getNombre().trim() + "%' and estado=" + dato.getEstado() + "\n";
        if (dato.getIdEquipo() != null) {
            query = query
                    + " and idequipo=" + dato.getIdEquipo() + "\n";
        }
        if (dato.getIdMarca() != null) {
            query = query
                    + " and idmarca=" + dato.getIdMarca() + "\n";
        }
        if (dato.getIdFlash() != null) {
            query = query
                    + " and idflash=" + dato.getIdFlash() + "\n";
        }
        if (dato.getIdRam() != null) {
            query = query
                    + " and idram=" + dato.getIdRam() + "\n";
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

    public Integer obtenerListadoSize(Modelo dato) {
        String query
                = "SELECT COUNT(id)\n"
                + "FROM Modelo\n"
                + "WHERE nombre LIKE '%" + dato.getNombre().trim() + "%' and estado=" + dato.getEstado() + "\n";
        if (dato.getIdEquipo() != null) {
            query = query
                    + " and idequipo=" + dato.getIdEquipo() + "\n";
        }
        if (dato.getIdMarca() != null) {
            query = query
                    + " and idmarca=" + dato.getIdMarca() + "\n";
        }
        if (dato.getIdFlash() != null) {
            query = query
                    + " and idflash=" + dato.getIdFlash() + "\n";
        }
        if (dato.getIdRam() != null) {
            query = query
                    + " and idram=" + dato.getIdRam() + "\n";
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

    public JSONArray busquedaId(Modelo dato) {
        JSONArray lista = null;
        JSONParser parser = new JSONParser();
        String query
                = "SELECT id,nombre,fechafin,estado,\n"
                + "(SELECT id,nombre\n"
                + "FROM Hardware \n"
                + "WHERE Hardware.id=Modelo.idmarca\n"
                + "FOR JSON AUTO) as Marca,\n"
                + "(SELECT id,nombre\n"
                + "FROM Hardware \n"
                + "WHERE Hardware.id=Modelo.idequipo\n"
                + "FOR JSON AUTO) as Equipo,\n"
                + "(SELECT id,nombre\n"
                + "FROM Hardware \n"
                + "WHERE Hardware.id=Modelo.idflash\n"
                + "FOR JSON AUTO) as Flash,\n"
                + "(SELECT id,nombre\n"
                + "FROM Hardware \n"
                + "WHERE Hardware.id=Modelo.idram\n"
                + "FOR JSON AUTO) as Ram\n"
                + "FROM Modelo\n"
                + "WHERE id = " + dato.getId() + "\n"
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

    public JSONArray busquedaNombre(Modelo dato) {
        JSONArray lista = null;
        JSONParser parser = new JSONParser();
        String query
                = "SELECT id,nombre\n"
                + "FROM Modelo\n"
                + "WHERE nombre LIKE '%" + dato.getNombre().trim() + "%'  and estado=1\n"
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

    public boolean actualizar(Modelo dato) {
        String query
                = "UPDATE Modelo\n"
                + "   SET nombre = '" + dato.getNombre().trim() + "'\n";
        if (dato.getFecha() != null) {
            query = query
                    + "      ,fechafin = '" + dato.getFecha().trim() + "'\n";
        } else {
            query = query
                    + "      ,fechafin = " + dato.getFecha() + "\n";
        }
        query = query
                +"      ,idmarca = " + dato.getIdMarca() + "\n"
                + "      ,idequipo = " + dato.getIdEquipo() + "\n"
                + "      ,idflash = " + dato.getIdFlash() + "\n"
                + "      ,idram = " + dato.getIdRam() + "\n"
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

    public boolean crear(Modelo dato) {
        
        String query
                = "INSERT INTO Modelo\n"
                + "           (id\n"
                + "           ,nombre\n"
                + "           ,fechafin\n"
                + "           ,idequipo\n"
                + "           ,idflash\n"
                + "           ,idram\n"
                + "           ,idmarca)\n"
                + "     VALUES\n"
                + "           (NEXT VALUE FOR SeqModelo\n"
                + "           ,'" + dato.getNombre().trim() + "'\n";
        if (dato.getFecha() != null) {
            query = query
                    + "           ,'" + dato.getFecha().trim() + "'\n";
        } else {
            query = query
                    + "           ," + dato.getFecha() + "\n";
        }
        query = query
                + "           ," + dato.getIdEquipo() + "\n"
                + "           ," + dato.getIdFlash() + "\n"
                + "           ," + dato.getIdRam() + "\n"
                + "           ," + dato.getIdMarca() + ");";
        PreparedStatement st = null;
        ResultSet rs = null;
        //System.out.println(query);
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

    public boolean existeModelo(String nombre){
        /*validar upper case down case*/
        String query = "SELECT COUNT(*) FROM MODELO WHERE nombre like '" + nombre.trim()+"'";
        PreparedStatement st = null;
        ResultSet rs = null;
        try{
            st = con.getConnection().prepareStatement(query);
            rs = st.executeQuery();
            if (rs != null && rs.next()) {
                int count = rs.getInt(1); // Obtener el valor de la primera columna
                //System.out.println("Cantidad de modelos con el nombre '" + nombre + "': " + count); // Imprimir el resultado
                return count > 0; // Retornar true si hay registros
            }
            
        }catch(Exception e){
            System.out.println(this.getClass().toString()+".existeModelo"+e.getMessage());
        }finally{
            try{
                if(rs!=null){
                    rs.close();
                }
                if(st != null){
                    st.close();
                }
                    
            }catch (Exception e){
                System.out.println(this.getClass().toString()+".existeModelo"+e.getMessage());
            }
        }
        return false;
    }
    

}
