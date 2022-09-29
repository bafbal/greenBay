package com.bafbal.greenbay.services;

import com.bafbal.greenbay.exceptions.BidTooLowException;
import com.bafbal.greenbay.exceptions.InsufficientBalanceException;
import com.bafbal.greenbay.exceptions.ItemAlreadySoldException;
import com.bafbal.greenbay.exceptions.ItemNotFoundException;
import com.bafbal.greenbay.models.Bid;
import com.bafbal.greenbay.models.Item;
import com.bafbal.greenbay.repositories.BidRepository;
import com.bafbal.greenbay.security.MyUserDetailsService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BidServiceImpl implements BidService {

  private ItemService itemService;
  private MyUserDetailsService myUserDetailsService;
  private BidRepository bidRepository;

  @Autowired
  public BidServiceImpl(ItemService itemService, MyUserDetailsService myUserDetailsService,
      BidRepository bidRepository) {
    this.itemService = itemService;
    this.myUserDetailsService = myUserDetailsService;
    this.bidRepository = bidRepository;
  }

  @Override
  public Bid getBidFromRequest(Long itemId, Long bidToBePlaced) {
    Optional<Item> optionalItem = itemService.getItem(itemId);
    validateBidRequest(optionalItem, bidToBePlaced);
    return new Bid(myUserDetailsService.getLoggedInUser(), bidToBePlaced, optionalItem.get());
  }

  private Item validateBidRequest(Optional<Item> optionalItem, Long bidToBePlaced) {
    if (optionalItem.isEmpty()) {
      throw new ItemNotFoundException(("Item not found."));
    }
    Item item = optionalItem.get();
    if (item.getBuyer() != null) {
      throw new ItemAlreadySoldException("Item already sold.");
    }
    if (myUserDetailsService.getLoggedInUser().getBalance() < bidToBePlaced) {
      throw new InsufficientBalanceException("User balance is insufficient to place such bid.");
    }
    if (bidToBePlaced < item.getStartPrice() || item.getLastBid() != null && !(bidToBePlaced > item.getLastBid().getPrice())) {
      throw new BidTooLowException("Bid is too low");
    }
    return item;
  }

  @Override
  public void placeBid(Bid bid) {
    bidRepository.save(bid);
    assignBuyer(bid);
  }

  private void assignBuyer(Bid bid) {
    Item item = bid.getItem();
    if (!(item.getPurchasePrice() > bid.getPrice())) {
      item.setBuyer(myUserDetailsService.getLoggedInUser());
      itemService.save(item);
    }
  }
}
