package com.tripple.event.entity;

import javax.persistence.*;

@Entity
public class Pointlog {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pointlog_id")
    private Long id;
    private int value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;

    public int getValue() {
        return value;
    }

    public Pointlog() {
    }

    public Pointlog(int value, Review review) {
        this.value = value;
        this.review = review;
    }
}
