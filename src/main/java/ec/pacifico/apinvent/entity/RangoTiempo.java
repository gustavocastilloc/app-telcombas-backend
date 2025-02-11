/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.pacifico.apinvent.entity;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 *
 * @author Carolina
 */
public class RangoTiempo {
    LocalDateTime Ini;
    LocalDateTime End;
    Long id;

    public LocalDateTime getIni() {
        return Ini;
    }

    public void setIni(LocalDateTime Ini) {
        this.Ini = Ini;
    }

    public LocalDateTime getEnd() {
        return End;
    }

    public void setEnd(LocalDateTime End) {
        this.End = End;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    
}
