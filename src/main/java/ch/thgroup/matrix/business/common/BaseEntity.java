package ch.thgroup.matrix.business.common;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @Column(name = "created_by", nullable = false)
    private Short createdBy;

    @Column(name = "last_updated_date")
    private LocalDateTime lastUpdatedDate;

    @Column(name = "updated_by")
    private Short updatedBy;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    @PrePersist
    protected void prePersist() {
        createdDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void preUpdate() {
        lastUpdatedDate = LocalDateTime.now();
    }

}