package com.justsend.justsendbackend.Repository;

import com.justsend.justsendbackend.Entity.DataEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface DataRepository extends JpaRepository<DataEntity, UUID> {

    Optional<DataEntity> findByCode(String code);
}
