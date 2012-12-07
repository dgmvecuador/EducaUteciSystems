/*
 *  ChatConstants.java
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
public class ChatConstants {
	/* Main Commands */
	public static final String CHAT_HEADER_MAIN_COMMAND =		"CHAT-SERVER 1.0";
	public static final String CHAT_HEADER_RESPONSE_COMMAND =	"Response";
	public static final String CHAT_END_HEADER =				"\r\n";
	
	/* Labels */
	public static final String LABEL_COMMAND =			"Command";
	public static final String LABEL_REAL_NAME =		"Real-Name";
	public static final String LABEL_NICKNAME =			"Nickname";
	public static final String LABEL_USER_TOKEN =		"User-Token";
	public static final String LABEL_FORMAT =			"Format";
	public static final String LABEL_CONTENT_LENGHT =	"Content-Length";
	public static final String LABEL_USER_ID =			"User-ID";
	public static final String LABEL_DESCRIPTION =		"Description";
	
	/* Commands */
	public static final String COMMAND_LOGIN =		"login";
	public static final String COMMAND_GET_USERS =	"getListUsers";
	public static final String COMMAND_LOGOUT =		"logout";
	
	/* Responses */
	public static final String RESPONSE_OK =	"OK";
	public static final String RESPONSE_ERROR = "Error";
}
