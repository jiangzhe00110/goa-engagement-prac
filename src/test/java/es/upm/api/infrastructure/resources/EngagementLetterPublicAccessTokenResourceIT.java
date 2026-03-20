package es.upm.api.infrastructure.resources;

import es.upm.api.domain.model.PublicAccessToken;
import es.upm.api.domain.model.TokenPurpose;
import es.upm.api.domain.services.EngagementLetterService;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class EngagementLetterPublicAccessTokenResourceIT {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EngagementLetterService engagementLetterService;

    @Test
    @WithMockUser(username = "admin", authorities = {"ROLE_admin"})
    void shouldGeneratePublicAccessToken() throws Exception {
        BDDMockito.given(this.engagementLetterService.createPublicAccessToken(any(UUID.class)))
                .willReturn(PublicAccessToken.builder()
                        .id(UUID.randomUUID())
                        .token("public-token-123")
                        .purpose(TokenPurpose.ACCEPT_ENGAGEMENT)
                        .expiresAt(LocalDateTime.now().plusDays(5))
                        .maxUses(5)
                        .usedCount(0)
                        .isActive(true)
                        .engagementLetterId(UUID.randomUUID())
                        .customerId(UUID.randomUUID())
                        .build());

        this.mockMvc.perform(post(EngagementLetterResource.ENGAGEMENT_LETTER
                        + EngagementLetterResource.ID_ID
                        + EngagementLetterResource.PUBLIC_ACCESS_TOKEN, UUID.randomUUID()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("public-token-123"))
                .andExpect(jsonPath("$.purpose").value("ACCEPT_ENGAGEMENT"))
                .andExpect(jsonPath("$.maxUses").value(5))
                .andExpect(jsonPath("$.usedCount").value(0))
                .andExpect(jsonPath("$.isActive").value(true))
                .andExpect(jsonPath("$.publicUrl").value("/public/engagement-letters/access?token=public-token-123"));
    }
}
