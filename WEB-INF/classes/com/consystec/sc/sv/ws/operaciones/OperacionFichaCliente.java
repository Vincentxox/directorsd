package com.consystec.sc.sv.ws.operaciones;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.ws.Holder;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.telefonica.globalintegration.header.HeaderInType;
import com.telefonica.globalintegration.header.HeaderOutType;
import com.telefonica.globalintegration.services.createcustomeraccount.v1.AttributeValuePairItemType;
import com.telefonica.globalintegration.services.createcustomeraccount.v1.AttributeValuePairListItemType;
import com.telefonica.globalintegration.services.createcustomeraccount.v1.CreateCustomerAccountRequestType;
import com.telefonica.globalintegration.services.createcustomeraccount.v1.CreateCustomerAccountResponseType;
import com.telefonica.globalintegration.services.createlocation.v1.CreateLocationRequestType;
import com.telefonica.globalintegration.services.createlocation.v1.CreateLocationResponseType;
import com.telefonica.globalintegration.services.createresidentialcustomer.v1.ContactMediumItemType;
import com.telefonica.globalintegration.services.createresidentialcustomer.v1.ContactMediumListItemType;
import com.telefonica.globalintegration.services.createresidentialcustomer.v1.CountryItemType;
import com.telefonica.globalintegration.services.createresidentialcustomer.v1.CreateResidentialCustomerRequestType;
import com.telefonica.globalintegration.services.createresidentialcustomer.v1.CreateResidentialCustomerResponseType;
import com.telefonica.globalintegration.services.createresidentialcustomer.v1.CustomerAccountLocationItemType;
import com.telefonica.globalintegration.services.createresidentialcustomer.v1.CustomerAccountLocationListItemType;
import com.telefonica.globalintegration.services.createresidentialcustomer.v1.GeographicAddressItemType;
import com.telefonica.globalintegration.services.createresidentialcustomer.v1.IndividualContactItemType;
import com.telefonica.globalintegration.services.createresidentialcustomer.v1.IndividualContactListItemType;
import com.telefonica.globalintegration.services.createresidentialcustomer.v1.IndividualIdentificationItemType;
import com.telefonica.globalintegration.services.createresidentialcustomer.v1.LanguageItemType;
import com.telefonica.globalintegration.services.createresidentialcustomer.v1.OccupationIndividualItemType;
import com.telefonica.globalintegration.services.createresidentialcustomer.v1.TimePeriodItemType;
import com.telefonica.globalintegration.services.getresidentialcustomer.v1.GetResidentialCustomerRequestType;
import com.telefonica.globalintegration.services.getresidentialcustomer.v1.GetResidentialCustomerResponseType;
import com.telefonica.globalintegration.services.soap.createcustomeraccount.v1.CreateCustomerAccountBindingQSService;
import com.telefonica.globalintegration.services.soap.createcustomeraccount.v1.CreateCustomerAccountV1;
import com.telefonica.globalintegration.services.soap.createlocation.v1.CreateLocationBindingQSService;
import com.telefonica.globalintegration.services.soap.createlocation.v1.CreateLocationV1;
import com.telefonica.globalintegration.services.soap.createresidentialcustomer.v1.CreateResidentialCustomerBindingQSService;
import com.telefonica.globalintegration.services.soap.createresidentialcustomer.v1.CreateResidentialCustomerV1;
import com.telefonica.globalintegration.services.soap.createresidentialcustomer.v1.MessageFault;
import com.telefonica.globalintegration.services.soap.getresidentialcustomer.v1.GetResidentialCustomerBindingQSService;
import com.telefonica.globalintegration.services.soap.getresidentialcustomer.v1.GetResidentialCustomerV1;
import com.telefonica.globalintegration.soap.createaddress_v1.v1.CreateAddressRequestType;
import com.telefonica.globalintegration.soap.createaddress_v1.v1.CreateAddressResponseType;
import com.telefonica.globalintegration.soap.createaddress_v1.v1.CustomGeographicAreaItemType;
import com.telefonica.wsdl.globalintegration.soap.basename.v1.createaddress_v1.CreateAddressV1;
import com.telefonica.wsdl.globalintegration.soap.basename.v1.createaddress_v1.CreateAddressV1BindingQSService;
import com.consystec.sc.ca.ws.input.fichacliente.InputFichaCliente;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.fichacliente.IndividualContactItem;
import com.consystec.sc.ca.ws.output.fichacliente.OutputDatosClienteFS;
import com.consystec.sc.ca.ws.output.fichacliente.OutputFichaCliente;
import com.consystec.sc.ca.ws.util.ConfiguracionExcepcion;
import com.consystec.sc.sv.ws.orm.ConsumoWebservice;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.CurrentClassGetter;
import com.consystec.sc.sv.ws.util.FSUtil;
import com.consystec.sc.sv.ws.util.UtileriasJava;
import com.google.gson.GsonBuilder;

/**
 * @author Victor Cifuentes @ Consystec - 2015
 *
 */
public class OperacionFichaCliente {
	private OperacionFichaCliente() {
	}

	private static final Logger log = Logger.getLogger(OperacionFichaCliente.class);

	// * El Salvador
	public static OutputFichaCliente doGet(String codArea) throws SQLException {
		String nombreMetodo = "doGet";
		String nombreClase = new CurrentClassGetter().getClassName();

		Respuesta respuesta = null;

		OutputFichaCliente output = null;

		// TODO cambiar por datos correctos de clientes
		respuesta = new Respuesta();
		respuesta.setDescripcion(new ControladorBase()
				.getMensaje(Conf_Mensajes.MSJ_RECURSOS_OBTENIDOS, null, nombreClase, nombreMetodo, null, codArea)
				.getDescripcion());

		output = new OutputFichaCliente();
		output.setRespuesta(respuesta);

		return output;
	}// */

