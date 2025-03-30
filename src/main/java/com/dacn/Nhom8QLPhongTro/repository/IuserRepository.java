package com.dacn.Nhom8QLPhongTro.repository;

import com.dacn.Nhom8QLPhongTro.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IuserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.username = ?1")
    User findByUsername(String username);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO user_roles (users_id, roles_id) "+"VALUES(?1,?2)", nativeQuery = true)
    void addRoleToUser(Long userId, Long roleId);

    @Query(value = "select u.id from User u where u.username = ?1")
    Long getUserIdByUsername(String username);

    @Query(value = "SELECT r.name FROM  role  r INNER JOIN user_roles ur "+ "ON r.id = ur.roles_id " +
            "WHERE ur.users_id = ?1 ", nativeQuery = true)
    String[] getRoleOfUser(Long userId);

}
