package com.tripple.event.entity;

import org.springframework.data.domain.Persistable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
public class Place extends BaseEntity implements Persistable<String> {

    @Id
    @Column(name = "place_id")
    private String id;

    private String name;

    public Place() {
    }

    public Place(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public boolean isNew() {
        return getCreatedTime() == null;
    }
}