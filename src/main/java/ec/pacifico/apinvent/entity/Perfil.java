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
public class Perfil {
    
    private Long id;
    private String nombre;
    private Integer informacion;
    private Integer enlaces;
    private Integer eliminar;
    private Integer administrar;
    private Integer estado;
    private Integer pageSize;
    private Integer pageIndex;
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getInformacion() {
        return informacion;
    }

    public void setInformacion(Integer informacion) {
        this.informacion = informacion;
    }

    public Integer getEnlaces() {
        return enlaces;
    }

    public void setEnlaces(Integer enlaces) {
        this.enlaces = enlaces;
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

    
    public Integer getEliminar() {
        return eliminar;
    }

    public void setEliminar(Integer eliminar) {
        this.eliminar = eliminar;
    }

    public Integer getAdministrar() {
        return administrar;
    }

    public void setAdministrar(Integer administrar) {
        this.administrar = administrar;
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

    
}
