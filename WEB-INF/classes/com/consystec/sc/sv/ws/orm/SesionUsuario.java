package com.consystec.sc.sv.ws.orm;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class SesionUsuario {
    public static final String N_TABLA = "TC_SC_SESION";
    public static final String N_TABLA_ID = "TCSCSESIONID";
    public static final String SEQUENCE = "TC_SC_SESION_SQ.nextval";

    public final static String CAMPO_TCSESIONID = "tcscsesionid";
    public final static String CAMPO_USERNAME = "username";
    public final static String CAMPO_TCSCCATPAISID = "tcsccatpaisid";
    public final static String CAMPO_TOKEN = "token";
    public final static String CAMPO_ESTADO = "estado";
    public final static String CAMPO_COD_DISPOSITIVO = "cod_dispositivo";
    public final static String CAMPO_CREADO_EL = "creado_el";
    public final static String CAMPO_CREADO_POR = "creado_por";
    public final static String CAMPO_MODIFICADO_EL = "modificado_el";

    private BigDecimal tcscsesionid;
    private String username;
    private BigDecimal tcsccatpaisid;
    private String token;
    private BigDecimal estado;
    private Timestamp creado_el;
    private String creado_por;
    private Timestamp modificado_el;

    public BigDecimal getTcscsesionid() {
        return tcscsesionid;
    }

    public void setTcscsesionid(BigDecimal tcscsesionid) {
        this.tcscsesionid = tcscsesionid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public BigDecimal getTcsccatpaisid() {
        return tcsccatpaisid;
    }

    public void setTcsccatpaisid(BigDecimal tcsccatpaisid) {
        this.tcsccatpaisid = tcsccatpaisid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public BigDecimal getEstado() {
        return estado;
    }

    public void setEstado(BigDecimal estado) {
        this.estado = estado;
    }

    public Timestamp getCreado_el() {
        return creado_el;
    }

    public void setCreado_el(Timestamp creado_el) {
        this.creado_el = creado_el;
    }

    public String getCreado_por() {
        return creado_por;
    }

    public void setCreado_por(String creado_por) {
        this.creado_por = creado_por;
    }

    public Timestamp getModificado_el() {
        return modificado_el;
    }

    public void setModificado_el(Timestamp modificado_el) {
        this.modificado_el = modificado_el;
    }

}
