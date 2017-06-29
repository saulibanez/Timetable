package MainServer;
//https://carmazone.wordpress.com/2014/09/07/leer-json-desde-directorio-raw-en-android/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;

public class MainServer {

	private final static int PORT = 5000;

	public static void main(String[] args) {
		Json j = new Json();

		try {
			// Socket de servidor para esperar peticiones de la red
			ServerSocket serverSocket = new ServerSocket(PORT);
			System.out.println("Servidor> Servidor iniciado");
			System.out.println("Servidor> En espera de cliente...");
			// Socket de cliente
			Socket clientSocket;
			while (true) {
				// en espera de conexion, si existe la acepta
				clientSocket = serverSocket.accept();
				// Para leer lo que envie el cliente
				BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				// para imprimir datos de salida
				PrintStream output = new PrintStream(clientSocket.getOutputStream());
				// se lee peticion del cliente
				String request = input.readLine();
				System.out.println("Cliente> petición [" + request + "]");
				// se procesa la peticion y se espera resultado
				String[] strOutput = splitCampos(request);
				String student = j.readJson(strOutput[0], strOutput[1]);
				// Se imprime en consola "servidor"
				System.out.println("Servidor> Resultado de petición");
				System.out.println("Servidor> \"" + student + "\"");
				// se imprime en cliente
				output.flush();// vacia contenido
				output.println(student);
				// cierra conexion
				clientSocket.close();
			}
		} catch (IOException ex) {
			System.err.println(ex.getMessage());
		}
	}

	public static String[] splitCampos(String args) {
		String[] campos = args.split(";");
		for (int i = 0; i < campos.length; i++) {
			System.out.println(campos[i]);
		}
		return campos;
	}
}
