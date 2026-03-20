package es.upm.api.domain.services;

import es.upm.api.domain.exceptions.BadRequestException;
import es.upm.api.domain.exceptions.NotFoundException;
import es.upm.api.domain.model.EngagementLetter;
import es.upm.api.domain.model.PublicAccessToken;
import es.upm.api.domain.model.TokenPurpose;
import es.upm.api.domain.model.UserDto;
import es.upm.api.domain.persistence.EngagementLetterPersistence;
import es.upm.api.domain.persistence.PublicAccessTokenPersistence;
import es.upm.api.domain.webclients.UserWebClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EngagementLetterPublicAccessTokenServiceTest {

    @Mock
    private EngagementLetterPersistence engagementLetterPersistence;
    @Mock
    private PublicAccessTokenPersistence publicAccessTokenPersistence;
    @Mock
    private UserWebClient userWebClient;

    @InjectMocks
    private EngagementLetterService engagementLetterService;

    @Test
    void shouldCreatePublicAccessTokenWithDefaultValues() {
        UUID engagementLetterId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        EngagementLetter engagementLetter = EngagementLetter.builder()
                .id(engagementLetterId)
                .owner(UserDto.builder().id(customerId).mobile("666666000").build())
                .build();
        given(this.engagementLetterPersistence.readById(engagementLetterId)).willReturn(engagementLetter);
        given(this.publicAccessTokenPersistence.create(any(PublicAccessToken.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        PublicAccessToken created = this.engagementLetterService.createPublicAccessToken(engagementLetterId);

        assertThat(created.getPurpose()).isEqualTo(TokenPurpose.ACCEPT_ENGAGEMENT);
        assertThat(created.getUsedCount()).isZero();
        assertThat(created.getIsActive()).isTrue();
        assertThat(created.getMaxUses()).isEqualTo(5);
        assertThat(created.getEngagementLetterId()).isEqualTo(engagementLetterId);
        assertThat(created.getCustomerId()).isEqualTo(customerId);
        assertThat(created.getToken()).isNotBlank();
        assertThat(created.getExpiresAt()).isAfter(LocalDateTime.now().plusDays(4));

        ArgumentCaptor<PublicAccessToken> captor = ArgumentCaptor.forClass(PublicAccessToken.class);
        verify(this.publicAccessTokenPersistence).create(captor.capture());
        assertThat(captor.getValue().getCustomerId()).isEqualTo(customerId);
    }

    @Test
    void shouldFailWhenOwnerIsMissing() {
        UUID engagementLetterId = UUID.randomUUID();
        given(this.engagementLetterPersistence.readById(engagementLetterId))
                .willReturn(EngagementLetter.builder().id(engagementLetterId).owner(null).build());

        assertThatThrownBy(() -> this.engagementLetterService.createPublicAccessToken(engagementLetterId))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("engagement letter owner is required");
    }

    @Test
    void shouldPropagateNotFoundWhenEngagementLetterDoesNotExist() {
        UUID engagementLetterId = UUID.randomUUID();
        given(this.engagementLetterPersistence.readById(engagementLetterId))
                .willThrow(new NotFoundException("The EngagementLetter ID doesn't exist: " + engagementLetterId));

        assertThatThrownBy(() -> this.engagementLetterService.createPublicAccessToken(engagementLetterId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(engagementLetterId.toString());
    }
}
