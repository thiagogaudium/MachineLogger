package com.taximachine.machinelogger;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class GpsDataObject {
    private String modelo, versao_so, operadora, identifier;
    private long memoria_ram_livre, total_memoria_ram;
    private List<Posicao> posicoes = new ArrayList<Posicao>();

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getVersao_so() {
        return versao_so;
    }

    public void setVersao_so(String versao_so) {
        this.versao_so = versao_so;
    }

    public long getMemoria_ram_livre() {
        return memoria_ram_livre;
    }

    public void setMemoria_ram_livre(long memoria_ram_livre) {
        this.memoria_ram_livre = memoria_ram_livre;
    }

    public long getTotal_memoria_ram() {
        return total_memoria_ram;
    }

    public void setTotal_memoria_ram(long total_memoria_ram) {
        this.total_memoria_ram = total_memoria_ram;
    }

    public List<Posicao> getPosicoes() {
        return posicoes;
    }

    public void addPosicao(Posicao posicao) {
        posicoes.add(posicao);
    }

    public String getOperadora() {
        return operadora;
    }

    public void setOperadora(String operadora) {
        this.operadora = operadora;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }


    public static class Posicao {
        private BigDecimal lat, lng;
        private Float velocidade;
        private float acuracia;
        private String tempo;

        public Posicao(BigDecimal lat, BigDecimal lng, Float velocidade, Float acucaria, String tempo) {
            this.lat = lat;
            this.lng = lng;
            this.velocidade = velocidade;
            this.acuracia = acucaria;
            this.tempo = tempo;
        }

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

        public Float getVelocidade() {
            return velocidade;
        }

        public void setVelocidade(Float velocidade) {
            this.velocidade = velocidade;
        }

        public Float getAcuracia() {
            return acuracia;
        }

        public void setAcuracia(float acuracia) {
            this.acuracia = acuracia;
        }

        public String getTempo() {
            return tempo;
        }

        public void setTempo(String tempo) {
            this.tempo = tempo;
        }
    }
}
