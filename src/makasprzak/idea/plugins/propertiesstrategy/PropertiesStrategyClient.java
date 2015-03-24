package makasprzak.idea.plugins.propertiesstrategy;

import com.intellij.psi.PsiClass;
import makasprzak.idea.plugins.model.Property;

import java.util.List;

/**
 * @author mkasprzak
 */
public class PropertiesStrategyClient {

   private final PropertiesConsumer propertiesConsumer;

   public PropertiesStrategyClient(PropertiesConsumer propertiesConsumer) {
      this.propertiesConsumer = propertiesConsumer;
   }

   public void executeStrategy(PsiClass psiClass, PropertiesStrategy strategy) {
      strategy.start(psiClass);
      if (strategy.isOk())
         propertiesConsumer.consume(strategy.getProperties());
   }

   public static interface PropertiesConsumer {
      void consume(List<Property> properties);
   }
}
