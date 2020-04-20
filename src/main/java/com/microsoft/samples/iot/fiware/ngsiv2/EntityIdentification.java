package com.microsoft.samples.iot.fiware.ngsiv2;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

import org.springframework.util.StringUtils;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EntityIdentification {

    public static final String TYPE = "type";

    public static final String TYPE_PATTERN = "typePattern";

    public static final String ID = "id";

    public static final String ID_PATTERN = "idPattern";

    public static final String PLACEHOLDER_VALUE = ".*";

    private String id;

    private String idPattern;

    private String type;

    private String typePattern;

    public EntityIdentification() {
    }

    public EntityIdentification(String id) {
        this.id = id;
    }

    public EntityIdentification(String type, String id) {
        this.type = type;
        this.id = id;
    }

    public EntityIdentification(Map<String,Object> json) {
        if (json != null && json.size() > 0) {
            if (json.containsKey(TYPE))
                this.type = json.get(TYPE).toString();
            if (json.containsKey(TYPE_PATTERN))
                this.typePattern = json.get(TYPE_PATTERN).toString();
            if (json.containsKey(ID))
                this.id = json.get(ID).toString();
            if (json.containsKey(ID_PATTERN))
                this.idPattern = json.get(ID_PATTERN).toString();
        }
    }

    /**
     * 
     * @param json
     * @return
     */
    public static EntityIdentification fromJson(Map<String,Object> json) {

        if (json != null)
            return new EntityIdentification(json);
        else
            return null;
    }

    /**
     * 
     * @return
     */
    public boolean isValid() {
        
        if (StringUtils.hasText(this.type) && StringUtils.hasText(this.typePattern))
            return false;
        if (StringUtils.hasText(this.id) && StringUtils.hasText(this.idPattern))
            return false;

        return !(this.type == null && this.typePattern == null) 
            && !(this.id == null && this.idPattern == null);
    }

    public boolean identifiesSame(EntityIdentification other) {

        if (other == null)
            return false;
        else {
            if (!other.isValid()) {
                throw new IllegalArgumentException("Parameter 'other' is invalid. " + other);
            } else if (!this.isValid()) {
                throw new IllegalStateException("Object is invalid. " + this);
            }
       
            return this.equals(other);
        }
    }

    public boolean isForSingleType() {

        return StringUtils.hasText(this.type);
    }

    public boolean isForSingleEntity() {
        
        return StringUtils.hasText(this.id);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdPattern() {
        return idPattern;
    }

    public void setIdPattern(String idPattern) {
        this.idPattern = idPattern;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypePattern() {
        return typePattern;
    }

    public void setTypePattern(String typePattern) {
        this.typePattern = typePattern;
    }

    @Override
    public String toString() {
        return "EntityIdentification [id=" + id + ", idPattern=" + idPattern + ", type=" + type + ", typePattern="
                + typePattern + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((idPattern == null) ? 0 : idPattern.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        result = prime * result + ((typePattern == null) ? 0 : typePattern.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        EntityIdentification other = (EntityIdentification) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (idPattern == null) {
            if (other.idPattern != null)
                return false;
        } else if (!idPattern.equals(other.idPattern))
            return false;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        if (typePattern == null) {
            if (other.typePattern != null)
                return false;
        } else if (!typePattern.equals(other.typePattern))
            return false;
        return true;
    }
}