package net.datasa.finders.domain.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id")
    private Integer clientId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "member_id")
    private MemberEntity member;

    @Column(name = "client_phone", length = 20)
    private String clientPhone;

//    @Column(name = "client_address", columnDefinition = "TEXT")
//    private String clientAddress;

    @Column(name = "industry", length = 100)
    private String industry;

    @Column(name = "founded_date")
    private LocalDate foundedDate;

    @Column(name = "employee_count")
    private Integer employeeCount;

    @Column(name = "website", length = 255)
    private String website;
    
	@Column(name = "postal_code", length = 20)
	private String postalCode;
	
	@Column(name = "address", length = 100)
	private String address;
	
	@Column(name = "detail_address", length = 100)
	private String detailAddress;	
	
	@Column(name = "extra_address", length = 100)
	private String extraAddress;

}
