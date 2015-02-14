package com.lajommariano.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@MappedSuperclass
public class BaseModel extends BaseObject {
	
	private Long id;
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getClass().getSimpleName());
		sb.append("[id=");
		sb.append(id);
		sb.append("]");
	
		return sb.toString();
	}
	
    public boolean equals(Object o) {
		if (o == this) return true;
		if (o == null) return false;			
		if (getClass() != o.getClass()) return false;
		
		BaseModel other = (BaseModel) o;
		if (id == null && other.getId() != null) return false;
		
		return new EqualsBuilder().append(id, other.getId()).isEquals();
	}
	
    public int hashCode() {
    	return new HashCodeBuilder().append(id).toHashCode();
    }
    
    @Id
	@GeneratedValue(strategy=GenerationType.AUTO)
    @Column(unique = true, nullable = false)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id){
		this.id = id;
	}
}
