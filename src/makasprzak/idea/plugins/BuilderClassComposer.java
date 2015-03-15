package makasprzak.idea.plugins;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.intellij.psi.*;

import java.util.List;
import java.util.Map;


/**
 * Created by Maciej Kasprzak on 2014-09-23.
 */
public class BuilderClassComposer {

    private ElementGenerator elementGenerator;
    private PsiElementFactory psiElementFactory;

    public BuilderClassComposer(ElementGenerator elementGenerator, PsiElementFactory psiElementFactory) {
        this.elementGenerator = elementGenerator;
        this.psiElementFactory = psiElementFactory;
    }

    public PsiClass builderClass(PsiClass psiClass, List<PsiField> fields) {
        Map<PsiField, String> nextInterfacesByFields = mapNextStepsByFields(fields);
        PsiClass builderClass = generateClass(elementGenerator.builderClass(fields));
        for (PsiField field : fields) {
            addField(builderClass, field);
        }
        builderClass.add(createBuilderConstructor(builderClass));
        builderClass.add(createBuilderFactoryMethod(psiClass, fields, builderClass));
        for (PsiField field : fields) {
            addBuilderMethod(nextInterfacesByFields, builderClass, field);
        }
        builderClass.add(createBuildMethod(psiClass, fields, builderClass));
        return builderClass;
    }

    public List<PsiClass> stepInterfaces(PsiClass psiClass, List<PsiField> fields) {
        final Map<PsiField, String> nextInterfacesByFields = mapNextStepsByFields(fields);
        return ImmutableList.<PsiClass>builder()
                .addAll(
                        Lists.transform(fields, toWithStepInterface(nextInterfacesByFields))
                ).add(createBuildStepInterface(psiClass)
                ).build();
    }

    private Function<PsiField, PsiClass> toWithStepInterface(final Map<PsiField, String> nextInterfacesByFields) {
        return new Function<PsiField, PsiClass>() {
            @Override
            public PsiClass apply(PsiField field) {
                return createWithStepInterface(nextInterfacesByFields, field);
            }
        };
    }

    private PsiMethod createBuildMethod(PsiClass psiClass, List<PsiField> fields, PsiClass builderClass) {
        return psiElementFactory.createMethodFromText(elementGenerator.buildMethod(psiClass, fields), builderClass);
    }

    private PsiElement addBuilderMethod(Map<PsiField, String> nextInterfacesByFields, PsiClass builderClass, PsiField field) {
        return builderClass.add(psiElementFactory.createMethodFromText(elementGenerator.builderMethod(field, nextInterfacesByFields.get(field)), builderClass));
    }

    private PsiMethod createBuilderFactoryMethod(PsiClass psiClass, List<PsiField> fields, PsiClass builderClass) {
        return psiElementFactory.createMethodFromText(elementGenerator.builderFactoryMethod(psiClass, fields.get(0)),builderClass);
    }

    private PsiMethod createBuilderConstructor(PsiClass builderClass) {
        return psiElementFactory.createMethodFromText(elementGenerator.builderConstructor(), builderClass);
    }

    private PsiElement addField(PsiClass builderClass, PsiField field) {
        return builderClass.add(psiElementFactory.createFieldFromText(elementGenerator.field(field) + ';', builderClass));
    }

    private Map<PsiField, String> mapNextStepsByFields(List<PsiField> fields) {
        return elementGenerator.mapReturnedInterfaces(fields);
    }

    private PsiClass createBuildStepInterface(PsiClass psiClass) {
        return generateClass(elementGenerator.buildStepInterface(psiClass));
    }

    private PsiClass createWithStepInterface(Map<PsiField, String> nextInterfacesByFields, PsiField field) {
        return generateClass(elementGenerator.interfaceDefinition(field, nextInterfacesByFields.get(field)));
    }

    private PsiClass generateClass(String classText) {
        return psiElementFactory.createClassFromText(classText, null).getInnerClasses()[0];
    }
}
