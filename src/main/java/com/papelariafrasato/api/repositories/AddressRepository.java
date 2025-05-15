package com.papelariafrasato.api.repositories;

import com.papelariafrasato.api.models.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, String>{
    @Query(value = "SELECT * FROM address WHERE user_id = :userId", nativeQuery = true)
    Optional<Address> findByUserId(@Param("userId")String userId);
}
