package com.bafbal.greenbay.unit;

import com.bafbal.greenbay.configurations.TestConfiguration;
import com.bafbal.greenbay.dtos.CreateItemDTO;
import com.bafbal.greenbay.dtos.SellableItemDTO;
import com.bafbal.greenbay.dtos.SoldItemDTO;
import com.bafbal.greenbay.exceptions.BidTooLowException;
import com.bafbal.greenbay.exceptions.InsufficientBalanceException;
import com.bafbal.greenbay.exceptions.ItemAlreadySoldException;
import com.bafbal.greenbay.exceptions.ItemNotFoundException;
import com.bafbal.greenbay.exceptions.ItemPriceNotAcceptableException;
import com.bafbal.greenbay.exceptions.MissingItemDetailException;
import com.bafbal.greenbay.exceptions.UrlNotValidException;
import com.bafbal.greenbay.models.Item;
import com.bafbal.greenbay.models.User;
import com.bafbal.greenbay.repositories.ItemRepository;
import com.bafbal.greenbay.repositories.UserRepository;
import com.bafbal.greenbay.security.MyUserDetailsService;
import com.bafbal.greenbay.services.ItemService;
import com.bafbal.greenbay.services.ItemServiceImpl;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestConfiguration.class)
public class ItemServiceTest {

  @Autowired
  private BeanFactory beanFactory;

  ItemRepository itemRepository = Mockito.mock(ItemRepository.class);
  UserRepository userRepository = Mockito.mock(UserRepository.class);
  ModelMapper modelMapper = Mockito.mock(ModelMapper.class);
  MyUserDetailsService myUserDetailsService = Mockito.mock(MyUserDetailsService.class);
  ItemService itemService = new ItemServiceImpl(itemRepository, userRepository, modelMapper, myUserDetailsService);

  private CreateItemDTO okCreateItemDTO;
  private CreateItemDTO createItemDTOWithNullItemName;
  private CreateItemDTO createItemDTOWithNullDescription;
  private CreateItemDTO createItemDTOWithNullPhotoUrl;
  private CreateItemDTO createItemDTOWithNullStartPrice;
  private CreateItemDTO createItemDTOWithNullPurchasePrice;
  private CreateItemDTO createItemDTOWithNegativeStartPrice;
  private CreateItemDTO createItemDTOWithNegativePurchasePrice;
  private CreateItemDTO createItemDTOWithNotWholeStartPrice;
  private CreateItemDTO createItemDTOWithNotWholePurchasePrice;
  private CreateItemDTO createItemDTOWithInvalidPhotoUrl;
  private Item sellableItem;
  private Item soldItem;
  private Item sellableItemWithBid;

  @BeforeEach
  public void getModels() {
    okCreateItemDTO = beanFactory.getBean("okCreateItemDTO", CreateItemDTO.class);
    createItemDTOWithNullItemName = beanFactory.getBean("createItemDTOWithNullItemName", CreateItemDTO.class);
    createItemDTOWithNullDescription = beanFactory.getBean("createItemDTOWithNullDescription", CreateItemDTO.class);
    createItemDTOWithNullPhotoUrl = beanFactory.getBean("createItemDTOWithNullPhotoUrl", CreateItemDTO.class);
    createItemDTOWithNullStartPrice = beanFactory.getBean("createItemDTOWithNullStartPrice", CreateItemDTO.class);
    createItemDTOWithNullPurchasePrice = beanFactory.getBean("createItemDTOWithNullPurchasePrice", CreateItemDTO.class);
    createItemDTOWithNegativeStartPrice = beanFactory.getBean("createItemDTOWithNegativeStartPrice", CreateItemDTO.class);
    createItemDTOWithNegativePurchasePrice = beanFactory.getBean("createItemDTOWithNegativePurchasePrice", CreateItemDTO.class);
    createItemDTOWithNotWholeStartPrice = beanFactory.getBean("createItemDTOWithNotWholeStartPrice", CreateItemDTO.class);
    createItemDTOWithNotWholePurchasePrice = beanFactory.getBean("createItemDTOWithNotWholePurchasePrice", CreateItemDTO.class);
    createItemDTOWithInvalidPhotoUrl = beanFactory.getBean("createItemDTOWithInvalidPhotoUrl", CreateItemDTO.class);
    sellableItem = beanFactory.getBean("sellableItem", Item.class);
    soldItem = beanFactory.getBean("soldItem", Item.class);
    sellableItemWithBid = beanFactory.getBean("sellableItemWithBid", Item.class);
  }

  @Test
  public void validateCreateItemDTO_ifValidDataGiven_ThrowsNoException() {
    Assertions.assertDoesNotThrow(() -> itemService.validateCreateItemDTO(okCreateItemDTO));
  }

  @Test
  public void validateCreateItemDTO_ifNullItemNameGiven_ThrowsException() {
    Throwable exception = Assertions.assertThrows(MissingItemDetailException.class,
        () -> itemService.validateCreateItemDTO(createItemDTOWithNullItemName));
    Assertions.assertEquals("Item name is missing.", exception.getMessage());
  }

