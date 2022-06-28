package com.tripple.event.entity;

import javax.persistence.*;

@Entity
public class Pointlog {

    @Id @GeneratedValue
    @Column(name = "pointlog_id")
    private Long id;
    private int value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
