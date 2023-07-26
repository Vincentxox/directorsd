package com.consystec.sc.sv.ws.util;


public class Conf {
	private Conf(){} 
    // par\u00E9metros configurables utilizados por servicios
    public static final String CANTIDAD_MINUTOS_SESION = "CANT_MINUTOS_SESION";

    public static final String PAIS_DEFAULT = "pais_default";

    public static final String URI_SOCKET_VENTA = "URI_SOCKET_VENTA";
    //Configuraciones de LOG
    public static final String CARPETA_LOG = "logs";
    public static final String GRUPO_LOCAL_CONFIG_LOG4J = "LOCAL_CONFIG_LOG4J";
    public static final String LEVEL_LOG4J = "LEVEL";
    public static final String PATH_LOG4J = "PATH_LOG4J";
    public static final String LOG4J_OFF = "OFF";
    public static final String LOG4J_FATAL = "FATAL";
    public static final String LOG4J_ERROR = "ERROR";
    public static final String LOG4J_WARN = "WARN";
    public static final String LOG4J_INFO = "INFO";
    public static final String LOG4J_DEBUG = "DEBUG";
    public static final String LOG4J_TRACE = "TRACE";
    public static final String LOG4J_ALL = "ALL";
    public static final String LOG4J_SIZE= "SIZE";
    public static final String LOG4J_CONVERSIONPATTERN= "CONVERSIONPATTERN";
    public static final String LOG4J_FILENAME= "FILENAME";
    public static final String LOG4J_LIMIT= "LIMIT";
    public static final String TIPO_GRUPO_SIMCAR = "SIMCARD";
    public static final String GRUPO_SIMCARD="GRUPO_SIMCARD";
    /* PAR\u00E9METROS PARA DETERMINAR SI REQUIERE PAGO UNA ORDEN */
    public static final String REQUIERE_PAGO_SI = "requiere_pago_si";
    public static final String REQUIERE_PAGO_NO = "requiere_pago_no";
    public static final String REQUIERE_PAGO = "requiere_pago";
    public static final String NO_REQUIERE_PAGO = "no_requiere_pago";
    public static final String ESTADO_ALTA_TXT = "estado_alta_txt";
    /* Formatos de fecha */
    public static final String TXT_FORMATO_FECHA = "dd/MM/yyyy";
    public static final String TXT_FORMATO_FECHA_CORTA = "yyyyMMdd";
    public static final String TXT_FORMATO_FECHA_INPUT = "dd/MM/yyyy";
    public static final String TXT_FORMATO_FECHAHORA = "dd/MM/yyyy HH:mm:ss";
    public static final String TXT_FORMATO_FECHAHORA2 = "dd/MM/yyyy HH24:mi:ss";
    public static final String TXT_FORMATO_TIMESTAMP = "YYYYMMddHH24MISS";
    public static final String TXT_FORMATO_FECHA_CORTA_GT = "ddMMyyyy";
    public static final String TXT_FORMATO_FECHA_BD = "YYYY-MM-DD HH24:MI:SS.FF";


    public static final int LONG_CODPAIS = 3;
    public static final int LONG_NUMERO = 8;
    public static final int LONG_TOKEN = 256;
    public static final int LONG_USUARIO = 50;
    public static final int LONG_NOMBRE_ARCHIVO = 100;
    public static final int CANT_DECIMALES_PAGOS = 2;
    public static final int LONG_PIN_RECAGA = 4;

    /*
     * PAR\u00E9METROS PARA OBTENER ETIQUETAS O VALORES DE LA TABLA PARAMETROS O
     * ESTADOS DE ORDEN
     */
    public static final int TIPO_PARAMETRO = 1;
    public static final int TIPO_ESTADO_ORDEN = 2;
    public static final int TIPO_ESTADO_LOTE = 3;

    /* PAR\u00E9METROS DE APLICACION */
    public static final String NOMBRE_APLICACION = "APPMOVIL";
    
    /* PARAMETROS PARA VALIDAR LA RESPUESTA DE LOS SERVICIOS SPN SCL*/
    public static final String SPN_SCL_OK = "0";
    public static final String SPN_SCL_ERROR = "1";

    /* PAR\u00E9METROS SERVICIO PAGO */
    public static final int YEAR = 2000;
    public static final int MONTH = -1;

    /* PARA FTP DE CARGA DE IMAGENES */
    public static final String IP = "173.15.2.73";
    public static final String DIR = "ImageUpload";
    public static final String PUERTO = "21";
    public static final String USUARIO = "consystec";
    public static final String CLAVE = "consystec";

    public static final String FTPNUEVO = "NUEVO";
    public static final String FTPBORRAR = "BORRAR";

    /* PAR\u00E9METROS PARA METODOS DE SERVICIOS REST */
    public static final int METODO_GET = 0;
    public static final int METODO_POST = 1;
    public static final int METODO_PUT = 2;
    public static final int METODO_DELETE = 3;
    public static final int METODO_COUNT = 4;

    /* PAR\u00E9METROS PARA FILTROS GEN\u00E9RICOS, COMPUESTOS Y PREDEFINIDOS */
    public static final int FILTRO_ESTADO = 0;
    public static final int FILTRO_PDV = 1;
    public static final int FILTRO_PANEL = 2;
    public static final int FILTRO_ID_NUMERICO_AND = 3;
    public static final int FILTRO_ID_NUMERICO_OR = 4;
    public static final int FILTRO_TEXTO_AND = 5;
    public static final int FILTRO_TEXTO_OR = 6;
    public static final int FILTRO_TEXTO_UPPER_AND = 7;
    public static final int FILTRO_TEXTO_UPPER_OR = 8;
    public static final int FILTRO_FECHA = 9;
    public static final int FILTRO_RANGO_FECHAS = 10;
    public static final int FILTRO_IN_AND = 11;
    public static final int FILTRO_IN_OR = 12;
    public static final int FILTRO_IN_UPPER_AND = 13;
    public static final int FILTRO_IN_UPPER_OR = 14;
    public static final int FILTRO_IS_NULL_AND = 15;
    public static final int FILTRO_IS_NULL_OR = 16;
    public static final int FILTRO_IS_NOT_NULL_AND = 17;
    public static final int FILTRO_IS_NOT_NULL_OR = 18;
    public static final int FILTRO_LIKE_AND = 19;
    public static final int FILTRO_LIKE_OR = 20;
    public static final int FILTRO_NOT_IN_AND = 21;
    public static final int FILTRO_NOT_IN_OR = 22;
    public static final int FILTRO_TEXTO_UPPER_AND_NEQ = 23;
    public static final int FILTRO_TEXTO_UPPER_OR_NEQ = 24;
    public static final int FILTRO_FECHA_LTE_AND = 25;
    public static final int FILTRO_FECHA_GTE_AND = 26;
    public static final int FILTRO_FECHA_LTE_OR = 27;
    public static final int FILTRO_FECHA_GTE_OR = 28;
    public static final int FILTRO_FECHA_TRUNC = 29;
    public static final int FILTRO_ID_NUMERICO_AND_NEQ = 30;
    public static final int FILTRO_AVANZADO_AND = 31;
    public static final int FILTRO_AVANZADO_OR = 32;
    public static final int FILTRO_SYSDATE_TRUNC_AND = 33;
    public static final int FILTRO_TIMESTAMP_GTE_AND = 34;
    public static final int FILTRO_TIMESTAMP_TRUNC_AND = 35;
    public static final int FILTRO_LIKE_PR = 36;
   
    
    /* PAR\u00E9METROS PARA TIPOS DE INSERTS */
    public static final int INSERT_SECUENCIA = 0;
    public static final int INSERT_NUMERO = 1;
    public static final int INSERT_TEXTO = 2;
    public static final int INSERT_TEXTO_UPPER = 3;
    public static final int INSERT_FECHA = 4;
    public static final int INSERT_SYSDATE = 5;
    public static final int INSERT_NULL = 6;
    public static final int INSERT_TIMESTAMP = 7;
    public static final String INSERT_TEXTO_SEPARADOR = ", ";
    public static final boolean INSERT_SEPARADOR_SI = true;
    public static final boolean INSERT_SEPARADOR_NO = false;

    /* PAR\u00E9METROS PARA TIPOS DE SELECTS */
    public static final int SELECT_DISTINCT = 0;
    public static final int SELECT_UPPER = 1;
    public static final int SELECT_SUM = 2;
    public static final int SELECT_COUNT = 3;
    public static final int SELECT_FECHA_TRUNC = 4;

    /* PAR\u00E9METROS PARA TIPOS DE VALORES */
    public static final int TIPO_TEXTO = 0;
    public static final int TIPO_NUMERICO = 1;
    public static final int TIPO_FECHA = 2;

    /* PAR\u00E9METROS PARA FUNCI\u00F3N DTS */
    public static final String DTS_NOMBRES = "Nombres";
    public static final String DTS_IDBODEGAVIRTUAL = "TcScBodegaVirtual";
    public static final String DTS_TIPO = "Tipo";

    /* PAR\u00E9METROS PARA NOMBRES DE CONFIGURACIONES */
    public static final String ESTADO_ACTIVO = "ACTIVO";
    public static final String ESTADO_ALTA = "ALTA";
    public static final String ESTADO_BAJA = "BAJA";
    public static final String ESTADO_ALTA_BOOL = "ALTA_BOOL";
    public static final String ESTADO_BAJA_BOOL = "BAJA_BOOL";
    public static final String TIPO_PDV = "PDV";
    public static final String TIPO_PANEL = "PANEL";
    public static final String TIPO_RUTA = "RUTA";
    public static final String TIPO_DISPOSITIVOS = "DISPOSITIVOS";
    
    //Configuraciones de Oferta Comercial
    public static final String TIPO_OFERTA = "OFERTA";
    public static final String TIPO_CAMPANIA = "CAMPA\u00D1A";

    public static final String TIPO_PROMOCION = "PROMOCION";
    
    public static final String TIPO_PORCENTAJE = "PORCENTAJE";
    public static final String TIPO_MONTO = "MONTO";

    public static final String DESCU_CONFIG_MAYOR = "MAYOR";
    public static final String DESCU_CONFIG_MENOR = "MENOR";
    public static final String DESCU_CONFIG_IGUAL = "IGUAL";
    public static final String DESCU_CONFIG_ENTRE = "ENTRE";
    public static final String DESCU_CONFIG_MAYOR_SYM = "DESC_MAYOR_SYM";
    public static final String DESCU_CONFIG_MENOR_SYM = "DESC_MENOR_SYM";
    public static final String DESCU_CONFIG_IGUAL_SYM = "DESC_IGUAL_SYM";

    public static final String SOL_TIPO_DEVOLUCION = "DEVOLUCION";
    public static final String SOL_TIPO_SINIESTRO = "SINIESTRO";
    public static final String SOL_TIPO_RESERVA = "RESERVA";
    public static final String SOL_TIPO_PEDIDO = "PEDIDO";
    public static final String SOL_TIPO_NUMEROS_PAYMENT = "NUMEROS_PAYMENT";
    public static final String SOL_TIPO_TODAS = "TODAS";
    public static final String SOL_TIPO_DEUDA = "DEUDA";

