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
public class Inventario {

    private Long id;
    private String nombre;
    private String ip;
    private String so;
    private String serie; // una serie
    private String inventario;
    private Integer rack;
    private Integer piso;
    private String ubicacion;
    private Integer critico; 
    private Integer opmger; 
    private Integer bpac; 
    private Long idAmbiente; 
    private String nAmbiente; 
    private Long idModelo; 
    private String nModelo;  
    private String marca;
    private String nEquipo;
    private Long idPropietario; 
    private String nPropietario; 
    private Long idOrion; 
    private String norion;  
    private Long agencia;
    private String nagencia;
    private Long tipo; 
    private String ntipo; 
    private Long ciudad; 
    private String nciudad; 
    private Integer util; 
    private String username; 
    private String fecha;
    private String fechaini;
    private String fechafin;
    private String fechabanco;
    private Integer estado; 
    private Integer pageSize;
    private Integer pageIndex;

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

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getSo() {
        return so;
    }

    public void setSo(String so) {
        this.so = so;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getInventario() {
        return inventario;
    }

    public void setInventario(String inventario) {
        this.inventario = inventario;
    }

    public Integer getRack() {
        return rack;
    }

    public void setRack(Integer rack) {
        this.rack = rack;
    }

    public Integer getPiso() {
        return piso;
    }

    public void setPiso(Integer piso) {
        this.piso = piso;
    }

    public Integer getCritico() {
        return critico;
    }

    public void setCritico(Integer critico) {
        this.critico = critico;
    }

    public Integer getOpmger() {
        return opmger;
    }

    public void setOpmger(Integer opmger) {
        this.opmger = opmger;
    }

    public Integer getBpac() {
        return bpac;
    }

    public void setBpac(Integer bpac) {
        this.bpac = bpac;
    }

    public Long getIdAmbiente() {
        return idAmbiente;
    }

    public void setIdAmbiente(Long idAmbiente) {
        this.idAmbiente = idAmbiente;
    }

    public Long getIdModelo() {
        return idModelo;
    }

    public void setIdModelo(Long idModelo) {
        this.idModelo = idModelo;
    }

    public String getnEquipo() {
        return nEquipo;
    }

    public void setnEquipo(String nEquipo) {
        this.nEquipo = nEquipo;
    }

    public Long getIdPropietario() {
        return idPropietario;
    }

    public void setIdPropietario(Long idPropietario) {
        this.idPropietario = idPropietario;
    }

    public Long getIdOrion() {
        return idOrion;
    }

    public void setIdOrion(Long idOrion) {
        this.idOrion = idOrion;
    }

    public String getNorion() {
        return norion;
    }

    public void setNorion(String norion) {
        this.norion = norion;
    }

    public Long getAgencia() {
        return agencia;
    }

    public void setAgencia(Long agencia) {
        this.agencia = agencia;
    }

    public Long getTipo() {
        return tipo;
    }

    public void setTipo(Long tipo) {
        this.tipo = tipo;
    }

    public Long getCiudad() {
        return ciudad;
    }

    public void setCiudad(Long ciudad) {
        this.ciudad = ciudad;
    }

    public Integer getUtil() {
        return util;
    }

    public void setUtil(Integer util) {
        this.util = util;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getFechabanco() {
        return fechabanco;
    }

    public void setFechabanco(String fechabanco) {
        this.fechabanco = fechabanco;
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

    
    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getnAmbiente() {
        return nAmbiente;
    }

    public void setnAmbiente(String nAmbiente) {
        this.nAmbiente = nAmbiente;
    }

    public String getnModelo() {
        return nModelo;
    }

    public void setnModelo(String nModelo) {
        this.nModelo = nModelo;
    }

    public String getnPropietario() {
        return nPropietario;
    }

    public void setnPropietario(String nPropietario) {
        this.nPropietario = nPropietario;
    }

    public String getnorion() {
        return norion;
    }

    public void setnorion(String norion) {
        this.norion = norion;
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

    public String getFechaini() {
        return fechaini;
    }

    public void setFechaini(String fechaini) {
        this.fechaini = fechaini;
    }

    public String getFechafin() {
        return fechafin;
    }

    public void setFechafin(String fechafin) {
        this.fechafin = fechafin;
    }
    
    
    

}
