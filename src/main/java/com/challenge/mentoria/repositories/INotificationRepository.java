package com.challenge.mentoria.repositories;

import com.challenge.mentoria.models.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface INotificationRepository extends JpaRepository <Notification, Integer> {
//    @Query("SELECT n FROM Notification n WHERE n.user.id = :user_id")
//    public List<Notification> findByUserId(@Param("user_id") Integer id);

    List<Notification> findByUsuario_Id(Integer userId); /*hace lo mismo*/
}
