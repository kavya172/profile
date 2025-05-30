package com.galaxe.gxworkflow.mapper;

import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(name = "InputVariable")
public class InputVariable {

	@XmlAnyAttribute
	String name;
	
	@XmlValue
	String actualValue;
	
}
