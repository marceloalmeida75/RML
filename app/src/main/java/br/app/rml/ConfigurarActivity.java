package br.app.rml;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ConfigurarActivity extends AppCompatActivity {

    private Button btnRodaDir;
    private Button btnRodaEsq;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_configurar );

        btnRodaDir = ( Button ) findViewById( R.id.btnRodaDir );
        btnRodaEsq = ( Button ) findViewById( R.id.btnRodaEsq );

        btnRodaDir.setOnClickListener( new View.OnClickListener( ) {

            @Override
            public void onClick( View v ) {

                AlertDialog.Builder dialogLoop = new AlertDialog.Builder( ConfigurarActivity.this );
                dialogLoop.setTitle( "CONFIGURAR VELOCIDADE" );
                dialogLoop.setMessage( "Valor da Velocidade Roda Direita de 0 até 255" );
                final EditText input = new EditText(ConfigurarActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setText( String.valueOf( 2 ) );
                input.setLayoutParams(lp);
                dialogLoop.setView(input);
                dialogLoop.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        if( Integer.parseInt( input.getText().toString() ) < 0 )
                            MainActivity.motor2 = 0;
                        else if( Integer.parseInt( input.getText().toString() ) > 255 )
                            MainActivity.motor2 = 255;
                        else
                            MainActivity.motor2 = Integer.parseInt( input.getText().toString() );

                    }

                });

                dialogLoop.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();

                    }

                });

                dialogLoop.show();

            }

        } );

        btnRodaEsq.setOnClickListener( new View.OnClickListener( ) {

            @Override
            public void onClick( View v ) {

                AlertDialog.Builder dialogLoop = new AlertDialog.Builder( ConfigurarActivity.this );
                dialogLoop.setTitle( "CONFIGURAR VELOCIDADE" );
                dialogLoop.setMessage( "Valor da Velocidade Roda Esquerda de 0 até 255" );
                final EditText input = new EditText(ConfigurarActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setText( String.valueOf( 2 ) );
                input.setLayoutParams(lp);
                dialogLoop.setView(input);
                dialogLoop.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        if( Integer.parseInt( input.getText().toString() ) < 0 )
                            MainActivity.motor1 = 0;
                        else if( Integer.parseInt( input.getText().toString() ) > 255 )
                            MainActivity.motor1 = 255;
                        else
                            MainActivity.motor1 = Integer.parseInt( input.getText().toString() );

                    }

                });

                dialogLoop.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();

                    }

                });

                dialogLoop.show();

            }

        } );

    }
}
