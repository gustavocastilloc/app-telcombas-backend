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
public class ConfigActividad {

    private Long id;
    private String nombre;
    private Integer editmins;
    private Integer funmin;
    private Integer hassub;
    private Integer issub;
    private Integer mins;
    private String subact0;
    private String subact1;
    private String subact2;
    private String subact3;
    private Integer pageSize;
    private Integer pageIndex;
    private Integer estado;
    private String username;
    private Integer subactop;

    public Integer getSubactop() {
        return subactop;
    }

    public void setSubactop(Integer subactop) {
        this.subactop = subactop;
    }

    public String getSubact0() {
        return subact0;
    }

    public void setSubact0(String subact0) {
        this.subact0 = subact0;
    }

    public String getSubact1() {
        return subact1;
    }

    public void setSubact1(String subact1) {
        this.subact1 = subact1;
    }

    public String getSubact2() {
        return subact2;
    }

    public void setSubact2(String subact2) {
        this.subact2 = subact2;
    }

    public String getSubact3() {
        return subact3;
    }

    public void setSubact3(String subact3) {
        this.subact3 = subact3;
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

    public Integer getEditmins() {
        return editmins;
    }

    public void setEditmins(Integer editmins) {
        this.editmins = editmins;
    }

    public Integer getFunmin() {
        return funmin;
    }

    public void setFunmin(Integer funmin) {
        this.funmin = funmin;
    }

    public Integer getHassub() {
        return hassub;
    }

    public void setHassub(Integer hassub) {
        this.hassub = hassub;
    }

    public Integer getIssub() {
        return issub;
    }

    public void setIssub(Integer issub) {
        this.issub = issub;
    }

    public Integer getMins() {
        return mins;
    }

    public void setMins(Integer mins) {
        this.mins = mins;
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
