package org.apereo.cas.web.flow.client;

import lombok.extern.slf4j.Slf4j;

import java.util.regex.Pattern;

/**
 * A simple implementation of {@link BaseSpnegoKnownClientSystemsFilterAction} to allow / skip SPNEGO / KRB /
 * NTLM authentication based on a regex match against a reverse DNS lookup of the requesting
 * system.
 *
 * @author Sean Baker
 * @author Misagh Moayyed
 * @since 4.1
 */

@Slf4j
public class HostNameSpnegoKnownClientSystemsFilterAction extends BaseSpnegoKnownClientSystemsFilterAction {


    private final Pattern hostNamePatternString;

    /**
     * Instantiates a new hostname spnego known client systems filter action.
     *
     * @param ipsToCheckPattern     the ips to check pattern
     * @param alternativeHost       the alternative remote host attribute
     * @param dnsTimeout            # of milliseconds to wait for a DNS request to return
     * @param hostNamePatternString the host name pattern string.
     */
    public HostNameSpnegoKnownClientSystemsFilterAction(final Pattern ipsToCheckPattern, final String alternativeHost,
                                                        final long dnsTimeout,
                                                        final String hostNamePatternString) {
        super(ipsToCheckPattern, alternativeHost, dnsTimeout);
        this.hostNamePatternString = Pattern.compile(hostNamePatternString);
    }

    /**
     * {@inheritDoc}.
     * <p>
     * Checks whether the IP should even be paid attention to,
     * then does a reverse DNS lookup, and if it matches the supplied pattern, performs SPNEGO
     * else skips the process.
     *
     * @param remoteIp The remote ip address to validate
     */
    @Override
    protected boolean shouldDoSpnego(final String remoteIp) {
        final var ipCheck = ipPatternCanBeChecked(remoteIp);
        if (ipCheck && !ipPatternMatches(remoteIp)) {
            return false;
        }
        final var hostName = getRemoteHostName(remoteIp);
        LOGGER.debug("Retrieved host name for the remote ip is [{}]", hostName);
        return this.hostNamePatternString.matcher(hostName).find();
    }
}
