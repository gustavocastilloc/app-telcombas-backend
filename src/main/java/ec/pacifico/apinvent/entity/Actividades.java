/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.pacifico.apinvent.entity;

/**
 *
 * @author Carolina
 */
public class Actividades {
    private Long id;
    private String fecha;
    private String fecha2;
    private Long subactividad;
    private Long actividad;
    private String nameact;
    private String namesubact;
    private String comentario;
    private String usuario;
    private Integer mins;
    private String time0;
    private String time2;
    private Integer pageSize;
    private Integer pageIndex;
    private Integer estado;
    private String username;
    private Integer abierto;
    private Integer headab;
    private Integer headabbanco;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAbierto() {
        return abierto;
    }

    public void setAbierto(Integer abierto) {
        this.abierto = abierto;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Integer getHeadabbanco() {
        return headabbanco;
    }

    public void setHeadabbanco(Integer headabbanco) {
        this.headabbanco = headabbanco;
    }

    public Integer getHeadab() {
        return headab;
    }

    public void setHeadab(Integer headab) {
        this.headab = headab;
    }
    
    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getFecha2() {
        return fecha2;
    }

    public void setFecha2(String fecha2) {
        this.fecha2 = fecha2;
    }
    
    public String getNameact() {
        return nameact;
    }

    public void setNameact(String nameact) {
        this.nameact = nameact;
    }

    public Long getSubactividad() {
        return subactividad;
    }

    public void setSubactividad(Long subactividad) {
        this.subactividad = subactividad;
    }

    public Long getActividad() {
        return actividad;
    }

    public void setActividad(Long actividad) {
        this.actividad = actividad;
    }
    
    public String getNamesubact() {
        return namesubact;
    }

    public void setNamesubact(String namesubact) {
        this.namesubact = namesubact;
    }

    
    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Integer getMins() {
        return mins;
    }

    public void setMins(Integer mins) {
        this.mins = mins;
    }

    public String getTime0() {
        return time0;
    }

    public void setTime0(String time0) {
        this.time0 = time0;
    }

    public String getTime2() {
        return time2;
    }

    public void setTime2(String time2) {
        this.time2 = time2;
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