    public static final String SOL_ORIGEN_PC = "PC";
    public static final String SOL_ORIGEN_MOVIL = "MOVIL";
    public static final String SOL_TIPOINV_TELCA = "INV_TELCA";
    public static final String SOL_TIPOINV_SIDRA = "INV_SIDRA";

    public static final String SOL_ESTADO_ABIERTA = "ABIERTA";
    public static final String SOL_ESTADO_CERRADA = "CERRADA";
    public static final String SOL_ESTADO_CERRADA_SINIESTRO = "CERRADA_SINIESTRO";

    public static final String SOL_ESTADO_ACEPTADA = "ACEPTADA";
    public static final String SOL_ESTADO_RECHAZADA = "RECHAZADA";
    public static final String SOL_ESTADO_PENDIENTE = "PENDIENTE";
    public static final String SOL_ESTADO_ANULADO_SINIESTRO = "ANULADO_SINIESTRO";
    public static final String SOL_ESTADO_CANCELADA = "CANCELADA";
    public static final String SOL_ESTADO_FINALIZADA = "FINALIZADA";
    public static final String SOL_ESTADO_RECHAZADA_TELCA = "RECHAZADA_TELCA";
    public static final String SOL_ESTADO_ENVIADO = "ENVIADO";

    public static final String INV_EST_DISPONIBLE = "DISPONIBLE";
    public static final String INV_EST_VENDIDO = "VENDIDO";
    public static final String INV_EST_DEVOLUCION = "En Proceso Devoluci\u00F3n";
    public static final String INV_EST_DEVUELTO = "DEVOLUCION";
    public static final String INV_EST_RESERVADO = "Reservado";
    public static final String INV_EST_SINIESTRO = "En Proceso Siniestro";
    public static final String INV_EST_SINIESTRADO = "Siniestrado";

    public static final String INGRESO = "ingreso";
    public static final String SALIDA = "salida";
    public static final String BUZON_TELCA = "buzon_telca";
    public static final String NOMBRE_BUZON = "NOMBRE_BUZON";
    public static final String BUZON_PAYMENT = "BUZON_PAYMENT";

    public static final String AR_TIPO_ASIGNACION = "ASIGNACION";
    public static final String AR_TIPO_RESERVA = "RESERVA";

    public static final String AR_ESTADO_ALTA = "ALTA";
    public static final String AR_ESTADO_FINALIZADA = "FINALIZADA";
    public static final String AR_ESTADO_CANCELADA = "CANCELADA";
    public static final String AR_ESTADO_ART_ASIGNADO = "Asignado";
    public static final String AR_ESTADO_ART_RESERVADO = "Reservado";
    public static final String AR_ESTADO_ART_CANCELADO = "Cancelado";
    
    // Par\u00E9metros de Jornadas
    public static final String JORNADA_TIPO_INICIO = "INICIO DE JORNADA";
    public static final String JORNADA_TIPO_FIN = "FIN DE JORNADA";
    public static final String JORNADA_TIPO_LIQUIDACION = "LIQUIDACION";
    public static final String JORNADA_TIPO_RECHAZO = "RECHAZO";
    public static final String JORNADA_ESTADO_INICIADA = "INICIADA";
    public static final String JORNADA_ESTADO_FINALIZADA = "FINALIZADA";
    public static final String JORNADA_ESTADO_PENDIENTE = "PENDIENTE";
    public static final String JORNADA_ESTADO_LIQUIDADA = "LIQUIDADA";
    public static final String JORNADA_ESTADO_RECHAZADA = "RECHAZADA";
    public static final String JORNADA_ESTADO_PAGADA = "PAGADA";

    public static final String CONDICION_OFERTA_VENTA = "VENTA";
    public static final String CONDICION_OFERTA_ARTICULO = "ARTICULO";
    public static final String CONDICION_OFERTA_PDV = "PDV";
    public static final String CONDICION_OFERTA_ZONA = "ZONA";
    public static final String CONDICION_OFERTA_TECNOLOGIA = "TECNOLOGIA";
    public static final String CONDICION_OFERTA_PAGUE_LLEVE = "PAGUE_LLEVE";

    public static final String CONDICION_GESTION_VENTA = "VENTA";
    public static final String CONDICION_PORTABILIDAD="PORTABILIDAD";

    public static final String TIPO_CONDICION_ARTICULO = "ARTICULO";
    public static final String TIPO_CONDICION_GENERICO = "GENERICO";
    public static final String TIPO_CONDICION_PDV = "PDV";
    public static final String TIPO_CONDICION_TECNOLOGIA = "TECNOLOGIA";

    public static final String GRUPO_JORNADA_HORARIOS = "JORNADA_HORARIOS";
    public static final String GRUPO_JORNADA_TIPO_ALARMA = "JORNADA_TIPO_ALARMA";
    public static final String GRUPO_JORNADA_CONFIG_CORREO = "JORNADA_CONFIG_CORREO";
    public static final String GRUPO_JORNADA_PLANTILLA_ALARMA = "PLANTILLA_ALARMA_JORNADA";
    public static final String GRUPO_CLIENTE_PERCEPCION = "CLIENTE_PERCEPCION";
    

    public static final String JORNADA_HORARIO_INICIO = "HORARIO_INICIO";
    public static final String JORNADA_HORARIO_FINAL = "HORARIO_FINAL";

    public static final String JORNADA_NOMBRE_ALARMA = "JORNADA";
    public static final String JORNADA_ALARMA_HORARIO = "ALARMA_HORARIO";
    public static final String JORNADA_ALARMA_FECHA = "ALARMA_FECHA";
    public static final String JORNADA_ALARMA_AMBAS = "ALARMA_AMBAS";

    public static final String CONFIG_CORREO_HOST = "mail.smtp.host";
    public static final String CONFIG_CORREO_PORT = "mail.smtp.socketFactory.port";
    public static final String CONFIG_CORREO_USER = "mail.smtp.user";
    public static final String CONFIG_CORREO_CLAV = "mail.smtp.password";
    public static final String CONFIG_CORREO_SENDER = "sender";
    public static final String CONFIG_CORREO_MASK = "mascara_email";
    public static final String CONFIG_CORREO_ASUNTO_PDV_DUP = "asunto";
    public static final String CONFIG_CORREO_CUERPO_PDV_DUP = "cuerpo.pdv";

    public static final String CONFIG_CORREO_ASUNTO = "ASUNTO";
    public static final String CONFIG_CORREO_CUERPO = "CUERPO";
    public static final String CONFIG_CORREO_MOTIVO_HORARIO = "MOTIVO_HORARIO";
    public static final String CONFIG_CORREO_MOTIVO_FECHA = "MOTIVO_FECHA";
    
    public static final String GRUPO_PLANTILLA_NOTIFICACION_DTS = "PLANTILLA_NOTIFICACION_DTS";

    // Cambiar por valor correcto del sistema comercial
    public static final String NOMBRE_VENDEDOR = "Nombre vendedor";

    // P\u00E9rametros de configuraciones de Sidra
    public static final String NOMBRE_GENERICO = "GENERICO";
    public static final String PAIS = "PAIS";
    public static final String IMPUESTO = "impuestos";
    public static final String REGISTROS_MAXIMOS = "REGITROS_MAXIMOS";
    public static final String FOTOGRAFIAS_MAXIMAS = "FOTOGRAFIAS_MAXIMAS";
    public static final String CANT_DECIMALES = "CANTDECIMALES";
    public static final String CANT_DECIMALES_BD = "CANTDECIMALES_BD";
    public static final String LONGITUD_TELEFONO = "LONGITUD_TELEFONO";
    public static final String LONG_IDENTIFICACION = "LONG_IDENTIFICACION";
    public static final String PAQUETE_SIDRA = "PAQUETE_SIDRA";
    public static final String OBS_JORNADA_SINIESTRO = "OBS_JORNADA_SINIESTRO";
    public static final String URL_SESIONES_REGIONAL = "URL_SESIONES_REGIONAL";
    public static final String DIFERENCIA_HORARIO = "DIFERENCIA_HORARIO";
    public static final String VALIDA_SERIE = "VALIDA_SERIE";
    public static final String FOLIO_MANUAL = "FOLIO_MANUAL";
    public static final String BANCOS_EFECTIVO = "BANCOS_EFECTIVO";
    public static final String PIN_DEFAULT = "PIN_DEFAULT";

    public static final String GRUPO_MAPAS_URL_SERVICIOS = "MAPAS_URL_SERVICIOS";
    public static final String URL_SERVICIO_DTS = "URL_SERVICIO_DTS";
    public static final String URL_SERVICIO_PDV = "URL_SERVICIO_PDV";
    public static final String URL_SERVICIO_VISITA = "URL_SERVICIO_VISITA";
    public static final String URL_SERVICIO_INV = "URL_SERVICIO_INV";

    //Para folios SCL
    public static final String COD_TIPODOC_SCL = "COD_TIPODOC_SCL";
    
    //Par\u00E9metros para DTS
    public static final String GRUPO_CAMPOS_DTS = "CAMPOS_DTS";
    public static final String GRUPO_CANAL_DTS = "CANAL_DTS";
    public static final String CAMPO_NUM_CONVENIO = "NUM_CONVENIO";
    

    /* PAR\u00E9METRO PARA CODIGO TIPO TRANSACCION */
    public static final String CODIGO_TRANSACCION_TRASLADO = "T1";
    public static final String CODIGO_TRANSACCION_ASIGNACION = "ASIG";
    public static final String CODIGO_TRANSACCION_ASIGNA_RESERVA = "ARES";
    public static final String CODIGO_TRANSACCION_RESERVA = "RES";
    public static final String CODIGO_TRANSACCION_VENTA = "VEN";
    public static final String CODIGO_TRANSACCION_MOD_ASIG_RES = "MAR";
    public static final String CODIGO_TRANSACCION_CAN_ASIG = "CAN";

