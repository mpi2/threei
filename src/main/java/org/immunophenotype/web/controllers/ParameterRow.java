package org.immunophenotype.web.controllers;
import java.util.HashMap;
import java.util.Map;

import org.immunophenotype.web.common.Result;

public class ParameterRow {
	private String parameterName = "Parameter Name";
	// should be female Hom or male Hom to match the possible column headers
	private Map<String, Result> headerKeyToResult = new HashMap<>();

	public String getParameterName() {
		return parameterName;
	}

	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

}
