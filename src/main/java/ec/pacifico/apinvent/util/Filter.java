/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.pacifico.apinvent.util;

import java.io.IOException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author Carolina
 */
@Provider
public class Filter implements ContainerResponseFilter, ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext crc, ContainerResponseContext crc2) throws IOException {
        MultivaluedMap<String, Object> headers = crc2.getHeaders();
        headers.add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        headers.add("Access-Control-Max-Age", "1209600");
        headers.add("Access-Control-Allow-Headers", "Origin, Access-Control-Allow-Origin,Content-Type, Accept, Authorization");
        headers.add("Access-Control-Expose-Headers", "totalpaginas, totalresultados");
        headers.add("Access-Control-Allow-Origin", "*");
        
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
    }

}