    /* PAR\u00E9METROS PARA GRUPOS DE CONFIGURACIONES */
    public static final String GRUPO_CONFIG_SIDRA = "CONFIG_SIDRA";
    public static final String GRUPO_DESCUENTOS = "DESCUENTOS";
    public static final String GRUPO_OFERTA_COMERCIAL = "OFERTA_COMERCIAL";
    public static final String GRUPO_ESTADOS = "ESTADOS";
    public static final String GRUPO_ESTADOS_ESTADOS_PDV = "ESTADOS_PDV";
    public static final String GRUPO_PANELPDV = "PANEL-PDV";
    public static final String GRUPO_PANELRUTA = "PANEL-RUTA";
    public static final String GRUPO_SOLICITUDES_TIPO = "SOLICITUDES_TIPO";
    public static final String GRUPO_SOLICITUDES_ORIGEN = "SOLICITUDES_ORIGEN";
    public static final String GRUPO_SOLICITUDES_TIPOINV = "SOLICITUDES_TIPOINV";
    public static final String GRUPO_ESTADOS_SOLICITUD = "ESTADOS_SOLICITUD";
    public static final String GRUPO_ESTADOS_INVENTARIO = "ESTADOS_INVENTARIO";
    public static final String GRUPO_ART_PROMOCIONALES = "GRUPOS_ART_PROMOCIONALES";
    public static final String GRUPO_MOV_INV_PROMOCIONAL = "MOV_INV_PROMOCIONAL";
    public static final String GRUPO_ASIGNACIONES_TIPO = "ASIGNACIONES_TIPO";
    public static final String GRUPO_ASIGNACIONES_ESTADOS = "ASIGNACIONES_ESTADOS";
    public static final String GRUPO_TIPO_FOLIO = "TIPO_FOLIO";
    public static final String GRUPO_ESTADOS_FOLIOS = "ESTADOS_FOLIOS";
    public static final String GRUPO_ESTADOS_FOLIOS_SINIESTRO = "ESTADOS_FOLIOS_SINIESTRO";

    public static final String GRUPO_JORNADA_TIPOS = "JORNADA";
    public static final String GRUPO_JORNADA_ESTADOS = "JORNADA_ESTADOS";
    public static final String GRUPO_JORNADA_ESTADOS_LIQ = "JORNADA_ESTADOS_LIQ";
    public static final String GRUPO_FECHA_CIERRE_JORNADA = "FECHA_CIERRE_JORNADA";
    
    public static final String GRUPO_TIPO_GESTION_VENTA = "TIPO_GESTION_VENTA";
    public static final String GRUPO_CONDICION_TIPO = "CONDICION_TIPO";
    public static final String GRUPO_CONDICION_TIPOOFERTA = "CONDICION_TIPOOFERTA";
    public static final String GRUPO_CONDICION_TIPOCAMPANIA = "CONDICION_TIPOCAMPANIA";
    
    public static final String GRUPO_TIPO_TECNOLOGIA = "TIPO_TECNOLOGIA";
    public static final String GRUPO_TIPO_CLIENTE_OFERTA = "TIPO_CLIENTE_OFERTA";

    public static final String GRUPO_TIPOS_NEGOCIO_PDV = "TIPOS_NEGOCIO_PDV";
    public static final String GRUPO_CATEGORIA_PDV = "CATEGORIA_PDV";
    public static final String GRUPO_TIPO_CONTRIBUYENTE_PDV = "TIPO_CONTRIBUYENTE_PDV";
    public static final String GRUPO_TIPO_PRODUCTO_PDV = "TIPO_PRODUCTO_PDV";
    public static final String GRUPO_CANAL_PDV = "CANAL_PDV";
    public static final String GRUPO_SUBCANAL_PDV = "SUBCANAL_PDV";
    public static final String GRUPO_CAMPOS_PDV = "CAMPOS_PDV";
    public static final String GRUPO_CAMPOS_ENCARGADO_PDV = "CAMPOS_ENCARGADO_PDV";
    public static final String CAMPO_ENCARGADO = "encargado";
    public static final String GRUPO_SECCIONES_CAMPOS_PDV = "SECCIONES_CAMPOS_PDV";
    public static final String GRUPO_DOC_FISCAL = "DOC_FISCAL";
    public static final String GRUPO_ZONA_COMERCIAL_PDV = "ZONA_COMERCIAL_PDV";
    
    public static final String GRUPO_BANCOS = "BANCOS";
    public static final String GRUPO_TIPO_CUENTA_BANCARIA = "TIPO_CUENTA_BANCARIA";
    
    public static final String GRUPO_ESTADOS_REMESA = "ESTADOS_REMESA";
    public static final String GRUPO_ESTADOS_PAYMENT = "ESTADOS_PAYMENT";
    public static final String GRUPO_TIPOS_SINIESTROS = "TIPOS_SINIESTROS";
    public static final String GRUPO_ESTADOS_SINIESTROS = "ESTADOS_SINIESTROS";
    
    public static final String GRUPO_ENCABEZADOS_TICKET = "ENCABEZADO_TIKECT";
    
    /* GRUPOS PARA VENTAS */
    public static final String GRUPO_DOC_IDENT_ALTA = "DOC_IDENT_ALTA";
    public static final String GRUPO_TIPOS_DOCUMENTO_VENTA = "TIPOS_DOCUMENTO_VENTA";
    public static final String GRUPO_FORMA_PAGO_VENTA = "FORMA_PAGO_VENTA";
    public static final String GRUPO_TIPO_CLIENTE_VENTA = "TIPO_CLIENTE_VENTA";
    public static final String GRUPO_ESTADOS_VENTA = "ESTADOS_VENTA";
    public static final String GRUPO_IMPUESTO_PAIS = "IMPUESTO_PAIS";
    public static final String GRUPO_TIPO_EXENTO = "TIPO_EXENTO";
    public static final String GRUPO_PARAM_VENTA = "PARAM_VENTAS";
    
    public static final String GRUPO_RAZONES_ANULACION = "RAZONES_ANULACION";
    
    public static final String GRUPO_ARTICULO_CANTIDAD = "ARTICULO_CANTIDAD";
    public static final String GRUPO_ZONA_COMERCIAL_REG = "ZONA_COMERCIAL_PDV_REG";
    
    public static final String ZONA_COMERCIAL_1 = "ZONA 1";
    public static final String ARTICULO_RECARGA = "ARTICULO_RECARGA";

    //GRUPOS PARA FICHA CLIENTE SCL
    public static final String GRUPO_FICHA_CLIENTE_CUENTA = "FICHA_CLIENTE_CUENTA";
    public static final String GRUPO_FICHA_CLIENTE_CLIENTE = "FICHA_CLIENTE";
    public static final String GRUPO_CUENTA_CLIENTE = "CUENTA_CLIENTE";
    public static final String GRUPO_FICHA_CLIENTE_SPNSCL = "FICHA_CLIENTE_SPNSCL";
    public static final String GRUPO_FICHA_CLIENTE_CONSULTA = "FICHA_CLIENTE_CONSULTA";
    
    //CONFIGURACION GENERAL PARA FICHA CLIENTE
    public static final String SPN_SCL_ROLLBACK = "SPN_SCL_ROLLBACK";
    public static final String URL_SPN_SCL = "URL_SPN_SCL";
    public static final String COD_DIRECCION = "COD_DIRECCION";
    
    //CONFIGURACION CONSULTA FICHA DE CLIENTE
    public static final String URL_BEAN_PORT_PA = "URL_BEAN_PORT_PA";
    public static final String TIPO_CONSULTA_FICHACLIENTE = "TIPO_CONSULTA_FICHACLIENTE";
    
    //CONFIGURACIONES PARA FICHA CLIENTE CLIENTE SCL
    public static final String CLIENTE_USUARIO_ORA = "CLIENTE_USUARIO_ORA";
    public static final String CLIENTE_SISTEMA_PAGO = "CLIENTE_SISTEMA_PAGO"; 
    public static final String CLIENTE_CAT_IMPOS = "CLIENTE_CAT_IMPOS";
    public static final String CLIENTE_CATEGORIA = "CLIENTE_CATEGORIA";
    public static final String CLIENTE_CODIGO_CICLO = "CLIENTE_CODIGO_CICLO";
    public static final String CLIENTE_CODIGO_OFICINA = "CLIENTE_CODIGO_OFICINA";
    public static final String CLIENTE_CODIGO_PAIS = "CLIENTE_CODIGO_PAIS";
    public static final String CLIENTE_CODIGO_USO = "CLIENTE_CODIGO_USO";
    public static final String CLIENTE_INT_GRUPO_FAM = "CLIENTE_INT_GRUPO_FAM";
    public static final String CLIENTE_PAGO_AUT_MAN = "CLIENTE_PAGO_AUT_MAN";
    
    //CONFIGURACIONES PARA FICHA CLIENTE CUENTA EN SCL
    public final static String CUENTA_CAT_TRIBUTARIA = "CUENTA_CAT_TRIBUTARIA"; 
	public final static String CUENTA_TIPO_CUENTA = "CUENTA_TIPO_CUENTA"; 
	public final static String CUENTA_COD_CATEGORIA = "CUENTA_COD_CATEGORIA"; 
	public final static String CUENTA_DESCRIPCION = "CUENTA_DESCRIPCION"; 
	public final static String CUENTA_EXISTENTE_ERROR = "CUENTA_EXISTENTE_ERROR";
    
    // CONFIGURACIONES PARA VALIDACIONES DINAMICAS
    public static final String GRUPO_VALIDACIONES_DINAMICAS = "VALIDACIONES_DINAMICAS";
    public static final String VALIDAR_ZONA_PDV = "ZONA_PDV";
    public static final String NOMBRE_NUM_PAYMENT = "NOMBRE_NUM_PAYMENT";
    public static final String NOMBRE_DISPOSITIVO_SINIESTRO = "NOMBRE_DISPOSITIVO_SINIESTRO";

    // CONFIGURACIONES ESTADOS PAYMENT
    public static final String PAYMENT_ESTADO_ACTIVADO = "ACTIVADO";
    public static final String PAYMENT_ESTADO_DESACTIVADO = "DESACTIVADO";
    public static final String PAYMENT_ESTADO_RECHAZADO = "RECHAZADO";

    // CONFIGURACIONES TIPOS DE SINIESTROS
    public static final String SINIESTRO_TOTAL = "TOTAL";
    public static final String SINIESTRO_PARCIAL = "PARCIAL";
    public static final String SINIESTRO_DISPOSITIVO = "DISPOSITIVO";

    // CONFIGURACIONES ESTADOS SINIESTROS
    public static final String SINIESTRO_ESTADO_EN_PROCESO = "EN_PROCESO";
    public static final String SINIESTRO_ESTADO_SINIESTRADO = "SINIESTRADO";

    /* PAR\u00E9METROS PARA OPERACIONES DEL LOG SIDRA */
    public static final String LOG_TRANSACCION_CREA_SOLICITUD = "CREAR_SOLICITUD_WF";
    public static final String LOG_TRANSACCION_CREA_ASIGNACION_RESERVA = "CREA_ASIGNACION_RESERVA";
    public static final String LOG_TRANSACCION_MOD_ASIGNACION_RESERVA = "MOD_ASIGNACION_RESERVA";
    public static final String LOG_TRANSACCION_TRASLADO = "TRASLADO";
    public static final String LOG_TRANSACCION_CAMBIO_ESTADO_ARTICULO = "CAMBIO_ESTADO_ARTICULO";
    public static final String LOG_TRANSACCION_CAMBIO_ESTADO = "CAMBIO_ESTADO";
    public static final String LOG_TRANSACCION_CAMBIO_ESTADO_OPERACION = "CAMBIO_ESTADO_OPERACION";
    public static final String LOG_TRANSACCION_CONSULTA_DATOS = "CONSULTA_DATOS";
    public static final String LOG_TRANSACCION_INGRESO_SALIDA_PROMO = "INGRESO_SALIDA_INV";
    public static final String LOG_TRANSACCION_INGRESO_PROMO = "INGRESO";
    public static final String LOG_TRANSACCION_SALIDA_PROMO = "SALIDA";
    public static final String LOG_TRANSACCION_ACEPTA_DEVOLUCION = "ACEPTA_DEVOLUCION";
    public static final String LOG_TRANSACCION_RECHAZA_DEVOLUCION = "RECHAZA_DEVOLUCION";
    public static final String LOG_TRANSACCION_ACEPTA_SINIESTRO = "ACEPTA_SINIESTRO";
    public static final String LOG_TRANSACCION_VENTA = "VENTA";

