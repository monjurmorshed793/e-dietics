package org.morshed.config.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.morshed.patient.domain.Patient;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Supplements.
 */
@Document(collection = "supplements")
public class Supplements implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("name")
    private String name;

    @Field("description")
    private String description;

    @DBRef
    @Field("patients")
    @JsonIgnoreProperties(
        value = { "nutritionState", "activityLevel", "dietNatures", "supplements", "restrictedFoodCategories" },
        allowSetters = true
    )
    private Set<Patient> patients = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Supplements id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Supplements name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Supplements description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Patient> getPatients() {
        return this.patients;
    }

    public void setPatients(Set<Patient> patients) {
        if (this.patients != null) {
            this.patients.forEach(i -> i.removeSupplements(this));
        }
        if (patients != null) {
            patients.forEach(i -> i.addSupplements(this));
        }
        this.patients = patients;
    }

    public Supplements patients(Set<Patient> patients) {
        this.setPatients(patients);
        return this;
    }

    public Supplements addPatient(Patient patient) {
        this.patients.add(patient);
        patient.getSupplements().add(this);
        return this;
    }

    public Supplements removePatient(Patient patient) {
        this.patients.remove(patient);
        patient.getSupplements().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Supplements)) {
            return false;
        }
        return id != null && id.equals(((Supplements) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Supplements{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
