package com.bafbal.greenbay.unit;

import com.bafbal.greenbay.configurations.TestConfiguration;
import com.bafbal.greenbay.exceptions.BidTooLowException;
import com.bafbal.greenbay.exceptions.InsufficientBalanceException;
import com.bafbal.greenbay.exceptions.ItemAlreadySoldException;
import com.bafbal.greenbay.exceptions.ItemNotFoundException;
import com.bafbal.greenbay.models.Bid;
import com.bafbal.greenbay.models.Item;
import com.bafbal.greenbay.models.User;
import com.bafbal.greenbay.repositories.BidRepository;
import com.bafbal.greenbay.security.MyUserDetailsService;
import com.bafbal.greenbay.services.BidService;
import com.bafbal.greenbay.services.BidServiceImpl;
import com.bafbal.greenbay.services.ItemService;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestConfiguration.class)
public class BidServiceTest {

  @Autowired
  private BeanFactory beanFactory;

  ItemService itemService = Mockito.mock(ItemService.class);
  MyUserDetailsService myUserDetailsService = Mockito.mock(MyUserDetailsService.class);
  BidRepository bidRepository = Mockito.mock(BidRepository.class);
  BidService bidService = new BidServiceImpl(itemService, myUserDetailsService, bidRepository);

  private Item sellableItem;
  private Item soldItem;
  private Item sellableItemWithBid;
  private User userWithBalance;

  @BeforeEach
  public void getModels() {
    sellableItem = beanFactory.getBean("sellableItem", Item.class);
    soldItem = beanFactory.getBean("soldItem", Item.class);
    sellableItemWithBid = beanFactory.getBean("sellableItemWithBid", Item.class);
    userWithBalance = beanFactory.getBean("userWithBalance", User.class);
  }

  @Test
  public void getBidFromRequest_IfItemWithGivenIdDoesNotExist_ThrowsException() {
    Mockito.when(itemService.getItem(1l)).thenReturn(Optional.empty());

    Throwable exception = Assertions.assertThrows(ItemNotFoundException.class, () -> bidService.getBidFromRequest(1l, 1l));
    Assertions.assertEquals("Item not found.", exception.getMessage());
  }

  @Test
  public void getBidFromRequest_IfItemIsSold_ThrowsException() {
    Mockito.when(itemService.getItem(1l)).thenReturn(Optional.of(soldItem));

    Throwable exception = Assertions.assertThrows(ItemAlreadySoldException.class, () -> bidService.getBidFromRequest(1l, 1l));
    Assertions.assertEquals("Item already sold.", exception.getMessage());
  }

  @Test
  public void getBidFromRequest_IfUserBalanceIsLowerThanBid_ThrowsException() {
    Mockito.when(itemService.getItem(1l)).thenReturn(Optional.of(sellableItem));
    Mockito.when(myUserDetailsService.getLoggedInUser()).thenReturn(new User());

    Throwable exception = Assertions.assertThrows(InsufficientBalanceException.class, () -> bidService.getBidFromRequest(1l, 2l));
    Assertions.assertEquals("User balance is insufficient to place such bid.", exception.getMessage());
  }

  @Test
  public void getBidFromRequest_IfBidIsLowerThanItemStartPrice_ThrowsException() {
    Mockito.when(itemService.getItem(1l)).thenReturn(Optional.of(sellableItem));
    Mockito.when(myUserDetailsService.getLoggedInUser()).thenReturn(userWithBalance);

    Throwable exception = Assertions.assertThrows(BidTooLowException.class, () -> bidService.getBidFromRequest(1l, 2l));
    Assertions.assertEquals("Bid is too low", exception.getMessage());
  }

  @Test
  public void getBidFromRequest_IfBidIsNotHigherThanLastBid_ThrowsException() {
    Mockito.when(itemService.getItem(1l)).thenReturn(Optional.of(sellableItemWithBid));
    Mockito.when(myUserDetailsService.getLoggedInUser()).thenReturn(userWithBalance);

    Throwable exception = Assertions.assertThrows(BidTooLowException.class, () -> bidService.getBidFromRequest(1l, 7l));
    Assertions.assertEquals("Bid is too low", exception.getMessage());
  }

  @Test
  public void getBidFromRequest_IfProperBidRequestIsMade_ReturnsBid() {
    Mockito.when(itemService.getItem(1l)).thenReturn(Optional.of(sellableItemWithBid));
    Mockito.when(myUserDetailsService.getLoggedInUser()).thenReturn(userWithBalance);

    Bid bid = bidService.getBidFromRequest(1l, 9l);
    Assertions.assertEquals(sellableItemWithBid, bid.getItem());
    Assertions.assertEquals(9l, bid.getPrice());
    Assertions.assertEquals(userWithBalance, bid.getUser());
  }

  @Test
  public void placeBid_IfBidIsLowerThanPurchasePrice_SavesBid() {
    Bid bid = new Bid(userWithBalance, 1l, sellableItem);

    bidService.placeBid(bid);

    Mockito.verify(bidRepository, Mockito.times(1)).save(bid);
    Mockito.verify(itemService, Mockito.times(0)).save(sellableItem);
  }

  @Test
  public void placeBid_IfPurchasePriceIsNotHigherThanBid_SavesBidAndItemWithBuyer() {
    Bid bid = new Bid(userWithBalance, sellableItem.getPurchasePrice(), sellableItem);
    Mockito.when(myUserDetailsService.getLoggedInUser()).thenReturn(userWithBalance);

    bidService.placeBid(bid);

    Mockito.verify(bidRepository, Mockito.times(1)).save(bid);
    Mockito.verify(itemService, Mockito.times(1)).save(sellableItem);
    Assertions.assertEquals(userWithBalance, bid.getItem().getBuyer());
  }
}
