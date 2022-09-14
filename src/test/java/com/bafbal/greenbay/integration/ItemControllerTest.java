package com.bafbal.greenbay.integration;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bafbal.greenbay.models.User;
import com.bafbal.greenbay.repositories.ItemRepository;
import com.bafbal.greenbay.repositories.UserRepository;
import com.bafbal.greenbay.security.GreenBayUserDetails;
import com.bafbal.greenbay.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
  private MockMvc mockMvc;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ItemRepository itemRepository;

  @Autowired
  private JwtUtil jwtUtil;

  private String token;

  @BeforeEach
  public void prepareDbAndToken() {
    itemRepository.deleteAll();
    userRepository.deleteAll();
    userRepository.save(new User("foo", "bar"));
    GreenBayUserDetails userDetails = new GreenBayUserDetails(userRepository.findByUsername("foo").get());
    SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
    token = jwtUtil.generateToken(userDetails);
  }

  @Test
  void createItem_WhenValidItemGiven_CreatesItem() throws Exception {
    String itemName = "game";
    String description = "for kids";
    String photoUrl = "https://www.linkedin.com/notifications/";
    String startPrice = "5.0";
    String purchasePrice = "10.0";
    String body = "{\"itemName\": \"" + itemName + "\",\"description\": \"" + description + "\", \"photoUrl\": \"" + photoUrl + "\",\"startPrice\": \"" + startPrice + "\",\"purchasePrice\": \"" + purchasePrice + "\"}";

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
    String body = "{\"description\": \"" + description + "\", \"photoUrl\": \"" + photoUrl + "\",\"startPrice\": \"" + startPrice + "\",\"purchasePrice\": \"" + purchasePrice + "\"}";

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
    String body = "{\"itemName\": \"" + itemName + "\", \"photoUrl\": \"" + photoUrl + "\",\"startPrice\": \"" + startPrice + "\",\"purchasePrice\": \"" + purchasePrice + "\"}";

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
    String body = "{\"itemName\": \"" + itemName + "\",\"description\": \"" + description + "\", \"startPrice\": \"" + startPrice + "\",\"purchasePrice\": \"" + purchasePrice + "\"}";

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
    String body = "{\"itemName\": \"" + itemName + "\",\"description\": \"" + description + "\", \"photoUrl\": \"" + photoUrl + "\" ,\"purchasePrice\": \"" + purchasePrice + "\"}";

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
    String body = "{\"itemName\": \"" + itemName + "\",\"description\": \"" + description + "\", \"photoUrl\": \"" + photoUrl + "\",\"startPrice\": \"" + startPrice + "\"}";

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
    String body = "{\"itemName\": \"" + itemName + "\",\"description\": \"" + description + "\", \"photoUrl\": \"" + photoUrl + "\",\"startPrice\": \"" + startPrice + "\",\"purchasePrice\": \"" + purchasePrice + "\"}";

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
    String body = "{\"itemName\": \"" + itemName + "\",\"description\": \"" + description + "\", \"photoUrl\": \"" + photoUrl + "\",\"startPrice\": \"" + startPrice + "\",\"purchasePrice\": \"" + purchasePrice + "\"}";

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
    String body = "{\"itemName\": \"" + itemName + "\",\"description\": \"" + description + "\", \"photoUrl\": \"" + photoUrl + "\",\"startPrice\": \"" + startPrice + "\",\"purchasePrice\": \"" + purchasePrice + "\"}";

    mockMvc.perform(MockMvcRequestBuilders.post("/create")
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(jsonPath("message", is("Url is not valid.")))
        .andExpect(status().isBadRequest());
  }
}
