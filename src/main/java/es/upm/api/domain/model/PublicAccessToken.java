package es.upm.api.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublicAccessToken {
    private UUID id;
    private String token;
    private TokenPurpose purpose;
    private LocalDateTime expiresAt;
    private Integer maxUses;
    private Integer usedCount;
    private Boolean isActive;
    private UUID engagementLetterId;
    private UUID customerId;
}
