package es.upm.api.infrastructure.mongodb.persistence;

import es.upm.api.domain.model.PublicAccessToken;
import es.upm.api.domain.persistence.PublicAccessTokenPersistence;
import es.upm.api.infrastructure.mongodb.entities.PublicAccessTokenEntity;
import es.upm.api.infrastructure.mongodb.repositories.PublicAccessTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PublicAccessTokenPersistenceMongodb implements PublicAccessTokenPersistence {
    private final PublicAccessTokenRepository publicAccessTokenRepository;

    @Autowired
    public PublicAccessTokenPersistenceMongodb(PublicAccessTokenRepository publicAccessTokenRepository) {
        this.publicAccessTokenRepository = publicAccessTokenRepository;
    }

    @Override
    public PublicAccessToken create(PublicAccessToken publicAccessToken) {
        return this.publicAccessTokenRepository.save(new PublicAccessTokenEntity(publicAccessToken))
                .toPublicAccessToken();
    }
}
