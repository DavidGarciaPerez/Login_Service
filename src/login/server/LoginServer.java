package login.server;

import java.io.IOException;
import java.net.ServerSocket;

public class LoginServer {
	private static int numClients = 0;

	public static void main(String args[]) {
		if (args.length < 1) {
			System.err.println(" # Usage: LoginServer [PORT]");
			System.exit(1);
		}

		// args[1] = Server socket port
		int serverPort = Integer.parseInt(args[0]);

		try (ServerSocket tcpServerSocket = new ServerSocket(serverPort);) {
			System.out.println(
					" - LoginServer: Waiting for connections '" + tcpServerSocket.getInetAddress().getHostAddress()
							+ ":" + tcpServerSocket.getLocalPort() + "' ...");

			while (true) {
				new LoginService(tcpServerSocket.accept());
				System.out.println(" - LoginServer: New client connection accepted. Client number: " + ++numClients);
			}
		} catch (IOException e) {
			System.err.println("# LoginServer: IO error:" + e.getMessage());
		}
	}
}
