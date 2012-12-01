/*
 *  MessageHeaderParser.java
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

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author dgmv
 */
public class MessageHeaderParser {

	private Map vars = new HashMap();
	private String headerText;
	private String command;

	private MessageHeaderParser(String headerText) {
		this.headerText = headerText;
		this.command = null;
		parseMessageHeaderReal();
	}
	
	public String getVar( String varName ) {
		return vars.get(varName).toString();
	}

	private void parseMessageHeaderReal() {
		String[] lines = headerText.split("\n");

		for (String line : lines) {
			if (getCommand() == null) {
				command = line;
				continue;
			}

			String[] info = line.split(":", 2);
			String var = info[1];

			if (var.length() > 1 && var.charAt(0) == ' ') {
				var = var.substring(1, var.length() - 1);
			}

			vars.put(info[0], var);
		}
	}

	public static MessageHeaderParser parseMessageHeader(InputStream entrada) throws IOException {
		StringBuilder headerRAW = new StringBuilder();
		int readByte = entrada.read();
		int lastByte = readByte;

		while (readByte != -1) {
			headerRAW.append((char) readByte);
			if (lastByte == (int) '\n' && readByte == '\n') {
				break;
			}
			lastByte = readByte;
			readByte = entrada.read();
		}

		return new MessageHeaderParser(headerRAW.toString());
	}

	/**
	 * @return the command
	 */
	public String getCommand() {
		return command;
	}
}
