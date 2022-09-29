package com.bafbal.greenbay.integration;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bafbal.greenbay.models.Item;
import com.bafbal.greenbay.models.User;
import com.bafbal.greenbay.repositories.ItemRepository;
import com.bafbal.greenbay.repositories.UserRepository;
import com.bafbal.greenbay.security.GreenBayUserDetails;
import com.bafbal.greenbay.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@ContextConfiguration
@AutoConfigureMockMvc
public class ItemControllerTest {

  @Autowired
  private BeanFactory beanFactory;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ItemRepository itemRepository;

  @Autowired
  private JwtUtil jwtUtil;

  private String token;
  private Item soldItem;

  @BeforeEach
  public void prepareDbAndToken() {
    itemRepository.deleteAll();
    userRepository.deleteAll();
    User user1 = new User("foo", "bar");
    User user2 = new User("John", "Doe");
    userRepository.save(user1);
    userRepository.save(user2);
    itemRepository.save(new Item("first item", "first item description", "https://www.firstitem.com", 1l, 2l, user1));
    itemRepository.save(new Item("second item", "second item description", "https://www.seconditem.com", 2l, 4l, user1));
    itemRepository.save(new Item("third item", "third item description", "https://www.thirditem.com", 3l, 6l, user1));
    itemRepository.save(new Item("fourth item", "fourth item description", "https://www.fourthitem.com", 4l, 8l, user1));
    Item item = new Item("game", "for kids", "https://www.linkedin.com/notifications/", 5l, 10l,user1);
    item.setBuyer(user2);
    itemRepository.save(item);

//    itemRepository.save(new Item("first item", "first item description", "https://www.firstitem.com", 1l, 2l,user));
//    itemRepository.save(new Item("first item", "first item description", "https://www.firstitem.com", 1l, 2l,user));
//    itemRepository.save(new Item("first item", "first item description", "https://www.firstitem.com", 1l, 2l,user));
    GreenBayUserDetails userDetails = new GreenBayUserDetails(userRepository.findByUsername("foo").get());
    SecurityContextHolder.getContext()
        .setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
    token = jwtUtil.generateToken(userDetails);
  }

