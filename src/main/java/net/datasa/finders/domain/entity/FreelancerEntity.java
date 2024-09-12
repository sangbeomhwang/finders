package net.datasa.finders.domain.entity;

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
@Table(name = "freelancer_member")

public class FreelancerEntity {

    @Id
    @Column(name = "freelancer_id")
    private String freelancerId;

    @ManyToOne
    @JoinColumn(name = "freelancer_id", referencedColumnName = "member_id", insertable = false, updatable = false)
    private MemberEntity member;

	@Column(name = "freelancer_phone", length = 100)
	private String freelancerPhone;
	
	@Column(name = "postal_code", length = 20)
	private String postalCode;
	
	@Column(name = "address", length = 100)
	private String address;
	
	@Column(name = "detail_address", length = 100)
	private String detailAddress;	
	
	@Column(name = "extra_address", length = 100)
	private String extraAddress;
	

	@Column(name = "country", length = 100)
	private String country;

}
