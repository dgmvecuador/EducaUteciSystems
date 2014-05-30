/*
 *  ChatMessage.java
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
package org.educautecisystems.core.chat.elements;

/**
 *
 * @author dgmv
 */
public class ChatMessage {
	private String	message;
	private int		idUserOrigin;
	private int		idUser;
	
	public ChatMessage () {
		this.message =		"";
		this.idUserOrigin = 0;
		this.idUser	=		0;
	}
	
	public ChatMessage ( String message, int idUserOrigin, int idUser ) {
		this.message =		message;
		this.idUserOrigin = idUserOrigin;
		this.idUser =		idUser;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the idUserOrigin
	 */
	public int getIdUserOrigin() {
		return idUserOrigin;
	}

	/**
	 * @param idUserOrigin the idUserOrigin to set
	 */
	public void setIdUserOrigin(int idUserOrigin) {
		this.idUserOrigin = idUserOrigin;
	}

	/**
	 * @return the idUser
	 */
	public int getIdUser() {
		return idUser;
	}

	/**
	 * @param idUser the idUser to set
	 */
	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}
}
