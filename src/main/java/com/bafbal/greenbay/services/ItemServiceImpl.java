package com.bafbal.greenbay.services;

import com.bafbal.greenbay.dtos.CreateItemDTO;
import com.bafbal.greenbay.dtos.ItemDTO;
import com.bafbal.greenbay.dtos.ListSellableItemsDTO;
import com.bafbal.greenbay.dtos.SavedItemDTO;
import com.bafbal.greenbay.dtos.SellableItemDTO;
import com.bafbal.greenbay.dtos.SoldItemDTO;
import com.bafbal.greenbay.exceptions.InValidPageException;
import com.bafbal.greenbay.exceptions.InsufficientBalanceException;
import com.bafbal.greenbay.exceptions.BidTooLowException;
import com.bafbal.greenbay.exceptions.ItemAlreadySoldException;
import com.bafbal.greenbay.exceptions.ItemNotFoundException;
import com.bafbal.greenbay.exceptions.ItemPriceNotAcceptableException;
import com.bafbal.greenbay.exceptions.MissingItemDetailException;
import com.bafbal.greenbay.exceptions.UrlNotValidException;
import com.bafbal.greenbay.models.Bid;
import com.bafbal.greenbay.models.Item;
import com.bafbal.greenbay.models.User;
import com.bafbal.greenbay.repositories.ItemRepository;
import com.bafbal.greenbay.repositories.UserRepository;
import com.bafbal.greenbay.security.GreenBayUserDetails;
import com.bafbal.greenbay.security.MyUserDetailsService;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ItemServiceImpl implements ItemService {

  private ItemRepository itemRepository;
  private UserRepository userRepository;
  private ModelMapper modelMapper;
  private MyUserDetailsService myUserDetailsService;

  @Autowired
  public ItemServiceImpl(ItemRepository itemRepository, UserRepository userRepository, ModelMapper modelMapper,
      MyUserDetailsService myUserDetailsService) {
    this.itemRepository = itemRepository;
    this.userRepository = userRepository;
    this.modelMapper = modelMapper;
    this.myUserDetailsService = myUserDetailsService;
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
      throw new ItemPriceNotAcceptableException("Price cannot be negative.");
    }
    if (Math.ceil(createItemDTO.getPurchasePrice()) != Math.floor(createItemDTO.getPurchasePrice())
        || Math.ceil(createItemDTO.getStartPrice()) != Math.floor(
        createItemDTO.getStartPrice())) {
      throw new ItemPriceNotAcceptableException("Price must be a whole number.");
    }
  }

  private void validateUrl(CreateItemDTO createItemDTO) {
    try {
      URL url = new URL(createItemDTO.getPhotoUrl());
    } catch (MalformedURLException e) {
      throw new UrlNotValidException("Url is not valid.");
    }
  }

  @Override
  public SavedItemDTO createItem(CreateItemDTO createItemDTO) {
    GreenBayUserDetails userDetails = (GreenBayUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    User seller = userDetails.getUser();
    Item item = new Item(createItemDTO.getItemName(), createItemDTO.getDescription(), createItemDTO.getPhotoUrl(),
        Math.round(createItemDTO.getStartPrice()), Math.round(createItemDTO.getPurchasePrice()), seller);
    itemRepository.save(item);
    return new SavedItemDTO(item.getId(), item.getItemName(), item.getDescription(), item.getPhotoUrl(), item.getStartPrice(),
        item.getPurchasePrice(), item.getSeller().getId());
  }

  @Override
  public List<ListSellableItemsDTO> getListOfItems(Integer pageNumber) {
    pageNumber = corrigatePageNumber(pageNumber);
    Pageable pageForQuery = PageRequest.of(pageNumber, 2);
    Page<Item> page = itemRepository.findAllByBuyerIsNull(pageForQuery);
    List<ListSellableItemsDTO> listOfSellableItems = new ArrayList<>();
    for (Item item : page) {
      listOfSellableItems.add(new ListSellableItemsDTO(item.getItemName(), item.getPhotoUrl(), item.getHighestBid()));
    }
    return listOfSellableItems;
  }

  private Integer corrigatePageNumber(Integer page) {
    if (page != null && page < 1) {
      throw new InValidPageException("Page number must be higher than zero.");
    }
    return page == null ? 0 : page - 1;
  }

  @Override
  public Optional<Item> getItem(Long id) {
    return itemRepository.findById(id);
  }

  @Override
  public ItemDTO getItemDTO(Item item) {
    if (item.getBuyer() == null) {
      return modelMapper.map(item, SellableItemDTO.class);
    } else {
      return modelMapper.map(item, SoldItemDTO.class);
    }
  }

  @Override
  public void save(Item item) {
    itemRepository.save(item);
  }
}
