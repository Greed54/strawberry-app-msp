package com.strawberry.app.core.context.team.projection;

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


@Getter
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "StrawberryTeamProjectionEvent")
public class StrawberryTeamProjectionEntity extends ProjectionEntity {

  String teamName;

  @Nullable
  String teamLeadId;

  Boolean removed;

  Instant createdAt;

  @Nullable
  String createdBy;

  @Nullable
  Instant modifiedAt;

  @Nullable
  String modifiedBy;

  @Builder(toBuilder = true)
  public StrawberryTeamProjectionEntity(String identity, String teamName, @Nullable String teamLeadId, Boolean removed, Instant createdAt,
      @Nullable String createdBy, @Nullable Instant modifiedAt, @Nullable String modifiedBy) {
    super(identity);
    this.teamName = teamName;
    this.teamLeadId = teamLeadId;
    this.removed = removed;
    this.createdAt = createdAt;
    this.createdBy = createdBy;
    this.modifiedAt = modifiedAt;
    this.modifiedBy = modifiedBy;
  }
}
