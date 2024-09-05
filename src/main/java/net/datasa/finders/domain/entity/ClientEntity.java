package net.datasa.finders.domain.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
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
@Table(name = "client_member")

public class ClientEntity {

	@Id
	@ManyToOne
	@JoinColumn(name = "client_id", referencedColumnName = "member_id")
	private MemberEntity clientId;

    @Column(name = "client_phone", length = 20)
    private String clientPhone;

    @Column(name = "client_address", columnDefinition = "TEXT")
    private String clientAddress;

    @Column(name = "industry", length = 100)
    private String industry;

    @Column(name = "founded_date")
    private LocalDate foundedDate;

    @Column(name = "employee_count")
    private Integer employeeCount;

    @Column(name = "website", length = 255)
    private String website;

    @Column(name = "updated_time", nullable = false, insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updatedTime;

}