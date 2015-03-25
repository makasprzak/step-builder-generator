package makasprzak.idea.plugins.dialog;

import com.google.common.base.Function;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiParameter;
import makasprzak.idea.plugins.mappers.PsiParameterMapper;
import makasprzak.idea.plugins.model.PsiPropertyContainer;

import java.util.List;

import static com.google.common.collect.Lists.transform;
import static makasprzak.idea.plugins.PsiClassAdapter.forA;
import static makasprzak.idea.plugins.model.PsiPropertyContainer.Builder.psiPropertyContainer;

public class FromConstructorDialogFactory implements DialogFactory {

   private final PsiParameterMapper psiParameterMapper = new PsiParameterMapper();

   @Override
   public GeneratorDialog create(PsiClass psiClass) {
      return new GeneratorDialog(
         psiClass,
         transformConstructorArgumentsToProperties(forA(psiClass).getBiggestConstructorsArgs()));
   }

   private List<PsiPropertyContainer> transformConstructorArgumentsToProperties(List<PsiParameter> psiParameters) {
      return transform(psiParameters, new Function<PsiParameter, PsiPropertyContainer>() {
         @Override
         public PsiPropertyContainer apply(PsiParameter psiParameter) {
            return psiPropertyContainer()
               .withPsiElement(psiParameter)
               .withProperty(psiParameterMapper.map(psiParameter))
               .build();
         }
      });
   }

}
