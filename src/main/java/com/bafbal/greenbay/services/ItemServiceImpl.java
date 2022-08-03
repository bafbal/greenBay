package com.bafbal.greenbay.services;

import com.bafbal.greenbay.dtos.CreateItemDTO;
import com.bafbal.greenbay.dtos.SavedItemDTO;
import com.bafbal.greenbay.exceptions.ItemPriceNotAcceptableException;
import com.bafbal.greenbay.exceptions.MissingItemDetailException;
import com.bafbal.greenbay.exceptions.UrlNotValidException;
import com.bafbal.greenbay.models.Item;
import com.bafbal.greenbay.models.User;
import com.bafbal.greenbay.repositories.ItemRepository;
import com.bafbal.greenbay.repositories.UserRepository;
import java.net.MalformedURLException;
import java.net.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ItemServiceImpl implements ItemService {

  private ItemRepository itemRepository;
  private UserRepository userRepository;

  @Autowired
  public ItemServiceImpl(ItemRepository itemRepository, UserRepository userRepository) {
    this.itemRepository = itemRepository;
    this.userRepository = userRepository;
  }

  @Override
  public void validateCreateItemDTO(CreateItemDTO createItemDTO) {
    checkForNulls(createItemDTO);
    validatePrices(createItemDTO);
    validateUrl(createItemDTO);
  }

  private void checkForNulls(CreateItemDTO createItemDTO) {
    if (createItemDTO.getItemName() == null) {
      throw new MissingItemDetailException("Item name is missing.");
    }
    if (createItemDTO.getDescription() == null) {
      throw new MissingItemDetailException("Item description is missing.");
    }
    if (createItemDTO.getPhotoUrl() == null) {
      throw new MissingItemDetailException("Item picture url is missing.");
    }
    if (createItemDTO.getStartPrice() == null) {
      throw new MissingItemDetailException("Item start price is missing.");
    }
    if (createItemDTO.getPurchasePrice() == null) {
      throw new MissingItemDetailException("Item purchase price is missing.");
    }
  }

  private void validatePrices(CreateItemDTO createItemDTO) {
    if (createItemDTO.getPurchasePrice() < 0 || createItemDTO.getStartPrice() < 0) {
      throw new ItemPriceNotAcceptableException("Price cannot be negative");
    }
    if (Math.ceil(createItemDTO.getPurchasePrice()) != Math.floor(createItemDTO.getPurchasePrice()) || Math.ceil(createItemDTO.getStartPrice()) != Math.floor(
        createItemDTO.getStartPrice())) {
      throw new ItemPriceNotAcceptableException("Price must be a whole number");
    }
  }

  private void validateUrl(CreateItemDTO createItemDTO) {
    try {
      URL url = new URL(createItemDTO.getPhotoUrl());
    } catch (MalformedURLException e) {
      throw new UrlNotValidException("Url is not valid");
    }
  }

  @Override
  public SavedItemDTO createItem(CreateItemDTO createItemDTO) {
    Long sellerId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    User seller = userRepository.findById(sellerId).get();
    Item item = new Item(createItemDTO.getItemName(), createItemDTO.getDescription(), createItemDTO.getPhotoUrl(),
        Double.doubleToLongBits(createItemDTO.getStartPrice()), Double.doubleToLongBits(createItemDTO.getPurchasePrice()), seller);
    itemRepository.save(item);
    return new SavedItemDTO(item.getId(), item.getItemName(), item.getDescription(), item.getPhotoUrl(), item.getStartPrice(),
        item.getPurchasePrice(), item.getSeller().getId());
  }
}