    public static final String LOG_TRANSACCION_ADJUNTO = "ADJUNTO";
    public static final String LOG_TRANSACCION_ALTA_PREPAGO = "ALTA_PREPAGO";

    public static final String LOG_TRANSACCION_CREA_BODEGA_ALMACEN = "CREA_BODEGA_ALMACEN";
    public static final String LOG_TRANSACCION_CREA_BODEGA_VIRTUAL = "CREA_BODEGA_VIRTUAL";
    public static final String LOG_TRANSACCION_CREA_BUZON = "CREA_BUZON";
    public static final String LOG_TRANSACCION_CREA_CARGA_FILE = "CREA_CARGA_FILE";
    public static final String LOG_TRANSACCION_CREA_CATALOGO = "CREA_CATALOGO";
    public static final String LOG_TRANSACCION_CREA_CLIENTE = "CREA_CLIENTE";
    public static final String LOG_TRANSACCION_CREA_CONDICION = "CREA_CONDICION";
    public static final String LOG_TRANSACCION_CREA_CONDICION_OFERTA = "CREA_CONDICION_OFERTA";
    public static final String LOG_TRANSACCION_CREA_DISPOSITIVO = "CREA_DISPOSITIVO";
    public static final String LOG_TRANSACCION_CREA_DISTRIBUIDOR = "CREA_DISTRIBUIDOR";
    public static final String LOG_TRANSACCION_CREA_FOLIO = "CREA_FOLIO";
    public static final String LOG_TRANSACCION_CREA_JORNADA = "CREA_JORNADA";
    public static final String LOG_TRANSACCION_CREA_OFERTA_CAMPANIA = "CREA_OFERTA_CAMPANIA";
    public static final String LOG_TRANSACCION_CREA_PANEL = "CREA_PANEL";
    public static final String LOG_TRANSACCION_CREA_PDV = "CREA_PDV";
    public static final String LOG_TRANSACCION_CREA_ARTICULO_PROMOCIONAL = "CREA_ARTICULO_PROMOCIONAL";
    public static final String LOG_TRANSACCION_CREA_PROMOCIONAL_CAMPANIA = "CREA_PROMOCIONAL_CAMPANIA";
    public static final String LOG_TRANSACCION_CREA_RUTA = "CREA_RUTA";
    public static final String LOG_TRANSACCION_CREA_TIPO_TRANSACCION = "CREA_TIPO_TRANSACCION";
    public static final String LOG_TRANSACCION_CREA_USUARIO_BUZON = "CREA_USUARIO_BUZON";
    public static final String LOG_TRANSACCION_CREA_VENDEDOR_DTS = "CREA_VENDEDOR_DTS";
    public static final String LOG_TRANSACCION_CREA_VISITA_GESTION = "CREA_VISITA_GESTION";
    public static final String LOG_TRANSACCION_CREA_CUENTA = "CREA_CUENTA";
    public static final String LOG_TRANSACCION_CREA_REMESA = "CREA_REMESA";
    public static final String LOG_TRANSACCION_CREA_SINCRONIZACION = "CREA_SINCRONIZACION";
    public static final String LOG_TRANSACCION_RESERVA_FOLIO_FS = "RESERVA_FOLIO_FS";
    public static final String LOG_TRANSACCION_CREA_ANULACION = "CREA_ANULACION";
    public static final String LOG_TRANSACCION_CREA_FECHA_JORNADA = "CREA_FECHA_JORNADA";
    
    public static final String LOG_TRANSACCION_MOD_BODEGA_ALMACEN = "MOD_BODEGA_ALMACEN";
    public static final String LOG_TRANSACCION_MOD_BODEGA_VIRTUAL = "MOD_BODEGA_VIRTUAL";
    public static final String LOG_TRANSACCION_MOD_BUZON = "MOD_BUZON";
    public static final String LOG_TRANSACCION_MOD_CARGA_FILE = "MOD_CARGA_FILE";
    public static final String LOG_TRANSACCION_MOD_CATALOGO = "MOD_CATALOGO";
    public static final String LOG_TRANSACCION_MOD_CLIENTE = "MOD_CLIENTE";
    public static final String LOG_TRANSACCION_MOD_CONDICION = "MOD_CONDICION";
    public static final String LOG_TRANSACCION_MOD_CONDICION_OFERTA = "MOD_CONDICION_OFERTA";
    public static final String LOG_TRANSACCION_MOD_DISPOSITIVO = "MOD_DISPOSITIVO";
    public static final String LOG_TRANSACCION_MOD_DISTRIBUIDOR = "MOD_DISTRIBUIDOR";
    public static final String LOG_TRANSACCION_MOD_FOLIO = "MOD_FOLIO";
    public static final String LOG_TRANSACCION_MOD_JORNADA = "MOD_JORNADA";
    public static final String LOG_TRANSACCION_MOD_OFERTA_CAMPANIA = "MOD_OFERTA_CAMPANIA";
    public static final String LOG_TRANSACCION_MOD_PANEL = "MOD_PANEL";
    public static final String LOG_TRANSACCION_MOD_PDV = "MOD_PDV";
    public static final String LOG_TRANSACCION_GET_PDV_DISPONIBLES = "GET_PDV_DISPONIBLES";
    public static final String LOG_TRANSACCION_MOD_ARTICULO_PROMOCIONAL = "MOD_ARTICULO_PROMOCIONAL";
    public static final String LOG_TRANSACCION_MOD_PROMOCIONAL_CAMPANIA = "MOD_PROMOCIONAL_CAMPANIA";
    public static final String LOG_TRANSACCION_MOD_RUTA = "MOD_RUTA";
    public static final String LOG_TRANSACCION_MOD_TIPO_TRANSACCION = "MOD_TIPO_TRANSACCION";
    public static final String LOG_TRANSACCION_MOD_USUARIO_BUZON = "MOD_USUARIO_BUZON";
    public static final String LOG_TRANSACCION_MOD_VENDEDOR_DTS = "MOD_VENDEDOR_DTS";
    public static final String LOG_TRANSACCION_MOD_CUENTA = "MOD_CUENTA";
    public static final String LOG_TRANSACCION_MOD_REMESA = "MOD_REMESA";
    
    public static final String LOG_ASOCIA_RUTA_PDV = "ASOCIA_RUTA_PDV";
    public static final String LOG_TRANSACCION_SERVICIOS_MAPA = "SERVICIOS_MAPA";
    public static final String LOG_TRANSACCION_INSERT_LOG = "INSERT_LOG";
    
    // --------------------------------------------------------------------------------

    // Tipos de ID para el Log
    public static final String LOG_REPORTE_RECARGA = "REPORTE_RECARGA";
    public static final String LOG_TIPO_SOLICITUD = "SOLICITUD";
    public static final String LOG_TIPO_ASIGNACION_RESERVA = "ASIGNACION/RESERVA";
    public static final String LOG_TIPO_NINGUNO = "NINGUNO";
    public static final String LOG_TIPO_ARTICULO = "ARTICULO";
    public static final String LOG_TIPO_VENTA = "VENTA SIDRA";
    public static final String LOG_TIPO_TRASPASO = "TRASPASO_SCL";

    public static final String LOG_TIPO_ADJUNTO = "ADJUNTO";
    public static final String LOG_TIPO_ALTA_PREPAGO = "ALTA_PREPAGO";
    public static final String LOG_TIPO_BODEGA_ALMACEN = "BODEGA_ALMACEN";
    public static final String LOG_TIPO_BODEGA_VIRTUAL = "BODEGA_VIRTUAL";
    public static final String LOG_TIPO_BUZON = "BUZON";
    public static final String LOG_TIPO_CARGA_FILE = "CARGA_FILE";
    public static final String LOG_TIPO_CLIENTE = "CLIENTE";
    public static final String LOG_TIPO_CONDICION = "CONDICION";
    public static final String LOG_TIPO_CONDICION_OFERTA = "CONDICION_OFERTA";
    public static final String LOG_TIPO_DISPOSITIVO = "DISPOSITIVO";
    public static final String LOG_TIPO_DISTRIBUIDOR = "DISTRIBUIDOR";
    public static final String LOG_TIPO_FOLIO = "FOLIO";
    public static final String LOG_TIPO_JORNADA = "JORNADA";
    public static final String LOG_TIPO_OFERTA_CAMPANIA = "OFERTA_CAMPA\u00D1A";
    public static final String LOG_TIPO_PANEL = "PANEL";
    public static final String LOG_TIPO_PDV = "PDV";
    public static final String LOG_TIPO_ARTICULO_PROMOCIONAL = "ARTICULO_PROMOCIONAL";
    public static final String LOG_TIPO_RUTA = "RUTA";
    public static final String LOG_TIPO_TIPO_TRANSACCION = "TIPO_TRANSACCION";
    public static final String LOG_TIPO_USUARIO_BUZON = "USUARIO_BUZON";
    public static final String LOG_TIPO_VENDEDOR_DTS = "VENDEDOR_DTS";
    public static final String LOG_TIPO_VENDEDOR = "VENDEDOR";
    public static final String LOG_TIPO_CUENTA = "CUENTA";
    public static final String LOG_TIPO_REMESA = "REMESA";
    public static final String LOG_TIPO_SINCRONIZACION = "SINCRONIZACION";
    public static final String LOG_TIPO_ANULACION = "ANULACION";
    public static final String LOG_TIPO_FICHA_CLIENTE = "FICHA_CLIENTE";
    public static final String LOG_ACTIVACION_T_RASCA = "ACTIVACION_T_RASCA";

