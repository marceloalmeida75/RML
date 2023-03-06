package br.app.rml.automato;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

/**
 * Created by Marcelo Ciacco de Almeida on 21/05/2016.
 */
public class Automato {

    private Map< Integer, Estado > estados;
    private Set< Transicao > transicoes;
    private Estado estadoInicial;
    private Estado estadoFinal;
    private Stack<String> pilha;

    public Automato( ) {

        estados = new HashMap<>(  );
        transicoes = new HashSet<>(  );
        pilha = new Stack<>();

    }

    public Map< Integer, Estado > getEstados( ) {
        return estados;
    }

    public void setEstados( Integer integer, Estado estado ) {
        estados.put( integer, estado );
    }

    public Set< Transicao > getTransicoes( ) {
        return transicoes;
    }

    public Transicao getTransicao( String simbolo, Estado estadoOrigem, Estado estadoDestino  ){

        Transicao transicao = new Transicao(  );

        Iterator<Transicao> transicaoIterator = transicoes.iterator();

        while( transicaoIterator.hasNext() ){

            Transicao trans = transicaoIterator.next();

            if( trans.getSimbolo().equals( simbolo ) && trans.getOrigem().getId() == estadoOrigem.getId() && trans.getDestino().getId() == estadoDestino.getId() ){

                transicao = trans;
                return transicao;

            }
            else {

                for( int i = trans.getOrigem().getId(); i <= estadoFinal.getId(); i++ ){

                    if( trans.getSimbolo().equals( simbolo ) && trans.getOrigem().getId() == estadoOrigem.getId() && trans.getDestino().getId() == i ){

                        transicao = trans;
                        return transicao;

                    }

                }

                for( int i = estadoFinal.getId(); i >= 0; i-- ){

                    if( trans.getSimbolo().equals( simbolo ) && trans.getOrigem().getId() == estadoOrigem.getId() && trans.getDestino().getId() == i ){

                        transicao = trans;
                        return transicao;

                    }

                }


            }

            transicao = null;

        }

        return transicao;

    }

    public void setTransicoes( Transicao transicao ) {
        transicoes.add( transicao );
    }

    public Estado getEstadoInicial( ) {
        return estadoInicial;
    }

    public void setEstadoInicial( Estado estadoInicial ) {
        this.estadoInicial = estadoInicial;
    }

    public Estado getEstadoFinal( ) {
        return estadoFinal;
    }

    public void setEstadoFinal( Estado estadoFinal ) {
        this.estadoFinal = estadoFinal;
    }

    public Stack< String > getPilha( ) {
        return pilha;
    }

    public void empilhar( String simbolo ){

        pilha.push( simbolo );

    }

    public boolean desempilhar( String simbolo ){

        if( !pilha.isEmpty() ){

            if( pilha.peek().equals( simbolo ) ){

                pilha.pop();
                return true;

            }

            return false;

        }
        else{

            return false;

        }

    }
}
