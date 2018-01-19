package org.immunophenotype.web.services;

import org.immunophenotype.web.common.SignificanceType;

import java.util.Objects;

public class ParameterDetails {

    private String name;
    private SignificanceType maleSignificant;
    private SignificanceType femaleSignificant;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SignificanceType getMaleSignificant() {
        return maleSignificant;
    }

    public void setMaleSignificant(SignificanceType maleSignificant) {
        this.maleSignificant = maleSignificant;
    }

    public SignificanceType getFemaleSignificant() {
        return femaleSignificant;
    }

    public void setFemaleSignificant(SignificanceType femaleSignificant) {
        this.femaleSignificant = femaleSignificant;
    }

    @Override
    public String toString() {
        return "ParameterDetails{" +
                "name='" + name + '\'' +
                ", maleSignificant=" + maleSignificant +
                ", femaleSignificant=" + femaleSignificant +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParameterDetails that = (ParameterDetails) o;
        return Objects.equals(name, that.name) &&
                maleSignificant == that.maleSignificant &&
                femaleSignificant == that.femaleSignificant;
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, maleSignificant, femaleSignificant);
    }
}
