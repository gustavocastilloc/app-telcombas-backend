/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.pacifico.apinvent.entity;

import java.math.BigDecimal;

import java.util.List;
/**
 *
 * @author Carolina
 */
public class Temperatura {

    private Long id;
    private String serie;
    private String hostname;
    private String year0;
    private String month0;
    private String day0;
    private String hour0;
    private String halfhour0;
    private String filtro0;
    private String filtroymd;
    private String castymd;
    private String time1;
    private String time0;
    private BigDecimal temperatura;
    private Integer rack;
    private Integer estado;
    private Integer pageSize;
    private Integer pageIndex;
    private String username;
    private List<Temperatura> arrtemp;


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

    public String getYear0() {
        return year0;
    }

    public void setYear0(String year0) {
        this.year0 = year0;
    }

    public List<Temperatura> getArrtemp() {
        return arrtemp;
    }

    public void setArrtemp(List<Temperatura> arrtemp) {
        this.arrtemp = arrtemp;
    }

    public String getMonth0() {
        return month0;
    }

    public void setMonth0(String month0) {
        this.month0 = month0;
    }

    public String getDay0() {
        return day0;
    }

    public void setDay0(String day0) {
        this.day0 = day0;
    }

    public String getHour0() {
        return hour0;
    }

    public void setHour0(String hour0) {
        this.hour0 = hour0;
    }

    public String getHalfhour0() {
        return halfhour0;
    }

    public void setHalfhour0(String halfhour0) {
        this.halfhour0 = halfhour0;
    }

    public String getFiltro0() {
        return filtro0;
    }

    public void setFiltro0(String filtro0) {
        this.filtro0 = filtro0;
    }

    public String getFiltroymd() {
        return filtroymd;
    }

    public void setFiltroymd(String filtroymd) {
        this.filtroymd = filtroymd;
    }

    public String getCastymd() {
        return castymd;
    }

    public void setCastymd(String castymd) {
        this.castymd = castymd;
    }  
    
    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getTime1() {
        return time1;
    }

    public void setTime1(String time1) {
        this.time1 = time1;
    }

    public String getTime0() {
        return time0;
    }

    public void setTime0(String time0) {
        this.time0 = time0;
    }


    
    
    

    public BigDecimal getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(BigDecimal temperatura) {
        this.temperatura = temperatura;
    }

    public Integer getRack() {
        return rack;
    }

    public void setRack(Integer rack) {
        this.rack = rack;
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