    /* NOMBRE DE SERVICIOS PARA LOG SIDRA */
    public static final String LOG_GET_SOLICITUDES = "WS OBTENER SOLICITUDES";
    public static final String LOG_POST_SOLICITUDES = "WS CREA SOLICITUD";
    public static final String LOG_PUT_SOLICITUDES = "WS MODIFICAR SOLICITUD";
    public static final String LOG_PUT_TRASLADO = "WS CREAR TRASLADOS";
    public static final String LOG_GET_ASIGNACIONES_RESERVAS = "WS OBTENER ASIGNACIONES/RESERVAS";
    public static final String LOG_POST_ASIGNACIONES_RESERVAS = "WS CREAR ASIGNACION/RESERVA";
    public static final String LOG_PUT_ASIGNACIONES_RESERVAS = "WS MODIFICAR ASIGNACION/RESERVA";
    public static final String LOG_WS_ACEPTA_RECHAZA_DEVOLUCION = "WS ACEPTA/RECHAZA DEVOLUCION";
    public static final String LOG_WS_INGRESO_SALIDAD_PROMOCIONALES = "WS INGRESO/SALIDA PROMOCIONALES";
    public static final String LOG_GET_INVENTARIO = "WS OBTENER INVENTARIO";
    public static final String LOG_GET_FICHA_CLIENTE = "WS OBTENER FICHA CLIENTE";
    public static final String LOG_POST_FICHA_CLIENTE = "WS CREA FICHA CLIENTE";
    public static final String LOG_PUT_FICHA_CLIENTE = "WS MODIFICA FICHA CLIENTE";
    
    public static final String LOG_REGISTRA_VISOR_TCK = "WS REGISTRA VISOR TCK";
    public static final String LOG_OBTIENE_VISOR_TCK = "WS OBTIENE VISOR TCK";

    public static final String LOG_GET_BODEGAS_SCL = "WS OBTENER BODEGAS SCL";
    public static final String LOG_GET_ARTICULOS = "WS OBTENER ARTICULOS";
    public static final String LOG_VALIDA_SERIES = "WS VALIDA SERIES";
    public static final String LOG_GET_HISTORICO = "WS OBTENER HISTORICO";
    public static final String LOG_POST_ADJUNTO = "WS CREA ADJUNTO GESTION";
    public static final String LOG_GET_ADJUNTO = "WS OBTENER ADJUNTO GESTION";
    public static final String LOG_DEL_ADJUNTO = "WS ELIMINAR ADJUNTO GESTION";
    public static final String LOG_POST_ALTA_PREPAGO = "WS REGISTRAR ALTA PREPAGO";
    
    public static final String LOG_GET_BODEGA_ALMACEN = "WS OBTENER BODEGAS";
    public static final String LOG_POST_BODEGA_ALMACEN = "WS CREAR BODEGAS";
    public static final String LOG_PUT_BODEGA_ALMACEN = "WS MODIFICAR BODEGAS";
    
    public static final String LOG_GET_BODEGA_VIRTUAL = "WS OBTENER BODEGA VIRTUAL";
    public static final String LOG_POST_BODEGA_VIRTUAL = "WS CREAR BODEGA VIRTUAL";
    public static final String LOG_PUT_BODEGA_VIRTUAL = "WS MODIFICAR BODEGA VIRTUAL";

    public static final String LOG_GET_BUZON = "WS OBTENER BUZON";
    public static final String LOG_POST_BUZON = "WS CREAR BUZON";
    public static final String LOG_PUT_BUZON = "WS MODIFICAR BUZON";

    public static final String LOG_GET_CARGA_FILE = "WS OBTENER IMAGEN";
    public static final String LOG_POST_CARGA_FILE = "WS CREAR CARGA DE IMAGEN";
    public static final String LOG_DEL_CARGA_FILE = "WS ELIMINAR CARGA DE IMAGEN";
    public static final String LOG_PUT_CARGA_FILE = "WS MODIFICAR CARGA DE IMAGEN";
    
    public static final String LOG_GET_CATALOGO = "WS OBTENER CATALOGO";
    public static final String LOG_POST_CATALOGO = "WS CREAR CATALOGO";
    public static final String LOG_PUT_CATALOGO = "WS MODIFICAR CATALOGO";
    
    public static final String LOG_GET_CLIENTE = "WS OBTENER CLIENTE";
    public static final String LOG_POST_CLIENTE = "WS CREAR CLIENTE";
    public static final String LOG_PUT_CLIENTE = "WS MODIFICAR CLIENTE";
    
    public static final String LOG_GET_CONDICION = "WS OBTENER CONDICION CAMPA\u00D1A";
    public static final String LOG_POST_CONDICION = "WS CREAR CONDICION CAMPA\u00D1A";
    public static final String LOG_PUT_CONDICION = "WS MODIFICAR CONDICION CAMPA\u00D1A";
    
    public static final String LOG_GET_CONDICION_OFERTA = "WS OBTENER CONDICION OFERTA";
    public static final String LOG_POST_CONDICION_OFERTA = "WS CREAR CONDICION OFERTA";
    public static final String LOG_PUT_CONDICION_OFERTA = "WS MODIFICAR CONDICION OFERTA";

    public static final String LOG_GET_CANT_INV = "WS OBTENER CANTIDAD INVENTARIO";
    public static final String LOG_GET_DATOS_PAIS = "WS OBTENER DATOS PAIS";
    public static final String LOG_GET_DATOS_SERIES = "WS OBTENER DATOS SERIES";
    public static final String LOG_GET_DATOS_IMPUESTOS = "WS OBTENER DATOS IMPUESTOS";
    
    public static final String LOG_GET_INVENTARIO_WEB = "WS OBTENER INVENTARIO WEB";
    public static final String LOG_GET_INVENTARIO_MOVIL = "WS OBTENER INVENTARIO MOVIL";

    public static final String LOG_GET_DISPOSITIVO = "WS OBTENER DISPOSITIVO";
    public static final String LOG_POST_DISPOSITIVO = "WS CREAR DISPOSITIVO";
    public static final String LOG_PUT_DISPOSITIVO = "WS MODIFICAR DISPOSITIVO";

    public static final String LOG_GET_DISTRIBUIDOR = "WS OBTENER DISTRIBUIDOR";
    public static final String LOG_POST_DISTRIBUIDOR = "WS CREAR DISTRIBUIDOR";
    public static final String LOG_PUT_DISTRIBUIDOR = "WS MODIFICAR DISTRIBUIDOR";

    public static final String LOG_GET_FOLIOVIRTUAL = "WS OBTENER FOLIO VIRTUAL";
    public static final String LOG_POST_FOLIOVIRTUAL = "WS CREAR FOLIO VIRTUAL";
    public static final String LOG_PUT_FOLIOVIRTUAL = "WS MODIFICAR FOLIO VIRTUAL";
    
    public static final String LOG_GET_JORNADA = "WS OBTENER JORNADA";
    public static final String LOG_POST_JORNADA = "WS CREAR JORNADA";
    public static final String LOG_PUT_JORNADA = "WS MODIFICAR JORNADA";
    
    public static final String LOG_GET_FECHA_JORNADA = "WS OBTENER FECHA JORNADA";
    public static final String LOG_POST_FECHA_JORNADA = "WS CREAR FECHA JORNADA";

    public static final String LOG_GET_OFERTA_CAMPANIA = "WS OBTENER OFERTA/CAMPA\u00D1A";
    public static final String LOG_POST_OFERTA_CAMPANIA = "WS CREAR OFERTA/CAMPA\u00D1A";
    public static final String LOG_PUT_OFERTA_CAMPANIA = "WS MODIFICAR OFERTA/CAMPA\u00D1A";

    public static final String LOG_GET_OFERTA_CAMPANIA_MOVIL = "WS OBTENER OFERTA/CAMPA\u00D1A M\u00F3VIL";
    
    public static final String LOG_GET_PANEL = "WS OBTENER PANEL";
    public static final String LOG_POST_PANEL = "WS CREAR PANEL";
    public static final String LOG_PUT_PANEL = "WS MODIFICAR PANEL";
    
    public static final String LOG_GET_PDV = "WS OBTENER PUNTO DE VENTA";
    public static final String LOG_POST_PDV = "WS CREAR PUNTO DE VENTA";
    public static final String LOG_PUT_PDV = "WS MODIFICAR PUNTO DE VENTA";
    public static final String LOG_GET_PDV_DISPONIBLES = "WS OBTENER PUNTO DE VENTA DISPONBILE";
    public static final String LOG_COUNT_PDV = "WS CUENTA EL TOTAL DE PUNTOS DE VENTA";
    
    public static final String LOG_GET_PROMOCIONALES = "WS OBTENER PROMOCIONALES";
    public static final String LOG_POST_PROMOCIONALES = "WS CREAR PROMOCIONALES";
    public static final String LOG_PUT_PROMOCIONALES = "WS MODIFICAR PROMOCIONALES";

    public static final String LOG_GET_PROMOCIONAL_CAMPANIA = "WS OBTENER PROMOCIONAL DE CAMPA\u00D1A";
    public static final String LOG_POST_PROMOCIONAL_CAMPANIA = "WS ASIGNAR PROMOCIONAL DE CAMPA\u00D1A";
    
    public static final String LOG_GET_RUTA = "WS OBTENER RUTA";
    public static final String LOG_POST_RUTA = "WS CREAR RUTA";
    public static final String LOG_PUT_RUTA = "WS MODIFICAR RUTA";

    public static final String LOG_GET_TIPO_TRANSACCION = "WS OBTENER TIPO DE TRANSACCION";
    public static final String LOG_POST_TIPO_TRANSACCION = "WS CREAR TIPO DE TRANSACCION";
    public static final String LOG_PUT_TIPO_TRANSACCION = "WS MODIFICAR TIPO DE TRANSACCION";

    public static final String LOG_GET_VENDEDORDTS = "WS OBTENER VENDEDORES ASIGNADOS A DTS";
    public static final String LOG_POST_VENDEDORDTS = "WS CREAR ASIGNACION DE VENDEDOR A DTS";
    public static final String LOG_PUT_VENDEDORDTS = "WS MODIFICAR ASIGNACION DE VENDEDOR A DTS";
    
    public static final String LOG_GET_VENDEDORXDTS = "WS OBTENER VENDEDORES POR DTS";
    public static final String LOG_GET_VERSION_CATALOGO = "WS OBTENER VERSIONES DE CATALOGO";

    public static final String LOG_GET_USUARIO_BUZON = "WS OBTENER USUARIOS ASIGNADOS A BUZON";
    public static final String LOG_POST_USUARIO_BUZON = "WS CREAR ASIGNACION DE USUARIO A BUZON";
    public static final String LOG_PUT_USUARIO_BUZON = "WS MODIFICAR ASIGNACION DE USUARIO A BUZON";
    
    public static final String LOG_GET_VISITA = "WS OBTENER VISITA";
    public static final String LOG_POST_VISITA = "WS REGISTRAR VISITA";
    
    public static final String LOG_GET_VENTA = "WS OBTENER VENTA";
    public static final String LOG_GET_VENTA_DET = "WS OBTENER DETALLE DE VENTA";
    public static final String LOG_POST_VENTA = "WS REGISTRAR VENTA";
    
    public static final String LOG_GET_CUENTA = "WS OBTENER CUENTA";
    public static final String LOG_POST_CUENTA = "WS CREAR CUENTA";
    public static final String LOG_PUT_CUENTA = "WS MODIFICAR CUENTA";
    
