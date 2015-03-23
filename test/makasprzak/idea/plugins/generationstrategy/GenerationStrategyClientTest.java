package makasprzak.idea.plugins.generationstrategy;

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
public class GenerationStrategyClientTest {

   @Mock private GenerationStrategyClient.PropertiesConsumer propertiesConsumer;
   @Mock private GenerationStrategy generationStrategy;
   @InjectMocks private GenerationStrategyClient generationStrategyClient;

   @Mock private List<Property> properties;
   @Mock private PsiClass psiClass;

   @Test
   public void shouldExecuteStrategyGeneratingCode() throws Exception {
      given(generationStrategy.isOk()).willReturn(true);
      given(generationStrategy.getProperties()).willReturn(properties);

      generationStrategyClient.executeStrategy(psiClass, generationStrategy);

      InOrder inOrder = inOrder(generationStrategy,propertiesConsumer);
      inOrder.verify(generationStrategy).start(psiClass);
      inOrder.verify(propertiesConsumer).consume(properties);
   }

   @Test
   public void shouldExecuteStrategyNotGeneratingCode() throws Exception {
      given(generationStrategy.isOk()).willReturn(false);
      given(generationStrategy.getProperties()).willReturn(properties);

      generationStrategyClient.executeStrategy(psiClass, generationStrategy);

      verify(generationStrategy).start(psiClass);
      verify(propertiesConsumer, never()).consume(anyList());
   }
}