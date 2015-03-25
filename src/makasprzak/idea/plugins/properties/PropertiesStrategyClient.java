package makasprzak.idea.plugins.properties;

import com.intellij.psi.PsiClass;

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

}
