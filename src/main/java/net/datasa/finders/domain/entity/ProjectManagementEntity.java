package net.datasa.finders.domain.entity;

import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "project_management")
public class ProjectManagementEntity {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "management_num")
    private Integer managementNum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_num", referencedColumnName = "project_num")
    private ProjectPublishingEntity projectPublishing;

    @Column(name = "complete_status", columnDefinition = "TINYINT(1) DEFAULT 0 CHECK (complete_status IN (0, 1))")
    private Boolean completeStatus;

}
