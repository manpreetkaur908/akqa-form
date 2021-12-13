package com.adobe.aem.guides.wknd.spa.react.core.servlets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import java.io.IOException;
import javax.jcr.*;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({AemContextExtension.class,MockitoExtension.class})
class AKQAFormServletTest {

    private static final String WORD = "ten";

    public final AemContext aemContext = new AemContext(ResourceResolverType.JCR_MOCK);

    @Mock
    private Session session;

    @InjectMocks
    AKQAFormServlet akqaformservlet;

    SlingHttpServletRequest request = mock(SlingHttpServletRequest.class);

    SlingHttpServletResponse response = mock(SlingHttpServletResponse.class);

    ResourceResolver resourceResolver = mock(ResourceResolver.class);

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testPost() throws IOException, ServletException, RepositoryException {
        assertNotNull(request);
        assertNotNull(response);
        akqaformservlet.doPost(request, response);
    }

    @Test
    void testnumberToWord() throws IOException, ServletException, RepositoryException {
        assertNotNull(akqaformservlet.numberToWord(100));
        assertEquals(WORD, akqaformservlet.numberToWord(10));
    }
}

