package makasprzak.idea.plugins;

import com.google.common.collect.ImmutableList;
import com.intellij.psi.*;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

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
        fields.forEach(field -> addField(builderClass, field));
        builderClass.add(createBuilderConstructor(builderClass));
        builderClass.add(createBuilderFactoryMethod(psiClass, fields, builderClass));
        fields.forEach(field -> addBuilderMethod(nextInterfacesByFields, builderClass, field));
        builderClass.add(createBuildMethod(psiClass, fields, builderClass));
        return builderClass;
    }

    public List<PsiClass> stepInterfaces(PsiClass psiClass, List<PsiField> fields) {
        Map<PsiField, String> nextInterfacesByFields = mapNextStepsByFields(fields);
        return ImmutableList.<PsiClass>builder()
                .addAll(
                        fields.stream()
                                .map(field -> createWithStepInterface(nextInterfacesByFields, field))
                                .collect(toList())
                ).add(createBuildStepInterface(psiClass)
                ).build();
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
