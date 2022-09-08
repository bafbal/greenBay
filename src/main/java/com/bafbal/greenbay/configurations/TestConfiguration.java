package com.bafbal.greenbay.configurations;

import com.bafbal.greenbay.dtos.CreateItemDTO;
import com.bafbal.greenbay.dtos.UsernamePasswordDTO;
import com.bafbal.greenbay.models.User;
import com.bafbal.greenbay.security.GreenBayUserDetails;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class TestConfiguration {

  @Bean(name = "usernamePasswordDtoWithNullUsername")
  @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  public UsernamePasswordDTO getUsernamePasswordDtoWithNullUsername() {
    return new UsernamePasswordDTO(null, "bar");
  }

  @Bean(name = "usernamePasswordDtoWithBlankUsername")
  @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  public UsernamePasswordDTO getUsernamePasswordDtoWithBlankUsername() {
    return new UsernamePasswordDTO("   ", "bar");
  }

  @Bean(name = "usernamePasswordDtoWithNullPassword")
  @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  public UsernamePasswordDTO getUsernamePasswordDtoWithNullPassword() {
    return new UsernamePasswordDTO("foo", null);
  }

  @Bean(name = "usernamePasswordDtoWithBlankPassword")
  @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  public UsernamePasswordDTO getUsernamePasswordDtoWithBlankPassword() {
    return new UsernamePasswordDTO("foo", "    ");
  }

  @Bean(name = "usernamePasswordDtoWithBlankUsernameAndPassword")
  @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  public UsernamePasswordDTO getUsernamePasswordDtoWithBlankUsernameAndPassword() {
    return new UsernamePasswordDTO("     ", "    ");
  }

  @Bean(name = "usernamePasswordDtoWithNullUsernameAndPassword")
  @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  public UsernamePasswordDTO getUsernamePasswordDtoWithNullUsernameAndPassword() {
    return new UsernamePasswordDTO(null, null);
  }

  @Bean(name = "fooBarUsernamePasswordDTO")
  @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  public UsernamePasswordDTO getFooBarUsernamePasswordDTO() {
    return new UsernamePasswordDTO("foo", "bar");
  }

  @Bean(name = "fooBarGreenBayUserDetails")
  @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  public GreenBayUserDetails getFooBarGreenBayUserDetails() {
    return new GreenBayUserDetails(new User("foo", "bar"));
  }

  @Bean(name = "okCreateItemDTO")
  @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  public CreateItemDTO getOkCreateItemDTO() {
    return new CreateItemDTO("game", "for kids", "https://www.linkedin.com/notifications/", 5.0, 10.0);
  }

  @Bean(name = "createItemDTOWithNullItemName")
  @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  public CreateItemDTO getCreateItemDTOWithNullItemName() {
    return new CreateItemDTO(null, "for kids", "https://www.linkedin.com/notifications/", 5.0, 10.0);
  }

  @Bean(name = "createItemDTOWithNullDescription")
  @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  public CreateItemDTO getCreateItemDTOWithNullDescription() {
    return new CreateItemDTO("game", null, "https://www.linkedin.com/notifications/", 5.0, 10.0);
  }

  @Bean(name = "createItemDTOWithNullPhotoUrl")
  @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  public CreateItemDTO getCreateItemDTOWithNullPhotoUrl() {
    return new CreateItemDTO("game", "for kids", null, 5.0, 10.0);
  }

  @Bean(name = "createItemDTOWithNullStartPrice")
  @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  public CreateItemDTO getCreateItemDTOWithNullStartPrice() {
    return new CreateItemDTO("game", "for kids", "https://www.linkedin.com/notifications/", null, 10.0);
  }

  @Bean(name = "createItemDTOWithNullPurchasePrice")
  @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  public CreateItemDTO getCreateItemDTOWithNullPurchasePrice() {
    return new CreateItemDTO("game", "for kids", "https://www.linkedin.com/notifications/", 5.0, null);
  }

  @Bean(name = "createItemDTOWithNegativeStartPrice")
  @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  public CreateItemDTO getCreateItemDTOWithNegativeStartPrice() {
    return new CreateItemDTO("game", "for kids", "https://www.linkedin.com/notifications/", -5.0, 10.0);
  }

  @Bean(name = "createItemDTOWithNegativePurchasePrice")
  @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  public CreateItemDTO getCreateItemDTOWithNegativePurchasePrice() {
    return new CreateItemDTO("game", "for kids", "https://www.linkedin.com/notifications/", 5.0, -10.0);
  }

  @Bean(name = "createItemDTOWithNotWholeStartPrice")
  @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  public CreateItemDTO getCreateItemDTOWithNotWholeStartPrice() {
    return new CreateItemDTO("game", "for kids", "https://www.linkedin.com/notifications/", 5.5, 10.0);
  }

  @Bean(name = "createItemDTOWithNotWholePurchasePrice")
  @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  public CreateItemDTO getCreateItemDTOWithNotWholePurchasePrice() {
    return new CreateItemDTO("game", "for kids", "https://www.linkedin.com/notifications/", 5.0, 10.5);
  }

  @Bean(name = "createItemDTOWithInvalidPhotoUrl")
  @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  public CreateItemDTO getCreateItemDTOWithInvalidPhotoUrl() {
    return new CreateItemDTO("game", "for kids", "ht://ww.linkedin.com/notifications/", 5.0, 10.0);
  }

}
