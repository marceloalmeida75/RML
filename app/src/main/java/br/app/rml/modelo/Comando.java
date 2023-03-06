package br.app.rml.modelo;

/**
 * Created by Marcelo Ciacco de Almeida on 28/03/2016.
 */
public class Comando {

    private String letra;
    private int imagem;
    private int repetir;
    private boolean error;
    private boolean ativo;

    public Comando( boolean error ) {
        this.error = error;
        repetir = 0;
        ativo = false;
    }

    public int getImagem( ) {
        return imagem;
    }

    public void setImagem( int imagem ) {
        this.imagem = imagem;
    }

    public String getLetra( ) {
        return letra;
    }

    public void setLetra( String letra ) {
        this.letra = letra;
    }

    public int getRepetir( ) {
        return repetir;
    }

    public void setRepetir( int repetir ) {
        this.repetir = repetir;
    }

    public boolean isError( ) {
        return error;
    }

    public void setError( boolean error ) {
        this.error = error;
    }

    public boolean isAtivo( ) {
        return ativo;
    }

    public void setAtivo( boolean ativo ) {
        this.ativo = ativo;
    }

    @Override
    public String toString( ) {
        return "Comando{" +
                "letra='" + letra + '\'' +
                ", repetir=" + repetir +
                '}';
    }
}
