package org.morshed.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A ComponentNavigation.
 */
@Document(collection = "component_navigation")
public class ComponentNavigation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull(message = "must not be null")
    @Field("name")
    private String name;

    @Field("icon")
    private String icon;

    @NotNull(message = "must not be null")
    @Field("location")
    private String location;

    @Field("roles")
    private String roles;

    @DBRef
    @Field("parent")
    @JsonIgnoreProperties(value = { "parent" }, allowSetters = true)
    private ComponentNavigation parent;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public ComponentNavigation id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public ComponentNavigation name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public ComponentNavigation icon(String icon) {
        this.setIcon(icon);
        return this;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getLocation() {
        return this.location;
    }

    public ComponentNavigation location(String location) {
        this.setLocation(location);
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRoles() {
        return this.roles;
    }

    public ComponentNavigation roles(String roles) {
        this.setRoles(roles);
        return this;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public ComponentNavigation getParent() {
        return this.parent;
    }

    public void setParent(ComponentNavigation componentNavigation) {
        this.parent = componentNavigation;
    }

    public ComponentNavigation parent(ComponentNavigation componentNavigation) {
        this.setParent(componentNavigation);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ComponentNavigation)) {
            return false;
        }
        return id != null && id.equals(((ComponentNavigation) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ComponentNavigation{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", location='" + getLocation() + "'" +
            ", roles='" + getRoles() + "'" +
            "}";
    }
}
