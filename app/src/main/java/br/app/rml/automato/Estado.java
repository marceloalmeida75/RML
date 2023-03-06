package br.app.rml.automato;

/**
 * Created by Marcelo Ciacco de Almeida on 21/05/2016.
 */
public class Estado {

    private int id;
    private String nome;

    public Estado( int id, String nome ) {
        this.id = id;
        this.nome = nome;
    }

    public int getId( ) {
        return id;
    }

    public void setId( int id ) {
        this.id = id;
    }

    public String getNome( ) {
        return nome;
    }

    public void setNome( String nome ) {
        this.nome = nome;
    }

}
