package org.command4spring.sample.customer.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Customer {

    @Id
    Long id;

    String name;

    public Customer() {
    }

    public Customer(final long id, final String name) {
        this.name=name;
        this.id=id;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

}
