/*
 *  ServidorChat.java
 *  Copyright (C) 2012  Diego Estévez <dgmvecuador@gmail.com>
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.educautecisystems.core.chat;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author dgmv
 */
public class ServidorChat extends Thread {
	/* Datos escenciales para el servidor */
	private ServerSocket socketServidor;
	private ArrayList<AtenderClienteServidor> clientes;
	private Socket cliente;
	private boolean continuar;

	/* Valores constantes */
	public static final int PUERTO_SERVIDOR = 7586;

	public ServidorChat() {
		continuar = true;
		clientes = new ArrayList<AtenderClienteServidor>();
	}

	@Override
	public void run () {
		try {
			socketServidor = new ServerSocket(PUERTO_SERVIDOR);

			while ( continuar ) {
				cliente = socketServidor.accept();
				AtenderClienteServidor atencionCliente = new AtenderClienteServidor(cliente);
				atencionCliente.start();
				insertarCliente(atencionCliente);
				depurarListaClientes();
			}
		} catch ( Exception e ) {
			
		}
	}

	private void insertarCliente ( AtenderClienteServidor atencionCliente ) {
		clientes.add(atencionCliente);
	}

	private void quitarCliente( AtenderClienteServidor atencionCliente) {
		clientes.remove(atencionCliente);
	}

	public void depurarListaClientes () {
		for( AtenderClienteServidor atencionCliente : clientes ) {
			if ( !atencionCliente.estaCorriendo() ) {
				quitarCliente(atencionCliente);
			}
		}
	}

	public void detenerServidor() {
		continuar = false;
		try {
			socketServidor.close();
		} catch ( Exception e ) {
			
		}
	}
}
