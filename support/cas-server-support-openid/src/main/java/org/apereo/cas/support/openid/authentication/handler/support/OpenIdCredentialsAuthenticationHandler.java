package org.apereo.cas.support.openid.authentication.handler.support;

import lombok.extern.slf4j.Slf4j;
import org.apereo.cas.authentication.AbstractAuthenticationHandler;
import org.apereo.cas.authentication.AuthenticationHandlerExecutionResult;
import org.apereo.cas.authentication.BasicCredentialMetaData;
import org.apereo.cas.authentication.Credential;
import org.apereo.cas.authentication.DefaultAuthenticationHandlerExecutionResult;
import org.apereo.cas.authentication.principal.PrincipalFactory;
import org.apereo.cas.services.ServicesManager;
import org.apereo.cas.support.openid.authentication.principal.OpenIdCredential;
import org.apereo.cas.ticket.TicketGrantingTicket;
import org.apereo.cas.ticket.registry.TicketRegistry;

import javax.security.auth.login.FailedLoginException;
import java.security.GeneralSecurityException;

/**
 * Ensures that the OpenId provided matches with the existing
 * TicketGrantingTicket. Otherwise, fail authentication.
 *
 * @author Scott Battaglia
 * @since 3.1
 */
@Slf4j
public class OpenIdCredentialsAuthenticationHandler extends AbstractAuthenticationHandler {
    
    private final TicketRegistry ticketRegistry;

    public OpenIdCredentialsAuthenticationHandler(final String name, final ServicesManager servicesManager, final PrincipalFactory principalFactory,
                                                  final TicketRegistry ticketRegistry) {
        super(name, servicesManager, principalFactory, null);
        this.ticketRegistry = ticketRegistry;
    }

    @Override
    public AuthenticationHandlerExecutionResult authenticate(final Credential credential) throws GeneralSecurityException {
        final var c = (OpenIdCredential) credential;

        final var t = this.ticketRegistry.getTicket(c.getTicketGrantingTicketId(), TicketGrantingTicket.class);

        if (t == null || t.isExpired()) {
            throw new FailedLoginException("TGT is null or expired.");
        }
        final var principal = t.getAuthentication().getPrincipal();
        if (!principal.getId().equals(c.getUsername())) {
            throw new FailedLoginException("Principal ID mismatch");
        }
        return new DefaultAuthenticationHandlerExecutionResult(this, new BasicCredentialMetaData(c), principal);
    }

    @Override
    public boolean supports(final Credential credential) {
        return credential instanceof OpenIdCredential;
    }

}
