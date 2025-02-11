/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.pacifico.apinvent.facade;

import ec.pacifico.apinvent.entity.Usuario;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author Carolina
 */
public class UsuarioFacade {

    private Conexion con;

    public UsuarioFacade(Conexion con) {
        this.con = con;
    }

    public JSONArray obtenerListado(Usuario dato) {
        JSONArray lista = null;
        JSONParser parser = new JSONParser();
        String query
                = "SELECT Usuario.id, Usuario.usuario, Usuario.estado, Usuario.perfil, Usuario.nombre, \n"
                + "Perfil.nombre as nperfil\n"
                + "FROM Usuario\n"
                + "FULL OUTER JOIN Perfil on(Usuario.perfil=Perfil.id)\n"
                + "WHERE Usuario.usuario LIKE '%" + dato.getUsuario().trim() + "%'\n";

        if (dato.getEstado() != null) {
            query = query
                    + " and Usuario.estado=" + dato.getEstado() + "\n";
        }

        query = query
                + "ORDER BY CAST(Usuario.usuario as Varchar(1000)) asc\n"
                //+ "OFFSET ? ROWS\n"
                //+ "FETCH NEXT ? ROWS ONLY\n"
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

    public Integer obtenerListadoSize(Usuario dato) {
        String query
                = "SELECT COUNT(id)\n"
                + "FROM Usuario\n"
                + "WHERE usuario LIKE '%" + dato.getUsuario().trim() + "%'\n";

        if (dato.getEstado() != null) {
            query = query
                    + " and estado=" + dato.getEstado() + "\n";
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

    public JSONArray busquedaId(Usuario dato) {
        JSONArray lista = null;
        JSONParser parser = new JSONParser();
        String query
                = "SELECT *\n"
                + "FROM Usuario\n"
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

    public JSONArray busquedaNombre(Usuario dato) {
        JSONArray lista = null;
        JSONParser parser = new JSONParser();
        String query
                = "SELECT id,usuario,nombre\n"
                + "FROM Usuario\n"
                + "WHERE nombre LIKE '%" + dato.getNombre().trim() + "%'  and estado=1\n"
                + "ORDER BY CAST(usuario as Varchar(1000)) asc\n"
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

    public JSONArray busquedaPermisos(String usuario) {
        JSONArray lista = null;
        JSONParser parser = new JSONParser();
        String query
                = "SELECT Perfil.informacion, Perfil.enlaces, Perfil.eliminar, Perfil.administrar, Perfil.estado\n"
                + "FROM USUARIO INNER JOIN Perfil \n"
                + "ON Usuario.perfil=Perfil.id \n"
                + "WHERE Usuario.usuario LIKE '" + usuario.trim() + "'  and Usuario.estado=1\n"
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
            System.out.println(this.getClass().toString() + ".busquedaPermisos " + e.getMessage());
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
                System.out.println(this.getClass().toString() + ".busquedaPermisos " + e.getMessage());
            }
        }
    }

    public JSONArray busquedaLogin(Usuario dato) {
        JSONArray lista = null;
        JSONParser parser = new JSONParser();
        String query
                = "SELECT id,usuario,estado,perfil\n"
                + "FROM Usuario\n"
                + "WHERE usuario LIKE '" + dato.getUsuario().trim() + "' and estado=1\n"
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
                boolean auth = authUser(dato.getUsuario().trim(),dato.getPassword().trim());
                lista.add(auth);
            }

            return lista;
        } catch (Exception e) {
            System.out.println(this.getClass().toString() + ".busquedaLogin " + e.getMessage());
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
                System.out.println(this.getClass().toString() + ".busquedaLogin " + e.getMessage());
            }
        }
    }

    public static boolean authUser(String username, String password)
	{
		try {
			Properties env = new Properties();
			env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
			env.put(Context.PROVIDER_URL, "ldap://10.1.202.7:389");
			env.put(Context.SECURITY_PRINCIPAL, ""+username+"@pacifico");  //check the DN correctly
			env.put(Context.SECURITY_CREDENTIALS, password);
			DirContext con = new InitialDirContext(env);
			con.close();
			return true;
		}catch (Exception e) {
			System.out.println("failed: "+e.getMessage());
			return false;
		}
	}

    public boolean actualizar(Usuario dato) {
        String query
                = "UPDATE Usuario\n"
                + "   SET usuario = '" + dato.getUsuario().trim() + "'\n";
        /*if (dato.getPassword() != null || dato.getPassword().length() > 0) {
            query = query
                    + "     ,HASHBYTES('md5','" + dato.getPassword() + "')\n";

        }*/
        if (dato.getNombre()!= null) {
            query = query
                    + "    ,nombre = '" + dato.getNombre() + "'\n";
        }
        if (dato.getEstado() != null) {
            query = query
                    + "    ,estado = " + dato.getEstado() + "\n";
        }
        query = query
                + "      ,perfil = " + dato.getPerfil() + "\n"
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

    public boolean crear(Usuario dato) {
        String query
                = "INSERT INTO Usuario\n"
                + "           (id\n"
                + "           ,usuario"
                + "           ,nombre"
                + "           ,perfil)\n"
                + "     VALUES\n"
                + "           (NEXT VALUE FOR SeqUsuario\n"
                + "           ,'" + dato.getUsuario() + "'\n"
                + "           ,'" + dato.getNombre()+ "'\n"
                + "           ," + dato.getPerfil() + ")\n;";
        //+ "           ,HASHBYTES('md5','" + dato.getPassword() + "'));";
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
