package makasprzak.idea.plugins.properties;

import com.intellij.psi.PsiClass;
import makasprzak.idea.plugins.model.Property;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PropertiesStrategyClientTest {

   @Mock private PropertiesConsumer propertiesConsumer;
   @Mock private PropertiesStrategy propertiesStrategy;
   @InjectMocks private PropertiesStrategyClient propertiesStrategyClient;

   @Mock private List<Property> properties;
   @Mock private PsiClass psiClass;

   @Test
   public void shouldExecuteStrategyGeneratingCode() throws Exception {
      given(propertiesStrategy.isOk()).willReturn(true);
      given(propertiesStrategy.getProperties()).willReturn(properties);

      propertiesStrategyClient.executeStrategy(psiClass, propertiesStrategy);

      InOrder inOrder = inOrder(propertiesStrategy,propertiesConsumer);
      inOrder.verify(propertiesStrategy).start(psiClass);
      inOrder.verify(propertiesConsumer).consume(properties);
   }

   @Test
   public void shouldExecuteStrategyNotGeneratingCode() throws Exception {
      given(propertiesStrategy.isOk()).willReturn(false);
      given(propertiesStrategy.getProperties()).willReturn(properties);

      propertiesStrategyClient.executeStrategy(psiClass, propertiesStrategy);

      verify(propertiesStrategy).start(psiClass);
      verify(propertiesConsumer, never()).consume(anyList());
   }
}