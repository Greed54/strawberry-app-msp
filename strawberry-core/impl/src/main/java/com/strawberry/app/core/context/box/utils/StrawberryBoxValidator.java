package com.strawberry.app.core.context.box.utils;

import static com.strawberry.app.common.ValidationUtils.validateCollectionNotEmpty;

import com.strawberry.app.common.context.ValidationResult;
import com.strawberry.app.common.property.context.identity.card.CardId;
import com.strawberry.app.core.context.employee.service.StrawberryEmployeeService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class StrawberryBoxValidator {

  StrawberryEmployeeService employeeService;

  public ValidationResult validateCardId(CardId cardId) {
    return validateCollectionNotEmpty(employeeService.getEmployeesByCardId(cardId), "Employees by " + cardId.toString() + " ");
  }
}
