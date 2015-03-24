package makasprzak.idea.plugins.dialog;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import makasprzak.idea.plugins.PsiClassAdapter;
import makasprzak.idea.plugins.model.Property;

import java.util.List;

import static com.google.common.collect.Lists.transform;
import static java.util.Arrays.asList;
import static makasprzak.idea.plugins.PsiClassAdapter.forA;
import static makasprzak.idea.plugins.mappers.PsiParameterMapper.toProperty;

/**
 * @author mkasprzak
 */
public class FromConstructorDialogFactory implements DialogFactory {
   @Override
   public GeneratorDialog create(PsiClass psiClass) {
      return new GeneratorDialog(
         psiClass,
         transformConstructorArgumentsToProperties(forA(psiClass).getBiggestConstructorsArgs()));
   }

   private List<Property> transformConstructorArgumentsToProperties(List<PsiParameter> psiParameters) {
      return transform(psiParameters,toProperty());
   }

}
