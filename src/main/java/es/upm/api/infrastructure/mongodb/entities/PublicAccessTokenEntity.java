package es.upm.api.infrastructure.mongodb.entities;

import es.upm.api.domain.model.PublicAccessToken;
import es.upm.api.domain.model.TokenPurpose;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class PublicAccessTokenEntity {
    @Id
    private UUID id;
    @Indexed(unique = true)
    private String token;
    private TokenPurpose purpose;
    private LocalDateTime expiresAt;
    private Integer maxUses;
    private Integer usedCount;
    private Boolean isActive;
    private UUID engagementLetterId;
    private UUID customerId;

    public PublicAccessTokenEntity(PublicAccessToken publicAccessToken) {
        BeanUtils.copyProperties(publicAccessToken, this);
    }

    public PublicAccessToken toPublicAccessToken() {
        PublicAccessToken publicAccessToken = new PublicAccessToken();
        BeanUtils.copyProperties(this, publicAccessToken);
        return publicAccessToken;
    }
}
