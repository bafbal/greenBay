package com.bafbal.greenbay.controllers;

import com.bafbal.greenbay.dtos.CreateItemDTO;
import com.bafbal.greenbay.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ItemController {

  private ItemService itemService;

  @Autowired
  public ItemController(ItemService itemService) {
    this.itemService = itemService;
  }

  @PostMapping("/create")
  public ResponseEntity<?> createItem(@RequestBody CreateItemDTO createItemDTO) {
    itemService.validateCreateItemDTO(createItemDTO);
    return ResponseEntity.status(201).body(itemService.createItem(createItemDTO));
  }

}
