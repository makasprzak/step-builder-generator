package makasprzak.idea.plugins.dialog;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import makasprzak.idea.plugins.model.Property;

import java.util.List;

import static com.google.common.collect.Lists.transform;
import static java.util.Arrays.asList;
import static makasprzak.idea.plugins.mappers.PsiParameterMapper.toProperty;

/**
 * @author mkasprzak
 */
public class FromConstructorDialogFactory implements DialogFactory {
   @Override
   public GeneratorDialog create(PsiClass psiClass) {
      return new GeneratorDialog(
         psiClass,
         transformConstructorArgumentsToProperties(getBiggestConstructor(psiClass)));
   }

   private List<Property> transformConstructorArgumentsToProperties(PsiMethod biggestConstructor) {
      return transform(
         getArgumentsOf(biggestConstructor),
         toProperty());
   }

   private List<PsiParameter> getArgumentsOf(PsiMethod biggestConstructor) {
      return asList(biggestConstructor.getParameterList().getParameters());
   }

   private PsiMethod getBiggestConstructor(PsiClass psiClass) {
      PsiMethod[] constructors = psiClass.getConstructors();
      PsiMethod biggestConstructor = null;
      for (PsiMethod constructor : constructors) {
         if (biggestConstructor == null || parametersCount(constructor) > parametersCount(biggestConstructor)) {
            biggestConstructor = constructor;
         }
      }
      return biggestConstructor;
   }

   private int parametersCount(PsiMethod constructor) {
      return constructor.getParameterList().getParametersCount();
   }
}
