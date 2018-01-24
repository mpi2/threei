package org.immunophenotype.web.services;

import org.immunophenotype.web.common.Result;
import org.immunophenotype.web.common.SignificanceType;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ParameterDetails {

	private Map<String, List<Result>>resultsBySex;
    

	public Map<String, List<Result>> getResultsBySex() {
		return resultsBySex;
	}

	public void setResultsBySex(Map<String, List<Result>> resultsBySex) {
		this.resultsBySex = resultsBySex;
	}

	private String name;
    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

   

    @Override
    public String toString() {
        return "ParameterDetails{" +
                "name='" + name + '\'' +
                
                '}';
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        ParameterDetails that = (ParameterDetails) o;
//        return Objects.equals(name, that.name) &&
//                maleSignificant == that.maleSignificant &&
//                femaleSignificant == that.femaleSignificant;
//    }

//    @Override
//    public int hashCode() {
//
//        return Objects.hash(name, maleSignificant, femaleSignificant);
//    }

	public Result getResults() {
		// TODO Auto-generated method stub
		return null;
	}
}
