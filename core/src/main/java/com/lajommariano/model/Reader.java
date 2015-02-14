package com.lajommariano.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.DiscriminatorValue;

@Entity
@DiscriminatorValue("R")
public class Reader extends User {

	private static final long serialVersionUID = 1L;
	
	private List<Book> subscriptions = new ArrayList<Book>();
	
	@ManyToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinTable(
		name = "subscription", 
		joinColumns = { @JoinColumn(name="user_id", nullable=false, updatable=false) }, 
		inverseJoinColumns = { @JoinColumn(name = "book_id", nullable=false, updatable=false) }
	)
	public List<Book> getSubscriptions() {
		return subscriptions;
	}

	public void setSubscriptions(List<Book> subscriptions) {
		this.subscriptions = subscriptions;
	}

}
