package sibanez.mov.urjc.es.timetable;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    /**
     * Controles
     */
    private Button button, volver;
    private EditText user;
    private EditText password;
    private TextView editText, frase_pss, frase_user;
    private Context context = this;
    private SubjectBBDD bbdd = null;
    private String email = "";
    private Boolean connect = false;


    /**
     * Puerto
     */
    private static final int SERVERPORT = 5000;
    /**
     * HOST
     */
    private static final String ADDRESS = "10.0.2.2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = ((Button) findViewById(R.id.button));
        user = ((EditText) findViewById(R.id.editTextUser));
        password = ((EditText) findViewById(R.id.editTextPass));
        editText = ((TextView) findViewById(R.id.editText));
        frase_pss = ((TextView) findViewById(R.id.Passwd));
        frase_user = ((TextView) findViewById(R.id.User));
        volver = ((Button) findViewById(R.id.volver));
        volver.setVisibility(View.INVISIBLE);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (password.getText().toString().length() > 0 && user.getText().toString().length() > 0) {
                    MyATaskCliente myATaskYW = new MyATaskCliente();
                    myATaskYW.execute(user.getText().toString(), password.getText().toString());
                    user.setVisibility(View.INVISIBLE);
                    password.setVisibility(View.INVISIBLE);
                    button.setVisibility(View.INVISIBLE);
                    frase_pss.setVisibility(View.INVISIBLE);
                    frase_user.setVisibility(View.INVISIBLE);
                    volver.setVisibility(View.VISIBLE);

                    // Ocultar el teclado
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }

                } else {
                    Toast.makeText(context, "Introduzca al Usuario y su contraseña", Toast.LENGTH_LONG).show();
                }
            }
        });

        volver.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                user.setVisibility(View.VISIBLE);
                password.setVisibility(View.VISIBLE);
                button.setVisibility(View.VISIBLE);
                frase_pss.setVisibility(View.VISIBLE);
                frase_user.setVisibility(View.VISIBLE);
                volver.setVisibility(View.INVISIBLE);
                editText.setText("");

                // Mostrar el teclado
                user.requestFocus(); //Asegurar que user tiene focus
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(user, InputMethodManager.SHOW_IMPLICIT);


            }
        });
    }

    /**
     * Clase para interactuar con el servidor
     */
    class MyATaskCliente extends AsyncTask<String, Void, String> {

        /**
         * Ventana que bloqueara la pantalla del movil hasta recibir respuesta del servidor
         */
        ProgressDialog progressDialog;

        /**
         * muestra una ventana emergente
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            String s = "";

            if (bbdd != null) {
                int tamanio = bbdd.getSubjects().size();
                for (int i = 0; i < tamanio; i++) {
                    if (user.getText().toString().equals(bbdd.getSubjects().get(i).getName()) &&
                            email.equals(bbdd.getSubjects().get(i).getEmail())) {
                        s += bbdd.getSubject(i).getSubject() + ":\n\t\tdías: " + bbdd.getSubject(i).getDays() + "\n\t\thora:" + bbdd.getSubject(i).getTime() + "\n\n";
                    }
                }
                editText.setText(s);
            }

            if (s.equals("")) {
                progressDialog = new ProgressDialog(context);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setTitle("Connecting to server");
                progressDialog.setMessage("Please wait...");
                progressDialog.show();
            }
        }

        /**
         * Se conecta al servidor y trata resultado
         */
        @Override
        protected String doInBackground(String... values) {

            try {
                //Se conecta al servidor
                InetAddress serverAddr = InetAddress.getByName(ADDRESS);
                Log.i("I/TCP Client", "Connecting...");
                Socket socket = new Socket(serverAddr, SERVERPORT);
                Log.i("I/TCP Client", "Connected to server");

                //envia peticion de cliente
                Log.i("I/TCP Client", "Send data to server");
                PrintStream output = new PrintStream(socket.getOutputStream());
                String request = values[0] + ";" + values[1];
                output.println(request);

                //recibe respuesta del servidor y formatea a String
                Log.i("I/TCP Client", "Received data to server");
                InputStream stream = socket.getInputStream();
                byte[] lenBytes = new byte[256];
                stream.read(lenBytes, 0, 256);
                String received = new String(lenBytes, "UTF-8").trim();
                Log.i("I/TCP Client", "Received " + received);
                Log.i("I/TCP Client", "");

                //cierra conexion
                socket.close();
                return received;
            } catch (UnknownHostException ex) {
                Log.e("E/TCP Client", "" + ex.getMessage());
                return ex.getMessage();
            } catch (IOException ex) {
                Log.e("E/TCP Client", "" + ex.getMessage());
                return ex.getMessage();
            }
        }

        /**
         * Oculta ventana emergente y muestra resultado en pantalla
         */
        @Override
        protected void onPostExecute(String value) {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            String s = "Asignaturas de " + user.getText() + ": \n\n";

            if (value.equals("fail")) {
                editText.setText("Se ha introducido mal el usuario o la contraseña");
            } else {
                bbdd = splitCampos(value);

                for (int i = 0; i < bbdd.getSubjects().size(); i++) {
                    s += bbdd.getSubject(i).getSubject() + ":\n\t\tdías: " + bbdd.getSubject(i).getDays() + "\n\t\thora:" + bbdd.getSubject(i).getTime() + "\n\n";
                }
                editText.setText(s);
            }
        }

        private SubjectBBDD splitCampos(String args) {
            String[] campos = args.split("\\|");
            String[] campos_aux;
            bbdd = new SubjectBBDD(getApplicationContext());
            int tamanio;

            // Vacio la BBDD
            if (bbdd != null && connect) {
                tamanio = bbdd.getSubjects().size();
                for (int i = 0; i < tamanio; i++) {
                    bbdd.deleteSubject(i);
                }
            }

            connect = true;

            for (int i = 0; i < campos.length; i++) {
                campos_aux = campos[i].split("\\;");
                for (int j = 0; j < campos_aux.length; j++) {
                    Log.i("Campo", j + ": " + campos_aux[j]);
                }
                bbdd.deleteSubject(i);
                bbdd.insertSubject(i, campos_aux[0], campos_aux[1], campos_aux[2], campos_aux[3], campos_aux[4], campos_aux[5]);
                email = bbdd.getSubject(i).getEmail();
            }
            Log.d("TOTAL", Integer.toString(bbdd.getSubjects().size()));

            return bbdd;
        }
    }
}