	public static OutputFichaCliente creaCuentaFS(Connection conn, InputFichaCliente input, String idCustomer,
			String contactId, Map<String, String> parame)
			throws MalformedURLException, SQLException, ConfiguracionExcepcion {
		OutputFichaCliente output = new OutputFichaCliente();
		URL endpoint = new URL(
				UtileriasJava.getConfig(conn, Conf.GRUPO_URL_FS_WS, Conf.FS_WS_CREA_CUENTA, input.getCodArea()));

		String CTA_DescriptionCustomerAccount_V = ControladorBase.validarParametroVacio(
				parame.get(Conf.V_CTA_DescriptionCustomerAccount.toUpperCase()), Conf.V_CTA_DescriptionCustomerAccount);
		String CTA_IdMoney_V = ControladorBase.validarParametroVacio(parame.get(Conf.V_CTA_IdMoney.toUpperCase()),
				Conf.V_CTA_IdMoney);
		String CTA_IDPaymentMethod_V = ControladorBase.validarParametroVacio(
				parame.get(Conf.V_CTA_IDPaymentMethod.toUpperCase()), Conf.V_CTA_IDPaymentMethod);
		String CTA_IDLanguage_V = ControladorBase.validarParametroVacio(parame.get(Conf.V_CTA_IDLanguage.toUpperCase()),
				Conf.V_CTA_IDLanguage);
		String CTA_CreditClassId_V = ControladorBase
				.validarParametroVacio(parame.get(Conf.V_CTA_CreditClassId.toUpperCase()), Conf.V_CTA_CreditClassId);
		String CTA_ContactPointSupplyId_V = ControladorBase.validarParametroVacio(
				parame.get(Conf.V_CTA_ContactPointSupplyId.toUpperCase()), Conf.V_CTA_ContactPointSupplyId);
		String CTA_StyleIdCustomerBillFormat_V = ControladorBase.validarParametroVacio(
				parame.get(Conf.V_CTA_StyleIdCustomerBillFormat.toUpperCase()), Conf.V_CTA_StyleIdCustomerBillFormat);
		String CTA_InvoiceIdOrganization_V = ControladorBase.validarParametroVacio(
				parame.get(Conf.V_CTA_InvoiceIdOrganization.toUpperCase()), Conf.V_CTA_InvoiceIdOrganization);
		String CTA_AccountTypeIDCustomerAccount_V = ControladorBase.validarParametroVacio(
				parame.get(Conf.V_CTA_AccountTypeIDCustomerAccount.toUpperCase()),
				Conf.V_CTA_AccountTypeIDCustomerAccount);
		String CTA_AttributeIDAttributeValuePair_V = ControladorBase.validarParametroVacio(
				parame.get(Conf.V_CTA_AttributeIDAttributeValuePair.toUpperCase()),
				Conf.V_CTA_AttributeIDAttributeValuePair);
		String CTA_AttributeValueAttributeValuePair_V = ControladorBase.validarParametroVacio(
				parame.get(Conf.V_CTA_AttributeValueAttributeValuePair.toUpperCase()),
				Conf.V_CTA_AttributeValueAttributeValuePair);
		String CTA_AttributeIndexAttributeValuePair_V = ControladorBase.validarParametroVacio(
				parame.get(Conf.V_CTA_AttributeIndexAttributeValuePair.toUpperCase()),
				Conf.V_CTA_AttributeIndexAttributeValuePair);
		String CTA_AttributeNameAttributeValuePair_V = ControladorBase.validarParametroVacio(
				parame.get(Conf.V_CTA_AttributeNameAttributeValuePair.toUpperCase()),
				Conf.V_CTA_AttributeNameAttributeValuePair);
		String CTA_AttributeTypeAttributeValuePair_V = ControladorBase.validarParametroVacio(
				parame.get(Conf.V_CTA_AttributeTypeAttributeValuePair.toUpperCase()),
				Conf.V_CTA_AttributeTypeAttributeValuePair);
		String CTA_AttributeValueAttributeValuePair_N_V = ControladorBase.validarParametroVacio(
				parame.get(Conf.V_CTA_AttributeValueAttributeValuePair_N.toUpperCase()),
				Conf.V_CTA_AttributeValueAttributeValuePair_N);
		String CTA_AttributeIDAttributeValuePair_N_V = ControladorBase.validarParametroVacio(
				parame.get(Conf.V_CTA_AttributeIDAttributeValuePair_N.toUpperCase()),
				Conf.V_CTA_AttributeIDAttributeValuePair_N);

		CreateCustomerAccountBindingQSService ws = new CreateCustomerAccountBindingQSService(endpoint);
		CreateCustomerAccountV1 w = ws.getCreateCustomerAccountBindingQSPort();

		CreateCustomerAccountRequestType requestBody = new CreateCustomerAccountRequestType();
		Holder<HeaderOutType> hOutType = new Holder<HeaderOutType>();

		requestBody.setNameCustomerAccount(input.getPrimerNombre() + " " + input.getSegundoNombre());
		requestBody.setDescriptionCustomerAccount(CTA_DescriptionCustomerAccount_V);
		requestBody.setIdMoney(CTA_IdMoney_V);
		requestBody.setIDCustomer(idCustomer);
		requestBody.setIDPaymentMethod(CTA_IDPaymentMethod_V); // ?
		requestBody.setIDLanguage(CTA_IDLanguage_V); // ?
		requestBody.setCreditClassId(CTA_CreditClassId_V); // ?
		// amountMoney
		requestBody.setContactPointSupplyId(CTA_ContactPointSupplyId_V);// ?
		requestBody.setStyleIdCustomerBillFormat(CTA_StyleIdCustomerBillFormat_V);
		requestBody.setBillingAddressIDCustomerAccount(input.getMunicipio());
		requestBody.setBillingContactID(contactId);
		requestBody.setInvoiceIdOrganization(CTA_InvoiceIdOrganization_V); // ??
		requestBody.setAccountTypeIDCustomerAccount(CTA_AccountTypeIDCustomerAccount_V); // ??

		AttributeValuePairItemType aValuePair = new AttributeValuePairItemType();

		aValuePair.setAttributeIDAttributeValuePair(CTA_AttributeIDAttributeValuePair_V); // ?????????????????
		// opcional
		aValuePair.setAttributeValueAttributeValuePair(CTA_AttributeValueAttributeValuePair_V);// ?
		aValuePair.setAttributeIndexAttributeValuePair(CTA_AttributeIndexAttributeValuePair_V);

		AttributeValuePairListItemType listAttributeValuePair = new AttributeValuePairListItemType();
		listAttributeValuePair.getAttributeValuePairItem().add(aValuePair);
		requestBody.setAttributeValuePairListItem(listAttributeValuePair);

		AttributeValuePairItemType mapAttribute = new AttributeValuePairItemType();
		mapAttribute.setAttributeIDAttributeValuePair(CTA_AttributeIDAttributeValuePair_N_V); // ????????????????
		mapAttribute.setAttributeNameAttributeValuePair(CTA_AttributeNameAttributeValuePair_V);
		mapAttribute.setAttributeTypeAttributeValuePair(CTA_AttributeTypeAttributeValuePair_V);
		mapAttribute.setAttributeValueAttributeValuePair(CTA_AttributeValueAttributeValuePair_N_V);

		AttributeValuePairListItemType listExtendedMap = new AttributeValuePairListItemType();
		listExtendedMap.getAttributeValuePairItem().add(mapAttribute);
		requestBody.setExtendedMapAttributeValueListItem(listExtendedMap);
		System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(requestBody));
		// header
		HeaderInType h = FSUtil.instanciaHeader(input.getUsuario(), input.getCodArea());
		CreateCustomerAccountResponseType response = new CreateCustomerAccountResponseType();
		try {
			log.trace(new GsonBuilder().setPrettyPrinting().create().toJson(requestBody));
			response = w.createCustomerAccount(requestBody, h, hOutType);
		} catch (com.telefonica.globalintegration.services.soap.createcustomeraccount.v1.MessageFault e) {

			e.printStackTrace();
		}

