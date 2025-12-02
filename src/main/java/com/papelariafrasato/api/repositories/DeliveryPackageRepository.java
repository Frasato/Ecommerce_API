package com.papelariafrasato.api.repositories;

import com.papelariafrasato.api.models.DeliveryPackage;
import com.papelariafrasato.api.models.ProductAnalytics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryPackageRepository extends JpaRepository<DeliveryPackage, String> {
    @Query(value = "SELECT * FROM delivery_package WHERE status = 'PENDING'", nativeQuery = true)
    List<DeliveryPackage> findByPendingStatus();

    @Query(value = "SELECT * FROM delivery_package WHERE status != 'PENDING'", nativeQuery = true)
    List<DeliveryPackage> findByOtherStatus();
}
