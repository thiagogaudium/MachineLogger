package com.taximachine.machinelogger;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class GpsDataObject {
    private String modelo, versaoSO, operadora;
    private long memoriaRAMLivre, totalMemoriaRAM;
    private List<Posicao> posicoes = new ArrayList<Posicao>();

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


    public static class Posicao {
        private BigDecimal lat, lng;
        private Float velocidade;
        private float acuracia;
        private String tempo;

        public Posicao(BigDecimal lat, BigDecimal lng, float velocidade, Float acucaria, String tempo) {
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

        public float getAcuracia() {
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
