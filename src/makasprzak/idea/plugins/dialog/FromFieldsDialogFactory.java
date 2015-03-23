package makasprzak.idea.plugins.dialog;

import com.intellij.psi.PsiClass;
import makasprzak.idea.plugins.mappers.PsiFieldMapper;
import makasprzak.idea.plugins.model.Property;

import java.util.List;

import static com.google.common.collect.Lists.transform;
import static java.util.Arrays.asList;

/**
 * @author mkasprzak
 */
public class FromFieldsDialogFactory implements DialogFactory{
   @Override
   public GeneratorDialog create(PsiClass psiClass) {
      return new GeneratorDialog(psiClass, transformFieldsToProperties(psiClass));
   }

   private List<Property> transformFieldsToProperties(PsiClass psiClass) {
      return transform(asList(psiClass.getAllFields()), PsiFieldMapper.toProperty());
   }
}
