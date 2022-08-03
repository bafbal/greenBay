package com.bafbal.greenbay.services;

import com.bafbal.greenbay.dtos.CreateItemDTO;
import com.bafbal.greenbay.dtos.SavedItemDTO;

public interface ItemService {
  
  void validateCreateItemDTO(CreateItemDTO createItemDTO);

  SavedItemDTO createItem(CreateItemDTO createItemDTO);
}
