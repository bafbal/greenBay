package com.bafbal.greenbay.services;

import com.bafbal.greenbay.dtos.CreateItemDTO;
import com.bafbal.greenbay.dtos.ListSellableItemsDTO;
import com.bafbal.greenbay.dtos.SavedItemDTO;
import java.util.List;

public interface ItemService {
  
  void validateCreateItemDTO(CreateItemDTO createItemDTO);

  SavedItemDTO createItem(CreateItemDTO createItemDTO);

  List<ListSellableItemsDTO> getListOfItems(Integer page);
}
