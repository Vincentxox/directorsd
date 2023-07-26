package com.consystec.sc.sv.ws.orm;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Articulos {
    public static final String N_TABLA = "TES_ARTICULOS_INV";

    public final static String CAMPO_TESARTICULOSINVID = "tesarticulosinvid";
    public final static String CAMPO_COD_ARTICULO = "cod_articulo";
    public final static String CAMPO_TESGRUPOSINVID = "tesgruposinvid";
    public final static String CAMPO_TESMARCASINVID = "tesmarcasinvid";
    public final static String CAMPO_MODELO = "modelo";
    public final static String CAMPO_DESCRIPCION = "descripcion";
    public final static String CAMPO_TESTIPOSACCESORIOSINVID = "testiposaccesoriosinvid";
    public final static String CAMPO_TIPO_MODELO = "tipo_modelo";
    public final static String CAMPO_TESPROVEEDORESINVID = "tesproveedoresinvid";
    public final static String CAMPO_SWCREATEDBY = "swcreatedby";
    public final static String CAMPO_SWDATECREATED = "swdatecreated";
    public final static String CAMPO_TIMESTAMP = "timestamp";
    public final static String CAMPO_FECHA_BAJA = "fecha_baja";
    public final static String CAMPO_MODIFICADO_POR = "modificado_por";
    public final static String CAMPO_ESTADO = "estado";
    public final static String CAMPO_NUM_PRECIO = "num_precio";
    public final static String CAMPO_NUM_NRARBOR = "num_nrarbor";
    public final static String CAMPO_COD_PAQUETE = "cod_paquete";
    public final static String CAMPO_COD_COLOR = "cod_color";
    public final static String CAMPO_UNIDADQ = "unidadq";
    public final static String CAMPO_TECNOLOGIA = "tecnologia";
    public final static String CAMPO_MAXIMO = "maximo";
    public final static String CAMPO_MINIMO = "minimo";
    public final static String CAMPO_SOPORTA_GPRS = "soporta_gprs";
    public final static String CAMPO_SOPORTA_FAX = "soporta_fax";
    public final static String CAMPO_SYS_CORREOMOV = "sys_correomov";
    public final static String CAMPO_MANEJA_EXISTENCIAS = "maneja_existencias";
    public final static String CAMPO_CODIGO_BARRAS = "codigo_barras";

    private BigDecimal tesarticulosinvid;
    private String cod_articulo;
    private BigDecimal tesgruposinvid;
    private BigDecimal tesmarcasinvid;
    private String modelo;
    private String descripcion;
    private BigDecimal testiposaccesoriosinvid;
    private String tipo_modelo;
    private BigDecimal tesproveedoresinvid;
    private String swcreatedby;
    private Timestamp swdatecreated;
    private String timestamp;
    private Timestamp fecha_baja;
    private String modificado_por;
    private String estado;
    private String num_precio;
    private BigDecimal num_nrarbor;
    private BigDecimal cod_paquete;
    private String cod_color;
    private String unidadq;
    private BigDecimal tecnologia;
    private BigDecimal maximo;
    private BigDecimal minimo;
    private BigDecimal soporta_gprs;
    private BigDecimal soporta_fax;
    private String sys_correomov;
    private String maneja_existencias;
    private String codigo_barras;
    
    public BigDecimal getTesarticulosinvid() {
        return tesarticulosinvid;
    }
    public void setTesarticulosinvid(BigDecimal tesarticulosinvid) {
        this.tesarticulosinvid = tesarticulosinvid;
    }
    public String getCod_articulo() {
        return cod_articulo;
    }
    public void setCod_articulo(String cod_articulo) {
        this.cod_articulo = cod_articulo;
    }
    public BigDecimal getTesgruposinvid() {
        return tesgruposinvid;
    }
    public void setTesgruposinvid(BigDecimal tesgruposinvid) {
        this.tesgruposinvid = tesgruposinvid;
    }
    public BigDecimal getTesmarcasinvid() {
        return tesmarcasinvid;
    }
    public void setTesmarcasinvid(BigDecimal tesmarcasinvid) {
        this.tesmarcasinvid = tesmarcasinvid;
    }
    public String getModelo() {
        return modelo;
    }
    public void setModelo(String modelo) {
        this.modelo = modelo;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public BigDecimal getTestiposaccesoriosinvid() {
        return testiposaccesoriosinvid;
    }
    public void setTestiposaccesoriosinvid(BigDecimal testiposaccesoriosinvid) {
        this.testiposaccesoriosinvid = testiposaccesoriosinvid;
    }
    public String getTipo_modelo() {
        return tipo_modelo;
    }
    public void setTipo_modelo(String tipo_modelo) {
        this.tipo_modelo = tipo_modelo;
    }
    public BigDecimal getTesproveedoresinvid() {
        return tesproveedoresinvid;
    }
    public void setTesproveedoresinvid(BigDecimal tesproveedoresinvid) {
        this.tesproveedoresinvid = tesproveedoresinvid;
    }
    public String getSwcreatedby() {
        return swcreatedby;
    }
    public void setSwcreatedby(String swcreatedby) {
        this.swcreatedby = swcreatedby;
    }
    public Timestamp getSwdatecreated() {
        return swdatecreated;
    }
    public void setSwdatecreated(Timestamp swdatecreated) {
        this.swdatecreated = swdatecreated;
    }
    public String getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
    public Timestamp getFecha_baja() {
        return fecha_baja;
    }
    public void setFecha_baja(Timestamp fecha_baja) {
        this.fecha_baja = fecha_baja;
    }
    public String getModificado_por() {
        return modificado_por;
    }
    public void setModificado_por(String modificado_por) {
        this.modificado_por = modificado_por;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
    public String getNum_precio() {
        return num_precio;
    }
    public void setNum_precio(String num_precio) {
        this.num_precio = num_precio;
    }
    public BigDecimal getNum_nrarbor() {
        return num_nrarbor;
    }
    public void setNum_nrarbor(BigDecimal num_nrarbor) {
        this.num_nrarbor = num_nrarbor;
    }
    public BigDecimal getCod_paquete() {
        return cod_paquete;
    }
    public void setCod_paquete(BigDecimal cod_paquete) {
        this.cod_paquete = cod_paquete;
    }
    public String getCod_color() {
        return cod_color;
    }
    public void setCod_color(String cod_color) {
        this.cod_color = cod_color;
    }
    public String getUnidadq() {
        return unidadq;
    }
    public void setUnidadq(String unidadq) {
        this.unidadq = unidadq;
    }
    public BigDecimal getTecnologia() {
        return tecnologia;
    }
    public void setTecnologia(BigDecimal tecnologia) {
        this.tecnologia = tecnologia;
    }
    public BigDecimal getMaximo() {
        return maximo;
    }
    public void setMaximo(BigDecimal maximo) {
        this.maximo = maximo;
    }
    public BigDecimal getMinimo() {
        return minimo;
    }
    public void setMinimo(BigDecimal minimo) {
        this.minimo = minimo;
    }
    public BigDecimal getSoporta_gprs() {
        return soporta_gprs;
    }
    public void setSoporta_gprs(BigDecimal soporta_gprs) {
        this.soporta_gprs = soporta_gprs;
    }
    public BigDecimal getSoporta_fax() {
        return soporta_fax;
    }
    public void setSoporta_fax(BigDecimal soporta_fax) {
        this.soporta_fax = soporta_fax;
    }
    public String getSys_correomov() {
        return sys_correomov;
    }
    public void setSys_correomov(String sys_correomov) {
        this.sys_correomov = sys_correomov;
    }
    public String getManeja_existencias() {
        return maneja_existencias;
    }
    public void setManeja_existencias(String maneja_existencias) {
        this.maneja_existencias = maneja_existencias;
    }
    public String getCodigo_barras() {
        return codigo_barras;
    }
    public void setCodigo_barras(String codigo_barras) {
        this.codigo_barras = codigo_barras;
    }
}
