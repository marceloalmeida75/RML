package br.app.rml;

import android.graphics.Color;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class SorteioActivity extends AppCompatActivity {

    private String[] toBe = {"Você é da Jamaica?", "Que outra língua os canadenses falam?", "Quantos anos têm sua mãe e seu pai?", "Minha sala de aula é legal.", "Estou tão feliz em receber seu e-mail.",
    "Seus amigos são legais?", "Minha escola não é grande.", "Onde é sua sala de estudos?", "Que cor são as paredes do quarto?", "A biblioteca na escola é fantástica."};
    private String[] present = {"Sempre amei matemática!","Trabalho para esta empresa há 10 anos.","Ele é escritor desde 1995.", "Eles não jogam futebol há cinco anos.", "Ela ainda não viu esse filme.",
            "Você já jogou tênis?", "Nunca viajei para os EUA.", "Alguma coisa mudou na carreira de Marta desde que o artigo foi publicado?", "As histórias de J. K. Rowling atraíram milhões de pessoas às livrarias.",
    "Brian não deixou nenhum recado para você."};
    private String[] phrasal = { "É trabalho duro cuidar de três crianças o dia inteiro.", "Por favor, me pegue depois da festa.", "Ele está se candidatando para um emprego em São Paulo.", "Quero participar da discussão.",
    "Ficarei muito surpreso se eles aparecerem pontualmente.", "Por favor, ligue o rádio.", "Eu odeio levantar muito cedo.", "Meu computador desligou.", "Não podemos atrasar o relógio.", "Estudantes, sentem-se, por favor!"};

    private int randomico;

    private Button btnToBe;
    private Button btnPresent;
    private Button btnPhrasal;
    private TextView txtSorteio;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_sorteio );

        btnToBe = (Button) findViewById( R.id.btn_toBe );
        btnPresent = (Button) findViewById( R.id.btnPresent );
        btnPhrasal = (Button) findViewById( R.id.btnPhrasal );

        txtSorteio = ( TextView ) findViewById( R.id.txtSorteio );

        randomico = 0;

        btnToBe.setOnClickListener( new View.OnClickListener( ) {

            @Override
            public void onClick( View v ) {

                Random random = new Random();
                randomico = random.nextInt(10);
                txtSorteio.setTextColor( Color.rgb( 115, 180, 115 ) );
                txtSorteio.setText( toBe[randomico] );

            }
        } );

        btnPresent.setOnClickListener( new View.OnClickListener( ) {

            @Override
            public void onClick( View v ) {

                Random random = new Random();
                randomico = random.nextInt(10);
                txtSorteio.setTextColor( Color.rgb( 112, 185, 219 ) );
                txtSorteio.setText( present[randomico] );

            }
        } );

        btnPhrasal.setOnClickListener( new View.OnClickListener( ) {

            @Override
            public void onClick( View v ) {

                Random random = new Random();
                randomico = random.nextInt(10);
                txtSorteio.setTextColor( Color.rgb( 230, 57, 57 ) );
                txtSorteio.setText( phrasal[randomico] );

            }
        } );

    }
}
