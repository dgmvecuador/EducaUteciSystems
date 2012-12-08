/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
