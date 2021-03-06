package org.apereo.cas.util.services;

import lombok.extern.slf4j.Slf4j;
import org.apereo.cas.services.util.DefaultRegisteredServiceJsonSerializer;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * This is {@link DefaultRegisteredServiceJsonSerializerTests}.
 *
 * @author Misagh Moayyed
 * @since 5.1.0
 */
@Slf4j
public class DefaultRegisteredServiceJsonSerializerTests {

    @Test
    public void checkNullability() {
        final var zer = new DefaultRegisteredServiceJsonSerializer();
        final var json = "    {\n"
                + "        \"@class\" : \"org.apereo.cas.services.RegexRegisteredService\",\n"
                + "            \"serviceId\" : \"^https://xyz.*\",\n"
                + "            \"name\" : \"XYZ\",\n"
                + "            \"id\" : \"20161214\"\n"
                + "    }";

        final var s = zer.from(json);
        assertNotNull(s);
        assertNotNull(s.getAccessStrategy());
        assertNotNull(s.getAttributeReleasePolicy());
        assertNotNull(s.getProxyPolicy());
        assertNotNull(s.getUsernameAttributeProvider());
    }
}
