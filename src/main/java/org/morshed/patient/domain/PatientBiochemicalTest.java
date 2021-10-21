package org.morshed.patient.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import org.morshed.config.domain.BiochemicalTest;
import org.morshed.config.domain.MeasurementType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A PatientBiochemicalTest.
 */
@Document(collection = "patient_biochemical_test")
public class PatientBiochemicalTest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("other")
    private String other;

    @Field("value")
    private Double value;

    @DBRef
    @Field("patient")
    @JsonIgnoreProperties(
        value = { "nutritionState", "activityLevel", "dietNatures", "supplements", "restrictedFoodCategories" },
        allowSetters = true
    )
    private Patient patient;

    @DBRef
    @Field("biochemicalTest")
    @JsonIgnoreProperties(value = { "defaultMeasurementType" }, allowSetters = true)
    private BiochemicalTest biochemicalTest;

    @DBRef
    @Field("measurementType")
    private MeasurementType measurementType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public PatientBiochemicalTest id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOther() {
        return this.other;
    }

    public PatientBiochemicalTest other(String other) {
        this.setOther(other);
        return this;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public Double getValue() {
        return this.value;
    }

    public PatientBiochemicalTest value(Double value) {
        this.setValue(value);
        return this;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Patient getPatient() {
        return this.patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public PatientBiochemicalTest patient(Patient patient) {
        this.setPatient(patient);
        return this;
    }

    public BiochemicalTest getBiochemicalTest() {
        return this.biochemicalTest;
    }

    public void setBiochemicalTest(BiochemicalTest biochemicalTest) {
        this.biochemicalTest = biochemicalTest;
    }

    public PatientBiochemicalTest biochemicalTest(BiochemicalTest biochemicalTest) {
        this.setBiochemicalTest(biochemicalTest);
        return this;
    }

    public MeasurementType getMeasurementType() {
        return this.measurementType;
    }

    public void setMeasurementType(MeasurementType measurementType) {
        this.measurementType = measurementType;
    }

    public PatientBiochemicalTest measurementType(MeasurementType measurementType) {
        this.setMeasurementType(measurementType);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PatientBiochemicalTest)) {
            return false;
        }
        return id != null && id.equals(((PatientBiochemicalTest) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PatientBiochemicalTest{" +
            "id=" + getId() +
            ", other='" + getOther() + "'" +
            ", value=" + getValue() +
            "}";
    }
}
