package makasprzak.idea.plugins.dialog;

import com.google.common.base.Function;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import makasprzak.idea.plugins.mappers.PsiFieldMapper;
import makasprzak.idea.plugins.model.PsiPropertyContainer;

import java.util.List;

import static com.google.common.collect.Lists.transform;
import static java.util.Arrays.asList;
import static makasprzak.idea.plugins.model.PsiPropertyContainer.Builder.psiPropertyContainer;

public class FromFieldsDialogFactory implements DialogFactory{

   private final PsiFieldMapper psiFieldMapper = new PsiFieldMapper();

   @Override
   public GeneratorDialog create(PsiClass psiClass) {
      return new GeneratorDialog(psiClass, transformFieldsToProperties(psiClass));
   }

   private List<PsiPropertyContainer> transformFieldsToProperties(PsiClass psiClass) {
      return transform(asList(psiClass.getAllFields()), new Function<PsiField, PsiPropertyContainer>() {
         @Override
         public PsiPropertyContainer apply(PsiField psiField) {
            return psiPropertyContainer()
               .withPsiElement(psiField)
               .withProperty(psiFieldMapper.map(psiField))
               .build();
         }
      });
   }
}
