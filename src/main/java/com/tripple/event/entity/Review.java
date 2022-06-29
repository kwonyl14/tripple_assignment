package com.tripple.event.entity;

import com.tripple.event.request.ReviewUpdateReq;
import lombok.Getter;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "reviewUniqueIndex",
                        columnNames = {"user_id", "place_id"}
                )
        }
)
public class Review extends BaseEntity implements Persistable<String>{

    @Id
    @Column(name = "review_id")
    private String id;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_ID", nullable = false)
    private Place place;

    @OneToMany(mappedBy = "review")
    private List<Photo> photos = new ArrayList<>();

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public Review() {
    }

    public Review(String id, String content, User user, Place place) {
        this.id = id;
        this.content = content;
        this.user = user;
        this.place = place;
    }

    public void updateReview(ReviewUpdateReq reviewUpdateReq) {
        if (reviewUpdateReq.getContent() != null && reviewUpdateReq.getContent().trim().length()>0)
            this.content = reviewUpdateReq.getContent();
    }

    @Override
    public boolean isNew() {
        return getCreatedTime() == null;
    }
}
