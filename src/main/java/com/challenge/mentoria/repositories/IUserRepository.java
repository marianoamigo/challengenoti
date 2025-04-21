package com.challenge.mentoria.repositories;

import com.challenge.mentoria.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository <Usuario, Integer> {

    Usuario findByMail(String mail);
//    @Query("SELECT u FROM User u WHERE u.mail = :mail")
//    public Usuario findByMail(@Param("mail") String mail);
}
