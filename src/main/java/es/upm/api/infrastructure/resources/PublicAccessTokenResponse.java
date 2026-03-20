package es.upm.api.infrastructure.resources;

import es.upm.api.domain.model.PublicAccessToken;
import es.upm.api.domain.model.TokenPurpose;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PublicAccessTokenResponse {
    private static final String PUBLIC_ACCESS_PATH_TEMPLATE = "/public/engagement-letters/access?token=%s";

    private final String token;
    private final TokenPurpose purpose;
    private final LocalDateTime expiresAt;
    private final Integer maxUses;
    private final Integer usedCount;
    private final Boolean isActive;
    private final String publicUrl;

    public PublicAccessTokenResponse(PublicAccessToken publicAccessToken) {
        this.token = publicAccessToken.getToken();
        this.purpose = publicAccessToken.getPurpose();
        this.expiresAt = publicAccessToken.getExpiresAt();
        this.maxUses = publicAccessToken.getMaxUses();
        this.usedCount = publicAccessToken.getUsedCount();
        this.isActive = publicAccessToken.getIsActive();
        this.publicUrl = PUBLIC_ACCESS_PATH_TEMPLATE.formatted(publicAccessToken.getToken());
    }
}
