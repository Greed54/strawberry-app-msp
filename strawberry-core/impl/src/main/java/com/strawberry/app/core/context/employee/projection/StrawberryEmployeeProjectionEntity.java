package com.strawberry.app.core.context.employee.projection;

import com.strawberry.app.core.context.cqrscommon.ProjectionEntity;
import java.time.Instant;
import javax.annotation.Nullable;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "StrawberryEmployeeProjectionEvent")
public class StrawberryEmployeeProjectionEntity extends ProjectionEntity {

  String cardId;

  String firstName;

  String lastName;

  @Nullable
  String employeeRole;

  String teamId;

  String note;

  Boolean removed;

  Instant createdAt;

  @Nullable
  String createdBy;

  @Nullable
  Instant modifiedAt;

  @Nullable
  String modifiedBy;

  @Builder(toBuilder = true)
  public StrawberryEmployeeProjectionEntity(String identity, String cardId, String firstName, String lastName, @Nullable String employeeRole,
      String teamId, String note, Boolean removed, Instant createdAt, @Nullable String createdBy, @Nullable Instant modifiedAt,
      @Nullable String modifiedBy) {
    super(identity);
    this.cardId = cardId;
    this.firstName = firstName;
    this.lastName = lastName;
    this.employeeRole = employeeRole;
    this.teamId = teamId;
    this.note = note;
    this.removed = removed;
    this.createdAt = createdAt;
    this.createdBy = createdBy;
    this.modifiedAt = modifiedAt;
    this.modifiedBy = modifiedBy;
  }
}
