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
 * A FoodCategory.
 */
@Document(collection = "food_category")
public class FoodCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("name")
    private String name;

    @Field("description")
    private String description;

    @DBRef
    @Field("foodCategories")
    @JsonIgnoreProperties(
        value = { "nutritionState", "activityLevel", "dietNatures", "supplements", "restrictedFoodCategories" },
        allowSetters = true
    )
    private Set<Patient> foodCategories = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public FoodCategory id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public FoodCategory name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public FoodCategory description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Patient> getFoodCategories() {
        return this.foodCategories;
    }

    public void setFoodCategories(Set<Patient> patients) {
        if (this.foodCategories != null) {
            this.foodCategories.forEach(i -> i.removeRestrictedFoodCategory(this));
        }
        if (patients != null) {
            patients.forEach(i -> i.addRestrictedFoodCategory(this));
        }
        this.foodCategories = patients;
    }

    public FoodCategory foodCategories(Set<Patient> patients) {
        this.setFoodCategories(patients);
        return this;
    }

    public FoodCategory addFoodCategory(Patient patient) {
        this.foodCategories.add(patient);
        patient.getRestrictedFoodCategories().add(this);
        return this;
    }

    public FoodCategory removeFoodCategory(Patient patient) {
        this.foodCategories.remove(patient);
        patient.getRestrictedFoodCategories().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FoodCategory)) {
            return false;
        }
        return id != null && id.equals(((FoodCategory) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FoodCategory{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
