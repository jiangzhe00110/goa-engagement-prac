package es.upm.api.infrastructure.resources;

import es.upm.api.domain.model.EngagementLetter;
import es.upm.api.domain.model.EngagementLetterFindCriteria;
import es.upm.api.domain.services.EngagementLetterService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@PreAuthorize(Security.ADMIN_MANAGER_OPERATOR)
@RequestMapping(EngagementLetterResource.ENGAGEMENT_LETTER)
public class EngagementLetterResource {
    public static final String ENGAGEMENT_LETTER = "/engagement-letters";
    public static final String ID_ID = "/{id}";
    public static final String PUBLIC_ACCESS_TOKEN = "/public-access-token";

    private final EngagementLetterService engagementLetterService;

    @Autowired
    public EngagementLetterResource(EngagementLetterService engagementLetterService) {
        this.engagementLetterService = engagementLetterService;
    }

    @GetMapping
    public List<EngagementLetter> findNullSafe(@ModelAttribute EngagementLetterFindCriteria criteria) {
        return this.engagementLetterService.findNullSafe(criteria).toList();
    }

    @PostMapping
    public void create(@Valid @RequestBody EngagementLetter engagementLetter) {
        this.engagementLetterService.create(engagementLetter);
    }

    @GetMapping(ID_ID)
    public EngagementLetter read(@PathVariable UUID id) {
        return this.engagementLetterService.readById(id);
    }

    @PutMapping(ID_ID)
    public void update(@PathVariable UUID id, @Valid @RequestBody EngagementLetter engagementLetter) {
        this.engagementLetterService.update(id, engagementLetter);
    }

    @PostMapping(ID_ID + PUBLIC_ACCESS_TOKEN)
    public PublicAccessTokenResponse createPublicAccessToken(@PathVariable UUID id) {
        return new PublicAccessTokenResponse(this.engagementLetterService.createPublicAccessToken(id));
    }

    @DeleteMapping(ID_ID)
    public void delete(@PathVariable UUID id) {
        this.engagementLetterService.delete(id);
    }

}