		if (response.getNameCustomerAccount() != null && response.getIDCustomerAccount() != null) {
			output.setIDCustomer(response.getIDCustomerAccount());
			log.trace("Se creo cuenta con id " + response.getIDCustomerAccount());
		}
		return output;
	}

	public static OutputFichaCliente creaUbicacionFS(Connection conn, InputFichaCliente input, String direccion)
			throws MalformedURLException, SQLException {
		OutputFichaCliente output = new OutputFichaCliente();
		URL endpoint = new URL(
				UtileriasJava.getConfig(conn, Conf.GRUPO_URL_FS_WS, Conf.FS_CREA_UBICACION, input.getCodArea()));

		CreateLocationBindingQSService ws = new CreateLocationBindingQSService(endpoint);
		CreateLocationV1 w = ws.getCreateLocationBindingQSPort();
		CreateLocationRequestType requestBody = new CreateLocationRequestType();
		CreateLocationResponseType response = new CreateLocationResponseType();

		try {
			requestBody.setIDCustomer("true");
			requestBody.setNameGeographicLocationName(input.getDireccion());
			requestBody.setIDGeographicLocation(direccion);
			// opcional
			requestBody.setTypeGeographicArea(input.getDepartamento());

			HeaderInType h = FSUtil.instanciaHeader(input.getUsuario(), input.getCodArea());
			Holder<HeaderOutType> hOutType = new Holder<HeaderOutType>();

			response = w.createLocation(requestBody, h, hOutType);
		} catch (com.telefonica.globalintegration.services.soap.createlocation.v1.MessageFault e) {
			e.printStackTrace();
		}

		if (response.getIDGeographicLocation() != null) {
			InputFichaCliente fCliente = new InputFichaCliente();
			fCliente.setUbicacion(response.getIDGeographicLocation());
			output.setCliente(fCliente);
		}

		return output;
	}

	public static OutputFichaCliente creaDireccionFS(Connection conn, InputFichaCliente input,
			Map<String, String> parame) throws MalformedURLException, SQLException, ConfiguracionExcepcion {
		OutputFichaCliente output = new OutputFichaCliente();
		String DIR_TypeGeographicArea_V = ControladorBase.validarParametroVacio(
				parame.get(Conf.V_DIR_TypeGeographicArea.toUpperCase()), Conf.V_DIR_TypeGeographicArea);
		URL endpoint = new URL(
				UtileriasJava.getConfig(conn, Conf.GRUPO_URL_FS_WS, Conf.FS_CREA_DIRECCION, input.getCodArea()));
		CreateAddressV1BindingQSService ws = new CreateAddressV1BindingQSService(endpoint);
		CreateAddressV1 w = ws.getCreateAddressV1BindingQSPort();
		CreateAddressRequestType requestBody = new CreateAddressRequestType();

		CustomGeographicAreaItemType area = new CustomGeographicAreaItemType();
		CustomGeographicAreaItemType subArea = new CustomGeographicAreaItemType();

		Holder<HeaderOutType> hres = new Holder<HeaderOutType>();
		Holder<CreateAddressResponseType> cResponse = new Holder<CreateAddressResponseType>();

		try {
			area.setIDGeographicArea(input.getMunicipio());

			subArea.setNameGeographicArea(input.getDireccion());
			subArea.setIDGeographicArea(input.getMunicipio());
			subArea.setTypeGeographicArea(DIR_TypeGeographicArea_V);

			area.setChildGeographicAreaItem(subArea);

			requestBody.setCreateIfNotExists(true);
			requestBody.setGeographicAreaItem(area);

			log.trace(new GsonBuilder().setPrettyPrinting().create().toJson(requestBody));

			HeaderInType h = FSUtil.instanciaHeader(input.getUsuario(), input.getCodArea());

			w.createAddress(requestBody, h, cResponse, hres);
		} catch (com.telefonica.wsdl.globalintegration.soap.basename.v1.createaddress_v1.MessageFault e) {
			log.error("Excepcion: " + e.getMessage(), e);
		}

		CreateAddressResponseType resValue = cResponse.value;

		if (resValue.getIDGeographicArea() != null && !resValue.getIDGeographicArea().trim().equals("")) {

			output.setIDGeographicArea(resValue.getIDGeographicArea());

			log.trace("Se creo direccion con id" + resValue.getIDGeographicArea());
		}
		return output;
	}

	public static OutputDatosClienteFS getDatosClienteFS(Connection conn, InputFichaCliente input, String idCustomer)
			throws MalformedURLException, SQLException {
		OutputDatosClienteFS output = new OutputDatosClienteFS();

		URL endpoint = new URL(
				UtileriasJava.getConfig(conn, Conf.GRUPO_URL_FS_WS, Conf.FS_GET_DATOS_CLIENTE, input.getCodArea()));
		GetResidentialCustomerBindingQSService ws = new GetResidentialCustomerBindingQSService(endpoint);
		GetResidentialCustomerV1 w = ws.getGetResidentialCustomerBindingQSPort();
		GetResidentialCustomerRequestType requestBody = new GetResidentialCustomerRequestType();
		GetResidentialCustomerResponseType response = new GetResidentialCustomerResponseType();

		try {
			requestBody.setIDCustomer(idCustomer);

			log.trace(new GsonBuilder().setPrettyPrinting().create().toJson(requestBody));

			Holder<HeaderOutType> hres = new Holder<HeaderOutType>();
			HeaderInType h = FSUtil.instanciaHeader(input.getUsuario(), input.getCodArea());
			response = w.getResidentialCustomer(requestBody, h, hres);
		} catch (com.telefonica.globalintegration.services.soap.getresidentialcustomer.v1.MessageFault e) {

			e.printStackTrace();
		}

		if (response.getIDCustomer() != null) {
			List<IndividualContactItem> list = new ArrayList<IndividualContactItem>();
			IndividualContactItem item = new IndividualContactItem();
			item.setIDCustomerAccountContact(response.getIndividualContactListItem().getIndividualContactItem().get(0)
					.getIDCustomerAccountContact());
			list.add(item);

			output.setIdGeographicLocation(response.getCustomerAccountLocationListItem()
					.getCustomerAccountLocationItem().get(0).getIDGeographicLocation());
			output.setIndividualContactItem(list);
			output.setIdCustomer(response.getIDCustomer());
			output.setAccountNumberCustomer(response.getAccountNumberCustomer());
		}
		return output;
	}

	/**
	 * * M\u00E9todo para crear cliente desde FS
	 * 
	 * @throws SQLException
	 * @throws MalformedURLException
	 * @throws ConfiguracionExcepcion
	 */
	public static OutputFichaCliente creaClienteFS(Connection conn, InputFichaCliente input, Map<String, String> parame)
			throws MalformedURLException, SQLException, ConfiguracionExcepcion {
		OutputFichaCliente output = new OutputFichaCliente();
		URL endpoint = new URL(
				UtileriasJava.getConfig(conn, Conf.GRUPO_URL_FS_WS, Conf.FS_WS_CREA_CLIENTE, input.getCodArea()));

		String CLIE_IDSegmentCustomer_V = ControladorBase.validarParametroVacio(
				parame.get(Conf.V_CLIE_IDSegmentCustomer.toUpperCase()), Conf.V_CLIE_IDSegmentCustomer);
		String CLIE_IDCustomerCategory_V = ControladorBase.validarParametroVacio(
				parame.get(Conf.V_CLIE_IDCustomerCategory.toUpperCase()), Conf.V_CLIE_IDCustomerCategory);
		String CLIE_CustomerStatusCustomer_V = ControladorBase.validarParametroVacio(
				parame.get(Conf.V_CLIE_CustomerStatusCustomer.toUpperCase()), Conf.V_CLIE_CustomerStatusCustomer);
		String CLIE_LegalAddressIdCustomerAccount_V = ControladorBase.validarParametroVacio(
				parame.get(Conf.V_CLIE_LegalAddressIdCustomerAccount.toUpperCase()),
				Conf.V_CLIE_LegalAddressIdCustomerAccount);
		String CLIE_GenderIndividual_V = ControladorBase.validarParametroVacio(
				parame.get(Conf.V_CLIE_GenderIndividual.toUpperCase()), Conf.V_CLIE_GenderIndividual);
		String CLIE_ExistsDuringOrganization_V = ControladorBase.validarParametroVacio(
				parame.get(Conf.V_CLIE_ExistsDuringOrganization.toUpperCase()), Conf.V_CLIE_ExistsDuringOrganization);
		String CLIE_NameLanguage_V = ControladorBase
				.validarParametroVacio(parame.get(Conf.V_CLIE_NameLanguage.toUpperCase()), Conf.V_CLIE_NameLanguage);
		String CLIE_StatusCustomerAccountContact_V = ControladorBase.validarParametroVacio(
				parame.get(Conf.V_CLIE_StatusCustomerAccountContact.toUpperCase()),
				Conf.V_CLIE_StatusCustomerAccountContact);
		String CLIE_OccupationIDIndividual_V = ControladorBase.validarParametroVacio(
				parame.get(Conf.V_CLIE_OccupationIDIndividual.toUpperCase()), Conf.V_CLIE_OccupationIDIndividual);
		String CLIE_CLIE_SalaryIndividual_V = ControladorBase.validarParametroVacio(
				parame.get(Conf.V_CLIE_CLIE_SalaryIndividual.toUpperCase()), Conf.V_CLIE_CLIE_SalaryIndividual);
		String CLIE_IDContactMedium_V = ControladorBase.validarParametroVacio(
				parame.get(Conf.V_CLIE_IDContactMedium.toUpperCase()), Conf.V_CLIE_IDContactMedium);
		String CLIE_TypeContactMedium_V = ControladorBase.validarParametroVacio(
				parame.get(Conf.V_CLIE_TypeContactMedium.toUpperCase()), Conf.V_CLIE_TypeContactMedium);
		String CLIE_AttributeNameAttributeValuePair_V = ControladorBase.validarParametroVacio(
				parame.get(Conf.V_CLIE_AttributeNameAttributeValuePair.toUpperCase()),
				Conf.V_CLIE_AttributeNameAttributeValuePair);
		String CLIE_AttributeTypeAttributeValuePair_V = ControladorBase.validarParametroVacio(
				parame.get(Conf.V_CLIE_AttributeTypeAttributeValuePair.toUpperCase()),
				Conf.V_CLIE_AttributeTypeAttributeValuePair);
		String CLIE_NameTypeGeographicLocationName_V = ControladorBase.validarParametroVacio(
				parame.get(Conf.V_CLIE_NameTypeGeographicLocationName.toUpperCase()),
				Conf.V_CLIE_NameTypeGeographicLocationName);
		String CLIE_NameGeographicLocationName_V = ControladorBase.validarParametroVacio(
				parame.get(Conf.V_CLIE_NameGeographicLocationName.toUpperCase()),
				Conf.V_CLIE_NameGeographicLocationName);
		String CLIE_IDGeographicAddress_V = ControladorBase.validarParametroVacio(
				parame.get(Conf.V_CLIE_IDGeographicAddress.toUpperCase()), Conf.V_CLIE_IDGeographicAddress);

		CreateResidentialCustomerBindingQSService ws = new CreateResidentialCustomerBindingQSService(endpoint);
		CreateResidentialCustomerV1 w = ws.getCreateResidentialCustomerBindingQSPort();

		HeaderInType header;
		header = FSUtil.instanciaHeader(input.getUsuario(), input.getCodArea());
		Holder<HeaderOutType> headerResp = new Holder<HeaderOutType>();
		// armando input para crear Cliente
		CreateResidentialCustomerRequestType inputCliente = new CreateResidentialCustomerRequestType();
		IndividualContactListItemType lista = new IndividualContactListItemType();
		IndividualContactItemType contacto = new IndividualContactItemType();
		IndividualIdentificationItemType identContacto = new IndividualIdentificationItemType();
		LanguageItemType lenguaje = new LanguageItemType();
		OccupationIndividualItemType ocupacion = new OccupationIndividualItemType();
		ContactMediumItemType contactMedium = new ContactMediumItemType();
		com.telefonica.globalintegration.services.createresidentialcustomer.v1.AttributeValuePairItemType valuePair = new com.telefonica.globalintegration.services.createresidentialcustomer.v1.AttributeValuePairItemType();
		com.telefonica.globalintegration.services.createresidentialcustomer.v1.AttributeValuePairListItemType listValuePair = new com.telefonica.globalintegration.services.createresidentialcustomer.v1.AttributeValuePairListItemType();
		ContactMediumListItemType listContactMedium = new ContactMediumListItemType();
		CustomerAccountLocationItemType location = new CustomerAccountLocationItemType();
		CustomerAccountLocationListItemType listLocation = new CustomerAccountLocationListItemType();

		inputCliente.setTaxId(input.getNit());
		inputCliente.setIDSegmentCustomer(CLIE_IDSegmentCustomer_V);
		inputCliente.setIDCustomerCategory(CLIE_IDCustomerCategory_V);
		inputCliente.setCustomerStatusCustomer(CLIE_CustomerStatusCustomer_V);
		inputCliente.setLegalAddressIdCustomerAccount(CLIE_LegalAddressIdCustomerAccount_V);

		contacto.setIDCustomerAccountContact("");
		contacto.setDepartmentNameContact("");
		contacto.setGenderIndividual(CLIE_GenderIndividual_V);
		contacto.setGivenNamesIndividualName(input.getPrimerNombre());
		contacto.setMiddleNamesIndividualName(input.getSegundoNombre());
		contacto.setFirstLastNameIndividualName(input.getPrimerApellido());
		contacto.setSecondLastNameIndividualName(input.getSegundoApellido());
		contacto.setAliveDuringIndividual(header.getTimestamp());
		contacto.setPreferedIDContactMedium("");
		contacto.setExistsDuringOrganization(CLIE_ExistsDuringOrganization_V);
		contacto.setTradingNameOrganizationName("");
		contacto.setFormOfAddressIndividualName("");

		lenguaje.setIDLanguage("");
		lenguaje.setNameLanguage(CLIE_NameLanguage_V);
		contacto.setLanguageItem(lenguaje);
		contacto.setStatusCustomerAccountContact(CLIE_StatusCustomerAccountContact_V);

		ocupacion.setOccupationIndividual("");
		ocupacion.setOccupationIDIndividual(CLIE_OccupationIDIndividual_V); // 9143662176713719493
		contacto.setOccupationIndividualItem(ocupacion);

		contacto.setAccountStatusCustomerAccount("");
		contacto.setSalaryIndividual(CLIE_CLIE_SalaryIndividual_V);

		identContacto.setIDIndividualIdentification("");
		identContacto.setIdentificationNumberIndividualIdentification(input.getNoDocumento());
		identContacto.setIdentificationTypeIndividualIdentification("9141074110413397725"); // DUI
		contacto.setIndividualIdentificationItem(identContacto);

		contactMedium.setNameContactMedium("");
		contactMedium.setIDContactMedium(CLIE_IDContactMedium_V);
		contactMedium.setTypeContactMedium(CLIE_TypeContactMedium_V);
		contactMedium.setValueContactMedium(input.getTelContacto()); // numero de telefono

		valuePair.setAttributeIDAttributeValuePair(""); // xxxxxxxxxx
		valuePair.setAttributeNameAttributeValuePair(CLIE_AttributeNameAttributeValuePair_V);
		valuePair.setAttributeTypeAttributeValuePair(CLIE_AttributeTypeAttributeValuePair_V);
		valuePair.setAttributeValueAttributeValuePair("");
		valuePair.setAttributeIndexAttributeValuePair("");

		listValuePair.getAttributeValuePairItem().add(valuePair);
		contactMedium.setExtendedMapAttributeValueListItem(listValuePair);
		listContactMedium.getContactMediumItem().add(contactMedium);
		contacto.setContactMediumListItem(listContactMedium);

		contacto.setEmailID("");

		location.setNameTypeGeographicLocationName(CLIE_NameTypeGeographicLocationName_V);
		location.setNameGeographicLocationName(CLIE_NameGeographicLocationName_V);
		location.setIDGeographicLocation(""); // 9143662888613266918
		location.setEMailAddressEmailContact("");

		GeographicAddressItemType geoAddresType = new GeographicAddressItemType();
		geoAddresType.setIDGeographicAddress(CLIE_IDGeographicAddress_V);
		geoAddresType.setFullAddressLocalAddress("");
		geoAddresType.setNameGeographicAddress("");
		geoAddresType.setSubUnitType("");
		geoAddresType.setAddressType("");
		CountryItemType countryType = new CountryItemType();
		countryType.setNameCountry("");
		countryType.setIso2CodeCountry("");
		countryType.setIso3CodeCountry("");
		countryType.setTypeCountry("");
		countryType.setAccuracyCountry("");
		countryType.setIDCountry("");

		TimePeriodItemType timePeriod = new TimePeriodItemType();
		timePeriod.setStartDateTimeTimePeriod("");
		timePeriod.setEndDateTimeTimePeriod("");
		countryType.setValidForCountry(timePeriod);

		geoAddresType.setCountryItem(countryType);
		location.setGeographicAddressItem(geoAddresType);

		listLocation.getCustomerAccountLocationItem().add(location);
		inputCliente.setCustomerAccountLocationListItem(listLocation);

		lista.getIndividualContactItem().add(contacto);
		inputCliente.setIndividualContactListItem(lista);

		log.trace(new GsonBuilder().setPrettyPrinting().create().toJson(inputCliente));

		CreateResidentialCustomerResponseType respuesta = new CreateResidentialCustomerResponseType();
		try {
			respuesta = w.createResidentialCustomer(inputCliente, header, headerResp);
		} catch (MessageFault e) {
			log.trace("CREA CLIENTE FS: " + e.getFaultInfo().getAppDetail().getExceptionAppCause());
		}

		if (respuesta.getIDCustomer() != null && !respuesta.getIDCustomer().trim().equals("")) {
			output.setIDCustomer(respuesta.getIDCustomer());

			log.trace("Se creo cliente con id" + respuesta.getIDCustomer());
		}
		return output;
	}

	public static OutputFichaCliente doPost(Connection conn, InputFichaCliente input)
			throws SQLException, ConfiguracionExcepcion {
		OutputFichaCliente out = new OutputFichaCliente();
		Respuesta res = new Respuesta();
		String nombreMetodo = "doPost";
		String nombreClase = new CurrentClassGetter().getClassName();
		String idCustomerC = "";
		String billingCustomer = "";
		String idGeoLocation = "";
		String idCustomerAccount = "";

		log.trace("IdPDV " + input.getIdPdv());
		log.trace("IdDTS" + input.getIdDts());

		List<String> gruposParamFichaCliente = new ArrayList<>();
		gruposParamFichaCliente.add(Conf.G_FS_CREA_FICHA_CLIENTE);

		HashMap<String, String> parametrosFichaCliente = ControladorBase.getGrupoParam(conn, gruposParamFichaCliente,
				input.getCodArea());

		try {
			OutputFichaCliente direccion = creaDireccionFS(conn, input, parametrosFichaCliente);
			OutputFichaCliente idCustomer = creaClienteFS(conn, input, parametrosFichaCliente);
			OutputDatosClienteFS datosCliente = getDatosClienteFS(conn, input, idCustomer.getIDCustomer());
			OutputFichaCliente cuenta = creaCuentaFS(conn, input, datosCliente.getIdCustomer(),
					datosCliente.getIndividualContactItem().get(0).getIDCustomerAccountContact(),
					parametrosFichaCliente);

			InputFichaCliente cliente = new InputFichaCliente();

			if (direccion.getIDGeographicArea() != null && idCustomer.getIDCustomer() != null
					&& cuenta.getIDCustomer() != null) {
				cliente.setIdDts(input.getIdDts());
				cliente.setIdPdv(input.getIdPdv());

				idCustomerC = idCustomer.getIDCustomer();
				idGeoLocation = datosCliente.getIdGeographicLocation();
				idCustomerAccount = cuenta.getIDCustomer();

				log.trace("idCustomer " + idCustomerC);
				log.trace("idGeographicLocation " + idGeoLocation);
				log.trace("billing account " + billingCustomer);
				log.trace("id costumer account " + idCustomerAccount);

				res = new ControladorBase().getMensaje(Conf_Mensajes.OK_CREA_FICHA_CLIENTE_68, null, nombreClase,
						nombreMetodo, "El cliente fue creado con éxito en SCL", input.getCodArea());
				out.setCliente(cliente);
				out.setRespuesta(res);

			} else {
				res = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_CREAR_FICHA_CLIENTE_604, null,
						nombreClase, nombreMetodo, "Inconvenientes al crear cliente en SCL", input.getCodArea());
				out.setRespuesta(res);
			}
		} catch (MalformedURLException e) {
			log.error(e, e);
		} catch (SQLException e) {
			log.error(e, e);
		}
		actualizarPDV(conn, input.getIdPdv(), idCustomerC, idGeoLocation, idCustomerAccount);
		return out;
	}

	private static void actualizarPDV(Connection conn, String idPdv, String idCustomer, String idLocation,
			String idCustomerAccount) throws SQLException {
		PreparedStatement pstmt = null;
		String sql = "";

		sql = "UPDATE TC_SC_PUNTOVENTA " + "   SET ID_CUSTOMER_ACCOUNT = ?, ID_COSTUMER =?, ID_GEOGRAPHIC_LOCATION = ?"
				+ " WHERE TCSCPUNTOVENTAID = ?";

		log.trace("UPDATE PUNTO DE VENTA " + sql);
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, idCustomerAccount);
			pstmt.setString(2, idCustomer);
			pstmt.setString(3, idLocation);
			pstmt.setBigDecimal(4, new BigDecimal(idPdv));
			pstmt.executeUpdate();
		} finally {
			DbUtils.closeQuietly(pstmt);
		}
	}

	/**
	 * Funcion para consultar en las tablas de SIDRA, que un PDV pertenezca a un DTS
	 * y retornar el numero de cuenta perteneciente al DTS
	 * 
	 * @param conn
	 * @param idDts
	 * @param idPdv
	 * 
	 * @return String
	 * @throws SQLException
	 */
	@SuppressWarnings("unused")
	private static String getCustomerDts(Connection conn, String idDts, String idPdv) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String cuentaDts = "";

		String sql = "SELECT B.TCSCDTSID, B.ID_CUSTOMER_ACCOUNT " + "  FROM TC_SC_PUNTOVENTA A, TC_SC_DTS B "
				+ " WHERE     A.TCSCDTSID = B.TCSCDTSID " + "       AND A.TCSCDTSID = ? "
				+ "       AND A.TCSCPUNTOVENTAID =? ";

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setBigDecimal(1, new BigDecimal(idDts));
			pstmt.setBigDecimal(2, new BigDecimal(idPdv));
			rs = pstmt.executeQuery();

			if (rs != null && rs.next()) {
				cuentaDts = rs.getString("COD_CUENTA");
				if (cuentaDts == null || "".equals(cuentaDts.trim())) {
					cuentaDts = "CLIENTE_NO"; // indica que el DTS no tiene cuenta creada en scl
				}
			} else {
				cuentaDts = "DTS_NO"; // indica que no existe DTS
			}
		} finally {
			DbUtils.closeQuietly(pstmt);
			DbUtils.closeQuietly(rs);
		}
		return cuentaDts;
	}

	/**
	 * Funcion para consultar en las tablas de SIDRA, los datos de un DTS y retornar
	 * el numero de cuenta perteneciente al DTS
	 * 
	 * @param conn
	 * @param idDts
	 * @param idPdv
	 * 
	 * @return String
	 * @throws SQLException
	 */
	@SuppressWarnings("unused")
	private static String getCuentaDts(Connection conn, String idDts) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rst = null;
		String cuenta = "";

		String sql = "SELECT TCSCDTSID, COD_CUENTA " + "  FROM TC_SC_DTS " + " WHERE TCSCDTSID =?";

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setBigDecimal(1, new BigDecimal(idDts));
			rst = pstmt.executeQuery();

			if (rst != null && rst.next()) {
				cuenta = rst.getString("COD_CUENTA");
				if (cuenta == null || "".equals(cuenta.trim())) {
					cuenta = "CUENTA_NO"; // indica que el DTS no tiene cuenta creada en scl
				}
			} else {
				cuenta = "DTS_NO";
			}
		} finally {

			DbUtils.closeQuietly(pstmt);
			DbUtils.closeQuietly(rst);
		}

		return cuenta;
	}

	/**
	 * Este m\u00E9todo ejecuta una query para actualizar la tabla TC_SC_PUNTOVENTA,
	 * en el cual se actualizar\u00E9n dos campos COD_CLIENTE con el resutlado del
	 * codigo de SCL y RESULTADO_SCL que indicar\u00E9 si la ejecuci\u00F3n de la
	 * estructura SCL fue exitosa o no.
	 * 
	 * @throws SQLException
	 * @throws NumberFormatException
	 */
	@SuppressWarnings("unused")
	private static void actualizarDTS(Connection conn, String idDts, String codCuenta, String codCliente)
			throws SQLException {
		PreparedStatement pstmt = null;
		String sql = getqueryactualizaDTS(idDts, codCuenta, codCliente);
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
		} finally {
			DbUtils.close(pstmt);
		}
	}

	public static String getqueryactualizaDTS(String idDts, String codCuenta, String codCliente) {
		String sql = "";
		if (codCuenta != null && !"".equals(codCuenta.trim()) && codCliente != null && !"".equals(codCliente.trim())) {
			sql = "UPDATE TC_SC_DTS " + "   SET COD_CUENTA = " + codCuenta + ", COD_CLIENTE = " + codCliente
					+ ", RESULTADO_SCL = 0 " + " WHERE TCSCDTSID = " + idDts + " ";

		} else {
			sql = "UPDATE TC_SC_DTS " + "   SET RESULTADO_SCL = 1";

			if (codCuenta != null && !"".equals(codCuenta.trim())) {
				sql += ", COD_CUENTA = " + codCuenta + " ";
			}

			if (codCliente != null && !"".equals(codCliente.trim())) {
				sql += ", COD_CLIENTE = " + codCliente + " ";
			}
			sql += " WHERE TCSCDTSID = " + idDts + " ";
		}

		return sql;
	}

	/**
	 * Este m\u00E9todo recibe dos ID's que son de Punto de Venta o de Distribuidor,
	 * dependiendo cual de ellos dos reciba consultar\u00E9 en las tablas de SIDRA
	 * si ese id ya tiene creada una ficha de cliente.
	 * 
	 * @param conn
	 * @param idDts
	 * @param idPdv
	 * 
	 * @return boolean
	 * @throws SQLException
	 */
	@SuppressWarnings("unused")
	private static boolean validarExistenciaFicha(Connection conn, String idDts, String idPdv, BigDecimal idPais)
			throws SQLException {
		boolean respuesta = false;
		String sql = "";

		if (idPdv != null) {
			sql = "SELECT TCSCPUNTOVENTAID " + "  FROM TC_SC_PUNTOVENTA " + " WHERE     TCSCPUNTOVENTAID = ?"
					+ "       AND TCSCCATPAISID =?" + "       AND RESULTADO_SCL = 0 "
					+ "       AND ID_CUSTOMER_ACCOUNT IS NOT NULL ";
			try (PreparedStatement pstmt = conn.prepareStatement(sql);) {
				pstmt.setBigDecimal(1, new BigDecimal(idPdv));
				pstmt.setBigDecimal(2, idPais);
				try (ResultSet rst = pstmt.executeQuery();) {
					if (rst != null && rst.next()) {
						rst.getString(1);
						respuesta = true;
					}

				}
			}

		} else if (idDts != null) {
			sql = "SELECT TCSCDTSID " + "  FROM TC_SC_DTS " + " WHERE     TCSCDTSID = ?"
					+ "       AND TCSCCATPAISID =? " + "       AND RESULTADO_SCL = 0 "
					+ "       AND ID_CUSTOMER_ACCOUNT IS NOT NULL " + "       AND ID_CUSTOMER IS NOT NULL ";

			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setBigDecimal(1, new BigDecimal(idDts));
				pstmt.setBigDecimal(2, idPais);

				try (ResultSet rst = pstmt.executeQuery()) {
					if (rst != null && rst.next()) {
						rst.getString(1);
						respuesta = true;
					}
				}

			}

		}
		return respuesta;
	}

	/**
	 * Este metodo permite consultar en SCL los datos de un cliente a traves del
	 * campo codCliente, cuando se encuentran los datos se llena un objeto de tipo
	 * InputFichaCliente de lo contrario si no existe coincidencia el objeto
	 * ser\u00E9 null.
	 * 
	 * @param conn
	 * @param codCliente
	 * @param paramSppn
	 * 
	 * @return boolean
	 * @throws SQLException
	 */
	public static InputFichaCliente getInfoCliente(String codCliente, HashMap<String, String> paramSpn)
			throws SQLException {
		InputFichaCliente cliente = null;
		PreparedStatement pstmt = null;

		Connection conn = null;

		String sql = "SELECT * FROM GE_CLIENTES WHERE COD_CLIENTE = ?";
		try {
			conn = new ControladorBase().getConnRegional();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, codCliente);

			try (ResultSet rst = pstmt.executeQuery()) {
				if (rst != null && rst.next()) {
					cliente = new InputFichaCliente();
					cliente.setTipoDocumento(rst.getString("COD_TIPIDENT"));
					cliente.setNoDocumento(rst.getString("NUM_IDENT"));
					cliente.setPrimerApellido(rst.getString("NOM_APECLIEN1"));
					cliente.setSegundoApellido(rst.getString("NOM_APECLIEN2"));
					cliente.setNumTelefono(rst.getString("TEF_CLIENTE1"));
					cliente.setCodCuenta(rst.getString("COD_CUENTA"));

					String nombres = rst.getString("NOM_CLIENTE");
					String[] nombre = nombres.split(" ");
					cliente.setPrimerNombre(nombre[0]);
					if (nombre.length > 1) {
						cliente.setSegundoNombre(nombre[1]);
					}

					String tipoDoc = rst.getString("COD_TIPIDENT");
					@SuppressWarnings("rawtypes")
					Iterator it = paramSpn.entrySet().iterator();
					while (it.hasNext()) {
						@SuppressWarnings("rawtypes")
						Map.Entry e = (Map.Entry) it.next();
						if (tipoDoc.equals(e.getValue().toString())) {
							cliente.setTipoDocumento(e.getKey().toString().toUpperCase());
							break;
						}
					}
				}
			}

		} finally {
			DbUtils.close(pstmt);
		}
		return cliente;
	}

	public static void logConsumoWS(Connection conn, ConsumoWebservice consumo) {
		PreparedStatement pstmt = null;

		try {
			if ((consumo.getPeticion() != null && !"".equals(consumo.getPeticion().trim()))
					|| (consumo.getRespuesta() != null && !"".equals(consumo.getRespuesta().trim()))) {

				String query = "INSERT INTO tc_sc_consumo_webservice(tcsccatpaisid, tcsclogsidraid, peticion, respuesta, creado_el, creado_por)"
						+ " VALUES (?, ?, ?, ?, SYSDATE, ?) ";

				pstmt = conn.prepareStatement(query);
				pstmt.setBigDecimal(1, consumo.getTcsccatpaisid());
				pstmt.setBigDecimal(2, consumo.getTcsclogsidraid());
				pstmt.setString(3, consumo.getPeticion());
				pstmt.setString(4, consumo.getRespuesta());
				pstmt.setString(5, consumo.getCreado_por());
				pstmt.executeUpdate();
			}
		} catch (Exception e) {
			// el registrar la peticion y respuesta no debe afectar el flujo de creacion
			log.error("Error al registrar log Consumo SPN SCL", e);
		} finally {
			DbUtils.closeQuietly(pstmt);
		}
	}

}
