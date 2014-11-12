package org.command4spring.sample.billing.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Invoice {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    Long id;
    Long customerId;
    String title;
    Integer value;

    protected Invoice() {
    }

    public Invoice(final Long customerId, final String title, final Integer value) {
        super();
        this.customerId = customerId;
        this.title = title;
        this.value = value;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return this.customerId;
    }

    public void setCustomerId(final Long customerId) {
        this.customerId = customerId;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public Integer getValue() {
        return this.value;
    }

    public void setValue(final Integer value) {
        this.value = value;
    }

}