    public static final String LOG_GET_REMESA = "WS OBTENER REMESA";
    public static final String LOG_POST_REMESA = "WS CREAR REMESA";
    public static final String LOG_PUT_REMESA = "WS MODIFICAR REMESA";
    
    public static final String LOG_GET_SINCRONIZACION = "WS OBTENER SINCRONIZACION";
    public static final String LOG_POST_SINCRONIZACION = "WS CREAR SINCRONIZACION";
    
    public static final String LOG_GET_REPORTE = "LOG_GET_REPORTE";
    
    public static final String LOG_GET_CONSULTA_NUM_RECARGA = "WS OBTENER ESTADO NUMERO RECARGA";
    
    public static final String LOG_GET_FOLIOS_FS = "WS OBTENER FOLIOS FS";
    public static final String LOG_POST_FOLIOS_FS = "WS RESERVAR FOLIOS FS";
    public static final String FS_GET_ALL_DATOS_CLIENTE = "FS_GET_ALL_DATOS_CLIENTE";
    public static final String FS_UPDATE_DATOS_CONSUMER="FS_UPDATE_CONSUMER";
    public static final String FS_GET_DATOS_CONSUMER="FS_GET_PRODUCT_CUSTOMER";
    public static final String FS_ADD_CUSTOMER_ATTACHMENT="FS_ADD_CUSTOMER_ATTACHMENT";
    
    public static final String LOG_GET_FOLIOS_SCL = "WS OBTENER FOLIOS SCL";
    public static final String LOG_GET_OFICINAS_SCL = "WS OBTENER OFICINAS SCL";

    public static final String LOG_GET_REPORTEXZ = "WS OBTENER REPORTE XZ";
    public static final String LOG_GET_REPORTE_PDV = "WS OBTENER REPORTE PDV";
    public static final String LOG_GET_REPORTE_CANT_VENDIDA = "WS OBTENER REPORTE CANT VENDIDA";
    public static final String LOG_GET_REPORTE_CTA_DTS_DEBE = "WS OBTENER REPORTE CTA DTS DEBE";
    public static final String LOG_GET_REPORTE_CTA_DTS_HABER = "WS OBTENER REPORTE CTA DTS HABER";
    public static final String LOG_GET_REPORTE_CTA_DTS_RESUMEN = "WS OBTENER REPORTE CTA DTS RESUMEN";
    public static final String LOG_POST_REPORTE_CTA_DTS_RESUMEN = "WS AGREGAR DATOS HABER CTA DTS";
    
    public static final String LOG_GET_CONTROL_PRECIOS = "WS OBTENER CONTROL PRECIOS";

    public static final String LOG_GET_SALDO_PAYMENT = "WS CONSULTA SALDO PAYMENT";

    public static final String LOG_GET_ANULACION = "WS CONSULTA ANULACIONES";
    public static final String LOG_POST_ANULACION = "WS CREAR ANULACION";
    
    public static final String LOG_CREA_ASOCIACION = "WS CREAR ASOCIACION";
    public static final String LOG_ELIMINA_ASOCIACION = "WS ELIMINA ASOCIACION";
    
    public static final String LOG_INSERT = "WS INSERT_LOG";
    
    public static final String LOG_GET_RESUMEN_DEUDA = "WS OBTENER RESUMEN DEUDA";
    
    public static final String LOG_GET_OFERTA_FS = "WS OBTENER OFERTAS FS";
    
    public static final String LOG_GET_DESCUENTO_FS = "WS OBTENER DESCUENTO FS";

    /*----------------------------TRANSACCIONES INVENTARIO--------------------------*/
    public static final String T_SALIDA = "S";
    public static final String T_INGRESO = "I";
    public static final String T_DEVOLUCION = "D";
    public static final String T_SINESTRO = "SN";

    /* TIPOS DE GESTI\u00F3N PARA LAS VISITAS A PUNTOS DE VENTA */
    public static final String TIPO_GESTION_GRUPO = "GESTION_VISITA";
    public static final String GESTION_VENTA = "VENTA";
    public static final String GESTION_NO_VENTA = "NO VENTA";
    public static final String GESTION_OBSERVACION = "OBSERVACION";

    // PARAMETROS PARA TIPO CLIENTE EN VENTAS
    public static final String CLIENTE_PDV = "PDV";
    public static final String CLIENTEFINAL = "CF";
    
    // PARAMETROS PARA TIPO CLIENTE EN OFERTAS
    public static final String CLIENTE_OFERTA_PDV = "PDV";
    public static final String CLIENTE_OFERTA_CF = "CF";
    public static final String CLIENTE_OFERTA_AMBOS = "AMBOS";

    // PARAMETROS PARA GRUPOS DE IMPUESTOS
    public static final String IMPUESTOS_NOMBRE = "IMPUESTO_PAIS";
    public static final String GRUPO_DESCUENTO_PAIS = "DESCUENTO_PAIS";
    public static final String IMPUESTOS_DESCUENTO = "IMPUESTO_PAIS_DESCUENTO";

    // ESTADOS PARA VENTAS
    public static final String VENTA_REGISTRADO_SIDRA = "REGISTRADO_SIDRA";
    public static final String VENTA_ANULADO = "ANULADO";
    public static final String VENTA_ANULADO_SCL = "ANULADO_SCL";
    // ESTADOS DE FOLIOS
    public static final String FOLIO_FINALIZADO = "FINALIZADO";
    public static final String FOLIO_EN_USO = "EN_USO";

    // ESTADOS DE REMESAS
    public static final String REMESA_ESTADO_ALTA = "ALTA";
    public static final String REMESA_ESTADO_BAJA = "BAJA";
    public static final String REMESA_ESTADO_ANULADA = "ANULADA";

    // PARAMETROS PARA CLIENTES EXENTO O NO EXENTO
    public static final String TIPO_EXENTO = "EXENTO";
    public static final String TIPO_NO_EXENTO = "NOEXENTO";

    // PARAMETROS PARA TIPO DOCUMENTO EN VENTAS
    public static final String DOCUMENTO_TCK = "TKT";
    public static final String DOCUMENTO_CCF = "CCF";
    public static final String DOCUMENTO_TCF = "TCF";
    public static final String DOCUMENTO_FFP = "FFP";
    

    // IMPUESTOS
    public static final String IMPUESTO_IVA = "IVA";

    public static final String FORMA_PAGO_TARJETA = "TARJETA";
    public static final String FORMA_PAGO_CHEQUE = "CHEQUE";
    public static final String FORMA_PAGO_EFECTIVO = "EFECTIVO";
    public static final String FORMA_PAGO_CREDITO="CREDITO";
    public static final String FORMA_PAGO_MPOS="MPOS";

    // NUEVOS PARAMETROS PDV
    public static final String TIPO_N_ABARROTERIA = "ABARROTERIA";
    public static final String TIPO_N_FARMACIA = "FARMACIA";
    public static final String TIPO_N_TIENDA = "TIENDA";
    public static final String CATEGORIA_A = "A";
    public static final String CATEGORIA_B = "B";
    public static final String CATEGORIA_C = "C";
    public static final String PEQ_CONTRIBUYENTE = "PequenioContribuyente";
    public static final String GRAN_CONTRIBUYENTE = "GranContribuyente";
    public static final String TIPO_P_FISICO = "FISICO";
    public static final String TIPO_P_FVIRTUAL = "FISICO_VIRTUAL";
    public static final String CANAL_SIDRA = "SIDRA";
    public static final String SUBCANAL_DTS = "DTS";
    public static final String DOCUMENTO_FISCAL = "DOCUMENTO";
    public static final String DOC_FISCAL_GENERICO = "GENERICO";
    public static final String INFORMACION_FISCAL = "INFORMACION_FISCAL";

    // PARAMETROS REPORTES
    public static final String TIPO_REPORTEX = "TIPO_REPORTEX";
    public static final String TIPO_REPORTEZ = "TIPO_REPORTEZ";
    public static final String TIPO_REPORTEZM = "TIPO_REPORTEZM";
    
    public static final String PAIS_GRUPO = "PAIS";
    public static final String PAIS_DEPTO_GRUPO = "DEPTO_";
    public static final String PAIS_MPIO_GRUPO = "MPIO_";
    public static final String PAIS_REGION_GRUPO = "REGION_SV_";
    
    public static final String GRUPO_BODEGASCL = "BODEGASCL";
    public static final String TIP_BODEGA = "TIP_BODEGA";

    // TODO Cambiar configuraciones espec\u00EDficas por pais.
    // TODO cambiar DBlink PROD o DESA
    //*-------------------------------------------------------------------- CONFIGURACIONES ESPEC\u00EDFICAS DE EL SALVADOR
    public static final String DOC_IDENT_FICHAPDV_NIT_RUC = "NIT";
    // ---------------------------------- CONFIGURACIONES ESPEC\u00EDFICAS DE EL SALVADOR */

    /*-------------------------------------------------------------------- CONFIGURACIONES ESPEC\u00EDFICAS DE NICARAGUA
    //PAR\u00E9METROS PARA SERVICIOS DE ARTICULOS
    public static final String GRUPO_WS_LOCAL_ARTICULOS = "WS_LOCAL_ARTICULOS";
    public static final String URL_SERVICIO_ARTICULOS = "SERVICIO_LOCAL_GETARTICULOS";
    public static final String GRUPO_SERVICIOS = "SERVICIOS_ARTICULOS";
    public static final String SERVICIOS_NUM_MESES = "NUM_MESES";
    public static final String SERVICIOS_COD_CATEGORIA = "COD_CATEGORIA";
    public static final String SERVICIOS_USUARIO_SCL = "USUARIO_SCL";

    //PAR\u00E9METROS PARA SERVICIO DE REGISTRAR VENTA
    public static final String GRUPO_SERVICIO_REG_VENTA = "SERVICIO_REG_VENTA";
    public static final String SERVICIOS_CODIGO_VENDEDOR = "CODIGO_VENDEDOR";
    public static final String SERVICIOS_CODIGO_CLIENTE = "CODIGO_CLIENTE";
    public static final String SERVICIOS_CODIGO_OFICINA = "CODIGO_OFICINA";
    public static final String SERVICIOS_COD_PLANTARIF = "COD_PLANTARIF";
    public static final String SERVICIOS_CODIGO_CUENTA = "CODIGO_CUENTA";
    public static final String SERVICIOS_USUARIO_ORACLE = "USUARIO_ORACLE";
    public static final String SERVICIOS_CODIGO_CENTRAL = "CODIGO_CENTRAL";
    public static final String SERVICIOS_TIPO_DESCUENTO = "TIPO_DESCUENTO";

    //PAR\u00E9METROS PARA CONSULTA DE ART\u00EDCULOS DEL SISTEMA COMERCIAL
    public static final String TIPO_ARTICULO = "TIPO_ARTICULO";
    public static final String ARTICULO_COD_TIENDA_VALOR = "ARTICULO_COD_TIENDA_VALOR";
    public static final String ARTICULO_AL_DCTO_ARTTIENDA = "AL_DCTO_ARTTIENDA"; //nombre de tabla
    public static final String ARTICULO_VAL_DCTO = "VAL_DCTO"; //nombre de columna
    public static final String ARTICULO_COD_TIENDA = "COD_TIENDA"; //nombre de columna
    public static final String ARTICULO_COD_ARTICULO = "COD_ARTICULO"; //nombre de columna

    public static final String DOC_IDENT_FICHAPDV_NIT_RUC = "RUC";

    //DBLink NI DESA
    public static final String DBLINK_SCL = "@LINK_SCLNIC25_SISCEL";

    //DBLink NI PRODUCCION
    //public static final String DBLINK_SCL = "@LNK_SCL";
    // ----------------------------------- CONFIGURACIONES ESPEC\u00EDFICAS DE EL NICARAGUA */

