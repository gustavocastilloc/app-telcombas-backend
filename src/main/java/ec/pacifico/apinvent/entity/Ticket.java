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
public class Ticket {

    private Long id;
    private Long idagencia;
    private Long idenlace;
    private Integer lan;
    private String tcompleto;
    private String tdias;
    private Long tmins;
    private Integer min;
    private Integer max;
    private String time0;
    private String time1;
    private String time2;
    private Integer pageSize;
    private Integer pageIndex;
    private Integer abierto; //CERRADO O ABIERTO
    private Integer estado; //ELIMINADO O NO
    private String username;
    private Long idheader;
    private String adicional;
    private String nagencia;
    private String ntipo;
    private String nciudad;
    private String ticket;
    private String proveedor;
    private String problema;
    private String tecnicorespon;
    private String tecnicoreporte;

    public Integer getMin() {
        return min;
    }

    public void setMin(Integer min) {
        this.min = min;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
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
    
    

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public String getProblema() {
        return problema;
    }

    public void setProblema(String problema) {
        this.problema = problema;
    }
    
    

    public String getNagencia() {
        return nagencia;
    }

    public void setNagencia(String nagencia) {
        this.nagencia = nagencia;
    }

    public String getNtipo() {
        return ntipo;
    }

    public void setNtipo(String ntipo) {
        this.ntipo = ntipo;
    }

    public String getNciudad() {
        return nciudad;
    }

    public void setNciudad(String nciudad) {
        this.nciudad = nciudad;
    }

    public String getAdicional() {
        return adicional;
    }

    public void setAdicional(String adicional) {
        this.adicional = adicional;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdagencia() {
        return idagencia;
    }

    public void setIdagencia(Long idagencia) {
        this.idagencia = idagencia;
    }

    public Long getIdenlace() {
        return idenlace;
    }

    public void setIdenlace(Long idenlace) {
        this.idenlace = idenlace;
    }

    public Long getIdheader() {
        return idheader;
    }

    public void setIdheader(Long idheader) {
        this.idheader = idheader;
    }

    public Integer getLan() {
        return lan;
    }

    public void setLan(Integer lan) {
        this.lan = lan;
    }

    public String getTcompleto() {
        return tcompleto;
    }

    public void setTcompleto(String tcompleto) {
        this.tcompleto = tcompleto;
    }

    public String getTdias() {
        return tdias;
    }

    public void setTdias(String tdias) {
        this.tdias = tdias;
    }

    public Long getTmins() {
        return tmins;
    }

    public void setTmins(Long tmins) {
        this.tmins = tmins;
    }

    public String getTime0() {
        return time0;
    }

    public void setTime0(String time0) {
        this.time0 = time0;
    }

    public String getTime1() {
        return time1;
    }

    public void setTime1(String time1) {
        this.time1 = time1;
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

    public Integer getAbierto() {
        return abierto;
    }

    public void setAbierto(Integer abierto) {
        this.abierto = abierto;
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

    @Override
    public String toString() {
        return "Ticket{" + "id=" + id + ", idagencia=" + idagencia + ", idenlace=" + idenlace + ", lan=" + lan + ", tcompleto=" + tcompleto + ", tdias=" + tdias + ", tmins=" + tmins + ", time0=" + time0 + ", time1=" + time1 + ", time2=" + time2 + ", pageSize=" + pageSize + ", pageIndex=" + pageIndex + ", abierto=" + abierto + ", estado=" + estado + ", username=" + username + ", idheader=" + idheader + '}';
    }

}
