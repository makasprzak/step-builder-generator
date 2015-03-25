package makasprzak.idea.plugins.properties;

import com.intellij.psi.PsiClass;
import makasprzak.idea.plugins.model.Property;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class UseConstructorArgsPropertiesProviderTest {

    @Mock private ConstructorPropertiesAnalyzer constructorPropertiesAnalyzer;
    @InjectMocks private UseConstructorArgsPropertiesProvider propertiesProvider;

    @Mock private PropertiesConsumer consumer;
    @Mock private PsiClass psiClass;
    @Mock private List<Property> properties;

    @Test
    public void shouldGetPropertiesFromBiggestConstructor() throws Exception {
        given(constructorPropertiesAnalyzer.getBiggestConstructorArgs(psiClass)).willReturn(properties);
        propertiesProvider.getProperties(psiClass, consumer);
        verify(consumer).consume(properties);

    }
}