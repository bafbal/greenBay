package com.bafbal.greenbay.services;

import com.bafbal.greenbay.dtos.CreateItemDTO;
import com.bafbal.greenbay.dtos.ItemDTO;
import com.bafbal.greenbay.dtos.ListSellableItemsDTO;
import com.bafbal.greenbay.dtos.SavedItemDTO;
import com.bafbal.greenbay.models.Item;
import java.util.List;
import java.util.Optional;

public interface ItemService {
  
  void validateCreateItemDTO(CreateItemDTO createItemDTO);

  SavedItemDTO createItem(CreateItemDTO createItemDTO);

  List<ListSellableItemsDTO> getListOfItems(Integer page);

  Optional<Item> getItem(Long id);

  ItemDTO getItemDTO(Item item);
}
