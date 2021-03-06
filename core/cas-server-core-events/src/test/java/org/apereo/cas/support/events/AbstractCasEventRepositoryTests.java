package org.apereo.cas.support.events;

import lombok.extern.slf4j.Slf4j;
import org.apereo.cas.mock.MockTicketGrantingTicket;
import org.apereo.cas.support.events.dao.CasEvent;
import org.apereo.cas.support.events.ticket.CasTicketGrantingTicketCreatedEvent;
import org.apereo.cas.ticket.TicketGrantingTicket;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.*;

/**
 * This is {@link AbstractCasEventRepositoryTests}.
 *
 * @author Misagh Moayyed
 * @since 5.0.0
 */
@Slf4j
public abstract class AbstractCasEventRepositoryTests {

    @Test
    public void verifySave() {
        final var dto1 = getCasEvent();
        getRepositoryInstance().save(dto1);

        final var dto2 = getCasEvent();
        getRepositoryInstance().save(dto2);
        
        final Collection<? extends CasEvent> col = getRepositoryInstance().load();
        assertEquals(2, col.size());
        
        assertNotEquals(dto1.getId(), 0);
        assertNotEquals(dto2.getId(), 0);
        assertNotEquals(dto2.getId(), dto1.getId());
        
        final CasEvent casEvent = col.stream().findFirst().get();
        assertFalse(casEvent.getProperties().isEmpty());
    }

    private CasEvent getCasEvent() {
        final TicketGrantingTicket ticket = new MockTicketGrantingTicket("casuser");
        final var event = new CasTicketGrantingTicketCreatedEvent(this, ticket);

        final var dto = new CasEvent();
        dto.setType(event.getClass().getCanonicalName());
        dto.putTimestamp(event.getTimestamp());
        dto.setCreationTime(event.getTicketGrantingTicket().getCreationTime().toString());
        dto.putId(event.getTicketGrantingTicket().getId());
        dto.setPrincipalId(event.getTicketGrantingTicket().getAuthentication().getPrincipal().getId());
        return dto;
    }

    public abstract CasEventRepository getRepositoryInstance();
}
