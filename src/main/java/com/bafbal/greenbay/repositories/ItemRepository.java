package com.bafbal.greenbay.repositories;

import com.bafbal.greenbay.models.Item;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

  Page<Item> findAllByBuyerIsNull(Pageable pageable);

  Optional<Item> findByItemName(String itemName);
}
