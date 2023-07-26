define({ "api": [
  {
    "type": "POST",
    "url": "/opsidra/cargaradjuntogestion/",
    "title": "[cargaradjuntogestion]",
    "name": "cargaradjuntogestion",
    "description": "<p>Servicio para realizar cargas de archivos adjuntos a gestiones.</p>",
    "group": "AdjuntoGestion",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs en el que se desea realizar la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "token",
            "description": "<p>token de validaci\\u00F3n de usuario.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codDispositivo",
            "description": "<p>c\\u00F3digo del dispositivo desde donde se realiza la operaci\\u00F3n, en caso de utilizarse la APP M\\u00F3VIL.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idGestion",
            "description": "<p>Identificador de la gestion a la que se asociar\\u00E1 el archivo.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "gestion",
            "description": "<p>Nombre del tipo de gesti\\u00F3n a la que pertenece el ID del archivo adjunto.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "adjunto",
            "description": "<p>String codificado en base64 de la imagen a adjuntar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "nombreArchivo",
            "description": "<p>Nombre del archivo adjunto.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "tipoArchivo",
            "description": "<p>Tipo de archivo a adjuntar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "extension",
            "description": "<p>Extensi\\u00F3n del archivo a adjuntar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "tipoDocumento",
            "description": "<p>Tipo del documento a adjuntar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idPortaMovil",
            "description": "<p>Id de portabilidad m\\u00F3vil si el adjunto pertenece a portabilidad.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"codDispositivo\": \"64E1CE4A48BF656974C376B7905F7E62\",\n    \"usuario\": \"usuario.pruebas\",\n    \"codArea\": \"505\",\n    \"token\": \"WEB\",\n    \"idGestion\": \"1\",\n    \"gestion\": \"REMESA\",\n    \"adjunto\": \"/9j/4AAQSkZJRgABAQAAAQABAAD/2wCEAAkG....\",\n    \"nombreArchivo\": \"Adjunto 1\",\n    \"tipoArchivo\": \"Imagen\",\n    \"extension\": \"JPG\",\n    \"tipoDocumento\": \"DPI\",\n    \"idPortaMovil\": \"\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n  \"idAdjunto\": \"1\",\n  \"respuesta\": {\n    \"codResultado\": \"64\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"OK. Se registr\\u00F3 el archivo adjunto correctamente.\",\n    \"clase\": \"CtrlAdjuntoGestion\",\n    \"metodo\": \"cargarAdjunto\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"-199\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"Los siguientes datos faltan o son incorrectos:  Tipo Documento no corresponde a ninguno de los tipos definidos.\",\n    \"clase\": \"CtrlAdjuntoGestion\",\n    \"metodo\": \"validarInput\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioOperacion.java",
    "groupTitle": "AdjuntoGestion"
  },
  {
    "type": "POST",
    "url": "/opsidra/deladjuntogestion/",
    "title": "[deladjuntogestion]",
    "name": "deladjuntogestion",
    "description": "<p>Servicio para eliminar adjuntos.</p>",
    "group": "AdjuntoGestion",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs en el que se desea realizar la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "token",
            "description": "<p>token de validaci\\u00F3n de usuario.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codDispositivo",
            "description": "<p>c\\u00F3digo del dispositivo desde donde se realiza la operaci\\u00F3n, en caso de utilizarse la APP M\\u00F3VIL.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idAdjunto",
            "description": "<p>Identificador del adjunto de gesti\\u00F3n a eliminar.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo parametros de Entrada:",
          "content": "{\n    \"codDispositivo\": \"64E1CE4A48BF656974C376B7905F7E62\",\n    \"usuario\": \"usuario.pruebas\",\n    \"codArea\": \"505\",\n    \"token\": \"WEB\",\n    \"idAdjunto\": \"1\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "{} JSON Respuesta-\\u00E9xito:",
          "content": "{\n  \"token\": \"WEB\",\n  \"respuesta\": {\n    \"codResultado\": \"79\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"OK. Se elimin\\u00F3 el adjunto correctamente.\",\n    \"clase\": \"CtrlAdjuntoGestion\",\n    \"metodo\": \"delAdjunto\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "{} JSON Respuesta-Error:     ",
          "content": "{\n  \"token\": \"WEB\",\n  \"respuesta\": {\n    \"codResultado\": \"-891\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"No se encontr\\u00F3 el adjunto solicitado.\",\n    \"clase\": \" \",\n    \"metodo\": \"delAdjunto\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioOperacion.java",
    "groupTitle": "AdjuntoGestion"
  },
  {
    "type": "POST",
    "url": "/consultasidra/getadjuntogestion/",
    "title": "[getadjuntogestion]",
    "name": "getadjuntogestion",
    "description": "<p>Servicio para obtener adjuntos.</p>",
    "group": "AdjuntoGestion",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs en el que se desea realizar la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codDispositivo",
            "description": "<p>c\\u00F3digo del dispositivo desde donde se realiza la operaci\\u00F3n, en caso de utilizarse la APP M\\u00F3VIL.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "token",
            "description": "<p>token de validaci\\u00F3n de autenticaci\\u00F3n de usuario.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idAdjunto",
            "description": "<p>Identificador del adjunto a consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idGestion",
            "description": "<p>Identificador de la gestion a consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "gestion",
            "description": "<p>Nombre de la gestion a consultar.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"codArea\": \"505\",\n    \"token\": \"WEB\",\n    \"usuario\": \"usuario.pruebas\",\n    \"codDispositivo\": \"64E1CE4A48BF656974C376B7905F7E62\",\n    \"idAdjunto\": \"10\",\n    \"idGestion\": \"\",\n    \"gestion\": \"\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n  \"token\": \"WEB\",\n  \"respuesta\": {\n    \"codResultado\": \"12\",\n    \"mostrar\": \"0\",\n    \"descripcion\": \"OK. Datos obtenidos exitosamente\",\n    \"clase\": \" \",\n    \"metodo\": \" \",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"\"\n  },\n  \"adjunto\": {\n    \"idAdjunto\": \"10\",\n    \"idGestion\": \"1\",\n    \"gestion\": \"REMESA\",\n    \"adjunto\": \"/9j/4AAQSkZJRgABAQAAAQABAAD/2wCEAAkG\",\n    \"nombreArchivo\": \"Adjunto 1\",\n    \"tipoArchivo\": \"Imagen\",\n    \"extension\": \"JPG\",\n    \"tipoDocumento\": \"DPI\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n  \"token\": \"WEB\",\n  \"respuesta\": {\n    \"codResultado\": \"-205\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"No existen registros con esos par\\u00E1metros.\",\n    \"clase\": \"OperacionCargaFile\",\n    \"metodo\": \"getImagenPDV\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioConsulta.java",
    "groupTitle": "AdjuntoGestion"
  },
  {
    "type": "POST",
    "url": "/opsidra/crearanulacion/",
    "title": "[crearanulacion]",
    "name": "crearanulacion",
    "description": "<p>Servicio para anular ventas registradas en Sidra.</p>",
    "group": "Anulacion",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "token",
            "description": "<p>token de validaci\\u00F3n de autenticaci\\u00F3n de usuario.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codDispositivo",
            "description": "<p>C\\u00F3digo del dispositivo al que se reservar\\u00E1n los folios.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs del que se desea obtener informaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idJornada",
            "description": "<p>Identificador de la jornada activa.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idVendedor",
            "description": "<p>Identificador del vendedor de la jornada y la venta.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idVenta",
            "description": "<p>Identificador de la venta a anular.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "fechaAnulacion",
            "description": "<p>Fecha y hora de la anulaci\\u00F3n en formato yyyyMMddHHMISS.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "razonAnulacion",
            "description": "<p>Raz\\u00F3n de la anulaci\\u00F3n, se permiten \\u00FAnicamente los valores configurados en el sistema.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "observaciones",
            "description": "<p>Observaciones de la anulaci\\u00F3n.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"token\": \"WEB\",\n    \"codDispositivo\": \"64E1CE4A48BF656974C376B7905F7E62\",\n    \"codArea\":\"505\",\n    \"usuario\":\"usuario.pruebas\",\n    \"idJornada\":\"4\",\n    \"idVendedor\":\"2242\",\n    \"idVenta\":\"8\",\n    \"fechaAnulacion\":\"20161028161202\",\n    \"razonAnulacion\":\"CANCELACION\",\n    \"observaciones\":\"\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n  \"token\": \"WEB\",\n  \"idAnulacion\": \"1\",\n  \"respuesta\": {\n    \"codResultado\": \"200\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"Campos agregados exitosamente.\",\n    \"clase\": \"OperacionAnulacion\",\n    \"metodo\": \"doPost\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:",
          "content": "{\n  \"token\": \"WEB\",\n  \"respuesta\": {\n    \"codResultado\": \"-800\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"La fecha de anulaci\\u00F3n no puede ser antes de la fecha de venta.\",\n    \"clase\": \"CtrlAnulacion\",\n    \"metodo\": \"validarInput\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioOperacion.java",
    "groupTitle": "Anulacion"
  },
  {
    "type": "POST",
    "url": "/consultasidra/getanulacion/",
    "title": "[getanulacion]",
    "name": "getanulacion",
    "description": "<p>Servicio para obtener el listado de anulaciones realizadas.</p>",
    "group": "Anulacion",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs en el que se desea realizar la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "token",
            "description": "<p>token de validaci\\u00F3n de autenticaci\\u00F3n de usuario.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codDispositivo",
            "description": "<p>c\\u00F3digo del dispositivo desde donde se realiza la operaci\\u00F3n, en caso de utilizarse la APP M\\u00F3VIL.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idJornada",
            "description": "<p>Identificador de la jornada que se desea consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idVendedor",
            "description": "<p>Identificador del vendedor que se desea consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idVenta",
            "description": "<p>Identificador de la venta a consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "razonAnulacion",
            "description": "<p>Raz\\u00F3n de anulaci\\u00F3n que se desea consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "fechaInicio",
            "description": "<p>Fecha en formato yyyyMMdd desde la cu\\u00E1l se desea consultar la anulaci\\u00F3n.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"codArea\": \"505\",\n    \"token\": \"WEB\",\n    \"usuario\": \"usuarioPruebas\",\n    \"codDispositivo\": \"64E1CE4A48BF656974C376B7905F7E62\",\n    \"idJornada\":\"\",\n    \"idVendedor\":\"\",\n    \"idVenta\":\"\",\n    \"razonAnulacion\":\"\",\n    \"fechaInicio\":\"20161101\",\n    \"fechaFin\":\"20161130\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n  \"token\": \"WEB\",\n  \"respuesta\": {\n    \"codResultado\": \"22\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"OK. Carga de anulaciones correctamente.\",\n    \"clase\": \"OperacionAnulacion\",\n    \"metodo\": \"doGet\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"\"\n  },\n  \"anulaciones\": {\n    \"idAnulacion\": \"4\",\n    \"fechaAnulacion\": \"28/10/2016 16:12:02\",\n    \"idJornada\": \"4\",\n    \"idVendedor\": \"2242\",\n    \"nombreVendedor\": \"usuario.sidra\",\n    \"idVenta\": \"8\",\n    \"razonAnulacion\": \"CANCELACION\",\n    \"creado_el\": \"11/11/2016 09:14:34\",\n    \"creado_por\": \"usuario.pruebas\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n  \"token\": \"WEB\",\n  \"respuesta\": {\n    \"codResultado\": \"-61\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"Debe ingresar ambas fechas: Fecha inicio y fecha Fin\",\n    \"clase\": \"CtrlAnulacion\",\n    \"metodo\": \"validarInput\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioConsulta.java",
    "groupTitle": "Anulacion"
  },
  {
    "type": "POST",
    "url": "/consultasidra/obtenerarticuloprecio/",
    "title": "[obtenerarticuloprecio]",
    "name": "obtenerarticuloprecio",
    "description": "<p>Servicio para obtener configuraci\\u00F3n de art\\u00EDculo precio,</p>",
    "group": "ArticuloPrecio_",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs en el que se desea realizar la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idArticulo",
            "description": "<p>Identificador del articulo a asociar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "tipoGestion",
            "description": "<p>nombre de la gesti\\u00F3n a la que estar\\u00E1 asociado el articulo y su precio. ALTA_PREPAGO y PORTABILIDAD.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "precio",
            "description": "<p>cantidad en cuanto al precio que se le dar\\u00E1 al art\\u00EDculo.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idProductOffering",
            "description": "<p>Identificador de la oferta que se asociar\\u00E1 al precio de un art\\u00EDculo.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo parametros de Entrada:",
          "content": "{\n    \"usuario\": \"usuario.pruebas\",\n    \"codArea\": \"503\",\n    \"idArticulo\":\"\",\n    \"tipoGestion\":\"\",\n    \"precio\":\"\",\n    \"idProductOffering\":\"9145627885465644655\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "{} JSON Respuesta-\\u00E9xito:",
          "content": "{\n  \"respuesta\": {\n     \"codResultado\": \"12\",\n    \"mostrar\": \"1\",\n     \"descripcion\": \"OK. Datos obtenidos exitosamente\",\n    \"clase\": \"\",\n    \"metodo\": \"\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"\"\n  },\n  \"articulosPrecio\": {\n  \t\"idArticulo\": \"9293\",\n  \t\"tipoGestion\": \"PORTABILIDAD\",\n  \t\"precio\": \"4.45\",\n  \t\"idProductOffering\": \"9145627885465644655\",\n  \t\"estado\": \"ALTA\",\n  \t\"nombreArticulo\": \"TARJETA PREPAGO MOVISTAR $5.00\",\n  \t\"version\": \"\",\n  \t\"creadoEl\": \"18/09/2017 16:15:30\",\n  \t\"creadoPor\": \"admin-sidra\",\n     \"modificadoEl\": \"19/09/2017 15:01:26\",\n     \"modificadoPor\": \"usuario.pruebas\"\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "{} JSON Respuesta-Error:     ",
          "content": "{\n  \"token\": \"WEB\",\n  \"respuesta\": {\n    \"codResultado\": \"-2\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"El par\\u00E1metro de entrada \\\\\\\"usuario\\\"\\\\ esta vac\\u00EDo\",\n    \"clase\": \"CtrlArticuloPrecio\",\n    \"metodo\": \"validarInput\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioConsulta.java",
    "groupTitle": "ArticuloPrecio_"
  },
  {
    "type": "POST",
    "url": "/opsidra/asociaarticuloprecio/",
    "title": "[asociaarticuloprecio]",
    "name": "asociaarticuloprecio",
    "description": "<p>Servicio para asociar articulo-precio.</p>",
    "group": "ArticuloPrecio",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs en el que se desea realizar la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idArticulo",
            "description": "<p>Identificador del articulo a asociar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "tipoGestion",
            "description": "<p>nombre de la gesti\\u00F3n a la que estar\\u00E1 asociado el articulo y su precio. ALTA_PREPAGO y PORTABILIDAD.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "precio",
            "description": "<p>cantidad en cuanto al precio que se le dar\\u00E1 al art\\u00EDculo.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idProductOffering",
            "description": "<p>Identificador de la oferta que se asociar\\u00E1 al precio de un art\\u00EDculo.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo parametros de Entrada:",
          "content": "{\n    \"usuario\": \"usuario.pruebas\",\n    \"codArea\": \"505\",\n    \"idArticulo\":\"9293\",\n    \"tipoGestion\":\"ALTA_PREPAGO\",\n    \"precio\":\"4.45\",\n    \t\"idProductOffering\":\"9145627885465644655\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "{} JSON Respuesta-\\u00E9xito:",
          "content": "{\n  \"token\": \"WEB\",\n  \"respuesta\": {\n     \"codResultado\": \"81\",\n    \"mostrar\": \"1\",\n     \"descripcion\": \"OK. Se creo asociaci\\u00F3n de art\\u00EDculo y oferta \",\n    \"clase\": \"\",\n    \"metodo\": \"\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "{} JSON Respuesta-Error:     ",
          "content": "{\n  \"token\": \"WEB\",\n  \"respuesta\": {\n    \"codResultado\": \"-665\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"El par\\u00E1metro de entrada \\\\\\\"IdProductOffering\\\"\\\\ esta vac\\u00EDo\",\n    \"clase\": \"CtrlArticuloPrecio\",\n    \"metodo\": \"validarInput\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioOperacion.java",
    "groupTitle": "ArticuloPrecio"
  },
  {
    "type": "POST",
    "url": "/opsidra/modarticuloprecio/",
    "title": "[modarticuloprecio]",
    "name": "modarticuloprecio",
    "description": "<p>Servicio para modificar asociaci\\u00F3n de articulo-precio.</p>",
    "group": "ArticuloPrecio",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs en el que se desea realizar la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idArticulo",
            "description": "<p>Identificador del articulo a asociar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "tipoGestion",
            "description": "<p>nombre de la gesti\\u00F3n a la que estar\\u00E1 asociado el articulo y su precio. ALTA_PREPAGO y PORTABILIDAD.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "precio",
            "description": "<p>cantidad en cuanto al precio que se le dar\\u00E1 al art\\u00EDculo.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idProductOffering",
            "description": "<p>Identificador de la oferta que se asociar\\u00E1 al precio de un art\\u00EDculo.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo parametros de Entrada:",
          "content": "{\n    \"usuario\": \"usuario.pruebas\",\n    \"codArea\": \"503\",\n    \"idArticulo\":\"9293\",\n    \"tipoGestion\":\"ALTA_PREPAGO\",\n    \"precio\":\"4.45\",\n    \"idProductOffering\":\"9145627885465644655\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "{} JSON Respuesta-\\u00E9xito:",
          "content": "{\n  \"token\": \"WEB\",\n  \"respuesta\": {\n     \"codResultado\": \"81\",\n    \"mostrar\": \"1\",\n     \"descripcion\": \"OK. art\\u00EDculo-precio modificado exitosamente. \",\n    \"clase\": \"\",\n    \"metodo\": \"\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "{} JSON Respuesta-Error:     ",
          "content": "{\n  \"token\": \"WEB\",\n  \"respuesta\": {\n    \"codResultado\": \"-665\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"El par\\u00E1metro de entrada \\\\\\\"IdProductOffering\\\"\\\\ esta vac\\u00EDo\",\n    \"clase\": \"CtrlArticuloPrecio\",\n    \"metodo\": \"validarInput\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioOperacion.java",
    "groupTitle": "ArticuloPrecio"
  },
  {
    "type": "POST",
    "url": "/consultasidra/getArticulos/",
    "title": "[getArticulos]",
    "name": "getArticulos",
    "description": "<p>Servicio para obtener los datos de art\\u00EDculos del sistema comercial.</p>",
    "group": "Articulos",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs del que se desea obtener informaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operacion.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"codArea\": \"505\",\n    \"usuario\": \"usuario.pruebas\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n   \"respuesta\": {\n       \"codResultado\": \"0\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"Recursos recuperados exitosamente.\"\n   },\n   \"articulos\": [\n       {\n         \"idArticulo\": \"403\",\n         \"codArticulo\": \"71\",\n         \"descArticulo\": \"ALCATEL 156A\"\n       },\n       {\n         \"idArticulo\": \"439\",\n         \"codArticulo\": \"13\",\n         \"descArticulo\": \"TERMINAL GSM PRUEBA\"\n       }\n   ]\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n   \"respuesta\": {\n       \"codResultado\": \"-199\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"Los siguientes datos faltan o son incorrectos:  Usuario.\",\n       \"clase\": \"CtrlArtriculo\",\n       \"metodo\": \"validarInput\",\n       \"excepcion\": \" \",\n       \"tipoExepcion\": \"\"\n   }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioConsulta.java",
    "groupTitle": "Articulos"
  },
  {
    "type": "POST",
    "url": "/consultasidra/getPrecioArticulo/",
    "title": "[getPrecioArticulo]",
    "name": "getPrecioArticulo",
    "description": "<p>Servicio para obtener los datos de art\\u00EDculos del sistema comercial.</p>",
    "group": "Articulos",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs del que se desea obtener informaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idArticulo",
            "description": "<p>Identificador del art\\u00EDculo a buscar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "tipoGestion",
            "description": "<p>Nombre del tipo de gesti\\u00F3n. Este campo es aplicable \\u00FAnicamente a El Salvador, enviar vacio en los dem\\u00E1s paises.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"codArea\": \"505\",\n    \"usuario\": \"usuario.pruebas\",\n    \"idArticulo\": \"1894\",\n    \"tipoGestion\": \"ALTA\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n   \"respuesta\": {\n       \"codResultado\": \"0\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"Recursos recuperados exitosamente.\"\n   },\n   \"articulos\": [\n       {\n          \"descArticulo\": \"GSM-ALCATEL OT-720\",\n          \"desTipoPrecio\": \"PACK PREPAGO INDIVIDUAL\",\n          \"precioArticulo\": \"57.5221\"\n       }\n   ]\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n   \"respuesta\": {\n       \"codResultado\": \"-199\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"Los siguientes datos faltan o son incorrectos:  C\\u00F3digo de Art\\u00EDculo.\",\n       \"clase\": \"CtrlArtriculo\",\n       \"metodo\": \"validarInput\",\n       \"excepcion\": \" \",\n       \"tipoExepcion\": \"\"\n   }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioConsulta.java",
    "groupTitle": "Articulos"
  },
  {
    "type": "POST",
    "url": "/opsidra/creaasignacionreserva/",
    "title": "[creaAsignacion]",
    "name": "creaAsignacion",
    "description": "<p>Servicio para crear Asignaciones por pa\\u00EDs.</p>",
    "group": "Asignacion_Reserva",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pais donde se realizar\\u00E1 la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "tipo",
            "description": "<p>Nombre transacci\\u00F3n. Puede ser RESERVA o ASIGNACION.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idVendedor",
            "description": "<p>Identificador del vendedor que reserva/asigna.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idBodegaOrigen",
            "description": "<p>Identificador de la bodega origen.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idBodegaDestino",
            "description": "<p>Identificador de la bodega destino.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "observaciones",
            "description": "<p>Observaciones de la transacci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idArticulo",
            "description": "<p>Identificador del art\\u00EDculo a reservar/asignar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "serie",
            "description": "<p>Serie del art\\u00EDculo a reservar/asignar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "serieFinal",
            "description": "<p>Serie final del rango del art\\u00EDculo a reservar/asignar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "cantidad",
            "description": "<p>Cantidad de art\\u00EDculos a reservar/asignar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "serieAsociada",
            "description": "<p>Serie asociada al art\\u00EDculo.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "tipoInv",
            "description": "<p>Nombre del tipo de inventario del art\\u00EDculo.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"codArea\": \"505\",\n    \"usuario\": \"usuarioprueba\",\n    \"tipo\": \"RESERVA\",\n    \"idVendedor\": \"10\",\n    \"idBodegaOrigen\": \"100\",\n    \"idBodegaDestino\": \"110\",\n    \"observaciones\": \"\",\n    \"articulos\":[\n        {\n            \"idArticulo\":\"15\",\n            \"serie\":\"\",\n            \"serieFinal\":\"\",\n            \"cantidad\":\"10\",\n            \"serieAsociada\":\"\",\n            \"tipoInv\": \"INV_TELCA\"\n        },\n        {\n            \"idArticulo\":\"\",\n            \"serie\":\"A1\",\n            \"serieFinal\":\"\",\n            \"cantidad\":\"1\",\n            \"serieAsociada\":\"\",\n            \"tipoInv\": \"INV_TELCA\"\n        },\n        {\n            \"idArticulo\":\"\",\n            \"serie\":\"1000\",\n            \"serieFinal\":\"2000\",\n            \"cantidad\":\"\",\n            \"serieAsociada\":\"\",\n            \"tipoInv\": \"INV_TELCA\"\n        }\n    ]\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n  \"idAsignacion\": \"20\",\n  \"respuesta\": {\n    \"codResultado\": \"200\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"Campos agregados exitosamente. Algunas series no se pudieron asignar: A3 Algunos art\\u00EDculos no se pudieron asignar por las siguientes razones: Art\\u00EDculos no existen en el inventario: 598. \",\n    \"clase\": \"OperacionAsignacion\",\n    \"metodo\": \"doPost\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"\"\n  },\n  \"descErrorSeries\": \"Series no v\\u00E1lidas en la bodega.\",\n  \"series\": {\n    \"serie\": \"A3\"\n  },\n  \"descErrorArticulos\": \"Art\\u00EDculos no existen en el inventario: \",\n  \"articulos\": {\n    \"idArticulo\": \"598\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"-199\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"Los siguientes datos faltan o son incorrectos:  La Bodega Destino enviada no es apta.\",\n    \"clase\": \"CtrlAsignacion\",\n    \"metodo\": \"validarInput\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioOperacion.java",
    "groupTitle": "Asignacion_Reserva"
  },
  {
    "type": "POST",
    "url": "/consultasidra/getasignacionreserva/",
    "title": "[getAsignacionReserva]",
    "name": "getAsignacionReserva",
    "description": "<p>Servicio para obtener los datos de asignaciones o reservas por pais.</p>",
    "group": "Asignacion_Reserva",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pais.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operacion.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idAsignacionReserva",
            "description": "<p>Identificador de la asignaci\\u00F3n/reserva que se desea.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "tipo",
            "description": "<p>Nombre del tipo de operacion que se desea consultar, puede ser ASIGNACION o RESERVA.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idDTS",
            "description": "<p>Identificador del distribuidor que se desea consultar en la solicitud. Necesario \\u00FAnicamente si el tipo de Solicitud es MOVIL.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idVendedor",
            "description": "<p>Identificador del vendedor que se desea consultar en la solicitud. Necesario \\u00FAnicamente si el tipo de Solicitud es MOVIL.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idBodegaOrigen",
            "description": "<p>Identificador de la bodega origen que se desea.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idBodegaDestino",
            "description": "<p>Identificador de la bodega destino que se desea.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "fechaInicio",
            "description": "<p>Fecha en formato yyyyMMdd desde la cu\\u00E1l se desea consultar la solicitud.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "fechaFin",
            "description": "<p>Fecha en formato yyyyMMdd hasta la cu\\u00E1l se desea consultar la solicitud.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n   \"codArea\": \"505\",\n   \"usuario\":\"usuarioprueba\",\n   \"idAsignacionReserva\":\"1\",\n   \"tipo\":\"ASIGNACION\",\n   \"idDTS\":\"\",\n   \"idVendedor\":\"15\",\n   \"idBodegaOrigen\":\"3\",\n   \"idBodegaDestino\":\"7\",\n   \"fechaInicio\":\"20151005\",\n   \"fechaFin\":\"20151005\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"0\",\n    \"mostrar\": \"0\",\n    \"descripcion\": \"Recursos recuperados exitosamente.\"\n  },\n  \"asignacionReserva\": {\n    \"idAsignacionReserva\": \"28\",\n    \"tipo\": \"RESERVA\",\n    \"idVendedor\": \"661\",\n    \"nombreVendedor\": \"ejecutivo2\",\n    \"idBodegaOrigen\": \"81\",\n    \"nombreBodegaOrigen\": \"bodega pruebas\",\n    \"idBodegaDestino\": \"148\",\n    \"nombreBodegaDestino\": \"BODEGA VENDEDOR 661\",\n    \"observaciones\": \" Algunas series no se pudieron ingresar: A3 Algunos art\\u00EDculos no se pudieron ingresar por las siguientes razones: Art\\u00EDculos no existen en el inventario: 598. \",\n    \"estado\": \"ALTA\",\n    \"creado_el\": \"29/12/2015 17:15:01\",\n    \"creado_por\": \"usuarioprueba\",\n    \"articulos\": [\n      {\n        \"idArticulo\": \"3002\",\n        \"descripcion\": \"Nokia N8\",\n        \"serie\": \"B2\",\n        \"serieFinal\":\"NULL\",\n        \"serieAsociada\":\"\",\n        \"cantidad\": \"1\",\n        \"estado\": \"Reservado\"\n      },\n      {\n        \"idArticulo\": \"3003\",\n        \"descripcion\": \"Estuche Nokia Lumia\",\n        \"serie\": \"NULL\",\n        \"serieFinal\":\"NULL\",\n        \"serieAsociada\":\"\",\n        \"cantidad\": \"3\",\n        \"estado\": \"Reservado\"\n      }\n    ]\n  }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"-199\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"Los siguientes datos faltan o son incorrectos:  Tipo debe especificarse como ASIGNACION o RESERVA.\",\n    \"clase\": \"CtrlAsignacion\",\n    \"metodo\": \"validarInput\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioConsulta.java",
    "groupTitle": "Asignacion_Reserva"
  },
  {
    "type": "POST",
    "url": "/opsidra/modasignacionreserva/",
    "title": "[modAsignacion]",
    "name": "modAsignacion",
    "description": "<p>Servicio para modificar Asignaciones o Reservas por pa\\u00EDs.</p>",
    "group": "Asignacion_Reserva",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pais donde se realizar\\u00E1 la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idAsignacionReserva",
            "description": "<p>Identificador de la asignaci\\u00F3n/reserva que se desea modificar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "estado",
            "description": "<p>Estado al que se desea cambiar la asignaci\\u00F3n/reserva. Puede ser FINALIZADA o CANCELADA.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "observaciones",
            "description": "<p>Observaciones de la transacci\\u00F3n.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n   \"codArea\": \"505\",\n   \"usuario\": \"usuario.pruebas\",\n   \"idAsignacionReserva\": \"15\",\n   \"estado\": \"CANCELADA\",\n   \"observaciones\": \"Observaciones de la transacci\\u00F3n.\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"200\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"Campos agregados exitosamente.\",\n    \"clase\": \"OperacionAsignacion\",\n    \"metodo\": \"doPut\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"\"\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"-201\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"No existe el recurso deseado.\",\n    \"clase\": \"OperacionAsignacion\",\n    \"metodo\": \"doPut\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioOperacion.java",
    "groupTitle": "Asignacion_Reserva"
  },
  {
    "type": "POST",
    "url": "/opsidra/moddetasignacionreserva/",
    "title": "[modDetAsignacion]",
    "name": "modDetAsignacion",
    "description": "<p>Servicio para modificar detalle de Asignaciones o Reservas por pa\\u00EDs.</p>",
    "group": "Asignacion_Reserva",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pais donde se realizar\\u00E1 la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idAsignacionReserva",
            "description": "<p>Identificador de la asignaci\\u00F3n/reserva que se desea modificar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "observaciones",
            "description": "<p>Observaciones de la transacci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "articulos",
            "description": "<p>Listado de art\\u00EDculos a agregar/quitar/modificar de la asignaci\\u00F3n o reserva. En caso de ser una Asignaci\\u00F3n y no se env\\u00EDa el listado de art\\u00EDculos, se CANCELA la asignaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idArticulo",
            "description": "<p>Identificador del art\\u00EDculo a reservar/asignar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "serie",
            "description": "<p>Serie del art\\u00EDculo a reservar/asignar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "serieFinal",
            "description": "<p>Serie final del rango del art\\u00EDculo a reservar/asignar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "cantidad",
            "description": "<p>Cantidad de art\\u00EDculos a reservar/asignar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "serieAsociada",
            "description": "<p>Serie asociada al art\\u00EDculo.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "tipoInv",
            "description": "<p>Nombre del tipo de inventario del art\\u00EDculo.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"codArea\": \"505\",\n    \"usuario\": \"usuario.pruebas\",\n    \"idAsignacionReserva\": \"28\",\n    \"observaciones\": \"Observaciones de la transacci\\u00F3n.\",\n    \"articulos\": [\n        {\n            \"idArticulo\":\"\",\n            \"serie\":\"427C1F97\",\n            \"serieFinal\":\"\",\n            \"cantidad\":\"\",\n            \"serieAsociada\":\"\",\n            \"tipoInv\": \"INV_TELCA\"\n        },\n        {\n            \"idArticulo\":\"\",\n            \"serie\":\"143\",\n            \"serieFinal\":\"145\",\n            \"cantidad\":\"\",\n            \"serieAsociada\":\"\",\n            \"tipoInv\": \"INV_TELCA\"\n        },\n        {\n            \"idArticulo\":\"9\",\n            \"serie\":\"\",\n            \"serieFinal\":\"\",\n            \"cantidad\":\"15\",\n            \"serieAsociada\":\"\",\n            \"tipoInv\": \"INV_SIDRA\"\n        }\n    ]\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"201\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"Recurso modificado exitosamente.\",\n    \"clase\": \"OperacionAsignacion\",\n    \"metodo\": \"doModAsignacionReserva\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"\"\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"-201\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"No existe el recurso deseado.\",\n    \"clase\": \"OperacionAsignacion\",\n    \"metodo\": \"doModAsignacionReserva\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioOperacion.java",
    "groupTitle": "Asignacion_Reserva"
  },
  {
    "type": "POST",
    "url": "/opsidra/asociarbodegadts/",
    "title": "[AsociarBodegasDTS]",
    "name": "AsociarBodegasDTS",
    "description": "<p>Servicio para asociar bodegas del sistema comercial a distribuidores internos o externos de SIDRA.</p>",
    "group": "Bodegas_Almacen",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operacion.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "bodegaSCL",
            "description": "<p>Identificador de la bodega del sistema comercial que se asociar\\u00E1 al distribuidor.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "nombre",
            "description": "<p>Nombre de la bodega DTS que se crear\\u00E1.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "distribuidor",
            "description": "<p>Identificador del distribuidor al que ser\\u00E1 asociada la bodega del sistema comercial.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"codArea\": \"505\",\n    \"usuario\": \"vCifuentes\",\n    \"bodegaSCL\": \"100\",\n    \"nombre\": \"Bodega Prueba\",\n    \"distribuidor\": \"15\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n   \"respuesta\": {\n       \"codResultado\": \"200\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"Campos agregados exitosamente.\",\n       \"clase\": \"OperacionBodegaDTS\",\n       \"metodo\": \"doPost\",\n       \"excepcion\": \" \",\n       \"tipoExepcion\": \" \"\n   }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n   \"respuesta\": {\n       \"codResultado\": \"-205\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"Los siguientes datos faltan o son incorrectos:  Bodega.\",\n       \"clase\": \"CtrlBodegaDTS\",\n       \"metodo\": \"validarInput\",\n       \"excepcion\": \" \",\n       \"tipoExepcion\": \" \"\n   }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioOperacion.java",
    "groupTitle": "Bodegas_Almacen"
  },
  {
    "type": "POST",
    "url": "/opsidra/bajabodegadts/",
    "title": "[BajaBodegaDTS]",
    "name": "BajaBodegaDTS",
    "description": "<p>Servicio para dar de baja la asociaci\\u00F3n de bodegas a distribuidores de SIDRA, de igual forma si contiene folios configurados estos tambi\\u00E9n ser\\u00E1n dados de baja.</p>",
    "group": "Bodegas_Almacen",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operacion.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idBodega",
            "description": "<p>Identificador de la bodega a dar de baja.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"codArea\": \"505\",\n    \"usuario\": \"vCifuentes\",\n    \"idBodega\": \"73\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n   \"respuesta\": {\n       \"codResultado\": \"201\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"Recurso modificado exitosamente\",\n       \"clase\": \"OperacionBodegaDTS\",\n       \"metodo\": \"doPutDel\",\n       \"excepcion\": \" \",\n       \"tipoExepcion\": \" \"\n   }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n   \"respuesta\": {\n       \"codResultado\": \"-205\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"Los siguientes datos faltan o son incorrectos:  Bodega.\",\n       \"clase\": \"CtrlBodegaDTS\",\n       \"metodo\": \"validarInput\",\n       \"excepcion\": \" \",\n       \"tipoExepcion\": \" \"\n   }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioOperacion.java",
    "groupTitle": "Bodegas_Almacen"
  },
  {
    "type": "POST",
    "url": "/opsidra/modificarbodegadts/",
    "title": "[ModificarBodegasDTS]",
    "name": "ModificarBodegasDTS",
    "description": "<p>Servicio para modificar la asociaci\\u00F3n de bodegas a distribuidores de SIDRA, solo podr\\u00E1 modificarse la bodega de SCL por una diferente.</p>",
    "group": "Bodegas_Almacen",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operacion.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idBodega",
            "description": "<p>Identificador de la bodega a modificar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "bodegaSCL",
            "description": "<p>Identificador de la bodega del sistema comercial.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "nombre",
            "description": "<p>Nombre de la bodega asociada.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "distribuidor",
            "description": "<p>Nombre del distribuidor al que esta asociada la bodega del sistema comercial.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "estado",
            "description": "<p>Estado de la bodega.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n   \"codArea\": \"505\",\n   \"usuario\": \"usuario.pruebas\",\n   \"idBodega\": \"73\",\n   \"bodegaSCL\": \"101\",\n   \"nombre\": \"Bodega Prueba2\",\n   \"distribuidor\": \"16\",\n   \"estado\": \"ALTA\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n   \"respuesta\": {\n      \"codResultado\": \"201\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"Recurso modificado exitosamente.\",\n       \"clase\": \"OperacionBodegaDTS\",\n       \"metodo\": \"doPutDel\",\n       \"excepcion\": \" \",\n       \"tipoExepcion\": \" \"\n   }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n   \"respuesta\": {\n       \"codResultado\": \"-205\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"Los siguientes datos faltan o son incorrectos:  Bodega.\",\n       \"clase\": \"CtrlBodegaDTS\",\n       \"metodo\": \"validarInput\",\n       \"excepcion\": \" \",\n       \"tipoExepcion\": \" \"\n   }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioOperacion.java",
    "groupTitle": "Bodegas_Almacen"
  },
  {
    "type": "POST",
    "url": "/consultasidra/getbodalmacen/",
    "title": "[getbodalmacen]",
    "name": "getbodalmacen",
    "description": "<p>Servicio para obtener las bodegas asociadas a los distribuidores de un pa\\u00EDs determinado.</p>",
    "group": "Bodegas_Almacen",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operacion.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idBodega",
            "description": "<p>Identificador de la bodega a consultar. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "bodegaSCL",
            "description": "<p>Nombre de la bodega del sistema comercial que se ha asociado al distribuidor. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "nombre",
            "description": "<p>Nombre de la bodega DTS. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "distribuidor",
            "description": "<p>Identificador del distribuidor asociado a bodega. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "estado",
            "description": "<p>Estado de la bodega. Este campo es opcional.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"codArea\": \"505\",\n    \"usuario\": \"usuario.pruebas\",\n    \"idBodega\": \"\",\n    \"bodegaSCL\": \"\",\n    \"nombre\": \"\",\n    \"distribuidor\": \"\",\n    \"estado\": \"\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n   \"respuesta\": {\n       \"codResultado\": \"0\",\n       \"mostrar\": \"0\",\n       \"descripcion\": \"Recursos recuperados exitosamente.\"\n   },\n   \"bodegaDTS\": {\n       \"bodegaSCL\": \"3\",\n       \"bodegaId\": \"1\",\n       \"nombre\": \"BODEGA 1\",\n       \"distribuidor\": \"1\",\n       \"estado\": \"ALTA\",\n       \"usuario\": \"usuario.pruebas\",\n       \"creado_el\": \"2015-10-22 14:20:26.0\",\n       \"creado_por\": \"usuario.pruebas\",\n       \"modificado_el\": \"2015-10-22 14:27:28.0\",\n       \"modificado_por\": \"usuario.pruebas\"\n   }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n   \"respuesta\": {\n       \"codResultado\": \"-205\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"No existen registros con esos par\\u00E1metros.\",\n       \"clase\": \"OperacionBodegaDTS\",\n       \"metodo\": \"doGet\",\n       \"excepcion\": \" \",\n       \"tipoExepcion\": \" \"\n   }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioConsulta.java",
    "groupTitle": "Bodegas_Almacen"
  },
  {
    "type": "POST",
    "url": "/opsidra/bajabodegavirtual/",
    "title": "[bajaBodegaVirtual]",
    "name": "bajaBodegaVirtual",
    "description": "<p>Servicio para dar de baja la asociaci\\u00F3n de bodegas virtuales a distribuidores de SIDRA.</p>",
    "group": "Bodegas_Virtuales",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs del que se desea obtener informaci\\u00F3n de bodegas.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operacion.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idBodega",
            "description": "<p>Identificador de la bodega virtual que ser\\u00E1 dada de baja.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"codArea\": \"505\",\n    \"usuario\": \"usuario.pruebas\",\n    \"idBodega\": \"63\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n   \"respuesta\": {\n       \"codResultado\": \"201\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"Recurso modificado exitosamente\",\n       \"clase\": \"OperacionBodegaVirtual\",\n       \"metodo\": \"doPutDel\",\n       \"excepcion\": \" \",\n       \"tipoExepcion\": \" \"\n   }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n   \"respuesta\": {\n       \"codResultado\": \"-205\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"Los siguientes datos faltan o son incorrectos:  Bodega.\",\n       \"clase\": \"CtrlBodegaVirtual\",\n       \"metodo\": \"validarInput\",\n       \"excepcion\": \" \",\n       \"tipoExepcion\": \" \"\n   }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioOperacion.java",
    "groupTitle": "Bodegas_Virtuales"
  },
  {
    "type": "POST",
    "url": "/opsidra/creabodegavirtual/",
    "title": "[creaBodegaVirtual]",
    "name": "creaBodegaVirtual",
    "description": "<p>Servicio para asociar bodegas virtuales a los distribuidores internos o externos de SIDRA.</p>",
    "group": "Bodegas_Virtuales",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs del que se desea obtener informaci\\u00F3n de bodegas.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "nombre",
            "description": "<p>Nombre de la bodega virtual que ser\\u00E1 asociada la bodega DTS.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "nivel",
            "description": "<p>Indicar\\u00E1 si la bodega a crear es bodega principal de DTS o es bodega virtual de DTS. Los niveles ser\\u00E1n: Bodega Para Inventario de Sidra: 0, Bodega Principal: 1, Bodega Virtual: &gt;= 2.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "tipo:",
            "description": "<p>Indica el tipo de bodega de nivel 0, es obligatorio al crear bodegas de nivel 0.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idBodegaPadre",
            "description": "<p>ID de la bodega principal del dts a la que se esta creando bodega virtual, ser\\u00E1 obligatoria para niveles mayores a 1.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idBodegaOrigen",
            "description": "<p>ID de la bodega de distribuidor de origen. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "latitud",
            "description": "<p>Latitud geogr\\u00E1fica de la bodega, ser\\u00E1 obligatorio para niveles mayores a 1.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "longitud",
            "description": "<p>Longitud geogr\\u00E1fica de la bodega, ser\\u00E1 obligatorio para niveles mayores a 1.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n   \"codArea\": \"505\",\n   \"usuario\": \"usuario.pruebas\",\n   \"nombre\": \"BODEGA VIRTUAL\",\n   \"nivel\":\"1\",\n   \"tipo\": \"PRINCIPAL\",\n   \"idBodegaPadre\":\"\",\n   \"idBodegaOrigen\":\"\",\n   \"latitud\": \"\",\n   \"longitud\": \"\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n   \"idBodegaVirtual\":\"1\", \n   \"respuesta\": {\n       \"codResultado\": \"200\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"Campos agregados exitosamente.\",\n       \"clase\": \"OperacionBodegaVirtual\",\n       \"metodo\": \"doPost\",\n       \"excepcion\": \" \",\n       \"tipoExepcion\": \"\"\n   }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n   \"respuesta\": {\n       \"codResultado\": \"-205\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"Los siguientes datos faltan o son incorrectos:  Bodega.\",\n       \"clase\": \"CtrlBodegaVirtual\",\n       \"metodo\": \"validarInput\",\n       \"excepcion\": \" \",\n       \"tipoExepcion\": \"\"\n   }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioOperacion.java",
    "groupTitle": "Bodegas_Virtuales"
  },
  {
    "type": "POST",
    "url": "/consultasidra/getbodegavirtual/",
    "title": "[getBodegaVirtual]",
    "name": "getBodegaVirtual",
    "description": "<p>Servicio para obtener las bodegas virtuales asociadas a los distribuidores de un pa\\u00EDs determinado.</p>",
    "group": "Bodegas_Virtuales",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs del que se desea obtener informaci\\u00F3n .</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operacion.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idBodega",
            "description": "<p>Identificador de la bodega virtual. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "nombre",
            "description": "<p>Nombre de la bodega virtual que se desea obtener. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idDTS",
            "description": "<p>Id del distribuidor al que se encuentra asignada una bodega virtual. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idRuta",
            "description": "<p>Id de la Ruta a la que se encuentra asignada una bodega virtual. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idPanel",
            "description": "<p>Id de la Panel a la que se encuentra asignada una bodega virtual. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "nivel",
            "description": "<p>indicar\\u00E1 el nivel de bodega a buscar. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "Indica",
            "description": "<p>el tipo de bodega de nivel 0, es obligatorio al crear bodegas de nivel 0.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idBodegaPadre",
            "description": "<p>id de la bodega padre de bodegas virtuales a buscar. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "estado",
            "description": "<p>Estado de bodegas que se desa obtener. Este campo es opcional.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n   \"codArea\": \"505\",\n   \"usuario\": \"usuario.pruebas\",\n   \"idBodega\": \"3\",\n   \"nombre\": \"BODEGA VIRTUAL\",\n   \"idDTS\": \"2\",\n   \"idRuta\": \"\",\n   \"idPanel\": \"\", \n   \"nivel\":\"1\",\n   \"tipo\": \"PRINCIPAL\",\n   \"idBodegaPadre\":\"\",\n   \"estado\": \"ALTA\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n   \"respuesta\": {\n       \"codResultado\": \"0\",\n       \"mostrar\": \"0\",\n       \"descripcion\": \"Recursos recuperados exitosamente.\"\n   },\n   \"bodegaVirtual\": [\n      {\n         \"bodegaId\": \"1\",\n         \"nombre\": \"BODEGA VIRTUAL 1 DTS 1\",\n         \"idBodegaPadre\": \"42\",\n         \"nivel\": \"2\",\n         \"estado\": \"ALTA\",\n         \"usuario\": \"usuario.pruebas\",\n         \"latitud\": \"1825.15\",\n         \"longitud\": \"7.21\",\n         \"creado_el\": \"2015-10-22 14:20:26.0\",\n         \"creado_por\": \"usuario.pruebas\",\n         \"modificado_el\": \"2015-10-22 14:27:28.0\",\n         \"modificado_por\": \"usuario.pruebas\"\n      },\n      {\n         \"bodegaId\": \"2\",\n         \"nombre\": \"BODEGA VIRTUAL 2 DTS 1\",\n         \"idBodegaPadre\": \"\",\n         \"nivel\": \"1\",\n         \"estado\": \"ALTA\",\n         \"usuario\": \"usuario.pruebas\",\n         \"creado_el\": \"2015-10-22 14:20:26.0\",\n         \"creado_por\": \"usuario.pruebas\"\n      }\n   ]\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n   \"respuesta\": {\n       \"codResultado\": \"-205\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"No existen registros con esos par\\u00E1metros.\",\n       \"clase\": \"OperacionBodegaVirtual\",\n       \"metodo\": \"doGet\",\n       \"excepcion\": \" \",\n       \"tipoExepcion\": \" \"\n   }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioConsulta.java",
    "groupTitle": "Bodegas_Virtuales"
  },
  {
    "type": "POST",
    "url": "/opsidra/modbodegavirtual/",
    "title": "[modBodegaVirtual]",
    "name": "modBodegaVirtual",
    "description": "<p>Servicio para modificar la asociaci\\u00F3n de bodegas virtuales a distribuidores de SIDRA, solo podr\\u00E1 modificarse el nombre de la bodega por uno diferente.</p>",
    "group": "Bodegas_Virtuales",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs del que se desea obtener informaci\\u00F3n de bodegas.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operacion.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idBodega",
            "description": "<p>Identificador de la bodega a modificar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "nombre",
            "description": "<p>Nombre nuevo de la bodega indicada.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "nivel",
            "description": "<p>indicar\\u00E1 si la bodega a modificar es bodega principal de DTS o es bodega virtual de dts. Los niveles ser\\u00E1n: Bodega Principal: 1, Bodega Virtual: 2.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "tipo:",
            "description": "<p>Indica el tipo de bodega de nivel 0, es obligatorio al crear o modificar bodegas de nivel 0.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "latitud",
            "description": "<p>Latitud geogr\\u00E1fica de la bodega, ser\\u00E1 obligatorio solo para nivel 2.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "longitud",
            "description": "<p>Longitud geogr\\u00E1fica de la bodega, ser\\u00E1 obligatorio solo para nivel 2.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "estado",
            "description": "<p>Estado de la bodega.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"codArea\": \"505\",\n    \"usuario\": \"usuario.pruebas\",\n    \"idBodega\": \"73\",\n    \"nombre\": \"BODEGA VIRTUAL\",\n    \"nivel\": \"2\",\n    \"tipo\": \"PRINCIPAL\",\n    \"latitud\": \"42.2\",\n    \"longitud\": \"139.89\",\n    \"estado\": \"ALTA\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n   \"respuesta\": {\n      \"codResultado\": \"201\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"Recurso modificado exitosamente.\",\n       \"clase\": \"OperacionBodegaVirtual\",\n       \"metodo\": \"doPutDel\",\n       \"excepcion\": \" \",\n       \"tipoExepcion\": \" \"\n   }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n   \"respuesta\": {\n       \"codResultado\": \"-205\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"Los siguientes datos faltan o son incorrectos:  Bodega.\",\n       \"clase\": \"CtrlBodegaVirtual\",\n       \"metodo\": \"validarInput\",\n       \"excepcion\": \" \",\n       \"tipoExepcion\": \" \"\n   }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioOperacion.java",
    "groupTitle": "Bodegas_Virtuales"
  },
  {
    "type": "POST",
    "url": "/consultasidra/getbodegascl/",
    "title": "[getBodegasSCL]",
    "name": "getBodegasSCL",
    "description": "<p>Servicio para obtener las bodegas disponibles en el sistema comercial</p>",
    "group": "Bodegas",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de  \\u00E1rea del pa\\u00EDs del que se desea obtener informaci\\u00F3n  de bodegas.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>nombre de usuario que solicita la operacion.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"codArea\": \"505\",\n    \"usuario\": \"usuario.pruebas\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n   \"respuesta\": {\n       \"codResultado\": \"1\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"Ok. Obtenci\\u00F3n  de Bodegas exitosa.\",\n       \"clase\": \" \",\n       \"metodo\": \" \",\n       \"excepcion\": \" \",\n       \"tipoExepcion\": \" \"\n   },\n   \"bodegas\": [\n       {\n           \"idBodega\": \"576\",\n           \"codBodega\": \"600\",\n           \"nombreBodega\": \"UNIDAD DE RETENCI\\u00F3n \"\n       },\n       {\n           \"idBodega\": \"577\",\n           \"codBodega\": \"601\",\n           \"nombreBodega\": \"SEGUROS\"\n       }\n  ]\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:      ",
          "content": "{\n   \"respuesta\": {\n       \"codResultado\": \"-101\",\n       \"mostrar\": \"0\",\n       \"descripcion\": \"Ocurrio un Problema inesperado, contacte a su Administrador.\",\n       \"clase\": \"com.consystec.sc.ca.ws.metodos.Bodegas\",\n       \"metodo\": \"getMensaje\",\n       \"excepcion\": \"ORA-00942: table or view does not exist\\n\",\n       \"tipoExepcion\": \"Excepcion SQL\"\n   }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioConsulta.java",
    "groupTitle": "Bodegas"
  },
  {
    "type": "POST",
    "url": "/opsidra/creabuzon/",
    "title": "[creaBuzon]",
    "name": "creaBuzon",
    "description": "<p>Servicio para crear buzones que ser\\u00E1n utiles en la creaci\\u00F3n de solitudes para el work flow.</p>",
    "group": "Buzones",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo del pa\\u00EDs donde se desea guardar la informaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operacion.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "Nombre",
            "description": "<p>nombre del buz\\u00F3n a crear.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idDistribuidor",
            "description": "<p>id del distribuidor al que se encontrar\\u00E1 asociado el buzon. Este solo se ingresar\\u00E1 en caso que el buz\\u00F3n a registrar sea de nivel 2-3.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idBodegavirtual",
            "description": "<p>id de la bodega virtual al que se encontrar\\u00E1 asociado el buzon. Este solo se ingresar\\u00E1 en caso que el buz\\u00F3n a registrar sea de nivel 3.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "nivel",
            "defaultValue": "1 o 2",
            "description": "<p>nivel del distribuidor a crear .</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "tipoWF",
            "description": "<p>Indica que tipo de solicitud atender\\u00E1 el buzon. Ejemplo: DEVOLUCION, SINIESTRO o TODAS.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n   \"codArea\":\"505\",\n   \"usuario\": \"usuario\",\n   \"nombre\": \"Log\\u00EDstica\",\n   \"idDistribuidor\": \"11\",\n   \"idBodegaVirtual\": \"11\",\n   \"nivel\": \"2\",\n   \"tipoWF\": \"DEVOLUCION\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n   \"idBuzon\": \"1\",\n   \"respuesta\": {\n       \"codResultado\": \"10\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"OK. Creaci\\u00F3n de Buz\\u00F3n exitosa.\",\n       \"clase\": \" \",\n       \"metodo\": \" \",\n       \"excepcion\": \" \",\n       \"tipoExepcion\": \"\"\n   }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n   \"respuesta\": {\n       \"codResultado\": \"-45\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"El par\\u00E1metro de entrada \\\"nombre\"\\ esta vac\\u00EDo.\",\n       \"clase\": \" \",\n       \"metodo\": \"crearBuzon\",\n       \"excepcion\": \"class CtrlBuzonSidra\",\n       \"tipoExepcion\": \"Generales\"\n   }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioOperacion.java",
    "groupTitle": "Buzones"
  },
  {
    "type": "POST",
    "url": "/consultasidra/getbuzones/",
    "title": "[getBuzones]",
    "name": "getBuzones",
    "description": "<p>Servicio para obtener informaci\\u00F3n de los buzones existentes, al no ingresar ning\\u00FAn valor en el input, se obtendr\\u00E1n todos los buzones registrados.</p>",
    "group": "Buzones",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>c\\u00F3digo de area del pa\\u00EDs que se desea obtener la informaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>nombre de usuario que realiza la operacion.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idBuzon",
            "description": "<p>id del buzon que se desea obtener. CAMPO NO OBLIGATORIO.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "nombre",
            "description": "<p>Nombre de buz\\u00F3n a buscar. CAMPO NO OBLIGATORIO.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idDistribuidor",
            "description": "<p>id del distribuidor al que se encontrar\\u00E1 asociado el buzon. Este solo se ingresar\\u00E1 en caso que el buz\\u00F3n a registrar sea de nivel 2.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idBodegaVirtual",
            "description": "<p>id de la bodega virtual a la que se desea consultar. Este solo se ingresar\\u00E1 en caso que el buz\\u00F3n a registrar sea de nivel 3.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "nivel",
            "defaultValue": "1 o 2 o 3",
            "description": "<p>nivel del distribuidor a crear .</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "tipoWF",
            "description": "<p>Indica que tipo de solicitud atender\\u00E1 el buzon. Ejemplo: DEVOLUCION, SINIESTRO o TODAS.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "estado",
            "description": "<p>estado actual que  se desea buscar.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"codArea\": \"505\", \n    \"usuario\":\"usuarioprueba\",\n    \"idBuzon\":\"\",\n    \"nombre\": \"\",\n    \"idDistribuidor\": \"\",\n    \"idBodegaVirtual\": \"\",\n    \"nivel\": \"\",\n    \"tipoWF\": \"\",\n    \"estado\": \"\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": " {\n  \"respuesta\": {\n    \"codResultado\": \"12\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"OK. Datos obtenidos exitosamente\",\n    \"clase\": \" \",\n    \"metodo\": \" \",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"\"\n  },\n  \"buzones\": [\n      {\n          \"idBuzon\": \"2\",\n          \"idDistribuidor\":\"\",\n \t\t   \"nombreDistribuidor\":\"\",\n    \t   \"idBodegaVirtual\":\"\",\n \t\t   \"nombreBodega\":\"\",\n          \"nombre\": \"Logistica\",\n          \"nivel\":\"\",\n \t\t   \"tipoWF\":\"\",\n          \"estado\": \"ALTA\",\n          \"creado_por\": \"usuario.pruebas\",\n          \"creado_el\": \"08/12/2015 00:00:00\",\n          \"modificado_por\": \" \",\n          \"modificado_el\": \" \"\n      },\n      {\n          \"idBuzon\": \"8\",\n          \"idDistribuidor\":\"\",\n \t\t   \"nombreDistribuidor\":\"\",\n \t\t   \"idBodegaVirtual\":\"\",\n \t\t   \"nombreBodega\":\"\",\n          \"nombre\": \"Buzon2\",\n          \"nivel\":\"\",\n \t\t   \"tipoWF\":\"\",\n          \"estado\": \"ALTA\",\n          \"creado_por\": \"usuarioprueba\",\n          \"creado_el\": \"08/12/2015 00:00:00\",\n          \"modificado_por\": \" \",\n          \"modificado_el\": \" \"\n      }\n  ]\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"-392\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"No se encontraron buzones configurados.\",\n    \"clase\": \"CtrlBuzonSidra\",\n    \"metodo\": \"getBuzon\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioConsulta.java",
    "groupTitle": "Buzones"
  },
  {
    "type": "POST",
    "url": "/opsidra/modbuzon/",
    "title": "[modBuzon]",
    "name": "modBuzon",
    "description": "<p>Servicio para modificar buzones que ser\\u00E1n utiles en la creaci\\u00F3n de solitudes para el work flow.</p>",
    "group": "Buzones",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo del pa\\u00EDs donde se desea modificar la informaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operacion.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idBuzon",
            "description": "<p>id del buz\\u00F3n que se desea modificar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "Nombre",
            "description": "<p>nombre del buz\\u00F3n a modificar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "estado",
            "description": "<p>estado que desea aplicarse al buzon.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n   \"codArea\":\"505\",\n   \"usuario\": \"usuario\",\n   \"idBuzon\": \"1\",\n   \"nombre\": \"Log\\u00EDstica\",\n   \"estado\": \"BAJA\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n   \"idBuzon\": \"1\",\n   \"respuesta\": {\n       \"codResultado\": \"11\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"Ok. Buz\\u00F3n modificado exitosamente.\",\n       \"clase\": \" \",\n       \"metodo\": \" \",\n       \"excepcion\": \" \",\n       \"tipoExepcion\": \"\"\n   }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n   \"respuesta\": {\n       \"codResultado\": \"-45\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"El par\\u00E1metro de entrada \\\"nombre\"\\ esta vac\\u00EDo.\",\n       \"clase\": \" \",\n       \"metodo\": \"modBuzon\",\n       \"excepcion\": \"class CtrlBuzonSidra\",\n       \"tipoExepcion\": \"Generales\"\n   }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioOperacion.java",
    "groupTitle": "Buzones"
  },
  {
    "type": "POST",
    "url": "/opsidra/bajacondicionoferta/",
    "title": "[delCondicionOferta]",
    "name": "bajacondicionoferta",
    "description": "<p>Servicio para dar de baja condiciones creadas.</p>",
    "group": "CondicionOferta",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs en el que se desea realizar la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idCondicionOferta",
            "description": "<p>Identificador de la Condici\\u00F3n que se desea borrar.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"codArea\": \"505\",\n    \"usuario\": \"usuario.prueba\",\n    \"idCondicionOferta\": \"1\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"201\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"Recurso modificado exitosamente.\",\n    \"clase\": \"OperacionCondicionOferta\",\n    \"metodo\": \"doDel\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"-201\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"No existe el recurso deseado.\",\n    \"clase\": \"OperacionCondicionOferta\",\n    \"metodo\": \"doDel\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioOperacion.java",
    "groupTitle": "CondicionOferta"
  },
  {
    "type": "POST",
    "url": "/opsidra/creacondicionoferta/",
    "title": "[creacondicionoferta]",
    "name": "creacondicionoferta",
    "description": "<p>Servicio para crear Condiciones a Ofertas por pa\\u00EDs.</p>",
    "group": "CondicionOferta",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs en el que se desea realizar la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idOfertaCampania",
            "description": "<p>Identificador de la Campa\\u00F1a a la que se asociar\\u00E1n las condiciones.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "condiciones",
            "description": "<p>Arreglo con el listado de condiciones a asociar con la Campa\\u00F1a.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "condiciones.nombre",
            "description": "<p>Nombre de la Condici\\u00F3n a crear.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "condiciones.tipoGestion",
            "defaultValue": "ALTA_PREPAGO o VENTA",
            "description": "<p>Tipo de gesti\\u00F3n de la Condici\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "condiciones.tipoCondicionOferta",
            "defaultValue": "VENTA, ARTICULO o PDV",
            "description": "<p>Tipo de la Condici\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "condiciones.detalle",
            "description": "<p>Arreglo con el listado de detalles a asociar con la Condici\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "condiciones.detalle.tipo",
            "defaultValue": "VENTA, ARTICULO, PDV, ZONA o TECNOLOGIA",
            "description": "<p>Tipo al que se le aplica la Condici\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "condiciones.detalle.idArticulo",
            "description": "<p>Identificador del art\\u00EDculo al que se le aplica la Condici\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "condiciones.detalle.tipoCliente",
            "description": "<p>Tipo de cliente al que se le aplica la Condici\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "condiciones.detalle.tecnologia",
            "description": "<p>Tipo de tecnolog\\u00EDa a la que se le aplica la Condici\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "condiciones.detalle.montoInicial",
            "description": "<p>Monto inicial de la condic\\u00ED\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "condiciones.detalle.montoFinal",
            "description": "<p>Monto final de la condic\\u00ED\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "condiciones.detalle.idPDV",
            "description": "<p>Identificador del PDV al que aplica la condici\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "condiciones.detalle.zonaComercialPDV",
            "description": "<p>Zona del PDV al que aplica la condici\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "condiciones.detalle.categoriaPDV",
            "description": "<p>Categoria del PDV al que aplica la condici\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "condiciones.detalle.idArticuloRegalo",
            "description": "<p>Identificador del art\\u00EDculo que se regalar\\u00E1 con la condici\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "condiciones.detalle.cantidadArticuloRegalo",
            "description": "<p>Cantidad de art\\u00EDculos que se regalar\\u00E1n con la condici\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "condiciones.detalle.tipoDescuento",
            "defaultValue": "MONTO o PORCENTAJE",
            "description": "<p>Tipo de descuento a aplicar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "condiciones.detalle.valorDescuento",
            "description": "<p>Valor del descuento a aplicar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "condiciones.detalle.idDescuento",
            "description": "<p>id que identifica el tipo de Descuento FS.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "condiciones.detalle.idProductOffering",
            "description": "<p>id del producto ofertado de FS.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"usuario\": \"usuario.pruebas\",\n    \"codArea\": \"505\",\n    \"idOfertaCampania\": \"9\",\n    \"condiciones\":\n    [\n        {\n            \"nombre\": \"Nombre 1\",\n            \"tipoGestion\": \"VENTA\",\n            \"tipoCondicion\": \"ARTICULO\",\n            \"detalle\":\n            [\n                {\n                    \"tipo\": \"PDV\",\n                    \"idArticulo\": \"\",\n                    \"tipoCliente\": \"\",\n                    \"tecnologia\": \"\",\n                    \"montoInicial\": \"\",\n                    \"montoFinal\": \"\",\n                    \"idPDV\": \"\",\n                    \"zonaComercialPDV\": \"Zona 1\",\n                    \"categoriaPDV\": \"A\",\n                    \"tipoDescuento\": \"MONTO\",\n                    \"valorDescuento\": \"5\",\n                    \"idArticuloRegalo\": \"\",\n                    \"cantidadArticuloRegalo\": \"\",\n                    \"idDescuento\": \"35\",\n                    \"idProductOffering\":\"654646545665\"\n                },\n                {\n                    \"tipo\": \"ZONA\",\n                    \"idArticulo\": \"\",\n                    \"tipoCliente\": \"\",\n                    \"tecnologia\": \"\",\n                    \"montoInicial\": \"\",\n                    \"montoFinal\": \"\",\n                    \"idPDV\": \"55\",\n                    \"zonaComercialPDV\": \"\",\n                    \"categoriaPDV\": \"\",\n                    \"tipoDescuento\": \"PORCENTAJE\",\n                    \"valorDescuento\": \"10\",\n                    \"idArticuloRegalo\": \"\",\n                    \"cantidadArticuloRegalo\": \"\",\n                    \"idDescuento\": \"35\",\n                    \"idProductOffering\":\"654646545665\"\n                }\n            ]\n        },\n        {\n            \"nombre\": \"Nombre 2\",\n            \"tipoGestion\": \"ALTA_PREPAGO\",\n            \"tipoCondicion\": \"GENERICO\",\n            \"detalle\":\n            [\n                {\n                    \"tipo\": \"VENTA\",\n                    \"idArticulo\": \"1\",\n                    \"tipoCliente\": \"CF\",\n                    \"tecnologia\": \"\",\n                    \"montoInicial\": \"1\",\n                    \"montoFinal\": \"\",\n                    \"idPDV\": \"\",\n                    \"zonaComercialPDV\": \"\",\n                    \"categoriaPDV\": \"\",\n                    \"tipoDescuento\": \"MONTO\",\n                    \"valorDescuento\": \"1\",\n                    \"idArticuloRegalo\": \"\",\n                    \"cantidadArticuloRegalo\": \"\",\n                    \"idDescuento\": \"35\",\n                    \"idProductOffering\":\"654646545665\"\n                },\n                {\n                    \"tipo\": \"VENTA\",\n                    \"idArticulo\": \"\",\n                    \"tipoCliente\": \"\",\n                    \"tecnologia\": \"\",\n                    \"montoInicial\": \"2\",\n                    \"montoFinal\": \"2\",\n                    \"idPDV\": \"\",\n                    \"zonaComercialPDV\": \"\",\n                    \"categoriaPDV\": \"\",\n                    \"tipoDescuento\": \"MONTO\",\n                    \"valorDescuento\": \"1\",\n                    \"idArticuloRegalo\": \"\",\n                    \"cantidadArticuloRegalo\": \"\"\n                }\n            ]\n        },\n        {\n            \"nombre\": \"Nombre 3\",\n            \"tipoGestion\": \"VENTA\",\n            \"tipoCondicion\": \"ARTICULO\",\n            \"detalle\":\n            [\n                {\n                    \"tipo\": \"TECNOLOGIA\",\n                    \"idArticulo\": \"\",\n                    \"tipoCliente\": \"\",\n                    \"tecnologia\": \"3G\",\n                    \"montoInicial\": \"\",\n                    \"montoFinal\": \"\",\n                    \"idPDV\": \"\",\n                    \"zonaComercialPDV\": \"\",\n                    \"categoriaPDV\": \"\",\n                    \"tipoDescuento\": \"PORCENTAJE\",\n                    \"valorDescuento\": \"5\",\n                    \"idDescuento\": \"35\",\n                    \"idProductOffering\":\"654646545665\"\n                }\n            ]\n        }\n    ]\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"200\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"Campos agregados exitosamente.\",\n    \"clase\": \"OperacionCondicionOferta\",\n    \"metodo\": \"doPost\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"-412\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"Ya existe una condici\\u00F3n con el nombre enviado. En la condici\\u00F3n 1.\",\n    \"clase\": \"OperacionCondicionOferta\",\n    \"metodo\": \"doPost\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioOperacion.java",
    "groupTitle": "CondicionOferta"
  },
  {
    "type": "POST",
    "url": "/consultasidra/getcondicionoferta/",
    "title": "[getCondicionOferta]",
    "name": "getcondicionoferta",
    "description": "<p>Servicio para obtener los datos de Condiciones de Oferta y sus detalles.</p>",
    "group": "CondicionOferta",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pais.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idCondicion",
            "description": "<p>Identificador de la Condici\\u00F3n que se desea obtener.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idOfertaCampania",
            "description": "<p>Identificador de la Oferta que se desea obtener.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "nombre",
            "description": "<p>Nombre de la Condici\\u00F3n a buscar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "tipoGestion",
            "description": "<p>Tipo de gesti\\u00F3n de la Condici\\u00F3n que se desea.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "tipo",
            "description": "<p>Tipo al que se le aplica la Condici\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idArticulo",
            "description": "<p>Identificador del art\\u00EDculo al que se le aplica la Condici\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idPDV",
            "description": "<p>Identificador del punto de venta que se desea consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "zonaComercialPDV",
            "description": "<p>Zona comercial de puntos de venta que se desea consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "categoriaPDV",
            "description": "<p>Categor\\u00EDa de puntos de venta que se desea consultar.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"usuario\": \"usuario.pruebas\",\n    \"codArea\": \"505\",\n    \"idCondicion\": \"\",\n    \"idOfertaCampania\": \"\",\n    \"nombre\": \"\",\n    \"tipoGestion\": \"\",\n    \"tipo\": \"\",\n    \"idArticulo\": \"\",\n    \"idPDV\": \"\",\n    \"zonaComercialPDV\": \"\",\n    \"categoriaPDV\": \"\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"0\",\n    \"mostrar\": \"0\",\n    \"descripcion\": \"Recursos recuperados exitosamente.\"\n  },\n  \"condiciones\": [\n    {\n      \"idCondicion\": \"40\",\n      \"idOfertaCampania\": \"9\",\n      \"nombreCampania\": \"Oferta ruta\",\n      \"nombre\": \"Nombre 2\",\n      \"tipoGestion\": \"ALTA_PREPAGO\",\n      \"tipoCondicion\": \"GENERICO\",\n      \"estado\": \"ALTA\",\n      \"creadoEl\": \"04/03/2016 14:45:28\",\n      \"creadoPor\": \"usuario.pruebas\",\n      \"detalle\": [\n        {\n          \"idCondicion\": \"40\",\n          \"tipo\": \"ARTICULO\",\n          \"idArticulo\": \"15\",\n          \"nombreArticulo\": \"BLACKBERRY Q10 NEGRO\",\n          \"tipoCliente\": \"PDV\",\n          \"tecnologia\": \"\",\n          \"montoInicial\": \"1\",\n          \"montoFinal\": \"\",\n          \"tipoDescuento\": \"MONTO\",\n          \"valorDescuento\": \"1\",\n          \"idPDV\": \"\",\n          \"nombrePDV\": \"\",\n          \"zonaComercialPDV\": \"\",\n          \"categoriaPDV\": \"\",\n          \"idArticuloRegalo\": \"\",\n          \"nombreArticuloRegalo\": \"\",\n          \"cantidadArticuloRegalo\": \"\",\n          \"tipoDescuentoRegalo\": \"\",\n          \"valorDescuentoRegalo\": \"\",\n          \"estado\": \"ALTA\",\n          \"creadoPor\": \"usuario.pruebas\",\n          \"creadoEl\": \"04/03/2016 14:45:28\",\n          \"modificadoPor\": \"\",\n          \"modificadoEl\": \"\"\n        },\n        {\n          \"idCondicion\": \"40\",\n          \"tipo\": \"VENTA\",\n          \"idArticulo\": \"\",\n          \"nombreArticulo\": \"\",\n          \"tipoCliente\": \"\",\n          \"tecnologia\": \"\",\n          \"montoInicial\": \"2\",\n          \"montoFinal\": \"2\",\n          \"tipoDescuento\": \"MONTO\",\n          \"valorDescuento\": \"1\",\n          \"idPDV\": \"\",\n          \"nombrePDV\": \"\",\n          \"zonaComercialPDV\": \"\",\n          \"categoriaPDV\": \"\",\n          \"idArticuloRegalo\": \"\",\n          \"nombreArticuloRegalo\": \"\",\n          \"cantidadArticuloRegalo\": \"\",\n          \"tipoDescuentoRegalo\": \"\",\n          \"valorDescuentoRegalo\": \"\",\n          \"estado\": \"ALTA\",\n          \"creadoPor\": \"usuario.pruebas\",\n          \"creadoEl\": \"04/03/2016 14:45:28\",\n          \"modificadoPor\": \"\",\n          \"modificadoEl\": \"\"\n        }\n      ]\n    }\n  ]\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"-396\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"No se encontraron condiciones de oferta configuradas.\",\n    \"clase\": \"OperacionCondicionOferta\",\n    \"metodo\": \"doGet\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioConsulta.java",
    "groupTitle": "CondicionOferta"
  },
  {
    "type": "POST",
    "url": "/consultasidra/getofertasruta/",
    "title": "[getofertasruta]",
    "name": "getofertasruta",
    "description": "<p>Servicio para obtener los datos de Condiciones de Oferta de una ruta espec\\u00EDfica.</p>",
    "group": "CondicionOferta",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codDispositivo",
            "description": "<p>c\\u00F3digo del dispositivo desde donde se realiza la operaci\\u00F3n, en caso de utilizarse la APP M\\u00F3VIL.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "token",
            "description": "<p>token de validaci\\u00F3n de autenticaci\\u00F3n de usuario.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pais.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idRuta",
            "description": "<p>Identificador de la ruta a consultar.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo parametros de Entrada:",
          "content": "{\n    \"token\": \"WEB\",\n    \"codDispositivo\": \"B81A18BA1F8131C818E91CA3D19\",\n    \"usuario\": \"usuario.pruebas\",\n    \"codArea\": \"505\",\n    \"idRuta\": \"10\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "{} JSON Respuesta-\\u00E9xito:",
          "content": "{\n  \"token\": \"WEB\",\n  \"respuesta\": {\n    \"codResultado\": \"202\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"Recursos recuperados exitosamente.\",\n    \"clase\": \"OperacionCondicionOferta\",\n    \"metodo\": \"doGet\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"\"\n  },\n  \"condicionesPdv\": [{\n    \"idCondicion\": \"2\",\n    \"idOfertaCampania\": \"5\",\n    \"nombreCampania\": \"Camapa\\u00F1a Descuentos Esperanza\",\n    \"nombre\": \"Condicion PDV Punto 10\",\n    \"tipoGestion\": \"VENTA\",\n    \"tipoCondicion\": \"ARTICULO\",\n    \"estado\": \"ALTA\",\n    \"creadoEl\": \"28/03/2017 17:27:28\",\n    \"creadoPor\": \"usuario.pruebas\",\n    \"modificadoEl\": \"\",\n    \"modificadoPor\": \"\",\n    \"detalle\": [\n      {\n        \"idCondicion\": \"1\",\n        \"tipo\": \"PDV\",\n        \"idArticulo\": \"2\",\n        \"nombreArticulo\": \"BLACKBERRY Q10 NEGRO\",\n        \"tipoCliente\": \"PDV\",\n        \"tecnologia\": \"\",\n        \"tipoInv\": \"INV_TELCA\",\n        \"montoInicial\": \"1\",\n        \"montoFinal\": \"0\",\n        \"tipoDescuento\": \"PORCENTAJE\",\n        \"valorDescuento\": \"5\",\n        \"idPDV\": \"1\",\n        \"nombrePDV\": \"Reforma 7\",\n        \"zonaComercialPDV\": \"\",\n        \"categoriaPDV\": \"\",\n        \"idArticuloRegalo\": \"103\",\n        \"nombreArticuloRegalo\": \"TARJETA RASCA $C20\",\n        \"cantidadArticuloRegalo\": \"1\",\n        \"tipoDescuentoRegalo\": \"PORCENTAJE\",\n        \"valorDescuentoRegalo\": \"100\",\n        \"estado\": \"ALTA\",\n        \"creadoPor\": \"usuario.pruebas\",\n        \"creadoEl\": \"28/03/2017 17:27:28\",\n        \"modificadoPor\": \"\",\n        \"modificadoEl\": \"\"\n      },\n      {\n        \"idCondicion\": \"2\",\n        \"tipo\": \"PDV\",\n        \"idArticulo\": \"4\",\n        \"nombreArticulo\": \"BLACKBERRY Q10 NEGRO\",\n        \"tipoCliente\": \"PDV\",\n        \"tecnologia\": \"\",\n        \"tipoInv\": \"INV_TELCA\",\n        \"montoInicial\": \"1\",\n        \"montoFinal\": \"0\",\n        \"tipoDescuento\": \"PORCENTAJE\",\n        \"valorDescuento\": \"5\",\n        \"idPDV\": \"2\",\n        \"nombrePDV\": \"Reforma 14\",\n        \"zonaComercialPDV\": \"\",\n        \"categoriaPDV\": \"\",\n        \"idArticuloRegalo\": \"103\",\n        \"nombreArticuloRegalo\": \"TARJETA RASCA $C20\",\n        \"cantidadArticuloRegalo\": \"1\",\n        \"tipoDescuentoRegalo\": \"PORCENTAJE\",\n        \"valorDescuentoRegalo\": \"100\",\n        \"estado\": \"ALTA\",\n        \"creadoPor\": \"usuario.pruebas\",\n        \"creadoEl\": \"28/03/2017 17:27:28\",\n        \"modificadoPor\": \"\",\n        \"modificadoEl\": \"\"\n      }\n    ]\n  }],\n  \"condicionesZonaCat\": [{\n    \"idCondicion\": \"2\",\n    \"idOfertaCampania\": \"5\",\n    \"nombreCampania\": \"Campa\\u00F1a Descuentos Esperanza\",\n    \"nombre\": \"Condicion PDV Punto 10\",\n    \"tipoGestion\": \"VENTA\",\n    \"tipoCondicion\": \"ARTICULO\",\n    \"estado\": \"ALTA\",\n    \"creadoEl\": \"28/03/2017 17:27:28\",\n    \"creadoPor\": \"usuario.pruebas\",\n    \"modificadoEl\": \"\",\n    \"modificadoPor\": \"\",\n    \"detalle\": [{\n      \"idCondicion\": \"91\",\n      \"tipo\": \"ZONA\",\n      \"idArticulo\": \"130\",\n      \"nombreArticulo\": \"BLACKBERRY Q10 NEGRO\",\n      \"tipoCliente\": \"PDV\",\n      \"tecnologia\": \"\",\n      \"tipoInv\": \"INV_TELCA\",\n      \"montoInicial\": \"1\",\n      \"montoFinal\": \"0\",\n      \"tipoDescuento\": \"PORCENTAJE\",\n      \"valorDescuento\": \"5\",\n      \"idPDV\": \"15\",\n      \"nombrePDV\": \"Managua Central\",\n      \"zonaComercialPDV\": \"Managua\",\n      \"categoriaPDV\": \"\",\n      \"idArticuloRegalo\": \"103\",\n      \"nombreArticuloRegalo\": \"TARJETA RASCA $C20\",\n      \"cantidadArticuloRegalo\": \"1\",\n      \"tipoDescuentoRegalo\": \"PORCENTAJE\",\n      \"valorDescuentoRegalo\": \"100\",\n      \"estado\": \"ALTA\",\n      \"creadoPor\": \"usuario.pruebas\",\n      \"creadoEl\": \"28/03/2017 17:27:28\",\n      \"modificadoPor\": \"\",\n      \"modificadoEl\": \"\"\n    }]\n  }]\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "{} JSON Respuesta-Error:     ",
          "content": "{\n  \"token\": \"WEB\",\n  \"respuesta\": {\n    \"codResultado\": \"-396\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"No se encontraron condiciones de oferta configuradas.\",\n    \"clase\": \"OperacionCondicionOferta\",\n    \"metodo\": \"doGet\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioConsulta.java",
    "groupTitle": "CondicionOferta"
  },
  {
    "type": "POST",
    "url": "/opsidra/creacondicion/",
    "title": "[creacondicion]",
    "name": "creacondicion",
    "description": "<p>Servicio para crear Condiciones a Campa\\u00F1as por pa\\u00EDs.</p>",
    "group": "Condicion",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs en el que se desea realizar la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idOfertaCampania",
            "description": "<p>Identificador de la Campa\\u00F1a a la que se asociar\\u00E1n las condiciones.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "condiciones",
            "description": "<p>Arreglo con el listado de Condiciones a asociar con la Campa\\u00F1a.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "nombre",
            "description": "<p>Nombre de la Condici\\u00F3n a crear.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "tipoGestion",
            "description": "<p>Tipo de gesti\\u00F3n de la Condici\\u00F3n (Permitidos \\u00FAnicamente los valores configurados en la aplicaci\\u00F3n).</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "tipoCondicion",
            "description": "<p>Tipo de la Condici\\u00F3n (Permitidos \\u00FAnicamente los valores configurados en la aplicaci\\u00F3n).</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "detalle",
            "description": "<p>Arreglo con el listado de detalles a asociar con la Condici\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "tipo",
            "description": "<p>Tipo al que se le aplica la Condici\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idArticulo",
            "description": "<p>Identificador del art\\u00EDculo al que se le aplica la Condici\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "montoInicial",
            "description": "<p>Monto inicial de la condic\\u00ED\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "montoFinal",
            "description": "<p>Monto final de la condic\\u00ED\\u00F3n.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"usuario\": \"usuario.pruebas\",\n    \"codArea\": \"505\",\n    \"idOfertaCampania\": \"3\",\n    \"condiciones\":\n    [\n        {\n            \"nombre\": \"Nombre\",\n            \"tipoGestion\": \"VENTA\",\n            \"tipoCondicion\": \"GENERICO\",\n            \"detalle\":\n            [\n                {\n                    \"tipo\": \"tipo 1\",\n                    \"idArticulo\": \"\",\n                    \"montoInicial\": \"1\",\n                    \"montoFinal\": \"2\"\n                },\n                {\n                    \"tipo\": \"\",\n                    \"idArticulo\": \"3\",\n                    \"montoInicial\": \"7\",\n                    \"montoFinal\": \"\"\n                }\n            ]\n        },\n        {\n            \"nombre\": \"Nombre 2\",\n            \"tipoGestion\": \"ALTA_PREPAGO\",\n            \"tipoCondicion\": \"ARTICULO\",\n            \"detalle\":\n            [\n                {\n                    \"tipo\": \"\",\n                    \"idArticulo\": \"1\",\n                    \"montoInicial\": \"21\",\n                    \"montoFinal\": \"\"\n                }\n            ]\n        }\n    ]\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"200\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"Campos agregados exitosamente.\",\n    \"clase\": \"OperacionCondicion\",\n    \"metodo\": \"doPost\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"-412\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"Ya existe una condici\\u00F3n con el nombre enviado. En la condici\\u00F3n 1.\",\n    \"clase\": \"OperacionCondicion\",\n    \"metodo\": \"doPost\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioOperacion.java",
    "groupTitle": "Condicion"
  },
  {
    "type": "POST",
    "url": "/opsidra/bajacondicion/",
    "title": "[delCondicion]",
    "name": "delcondicion",
    "description": "<p>Servicio para dar de baja condiciones creadas.</p>",
    "group": "Condicion",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs en el que se desea realizar la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idCondicion",
            "description": "<p>Identificador de la Condici\\u00F3n que se desea borrar.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"codArea\": \"505\",\n    \"usuario\": \"usuario.prueba\",\n    \"idCondicion\": \"1\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"201\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"Recurso modificado exitosamente.\",\n    \"clase\": \"OperacionCondicion\",\n    \"metodo\": \"doDel\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"-201\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"No existe el recurso deseado.\",\n    \"clase\": \"OperacionCondicion\",\n    \"metodo\": \"doDel\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioOperacion.java",
    "groupTitle": "Condicion"
  },
  {
    "type": "POST",
    "url": "/consultasidra/getcondicion/",
    "title": "[getCondicion]",
    "name": "getCondicion",
    "description": "<p>Servicio para obtener los datos de Condiciones de Campa\\u00F1as y sus detalles.</p>",
    "group": "Condicion",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pais.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idCondicion",
            "description": "<p>Identificador de la Condici\\u00F3n que se desea obtener. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idOfertaCampania",
            "description": "<p>Identificador de la Campa\\u00F1a que se desea obtener. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "nombre",
            "description": "<p>Nombre de la Condici\\u00F3n a buscar. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "tipoGestion",
            "description": "<p>Tipo de gesti\\u00F3n de la Condici\\u00F3n que se desea. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "tipo",
            "description": "<p>Tipo al que se le aplica la Condici\\u00F3n. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idArticulo",
            "description": "<p>Identificador del art\\u00EDculo al que se le aplica la Condici\\u00F3n. Este campo es opcional.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"usuario\": \"usuario.pruebas\",\n    \"codArea\": \"505\",\n    \"idCondicion\": \"\",\n    \"idOfertaCampania\": \"\",\n    \"nombre\": \"\",\n    \"tipoGestion\": \"\",\n    \"tipo\": \"\",\n    \"idArticulo\": \"\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"0\",\n    \"mostrar\": \"0\",\n    \"descripcion\": \"Recursos recuperados exitosamente.\"\n  },\n  \"condiciones\": [\n    {\n      \"idCondicion\": \"23\",\n      \"idOfertaCampania\": \"3\",\n      \"nombreCampania\": \"Oferta 1\",\n      \"nombre\": \"Nombre\",\n      \"tipoGestion\": \"VENTA\",\n      \"tipoCondicion\": \"GENERICO\",\n      \"estado\": \"ALTA\",\n      \"creadoEl\": \"03/03/2016 08:52:27\",\n      \"creadoPor\": \"usuario.pruebas\",\n      \"detalle\": [\n        {\n          \"idCondicion\": \"23\",\n          \"tipo\": \"TIPO 1\",\n          \"idArticulo\": \"\",\n          \"tipoInv\": \"\",\n          \"montoInicial\": \"1\",\n          \"montoFinal\": \"20\",\n          \"estado\": \"ALTA\",\n          \"creadoEl\": \"03/03/2016 08:52:27\"\n        },\n        {\n          \"idCondicion\": \"23\",\n          \"tipo\": \"\",\n          \"idArticulo\": \"3\",\n          \"tipoInv\": \"INV_TELCA\",\n          \"montoInicial\": \"7\",\n          \"montoFinal\": \"\",\n          \"estado\": \"ALTA\",\n          \"creadoEl\": \"03/03/2016 08:52:27\"\n        }\n      ]\n    },\n    {\n      \"idCondicion\": \"24\",\n      \"idOfertaCampania\": \"3\",\n      \"nombreCampania\": \"Oferta 1\",\n      \"nombre\": \"Nombre 2\",\n      \"tipoGestion\": \"ALTA_PREPAGO\",\n      \"tipoCondicion\": \"ARTICULO\",\n      \"estado\": \"ALTA\",\n      \"creadoEl\": \"03/03/2016 08:52:27\",\n      \"creadoPor\": \"usuario.pruebas\",\n      \"detalle\": [\n        {\n          \"idCondicion\": \"24\",\n          \"tipo\": \"\",\n          \"idArticulo\": \"1\",\n          \"tipoInv\": \"INV_TELCA\",\n          \"montoInicial\": \"21\",\n          \"montoFinal\": \"\",\n          \"estado\": \"ALTA\",\n          \"creadoEl\": \"03/03/2016 08:52:27\"\n        },\n        {\n          \"idCondicion\": \"24\",\n          \"tipo\": \"\",\n          \"idArticulo\": \"2\",\n          \"tipoInv\": \"INV_TELCA\",\n          \"montoInicial\": \"2\",\n          \"montoFinal\": \"\",\n          \"estado\": \"ALTA\",\n          \"creadoEl\": \"03/03/2016 08:52:27\"\n        }\n      ]\n    }\n  ]\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"-391\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"No se encontraron condiciones de campa\\u00F1a configuradas.\",\n    \"clase\": \"OperacionCondicion\",\n    \"metodo\": \"doGet\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioConsulta.java",
    "groupTitle": "Condicion"
  },
  {
    "type": "POST",
    "url": "/opsidra/crearcatalogo/",
    "title": "[CrearCatalogo]",
    "name": "CrearCatalogo",
    "description": "<p>Servicio para crear grupo de catalogos de configuraciones por pais</p>",
    "group": "Configuraciones",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pais del que se desea obtener informaci\\u00F3n de bodegas.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>nombre de usuario que solicita la operacion.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "grupoParametro",
            "description": "<p>Nombre del grupo de catalago a crear.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "nombre",
            "description": "<p>nombre del par\\u00E1metro.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "valor",
            "description": "<p>valor que tendr\\u00E1  el par\\u00E1metro a crear.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "descripcion",
            "description": "<p>descripci\\u00F3n del par\\u00E1metro a crear</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n   \"codArea\": \"505\",\n   \"usuario\": \"usuario.pruebas\",\n   \"grupoParametro\": \"Grupo 4\",\n   \"par\\u00E1metros\": [\n       {\n           \"nombre\": \"Nombre Nuevo 1\",\n           \"valor\": \"Valor Nuevo 1\",\n           \"descripcion\": \"descripci\\u00F3n Nueva 1\"\n       },\n       {\n           \"nombre\": \"Nombre Nuevo 2\",\n           \"valor\": \"Valor Nuevo 2\",\n           \"descripcion\": \"descripci\\u00F3n Nueva 2\"\n       }\n   ]\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n   \"respuesta\": {\n       \"codResultado\": \"200\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"Campos agregados exitosamente.\",\n       \"clase\": \"OperacionCatalogo\",\n       \"metodo\": \"doPost\",\n       \"excepcion\": \" \",\n       \"tipoExepcion\": \" \"\n   }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:      ",
          "content": "{\n   \"respuesta\": {\n       \"codResultado\": \"-199\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"Los siguientes datos faltan o son incorrectos:  Datos del par\\u00E1metro #1. Datos del par\\u00E1metro #2.\",\n       \"clase\": \"CtrlCatalogo\",\n       \"metodo\": \"validarInput\",\n       \"excepcion\": \" \",\n       \"tipoExepcion\": \" \"\n   }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioOperacion.java",
    "groupTitle": "Configuraciones"
  },
  {
    "type": "POST",
    "url": "/opsidra/modificarcatalogo/",
    "title": "[ModificarCatalogo]",
    "name": "ModificarCatalogo",
    "description": "<p>Servicio para modificar grupo de catalogos de configuraciones por pais</p>",
    "group": "Configuraciones",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pais del que se desea obtener informaci\\u00F3n de bodegas.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>nombre de usuario que solicita la operacion.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "grupoParametro",
            "description": "<p>Nombre del grupo de catalago a modificar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "nombre",
            "description": "<p>nombre del par\\u00E1metro a modificar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "valor",
            "description": "<p>valor que tendr\\u00E1  el par\\u00E1metro a modificar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "descripcion",
            "description": "<p>descripci\\u00F3n del par\\u00E1metro a modificar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "estado",
            "description": "<p>valor del estado del par\\u00E1metro a modificar.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n   \"codArea\": \"505\",\n   \"usuario\": \"usuario.pruebas\",\n   \"grupoParametro\": \"Grupo 4\",\n   \"par\\u00E1metros\": [\n       {\n           \"nombre\": \"Nombre par\\u00E1metro 1\",\n           \"valor\": \"Valor editado 1\",\n           \"descripcion\": \"descripci\\u00F3n Nueva 1\",\n           \"estado\": \"ALTA\"\n       }\n   ]\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n  \"respuesta\": {\n       \"codResultado\": \"201\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"Recurso modificado exitosamente.\",\n       \"clase\": \"OperacionCatalogo\",\n       \"metodo\": \"doPutDel\",\n       \"excepcion\": \" \",\n       \"tipoExepcion\": \" \"\n   }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:      ",
          "content": "{\n   \"respuesta\": {\n       \"codResultado\": \"-199\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"Los siguientes datos faltan o son incorrectos:  Datos del par\\u00E1metro #1. Datos del par\\u00E1metro #2.\",\n       \"clase\": \"CtrlCatalogo\",\n       \"metodo\": \"validarInput\",\n       \"excepcion\": \" \",\n       \"tipoExepcion\": \" \"\n   }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioOperacion.java",
    "groupTitle": "Configuraciones"
  },
  {
    "type": "POST",
    "url": "/opsidra/bajaCatalogo/",
    "title": "[bajaCatalogo]",
    "name": "bajaCatalogo",
    "description": "<p>Servicio para dar de baja un grupo de catalogos de configuraciones por pa\\u00EDs.</p>",
    "group": "Configuraciones",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs del que se desea obtener informaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operacion.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "grupoParametro",
            "description": "<p>Nombre del grupo de catalago a dar de baja.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "nombre",
            "description": "<p>Nombre del par\\u00E1metro a dar de baja.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n   \"codArea\": \"505\",\n   \"usuario\": \"usuario.pruebas\",\n   \"grupoParametro\": \"Grupo 1\",\n   \"par\\u00E1metros\": [\n       {\n           \"nombre\": \"Nombre par\\u00E1metro 1\"\n       }\n   ]\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n   \"respuesta\": {\n       \"codResultado\": \"201\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"Recurso modificado exitosamente.\",\n       \"clase\": \"OperacionCatalogo\",\n       \"metodo\": \"doPutDel\",\n       \"excepcion\": \" \",\n       \"tipoExepcion\": \"\"\n   }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n   \"respuesta\": {\n       \"codResultado\": \"-201\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"No existe el recurso deseado.\",\n       \"clase\": \"OperacionCatalogo\",\n       \"metodo\": \"doPutDel\",\n       \"excepcion\": \" \",\n       \"tipoExepcion\": \"\"\n   }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioOperacion.java",
    "groupTitle": "Configuraciones"
  },
  {
    "type": "POST",
    "url": "/consultasidra/getcatalogo/",
    "title": "[getConfiguracionCatalogo]",
    "name": "getConfiguracionCatalogo",
    "description": "<p>Servicio para obtener los grupos de catalogos configurados por pa\\u00EDs</p>",
    "group": "Configuraciones",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operacion.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "grupoParametro",
            "description": "<p>Nombre del grupo de par\\u00E1metros que se desea obtener. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "nombre",
            "description": "<p>Nombre del par\\u00E1metro que se desea obtener. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "estado",
            "description": "<p>Estado del par\\u00E1metro que se desea obtener. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "tipoParametro",
            "description": "<p>Tipo del par\\u00E1metro que se desea obtener. Este campo es opcional (1 = Interno o 0 = Externo).</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"usuario\": \"usuario.pruebas\",\n    \"grupoParametro\": \"\",\n    \"par\\u00E1metros\": [\n        {\n            \"nombre\": \"\",\n            \"estado\": \"\",\n            \"tipoParametro\": \"\"\n        }\n    ]\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n   \"respuesta\": {\n       \"codResultado\": \"0\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"Recursos recuperados exitosamente.\",\n       \"clase\": \" \",\n       \"metodo\": \" \",\n       \"excepcion\": \" \",\n       \"tipoExepcion\": \" \"\n   },\n   \"grupoParametro\":\"Grupo1\",\n   \"grupo\": [\n       {\n         \"grupo\": \"Grupo 1\",\n         \"par\\u00E1metros\": [\n           {\n             \"codArea\": \"505\",\n             \"grupo\": \"Grupo 1\",\n             \"nombre\": \"Nombre 1\",\n             \"valor\": \"Valor 1\",\n             \"descripcion\": \"Descripcion 1\",\n             \"estado\": \"ALTA\",\n             \"creado_el\": \"2015-10-22 14:20:26.0\",\n             \"creado_por\": \"usuario.pruebas\",\n             \"modificado_el\": \"2015-10-22 14:27:28.0\",\n             \"modificado_por\": \"usuario.pruebas\"\n           },\n           {\n             \"codArea\": \"505\",\n             \"grupo\": \"Grupo 1\",\n             \"nombre\": \"Nombre 2\",\n             \"valor\": \"Valor 2\",\n             \"descripcion\": \"Descripcion 2\",\n             \"estado\": \"ALTA\",\n             \"creado_el\": \"2015-10-22 14:20:26.0\",\n             \"creado_por\": \"usuario.pruebas\",\n             \"modificado_el\": \"2015-10-22 14:27:28.0\",\n             \"modificado_por\": \"usuario.pruebas\"\n           }\n         ]\n       },\n       {\n         \"grupo\": \"Grupo 2\",\n         \"par\\u00E1metros\": {\n           \"codArea\": \"505\",\n           \"grupo\": \"Grupo 2\",\n           \"nombre\": \"Nombre 2\",\n           \"valor\": \"Valor 2\",\n           \"descripcion\": \"Descripcion 2\",\n           \"estado\": \"ALTA\",\n           \"creado_el\": \"2015-10-22 14:20:26.0\",\n           \"creado_por\": \"usuario.pruebas\",\n           \"modificado_el\": \"2015-10-22 14:27:28.0\",\n           \"modificado_por\": \"usuario.pruebas\"\n         }\n       }\n    ]\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n   \"respuesta\": {\n       \"codResultado\": \"-205\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"No existen registros con esos par\\u00E1metros.\",\n       \"clase\": \"OperacionCatalogo\",\n       \"metodo\": \"doGet\",\n       \"excepcion\": \" \",\n       \"tipoExepcion\": \" \"\n   }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioConsulta.java",
    "groupTitle": "Configuraciones"
  },
  {
    "type": "POST",
    "url": "/consultasidra/getcantinv/",
    "title": "[getCantInv]",
    "name": "getCantInv",
    "description": "<p>Servicio para obtener los datos del inventario de bodegas virtuales.</p>",
    "group": "Consulta",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pais.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "token",
            "description": "<p>Token de autenticaci\\u00F3n de sesion de usuario.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operacion.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codDispositivo",
            "description": "<p>c\\u00F3digo del dispositivo desde donde se realiza la operaci\\u00F3n, en caso de utilizarse la APP M\\u00F3VIL.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idBodega",
            "description": "<p>Identificador de la Bodega Virtual que se desea consultar. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idArticulo",
            "description": "<p>Identificador del art\\u00EDculo que se desea consultar. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "serie",
            "description": "<p>Serie que se desea consultar. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "descripcion",
            "description": "<p>Descripci\\u00F3n del art\\u00EDculo que se desea consultar. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "tipoInv",
            "description": "<p>Tipo de inventario que se desea consultar. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "estado",
            "description": "<p>Estado que se desea consultar. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "numTraspasoScl",
            "description": "<p>N\\u00FAmero de traspaso SCL que se desea consultar. Este campo es opcional.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"token\": \"WEB\",\n    \"codArea\": \"505\",\n    \"usuario\": \"usuario.pruebas\",\n    \"codDispositivo\": \"64E1CE4A48BF656974C376B7905F7E62\",\n    \"idBodega\": \"\",\n    \"idArticulo\": \"\",\n    \"serie\": \"\",\n    \"descripcion\": \"\",\n    \"tipoInv\": \"\",\n    \"estado\": \"\",\n    \"numTraspasoScl\": \"\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"0\",\n    \"mostrar\": \"0\",\n    \"descripcion\": \"Recursos recuperados exitosamente.\"\n  },\n  \"datos\": [\n    {\n      \"idBodega\": \"203\",\n      \"cantInv\": \"10\",\n      \"cantTotalInv\": \"301\"\n    },\n    {\n      \"idBodega\": \"205\",\n      \"cantInv\": \"368\",\n      \"cantTotalInv\": \"3001\"\n    },\n    {\n      \"idBodega\": \"206\",\n      \"cantInv\": \"27\",\n      \"cantTotalInv\": \"72\"\n    }\n  ]\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"-199\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"Los siguientes datos faltan o son incorrectos:  Datos no num\\u00E9ricos en ID Bodega Virtual.\",\n    \"clase\": \"CtrlConsultaCantInv\",\n    \"metodo\": \"validarInput\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioConsulta.java",
    "groupTitle": "Consulta"
  },
  {
    "type": "POST",
    "url": "/consultasidra/getinventario/",
    "title": "[getInventario]",
    "name": "getInventario",
    "description": "<p>Servicio para obtener los datos del inventario de bodegas virtuales.</p>",
    "group": "Consulta",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pais.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "token",
            "description": "<p>Token de autenticaci\\u00F3n de sesion de usuario.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operacion.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codDispositivo",
            "description": "<p>c\\u00F3digo del dispositivo desde donde se realiza la operaci\\u00F3n, en caso de utilizarse la APP M\\u00F3VIL.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idBodega",
            "description": "<p>Identificador de la Bodega Virtual que se desea consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idArticulo",
            "description": "<p>Identificador del art\\u00EDculo que se desea consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "descripcion",
            "description": "<p>Nombre del art\\u00EDculo que se desea consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "serie",
            "description": "<p>Serie del art\\u00EDculo que se desea consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "tipoInv",
            "description": "<p>Tipo de Inventario que se desea consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "tipoGrupo",
            "description": "<p>Grupo que se desea consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "seriado",
            "description": "<p>Tipo de Art\\u00EDculo que se desea, 1 seriados o 0 no seriados.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idVendedor",
            "description": "<p>Identificador del vendedor que se desea consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "estado",
            "description": "<p>Estado del art\\u00EDculo que se desea consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "tecnologia",
            "description": "<p>Tipo de tecnolog\\u00EDa del art\\u00EDculo.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "min",
            "description": "<p>Registro m\\u00EDnimo a consultar para pagineo.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "max",
            "description": "<p>Registro m\\u00E1ximo a consultar para pagineo.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "mostrarDetalle",
            "description": "<p>Campo que sirve para indicar si se debe mostrar el detalle de los art\\u00EDculos o no (1 = S\\u00ED, 0 = No).</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "datosWeb",
            "description": "<p>Par\\u00E1metro que indica si se deben mostrar los datos para la aplicaci\\u00F3n web o normales (1 = S\\u00ED, 0 = No).</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "mostrarRecarga",
            "description": "<p>Campo que sirve para indicar si se debe mostrar el art\\u00EDculo de recarga o no (1 = S\\u00ED, 0 = No).</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "numTraspasoScl",
            "description": "<p>N\\u00FAmero de traspaso SCL.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"token\": \"WEB\",\n    \"codArea\": \"505\",\n    \"usuario\": \"usuario.pruebas\",\n    \"codDispositivo\": \"\",\n    \"idBodega\": \"\",\n    \"idArticulo\": \"\",\n    \"descripcion\": \"\",\n    \"serie\": \"\",\n    \"tipoInv\": \"\",\n    \"tipoGrupo\": \"\",\n    \"seriado\": \"\",\n    \"idVendedor\": \"\",\n    \"estado\": \"\",\n    \"tecnologia\": \"\",\n    \"min\": \"\",\n    \"max\": \"\",\n    \"mostrarDetalle\": \"\",\n    \"datosWeb\": \"\",\n    \"mostrarRecarga\": \"\",\n    \"numTraspasoScl\": \"\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"0\",\n    \"mostrar\": \"0\",\n    \"descripcion\": \"Recursos recuperados exitosamente.\"\n  },\n  \"inventario\": [\n    {\n      \"bodegas\": [\n        {\n          \"idBodega\": \"73\",\n          \"nombreBodega\": \"BODEGA PRUEBA\",\n          \"grupos\": [\n            {\n              \"grupo\": \"OTRO\",\n              \"articulos\": [\n                {\n                   \"idArticulo\": \"9\",\n                   \"descripcion\": \"Articulo 1\",\n                   \"precioScl\": \"\",\n                   \"cantidad\": \"39\",\n                   \"seriado\": \"0\",\n                   \"tipoInv\": \"INV_SIDRA\",\n                   \"tecnologia\": \"\",\n                   \"detalleArticulo\": {\n                     \"idInventario\": \"645\",\n                     \"tipoInv\": \"INV_SIDRA\",\n                     \"estado\": \"Disponible\",\n                     \"numTelefono\": \"32166897\",\n                      \"icc\": \"8950304203612907440\",\n                      \"imei\": \"451815082155740\"\n                     \"creado_el\": \"28/12/2015 17:43:17\",\n                     \"creado_por\": \"usuarioprueba\",\n                     \"modificado_el\": \"28/12/2015 17:44:24\",\n                     \"modificado_por\": \"usuario.pruebas\"\n                   }\n                 },\n                 {\n                   \"idArticulo\": \"9\",\n                   \"descripcion\": \"Articulo 2\",\n                   \"cantidad\": \"5\",\n                   \"seriado\": \"0\",\n                   \"tipoInv\": \"INV_SIDRA\",\n                   \"detalleArticulo\": {\n                     \"idInventario\": \"640\",\t\n                     \"idSolicitud\": \"232\",\n                     \"tipoInv\": \"INV_SIDRA\",\n                     \"estado\": \"En Proceso Devoluci\\u00F3n\",\n                     \"numTelefono\": \"32165497\",\n                     \"icc\": \"8950301303609907440\",\n                     \"imei\": \"451819882155740\"\n                     \"creado_el\": \"28/12/2015 17:18:31\",\n                     \"creado_por\": \"sergio.lujan\"\n                   }\n                 },\n              ]\n            },\n            {\n              \"grupo\": \"TERMINAL\",\n              \"articulos\": [\n                {\n                  \"idArticulo\": \"8\",\n                  \"descripcion\": \"Articulo 3\",\n                  \"precioScl\": \".58733531\",\n                  \"cantidad\": \"1\",\n                  \"seriado\": \"1\",\n                  \"tipoInv\": \"INV_TELCA\",\n                  \"tecnologia\": \"3G\",\n                  \"detalleArticulo\": [\n                    {\n                      \"idInventario\": \"3\",\n                      \"tipoInv\": \"INV_TELCA\",\n                      \"serie\": \"A2\",\n                      \"serieAsociada\": \"A2ASOC\",\n                      \"estado\": \"Disponible\",\n                      \"numTelefono\": \"32165497\",\n                      \"icc\": \"8950304203609907440\",\n                      \"imei\": \"451815082155740\"\n                      \"creado_el\": \"10/12/2015 16:17:58\",\n                      \"creado_por\": \"usuario.pruebas\"\n                    },\n                    {\n                      \"idInventario\": \"4\",\n                      \"tipoInv\": \"INV_TELCA\",\n                      \"serie\": \"A3\",\n                      \"serieAsociada\": \"A3ASOC\",\n                      \"estado\": \"Disponible\",\n                      \"numTelefono\": \"32165499\",\n                      \"icc\": \"8950304203609907555\",\n                      \"imei\": \"451815082155780\"\n                      \"creado_el\": \"10/12/2015 16:17:58\",\n                      \"creado_por\": \"usuario.pruebas\"\n                    }\n                  ]\n                }\n              ]\n            }\n          ]\n        }\n      ]\n    }\n  ]\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"-205\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"No existen registros con esos par\\u00E1metros.\",\n    \"clase\": \"OperacionInventario\",\n    \"metodo\": \"doGet\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioConsulta.java",
    "groupTitle": "Consulta"
  },
  {
    "type": "POST",
    "url": "/consultasidra/getinventariomovil/",
    "title": "[getInventarioMovil]",
    "name": "getInventarioMovil",
    "description": "<p>Servicio para obtener los datos del inventario de bodegas virtuales.</p>",
    "group": "Consulta",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pais.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "token",
            "description": "<p>Token de autenticaci\\u00F3n de sesion de usuario.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codDispositivo",
            "description": "<p>c\\u00F3digo del dispositivo desde donde se realiza la operaci\\u00F3n, en caso de utilizarse la APP M\\u00F3VIL.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idBodega",
            "description": "<p>Identificador de la Bodega Virtual que se desea consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idArticulo",
            "description": "<p>Identificador del art\\u00EDculo que se desea consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "serie",
            "description": "<p>Serie del art\\u00EDculo que se desea consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "tipoInv",
            "description": "<p>Tipo de Inventario que se desea consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "tipoGrupo",
            "description": "<p>Grupo que se desea consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "seriado",
            "description": "<p>Tipo de Art\\u00EDculo que se desea, 1 seriados o 0 no seriados.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idVendedor",
            "description": "<p>Identificador del vendedor que se desea consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "estado",
            "description": "<p>Estado del art\\u00EDculo que se desea consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "tecnologia",
            "description": "<p>Tipo de tecnolog\\u00EDa del art\\u00EDculo.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "version",
            "description": "<p>Version de los precios que se desean consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "mostrarRecarga",
            "description": "<p>Campo que sirve para indicar si se debe mostrar el art\\u00EDculo de recarga o no (1 = S\\u00ED, 0 = No).</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"token\": \"WEB\",\n    \"codArea\": \"505\",\n    \"usuario\": \"usuario.pruebas\",\n    \"codDispositivo\": \"64E1CE4A48BF656974C376B7905F7E62\",\n    \"idBodega\": \"114\",\n    \"idArticulo\": \"\",\n    \"serie\": \"\",\n    \"tipoInv\": \"\",\n    \"tipoGrupo\": \"\",\n    \"seriado\": \"\",\n    \"idVendedor\": \"\",\n    \"estado\": \"\",\n    \"tecnologia\": \"\",\n    \"idTipo\": \"82\",\n    \"tipo\": \"PANEL\",\n    \"version\": \"20160414111754\",\n    \"mostrarRecarga\": \"\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"0\",\n    \"mostrar\": \"0\",\n    \"descripcion\": \"Recursos recuperados exitosamente.\"\n  },\n  \"inventario\": [\n    {\n      \"bodegas\": [\n        {\n          \"idBodega\": \"73\",\n          \"nombreBodega\": \"BODEGA PRUEBA\",\n          \"grupos\": [\n            {\n              \"grupo\": \"TERMINAL\",\n              \"articulos\": [\n                {\n                   \"idArticulo\": \"12147\",\n                   \"descripcion\": \"3G|GSM-BASE MOTOROLA FXC 851\",\n                   \"cantidad\": \"1\",\n                   \"seriado\": \"1\",\n                   \"tipoInv\": \"INV_TELCA\",\n                   \"tecnologia\": \"3G\",\n                   \n                   \"detallePrecios\": [\n                      {\n                         \"idArticulo\": \"20227\",\n                         \"precioSCL\": \"39.13\",\n                         \"descuentoSCL\": \"0\",\n                         \"tipoGestion\": \"VENTA\",\n                         \"version\": \"20160414111754\"\n                      },\n                      {\n                         \"idArticulo\": \"20227\",\n                         \"precioSCL\": \"156.73999944\",\n                         \"descuentoSCL\": \"0\",\n                         \"tipoGestion\": \"ALTA_PREPAGO\",\n                         \"version\": \"20160414111754\"\n                      }\n                   ],\n                   \n                   \"descuentos\": [\n                       {\n                          \"tipo\": \"ARTICULO\",\n                          \"tecnologia\": \"\",\n                          \"tipoGestion\": \"VENTA\",\n                          \"tipoCliente\": \"CF\",\n                          \"tipoDescuento\": \"MONTO\",\n                          \"valorDescuento\": \"10\",\n                          \"idOfertaCampania\": \"33\",\n                          \"nombreCampania\": \"Descuento a Terminales 3G\"\n                       },\n                       {\n                          \"tipo\": \"ARTICULO\",\n                          \"tecnologia\": \"\",\n                          \"tipoGestion\": \"ALTA_PREPAGO\",\n                          \"tipoCliente\": \"PDV\",\n                          \"tipoDescuento\": \"PORCENTAJE\",\n                          \"valorDescuento\": \"10\",\n                          \"idOfertaCampania\": \"33\",\n                          \"nombreCampania\": \"Descuento a Terminales 3G\"\n                       }\n                   ],\n                   \n                   \"detalleArticulo\": [\n                     {\n                       \"idInventario\": \"3789127\",\n                       \"idAsignacionReserva\": \"24\",\n                       \"serie\": \"014027001710521\",\n                       \"numTelefono\": \"32165497\",\n                       \"icc\": \"8950304203609907440\",\n                       \"imei\": \"451815082155740\"\n                       \"estadoComercial\": \"L\",\n                       \"tipoInv\": \"INV_TELCA\",\n                       \"estado\": \"Disponible\",\n                       \"creado_el\": \"03/03/2016 12:26:32\",\n                       \"creado_por\": \"P_SINCR_SIDRA\"\n                     },\n                     {\n                       \"idInventario\": \"227618\",\n                       \"idAsignacionReserva\": \"24\",\n                       \"serie\": \"355986036045244\",\n                       \"numTelefono\": \"32165498\",\n                       \"icc\": \"8950304203609907960\",\n                       \"imei\": \"451815082155810\"\n                       \"estadoComercial\": \"L\",\n                       \"tipoInv\": \"INV_TELCA\",\n                       \"estado\": \"Disponible\",\n                       \"creado_el\": \"03/03/2016 12:26:32\",\n                       \"creado_por\": \"P_SINCR_SIDRA\"\n                     }\n                    ]\n                  ]\n                }\n              ]\n            }\n          ]\n        }\n      ]\n    }\n  ]\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"-205\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"No existen registros con esos par\\u00E1metros.\",\n    \"clase\": \"OperacionInventarioMovil\",\n    \"metodo\": \"doGet\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioConsulta.java",
    "groupTitle": "Consulta"
  },
  {
    "type": "POST",
    "url": "/consultasidra/getseries/",
    "title": "[getSeries]",
    "name": "getSeries",
    "description": "<p>Servicio para obtener los datos del inventario de bodegas virtuales.</p>",
    "group": "Consulta",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "getSeries": [
          {
            "group": "getSeries",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pais.</p>"
          },
          {
            "group": "getSeries",
            "type": "String",
            "optional": false,
            "field": "token",
            "description": "<p>Token de autenticaci\\u00F3n de sesion de usuario.</p>"
          },
          {
            "group": "getSeries",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operacion.</p>"
          },
          {
            "group": "getSeries",
            "type": "String",
            "optional": false,
            "field": "codDispositivo",
            "description": "<p>c\\u00F3digo del dispositivo desde donde se realiza la operaci\\u00F3n, en caso de utilizarse la APP M\\u00F3VIL.</p>"
          },
          {
            "group": "getSeries",
            "type": "String",
            "optional": false,
            "field": "idBodega",
            "description": "<p>Identificador de la Bodega Virtual que se desea consultar.</p>"
          },
          {
            "group": "getSeries",
            "type": "String",
            "optional": false,
            "field": "idArticulo",
            "description": "<p>Identificador del art\\u00EDculo que se desea consultar.</p>"
          },
          {
            "group": "getSeries",
            "type": "String",
            "optional": false,
            "field": "tipoInv",
            "description": "<p>Tipo de Inventario que se desea consultar, puede ser INV_TELCA o INV_SIDRA.</p>"
          },
          {
            "group": "getSeries",
            "type": "String",
            "optional": false,
            "field": "estado",
            "description": "<p>Estado del art\\u00EDculo que se desea consultar.</p>"
          },
          {
            "group": "getSeries",
            "type": "String",
            "optional": false,
            "field": "min",
            "description": "<p>Registro m\\u00EDnimo a consultar para pagineo.</p>"
          },
          {
            "group": "getSeries",
            "type": "String",
            "optional": false,
            "field": "max",
            "description": "<p>Registro m\\u00E1ximo a consultar para pagineo.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"token\": \"WEB\",\n    \"codArea\": \"505\",\n    \"usuario\": \"usuario.pruebas\",\n    \"codDispositivo\": \"64E1CE4A48BF656974C376B7905F7E62\",\n    \"idBodega\": \"1\",\n    \"idArticulo\": \"1\",\n    \"tipoInv\": \"INV_SIDRA\",\n    \"estado\": \"\",\n    \"min\": \"\",\n    \"max\": \"\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"202\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"Recursos recuperados exitosamente.\",\n    \"clase\": \"OperacionConsultaSeries\",\n    \"metodo\": \"doGet\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"\"\n  },\n  \"idArticulo\": \"48\",\n  \"descripcion\": \"ARTICULO DE PRUEBA QA #1\",\n  \"tipoInv\": \"INV_SIDRA\",\n  \"precioScl\": \"40.00\",\n  \"series\": [\n    {\n      \"idInventario\": \"214526\",\n      \"serie\": \"A1\",\n      \"serieAsociada\": \"A2\"\n    },\n    {\n      \"idInventario\": \"214527\",\n      \"serie\": \"A3\",\n      \"serieAsociada\": \"A4\"\n    }\n  ]\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"-199\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"Los siguientes datos faltan o son incorrectos:  Datos no num\\u00E9ricos en ID de Art\\u00EDculo.\",\n    \"clase\": \"CtrlConsultaSeries\",\n    \"metodo\": \"validarInput\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioConsulta.java",
    "groupTitle": "Consulta"
  },
  {
    "type": "POST",
    "url": "/consultasidra/gethistorico/",
    "title": "[gethistorico]",
    "name": "gethistorico",
    "description": "<p>Servicio para obtener informaci\\u00F3n de transacciones realizadas en el inventario de SIDRA.</p>",
    "group": "Consulta",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de area del pais del cual se desea buscar informaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre del usuario que desea obtener la informaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idTraslado",
            "description": "<p>Id de traslado si se desea obtener informacion de traslados. CAMPO NO OBLIGATORIO.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "tipoMovimiento",
            "description": "<p>Tipo de movimiento de inventario que se desea buscar. CAMPO NO OBLIGATORIO.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "bodegaOrigen",
            "description": "<p>id de la bodega de donde proceden las transacciones.CAMPO NO OBLIGATORIO.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "bodegaDestino",
            "description": "<p>id de la bodega hacia donde van las transacciones.CAMPO NO OBLIGATORIO.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "articulo",
            "description": "<p>id del art\\u00EDculo del que se desea buscar la transaccion.CAMPO NO OBLIGATORIO.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "descripcion",
            "description": "<p>descripcion del articulo del que se desea buscar el historico.CAMPO NO OBLIGATORIO.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "serie",
            "description": "<p>serie de art\\u00EDculo del que se desea buscar historico de transacciones.CAMPO NO OBLIGATORIO.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "serieFinal",
            "description": "<p>Serie final del rango que se desea buscar del hist\\u00F3rico de transacciones. CAMPO NO OBLIGATORIO.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "tipoInv",
            "description": "<p>tipo de inventario del que se desea buscar historico de transacciones (INV_SIDRA/INV_TELCA).CAMPO NO OBLIGATORIO.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "fechaInicio",
            "description": "<p>rango inicial de fechas de donde se desea iniciar a buscar el historico de transacciones.Formato YYYYMMDD. CAMPO NO OBLIGATORIO.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "fechaFin",
            "description": "<p>rango final de fechas de donde se desea finalizar la busqueda del historico de transacciones.Formato YYYYMMDD.CAMPO NO OBLIGATORIO.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"codArea\": \"505\",\n    \"usuario\":\"usuarioprueba\",\n    \"idTraslado\":\"\",\n    \"tipoMovimiento\":\"\",\n    \"bodegaOrigen\":\"\",\n    \"bodegaDestino\":\"\",\n    \"articulo\":\"\",\n    \"descripcion\":\"\",\n    \"serie\":\"\",\n    \"serieFinal\":\"\",\n    \"tipoInv\":\"\",\n    \"fechaInicio\":\"20151220\",\n    \"fechaFin\":\"20151225\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": " {\n    \"respuesta\": {\n        \"codResultado\": \"12\",\n        \"mostrar\": \"1\",\n        \"descripcion\": \"OK. Datos obtenidos exitosamente\",\n        \"clase\": \" \",\n        \"metodo\": \" \",\n        \"excepcion\": \" \",\n        \"tipoExepcion\": \"\"\n    },\n    \"historico\": [\n        {\n            \"tcscloginvsidraid\": \"26\",\n              \"idTraslado\": \"17\",\n            \"codigoTransaccion\": \"T1\",\n            \"tipoMovimiento\": \"Transaccion entre Almacenes\",\n            \"bodegaOrigen\": \"73\",\n            \"nombreBodegaOrigen\": \"BODEGA PRUEBA MIERCOLES\",\n            \"bodegaDestino\": \"72\",\n            \"nombreBodegaDestino\": \"BODEGA VIRTUAL2\",             \n            \"articulo\": \"7\",\n            \"descripcion\": \"SIMCARD\",\n            \"precio\": \"4.00\",\n            \"serie\": \"A1\",\n            \"serieFinal\": \" \",\n            \"cantidad\": \"1\",\n            \"serieAsociada\": \" \",\n            \"codNum\": \" \",\n            \"estado\": \"ALTA\",\n            \"tipoInv\": \" \",\n            \"creado_por\": \"usuarioprueba\",\n            \"creado_el\": \"23/12/2015 12:43:51\"\n        },\n        {\n            \"tcscloginvsidraid\": \"27\",\n            \"idTraslado\": \"17\",\n            \"codigoTransaccion\": \"T1\",\n            \"tipoMovimiento\": \"Transaccion entre Almacenes\",\n            \"bodegaOrigen\": \"73\",\n            \"nombreBodegaOrigen\": \"BODEGA PRUEBA MIERCOLES\",\n            \"bodegaDestino\": \"72\",\n            \"nombreBodegaDestino\": \"BODEGA VIRTUAL2\",\n            \"articulo\": \"9\",\n            \"descripcion\": \"SIMCARD\",\n            \"precio\": \"4.00\",\n            \"serie\": \" \",\n            \"serieFinal\": \" \",\n            \"cantidad\": \"5\",\n            \"serieAsociada\": \" \",\n            \"codNum\": \" \",\n            \"estado\": \"ALTA\",\n            \"tipoInv\": \" \",\n            \"creado_por\": \"usuarioprueba\",\n            \"creado_el\": \"23/12/2015 12:43:51\"\n        }\n    ]\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"-42\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"No se encontraron datos.\",\n    \"clase\": \"\",\n    \"metodo\": \"getHistorico\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioConsulta.java",
    "groupTitle": "Consulta"
  },
  {
    "type": "POST",
    "url": "/consultasidra/getsaldopayment/",
    "title": "[getsaldopayment]",
    "name": "getsaldopayment",
    "description": "<p>Servicio para consultar el saldo payment de un numero de recarga.</p>",
    "group": "Consulta",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs en el que se desea realizar la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "token",
            "description": "<p>Token de validaci\\u00F3n de autenticaci\\u00F3n de usuario.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codDispositivo",
            "description": "<p>c\\u00F3digo del dispositivo desde donde se realiza la operaci\\u00F3n, en caso de utilizarse la APP M\\u00F3VIL.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idVendedor",
            "description": "<p>Identificador del vendedor a consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "numRecarga",
            "description": "<p>n\\u00FAmero de telefono a consultar.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"token\": \"WEB\",\n    \"codDispositivo\": \"CF3F305997BF98946B3B1DF77862936E1620E01E\",\n    \"codArea\": \"505\",\n    \"usuario\": \"usuario.pruebas\",\n    \"idVendedor\": \"904\",\n    \"numRecarga\": \"40258541\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n   \"token\": \"WEB\",\n   \"respuesta\": {\n       \"codResultado\": \"12\",\n       \"mostrar\": \"0\",\n       \"descripcion\": \"OK. Datos obtenidos exitosamente\",\n       \"clase\": \" \",\n       \"metodo\": \" \",\n       \"excepcion\": \" \",\n       \"tipoExepcion\": \"\"\n   },\n   \"saldoPayment\":\"1000\"\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "{} JSON Respuesta-Error:     ",
          "content": "{\n   \"token\":\"WEB\",\n   \"respuesta\": {\n        \"codResultado\": \"-522\",\n        \"mostrar\": \"1\",\n        \"descripcion\": \"El idVendedor y n\\u00FAmero de recarga no coinciden\",\n        \"clase\": \"CtrlSaldoPayment\",\n        \"metodo\": \"validarDatos\",\n        \"excepcion\": \" \",\n        \"tipoExepcion\": \"Generales\"\n    }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioConsulta.java",
    "groupTitle": "Consulta"
  },
  {
    "type": "POST",
    "url": "/consultasidra/getvendxdts/",
    "title": "[getvendxdts]",
    "name": "getvendxdts",
    "description": "<p>Servicio para obtener informaci\\u00F3n de vendedores por distribuidor.</p>",
    "group": "Consulta",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs de donde se desea obtener la informaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre del usuario que desea obtener la informaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idDTS",
            "description": "<p>Id del distribuidor del cual se desea consultar los vendedores asociados.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idVendedor",
            "description": "<p>Id del vendedor que se desea consultar.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"codArea\": \"505\",\n    \"usuario\":\"usuarioprueba\",\n    \"idDTS\": \"\",\n    \"idVendedor\": \"\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n   \"respuesta\": {\n       \"codResultado\": \"12\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"OK. Datos obtenidos exitosamente\",\n       \"clase\": \" \",\n       \"metodo\": \" \",\n       \"excepcion\": \" \",\n       \"tipoExepcion\": \"\"\n   },\n   \"vendedores\": [\n      {\n          \"idDTS\": \"41\",\n          \"nombreDistribuidor\":\"DTS1\",\n          \"idBodegaVirtual\": \"50003\",\n          \"idBodegaVendedor\": \"137\",\n          \"idVendedor\": \"123\",\n          \"nombreUsuario\": \"Nombre vendedor\",\n          \"tipo\": \"PANEL\"\n      },\n      {\n          \"idDTS\": \"41\",\n          \"nombreDistribuidor\":\"DTS1\",\n          \"idBodegaVirtual\": \"50003\",\n          \"idBodegaVendedor\": \"135\",\n          \"idVendedor\": \"1034\",\n          \"nombreUsuario\": \"Nombre vendedor\",\n          \"tipo\": \"PANEL\"\n      },\n      {\n          \"idDTS\": \"41\",\n          \"nombreDistribuidor\":\"DTS1\",\n          \"idBodegaVirtual\": \"50003\",\n          \"idBodegaVendedor\": \"130\",\n          \"idVendedor\": \"1522\",\n          \"nombreUsuario\": \"aviasesorsv\",\n          \"tipo\": \"PANEL\"\n      }\n   ]\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"-42\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"No se encontraron datos.\",\n    \"clase\": \"\",\n    \"metodo\": \"getVendedorxDTS\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioConsulta.java",
    "groupTitle": "Consulta"
  },
  {
    "type": "POST",
    "url": "/consultasidra/validaseries/",
    "title": "[validaseries]",
    "name": "validaseries",
    "description": "<p>Servicio para verificar que las series que ser\\u00E1n vendidas se encuentran en estado Disponible.</p>",
    "group": "Consulta",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>c\\u00F3digo del pa\\u00EDs al que se desea consultar la informaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "token",
            "description": "<p>token de validaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codDispositivo",
            "description": "<p>codigo del dispositivo desde el cual se realiza la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idBodegaVendedor",
            "description": "<p>id de la bodega del vendedor que se desea consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idArticulo",
            "description": "<p>id del art\\u00EDculo a verificar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "serieInicial",
            "description": "<p>serie a verificar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "serieFinal",
            "description": "<p>serie final a verificar, en caso de verificar un rango.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n        \"token\": \"WEB\",\n        \"codArea\": \"505\",\n        \"usuario\": \"oscar.sanchez\",\n        \"codDispositivo\": \"123123123123\",\n        \"idBodegaVendedor\":\"\",\n\n        \"listadoSeries\": [{\n                \"idArticulo\":\"90\",\n                \"serieInicial\": \"123123123\",\n                \"serieFinal\": \"\"\n\n        }, {\n                \"idArticulo\":\"1090\",    \n                \"serieInicial\": \"111111\",\n                \"serieFinal\": \"111112\"\n        }]\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n   \"token\":\"WEB\"\n   \"respuesta\": {\n       \"codResultado\": \"20\",\n       \"mostrar\": \"0\",\n       \"descripcion\": \"OK. Series Correctas.\",\n       \"clase\": \" \",\n       \"metodo\": \" \",\n       \"excepcion\": \" \",\n       \"tipoExepcion\": \"\"\n   }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "{} JSON Respuesta-Error:     ",
          "content": "\t{\n\t \"token\":\"WEB\",\n   \"respuesta\": {\n       \"codResultado\": \"-473\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"Las siguientes series o art\\u00EDculos no se encuentran disponibles en inventario. \",\n       \"clase\": \"CtrlValidaArticulos\",\n       \"metodo\": \"validaSeries\",\n       \"excepcion\": \" \",\n       \"tipoExepcion\": \"Generales\"\n   },\n   \"articulos\": {\n       \"idArticulo\": \"1011\",\n       \"tipoInventario\": \"INV_TELCA\",\n       \"descripcionArticulo\": \"TARJETA PREPAGADA $1\",\n       \"serieInicial\": \"123238020001001\",\n       \"serieFinal\": \"123238020001020\",\n       \"grupo\": \"TARJETASRASCA\",\n       \"estado\": \"NO EXISTE\"\n   }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioConsulta.java",
    "groupTitle": "Consulta"
  },
  {
    "type": "POST",
    "url": "/opsidra/crearCuenta/",
    "title": "[crearCuenta]",
    "name": "crearCuenta",
    "description": "<p>Servicio para crear cuentas bancarias por pa\\u00EDs.</p>",
    "group": "Cuenta",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs en el que se desea realizar la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idDts",
            "description": "<p>Id del distribuidor al que se le crea la cuenta.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "banco",
            "description": "<p>Nombre del banco a asociar con la cuenta a crear.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "noCuenta",
            "description": "<p>N\\u00FAmero de la cuenta bancaria a crear.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "tipoCuenta",
            "description": "<p>Tipo de la cuenta crear.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "nombreCuenta",
            "description": "<p>Nombre de la cuenta a crear.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"codArea\":\"505\",\n    \"usuario\":\"usuario.pruebas\",\n    \"idDts\":\"1\",\n    \"banco\":\"Banco General\",\n    \"noCuenta\":\"46794697154\",\n    \"tipoCuenta\":\"Monetaria\",\n    \"nombreCuenta\":\"Cuenta de Ahorro BdO\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n  \"idCuenta\": \"7\",\n  \"respuesta\": {\n    \"codResultado\": \"200\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"Campos agregados exitosamente.\",\n    \"clase\": \"OperacionCuenta\",\n    \"metodo\": \"doPost\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"-701\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"El par\\u00E1metro de entrada \\\"banco\\\" esta vac\\u00EDo.\",\n    \"clase\": \"CtrlCuenta\",\n    \"metodo\": \"validarInput\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioOperacion.java",
    "groupTitle": "Cuenta"
  },
  {
    "type": "POST",
    "url": "/consultasidra/getcuenta/",
    "title": "[getCuenta]",
    "name": "getCuenta",
    "description": "<p>Servicio para consultar los datos de cuentas bancarias creadas en Sidra.</p>",
    "group": "Cuenta",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pais.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "token",
            "description": "<p>Token de autenticaci\\u00F3n de sesion de usuario.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codDispositivo",
            "description": "<p>c\\u00F3digo del dispositivo desde donde se realiza la operaci\\u00F3n, en caso de utilizarse la APP M\\u00F3VIL.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idDts",
            "description": "<p>Id del distribuidor al que pertenencen las cuentas a consultar .</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idCuenta",
            "description": "<p>Identificador de la cuenta a consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "banco",
            "description": "<p>Nombre del banco asociado a la cuenta a consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "noCuenta",
            "description": "<p>N\\u00FAmero de la cuenta bancaria a consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "tipoCuenta",
            "description": "<p>Tipo de la cuenta consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "nombreCuenta",
            "description": "<p>Nombre de la cuenta a consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "estado",
            "defaultValue": "ALTA o BAJA",
            "description": "<p>Estado de la cuenta a consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "bancosAsociados",
            "defaultValue": "1 o 0",
            "description": "<p>Par\\u00E1metro para indicar si se desean consultar los bancos con alguna cuenta asociada o las cuentas.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"token\":\"WEB\",\n    \"codArea\":\"505\",\n    \"usuario\":\"usuario.pruebas\",\n    \"codDispositivo\": \"64E1CE4A48BF656974C376B7905F7E62\",\n    \"idDts\"\"\",\n    \"idCuenta\":\"\",\n    \"banco\":\"\",\n    \"noCuenta\":\"\",\n    \"tipoCuenta\":\"\",\n    \"nombreCuenta\":\"\",\n    \"estado\":\"\",\n    \"bancosAsociados\":\"1\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n  \"token\":\"WEB\",\n  \"respuesta\": {\n    \"codResultado\": \"0\",\n    \"mostrar\": \"0\",\n    \"descripcion\": \"Recursos recuperados exitosamente.\"\n  },\n\t \"distribuidores\": {\n\t     \"idDts\": \"12\",\n\t     \"nombre\": \"DTS_hvrm\",\n\t     \"cuentas\": \n          [\n\t            {\n\t                \"idCuenta\": \"1\",\n\t                \"banco\": \"Banco Industrial De El Salvdor\",\n\t                \"noCuenta\": \"1234567890\",\n\t                \"tipoCuenta\": \"Ahorro\",\n\t                \"nombreCuenta\": \"Cuenta_hvrm\",\n\t                \"estado\": \"ALTA\",\n\t                \"creado_el\": \"01/11/2016 10:21:21\",\n\t                \"creado_por\": \"vladimir.rodriguez\",\n\t                \"modificado_el\": \"\"\n\t            },\n\t            {\n\t                \"idCuenta\": \"2\",\n\t                \"banco\": \"Washington Mutual\",\n\t                \"noCuenta\": \"4105545054564\",\n\t                \"tipoCuenta\": \"Monetaria\",\n\t                \"nombreCuenta\": \"Cuenta de Ahorro BdO\",\n\t                \"estado\": \"ALTA\",\n\t                \"creado_el\": \"04/11/2016 11:23:51\",\n\t                \"creado_por\": \"usuario.pruebas\",\n\t                \"modificado_el\": \"\"\n\t            }\n\t        ]\n\t    },\n  \"bancos\": [\n    {\n      \"banco\": \"Banco de Occidente\"\n    },\n    {\n      \"banco\": \"Banco General\"\n    }\n  ]\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"-205\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"No existen registros con esos par\\u00E1metros.\",\n    \"clase\": \"OperacionCuenta\",\n    \"metodo\": \"doGet\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioConsulta.java",
    "groupTitle": "Cuenta"
  },
  {
    "type": "POST",
    "url": "/opsidra/modCuenta/",
    "title": "[modCuenta]",
    "name": "modCuenta",
    "description": "<p>Servicio para modificar cuentas bancarias creadas en Sidra.</p>",
    "group": "Cuenta",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs en el que se desea realizar la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idCuenta",
            "description": "<p>Identificador de la cuenta a modificar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "banco",
            "description": "<p>Nombre del banco asociado a la cuenta a modificar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "noCuenta",
            "description": "<p>N\\u00FAmero de la cuenta bancaria a modificar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "tipoCuenta",
            "description": "<p>Tipo de la cuenta modificar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "nombreCuenta",
            "description": "<p>Nombre de la cuenta a modificar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "estado",
            "defaultValue": "ALTA o BAJA",
            "description": "<p>Estado de la cuenta a modificar.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"codArea\":\"505\",\n    \"usuario\":\"usuario.pruebas\",\n    \"idCuenta\":\"4\",\n    \"banco\":\"Banco General\",\n    \"noCuenta\":\"46794697164\",\n    \"tipoCuenta\":\"Monetaria\",\n    \"nombreCuenta\":\"Cuenta corriente\",\n    \"estado\":\"ALTA\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"201\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"Recurso modificado exitosamente.\",\n    \"clase\": \"OperacionCuenta\",\n    \"metodo\": \"doPut\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"-705\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"El tipo de cuenta enviada no corresponde a ninguna de las registradas en el sistema.\",\n    \"clase\": \"CtrlCuenta\",\n    \"metodo\": \"validarInput\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioOperacion.java",
    "groupTitle": "Cuenta"
  },
  {
    "type": "POST",
    "url": "/consultasidra/getresumendeuda/",
    "title": "[getresumendeuda]",
    "name": "getresumendeuda",
    "description": "<p>Servicio para obtener los datos de usuarios asignados a buzones por pais.</p>",
    "group": "Deuda",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "getresumendeuda": [
          {
            "group": "getresumendeuda",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pais.</p>"
          },
          {
            "group": "getresumendeuda",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "getresumendeuda",
            "type": "String",
            "optional": false,
            "field": "idDts",
            "description": "<p>Identificador del distribuidor a consultar.</p>"
          },
          {
            "group": "getresumendeuda",
            "type": "String",
            "optional": false,
            "field": "idBodega",
            "description": "<p>Identificador de la bodega a consultar.</p>"
          }
        ],
        "Parameter": [
          {
            "group": "Parameter",
            "type": "getresumendeuda",
            "optional": true,
            "field": "fechaInicio",
            "description": "<p>Fecha en formato yyyyMMdd desde la cu\\u00E1l se desea consultar la solicitud.</p>"
          },
          {
            "group": "Parameter",
            "type": "getresumendeuda",
            "optional": true,
            "field": "fechaFin",
            "description": "<p>Fecha en formato yyyyMMdd hasta la cu\\u00E1l se desea consultar la solicitud.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo parametros de Entrada:",
          "content": "{\n    \"codArea\": \"505\",\n    \"usuario\": \"usuario.pruebas\",\n    \"idDts\": \"\",\n    \"idBodega\": \"\",\n    \"fechaInicio\": \"\",\n    \"fechaFin\": \"\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "{} JSON Respuesta-\\u00E9xito:",
          "content": "{\n\t  \"respuesta\": {\n\t    \"codResultado\": \"12\",\n\t    \"mostrar\": \"0\",\n\t    \"descripcion\": \"Recursos recuperados exitosamente.\"\n\t  },\n\t\n\t    \"listaSolicitudes\": [\n\t\t{\"fecha\": \"02/08/2016 00:00:00\",\n\t       \"idSolicitud\": \"91\",\n\t       \"idBodega\": \"114\",\n\t       \"idDTS\": \"22\",\n\t       \"nombreDTS\": \"DTS TECNOLOGIA S.A\",\n\t       \"idBuzon\": \"11\",\n\t\t   \"nombreBuzon\":\"\",\n\t       \"idBuzonAnterior\": \"\",\n\t\t   \"nombreBuzonAnterior\":\"\",   \n\t       \"tipoSolicitud\": \"DEUDA\",\n\t       \"observaciones\": \"Observaciones payment\",\n\t       \"origen\": \"PC\",\n\t       \"totalDeuda\": \"\",\n\t       \"tasaCambio\": \"\",\n\t       \"estado\": \"CERRADA\",\n\t\t   \"origenCancelacion\":\"\",\n\t\t   \"obsCancelacion\":\"\",\n\t       \"creado_el\": \"03/08/2016 10:00:11\",\n\t       \"creado_por\": \"victor.cifuentes\",\n\t       \"modificado_el\": \"04/08/2016 11:53:26\",\n\t       \"modificado_por\": \"sergio.lujan\"\n\t\t   },\n\t\t   {\n\t\t    \"fecha\": \"03/09/2016 00:00:00\",\n\t       \"idSolicitud\": \"91\",\n\t       \"idBodega\": \"114\",\n\t\t\t\"nombreBodega\": \"PROMIX 1.1\",\n\t       \"idDTS\": \"22\",\n\t       \"nombreDTS\": \"DTS TECNOLOGIA S.A\",\n\t       \"idBuzon\": \"11\",\n\t\t   \"nombreBuzon\":\"\",\n\t       \"idBuzonAnterior\": \"\",\n\t \t   \"nombreBuzonAnterior\":\"\",\n\t       \"tipoSolicitud\": \"DEUDA\",\n\t       \"observaciones\": \"Observaciones payment\",\n\t       \"origen\": \"PC\",\n\t       \"totalDeuda\": \"\",\n\t       \"tasaCambio\": \"\",\n\t       \"estado\": \"CERRADA\",\n\t\t   \"origenCancelacion\": \"\",\n   \t   \"obsCancelacion\": \"\",\n\t       \"creado_el\": \"03/08/2016 10:00:11\",\n\t       \"creado_por\": \"victor.cifuentes\",\n\t       \"modificado_el\": \"04/08/2016 11:53:26\",\n\t       \"modificado_por\": \"sergio.lujan\"\n\t\t   },\n\t\t   {\n    \"fecha\": \"04/09/2016 00:00:00\",\n    \"idSolicitud\": \"\",\n    \"idBodega\": \"\",\n    \"nombreBodega\": \"\",\n    \"idDTS\": \"\",\n    \"nombreDTS\": \"\",\n    \"idBuzon\": \"\",\n    \"nombreBuzon\": \"\",\n    \"idBuzonAnterior\": \"\",\n    \"nombreBuzonAnterior\": \"\",\n    \"tipoSolicitud\": \"\",\n    \"observaciones\": \"\",\n    \"origen\": \"\",\n    \"totalDeuda\": \"\",\n    \"tasaCambio\": \"\",\n    \"estado\": \"\",\n    \"origenCancelacion\": \"\",\n    \"obsCancelacion\": \"\",\n    \"creado_el\": \"\",\n    \"creado_por\": \"\",\n    \"modificado_el\": \"\",\n    \"modificado_por\": \"\"\n  \t}\n\t\t  ]\n\t\t  }",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "{} JSON Respuesta-Error:     ",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"-205\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"No existen registros con esos par\\u00E1metros.\",\n    \"clase\": \"OperacionDeuda\",\n    \"metodo\": \"doGet\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioConsulta.java",
    "groupTitle": "Deuda"
  },
  {
    "type": "POST",
    "url": "/consultasidra/gettransaccionesdeuda/",
    "title": "[gettransaccionesdeuda]",
    "name": "gettransaccionesdeuda",
    "description": "<p>Servicio para obtener los datos de las transacciones de pago realizadas en las jornadas de una deuda.</p>",
    "group": "Deuda",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pais.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idJornada",
            "description": "<p>Identificador de la jornada a consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idDTS",
            "description": "<p>Identificador del distribuidor a consultar.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo parametros de Entrada:",
          "content": "{\n    \"codArea\": \"505\",\n    \"usuario\": \"victor.cifuentes\",\n    \"idJornada\": \"198\",\n    \"idDTS\": \"16\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "{} JSON Respuesta-\\u00E9xito:",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"12\",\n    \"mostrar\": \"0\",\n    \"descripcion\": \"OK. Datos obtenidos exitosamente\",\n    \"clase\": \"OperacionTransaccionesDeuda\",\n    \"metodo\": \"doGet\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"\"\n  },\n  \"remesas\": [\n    {\n      \"idRemesa\": \"326\",\n      \"origen\": \"MOVIL\",\n      \"idVendedor\": \"242\",\n      \"nombreVendedor\": \"cesar.casia\",\n      \"idTipo\": \"10\",\n      \"tipo\": \"RUTA\",\n      \"nombreTipo\": \"Managua\",\n      \"idJornada\": \"198\",\n      \"monto\": \"10.10\",\n      \"tasaCambio\": \"29.8906\",\n      \"noBoleta\": \"4477445665\",\n      \"banco\": \"BAC\",\n      \"idCuenta\": \"10\",\n      \"noCuenta\": \"64654-654-36-2\",\n      \"tipoCuenta\": \"Cuenta Corriente\",\n      \"nombreCuenta\": \"Distribuidor Alfredo Alaniz\",\n      \"estado\": \"ALTA\",\n      \"creado_el\": \"23/05/2017 17:56:54\",\n      \"creado_por\": \"cesar.casia\",\n      \"modificado_el\": \"\",\n      \"modificado_por\": \"\"\n    },\n    {\n      \"idRemesa\": \"325\",\n      \"origen\": \"MOVIL\",\n      \"idVendedor\": \"242\",\n      \"nombreVendedor\": \"cesar.casia\",\n      \"idTipo\": \"10\",\n      \"tipo\": \"RUTA\",\n      \"nombreTipo\": \"Managua\",\n      \"idJornada\": \"198\",\n      \"monto\": \"10.10\",\n      \"tasaCambio\": \"29.8906\",\n      \"noBoleta\": \"4587\",\n      \"banco\": \"BAC\",\n      \"idCuenta\": \"7\",\n      \"noCuenta\": \"3215481-1\",\n      \"tipoCuenta\": \"Cuenta Corriente\",\n      \"nombreCuenta\": \"Monetaria de remesas\",\n      \"estado\": \"ALTA\",\n      \"creado_el\": \"23/05/2017 17:49:58\",\n      \"creado_por\": \"cesar.casia\",\n      \"modificado_el\": \"\",\n      \"modificado_por\": \"\"\n    }\n  ],\n  \"transaccionesTarjeta\": [\n    {\n      \"banco\": \"Banco De Finanzas Nicaragua\",\n      \"monto\": \"99.4935102885\",\n      \"numAutorizacion\": \"52345234\",\n      \"marcaTarjeta\": \"MASTERCARD\",\n      \"digitosTarjeta\": \"2345\",\n      \"idVendedor\": \"387\",\n      \"nombreVendedor\": \"Hugo Siles\",\n      \"idTipo\": \"47\",\n      \"tipo\": \"PANEL\",\n      \"nombreTipo\": \"El Porvenir\",\n      \"estado\": \"ALTA\",\n      \"creado_el\": \"23/05/2017 17:56:54\",\n      \"creado_por\": \"cesar.casia\",\n      \"modificado_el\": \"\",\n      \"modificado_por\": \"\"\n    }\n  ],\n  \"transaccionesCheque\": [\n    {\n      \"banco\": \"Banco Centro\",\n      \"monto\": \"149.270138907\",\n      \"numeroCheque\": \"123423\",\n      \"fechaEmision\": \"03/05/2017 00:00:00\",\n      \"numeroReserva\": \"12343\",\n      \"cuentaCliente\": \"123412344\",\n      \"idVendedor\": \"387\",\n      \"nombreVendedor\": \"Hugo Siles\",\n      \"idTipo\": \"47\",\n      \"tipo\": \"PANEL\",\n      \"nombreTipo\": \"El Porvenir\",\n      \"estado\": \"ALTA\",\n      \"creado_el\": \"23/05/2017 17:56:54\",\n      \"creado_por\": \"cesar.casia\",\n      \"modificado_el\": \"\",\n      \"modificado_por\": \"\"\n    }\n  ]\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "{} JSON Respuesta-Error:     ",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"-42\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"No se encontraron datos.\",\n    \"clase\": \"OperacionTransaccionesDeuda\",\n    \"metodo\": \"doGet\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioConsulta.java",
    "groupTitle": "Deuda"
  },
  {
    "type": "POST",
    "url": "/opsidra/creadispositivo/",
    "title": "[creadispositivo]",
    "name": "creadispositivo",
    "description": "<p>Servicio para crear dispositivos que ser\\u00E1n utilizados por vendedores con la aplicacion movil de sidra.</p>",
    "group": "Dispositivos",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>c\\u00F3digo de \\u00E1rea del pa\\u00EDs donde se desea realizar la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operacion.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codigoDispositivo",
            "description": "<p>c\\u00F3digo unico que identifica al dispositivo, el cual es anexado al folio que utiliza el vendedor.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "modelo",
            "description": "<p>modelo del dispositivo que se esta registrando.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "descripcion",
            "description": "<p>descripci\\u00F3n del dispositivo.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "numTelefono",
            "description": "<p>n\\u00FAmero del telefono del dispositivo.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "cajaNumero",
            "description": "<p>N\\u00FAmero de caja del dispositivo.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "zona",
            "description": "<p>Zona del dispositivo.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codOficina",
            "description": "<p>codigo de la oficina.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codVendedor",
            "description": "<p>codigo de vendedor.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idPlaza",
            "description": "<p>id que identifica a la plaza.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idPuntoVenta",
            "description": "<p>id que identifica al Punto de Venta.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "userId",
            "description": "<p>id de user.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "username",
            "description": "<p>nombre de usuario.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "resolucion",
            "description": "<p>N\\u00FAmero de resoluci\\u00F3n del dispositivo.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "fechaResolucion",
            "description": "<p>Fecha de la resoluci\\u00F3n en formato yyyyMMdd.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n   \"codArea\":\"505\",\n   \"usuario\": \"usuario\",\n   \"codigoDispositivo\": \"545645ds\",\n   \"modelo\":\"AA\",\n   \"descripcion\":\"SAMSUNG S4 MINI\",\n   \"numTelefono\":\"54870022\",\n   \"cajaNumero\":\"2\",\n   \"zona\":\"1\",\n   \"codOficina\": \"\",\n   \"codVendedor\": \"\",\n   \"idPlaza\":\"\",\n   \"idPuntoVenta\": \"\",\n   \"userId\": \"\",\n   \"username\": \"\",\n   \"resolucion\":\"54870021\",\n   \"fechaResolucion\":\"20160728\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n    \"idDispositivo\": \"1\",\n    \"respuesta\": {\n        \"codResultado\": \"15\",\n        \"mostrar\": \"1\",\n        \"descripcion\": \"Ok. Creaci\\u00F3n de Dispositivo exitosa \",\n        \"clase\": \" \",\n        \"metodo\": \" \",\n        \"excepcion\": \" \",\n        \"tipoExepcion\": \"\"\n    }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n    \"respuesta\": {\n        \"codResultado\": \"-70\",\n        \"mostrar\": \"1\",\n        \"descripcion\": \"El par\\u00E1metro de entrada \\\\\\\"numTelefono\\\"\\\\ esta vac\\u00EDo.\",\n        \"clase\": \" \",\n        \"metodo\": \"validarDatos\",\n        \"excepcion\": \"class CtrlDispositivo\",\n        \"tipoExepcion\": \"Generales\"\n    }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioOperacion.java",
    "groupTitle": "Dispositivos"
  },
  {
    "type": "POST",
    "url": "/consultasidra/getdispositivo/",
    "title": "[getdispositivo]",
    "name": "getdispositivo",
    "description": "<p>Servicio para obtener informaci\\u00F3n de los dispositivos existentes, al no ingresar ning\\u00FAn valor en el input, se obtendr\\u00E1n todos los dispositivos registrados.</p>",
    "group": "Dispositivos",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de area del pa\\u00EDs del cual se desea obtener informaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre del usuario que solicita la operacion.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idDispositivo",
            "description": "<p>Id del dispositivo que se desea obtener.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "codigoDispositivo",
            "description": "<p>C\\u00F3digo del dispositivo a buscar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "modelo",
            "description": "<p>Modelo de dispositivo a buscar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "descripcion",
            "description": "<p>Descripci\\u00F3n de dispositivo a buscar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "numTelefono",
            "description": "<p>N\\u00FAmero de telefono de dispositivo a buscar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "responsable",
            "description": "<p>Identificador de la panel o ruta asociada al dispositivo a buscar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "tipoResponsable",
            "defaultValue": "RUTA o PANEL",
            "description": "<p>Tipo panel o ruta asociada al dispositivo a buscar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "estado",
            "description": "<p>Estado actual que se desea buscar.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"usuario\":\"usuario.sidra\",\n    \"idDispositivo\":\"\",\n    \"codigoDispositivo\": \"\",\n    \"modelo\":\"\",\n    \"descripcion\":\"\",\n    \"numTelefono\":\"\",\n    \"responsable\":\"\",\n    \"tipoResponsable\":\"\",\n    \"estado\": \"\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n   \"respuesta\": {\n       \"codResultado\": \"12\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"OK. Datos obtenidos exitosamente\",\n       \"clase\": \" \",\n       \"metodo\": \" \",\n       \"excepcion\": \" \",\n       \"tipoExepcion\": \"\"\n   },\n   \"dispositivos\": [\n       {\n           \"idDispositivo\": \"2\",\n           \"codigoDispositivo\": \"545645ds\",\n           \"modelo\": \"AA\",\n           \"descripcion\": \"SAMSUNG S4 MINI\",\n           \"numTelefono\": \"54140022\",\n           \"responsable\":\"10\",\n           \"tipoResponsable\":\"RUTA\",\n           \"nombreResponsable\":\"Ruta Comercial Sur\",\n           \"vendedorAsignado\": \"1020\",\n           \"idDistribuidor\":\"\",\n           \"codOficina\":\"\",\n           \"codVendedor\":\"\",\n           \"estado\": \"ALTA\",\n   \t\t  \"idPlaza\":\"\",\n   \t\t  \"idPuntoVenta\": \"\",\n   \t\t  \"userId\": \"\",\n   \t\t  \"username\": \"\",\n           \"creado_el\": \"30/12/2015 10:48:55\",\n           \"creado_por\": \"usuario\",\n           \"modificado_por\": \"\",\n           \"modificado_el\": \"\"\n       },\n       {\n           \"idDispositivo\": \"3\",\n           \"codigoDispositivo\": \"asdasf\",\n           \"modelo\": \"1\",\n           \"descripcion\": \"SAMSUNG S4 MINI\",\n           \"numTelefono\": \"54140021\",\n           \"responsable\":\"100\",\n           \"tipoResponsable\":\"PANEL\",\n           \"nombreResponsable\":\"Metacentro\",\n           \"vendedorAsignado\": \"5000\",\n           \"idDistribuidor\":\"\",\n           \"codOficina\":\"\",\n           \"codVendedor\":\"\",\n           \"estado\": \"ALTA\",\n           \"idPlaza\":\"\",\n   \t\t  \"idPuntoVenta\": \"\",\n   \t\t  \"userId\": \"\",\n   \t\t  \"username\": \"\",\n           \"creado_el\": \"30/12/2015 10:59:22\",\n           \"creado_por\": \"usuario\",\n           \"modificado_por\": \"\",\n           \"modificado_el\": \"\"\n       }\n   ]\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"-42\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"No se encontraron datos.\",\n    \"clase\": \"CtrlDispositivo\",\n    \"metodo\": \"getDispositivo\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioConsulta.java",
    "groupTitle": "Dispositivos"
  },
  {
    "type": "POST",
    "url": "/opsidra/modificadispositivo/",
    "title": "[modificadispositivo]",
    "name": "modificadispositivo",
    "description": "<p>Servicio para modificar dispositivos.</p>",
    "group": "Dispositivos",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs donde se desea realizar la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idDispositivo",
            "description": "<p>Id del dispositivo que se desea modificar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "descripcion",
            "description": "<p>Descripci\\u00F3n a modificar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "numTelefono",
            "description": "<p>N\\u00FAmero de tel\\u00E9fono a modificar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "responsable",
            "description": "<p>Id de la panel o ruta a la que se asignar\\u00E1 el dispositivo.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "tipoResponsable",
            "defaultValue": "PANEL o RUTA",
            "description": "<p>Tipo panel o ruta a la que se asignar\\u00E1 el dispositivo.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "vendedorAsingado",
            "description": "<p>id del vendedor de la ruta o panel que utilizar\\u00E1 el dispositivo.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "cajaNumero",
            "description": "<p>N\\u00FAmero de caja del dispositivo.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "zona",
            "description": "<p>Zona del dispositivo.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codOficina",
            "description": "<p>codigo de la oficina.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codVendedor",
            "description": "<p>codigo de vendedor.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idPlaza",
            "description": "<p>id que identifica a la plaza.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idPuntoVenta",
            "description": "<p>id que identifica al Punto de Venta.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "userId",
            "description": "<p>id de user.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "username",
            "description": "<p>nombre de usuario.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "resolucion",
            "description": "<p>N\\u00FAmero de resoluci\\u00F3n del dispositivo.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "fechaResolucion",
            "description": "<p>Fecha de la resoluci\\u00F3n en formato yyyyMMdd.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idDistribuidor",
            "description": "<p>Id del distribuidor asociado.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "estado",
            "description": "<p>Estado que desea aplicarse al dispositivo.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n   \"codArea\": \"505\",\n   \"usuario\": \"usuario\",\n   \"idDispositivo\": \"1\",\n   \"descripcion\": \"ALCATEL ONE TOUCH \",\n   \"numTelefono\": \"78544500\",\n   \"responsable\": \"10\",\n   \"tipoResponsable\": \"PANEL\",\n   \"vendedorAsignado\": \"1020\",\n   \"cajaNumero\":\"2\",\n   \"zona\":\"1\",\n   \"idPlaza\":\"\",\n   \"idPuntoVenta\": \"\",\n   \"userId\": \"\",\n   \"username\": \"\",\n   \"resolucion\":\"54870021\",\n   \"fechaResolucion\":\"20160728\",\n   \"idDistribuidor\":\"\",\n   \"codOficina\":\"\",\n   \"codVendedor\":\"\",\n   \"estado\": \"BAJA\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n   \"idDispositivo\": \"1\",\n   \"respuesta\": {\n       \"codResultado\": \"11\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"Ok. Buz\\u00F3n modificado exitosamente.\",\n       \"clase\": \" \",\n       \"metodo\": \" \",\n       \"excepcion\": \" \",\n       \"tipoExepcion\": \"\"\n   }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n    \"respuesta\": {\n        \"codResultado\": \"-70\",\n        \"mostrar\": \"1\",\n        \"descripcion\": \"El par\\u00E1metro de entrada \\\\\\\"numTelefono\\\"\\\\ esta vac\\u00EDo.\",\n        \"clase\": \" \",\n        \"metodo\": \"validarDatos\",\n        \"excepcion\": \"class CtrlDispositivo\",\n        \"tipoExepcion\": \"Generales\"\n    }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioOperacion.java",
    "groupTitle": "Dispositivos"
  },
  {
    "type": "POST",
    "url": "/opsidra/bajadistribuidor/",
    "title": "[bajaDistribuidor]",
    "name": "bajaDistribuidor",
    "description": "<p>Servicio para dar de baja un distribuidor por pa\\u00EDs.</p>",
    "group": "Distribuidor",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1re del pa\\u00EDs en el cual se desea realizar la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operacion.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idDTS",
            "description": "<p>Identificador del distribuidor que se desea dar de baja.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"codArea\": \"505\",\n    \"usuario\": \"usuario.pruebas\",\n    \"idDTS\": \"6\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n   \"respuesta\": {\n       \"codResultado\": \"201\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"Recurso modificado exitosamente.\",\n       \"clase\": \"OperacionDistribuidor\",\n       \"metodo\": \"doPutDel\",\n       \"excepcion\": \" \",\n       \"tipoExepcion\": \" \"\n   }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n   \"respuesta\": {\n       \"codResultado\": \"-201\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"No existe el recurso deseado.\",\n       \"clase\": \"OperacionDistribuidor\",\n       \"metodo\": \"doPutDel\",\n       \"excepcion\": \" \",\n       \"tipoExepcion\": \" \"\n   }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioOperacion.java",
    "groupTitle": "Distribuidor"
  },
  {
    "type": "POST",
    "url": "/opsidra/creadts/",
    "title": "[creaDistribuidor]",
    "name": "creaDistribuidor",
    "description": "<p>Servicio para crear distribuidores por pais.</p>",
    "group": "Distribuidor",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pais del que se desea obtener informaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operacion.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "tipo",
            "description": "<p>Nombre del tipo de distribuidor.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idfichacliente",
            "description": "<p>Identificador de la ficha del cliente del sistema comercial.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idBodegaVirtual",
            "description": "<p>Identificador de la bodega virtual que ser\\u00E1 asociada al distribuidor.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "nombres",
            "description": "<p>Nombre o nombres del distribuidor que se desea crear.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "numero",
            "description": "<p>N\\u00FAmero del distribuidor que se desea crear.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "email",
            "description": "<p>Email del distribuidor que se desea crear.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "administrador",
            "description": "<p>id del usuario del m\\u00F3dulo de seguridad que es asociado a un distribuidor.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "pagoautomatico",
            "defaultValue": "1 o 0",
            "description": "<p>Dato boolean que indica si el distribuidor realiza pago autom\\u00E1tico.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "canal",
            "description": "<p>Canal del distribuidor a crear.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "numConvenio",
            "description": "<p>N\\u00FAmero de convenio del distribuidor a crear.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"codArea\": \"505\",\n    \"usuario\": \"usuario.pruebas\",\n    \"tipo\": \"Externo\",\n    \"idfichacliente\": \"1\",\n    \"idBodegaVirtual\": \"1\",     \n    \"nombres\": \"Victor\",\n    \"numero\": \"40410431\",\n    \"email\": \"correo@proveedor.com\",\n    \"administrador\":\"41545\",\n    \"pagoautomatico\": \"1\",\n    \"canal\": \"CANAL\",\n    \"numConvenio\": \"N1\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n  \"idDTS\": \"8\",\n  \"respuesta\": {\n    \"codResultado\": \"200\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"Campos agregados exitosamente.\",\n    \"clase\": \"OperacionDistribuidor\",\n    \"metodo\": \"doPost\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"-200\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"Ya existe el recurso enviado.\",\n    \"clase\": \"OperacionDistribuidor\",\n    \"metodo\": \"doPost\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioOperacion.java",
    "groupTitle": "Distribuidor"
  },
  {
    "type": "POST",
    "url": "/consultasidra/getdts/",
    "title": "[getDistribuidor]",
    "name": "getDistribuidor",
    "description": "<p>Servicio para obtener los datos de distribuidores internos o externos configurados por pa\\u00EDs.</p>",
    "group": "Distribuidor",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs del que se desea obtener informaci\\u00F3n .</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operacion.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idBodegaVirtual",
            "description": "<p>Identificador de la bodega virtual.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "tipo",
            "description": "<p>Nombre del tipo de distribuidor.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idfichacliente",
            "description": "<p>Identificador de la ficha del cliente del sistema comercial.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "nombres",
            "description": "<p>Nombre o nombres del distribuidor que se desea obtener.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "numero",
            "description": "<p>N\\u00FAmero del distribuidor que se desea obtener.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "email",
            "description": "<p>Email del distribuidor que se desea consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "administrador",
            "description": "<p>Identificador del usuario del m\\u00F3dulo de seguridad que es asociado a un distribuidor.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "pagoautomatico",
            "defaultValue": "1 o 0",
            "description": "<p>Dato boolean que indica si el distribuidor realiza pago autom\\u00E1tico.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "canal",
            "description": "<p>Canal del distribuidor a buscar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "numConvenio",
            "description": "<p>N\\u00FAmero de convenio del distribuidor a buscar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "codCliente",
            "description": "<p>C\\u00F3digo de cliente del distribuidor a buscar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "codCuenta",
            "description": "<p>C\\u00F3digo de cuenta del distribuidor a buscar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "resultadoSCL",
            "description": "<p>Resultado de ficha de cliente.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "estado",
            "defaultValue": "ALTA o BAJA",
            "description": "<p>Estado del cu\\u00E1l se desea obtener distribuidores.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"codArea\": \"505\",\n    \"usuario\": \"usuario.pruebas\",\n    \"idBodegaVirtual\": \"\",\n    \"tipo\": \"\",\n    \"idfichacliente\": \"\",\n    \"nombres\": \"\",\n    \"numero\": \"\",\n    \"email\": \"\",\n    \"pagoautomatico\": \"\",\n    \"canal\": \"\",\n    \"numConvenio\": \"\",\n    \"codCliente\": \"\",\n    \"codCuenta\": \"\",\n    \"resultadoSCL\": \"\",\n    \"estado\": \"\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n   \"respuesta\": {\n       \"codResultado\": \"0\",\n       \"mostrar\": \"0\",\n       \"descripcion\": \"Recursos recuperados exitosamente.\"\n   },\n   \"distribuidor\": [\n     {\n      \"idDTS\": \"36\",\n      \"tipo\": \"INTERNO\",\n      \"idfichacliente\": \"2202\",\n      \"idBodegaVirtual\": \"73\",\n      \"idAlmacenBod\": \"76\",\n      \"idBodegaSCL\": \"542\",\n      \"nombres\": \"daniel\",\n      \"numero\": \"58320096\",\n      \"email\": \"daniel@tobar.com\",\n      \"administrador\":\"122\",\n      \"pagoautomatico\": \"1\",\n      \"canal\": \"\",\n      \"numConvenio\": \"\",\n      \"codCliente\": \"\",\n      \"codCuenta\": \"\",\n      \"resultadoSCL\": \"0\",\n      \"estado\": \"ALTA\",\n      \"creado_el\": \"09/12/2015 16:27:05\",\n      \"creado_por\": \"sergio.lujan\"\n    },\n    {\n      \"idDTS\": \"33\",\n      \"tipo\": \"INTERNO\",\n      \"idfichacliente\": \"1542\",\n      \"idBodegaVirtual\": \"63\",\n      \"idAlmacenBod\": \"52\",\n      \"idBodegaSCL\": \"1108\",\n      \"nombres\": \"DIRCAM\",\n      \"numero\": \"71806642\",\n      \"email\": \"vladimir.rodriguez@telefonica.com\",\n      \"administrador\":\"122\",\n      \"pagoautomatico\": \"1\",\n      \"canal\": \"\",\n      \"numConvenio\": \"\",\n      \"codCliente\": \"\",\n      \"codCuenta\": \"\",\n      \"resultadoSCL\": \"0\",\n      \"estado\": \"ALTA\",\n      \"creado_el\": \"08/12/2015 17:09:52\",\n      \"creado_por\": \"vladimir.rodriguez\"\n    }\n  ]\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n   \"respuesta\": {\n       \"codResultado\": \"-101\",\n       \"mostrar\": \"0\",\n       \"descripcion\": \"Ocurrio un Problema inesperado, contacte a su Administrador.\",\n       \"clase\": \"OperacionDistribuidor\",\n       \"metodo\": \"doGet\",\n       \"excepcion\": \" \",\n       \"tipoExepcion\": \" \"\n   }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioConsulta.java",
    "groupTitle": "Distribuidor"
  },
  {
    "type": "POST",
    "url": "/opsidra/modificadts/",
    "title": "[modDistribuidor]",
    "name": "modDistribuidor",
    "description": "<p>Servicio para modificar grupo de Distribuidors de configuraciones por pais.</p>",
    "group": "Distribuidor",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pais del que se desea obtener informaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operacion.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idDTS",
            "description": "<p>Identificador del distribuidor que se desea modificar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "tipo",
            "description": "<p>Nombre del tipo de distribuidor.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idfichacliente",
            "description": "<p>Identificador de la ficha del cliente del sistema comercial.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "nombres",
            "description": "<p>Nombre o nombres a modificar del distribuidor indicado.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "apellidos",
            "description": "<p>Apellido o apellidos a modificar del distribuidor indicado.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "numero",
            "description": "<p>N\\u00FAmero nuevo para modificar del distribuidor indicado.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "email",
            "description": "<p>Email a modificar del distribuidor indicado.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "administrador",
            "description": "<p>id del usuario del m\\u00F3dulo de seguridad que es asociado a un distribuidor.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "pagoautomatico",
            "defaultValue": "1 o 0",
            "description": "<p>Dato boolean que indica si el distribuidor realiza pago autom\\u00E1tico.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "canal",
            "description": "<p>Canal del distribuidor a crear.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "numConvenio",
            "description": "<p>N\\u00FAmero de convenio del distribuidor a crear.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "estado",
            "defaultValue": "ALTA o BAJA",
            "description": "<p>Estado nuevo del distribuidor.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"codArea\": \"505\",\n    \"usuario\": \"usuario.pruebas\",\n    \"idDTS\": \"1\",\n    \"tipo\": \"Externo\",\n    \"idfichacliente\": \"1\",\n    \"nombres\": \"Victor\",\n    \"apellidos\": \"M\\u00E9ndez\",\n    \"numero\": \"40410431\",\n    \"email\": \"correo@proveedor.com\",\n    \"administrador\":\"41545\",\n    \"pagoautomatico\": \"1\",\n    \"canal\": \"CANAL\",\n    \"numConvenio\": \"N1\",\n    \"estado\": \"ALTA\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n   \"respuesta\": {\n       \"codResultado\": \"201\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"Recurso modificado exitosamente.\",\n       \"clase\": \"OperacionDistribuidor\",\n       \"metodo\": \"doPutDel\",\n       \"excepcion\": \" \",\n       \"tipoExepcion\": \"\"\n   }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n   \"respuesta\": {\n       \"codResultado\": \"-199\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"Los siguientes datos faltan o son incorrectos:  Datos no num\\u00E9ricos en Ficha Cliente. Longitud del N\\u00FAmero (8).\",\n       \"clase\": \"CtrlDistribuidor\",\n       \"metodo\": \"validarInput\",\n       \"excepcion\": \" \",\n       \"tipoExepcion\": \"\"\n   }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioOperacion.java",
    "groupTitle": "Distribuidor"
  },
  {
    "type": "POST",
    "url": "/consultasidra/getfichacliente/",
    "title": "[getFichaCliente]",
    "name": "getFichaCliente",
    "description": "<p>Servicio para obtener los datos de fichas de cliente.</p>",
    "group": "Ficha_Cliente",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pais.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "token",
            "description": "<p>Token de autenticaci\\u00F3n de sesion de usuario.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codDispositivo",
            "description": "<p>c\\u00F3digo del dispositivo desde donde se realiza la operaci\\u00F3n, en caso de utilizarse la APP M\\u00F3VIL.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "tipoDocumento",
            "description": "<p>Descripci\\u00F3n del documento de identificaci\\u00F3n del cliente. Campo opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "noDocumento",
            "description": "<p>N\\u00FAmero de documento de identificaci\\u00F3n del cliente. Campo opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "numTelefono",
            "description": "<p>N\\u00FAmero de telefono del cliente. Campo opcional</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"token\": \"WEB\",\n    \"codArea\": \"505\",\n    \"usuario\": \"usuario.pruebas\",\n    \"codDispositivo\": \"64E1CE4A48BF656974C376B7905F7E62\",\n    \"noDocumento\": \"8-443-43323\",\n    \"tipoDocumento\": \"RUC\",\n    \"numTelefono\": \"47588115\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n  \"token\": \"WEB\",\n  \"respuesta\": {\n    \"codResultado\": \"0\",\n    \"mostrar\": \"0\",\n    \"descripcion\": \"Recursos recuperados exitosamente.\"\n  },\n  \"cliente\":    {\n     \"primerNombre\": \"JORGE\",\n     \"segundoNombre\": \"MARIO\",\n     \"primerApellido\": \"YAC\",\n     \"segundoApellido\": \"AJU\",\n     \"tipoDocumento\": \"RUC\",\n     \"noDocumento\": \"8-443-43323\",\n     \"codCuenta\": \"2766661\",\n     \"codCliente\": \"3009488\",\n     \"numTelefono\": \"67979799\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"-199\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"Los siguientes datos faltan o son incorrectos: Ocurrio un Problema inesperado, contacte a su Administrador.\",\n    \"clase\": \"CtrlFichaCliente\",\n    \"metodo\": \"validarInput\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioConsulta.java",
    "groupTitle": "Ficha_Cliente"
  },
  {
    "type": "POST",
    "url": "/opsidra/postfichacliente/",
    "title": "[postFichaCliente]",
    "name": "postFichaCliente",
    "description": "<p>Servicio para crear fichas de Clientes en el sistema comercial por pa\\u00EDs (SCL).</p>",
    "group": "Ficha_Cliente",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "token",
            "description": "<p>Token de validaci\\u00F3n de sesi\\u00F3n de usuario.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs del que se desea obtener informaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operacion.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codDispositivo",
            "description": "<p>C\\u00F3digo del dispositivo desde donde se realiza la operaci\\u00F3n, en caso de utilizarse la APP M\\u00F3VIL.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "tipoCliente",
            "description": "<p>&quot;DTS o PDV&quot; Descripci\\u00F3n que indica si es un distribuidor o punto de venta</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idDts",
            "description": "<p>ID de Sidra que identifica un \\u00FAnico Distribuidor.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idPdv",
            "description": "<p>ID de Sidra que identifica un \\u00FAnico Punto de Venta. Este campo es opcional solo cuando tipoCliente = PDV</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "tipoDocumento",
            "description": "<p>Descripci\\u00F3n del tipo de documento con el que se identifica el cliente.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "noDocumento",
            "description": "<p>Numero \\u00FAnico de identificaci\\u00F3n del cliente.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "primerNombre",
            "description": "<p>Primer nombre del cliente</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "segundoNombre",
            "description": "<p>Segundo nombre del cliente. Este campo es opcional</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "primerApellido",
            "description": "<p>Primer apellido del cliente.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "segundoApellido",
            "description": "<p>Segundo apellido del cliente. Este campo es opcional</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "telContacto",
            "description": "<p>N\\u00FAmero de tel\\u00E9fono de contacto del cliente.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"token\": \"WEB\",\n    \"codArea\":\"505\",\n    \"usuario\":\"usuario.pruebas\",\n    \"tipoCliente\":\"PDV\",\n    \"idDts\":\"48\",\n    \"idPdv\":\"133\",\n    \"tipoDocumento\":\"RUC\",\n    \"noDocumento\":\"8-443-43323\",\n    \"primerNombre\":\"JOSE\",\n    \"segundoNombre\":\"MARIO\",\n    \"primerApellido\":\"YAC\",\n    \"segundoApellido\":\"PEREZ\",\n    \"telContacto\":\"67979799\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n   \"respuesta\":    {\n      \"codResultado\": \"68\",\n      \"mostrar\": \"1\",\n      \"descripcion\": \"OK. La ficha del cliente scl se creo correctamente. El cliente fue creado con \\u00E9xito en SCL\",\n      \"clase\": \"OperacionFichaCliente\",\n      \"metodo\": \"doPost\",\n      \"excepcion\": \" \",\n      \"tipoExepcion\": \"\"\n   },\n   \"cliente\":    {\n      \"codCliente\": \"3009493\",\n      \"codCuenta\": \"2766661\",\n      \"idPdv\": \"132\"\n   }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:",
          "content": "{\"respuesta\": {\n\t\t\"codResultado\": \"-604\",\n\t\t\"mostrar\": \"1\",\n\t\t\"descripcion\": \"Ocurrio un inconveniente al crear la ficha de cliente en scl. Ocurri\\u00F3 un error inesperado. 12037 - Formato numero de telefono inv\\u00E1lido = null\",\n\t\t\"clase\": \"OperacionFichaCliente\",\n\t\t\"metodo\": \"doPost\",\n\t\t\"excepcion\": \" \",\n\t\t\"tipoExepcion\": \"Generales\"\n}}",
          "type": "json"
        }
      ]
    },
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioOperacion.java",
    "groupTitle": "Ficha_Cliente"
  },
  {
    "type": "POST",
    "url": "/opsidra/bajafoliobodvirtual/",
    "title": "[bajaFolioBodVirtual]",
    "name": "bajaFolioBodVirtual",
    "description": "<p>Servicio para dar de baja folios de la bodega virtual asociada.</p>",
    "group": "Folios",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs del que se desea obtener informaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idTipo",
            "description": "<p>Identificador del tipo al que se dar\\u00E1 de baja, actualmente la MAC ADRRESS.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "tipo",
            "description": "<p>Nombre del tipo que se dar\\u00E1 de baja.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idFolio",
            "description": "<p>Id del rango del folio a dar de baja.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"codArea\": \"505\",\n    \"usuario\": \"usuario.pruebas\",\n    \"idTipo\": \"11:22:33:44:55\",\n    \"tipo\": \"DISPOSITIVOS\",\n    \"folios\": [\n        {\n            \"idFolio\": \"1\"\n        }\n    ]\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n   \"respuesta\": {\n       \"codResultado\": \"201\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"Recurso modificado exitosamente\",\n       \"clase\": \"OperacionConfiguracionFolioVirtual\",\n       \"metodo\": \"doPutDel\",\n       \"excepcion\": \" \",\n       \"tipoExepcion\": \"\"\n   }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n   \"respuesta\": {\n       \"codResultado\": \"-199\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"Los siguientes datos faltan o son incorrectos:  Datos no num\\u00E9ricos en el rango.\",\n       \"clase\": \"CtrlConfiguracionFolioVirtual\",\n       \"metodo\": \"validarInput\",\n       \"excepcion\": \" \",\n       \"tipoExepcion\": \"\"\n   }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioOperacion.java",
    "groupTitle": "Folios"
  },
  {
    "type": "POST",
    "url": "/opsidra/creafoliobodvirtual/",
    "title": "[creaFolioBodVirtual]",
    "name": "creaFolioBodVirtual",
    "description": "<p>Servicio para asociar configuracion de folios a bodegas virtuales.</p>",
    "group": "Folios",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs del que se desea obtener informaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idTipo",
            "description": "<p>Identificador del tipo al que se asociar\\u00E1 el folio, actualmente la MAC ADRRESS.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "tipo",
            "description": "<p>Nombre del tipo que se asociar\\u00E1 el folio (DISPOSITIVOS).</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "tipoDocumento",
            "description": "<p>Tipo de documento por el que se manejan folios de la bodega asociada, en caso que los folios en el pa\\u00EDs al que pertenece el distribuidor sea por reserva de folios.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "serie",
            "description": "<p>Serie del folio a configurar en la bodega.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "noInicialFolio",
            "description": "<p>Rango inicial del folio a configurar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "noFinalFolio",
            "description": "<p>N\\u00FAmero final del rango a configurar para el folio.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"codArea\": \"505\",\n    \"usuario\": \"usuario.pruebas\",\n    \"idTipo\": \"11:22:33:44:55\",\n    \"tipo\": \"DISPOSITIVOS\",\n    \"folios\": [\n        {\n            \"tipoDocumento\": \"FACTURA\",\n            \"serie\": \"A\",\n            \"noInicialFolio\": \"1\",\n            \"noFinalFolio\": \"50\"\n        },\n        {\n            \"tipoDocumento\": \"FACTURA\",\n            \"serie\": \"A\",\n            \"noInicialFolio\": \"51\",\n            \"noFinalFolio\": \"100\"\n        }\n    ]\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n   \"respuesta\": {\n       \"codResultado\": \"200\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"Campos agregados exitosamente.\",\n       \"clase\": \"OperacionConfiguracionFolioVirtual\",\n       \"metodo\": \"doPost\",\n       \"excepcion\": \" \",\n       \"tipoExepcion\": \"\"\n   }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n   \"respuesta\": {\n       \"codResultado\": \"-199\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"Los siguientes datos faltan o son incorrectos:  Datos no num\\u00E9ricos en el rango.\",\n       \"clase\": \"CtrlConfiguracionFolioVirtual\",\n       \"metodo\": \"validarInput\",\n       \"excepcion\": \" \",\n       \"tipoExepcion\": \"\"\n   }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioOperacion.java",
    "groupTitle": "Folios"
  },
  {
    "type": "POST",
    "url": "/consultasidra/getfoliobodvirtual/",
    "title": "[getConfiguracionFolioVirtual]",
    "name": "getConfiguracionFolioVirtual",
    "description": "<p>Servicio para obtener las configuraciones de folios asociadas a las bodegas virtuales de los distribuidores de un pa\\u00EDs determinado.</p>",
    "group": "Folios",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs del que se desea obtener informaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idTipo",
            "description": "<p>Identificador del dispositivo (MAC ADDRESS). Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "tipo",
            "description": "<p>Nombre del tipo (DISPOSITIVOS). Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "estado",
            "description": "<p>Estado del folio asociado.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"codArea\": \"505\",\n    \"usuario\": \"usuario.pruebas\",\n    \"idTipo\": \"11:22:33:44:55\",\n    \"tipo\": \"DISPOSITIVOS\",\n    \"estado\": \"\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n   \"respuesta\": {\n       \"codResultado\": \"0\",\n       \"mostrar\": \"0\",\n       \"descripcion\": \"Recursos recuperados exitosamente.\"\n   },\n   \"configuracionFolio\": {\n    \"usuario\": \"usuario.pruebas\",\n    \"idTipo\": \"11:22:33:44:55\",\n    \"tipo\": \"DISPOSITIVOS\",\n    \"folios\": [\n      {\n        \"idFolio\": \"101\",\n        \"tipoDocumento\": \"FACTURA\",\n        \"serie\": \"A\",\n        \"noInicialFolio\": \"1\",\n        \"noFinalFolio\": \"50\",\n        \"cant_utilizados\": \"0\",\n        \"ultimo_utilizado\": \"\",\n        \"folio_siguiente\": \"1\",\n        \"estado\": \"ALTA\",\n        \"caja_numero\": \"7845\",\n        \"zona\": \"13\",\n        \"creado_el\": \"02/12/2015 16:35:37\",\n        \"creado_por\": \"usuario.pruebas\"\n      },\n      {\n        \"idFolio\": \"102\",\n        \"tipoDocumento\": \"FACTURA\",\n        \"serie\": \"A\",\n        \"noInicialFolio\": \"51\",\n        \"noFinalFolio\": \"100\",\n        \"cant_utilizados\": \"1\",\n        \"ultimo_utilizado\": \"51\",\n        \"folio_siguiente\": \"52\",\n        \"estado\": \"EN_USO\",\n        \"caja_numero\": \"4564\",\n        \"zona\": \"12\",\n        \"creado_el\": \"02/12/2015 16:35:37\",\n        \"creado_por\": \"usuario.pruebas\"\n      }\n    ]\n  }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n   \"respuesta\": {\n       \"codResultado\": \"-394\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"No se encontraron folios configurados.\",\n       \"clase\": \"OperacionFolioVirtual\",\n       \"metodo\": \"doGet\",\n       \"excepcion\": \" \",\n       \"tipoExepcion\": \" \"\n   }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioConsulta.java",
    "groupTitle": "Folios"
  },
  {
    "type": "POST",
    "url": "/consultasidra/getvisita/",
    "title": "[getvisita]",
    "name": "getvisita",
    "description": "<p>Servicio para obtener informaci\\u00F3n de los tipos de transacciones de inventario que pueden realizarse en SIDRA.</p>",
    "group": "GESTION_VISITA",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "token",
            "description": "<p>token de validaci\\u00F3n de sesi\\u00F3n de usuario</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>c\\u00F3digo de area del pa\\u00EDs en el que se desea obtener informaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>nombre del usuario que solicita la informaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codDispositivo",
            "description": "<p>c\\u00F3digo del dispositivo desde donde se realiza la operaci\\u00F3n, en caso de utilizarse la APP M\\u00F3VIL.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idVendedor",
            "description": "<p>id del vendedor del cual se desean obtener las visitas registradas.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "fechaInicio",
            "description": "<p>rango inicial de fechas a tomar en cuenta para obtener datos. FORMATO:YYYYmmdd.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "fechaFin",
            "description": "<p>rango final de fechas a tomar en cuenta para obtener datos. FORMATO:YYYYmmdd.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idPuntoVenta",
            "description": "<p>id del punto de venta del que se desean obtener visitas.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idJornada",
            "description": "<p>id de la Jornada de la que se desean obtener visitas.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "gestion",
            "defaultValue": "VENTA o NO VENTA",
            "description": "<p>tipo de gestion realizada en la visita.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "numRecarga",
            "description": "<p>N\\u00FAmero de recarga del punto de venta a consultar.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"token\": \"WEB\",\n    \"codArea\": \"505\",\n    \"usuario\":\"usuario.sidra\",\n    \"codDispositivo\": \"64E1CE4A48BF656974C376B7905F7E62\",\n    \"idVendedor\":\"\",\n    \"fechaInicio\": \"\",\n    \"fechaFin\":\"\",\n    \"idPuntoVenta\":\"\",\n    \"idJornada\": \"\",\n    \"gestion\": \"\",\n    \"numRecarga\": \"\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n   \"token\": \"ewwer\",\n   \"respuesta\": {\n      \"codResultado\": \"12\",\n      \"mostrar\": \"0\",\n      \"descripcion\": \"OK. Datos obtenidos exitosamente\",\n      \"clase\": \" \",\n      \"metodo\": \" \",\n      \"excepcion\": \" \",\n      \"tipoExepcion\": \"\"\n   },\n   \"visitas\": [\n      {\n          \"idVisita\": \"9\",\n          \"idVendedor\": \"2322\",\n          \"nombreVendedor\": \"usuario sidra2\",\n          \"idJornada\": \"26\",\n          \"fecha\": \"25/02/2016 12:22:00\",\n          \"idPuntoVenta\": \"265\",\n          \"nombrePuntoVenta\": \"TIENDA MARY\",\n          \"latitud\": \"0.0\",\n          \"longitud\": \"0.0\",\n          \"gestion\": \"NO VENTA\",\n          \"causa\": \"No se encotr\\u00F3 abierto.\",\n          \"folio\": \" \",\n          \"observaciones\": \"\",\n          \"creado_el\": \"25/02/2016 14:36:24\",\n          \"creado_por\": \"usuario\",\n          \"modificado_el\": \" \",\n          \"modificado_por\": \" \"\n      }\n   ]\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n   \"token\": \"WEB\",   \n   \"respuesta\": {\n      \"codResultado\": \"-42\",\n      \"mostrar\": \"1\",\n      \"descripcion\": \"No se encontraron datos.\",\n      \"clase\": \"CtrlVisitaGestion\",\n      \"metodo\": \"getTipoTransaccionInv\",\n      \"excepcion\": \" \",\n      \"tipoExepcion\": \"Generales\"\n   }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioConsulta.java",
    "groupTitle": "GESTION_VISITA"
  },
  {
    "type": "POST",
    "url": "/opsidra/registravisita/",
    "title": "[registravisita]",
    "name": "registravisita",
    "description": "<p>Servicio para registrar las visitas realizadas por el vendedor a un punto de venta en el sistema de SIDRA..</p>",
    "group": "GESTION_VISITA",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "token",
            "description": "<p>token de validaci\\u00F3n de usuario.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>c\\u00F3digo del pa\\u00EDs de donde se desea realizar la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codDispositivo",
            "description": "<p>c\\u00F3digo del dispositivo desde donde se realiza la operaci\\u00F3n, en caso de utilizarse la APP M\\u00F3VIL.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idVendedor",
            "description": "<p>id del vendedor que realiza la visita.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idJornada",
            "description": "<p>id la jornada activa en la que se realiza la visita.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "fecha",
            "description": "<p>fecha en formato YYYYmmddHHMMss, que se registra al momento de realizar la visita.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idPuntoVenta",
            "description": "<p>id del punto de venta que se visita.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "latitud",
            "description": "<p>datos de ubicaci\\u00F3n del pdv.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "longitud",
            "description": "<p>datos de ubicaci\\u00F3n del pdv.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "gestion",
            "description": "<p>indica si se realizo venta o no durante la visita.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "folio",
            "description": "<p>id de venta si se realiz\\u00F3 venta.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "observaciones",
            "description": "<p>comentarios de la visita realizada.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "causa",
            "description": "<p>Causa de la gesti\\u00F3n de NO VENTA.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n   \"token\":\"DSFSDF\",\n   \"codArea\":\"505\",\n   \"usuario\": \"usuario\",\n   \"codDispositivo\": \"64E1CE4A48BF656974C376B7905F7E62\",\n   \"idVendedor\":\"11\",\n   \"idJornada\":\"11\",\n   \"fecha\":\"YYYYmmddHHMMss\",\n   \"idPuntoVenta\":\"12\",\n   \"latitud\":\"\",\n   \"longitud\":\"\",\n   \"gestion\":\"\",\n   \"folio\":\"\",\n   \"causa\": \"No se encotr\\u00F3 abierto.\",\n   \"observaciones\":\"\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n   \"token\":\"dsfsddfsfdds\",\n     \"respuesta\": {\n        \"codResultado\": \"17\",\n        \"mostrar\": \"1\",\n        \"descripcion\": \"OK. Registro visita correctamente \",\n        \"clase\": \" \",\n        \"metodo\": \" \",\n        \"excepcion\": \" \",\n        \"tipoExepcion\": \"\"\n    }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n    \"codResultado\": \"-93\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"La Jornada ingresada no existe o no es v\\u00E1lida para la fecha actual. \",\n    \"clase\": \"CtrlVisitaGestion\",\n    \"metodo\": \"registraVisita\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioOperacion.java",
    "groupTitle": "GESTION_VISITA"
  },
  {
    "type": "POST",
    "url": "/consultasidra/gethistoricopromo/",
    "title": "[getHistoricoPromo]",
    "name": "getHistoricoPromo",
    "description": "<p>Servicio para consultar los datos historicos de promocionales otorgados en Sidra.</p>",
    "group": "Historico",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pais.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "token",
            "description": "<p>Token de autenticaci\\u00F3n de sesion de usuario.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codDispositivo",
            "description": "<p>c\\u00F3digo del dispositivo desde donde se realiza la operaci\\u00F3n, en caso de utilizarse la APP M\\u00F3VIL.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idCampania",
            "description": "<p>Identificador de la campa\\u00F1a a consultar. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "tipoCliente",
            "description": "<p>Tipo de cliente a buscar, puede ser CF o PDV.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idTipo",
            "description": "<p>Identificador del cliente a consultar. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idRuta",
            "description": "<p>Identificador de la ruta a consultar. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idVendedor",
            "description": "<p>Identificador del vendedor a consultar. Este campo es opcional.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"token\":\"WEB\",\n    \"codArea\":\"505\",\n    \"usuario\":\"usuarioprueba\",\n    \"codDispositivo\": \"64E1CE4A48BF656974C376B7905F7E62\",\n    \"idCampania\":\"\",\n    \"tipoCliente\":\"\",\n    \"idTipo\":\"\",\n    \"idRuta\":\"\",\n    \"idVendedor\":\"\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n  \"token\": \"WEB\",\n  \"respuesta\": {\n    \"codResultado\": \"0\",\n    \"mostrar\": \"0\",\n    \"descripcion\": \"Recursos recuperados exitosamente.\"\n  },\n  \"clientes\": {\n    \"tipoCliente\": \"CF\",\n    \"idTipo\": \"55213455\",\n    \"campanias\": {\n      \"idCampania\": \"21\",\n      \"nombreCampania\": \"Promcion Abril\",\n      \"cantPromocionales\": \"7\",\n      \"articulos\": [\n        {\n          \"idArticulo\": \"8\",\n          \"descripcion\": \"PLAYERA M\",\n          \"cantidad\": \"2\",\n          \"tipoInv\": \"INV_SIDRA\"\n        },\n        {\n          \"idArticulo\": \"7\",\n          \"descripcion\": \"PLAYERA S\",\n          \"cantidad\": \"5\",\n          \"tipoInv\": \"INV_SIDRA\"\n        }\n      ]\n    }\n  }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"-199\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"Los siguientes datos faltan o son incorrectos: El par\\u00E1metro de entrada \\\"tipo\\\"\\\\ esta vac\\u00EDo\",\n    \"clase\": \"CtrlHistoricoPromo\",\n    \"metodo\": \"validarInput\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioConsulta.java",
    "groupTitle": "Historico"
  },
  {
    "type": "POST",
    "url": "/opsidra/cargafile/",
    "title": "[CargaFile]",
    "name": "CargaFile",
    "description": "<p>Servicio para cargar una fotografia de un punto de venta.</p>",
    "group": "ImagenPDV",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs en el que se desea realizar la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codDispositivo",
            "description": "<p>c\\u00F3digo del dispositivo desde donde se realiza la operaci\\u00F3n, en caso de utilizarse la APP M\\u00F3VIL.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "token",
            "description": "<p>token de validaci\\u00F3n de autenticaci\\u00F3n de usuario.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idPDV",
            "description": "<p>Id del registro al cual se desea agregar la imagen.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "archivo",
            "description": "<p>String codificado en base64 de la imagen a adjuntar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "nombreArchivo",
            "description": "<p>Nombre del archivo a adjuntar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "extension",
            "description": "<p>Extensi\\u00F3n del archivo a adjuntar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idVisita",
            "description": "<p>Id de la visita a la que se asociar\\u00E1 la imagen.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "observaciones",
            "description": "<p>Observaciones de la imagen de la visita.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{ \n    \"token\":\"WEB\",\n    \"codArea\": \"505\",\n    \"usuario\": \"usuario.pruebas\" ,\n    \"codDispositivo\": \"64E1CE4A48BF656974C376B7905F7E62\",\n    \"idPDV\": \"32\",\n    \"archivo\": \"/9j/4AAQSkZJRgABAQAAAQABAAD/2wCEAAkG....\",\n    \"nombreArchivo\": \"fotografia\",\n    \"extension\": \".jpg\",\n    \"idVisita\": \"\",\n    \"observaciones\": \"\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n  \"token\":\"WEB\",\n  \"idImgPDV\": \"5\",\n  \"respuesta\": {\n    \"codResultado\": \"5\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"OK. Carga de imagen correctamente\",\n    \"clase\": \"CtrlCargaFile\",\n    \"metodo\": \"enviarImagen\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n  \"token\":\"WEB\",\n  \"respuesta\": {\n    \"codResultado\": \"-372\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"El punto de venta ya cuenta con la cantidad m\\u00E1xima de fotograf\\u00EDas permitida 3.\",\n    \"clase\": \" \",\n    \"metodo\": \"validarDatos\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioOperacion.java",
    "groupTitle": "ImagenPDV"
  },
  {
    "type": "POST",
    "url": "/opsidra/delcargafile/",
    "title": "[delCargafile]",
    "name": "delCargafile",
    "description": "<p>Servicio para dar de baja una fotografia de un punto de venta.</p>",
    "group": "ImagenPDV",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs en el que se desea realizar la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codDispositivo",
            "description": "<p>c\\u00F3digo del dispositivo desde donde se realiza la operaci\\u00F3n, en caso de utilizarse la APP M\\u00F3VIL.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "token",
            "description": "<p>token de validaci\\u00F3n de autenticaci\\u00F3n de usuario.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idImgPDV",
            "description": "<p>Id del registro que se desea dar de baja.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"codArea\": \"505\",\n    \"token\": \"WEB\",\n    \"usuario\": \"usuario.pruebas\",\n    \"codDispositivo\": \"64E1CE4A48BF656974C376B7905F7E62\",\n    \"idImgPDV\": \"4\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"201\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"Recurso modificado exitosamente.\",\n    \"clase\": \"CtrlCargaFile\",\n    \"metodo\": \"enviarImagen\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"-207\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"Ocurri\\u00F3 un Problema al modificar los datos.\",\n    \"clase\": \"CtrlCargaFile\",\n    \"metodo\": \"enviarImagen\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioOperacion.java",
    "groupTitle": "ImagenPDV"
  },
  {
    "type": "POST",
    "url": "/consultasidra/getImagen/",
    "title": "[ObtenerImagen]",
    "name": "getImagen",
    "description": "<p>Servicio para obtener imagen almacenada asociada a un punto de venta.</p>",
    "group": "ImagenPDV",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs en el que se desea realizar la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codDispositivo",
            "description": "<p>c\\u00F3digo del dispositivo desde donde se realiza la operaci\\u00F3n, en caso de utilizarse la APP M\\u00F3VIL.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "token",
            "description": "<p>token de validaci\\u00F3n de autenticaci\\u00F3n de usuario.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idImgPDV",
            "description": "<p>Id del registro que se desea consultar.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"codArea\": \"505\",\n    \"token\": \"WEB\",\n    \"usuario\": \"usuario.pruebas\",\n    \"codDispositivo\": \"64E1CE4A48BF656974C376B7905F7E62\",\n    \"idImgPDV\": \"4\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"0\",\n    \"mostrar\": \"0\"\n  },\n  \"imagen\": {\n    \"idPDV\": \"32\",\n    \"archivo\": \"/9j/4AAQSkZJRgABAQAAAQABAAD/2wCEAAkG\",\n    \"nombreArchivo\": \"fotografia\",\n    \"extension\": \".jpg\",\n    \"creado_el\": \"27/05/2016 09:28:39\",\n    \"creado_por\": \"usuario.pruebas\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"-205\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"No existen registros con esos par\\u00E1metros.\",\n    \"clase\": \"OperacionCargaFile\",\n    \"metodo\": \"getImagenPDV\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioConsulta.java",
    "groupTitle": "ImagenPDV"
  },
  {
    "type": "POST",
    "url": "/consultasidra/getimagenvisita/",
    "title": "[getimagenvisita]",
    "name": "getimagenvisita",
    "description": "<p>Servicio para obtener el listado de imagenes asociadas a una visita.</p>",
    "group": "ImagenPDV",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs en el que se desea realizar la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "token",
            "description": "<p>token de validaci\\u00F3n de autenticaci\\u00F3n de usuario.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codDispositivo",
            "description": "<p>c\\u00F3digo del dispositivo desde donde se realiza la operaci\\u00F3n, en caso de utilizarse la APP M\\u00F3VIL.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idPDV",
            "description": "<p>Id del punto de venta que se desea consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idVisita",
            "description": "<p>Id de la visita que se desea consultar.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"codArea\": \"505\",\n    \"token\": \"WEB\",\n    \"usuario\": \"usuarioPruebas\",\n    \"codDispositivo\": \"64E1CE4A48BF656974C376B7905F7E62\",\n    \"idPDV\": \"5\",\n    \"idVisita\":\"1\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"20\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"OK. Carga de im\\u00E1genes correctamente.\",\n    \"clase\": \"CtrlCargaFile\",\n    \"metodo\": \"getImagenVisita\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"\"\n  },\n  \"imgAsociadas\": [\n    {\n      \"idImgPDV\": \"28\",\n      \"observaciones\": \"\"\n    },\n    {\n      \"idImgPDV\": \"29\",\n      \"observaciones\": \"\"\n    }\n  ]\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"-797\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"No se encontraron registros de im\\u00E1genes asociadas.\",\n    \"clase\": \"CtrlCargaFile\",\n    \"metodo\": \"getImagenVisita\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioConsulta.java",
    "groupTitle": "ImagenPDV"
  },
  {
    "type": "POST",
    "url": "/consultasidra/getimpuestos/",
    "title": "[getImpuestos]",
    "name": "getImpuestos",
    "description": "<p>Servicio para obtener los datos de impuestos por pais.</p>",
    "group": "Impuestos",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pais.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "token",
            "description": "<p>Token de autenticaci\\u00F3n de sesion de usuario.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codDispositivo",
            "description": "<p>c\\u00F3digo del dispositivo desde donde se realiza la operaci\\u00F3n, en caso de utilizarse la APP M\\u00F3VIL.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"token\": \"WEB\",\n    \"codArea\": \"505\",\n    \"usuario\":\"usuario.sidra\",\n    \"codDispositivo\": \"64E1CE4A48BF656974C376B7905F7E62\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"0\",\n    \"mostrar\": \"0\",\n    \"descripcion\": \"Recursos recuperados exitosamente.\"\n  },\n  \"codArea\": \"505\",\n  \"impuestos\": [{\n    \"nombre\": \"IVA\",\n    \"valor\": \"IVA\",\n    \"tipoCliente\": \"\",\n    \"despuesDeDescuento\": \"TRUE\",\n    \"porcentaje\": \"15\",\n    \"estado\": \"ALTA\",\n    \"grupos\": [\n      {\n        \"nombre\": \"TERMINAL\",\n        \"valor\": \"Terminal\"\n      },\n      {\n        \"nombre\": \"SIMCARD\",\n        \"valor\": \"Simcard\"\n      }\n    ]\n  }],\n  \"descuentos\": [\n    {\n      \"nombre\": \"ISC\",\n      \"valor\": \"ISC\",\n      \"porcentaje\": \"4.381\",\n      \"despuesDeDescuento\": \"\",\n      \"estado\": \"ALTA\",\n      \"grupos\": [\n        {\n          \"nombre\": \"RECARGA\",\n          \"valor\": \"Recarga\"\n        },\n        {\n          \"nombre\": \"TARJETASRASCA\",\n          \"valor\": \"Tarjetas Rasca\"\n        }\n      ]\n    }\n  ]\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"-199\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"Los siguientes datos faltan o son incorrectos:  C\\u00F3digo de \\u00E1rea debe ser de 3 d\\u00EDgitos.\",\n    \"clase\": \"CtrlImpuestos\",\n    \"metodo\": \"validarInput\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioConsulta.java",
    "groupTitle": "Impuestos"
  },
  {
    "type": "POST",
    "url": "/opsidra/asignafechajornada/",
    "title": "[asignafechajornada]",
    "name": "asignafechajornada",
    "description": "<p>Servicio para asignar fecha de cierrr de jornada por vendedor.</p>",
    "group": "Jornada",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs del que se desea obtener informaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idVendedor",
            "description": "<p>Identificador del vendedor de la jornada y la venta.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "fecha",
            "description": "<p>Fecha del cierre de jornada en formato ddMMyyyy.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"codArea\":\"505\",\n    \"usuario\":\"usuario.pruebas\",\n    \"idVendedor\":\"2242\",\n    \"fecha\":\"31122016\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"71\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"OK. Fecha registrada correctamente.\",\n    \"clase\": \"OperacionJornada\",\n    \"metodo\": \"asignaFechaJornada\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"-848\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"El vendedor enviado no se encontr\\u00F3 como vendedor responsable.\",\n    \"clase\": \"CtrlJornada\",\n    \"metodo\": \"validarInput\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioOperacion.java",
    "groupTitle": "Jornada"
  },
  {
    "type": "POST",
    "url": "/opsidra/creajornadamasiva/",
    "title": "[creajornadamasiva]",
    "name": "creajornadamasiva",
    "description": "<p>Servicio para crear Jornadas por pa\\u00EDs.</p>",
    "group": "Jornada",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs en el que se desea realizar la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "token",
            "description": "<p>token de validaci\\u00F3n de autenticaci\\u00F3n de usuario.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "fecha",
            "description": "<p>Fecha de la jornada en formato yyyyMMddHHMISS.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "tipo",
            "defaultValue": "RUTA o PANEL",
            "description": "<p>Nombre del tipo asociado.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idDistribuidor",
            "description": "<p>Identificador del distribuidor a asociar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idBodegaVendedor",
            "description": "<p>Identificador de la bodega del vendedor que ser\\u00E1 asociada a la Jornada.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idResponsable",
            "description": "<p>Identificador del vendedor responsable de la panel o ruta a asociar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codDispositivo",
            "description": "<p>C\\u00F3digo del dispositivo a asociar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "observaciones",
            "description": "<p>Observaciones de la Jornada que se desea crear.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "vendedores",
            "description": "<p>Listado de vendedores que iniciar\\u00E1n la jornada.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "vendedores.idVendedor",
            "description": "<p>Identificador del vendedor que iniciar\\u00E1 jornada.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"codArea\": \"505\",\n    \"token\": \"WEB\",\n    \"usuario\": \"usuario.pruebas\",\n    \"fecha\": \"20160714143000\",\n    \"tipo\": \"PANEL\",\n    \"idDistribuidor\": \"17\",\n    \"idBodegaVendedor\": \"60\",\n    \"idResponsable\": \"462\",\n    \"codDispositivo\": \"ASDF123QWER456ZXCV789\",\n    \"observaciones\": \"\",\n    \"vendedores\": [\n        {\n            \"idVendedor\": \"2322\"\n        },\n        {\n            \"idVendedor\": \"2016\"\n        }\n    ]\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n  \"token\": \"WEB\",\n  \"respuesta\": {\n    \"codResultado\": \"200\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"Campos agregados exitosamente.\",\n    \"clase\": \"OperacionJornadaMasiva\",\n    \"metodo\": \"doPost\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"\"\n  }\n  \"jornadas\": [\n    {\n      \"idJornada\": \"28\",\n      \"idVendedor\": \"462\",\n      \"saldoInicial\": \"2000\"\n    },\n    {\n      \"idJornada\": \"29\",\n      \"idVendedor\": \"904\",\n      \"saldoInicial\": \"2000\"\n    },\n    {\n      \"idJornada\": \"30\",\n      \"idVendedor\": \"1382\",\n      \"saldoInicial\": \"2000\"\n    }\n  ]\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"-739\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"El responsable enviado no coincide con el responsable asignado a la panel.\",\n    \"clase\": \"OperacionJornadaMasiva\",\n    \"metodo\": \"doPost\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioOperacion.java",
    "groupTitle": "Jornada"
  },
  {
    "type": "POST",
    "url": "/consultasidra/getjornada/",
    "title": "[getJornada]",
    "name": "getJornada",
    "description": "<p>Servicio para obtener los datos de Jornadaes internos o externos configurados por pa\\u00EDs.</p>",
    "group": "Jornada",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs en el que se desea realizar la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "token",
            "description": "<p>token de validaci\\u00F3n de autenticaci\\u00F3n de usuario.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codDispositivo",
            "description": "<p>c\\u00F3digo del dispositivo desde donde se realiza la operaci\\u00F3n, en caso de utilizarse la APP M\\u00F3VIL.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idDistribuidor",
            "description": "<p>Identificador del distribuidor a asociar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idJornada",
            "description": "<p>Identificador de la jornada a consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idBodegaVirtual",
            "description": "<p>Identificador de la bodega virtual asociada a la Jornada.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idVendedor",
            "description": "<p>Identificador del vendedor a consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "dispositivoJornada",
            "description": "<p>C\\u00F3digo del dispositivo a consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idTipo",
            "description": "<p>Identificador del tipo asociado a la Jornada.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "tipo",
            "defaultValue": "RUTA o PANEL",
            "description": "<p>Nombre del tipo asociado. Obligatorio \\u00FAnicamente en caso de enviar idTipo.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "estado",
            "defaultValue": "INICIADA o FINALIZADA",
            "description": "<p>Estado del cu\\u00E1l se desea obtener Jornadas.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "fechaDesde",
            "description": "<p>Fecha desde la cual se desea obtener informaci\\u00F3n de jornadas iniciadas en formato yyyyMMdd.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "fechaHasta",
            "description": "<p>Fecha final de la cual se desea obtener informaci\\u00F3n de jornadas iniciadas en formato yyyyMMdd.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "estadoLiquidacion",
            "defaultValue": "PENDIENTE, RECHAZADA o LIQUIDADA",
            "description": "<p>Estado del cu\\u00E1l se desea obtener Jornadas finalizadas.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "fechaFinDesde",
            "description": "<p>Fecha desde la cual se desea obtener informaci\\u00F3n de jornadas finalizadas en formato yyyyMMdd.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "fechaFinHasta",
            "description": "<p>Fecha final de la cual se desea obtener informaci\\u00F3n de jornadas finalizadas en formato yyyyMMdd.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "fechaLiqDesde",
            "description": "<p>Fecha desde la cual se desea obtener informaci\\u00F3n de jornadas liquidadas en formato yyyyMMdd.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "fechaLiqHasta",
            "description": "<p>Fecha final de la cual se desea obtener informaci\\u00F3n de jornadas liquidadas en formato yyyyMMdd.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "mostrarDetPago",
            "description": "<p>Filtro para mostrar detalle del Pago: 1. mostrarar detalla 0 no mostrara detalle.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "mostarObservacion",
            "description": "<p>Filtro para mostrar observaciones de liquidacion: 1. mostrarar detalla 0 no mostrara detalle.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"codArea\": \"505\",\n    \"token\": \"WEB\",\n    \"codDispositivo\": \"\",\n    \"usuario\": \"usuario.pruebas\",\n    \"codDispositivo\": \"64E1CE4A48BF656974C376B7905F7E62\",\n    \"idJornada\": \"\",\n    \"idDistribuidor\": \"5\",\n    \"idBodegaVirtual\": \"\",\n    \"idVendedor\": \"\",\n    \"dispositivoJornada\": \"\",\n    \"idTipo\": \"\",\n    \"tipo\": \"\",\n    \"estado\": \"\",\n    \"fechaDesde\": \"\",\n    \"fechaHasta\": \"\",\n    \"estadoLiquidacion\": \"\",\n    \"fechaFinDesde\": \"\",\n    \"fechaFinHasta\": \"\",\n    \"fechaLiqDesde\": \"\",\n    \"fechaLiqHasta\": \"\",\n    \"mostrarDetPago\":\"0\",\n    \"mostrarObservacion\":\"0\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"0\",\n    \"mostrar\": \"0\",\n    \"descripcion\": \"Recursos recuperados exitosamente.\"\n  },\n  \"jornadas\": {\n    \"idJornada\": \"62\",\n    \"idVendedor\": \"2322\",\n    \"nombreVendedor\": \"usuario sidra2\",\n    \"usuarioVendedor\": \"usuario.sidra2\",\n    \"codDispositivo\": \"00:11:22:33:44:55\",\n    \"idDistribuidor\": \"5\",\n    \"nombreDistribuidor\": \"DTS Uno\",\n    \"idTipo\": \"7\",\n    \"tipo\": \"RUTA\",\n    \"nombreTipo\": \"RUTA UNO\",\n    \"idBodegaVirtual\": \"28\",\n    \"nombreBodegaVirtual\": \"BODEGA VIRTUAL\",\n    \"idBodegaVendedor\": \"30\",\n    \"estado\": \"FINALIZADA\",\n    \"fecha\": \"16/06/2016 15:15:00\",\n    \"observaciones\": \"observaciones al iniciar jornada\",\n    \"fechaFinalizacion\": \"16/06/2016 17:11:43\",\n    \"estadoLiquidacion\": \"RECHAZADA\",\n    \"fechaLiquidacion\": \"16/06/2016 17:11:43\",\n    \"obsLiquidacion\": [\n      {\n        \"observacion\": \"Observacion 1\",\n        \"creado_el\": \"16/06/2016 17:36:01\",\n        \"creado_por\": \"victor.cifuentes\"\n      },\n      {\n        \"observacion\": \"Observacion 2\",\n        \"creado_el\": \"16/06/2016 17:36:18\",\n        \"creado_por\": \"victor.cifuentes\"\n      }\n    ],\n    \"creado_el\": \"16/06/2016 15:11:53\",\n    \"creado_por\": \"victor.cifuentes\",\n    \"modificado_el\": \"16/06/2016 17:12:00\",\n    \"modificado_por\": \"usuario.sidra\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "{} JSON Respuesta-Error:     ",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"-199\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"Los siguientes datos faltan o son incorrectos:  Usuario. ID Distribuidor.\",\n    \"clase\": \"CtrlJornada\",\n    \"metodo\": \"validarInput\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioConsulta.java",
    "groupTitle": "Jornada"
  },
  {
    "type": "POST",
    "url": "/consultasidra/getjornadamasiva/",
    "title": "[getJornadaMasiva]",
    "name": "getJornadaMasiva",
    "description": "<p>Servicio para obtener los datos de Jornadas registradas en Sidra por pa\\u00EDs.</p>",
    "group": "Jornada",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs en el que se desea realizar la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "token",
            "description": "<p>token de validaci\\u00F3n de autenticaci\\u00F3n de usuario.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codDispositivo",
            "description": "<p>c\\u00F3digo del dispositivo desde donde se realiza la operaci\\u00F3n, en caso de utilizarse la APP M\\u00F3VIL.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idDistribuidor",
            "description": "<p>Identificador del distribuidor a asociar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idJornada",
            "description": "<p>Identificador de la jornada a consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idJornadaResponsable",
            "description": "<p>Identificador de la jornada del responsable que se desea a consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idBodegaVirtual",
            "description": "<p>Identificador de la bodega virtual asociada a la Jornada.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idVendedor",
            "description": "<p>Identificador del vendedor a consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "dispositivoJornada",
            "description": "<p>C\\u00F3digo del dispositivo a consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idTipo",
            "description": "<p>Identificador del tipo asociado a la Jornada.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "tipo",
            "defaultValue": "RUTA o PANEL",
            "description": "<p>Nombre del tipo asociado. Obligatorio en caso de enviar idTipo.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "estado",
            "defaultValue": "INICIADA o FINALIZADA",
            "description": "<p>Estado del cu\\u00E1l se desea obtener Jornadas.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "fechaDesde",
            "description": "<p>Fecha desde la cual se desea obtener informaci\\u00F3n de jornadas iniciadas en formato yyyyMMdd.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "fechaHasta",
            "description": "<p>Fecha final de la cual se desea obtener informaci\\u00F3n de jornadas iniciadas en formato yyyyMMdd.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "estadoLiquidacion",
            "defaultValue": "PENDIENTE, RECHAZADA o LIQUIDADA",
            "description": "<p>Estado del cu\\u00E1l se desea obtener Jornadas finalizadas.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "fechaFinDesde",
            "description": "<p>Fecha desde la cual se desea obtener informaci\\u00F3n de jornadas finalizadas en formato yyyyMMdd.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "fechaFinHasta",
            "description": "<p>Fecha final de la cual se desea obtener informaci\\u00F3n de jornadas finalizadas en formato yyyyMMdd.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "fechaLiqDesde",
            "description": "<p>Fecha desde la cual se desea obtener informaci\\u00F3n de jornadas liquidadas en formato yyyyMMdd.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "fechaLiqHasta",
            "description": "<p>Fecha final de la cual se desea obtener informaci\\u00F3n de jornadas liquidadas en formato yyyyMMdd.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "envioAlarma",
            "defaultValue": "1 o 0",
            "description": "<p>Par\\u00E1metro para consultar jornadas que han enviado alarmas en el inicio de jornada.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "envioAlarmaFin",
            "defaultValue": "1 o 0",
            "description": "<p>Par\\u00E1metro para consultar jornadas que han enviado alarmas en el fin de jornada.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idDeuda",
            "description": "<p>Identificado de la deuda asociada.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "estadoPago",
            "defaultValue": "PENDIENTE o PAGADA",
            "description": "<p>Estado de pago de la jornada a consultar.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"codArea\": \"505\",\n    \"token\": \"WEB\",\n    \"codDispositivo\": \"\",\n    \"usuario\": \"usuario.pruebas\",\n    \"codDispositivo\": \"64E1CE4A48BF656974C376B7905F7E62\",\n    \"idJornada\": \"\",\n    \"idJornadaResponsable\": \"\",\n    \"idDistribuidor\": \"5\",\n    \"idBodegaVirtual\": \"\",\n    \"idVendedor\": \"\",\n    \"dispositivoJornada\": \"\",\n    \"idTipo\": \"\",\n    \"tipo\": \"\",\n    \"estado\": \"\",\n    \"fechaDesde\": \"\",\n    \"fechaHasta\": \"\",\n    \"estadoLiquidacion\": \"\",\n    \"fechaFinDesde\": \"\",\n    \"fechaFinHasta\": \"\",\n    \"fechaLiqDesde\": \"\",\n    \"fechaLiqHasta\": \"\",\n    \"envioAlarma\": \"1\",\n    \"envioAlarmaFin\": \"0\",\n    \"idDeuda\": \"\",\n    \"estadoPago\": \"\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"0\",\n    \"mostrar\": \"0\",\n    \"descripcion\": \"Recursos recuperados exitosamente.\"\n  },\n  \"jornadas\": [{\n    \"idJornada\": \"62\",\n    \"idJornadaResponsable\": \"61\",\n    \"idVendedor\": \"2322\",\n    \"nombreVendedor\": \"usuario sidra2\",\n    \"usuarioVendedor\": \"usuario.sidra2\",\n    \"codDispositivo\": \"2669305997BF98946B3B1DF77862936E1620E01E\",\n    \"idDistribuidor\": \"5\",\n    \"nombreDistribuidor\": \"DTS Uno\",\n    \"idTipo\": \"7\",\n    \"tipo\": \"RUTA\",\n    \"nombreTipo\": \"RUTA UNO\",\n    \"idBodegaVirtual\": \"28\",\n    \"nombreBodegaVirtual\": \"BODEGA VIRTUAL\",\n    \"idBodegaVendedor\": \"30\",\n    \"saldoInicial\": \"2000\"\n    \"estado\": \"FINALIZADA\",\n    \"envioAlarma\": \"1\",\n    \"tipoAlarma\": \"Jornada en fecha festiva.\",\n    \"envioAlarmaFin\": \"0\",\n    \"tipoAlarmaFin\": \"\",\n    \"fecha\": \"16/06/2016 15:15:00\",\n    \"observaciones\": \"observaciones al iniciar jornada\",\n    \"fechaFinalizacion\": \"16/06/2016 17:11:43\",\n    \"codDispositivoFinalizacion\": \"2669305997BF98946B3B1DF77862936E1620E01E\",\n    \"estadoLiquidacion\": \"RECHAZADA\",\n    \"fechaLiquidacion\": \"16/06/2016 17:11:43\",\n    \"detallePagos\": [\n      {\n        \"formaPago\": \"TARJETA\",\n        \"monto\": \"10.1\"\n      },\n      {\n        \"formaPago\": \"REMESAS\",\n        \"monto\": \"20.2\"\n      },\n      {\n        \"formaPago\": \"EFECTIVO\",\n        \"monto\": \"27907.5\"\n      }\n    ],\n    \"idDeuda\": \"\",\n    \"estadoPago\": \"\",\n    \"obsLiquidacion\": [\n      {\n        \"observacion\": \"Observacion 1\",\n        \"creado_el\": \"16/06/2016 17:36:01\",\n        \"creado_por\": \"victor.cifuentes\"\n      },\n      {\n        \"observacion\": \"Observacion 2\",\n        \"creado_el\": \"16/06/2016 17:36:18\",\n        \"creado_por\": \"victor.cifuentes\"\n      }\n    ],\n    \"creado_el\": \"16/06/2016 15:11:53\",\n    \"creado_por\": \"victor.cifuentes\",\n    \"modificado_el\": \"16/06/2016 17:12:00\",\n    \"modificado_por\": \"usuario.sidra\"\n  }]\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "{} JSON Respuesta-Error:     ",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"-199\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"Los siguientes datos faltan o son incorrectos:  Usuario. ID Distribuidor.\",\n    \"clase\": \"CtrlJornada\",\n    \"metodo\": \"validarInput\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioConsulta.java",
    "groupTitle": "Jornada"
  },
  {
    "type": "POST",
    "url": "/consultasidra/getfechajornada/",
    "title": "[getfechajornada]",
    "name": "getfechajornada",
    "description": "<p>Servicio para obtener la fecha de cierre de la jornada de un vendedor.</p>",
    "group": "Jornada",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs en el que se desea realizar la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idVendedor",
            "description": "<p>Identificador del vendedor que se desea consultar.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"codArea\":\"505\",\n    \"usuario\":\"victor.cifuentes\",\n    \"idVendedor\":\"\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"72\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"OK. Fecha de cierre obtenida correctamente.\",\n    \"clase\": \"OperacionJornada\",\n    \"metodo\": \"getFechaJornada\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"\"\n  },\n  \"jornada\": {\n    \"idVendedor\": \"2564\",\n    \"fechaCierre\": \"30122016\",\n    \"creado_el\": \"29/12/2016 15:08:48\",\n    \"creado_por\": \"victor.cifuentes\",\n    \"modificado_el\": \"29/12/2016 15:10:19\",\n    \"modificado_por\": \"victor.cifuentes\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "{} JSON Respuesta-Error:",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"-199\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"Los siguientes datos faltan o son incorrectos:  ID Vendedor.\",\n    \"clase\": \"CtrlJornada\",\n    \"metodo\": \"validarInput\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioConsulta.java",
    "groupTitle": "Jornada"
  },
  {
    "type": "POST",
    "url": "/opsidra/modjornada/",
    "title": "[modjornada]",
    "name": "modjornada",
    "description": "<p>Servicio para modificar Jornadas por pa\\u00EDs.</p>",
    "group": "Jornada",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs en el que se desea realizar la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "token",
            "description": "<p>token de validaci\\u00F3n de autenticaci\\u00F3n de usuario.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idVendedor",
            "description": "<p>Identificador del vendedor asociado.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idJornada",
            "description": "<p>Identificador de la jornada a modificar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "tipoOperacion",
            "defaultValue": "RECHAZO o LIQUIDACION",
            "description": "<p>Nombre del tipo de operaci\\u00F3n a aplicar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "observaciones",
            "description": "<p>Observaciones de la operacion de la Jornada.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"codArea\": \"505\",\n    \"token\": \"WEB\",\n    \"usuario\": \"usuario.pruebas\",\n    \"idVendedor\": \"2322\",\n    \"idJornada\": \"63\",\n    \"tipoOperacion\": \"LIQUIDACION\",\n    \"observaciones\": \"\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"201\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"Recurso modificado exitosamente.\",\n    \"clase\": \"OperacionJornada\",\n    \"metodo\": \"doPut\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"-368\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"La jornada no se encuentra en estado FINALIZADA.\",\n    \"clase\": \"OperacionJornada\",\n    \"metodo\": \"doPut\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioOperacion.java",
    "groupTitle": "Jornada"
  },
  {
    "type": "POST",
    "url": "/opsidra/modjornadamasiva/",
    "title": "[modjornadamasiva]",
    "name": "modjornadamasiva",
    "description": "<p>Servicio para modificar Jornadas por pa\\u00EDs.</p>",
    "group": "Jornada",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs en el que se desea realizar la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "token",
            "description": "<p>token de validaci\\u00F3n de autenticaci\\u00F3n de usuario.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idDistribuidor",
            "description": "<p>Identificador del distribuidor asociado.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idResponsable",
            "description": "<p>Identificador del vendedor responsable de la panel o ruta.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idJornada",
            "description": "<p>Identificador de la jornada a modificar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codDispositivo",
            "description": "<p>C\\u00F3digo del dispositivo asociado.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "observaciones",
            "description": "<p>Observaciones de la operacion de la Jornada.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"token\": \"WEB\",\n    \"usuario\": \"usuario.pruebas\",\n    \"codArea\": \"505\",\n    \"idDistribuidor\": \"17\",\n    \"idResponsable\": \"462\",\n    \"idJornada\": \"55\",\n    \"codDispositivo\": \"ASDF123QWER456ZXCV789\",\n    \"observaciones\": \"\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"201\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"Recurso modificado exitosamente.\",\n    \"clase\": \"OperacionJornadaMasiva\",\n    \"metodo\": \"doPut\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"\"\n  },\n  \"dispositivos\": [\n     {\n         \"codigoDispositivo\": \"64E1CE4A48BF656974C376B7905F7E62\"\n     }\n  ]\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"-199\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"Los siguientes datos faltan o son incorrectos:  C\\u00F3digo de Dispositivo.\",\n    \"clase\": \"CtrlJornadaMasiva\",\n    \"metodo\": \"validarInput\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioOperacion.java",
    "groupTitle": "Jornada"
  },
  {
    "type": "POST",
    "url": "/opsidra/login/",
    "title": "[login]",
    "name": "login",
    "description": "<p>Servicio para inicio de sesi\\u00F3n de app m\\u00F3vil.</p>",
    "group": "LOGIN",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo del pa\\u00EDs al que pertenece el usuario que inicia sesi\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que inicia sesion.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "password",
            "description": "<p>Contrase\\u00F1a del usuario.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codDispositivo",
            "description": "<p>C\\u00F3digo del dispositivo desde donde se inicia sesi\\u00F3n.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n   \"codArea\":\"505\",\n   \"usuario\": \"usuario\",\n   \"password\": \"1232\",\n   \"codDispositivo\": \"64E1CE4A48BF656974C376B7905F7E62\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n    \"respuesta\": {\n       \"codResultado\": \"4\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"OK. Usuario logueado correctamente, pero no se encuentra asociado a una Panel o Ruta\",\n       \"clase\": \" \",\n       \"metodo\": \" \",\n       \"excepcion\": \" \",\n       \"tipoExepcion\": \"\"\n    },\n    \"token\": \"FB116A9905314D410B56C2A8BC21F4902D54041634564CC41063560083B0E790\",\n    \"idVendedor\": \"1823\",\n    \"responsable\":\"1\",\n    \"idResponsable\":\"1823\",\n    \"idBodegaVirtual\": \"\",\n    \"idBodegaVendedor\": \"\",\n    \"idDTS\": \"\",\n    \"nombreDistribuidor\":\"\",\n    \"tipo\": \"\"\n    \"idTipo\": \"97\",\n    \"folioManual\": \"1\",\n    \"longitud\": \" \",\n\t  \"latitud\": \" \",\n\t  \"nombreTipo\": \"RUTA TECH S.A.\",\n    \"numRecarga\": \"63257899\",\n    \"fechaCierre\": \"ddMMyyyy\",\n    \"numConvenio\": \"5452\",\n    \"tasaCambio\": \"20.00\",\n    \"vendedorAsignado\": \"4510\",\n    \"pin\": \"512\",\n    \"nivelBuzon\": \"2\",\n   \"numIdentificacion\": \"34543534534534\",\n   \"tipoIdentificacion\": \"RUC\",\n   \"numTelefono\": \"88005544\",\n   \"idDispositivo\": \"236\"\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n   \"respuesta\": {\n       \"codResultado\": \"-101\",\n       \"mostrar\": \"0\",\n       \"descripcion\": \"Ocurrio un Problema inesperado, contacte a su Administrador.\",\n       \"clase\": \" \", \n       \"metodo\": \"\",\n       \"excepcion\": \"\",\n       \"tipoExepcion\": \"\"\n   }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioOperacion.java",
    "groupTitle": "LOGIN"
  },
  {
    "type": "POST",
    "url": "/opsidra/crearlog/",
    "title": "[crearlog]",
    "name": "crearlog",
    "description": "<p>Servicio para crear registros de log.</p>",
    "group": "Log",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>c\\u00F3digo de \\u00E1rea del pa\\u00EDs donde se desea realizar la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operacion.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "log",
            "description": "<p>Listado de logs a insertar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "log.tipoTransaccion",
            "description": "<p>Nombre del tipo de transacci\\u00F3n del log.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "log.origen",
            "description": "<p>Origen del log.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "log.id",
            "description": "<p>Identificador del tipo.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "log.tipoId",
            "description": "<p>Tipo de Id que se insertar\\u00E1 en el log.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "log.resultado",
            "description": "<p>Resultado a ingresar en el log.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "log.descripcionError",
            "description": "<p>Error que se desea insertar en el log.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"codArea\": \"505\",\n    \"usuario\": \"usuario\",\n    \"log\" :[\n        { \n            \"tipoTransaccion\": \"CREAR_SOLICITUD_WF\",\n            \"origen\": \"WS CREA SOLICITUD\",\n            \"id\": \"10\",\n            \"tipoId\": \"solicitud\",\n            \"resultado\": \"OK. CREACION EXITOSA\",\n            \"descripcionError\": \"\"\n        }\n    ]\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n   \"idBuzon\": \"1\",\n   \"respuesta\": {\n       \"codResultado\": \"11\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"Ok. Buz\\u00F3n modificado exitosamente.\",\n       \"clase\": \" \",\n       \"metodo\": \" \",\n       \"excepcion\": \" \",\n       \"tipoExepcion\": \"\"\n   }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n    \"respuesta\": {\n        \"codResultado\": \"-70\",\n        \"mostrar\": \"1\",\n        \"descripcion\": \"El par\\u00E1metro de entrada \\\\\\\"numTelefono\\\"\\\\ esta vac\\u00EDo.\",\n        \"clase\": \" \",\n        \"metodo\": \"validarDatos\",\n        \"excepcion\": \"class CtrlDispositivo\",\n        \"tipoExepcion\": \"Generales\"\n    }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioOperacion.java",
    "groupTitle": "Log"
  },
  {
    "type": "POST",
    "url": "/opsidra/asignapromocampania/",
    "title": "[asignapromocampania]",
    "name": "asignapromocampania",
    "description": "<p>Servicio para asignar Art\\u00EDculos Promocionales a Campa\\u00F1as creadas.</p>",
    "group": "OfertaCampania",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs en el que se desea realizar la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idOfertaCampania",
            "description": "<p>Identificador de la Campa\\u00F1a a la que se asociar\\u00E1n los art\\u00EDculos.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "ofertaCampaniaDet",
            "description": "<p>Arreglo con el listado de art\\u00EDculos a asociar con la Oferta o Campa\\u00F1a.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "ofertaCampaniaDet.idArtPromocional",
            "description": "<p>Identificador del art\\u00EDculo promocional a asociar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "ofertaCampaniaDet.cantArticulos",
            "description": "<p>Cantidad de art\\u00EDculos promocionales a otorgar con la Campa\\u00F1a.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"codArea\": \"505\",\n    \"usuario\": \"usuario.prueba\",\n    \"idOfertaCampania\": \"1\",\n    \"ofertaCampaniaDet\": [\n        {\n            \"idArtPromocional\": \"1\",\n            \"cantArticulos\": \"2\"\n        },\n        {\n            \"idArtPromocional\": \"2\",\n            \"cantArticulos\": \"1\"\n        }\n    ]\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n  \"idPromoOfertaCampania\": \"1\",\n  \"respuesta\": {\n    \"codResultado\": \"200\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"Campos agregados exitosamente.\",\n    \"clase\": \"OperacionPromoOfertaCampania\",\n    \"metodo\": \"doPost\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"-199\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"Los siguientes datos faltan o son incorrectos:  Datos no num\\u00E9ricos en ID OfertaCampania.\",\n    \"clase\": \"CtrlPromoOfertaCampania\",\n    \"metodo\": \"validarInput\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioOperacion.java",
    "groupTitle": "OfertaCampania"
  },
  {
    "type": "POST",
    "url": "/opsidra/creaofertacampania/",
    "title": "[creaofertacampania]",
    "name": "creaofertacampania",
    "description": "<p>Servicio para crear Ofertas o Campa\\u00F1as por pa\\u00EDs.</p>",
    "group": "OfertaCampania",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs en el que se desea realizar la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "tipo",
            "description": "<p>Nombre del tipo de Oferta/Campa\\u00F1a que se desea, puede ser OFERTA o CAMPA\\u00D1A.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "nombre",
            "description": "<p>Nombre de la Oferta o Campa\\u00F1a a crear.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "descripcion",
            "description": "<p>Descripci\\u00F3n de la Oferta o Campa\\u00F1a a crear.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "cantMaxPromocionales",
            "description": "<p>Valor m\\u00E1ximo de pormocionales a brindar en la Campa\\u00F1a. Este campo es innecesario si el Tipo de Oferta/Campa\\u00F1a es OFERTA.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "fechaDesde",
            "description": "<p>Fecha en formato dd/MM/aaaa desde la que aplica la Oferta o Campa\\u00F1a.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "fechaHasta",
            "description": "<p>Fecha en formato dd/MM/aaaa hasta la que aplica la Oferta o Campa\\u00F1a.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "ofertaCampaniaDet",
            "description": "<p>Arreglo con el listado de detalles a asociar con la Oferta o Campa\\u00F1a.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "ofertaCampaniaDet.idTipo",
            "description": "<p>Identificador del tipo de detalle a asociar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "ofertaCampaniaDet.tipo",
            "description": "<p>Tipo del detalle a asociar, puede ser PANEL o RUTA.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"codArea\": \"505\",\n    \"usuario\": \"usuario.prueba\",\n    \"tipo\": \"CAMPA\\u00D1A\",\n    \"nombre\": \"Oferta 1\",\n    \"descripcion\": \"Desc Oferta 1\",\n    \"cantMaxPromocionales\": \"5\",\n    \"fechaDesde\": \"30/10/2015\",\n    \"fechaHasta\": \"30/11/2015\",\n    \"ofertaCampaniaDet\": [\n        {\n            \"idTipo\": \"1\",\n            \"tipo\": \"PANEL\"\n        },\n        {\n            \"idTipo\": \"2\",\n            \"tipo\": \"PANEL\"\n        }\n    ]\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n  \"idOfertaCampania\": \"1\",\n  \"respuesta\": {\n    \"codResultado\": \"200\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"Campos agregados exitosamente.\",\n    \"clase\": \"OperacionOfertaCampania\",\n    \"metodo\": \"doPost\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"-199\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"Los siguientes datos faltan o son incorrectos: Fecha Desde debe ser igual o mayor a la Fecha Actual.\",\n    \"clase\": \"CtrlOfertaCampania\",\n    \"metodo\": \"validarInput\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioOperacion.java",
    "groupTitle": "OfertaCampania"
  },
  {
    "type": "POST",
    "url": "/consultasidra/getofertacampania/",
    "title": "[getOfertaCampania]",
    "name": "getOfertaCampania",
    "description": "<p>Servicio para obtener los datos de Ofertas o Campa\\u00F1as y sus detalles.</p>",
    "group": "OfertaCampania",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pais.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operacion.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idOfertaCampania",
            "description": "<p>Identificador de la Oferta o Campa\\u00F1a que se desea obtener. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "tipo",
            "description": "<p>Nombre del tipo de Oferta o Campa\\u00F1a que se desea, puede ser OFERTA o CAMPA\\u00F1A. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "nombre",
            "description": "<p>Nombre de la Oferta o Campa\\u00F1a a buscar. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "fechaDesde",
            "description": "<p>Fecha en formato aaaaMMdd desde la que aplica la Oferta o Campa\\u00F1a. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "fechaHasta",
            "description": "<p>Fecha en formato aaaaMMdd hasta la que aplica la Oferta o Campa\\u00F1a. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "estado",
            "description": "<p>Estado del cu\\u00E1l se desea obtener Ofertas o Campa\\u00F1as (ALTA o BAJA). Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idRuta",
            "description": "<p>Identificador de la ruta a la que se le aplica la Oferta o Campa\\u00F1a. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idPanel",
            "description": "<p>Identificador de la panel a la que se le aplica la Oferta o Campa\\u00F1a. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idDTS",
            "description": "<p>Identificador del Distribuidot asociado a una panel o ruta a la que se le aplica una Oferta o Campa\\u00F1a. Este campo es opcional.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"codArea\": \"505\",\n    \"usuario\": \"usuario.prueba\",\n    \"idOfertaCampania\": \"\",\n    \"tipo\": \"\",\n    \"nombre\": \"\",\n    \"fechaDesde\": \"\",\n    \"fechaHasta\": \"\",\n    \"estado\": \"\",\n    \"idRuta\": \"\",\n    \"idPanel\": \"\",\n    \"idDTS\": \"\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"0\",\n    \"mostrar\": \"0\",\n    \"descripcion\": \"Recursos recuperados exitosamente.\"\n  },\n  \"ofertaCampania\": {\n    \"idOfertaCampania\": \"3\",\n    \"tipo\": \"CAMPA\\u00D1A\",\n    \"nombre\": \"Oferta 1\",\n    \"descripcion\": \"Desc Oferta 1\",\n    \"cantMaxPromocionales\": \"5\",\n    \"fechaDesde\": \"25/02/2016\",\n    \"fechaHasta\": \"25/02/2016\",\n    \"estado\": \"ALTA\",\n    \"creado_el\": \"25/02/2016 16:38:58\",\n    \"creado_por\": \"usuario.prueba\",\n    \"modificado_el\": \"25/02/2016 16:43:11\",\n    \"modificado_por\": \"usuario.prueba\",\n    \"ofertaCampaniaDet\": [\n      {\n        \"idTipo\": \"1\",\n        \"tipo\": \"PANEL\",\n        \"nombreTipo\": \"PRUEBAS UNO\",\n        \"idDTS\": \"7\",\n        \"nombreDTS\": \"DTS UNO\",\n        \"estado\": \"ALTA\",\n        \"creado_el\": \"25/02/2016 16:43:12\",\n        \"creado_por\": \"usuario.prueba\"\n      },\n      {\n        \"idTipo\": \"2\",\n        \"tipo\": \"PANEL\",\n        \"nombreTipo\": \"PRUEBAS DOS\",\n        \"idDTS\": \"7\",\n        \"nombreDTS\": \"DTS UNO\",\n        \"estado\": \"ALTA\",\n        \"creado_el\": \"25/02/2016 16:43:12\",\n        \"creado_por\": \"usuario.prueba\"\n      }\n    ]\n  }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n   \"respuesta\": {\n       \"codResultado\": \"-205\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"No existen registros con esos par\\u00E1metros.\",\n       \"clase\": \"OperacionOfertaCampania\",\n       \"metodo\": \"doGet\",\n       \"excepcion\": \" \",\n       \"tipoExepcion\": \"Generales\"\n   }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioConsulta.java",
    "groupTitle": "OfertaCampania"
  },
  {
    "type": "POST",
    "url": "/consultasidra/getofertacampaniamovil/",
    "title": "[getOfertaCampaniaMovil]",
    "name": "getOfertaCampaniaMovil",
    "description": "<p>Servicio para obtener los datos de Ofertas o Campa\\u00F1as y los art\\u00EDculos promocionales asociados.</p>",
    "group": "OfertaCampania",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pais.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codDispositivo",
            "description": "<p>c\\u00F3digo del dispositivo desde donde se realiza la operaci\\u00F3n, en caso de utilizarse la APP M\\u00F3VIL.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idOfertaCampania",
            "description": "<p>Identificador de la Oferta o Campa\\u00F1a que se desea obtener. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "tipo",
            "description": "<p>Nombre del tipo de Oferta o Campa\\u00F1a que se desea, puede ser OFERTA o CAMPA\\u00F1A. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "nombre",
            "description": "<p>Nombre de la Oferta o Campa\\u00F1a a buscar. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "fechaDesde",
            "description": "<p>Fecha en formato aaaaMMdd desde la que aplica la Oferta o Campa\\u00F1a. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "fechaHasta",
            "description": "<p>Fecha en formato aaaaMMdd hasta la que aplica la Oferta o Campa\\u00F1a. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "estado",
            "description": "<p>Estado del cu\\u00E1l se desea obtener Ofertas o Campa\\u00F1as (ALTA o BAJA). Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idRuta",
            "description": "<p>Identificador de la ruta a la que se le aplica la Oferta o Campa\\u00F1a. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idPanel",
            "description": "<p>Identificador de la panel a la que se le aplica la Oferta o Campa\\u00F1a. Este campo es opcional.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"codArea\": \"505\",\n    \"usuario\": \"usuario.prueba\",\n    \"codDispositivo\": \"64E1CE4A48BF656974C376B7905F7E62\",\n    \"idOfertaCampania\": \"\",\n    \"tipo\": \"\",\n    \"nombre\": \"\",\n    \"fechaDesde\": \"\",\n    \"fechaHasta\": \"\",\n    \"estado\": \"\",\n    \"idRuta\": \"\",\n    \"idPanel\": \"\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"0\",\n    \"mostrar\": \"0\",\n    \"descripcion\": \"Recursos recuperados exitosamente.\"\n  },\n  \"ofertaCampania\": {\n    \"idOfertaCampania\": \"3\",\n    \"tipo\": \"CAMPA\\u00D1A\",\n    \"nombre\": \"Oferta 1\",\n    \"descripcion\": \"Desc Oferta 1\",\n    \"cantMaxPromocionales\": \"5\",\n    \"fechaDesde\": \"25/02/2016 00:00:00\",\n    \"fechaHasta\": \"25/02/2016 00:00:00\",\n    \"estado\": \"ALTA\",\n    \"creado_el\": \"25/02/2016 16:38:58\",\n    \"creado_por\": \"usuario.prueba\",\n    \"modificado_el\": \"25/02/2016 16:43:11\",\n    \"modificado_por\": \"usuario.prueba\",\n    \"articulosPromocionales\": [\n      {\n        \"idPromoOfertaCampania\": \"1\",\n        \"idOfertaCampania\": \"4\",\n        \"idArtPromocional\": \"51\",\n        \"nombreArticulo\": \"PLAYERA M\",\n        \"tipoInv\": \"INV_SIDRA\",\n        \"cantArticulos\": \"2\",\n        \"estado\": \"ALTA\",\n        \"creado_el\": \"29/02/2016 15:36:27\",\n        \"creado_por\": \"usuario.prueba\"\n      },\n      {\n        \"idPromoOfertaCampania\": \"2\",\n        \"idOfertaCampania\": \"4\",\n        \"idArtPromocional\": \"52\",\n        \"nombreArticulo\": \"PLAYERA M\",\n        \"tipoInv\": \"INV_SIDRA\",\n        \"cantArticulos\": \"1\",\n        \"estado\": \"ALTA\",\n        \"creado_el\": \"29/02/2016 15:36:27\",\n        \"creado_por\": \"usuario.prueba\"\n      }\n    ]\n  }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n   \"respuesta\": {\n       \"codResultado\": \"-390\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"No se encontraron campa\\u00F1as configuradas.\",\n       \"clase\": \"CtrlOfertaCampaniaMovil\",\n       \"metodo\": \"doGet\",\n       \"excepcion\": \" \",\n       \"tipoExepcion\": \"Generales\"\n   }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioConsulta.java",
    "groupTitle": "OfertaCampania"
  },
  {
    "type": "POST",
    "url": "/consultasidra/getpromocampania/",
    "title": "[getPromoCampania]",
    "name": "getPromoCampania",
    "description": "<p>Servicio para obtener los datos de Ofertas o Campa\\u00F1as y sus detalles.</p>",
    "group": "OfertaCampania",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pais.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idPromoCampania",
            "description": "<p>Identificador de la asignaci\\u00F3n de Promocional a Campa\\u00F1a que se desea obtener. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idOfertaCampania",
            "description": "<p>Identificador de la Campa\\u00F1a que se desea obtener. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idArtPromocional",
            "description": "<p>Identificador del Art\\u00EDculo Promocional que se desea obtener. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "estado",
            "description": "<p>Estado del cu\\u00E1l se desea consultar (ALTA o BAJA). Este campo es opcional.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"codArea\": \"505\",\n    \"usuario\": \"usuario.prueba\",\n    \"idPromoCampania\": \"\",\n    \"idOfertaCampania\": \"\",\n    \"idArtPromocional\": \"\",\n    \"estado\": \"\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"0\",\n    \"mostrar\": \"0\",\n    \"descripcion\": \"Recursos recuperados exitosamente.\"\n  },\n  \"campanias\": {\n    \"idOfertaCampania\": \"4\",\n    \"nombreCampania\": \"Oferta 1\",\n    \"articulosPromocionales\": [\n      {\n        \"idPromoCampania\": \"1\",\n        \"idOfertaCampania\": \"4\",\n        \"idArtPromocional\": \"51\",\n        \"nombreArticulo\": \"PLAYERA M\",\n        \"cantArticulos\": \"2\",\n        \"estado\": \"ALTA\",\n        \"creado_el\": \"29/02/2016 15:36:27\",\n        \"creado_por\": \"usuario.prueba\"\n      },\n      {\n        \"idPromoCampania\": \"2\",\n        \"idOfertaCampania\": \"4\",\n        \"idArtPromocional\": \"52\",\n        \"nombreArticulo\": \"PLAYERA S\",\n        \"cantArticulos\": \"1\",\n        \"estado\": \"ALTA\",\n        \"creado_el\": \"29/02/2016 15:36:27\",\n        \"creado_por\": \"usuario.prueba\"\n      }\n    ]\n  }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"-205\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"No existen registros con esos par\\u00E1metros.\",\n    \"clase\": \"OperacionPromoOfertaCampania\",\n    \"metodo\": \"doGet\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioConsulta.java",
    "groupTitle": "OfertaCampania"
  },
  {
    "type": "POST",
    "url": "/consultasidra/getodescuentofs/",
    "title": "[getodescuentofs]",
    "name": "getodescuentofs",
    "description": "<p>Servicio para obtener descuentos desde fullstack,</p>",
    "group": "OfertaCampania",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs en el que se desea realizar la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idProductOffering",
            "description": "<p>Identificador de la oferta de la que se desea obtener sus descuentos. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idDescuento",
            "description": "<p>Identificador del descuento del que se desea obtener informaci\\u00F3n. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "nombreDescuento",
            "description": "<p>Nombre del descuento del que se desea obtener informaci\\u00F3n. Este campo es opcional.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo parametros de Entrada:",
          "content": "{\n\t\"codArea\": \"505\",\n\t\"usuario\": \"usuario.pruebas\",\n\t\"idProductOffering\": \"9145627885465644616\",\n\t\"idDescuento\": \"\",\n\t\"nombreDescuento\":\"\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "{} JSON Respuesta-\\u00E9xito:",
          "content": "{\n\t\t\"respuesta\": {\n  \t\t\"codResultado\": \"12\",\n \t\t\"mostrar\": \"0\",\n\t\t\t\"descripcion\": \"OK. Datos obtenidos exitosamente\",\n\t\t\t\"clase\": \" \",\n\t\t\t\"metodo\": \" \",\n\t\t\t\"excepcion\": \" \",\n  \t\t\"tipoExepcion\": \"\"\n},\n\"listaDescuentos\": {\n  \"idProductOffering\": \"9145627885465644616\",\n  \"nombre\": \"Kit: Mobile Phone NEXUS GO MOBILE NX-175 AZUL NEGRO\",\n  \"descuentos\": [\n      {\n          \"idDescuento\": \"9145982480265567119\",\n         \"nombreDescuento\": \"Precio Kit prepago Cadenas (retail) 15% descuento\",\n          \"montoDescuento\": \"15\",\n          \"creadoEl\": \"13/09/2017 09:44:17\",\n          \"creadoPor\": \"PROC_SINC_OFERTA\",\n          \"modificadoEl\": \"\",\n          \"modificadoPor\": \"\",\n          \"tipoDescuento\": \"ES_%\"\n      },\n      {\n          \"idDescuento\": \"9145982480265567134\",\n          \"nombreDescuento\": \"Precio Kit prepago Cadenas (retail) 20% descuento\",\n          \"montoDescuento\": \"20\",\n          \"creadoEl\": \"13/09/2017 09:44:18\",\n          \"creadoPor\": \"PROC_SINC_OFERTA\",\n          \"modificadoEl\": \"\",\n          \"modificadoPor\": \"\",\n          \"tipoDescuento\": \"ES_%\"\n      },\n      {\n          \"idDescuento\": \"9145982480265567125\",\n          \"nombreDescuento\": \"Precio Kit prepago Cadenas (retail) 17% descuento\",\n          \"montoDescuento\": \"17\",\n          \"creadoEl\": \"13/09/2017 09:44:18\",\n          \"creadoPor\": \"PROC_SINC_OFERTA\",\n          \"modificadoEl\": \"\",\n          \"modificadoPor\": \"\",\n          \"tipoDescuento\": \"ES_%\"\n      },\n      {\n          \"idDescuento\": \"9145982480265567122\",\n          \"nombreDescuento\": \"Precio Kit prepago Cadenas (retail) 16% descuento\",\n          \"montoDescuento\": \"16\",\n          \"creadoEl\": \"13/09/2017 09:44:17\",\n          \"creadoPor\": \"PROC_SINC_OFERTA\",\n          \"modificadoEl\": \"\",\n          \"modificadoPor\": \"\",\n          \"tipoDescuento\": \"ES_%\"\n      },\n      {\n          \"idDescuento\": \"9145982480265567131\",\n          \"nombreDescuento\": \"Precio Kit prepago Cadenas (retail) 19% descuento\",\n          \"montoDescuento\": \"19\",\n          \"creadoEl\": \"13/09/2017 09:44:18\",\n          \"creadoPor\": \"PROC_SINC_OFERTA\",\n          \"modificadoEl\": \"\",\n          \"modificadoPor\": \"\",\n          \"tipoDescuento\": \"ES_%\"\n      },\n      {\n          \"idDescuento\": \"9145982480265567128\",\n          \"nombreDescuento\": \"Precio Kit prepago Cadenas (retail) 18% descuento\",\n          \"montoDescuento\": \"18\",\n          \"creadoEl\": \"13/09/2017 09:44:18\",\n          \"creadoPor\": \"PROC_SINC_OFERTA\",\n          \"modificadoEl\": \"\",\n          \"modificadoPor\": \"\",\n          \"tipoDescuento\": \"ES_%\"\n      }\n  ]\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "{} JSON Respuesta-Error:     ",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"-643\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"No se encontraron datos.\",\n    \"clase\": \"OperacionDescuentoFS\",\n    \"metodo\": \"doGet\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioConsulta.java",
    "groupTitle": "OfertaCampania"
  },
  {
    "type": "POST",
    "url": "/consultasidra/getofertafs",
    "title": "[getofertafs]",
    "name": "getofertafs",
    "description": "<p>Servicio para obtener ofertas desde fullstack,</p>",
    "group": "OfertaCampania",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs en el que se desea realizar la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "tipoArticulo",
            "defaultValue": "KIT, RECARGA, SIMCARD, TARJETARASCA, PROMOCIONAL",
            "description": "<p>Tipo de articulo del que se desea obtner oferta.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "tipoGestion",
            "defaultValue": "ALTA PREPAGO, PORTABILIDAD y MANTENIMIENTO NUMERO",
            "description": "<p>Tipo de gestion del que se desea tener ofertas .</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "nombre",
            "description": "<p>Nombre de la oferta que desea obtener.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo parametros de Entrada:",
          "content": "{\n\t\"codArea\": \"505\",\n\t\"usuario\": \"usuario.pruebas\",\n\t\"tipoArticulo\": \"SIMCARD\",\n\t\"tipoGestion\": \"\",\n\t\"nombre\":\"\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "{} JSON Respuesta-\\u00E9xito:",
          "content": "{\n\t\t\"respuesta\": {\n  \t\t\"codResultado\": \"12\",\n \t\t\"mostrar\": \"0\",\n\t\t\t\"descripcion\": \"OK. Datos obtenidos exitosamente\",\n\t\t\t\"clase\": \" \",\n\t\t\t\"metodo\": \" \",\n\t\t\t\"excepcion\": \" \",\n  \t\t\"tipoExepcion\": \"\"\n},\n\"oferta\": [\n  {\n      \"idProductOffering\": \"9145627885465644670\",\n      \"nombre\": \"Chip Movistar prepago (blister y sobre) PREACTIVA\",\n      \"precio\": \"1.69\",\n      \"precioMin\": \"\",\n      \"precioMax\": \"\"\n  },\n  {\n      \"idProductOffering\": \"9148633510165834045\",\n      \"nombre\": \"Chip Movistar prepago (Mantenimiento) PREACTIVA\",\n      \"precio\": \"1.27\",\n      \"precioMin\": \"\",\n      \"precioMax\": \"\"\n  },\n  {\n      \"idProductOffering\": \"9148633510165834033\",\n      \"nombre\": \"Chip Movistar prepago (PDV) PREACTIVA\",\n      \"precio\": \"0.21\",\n     \"precioMin\": \"\",\n      \"precioMax\": \"\"\n  },\n  {\n      \"idProductOffering\": \"9148633510165834039\",\n      \"nombre\": \"Chip Movistar prepago (PDV) PREACTIVA - 30 SIMCARD\",\n      \"precio\": \"0\",\n      \"precioMin\": \"\",\n      \"precioMax\": \"\"\n  },\n  {\n      \"idProductOffering\": \"9145627885465644677\",\n      \"nombre\": \"Chip Movistar prepago portabilidad PREACTIVA\",\n      \"precio\": \"1.69\",\n      \"precioMin\": \"\",\n      \"precioMax\": \"\"\n  },\n  {\n      \"idProductOffering\": \"9148633510165834027\",\n      \"nombre\": \"SIDRA: Chip Movistar prepago (blister y sobre) PREACTIVA\",\n      \"precio\": \"1.69\",\n      \"precioMin\": \"\",\n      \"precioMax\": \"\"\n  },\n  {\n      \"idProductOffering\": \"9148633510165834021\",\n      \"nombre\": \"SIDRA: Chip Movistar prepago portabilidad PREACTIVA\",\n      \"precio\": \"0.01\",\n      \"precioMin\": \"\",\n     \"precioMax\": \"\"\n  },\n  {\n      \"idProductOffering\": \"9145627885465644684\",\n      \"nombre\": \"Chip Movistar prepago (blister y sobre) LIBRE\",\n      \"precio\": \"1.69\",\n      \"precioMin\": \"\",\n      \"precioMax\": \"\"\n  }\n],",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "{} JSON Respuesta-Error:     ",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"-643\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"No se encontraron datos.\",\n    \"clase\": \"OperacionOfertaFS\",\n    \"metodo\": \"doGet\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioConsulta.java",
    "groupTitle": "OfertaCampania"
  },
  {
    "type": "POST",
    "url": "/opsidra/modofertacampania/",
    "title": "[modofertacampania]",
    "name": "modofertacampania",
    "description": "<p>Servicio para modificar Ofertas o Campa\\u00F1as creadas.</p>",
    "group": "OfertaCampania",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs en el que se desea realizar la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idOfertaCampania",
            "description": "<p>Identificador de la Oferta o Campa\\u00F1a que se desea modificar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "tipo",
            "description": "<p>Nombre del tipo de Oferta/Campa\\u00F1a que se desea, puede ser OFERTA o CAMPA\\u00D1A.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "nombre",
            "description": "<p>Nombre de la Oferta o Campa\\u00F1a a modificar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "descripcion",
            "description": "<p>Descripci\\u00F3n de la Oferta o Campa\\u00F1a a modificar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "cantMaxPromocionales",
            "description": "<p>Valor m\\u00E1ximo de pormocionales a brindar en la Campa\\u00F1a. Este campo es innecesario si el Tipo de Oferta/Campa\\u00F1a es OFERTA.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "fechaDesde",
            "description": "<p>Fecha en formato dd/MM/aaaa desde la que aplica la Oferta o Campa\\u00F1a.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "fechaHasta",
            "description": "<p>Fecha en formato dd/MM/aaaa hasta la que aplica la Oferta o Campa\\u00F1a.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "estado",
            "description": "<p>Estado de la Oferta o Campa\\u00F1a.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "ofertaCampaniaDet",
            "description": "<p>Arreglo con el listado de detalles a asociar con la Oferta o Campa\\u00F1a.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "ofertaCampaniaDet.idTipo",
            "description": "<p>Identificador del tipo de detalle a asociar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "ofertaCampaniaDet.tipo",
            "description": "<p>Tipo del detalle a asociar, puede ser PANEL o RUTA.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"codArea\": \"505\",\n    \"usuario\": \"usuario.prueba\",\n    \"idOfertaCampania\": \"1\",\n    \"tipo\": \"CAMPA\\u00D1A\",\n    \"nombre\": \"Oferta 1\",\n    \"descripcion\": \"Desc Oferta 1\",\n    \"cantMaxPromocionales\": \"5\",\n    \"fechaDesde\": \"30/10/2015\",\n    \"fechaHasta\": \"30/11/2015\",\n    \"estado\": \"ALTA\",\n    \"OfertaCampaniaDet\": [\n        {\n            \"idTipo\": \"1\",\n            \"tipo\": \"PANEL\"\n        },\n        {\n            \"idTipo\": \"2\",\n            \"tipo\": \"PANEL\"\n        }\n    ]\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"201\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"Recurso modificado exitosamente.\",\n    \"clase\": \"OperacionOfertaCampania\",\n    \"metodo\": \"doPutDel\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"-199\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"Los siguientes datos faltan o son incorrectos:  Estado.\",\n    \"clase\": \"CtrlOfertaCampania\",\n    \"metodo\": \"validarInput\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioOperacion.java",
    "groupTitle": "OfertaCampania"
  },
  {
    "type": "POST",
    "url": "/opsidra/cambiaestadopanel/",
    "title": "[cambiaEstadoPanel]",
    "name": "cambiaEstadoPanel",
    "description": "<p>Servicio para modificar puntos de venta.</p>",
    "group": "Panel",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "cambiaEstadoPanel": [
          {
            "group": "cambiaEstadoPanel",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs al que se le modificar\\u00E1 una panel.</p>"
          },
          {
            "group": "cambiaEstadoPanel",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operacion.</p>"
          },
          {
            "group": "cambiaEstadoPanel",
            "type": "String",
            "optional": false,
            "field": "idPanel",
            "description": "<p>id de la panel a modificar.</p>"
          },
          {
            "group": "cambiaEstadoPanel",
            "type": "String",
            "optional": false,
            "field": "estado",
            "description": "<p>nombre del estado que se desea modificar en panel.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n   \"codArea\": \"505\",\n   \"usuario\": \"usuario\",\n   \"idPanel\": \"1\",\n   \"estado\": \"baja\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n   \"idPanel\": \"1\",\n   \"respuesta\": {\n       \"codResultado\": \"8\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"OK.  Se cambio de estado correctamente.\",\n       \"clase\": \" \",\n       \"metodo\": \" \",\n       \"excepcion\": \" \",\n       \"tipoExepcion\": \"\"\n   }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n   \"respuesta\": {\n       \"codResultado\": \"-33\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"El estado ingresado no es v\\u00E1lido o no existe.\",\n       \"clase\": \" \",\n       \"metodo\": \"cambiarEstadoPanel\",\n       \"excepcion\": \"class CtrlPanel\",\n       \"tipoExepcion\": \"Generales\"\n   }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioOperacion.java",
    "groupTitle": "Panel"
  },
  {
    "type": "POST",
    "url": "/opsidra/creapanel/",
    "title": "[creaPanel]",
    "name": "creaPanel",
    "description": "<p>Servicio para crear paneles en Sidra.</p>",
    "group": "Panel",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs del que se desea obtener informaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idDistribuidor",
            "description": "<p>Id del distribuidor al que pertenecer\\u00E1 la panel.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idBodegaVirtual",
            "description": "<p>Id de la bodega que es asignada a la panel.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "nombre",
            "description": "<p>Nombre para la panel.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "datosVendedor",
            "description": "<p>Listado de vendedores con los datos implicados.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "datosVendedor.vendedor",
            "description": "<p>Id del vendedor a asociar a la panel.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "datosVendedor.responsable",
            "defaultValue": "1 o 0",
            "description": "<p>Indica si el vendedor es el responsable de la panel o no. 1=Si o 0=No.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n   \"usuario\": \"usuario.pruebas\",\n   \"idDistribuidor\": \"1\",\n   \"idBodegaVirtual\": \"1\",\n   \"nombre\": \"Panel Prueba\",\n   \"datosVendedor\":[\n       {\n           \"vendedor\": \"2322\",\n           \"responsable\": \"1\"\n       }\n   ]\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n   \"idPanel\": \"1\",\n   \"respuesta\": {\n       \"codResultado\": \"6\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"OK. Creaci\\u00F3n de panel exitosa\",\n       \"clase\": \" \",\n       \"metodo\": \" \",\n       \"excepcion\": \" \",\n       \"tipoExepcion\": \"\"\n   }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n   \"respuesta\": {\n       \"codResultado\": \"-43\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"Vendedores para panel no son admitidos. El vendedor supervisor3 ya ha sido asignado a una panel.\",\n       \"clase\": \" \",\n       \"metodo\": \"insertarPanel\",\n       \"excepcion\": \"CtrlPanel\",\n       \"tipoExepcion\": \"Generales\"\n   }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioOperacion.java",
    "groupTitle": "Panel"
  },
  {
    "type": "POST",
    "url": "/consultasidra/getpanel/",
    "title": "[getPanel]",
    "name": "getPanel",
    "description": "<p>Servicio para obtener los datos de Paneles configuradas.</p>",
    "group": "Panel",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "token",
            "description": "<p>Token de autenticaci\\u00F3n de sesion de usuario.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codDispositivo",
            "description": "<p>c\\u00F3digo del dispositivo desde donde se realiza la operaci\\u00F3n, en caso de utilizarse la APP M\\u00F3VIL.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs del que se desea obtener informaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idPanel",
            "description": "<p>Identificador de la Panel que se desea obtener.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idDistribuidor",
            "description": "<p>Identificador del distribuidor que se desea obtener.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idBodegaVirtual",
            "description": "<p>Identificador de la bodega virtual que se desea consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "estado",
            "description": "<p>Estado de la panel a consultar.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n   \"codArea\": \"505\",\n   \"token\": \"WEB\",\n   \"codDispositivo\": \"\",\n   \"usuario\": \"usuario.pruebas\",\n   \"idPanel\": \"\",\n   \"idDistribuidor\": \"\",\n   \"idBodegaVirtual\": \"\",\n   \"estado\": \"\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n   \"token\": \"WEB\",\n   \"respuesta\": {\n       \"codResultado\": \"0\",\n       \"mostrar\": \"0\",\n       \"descripcion\": \"Recursos recuperados exitosamente.\"\n   },\n   \"panel\": {\n       \"idPanel\": \"1\",\n       \"nombre\": \"PANEL 1\",\n       \"idDistribuidor\": \"1\",\n       \"idBodegaVirtual\": \"25\",\n       \"nombreDTS\": \"Victor Cifuentes\",\n       \"tipoDTS\": \"EXTERNO\",\n       \"responsable\": \"2010\",\n       \"nombreResponsable\": \"usuario.sidra\",\n       \"idBodResponsable\": \"96\",\n       \"nombreBodResponsable\": \"BODEGA VENDEDOR 2010\",\n       \"estado\": \"ALTA\",\n       \"creado_el\": \"2015-10-26 10:26:07.0\",\n       \"creado_por\": \"usuario.pruebas\",\n       \"datosVendedor\": [{\n         \"idVendPanelPDV\": \"2010\",\n         \"nombre\": \"usuario.sidra\",\n         \"cantInventario\": \"0\",\n         \"estado\": \"ALTA\"\n       }]\n   }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n   \"respuesta\": {\n       \"codResultado\": \"-205\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"No existen registros con esos par\\u00E1metros.\",\n       \"clase\": \"OperacionPanel\",\n       \"metodo\": \"doGet\",\n       \"excepcion\": \" \",\n       \"tipoExepcion\": \" \"\n   }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioConsulta.java",
    "groupTitle": "Panel"
  },
  {
    "type": "POST",
    "url": "/opsidra/modificapanel/",
    "title": "[modificaPanel]",
    "name": "modificaPanel",
    "description": "<p>Servicio para modificar datos de paneles.</p>",
    "group": "Panel",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs del que se desea obtener informaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idPanel",
            "description": "<p>Id de la panel a modificar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idDistribuidor",
            "description": "<p>Id del distribuidor al que pertenecer\\u00E1 la panel.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idBodegaVirtual",
            "description": "<p>Id de la bodega que es asignada a la panel.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "nombre",
            "description": "<p>Nombre para la panel.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "datosVendedor",
            "description": "<p>Listado de vendedores con los datos implicados.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "datosVendedor.vendedor",
            "description": "<p>Id del vendedor a asociar a la panel.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "datosVendedor.responsable",
            "defaultValue": "1 o 0",
            "description": "<p>Indica si el vendedor es el responsable de la panel o no. 1=Si o 0=No.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n   \"codArea\": \"505\",\n   \"usuario\": \"usuario.pruebas\",\n   \"idPanel\": \"1\",\n   \"idDistribuidor\": \"1\",\n   \"idBodegaVirtual\": \"1\",\n   \"nombre\": \"Panel Prueba\",\n   \"datosVendedor\":[\n       {\n           \"vendedor\": \"2322\",\n           \"responsable\": \"1\"\n       },{\n           \"vendedor\": \"2242\",\n           \"responsable\": \"0\"\n       }\n   ]\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n   \"idPanel\": \"1\",\n   \"respuesta\": {\n       \"codResultado\": \"7\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"OK. Panel modificado correctamente.\",\n       \"clase\": \" \",\n       \"metodo\": \" \",\n       \"excepcion\": \" \",\n       \"tipoExepcion\": \"\"\n   }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n   \"respuesta\": {\n       \"codResultado\": \"-43\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"Vendedores para panel no son admitidos. El vendedor supervisor3 ya ha sido asignado a una panel.\",\n       \"clase\": \" \",\n       \"metodo\": \"modificarPanel\",\n       \"excepcion\": \"CtrlPanel\",\n       \"tipoExepcion\": \"Generales\"\n   }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioOperacion.java",
    "groupTitle": "Panel"
  },
  {
    "type": "POST",
    "url": "/opsidra/cargararchivosporta/",
    "title": "[cargararchivosporta]",
    "name": "cargararchivosporta",
    "description": "<p>Servicio para realizar cargas de archivos para el proceso de Portabilidad.</p>",
    "group": "Portabilidad",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs en el que se desea realizar la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "token",
            "description": "<p>token de validaci\\u00F3n de usuario.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codDispositivo",
            "description": "<p>c\\u00F3digo del dispositivo desde donde se realiza la operaci\\u00F3n, en caso de utilizarse la APP M\\u00F3VIL.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idPortabilidad",
            "description": "<p>Identificador de la gestion de portabilidad.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idPortalMovil",
            "description": "<p>Identificador de la gestion a la que se asociar\\u00E1 el archivo.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "nombreArchivo",
            "description": "<p>Nombre del archivo adjunto.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "extension",
            "description": "<p>Extensi\\u00F3n del archivo a adjuntar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "tipoArchivo",
            "description": "<p>Tipo del documento a adjuntar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idAttachment",
            "description": "<p>id getion FS.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "adjunto",
            "description": "<p>Archivo Adjunto.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"codArea\": \"505\",\n    \"usuario\": \"usuario.pruebas\",     *     \n    \"token\": \"WEB\",\n    \"codDispositivo\": \"64E1CE4A48BF656974C376B7905F7E62\",\n    \"idPortabilidad\": \"126\"\n    \"idPortaMovil\": \"\"\n    \"nombreArchivo\": \"Adjunto 1\",\n    \"extension\": \"PDF\",\n    \"tipoArchivo\": \"PDF\",\n    \"idAttachment\" \"132\";\n    \"adjunto\": \"/9j/4AAQSkZJRgABAQAAAQABAAD/2wCEAAkG....\",\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n  \"idAdjunto\": \"1\",\n  \"respuesta\": {\n    \"codResultado\": \"64\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"OK. Se registr\\u00F3 el archivo adjunto correctamente.\",\n    \"clase\": \"CtrlCargaArchivosPorta\",\n    \"metodo\": \"cargarArchivoPorta\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"-199\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"Los siguientes datos faltan o son incorrectos:  Tipo Documento no corresponde a ninguno de los tipos definidos.\",\n    \"clase\": \"CtrlCargaArchivosPorta\",\n    \"metodo\": \"cargarArchivoPorta\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioOperacion.java",
    "groupTitle": "Portabilidad"
  },
  {
    "type": "POST",
    "url": "/opsidra/creaportabilidad/",
    "title": "[creaportabilidad]",
    "name": "creaportabilidad",
    "description": "<p>Servicio para crear portabilidad</p>",
    "group": "Portabilidad",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs en el que se desea realizar la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "token",
            "description": "<p>token de validaci\\u00F3n de usuario.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codDispositivo",
            "description": "<p>c\\u00F3digo del dispositivo desde donde se realiza la operaci\\u00F3n, en caso de utilizarse la APP M\\u00F3VIL.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idJornada",
            "description": "<p>Jornada activa del vendedor.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idVendedor",
            "description": "<p>Id del vendedor del cual se desean registradas la portabilidad.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idPortaMovil",
            "description": "<p>Id del Portal Movil.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "numPortar",
            "description": "<p>n\\u00FAmero a portar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "operadorDonante",
            "description": "<p>Nombre del operador donante</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "cip",
            "description": "<p>codigo cip</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "productoDonante",
            "description": "<p>Tipo de producto donante puede ser: PREPAGO/POSTPAGO</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "numTemporal",
            "description": "<p>N\\u00FAmero temporal</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "primerNombre",
            "description": "<p>Primer nombre del cliente</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "segundoNombre",
            "description": "<p>Segundo nombre del cliente. Este campo es opcional</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "primerApellido",
            "description": "<p>Primer apellido del cliente.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "segundoApellido",
            "description": "<p>Segundo apellido del cliente. Este campo es opcional</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "tipoDocumento",
            "description": "<p>Descripci\\u00F3n del tipo de documento con el que se identifica el cliente.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "noDocumento",
            "description": "<p>Numero \\u00FAnico de identificaci\\u00F3n del cliente.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "adjuntoporta.nombreArchivo",
            "description": "<p>nombre archivo adjunto.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "adjuntoporta.adjunto",
            "description": "<p>archivo adjunto.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo parametros de Entrada:",
          "content": "{\n\t\"usuario\": \"usuario.pruebas\",\n\t\"codArea\": \"505\",\n\t\"token\": \"\",\n\t\"codDispositivo\": \"\",\n    \"idJornada\":\"2\",\n    \"idVendedor\":\"\",\n\t\"idPortaMovil\":\"\",\n\t\"numPortar\":\"\",\n\t\"operadorDonante\":\"\",\n\t\"cip\":\"\",\n\t\"productoDonante\":\"POSTPAGO\",\n\t\"numTemporal\":\"\",\n\t\"primerNombre\": \"\",\n\t\"segundoNombre\": \"\",\n\t\"primerApellido\": \"\",\n\t\"segundoApellido\": \"\",\n\t\"tipoDocumento\": \"\",\n\t\"noDocumento\": \"\",\n\t\"adjuntoporta\":[\n\t\t{\n\t\t\t\"nombreArchivo\":\"\",\n\t\t\t\"adjunto\":\"\"\n\t\t}\n\t]\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "{} JSON Respuesta-\\u00E9xito:",
          "content": "{\n\t\"respuesta\": {\n\t\t\"codResultado\": \"202\",\n\t\t\"mostrar\": \"1\",\n\t\t\"descripcion\": \"OK. Solicitud de Portabilidad Enviada Correctamente\",\n\t \t\"clase\": \"\",\n\t\t\"metodo\": \"\",\n\t\t\"excepcion\": \" \",\n\t\t\"tipoExepcion\": \"\"\n\t\t},\n\n\t\t\"idSolicitudPorta\":\"\"\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "{} JSON Respuesta-Error:     ",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"-643\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"No se encontraron datos.\",\n    \"clase\": \"\",\n    \"metodo\": \"\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioOperacion.java",
    "groupTitle": "Portabilidad"
  },
  {
    "type": "POST",
    "url": "/consultasidra/getValidarPortacion/",
    "title": "[getValidarPortacion]",
    "name": "getValidarPortacion",
    "description": "<p>Servicio para consulta de CIP y operador donante</p>",
    "group": "Portabilidad",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pais.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "nip",
            "description": "<p>N\\u00FAmero de Identificaci\\u00F3n Personal (movil).</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "numTelefono",
            "description": "<p>N\\u00FAmero de tel\\u00E9fono.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "validar",
            "description": "<p>numero de servicio a validar 1 valida CIP, 2 valida operador donante y 3 valida ambos.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo parametros de Entrada:",
          "content": "{\n    \"codArea\": \"505\",\n    \"usuario\": \"admin-sidra\",\n    \"nip\": \"1982\",\n    \"numTelefono\": \"56322055\",\n    \"validar\": \"3\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "{} JSON Respuesta-\\u00E9xito:",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"205\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"OK. Se obtuvieron datos de operador donante\",\n    \"clase\": \"OperacionPortabilidad\",\n    \"metodo\": \"doGet\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"\"\n  },\n  \n  \"donante\": \"movistar\" \n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "{} JSON Respuesta-Error:     ",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"-643\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"No se encontraron datos.\",\n    \"clase\": \"OperacionPortabilidad\",\n    \"metodo\": \"doGet\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioConsulta.java",
    "groupTitle": "Portabilidad"
  },
  {
    "type": "POST",
    "url": "/consultasidra/getestadoporta/",
    "title": "[getestadoporta]",
    "name": "getestadoporta",
    "description": "<p>Servicio para consulta estado portabilidad</p>",
    "group": "Portabilidad",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "token",
            "description": "<p>token de validaci\\u00F3n de usuario.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs en el que se desea realizar la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codDispositivo",
            "description": "<p>c\\u00F3digo del dispositivo desde donde se realiza la operaci\\u00F3n, en caso de utilizarse la APP M\\u00F3VIL.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idJornada",
            "description": "<p>dentificador de la jornada activa.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idVendedor",
            "description": "<p>Identificador del vendedor de la jornada y la venta.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "IdVenta",
            "description": "<p>Identificador de la venta a anular.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "numeroaPortar",
            "description": "<p>N\\u00FAmero de cliente a proporcionar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "numeroTemporal",
            "description": "<p>indica el numero temporal a utilizar.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo parametros de Entrada:",
          "content": "{\n\t\t\"token\": \"WEB\",\n\t\t\"codArea\": \"503\",\n\t\t\"usuario\": \"usuario.pruebas\",\n\t\t\"codDispositivo\":\"\",\n\t\t\"idJornada\": \"\",\n\t\t\"idVendedor\": \"\",\n\t\t\"IdVenta\": \"\",\n\t\t\" numeroaPortar\":\"\",\n\t\t\"numeroTemporal\":\"\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "{} JSON Respuesta-\\u00E9xito:",
          "content": "{\n\t\"token\": \"WEB\",\n\t\"respuesta\": \n\t\t{\n\t\t\t\"codResultado\": \"1\",\n\t\t\t\"mostrar\": \"0\",\n\t\t\t\"descripcion\": \"OK Solicitudes obtenidas.\",\n\t\t\t\"clase\": \"CtrlPortabilidad\",\n\t\t\t\"metodo\": \"getDatos\",\n\t\t\t\"excepcion\": \" \"\n\t\t}\n\t\"ventas\":[\n\t\t{\n\t\t\t\"idVenta\":\"\",\n\t\t\t\"numeroaPortar\":\"\",\n\t\t\t\"numeroTemporal\":\"\",\n\t\t\t\"operadorDonante\":\"\",\n\t\t\t\"estado\":\"\"\n\t\t},\n\t\t{\n\t\t\t\"idVenta\":\"\",\n\t\t\t\"numeroaPortar\":\"\",\n\t\t\t\"numeroTemporal\":\"\",\n\t\t\t\"operadorDonante\":\"\",\n\t\t\t\"estado\":\"\"\n\t\t}\n\t\t]\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "{} JSON Respuesta-Error:     ",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"-643\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"No se encontraron datos.\",\n    \"clase\": \"getestadoporta\",\n    \"metodo\": \"doGet\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioConsulta.java",
    "groupTitle": "Portabilidad"
  },
  {
    "type": "POST",
    "url": "/opsidra/bajapromocional/",
    "title": "[bajapromocional]",
    "name": "bajapromocional",
    "description": "<p>Servicio para dar de baja un art\\u00EDculo promocional creado.</p>",
    "group": "Promocionales",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operacion.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idArtPromocional",
            "description": "<p>Identificador del art\\u00EDculo promocional a dar de baja.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"codArea\": \"505\",\n    \"usuario\": \"usuario.pruebas\",\n    \"idArtPromocional\": \"1\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n   \"respuesta\": {\n       \"codResultado\": \"201\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"Recurso modificado exitosamente.\",\n       \"clase\": \"OperacionPromocionales\",\n       \"metodo\": \"doPutDel\",\n       \"excepcion\": \" \",\n       \"tipoExepcion\": \" \"\n   }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n   \"respuesta\": {\n       \"codResultado\": \"-201\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"No existe el recurso deseado.\",\n       \"clase\": \"OperacionPromocionales\",\n       \"metodo\": \"doPutDel\",\n       \"excepcion\": \" \",\n       \"tipoExepcion\": \" \"\n   }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioOperacion.java",
    "groupTitle": "Promocionales"
  },
  {
    "type": "POST",
    "url": "/opsidra/creapromocional/",
    "title": "[creapromocional]",
    "name": "creapromocional",
    "description": "<p>Servicio para crear Promocionaleses por pa\\u00EDs.</p>",
    "group": "Promocionales",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operacion.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "descripcion",
            "description": "<p>Nombre del art\\u00EDculo promocional a crear.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "tipoGrupo",
            "description": "<p>Nombre del grupo del art\\u00EDculo promocional a crear.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idProductOffering",
            "description": "<p>Id de la oferta con la cual se venderan promocionales en FS, aplica solo para SV.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"codArea\": \"505\",\n    \"usuario\": \"usuario.pruebas\",\n    \"descripcion\": \"Pachon Blanco\",\n    \"tipoGrupo\": \"GRUPO B\",\n    \"idProductOffering\":\"9146027023265528900\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n  \"idArtPromocional\": \"4\",\n  \"respuesta\": {\n    \"codResultado\": \"200\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"Campos agregados exitosamente.\",\n    \"clase\": \"OperacionPromocionales\",\n    \"metodo\": \"doPost\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"-199\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"Los siguientes datos faltan o son incorrectos:  El nombre enviado se encontr\\u00F3 con el c\\u00F3digo 'C4', en el grupo: GRUPO B y con el estado: ALTA\",\n    \"clase\": \"CtrlPromocionales\",\n    \"metodo\": \"validarInput\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioOperacion.java",
    "groupTitle": "Promocionales"
  },
  {
    "type": "POST",
    "url": "/consultasidra/getpromocionales/",
    "title": "[getPromocionales]",
    "name": "getPromocionales",
    "description": "<p>Servicio para obtener los datos de art\\u00EDculo promocionales registrados por pais.</p>",
    "group": "Promocionales",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pais.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operacion.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idArtPromocional",
            "description": "<p>Identificador del art\\u00EDculo promocional a consultar. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArticulo",
            "description": "<p>C\\u00F3digo del art\\u00EDculo promocional que se desea consultar. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "descripcion",
            "description": "<p>Nombre del art\\u00EDculo promocional que se desea consultar. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "tipoGrupo",
            "description": "<p>Nombre del grupo del art\\u00EDculo promocional que se desea consultar. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "estado",
            "description": "<p>Estado del art\\u00EDculo promocional que se desea consultar. Este campo es opcional.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"codArea\": \"505\",\n    \"usuario\": \"usuario.pruebas\",\n    \"idArtPromocional\": \"\",\n    \"codArticulo\": \"\",\n    \"descripcion\": \"\",\n    \"tipoGrupo\": \"\",\n    \"estado\": \"\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"0\",\n    \"mostrar\": \"0\",\n    \"descripcion\": \"Recursos recuperados exitosamente.\"\n  },\n  \"grupos\": [\n    {\n      \"grupo\": \"GRUPO A\",\n      \"art\\u00EDculos\": [\n        {\n          \"idArtPromocional\": \"1\",\n          \"codArticulo\": \"C1\",\n          \"descripcion\": \"PACHON AZUL\",\n          \"tipoGrupo\": \"GRUPO A\",\n          \"idProductOffering\": \"9146027023265528900\",\n          \"estado\": \"BAJA\",\n          \"creado_el\": \"21/12/2015 08:23:16\",\n          \"creado_por\": \"usuario.pruebas\",\n          \"modificado_el\": \"21/12/2015 09:25:06\",\n          \"modificado_por\": \"usuario.pruebas\"\n        },\n        {\n          \"idArtPromocional\": \"2\",\n          \"codArticulo\": \"C2\",\n          \"descripcion\": \"PACHON NEGRO\",\n          \"tipoGrupo\": \"GRUPO A\",\n          \"idProductOffering\": \"9146027023265528900\",\n          \"estado\": \"ALTA\",\n          \"creado_el\": \"21/12/2015 08:40:44\",\n          \"creado_por\": \"usuario.pruebas\",\n          \"modificado_el\": \"21/12/2015 10:28:47\",\n          \"modificado_por\": \"usuario.pruebas\"\n        }\n      ]\n    },\n    {\n      \"grupo\": \"GRUPO B\",\n      \"art\\u00EDculos\": [\n        {\n          \"idArtPromocional\": \"4\",\n          \"codArticulo\": \"C4\",\n          \"descripcion\": \"PACHON BLANCO\",\n          \"tipoGrupo\": \"GRUPO B\",\n          \"idProductOffering\": \"9146027023265528900\",\n          \"estado\": \"ALTA\",\n          \"creado_el\": \"21/12/2015 10:48:21\",\n          \"creado_por\": \"usuario.pruebas\"\n        },\n        {\n          \"idArtPromocional\": \"3\",\n          \"codArticulo\": \"C3\",\n          \"descripcion\": \"PACHON VERDE\",\n          \"tipoGrupo\": \"GRUPO B\",\n          \"idProductOffering\": \"9146027023265528900\",\n          \"estado\": \"ALTA\",\n          \"creado_el\": \"21/12/2015 09:29:07\",\n          \"creado_por\": \"usuario.pruebas\"\n        }\n      ]\n    }\n  ]\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"-205\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"No existen registros con esos par\\u00E1metros.\",\n    \"clase\": \"OperacionPromocionales\",\n    \"metodo\": \"doGet\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioConsulta.java",
    "groupTitle": "Promocionales"
  },
  {
    "type": "POST",
    "url": "/consultasidra/getinventariopromo/",
    "title": "[getinventariopromo]",
    "name": "getinventariopromo",
    "description": "<p>Servicio para obtener una lista de los articulos promocionales por bodega.</p>",
    "group": "Promocionales",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de area del pa\\u00EDs en el que se desea obtener informaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre del usuario que solicita la informaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idBodegaVritual",
            "description": "<p>Identificador de la Bodega Virtual que se desea consultar sus articulos.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idArticulo",
            "description": "<p>Identificador del art\\u00EDculo a buscar. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "descripcion",
            "description": "<p>Descripcion del articulo. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "estado",
            "description": "<p>Estado del articulo. Este campo es opcional.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo parametros de Entrada:",
          "content": "{    \n    \"codArea\": \"505\",\n    \"usuario\": \"usuario.prueba\",\n    \"idBodegaVirtual\": \"159\",\n    \"idArticulo\": \"\",\n    \"descripcion\": \"\",\n    \"estado\": \"\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "{} JSON Respuesta-\\u00E9xito:",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\":\"1\",\n    \"mostrar\": \"0\",\n    \"descripcion\": \"Recursos recuperados exitosamente\",\n  },\n \"idBodegaVirtual\":\"1\",\n \"nombreBodegaVirtual\":\"BODEGA ABC\",\n \"articulos\":[\n {\n \"idArticulo\":\"1\",\n \"descripcion\":\"PACHON\",\n \"cantidad\":\"100\",\n \"estado\":\"Disponible\"\n },\n {\n \"idArticulo\":\"2\",\n \"descripcion\":\"LAPICERO MOVISTAR\",\n \"cantidad\":\"500\",\n \"estado\":\"Disponible\"\n }\n ]     \n }",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "{} JSON Respuesta-Error:     ",
          "content": "{\n   \"token\":\"\"\n    \"respuesta\": {\n        \"codResultado\": \"-244\",\n        \"mostrar\": \"1\",\n        \"descripcion\": \"No existe el Art\\u00EDculo Promocional enviado en el detalle #.\",\n        \"clase\": \"OperacionConsultaArticulosPromocionales\",\n        \"metodo\": \"Articulos\",\n        \"excepcion\": \" \",\n        \"tipoExepcion\": \"Generales\"\n    }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioConsulta.java",
    "groupTitle": "Promocionales"
  },
  {
    "type": "POST",
    "url": "/opsidra/ingresosalida/",
    "title": "[ingresoSalida]",
    "name": "ingresoSalida",
    "description": "<p>Servicio para modificar buzones que ser\\u00E1n utiles en la creaci\\u00F3n de solitudes para el work flow.</p>",
    "group": "Promocionales",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>c\\u00F3digo de area del pa\\u00EDs en el cual se desea</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operacion.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "articulos",
            "description": "<p>listado de articulos de los cuales se realizar\\u00E1 ingreso o egreso de inventario.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idBodegaVirtual",
            "description": "<p>id de bodega virtual a la que se realizar\\u00E1 la transacci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "tipo",
            "description": "<p>identifica si es ingreso o salida lo que se realizar\\u00E1.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idArticulo",
            "description": "<p>id del Art\\u00EDculo que se ingresar\\u00E1 o egresar\\u00E1.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "cantidad",
            "description": "<p>cantidad de art\\u00EDculos a operar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "observaciones",
            "description": "<p>observaciones varias.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"codArea\": \"505\",\n    \"usuario\": \"usuario\",\n    \"articulos\": [{\n        \"idBodegaVirtual\": \"82\",\n        \"tipo\": \"ingreso\",\n        \"idArticulo\": \"1\",\n        \"cantidad\": \"10\",\n        \"observaciones\": \"probandoingresos\"\n    }, {\n        \"idBodegaVirtual\": \"82\",\n        \"tipo\": \"salida\",\n        \"idArticulo\": \"2\",\n        \"cantidad\": \"10\",\n        \"observaciones\": \"probandoingresos\"\n    }]\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n    \"respuesta\": {\n        \"codResultado\": \"14\",\n        \"mostrar\": \"1\",\n        \"descripcion\": \"OK.Transacci\\u00F3n procesada correctamente. Los siguientes art\\u00EDculos no pudieron operarse correctamente.\",\n        \"clase\": \" \",\n        \"metodo\": \" \",\n        \"excepcion\": \" \",\n        \"tipoExepcion\": \"\"\n    },\n    \"datosIncorrectos\": {\n        \"razon\": \"Los art\\u00EDculos siguientes no existen en el Sistema de SIDRA.\",\n        \"articulos\": {\n            \"tipo\": \"INGRESO\",\n            \"idArticulo\": \"100\"\n        }\n    }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n    \"respuesta\": {\n        \"codResultado\": \"-59\",\n        \"mostrar\": \"1\",\n        \"descripcion\": \"El siguiente elemento del listado de art\\u00EDculos tiene incompletos los siguientes datos: No. Elemento:3.El campo cantidad esta vac\\u00EDo\",\n        \"clase\": \" \",\n        \"metodo\": \"validarDatos\",\n        \"excepcion\": \"class CtrlIngresoSalidaInvPromocional\",\n        \"tipoExepcion\": \"Generales\"\n    }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioOperacion.java",
    "groupTitle": "Promocionales"
  },
  {
    "type": "POST",
    "url": "/opsidra/modpromocional/",
    "title": "[modpromocional]",
    "name": "modpromocional",
    "description": "<p>Servicio para modificar art\\u00EDculos promocionales creados.</p>",
    "group": "Promocionales",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operacion.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idArtPromocional",
            "description": "<p>Identificador del art\\u00EDculo promocional a modificar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArticulo",
            "description": "<p>C\\u00F3digo del art\\u00EDculo promocional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "descripcion",
            "description": "<p>Nombre del art\\u00EDculo promocional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "tipoGrupo",
            "description": "<p>Nombre del grupo del art\\u00EDculo promocional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "estado",
            "description": "<p>Estado nuevo del art\\u00EDculo promocional (ALTA o BAJA).</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idProductOffering",
            "description": "<p>Id de la oferta con la cual se venderan promocionales en FS, aplica solo para SV.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"codArea\": \"505\",\n    \"usuario\": \"usuario.pruebas\",\n    \"idArtPromocional\": \"2\",\n    \"codArticulo\": \"C2\",\n    \"descripcion\": \"Pachon negro\",\n    \"tipoGrupo\": \"GRUPO B\",\n     \"idProductOffering\":\"9146027023265528900\",\n    \"estado\": \"ALTA\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n   \"respuesta\": {\n       \"codResultado\": \"201\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"Recurso modificado exitosamente.\",\n       \"clase\": \"OperacionPromocionales\",\n       \"metodo\": \"doPutDel\",\n       \"excepcion\": \" \",\n       \"tipoExepcion\": \" \"\n   }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"-199\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"Los siguientes datos faltan o son incorrectos:  El nombre enviado se encontr\\u00F3 con el c\\u00F3digo 'C4', en el grupo: GRUPO B y con el estado: ALTA\",\n    \"clase\": \"CtrlPromocionales\",\n    \"metodo\": \"validarInput\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioOperacion.java",
    "groupTitle": "Promocionales"
  },
  {
    "type": "POST",
    "url": "/opsidra/creaPDV/",
    "title": "[CreaPDV]",
    "name": "CreaPDV",
    "description": "<p>Servicio para crear puntos de venta.</p>",
    "group": "Puntos_de_Venta",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "token",
            "description": "<p>codigo de validaci\\u00F3n de sesi\\u00F3n para realizar operaciones, en caso de que el servicio sea utilizado desde la app WEB, el valor del token sera:WEB.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pais al que se le creara un punto de venta.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operacion.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "origen",
            "defaultValue": "MOVIL",
            "description": "<p>o PC indica desde donde se solicita la creaci\\u00F3n del punto de venta.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "tipoProducto",
            "description": "<p>indica que tipo de producto podr\\u00E1n vender los puntos de venta.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codDispositivo",
            "description": "<p>c\\u00F3digo del dispositivo desde donde se realiza la operaci\\u00F3n, en caso de utilizarse la APP M\\u00F3VIL.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "nombrePDV",
            "description": "<p>nombre para el punto de venta a crear.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "canal",
            "description": "<p>canal de distribucion.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "subcanal",
            "description": "<p>subcanal de distribucion.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "categoria",
            "description": "<p>categor\\u00EDa en la que se encuentra el pdv.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "distribuidorAsociado",
            "description": "<p>Id del distribuidor al que pertenecer\\u00E1 el punto de venta.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "tipoNegocio",
            "description": "<p>indica el tipo del negocio del punto de venta a registrar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "documento",
            "description": "<p>tipo de documento del punto de venta.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "nit",
            "description": "<p>nit del punto de venta o encargado del negocio.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "nombreFiscal",
            "description": "<p>Nombre fiscal del PDV.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "registroFiscal",
            "description": "<p>Registro fiscal del PDV.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "giroNegocio",
            "description": "<p>es el tipo de servicio que ofrezca el negocio.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "tipoContribuyente",
            "description": "<p>indica si el pdv es gran o peque\\u00F1o contribuyente.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "digitoValidador",
            "description": "<p>digito ingresado por el usuario, no aplica para todos los paises.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "calle",
            "description": "<p>n\\u00FAmero de calle donde se encuentra el punto de venta.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "avenida",
            "description": "<p>n\\u00FAmero de avenida donde es encuentra ubicado el punto de venta</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "pasaje",
            "description": "<p>parte de la direcci\\u00F3n del punto de venta.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "casa",
            "description": "<p>n\\u00FAmero de casa donde se encuentra el punto de venta.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "colonia",
            "description": "<p>colonia donde se encuentra ubicado el punto de venta.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "barrio",
            "description": "<p>barrio donde se encuentra ubicado el punto de venta, no aplica para todos los paises.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "referencia",
            "description": "<p>referencia sobre la ubicaci\\u00F3n del punto de venta.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "direccion",
            "description": "<p>Direcci\\u00F3n del punto de venta que est\\u00E1 siendo creado, no aplica para todos los paises.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "departamento",
            "description": "<p>Nombre del departamento al que pertenece el PDV.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "municipio",
            "description": "<p>Nombre del municipio al que pertenece el PDV.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "distrito",
            "description": "<p>Nombre del distrito donde se encuentra ubicado el PDV, no aplica para todos los paises.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "observaciones",
            "description": "<p>Observaciones q ue el usuario creador deseea agregar respecto a la  ubicaci\\u00F3n del PDV.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "dias",
            "description": "<p>Arreglo de d\\u00EDas en el que el punto de venta puede ser visitado.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "telefonoRecargo",
            "description": "<p>N\\u00FAmeros de telefono que pueden utilizarse para recargas electronicas .</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "latitud",
            "description": "<p>ubicaci\\u00F3n del punto de venta.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "longitud",
            "description": "<p>datas de ubicaci\\u00F3n del punto de venta.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "nombres",
            "description": "<p>nombres del encargado de punto de venta.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "apellidos",
            "description": "<p>apellidos del encargado del punto de venta.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "telefono",
            "description": "<p>n\\u00FAmero de telefono del encargado del punto de venta.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "cedula",
            "description": "<p>n\\u00FAmero de c\\u00E9dula del encargado, no aplica para todos los paises.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "qr",
            "description": "<p>C\\u00F3digo QR a recibir, no aplica para todos los paises.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"token\": \"WEB\",\n    \"pdv\": {\n        \"codArea\": \"505\",\n        \"usuario\": \"usuario\",\n        \"origen\": \"PC\",\n   \t   \"idRuta\": \"\",\n        \"codDispositivo\": \"64E1CE4A48BF656974C376B7905F7E62\",\n        \"tipoProducto\": \"FISICO\",\n        \"nombrePDV\": \"Prueba creacion3\",\n        \"canal\": \"SIDRA\",\n        \"subcanal\": \"DTS\",\n        \"categoria\": \"A\",\n        \"distribuidorAsociado\": \"1\",\n        \"tipoNegocio\": \"VENTA\",\n        \"documento\": \"NIT\",\n        \"nit\": \"54545454545\",\n        \"nombreFiscal\": \"EE\",\n        \"registroFiscal\": \"EE\",\n        \"giroNegocio\": \"VENTAS POR MENOR\",\n        \"tipoContribuyente\": \"Gran Contribuyente\",\n        \"digitoValidador\": \"\",\n        \"calle\": \"10\",\n        \"avenida\": \"5ta av.\",\n        \"pasaje\": \"123\",\n        \"casa\": \"2-20\",\n        \"colonia\": \"Colonia Monja Blanca\",\n        \"barrio\": \"\",\n        \"referencia\": \"Frente a Casa 2 niveles port\\u00F3n blanco\",\n        \"direccion\": \"ZONA 10, GUATEMALA\",\n        \"departamento\": \"GUATEMALA\",\n        \"municipio\": \"GUATEMALA\",\n        \"distrito\": \"\",\n        \"observaciones\": \"estoy creando un pdv :D\",\n        \"dias\": [\"Lunes\", \"Martes\", \"Mi\\u00E9rcoles\"],\n        \"telefonoRecargo\": [{\n            \"numero\": \"74520000\",\n            \"orden\": \"1\"\n        }, {\n            \"numero\": \"54120111\",\n            \"orden\": \"2\"\n        }],\n        \"latitud\": \"14.581026\",\n        \"longitud\": \"90.587321\",\n        \"encargado\": {\n            \"nombres\": \"LUISA FERNANDA\",\n            \"apellidos\": \"ROSALES MENDEZ\",\n            \"telefono\": \"45120022\",\n            \"cedula\": \"54454545\"\n        },\n        \"qr\": \"\"\n    }\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n   \"token\": \"WEB\",\n   \"respuesta\": {\n       \"codResultado\": \"4\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"Ok. Creaci\\u00F3n de Punto de Venta exitosa. \",\n       \"clase\": \" \",\n       \"metodo\": \" \",\n       \"excepcion\": \" \",\n       \"tipoExepcion\": \"\"\n   },\n   \"idPDV\": \"9\",\n    \"zonaComercial\": \"Managua\",\n   \"idEncargadoPDV\": \"11\"\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{ \n   \"token\":\" \",\n   \"respuesta\": {\n       \"codResultado\": \"-32\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"N\\u00FAmeros de Recarga repetidos. El N\\u00FAmero:50020111 ya ha sido asignado a otro punto de venta.\",\n       \"clase\": \" \",\n       \"metodo\": \"insertarPDV\",\n       \"excepcion\": \"class CtrlPuntoVenta\",\n       \"tipoExepcion\": \"Generales\"\n   }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioOperacion.java",
    "groupTitle": "Puntos_de_Venta"
  },
  {
    "type": "POST",
    "url": "/opsidra/modificaPDV/",
    "title": "[ModificaPDV]",
    "name": "ModificaPDV",
    "description": "<p>Servicio para modificar puntos de venta.</p>",
    "group": "Puntos_de_Venta",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "token",
            "description": "<p>codigo de validaci\\u00F3n de sesi\\u00F3n para realizar operaciones, en caso de que el servicio sea utilizado desde la app WEB, el valor del token sera:WEB.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs al que se le modificara un punto de venta.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operacion.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codDispositivo",
            "description": "<p>c\\u00F3digo del dispositivo desde donde se realiza la operaci\\u00F3n, en caso de utilizarse la APP M\\u00F3VIL.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idPDV",
            "description": "<p>id del punto de venta a modificar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "tipoProducto",
            "description": "<p>indica que tipo de producto podr\\u00E1n vender los puntos de venta.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "nombrePDV",
            "description": "<p>nombre para el punto de venta a modificar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "canal",
            "description": "<p>canal de distribucion.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "subcanal",
            "description": "<p>subcanal de distribucion.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "categoria",
            "description": "<p>categor\\u00EDa en la que se encuentra el pdv.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "distribuidorAsociado",
            "description": "<p>Id del distribuidor al que pertenecer\\u00E1 el punto de venta.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "tipoNegocio",
            "description": "<p>indica el tipo del negocio del punto de venta a registrar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "documento",
            "description": "<p>tipo de documento del punto de venta.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "nit",
            "description": "<p>nit del punto de venta o encargado del negocio.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "nombreFiscal",
            "description": "<p>Nombre fiscal del PDV.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "registroFiscal",
            "description": "<p>Registro fiscal del PDV.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "giroNegocio",
            "description": "<p>es el tipo de servicio que ofrezca el negocio.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "tipoContribuyente",
            "description": "<p>indica si el pdv es gran o peque\\u00F1o contribuyente.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "digitoValidador",
            "description": "<p>digito ingresado por el usuario, no aplica para todos los paises.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "calle",
            "description": "<p>n\\u00FAmero de calle donde se encuentra el punto de venta.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "avenida",
            "description": "<p>n\\u00FAmero de avenida donde es encuentra ubicado el punto de venta</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "pasaje",
            "description": "<p>parte de la direcci\\u00F3n del punto de venta.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "casa",
            "description": "<p>n\\u00FAmero de casa donde se encuentra el punto de venta.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "colonia",
            "description": "<p>colonia donde se encuentra ubicado el punto de venta.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "barrio",
            "description": "<p>barrio donde se encuentra ubicado el punto de venta, no aplica para todos los paises.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "referencia",
            "description": "<p>referencia sobre la ubicaci\\u00F3n del punto de venta.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "direccion",
            "description": "<p>Direcci\\u00F3n del punto de venta, no aplica para todos los paises.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "departamento",
            "description": "<p>Nombre del departamento al que pertenece el PDV.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "municipio",
            "description": "<p>Nombre del municipio al que pertenece el PDV.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "distrito",
            "description": "<p>Nombre del distrito donde se encuentra ubicado el PDV.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "observaciones",
            "description": "<p>Observaciones que el usuario deseea agregar respecto a la  ubicaci\\u00F3n del PDV.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "dias",
            "description": "<p>Arreglo de d\\u00EDas en el que el punto de venta puede ser visitado.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "telefonoRecargo",
            "description": "<p>N\\u00FAmeros de telefono que pueden utilizarse para recargas electronicas .</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "latitud",
            "description": "<p>ubicaci\\u00F3n del punto de venta.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "longitud",
            "description": "<p>datas de ubicaci\\u00F3n del punto de venta.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "nombres",
            "description": "<p>nombres del encargado de punto de venta.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "apellidos",
            "description": "<p>apellidos del encargado del punto de venta.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "telefono",
            "description": "<p>n\\u00FAmero de telefono del encargado del punto de venta.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "cedula",
            "description": "<p>n\\u00FAmero de c\\u00E9dula del encargado, no aplica para todos los paises.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"token\": \"WEB\",\n    \"pdv\": {\n        \"codArea\": \"505\",\n        \"usuario\": \"usuario\",\n        \"codDispositivo\": \"64E1CE4A48BF656974C376B7905F7E62\",\n        \"idPDV\":\"1\",\n        \"tipoProducto\": \"FISICO\",\n        \"nombrePDV\": \"Prueba creacion3\",\n        \"canal\": \"SIDRA\",\n        \"subcanal\": \"DTS\",\n        \"categoria\": \"A\",\n        \"distribuidorAsociado\": \"1\",\n        \"tipoNegocio\": \"VENTA\",\n        \"documento\": \"NIT\",\n        \"nit\": \"54545454545\",\n        \"nombreFiscal\": \"EE\",\n        \"registroFiscal\": \"EE\",\n        \"giroNegocio\": \"VENTAS POR MENOR\",\n        \"tipoContribuyente\": \"Gran Contribuyente\",\n        \"digitoValidador\": \"\",\n        \"calle\": \"10\",\n        \"avenida\": \"5ta av.\",\n        \"pasaje\": \"123\",\n        \"casa\": \"2-20\",\n        \"colonia\": \"Colonia Monja Blanca\",\n        \"barrio\": \"\",\n        \"referencia\": \"Frente a Casa 2 niveles port\\u00F3n blanco\",\n        \"direccion\": \"ZONA 10, GUATEMALA\",\n        \"departamento\": \"GUATEMALA\",\n        \"municipio\": \"GUATEMALA\",\n        \"distrito\": \"CENTRAL\",\n        \"observaciones\": \"estoy creando un pdv :D\",\n        \"dias\": [\"Lunes\", \"Martes\", \"Mi\\u00E9rcoles\"],\n        \"telefonoRecargo\": [{\n            \"numero\": \"74520000\",\n            \"orden\": \"1\",\n            \"estadoPayment\": \"\"\n        }, {\n            \"numero\": \"54120111\",\n            \"orden\": \"2\",\n            \"estadoPayment\": \"\"\n        }],\n        \"latitud\": \"14.581026\",\n        \"longitud\": \"90.587321\",\n        \"encargado\": {\n            \"idEncargadoPDV\": \"1\",\n            \"nombres\": \"LUISA FERNANDA\",\n            \"apellidos\": \"ROSALES MENDEZ\",\n            \"telefono\": \"45120022\",\n            \"cedula\": \"54454545\"\n        }\n    }\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n   \"token\": \"WEB\",\n   \"respuesta\": {\n       \"codResultado\": \"4\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"Ok. Punto de venta modificado correctamente. \",\n       \"clase\": \" \",\n       \"metodo\": \" \",\n       \"excepcion\": \" \",\n       \"tipoExepcion\": \"\"\n   },\n   \"idPDV\": \"9\",\n   \"zonaComercial\": \"Managua\"\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{ \n   \"token\":\"WEB\",\n   \"respuesta\": {\n       \"codResultado\": \"-32\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"N\\u00FAmeros de Recarga repetidos. El N\\u00FAmero:50020111 ya ha sido asignado a otro punto de venta.\",\n       \"clase\": \" \",\n       \"metodo\": \"insertarPDV\",\n       \"excepcion\": \"class CtrlPuntoVenta\",\n       \"tipoExepcion\": \"Generales\"\n   }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioOperacion.java",
    "groupTitle": "Puntos_de_Venta"
  },
  {
    "type": "POST",
    "url": "/opsidra/cambiaestadoPDV/",
    "title": "[cambiaEstadoPDV]",
    "name": "cambiaEstadoPDV",
    "description": "<p>Servicio para dar baja o alta un punto de venta existente.</p>",
    "group": "Puntos_de_Venta",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "token",
            "description": "<p>codigo de validaci\\u00F3n de sesi\\u00F3n para realizar operaciones, en caso de que el servicio sea utilizado desde la app WEB, el valor del token sera:WEB.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pais al que se le modificara un punto de venta.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operacion.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codDispositivo",
            "description": "<p>c\\u00F3digo del dispositivo desde donde se realiza la operaci\\u00F3n, en caso de utilizarse la APP M\\u00F3VIL.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idPDV",
            "description": "<p>id del punto de venta a modificar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "estado",
            "description": "<p>nombre del estado con el que ser\\u00E1 modificado el punto de venta.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n \"token\":\"WEB\", \n \"datos\":{ \n    \"codArea\": \"505\",\n    \"usuario\": \"usuario\",\n    \"codDispositivo\": \"64E1CE4A48BF656974C376B7905F7E62\",\n    \"idPDV\": \"9\",\n    \"estado\": \"BAJA\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n   \"token\": \"WEB\",\n   \"respuesta\": {\n       \"codResultado\": \"3\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"Ok. Se ha cambiado de estado correctamente al punto de venta\",\n       \"clase\": \" \",\n       \"metodo\": \" \",\n       \"excepcion\": \" \",\n       \"tipoExepcion\": \"\"\n   },\n   \"idPDV\": \"9\"\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n  \"token\": \"WEB\",\n   \"respuesta\": {\n       \"codResultado\": \"-33\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"El estado ingresado no es v\\u00E1lido o no existe.\",\n       \"clase\": \" \",\n       \"metodo\": \"cambiarEstadoPDV\",\n       \"excepcion\": \"class CtrlPuntoVenta\",\n       \"tipoExepcion\": \"Generales\"\n   }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioOperacion.java",
    "groupTitle": "Puntos_de_Venta"
  },
  {
    "type": "POST",
    "url": "/consultasidra/getestadonumrecarga/",
    "title": "[getEstadoNumRecarga]",
    "name": "getEstadoNumRecarga",
    "description": "<p>Servicio para consultar el estado de un numero de recarga.</p>",
    "group": "Puntos_de_Venta",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs en el que se desea realizar la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "token",
            "description": "<p>Token de validaci\\u00F3n de autenticaci\\u00F3n de usuario.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codDispositivo",
            "description": "<p>c\\u00F3digo del dispositivo desde donde se realiza la operaci\\u00F3n, en caso de utilizarse la APP M\\u00F3VIL.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idPDV",
            "description": "<p>Identificador del punto de venta a consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "numRecarga",
            "description": "<p>N\\u00FAmero de recarga a consultar.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"codArea\": \"505\",\n    \"token\": \"WEB\",\n    \"usuario\": \"usuario.pruebas\",\n    \"codDispositivo\": \"ASDF123QWER456ZXCV789\",\n    \"idPDV\": \"58\",\n    \"numRecarga\": \"44444444\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"202\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"Recursos recuperados exitosamente.\",\n    \"clase\": \"OperacionConsultaNumRecarga\",\n    \"metodo\": \"doGet\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"\"\n  },\n  \"estado\": \"ACTIVADO\"\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "{} JSON Respuesta-Error:     ",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"-754\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"Los siguientes n\\u00FAmeros de recarga no existen en el punto de venta: 55545555\",\n    \"clase\": \"OperacionConsultaNumRecarga\",\n    \"metodo\": \"doGet\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioConsulta.java",
    "groupTitle": "Puntos_de_Venta"
  },
  {
    "type": "POST",
    "url": "/consultasidra/getpdv/",
    "title": "[getPDV]",
    "name": "getPDV",
    "description": "<p>Servicio para obtener los datos de PDVs configuradas.</p>",
    "group": "Puntos_de_Venta",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operacion.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codDispositivo",
            "description": "<p>c\\u00F3digo del dispositivo desde donde se realiza la operaci\\u00F3n, en caso de utilizarse la APP M\\u00F3VIL.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idPDV",
            "description": "<p>Identificador de la PDV que se desea obtener.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idDTS",
            "description": "<p>Identificador del distribuidor asociado al punto de venta que se desea obtener.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idVendedor",
            "description": "<p>Identificador del vendedor asociado al punto de venta que se desea obtener.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idRuta",
            "description": "<p>Identificador de la ruta asociada que se desea obtener.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "numRecarga",
            "description": "<p>N\\u00FAmero de recarga del pdv, del cual se desea obtener informaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "ruc_nit",
            "description": "<p>Identificaci\\u00F3n tributaria del punto de venta a traves del cual se desea buscar el pdv.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "categoria",
            "description": "<p>Categor\\u00EDa del punto de venta que se desea consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "departamento",
            "description": "<p>Departamento del punto de venta que se desea consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "municipio",
            "description": "<p>Municipio del punto de venta que se desea consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "distrito",
            "description": "<p>Distrito del punto de venta que se desea consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "estado",
            "description": "<p>Estado del punto de venta.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "min",
            "description": "<p>Registro m\\u00EDnimo a consultar para pagineo.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "max",
            "description": "<p>Registro m\\u00E1ximo a consultar para pagineo.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "mostrarNumerosRecarga",
            "defaultValue": "1 o 0",
            "description": "<p>Valor boolean que indica si se deben obtener y mostrar los numeros de recarga (1 = S\\u00ED, 0 = No).</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "mostrarDiasVisita",
            "defaultValue": "1 o 0",
            "description": "<p>Valor boolean que indica si se deben obtener y mostrar los d\\u00EDas de visita (1 = S\\u00ED, 0 = No).</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n   \"token\":\"WEB\",\n   \"usuario\": \"usuario.pruebas\",\n   \"codDispositivo\": \"64E1CE4A48BF656974C376B7905F7E62\",\n   \"idPDV\": \"1\",\n   \"idDTS\": \"\",\n   \"idVendedor\": \"\",\n   \"idRuta\": \"\",\n   \"numRecarga\": \"\",\n   \"ruc_nit\": \"\",\n   \"categoria\": \"\",\n   \"departamento\": \"\",\n   \"municipio\": \"\",\n   \"distrito\": \"\",\n   \"estado\": \"\",\n   \"min\": \"\",\n   \"max\": \"\",\n   \"mostrarNumerosRecarga\": \"1\",\n   \"mostrarDiasVisita\": \"0\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n   \"respuesta\": {\n       \"codResultado\": \"0\",\n       \"mostrar\": \"0\",\n       \"descripcion\": \"Recursos recuperados exitosamente.\"\n   },\n   \"PuntoDeVenta\": [\n      {\n          \"codArea\": \"505\",\n          \"idPDV\": \"41\",\n          \"tipoProducto\": \"FISICO\",\n          \"nombrePDV\": \"Prueba creacion3\",\n          \"canal\": \"SIDRA\",\n          \"subcanal\": \"DTS\",\n          \"categoria\": \"A\",\n          \"digitoValidador\": \"\",\n          \"distribuidorAsociado\": \"5\",\n          \"nombreDTS\": \"DTS Uno\",\n          \"tipoDTS\": \"EXTERNO\",\n          \"tipoNegocio\": \"FARMACIA\",\n          \"documento\": \"NIT\",\n          \"nit\": \"54545454545\",\n          \"nombreFiscal\": \"EE\",\n          \"registroFiscal\": \"EE\",\n          \"giroNegocio\": \"VENTAS POR MENOR\",\n          \"tipoContribuyente\": \"Peque\\u00F1o Contribuyente\",\n          \"calle\": \"2-20\",\n          \"avenida\": \"5ta av.\",\n          \"pasaje\": \"123\",\n          \"colonia\": \"Colonia Monja Blanca\",\n          \"referencia\": \"Frente a Casa 2 niveles port\\u00F3n blanco\",\n          \"barrio\": \"\",\n          \"direccion\": \"ZONA 10, GUATEMALA\",\n          \"zonaComercial\": \"1\",\n          \"departamento\": \"GUATEMALA\",\n          \"municipio\": \"GUATEMALA\",\n          \"distrito\": \"CENTRAL\",\n          \"observaciones\": \"estoy creando un pdv :D\",\n          \"latitud\": \"14.581026\",\n          \"longitud\": \"90.587321\",\n          \"estado\": \"ACTIVO\",\n          \"encargado\": {\n              \"idEncargadoPDV\": \"1\",\n              \"nombres\": \"LUISA FERNANDA\",\n              \"apellidos\": \"ROSALES MENDEZ\",\n              \"telefono\": \"45124422\",\n              \"cedula\": \"\"\n          },\n          \"imgAsociadas\": [\n              {\n                 \"idImgPDV\": \"3\"\n              },\n              {\n                 \"idImgPDV\": \"10\"\n              }\n          ],\n          \"creado_el\": \"30/05/2016 10:09:41\",\n          \"creado_por\": \"usuario\",\n          \"modificado_el\": \"30/05/2016 16:28:09\",\n          \"modificado_por\": \"usuario\",\n          \"numerosRecarga\": [\n              {\n                  \"numero\": \"69857744\",\n                  \"orden\": \"1\",\n                  \"estado\": \"ALTA\",\n                  \"estadoPayment\": \"PENDIENTE\",\n                  \"idSolicitud\": \"44\"\n              },\n              {\n                  \"numero\": \"96325588\",\n                  \"orden\": \"2\",\n                  \"estado\": \"ALTA\",\n                  \"estadoPayment\": \"PENDIENTE\",\n                  \"idSolicitud\": \"44\"\n              }\n          ],\n          \"diasVisita\": [\n              {\n                  \"idDiaVisita\": \"255\",\n                  \"nombre\": \"LUNES\",\n                  \"estado\": \"ALTA\"\n              },\n              {\n                  \"idDiaVisita\": \"256\",\n                  \"nombre\": \"MARTES\",\n                  \"estado\": \"ALTA\"\n              },\n              {\n                  \"idDiaVisita\": \"257\",\n                  \"nombre\": \"MI\\u00E9RCOLES\",\n                  \"estado\": \"ALTA\"\n              }\n          ]\n      }\n   ]\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n   \"respuesta\": {\n       \"codResultado\": \"-395\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"No se encontraron puntos de venta configurados.\",\n       \"clase\": \"OperacionPDV\",\n       \"metodo\": \"doGet\",\n       \"excepcion\": \" \",\n       \"tipoExepcion\": \"\"\n   }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioConsulta.java",
    "groupTitle": "Puntos_de_Venta"
  },
  {
    "type": "POST",
    "url": "/consultasidra/getcountpdv/",
    "title": "[getcountpdv]",
    "name": "getcountpdv",
    "description": "<p>Servicio para contar los PDV's configurados en el sistema.</p>",
    "group": "Puntos_de_Venta",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operacion.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codDispositivo",
            "description": "<p>c\\u00F3digo del dispositivo desde donde se realiza la operaci\\u00F3n, en caso de utilizarse la APP M\\u00F3VIL.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idPDV",
            "description": "<p>Identificador de la PDV que se desea obtener.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idDTS",
            "description": "<p>Identificador del distribuidor asociado al punto de venta que se desea obtener.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idVendedor",
            "description": "<p>Identificador del vendedor asociado al punto de venta que se desea obtener.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idRuta",
            "description": "<p>Identificador de la ruta asociada que se desea obtener.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "categoria",
            "description": "<p>Categor\\u00EDa del punto de venta que se desea consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "departamento",
            "description": "<p>Departamento del punto de venta que se desea consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "municipio",
            "description": "<p>Municipio del punto de venta que se desea consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "distrito",
            "description": "<p>Distrito del punto de venta que se desea consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "estado",
            "description": "<p>Estado del punto de venta.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n   \"token\":\"WEB\",\n   \"usuario\": \"usuario.pruebas\",\n   \"codDispositivo\": \"64E1CE4A48BF656974C376B7905F7E62\",\n   \"idPDV\": \"1\",\n   \"idDTS\": \"\",\n   \"idRuta\": \"\",\n   \"categoria\": \"\",\n   \"departamento\": \"\",\n   \"municipio\": \"\",\n   \"distrito\": \"\",\n   \"estado\": \"\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n   \"respuesta\": {\n       \"codResultado\": \"0\",\n       \"mostrar\": \"0\",\n       \"descripcion\": \"Recursos recuperados exitosamente.\"\n   },\n   \"cantRegistros\": \"1\"\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n   \"respuesta\": {\n       \"codResultado\": \"-612\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"No se encontro informaci\\u00F3n con los datos recibidos.\",\n       \"clase\": \"OperacionPDV\",\n       \"metodo\": \"doGet\",\n       \"excepcion\": \" \",\n       \"tipoExepcion\": \"\"\n   }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioConsulta.java",
    "groupTitle": "Puntos_de_Venta"
  },
  {
    "type": "POST",
    "url": "/consultasidra/getcountpdvdisponible/",
    "title": "[getcountpdvdisponible]",
    "name": "getcountpdvdisponible",
    "description": "<p>Servicio para contar todos los PDVs configurados y disponibles.</p>",
    "group": "Puntos_de_Venta",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs en el que se desea realizar la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "token",
            "description": "<p>token de validaci\\u00F3n de autenticaci\\u00F3n de usuario.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codDispositivo",
            "description": "<p>c\\u00F3digo del dispositivo desde donde se realiza la operaci\\u00F3n, en caso de utilizarse la APP M\\u00F3VIL</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idPDV",
            "description": "<p>Identificador del punto de venta del que se desea obtener datos.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idDTS",
            "description": "<p>Identificador del distribuidor del que se desea obtener datos.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n   \"codArea\": \"505\",\n   \"token\": \"WEB\",\n   \"usuario\": \"usuarioPruebas\",\n   \"codDispositivo\": \"64E1CE4A48BF656974C376B7905F7E62\",\n   \"idPDV\": \"\",\n   \"idDTS\": \"1\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n   \"token\":\"WEB\",\t\n   \"respuesta\": {\n       \"codResultado\": \"77\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"OK. Conteo de PDVs Disponibles exitoso.\"\n   },\n   \"cantRegistros\": \"1\"\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n   \"token\": \"WEB\",\n   \"respuesta\": {\n       \"codResultado\": \"-541\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"No se encontraron puntos de venta disponibles.\",\n       \"clase\": \"CtrlPuntoVenta\",\n       \"metodo\": \"getPDVDisponible\",\n       \"excepcion\": \" \",\n       \"tipoExepcion\": \" \"\n   }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioConsulta.java",
    "groupTitle": "Puntos_de_Venta"
  },
  {
    "type": "POST",
    "url": "/consultasidra/getpdvdisponible/",
    "title": "[getpdvdisponible]",
    "name": "getpdvdisponible",
    "description": "<p>Servicio para obtener los datos de Panels configuradas.</p>",
    "group": "Puntos_de_Venta",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs en el que se desea realizar la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "token",
            "description": "<p>token de validaci\\u00F3n de autenticaci\\u00F3n de usuario.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codDispositivo",
            "description": "<p>c\\u00F3digo del dispositivo desde donde se realiza la operaci\\u00F3n, en caso de utilizarse la APP M\\u00F3VIL</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idPDV",
            "description": "<p>Identificador del punto de venta del que se desea obtener datos.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idDTS",
            "description": "<p>Identificador del distribuidor del que se desea obtener datos.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "min",
            "description": "<p>Valor numerico minimo que permite consultar el listado por rangos. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "max",
            "description": "<p>Valor numerico maximo que permite consultar el listado por rangos. Este campo es opcional.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n   \"codArea\": \"505\",\n   \"token\": \"WEB\",\n   \"usuario\": \"usuarioPruebas\",\n   \"codDispositivo\": \"64E1CE4A48BF656974C376B7905F7E62\",\n   \"idPDV\": \"\",\n   \"idDTS\": \"1\",\n   \"min\": \"1\",\n   \"max\": \"10\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n   \"token\":\"WEB\",\t\n   \"respuesta\": {\n       \"codResultado\": \"25\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"Ok. Puntos de venta obtenidos exitosamente.\"\n   },\n   \"puntoDeVenta\": [\n   \t{\n     \t\"idPDV\": \"1\",\n       \t\"nombrePDV\": \"TIENDA LA MEJOR\"\n   \t},\n   {\n     \t\"idPDV\": \"2\",\n       \t\"nombrePDV\": \"Tienda los canches\"\n   \t}\n   ]\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n   \"token\": \"WEB\",\n   \"respuesta\": {\n       \"codResultado\": \"-541\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"No se encontraron puntos de venta disponibles.\",\n       \"clase\": \"CtrlPuntoVenta\",\n       \"metodo\": \"getPDVDisponible\",\n       \"excepcion\": \" \",\n       \"tipoExepcion\": \" \"\n   }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioConsulta.java",
    "groupTitle": "Puntos_de_Venta"
  },
  {
    "type": "POST",
    "url": "/opsidra/crearemesa/",
    "title": "[crearemesa]",
    "name": "crearemesa",
    "description": "<p>Servicio para crear remesas en Sidra.</p>",
    "group": "Remesa",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "token",
            "description": "<p>Token de autenticaci\\u00F3n de sesion de usuario.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs en el que se desea realizar la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "origen",
            "defaultValue": "MOVIL o PC",
            "description": "<p>Origen de creaci\\u00F3n de las remesas.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idJornada",
            "description": "<p>Identificador de la jornada a la que se asocia la remesa. Obligatorio en caso de tratarse de origen MOVIL.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idDeuda",
            "description": "<p>Identificador de la deuda a la que se asociar\\u00E1n las remesas. Obligatorio en caso de tratarse de origen PC.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "remesas",
            "description": "<p>Listado de remesas a crear.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "remesas.idRemesa",
            "description": "<p>Identificador de la remesa creada en aplicaci\\u00F3n m\\u00F3vil. Opcional en caso de tratarse de origen PC.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "remesas.idCuenta",
            "description": "<p>Identificador de la cuenta a la que se asocia la remesa. Opcional en caso de tratarse de origen PC.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "remesas.banco",
            "description": "<p>Nombre del banco de la remesa. Obligatorio en caso de tratarse de origen PC.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "remesas.monto",
            "description": "<p>Monto de la remesa crear.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "remesas.noBoleta",
            "description": "<p>N\\u00FAmero de boleta de la remesa a crear.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "remesas.tasaCambio",
            "description": "<p>Tasa de cambio de la moneda del d\\u00EDa. Obligatorio en caso de tratarse de origen MOVIL.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"token\":\"WEB\",\n    \"codArea\":\"505\",\n    \"usuario\":\"usuario.pruebas\",\n    \"codDispositivo\": \"64E1CE4A48BF656974C376B7905F7E62\",\n    \"origen\":\"MOVIL\",\n    \"idJornada\":\"14\",\n    \"idDeuda\":\"\",\n    \"remesas\":[\n        {\n            \"idRemesa\":\"89\",\n            \"idCuenta\":\"4\",\n            \"banco\":\"\",\n            \"monto\":\"100\",\n            \"noBoleta\":\"A1\",\n            \"tasaCambio\":\"29.39\"\n        },\n        {\n            \"idRemesa\":\"90\",\n            \"idCuenta\":\"4\",\n            \"banco\":\"\",\n            \"monto\":\"1000\",\n            \"noBoleta\":\"A2\",\n            \"tasaCambio\":\"29.39\"\n        }\n    ]\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n  \"token\": \"WEB\",\n  \"respuesta\": {\n    \"codResultado\": \"200\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"Campos agregados exitosamente.\",\n    \"clase\": \"OperacionRemesa\",\n    \"metodo\": \"doPost\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"\"\n  },\n  \"datos\": [\n    {\n      \"idRemesa\": \"93\",\n      \"idApp\": \"1\"\n    },\n    {\n      \"idRemesa\": \"94\",\n      \"idApp\": \"2\"\n    }\n  ]\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n  \"token\":\"WEB\",\n  \"respuesta\": {\n    \"codResultado\": \"-711\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"El par\\u00E1metro de entrada \\\"monto\\\" esta vac\\u00EDo o no es dato num\\u00E9rico.\",\n    \"clase\": \"CtrlRemesa\",\n    \"metodo\": \"validarInput\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioOperacion.java",
    "groupTitle": "Remesa"
  },
  {
    "type": "POST",
    "url": "/consultasidra/getremesa/",
    "title": "[getRemesa]",
    "name": "getRemesa",
    "description": "<p>Servicio para consultar los datos de remesas creadas en Sidra.</p>",
    "group": "Remesa",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pais.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "token",
            "description": "<p>Token de autenticaci\\u00F3n de sesion de usuario.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codDispositivo",
            "description": "<p>c\\u00F3digo del dispositivo desde donde se realiza la operaci\\u00F3n, en caso de utilizarse la APP M\\u00F3VIL.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idRemesa",
            "description": "<p>Identificador de la remesa a consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idJornada",
            "description": "<p>Identificador de la jornada asociada a consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "noBoleta",
            "description": "<p>N\\u00FAmero de la boleta de remesa a consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "banco",
            "description": "<p>Nombre del banco de la cuenta asociada a consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idCuenta",
            "description": "<p>Identificador de la cuenta bancaria asociada a consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idDistribuidor",
            "description": "<p>Identificador del distribuidor a consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idTipo",
            "description": "<p>Identificador de la ruta o panel asociada a consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "tipo",
            "defaultValue": "RUTA o PANEL",
            "description": "<p>Nombre del tipo a consultar. Obligatorio \\u00FAnicamente si se ingresa idTipo.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idVendedor",
            "description": "<p>Identificador del vendedor asociado a consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "estado",
            "defaultValue": "ALTA o BAJA",
            "description": "<p>Estado de la remesa a consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "fechaInicio",
            "description": "<p>fecha desde la cual se desean obtener las remesas a consultar. Formato de fecha:yyyyMMdd.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "fechaFin",
            "description": "<p>fecha hasta la cual se desean obtener las remesas a consultar.Formato de fecha:yyyyMMdd.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"token\":\"WEB\",\n    \"codArea\":\"505\",\n    \"usuario\":\"usuario.pruebas\",\n    \"codDispositivo\": \"64E1CE4A48BF656974C376B7905F7E62\",\n    \"idRemesa\":\"\",\n    \"idJornada\":\"\",\n    \"noBoleta\":\"\",\n    \"banco\":\"\",\n    \"idCuenta\":\"\",\n    \"idDistribuidor\":\"\",\n    \"idTipo\":\"\",\n    \"tipo\":\"\",\n    \"idVendedor\":\"\",\n    \"estado\":\"\",\n    \"fechaInicio\":\"20160820\",\n    \"fechaFin\":\"20160906\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n  \"token\":\"WEB\",\n  \"respuesta\": {\n    \"codResultado\": \"202\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"Recursos recuperados exitosamente.\",\n    \"clase\": \"OperacionRemesa\",\n    \"metodo\": \"doGet\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"\"\n  },\n  \"datos\": {\n    \"idJornada\": \"10\",\n    \"estadoJornada\": \"FINALIZADA\",\n    \"fechaInicioJornada\": \"27/06/2016 12:13:22\",\n    \"estadoLiqJornada\": \"LIQUIDADA\",\n    \"fechaFinJornada\": \"28/06/2016 11:28:23\",\n    \"fechaLiqJornada\": \"28/06/2016 11:28:30\",\n    \"totalVentas\": \"260.9097\",\n    \"totaltarjetaCredito\": \"0\",\n    \"cantVentas\": \"3\",\n    \"remesas\": {\n      \"idRemesa\": \"2\",\n      \"origen\": \"MOVIL\",\n      \"idDistribuidor\": \"17\",\n      \"nombreDistribuidor\": \"Distribuidora Metro Mall\",\n      \"idVendedor\": \"2262\",\n      \"nombreVendedor\": \"pruebas.panama\",\n      \"idTipo\": \"15\",\n      \"tipo\": \"RUTA\",\n      \"nombreTipo\": \"METRO MALL ZONA SUR\",\n      \"monto\": \"200\",\n      \"noBoleta\": \"185\",\n      \"banco\": \"Banco de Occidente\",\n      \"idCuenta\": \"3\",\n      \"noCuenta\": \"46794697164\",\n      \"tipoCuenta\": \"Monetaria\",\n      \"nombreCuenta\": \"Cuenta de Ahorro\",\n      \"estado\": \"BAJA\",\n      \"creado_el\": \"04/07/2016 16:02:29\",\n      \"creado_por\": \"usuario.pruebas\",\n      \"modificado_el\": \"\",\n      \"modificado_por\": \"\"\n    }\n  }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"-714\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"No se encontraron datos de remesas.\",\n    \"clase\": \"OperacionRemesa\",\n    \"metodo\": \"doGet\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioConsulta.java",
    "groupTitle": "Remesa"
  },
  {
    "type": "POST",
    "url": "/opsidra/modremesa/",
    "title": "[modremesa]",
    "name": "modremesa",
    "description": "<p>Servicio para modificar remesas creadas en Sidra.</p>",
    "group": "Remesa",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "token",
            "description": "<p>Token de autenticaci\\u00F3n de sesion de usuario.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs en el que se desea realizar la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codDispositivo",
            "description": "<p>c\\u00F3digo del dispositivo desde donde se realiza la operaci\\u00F3n, en caso de utilizarse la APP M\\u00F3VIL.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idRemesa",
            "description": "<p>Identificador de la remesa a eliminar. Excluyente con idDeuda.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idDeuda",
            "description": "<p>Identificador de la solicitud de tipo DEUDA de la que se desea eliminar las remesas. Excluyente con idRemesa.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"token\":\"WEB\",\n    \"codArea\":\"505\",\n    \"usuario\":\"usuario.pruebas\",\n    \"codDispositivo\": \"64E1CE4A48BF656974C376B7905F7E62\",\n    \"idRemesa\":\"5\",\n    \"idDeuda\":\"\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n  \"token\":\"WEB\",\n  \"respuesta\": {\n    \"codResultado\": \"201\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"Recurso modificado exitosamente.\",\n    \"clase\": \"OperacionRemesa\",\n    \"metodo\": \"doPut\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n  \"token\":\"WEB\",\n  \"respuesta\": {\n    \"codResultado\": \"-710\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"El par\\u00E1metro de entrada \\\"idRemesa\\\" esta vac\\u00EDo o no es dato num\\u00E9rico.\",\n    \"clase\": \"CtrlRemesa\",\n    \"metodo\": \"validarInput\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioOperacion.java",
    "groupTitle": "Remesa"
  },
  {
    "type": "POST",
    "url": "/consultasidra/generareportexz/",
    "title": "[generareportexz]",
    "name": "generareportexz",
    "description": "<p>Servicio para generar reporte x o z. El cual contiene un resumen del total de ventas  por dispositivo  en caso de ser reporte z o vendedor en caso de ser reporte x</p>",
    "group": "Reportes",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs en el que se desea realizar la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "token",
            "description": "<p>Token de validaci\\u00F3n de autenticaci\\u00F3n de usuario.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codDispositivo",
            "description": "<p>C\\u00F3digo del dispositivo para validaci\\u00F3n de token.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idJornada",
            "description": "<p>Identificador de la jornada de la que se desea obtener el reporte</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idVendedor",
            "description": "<p>Identificador del vendedor del cual se desea generar reporte X, este campo solo es obligatorio para reporte X.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "dispositivo",
            "description": "<p>listado de C\\u00F3digos de dispositivos de los cuales se desea generar reporte Z.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "tipoReporte",
            "description": "<p>Describe si el reporte a generar ser\\u00E1 X o Z.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "fecha",
            "description": "<p>fecha de la cual se desea generar el reporte.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "origen",
            "description": "<p>indica si se esta solicitando el reporte desde la app m\\u00F3vil o Web.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idReporte",
            "description": "<p>identificador del reporte a consultar.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": " {\n \"token\":\"WEB\",\n \"codArea\":\"505\",\n \"usuario\":\"usuario123\",\n \"codDispositivo\":\"\",\n \"idJornada\":\"\",\n \"idVendedor\":\"\",\n \"dispositivos\":[\"DSHFKJS894654654DS\",\"SDFJ98UJMKLSDFS\"],\n \"tipoReporte\":\"\",\n \"fecha\":\"\",\n \"origen\":\"PC/MOVIL\",\n \"idReporte\":\"\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "    {\n    \"token\":\"SDSFDSFSDFSFD\",\n    \"respuesta\": {\n        \"codResultado\": \"12\",\n        \"mostrar\": \"0\",\n        \"descripcion\": \"OK. Datos obtenidos exitosamente\",\n        \"clase\": \" \",\n        \"metodo\": \" \",\n        \"excepcion\": \" \",\n        \"tipoExepcion\": \"\"\n    },\n    \"datos\": {\n        \"idReporte\": \"8\",\n        \"tipo\": \"Z\",\n        \"acumuladoVentas\": \"52.5197\",\n        \"fecha\": \"26/07/2016 00:00:00\",\n        \"ventas\": [\n            {\n                \"descripcion\": \"FONDOS INICIALES\",\n                \"monto\": \"52.5197\",\n                \"cantVentas\": \"2\"\n            },\n            {\n                \"descripcion\": \"DEVOLUCIONES\",\n                \"monto\": \"0\",\n                \"cantVentas\": \"0\"\n            }\n        ],\n        \"totalVentasNetas\": \"52.5197\",\n        \"transaccionesMonetarias\": [\n            {\n                \"descripcion\": \"FONDOS INICIALES\",\n                \"monto\": \"0\"\n            },\n            {\n                \"descripcion\": \"DINERO EN GAVETA\",\n                \"monto\": \"52.5197\"\n            },\n            {\n                \"descripcion\": \"ENTREGA PARCIAL\",\n                \"monto\": \"0\",\n                \"cantVentas\": \"0\"\n            },\n            {\n                \"descripcion\": \"PAGOS A TERCEROS\",\n                \"monto\": \"0\",\n                \"cantVentas\": \"0\"\n            }\n        ],\n        \"otrasTransacciones\": [\n            {\n                \"descripcion\": \"DESCUENTOS\",\n                \"monto\": \"0\",\n                \"cantVentas\": \"0\"\n            },\n            {\n                \"descripcion\": \"ANULACIONES\",\n                \"monto\": \"0\",\n                \"cantVentas\": \"0\"\n            },\n            {\n                \"descripcion\": \"NO VENTAS\",\n                \"monto\": \"0\",\n                \"cantVentas\": \"0\"\n            }\n        ],\n        \"formasPago\": [\n            {\n                \"formaPago\": \"EFECTIVO\",\n                \"cantidad\": \"2\",\n                \"monto\": \"28.9197\"\n            },\n            {\n                \"formaPago\": \"CHEQUE\",\n                \"monto\": \"0\"\n            },\n            {\n                \"formaPago\": \"CREDITO\",\n                \"cantidad\": \"1\",\n                \"monto\": \"23.6\"\n            }\n        ],\n        \"totales\": [\n            {\n                \"descripcion\": \"TOTAL GRAVADO\",\n                \"monto\": \"0\"\n            },\n            {\n                \"descripcion\": \"TOTAL EXENTO\",\n                \"monto\": \"52.5197\"\n            },\n            {\n                \"descripcion\": \"TOTAL NO SUJETO\",\n                \"monto\": \"0\"\n            }\n        ],\n        \"totalVenta\": \"52.5197\"\n    }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "{} JSON Respuesta-Error:     ",
          "content": "{\n    \"respuesta\": {\n        \"codResultado\": \"-67\",\n        \"mostrar\": \"1\",\n        \"descripcion\": \"El par\\u00E1metro de entrada \\\\\\\"codigoDispositivo\\\"\\\\ esta vac\\u00EDo\",\n        \"clase\": \"CtrlReporteXZ\",\n        \"metodo\": \"validarDatos\",\n        \"excepcion\": \" \",\n        \"tipoExepcion\": \"Generales\"\n    }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioConsulta.java",
    "groupTitle": "Reportes"
  },
  {
    "type": "POST",
    "url": "/consultasidra/getreporteefectividadventa/",
    "title": "[getreporteefectividadventa]",
    "name": "getReporteEfectividadVenta",
    "description": "<p>Servicio para obtener un listado de las ventas realizadas por cada Punto de Venta, en cada punto de venta se realiza una comparaci\\u00F3n de ventas segun las fechas que se envien de dos meses distintos.</p>",
    "group": "Reportes",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs en el que se desea realizar la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idDistribuidor",
            "description": "<p>Identificador del distribuidor que se desea obtener. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idBodegaVirtual",
            "description": "<p>Identificador de la bodega virtual de la que se vendieron los articulos a consultar. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idJornada",
            "description": "<p>Identificador de la jornada a consultar. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idRuta",
            "description": "<p>Identificador de la ruta que se desea obtener. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "zona",
            "description": "<p>Describe si la Zona de los puntos de venta a consultar. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "fechaInicioMesActual",
            "description": "<p>Fecha inicio en formato yyyyMMdd que corresponde al Mes que se tomar\\u00E1 como base para realizar la comparaci\\u00F3n de venta.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "fechaFinMesActual",
            "description": "<p>Fecha fin en formato yyyyMMdd que corresponde al Mes que se tomar\\u00E1 como base para realizar la comparaci\\u00F3n de venta.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "fechaInicioMesAnterior",
            "description": "<p>Fecha inicio en formato yyyyMMdd que corresponde al Mes con el cual se comparar\\u00E1n las ventas.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "fechaFinMesAnterior",
            "description": "<p>Fecha fin en formato yyyyMMdd que corresponde al Mes con el cual se comparar\\u00E1n las ventas.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n   \"codArea\": \"505\",\n   \"usuario\": \"usuario.pruebas\",\n   \"fechaInicioMesActual\":\"20170601\",\n   \"fechaFinMesActual\":\"20160730\",\n   \"fechaInicioMesAnterior\":\"20160601\",\n   \"fechaFinMesAnterior\":\"20160630\",\n   \"idDistribuidor\": \"22\",\n   \"idBodegaVirtual\": \"\",\n   \"idRuta\": \"\",\n   \"idJornada\": \"\",\n   \"zona\": \"\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n  \"respuesta\": {\n     \"codResultado\": \"12\",\n     \"mostrar\": \"0\",\n     \"descripcion\": \"OK. Datos obtenidos exitosamente\",\n     \"clase\": \" \",\n     \"metodo\": \" \",\n     \"excepcion\": \" \",\n     \"tipoExepcion\": \"\"\n  },\n  \"datosEfectividad\": {\n     \"idDistribuidor\": \"22\",\n     \"nombreDistribuidor\": \"DTS TECNOLOGIA S.A\",\n     \"idBodegaVirtual\": \"119\",\n     \"nombreBodega\": \"BODEGA RUTA 25\",\n     \"idRuta\": \"25\",\n     \"nombreRuta\": \"RUTA BOCAS DEL TORO 1\",\n     \"dpv\": {\n        \"idPuntoVenta\": \"61\",\n        \"departamento\": \"BOCAS DEL TORO\",\n        \"municipio\": \"BOCAS DEL TORO\",\n        \"distrito\": \"\",\n        \"nombrePdv\": \"TIENDA IV\\u00E1NCITO\",\n        \"comparacion\":          {\n           \"diaSemana\": \"VIERNES  \",\n           \"diaMes\": \"29\",\n           \"mes\": \"07\",\n           \"anio\": \"2016\",\n           \"venta\": \"SI\",\n           \"cantProductoFacturadoActual\": \"5\",\n           \"cantProductoFacturadoAnterior\": \"0\",\n           \"montoTotalFacturadoActual\": \"30\",\n           \"montoTotalFacturadoAnterior\": \"0\",\n           \"diferencia\": \"30\",\n           \"variacion\": \"100\",\n           \"detalleVenta\":             {\n              \"cantidadVendida\": \"5\",\n              \"montoTotalVendido\": \"30\",\n              \"tipoProducto\": \"TARJETASRASCA\"\n           }\n        }\n     }\n  }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "{} JSON Respuesta-Error:     ",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"-199\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"Los siguientes datos faltan o son incorrectos:  Usuario.\",\n    \"clase\": \"CtrlReporte\",\n    \"metodo\": \"validarInput\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioConsulta.java",
    "groupTitle": "Reportes"
  },
  {
    "type": "POST",
    "url": "/consultasidra/getreporteinventariovendido/",
    "title": "[getreporteinventariovendido]",
    "name": "getReporteInventarioVendido",
    "description": "<p>Servicio para obtener un listado detalle de los articulos vendidos.</p>",
    "group": "Reportes",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs en el que se desea realizar la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idDistribuidor",
            "description": "<p>Identificador del distribuidor que se desea obtener. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idBodegaVirtual",
            "description": "<p>Identificador de la bodega virtual de la que se vendieron los articulos a consultar. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idJornada",
            "description": "<p>Identificador de la jornada a consultar. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idVendedor",
            "description": "<p>Identificador del vendedor asociado al punto de venta que se desea obtener. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idTipo",
            "description": "<p>Identificador de una ruta o panel especifica de la cual se dese consultar. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "tipo",
            "description": "<p>Describe si el idtipo corresponde a una ruta o panel. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "fechaInicio",
            "description": "<p>Fecha en formato yyyyMMdd desde la cu\\u00E1l se desea consultar la solicitud.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "fechaFin",
            "description": "<p>Fecha en formato yyyyMMdd hasta la cu\\u00E1l se desea consultar la solicitud.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n   \"codArea\": \"505\",\n   \"usuario\": \"usuario.pruebas\",\n   \"fechaInicio\":\"20160601\",\n   \"fechaFin\":\"20160701\",\n   \"idDistribuidor\": \"22\",\n   \"idBodegaVirtual\": \"\",\n   \"idJornada\": \"67\",\n   \"idVendedor\": \"\",\n   \"idTipo\": \"\",\n   \"tipo\": \"\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n  \"respuesta\":    {\n     \"codResultado\": \"12\",\n     \"mostrar\": \"0\",\n     \"descripcion\": \"OK. Datos obtenidos exitosamente\",\n     \"clase\": \" \",\n     \"metodo\": \" \",\n     \"excepcion\": \" \",\n     \"tipoExepcion\": \"\"\n  },\n  \"detalle\": [\n     {\n        \"tipoProducto\": \"SIMCARD\",\n        \"articulos\": {\n           \"idArticulo\": \"1618\",\n           \"descripcion\": \"Z-TARJETA SIM PREPAGO LINEA CON NUMERO\",\n           \"cantidadVendida\": \"1\",\n           \"montoTotalVendido\": \"10\"\n        }\n     },\n     {\n        \"tipoProducto\": \"TARJETASRASCA\",\n        \"articulos\": [\n           {\n              \"idArticulo\": \"665\",\n              \"descripcion\": \"TARJETA PREPAGO DE $2.00\",\n              \"cantidadVendida\": \"1\",\n              \"montoTotalVendido\": \"7.339\"\n           },\n           {\n              \"idArticulo\": \"674\",\n              \"descripcion\": \"TARJETA PREPAGO DE $5.00\",\n              \"cantidadVendida\": \"4\",\n              \"montoTotalVendido\": \"20\"\n           },\n           {\n              \"idArticulo\": \"675\",\n              \"descripcion\": \"TARJETA PREPAGO DE $10.00\",\n              \"cantidadVendida\": \"1\",\n              \"montoTotalVendido\": \"10\"\n           }\n        ]\n     }\n  ]\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "{} JSON Respuesta-Error:     ",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"-199\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"Los siguientes datos faltan o son incorrectos:  Usuario.\",\n    \"clase\": \"CtrlReporte\",\n    \"metodo\": \"validarInput\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioConsulta.java",
    "groupTitle": "Reportes"
  },
  {
    "type": "POST",
    "url": "/consultasidra/getreporterecargas/",
    "title": "[getreporterecargas]",
    "name": "getReporteRecargas",
    "description": "<p>Servicio para obtener un listado recargas vendidas,</p>",
    "group": "Reportes",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs en el que se desea realizar la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idDts",
            "description": "<p>Identificador del distribuidor que se desea obtener. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idBodega",
            "description": "<p>Identificador de la bodega virtual de la que se vendieron las recargas a consultar. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idTipo",
            "description": "<p>Identificador de la ruta o panel que se desea obtener. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "tipo",
            "defaultValue": "RUTA o PANEL",
            "description": "<p>Nombre del tipo a consultar. Obligatorio \\u00FAnicamente si se ingresa idTipo.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "fechaInicio",
            "description": "<p>Fecha inicio en formato yyyyMMdd desde donde se desea consultar las recargas.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "fechaFin",
            "description": "<p>Fecha fin en formato yyyyMMdd hasta donde se desea consultar las recargas.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo parametros de Entrada:",
          "content": "{\n\t\"codArea\": \"505\",\n\t\"usuario\": \"usuario.pruebas\",\n\t\"idDts\":\"\",\n\t\"idBodega\":\"\",\n\t\"idTipo\":\"\",\n\t\"tipo\":\"\",\n\t\"fechaInicio\":\"20170307\",\n\t\"fechaFin\": \"20170405\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "{} JSON Respuesta-\\u00E9xito:",
          "content": "{\n\t\t\"respuesta\": {\n  \t\t\"codResultado\": \"12\",\n \t\t\"mostrar\": \"0\",\n\t\t\t\"descripcion\": \"OK. Datos obtenidos exitosamente\",\n\t\t\t\"clase\": \" \",\n\t\t\t\"metodo\": \" \",\n\t\t\t\"excepcion\": \" \",\n  \t\t\"tipoExepcion\": \"\"\n},\n\"recargas\": [\n  {\n      \"jornada\": \"49\",\n      \"estadoLiquidacionJornada\": \"LIQUIDADA\",\n      \"vendedor\": \"diego.urbina\",\n      \"totalFacturado\": \"3\",\n      \"fecha\": \"07/03/2017 00:00:00\",\n      \"nombreDts\": \"Movilway\",\n      \"zona\": \"Promix Aux3\"\n  },\n  {\n      \"jornada\": \"57\",\n      \"estadoLiquidacionJornada\": \"LIQUIDADA\",\n      \"vendedor\": \"carlos.lopez\",\n      \"totalFacturado\": \"5\",\n      \"fecha\": \"07/03/2017 00:00:00\",\n      \"nombreDts\": \"ANA BAEZ\",\n      \"zona\": \"Ana baez, Llanos del sol\"\n  },\n  {\n      \"jornada\": \"58\",\n      \"estadoLiquidacionJornada\": \"\",\n      \"vendedor\": \"carlos.lopez\",\n      \"totalFacturado\": \"6\",\n      \"fecha\": \"07/03/2017 00:00:00\",\n      \"nombreDts\": \"ANA BAEZ\",\n      \"zona\": \"Ana baez, Llanos del sol\"\n  },\n  {\n      \"jornada\": \"92\",\n      \"estadoLiquidacionJornada\": \"LIQUIDADA\",\n      \"vendedor\": \"hugo.siles\",\n      \"totalFacturado\": \"1\",\n      \"fecha\": \"20/03/2017 00:00:00\",\n      \"nombreDts\": \"DISTRIBUIDOR LA ESPERANZA\",\n      \"zona\": \"Bodega Esperanza 1\"\n  },\n  {\n      \"jornada\": \"125\",\n      \"estadoLiquidacionJornada\": \"LIQUIDADA\",\n      \"vendedor\": \"hugo.siles\",\n      \"totalFacturado\": \"9\",\n      \"fecha\": \"04/04/2017 00:00:00\",\n      \"nombreDts\": \"DISTRIBUIDOR LA ESPERANZA\",\n      \"zona\": \"Bodega Esperanza 1\"\n  }\n ]\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "{} JSON Respuesta-Error:     ",
          "content": "{\n\t\"respuesta\": {\n  \t\"codResultado\": \"-61\",\n  \t\"mostrar\": \"1\",\n  \t\"descripcion\": \"Debe ingresar ambas fechas: Fecha inicio y fecha Fin\",\n  \t\"clase\": \"CtrlReporteRecarga\",\n  \t\"metodo\": \"validarInput\",\n  \t\"excepcion\": \" \",\n  \t\"tipoExepcion\": \"Generales\"\n\t}\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioConsulta.java",
    "groupTitle": "Reportes"
  },
  {
    "type": "POST",
    "url": "/consultasidra/getresumeninventariovendido/",
    "title": "[getresumeninventariovendido]",
    "name": "getResumenInventarioVendido",
    "description": "<p>Servicio para obtener un listado del resumen de los articulos vendidos del inventario agrupados por tipo de producto.</p>",
    "group": "Reportes",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs en el que se desea realizar la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idDistribuidor",
            "description": "<p>Identificador del distribuidor que se desea obtener. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idBodegaVirtual",
            "description": "<p>Identificador de la bodega virtual de la que se vendieron los articulos a consultar. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idJornada",
            "description": "<p>Identificador de la jornada a consultar. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idVendedor",
            "description": "<p>Identificador del vendedor asociado al punto de venta que se desea obtener. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idTipo",
            "description": "<p>Identificador de una ruta o panel especifica de la cual se dese consultar. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "tipo",
            "description": "<p>Describe si el idtipo corresponde a una ruta o panel. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "fechaInicio",
            "description": "<p>Fecha en formato yyyyMMdd desde la cu\\u00E1l se desea consultar la solicitud.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "fechaFin",
            "description": "<p>Fecha en formato yyyyMMdd hasta la cu\\u00E1l se desea consultar la solicitud.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n   \"codArea\": \"505\",\n   \"usuario\": \"usuario.pruebas\",\n   \"fechaInicio\":\"20160601\",\n   \"fechaFin\":\"20160701\",\n   \"idDistribuidor\": \"22\",\n   \"idBodegaVirtual\": \"\",\n   \"idJornada\": \"67\",\n   \"idVendedor\": \"\",\n   \"idTipo\": \"\",\n   \"tipo\": \"\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n  \"respuesta\":\n  {\n     \"codResultado\": \"12\",\n     \"mostrar\": \"0\",\n     \"descripcion\": \"OK. Datos obtenidos exitosamente\",\n     \"clase\": \" \",\n     \"metodo\": \" \",\n     \"excepcion\": \" \",\n     \"tipoExepcion\": \"\"\n  },\n  \"resumen\": [\n     {\n        \"cantidadVendida\": \"1\",\n        \"montoTotalVendido\": \"10\",\n        \"tipoProducto\": \"SIMCARD\"\n     },\n     {\n        \"cantidadVendida\": \"6\",\n        \"montoTotalVendido\": \"37.339\",\n        \"tipoProducto\": \"TARJETASRASCA\"\n     }\n  ]\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "{} JSON Respuesta-Error:     ",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"-199\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"Los siguientes datos faltan o son incorrectos:  Usuario.\",\n    \"clase\": \"CtrlReporte\",\n    \"metodo\": \"validarInput\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioConsulta.java",
    "groupTitle": "Reportes"
  },
  {
    "type": "POST",
    "url": "/consultasidra/getcontrolprecios/",
    "title": "[getcontrolprecios]",
    "name": "getcontrolprecios",
    "description": "<p>Servicio para obtener la cantidad de datos que puede retornar sobre el control de precios con los filtros enviados.</p>",
    "group": "Reportes",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs en el que se desea realizar la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idDistribuidor",
            "description": "<p>Identificador del distribuidor a consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idTipo",
            "description": "<p>Identificador de la ruta o panel a consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "tipo",
            "description": "<p>Describe si el idtipo corresponde a una ruta o panel.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "Vendedor",
            "description": "<p>Identificador del vendedor a consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idBodegaVirtual",
            "description": "<p>Identificador de la bodega virtual de la que se vendieron los articulos a consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "articulo",
            "description": "<p>Identificador del art\\u00EDculo a consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "fechaInicio",
            "description": "<p>Fecha en formato yyyyMMdd desde la cu\\u00E1l se desea consultar los art\\u00EDculos.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "fechaFin",
            "description": "<p>Fecha en formato yyyyMMdd hasta la cu\\u00E1l se desea consultar los articulos.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "max",
            "description": "<p>Registro m\\u00E1ximo  a consultar para pagineo .</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "min",
            "description": "<p>Registro m\\u00EDnimo a consultar para pagineo .</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"codArea\": \"505\",\n    \"usuario\": \"usuario.pruebas\",\n    \"idDistribuidor\": \"53\",\n    \"idTipo\": \"12\",\n    \"tipo\": \"PANEL\",\n    \"vendedor\": \"904\",\n    \"idBodegaVirtual\": \"\",\n    \"articulo\": \"\",\n    \"fechaInicio\": \"20160506\",\n    \"fechaFin\": \"20160515\",\n    \"max\":\"100\",\n    \"min\":\"1\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "    {\n    \"respuesta\": {\n        \"codResultado\": \"12\",\n        \"mostrar\": \"0\",\n        \"descripcion\": \"OK. Datos obtenidos exitosamente\",\n         \"clase\": \" \",\n        \"metodo\": \" \",\n        \"excepcion\": \" \",\n        \"tipoExepcion\": \"\"\n    },\n    \"articulos\": [\n        {\n            \"idVenta\": \"42\",\n            \"numero\": \"\",\n            \"serie\": \" \",\n            \"folioSidra\": \"11221\",\n            \"serieSidra\": \"a\",\n            \"tipoDocumento\": \"TCK\",\n            \"fecha\": \"27/07/2016 17:15:20\",\n            \"articulo\": \"665\",\n            \"descripcion\": \"TARJETA PREPAGO DE $2.00\",\n            \"cantidad\": \"1\",\n            \"serieArticulo\": \"206893415\",\n            \"precioInicial\": \"7.339\",\n            \"descuentoSCL\": \"0\",\n            \"descuentoSIDRA\": \"0\",\n            \"precioVenta\": \"7.339\"\n        },\n        {\n            \"idVenta\": \"42\",\n            \"numero\": \"\",\n            \"serie\": \" \",\n            \"folioSidra\": \"11221\",\n            \"serieSidra\": \"a\",\n            \"tipoDocumento\": \"TCK\",\n            \"fecha\": \"27/07/2016 17:15:20\",\n            \"articulo\": \"1618\",\n            \"descripcion\": \"Z-TARJETA SIM PREPAGO LINEA CON NUMERO\",\n            \"cantidad\": \"1\",\n            \"serieArticulo\": \"8950702110514468103\",\n            \"precioInicial\": \"10\",\n            \"descuentoSCL\": \"0\",\n            \"descuentoSIDRA\": \"0\",\n            \"precioVenta\": \"10\"\n         }\n    ]\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "{} JSON Respuesta-Error:     ",
          "content": "{\n  \"respuesta\": {\n      \"codResultado\": \"-62\",\n      \"mostrar\": \"1\",\n      \"descripcion\": \"La fecha fin debe ser mayor a la fecha inicio\",\n      \"clase\": \"CtrlPrecios\",\n      \"metodo\": \"validarDatos\",\n      \"excepcion\": \" \",\n      \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioConsulta.java",
    "groupTitle": "Reportes"
  },
  {
    "type": "POST",
    "url": "/consultasidra/getcountcontrolprecios/",
    "title": "[getcountcontrolprecios]",
    "name": "getcountcontrolprecios",
    "description": "<p>Servicio para obtener la cantidad de datos que puede retornar sobre el control de precios con los filtros enviados.</p>",
    "group": "Reportes",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs en el que se desea realizar la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idDistribuidor",
            "description": "<p>Identificador del distribuidor a consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idTipo",
            "description": "<p>Identificador de la ruta o panel a consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "tipo",
            "description": "<p>Describe si el idtipo corresponde a una ruta o panel.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "Vendedor",
            "description": "<p>Identificador del vendedor a consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idBodegaVirtual",
            "description": "<p>Identificador de la bodega virtual de la que se vendieron los articulos a consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "articulo",
            "description": "<p>Identificador del art\\u00EDculo a consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "fechaInicio",
            "description": "<p>Fecha en formato yyyyMMdd desde la cu\\u00E1l se desea consultar los art\\u00EDculos.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "fechaFin",
            "description": "<p>Fecha en formato yyyyMMdd hasta la cu\\u00E1l se desea consultar los articulos.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"codArea\": \"505\",\n    \"usuario\": \"usuario.pruebas\",\n    \"idDistribuidor\": \"53\",\n    \"idTipo\": \"12\",\n    \"tipo\": \"PANEL\",\n    \"vendedor\": \"904\",\n    \"idBodegaVirtual\": \"\",\n    \"articulo\": \"\",\n    \"fechaInicio\": \"20160506\",\n    \"fechaFin\": \"20160515\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n   \"respuesta\": {\n       \"codResultado\": \"12\",\n       \"mostrar\": \"0\",\n       \"descripcion\": \"OK. Datos obtenidos exitosamente\",\n       \"clase\": \" \",\n       \"metodo\": \" \",\n       \"excepcion\": \" \",\n       \"tipoExepcion\": \"\"\n   },\n   \"cantRegistros\": \"0\"\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "{} JSON Respuesta-Error:     ",
          "content": "{\n  \"respuesta\": {\n      \"codResultado\": \"-62\",\n      \"mostrar\": \"1\",\n      \"descripcion\": \"La fecha fin debe ser mayor a la fecha inicio\",\n      \"clase\": \"CtrlPrecios\",\n      \"metodo\": \"validarDatos\",\n      \"excepcion\": \" \",\n      \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioConsulta.java",
    "groupTitle": "Reportes"
  },
  {
    "type": "POST",
    "url": "/consultasidra/getcountcumplimientoventa/",
    "title": "[getcountcumplimientoventa]",
    "name": "getcountcumplimientoventa",
    "description": "<p>Servicio para obtener la cantidad de datos que puede retornar sobre el reporte cumplimiento de ventas con los filtros enviados.</p>",
    "group": "Reportes",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs en el que se desea realizar la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idDistribuidor",
            "description": "<p>Identificador del distribuidor que se desea obtener. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idRuta",
            "description": "<p>Id de la Ruta de la cual se quiere obtener la informaci\\u00F3n de visitas que se han realizado. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idJornada",
            "description": "<p>Identificador de la jornada a consultar. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idPuntoVenta",
            "description": "<p>Identificador del punto de venta a consultar. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "fechaInicio",
            "description": "<p>Fecha en formato yyyyMMdd desde la cu\\u00E1l se desea consultar la solicitud.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "fechaFin",
            "description": "<p>Fecha en formato yyyyMMdd hasta la cu\\u00E1l se desea consultar la solicitud.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n   \"codArea\": \"505\",\n   \"usuario\": \"usuario.pruebas\",\n   \"fechaInicio\":\"20160701\",\n   \"fechaFin\":\"20160727\",\n   \"idDistribuidor\": \"123\",\n   \"idJornada\": \"\",\n   \"idRuta\": \"\",\n   \"idPuntoVenta\": \"\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n   \"respuesta\": {\n       \"codResultado\": \"12\",\n       \"mostrar\": \"0\",\n       \"descripcion\": \"OK. Datos obtenidos exitosamente\",\n       \"clase\": \" \",\n       \"metodo\": \" \",\n       \"excepcion\": \" \",\n       \"tipoExepcion\": \"\"\n   },\n   \"cantRegistros\": \"2\"\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "{} JSON Respuesta-Error:     ",
          "content": "{\n  \"respuesta\": {\n      \"codResultado\": \"-62\",\n      \"mostrar\": \"1\",\n      \"descripcion\": \"La fecha fin debe ser mayor a la fecha inicio\",\n      \"clase\": \"CtrlReporte\",\n      \"metodo\": \"getReporteCumplimientoVenta\",\n      \"excepcion\": \" \",\n      \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioConsulta.java",
    "groupTitle": "Reportes"
  },
  {
    "type": "POST",
    "url": "/consultasidra/getreportecumplimientoventa/",
    "title": "[getreportecumplimientoventa]",
    "name": "getreportecumplimientoventa",
    "description": "<p>Servicio para obtener un listado de Puntos de Ventas, cada item de PDV contendr\\u00E1 dos listados m\\u00E1s que representan las ventas realizadas, un listado de articulos y un listado de tarjetas.</p>",
    "group": "Reportes",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs en el que se desea realizar la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idDistribuidor",
            "description": "<p>Identificador del distribuidor que se desea obtener. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idRuta",
            "description": "<p>Id de la Ruta de la cual se quiere obtener la informaci\\u00F3n de visitas que se han realizado. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idJornada",
            "description": "<p>Identificador de la jornada a consultar. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idPuntoVenta",
            "description": "<p>Identificador del punto de venta a consultar. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "fechaInicio",
            "description": "<p>Fecha en formato yyyyMMdd desde la cu\\u00E1l se desea consultar la solicitud.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "fechaFin",
            "description": "<p>Fecha en formato yyyyMMdd hasta la cu\\u00E1l se desea consultar la solicitud.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "max",
            "description": "<p>Registro m\\u00E1ximo  a consultar para pagineo .</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "min",
            "description": "<p>Registro m\\u00EDnimo a consultar para pagineo .</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n   \"codArea\": \"505\",\n   \"usuario\": \"usuario.pruebas\",\n   \"fechaInicio\":\"20160701\",\n   \"fechaFin\":\"20160727\",\n   \"min\": \"0\",\n   \"max\": \"10\"\n   \"idDistribuidor\": \"123\",\n   \"idJornada\": \"\",\n   \"idRuta\": \"\",\n   \"idPuntoVenta\": \"\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n   \"respuesta\":    {\n      \"codResultado\": \"12\",\n      \"mostrar\": \"0\",\n      \"descripcion\": \"OK. Datos obtenidos exitosamente\",\n      \"clase\": \" \",\n      \"metodo\": \" \",\n      \"excepcion\": \" \",\n      \"tipoExepcion\": \"\"\n   },\n   \"cumplVenta\":    {\n      \"fecha\": \"2016-07-27 17:15:20.0\",\n      \"idPuntoVenta\": \"61\",\n      \"nombreDistribuidor\": \"DTS TECNOLOGIA S.A\",\n      \"idBodegaVirtual\": \"119\",\n      \"nombreBodega\": \"BODEGA RUTA 25\",\n      \"departamento\": \"BOCAS DEL TORO\",\n      \"municipio\": \"BOCAS DEL TORO\",\n      \"distrito\": \"\",\n      \"idRuta\": \"25\",\n      \"nombreRuta\": \"RUTA BOCAS DEL TORO 1\",\n      \"idVendedor\": \"2462\",\n      \"usuario\": \"ruta.panama\",\n      \"nombrePdv\": \"TIENDA IV\\u00E1NCITO\",\n      \"telPrimario\": \"65980044\",\n      \"tipoProducto\": \"FISICO\",\n      \"tipoNegocio\": \"Tienda\",\n      \"idVenta\": \"42\",\n      \"articulosVendidos\": {\n         \"tipoProducto\": \"SIMCARD\",\n         \"cantidadSugerida\": \"0\",\n         \"montoSugerido\": \"0\",\n         \"cantidadFacturada\": \"1\",\n         \"montoFacturado\": \"10\"\n      },\n      \"tarjetas\": {\n         \"idArticulo\": \"665\",\n         \"descripcion\": \"TARJETA PREPAGO DE $2.00\",\n         \"cantidadSugerida\": \"0\",\n         \"montoSugerido\": \"0\",\n         \"cantidadFacturada\": \"1\",\n         \"montoFacturado\": \"7.339\"\n      }\n   }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "{} JSON Respuesta-Error:     ",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"-199\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"Los siguientes datos faltan o son incorrectos:  Usuario.\",\n    \"clase\": \"CtrlReporte\",\n    \"metodo\": \"validarInput\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioConsulta.java",
    "groupTitle": "Reportes"
  },
  {
    "type": "POST",
    "url": "/consultasidra/getreportecumplimientovisita/",
    "title": "[getreportecumplimientovisita]",
    "name": "getreportecumplimientovisita",
    "description": "<p>Servicio para obtener un listado de visitas que realiza un vendedor por ruta.</p>",
    "group": "Reportes",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs en el que se desea realizar la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idDistribuidor",
            "description": "<p>Identificador del distribuidor que se desea obtener. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idRuta",
            "description": "<p>Id de la Ruta de la cual se quiere obtener la informaci\\u00F3n de visitas que se han realizado. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idPuntoVenta",
            "description": "<p>Id del punto de venta del que se desean obtener visitas. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "departamento",
            "description": "<p>Nombre del deparamento en donde se encuentran los puntos de venta que se desean consultar. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "municipio",
            "description": "<p>Nombre del municipio en donde se encuentran los puntos de venta que se desean consultar. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "distrito",
            "description": "<p>Nombre del distrito en donde se encuentran los puntos de venta que se desean consultar. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "fechaInicio",
            "description": "<p>Fecha en formato yyyyMMdd desde la cu\\u00E1l se desea consultar la solicitud.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "fechaFin",
            "description": "<p>Fecha en formato yyyyMMdd hasta la cu\\u00E1l se desea consultar la solicitud.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n   \"codArea\": \"505\",\n   \"usuario\": \"usuario.pruebas\",\n   \"fechaInicio\":\"20160701\",\n   \"fechaFin\":\"20160727\",\n   \"idDistribuidor\": \"123\",\n   \"idRuta\": \"\",\n   \"idPuntoVenta\": \"\",\n   \"departamento\": \"\",\n   \"municipio\": \"\",\n   \"distrito\": \"\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n   \"respuesta\":    {\n      \"codResultado\": \"12\",\n      \"mostrar\": \"0\",\n      \"descripcion\": \"OK. Datos obtenidos exitosamente\",\n      \"clase\": \" \",\n      \"metodo\": \" \",\n      \"excepcion\": \" \",\n      \"tipoExepcion\": \"\"\n   },\n   \"cumplVisita\": [\n      {\n         \"codPvd\": \"60\",\n         \"codDistribuidor\": \"22\",\n         \"distribuidor\": \"DTS TECNOLOGIA S.A\",\n         \"codBodega\": \"114\",\n         \"bodega\": \"BODEGA RUTA 24\",\n         \"departamento\": \"CHIRIQU\\u00ED\",\n         \"municipio\": \"CHIRIQU\\u00ED\",\n         \"distrito\": \"\",\n         \"usuario\": \"99887768\",\n         \"vendedor\": \"961\",\n         \"ruta\": \"RUTA TECH S.A.\",\n         \"nombrePdv\": \"FARMACIA LA SALUD\",\n         \"dia\": \"27\",\n         \"mes\": \"07\",\n         \"anio\": \"2016\",\n         \"fecha\": \"2016-07-27 09:59:22.0\",\n         \"asignado\": \"NO\",\n         \"gestion\": \"VENTA\",\n         \"observaciones\": \" \"\n      },\n      {\n         \"codPvd\": \"59\",\n         \"codDistribuidor\": \"22\",\n         \"distribuidor\": \"DTS TECNOLOGIA S.A\",\n         \"codBodega\": \"114\",\n         \"bodega\": \"BODEGA RUTA 24\",\n         \"departamento\": \"COCL\\u00E9\",\n         \"municipio\": \"COCL\\u00E9\",\n         \"distrito\": \"\",\n         \"usuario\": \"99887768\",\n         \"vendedor\": \"961\",\n         \"ruta\": \"RUTA TECH S.A.\",\n         \"nombrePdv\": \"TIENDA MARY\",\n         \"dia\": \"27\",\n         \"mes\": \"07\",\n         \"anio\": \"2016\",\n         \"fecha\": \"2016-07-27 09:59:22.0\",\n         \"asignado\": \"NO\",\n         \"gestion\": \"NO VENTA\",\n         \"observaciones\": \"Tiene art\\u00EDculos en existencia.\"\n      }\n   ]\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "{} JSON Respuesta-Error:     ",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"-199\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"Los siguientes datos faltan o son incorrectos:  Usuario.\",\n    \"clase\": \"CtrlReporte\",\n    \"metodo\": \"validarInput\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioConsulta.java",
    "groupTitle": "Reportes"
  },
  {
    "type": "POST",
    "url": "/consultasidra/getreporteinvjornada/",
    "title": "[getreporteinvjornada]",
    "name": "getreporteinvjornada",
    "description": "<p>Servicio para obtener las diferentes cantidades de inventario.</p>",
    "group": "Reportes",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs en el que se desea realizar la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "token",
            "description": "<p>token de validaci\\u00F3n de autenticaci\\u00F3n de usuario.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codDispositivo",
            "description": "<p>c\\u00F3digo del dispositivo desde donde se realiza la operaci\\u00F3n, en caso de utilizarse la APP M\\u00F3VIL.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idDTS",
            "description": "<p>Identificador del distribuidor que se desea consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idJornada",
            "description": "<p>Identificador de la jornada que se desea consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idVendedor",
            "description": "<p>Identificador del vendedor que se desea consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idRutaPanel",
            "description": "<p>Identificador de la ruta o panel que se desea consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "tipoRutaPanel",
            "description": "<p>Tipo ruta o panel a consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idArticulo",
            "description": "<p>Identificador del art\\u00EDculo que se desea consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "tipoGrupo",
            "description": "<p>Tipo del grupo a consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "tipoInv",
            "description": "<p>Tipo de inventario a consultar.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"token\":\"WEB\",\n    \"codDispositivo\":\"\",\n    \"codArea\":\"505\",\n    \"usuario\":\"victor.cifuentes\",\n    \"idDTS\":\"25\",\n    \"idJornada\":\"\",\n    \"idVendedor\":\"\",\n    \"idRutaPanel\":\"\",\n    \"tipoRutaPanel\":\"\",\n    \"idArticulo\":\"\",\n    \"tipoGrupo\":\"\",\n    \"tipoInv\":\"\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n  \"token\": \"WEB\",\n  \"respuesta\": {\n    \"codResultado\": \"70\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"OK. Datos de inventario inicial obtenidos correctamente.\",\n    \"clase\": \"OperacionReporteCantInvJornada\",\n    \"metodo\": \"doGet\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"\"\n  },\n  \"datos\": {\n    \"idJornada\": \"253\",\n    \"idDTS\": \"48\",\n    \"nombreDTS\": \"DISTRIBUIDOR OCCIDENTAL\",\n    \"idVendedor\": \"2563\",\n    \"nombreVendedor\": \"El\\u00EDas Daniel Tobar \",\n    \"usuarioVendedor\": \"elias.tobar\",\n    \"idRutaPanel\": \"40\",\n    \"tipoRutaPanel\": \"RUTA\",\n    \"nombreRutaPanel\": \"EL\\u00EDAS TOBAR\",\n    \"articulos\": [\n      {\n        \"idArticulo\": \"27\",\n        \"descripcion\": \"PELOTA FUTBOL \",\n        \"tipoInv\": \"INV_SIDRA\",\n        \"cantInicial\": \"75\",\n        \"cantReservada\": \"0\",\n        \"cantVendida\": \"0\",\n        \"cantProcDevolucion\": \"2\",\n        \"cantDevuelta\": \"0\",\n        \"cantProcSiniestro\": \"0\",\n        \"cantSiniestrada\": \"0\",\n        \"cantFinal\": \"73\"\n      },\n      {\n        \"idArticulo\": \"1011\",\n        \"descripcion\": \"TARJETA PREPAGADA $1\",\n        \"tipoInv\": \"INV_TELCA\",\n        \"cantInicial\": \"11\",\n        \"cantReservada\": \"0\",\n        \"cantVendida\": \"2\",\n        \"cantProcDevolucion\": \"1\",\n        \"cantDevuelta\": \"0\",\n        \"cantProcSiniestro\": \"0\",\n        \"cantSiniestrada\": \"0\",\n        \"cantFinal\": \"8\"\n      },\n      {\n        \"idArticulo\": \"1285\",\n        \"descripcion\": \"KIT APPLE IPHONE 16GB BLANCO\",\n        \"tipoInv\": \"INV_TELCA\",\n        \"cantInicial\": \"3\",\n        \"cantReservada\": \"0\",\n        \"cantVendida\": \"0\",\n        \"cantProcDevolucion\": \"1\",\n        \"cantDevuelta\": \"0\",\n        \"cantProcSiniestro\": \"0\",\n        \"cantSiniestrada\": \"0\",\n        \"cantFinal\": \"2\"\n      }\n    ]\n  }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "{} JSON Respuesta-Error:     ",
          "content": "{\n  \"token\": \"WEB\",\n  \"respuesta\": {\n    \"codResultado\": \"-847\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"No se encontraron datos de inventario inicial.\",\n    \"clase\": \"OperacionReporteCantInvJornada\",\n    \"metodo\": \"doGet\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioConsulta.java",
    "groupTitle": "Reportes"
  },
  {
    "type": "POST",
    "url": "/consultasidra/getreportepdv/",
    "title": "[getreportepdv]",
    "name": "getreportepdv",
    "description": "<p>Servicio para obtener los datos de ventas registradas por punto de venta.</p>",
    "group": "Reportes",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs en el que se desea realizar la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idDistribuidor",
            "description": "<p>Identificador del distribuidor que se desea consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idPDV",
            "description": "<p>Identificador del punto de venta que se desea consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idVendedor",
            "description": "<p>Identificador del vendedor que se desea consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idRuta",
            "description": "<p>Identificador de la ruta que se desea consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "categoria",
            "description": "<p>Categor\\u00EDa del punto de venta que se desea consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "departamento",
            "description": "<p>Departamento del punto de venta que se desea consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "municipio",
            "description": "<p>Municipio del punto de venta que se desea consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "distrito",
            "description": "<p>Distrito del punto de venta que se desea consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "estado",
            "description": "<p>Estado del punto de venta que se desea consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "fechaDesde",
            "description": "<p>Fecha desde la cual se desea consultar en formato dd/MM/yyyy.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "fechaHasta",
            "description": "<p>Fecha final se desea consultar en formato dd/MM/yyyy, el rango m\\u00E1ximo es de 6 meses (180 d\\u00EDas)</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"codArea\":\"505\",\n    \"usuario\":\"victor.cifuentes\",\n    \"idDistribuidor\":\"\",\n    \"idPDV\":\"\",\n    \"idVendedor\":\"\",\n    \"idRuta\":\"\",\n    \"categoria\": \"\",\n    \"departamento\": \"\",\n    \"municipio\": \"\",\n    \"distrito\": \"\",\n    \"estado\": \"\",\n    \"fechaDesde\":\"01/06/2016\",\n    \"fechaHasta\":\"31/08/2016\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"202\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"Recursos recuperados exitosamente.\",\n    \"clase\": \"OperacionReportePDV\",\n    \"metodo\": \"doGet\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"\"\n  },\n  \"datos\": {\n    \"idDistribuidor\": \"25\",\n    \"nombreDistribuidor\": \"Distribuidor Celular Center\",\n    \"puntosDeVenta\": {\n      \"idDistribuidor\": \"25\",\n      \"idPDV\": \"59\",\n      \"nombrePDV\": \"TIENDA MARY\",\n      \"idRuta\": \"24\",\n      \"nombreRuta\": \"RUTA TECH S.A.\",\n      \"idVendedor\": \"961\",\n      \"canal\": \"MULTIMARCA\",\n      \"subcanal\": \"DTS\",\n      \"categoria\": \"B\",\n      \"departamento\": \"DARI\\u00E9N\",\n      \"municipio\": \"CHEPIGANA\",\n      \"distrito\": \"\",\n      \"estado\": \"ACTIVO\",\n      \"creado_por\": \"susana.barrios\",\n      \"creado_el\": \"27/07/2016 10:26:55\",\n      \"ventas\": [\n        {\n          \"anio\": \"2016\",\n          \"mes\": \"Junio\",\n          \"sumFacturacion\": \"0\"\n        },\n        {\n          \"anio\": \"2016\",\n          \"mes\": \"Julio\",\n          \"sumFacturacion\": \"0\"\n        },\n        {\n          \"anio\": \"2016\",\n          \"mes\": \"Agosto\",\n          \"sumFacturacion\": \"208.39\"\n        }\n      ]\n    }\n  }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "{} JSON Respuesta-Error:     ",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"-138\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \" Datos no num\\u00E9ricos en ID Vendedor.\",\n    \"clase\": \"CtrlReportePDV\",\n    \"metodo\": \"validarInput\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioConsulta.java",
    "groupTitle": "Reportes"
  },
  {
    "type": "POST",
    "url": "/opsidra/asociarutapdv/",
    "title": "[asociarutapdv]",
    "name": "asociarutapdv",
    "description": "<p>Servicio para anular ventas registradas en Sidra.</p>",
    "group": "Rutas",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs del que se desea obtener informaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idRuta",
            "description": "<p>Id de la ruta a la que se realizar\\u00E1 la asocici\\u00F3n de pdv's.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "pdv",
            "description": "<p>lista de pdv's a asociar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "asociacion",
            "defaultValue": "1 o 0",
            "description": "<p>Con valor 1 se realiza asociaci\\u00F3n. Con valor 0 se elimina asociaci\\u00F3n.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"codArea\":\"505\",\n    \"usuario\":\"usuario.pruebas\",\n    \"idRuta\":\"4\",\n    \"pdv\":[1,24,25],\n    \"asociacion\":\"1\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"23\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"Ok. Asociacion de punto de venta a ruta, realizada correctamente.\",\n    \"clase\": \"\",\n    \"metodo\": \"\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"-539\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"El valor para el campo asociaci\\u00F3n es inv\\u00E1lido.\",\n    \"clase\": \"CtrlRutaPdv\",\n    \"metodo\": \"asociaRutaPdv\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioOperacion.java",
    "groupTitle": "Rutas"
  },
  {
    "type": "POST",
    "url": "/opsidra/crearuta/",
    "title": "[creaRuta]",
    "name": "creaRuta",
    "description": "<p>Servicio para crear Rutaes por pa\\u00EDs.</p>",
    "group": "Rutas",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs del que se desea obtener informaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operacion.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idDTS",
            "description": "<p>Identificador del distribuidor asociado a la ruta que se desea crear.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "nombreRuta",
            "description": "<p>Nombre de la ruta que se desea crear.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "secUsuarioId",
            "description": "<p>Identificador del vendedor o usuario del modulo de seguridad asociado.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idBodegaVirtual",
            "description": "<p>Identificador de la bodega virtual.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "estado",
            "description": "<p>Estado de la ruta, por defecto ser\\u00E1 ALTA.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"codArea\": \"505\",\n    \"usuario\": \"usuario.pruebas\",\n    \"idDTS\": \"1\",\n    \"nombreRuta\": \"RUTA 1\",\n    \"secUsuarioId\": \"1\",\n    \"idBodegaVirtual\": \"1\",\n    \"estado\": \"\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"200\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"Campos agregados exitosamente.\",\n    \"clase\": \"OperacionRuta\",\n    \"metodo\": \"doPost\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n\t \"idRuta\":\"10\",\n  \"respuesta\": {\n    \"codResultado\": \"-200\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"Ya existe el recurso enviado. El ID de Usuario ya ha sido registrado.\",\n    \"clase\": \"OperacionRuta\",\n    \"metodo\": \"doPost\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioOperacion.java",
    "groupTitle": "Rutas"
  },
  {
    "type": "POST",
    "url": "/opsidra/bajaruta/",
    "title": "[delRuta]",
    "name": "delRuta",
    "description": "<p>Servicio para dar de baja una rutas de configuraciones por pa\\u00EDs.</p>",
    "group": "Rutas",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs del que se desea obtener informaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operacion.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idDTS",
            "description": "<p>Identificador de la ruta que se desea dar de baja.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"codArea\": \"505\",\n    \"usuario\": \"usuario.pruebas\",\n    \"idRuta\": \"1\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n   \"respuesta\": {\n       \"codResultado\": \"201\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"Recurso modificado exitosamente.\",\n       \"clase\": \"OperacionRuta\",\n       \"metodo\": \"doPutDel\",\n       \"excepcion\": \" \",\n       \"tipoExepcion\": \"\"\n   }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n   \"respuesta\": {\n       \"codResultado\": \"-201\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"No existe el recurso deseado.\",\n       \"clase\": \"OperacionRuta\",\n       \"metodo\": \"doPutDel\",\n       \"excepcion\": \" \",\n       \"tipoExepcion\": \"\"\n   }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioOperacion.java",
    "groupTitle": "Rutas"
  },
  {
    "type": "POST",
    "url": "/opsidra/eliminarutapdv/",
    "title": "[eliminarutapdv]",
    "name": "eliminarutapdv",
    "description": "<p>Servicio para anular ventas registradas en Sidra.</p>",
    "group": "Rutas",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs del que se desea obtener informaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idRuta",
            "description": "<p>Id de la ruta a la que se eliminar\\u00E1 la asocici\\u00F3n de pdv's.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "pdv",
            "description": "<p>lista de pdv's a desasignar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "asociacion",
            "defaultValue": "1 o 0",
            "description": "<p>Con valor 1 se realiza asociaci\\u00F3n. Con valor 0 se elimina asociaci\\u00F3n.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"codArea\":\"505\",\n    \"usuario\":\"usuario.pruebas\",\n    \"idRuta\":\"4\",\n    \"pdv\":[1,24,25],\n    \"asociacion\":\"1\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"24\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"Ok. Asociaci\\u00F3n de punto de venta y ruta, eliminada correctamente.\",\n    \"clase\": \"\",\n    \"metodo\": \"\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"-539\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"El valor para el campo asociaci\\u00F3n es inv\\u00E1lido.\",\n    \"clase\": \"CtrlRutaPdv\",\n    \"metodo\": \"asociaRutaPdv\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioOperacion.java",
    "groupTitle": "Rutas"
  },
  {
    "type": "POST",
    "url": "/consultasidra/getRuta/",
    "title": "[getRuta]",
    "name": "getRuta",
    "description": "<p>Servicio para obtener los datos de Rutas configuradas.</p>",
    "group": "Rutas",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs del que se desea obtener informaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operacion.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idRuta",
            "description": "<p>Identificador de la ruta que se desea obtener.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idDTS",
            "description": "<p>Identificador del distribuidor que se desea obtener.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "nombreRuta",
            "description": "<p>Nombre de la ruta que se desea obtener.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "secUsuarioId",
            "description": "<p>Identificador del vendedor o usuario del modulo de seguridad.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idBodegaVirtual",
            "description": "<p>Identificador de la bodega virtual que se desea consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "estado",
            "description": "<p>Estado del cu\\u00E1l se desea obtener Rutas (ALTA o BAJA).</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"codArea\": \"505\",\n    \"usuario\": \"usuario.pruebas\",\n    \"idRuta\": \"\",\n    \"idDTS\": \"\",\n    \"nombreRuta\": \"\",\n    \"secUsuarioId\": \"\",\n    \"idBodegaVirtual\": \"\",\n    \"estado\": \"\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n   \"respuesta\": {\n       \"codResultado\": \"0\",\n       \"mostrar\": \"0\",\n       \"descripcion\": \"Recursos recuperados exitosamente.\"\n   },\n   \"ruta\": {\n       \"idBodegaVirtual\": \"26\",\n       \"idRuta\": \"1\",\n       \"nombreRuta\": \"RUTA 1\",\n       \"idDTS\": \"1\",\n       \"nombreDTS\": \"Mariel L\\u00F3pez\",\n       \"tipoDTS\": \"INTERNO\",\n       \"secUsuarioId\": \"1\",\n       \"idBodegaVendedor\": \"63\",\n       \"estado\": \"ALTA\",\n       \"creado_el\": \"25/10/2016 14:43:52\",\n       \"creado_por\": \"usuario.pruebas\",\n       \"datosVendedor\": {\n          \"idVendPanelPDV\": \"\",\n          \"vendedor\": \"2622\",\n          \"nombre\": \"bernabe.santos\",\n          \"cantInventario\": \"12\"\n       },\n       \"puntoVenta\": {\n          \"idPDV\": \"3\",\n          \"nombrePDV\": \"TIENDA LUCY\"\n       }\n   }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n   \"respuesta\": {\n       \"codResultado\": \"-205\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"No existen registros con esos par\\u00E1metros.\",\n       \"clase\": \"OperacionRuta\",\n       \"metodo\": \"doGet\",\n       \"excepcion\": \" \",\n       \"tipoExepcion\": \" \"\n   }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioConsulta.java",
    "groupTitle": "Rutas"
  },
  {
    "type": "POST",
    "url": "/opsidra/modificaruta/",
    "title": "[modificaRuta]",
    "name": "modificaRuta",
    "description": "<p>Servicio para modificar rutas creadas.</p>",
    "group": "Rutas",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs del que se desea obtener informaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operacion.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idRuta",
            "description": "<p>Identificador de la ruta que se desea modificar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idDTS",
            "description": "<p>Identificador del distribuidor nuevo a modificar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "nombreRuta",
            "description": "<p>Nombre a modificar de la ruta.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "secUsuarioId",
            "description": "<p>Identificador del vendedor o usuario del modulo de seguridad.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idBodegaVirtual",
            "description": "<p>Identificador de la bodega virtual.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "estado",
            "description": "<p>Estado nuevo de la ruta (ALTA o BAJA).</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"codArea\": \"505\",\n    \"usuario\": \"usuario.pruebas\",\n    \"idRuta\": \"1\",\n    \"idDTS\": \"1\",\n    \"nombreRuta\": \"RUTA 1\",\n    \"secUsuarioId\": \"1\",\n    \"idBodegaVirtual\": \"1\",\n    \"estado\": \"ALTA\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n   \"respuesta\": {\n       \"codResultado\": \"201\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"Recurso modificado exitosamente.\",\n       \"clase\": \"OperacionRuta\",\n       \"metodo\": \"doPutDel\",\n       \"excepcion\": \" \",\n       \"tipoExepcion\": \"\"\n   }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n   \"respuesta\": {\n       \"codResultado\": \"-199\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"Los siguientes datos faltan o son incorrectos:  Datos no num\\u00FAricos en Ficha Cliente. Longitud del N\\u00FAmero (8).\",\n       \"clase\": \"CtrlRuta\",\n       \"metodo\": \"validarInput\",\n       \"excepcion\": \" \",\n       \"tipoExepcion\": \"\"\n   }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioOperacion.java",
    "groupTitle": "Rutas"
  },
  {
    "type": "POST",
    "url": "/consultasidra/getfoliosscl/",
    "title": "[getfoliosscl]",
    "name": "getfoliosscl",
    "description": "<p>Servicio para obtener la fecha de cierre de la jornada de un vendedor.</p>",
    "group": "SCL",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs en el que se desea realizar la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codOficina",
            "description": "<p>C\\u00F3digo de la oficina del vendedor que se desea consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idDTS",
            "description": "<p>Identificador del distribuidor que se desea consultar.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"codArea\": \"505\",\n    \"usuario\": \"victor.cifuentes\",\n    \"codOficina\": \"\",\n    \"idDTS\": \"\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"73\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"OK. Folios SCL obtenidos correctamente.\",\n    \"clase\": \"OperacionFoliosSCL\",\n    \"metodo\": \"doGet\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"\"\n  },\n  \"folios\": {\n    \"tipoDocumento\": \"FACTURA CONTADO\",\n    \"serie\": \"PA\",\n    \"noInicialFolio\": \"189999999\",\n    \"noFinalFolio\": \"199999999\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "{} JSON Respuesta-Error:     ",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"-851\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"El par\\u00E1metro de entrada \\\"codVendedor\\\" esta vac\\u00EDo.\",\n    \"clase\": \"CtrlFoliosSCL\",\n    \"metodo\": \"validarInput\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioConsulta.java",
    "groupTitle": "SCL"
  },
  {
    "type": "POST",
    "url": "/consultasidra/getoficinasscl/",
    "title": "[getoficinasscl]",
    "name": "getoficinasscl",
    "description": "<p>Servicio para obtener las oficinas de SCL.</p>",
    "group": "SCL",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs en el que se desea realizar la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"codArea\": \"505\",\n    \"usuario\": \"victor.cifuentes\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"74\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"OK. Oficinas SCL obtenidas correctamente.\",\n    \"clase\": \"OperacionOficinasSCL\",\n    \"metodo\": \"doGet\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"\"\n  },\n  \"oficinas\": [\n    {\n      \"nombres\": \"SUCURSAL PRUEBA\",\n      \"codOficina\": \"16\"\n    },\n    {\n      \"nombres\": \"SUCURSAL LOS ANDES\",\n      \"codOficina\": \"13\"\n    }\n  ]\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "{} JSON Respuesta-Error:     ",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"-868\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"No se encontraron datos de oficinas SCL.\",\n    \"clase\": \"OperacionOficinasSCL\",\n    \"metodo\": \"doGet\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioConsulta.java",
    "groupTitle": "SCL"
  },
  {
    "type": "POST",
    "url": "/consultasidra/getSolicitudescip/",
    "title": "[getSolicitudescip]",
    "name": "getsolicitudescip",
    "description": "<p>.</p>",
    "group": "SOLICITUDES_CIP",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "getsolicitudessip": [
          {
            "group": "getsolicitudessip",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>codArea C\\u00F3digo de area del pa\\u00EDs.</p>"
          },
          {
            "group": "getsolicitudessip",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>usuario de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "getsolicitudessip",
            "type": "String",
            "optional": false,
            "field": "codDispositivo",
            "description": "<p>codDispositivo del usuario que solicita la informacion.</p>"
          },
          {
            "group": "getsolicitudessip",
            "type": "String",
            "optional": false,
            "field": "token",
            "description": "<p>token de validaci\\u00F3n de autenticaci\\u00F3n de usuario.</p>"
          },
          {
            "group": "getsolicitudessip",
            "type": "String",
            "optional": false,
            "field": "numeroAportar",
            "description": "<p>N\\u00FAmero de cliente a proporcionar.</p>"
          },
          {
            "group": "getsolicitudessip",
            "type": "String",
            "optional": false,
            "field": "tipoDocumento",
            "description": "<p>Descripci\\u00F3n del documento de identificaci\\u00F3n del cliente.</p>"
          },
          {
            "group": "getsolicitudessip",
            "type": "String",
            "optional": false,
            "field": "numeroDocumento",
            "description": "<p>N\\u00FAmero del documento.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo parametros de Entrada:",
          "content": "  { \t\n\t\"codArea\":\"503\",\n\t\"usuario\":\"pruebas\",\n\t\"codDispositivo\":\"456483\",\n\t\"token\":\"WEB\",\n\t\"numeroaPortar\":\"12345678\",\n\t\"tipoDocumento\":\"PAS\",\n\t\"numeroDocumento\":\"16545\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "{} JSON Respuesta-\\u00E9xito:",
          "content": "\t{\n\t\t\"token\":\"WEB\",\n     \"respuesta\": {\n        \"codResultado\":\"203\",\n        \"mostrar\":\"0\",\n        \"descripcion\":\"OK Servicio Solicitudes Cip.\"\n         \"clase\": \"CtrlCip\",\n         \"metodo\": \"getDatos\",\n         \"excepcion\": \" \",\n         \"tipoExepcion\": \"\"        \t\t        \t\t        \t\n       },\n       \n }",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "{} JSON Respuesta-Error:     ",
          "content": "{\n   \"token\":\"\",\n   \"respuesta\": {\n       \"codResultado\": \"-203\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"No se pudo obtener el c\\u00F3digo CIP\",\n       \"clase\": \"CtrlCip\",\n       \"metodo\": \"getDatos\",\n       \"excepcion\": \" \",\n       \"tipoExepcion\": \"Generales \"\n   }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioConsulta.java",
    "groupTitle": "SOLICITUDES_CIP"
  },
  {
    "type": "POST",
    "url": "/actualizacionsidra/getcatalogo/",
    "title": "[getCatalogo]",
    "name": "getCatalogo",
    "description": "<p>Servicio para obtener versiones de catalogos disponibles de la aplicaci\\u00F3n</p>",
    "group": "Servicios_Actualizacion",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "getCatalogo": [
          {
            "group": "getCatalogo",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de  \\u00E1rea del pa\\u00EDs del que se desea obtener informaci\\u00F3n  de bodegas</p>"
          },
          {
            "group": "getCatalogo",
            "type": "String",
            "optional": false,
            "field": "fecha",
            "description": "<p>fecha de la versi\\u00F3n que se desea consultar formato:yyyymmddHH24MiSS</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo parametros de Entrada:",
          "content": "{\n\t\"codArea\": \"502\",\n\t\"fecha\":\"20151103155643\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "{} JSON Exito-respuesta:",
          "content": "{\n   \"respuesta\": {\n       \"codResultado\": \"2\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"OK. Actualizaci\\u00F3n.\",\n       \"clase\": \" \",\n       \"metodo\": \" \",\n       \"excepcion\": \" \",\n       \"tipoExepcion\": \"\"\n   },\n   \"fecha\": \"20151103172722\",\n   \"catalogos\": [\n       {\n           \"nombre\": \"etiquetas\",\n           \"url\": \"sd\",\n           \"codPais\": \"503\"\n       },\n       {\n           \"nombre\": \"paises\",\n           \"url\": \"ss\",\n           \"codPais\": \"-1\"\n       }\n  ]\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "{} JSON Respuesta-Error:\t  ",
          "content": "{\n   \"respuesta\": {\n       \"codResultado\": \"-101\",\n       \"mostrar\": \"0\",\n       \"descripcion\": \"Ocurrio un Problema inesperado, contacte a su Administrador.\",\n       \"clase\": \"com.consystec.sc.ca.ws.metodos.Bodegas\",\n       \"metodo\": \"getMensaje\",\n       \"excepcion\": \"ORA-00942: table or view does not exist\\n\",\n       \"tipoExepcion\": \"Excepcion SQL\"\n   }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioActualizacion.java",
    "groupTitle": "Servicios_Actualizacion"
  },
  {
    "type": "POST",
    "url": "/actualizacionsidra/getetiqueta/",
    "title": "[getEtiqueta]",
    "name": "getEtiqueta",
    "description": "<p>Servicio para obtener etiquetas de pantallas de aplicaci\\u00F3n m\\u00F3vil</p>",
    "group": "Servicios_Actualizacion",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "getEtiqueta": [
          {
            "group": "getEtiqueta",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de  \\u00E1rea del pa\\u00EDs del que se desea obtener informaci\\u00F3n  de bodegas</p>"
          },
          {
            "group": "getEtiqueta",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>nombre de usuario que solicita la operacion</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo parametros de Entrada:",
          "content": "{\n   \"codArea\": \"503\",\n   \"usuario\": \"usuario.pruebas\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "{} JSON Exito-respuesta:",
          "content": "{\n\"respuesta\": {\n   \"codResultado\": \"2\",\n   \"mostrar\": \"1\",\n   \"descripcion\": \"OK. Actualizaci\\u00F3n.\",\n   \"origen\": \"Servicios Operaciones\",\n   \"clase\": \"com.consystec.sc.ca.ws.metodos.EtiquetaMov\",\n   \"metodo\": \"obtenerEtiquetasBD\",\n   \"excepcion\": \" \",\n   \"tipoExepcion\": \"\"\n},\n\"pantallas\": [\n   {\n       \"paisId\": \"503\",\n       \"id\": \"1\",\n       \"nombre\": \"Login\",\n       \"etiquetas\": [\n           {\n               \"nombreId\": \"1\",\n               \"nombre\": \"usuario\",\n               \"valorId\": \"1\",\n               \"valor\": \"Usuario:\",\n               \"orden\": \"1\",\n               \"mostrar\": \"1\",\n           \t\"obligatorio\": \"0\"\n           },\n           {\n               \"nombreId\": \"2\",\n               \"nombre\": \"contrasena\",\n               \"valorId\": \"2\",\n               \"valor\": \"Contrase\\u00F1a:\",\n               \"orden\": \"1\",\n                \"mostrar\": \"1\",\n           \t\"obligatorio\": \"0\"\n           },\n           {\n               \"nombreId\": \"3\",\n               \"nombre\": \"recordar\",\n               \"valorId\": \"3\",\n               \"valor\": \"Recordar contrase\\u00F1a\",\n              \"orden\": \"1\",\n               \"mostrar\": \"1\",\n           \t\"obligatorio\": \"0\"\n           },\n           {\n               \"nombreId\": \"4\",\n               \"nombre\": \"ingresar\",\n               \"valorId\": \"4\",\n               \"valor\": \"Ingresar\",\n               \"orden\": \"1\",\n                \"mostrar\": \"1\",\n           \t\"obligatorio\": \"0\"\n           }\n      ]\n   },",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "{} JSON Respuesta-Error:\t  ",
          "content": "{\n   \"respuesta\": {\n       \"codResultado\": \"-101\",\n       \"mostrar\": \"0\",\n       \"descripcion\": \"Ocurrio un Problema inesperado, contacte a su Administrador.\",\n       \"clase\": \"com.consystec.sc.ca.ws.metodos.Bodegas\",\n       \"metodo\": \"getMensaje\",\n       \"excepcion\": \"ORA-00942: table or view does not exist\\n\",\n       \"tipoExepcion\": \"Excepcion SQL\"\n   }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioActualizacion.java",
    "groupTitle": "Servicios_Actualizacion"
  },
  {
    "type": "POST",
    "url": "/actualizacionsidra/getdatospais/",
    "title": "[getdatospais]",
    "name": "getdatospais",
    "description": "<p>Servicio para obtener los datos del pa\\u00EDs.</p>",
    "group": "Servicios_Actualizacion",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "getdatospais": [
          {
            "group": "getdatospais",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pais.</p>"
          },
          {
            "group": "getdatospais",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operacion.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo parametros de Entrada:",
          "content": "{\n    \"codArea\":\"503\",\n    \"usuario\": \"usuario.pruebas\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "{} JSON Exito-respuesta:",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"0\",\n    \"mostrar\": \"0\",\n    \"descripcion\": \"Recursos recuperados exitosamente.\"\n  },\n  \"codArea\": \"503\",\n  \"nombrePais\": \"EL SALVADOR\",\n  \"longMaxNumero\": \"8\",\n  \"longMaxIdentificacion\": \"25\",\n  \"departamentos\": [\n    {\n      \"nombreDepartamento\": \"AHUACHAP\\u00E1N\",\n      \"municipios\": [\n        {\n          \"nombreMunicipio\": \"AHUACHAP\\u00E1N\"\n        },\n        {\n          \"nombreMunicipio\": \"APANECA\"\n        }\n    },\n    {\n      \"nombreDepartamento\": \"CABA\\u00D1AS\",\n      \"municipios\": [\n        {\n          \"nombreMunicipio\": \"SENSUNTEPEQUE\"\n        },\n    }\n  ]\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "{} JSON Respuesta-Error:     ",
          "content": "{\n   \"respuesta\": {\n       \"codResultado\": \"-205\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"No existen registros con esos par\\u00E1metros.\",\n       \"clase\": \"com.consystec.sc.sv.ws.operaciones.OperacionConsultaPais\",\n       \"metodo\": \"doGet\",\n       \"excepcion\": \" \",\n       \"tipoExepcion\": \" \"\n   }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioActualizacion.java",
    "groupTitle": "Servicios_Actualizacion"
  },
  {
    "type": "POST",
    "url": "/actualizacionsidra/getservidor/",
    "title": "[getservidor]",
    "name": "getservidor",
    "description": "<p>Servicio para obtener las url's utilizadas para consumir los diferentes servicios web.</p>",
    "group": "Servicios_Actualizacion",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "getservidor": [
          {
            "group": "getservidor",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pais.</p>"
          },
          {
            "group": "getservidor",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operacion.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo parametros de Entrada:",
          "content": "{\n    \"codArea\":\"503\",\n    \"usuario\": \"usuario.pruebas\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "{} JSON Exito-respuesta:",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"0\",\n    \"mostrar\": \"0\",\n    \"descripcion\": \"Recursos recuperados exitosamente.\"\n  },\n  \"codArea\": \"503\",\n  \"nombrePais\": \"EL SALVADOR\",\n  \"longMaxNumero\": \"8\",\n  \"longMaxIdentificacion\": \"25\",\n  \"departamentos\": [\n    {\n      \"nombreDepartamento\": \"AHUACHAP\\u00E1N\",\n      \"municipios\": [\n        {\n          \"nombreMunicipio\": \"AHUACHAP\\u00E1N\"\n        },\n        {\n          \"nombreMunicipio\": \"APANECA\"\n        }\n    },\n    {\n      \"nombreDepartamento\": \"CABA\\u00D1AS\",\n      \"municipios\": [\n        {\n          \"nombreMunicipio\": \"SENSUNTEPEQUE\"\n        },\n    }\n  ]\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "{} JSON Respuesta-Error:     ",
          "content": "{\n   \"respuesta\": {\n       \"codResultado\": \"-205\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"No existen registros con esos par\\u00E1metros.\",\n       \"clase\": \"com.consystec.sc.sv.ws.operaciones.OperacionConsultaPais\",\n       \"metodo\": \"doGet\",\n       \"excepcion\": \" \",\n       \"tipoExepcion\": \" \"\n   }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioActualizacion.java",
    "groupTitle": "Servicios_Actualizacion"
  },
  {
    "type": "POST",
    "url": "/opsidra/creasincronizacion/",
    "title": "[creasincronizacion]",
    "name": "creasincronizacion",
    "description": "<p>Servicio para registrar vendedores listos para finalizar Jornada.</p>",
    "group": "Sincronizacion",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs en el que se desea realizar la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "token",
            "description": "<p>token de validaci\\u00F3n de autenticaci\\u00F3n de usuario.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codDispositivo",
            "description": "<p>c\\u00F3digo del dispositivo desde donde se realiza la operaci\\u00F3n, en caso de utilizarse la APP M\\u00F3VIL.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idTipo",
            "description": "<p>Id de la ruta o panel al que pertenece el dispositivo.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "tipo",
            "defaultValue": "RUTA o PANEL",
            "description": "<p>Nombre del tipo asociado al pertenece el dispositivo.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idDispositivo",
            "description": "<p>Id del dispositivo.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "datos",
            "description": "<p>Listado de vendedores con sus jornadas.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "datos.idVendedor",
            "description": "<p>Id del vendedor asociado al tipo a registrar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "datos.idJornada",
            "description": "<p>Id de la jornada del vendedor a registrar.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"token\": \"WEB\",\n    \"codArea\": \"505\",\n    \"usuario\": \"usuario.pruebas\",\n    \"codDispositivo\": \"64E1CE4A48BF656974C376B7905F7E62\",\n    \"idTipo\": \"63\",\n    \"tipo\": \"panel\",\n    \"idDispositivo\": \"ASDF123QWER456ZXCV789\",\n    \"datos\":[\n        {\n            \"idVendedor\": \"904\",\n            \"idJornada\": \"53\"\n        },\n        {\n            \"idVendedor\": \"1382\",\n            \"idJornada\": \"54\"\n        }\n    ]\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"200\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"Campos agregados exitosamente.\",\n    \"clase\": \"OperacionSincronizacion\",\n    \"metodo\": \"doPost\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"-744\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"No coinciden los datos de vendedor y dispositivo con la jornada 593.\",\n    \"clase\": \"OperacionSincronizacion\",\n    \"metodo\": \"doPost\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioOperacion.java",
    "groupTitle": "Sincronizacion"
  },
  {
    "type": "POST",
    "url": "/consultasidra/getfaltasinc/",
    "title": "[getfaltasinc]",
    "name": "getfaltasinc",
    "description": "<p>Servicio para obtener los datos de vendedores que faltan por sincronizar operaciones para finalizar Jornada.</p>",
    "group": "Sincronizacion",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs en el que se desea realizar la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "token",
            "description": "<p>token de validaci\\u00F3n de autenticaci\\u00F3n de usuario.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codDispositivo",
            "description": "<p>c\\u00F3digo del dispositivo desde donde se realiza la operaci\\u00F3n, en caso de utilizarse la APP M\\u00F3VIL.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idJornada",
            "description": "<p>Identificador de la jornada a consultar.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"codArea\": \"505\",\n    \"token\": \"WEB\",\n    \"codDispositivo\": \"64E1CE4A48BF656974C376B7905F7E62\",\n    \"usuario\": \"usuario.pruebas\",\n    \"idJornada\": \"53\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"12\",\n    \"mostrar\": \"0\",\n    \"descripcion\": \"OK. Datos obtenidos exitosamente.\",\n    \"clase\": \"OperacionSincronizacion\",\n    \"metodo\": \"doGetFaltantes\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "{} JSON Respuesta-Error:     ",
          "content": "{\n  \"token\": \"WEB\",\n  \"respuesta\": {\n    \"codResultado\": \"-199\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"Los siguientes datos faltan o son incorrectos:  ID Jornada.\",\n    \"clase\": \"CtrlSincronizacion\",\n    \"metodo\": \"validarInput\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioConsulta.java",
    "groupTitle": "Sincronizacion"
  },
  {
    "type": "POST",
    "url": "/consultasidra/getsincronizacion/",
    "title": "[getsincronizacion]",
    "name": "getsincronizacion",
    "description": "<p>Servicio para obtener los datos de vendedores registrados para finalizar Jornada.</p>",
    "group": "Sincronizacion",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs en el que se desea realizar la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "token",
            "description": "<p>token de validaci\\u00F3n de autenticaci\\u00F3n de usuario.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codDispositivo",
            "description": "<p>c\\u00F3digo del dispositivo desde donde se realiza la operaci\\u00F3n, en caso de utilizarse la APP M\\u00F3VIL.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idDistribuidor",
            "description": "<p>Identificador del distribuidor a asociar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idJornada",
            "description": "<p>Identificador de la jornada a consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idDispositivo",
            "description": "<p>Identificador del dispositivo a consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idVendedor",
            "description": "<p>Identificador del vendedor a consultar.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"codArea\": \"505\",\n    \"token\": \"WEB\",\n    \"usuario\": \"usuario.pruebas\",\n    \"codDispositivo\": \"64E1CE4A48BF656974C376B7905F7E62\",\n    \"idJornada\": \"53\",\n    \"idDispositivo\": \"ASDF123QWER456ZXCV789\",\n    \"idVendedor\": \"904\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n  \"token\": \"WEB\",\n  \"respuesta\": {\n    \"codResultado\": \"202\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"Recursos recuperados exitosamente.\",\n    \"clase\": \"OperacionSincronizacion\",\n    \"metodo\": \"doGet\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"\"\n  },\n  \"datos\": {\n    \"idSincronizacion\": \"5\",\n    \"idDispositivo\": \"ASDF123QWER456ZXCV789\",\n    \"idVendedor\": \"904\",\n    \"nombreVendedor\": \"supervisor3\",\n    \"idJornada\": \"53\",\n    \"creado_el\": \"19/07/2016 09:32:04\",\n    \"creado_por\": \"usuario.pruebas\",\n    \"modificado_el\": \"\",\n    \"modificado_por\": \"\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "{} JSON Respuesta-Error:     ",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"-199\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"Los siguientes datos faltan o son incorrectos:  ID Jornada.\",\n    \"clase\": \"CtrlSincronizacion\",\n    \"metodo\": \"validarInput\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioConsulta.java",
    "groupTitle": "Sincronizacion"
  },
  {
    "type": "POST",
    "url": "/opsidra/validavendedor/",
    "title": "[validavendedor]",
    "name": "validavendedor",
    "description": "<p>Servicio para registrar vendedores listos para finalizar Jornada.</p>",
    "group": "Sincronizacion",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs en el que se desea realizar la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idVendedor",
            "description": "<p>Id del vendedor a validar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codDispositivo",
            "description": "<p>Id del dispositivo que el vendedor utiliza.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"codArea\":\"505\",\n    \"usuario\": \"usuario.pruebas\",\n    \"idVendedor\": \"63\",\n    \"codDispositivo\": \"ASDF123QWER456ZXCV789\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": " {\n\"respuesta\": {\n    \"codResultado\": \"12\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"OK. Datos obtenidos exitosamente\",\n    \"clase\": \" \",\n    \"metodo\": \" \",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"\"\n},\n\"vendedores\": [\n     {\n         \"idDTS\": \"41\",\n         \"idBodegaVirtual\": \"50003\",\n         \"idBodegaVendedor\": \"137\",\n         \"idVendedor\": \"123\",\n         \"nombreUsuario\": \"Nombre vendedor\",\n         \"responsable\": \"1\",\n         \"idResponsable\": \"1541\",      \n         \"tipo\": \"PANEL\",\n         \"longitud\":\"\",\n         \"latitud\": \" \",\n         \"nombreTipo\": \"RUTA TECH S.A.\",\n         \"numRecarga\": \"63257899\",\n         \"numConvenio\": \"25121\",\n         \"pin\": \"251\",\n         \"vendedorAsignado\": \"236\",\n         \"nivelBuzon\": \"2\",\n         \"numIdentificacion\": \"34543534534534\",\n         \"tipoIdentificacion\": \"RUC\",\n         \"numTelefono\": \"88005544\",\n         \"idDispositivo\": \"236\"\n     }\n ]\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"-42\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"No se encontraron datos.\",\n    \"clase\": \"\",\n    \"metodo\": \"getVendedorxDTS\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioOperacion.java",
    "groupTitle": "Sincronizacion"
  },
  {
    "type": "POST",
    "url": "/opsidra/aceptarsolicitud/",
    "title": "[aceptarSolicitud]",
    "name": "aceptarSolicitud",
    "description": "<p>Servicio aceptar o rechazar art\\u00EDculos de solicitudes tipo DEVOLUCION o SINIESTRO.</p>",
    "group": "Solicitud",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operacion.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idSolicitud",
            "description": "<p>id de la solicitud a aceptar o rechazar, esta solicitud debe ser de tipo DEVOLUCION o SINIESTRO.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idBodegaVirtual",
            "description": "<p>id de bodega virtual a la que se realizar\\u00E1 la transacci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "observaciones",
            "description": "<p>observaciones acerca de proceso.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idBuzonSiguiente",
            "description": "<p>id del buzon de workflow hacia donde se enviar\\u00E1 la solicitud.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "articulos",
            "description": "<p>listado de articulos de los cuales se aceptara  o rechazar\\u00E1 devolucion.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "tcscSolicitudId",
            "description": "<p>Id del registro que se ingresar\\u00E1 o egresar\\u00E1.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idArticulo",
            "description": "<p>id del Art\\u00EDculo que se ingresar\\u00E1 o egresar\\u00E1.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codDispositivo",
            "description": "<p>C\\u00F3digo del dispositivo que se aceptar\\u00E1 o rechazar\\u00E1.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "aceptado",
            "description": "<p>indica si el art\\u00EDculo se acepta o rechaza para la devoluci\\u00F3n. 1=ACEPTADO, 0=RECHAZADO.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n   \"codArea\": \"505\",\n   \"usuario\": \"usuario.pruebas\",\n   \"idSolicitud\":\"1\",\n   \"idBodega\":\"100\",\n   \"observaciones\":\"sdf5s5f4s65f456ssfds\",\n   \"idBuzonSiguiente\":\"\",\n   \"articulos\":[\n      {\n          \"tcscSolicitudId\":\"1\",\n          \"idArticulo\":\"1\",\n          \"codDispositivo\":\"\",\n          \"aceptado\":\"1\",\n          \"observaciones\":\"\"\n      },\n      {\n          \"tcscSolicitudId\":\"1\",\n          \"idArticulo\":\"2\",\n          \"codDispositivo\":\"\",\n          \"aceptado\":\"0\",\n          \"observaciones\":\"\"\n       }\n   ]\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n    \"codResultado\": \"13\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"OK.Devolucion procesada correctamente\",\n    \"clase\": \" \",\n    \"metodo\": \" \",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"\"\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n    \"codResultado\": \"-216\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"La solicitud no se encuentra ABIERTA.\",\n    \"clase\": \" \",\n    \"metodo\": \"aceptaRechaza\",\n    \"excepcion\": \"class CtrlDevolucion\",\n    \"tipoExepcion\": \"Generales\"\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioOperacion.java",
    "groupTitle": "Solicitud"
  },
  {
    "type": "POST",
    "url": "/opsidra/creasolicitud/",
    "title": "[creasolicitud]",
    "name": "creasolicitud",
    "description": "<p>Servicio para crear Solicitudes por pa\\u00EDs.</p>",
    "group": "Solicitud",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operacion.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codDispositivo",
            "description": "<p>c\\u00F3digo del dispositivo desde donde se realiza la operaci\\u00F3n, en caso de utilizarse la APP M\\u00F3VIL.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idBuzon",
            "description": "<p>Identificador del buz\\u00F3n a asociar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "tipoSolicitud",
            "defaultValue": "DEVOLUCION, RESERVA, PEDIDO, NUMEROS_PAYMENT, SINIESTRO o DEUDA",
            "description": "<p>Tipo de la solicitud.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idVendedor",
            "description": "<p>Identificador del vendedor a asociar. Opcional en solicitudes de origen PC.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idBodega",
            "description": "<p>Identificador de la bodega a asociar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idDTS",
            "description": "<p>Identificador del distribuidor a asociar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "fecha",
            "description": "<p>Fecha de la solicitud en formato yyyyMMdd.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "causaSolicitud",
            "description": "<p>Causa de la solicitud. Necesario \\u00FAnicamente en solicitudes tipo DEVOLUCION.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idPDV",
            "description": "<p>Identificador del punto de venta al ser solicitud tipo NUMEROS_PAYMENT.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "observaciones",
            "description": "<p>Observaciones de la solicitud.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "origen",
            "defaultValue": "MOVIL o PC",
            "description": "<p>Origen de la solicitud.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "seriado",
            "description": "<p>Indica si la solicitud es con series o no (1 o 0). Opcional en solicitudes de origen PC.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "tipoSiniestro",
            "defaultValue": "TOTAL, PARCIAL o DISPOSITIVO",
            "description": "<p>Tipo de siniestro de la solicitud.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idTipo",
            "description": "<p>Identificador del tipo Ruta o Panel asociado al vendedor.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "tipo",
            "defaultValue": "RUTA o PANEL",
            "description": "<p>Nombre del tipo asociado al vendedor.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "totalDeuda",
            "description": "<p>Total de la deuda. \\u00FAnicamente en caso de ser solicitud de tipo DEUDA.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idBuzonSiguiente",
            "description": "<p>Buz\\u00F3n al que se enviar\\u00E1 la deuda. \\u00FAnicamente en caso de ser solicitud de tipo DEUDA.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "articulos",
            "description": "<p>Listado de art\\u00EDculos de la solicitud. Innecesario al tratarse de una solicitud de tipo SINIESTRO y TOTAL.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "articulos.idArticulo",
            "description": "<p>Identificador del art\\u00EDculo a asociar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "articulos.serie",
            "description": "<p>Serie del art\\u00EDculo a asociar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "articulos.serieFinal",
            "description": "<p>Serie final del rango del art\\u00EDculo a asociar. Unicamente valores num\\u00E9ricos y cuando sean devoluciones por rango.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "articulos.cantidad",
            "description": "<p>Cantidad de art\\u00EDculos a asociar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "articulos.serieAsociada",
            "description": "<p>Serie secundaria asociada al art\\u00EDculo.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "articulos.tipoInv",
            "description": "<p>Nombre del tipo de inventario del art\\u00EDculo.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "dispositivos",
            "description": "<p>Listado de dispositivos a reportar en siniestros.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "dispositivos.codigoDispositivo",
            "description": "<p>C\\u00F3digo del dispositivo a reportar en como siniestrado.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "detallePagos",
            "description": "<p>Listado de dispositivos a reportar en siniestros.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "detallePagos.formaPago",
            "description": "<p>Nombre de la forma de pago.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "detallePagos.monto",
            "description": "<p>Monto de la forma de pago.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n  \"token\": \"WEB\",\n  \"codArea\": \"505\",\n  \"usuario\": \"sergio.lujan\",\n  \"codDispositivo\": \"64E1CE4A48BF656974C376B7905F7E62\",\n  \"idDTS\": \"33\",\n  \"idBuzon\": \"2\",\n  \"idBodega\": \"73\",\n  \"fecha\": \"20160822\",\n  \"tipoSolicitud\": \"Siniestro\",\n  \"idVendedor\": \"\",\n  \"causaSolicitud\": \"\",\n  \"idPDV\": \"\",\n  \"observaciones\": \"\",\n  \"origen\": \"pc\",\n  \"seriado\": \"1\",\n  \"tipoSiniestro\": \"parcial\",\n  \"idTipo\": \"82\",\n  \"tipo\": \"panel\",\n  \"totalDeuda\": \"\",\n  \"idBuzonSiguiente\": \"\",\n  \"articulos\": [\n    {\n      \"idArticulo\": \"8\",\n      \"serie\": \"\",\n      \"serieFinal\": \"\",\n      \"cantidad\": \"10\",\n      \"serieAsociada\": \"\",\n      \"tipoInv\": \"INV_TELCA\"\n    },\n    {\n      \"idArticulo\": \"\",\n      \"serie\": \"A3\",\n      \"serieFinal\": \"\",\n      \"cantidad\": \"\",\n      \"serieAsociada\": \"\",\n      \"tipoInv\": \"INV_TELCA\"\n    },\n    {\n      \"idArticulo\": \"\",\n      \"serie\": \"130\",\n      \"serieFinal\": \"139\",\n      \"cantidad\": \"\",\n      \"serieAsociada\": \"\",\n      \"tipoInv\": \"INV_TELCA\"\n    }\n  ],\n  \"dispositivos\": [\n     {\n        \"codigoDispositivo\": \"32DDA7D9A68CE8663B6315549EAAB637A79445D6\"\n     },{\n        \"codigoDispositivo\": \"3D3570521B4F675984131DCA362C04FD29A93A5B\"\n     }\n  ],\n  \"detallePagos\": [\n     {\n        \"formaPago\": \"EFECTIVO\",\n        \"monto\": \"1194987.5100257151\"\n     },{\n        \"formaPago\": \"TARTA\",\n        \"monto\": \"0\"\n     }\n  ]\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n  \"idSolicitud\": \"212\",\n  \"respuesta\": {\n    \"codResultado\": \"200\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"Campos agregados exitosamente. Algunas series no se pudieron ingresar: A3. Algunos art\\u00EDculos no se pudieron ingresar por las siguientes razones: Art\\u00EDculos no cuentan con existencias suficientes: 8. \",\n    \"clase\": \"OperacionSolicitud\",\n    \"metodo\": \"doPost\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"\"\n  },\n  \"descErrorSeries\": \"Series no v\\u00E1lidas en la bodega.\",\n  \"series\": {\n    \"serie\": \"A3\"\n  },\n  \"descErrorExistencias\": \"Art\\u00EDculos no cuentan con existencias suficientes: \",\n  \"existencias\": {\n    \"idArticulo\": \"8\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"-199\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"Los siguientes datos faltan o son incorrectos:  Origen debe especificarse como PC o MOVIL.\",\n    \"clase\": \"CtrlSolicitud\",\n    \"metodo\": \"validarInput\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioOperacion.java",
    "groupTitle": "Solicitud"
  },
  {
    "type": "POST",
    "url": "/consultasidra/getsolicitud/",
    "title": "[getSolicitud]",
    "name": "getSolicitud",
    "description": "<p>Servicio para obtener los datos de solicitudes por pais.</p>",
    "group": "Solicitud",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pais.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "token",
            "description": "<p>token de autenticaci\\u00F3n de sesion de usuario.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codDispositivo",
            "description": "<p>c\\u00F3digo del dispositivo desde donde se realiza la operaci\\u00F3n, en caso de utilizarse la APP M\\u00F3VIL.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idSolicitud",
            "description": "<p>Identificador de la solicitud que se desea.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "origen",
            "defaultValue": "MOVIL o PC",
            "description": "<p>Origen de las solicitudes que desea consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idVendedor",
            "description": "<p>Identificador del vendedor que se desea consultar en la solicitud.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idBodega",
            "description": "<p>Identificador de la bodega que se desea consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idDTS",
            "description": "<p>Identificador del Distribuidor que se desea consultar en la solicitud.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idBuzon",
            "description": "<p>Identificador del buz\\u00F3n que se desea consultar en la solicitud.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idBuzonAnterior",
            "description": "<p>Identificador del buz\\u00F3n anterior en que se encontraba la solicitud a consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "fechaInicio",
            "description": "<p>Fecha en formato yyyyMMdd desde la cu\\u00E1l se desea consultar la solicitud.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "fechaFin",
            "description": "<p>Fecha en formato yyyyMMdd hasta la cu\\u00E1l se desea consultar la solicitud.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "tipoSolicitud",
            "defaultValue": "DEVOLUCION, RESERVA, PEDIDO, NUMEROS_PAYMENT, SINIESTRO",
            "description": "<p>Nombre del tipo de solicitud que se desea consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "tipoSiniestro",
            "defaultValue": "TOTAL, PARCIAL o DISPOSITIVO",
            "description": "<p>Nombre del tipo de siniestro que se desea consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "seriado",
            "defaultValue": "1 o 0",
            "description": "<p>Tipo de solicitud que se desea, 1 seriadas o 0 no seriadas.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idJornada",
            "description": "<p>Identificador de la jornada que se desea consultar en la solicitud.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idTipo",
            "description": "<p>Identificador del tipo asociado que se desea consultar en la solicitud.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "tipo",
            "defaultValue": "RUTA, PANEL o PDV",
            "description": "<p>Tipo asociado que se desea consultar en la solicitud.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "creado_por",
            "description": "<p>Nombre del usuario que cre\\u00F3 la solicitud que se desea consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "estado",
            "description": "<p>Estado de la solicitud que se desea consultar.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"token\":\"WEB\",\n    \"codArea\":\"505\",\n    \"usuario\":\"usuarioprueba\",\n    \"idSolicitud\":\"\",\n    \"origen\":\"\",\n    \"idVendedor\":\"\",\n    \"idBodega\":\"\",\n    \"idDTS\":\"\",\n    \"idBuzon\":\"\",\n    \"idBuzonAnterior\": \"\",\n    \"fechaInicio\":\"\",\n    \"fechaFin\":\"\",\n    \"tipoSolicitud\":\"\",\n    \"tipoSiniestro\":\"\",\n    \"seriado\":\"\",\n    \"idJornada\":\"\",\n    \"idTipo\":\"\",\n    \"tipo\":\"\",\n    \"creado_por\":\"\",\n    \"estado\": \"\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"0\",\n    \"mostrar\": \"0\",\n    \"descripcion\": \"Recursos recuperados exitosamente.\"\n  },\n  \"solicitudes\": {\n    \"tipoSolicitud\": \"SINIESTRO\",\n    \"listaSolicitudes\": [{\n       \"idSolicitud\": \"91\",\n       \"idBodega\": \"114\",\n       \"idDTS\": \"22\",\n       \"nombreDTS\": \"DTS TECNOLOGIA S.A\",\n       \"idBuzon\": \"11\",\n       \"nombreBuzon\": \"Siniestros\",\n       \"idBuzonAnterior\": \"\",\n       \"fecha\": \"03/08/2016 00:00:00\",\n       \"idVendedor\": \"961\",\n       \"nombreVendedor\": \"Daniel\",\n       \"apellidoVendedor\": \"Estevez\",\n       \"usuarioVendedor\": \"99887768\",\n       \"tipoSolicitud\": \"SINIESTRO\",\n       \"tipoSiniestro\": \"TOTAL\",\n       \"causaSolicitud\": \"\",\n       \"idJornada\": \"70\",\n       \"idTipo\": \"24\",\n       \"tipo\": \"RUTA\",\n       \"nombreTipo\": \"RUTA TECH S.A.\",\n       \"observaciones\": \"Observaciones payment\",\n       \"seriado\": \"0\",\n       \"origen\": \"PC\",\n       \"totalDeuda\": \"\",\n       \"tasaCambio\": \"\",\n       \"estado\": \"CERRADA\",\n       \"origenCancelacion\":\"\",\n       \"obsCancelacion\":\"\"\n       \"creado_el\": \"03/08/2016 10:00:11\",\n       \"creado_por\": \"victor.cifuentes\",\n       \"modificado_el\": \"04/08/2016 11:53:26\",\n       \"modificado_por\": \"sergio.lujan\",\n       \"articulos\": [\n         {\n           \"idSolicitudDet\": \"102\",\n           \"codDispositivo\": \"\",\n           \"idArticulo\": \"91\",\n           \"descripcion\": \"TARJETA SIM PREPAGO 2FF/3FF\",\n           \"serie\": \"8950702310512556368\",\n           \"serieFinal\": \"NULL\",\n           \"serieAsociada\": \"\",\n           \"cantidad\": \"1\",\n           \"observaciones\": \"Rechazada\",\n           \"estado\": \"ACEPTADA\",\n           \"creado_el\": \"03/08/2016 10:00:11\",\n           \"creado_por\": \"victor.cifuentes\",\n           \"modificado_el\": \"04/08/2016 11:53:26\",\n           \"modificado_por\": \"sergio.lujan\"\n         },\n         {\n           \"idSolicitudDet\": \"103\",\n           \"codDispositivo\": \"\",\n           \"idArticulo\": \"12\",\n           \"descripcion\": \"PACHON AZUL\",\n           \"serie\": \"NULL\",\n           \"serieFinal\": \"NULL\",\n           \"serieAsociada\": \"\",\n           \"cantidad\": \"200\",\n           \"observaciones\": \"Rechazada\",\n           \"estado\": \"ACEPTADA\",\n           \"creado_el\": \"03/08/2016 10:00:11\",\n           \"creado_por\": \"victor.cifuentes\",\n           \"modificado_el\": \"04/08/2016 11:53:26\",\n           \"modificado_por\": \"sergio.lujan\"\n         }\n       ],\n       \"obsSolicitud\": {\n          \"observacion\": \"observacion\",\n          \"creado_el\": \"16/03/2017 22:44:05\",\n          \"creado_por\": \"usuario.pruebas\"\n       },\n       \"remesas\": [\n          {\n             \"idRemesa\": \"99\",\n             \"monto\": \"2944.45\",\n             \"tasaCambio\": \"29.4445\",\n             \"noBoleta\": \"A5\",\n             \"banco\": \"BAC\",\n             \"estado\": \"ALTA\",\n             \"creado_el\": \"15/03/2017 19:53:29\",\n             \"creado_por\": \"usuario.pruebas\",\n             \"modificado_el\": \"\",\n             \"modificado_por\": \"\"\n         }\n       ],\n       \"detallePagos\": [\n          {\n             \"formaPago\": \"EFECTIVO\",\n             \"monto\": \"1194987.510026\",\n             \"estado\": \"BAJA\",\n             \"creado_el\": \"05/06/2017 11:22:01\",\n             \"creado_por\": \"pablo.lopez\",\n             \"modificado_el\": \"05/06/2017 12:45:49\",\n             \"modificado_por\": \"pablo.lopez\"\n          }\n       ]\n     }]\n  }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"-205\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"No existen registros con esos par\\u00E1metros.\",\n    \"clase\": \"OperacionSolicitud\",\n    \"metodo\": \"doGet\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioConsulta.java",
    "groupTitle": "Solicitud"
  },
  {
    "type": "POST",
    "url": "/opsidra/modsolicitud/",
    "title": "[modsolicitud]",
    "name": "modsolicitud",
    "description": "<p>Servicio para modificar el estado de Solicitudes por pa\\u00EDs.</p>",
    "group": "Solicitud",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "token",
            "description": "<p>token de validaci\\u00F3n de autenticaci\\u00F3n de usuario.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codDispositivo",
            "description": "<p>c\\u00F3digo del dispositivo desde donde se realiza la operaci\\u00F3n, en caso de utilizarse la APP M\\u00F3VIL.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pais donde se realizar\\u00E1 la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idSolicitud",
            "description": "<p>Identificador de la solicitud a modificar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "estado",
            "defaultValue": "ACEPTADA, RECHAZADA, CANCELADA, FINALIZADA, RECHAZADA_TELCA o ENVIADO",
            "description": "<p>Estado con el que se operar\\u00E1 la solicitud.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idVendedor",
            "description": "<p>Identificador del vendedor que cancela la solicitud. Obligatorio cuando sea cancelaci\\u00F3n de origen m\\u00F3vil.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "observaciones",
            "description": "<p>Observaciones de la cancelaci\\u00F3n de la solicitud.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idBuzonSiguiente",
            "description": "<p>Identificador del buz\\u00F3n al que se enviar\\u00E1 la solicitud, necesario en caso de enviar estado ACEPTADA.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n     \"token\": \"WEB\",\n     \"codArea\": \"505\",\n     \"usuario\":\"usuario.pruebas\",\n     \"idSolicitud\":\"1\",\n     \"estado\": \"CANCELADA\",\n     \"idVendedor\":\"961\",\n     \"observaciones\":\"\",\n     \"idBuzonSiguiente\": \"\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"201\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"Recurso modificado exitosamente.\",\n    \"clase\": \"OperacionSolicitud\",\n    \"metodo\": \"doPut\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"-215\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"No se pueden cerrar solicitudes tipo DEVOLUCION o SINIESTRO.\",\n    \"clase\": \"OperacionSolicitud\",\n    \"metodo\": \"doPut\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioOperacion.java",
    "groupTitle": "Solicitud"
  },
  {
    "type": "POST",
    "url": "/opsidra/creatipotransaccion/",
    "title": "[creatipotransaccion]",
    "name": "creatipotransaccion",
    "description": "<p>Servicio para crear un catalogo de las transacciones que son realizadas en el inventario de SIDRA.</p>",
    "group": "TipoTransaccion",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>c\\u00F3digo de area del pa\\u00EDs que se desea  realizar la transacci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operacion.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codigoTransaccion",
            "description": "<p>c\\u00F3digo unico que identifica el tipo de transacci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "descripcion",
            "description": "<p>descripci\\u00F3n del tipo de transaccion a realizar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "tipoMovimiento",
            "description": "<p>indica el tipo de movimiento a realizar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "tipoAfecta",
            "description": "<p>indica de que manera afecta al inventario. Ej: Suma, Resta.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n   \"codArea\":\"505\",\n   \"usuario\": \"usuario\",\n   \"codigoTransaccion\": \"I\",\n   \"descripcion\":\"Ingreso de art\\u00EDculos a inventario\",\n   \"tipoMovimiento\":\"Ingreso\",\n   \"tipoAfecta\":\"Suma\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n    \"idTransaccion\": \"9\",\n    \"respuesta\": {\n        \"codResultado\": \"200\",\n        \"mostrar\": \"1\",\n        \"descripcion\": \"Campos agregados exitosamente. \",\n        \"clase\": \" \",\n        \"metodo\": \" \",\n        \"excepcion\": \" \",\n        \"tipoExepcion\": \"\"\n    }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n    \"respuesta\": {\n        \"codResultado\": \"-82\",\n        \"mostrar\": \"1\",\n        \"descripcion\": \"El par\\u00E1metro de entrada \\\\tipoAfecta\\\"\\\\ esta vac\\u00EDo.\",\n        \"clase\": \" \",\n        \"metodo\": \"validarDatos\",\n        \"excepcion\": \"class CtrlTipoTransaccionInv\",\n        \"tipoExepcion\": \"Generales\"\n    }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioOperacion.java",
    "groupTitle": "TipoTransaccion"
  },
  {
    "type": "POST",
    "url": "/consultasidra/gettipotransaccion/",
    "title": "[gettipotransaccion]",
    "name": "gettipotransaccion",
    "description": "<p>Servicio para obtener informaci\\u00F3n de los tipos de transacciones de inventario que pueden realizarse en SIDRA.</p>",
    "group": "TipoTransaccion",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>c\\u00F3digo de area del pa\\u00EDs en el que se desea obtener informaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>nombre del usuario que solicita la informaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idTipoTransaccion",
            "description": "<p>Id de la transaccion que se desea obtener.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "codigoTransaccion",
            "description": "<p>c\\u00F3digo de la transaccion a buscar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "descripcion",
            "description": "<p>descripci\\u00F3n de la transaccion a buscar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "tipoMovimiento",
            "description": "<p>tipo de movimiento de transaccion a buscar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "estado",
            "description": "<p>Estado actual que  se desea buscar.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"codArea\": \"505\",\n    \"usuario\":\"usuario.sidra\",\n    \"idTipoTransaccion\":\"\",\n    \"codigoTransaccion\": \"\",\n    \"descripcion\":\"\",\n    \"tipoMovimiento\":\"\",\n    \"estado\": \"\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": " {\n    \"respuesta\": {\n        \"codResultado\": \"12\",\n        \"mostrar\": \"0\",\n        \"descripcion\": \"OK. Datos obtenidos exitosamente\",\n        \"clase\": \" \",\n        \"metodo\": \" \",\n        \"excepcion\": \" \",\n        \"tipoExepcion\": \"\"\n    },\n    \"tiposTransaccion\": [\n        {\n            \"idTipoTransaccion\": \"1\",\n            \"codigoTransaccion\": \"T1\",\n            \"descripcion\": \"Traslado entre bodegas\",\n            \"tipoMovimiento\": \"Transaccion entre Almacenes\",\n            \"tipoAfecta\": \"Traslado\",\n            \"estado\": \"ALTA\",\n            \"creado_por\": \"susana.barrios\",\n            \"creado_el\": \"17/12/2015 10:11:02\",\n            \"modificado_por\": \" \",\n            \"modificado_el\": \" \"\n        },\n        {\n            \"idTipoTransaccion\": \"2\",\n            \"codigoTransaccion\": \"ASIG\",\n            \"descripcion\": \"Transacci\\u00F3n entre almacen a vendedor\",\n            \"tipoMovimiento\": \"Asignacion a vendedor\",\n            \"tipoAfecta\": \"Traslado\",\n            \"estado\": \"ALTA\",\n            \"creado_por\": \"susana.barrios\",\n            \"creado_el\": \"17/12/2015 10:21:24\",\n            \"modificado_por\": \" \",\n            \"modificado_el\": \" \"\n        }\n        \n    ]\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n    \"respuesta\": {\n        \"codResultado\": \"-42\",\n        \"mostrar\": \"1\",\n        \"descripcion\": \"No se encontraron datos.\",\n        \"clase\": \"CtrlTipoTransaccionInv\",\n        \"metodo\": \"getTipoTransaccionInv\",\n        \"excepcion\": \" \",\n        \"tipoExepcion\": \"Generales\"\n    }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioConsulta.java",
    "groupTitle": "TipoTransaccion"
  },
  {
    "type": "POST",
    "url": "/opsidra/modificatipotransaccion/",
    "title": "[modificatipotransaccion]",
    "name": "modificatipotransaccion",
    "description": "<p>Servicio para modificar informaci\\u00F3n sobre las transacciones que son realizadas en el inventario de SIDRA..</p>",
    "group": "TipoTransaccion",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>c\\u00F3digo de area del pa\\u00EDs que se desea  realizar la transacci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operacion.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idTipoTransaccion",
            "description": "<p>id de la transaccion que se desea modificar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "descripcion",
            "description": "<p>descripcion a modificar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "tipoMovimiento",
            "description": "<p>descripcion del tipo de movimiento.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "tipoAfecta",
            "description": "<p>descripci\\u00F3n de la forma que afecta el inventario.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "estado",
            "description": "<p>estado que desea aplicarse al dispositivo.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n   \"codArea\":\"505\",\n   \"usuario\": \"usuario\",\n   \"idTipoTransaccion\": \"1\",\n   \"descripcion\": \"Ingreso de art\\u00EDculos promocionales\",\n   \"tipoMovimiento\":\"Ingreso\",\n   \"tipoAfecta\":\"Suma\",\n   \"estado\": \"BAJA\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n    \"respuesta\": {\n        \"codResultado\": \"201\",\n        \"mostrar\": \"1\",\n        \"descripcion\": \"Recurso modificado exitosamente. \",\n        \"clase\": \" \",\n        \"metodo\": \" \",\n        \"excepcion\": \" \",\n        \"tipoExepcion\": \"\"\n    }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n    \"respuesta\": {\n        \"codResultado\": \"-82\",\n        \"mostrar\": \"1\",\n        \"descripcion\": \"El par\\u00E1metro de entrada \\\\tipoAfecta\\\"\\\\ esta vac\\u00EDo.\",\n        \"clase\": \" \",\n        \"metodo\": \"validarDatos\",\n        \"excepcion\": \"class CtrlTipoTransaccionInv\",\n        \"tipoExepcion\": \"Generales\"\n    }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioOperacion.java",
    "groupTitle": "TipoTransaccion"
  },
  {
    "type": "POST",
    "url": "/opsidra/creatraslado/",
    "title": "[creaTraslado]",
    "name": "creaTraslado",
    "description": "<p>Servicio para crear Traslados por pa\\u00EDs.</p>",
    "group": "Traslado",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pais donde se realizar\\u00E1 la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operacion.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "numTraspaso",
            "description": "<p>N\\u00FAmero de traspaso de SCL.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "bodegaOrigen",
            "description": "<p>Identificador de la bodega origen del traslado.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "bodegaDestino",
            "description": "<p>Identificador de la bodega destino del traslado.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "fecha",
            "description": "<p>Fecha del traslado.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "articulos",
            "description": "<p>Listado de art\\u00EDculos a trasladar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "articulos.idArticulo",
            "description": "<p>Identificador del art\\u00EDculo a trasladar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "articulos.serie",
            "description": "<p>Serie del art\\u00EDculo a trasladar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "articulos.serieFinal",
            "description": "<p>Serie final del rango del art\\u00EDculo a trasladar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "articulos.cantidad",
            "description": "<p>Cantidad de art\\u00EDculos a trasladar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "articulos.serieAsociada",
            "description": "<p>Serie asociada al art\\u00EDculo.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "articulos.tipoInv",
            "description": "<p>Nombre del tipo de inventario del art\\u00EDculo.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"codArea\":\"505\",\n    \"usuario\":\"usuarioprueba\",\n    \"numTraspaso\":\"\",\n    \"bodegaOrigen\":\"1055\",\n    \"bodegaDestino\":\"10\",\n    \"fecha\":\"20151005\",\n    \"articulos\": [\n        {\n            \"idArticulo\":\"\",\n            \"serie\":\"1001\",\n            \"serieFinal\":\"2000\",\n            \"cantidad\":\"\",\n            \"serieAsociada\":\"\",\n            \"tipoInv\": \"INV_TELCA\"\n        },\n        {\n            \"idArticulo\":\"8\",\n            \"serie\":\"\",\n            \"serieFinal\":\"\",\n            \"cantidad\":\"50\",\n            \"serieAsociada\":\"\",\n            \"tipoInv\": \"INV_TELCA\"\n        }\n    ]\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"200\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"Campos agregados exitosamente.  Algunos art\\u00EDculos no se pudieron trasladar por las siguientes razones: Art\\u00EDculos no existen en el inventario: 598. \",\n    \"clase\": \"OperacionTraslado\",\n    \"metodo\": \"doPutDel\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"\"\n  },\n  \"descErrorArticulos\": \"Art\\u00EDculos no existen en el inventario: \",\n  \"articulos\": {\n    \"idArticulo\": \"598\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"-199\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"Los siguientes datos faltan o son incorrectos:  La Bodega Destino enviada no es apta para el traslado.\",\n    \"clase\": \"CtrlTraslado\",\n    \"metodo\": \"validarInput\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioOperacion.java",
    "groupTitle": "Traslado"
  },
  {
    "type": "POST",
    "url": "/opsidra/asignausuariobuzon/",
    "title": "[asignaUsuarioBuzon]",
    "name": "asignaUsuarioBuzon",
    "description": "<p>Servicio para asignar buzones a vendedores por pa\\u00EDs.</p>",
    "group": "UsuarioBuzon",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operacion.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idBuzon",
            "description": "<p>Identificador del buzon a asociar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idVendedor",
            "description": "<p>Identificador del vendedor a asociar.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"codArea\": \"505\",\n    \"usuario\": \"usuario.pruebas\",\n    \"idBuzon\": \"1\",\n    \"idVendedor\": \"1\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"200\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"Campos agregados exitosamente.\",\n    \"clase\": \"OperacionUsuarioBuzon\",\n    \"metodo\": \"doPost\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \" \"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"-199\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"Los siguientes datos faltan o son incorrectos:  ID Buzon.\",\n    \"clase\": \"CtrlUsuarioBuzon\",\n    \"metodo\": \"validarInput\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \" \"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioOperacion.java",
    "groupTitle": "UsuarioBuzon"
  },
  {
    "type": "POST",
    "url": "/opsidra/bajausuariobuzon/",
    "title": "[delUsuarioBuzon]",
    "name": "delUsuarioBuzon",
    "description": "<p>Servicio para dar de baja UsuarioBuzons por pa\\u00EDs.</p>",
    "group": "UsuarioBuzon",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operacion.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idUsuarioBuzon",
            "description": "<p>Identificador del buzon de usuario que se desea dar de baja.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"codArea\": \"505\",\n    \"usuario\": \"usuario.pruebas\",\n    \"idUsuarioBuzon\": \"1\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n   \"respuesta\": {\n       \"codResultado\": \"201\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"Recurso modificado exitosamente.\",\n       \"clase\": \"OperacionUsuarioBuzon\",\n       \"metodo\": \"doPutDel\",\n       \"excepcion\": \" \",\n       \"tipoExepcion\": \" \"\n   }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n   \"respuesta\": {\n       \"codResultado\": \"-201\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"No existe el recurso deseado.\",\n       \"clase\": \"OperacionUsuarioBuzon\",\n       \"metodo\": \"doPutDel\",\n       \"excepcion\": \" \",\n       \"tipoExepcion\": \" \"\n   }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioOperacion.java",
    "groupTitle": "UsuarioBuzon"
  },
  {
    "type": "POST",
    "url": "/consultasidra/getusuariobuzon/",
    "title": "[getUsuarioBuzon]",
    "name": "getUsuarioBuzon",
    "description": "<p>Servicio para obtener los datos de usuarios asignados a buzones por pais.</p>",
    "group": "UsuarioBuzon",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pais.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operacion.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idUsuarioBuzon",
            "description": "<p>Identificador de la asignacion a consultar. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idVendedor",
            "description": "<p>Identificador del vendedor que se desea consultar. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idBuzon",
            "description": "<p>Identificador del buzon que se desea consultar. Este campo es opcional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "estado",
            "description": "<p>Estado de la asignacion que se desea consultar. Este campo es opcional.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"codArea\": \"505\",\n    \"usuario\": \"usuario.pruebas\",\n    \"idUsuarioBuzon\": \"\",\n    \"idBuzon\": \"\",\n    \"idVendedor\": \"\",\n    \"estado\": \"\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"0\",\n    \"mostrar\": \"0\",\n    \"descripcion\": \"Recursos recuperados exitosamente.\"\n  },\n  \"buzones\": [\n    {\n      \"idBuzon\": \"1\",\n      \"nombreBuzon\": \"Buzon 1\",\n      \"nivelBuzon\": \"1\",\n      \"buzones\": [\n        {\n          \"idUsuarioBuzon\": \"1\",\n          \"idBuzon\": \"1\",\n          \"idVendedor\": \"1\",\n          \"nombreVendedor\": \"Desarrollo El Salvador\",\n          \"estado\": \"ALTA\",\n          \"creado_el\": \"09/12/2015 17:04:13\",\n          \"creado_por\": \"usuario.pruebas\",\n          \"modificado_el\": \"09/12/2015 17:08:45\",\n          \"modificado_por\": \"usuario.pruebas\"\n        }\n      ]\n    },\n    {\n      \"idBuzon\": \"8\",\n      \"nombreBuzon\": \"Logistica1\",\n      \"nivelBuzon\": \"2\",\n      \"buzones\": [\n        {\n          \"idUsuarioBuzon\": \"2\",\n          \"idBuzon\": \"8\",\n          \"idVendedor\": \"2\",\n          \"nombreVendedor\": \"Nombre vendedor\",\n          \"estado\": \"ALTA\",\n          \"creado_el\": \"09/12/2015 17:26:02\",\n          \"creado_por\": \"usuario.pruebas\",\n          \"modificado_el\": \"09/12/2015 17:27:18\",\n          \"modificado_por\": \"usuario.pruebas\"\n        }\n      ]\n    }\n  ]\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"-205\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"No existen registros con esos par\\u00E1metros.\",\n    \"clase\": \"OperacionSolicitud\",\n    \"metodo\": \"doGet\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioConsulta.java",
    "groupTitle": "UsuarioBuzon"
  },
  {
    "type": "POST",
    "url": "/opsidra/modusuariobuzon/",
    "title": "[modUsuarioBuzon]",
    "name": "modUsuarioBuzon",
    "description": "<p>Servicio para modificar UsuarioBuzons creados.</p>",
    "group": "UsuarioBuzon",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operacion.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idUsuarioBuzon",
            "description": "<p>Identificador del buzon de usuario a modificar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idBuzon",
            "description": "<p>Identificador del buzon.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "estado",
            "description": "<p>Estado del buzon de usuario.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"codArea\": \"505\",\n    \"usuario\": \"usuario.pruebas\",\n    \"idUsuarioBuzon\": \"1\",\n    \"idBuzon\": \"1\",\n    \"estado\": \"ALTA\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"200\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"Campos agregados exitosamente.\",\n    \"clase\": \"OperacionUsuarioBuzon\",\n    \"metodo\": \"doPost\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \" \"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"-199\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"Los siguientes datos faltan o son incorrectos:  Tipo.\",\n    \"clase\": \"CtrlUsuarioBuzon\",\n    \"metodo\": \"validarInput\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \" \"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioOperacion.java",
    "groupTitle": "UsuarioBuzon"
  },
  {
    "type": "POST",
    "url": "/consultasidra/getventafe/",
    "title": "[getventafe]",
    "name": "getventafe",
    "description": "<p>Servicio para obtener las ventas sincronizadas en sidra que ya pasaron por facturac\\u00F3n electronica de CR.</p>",
    "group": "VENTAS_FACTURA_ELECTRONICA",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "getventafe": [
          {
            "group": "getventafe",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>codArea C\\u00F3digo de area del pa\\u00EDs.</p>"
          },
          {
            "group": "getventafe",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>usuario de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "getventafe",
            "type": "String",
            "optional": false,
            "field": "codDispositivo",
            "description": "<p>codDispositivo del usuario que solicita la informacion.</p>"
          },
          {
            "group": "getventafe",
            "type": "String",
            "optional": false,
            "field": "idVenta",
            "description": "<p>idVenta a consultar. Este campo es opcional.</p>"
          },
          {
            "group": "getventafe",
            "type": "String",
            "optional": false,
            "field": "idJornada",
            "description": "<p>idJornada a consultar. Este campo es opcional.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo parametros de Entrada:",
          "content": "  { \t\n\t\"codArea\":\"\",\n\t\"usuario\":\"\",\n\t\"codDispositivo\":\"\",\n\t\"token\":\"\",\n\t\"idVenta\":\"\",\n\t\"idJornada\":\"\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "{} JSON Respuesta-\\u00E9xito:",
          "content": "\t{\n\t\t\"token\":\"WEB\",\n     \"respuesta\": {\n        \"codResultado\":\"80\",\n        \"mostrar\":\"0\",\n        \"descripcion\":\"Ok. se obtuvo informacion de factura electr\\u00F3nica.\"\n       },\n       \"ventas\":[\n\t\t\t {\n\t\t\t  \"idVenta\":\"2657793\",\n\t\t\t  \"folio\":\"212\",\t\t\n\t\t\t  \"qr\":\"fkdjkdsgkdjsglsjglkdf\"\n\n\t\t\t  }\n\t\t  ]\n       }",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "{} JSON Respuesta-Error:     ",
          "content": "{\n   \"token\":\"\",\n   \"respuesta\": {\n       \"codResultado\": \"-205\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"No existen registros con esos par\\u00E1metros.\",\n       \"clase\": \"OperacionBodegaDTS\",\n       \"metodo\": \"doGet\",\n       \"excepcion\": \" \",\n       \"tipoExepcion\": \" \"\n   }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioConsulta.java",
    "groupTitle": "VENTAS_FACTURA_ELECTRONICA"
  },
  {
    "type": "POST",
    "url": "/opsidra/creaventaruta/",
    "title": "[creaventaruta]",
    "name": "creaventaruta",
    "description": "<p>Servicio para crear ventas de rutas.</p>",
    "group": "VENTAS",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs en el que se desea realizar la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codDispositivo",
            "description": "<p>c\\u00F3digo del dispositivo desde donde se realiza la operaci\\u00F3n, en caso de utilizarse la APP M\\u00F3VIL.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "token",
            "description": "<p>Token de validaci\\u00F3n de autenticaci\\u00F3n de usuario.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idVentaMovil",
            "description": "<p>Id generado por el m\\u00F3vil antes de sincronizar la venta.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idVendedor",
            "description": "<p>Id del vendedor que realiza la venta.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idJornada",
            "description": "<p>Id de la jornada en la que se realiza la venta.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idBodegaVendedor",
            "description": "<p>Id bodega de donde obtiene el inventario el vendedor.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "fecha",
            "description": "<p>Fecha en la que se realizo la venta, formato de fecha: YYYYmmddHHMMss.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "folioManual",
            "defaultValue": "1 o 0",
            "description": "<p>Indica si se maneja folios manuales o autom\\u00E1ticos, 1 = Folio Manual, 0 = Folio Autom\\u00E1tico.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idRangoFolio",
            "description": "<p>Id del registro del rango de folios. Opcional seg\\u00FAn el valor de folioManual.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "folio",
            "description": "<p>N\\u00FAmero de folio o factura generado por la app movil.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "serie",
            "description": "<p>Serie de factura. Opcional seg\\u00FAn el valor de folioManual.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "tipoDocumento",
            "description": "<p>Tipo de documento que se esta generando para la venta (TICKET, FACTURA, CCF, FCF).</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idTipo",
            "description": "<p>Id del cliente.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "tipo",
            "description": "<p>Indica el tipo de cliente de la venta es punto de venta o cliente final (PDV o CF).</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "nit",
            "description": "<p>Nit del cliente. Es obligatorio solo para documento ccf.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "giro",
            "description": "<p>Este campo solo es utilizado para ccf.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "registroFiscal",
            "description": "<p>este campo solo es utilizado para ccf.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "nombre",
            "description": "<p>Primer nombre del cliente.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "segundoNombre",
            "description": "<p>Segundo nombre del cliente.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "apellido",
            "description": "<p>Primer apellido del cliente.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "segundoApellido",
            "description": "<p>Segundo apellido del cliente.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "direccion",
            "description": "<p>Direcci\\u00F3n del cliente.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "numTelefono",
            "description": "<p>N\\u00FAmero de telefono del cliente.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "tipoDocCliente",
            "description": "<p>Tipo de documento de identificacion del cliente.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "numDocCliente",
            "description": "<p>N\\u00FAmero del documento de identificacion del cliente.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "exento",
            "description": "<p>Indica si el cliente esta exento de impuestos o no.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "impuestosExento",
            "description": "<p>Listado de impuestos exentos.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "impuestosExento.nombreImpuesto",
            "description": "<p>Nombre del impuesto exento.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idOfertaCampania",
            "description": "<p>Identificador de la oferta que origin\\u00F3 el descuento por monto de venta.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "descuentoMontoVenta",
            "description": "<p>Total de descuentos realizados sobre el monto de venta.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "montoFactura",
            "description": "<p>Monto total de la venta realizada.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "montoPagado",
            "description": "<p>Cantidad cancelada por el cliente.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "modoOnline",
            "defaultValue": "1 o 0",
            "description": "<p>Par\\u00E1metro que indica si la venta fue online u offline (1 = Online, 0 = Offline).</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "detallePago",
            "description": "<p>Detalle de pago de la venta.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "detallePago.formaPago",
            "description": "<p>Nombre de la forma de pago para el detallePago.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "detallePago.monto",
            "description": "<p>Monto de la factura que se cancelo con esa forma de pago.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "detallePago.banco",
            "description": "<p>N\\u00FAmero de autorizaci\\u00F3n generado si el pago es con tarjeta.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "detallePago.numAutorizacion",
            "description": "<p>N\\u00FAmero de autorizaci\\u00F3n generado si el pago es con tarjeta.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "detallePago.marcaTarjeta",
            "description": "<p>Marca de la tarjeta en caso de pagarse con una.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "detallePago.digitosTarjeta",
            "description": "<p>\\u00FAltimos 4 digitos de la tarjeta en caso de pagarse con una.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "detallePago.numeroCheque",
            "description": "<p>N\\u00FAmero de cheque si el pago es con el mismo.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "detallePago.fechaEmision",
            "description": "<p>Fecha de emision del cheque en caso de pagarse con uno, formato ddMMyyyy.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "detallePago.numeroReserva",
            "description": "<p>N\\u00FAmero de reserva en caso de pagarse con cheque.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "detallePago.cuentaCliente",
            "description": "<p>Cuenta del cliente en caso de pagarse con cheque.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "articulos",
            "description": "<p>Detalle de art\\u00EDculos a vender.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "articulos.articulo",
            "description": "<p>Id del art\\u00EDculo del que se esta realizando la venta.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "articulos.cantidad",
            "description": "<p>Cantidad de art\\u00EDculos que se esta vendiendo.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "articulos.seriado",
            "description": "<p>Indica si el art\\u00EDculo a vender tiene serie o no, valores: 1: Tiene serie. 0: No tiene serie</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "articulos.rango",
            "description": "<p>Indica si estoy vendiendo un rango de series o solo una serie, este aplica para las tarjetas rasca. Valores:1: Tiene rango de series. 0: No tiene rango de series .</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "articulos.serieInicial",
            "description": "<p>Indica de donde inicia la serie en caso de ser rango, ahora si solo es una serie se agrega la serie a vender.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "articulos.serieFinal",
            "description": "<p>Indica donde finaliza el rango de series a vender en caso de ser tarjetas rasca, cuando no es rango de series este campo va vac\\u00EDo.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "articulos.serieAsociada",
            "description": "<p>Serie en caso de que una terminal tenga un chip o simcard asociado.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "articulos.numTelefono",
            "description": "<p>N\\u00FAmero de tel\\u00E9fono en caso de ser un simcard.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "articulos.tipoInv",
            "defaultValue": "INV_TELCA",
            "description": "<p>Indica el tipo de inventario al que pertenece el art\\u00EDculo, para este caso es: INV_TELCA.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "articulos.precio",
            "description": "<p>Precio del art\\u00EDculo, este precio es el que se obtiene de scl o vantive.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "articulos.descuentoSCL",
            "description": "<p>Valor de descuento que tiene configurado un art\\u00EDculo en scl.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "articulos.descuentoSidra",
            "description": "<p>Descuento que puede tener un art\\u00EDculo para sidra.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "articulos.idOfertaCampania",
            "description": "<p>Id de la oferta de la que se obtuvo el descuento de sidra.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "detalleDescuentosSidra",
            "description": "<p>Detalle de descuentos del art\\u00EDculo de venta.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "detalleDescuentosSidra.descuento",
            "description": "<p>Valor del descuento a aplicar al art\\u00EDculo.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "detalleDescuentosSidra.idOfertaCampania",
            "description": "<p>Id de la campa\\u00F1a a la que pertenece la oferta del art\\u00EDculo.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "detalleDescuentosSidra.idCondicion",
            "description": "<p>Id de la condici\\u00F3n por la que se aplic\\u00F3 el descuento. Solo necesario en caso de ser oferta de tipo PAGUE_LLEVE.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "detalleDescuentosSidra.tipoDescuento",
            "description": "<p>Tipo del descuento que se aplica en la oferta.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "articulos.gestion",
            "description": "<p>Nombre de la gesti\\u00F3n a realizar en la venta.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "articulos.impuesto",
            "description": "<p>Impuestos calculados al art\\u00EDculo.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "articulos.precioTotal",
            "description": "<p>Precio final con impuestos y descuentos.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "articulos.modalidad",
            "description": "<p>Este campo aplica unicamente para el caso de recargas, indica si la recarga se hizo desde sidra o fuera del sistema de sidra.          *</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "articulos.impuestosArticulo",
            "description": "<p>Detalle de impuestos por art\\u00EDculo a vender.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "articulos.impuestosArticulo.nombreImpuesto",
            "description": "<p>Nombre de un impuesto espec\\u00EDfico calculado al art\\u00EDculo.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "articulos.impuestosArticulo.valor",
            "description": "<p>Valor total del impuesto espec\\u00EDfico que se suma al precio del art\\u00EDculo.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "articulosPromocionales",
            "description": "<p>Detalle de art\\u00EDculos promocionales a regalar en la venta.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "articulosPromocionales.idOfertaCampania",
            "description": "<p>Id de la campa\\u00F1a a la que pertenece el art\\u00EDculo promocional.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "articulosPromocionales.articuloPromocional",
            "description": "<p>Id del art\\u00EDculo promocional si la venta aplica a regalar art\\u00EDculos promocionales.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "articulosPromocionales.cantidad",
            "description": "<p>Cantidad de art\\u00EDculos promocionales a regalar.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n   \"usuario\": \"usuario.pruebas\",\n   \"codDispositivo\": \"64E1CE4A48BF656974C376B7905F7E62\",\n   \"codArea\": \"505\",\n   \"token\": \"865454545\",\n   \"idVentaMovil\": \"1\",\n   \"idVendedor\": \"1\",\n   \"idJornada\": \"1\",\n   \"idBodegaVendedor\": \"1\",\n   \"fecha\": \"YYYYmmddHHMMss\",\n   \"folioManual\": \"1\",\n   \"idRangoFolio\": \"3\",\n   \"folio\": \"11221\",\n   \"serie\": \"a\",\n   \"tipoDocumento\": \"ds\",\n   \"idTipo\": \"1\",\n   \"tipo\": \"PDV/CF\",\n   \"nit\": \"456445\",\n   \"registroFiscal\": \"456445\",\n   \"giro\": \"456445\",\n   \n   \"nombre\": \"Juan\",\n   \"segundoNombre\": \"\",\n   \"apellido\": \"D\\u00EDaz\",\n   \"segundoApellido\": \"\",\n   \"direccion\": \"Nicaragua\",\n   \"numTelefono\": \"12345678\",\n   \"tipoDocCliente\": \"Pasaporte\",\n   \"numDocCliente\": \"1821832151201\",\n       \n   \"exento\": \"NO EXENTO\",\n   \"impuestosExento\": [{\n        \"nombreImpuesto\": \"ISC\"\n    }],\n   \n   \"idOfertaCampania\": \"\",\n   \"descuentoMontoVenta\": \"\",\n   \"montoFactura\": \"\",\n   \"montoPagado\": \"\",\n   \n   \"detallePago\": [{\n       \"formaPago\": \"Tarjeta\",\n       \"monto\": \"50.181234\",\n       \"banco\": \"BANCO\",\n       \"numAutorizacion\": \"118588151358513\",\n       \"marcaTarjeta\": \"VISA\",\n       \"digitosTarjeta\": \"4241\",\n       \"numeroCheque\": \"\",\n       \"fechaEmision\": \"\",\n       \"numeroReserva\": \"\",\n       \"cuentaCliente\": \"\"\n   }, {\n       \"formaPago\": \"Efectivo\",\n       \"monto\": \"25.01234\",\n       \"banco\": \"\",\n       \"numAutorizacion\": \"\",\n       \"marcaTarjeta\": \"\",\n       \"digitosTarjeta\": \"\",\n       \"numeroCheque\": \"\",\n       \"fechaEmision\": \"\",\n       \"numeroReserva\": \"\",\n       \"cuentaCliente\": \"\"\n   }, {\n       \"formaPago\": \"Tarjeta\",\n       \"monto\": \"50.181234\",\n       \"banco\": \"BANCO\",\n       \"numAutorizacion\": \"\",\n       \"marcaTarjeta\": \"\",\n       \"digitosTarjeta\": \"\",\n       \"numeroCheque\": \"124\",\n       \"fechaEmision\": \"10022017\",\n       \"numeroReserva\": \"245\",\n       \"cuentaCliente\": \"12896742-553-1\"\n   }],\n\n   \"articulos\": [{\n       \"articulo\": \"1\",\n       \"cantidad\": \"1\",\n       \"seriado\": \"1\",\n       \"rango\": \"0\",\n       \"serieInicial\": \"232\",\n       \"serieFinal\": \"\",\n       \"serieAsociada\": \"2332\",\n       \"numTelefono\": \"74552332\",\n       \"tipoInv\": \"INV_TELCA\",\n       \"precio\": \"55\",\n       \"descuentoSCL\": \"4\",\n       \"descuentoSidra\": \"4\",\n       \"detalleDescuentosSidra\": [\n         {\n            \"descuento\": \"0.6\",\n            \"idOfertaCampania\": \"11\",\n            \"idCondicion\": \"\",\n            \"tipoDescuento\": \"ARTICULO\"\n         },{\n            \"descuento\": \"0.1\",\n            \"idOfertaCampania\": \"11\",\n            \"idCondicion\": \"\",\n            \"tipoDescuento\": \"TECNOLOGIA\"\n         }\n       ],\n       \"gestion\": \"ALTA PREPAGO\",\n       \"impuesto\": \"\",\n       \"precioTotal\": \"\",\n       \"modalidad\": \"\",\n       \"impuestosArticulo\": [\n           {\n               \"nombreImpuesto\": \"\",\n               \"valor\": \"\"\n           }\n       ]\n   }],\n   \n   \"articulosPromocionales\": [{\n       \"idOfertaCampania\": \"\",\n       \"articuloPromocional\": \"\",\n       \"cantidad\": \"\"\n   }, {\n       \"idOfertaCampania\": \"\",\n       \"articuloPromocional\": \"\",\n       \"cantidad\": \"\"\n   }]\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "{} JSON Respuesta-\\u00E9xito:",
          "content": "{\n  \"token\": \"WEB\",\n  \"respuesta\": {\n    \"codResultado\": \"18\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"Ok.Venta registrada correctamente.\",\n    \"clase\": \"\",\n    \"metodo\": \"\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n  \"token\": \"WEB\",\n  \"respuesta\": {\n    \"codResultado\": \"-92\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"La Jornada  no esta asociada al vendedor ingresado.\",\n    \"clase\": \"CtrlVentaRuta\",\n    \"metodo\": \"creaVentaRuta\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioOperacion.java",
    "groupTitle": "VENTAS"
  },
  {
    "type": "POST",
    "url": "/consultasidra/getdetalleventa/",
    "title": "[getdetalleventa]",
    "name": "getdetalleventa",
    "description": "<p>Servicio para obtener los detalles de una venta registrada en SIDRA.</p>",
    "group": "VENTAS",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de area del pa\\u00EDs en el que se desea obtener informaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre del usuario que solicita la informaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codDispositivo",
            "description": "<p>c\\u00F3digo del dispositivo desde donde se realiza la operaci\\u00F3n, en caso de utilizarse la APP M\\u00F3VIL.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idVenta",
            "description": "<p>Id de la venta de la que se desea obtener datos de su detalle.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "min",
            "description": "<p>Registro m\\u00EDnimo a consultar para pagineo.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "max",
            "description": "<p>Registro m\\u00E1ximo a consultar para pagineo.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"codArea\": \"505\",\n    \"usuario\":\"usuario.sidra\",\n    \"codDispositivo\": \"64E1CE4A48BF656974C376B7905F7E62\",\n    \"idVenta\":\"\",\n    \"min\":\"\",\n    \"max\":\"\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n   \"respuesta\": {\n      \"codResultado\": \"12\",\n      \"mostrar\": \"0\",\n      \"descripcion\": \"OK. Datos obtenidos exitosamente\",\n      \"clase\": \" \",\n      \"metodo\": \" \",\n      \"excepcion\": \" \",\n      \"tipoExepcion\": \"\"\n   },\n   \"detalleVenta\": [\n   {\n      \"idVentaDet\": \"162\",\n      \"articulo\": \"1173\",\n      \"descripcion\": \"RECARGA ELECTRONICA\",\n      \"cantidad\": \"100\",\n      \"seriado\": \"0\",\n      \"serie\": \"\",\n      \"serieAsociada\": \"\",\n      \"numTelefono\": \"12312312\",\n      \"tipoInv\": \"INV_TELCA\",\n      \"precio\": \"88\",\n      \"descuentoSCL\": \"0\",\n      \"descuentoSidra\": \"0\",\n      \"detalleDescuentosSidra\": {\n        \"idOfertaCampania\": \"Inicio de verano\",\n        \"nombreOfertaCampania\": \".3\",\n        \"descuento\": \"0.30\"\n      },\n      \"gestion\": \"ALTAPREPAGO\",\n      \"impuesto\": \"12\",\n      \"precioTotal\": \"100\",\n      \"modalidad\": \"\",\n      \"estado\": \"ALTA\",\n      \"impuestosArticulo\": [\n        {\n          \"nombreImpuesto\": \"ITBMS\",\n          \"valor\": \"10\"\n        },\n        {\n          \"nombreImpuesto\": \"ISC\",\n          \"valor\": \"2\"\n        }\n      ]\n    }\n  ]\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n    \"respuesta\": {\n        \"codResultado\": \"-42\",\n        \"mostrar\": \"1\",\n        \"descripcion\": \"No se encontraron datos.\",\n        \"clase\": \"CtrlVenta\",\n        \"metodo\": \"getDetalleVenta\",\n        \"excepcion\": \" \",\n        \"tipoExepcion\": \"Generales\"\n    }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioConsulta.java",
    "groupTitle": "VENTAS"
  },
  {
    "type": "POST",
    "url": "/consultasidra/getventa/",
    "title": "[getventa]",
    "name": "getventa",
    "description": "<p>Servicio para obtener informaci\\u00F3n de las ventas que se han hecho en SIDRA.</p>",
    "group": "VENTAS",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de area del pa\\u00EDs en el que se desea obtener informaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre del usuario que solicita la informaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codDispositivo",
            "description": "<p>c\\u00F3digo del dispositivo desde donde se realiza la operaci\\u00F3n, en caso de utilizarse la APP M\\u00F3VIL.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idVenta",
            "description": "<p>Id de la venta que se desea obtener.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idJornada",
            "description": "<p>Id de la jornada que se desea consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idVendedor",
            "description": "<p>Id del vendedor del cual se desean obtener las ventas registradas.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idDTS",
            "description": "<p>Id del Distribuidor del cual se desea obtener informaci\\u00F3n de las ventas asociadas a sus vendedores.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idRutaPanel",
            "description": "<p>Id de la Ruta o Panel de la cual se quiere obtener la informaci\\u00F3n de ventas que estas han realizado.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "tipoRutaPanel",
            "defaultValue": "RUTA o PANEL",
            "description": "<p>Tipo del ID de la Ruta o Panel de la cual se quiere obtener la informaci\\u00F3n de ventas que han realizado.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idTipo",
            "description": "<p>Id del punto de venta del que se desea obtener informaci\\u00F3n de vetas.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "tipoCliente",
            "description": "<p>Tipo de cliente que se desea consultar, CF o PDV.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "tipoDocumento",
            "description": "<p>Tipo de documento que se desea consultar TCK, CCF, etc.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "serie",
            "description": "<p>Serie de la venta a consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "folio",
            "description": "<p>N\\u00FAmero de folio o factura de venta que se desea consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "nit",
            "description": "<p>N\\u00FAmero de NIT del cliente registrado en la venta que se desea consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "primerNombreCliente",
            "description": "<p>Primer nombre del cliente registrado en la venta que se desea consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "primerApellidoCliente",
            "description": "<p>Primer apellido del cliente registrado en la venta que se desea consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "numTelefono",
            "description": "<p>N\\u00FAmero de tel\\u00E9fono del cliente registrado en la venta que se desea consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "tipoDocumentoCliente",
            "description": "<p>Tipo de documento de identificaci\\u00F3n del cliente registrado en la venta que se desea consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "numDocCliente",
            "description": "<p>N\\u00FAmero del documento de identificaci\\u00F3n del cliente registrado en la venta que se desea consultar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "fechaInicio",
            "description": "<p>Rango inicial de fechas a tomar en cuenta para obtener datos. FORMATO:YYYYmmdd.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "fechaFin",
            "description": "<p>Rango final de fechas a tomar en cuenta para obtener datos. FORMATO:YYYYmmdd.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"token\": \"WEB\",\n    \"usuario\": \"usuario.prueba\",\n    \"codDispositivo\": \"64E1CE4A48BF656974C376B7905F7E62\",\n    \"codArea\": \"505\",\n    \"idVenta\": \"\",\n    \"idJornada\": \"\",\n    \"idVendedor\": \"\",\n    \"idDTS\": \"5\",\n    \"idRutaPanel\": \"\",\n    \"tipoRutaPanel\": \"\",\n    \"idTipo\": \"\",\n    \"tipoCliente\": \"\",\n    \"tipoDocumento\": \"\",\n    \"serie\": \"\",\n    \"folio\": \"\",\n    \"serieSidra\": \"\",\n    \"folioSidra\": \"\",\n    \"nit\": \"\",\n    \"primerNombreCliente\": \"\",\n    \"primerApellidoCliente\": \"\",\n    \"numTelefono\": \"\",\n    \"tipoDocumentoCliente\": \"\",\n    \"numDocCliente\": \"\",\n    \"fechaInicio\": \"\",\n    \"fechaFin\": \"\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"12\",\n    \"mostrar\": \"0\",\n    \"descripcion\": \"OK. Datos obtenidos exitosamente\",\n    \"clase\": \" \",\n    \"metodo\": \" \",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"\"\n  },\n  \"venta\": {\n    \"idVenta\": \"44\",\n    \"idVendedor\": \"2322\",\n    \"nombreVendedor\": \"usuario sidra\",\n    \"idJornada\": \"44\",\n    \"idBodegaVendedor\": \"28\",\n    \"bodegaVendedor\": \"BODEGA VIRTUAL\",\n    \"fecha\": \"26/04/2016 11:51:13\",\n    \"folio\": \"13\",\n    \"serie\": \"A\",\n    \"folioSidra\": \"\",\n    \"serieSidra\": \"\",\n    \"tipoDocumento\": \"TCK\",\n    \"idTipo\": \"42345678\",\n    \"tipo\": \"CF\",\n    \"nombrePDV\": \"\",\n    \"departamento\": \"\",\n    \"municipio\": \"\",\n    \"nit\": \"\",\n    \"registroFiscal\": \"\",\n    \"giro\": \"\",\n    \"nombreFiscal\": \"44\",\n    \"nombre\": \"Hared\",\n    \"segundoNombre\": \"\",\n    \"apellido\": \"M\\u00E9ndez\",\n    \"segundoApellido\": \"Amaya\",\n    \"direccion\": \"Guatemala\",\n    \"numTelefono\": \"42345678\",\n    \"tipoDocCliente\": \"Pasaporte\",\n    \"numDocCliente\": \"1821832151201\",\n    \"nombresFacturacion\": \"\",\n    \"apellidosFacturacion\": \"\",\n    \"zonaComercial\": \"Ana baez, Llanos del sol\",\n    \"exento\": \"NO EXENTO\",\n    \"impuesto\": \"16.95557\",\n    \"idOfertaCampania\": \"\",\n    \"descuentoMontoVenta\": \"\",\n    \"descuentoTotal\": \"5\",\n    \"estado\": \"REGISTRADO_SIDRA\",\n    \"montoFactura\": \"75.181\",\n    \"montoPagado\": \"75.18\",\n    \"nombrePanelRuta\": \"RUTA UNO\",\n    \"creadoPor\": \"usuario.pruebas\",\n    \"creadoEl\": \"02/06/2016 10:20:54\",\n    \"envioAlarma\": \"0\"\n    \"detallePago\": [{\n        \"formaPago\": \"Tarjeta\",\n        \"monto\": \"50.1800000001234\",\n        \"numReferencia\": \"123456789012345\",\n        \"numAutorizacion\": \"118588151358513\"\n    }, {\n        \"formaPago\": \"Efectivo\",\n        \"monto\": \"25.000000001234\",\n        \"numReferencia\": \"\",\n        \"numAutorizacion\": \"\"\n    }],\n    \"articulosPromocionales\": {\n      \"articuloPromocional\": \"PLAYERA S\",\n      \"cantidad\": \"3\"\n    }\n  }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n    \"respuesta\": {\n        \"codResultado\": \"-205\",\n        \"mostrar\": \"1\",\n        \"descripcion\": \"No existen registros con esos par\\u00E1metros.\",\n        \"clase\": \"CtrlVenta\",\n        \"metodo\": \"getVenta\",\n        \"excepcion\": \" \",\n        \"tipoExepcion\": \"Generales\"\n    }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioConsulta.java",
    "groupTitle": "VENTAS"
  },
  {
    "type": "POST",
    "url": "/opsidra/asignavendedordts/",
    "title": "[asignavendedordts]",
    "name": "asignavendedordts",
    "description": "<p>Servicio para crear VendedorDTSes por pa\\u00EDs.</p>",
    "group": "VendedorDTS",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs en el que se desea realizar la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idDistribuidor",
            "description": "<p>Identificador del distribuidor a asociar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idBodegaVirtual",
            "description": "<p>Identificador de la bodega virtual que ser\\u00E1 asociada.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idVendedor",
            "description": "<p>Identificador del vendedor a asociar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuarioVendedor",
            "description": "<p>Usuario del vendedor a asociar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "nombres",
            "description": "<p>Nombre o nombres del vendedor que se desea asociar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "apellidos",
            "description": "<p>Apellido o apellidos del vendedor que se desea asociar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "canal",
            "description": "<p>canal de distribuci\\u00F3n del vendedor.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "subcanal",
            "description": "<p>subcanal de distribuci\\u00F3n del vendedor.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "numeroRecarga",
            "description": "<p>n\\u00FAmero que tendran disponible el vendedor para vender o transferir saldo de recargas.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "pin",
            "description": "<p>pin del n\\u00FAmero de recarga.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "dtsFuente",
            "description": "<p>n\\u00FAmero de distribuidor fuente del n\\u00FAmero de recarga.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codVendedor",
            "description": "<p>C\\u00F3digo de vendedor de SCL (\\u00FAnicamente PA).</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "email",
            "description": "<p>Email del vendedor que se desea asociar.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"codArea\":\"505\",\n    \"usuario\":\"usuario.pruebas\",\n    \"idDistribuidor\":\"96\",\n    \"idBodegaVirtual\":\"205\",\n    \"idVendedor\":\"305\",\n    \"usuarioVendedor\":\"sergio.lujan\",\n    \"nombres\":\"Sergio\",\n    \"apellidos\":\"Lujan\",\n    \"canal\":\"SIDRA\",\n    \"subcanal\":\"MULTIMARCA\",\n    \"numeroRecarga\":\"45502255\",\n    \"pin\":\"1123\",\n    \"dtsFuente\":\"111\",\n    \"email\":\"slujan@dts.com\",\n    \"codVendedor\":\"442\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"200\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"Campos agregados exitosamente.\",\n    \"clase\": \"OperacionVendedorDTS\",\n    \"metodo\": \"doPost\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"-160\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"El vendedor ya se encuentra registrado.\",\n    \"clase\": \"OperacionVendedorDTS\",\n    \"metodo\": \"doPost\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioOperacion.java",
    "groupTitle": "VendedorDTS"
  },
  {
    "type": "POST",
    "url": "/opsidra/bajavendedordts/",
    "title": "[bajavendedordts]",
    "name": "bajavendedordts",
    "description": "<p>Servicio para dar de baja un VendedorDTS por pa\\u00EDs.</p>",
    "group": "VendedorDTS",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs en el que se desea realizar la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idVendedorDTS",
            "description": "<p>Identificador del vendedor que se desea dar de baja.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"codArea\":\"505\",\n    \"usuario\":\"usuario.pruebas\",\n    \"idVendedorDTS\":\"4\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n   \"respuesta\": {\n       \"codResultado\": \"201\",\n       \"mostrar\": \"1\",\n       \"descripcion\": \"Recurso modificado exitosamente.\",\n       \"clase\": \"OperacionVendedorDTS\",\n       \"metodo\": \"doPutDel\",\n       \"excepcion\": \" \",\n       \"tipoExepcion\": \" \"\n   }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"-178\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"Las bodegas virtuales asociadas al vendedor poseen inventario.\",\n    \"clase\": \"OperacionVendedorDTS\",\n    \"metodo\": \"doPutDel\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioOperacion.java",
    "groupTitle": "VendedorDTS"
  },
  {
    "type": "POST",
    "url": "/consultasidra/getvenddts/",
    "title": "[getVendedorDTS]",
    "name": "getVendedorDTS",
    "description": "<p>Servicio para obtener los datos de VendedorDTSes internos o externos configurados por pa\\u00EDs.</p>",
    "group": "VendedorDTS",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs en el que se desea realizar la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operacion.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idVendedorDTS",
            "description": "<p>Identificador de la asociacion distribuidor-vendedor.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idDistribuidor",
            "description": "<p>Identificador del distribuidor asociado.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idBodegaVirtual",
            "description": "<p>Identificador de la bodega virtual asociada.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "idVendedor",
            "description": "<p>Identificador del vendedor a asociar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "usuarioVendedor",
            "description": "<p>Usuario del vendedor a asociar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "estado",
            "defaultValue": "ALTA o BAJA",
            "description": "<p>Estado del cu\\u00E1l se desea obtener informaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "soloDisponibles",
            "defaultValue": "0 o 1",
            "description": "<p>Permite mostrar \\u00FAnicamente los vendedores asociados a distribuidor que a\\u00FAn no poseen asociada una ruta o panel. 1 = Solo disponibles para asociar. 0 = Todos los vendedores.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"codArea\":\"505\",\n    \"usuario\":\"usuario.pruebas\",\n    \"idVendedorDTS\":\"\",\n    \"idDistribuidor\":\"60\",\n    \"idBodegaVirtual\":\"\",\n    \"idVendedor\":\"\",\n    \"usuarioVendedor\":\"\",\n    \"estado\":\"\",\n    \"soloDisponibles\":\"\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"0\",\n    \"mostrar\": \"0\",\n    \"descripcion\": \"Recursos recuperados exitosamente.\"\n  },\n  \"vendedores\": [\n    {\n      \"idVendedorDTS\": \"1\",\n      \"idDTS\": \"60\",\n      \"nombreDistribuidor\": \"DISTRIBUIDOR ISOFT 1001\",\n      \"idBodegaVirtual\": \"237\",\n      \"nombreBodegaVirtual\": \"BODEGA DE PRUEBA ISOFT #1\",\n      \"idBodegaVendedor\": \"30\",\n      \"idVendedor\": \"1\",\n      \"usuarioVendedor\": \"susana.barrios\",\n      \"nombres\": \"Susana\",\n      \"apellidos\": \"Barrios\",\n      \"canal\": \"SIDRA\",\n      \"subcanal\": \"MULTIMARCA\",\n      \"numeroRecarga\": \"45968700\",\n      \"pin\": \"1123\",\n      \"dtsFuente\": \"113\",\n      \"email\": \"sbarrios@dts.com\",\n      \"estado\": \"ALTA\",\n      \"tipoAsociado\": \"RUTA\",\n      \"nombreAsociado\": \"RUTA MOVIL ZONA 1\",\n      \"creado_el\": \"08/02/2016 11:31:38\",\n      \"creado_por\": \"usuario.pruebas\"\n    }\n  ]\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"-205\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"No existen registros con esos par\\u00E1metros.\",\n    \"clase\": \"OperacionVendedorDTS\",\n    \"metodo\": \"doGet\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioConsulta.java",
    "groupTitle": "VendedorDTS"
  },
  {
    "type": "POST",
    "url": "/opsidra/modVendedorDTS/",
    "title": "[modVendedorDTS]",
    "name": "modVendedorDTS",
    "description": "<p>Servicio para modificar asociaciones de Vendedores con Distribuidores Sidra.</p>",
    "group": "VendedorDTS",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs en el que se desea realizar la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idVendedorDTS",
            "description": "<p>Identificador de la asociaci\\u00F3n de vendedor a modificar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idDistribuidor",
            "description": "<p>Identificador del distribuidor a asociar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idBodegaVirtual",
            "description": "<p>Identificador de la bodega virtual que ser\\u00E1 asociada.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "nombres",
            "description": "<p>Nombre o nombres del vendedor que se desea modificar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "apellidos",
            "description": "<p>Apellido o apellidos del vendedor que se desea modificar.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "canal",
            "description": "<p>canal de distribuci\\u00F3n del vendedor.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "subcanal",
            "description": "<p>subcanal de distribuci\\u00F3n del vendedor.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "numeroRecarga",
            "description": "<p>n\\u00FAmero que tendran disponible el vendedor para vender o transferir saldo de recargas.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "pin",
            "description": "<p>pin del n\\u00FAmero de recarga.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "dtsFuente",
            "description": "<p>n\\u00FAmero de distribuidor fuente del n\\u00FAmero de recarga.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "email",
            "description": "<p>Email del vendedor que se desea asociar.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n    \"codArea\":\"505\",\n    \"usuario\":\"usuario.pruebas\",\n    \"idVendedorDTS\":\"100\",\n    \"idDistribuidor\":\"96\",\n    \"idBodegaVirtual\":\"205\",\n    \"nombres\":\"Sergio\",\n    \"apellidos\":\"Lujan\",\n    \"canal\":\"SIDRA\",\n    \"subcanal\":\"MULTIMARCA\",\n    \"numeroRecarga\":\"45502255\",\n    \"pin\":\"1123\",\n    \"dtsFuente\":\"111\",\n    \"email\":\"slujan@dts.com\",\n    \"estado\":\"ALTA\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"200\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"Campos modificados exitosamente.\",\n    \"clase\": \"OperacionVendedorDTS\",\n    \"metodo\": \"doPost\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n    \"respuesta\": {\n        \"codResultado\": \"-201\",\n        \"mostrar\": \"1\",\n        \"descripcion\": \"No existe el recurso deseado.\",\n        \"clase\": \"OperacionVendedorDTS\",\n        \"metodo\": \"doPutDel\",\n        \"excepcion\": \" \",\n        \"tipoExepcion\": \"Generales\"\n    }\n }",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioOperacion.java",
    "groupTitle": "VendedorDTS"
  },
  {
    "type": "POST",
    "url": "/consultasidra/getticket/",
    "title": "[getticket]",
    "name": "getticket",
    "description": "<p>Servicio para obtener ticket de una venta especifica,</p>",
    "group": "VisorTCK",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "token",
            "description": "<p>token de validaci\\u00F3n de usuario.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs en el que se desea realizar la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codDispositivo",
            "description": "<p>c\\u00F3digo del dispositivo desde donde se realiza la operaci\\u00F3n, en caso de utilizarse la APP M\\u00F3VIL.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "IdVenta",
            "description": "<p>Identificador de la venta a obtener ticket.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo parametros de Entrada:",
          "content": "{\n\t\"codArea\":\"503\",\n\t\"usuario\":\"pruebas\",\n\t\"codDispositivo\":\"456483\",\n \t\"token\":\"WEB\",\n\t\"idVenta\":\"1179\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "{} JSON Respuesta-\\u00E9xito:",
          "content": "{\n    \"token\": \"WEB\",\n    \"idVenta\": \"1179\",\n    \"lineas\": [\n        {\n            \"izquierda\": \"\",\n            \"centro\": \"\",\n            \"derecha\": \"\",\n            \"alineacion\": \"E\",\n            \"estilo\": \"\",\n            \"extra\": \"\"\n        },\n        {\n            \"izquierda\": \"TELEFONICA MOVILES EL SALVADOR, S.A. DE C.V.\",\n            \"centro\": \"\",\n            \"derecha\": \"\",\n            \"alineacion\": \"C\",\n            \"estilo\": \"\",\n            \"extra\": \"\"\n        }\n    ]\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "{} JSON Respuesta-Error:     ",
          "content": "{\n    \"token\": \"WEB\",\n    \"respuesta\": {\n        \"codResultado\": \"-921\",\n        \"mostrar\": \"1\",\n        \"descripcion\": \"El par\\u00E1metro de entrada \\\\\\\"idVenta\\\\\\\" esta vacio.\",\n        \"clase\": \" \",\n        \"metodo\": \"getTicket\",\n        \"excepcion\": \"class com.consystec.sc.sv.ws.metodos.CtrlVisorTicket\",\n        \"tipoExepcion\": \"Generales\"\n    }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioConsulta.java",
    "groupTitle": "VisorTCK"
  },
  {
    "type": "POST",
    "url": "/opsidra/registraticket/",
    "title": "[registraticket]",
    "name": "registraticket",
    "description": "<p>Servicio para registrar la impresion de ticket en SIDRA.</p>",
    "group": "VisorTCK",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codArea",
            "description": "<p>C\\u00F3digo de \\u00E1rea del pa\\u00EDs en el que se desea realizar la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "usuario",
            "description": "<p>Nombre de usuario que solicita la operaci\\u00F3n.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "token",
            "description": "<p>token de validaci\\u00F3n de usuario.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "codDispositivo",
            "description": "<p>c\\u00F3digo del dispositivo desde donde se realiza la operaci\\u00F3n, en caso de utilizarse la APP M\\u00F3VIL.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "idVenta",
            "description": "<p>Id de la venta de la cual se registrar\\u00E1 el visor.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "lineas",
            "description": "<p>detalle de lineas del ticket.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Ejemplo par\\u00E1metros de Entrada:",
          "content": "{\n\t'codArea':'503',\n\t'usuario':'pruebas',\n\t'codDispositivo':'456483',\n\t'token':'123484531813216843a5468as1d5123198135a1sd86151a2sd',\n\t'idVenta':'100',\n\t'lineas': [{\n\t\t\t\t\t'izquierda': '',\n\t\t\t\t\t'centro': null,\n\t\t\t\t\t'derecha': '',\n\t\t\t\t\t'alineacion': 'E',\n\t\t\t\t\t'estilo': null,\n\t\t\t\t\t'extra': null\n\t\t\t\t}, {\n\t\t\t\t\t'izquierda': 'TELEFONICA MOVILES EL SALVADOR, S.A. DE C.V.',\n\t\t\t\t\t'centro': null,\n\t\t\t\t\t'derecha': '',\n\t\t\t\t\t'alineacion': 'C',\n\t\t\t\t\t'estilo': null,\n\t\t\t\t\t'extra': null\n\t\t\t\t}\n\t\t\t\t]\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Respuesta-\\u00E9xito:",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"64\",\n    \"token\":\"gfdgdfg\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"OK. Se registr\\u00F3 el archivo adjunto correctamente.\",\n    \"clase\": \"CtrlCargaArchivosPorta\",\n    \"metodo\": \"cargarArchivoPorta\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Respuesta-Error:     ",
          "content": "{\n  \"respuesta\": {\n    \"codResultado\": \"-199\", \n    \"token\":\"gfdgdfg\",\n    \"mostrar\": \"1\",\n    \"descripcion\": \"Los siguientes datos faltan o son incorrectos:  Tipo Documento no corresponde a ninguno de los tipos definidos.\",\n    \"clase\": \"CtrlCargaArchivosPorta\",\n    \"metodo\": \"cargarArchivoPorta\",\n    \"excepcion\": \" \",\n    \"tipoExepcion\": \"Generales\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "permission": [
      {
        "name": "admin"
      }
    ],
    "filename": "WsDirectorSidraSv/src/com/consystec/sc/ca/ws/service/ServicioOperacion.java",
    "groupTitle": "VisorTCK"
  }
] });
