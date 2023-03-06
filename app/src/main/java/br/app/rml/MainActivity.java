package br.app.rml;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Stack;
import java.util.UUID;

import br.app.rml.adapter.ComandoListAdapter;
import br.app.rml.automato.Automato;
import br.app.rml.automato.Estado;
import br.app.rml.automato.Transicao;
import br.app.rml.dynamicgrid.DynamicGridView;
import br.app.rml.modelo.Comando;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private static final String TAG = MainActivity.class.getName();

    private static ComandoListAdapter comandoListAdapter;
    private static List<Comando> comandos;

    public static int repetir;
    public static int tempoFrente;
    public static int tempoTras;
    public static int tempoVirar;
    public static int grausEsq;
    public static int grausDir;

    public static int motor1;
    public static int motor2;

    public enum Language {
        PORTUGUES, INGLES
    }

    private Language language;

    private TextToSpeech tts;

    private static DynamicGridView gridView;

    private BluetoothDevice dispositivoBluetoohRemoto;
    private BluetoothAdapter meuBluetoothAdapter = null;
    private BluetoothSocket bluetoothSocket = null;
    private static final String endereco_MAC_do_Bluetooth_Remoto = "98:D3:31:90:4D:99";
    public static final int CÓDIGO_PARA_ATIVAÇÃO_DO_BLUETOOTH = 1;
    private static final UUID MEU_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static OutputStream outStream = null;

    private SpeechRecognizer mSpeechRecognizer;
    Intent mSpeechIntent;
    private Handler mHandler = new Handler();

    //legel commands
    private static final String[] VALID_COMMANDS = {
            "start",
            "forward",
            "backward",
            "turn on left ahead",
            "turn on right ahead",
            "turn on left",
            "turn on right",
            "repeat",
            "end repeat",
            "end",
            "delete",
    };

    private static final int VALID_COMMANDS_SIZE = VALID_COMMANDS.length;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        meuBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (!meuBluetoothAdapter.isEnabled()) {
            Intent novoIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            startActivityForResult(novoIntent, CÓDIGO_PARA_ATIVAÇÃO_DO_BLUETOOTH);
        }

        comandos = new ArrayList<>();
        comandoListAdapter = new ComandoListAdapter(getApplicationContext(), comandos, getResources().getInteger(R.integer.column_count));

        repetir = 1;
        tempoFrente = 1000;
        tempoTras = 1000;
        tempoVirar = 1000;
        motor1 = 80;
        motor2 = 50;

        language = Language.PORTUGUES;

        tts = new TextToSpeech(this, this);

        gridView = (DynamicGridView) findViewById(R.id.dynamic_grid);
        gridView.setAdapter(comandoListAdapter);

        ImageButton imageButton1 = (ImageButton) findViewById(R.id.imageButton);
        ImageButton imageButton2 = (ImageButton) findViewById(R.id.imageButton2);
        ImageButton imageButton3 = (ImageButton) findViewById(R.id.imageButton3);
        ImageButton imageButton4 = (ImageButton) findViewById(R.id.imageButton4);
        ImageButton imageButton5 = (ImageButton) findViewById(R.id.imageButton5);
        ImageButton imageButton6 = (ImageButton) findViewById(R.id.imageButton6);
        ImageButton imageButton7 = (ImageButton) findViewById(R.id.imageButton7);
        ImageButton imageButton8 = (ImageButton) findViewById(R.id.imageButton8);
        ImageButton imageButton9 = (ImageButton) findViewById(R.id.imageButton9);
        ImageButton imageButton10 = (ImageButton) findViewById(R.id.imageButton10);
        ImageButton imageButton11 = (ImageButton) findViewById(R.id.imageButton11);
        ImageButton imageButton12 = (ImageButton) findViewById(R.id.imageButton12);
        ImageButton imageButton13 = (ImageButton) findViewById(R.id.imageButton13);
        ImageButton imageButton14 = (ImageButton) findViewById(R.id.imageButton14);

        imageButton1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Comando comando = new Comando(false);
                comando.setLetra("S");
                comando.setImagem(R.drawable.ic_start);
                comandos.add(comando);
                comandoListAdapter.set(comandos);
                gridView.setAdapter(comandoListAdapter);
                switch (language) {

                    case PORTUGUES:
                        speakOut("Início");
                        break;

                    case INGLES:
                        speakOut("Start");
                        break;
                }

            }

        });

        imageButton2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Comando comando = new Comando(false);
                comando.setLetra("F");
                comando.setImagem(R.drawable.ic_finish);
                comandos.add(comando);
                comandoListAdapter.set(comandos);
                gridView.setAdapter(comandoListAdapter);
                switch (language) {

                    case PORTUGUES:
                        speakOut("Fim");
                        break;

                    case INGLES:
                        speakOut("End");
                        break;
                }

            }

        });

        imageButton3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Comando comando = new Comando(false);
                comando.setLetra("C");
                comando.setImagem(R.drawable.ic_forward);
                comandos.add(comando);
                comandoListAdapter.set(comandos);
                gridView.setAdapter(comandoListAdapter);
                switch (language) {

                    case PORTUGUES:
                        speakOut("Para Frente");
                        break;

                    case INGLES:
                        speakOut("Forward");
                        break;
                }


            }

        });

        imageButton3.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {

                AlertDialog.Builder dialogTempo = new AlertDialog.Builder(MainActivity.this);
                dialogTempo.setTitle("CONFIGURAR TEMPO");
                dialogTempo.setMessage("Valor do tempo em segundos");
                final EditText input = new EditText(MainActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setText(String.valueOf(1));
                input.setLayoutParams(lp);
                dialogTempo.setView(input);
                switch (language) {

                    case PORTUGUES:
                        speakOut("Quantos segundos ir para frente?");
                        break;

                    case INGLES:
                        speakOut("How many seconds do you want to go forward?");
                        break;
                }
                dialogTempo.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        if (Integer.parseInt(input.getText().toString()) <= 0)
                            tempoFrente = 1000;
                        else
                            tempoFrente = (int) Float.parseFloat(input.getText().toString()) * 1000;

                    }

                });

                dialogTempo.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();

                    }

                });

                dialogTempo.show();

                return false;
            }

        });

        imageButton4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Comando comando = new Comando(false);
                comando.setLetra("B");
                comando.setImagem(R.drawable.ic_backward);
                comandos.add(comando);
                comandoListAdapter.set(comandos);
                gridView.setAdapter(comandoListAdapter);
                switch (language) {

                    case PORTUGUES:
                        speakOut("Para Trás");
                        break;

                    case INGLES:
                        speakOut("Backward");
                        break;
                }


            }

        });

        imageButton4.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {

                AlertDialog.Builder dialogLoop = new AlertDialog.Builder(MainActivity.this);
                dialogLoop.setTitle("CONFIGURAR TEMPO");
                dialogLoop.setMessage("Valor do tempo em segundos");
                final EditText input = new EditText(MainActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setText(String.valueOf(1));
                input.setLayoutParams(lp);
                dialogLoop.setView(input);
                switch (language) {

                    case PORTUGUES:
                        speakOut("Quantos segundos ir para trás?");
                        break;

                    case INGLES:
                        speakOut("How many seconds do you want to go backward?");
                        break;
                }
                dialogLoop.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        if (Integer.parseInt(input.getText().toString()) <= 0)
                            tempoTras = 1000;
                        else
                            tempoTras = (int) (Float.parseFloat(input.getText().toString()) * 1000);

                    }

                });

                dialogLoop.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();

                    }

                });

                dialogLoop.show();

                return false;
            }

        });

        imageButton5.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Comando comando = new Comando(false);
                comando.setLetra("L");
                comando.setImagem(R.drawable.ic_left);
                comandos.add(comando);
                comandoListAdapter.set(comandos);
                gridView.setAdapter(comandoListAdapter);
                switch (language) {

                    case PORTUGUES:
                        speakOut("Virar à esquerda");
                        break;

                    case INGLES:
                        speakOut("Turn on left ahead");
                        break;
                }


            }

        });

        imageButton5.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {

                AlertDialog.Builder dialogLoop = new AlertDialog.Builder(MainActivity.this);
                dialogLoop.setTitle("CONFIGURAR TEMPO");
                dialogLoop.setMessage("Valor do tempo em segundos");
                final EditText input = new EditText(MainActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setText(String.valueOf(1));
                input.setLayoutParams(lp);
                dialogLoop.setView(input);
                switch (language) {

                    case PORTUGUES:
                        speakOut("Quantos segundos para virar à esquerda?");
                        break;

                    case INGLES:
                        speakOut("How many seconds do you want to Turn on left ahead?");
                        break;
                }
                dialogLoop.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        if (Integer.parseInt(input.getText().toString()) <= 0)
                            tempoVirar = 1000;
                        else
                            tempoVirar = (int) (Float.parseFloat(input.getText().toString()) * 1000);

                    }

                });

                dialogLoop.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();

                    }

                });

                dialogLoop.show();

                return false;
            }

        });

        imageButton6.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Comando comando = new Comando(false);
                comando.setLetra("R");
                comando.setImagem(R.drawable.ic_right);
                comandos.add(comando);
                comandoListAdapter.set(comandos);
                gridView.setAdapter(comandoListAdapter);
                switch (language) {

                    case PORTUGUES:
                        speakOut("Virar à direita");
                        break;

                    case INGLES:
                        speakOut("Turn on right ahead");
                        break;
                }

            }

        });

        imageButton6.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {

                AlertDialog.Builder dialogLoop = new AlertDialog.Builder(MainActivity.this);
                dialogLoop.setTitle("CONFIGURAR TEMPO");
                dialogLoop.setMessage("Valor do tempo em segundos");
                final EditText input = new EditText(MainActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setText(String.valueOf(1));
                input.setLayoutParams(lp);
                dialogLoop.setView(input);
                switch (language) {

                    case PORTUGUES:
                        speakOut("Quantos segundos para virar à esquerda?");
                        break;

                    case INGLES:
                        speakOut("How many seconds do you want to Turn on right ahead?");
                        break;
                }
                dialogLoop.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        if (Integer.parseInt(input.getText().toString()) <= 0)
                            tempoVirar = 1000;
                        else
                            tempoVirar = (int) (Float.parseFloat(input.getText().toString()) * 1000);

                    }

                });

                dialogLoop.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();

                    }

                });

                dialogLoop.show();

                return false;
            }

        });

        imageButton7.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Comando comando = new Comando(false);
                comando.setLetra("E");
                comando.setImagem(R.drawable.ic_turn_left);
                comando.setRepetir(repetir);
                comandos.add(comando);
                comandoListAdapter.set(comandos);
                gridView.setAdapter(comandoListAdapter);
                repetir = 1;
                switch (language) {

                    case PORTUGUES:
                        speakOut("Curva à esquerda");
                        break;

                    case INGLES:
                        speakOut("Turn on left");
                        break;
                }

            }

        });

        imageButton7.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {

                AlertDialog.Builder dialogLoop = new AlertDialog.Builder(MainActivity.this);
                dialogLoop.setTitle("CONFIGURAR GRAUS");
                dialogLoop.setMessage("Quantos Graus quer virar 1 até 360");
                final EditText input = new EditText(MainActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setText(String.valueOf(1));
                input.setLayoutParams(lp);
                dialogLoop.setView(input);
                switch (language) {

                    case PORTUGUES:
                        speakOut("Quantos graus para fazer curva à esquerda?");
                        break;

                    case INGLES:
                        speakOut("How many degrees do you want to Turn on left?");
                        break;
                }
                dialogLoop.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        if (Integer.parseInt(input.getText().toString()) <= 0) {
                            repetir = 1;
                            grausEsq = 1;
                        } else if (Integer.parseInt(input.getText().toString()) > 360) {
                            repetir = 360;
                            grausEsq = 360;
                        } else {
                            repetir = Integer.parseInt(input.getText().toString());
                            grausEsq = Integer.parseInt(input.getText().toString());
                        }

                    }

                });

                dialogLoop.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();

                    }

                });

                dialogLoop.show();

                return false;
            }

        });

        imageButton8.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Comando comando = new Comando(false);
                comando.setLetra("D");
                comando.setImagem(R.drawable.ic_turn_right);
                comando.setRepetir(repetir);
                comandos.add(comando);
                comandoListAdapter.set(comandos);
                gridView.setAdapter(comandoListAdapter);
                repetir = 1;
                switch (language) {

                    case PORTUGUES:
                        speakOut("Curva à direita");
                        break;

                    case INGLES:
                        speakOut("Turn on rigth");
                        break;
                }

            }

        });

        imageButton8.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {

                AlertDialog.Builder dialogLoop = new AlertDialog.Builder(MainActivity.this);
                dialogLoop.setTitle("CONFIGURAR GRAUS");
                dialogLoop.setMessage("Quantos Graus quer virar 1 até 360");
                final EditText input = new EditText(MainActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setText(String.valueOf(1));
                input.setLayoutParams(lp);
                dialogLoop.setView(input);
                switch (language) {

                    case PORTUGUES:
                        speakOut("Quantos graus para fazer curva à direita?");
                        break;

                    case INGLES:
                        speakOut("How many degrees do you want to Turn on right?");
                        break;
                }
                dialogLoop.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        if (Integer.parseInt(input.getText().toString()) <= 0) {
                            repetir = 1;
                            grausDir = 1;
                        } else if (Integer.parseInt(input.getText().toString()) > 360) {
                            repetir = 360;
                            grausDir = 360;
                        } else {
                            repetir = Integer.parseInt(input.getText().toString());
                            grausDir = Integer.parseInt(input.getText().toString());
                        }

                    }

                });

                dialogLoop.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();

                    }

                });

                dialogLoop.show();

                return false;
            }

        });

        imageButton9.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                int automato = -1;

                if (comandos.size() > 0) {

                    for (Comando com : comandos) {

                        com.setError(false);
                        com.setAtivo(false);

                    }

                    comandoListAdapter.set(comandos);
                    gridView.setAdapter(comandoListAdapter);

                    automato = verificarLinguagem();

                    switch (automato) {

                        case -1:
                            switch (language) {

                                case PORTUGUES:
                                    speakOut("Código Aceito");
                                    Toast.makeText(getApplicationContext(), "CÓDIGO ACEITO", Toast.LENGTH_SHORT).show();
                                    break;

                                case INGLES:
                                    speakOut("Accepted code");
                                    Toast.makeText(getApplicationContext(), "ACCEPTED CODE", Toast.LENGTH_SHORT).show();
                                    break;
                            }

                            new FuncionarRobo(comandos).execute();
                            break;

                        case 0:
                            comandos.get(0).setError(true);
                            comandoListAdapter.set(comandos);
                            gridView.setAdapter(comandoListAdapter);

                            switch (language) {

                                case PORTUGUES:
                                    speakOut("CÓDIGO COM ERRO NO INÍCIO");
                                    Toast.makeText(getApplicationContext(), "CÓDIGO COM ERRO 01: ERRO NO INÍCIO", Toast.LENGTH_SHORT).show();
                                    break;

                                case INGLES:
                                    speakOut("error at start");
                                    Toast.makeText(getApplicationContext(), "ERROR CODE 01: ERROR AT START", Toast.LENGTH_SHORT).show();
                                    break;
                            }

                            break;

                        case 1:
                            for (Comando com : comandos) {

                                if (com.getLetra().equals("O")) {

                                    com.setError(true);

                                }

                            }
                            comandoListAdapter.set(comandos);
                            gridView.setAdapter(comandoListAdapter);

                            switch (language) {

                                case PORTUGUES:
                                    speakOut("CÓDIGO COM ERRO NO INÍCIO DA REPETIÇÃO");
                                    Toast.makeText(getApplicationContext(), "CÓDIGO COM ERRO 02: ERRO NO INÍCIO DA REPETIÇÃO", Toast.LENGTH_SHORT).show();
                                    break;

                                case INGLES:
                                    speakOut("ERROR AT BEGINNING OF REPETITION");
                                    Toast.makeText(getApplicationContext(), "ERROR CODE 02: ERROR AT BEGINNING OF REPETITION", Toast.LENGTH_SHORT).show();
                                    break;
                            }

                            break;

                        case 2:
                            for (Comando com : comandos) {

                                if (com.getLetra().equals("P")) {

                                    com.setError(true);

                                }

                            }
                            comandoListAdapter.set(comandos);
                            gridView.setAdapter(comandoListAdapter);

                            switch (language) {

                                case PORTUGUES:
                                    speakOut("CÓDIGO COM ERRO NO FIM DA REPETIÇÃO");
                                    Toast.makeText(getApplicationContext(), "CÓDIGO COM ERRO 03: ERRO NO FIM DA REPETIÇÃO", Toast.LENGTH_SHORT).show();
                                    break;

                                case INGLES:
                                    speakOut("ERROR AT END OF REPETITION");
                                    Toast.makeText(getApplicationContext(), "ERROR CODE 03: ERROR AT END OF REPETITION", Toast.LENGTH_SHORT).show();
                                    break;
                            }

                            break;

                        case 3:
                            comandos.get(comandos.size() - 1).setError(true);
                            comandoListAdapter.set(comandos);
                            gridView.setAdapter(comandoListAdapter);

                            switch (language) {

                                case PORTUGUES:
                                    speakOut("CÓDIGO COM ERRO NO FIM");
                                    Toast.makeText(getApplicationContext(), "CÓDIGO COM ERRO 04: ERRO NO FIM", Toast.LENGTH_SHORT).show();
                                    break;

                                case INGLES:
                                    speakOut("ERROR AT END");
                                    Toast.makeText(getApplicationContext(), "ERROR CODE 04: ERROR AT END", Toast.LENGTH_SHORT).show();
                                    break;
                            }

                            break;

                    }

                } else {
                    switch (language) {

                        case PORTUGUES:
                            speakOut("AINDA NÃO TEM NENHUM CÓDIGO");
                            Toast.makeText(getApplicationContext(), "AINDA NÃO TEM NENHUM CÓDIGO", Toast.LENGTH_SHORT).show();
                            break;

                        case INGLES:
                            speakOut("HAS NO CODE YET");
                            Toast.makeText(getApplicationContext(), "HAS NO CODE YET", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }

        });

        imageButton10.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Comando comando = new Comando(false);
                comando.setLetra("O");
                comando.setImagem(R.drawable.ic_loop_start);
                comando.setRepetir(repetir);
                comandos.add(comando);
                comandoListAdapter.set(comandos);
                gridView.setAdapter(comandoListAdapter);
                switch (repetir) {

                    case 1:

                        switch (language) {

                            case PORTUGUES:
                                speakOut("Repetir uma vez");
                                break;

                            case INGLES:
                                speakOut("Repeat Once");
                                break;
                        }

                        break;

                    case 2:

                        switch (language) {

                            case PORTUGUES:
                                speakOut("Repetir duas vezes");
                                break;

                            case INGLES:
                                speakOut("Repeat Twice");
                                break;
                        }

                        break;

                    default:

                        switch (language) {

                            case PORTUGUES:
                                speakOut("Repetir " + repetir + " vezes");
                                break;

                            case INGLES:
                                speakOut("Repeat " + repetir + " times");
                                break;
                        }

                        break;
                }
                repetir = 1;
            }

        });

        imageButton10.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {

                AlertDialog.Builder dialogLoop = new AlertDialog.Builder(MainActivity.this);
                dialogLoop.setTitle("REPETIR");
                dialogLoop.setMessage("Quantidade de repetições");
                final EditText input = new EditText(MainActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setText(String.valueOf(1));
                input.setLayoutParams(lp);
                dialogLoop.setView(input);

                switch (language) {

                    case PORTUGUES:
                        speakOut("Quantas vezes quer repetir?");
                        break;

                    case INGLES:
                        speakOut("How many times do you want to repeat?");
                        break;
                }

                dialogLoop.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        if (Integer.parseInt(input.getText().toString()) <= 0)
                            repetir = 1;
                        else
                            repetir = Integer.parseInt(input.getText().toString());

                    }

                });

                dialogLoop.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();

                    }

                });

                dialogLoop.show();

                return true;
            }

        });

        imageButton11.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Comando comando = new Comando(false);
                comando.setLetra("P");
                comando.setImagem(R.drawable.ic_loop_finish);
                comandos.add(comando);
                comandoListAdapter.set(comandos);
                gridView.setAdapter(comandoListAdapter);
                switch (language) {

                    case PORTUGUES:
                        speakOut("Fim da repetição");
                        break;

                    case INGLES:
                        speakOut("End at repeat");
                        break;
                }

            }

        });

        imageButton12.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                comandos.clear();
                comandoListAdapter.set(comandos);
                gridView.setAdapter(comandoListAdapter);
                switch (language) {

                    case PORTUGUES:
                        speakOut("Apagou todos os comandos");
                        break;

                    case INGLES:
                        speakOut("Deleted all commands");
                        break;
                }

            }

        });

        imageButton13.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (mSpeechRecognizer != null)
                    iniciarParametrosFala();

                mSpeechRecognizer.startListening(mSpeechIntent);

            }

        });

        imageButton14.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String mot1, mot2;

                if (motor1 <= 9) {
                    mot1 = "00" + String.valueOf(motor1);
                } else if (motor1 >= 10 && motor1 <= 99) {
                    mot1 = "0" + String.valueOf(motor1);
                } else
                    mot1 = String.valueOf(motor1);

                if (motor2 <= 9) {
                    mot2 = "00" + String.valueOf(motor2);
                } else if (motor2 >= 10 && motor2 <= 99) {
                    mot2 = "0" + String.valueOf(motor2);
                } else
                    mot2 = String.valueOf(motor2);

                if (bluetoothSocket != null)
                    sendData(mot1 + mot2, MainActivity.this);

            }

        });

        gridView.setOnDragListener(new DynamicGridView.OnDragListener() {

            @Override
            public void onDragStarted(int position) {
                Log.d(TAG, "drag started at position " + position);
            }

            @Override
            public void onDragPositionsChanged(int oldPosition, int newPosition) {

                Log.d(TAG, String.format("drag item position changed from %d to %d", oldPosition, newPosition));

                Collections.swap(comandos, oldPosition, newPosition);
                gridView.stopEditMode();

            }

        });

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                gridView.startEditMode(position);
                return true;
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                removerComando(getApplicationContext(), position);
            }

        });

        iniciarParametrosFala();

    }

    public void iniciarParametrosFala() {

        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(MainActivity.this);
        SpeechListener mRecognitionListener = new SpeechListener();
        mSpeechRecognizer.setRecognitionListener((RecognitionListener) mRecognitionListener);
        mSpeechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        mSpeechIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, "TAG_FALA");

        mSpeechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 4);
        mSpeechIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);

    }

    private void processCommand(ArrayList<String> matchStrings) {
        String response = "Iniciando reconhecimento de comando de voz....";
        mSpeechRecognizer.stopListening();
        int maxStrings = matchStrings.size();
        boolean resultFound = false;
        for (int i = 0; i < VALID_COMMANDS_SIZE && !resultFound; i++) {
            for (int j = 0; j < maxStrings && !resultFound; j++) {
                if (StringUtils.getLevenshteinDistance(matchStrings.get(j), VALID_COMMANDS[i]) < (VALID_COMMANDS[i].length() / 3)) {
                    response = getResponse(i);
                }
            }
        }

        final String finalResponse = response;
        mHandler.post(new Runnable() {
            public void run() {
                Log.i("Reconhecimento", finalResponse);
            }
        });

    }

    private String getResponse(int command) {
        String retString = "Comando não reconhecido!";

        Comando comando;

        switch (command) {

            //Pausar
            case 0:
                comando = new Comando(false);
                comando.setLetra("S");
                comando.setImagem(R.drawable.ic_start);
                comandos.add(comando);
                comandoListAdapter.set(comandos);
                gridView.setAdapter(comandoListAdapter);
                break;

            case 1:
                comando = new Comando(false);
                comando.setLetra("C");
                comando.setImagem(R.drawable.ic_forward);
                comandos.add(comando);
                comandoListAdapter.set(comandos);
                gridView.setAdapter(comandoListAdapter);
                break;

            case 2:
                comando = new Comando(false);
                comando.setLetra("B");
                comando.setImagem(R.drawable.ic_backward);
                comandos.add(comando);
                comandoListAdapter.set(comandos);
                gridView.setAdapter(comandoListAdapter);
                break;

            case 3:
                comando = new Comando(false);
                comando.setLetra("L");
                comando.setImagem(R.drawable.ic_left);
                comandos.add(comando);
                comandoListAdapter.set(comandos);
                gridView.setAdapter(comandoListAdapter);
                break;

            case 4:
                comando = new Comando(false);
                comando.setLetra("R");
                comando.setImagem(R.drawable.ic_right);
                comandos.add(comando);
                comandoListAdapter.set(comandos);
                gridView.setAdapter(comandoListAdapter);
                break;

            case 5:
                comando = new Comando(false);
                comando.setLetra("E");
                comando.setImagem(R.drawable.ic_turn_left);
                comando.setRepetir(repetir);
                comandos.add(comando);
                comandoListAdapter.set(comandos);
                gridView.setAdapter(comandoListAdapter);
                repetir = 1;
                break;

            case 6:
                comando = new Comando(false);
                comando.setLetra("D");
                comando.setImagem(R.drawable.ic_turn_right);
                comando.setRepetir(repetir);
                comandos.add(comando);
                comandoListAdapter.set(comandos);
                gridView.setAdapter(comandoListAdapter);
                repetir = 1;
                break;

            case 7:
                comando = new Comando(false);
                comando.setLetra("O");
                comando.setImagem(R.drawable.ic_loop_start);
                comando.setRepetir(repetir);
                comandos.add(comando);
                comandoListAdapter.set(comandos);
                gridView.setAdapter(comandoListAdapter);
                break;

            case 8:
                comando = new Comando(false);
                comando.setLetra("P");
                comando.setImagem(R.drawable.ic_loop_finish);
                comandos.add(comando);
                comandoListAdapter.set(comandos);
                gridView.setAdapter(comandoListAdapter);
                break;

            case 9:
                comando = new Comando(false);
                comando.setLetra("F");
                comando.setImagem(R.drawable.ic_finish);
                comandos.add(comando);
                comandoListAdapter.set(comandos);
                gridView.setAdapter(comandoListAdapter);
                break;

            case 10:
                comandos.clear();
                comandoListAdapter.set(comandos);
                gridView.setAdapter(comandoListAdapter);
                break;

            default:
                break;
        }


        if (mSpeechRecognizer != null) {
            //mSpeechRecognizer.destroy();
            mSpeechRecognizer.stopListening();
            //mSpeechRecognizer = null;
        }

        return retString;
    }

    class SpeechListener implements RecognitionListener {
        public void onBufferReceived(byte[] buffer) {
            Log.d(TAG, "buffer recieved ");
        }

        public void onError(int error) {
            //if critical error then exit
            if (error == SpeechRecognizer.ERROR_CLIENT || error == SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS) {
                Log.d(TAG, "client error");
            }
            //else ask to repeats
            else {
                Log.d(TAG, "other error");
                mSpeechRecognizer.stopListening();
            }
        }

        public void onEvent(int eventType, Bundle params) {
            Log.d(TAG, "onEvent");
        }

        public void onPartialResults(Bundle partialResults) {
            Log.d(TAG, "partial results");
        }

        public void onReadyForSpeech(Bundle params) {
            Log.d(TAG, "on ready for speech");
        }

        public void onResults(Bundle results) {
            mSpeechRecognizer.stopListening();
            Log.d(TAG, "on results");
            ArrayList<String> matches = null;
            if (results != null) {
                matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null) {
                    Log.d(TAG, "results are " + matches.toString());
                    final ArrayList<String> matchesStrings = matches;
                    processCommand(matchesStrings);
                }
            }

        }

        public void onRmsChanged(float rmsdB) {
            //			Log.d(TAG, "rms changed");
        }

        public void onBeginningOfSpeech() {
            Log.d(TAG, "speach begining");
        }


        public void onEndOfSpeech() {
            Log.d(TAG, "speach done");
            mSpeechRecognizer.stopListening();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CÓDIGO_PARA_ATIVAÇÃO_DO_BLUETOOTH:
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(getApplicationContext(), "Bluetooth foi ativado", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Bluetooth não foi ativado", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }


    @Override
    protected void onDestroy() {

        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public static void removerComando(Context context, int pos) {

        comandos.remove(pos);
        comandoListAdapter.set(comandos);
        gridView.setAdapter(comandoListAdapter);

    }

    @Override
    public void onBackPressed() {

        if (gridView.isEditMode()) {
            gridView.stopEditMode();
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_conectar) {

            if (BluetoothAdapter.checkBluetoothAddress(endereco_MAC_do_Bluetooth_Remoto)) {
                dispositivoBluetoohRemoto = meuBluetoothAdapter.getRemoteDevice(endereco_MAC_do_Bluetooth_Remoto);
            } else {
                Toast.makeText(getApplicationContext(), "Endereço MAC do dispositivo Bluetooth remoto não é válido",
                        Toast.LENGTH_SHORT).show();
            }
            try {
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    return true;
                }
                bluetoothSocket = dispositivoBluetoohRemoto.createInsecureRfcommSocketToServiceRecord(MEU_UUID);
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    return true;
                }
                bluetoothSocket.connect();
                Toast.makeText(getApplicationContext(), "Conectado", Toast.LENGTH_SHORT).show();
            } catch(IOException e){
                Log.e("ERRO AO CONECTAR", "O erro foi" + e.getMessage());
                Toast.makeText(getApplicationContext(), "Conexão não foi estabelecida", Toast.LENGTH_SHORT).show();
            }
            try {
                outStream = bluetoothSocket.getOutputStream();
            } catch (IOException e) {
                errorExit("Fatal Error", "In onResume() and output stream creation failed:" + e.getMessage() + ".", this);
            }
            return true;
        }

        if( id == R.id.action_desconectar ) {

            if(bluetoothSocket != null) {
                try{
                    // Immediately close this socket, and release all associated resources.
                    bluetoothSocket.close();
                    bluetoothSocket = null;
                    Toast.makeText(getApplicationContext(), "Conexão encerrada", Toast.LENGTH_SHORT).show();
                } catch(IOException e){
                    Log.e("ERRO AO DESCONECTAR", "O erro foi" + e.getMessage());
                    Toast.makeText(getApplicationContext(), "Erro - A conexão permanece estabelecida", Toast.LENGTH_SHORT).show();
                }
            } else{
                Toast.makeText(getApplicationContext(), "Não há nenhuma conexão estabelecida a ser desconectada",
                        Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        if( id == R.id.action_configurar ) {

            startActivity( new Intent( getApplicationContext(), ConfigurarActivity.class ) );

            return true;
        }

        if( id == R.id.action_sortear ) {

            startActivity( new Intent( getApplicationContext(), SorteioActivity.class ) );

            return true;
        }

        if( id == R.id.action_linguagem ) {

            switch( language ){

                case PORTUGUES:
                    tts.setLanguage( Locale.ENGLISH );
                    language = Language.INGLES;
                    break;

                case INGLES:
                    tts.setLanguage( Locale.getDefault() );
                    language = Language.PORTUGUES;
                    break;

            }

            return true;
        }

        return super.onOptionsItemSelected( item );
    }

    private static void errorExit( String title, String message, Activity activity ){
        Toast msg = Toast.makeText(activity.getApplicationContext(),
                title + " - " + message, Toast.LENGTH_SHORT);
        msg.show();
        activity.finish();
    }

    public static void sendData(String message, Activity activity) {
        byte[] msgBuffer = message.getBytes( );
        try {
            outStream.write(msgBuffer);
        } catch (IOException e) {
            String msg = "In onResume() and an exception occurred during write: " + e.getMessage();
            if (endereco_MAC_do_Bluetooth_Remoto.equals("00:00:00:00:00:00"))
                msg = msg + ".\n\nUpdate your server address to the correct address in the java code";
            msg = msg + ".\n\nCheck that the SPP UUID: " + MEU_UUID.toString() + " exists on server.\n\n";
            errorExit("Fatal Error", msg, activity);
        }
    }

    public int verificarLinguagem( ){

        Automato automato = new Automato( );
        Transicao transicao = new Transicao(  );
        int erro = 0;
        int origem = 0;
        int destino = 1;

        automato.setEstados( 0, new Estado( 0, "q0" ) );
        automato.setEstados( 1, new Estado( 1, "q1" ) );
        automato.setEstados( 2, new Estado( 2, "q2" ) );
        automato.setEstados( 3, new Estado( 3, "q3" ) );

        automato.setEstadoInicial( new Estado( 0, "q0" ) );
        automato.setEstadoFinal( new Estado( 3, "q3" ) );

        automato.setTransicoes( new Transicao(
                automato.getEstados().get( 0 ),
                automato.getEstados().get( 1 ),
                "S",
                "V",
                "S")
        );

        automato.setTransicoes( new Transicao(
                automato.getEstados().get( 1 ),
                automato.getEstados().get( 1 ),
                "C",
                "V",
                "V")
        );

        automato.setTransicoes( new Transicao(
                automato.getEstados().get( 1 ),
                automato.getEstados().get( 1 ),
                "B",
                "V",
                "V")
        );

        automato.setTransicoes( new Transicao(
                automato.getEstados().get( 1 ),
                automato.getEstados().get( 1 ),
                "L",
                "V",
                "V")
        );

        automato.setTransicoes( new Transicao(
                automato.getEstados().get( 1 ),
                automato.getEstados().get( 1 ),
                "R",
                "V",
                "V")
        );

        automato.setTransicoes( new Transicao(
                automato.getEstados().get( 1 ),
                automato.getEstados().get( 1 ),
                "E",
                "V",
                "V")
        );

        automato.setTransicoes( new Transicao(
                automato.getEstados().get( 1 ),
                automato.getEstados().get( 1 ),
                "D",
                "V",
                "V")
        );

        automato.setTransicoes( new Transicao(
                automato.getEstados().get( 1 ),
                automato.getEstados().get( 1 ),
                "O",
                "V",
                "O")
        );

        automato.setTransicoes( new Transicao(
                automato.getEstados().get( 1 ),
                automato.getEstados().get( 2 ),
                "P",
                "O",
                "V")
        );

        automato.setTransicoes( new Transicao(
                automato.getEstados().get( 2 ),
                automato.getEstados().get( 1 ),
                "O",
                "V",
                "O")
        );

        automato.setTransicoes( new Transicao(
                automato.getEstados().get( 2 ),
                automato.getEstados().get( 2 ),
                "C",
                "V",
                "V")
        );

        automato.setTransicoes( new Transicao(
                automato.getEstados().get( 2 ),
                automato.getEstados().get( 2 ),
                "B",
                "V",
                "V")
        );

        automato.setTransicoes( new Transicao(
                automato.getEstados().get( 2 ),
                automato.getEstados().get( 2 ),
                "L",
                "V",
                "V")
        );

        automato.setTransicoes( new Transicao(
                automato.getEstados().get( 2 ),
                automato.getEstados().get( 2 ),
                "R",
                "V",
                "V")
        );

        automato.setTransicoes( new Transicao(
                automato.getEstados().get( 2 ),
                automato.getEstados().get( 2 ),
                "E",
                "V",
                "V")
        );

        automato.setTransicoes( new Transicao(
                automato.getEstados().get( 2 ),
                automato.getEstados().get( 2 ),
                "D",
                "V",
                "V")
        );

        automato.setTransicoes( new Transicao(
                automato.getEstados().get( 2 ),
                automato.getEstados().get( 2 ),
                "P",
                "O",
                "V")
        );

        automato.setTransicoes( new Transicao(
                automato.getEstados().get( 1 ),
                automato.getEstados().get( 3 ),
                "F",
                "S",
                "V")
        );

        automato.setTransicoes( new Transicao(
                automato.getEstados().get( 2 ),
                automato.getEstados().get( 3 ),
                "F",
                "S",
                "V")
        );

        for( int i = 0; i < comandos.size(); i++ ) {

            transicao = automato.getTransicao( comandos.get( i ).getLetra( ), automato.getEstados( ).get( origem ), automato.getEstados( ).get( destino ) );

            if( transicao != null ) {

                if( transicao.getLeitorPilha().equals( "V" ) ){

                    if( transicao.getEscritorPilha().equals( "V" ) ){

                        origem = transicao.getDestino().getId();

                    }
                    else{

                        automato.empilhar( transicao.getEscritorPilha() );
                        origem = transicao.getDestino().getId();

                    }

                }
                else{

                    if( automato.desempilhar( transicao.getLeitorPilha() ) ){

                        origem = transicao.getDestino().getId();
                        destino = transicao.getDestino().getId();

                    }
                    else {

                        erro = origem;
                        return erro;

                    }
                }

            }
            else{

                erro = origem;
                return erro;

            }
        }

        if( automato.getPilha().isEmpty() && origem == automato.getEstadoFinal().getId() && destino == automato.getEstadoFinal().getId() ){

            return -1;

        }
        else {

            erro = automato.getEstadoFinal().getId();
            return erro;

        }

    }

    @Override
    public void onInit( int status ) {

        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage( Locale.getDefault() );

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
                speakOut("");
            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }

    }

    private void speakOut(String text) {

        tts.speak( text, TextToSpeech.QUEUE_FLUSH, null );

    }

    private class FuncionarRobo extends AsyncTask< Void, Void, Void > {

        private List<Comando> comandos;
        private List<Integer> aux;

        public FuncionarRobo( List< Comando > comandos ) {

            this.comandos = comandos;
            aux = new ArrayList<>(  );
            for( Comando com : comandos ){

                aux.add( com.getRepetir() );

            }

        }

        @Override
        protected Void doInBackground( Void... params ) {

            int i = 0; int time = 1000;
            Stack<Integer> pilhaPosicao = new Stack<>();

            while( i < comandos.size() ) {

                for( Comando com : comandos ){

                    com.setAtivo( false );

                }

                if( comandos.get( i ).getLetra().equals( "O" ) ){
                    pilhaPosicao.push( i );
                    comandos.get( i ).setAtivo( true );

                    try {
                        Thread.sleep( time );
                    }
                    catch( InterruptedException e ) {
                        e.printStackTrace( );
                    }
                    publishProgress( );

                    i++;

                }
                else if( comandos.get( i ).getLetra().equals( "P" ) ){
                    int pos = pilhaPosicao.peek();
                    pilhaPosicao.pop();
                    comandos.get( pos ).setRepetir( comandos.get( pos ).getRepetir() - 1 );
                    if( comandos.get( pos ).getRepetir() == 0 ){

                        comandos.get( pos ).setRepetir( aux.get( pos ) );
                        i++;

                    }
                    else{

                        i = pos;

                    }

                }
                else{

                    comandos.get( i ).setAtivo( true );
                    time = 1000;

                    switch( comandos.get( i ).getLetra() ){

                        case "C":
                            if( bluetoothSocket != null ) {
                                sendData( "C", MainActivity.this );
                                time = tempoFrente;
                            }else
                                time = 1000;
                            break;

                        case "B":
                            if( bluetoothSocket != null ) {
                                sendData( "B", MainActivity.this );
                                time = tempoTras;
                            }else
                                time = 1000;
                            break;

                        case "R":
                            if( bluetoothSocket != null ) {
                                sendData( "R", MainActivity.this );
                                time = tempoVirar;
                            }else
                                time = 1000;
                            break;

                        case "L":
                            if( bluetoothSocket != null ) {
                                sendData( "L", MainActivity.this );
                                time = tempoVirar;
                            }else
                                time = 1000;
                            break;

                        case "D":
                            if( bluetoothSocket != null ) {
                                sendData( "D", MainActivity.this );
                                time = ( tempoVirar / 90 ) * comandos.get( i ).getRepetir( );
                            }
                            else
                                time = 1000;
                            break;

                        case "E":
                            if( bluetoothSocket != null ) {
                                sendData( "E", MainActivity.this );
                                time = ( tempoVirar / 90 ) * comandos.get( i ).getRepetir( );
                            }else
                                time = 1000;
                            break;

                        case "F":
                            if( bluetoothSocket != null ) {
                                sendData( "F", MainActivity.this );
                                time = 1000;
                            }else
                                time = 1000;
                            break;

                    }

                    try {
                        Thread.sleep( time );
                        if( bluetoothSocket != null ) {
                            Thread.sleep( 1000 );
                        }
                    }
                    catch( InterruptedException e ) {
                        e.printStackTrace( );
                    }
                    publishProgress( );

                    i++;

                }

            }

            return null;

        }

        @Override
        protected void onProgressUpdate( Void... values ) {

            comandoListAdapter.set( comandos );
            gridView.setAdapter( comandoListAdapter );

        }

    }
}