    /*------------------------------------------------------------------- CONFIGURACIONES ESPEC\u00EDFICAS DE PANAM\u00E9
    //PAR\u00E9METROS PARA SERVICIOS DE ARTICULOS
    public static final String URL_SERVICIO_ARTICULOS = "SERVICIO_LOCAL_GETARTICULOS";
    public static final String GRUPO_SERVICIOS = "SERVICIOS_ARTICULOS";
    public static final String SERVICIOS_TIP_STOCK = "TIP_STOCK";
    public static final String SERVICIOS_COD_USO = "COD_USO";
    public static final String SERVICIOS_COD_ESTADO = "COD_ESTADO";
    public static final String SERVICIOS_IND_RECAMBIO = "IND_RECAMBIO";
    public static final String SERVICIOS_COD_CATEGORIA = "COD_CATEGORIA";
    public static final String SERVICIOS_NUM_MESES = "NUM_MESES";
    public static final String SERVICIOS_COD_PROMEDIO = "COD_PROMEDIO";
    public static final String SERVICIOS_COD_ANTIGUEDAD = "COD_ANTIGUEDAD";
    public static final String SERVICIOS_COD_MODVENTA = "COD_MODVENTA";

    //PAR\u00E9METROS PARA CONSULTA DE ART\u00EDCULOS DEL SISTEMA COMERCIAL
    public static final String TIPO_ARTICULO = "TIPO_ARTICULO";
    public static final String ARTICULO_COD_TIENDA_VALOR = "ARTICULO_COD_TIENDA_VALOR";
    public static final String ARTICULO_AL_DCTO_ARTTIENDA = "AL_DCTO_ARTTIENDA"; //nombre de tabla
    public static final String ARTICULO_VAL_DCTO = "VAL_DCTO"; //nombre de columna
    public static final String ARTICULO_COD_TIENDA = "COD_TIENDA"; //nombre de columna
    public static final String ARTICULO_COD_ARTICULO = "COD_ARTICULO"; //nombre de columna

    public static final String DOC_IDENT_FICHAPDV_NIT_RUC = "RUC";

    // DBLink DESA
    public static final String DBLINK_SCL = "@LINK_PRESCLPA_SISCEL";
    // ---------------------------------- CONFIGURACIONES ESPEC\u00EDFICAS DE PANAM\u00E9 */

    /*------------------------------------------------------------------- CONFIGURACIONES ESPEC\u00EDFICAS DE COSTA RICA
    //GRUPOS PARA FICHA CLIENTE SCL 
    public static final String GRUPO_FICHA_CLIENTE = "FICHA_CLIENTE";
    public static final String FC_CATEGORIA = "FC_CATEGORIA";
	public final static String FC_TIPO_CUENTA = "FC_TIPO_CUENTA"; 
    public static final String FC_CODIGO_CICLO = "FC_CODIGO_CICLO";
    public final static String FC_CAT_TRIBUTARIA = "FC_CAT_TRIBUTARIA";
    public final static String FC_CAT_IMPOS_PDV = "FC_CAT_IMPOS_PDV";

    public static final String DOC_IDENT_FICHAPDV_NIT_RUC = "CEDULA RESIDENTE";

    // DBLink CR DESA
    public static final String DBLINK_SCL = "@LINK_SCLPRECR_SISCEL";

    // DBLink CR PRODUCTIVO 
    //public static final String DBLINK_SCL = "@LNK_SCLCSR";

	//config de activacion tarjeta rasca
	public static String GRUPO_PROCESO_ACTIVACION_RASCA = "PROCESO_ACTIVACION_RASCA";
	public static String TUX_ACTIVACION_RASCA = "TUX_ACTIVACION_RASCA";
	public static String TUX_COD_DESTINO = "TUX_COD_DESTINO";
	public static String TUX_COD_IDIOMA = "TUX_COD_IDIOMA";
	public static String TUX_COD_USUARIO = "TUX_COD_USUARIO";
	public static String ACT_ESTADO_ACTIVO = "2";
	public static String ACT_ESTADO_PREACTIVO = "1";
	public static String TUX_DIG_CONTROL = "TUX_DIG_CONTROL";
	public static String TUX_DIGITO_VALIDADOR = "TUX_DIGITO_VALIDADOR";
    // ---------------------------------- CONFIGURACIONES ESPEC\u00EDFICAS DE COSTA RICA */	

    // TIPOS DE CLIENTES EN LA CREACION DE FICHA SCL
    public static final String FICHA_CLIENTE_TIPO_PDV = "PDV";
    public static final String FICHA_CLIENTE_TIPO_DTS = "DTS";
    public static final String FICHA_CLIENTE_TIPO_CF = "CF";

    // PARAMETRO DE NIVELES DE BUZONES
    public static final int NIVEL_BUZON_TELCA = 1;
    public static final int NIVEL_BUZON_DTS = 2;

    //NIVEL DE BUZON PARA 
    public static final int NIVEL_ZONACOMERCIAL = 3;
    
    public static final String GRUPO_NIVEL_BUZON_LOGIN = "NIVEL_BUZON_LOGIN";



    // PARAMETROS
    public static final int ASOCIACION_PDV = 1;
    public static final int NO_ASOCIACION_PDV = 0;
    
    public static final String TIPO_GRUPO_BONO = "BONO";
    public static final String TIPO_GRUPO_RECARGA = "RECARGA";
    public static final String TIPO_GRUPO_TARJETASRASCA = "TARJETASRASCA";
    public static final String GRUPO_TIPO_ARTICULOS_INV = "TIPO_ARTICULOS_INV";
    
    // MONEDA
    public static final String GRUPO_MONEDAS_PRECIO = "MONEDAS_PRECIO";
    public static final String COD_MONEDA_DOLAR = "DOLAR";

    // TIPO DTS
    public static final String DTS_INTERNO = "INTERNO";
    public static final String DTS_EXTERNO = "EXTERNO";
    // SALDO PAYMENT
    public static final String URL_SALDO_PAYMENT = "URL_SALDO_PAYMENT";

    // PARAMETRO DE PAIS PARA MAPAS
    public static final String MAPAS_GUATEMALA = "GT";
    public static final String MAPAS_EL_SALVADOR = "SV";
    public static final String MAPAS_COSTARICA = "CR";
    public static final String MAPAS_NICARAGUA = "NI";
    public static final String MAPAS_PANAMA = "PA";
    
    //Tablas con particiones
    public static final int TC_SC_ASIGNACION_DET = 1;
    public static final int TC_SC_ASIGNACION_RESERVA = 2;
    public static final int TC_SC_BODEGAVIRTUAL = 3;
    public static final int TC_SC_CANT_INV_JORNADA = 4;
    public static final int TC_SC_CLIENTEF_SCL = 5;
    public static final int TC_SC_CTA_DEBE_DTS = 6;
    public static final int TC_SC_CTA_DTS_RESUMEN = 7;
    public static final int TC_SC_CTA_HABER_DTS = 8;
    public static final int TC_SC_HISTORICO_INVSIDRA = 9;
    public static final int TC_SC_INVENTARIO = 10;
    public static final int TC_SC_JORNADA_VEND = 11;
    public static final int TC_SC_LOG_SIDRA = 12;
    public static final int TC_SC_OFERTA_CAMPANIA = 13;
    public static final int TC_SC_PUNTOVENTA = 14;
    public static final int TC_SC_REMESA = 15;
    public static final int TC_SC_REPORTE_XZ = 16;
    public static final int TC_SC_SINC_VENDEDOR = 17;
    public static final int TC_SC_SOLICITUD = 18;
    public static final int TC_SC_VEND_DTS = 19;
    public static final int TC_SC_VENTA = 20;
    public static final int TC_SC_VENTA_DET = 21;
    public static final int TC_SC_VISITA = 22;
    
    //Tipos de partici\u00F3n
    public static final String PARTITION = "PARTITION";
    public static final String SUBPARTITION = "SUBPARTITION";
    public static final String PARTITION_DATE = "PARTITION_DATE";
    
    
    //URLS  SERVICIOS FS
    public static final String GRUPO_URL_FS_WS = "URL_WS_FS";
    public static final String FS_WS_GETBODEGAS = "FS_GETBODEGAS";
    public static final String FS_WS_CREA_CLIENTE = "FS_CREA_CLIENTE";
    public static final String FS_WS_CREA_CUENTA = "FS_CREA_CUENTA";
    public static final String FS_CREA_DIRECCION = "FS_CREA_DIRECCION";
    public static final String FS_CREA_UBICACION = "FS_CREA_UBICACION";
    public static final String FS_GET_DATOS_CLIENTE = "FS_GET_DATOS_CLIENTE";
    public static final String FS_RESERVA_FOLIOS = "FS_RESERVA_FOLIOS";
    public static final String FS_CANCELAR_FOLIOS = "FS_CANCELAR_FOLIOS";
    public static final String FS_VALIDAR_CIP = "FS_VALIDAR_CIP";
    public static final String FS_VALIDAR_DONANTE = "FS_VALIDAR_DONANTE";
    public static final String FS_WS_PRODUCTO_BYID = "FS_GET_PRODUCT_CUSTOMER_BYID";
    
    //CONFIGURACION CLIENTE FS
    public static final String FICHA_CLIENTE_FS_DIRECCION = "DIRECCION_CLIENTE";
    public static final String FICHA_CLIENTE_FS_CATEGORY = "CUSTOMER_CATEGORY";
    public static final String FICHA_CLIENTE_FS_STATUS_CUSTOMER_ACCOUNT = "STATUS_CUSTOMER_ACCOUNT";
    public static final String FICHA_CLIENTE_FS_STATUS_CUSTOMER_CONTACT = "STATUS_CUSTOMER_ACCOUNT_CONTACT";
    public static final String FICHA_CLIENTE_FS_SEGMENT_RESIDENCIAL = "RESIDENCIAL";
    public static final String FICHA_CLIENTE_FS_MALE = "MALE";
    public static final String FICHA_CLIENTE_FS_FEMALE = "FEMALE";
    public static final String FICHA_CLIENTE_FS_SALARIO = "SALARIO";
    public static final String FICHA_CLIENTE_FS_DUI = "DUI";
    public static final String FICHA_CLIENTE_FS_PASAPORTE = "PASAPORTE";
    public static final String FICHA_CLIENTE_FS_CARNET_RESIDENCIA = "CARNET_RESIDENCIA";

