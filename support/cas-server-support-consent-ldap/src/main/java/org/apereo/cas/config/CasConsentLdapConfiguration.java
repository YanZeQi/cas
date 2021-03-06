package org.apereo.cas.config;

import lombok.extern.slf4j.Slf4j;
import org.apereo.cas.configuration.CasConfigurationProperties;
import org.apereo.cas.consent.ConsentRepository;
import org.apereo.cas.consent.LdapConsentRepository;
import org.apereo.cas.util.LdapUtils;
import org.ldaptive.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This is {@link CasConsentLdapConfiguration}.
 *
 * @author Arnold Bergner
 * @since 5.2.0
 */
@Configuration("casConsentLdapConfiguration")
@EnableConfigurationProperties(CasConfigurationProperties.class)
@Slf4j
public class CasConsentLdapConfiguration {

    @Autowired
    private CasConfigurationProperties casProperties;

    @Bean
    public ConsentRepository consentRepository() {
        final var ldap = casProperties.getConsent().getLdap();
        final ConnectionFactory connectionFactory = LdapUtils.newLdaptivePooledConnectionFactory(ldap);
        return new LdapConsentRepository(connectionFactory, ldap);
    }
}
