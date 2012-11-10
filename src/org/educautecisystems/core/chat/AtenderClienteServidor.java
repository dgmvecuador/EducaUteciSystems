/*
 *  AtenderClienteServidor.java
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

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author dgmv
 */
public class AtenderClienteServidor extends Thread {
	/* Flujos del cliente */
	private Socket clienteSocket;
	private ObjectInputStream entrada;
	private ObjectOutputStream salida;

	private boolean continuar;

	public AtenderClienteServidor( Socket clienteSocket ) {
		this.clienteSocket = clienteSocket;
		continuar = true;
	}

	@Override
	public void run () {
		try {
			if ( clienteSocket == null )
				return;

			while ( continuar ) {
				
			}
		} catch ( Exception e ) {
			
		}
	}

	public boolean estaCorriendo() {
		return continuar;
	}

	public void detenerCliente () {
		continuar = false;

		try {
			entrada.close();
			salida.close();
			clienteSocket.close();
		} catch ( Exception e ) {
			
		}
	}
}
