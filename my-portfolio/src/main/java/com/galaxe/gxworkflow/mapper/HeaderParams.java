package com.galaxe.gxworkflow.mapper;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "QueryParams")
public class HeaderParams {

	List<InputVariable> parameters;
	
	//FIXME: WHY WE HAVE BOTH HeaderParams and QueryParams
}
