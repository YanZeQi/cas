package org.apereo.cas.adaptors.duo.web.flow.action;

import lombok.extern.slf4j.Slf4j;
import org.apereo.cas.adaptors.duo.authn.DuoDirectCredential;
import org.apereo.cas.web.support.WebUtils;
import org.springframework.webflow.action.AbstractAction;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

/**
 * This is {@link DuoDirectAuthenticationAction}.
 *
 * @author Misagh Moayyed
 * @since 5.0.0
 */
@Slf4j
public class DuoDirectAuthenticationAction extends AbstractAction {
    
    @Override
    protected Event doExecute(final RequestContext requestContext) {
        final var c = new DuoDirectCredential(WebUtils.getAuthentication(requestContext));
        WebUtils.putCredential(requestContext, c);
        return success();
    }
}
