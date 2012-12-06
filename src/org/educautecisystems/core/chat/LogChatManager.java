/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.educautecisystems.core.chat;

/**
 *
 * @author dgmv
 */
public interface LogChatManager {
	public void logInfo(String txt);
	public void logError(String txt);
	public void logWarning(String txt);
}
