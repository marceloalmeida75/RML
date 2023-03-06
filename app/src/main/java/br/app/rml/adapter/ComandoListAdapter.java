package br.app.rml.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Collections;
import java.util.List;

import br.app.rml.R;
import br.app.rml.dynamicgrid.BaseDynamicGridAdapter;
import br.app.rml.modelo.Comando;

/**
 * Created by Marcelo Ciacco de Almeida on 28/03/2016.
 */

public class ComandoListAdapter extends BaseDynamicGridAdapter {

    public ComandoListAdapter(Context context, List<Comando> comandos, int columnCount ) {

        super(context, comandos, columnCount);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ComandoHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.comando, null);
            holder = new ComandoHolder(convertView, position);
            convertView.setTag(holder);
        } else {
            holder = (ComandoHolder ) convertView.getTag();
        }
        holder.build();
        return convertView;
    }

    private class ComandoHolder {

        private ImageView imageButtonComando;
        private TextView textViewComando;
        private int position;

        private ComandoHolder(View view, int position) {
            imageButtonComando = ( ImageView ) view.findViewById(R.id.imageButtonComando);
            textViewComando = ( TextView ) view.findViewById( R.id.textViewComando );
            this.position = position;
        }

        void build() {

            Comando com = ( Comando ) getItem( position );
            imageButtonComando.setImageResource( com.getImagem() );
            textViewComando.setText( String.valueOf( com.getRepetir() ) );

            if( com.getRepetir() <= 0 ){

                textViewComando.setVisibility( View.INVISIBLE );

            }

            if( com.isError() ){

                imageButtonComando.setBackgroundColor( Color.RED );

            }

            if( com.isAtivo() ){

                imageButtonComando.setBackgroundColor( Color.BLUE );

            }

        }
    }

}

/*public class ComandoListAdapter extends BaseAdapter {

    private Context context;
    private List<Comando> comandos;

    public ComandoListAdapter( Context context, List< Comando > comandos ) {

        this.context = context;
        this.comandos = comandos;

    }

    @Override
    public int getCount( ) {
        return comandos.size();
    }

    @Override
    public Object getItem( int position ) {
        return comandos.get( position );
    }

    @Override
    public long getItemId( int position ) {
        return 0;
    }

    @Override
    public View getView( final int position, View convertView, ViewGroup parent ) {

        View view = LayoutInflater.from( parent.getContext( ) ).inflate( R.layout.comando, null);
        ImageButton imageButtonComando = (ImageButton) view.findViewById( R.id.imageButtonComando );

        imageButtonComando.setImageResource( comandos.get( position ).getImagem() );

        if( comandos.get( position ).isError() ){

            imageButtonComando.setBackgroundColor( Color.RED );

        }

        imageButtonComando.setOnClickListener( new View.OnClickListener( ) {

            @Override
            public void onClick( View v ) {

                MainActivity.removerComando( context, position );

            }

        } );


        return view;

    }

}*/