  @Test
  public void validateCreateItemDTO_ifNullDescriptionGiven_ThrowsException() {
    Throwable exception = Assertions.assertThrows(MissingItemDetailException.class,
        () -> itemService.validateCreateItemDTO(createItemDTOWithNullDescription));
    Assertions.assertEquals("Item description is missing.", exception.getMessage());
  }

  @Test
  public void validateCreateItemDTO_ifNullPhotoUrlGiven_ThrowsException() {
    Throwable exception = Assertions.assertThrows(MissingItemDetailException.class,
        () -> itemService.validateCreateItemDTO(createItemDTOWithNullPhotoUrl));
    Assertions.assertEquals("Item picture url is missing.", exception.getMessage());
  }

  @Test
  public void validateCreateItemDTO_ifNullStartPriceGiven_ThrowsException() {
    Throwable exception = Assertions.assertThrows(MissingItemDetailException.class,
        () -> itemService.validateCreateItemDTO(createItemDTOWithNullStartPrice));
    Assertions.assertEquals("Item start price is missing.", exception.getMessage());
  }

  @Test
  public void validateCreateItemDTO_ifNullPurchasePriceGiven_ThrowsException() {
    Throwable exception = Assertions.assertThrows(MissingItemDetailException.class,
        () -> itemService.validateCreateItemDTO(createItemDTOWithNullPurchasePrice));
    Assertions.assertEquals("Item purchase price is missing.", exception.getMessage());
  }

  @Test
  public void validateCreateItemDTO_ifNegativeStartPriceGiven_ThrowsException() {
    Throwable exception = Assertions.assertThrows(ItemPriceNotAcceptableException.class,
        () -> itemService.validateCreateItemDTO(createItemDTOWithNegativeStartPrice));
    Assertions.assertEquals("Price cannot be negative.", exception.getMessage());
  }

  @Test
  public void validateCreateItemDTO_ifNegativePurchasePriceGiven_ThrowsException() {
    Throwable exception = Assertions.assertThrows(ItemPriceNotAcceptableException.class,
        () -> itemService.validateCreateItemDTO(createItemDTOWithNegativePurchasePrice));
    Assertions.assertEquals("Price cannot be negative.", exception.getMessage());
  }

  @Test
  public void validateCreateItemDTO_ifNotWholeStartPriceGiven_ThrowsException() {
    Throwable exception = Assertions.assertThrows(ItemPriceNotAcceptableException.class,
        () -> itemService.validateCreateItemDTO(createItemDTOWithNotWholeStartPrice));
    Assertions.assertEquals("Price must be a whole number.", exception.getMessage());
  }

  @Test
  public void validateCreateItemDTO_ifNotWholePurchasePriceGiven_ThrowsException() {
    Throwable exception = Assertions.assertThrows(ItemPriceNotAcceptableException.class,
        () -> itemService.validateCreateItemDTO(createItemDTOWithNotWholePurchasePrice));
    Assertions.assertEquals("Price must be a whole number.", exception.getMessage());
  }

  @Test
  public void validateCreateItemDTO_ifInvalidPhotoUrlGiven_ThrowsException() {
    Throwable exception = Assertions.assertThrows(UrlNotValidException.class,
        () -> itemService.validateCreateItemDTO(createItemDTOWithInvalidPhotoUrl));
    Assertions.assertEquals("Url is not valid.", exception.getMessage());
  }

  @Test
  public void getItemDTO_IfItemIsSellable_ReturnsProperDTO() {
    Mockito.when(modelMapper.map(sellableItem, SellableItemDTO.class))
        .thenReturn(new SellableItemDTO("game", "for kids", "https://www.linkedin.com/notifications/", 5l, 10l, 1l));

    SellableItemDTO sellableItemDTO = (SellableItemDTO) itemService.getItemDTO(sellableItem);

    Assertions.assertEquals("game", sellableItemDTO.getItemName());
    Assertions.assertEquals("for kids", sellableItemDTO.getDescription());
    Assertions.assertEquals("https://www.linkedin.com/notifications/", sellableItemDTO.getPhotoUrl());
    Assertions.assertEquals(5l, sellableItemDTO.getStartPrice());
    Assertions.assertEquals(10l, sellableItemDTO.getPurchasePrice());
    Assertions.assertEquals(1l, sellableItemDTO.getSellerId());
  }

  @Test
  public void getItemDTO_IfItemIsSold_ReturnsProperDTO() {
    Mockito.when(modelMapper.map(soldItem, SoldItemDTO.class))
        .thenReturn(new SoldItemDTO("game", "for kids", "https://www.linkedin.com/notifications/", 5l, 10l, 1l, 1l));

    SoldItemDTO soldItemDTO = (SoldItemDTO) itemService.getItemDTO(soldItem);

    Assertions.assertEquals("game", soldItemDTO.getItemName());
    Assertions.assertEquals("for kids", soldItemDTO.getDescription());
    Assertions.assertEquals("https://www.linkedin.com/notifications/", soldItemDTO.getPhotoUrl());
    Assertions.assertEquals(5l, soldItemDTO.getStartPrice());
    Assertions.assertEquals(10l, soldItemDTO.getPurchasePrice());
    Assertions.assertEquals(1l, soldItemDTO.getSellerId());
    Assertions.assertEquals(1l, soldItemDTO.getBuyerId());
  }


}