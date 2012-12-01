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
import java.util.ArrayList;
import org.educautecisystems.core.chat.elements.ChatConstants;
import org.educautecisystems.core.chat.elements.MessageHeaderParser;
import org.educautecisystems.core.chat.elements.UserChat;

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

	public AtenderClienteServidor(Socket clienteSocket) {
		this.clienteSocket = clienteSocket;
		continuar = true;
	}
	
	public boolean esCliente( int idUsuario ) {
		return true;
	}

	@Override
	public void run() {
		try {
			if (clienteSocket == null) {
				return;
			}
			
			MessageHeaderParser header = MessageHeaderParser.parseMessageHeader(entrada);
			if (header.getCommand().equals(ChatConstants.CHAT_HEADER_MAIN_COMMAND)) {
				if (!header.getVar(ChatConstants.LABEL_COMMAND).equals(ChatConstants.COMMAND_LOGIN)) {
					procesarRequerimiento(header);
					return;
				} else {
					String realName = header.getVar(ChatConstants.LABEL_REAL_NAME);
					String nickName = header.getVar(ChatConstants.LABEL_NICKNAME);
				}
			}
			
			while (continuar) {
				
			}
			
		} catch (Exception e) {
		}
	}

	private void procesarRequerimiento(MessageHeaderParser header) {
		/* List of users */
		if ( header.getCommand().equals(ChatConstants.COMMAND_GET_USERS) ) {
			String userToken =	header.getVar(ChatConstants.LABEL_USER_TOKEN);
			String format =		header.getVar(ChatConstants.LABEL_FORMAT);
			
			/* Must be a valid token */
			if ( !ServidorChat.testToken(userToken) ) {
				detenerCliente();
				return;
			}
			
			/* Only XML is valid */
			if ( !format.equals("XML") ) {
				detenerCliente();
				return;
			}
			
			ArrayList<UserChat> users = ServidorChat.getUserList();
			byte [] xmlUsers = UserChat.generateXMLFromList(users).getBytes();
			long size = xmlUsers.length;
			
			/* Genering response */
			String headerResponse = "";
			headerResponse += 
					generateHeaderValue(ChatConstants.CHAT_HEADER_RESPONSE_COMMAND,
					ChatConstants.RESPONSE_OK);
			headerResponse +=
					generateHeaderValue(ChatConstants.LABEL_CONTENT_LENGHT, ""+size);
			headerResponse += ChatConstants.CHAT_END_HEADER;
			headerResponse += xmlUsers;
			
			detenerCliente();
			return;
		}
	}
	
	private String generateHeaderValue( String name, String val ) {
		return name + ": " + val + "\n";
	}

	public boolean estaCorriendo() {
		return continuar;
	}

	public void detenerCliente() {
		continuar = false;

		try {
			entrada.close();
			salida.close();
			clienteSocket.close();
		} catch (Exception e) {
		}
	}
}
