package net.datasa.finders.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.datasa.finders.domain.entity.MemberEntity;
import net.datasa.finders.domain.entity.RoleName;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, String> {

	// 특정 역할을 가진 모든 회원을 조회하는 메소드
    List<MemberEntity> findAllByRoleName(RoleName roleClient); // 역할 이름으로 변경
	
    MemberEntity findByMemberIdAndRoleName(String memberId, RoleName roleName);

    // 특정 프로젝트에 참여한 멤버를 찾는 메서드
    List<MemberEntity> findByProjects_ProjectNum(int projectNum);

    // 특정 멤버 ID와 프로젝트 번호를 기반으로 멤버를 찾는 메서드 추가
    List<MemberEntity> findByMemberIdAndProjects_ProjectNum(String memberId, int projectNum);

    // team 테이블의 데이터를 확인하기 위한 임시 메서드
    @Query(value = "SELECT project_num, member_id FROM team", nativeQuery = true)
    List<Object[]> findTeamEntries();

    // 새로운 쿼리 메서드 - JPQL 사용
    @Query("SELECT m FROM MemberEntity m WHERE m.memberId = :memberId")
    Optional<MemberEntity> findByCustomMemberId(@Param("memberId") String memberId);

    // 프로젝트 번호와 클라이언트 ID를 제외한 프리랜서 목록 조회
    @Query("SELECT m FROM MemberEntity m JOIN TeamEntity t ON m.memberId = t.memberId WHERE t.projectNum = :projectNum AND m.memberId != :clientId")
    List<MemberEntity> findByProjectNumAndNotClientId(@Param("projectNum") int projectNum, @Param("clientId") String clientId);

    // 프로젝트 번호를 기준으로 팀 멤버를 조회하는 쿼리
    @Query("SELECT m FROM MemberEntity m JOIN TeamEntity t ON m.memberId = t.memberId WHERE t.projectNum = :projectNum")
    List<MemberEntity> findByProjectNum(@Param("projectNum") int projectNum);

	List<MemberEntity> findByRoleName(RoleName roleFreelancer);
	
	// 이름과 이메일을 기준으로 사용자 찾는 메서드
	
	Optional<MemberEntity> findBymemberNameAndEmail(@Param("memberName") String memberName, @Param("email") String email);

	Optional<MemberEntity> findByMemberIdAndMemberNameAndEmail(String memberId, String memberName, String email);


	List<MemberEntity> findByRoleNameAndMemberIdContaining(RoleName roleFreelancer, String search);
    // member_id로 멤버를 찾는 메서드
    Optional<MemberEntity> findByMemberId(String memberId);

	Page<MemberEntity> findByRoleNameAndMemberIdContaining(RoleName roleFreelancer, String search, Pageable pageable);
}
