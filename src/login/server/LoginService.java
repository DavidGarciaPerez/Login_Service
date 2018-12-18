package login.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.StringTokenizer;

public class LoginService extends Thread {
	private DataInputStream in;
	private DataOutputStream out;
	private Socket tcpSocket;

	private static String DELIMITER = "#";
	private static HashMap<String, String> usuarios = createUsuarios();

	public LoginService(Socket socket) {
		try {
			this.tcpSocket = socket;
			this.in = new DataInputStream(socket.getInputStream());
			this.out = new DataOutputStream(socket.getOutputStream());
			this.start();
		} catch (IOException e) {
			System.err.println("# LoginService - TCPConnection IO error:" + e.getMessage());
		}
	}

	public void run() {
		try {
			String data = this.in.readUTF();
			System.out.println("   - LoginService - Received data from '" + tcpSocket.getInetAddress().getHostAddress()
					+ ":" + tcpSocket.getPort() + "' -> '" + data + "'");
			data = this.login(data);
			this.out.writeUTF(data);
			System.out.println("   - LoginService - Sent data to '" + tcpSocket.getInetAddress().getHostAddress() + ":"
					+ tcpSocket.getPort() + "' -> '" + data.toUpperCase() + "'");
		} catch (EOFException e) {
			System.err.println("   # LoginService - TCPConnection EOF error" + e.getMessage());
		} catch (IOException e) {
			System.err.println("   # LoginService - TCPConnection IO error:" + e.getMessage());
		} finally {
			try {
				tcpSocket.close();
			} catch (IOException e) {
				System.err.println("   # LoginService - TCPConnection IO error:" + e.getMessage());
			}
		}
	}

	public String login(String msg) {
		String translation = null;

		if (msg != null && !msg.trim().isEmpty()) {
			try {
				StringTokenizer tokenizer = new StringTokenizer(msg, DELIMITER);
				String email = tokenizer.nextToken();
				String password = tokenizer.nextToken();
				// Comprobamos si algún parámetro ha fallado:
				if (email != null && password != null && !password.trim().isEmpty()) {
					// Si todo ha ido bien entonces comprobamos email y contraseña:
					if (usuarios.get(email).equals(password)) {
						// Logeo correcto:
						System.out.println("EL USUARIO : " + email + " SE HA CONECTADO CORRECTAMENTE!");
						translation = "OK";
					}
				} else {
					System.err.println("PARAMETROS ERR!");
				}
			} catch (Exception e) {
				System.err.println("   # LoginService - LoginService API error:" + e.getMessage());
				translation = null;
			}
		}

		return (translation == null) ? "ERR" : "OK" + DELIMITER + translation;
	}

	private static HashMap<String, String> createUsuarios() {
		HashMap<String, String> dev = new HashMap<String, String>();
		dev.put("david.g.p@opendeusto.es", "12345");
		dev.put("arelloso@opendeusto.es", "12345");
		dev.put("joseba.garcia@opendeusto.es", "12345");
		dev.put("mvillate@opendeusto.es", "12345");
		dev.put("amartinez@opendeusto.es", "12345");
		dev.put("roberto@opendeusto.es", "12345");
		return dev;
	}
}
