package makasprzak.idea.plugins.dialog;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiType;
import makasprzak.idea.plugins.mappers.PsiSetterMapper;
import makasprzak.idea.plugins.model.PsiPropertyContainer;

import java.util.List;

import static com.google.common.collect.Lists.transform;
import static java.util.Arrays.asList;
import static makasprzak.idea.plugins.model.PsiPropertyContainer.Builder.psiPropertyContainer;

public class FromSettersDialogFactory implements DialogFactory{

   private final PsiSetterMapper psiSetterMapper = new PsiSetterMapper();

   @Override
   public GeneratorDialog create(PsiClass psiClass) {
      return new GeneratorDialog(psiClass, transformFieldsToProperties(psiClass));
   }

   private List<PsiPropertyContainer> transformFieldsToProperties(PsiClass psiClass) {
       List<PsiMethod> allMethods = asList(psiClass.getAllMethods());
       Iterable<PsiMethod> nonConstructorMethods = Iterables.filter(allMethods, isNotAConstructor());
       Iterable<PsiMethod> voidMethods = Iterables.filter(nonConstructorMethods, isVoid());
       Iterable<PsiMethod> oneArgVoidMethods = Iterables.filter(voidMethods, hasOneArg());
       Iterable<PsiMethod> setters = Iterables.filter(oneArgVoidMethods, startWithSet());
       Iterable<PsiPropertyContainer> setterContainers = Iterables.transform(setters, toPsiPropertyContainer());
       return ImmutableList.copyOf(setterContainers);
   }

    private Predicate<PsiMethod> isNotAConstructor() {
        return new Predicate<PsiMethod>() {
            @Override
            public boolean apply(PsiMethod psiMethod) {
                return !psiMethod.isConstructor();
            }
        };
    }

    private Function<PsiMethod, PsiPropertyContainer> toPsiPropertyContainer() {
        return new Function<PsiMethod, PsiPropertyContainer>() {
            @Override
            public PsiPropertyContainer apply(PsiMethod psiField) {
                return psiPropertyContainer()
                        .withPsiElement(psiField)
                        .withProperty(psiSetterMapper.map(psiField))
                        .build();
            }
        };
    }

    private Predicate<PsiMethod> startWithSet() {
        return new Predicate<PsiMethod>() {
            @Override
            public boolean apply(PsiMethod psiMethod) {
                return psiMethod.getName().startsWith("set");
            }
        };
    }

    private Predicate<PsiMethod> hasOneArg() {
        return new Predicate<PsiMethod>() {
            @Override
            public boolean apply(PsiMethod psiMethod) {
                return psiMethod.getParameterList().getParametersCount() == 1;
            }
        };
    }

    private Predicate<PsiMethod> isVoid() {
        return new Predicate<PsiMethod>() {
            @Override
            public boolean apply(PsiMethod psiMethod) {
                return psiMethod.getReturnType().equals(PsiType.VOID);
            }
        };
    }
}
