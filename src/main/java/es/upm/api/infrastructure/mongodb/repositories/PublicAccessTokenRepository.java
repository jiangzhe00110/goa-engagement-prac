package es.upm.api.infrastructure.mongodb.repositories;

import es.upm.api.infrastructure.mongodb.entities.PublicAccessTokenEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface PublicAccessTokenRepository extends MongoRepository<PublicAccessTokenEntity, UUID> {
}