  @Test
  void createItem_WhenValidItemGiven_CreatesItem() throws Exception {
    String itemName = "game";
    String description = "for kids";
    String photoUrl = "https://www.linkedin.com/notifications/";
    String startPrice = "5.0";
    String purchasePrice = "10.0";
    String body = "{\"itemName\": \"" + itemName + "\",\"description\": \"" + description + "\", \"photoUrl\": \"" + photoUrl
        + "\",\"startPrice\": \"" + startPrice + "\",\"purchasePrice\": \"" + purchasePrice + "\"}";

    mockMvc.perform(MockMvcRequestBuilders.post("/create")
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(jsonPath("itemName", is("game")))
        .andExpect(jsonPath("description", is("for kids")))
        .andExpect(jsonPath("photoUrl", is("https://www.linkedin.com/notifications/")))
        .andExpect(jsonPath("startPrice", is(5)))
        .andExpect(jsonPath("purchasePrice", is(10)))
        .andExpect(status().isCreated());
  }

  @Test
  void createItem_WhenItemWithoutItemNameGiven_DisplaysErrorMessage() throws Exception {
    String description = "for kids";
    String photoUrl = "https://www.linkedin.com/notifications/";
    String startPrice = "5.0";
    String purchasePrice = "10.0";
    String body = "{\"description\": \"" + description + "\", \"photoUrl\": \"" + photoUrl + "\",\"startPrice\": \"" + startPrice
        + "\",\"purchasePrice\": \"" + purchasePrice + "\"}";

    mockMvc.perform(MockMvcRequestBuilders.post("/create")
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(jsonPath("message", is("Item name is missing.")))
        .andExpect(status().isBadRequest());
  }

  @Test
  void createItem_WhenItemWithoutDescriptionGiven_DisplaysErrorMessage() throws Exception {
    String itemName = "game";
    String photoUrl = "https://www.linkedin.com/notifications/";
    String startPrice = "5.0";
    String purchasePrice = "10.0";
    String body = "{\"itemName\": \"" + itemName + "\", \"photoUrl\": \"" + photoUrl + "\",\"startPrice\": \"" + startPrice
        + "\",\"purchasePrice\": \"" + purchasePrice + "\"}";

    mockMvc.perform(MockMvcRequestBuilders.post("/create")
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(jsonPath("message", is("Item description is missing.")))
        .andExpect(status().isBadRequest());
  }

  @Test
  void createItem_WhenItemWithoutPhotoUrlGiven_DisplaysErrorMessage() throws Exception {
    String itemName = "game";
    String description = "for kids";
    String startPrice = "5.0";
    String purchasePrice = "10.0";
    String body = "{\"itemName\": \"" + itemName + "\",\"description\": \"" + description + "\", \"startPrice\": \"" + startPrice
        + "\",\"purchasePrice\": \"" + purchasePrice + "\"}";

    mockMvc.perform(MockMvcRequestBuilders.post("/create")
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(jsonPath("message", is("Item picture url is missing.")))
        .andExpect(status().isBadRequest());
  }

  @Test
  void createItem_WhenItemWithoutStartPriceGiven_DisplaysErrorMessage() throws Exception {
    String itemName = "game";
    String description = "for kids";
    String photoUrl = "https://www.linkedin.com/notifications/";
    String purchasePrice = "10.0";
    String body = "{\"itemName\": \"" + itemName + "\",\"description\": \"" + description + "\", \"photoUrl\": \"" + photoUrl
        + "\" ,\"purchasePrice\": \"" + purchasePrice + "\"}";

    mockMvc.perform(MockMvcRequestBuilders.post("/create")
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(jsonPath("message", is("Item start price is missing.")))
        .andExpect(status().isBadRequest());
  }

  @Test
  void createItem_WhenItemWithoutPurchasePriceGiven_DisplaysErrorMessage() throws Exception {
    String itemName = "game";
    String description = "for kids";
    String photoUrl = "https://www.linkedin.com/notifications/";
    String startPrice = "5.0";
    String body = "{\"itemName\": \"" + itemName + "\",\"description\": \"" + description + "\", \"photoUrl\": \"" + photoUrl
        + "\",\"startPrice\": \"" + startPrice + "\"}";

    mockMvc.perform(MockMvcRequestBuilders.post("/create")
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(jsonPath("message", is("Item purchase price is missing.")))
        .andExpect(status().isBadRequest());
  }

  @Test
  void createItem_WhenNegativePriceGiven_DisplaysErrorMessage() throws Exception {
    String itemName = "game";
    String description = "for kids";
    String photoUrl = "https://www.linkedin.com/notifications/";
    String startPrice = "-5.0";
    String purchasePrice = "10.0";
    String body = "{\"itemName\": \"" + itemName + "\",\"description\": \"" + description + "\", \"photoUrl\": \"" + photoUrl
        + "\",\"startPrice\": \"" + startPrice + "\",\"purchasePrice\": \"" + purchasePrice + "\"}";

    mockMvc.perform(MockMvcRequestBuilders.post("/create")
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(jsonPath("message", is("Price cannot be negative.")))
        .andExpect(status().isBadRequest());
  }

  @Test
  void createItem_WhenNotWholeNumberAsPriceGiven_CreatesItem() throws Exception {
    String itemName = "game";
    String description = "for kids";
    String photoUrl = "https://www.linkedin.com/notifications/";
    String startPrice = "5.5";
    String purchasePrice = "10.0";
    String body = "{\"itemName\": \"" + itemName + "\",\"description\": \"" + description + "\", \"photoUrl\": \"" + photoUrl
        + "\",\"startPrice\": \"" + startPrice + "\",\"purchasePrice\": \"" + purchasePrice + "\"}";

    mockMvc.perform(MockMvcRequestBuilders.post("/create")
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(jsonPath("message", is("Price must be a whole number.")))
        .andExpect(status().isBadRequest());
  }

  @Test
  void createItem_WhenInvalidUrlGiven_CreatesItem() throws Exception {
    String itemName = "game";
    String description = "for kids";
    String photoUrl = "ht://www.linkedin.com/notifications/";
    String startPrice = "5.0";
    String purchasePrice = "10.0";
    String body = "{\"itemName\": \"" + itemName + "\",\"description\": \"" + description + "\", \"photoUrl\": \"" + photoUrl
        + "\",\"startPrice\": \"" + startPrice + "\",\"purchasePrice\": \"" + purchasePrice + "\"}";

    mockMvc.perform(MockMvcRequestBuilders.post("/create")
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(jsonPath("message", is("Url is not valid.")))
        .andExpect(status().isBadRequest());
  }

  @Test
  void listSellableItems_WhenNoPageNumberGiven_FirstTwoItemsDisplayed() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/list")
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(200))
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].itemName", is("first item")))
        .andExpect(jsonPath("$[0].photoUrl", is("https://www.firstitem.com")))
        .andExpect(jsonPath("$[0].lastBid", equalTo(null)))
        .andExpect(jsonPath("$[1].itemName", is("second item")))
        .andExpect(jsonPath("$[1].photoUrl", is("https://www.seconditem.com")))
        .andExpect(jsonPath("$[1].lastBid", equalTo(null)))
        .andExpect(status().is(200));
  }

  @Test
  void listSellableItems_WhenOneAsPageNumberGiven_FirstTwoItemsDisplayed() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/list/1")
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(200))
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].itemName", is("first item")))
        .andExpect(jsonPath("$[0].photoUrl", is("https://www.firstitem.com")))
        .andExpect(jsonPath("$[0].lastBid", equalTo(null)))
        .andExpect(jsonPath("$[1].itemName", is("second item")))
        .andExpect(jsonPath("$[1].photoUrl", is("https://www.seconditem.com")))
        .andExpect(jsonPath("$[1].lastBid", equalTo(null)))
        .andExpect(status().is(200));
  }

  @Test
  void listSellableItems_WhenTwoAsPageNumberGiven_ThirdAndFourthItemsDisplayed() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/list/2")
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(200))
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].itemName", is("third item")))
        .andExpect(jsonPath("$[0].photoUrl", is("https://www.thirditem.com")))
        .andExpect(jsonPath("$[0].lastBid", equalTo(null)))
        .andExpect(jsonPath("$[1].itemName", is("fourth item")))
        .andExpect(jsonPath("$[1].photoUrl", is("https://www.fourthitem.com")))
        .andExpect(jsonPath("$[1].lastBid", equalTo(null)))
        .andExpect(status().is(200));
  }

