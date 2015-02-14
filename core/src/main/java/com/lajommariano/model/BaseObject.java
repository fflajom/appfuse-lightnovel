package com.lajommariano.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;


/**
 * Base class for Model objects. Child objects should implement toString(),
 * equals() and hashCode().
 * 
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
@MappedSuperclass
public abstract class BaseObject implements Serializable {    
	
	public abstract String toString();
	
	public abstract int hashCode();
	
	public abstract boolean equals(Object o);
}
