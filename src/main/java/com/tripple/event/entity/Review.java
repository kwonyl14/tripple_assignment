package com.tripple.event.entity;

import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Review extends BaseEntity implements Persistable<String>{

    @Id
    @Column(name = "review_id")
    private String id;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_ID")
    private Place place;

    @OneToMany(mappedBy = "review")
    private List<Photo> photos = new ArrayList<>();

    public Review() {
    }

    public Review(String id, String content, User user, Place place) {
        this.id = id;
        this.content = content;
        this.user = user;
        this.place = place;
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
