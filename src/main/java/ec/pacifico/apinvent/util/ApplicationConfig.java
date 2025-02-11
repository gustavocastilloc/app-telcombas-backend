/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.pacifico.apinvent.util;

import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 *
 * @author Carolina
 */
@ApplicationPath("")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method. It is automatically
     * populated with all resources defined in the project. If required, comment
     * out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(ec.pacifico.apinvent.util.Filter.class);
        resources.add(ec.pacifico.apinvent.ws.ActividadesWS.class);
        resources.add(ec.pacifico.apinvent.ws.AgenciaWS.class);
        resources.add(ec.pacifico.apinvent.ws.AmbienteWS.class);
        resources.add(ec.pacifico.apinvent.ws.CiudadWS.class);
        resources.add(ec.pacifico.apinvent.ws.ConfigActividadWS.class);
        resources.add(ec.pacifico.apinvent.ws.EnlaceWS.class);
        resources.add(ec.pacifico.apinvent.ws.EntidadesWS.class);
        resources.add(ec.pacifico.apinvent.ws.HardwareWS.class);
        resources.add(ec.pacifico.apinvent.ws.InventarioWS.class);
        resources.add(ec.pacifico.apinvent.ws.ModeloWS.class);
        resources.add(ec.pacifico.apinvent.ws.OrionWS.class);
        resources.add(ec.pacifico.apinvent.ws.PerfilWS.class);
        resources.add(ec.pacifico.apinvent.ws.ProblemaWS.class);
        resources.add(ec.pacifico.apinvent.ws.PropietarioWS.class);
        resources.add(ec.pacifico.apinvent.ws.TemperaturaWS.class);
        resources.add(ec.pacifico.apinvent.ws.TicketWS.class);
        resources.add(ec.pacifico.apinvent.ws.TipoWS.class);
        resources.add(ec.pacifico.apinvent.ws.UsuarioWS.class);
        resources.add(org.glassfish.jersey.server.wadl.internal.WadlResource.class);


    }
}
