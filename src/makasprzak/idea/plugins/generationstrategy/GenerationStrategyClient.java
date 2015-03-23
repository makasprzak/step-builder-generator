package makasprzak.idea.plugins.generationstrategy;

import com.intellij.psi.PsiClass;
import makasprzak.idea.plugins.model.Property;

import java.util.List;

/**
 * @author mkasprzak
 */
public class GenerationStrategyClient {

   private final PropertiesConsumer propertiesConsumer;

   public GenerationStrategyClient(PropertiesConsumer propertiesConsumer) {
      this.propertiesConsumer = propertiesConsumer;
   }

   public void executeStrategy(PsiClass psiClass, GenerationStrategy strategy) {
      strategy.start(psiClass);
      if (strategy.isOk())
         propertiesConsumer.consume(strategy.getProperties());
   }

   public static interface PropertiesConsumer {
      void consume(List<Property> properties);
   }
}