    //CONFIGURACION CUENTA FS
    public static final String CUENTA_CLIENTE_FS_MONEDA_$= "MONEDA_DOLAR";
    public static final String CUENTA_CLIENTE_FS_FORMA_P_EFECTIVO= "FORMA_PAGO_EFECTIVO";
    public static final String CUENTA_CLIENTE_FS_LENG_ESPANIOL= "LENGUAJE_ESPANIOL";
    public static final String CUENTA_CLIENTE_FS_CREDIT_CLASS_ID= "CREDIT_CLASS_ID";
    
    //CONFIGURACION DIRECCIONES
    public static final String DIRECCION_TYPE_GEOGRAPHIC_AREA = "TYPE_GEOGRAPHIC_AREA";
    public static final String TIPO_DOC_FS = "TIPO_DOCUMENTO_FOLIO_FS";
    
    // VENTAS valida portabilidad
    public static final String SERVICIO_LOCAL_VALIDA_PORTABILIDAD = "SERVICIO_LOCAL_VALIDA_PORTABILIDAD";
    
    public static final String SERVICIO_LOCAL_ESTADO_PORTA="WS ESTADO PORTA";
    
    public static final String LOG_GET_SOLICITUDES_CIP="WS SOLICITUDES SIP";
    public static final String LOG_SERVICIO_CRE_PORTA="WS CREAR PORTABILIDAD";
    public static final String FS_SOLICITUDES_CIP="FS_SOLICITUDES_CIP";
    public static final String GRUPO_TIPO_DOCUMENTO_DONANTE="TIPO_PRODUCTO_DONANTE";
   
    
    //CONFIGUARACION CONSULTA DE CIP
    public static final String  GRUPO_CONSULTA_CIP="CONSULTA_CIP";
    public static final String  OPERADOR_DONANTE="OPERADOR_DONANTE";
    public static final String  RESPUESTA_CERVICIO="RESPUESTA_CERVICIO";
    
    //CONFIGURACION DOCUMENTOS 
    public static final String  GRUPO_TIPO_DOCUMENTO="TIPO_DOCUMENTO";
    public static final String  TIPO_DOCUMENTO_Carne_Residente="Carne Residente";
    public static final String  TIPO_DOCUMENTO_PASAPORTE="PASAPORTE";
    public static final String  TIPO_DOCUMENTO_DUI="DUI";
    
  //CONF PRECIO ARTICULOS
    public static final String LOG_GET_ARTICULO_PRECIO = "LOG_GET_ARTICULO_PRECIO";
    public static final String LOG_POST_ARTICULO_PRECIO = "LOG_POST_ARTICULO_PRECIO";
    public static final String LOG_PUT_ARTICULO_PRECIO = "LOG_PUT_ARTICULO_PRECIO";
    public static final String LOG_TRANSACCION_CREA_ARTICULO_PRECIO = "CREA_ARTICULO_PRECIO";
    public static final String LOG_TIPO_ARTICULO_PRECIO = "ARTICULO_PRECIO";
    public static final String DESC_MONEDA = "DESC_MONEDA";
    public static final String LOG_TRANSACCION_MOD_ARTICULO_PRECIO = "MOD_ARTICULO_PRECIO";
    
    
    //SO -PORTABILIDAD
    public static final String GRUPO_URL_WS_FS = "URL_WS_FS";
    public static final String FS_MODIFICA_ITEM_ORDEN_VENTA = "FS_MODIFICA_ITEM_ORDEN_VENTA";
    public static final String GRUPO_CARACTERISTICAS_PORTA_FS = "CARACTERISTICAS_PORTA_FS";
    public static final String C_NUMERO_TEMPORAL = "numero_temporal";
    public static final String C_NIP = "nip";
    public static final String C_NUMERO_PORTAR = "numero_portar";
    public static final String C_OP_DONANTE = "op_donante";
    public static final String C_OP_RECEPTOR = "op_receptor";
    public static final String C_VALIDATION = "validation";
    public static final String C_RAZONVENTA = "razon_venta";
    
    public static final String FS_OBTIENE_ORDEN_VENTA = "GET_SALES_ORDER";
    
    public static final String CANAL_SIDRA_SO = "CANAL_SIDRA_SO";
    public static final String FS_CREA_ORDEN_VENTA =  "FS_CREA_ORDEN_VENTA";
    public static final String OP_TELEFONICA =  "telefonica";
    public static final String FS_FINALIZA_ORDEN_VENTA =  "FS_FINALIZA_ORDEN_VENTA";
    
    public static final String GRUPO_PRODUCT_CUSTOMER =  "VALOR_WS_PRODUCT_CUSTOMER";
    public static final String VALOR_PRODUCT=  "ACTIVE";
    public static final String VALOR_IDATTRIBUTE=  "IDATTRIBUTEVALUEPAIR";
    
    public static final String GRUPO_ESTADO_NUM_PORTA =  "ESTADOS_NUM_PORTABILIDAD";
    
    public static final String DBLINK_SCL = "@LINK_SCLNIC25_SISCEL";
    public static final String GRUPO_RUTA_ATTACHMENT="RUTA_CUSTOMERATTACHMENT";
    public static final String RUTA_ARCHIVO="RUTA DE ARCHIVOS";
    
    public static final String GRUPO_CONFIG_PERCEPCION="CONFIG_PERCEPCION";
    public static final String MONTO_MINIMO_PERCEPCION="MONTO_MINIMO_PERCEPCION";
    
    //CREA NUMERO
    public static final String  GRUPO_CREA_NUMERO="CREA_NUMERO";
    public static final String  TIPO_PRODUCTO_M_PREPAGO="TIPO_PRODUCTO_M_PREPAGO";
    public static final String FS_CREA_NUMERO = "CREANUMERO_FS";
    
    public static final String JORNADA_ESTADO_PAGADA_ZONA_COMERCIAL = "PAGADA ZONA COMERCIAL";
    
    public static final String OFERTA_PORTABILIDAD = "OFERTA_PORTABILIDAD";
    public static final String GRUPO_FTP_PORTABILIDAD = "DATOS_FTP";
    public static final String TIPO_FTP_RUTA = "RUTA DE ARCHIVOS";
    public static final String TIPO_FTP_IP = "IP";
    public static final String TIPO_FTP_USUARIO = "USUARIO";
    public static final String TIPO_FTP_CLAV = "CLAVE";
    public static final String TIPO_FTP_PUERTO = "PUERTO";
    
    //public static final String COD_AREA = "503"; Corregido RV no se utilizan estaticas
    
    /*crea direccion ficha de cliente*/
    public static final String V_DIR_TypeGeographicArea = "DIR_TypeGeographicArea";
    public static final String G_FS_CREA_FICHA_CLIENTE = "FS_CREA_FICHA_CLIENTE";
    
    /*crea cliente ficha cliente*/
    public static final String V_CLIE_IDSegmentCustomer = "CLIE_IDSegmentCustomer";
    public static final String V_CLIE_IDCustomerCategory = "CLIE_IDCustomerCategory";
    public static final String V_CLIE_CustomerStatusCustomer = "CLIE_CustomerStatusCustomer";
    public static final String V_CLIE_LegalAddressIdCustomerAccount = "CLIE_LegalAddressIdCustomerAccount";
    public static final String V_CLIE_GenderIndividual = "CLIE_GenderIndividual";
    public static final String V_CLIE_ExistsDuringOrganization = "CLIE_ExistsDuringOrganization";
    public static final String V_CLIE_NameLanguage = "CLIE_NameLanguage";
    public static final String V_CLIE_StatusCustomerAccountContact = "CLIE_StatusCustomerAccountContact";
    public static final String V_CLIE_OccupationIDIndividual = "CLIE_OccupationIDIndividual";
    public static final String V_CLIE_CLIE_SalaryIndividual = "CLIE_SalaryIndividual";
    public static final String V_CLIE_IDContactMedium = "CLIE_IDContactMedium";
    public static final String V_CLIE_TypeContactMedium = "CLIE_TypeContactMedium";
    public static final String V_CLIE_AttributeNameAttributeValuePair = "CLIE_AttributeNameAttributeValuePair";
    public static final String V_CLIE_AttributeTypeAttributeValuePair = "CLIE_AttributeTypeAttributeValuePair";
    public static final String V_CLIE_NameTypeGeographicLocationName = "CLIE_NameTypeGeographicLocationName";
    public static final String V_CLIE_NameGeographicLocationName = "CLIE_NameGeographicLocationName";
    public static final String V_CLIE_IDGeographicAddress = "CLIE_IDGeographicAddress";
    public static final String V_CTA_DescriptionCustomerAccount = "CTA_DescriptionCustomerAccount";
    public static final String V_CTA_IdMoney = "CTA_IdMoney";
    public static final String V_CTA_IDPaymentMethod = "CTA_IDPaymentMethod";
    public static final String V_CTA_IDLanguage = "CTA_IDLanguage";
    public static final String V_CTA_CreditClassId = "CTA_CreditClassId";
    public static final String V_CTA_ContactPointSupplyId = "CTA_ContactPointSupplyId";
    public static final String V_CTA_StyleIdCustomerBillFormat = "CTA_StyleIdCustomerBillFormat";
    public static final String V_CTA_InvoiceIdOrganization = "CTA_InvoiceIdOrganization";
    public static final String V_CTA_AccountTypeIDCustomerAccount = "CTA_AccountTypeIDCustomerAccount";
    public static final String V_CTA_AttributeIDAttributeValuePair = "CTA_AttributeIDAttributeValuePair";
    public static final String V_CTA_AttributeValueAttributeValuePair = "CTA_AttributeValueAttributeValuePair";
    public static final String V_CTA_AttributeIndexAttributeValuePair = "CTA_AttributeIndexAttributeValuePair";
    public static final String V_CTA_AttributeNameAttributeValuePair = "CTA_AttributeNameAttributeValuePair";
    public static final String V_CTA_AttributeTypeAttributeValuePair = "CTA_AttributeTypeAttributeValuePair";
    public static final String V_CTA_AttributeValueAttributeValuePair_N = "CTA_AttributeValueAttributeValuePair_N";
    public static final String V_CTA_AttributeIDAttributeValuePair_N = "CTA_AttributeIDAttributeValuePair_N";
    
    public static final String LONGITUD_NUMERO = "LONGITUD_NUMERO";

    
    public static final String ESTADOS_REPORTE_XZ="ESTADOS_REPORTE_XZ";
    public static final String ESTADOS_ANULADOS="ESTADOS_ANULADOS";
    
    
   
}
