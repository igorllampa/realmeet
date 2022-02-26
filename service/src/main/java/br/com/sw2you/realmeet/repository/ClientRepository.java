package br.com.sw2you.realmeet.repository;

import br.com.sw2you.realmeet.domain.entity.Client;
import br.com.sw2you.realmeet.domain.entity.Room;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, String> {}
