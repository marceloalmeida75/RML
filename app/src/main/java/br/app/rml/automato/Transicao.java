package br.app.rml.automato;

/**
 * Created by Marcelo Ciacco de Almeida on 21/05/2016.
 */
public class Transicao {

    private Estado origem;
    private Estado destino;
    private String simbolo;
    private String leitorPilha;
    private String escritorPilha;

    public Transicao( ) {
    }

    public Transicao( Estado origem, Estado destino, String simbolo, String leitorPilha, String escritorPilha ) {
        this.origem = origem;
        this.destino = destino;
        this.simbolo = simbolo;
        this.leitorPilha = leitorPilha;
        this.escritorPilha = escritorPilha;
    }

    public Estado getOrigem( ) {
        return origem;
    }

    public void setOrigem( Estado origem ) {
        this.origem = origem;
    }

    public Estado getDestino( ) {
        return destino;
    }

    public void setDestino( Estado destino ) {
        this.destino = destino;
    }

    public String getSimbolo( ) {
        return simbolo;
    }

    public void setSimbolo( String simbolo ) {
        this.simbolo = simbolo;
    }

    public String getLeitorPilha( ) {
        return leitorPilha;
    }

    public void setLeitorPilha( String leitorPilha ) {
        this.leitorPilha = leitorPilha;
    }

    public String getEscritorPilha( ) {
        return escritorPilha;
    }

    public void setEscritorPilha( String escritorPilha ) {
        this.escritorPilha = escritorPilha;
    }

}
