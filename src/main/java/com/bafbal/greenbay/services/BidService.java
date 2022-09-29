package com.bafbal.greenbay.services;

import com.bafbal.greenbay.models.Bid;

public interface BidService {

  Bid getBidFromRequest(Long itemId, Long bidToBePlaced);

  void placeBid(Bid bid);
}
