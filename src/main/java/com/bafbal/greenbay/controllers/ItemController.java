package com.bafbal.greenbay.controllers;

import com.bafbal.greenbay.dtos.CreateItemDTO;
import com.bafbal.greenbay.exceptions.ItemNotFoundException;
import com.bafbal.greenbay.models.Bid;
import com.bafbal.greenbay.models.Item;
import com.bafbal.greenbay.services.BidService;
import com.bafbal.greenbay.services.ItemService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ItemController {

  private ItemService itemService;
  private BidService bidService;

  @Autowired
  public ItemController(ItemService itemService, BidService bidService) {
    this.itemService = itemService;
    this.bidService = bidService;
  }

  @PostMapping("/create")
  public ResponseEntity<?> createItem(@RequestBody CreateItemDTO createItemDTO) {
    itemService.validateCreateItemDTO(createItemDTO);
    return ResponseEntity.status(201).body(itemService.createItem(createItemDTO));
  }


  @GetMapping({"/list/{page}", "/list"})
  public ResponseEntity<?> listSellableItems(@PathVariable(required = false) Integer page) {
    return ResponseEntity.status(200).body(itemService.getListOfItems(page));
  }

  @GetMapping({"/view/{id}"})
  public ResponseEntity<?> viewItem(@PathVariable(required = false) Long id) {
    Optional<Item> optionalItem = itemService.getItem(id);
    if (optionalItem.isPresent()) {
      return ResponseEntity.status(200).body(itemService.getItemDTO(optionalItem.get()));
    } else {
      throw new ItemNotFoundException("Item not found.");
    }
  }

  @PostMapping("/bid")
  public ResponseEntity<?> placeBidOnItem(@RequestParam(name = "id") Long itemId, @RequestParam(name = "bid") Long bidToBePlaced) {
    Bid bid = bidService.getBidFromRequest(itemId, bidToBePlaced);
    bidService.placeBid(bid);
    return ResponseEntity.status(200).body(itemService.getItemDTO(bid.getItem()));
  }
}
