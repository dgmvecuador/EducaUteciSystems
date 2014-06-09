/*
 *  Chat.java
 *  Copyright (C) 2014  Guillermo Pazos <shadowguiller@hotmail.com>
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

package org.educautecisystems.intefaz;

/**
 *
 * @author Guillermo
 */
public class TareaSubida {
    private String direccionArchivo;
    private String nombreArchivo;
    private String nombreReal;
    private String fecha;
    private long size;
    
    public TareaSubida() {
        this.direccionArchivo = "";
        this.nombreReal = "";
        this.size = 0;
    }

    /**
     * @return the direccionArchivo
     */
    public String getDireccionArchivo() {
        return direccionArchivo;
    }

    /**
     * @param direccionArchivo the direccionArchivo to set
     */
    public void setDireccionArchivo(String direccionArchivo) {
        this.direccionArchivo = direccionArchivo;
    }

    /**
     * @return the nombreReal
     */
    public String getNombreReal() {
        return nombreReal;
    }

    /**
     * @param nombreReal the nombreReal to set
     */
    public void setNombreReal(String nombreReal) {
        this.nombreReal = nombreReal;
    }

    /**
     * @return the size
     */
    public long getSize() {
        return size;
    }

    /**
     * @param size the size to set
     */
    public void setSize(long size) {
        this.size = size;
    }
    
    public String toString() {
        return "<html><b>"+nombreReal+"</b> (<i>"+formatearFecha()+") </i>"+nombreArchivo.replaceAll(" ", "&nbsp;")+"</html>";
    }
    
    private String formatearFecha() {
        if ( fecha == null || !fecha.matches("(\\d{2,})(\\d{2,})(\\d{4,})(\\d{2,})(\\d{2,})") ) {
            return "31/12/9999 23:59";
        }
        
        /* Obtener los datos de la fecha. */
        String dia = fecha.replaceAll("(\\d{2,})(\\d{2,})(\\d{4,})(\\d{2,})(\\d{2,})", "$1");
        String mes = fecha.replaceAll("(\\d{2,})(\\d{2,})(\\d{4,})(\\d{2,})(\\d{2,})", "$2");
        String anio = fecha.replaceAll("(\\d{2,})(\\d{2,})(\\d{4,})(\\d{2,})(\\d{2,})", "$3");
        String hora = fecha.replaceAll("(\\d{2,})(\\d{2,})(\\d{4,})(\\d{2,})(\\d{2,})", "$4");
        String minuto = fecha.replaceAll("(\\d{2,})(\\d{2,})(\\d{4,})(\\d{2,})(\\d{2,})", "$5");
        
        return String.format("%s/%s/%s %s:%s", dia, mes, anio, hora, minuto);
    }

    /**
     * @return the fecha
     */
    public String getFecha() {
        return fecha;
    }

    /**
     * @param fecha the fecha to set
     */
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    /**
     * @return the nombreArchivo
     */
    public String getNombreArchivo() {
        return nombreArchivo;
    }

    /**
     * @param nombreArchivo the nombreArchivo to set
     */
    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }
}
