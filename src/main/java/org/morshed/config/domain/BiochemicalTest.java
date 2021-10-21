package org.morshed.config.domain;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A BiochemicalTest.
 */
@Document(collection = "biochemical_test")
public class BiochemicalTest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("test_name")
    private String testName;

    @Field("description")
    private String description;

    @DBRef
    @Field("defaultMeasurementType")
    private MeasurementType defaultMeasurementType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public BiochemicalTest id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTestName() {
        return this.testName;
    }

    public BiochemicalTest testName(String testName) {
        this.setTestName(testName);
        return this;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getDescription() {
        return this.description;
    }

    public BiochemicalTest description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MeasurementType getDefaultMeasurementType() {
        return this.defaultMeasurementType;
    }

    public void setDefaultMeasurementType(MeasurementType measurementType) {
        this.defaultMeasurementType = measurementType;
    }

    public BiochemicalTest defaultMeasurementType(MeasurementType measurementType) {
        this.setDefaultMeasurementType(measurementType);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BiochemicalTest)) {
            return false;
        }
        return id != null && id.equals(((BiochemicalTest) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BiochemicalTest{" +
            "id=" + getId() +
            ", testName='" + getTestName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
