package ru.kataaas.kaflent.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.kataaas.kaflent.entity.GroupRoleKey;
import ru.kataaas.kaflent.entity.GroupUser;

import java.util.List;

public interface GroupUserJoinRepository extends JpaRepository<GroupUser, GroupRoleKey> {

    GroupUser findByUserIdAndGroupId(Long userId, Long groupId);

    List<GroupUser> getAllByGroupId(Long groupId);

    List<GroupUser> getAllByUserId(Long userId);

    @Query(value = "SELECT g.user_id FROM group_user g WHERE group_id = :groupId", nativeQuery = true)
    List<Long> getUsersIdInGroup(@Param("groupId") Long groupId);

    int countAllByGroupId(Long groupId);

    boolean existsByUserIdAndGroupId(Long userId, Long groupId);

    void deleteAllByGroupId(Long groupId);
}
