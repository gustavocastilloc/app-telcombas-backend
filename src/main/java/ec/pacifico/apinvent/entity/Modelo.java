/*
 * Contiene informacion de cada modelo de equipo que se puede ingresar, esta clase
 * tiene como objetivo crear varios modelos acorde segun los requerimientos. ,
 * a partir de aqui puedo crear multiples modelos.
 */
package ec.pacifico.apinvent.entity;

import java.util.Date;

/**
 *
 * @author Carolina
 */
public class Modelo {
    /*Identificador unico del modelo, identificador para la base de datos*/
    private Long id;
    /*Nombre del modelo, se debe validar que sea único para modelo*/
    private String nombre;
    /*El estado es binario, donde 1 es activo y 0 es eliminado*/
    private Integer estado;
    private Integer pageSize;
    private Integer pageIndex;
    /*Identificador de la marca del modelo, es un numero*/
    private Long idMarca;
    /*Identificador unico del equipo, es un numero*/
    private Long idEquipo;
    /*Identificador unico de la memoria flash, es un numero*/
    private Long idFlash;
    /*Identificador unico de la memoria ram, es un numero*/
    private Long idRam;
    /*Fecha del modelo, tipo fecha*/
    private String fecha;
    /*usuario que esta interacturando con el modelo*/
    private String username;

    /*Metodo get para obtener el nombre de usuario desde otras clases*/
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Long getIdMarca() {
        return idMarca;
    }

    public void setIdMarca(Long idMarca) {
        this.idMarca = idMarca;
    }

    public Long getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(Long idEquipo) {
        this.idEquipo = idEquipo;
    }

    public Long getIdFlash() {
        return idFlash;
    }

    public void setIdFlash(Long idFlash) {
        this.idFlash = idFlash;
    }

    public Long getIdRam() {
        return idRam;
    }

    public void setIdRam(Long idRam) {
        this.idRam = idRam;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
    
    
}
