/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.pacifico.apinvent.facade;

import ec.pacifico.apinvent.entity.Temperatura;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import java.time.LocalDateTime;

/**
 *
 * @author Carolina
 */
public class TemperaturaFacade {

    private Conexion con;

    public TemperaturaFacade(Conexion con) {
        this.con = con;
    }

    public boolean crear(Temperatura dato) {

        String query = "";
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            for (Temperatura tt : dato.getArrtemp()) {

                LocalDateTime dtime = LocalDateTime.parse((CharSequence) tt.getTime0().replace(" ", "T"));
                Integer year0 = dtime.getYear();
                Integer month0 = dtime.getMonth().getValue();
                Integer day0 = dtime.getDayOfMonth();
                Integer hour0 = dtime.getHour();
                Integer min0 = dtime.getMinute();
                Double halfhour0 = hour0.doubleValue();

                if (min0 <= 14) {
                    halfhour0 = hour0.doubleValue();
                } else if (min0 > 14 && min0 <= 29) {
                    halfhour0 = hour0 + 0.5;
                } else if (min0 > 14 && min0 <= 29) {
                    halfhour0 = hour0 + 0.5;
                } else if (min0 > 14 && min0 <= 29) {
                    halfhour0 = hour0 + 1.0;
                }
                query = query + "INSERT INTO [dbo].[Temperatura]\n"
                        + "           (id\n"
                        + "           ,serie\n"
                        + "           ,hostname\n"
                        + "           ,temperatura\n"
                        + "           ,rack\n"
                        + "           ,time0\n"
                        + "           ,year0\n"
                        + "           ,month0\n"
                        + "           ,day0\n"
                        + "           ,hour0\n"
                        + "           ,halfhour0)\n"
                        + "     VALUES\n"
                        + "           (NEXT VALUE FOR SeqTemperatura\n"
                        + "           ,'" + tt.getSerie() + "'\n"
                        + "           ,'" + tt.getHostname() + "'\n"
                        + "           ,'" + tt.getTemperatura() + "'\n"
                        + "           ," + tt.getRack() + "\n"
                        + "           ,'" + tt.getTime0() + "'\n"
                        + "           ,'" + year0 + "'\n"
                        + "           ,'" + month0 + "'\n"
                        + "           ,'" + day0 + "'\n"
                        + "           ,'" + hour0 + "'\n"
                        + "           ,'" + halfhour0 + "');\n";
            }
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

    public JSONArray obtenerListadofiltro(Temperatura dato) {
        JSONArray lista = null;
        JSONParser parser = new JSONParser();
        String query
                = "SELECT " + dato.getFiltroymd().trim() + ", AVG(CAST(temperatura AS DECIMAL(10,3))) as temperatura\n"
                + "FROM temperatura\n"
                + "WHERE time0 between '" + dato.getTime0().trim() + "' and '" + dato.getTime1().trim() + "'\n"
                + "and rack = " + dato.getRack() + "\n"
                + "GROUP BY " + dato.getFiltroymd().trim() + "\n"
                + "ORDER BY " + dato.getCastymd() + "\n"
                + "FOR JSON AUTO; \n";
        System.out.println(query);
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
            System.out.println(this.getClass().toString() + ".obtenerListadofiltro " + e.getMessage());
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
                System.out.println(this.getClass().toString() + ".obtenerListadofiltro " + e.getMessage());
            }
        }
    }

    public JSONArray obtenerListadofiltrobyequipo(Temperatura dato) {
        JSONArray lista = null;
        JSONParser parser = new JSONParser();
        String query
                = "SELECT " + dato.getFiltroymd().trim() + ", AVG(CAST(temperatura AS DECIMAL(10,3))) as temperatura\n"
                + "FROM temperatura\n"
                + "WHERE time0 between '" + dato.getTime0().trim() + "' and '" + dato.getTime1().trim() + "'\n"
                + "and rack = " + dato.getRack() + " and serie like'" + dato.getSerie()+ "' and hostname like '" + dato.getHostname()+ "'\n"
                + "GROUP BY " + dato.getFiltroymd().trim() + "\n"
                + "ORDER BY " + dato.getCastymd() + "\n"
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
            System.out.println(this.getClass().toString() + ".obtenerListadofiltrobyequipo " + e.getMessage());
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
                System.out.println(this.getClass().toString() + ".obtenerListadofiltrobyequipo " + e.getMessage());
            }
        }
    }

    public JSONArray obtenerEquipos(Temperatura dato) {
        JSONArray lista = null;
        JSONParser parser = new JSONParser();
        String query
                = "SELECT CAST(serie AS VARCHAR(MAX)) as serie, CAST(hostname AS VARCHAR(MAX)) as hostname\n"
                + "FROM temperatura\n"
                + "WHERE time0 between '" + dato.getTime0().trim() + "' and '" + dato.getTime1().trim() + "'\n"
                + "and rack = " + dato.getRack() + "\n"
                + "GROUP BY CAST(serie AS VARCHAR(MAX)), CAST(hostname AS VARCHAR(MAX))\n"
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
            System.out.println(this.getClass().toString() + ".obtenerEquipos " + e.getMessage());
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
                System.out.println(this.getClass().toString() + ".obtenerEquipos " + e.getMessage());
            }
        }
    }

}
