/*
 * Contiene informacion de cada Actividad plantilla, esta clase
 * tiene como objetivo dar una estructura para ingresar un tipo de actividad especifica o subactividad,
 * a partir de aqui puedo crear multiples actividades.
 */
package ec.pacifico.apinvent.entity;

import java.util.List;

/**
 *
 * @author Carolina
 */
public class ActHeader {
    //ATRIBUTOS
    /**
      * Identificador único del actheader
      */
    private Long id;
    /**
      * campo de actividad
      */
    private Long actividad;
    private Integer subactop;
    private String nameact;
    private String comentario;
    private Integer pageSize;
    private Integer pageIndex;
    private Integer estado;
    private Integer check;
    private Integer abbanco;
    private String username;
    private List<Actividades> subactividades;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAbbanco() {
        return abbanco;
    }

    public void setAbbanco(Integer abbanco) {
        this.abbanco = abbanco;
    }

    public Integer getCheck() {
        return check;
    }

    public void setCheck(Integer check) {
        this.check = check;
    }

    public Integer getSubactop() {
        return subactop;
    }

    public void setSubactop(Integer subactop) {
        this.subactop = subactop;
    }

    
    public List<Actividades> getSubactividades() {
        return subactividades;
    }

    public void setSubactividades(List<Actividades> subactividades) {
        this.subactividades = subactividades;
    }
    
    

    public Long getActividad() {
        return actividad;
    }

    public void setActividad(Long actividad) {
        this.actividad = actividad;
    }

    public String getNameact() {
        return nameact;
    }

    public void setNameact(String nameact) {
        this.nameact = nameact;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
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

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    
}
