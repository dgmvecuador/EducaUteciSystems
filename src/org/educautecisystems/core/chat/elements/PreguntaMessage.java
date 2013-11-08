/*
 *  PreguntaMessage.java
 *  Copyright (C) 2013  Diego Estévez <dgmvecuador@gmail.com>
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
public class PreguntaMessage {
    private ReceptorPregunta receptorPregunta;
    private int idPregunta;
    private String pregunta;
    
    public PreguntaMessage () {
        idPregunta = 0;
        pregunta = "";
        receptorPregunta = null;
    }
    
    public PreguntaMessage( int idPregunta, String pregunta, ReceptorPregunta receptorPregunta ) {
        this.idPregunta = idPregunta;
        this.pregunta = pregunta;
        this.receptorPregunta = receptorPregunta;
    }

    /**
     * @return the idPregunta
     */
    public int getIdPregunta() {
        return idPregunta;
    }

    /**
     * @param idPregunta the idPregunta to set
     */
    public void setIdPregunta(int idPregunta) {
        this.idPregunta = idPregunta;
    }

    /**
     * @return the pregunta
     */
    public String getPregunta() {
        return pregunta;
    }

    /**
     * @param pregunta the pregunta to set
     */
    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    /**
     * @return the receptorPregunta
     */
    public ReceptorPregunta getReceptorPregunta() {
        return receptorPregunta;
    }

    /**
     * @param receptorPregunta the receptorPregunta to set
     */
    public void setReceptorPregunta(ReceptorPregunta receptorPregunta) {
        this.receptorPregunta = receptorPregunta;
    }
}
