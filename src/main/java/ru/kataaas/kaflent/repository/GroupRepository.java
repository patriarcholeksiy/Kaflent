package ru.kataaas.kaflent.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.kataaas.kaflent.entity.GroupEntity;

public interface GroupRepository extends JpaRepository<GroupEntity, Long> {

    @Query(value = "SELECT g.id FROM groups_entities g WHERE g.name = :name", nativeQuery = true)
    Long findIdByName(@Param("name") String name);

    GroupEntity findByName(String name);

    boolean existsByName(String name);

}
