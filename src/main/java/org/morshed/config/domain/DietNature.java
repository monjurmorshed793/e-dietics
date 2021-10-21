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
 * A DietNature.
 */
@Document(collection = "diet_nature")
public class DietNature implements Serializable {

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

    public DietNature id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public DietNature name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public DietNature description(String description) {
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
            this.patients.forEach(i -> i.removeDietNatures(this));
        }
        if (patients != null) {
            patients.forEach(i -> i.addDietNatures(this));
        }
        this.patients = patients;
    }

    public DietNature patients(Set<Patient> patients) {
        this.setPatients(patients);
        return this;
    }

    public DietNature addPatient(Patient patient) {
        this.patients.add(patient);
        patient.getDietNatures().add(this);
        return this;
    }

    public DietNature removePatient(Patient patient) {
        this.patients.remove(patient);
        patient.getDietNatures().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DietNature)) {
            return false;
        }
        return id != null && id.equals(((DietNature) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DietNature{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
