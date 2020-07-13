package com.galid.commerce.domains.delivery.domain;

import com.galid.commerce.domains.delivery.domain.DeliveryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<DeliveryEntity, Long> {
}
