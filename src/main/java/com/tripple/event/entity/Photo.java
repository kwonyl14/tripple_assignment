package com.tripple.event.entity;

import org.springframework.data.domain.Persistable;

import javax.persistence.*;

@Entity
public class Photo extends BaseEntity implements Persistable<String> {

    @Id
    @Column(name = "photo_id")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;

    public Photo() {
    }

    public Photo(String id, Review review) {
        this.id = id;
        this.review = review;
    }

    @Override
    public String getId() {
        return this.id;
    }

    public Review getReview() {
        return review;
    }

    @Override
    public boolean isNew() {
        return getCreatedTime() == null;
    }
}