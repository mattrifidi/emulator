package org.rifidi.dynamicswtforms.xml.processor.impl;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.dynamicswtforms.xml.annotaions.Form;
import org.rifidi.dynamicswtforms.xml.annotaions.FormElement;
import org.rifidi.dynamicswtforms.xml.constants.FormData;
import org.rifidi.dynamicswtforms.xml.constants.FormElementData;
import org.rifidi.dynamicswtforms.xml.exceptions.DynamicSWTFormAnnotationException;
import org.rifidi.dynamicswtforms.xml.processor.DynamicSWTFormXMLProcessor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * The implementation of the DynamicSWTFormXMLProcessor
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class DynamicSWTFormXMLProcessorImpl implements
		DynamicSWTFormXMLProcessor {

	private Log logger = LogFactory
			.getLog(DynamicSWTFormXMLProcessorImpl.class);

	@Override
	public Document processAnnotation(Class<?> clazz)
			throws DynamicSWTFormAnnotationException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		try {
			db = dbf.newDocumentBuilder();
			Document doc = db.newDocument();
			Element e = processClass(clazz, doc);
			doc.appendChild(e);
			return doc;
		} catch (ParserConfigurationException e) {
			throw new DynamicSWTFormAnnotationException(e);
		}
	}

	@Override
	public Document processAnnotation(String rootName, List<Class<?>> classes)
			throws DynamicSWTFormAnnotationException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		if (rootName == null) {
			throw new DynamicSWTFormAnnotationException(
					"rootName cannot be null");
		}
		try {
			db = dbf.newDocumentBuilder();
			Document doc = db.newDocument();
			Element root = doc.createElement(rootName);
			for (Class<?> clazz : classes) {
				root.appendChild(processClass(clazz, doc));
			}
			doc.appendChild(root);

			return doc;
		} catch (ParserConfigurationException ex) {
			throw new DynamicSWTFormAnnotationException(ex);
		}
	}

	private Element processClass(Class<?> clazz, Document doc) {
		Form anntoations = clazz.getAnnotation(Form.class);

		Element annotationNode = doc.createElement(FormData.DYNAMIC_SWT_FORM
				.toString());
		annotationNode.setAttribute(FormData.NAME.toString(), clazz
				.getSimpleName());
		if (anntoations != null) {
			for (FormElement w : anntoations.formElements()) {
				annotationNode.appendChild(processFormElement(w, doc));
			}
		}
		return annotationNode;
	}

	private Element processFormElement(FormElement formElement, Document doc) {

		Element element = doc.createElement(formElement.type().toString());

		Element name = doc.createElement(FormElementData.ELEMENT_NAME
				.toString());
		name.appendChild(doc.createTextNode(formElement.elementName()));
		element.appendChild(name);

		Element displayname = doc.createElement(FormElementData.DISPLAY_NAME
				.toString());
		displayname.appendChild(doc.createTextNode(formElement.displayName()));
		element.appendChild(displayname);

		Element defaultValue = doc.createElement(FormElementData.DEFAULT_VALUE
				.toString());
		defaultValue
				.appendChild(doc.createTextNode(formElement.defaultValue()));
		element.appendChild(defaultValue);

		Element editable = doc.createElement(FormElementData.EDITABLE
				.toString());
		editable.appendChild(doc.createTextNode(Boolean.toString(formElement
				.editable())));
		element.appendChild(editable);

		switch (formElement.type()) {
		case BOOLEAN:
			processBooleanFormElement(formElement, element);
			break;
		case STRING:
			processStringFormElement(formElement, element);
			break;
		case INTEGER:
			processIntegerFormElement(formElement, element);
			break;
		case FLOAT:
			processFloatFormElement(formElement, element);
			break;
		case CHOICE:
			processChoiceFormElement(formElement, element);
			break;
		}

		return element;
	}

	private void processBooleanFormElement(FormElement formElement,
			Element element) {

	}

	private void processStringFormElement(FormElement formElement,
			Element element) {
		Element regex = element.getOwnerDocument().createElement(
				FormElementData.REGEX.toString());
		regex.appendChild(element.getOwnerDocument().createTextNode(
				formElement.regex()));
		element.appendChild(regex);
	}

	private void processIntegerFormElement(FormElement formElement,
			Element element) {
		Element min = element.getOwnerDocument().createElement(
				FormElementData.MIN.toString());
		Integer minInt = new Double(formElement.min()).intValue();
		min.appendChild(element.getOwnerDocument().createTextNode(
				Integer.toString(minInt)));
		element.appendChild(min);

		Element max = element.getOwnerDocument().createElement(
				FormElementData.MAX.toString());
		Integer maxInt = new Double(formElement.max()).intValue();
		max.appendChild(element.getOwnerDocument().createTextNode(
				Integer.toString(maxInt)));
		element.appendChild(max);

	}

	private void processFloatFormElement(FormElement formElement,
			Element element) {
		Element min = element.getOwnerDocument().createElement(
				FormElementData.MIN.toString());
		Float minFloat = new Double(formElement.min()).floatValue();
		min.appendChild(element.getOwnerDocument().createTextNode(
				Float.toString(minFloat)));
		element.appendChild(min);

		Element max = element.getOwnerDocument().createElement(
				FormElementData.MAX.toString());
		Float maxLong = new Double(formElement.max()).floatValue();
		max.appendChild(element.getOwnerDocument().createTextNode(
				Float.toString(maxLong)));
		element.appendChild(max);

		Element decimal = element.getOwnerDocument().createElement(
				FormElementData.DECIMAL_PLACES.toString());
		decimal.appendChild(element.getOwnerDocument().createTextNode(
				Integer.toString(formElement.decimalPlaces())));
		element.appendChild(decimal);

	}

	private void processChoiceFormElement(FormElement widget, Element element) {
		String enumClass = widget.enumClass();
		try {
			ArrayList<String> enumValues = new ArrayList<String>();
			Enum<?>[] enums = (Enum[]) Class.forName(enumClass)
					.getEnumConstants();
			for (Enum<?> e : enums) {
				enumValues.add(e.toString());
			}
			processPossibleValues(element, enumValues);
		} catch (ClassNotFoundException e) {
			logger.debug(e.getMessage());
		}
	}

	private void processPossibleValues(Element element,
			ArrayList<String> possibleValues) {
		if (possibleValues.size() > 0) {
			Element values = element.getOwnerDocument().createElement(
					FormElementData.POSSIBLE_VALUES.toString());
			for (String value : possibleValues) {
				Element valueElement = element.getOwnerDocument()
						.createElement(
								FormElementData.POSSIBLE_VALUE.toString());
				valueElement.appendChild(element.getOwnerDocument()
						.createTextNode(value));
				values.appendChild(valueElement);
			}
			element.appendChild(values);
		}
	}

}
