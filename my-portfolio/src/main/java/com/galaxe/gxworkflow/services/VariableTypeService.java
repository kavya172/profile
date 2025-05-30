package com.galaxe.gxworkflow.services;

import com.galaxe.gxworkflow.entity.VariableType;

public interface VariableTypeService {


	VariableType findVariableTypeByName(String code) throws Exception;

	
}
