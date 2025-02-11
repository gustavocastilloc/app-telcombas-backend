/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.pacifico.apinvent.entity;

import java.util.List;

/**
 *
 * @author Carolina
 */
public class TTHeader {

    private Long id;
    private Integer conteo;
    private String fecha;
    private Long idproblema;
    private Long idproveedor;
    private String soporte;
    private String ttproveedor;
    private String descripcion;
    private String tecnicorespon;
    private String tecnicoreporte;
    private Integer pageSize;
    private Integer pageIndex;
    private Integer estado; //ELIMINADO O NO
    private String username;
    private Integer tthabierto;
    private List<ActHeader> actividades;
    private List<Ticket> tickets;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTthabierto() {
        return tthabierto;
    }

    public void setTthabierto(Integer tthabierto) {
        this.tthabierto = tthabierto;
    }

    
    public Long getIdproblema() {
        return idproblema;
    }

    public void setIdproblema(Long idproblema) {
        this.idproblema = idproblema;
    }

    public List<ActHeader> getActividades() {
        return actividades;
    }

    public void setActividades(List<ActHeader> actividades) {
        this.actividades = actividades;
    }

    public Long getIdproveedor() {
        return idproveedor;
    }

    public void setIdproveedor(Long idproveedor) {
        this.idproveedor = idproveedor;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public String getTtproveedor() {
        return ttproveedor;
    }

    public void setTtproveedor(String ttproveedor) {
        this.ttproveedor = ttproveedor;
    }

    public Integer getConteo() {
        return conteo;
    }

    public void setConteo(Integer conteo) {
        this.conteo = conteo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }


    public String getSoporte() {
        return soporte;
    }

    public void setSoporte(String soporte) {
        this.soporte = soporte;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTecnicorespon() {
        return tecnicorespon;
    }

    public void setTecnicorespon(String tecnicorespon) {
        this.tecnicorespon = tecnicorespon;
    }

    public String getTecnicoreporte() {
        return tecnicoreporte;
    }

    public void setTecnicoreporte(String tecnicoreporte) {
        this.tecnicoreporte = tecnicoreporte;
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
