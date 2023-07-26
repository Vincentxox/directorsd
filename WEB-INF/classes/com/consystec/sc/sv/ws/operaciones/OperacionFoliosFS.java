package com.consystec.sc.sv.ws.operaciones;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.consystec.sc.ca.ws.input.general.InputFolioRutaPanel;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.folio.OutputConfiguracionFolioVirtual;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.CurrentClassGetter;

/**
 * @author Victor Cifuentes @ Consystec - 2016
 *
 */
public class OperacionFoliosFS {
private OperacionFoliosFS(){}
    /**
     * Funci\u00F3n que arma el query a utilizar al consultar en m\u00E9todo GET.
     * 
     * @param conn

     * @param input
     * @param metodo
     * @param estadoBaja 
     * @param estadoAlta 
     * @return OutputRemesa
     * @throws SQLException
     */
    public static OutputConfiguracionFolioVirtual doGet(String codArea)
            throws SQLException {
        String nombreMetodo = "doGet";
        String nombreClase = new CurrentClassGetter().getClassName();

        List<InputFolioRutaPanel> list = new ArrayList<InputFolioRutaPanel>();
        Respuesta respuesta;
        OutputConfiguracionFolioVirtual output ;


        respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSOS_OBTENIDOS, null, nombreClase,
                nombreMetodo, null, codArea);
        
        for (int i = 1; i < 4; i++) {
        	InputFolioRutaPanel item = new InputFolioRutaPanel();
            item.setIdFolio(i + "");
            item.setNoInicialFolio(((i + 2) * 7) + "");
            item.setNoFinalFolio(((i + 2) * 7 + 100) + "");
            item.setTipoDocumento("TKT");
            item.setSerie("A" + i);
            item.setEstado("ALTA");
            
            list.add(item);
        }
        
        
        output = new OutputConfiguracionFolioVirtual();
        output.setRespuesta(respuesta);
        output.setFolios(list);

        return output;
    }
    
    /**
     * Funci\u00F3n que arma el query a utilizar al trabajar en m\u00E9todo POST.
     * 
     * @param conn
     * @param input
     * @param estadoBaja
     * @param estadoAlta
     * @return OutputRemesa
     * @throws SQLException
     */
    public static OutputConfiguracionFolioVirtual doPost(String codArea) throws SQLException {
        String nombreMetodo = "doPost";
        String nombreClase = new CurrentClassGetter().getClassName();
        Respuesta respuesta = null;
        OutputConfiguracionFolioVirtual output = new OutputConfiguracionFolioVirtual();;
           
        //TODO Cambiar servicio dummy FS
        respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSO_CREADO, null, nombreClase,
                nombreMetodo, null, codArea);
        output.setRespuesta(respuesta);

        return output;
    }

}