  @Test
  void listSellableItems_WhenZeroAsPageNumberGiven_ErrorMessageDisplayed() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/list/0")
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(400))
        .andExpect(jsonPath("message", is("Page number must be higher than zero.")));
  }

  @Test
  void listSellableItems_WhenNotItemAvailable_EmptyListReturned() throws Exception {
    itemRepository.deleteAll();
    mockMvc.perform(MockMvcRequestBuilders.get("/list")
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(200))
        .andExpect(jsonPath("$", hasSize(0)));
  }

  @Test
  void viewItem_WhenNotExistingIdIsGiven_ErrorMessageDisplayed() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/view/4008")
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(400))
        .andExpect(jsonPath("message", is("Item not found.")));
  }

  @Test
  void viewItem_WhenIdOfSellableItemIsGiven_ItemDetailsDisplayed() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/view/" + itemRepository.findByItemName("first item").get().getId())
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(200))
        .andExpect(jsonPath("itemName", is("first item")))
        .andExpect(jsonPath("description", is("first item description")))
        .andExpect(jsonPath("photoUrl", is("https://www.firstitem.com")))
        .andExpect(jsonPath("startPrice", is(1)))
        .andExpect(jsonPath("purchasePrice", is(2)))
        .andExpect(jsonPath("sellerId", is(userRepository.findByUsername("foo").get().getId().intValue())));
  }

  @Test
  void viewItem_WhenIdOfSoldItemIsGiven_ItemDetailsDisplayed() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/view/" + itemRepository.findByItemName("game").get().getId())
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(200))
        .andExpect(jsonPath("itemName", is("game")))
        .andExpect(jsonPath("description", is("for kids")))
        .andExpect(jsonPath("photoUrl", is("https://www.linkedin.com/notifications/")))
        .andExpect(jsonPath("startPrice", is(5)))
        .andExpect(jsonPath("purchasePrice", is(10)))
        .andExpect(jsonPath("sellerId", is(userRepository.findByUsername("foo").get().getId().intValue())))
        .andExpect(jsonPath("buyerId", is(userRepository.findByUsername("John").get().getId().intValue())));
  }
}