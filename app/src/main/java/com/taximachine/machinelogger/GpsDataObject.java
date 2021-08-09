package com.taximachine.machinelogger;

import java.math.BigDecimal;

public class GpsDataObject {
    private BigDecimal lat, lng;
    private String modelo, versaoSO;
    private long memoriaRAMLivre, totalMemoriaRAM;

    public BigDecimal getLat() {
        return lat;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
    }

    public BigDecimal getLng() {
        return lng;
    }

    public void setLng(BigDecimal lng) {
        this.lng = lng;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getVersaoSO() {
        return versaoSO;
    }

    public void setVersaoSO(String versaoSO) {
        this.versaoSO = versaoSO;
    }

    public long getMemoriaRAMLivre() {
        return memoriaRAMLivre;
    }

    public void setMemoriaRAMLivre(long memoriaRAMLivre) {
        this.memoriaRAMLivre = memoriaRAMLivre;
    }

    public long getTotalMemoriaRAM() {
        return totalMemoriaRAM;
    }

    public void setTotalMemoriaRAM(long totalMemoriaRAM) {
        this.totalMemoriaRAM = totalMemoriaRAM;
    }
}
