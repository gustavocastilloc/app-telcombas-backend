/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.pacifico.apinvent.facade;

import ec.pacifico.apinvent.entity.Agencia;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author Carolina
 */
public class AgenciaFacade {

    private Conexion con;

    public AgenciaFacade(Conexion con) {
        this.con = con;
    }

    public Integer obtenerListadoAllSize(Agencia dato) {
        String query
                = "SELECT COUNT(Agencia.id)\n"
                + "FROM Agencia\n"
                + "FULL OUTER JOIN Tipo on(Tipo.id=Agencia.idtipo)\n"
                + "FULL OUTER JOIN Ciudad on(Ciudad.id=Tipo.idciudad)\n"
                + "WHERE Agencia.estado=" + dato.getEstado() + "\n";
        if (dato.getCiudad() != null) {
            //query = query
            //        + " and Ciudad.nombre LIKE '%" + dato.getNciudad() + "%'\n";
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
            //query = query
            //        + " and Tipo.nombre LIKE '%" + dato.getNtipo().trim() + "%'\n";
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
        if (dato.getNombre() != null) {
            //query = query
            //        + " and Agencia.nombre LIKE '%" + dato.getNagencia().trim() + "%'\n";
            if (dato.getNombre().contains(",")) {
                query = query + "and CAST(Agencia.nombre as Varchar) IN (";
                String[] lnagencia = dato.getNombre().split(",");
                query = query + "'" + lnagencia[0].trim() + "'";
                for (int l = 1; l < lnagencia.length; l++) {
                    query = query + ",'" + lnagencia[l].trim() + "'";
                }
                query = query + ")\n";
            } else {
                query = query
                        + " and Agencia.nombre LIKE '%" + dato.getNombre().trim() + "%'\n";
            }
        }
        
        if(dato.getCountenlaces()!=null){
            query = query
            +"and (SELECT Count(Enlace.id) FROM Enlace WHERE Enlace.idagencia = Agencia.id and Enlace.estado !=0 ) = "+dato.getCountenlaces()+"\n";
        }
        if(dato.getCountinv()!=null){
            query = query
            +"and (SELECT Count(Inventario.id) FROM Inventario WHERE Inventario.idagencia = Agencia.id and Inventario.estado=1) ="+dato.getCountinv()+"\n";
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

    public JSONArray obtenerListadoall(Agencia dato) {
        JSONArray lista = null;
        JSONParser parser = new JSONParser();
        String query
                = "SELECT Agencia.estado, Agencia.id,\n"
                + "(SELECT Count(Enlace.id) FROM Enlace WHERE Enlace.idagencia = Agencia.id and Enlace.estado !=0 ) as countenlaces,\n" 
                + "(SELECT Count(Inventario.id) FROM Inventario WHERE Inventario.idagencia = Agencia.id and Inventario.estado=1) as countinv,"
                + "Ciudad.id as idciudad,Ciudad.nombre as nombreciudad,\n"
                + "Tipo.id as idtipo, Tipo.nombre as nombretipo,\n"
                + "Agencia.nombre as nombreagencia\n"
                + "FROM Agencia\n"
                + "FULL OUTER JOIN Tipo on(Tipo.id=Agencia.idtipo)\n"
                + "FULL OUTER JOIN Ciudad on(Ciudad.id=Tipo.idciudad)\n"
                + "WHERE Agencia.estado=" + dato.getEstado() + "\n";
        if (dato.getCiudad() != null) {
            //query = query
            //        + " and Ciudad.nombre LIKE '%" + dato.getNciudad() + "%'\n";
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
            //query = query
            //        + " and Tipo.nombre LIKE '%" + dato.getNtipo().trim() + "%'\n";
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
        if (dato.getNombre() != null) {
            //query = query
            //        + " and Agencia.nombre LIKE '%" + dato.getNagencia().trim() + "%'\n";
            if (dato.getNombre().contains(",")) {
                query = query + "and CAST(Agencia.nombre as Varchar) IN (";
                String[] lnagencia = dato.getNombre().split(",");
                query = query + "'" + lnagencia[0].trim() + "'";
                for (int l = 1; l < lnagencia.length; l++) {
                    query = query + ",'" + lnagencia[l].trim() + "'";
                }
                query = query + ")\n";
            } else {
                query = query
                        + " and Agencia.nombre LIKE '%" + dato.getNombre().trim() + "%'\n";
            }
        }
        if(dato.getCountenlaces()!=null){
            query = query
            +"and (SELECT Count(Enlace.id) FROM Enlace WHERE Enlace.idagencia = Agencia.id and Enlace.estado !=0 ) = "+dato.getCountenlaces()+"\n";
        }
        if(dato.getCountinv()!=null){
            query = query
            +"and (SELECT Count(Inventario.id) FROM Inventario WHERE Inventario.idagencia = Agencia.id and Inventario.estado=1) ="+dato.getCountinv()+"\n";
        }

        query = query
                + "ORDER BY CAST(Ciudad.nombre as Varchar(1000)),CAST(Tipo.nombre as Varchar(1000)),CAST(Agencia.nombre as Varchar(1000)) asc \n"
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

    public JSONArray obtenerListadoTickets(Agencia dato) {
        JSONArray lista = null;
        JSONParser parser = new JSONParser();
        String query
                = "SELECT Agencia.estado, Agencia.id,\n"
                + "(SELECT Count(Enlace.id) FROM Enlace WHERE Enlace.idagencia = Agencia.id and Enlace.estado !=0 ) as countenlaces,\n" 
                + "Ciudad.nombre as nombreciudad,\n"
                + "Tipo.nombre as nombretipo,\n"
                + "Agencia.nombre as nombreagencia\n";

        if (dato.getIdproveedor() != null) {
            query = query + ",Enlace.id as idEnlace, Enlace.tunel \n";
        }
        query = query
                + "FROM Agencia\n"
                + "FULL OUTER JOIN Tipo on(Tipo.id=Agencia.idtipo)\n"
                + "FULL OUTER JOIN Ciudad on(Ciudad.id=Tipo.idciudad)\n";
        if (dato.getIdproveedor() != null) {
            query = query + "INNER JOIN Enlace on(Enlace.idproveedor="+dato.getIdproveedor()+" and Enlace.idagencia = Agencia.id) \n";
        }
        query = query
                + "WHERE Agencia.estado=1 and \n"
                + "(SELECT Count(Enlace.id) FROM Enlace WHERE Enlace.idagencia = Agencia.id and Enlace.estado !=0 ) >0 \n";
        if (dato.getIdproveedor() != null){
            query = query
                + "and Enlace.idpropiedad=1 \n";
        }
        if (dato.getCiudad() != null) {
            //query = query
            //        + " and Ciudad.nombre LIKE '%" + dato.getNciudad() + "%'\n";
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
            //query = query
            //        + " and Tipo.nombre LIKE '%" + dato.getNtipo().trim() + "%'\n";
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
        if (dato.getNombre() != null) {
            //query = query
            //        + " and Agencia.nombre LIKE '%" + dato.getNagencia().trim() + "%'\n";
            if (dato.getNombre().contains(",")) {
                query = query + "and CAST(Agencia.nombre as Varchar) IN (";
                String[] lnagencia = dato.getNombre().split(",");
                query = query + "'" + lnagencia[0].trim() + "'";
                for (int l = 1; l < lnagencia.length; l++) {
                    query = query + ",'" + lnagencia[l].trim() + "'";
                }
                query = query + ")\n";
            } else {
                query = query
                        + " and Agencia.nombre LIKE '%" + dato.getNombre().trim() + "%'\n";
            }
        }

        query = query
                + "ORDER BY CAST(Ciudad.nombre as Varchar(1000)), CAST(tipo.nombre as Varchar(1000)), CAST(Agencia.nombre as Varchar(1000)) asc \n"
                + "FOR JSON AUTO; \n";
        PreparedStatement st = null;
        ResultSet rs = null;
        System.out.println(query);
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

    public JSONArray busquedaId(Agencia dato) {
        JSONArray lista = null;
        JSONParser parser = new JSONParser();
        String query
                = "SELECT Agencia.estado, Agencia.id,Agencia.nombre,\n"
                + "Ciudad.id as idciudad,Ciudad.nombre as nombreciudad,\n"
                + "Tipo.id as idtipo, Tipo.nombre as nombretipo\n"
                + "FROM Agencia\n"
                + "FULL OUTER JOIN Tipo on(Tipo.id=Agencia.idtipo)\n"
                + "FULL OUTER JOIN Ciudad on(Ciudad.id=Tipo.idciudad)\n"
                + "WHERE Agencia.id = " + dato.getId() + "\n"
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

    public JSONArray busquedaNombre(Agencia dato) {
        JSONArray lista = null;
        JSONParser parser = new JSONParser();
        String query
                = "SELECT id, nombre\n"
                + "FROM Agencia\n"
                + "WHERE nombre LIKE '%" + dato.getNombre().trim() + "%' and idtipo=" + dato.getIdLink() + "  and estado=1\n"
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

    public JSONArray busquedaNombre2(Agencia dato) {
        JSONArray lista = null;
        JSONParser parser = new JSONParser();
        String query
                = "SELECT Agencia.id, Agencia.nombre\n"
                + "FROM Agencia\n"
                + "FULL OUTER JOIN Tipo on(Tipo.id=Agencia.idtipo)\n"
                + "WHERE (Agencia.nombre LIKE '%Matriz%' \n"
                + " or Agencia.nombre LIKE '%Datacenter%'\n"
                + " or Agencia.nombre LIKE '%Principal%')\n"
                + " and Agencia.estado=1\n"
                + " and Tipo.nombre LIKE '%CIN%'\n"
                + "ORDER BY CAST(Agencia.nombre as Varchar(1000)) asc\n"
                + "OFFSET 0 ROWS\n"
                + "FETCH FIRST 100 ROWS ONLY\n"
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

    public boolean actualizar(Agencia dato) {
        String query
                = "UPDATE Agencia\n"
                + "   SET nombre = '" + dato.getNombre() + "'\n"
                + "      ,estado = " + dato.getEstado() + "\n";
        if(dato.getIdLink()!=null){
            query = query 
                + "      ,idtipo = " + dato.getIdLink() + "\n";
        }
        query = query
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

    public boolean crear(Agencia dato) {
        String query
                = "INSERT INTO Agencia\n"
                + "           (id\n"
                + "           ,nombre"
                + "           ,idtipo)\n"
                + "     VALUES\n"
                + "           (NEXT VALUE FOR SeqAgencia\n"
                + "           ,'" + dato.getNombre() + "'\n"
                + "           ," + dato.getIdLink() + ");";
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

    public Boolean existenEnlaces(Agencia dato) {

        String query
                = "SELECT Count(Enlace.id) FROM Enlace WHERE Enlace.idagencia = "+dato.getId()+" and Enlace.estado !=0 \n;";
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            con.getConnection().setAutoCommit(false);
            st = con.getConnection().prepareStatement(query);
            rs = st.executeQuery();
            con.getConnection().commit();
            Integer conteo = 0;
            while (rs.next()) {
                conteo = rs.getInt(1);
            }
            if (conteo !=0){
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

    public Boolean existenEquipos(Agencia dato) {

        String query
                = "SELECT Count(Inventario.id) FROM Inventario WHERE Inventario.idagencia = "+dato.getId()+" and Inventario.estado=1 \n;";
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            con.getConnection().setAutoCommit(false);
            st = con.getConnection().prepareStatement(query);
            rs = st.executeQuery();
            con.getConnection().commit();
            Integer conteo = 0;
            while (rs.next()) {
                conteo = rs.getInt(1);
            }
            if (conteo !=0){
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

}
