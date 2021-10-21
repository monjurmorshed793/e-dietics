package org.morshed.domain;

import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A ActivityLevel.
 */
@Document(collection = "activity_level")
public class ActivityLevel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("order")
    private Integer order;

    @NotNull(message = "must not be null")
    @Field("label")
    private String label;

    @Field("note")
    private String note;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public ActivityLevel id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getOrder() {
        return this.order;
    }

    public ActivityLevel order(Integer order) {
        this.setOrder(order);
        return this;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getLabel() {
        return this.label;
    }

    public ActivityLevel label(String label) {
        this.setLabel(label);
        return this;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getNote() {
        return this.note;
    }

    public ActivityLevel note(String note) {
        this.setNote(note);
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ActivityLevel)) {
            return false;
        }
        return id != null && id.equals(((ActivityLevel) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ActivityLevel{" +
            "id=" + getId() +
            ", order=" + getOrder() +
            ", label='" + getLabel() + "'" +
            ", note='" + getNote() + "'" +
            "}";
    }
}
