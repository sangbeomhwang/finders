package net.datasa.finders.repository;

import net.datasa.finders.domain.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//채팅 전용
//채팅에서 project entity, dto를 사용하는 repository
@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, Integer> {

    // 프로젝트 제목으로 프로젝트 검색 (필요 시 커스텀 메서드 추가 가능)
    Optional<ProjectEntity> findByProjectName(String projectName);
}
