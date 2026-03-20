package es.upm.api.domain.persistence;

import es.upm.api.domain.model.PublicAccessToken;
import org.springframework.stereotype.Repository;

@Repository
public interface PublicAccessTokenPersistence {
    PublicAccessToken create(PublicAccessToken publicAccessToken);
}
