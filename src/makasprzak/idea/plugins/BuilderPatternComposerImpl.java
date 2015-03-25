package makasprzak.idea.plugins;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementFactory;
import makasprzak.idea.plugins.element.ElementGenerator;
import makasprzak.idea.plugins.model.Pojo;
import makasprzak.idea.plugins.model.Property;
import makasprzak.idea.plugins.model.StepBuilderPattern;

import static makasprzak.idea.plugins.model.StepBuilderPattern.Builder.stepBuilderPattern;


/**
 * @author makasprzak
 */
public class BuilderPatternComposerImpl implements BuilderPatternComposer {

    private final PsiElementFactory psiElementFactory;
    private final ElementGenerator elementGenerator;

    public BuilderPatternComposerImpl(PsiElementFactory psiElementFactory,
                                      ElementGenerator elementGenerator) {
        this.psiElementFactory = psiElementFactory;
        this.elementGenerator = elementGenerator;
    }

    @Override
    public StepBuilderPattern build(Pojo pojo) {
        return stepBuilderPattern()
                .withBuilderClass(builderClass(pojo))
                .withStepInterfaces(stepInterfaces(pojo))
                .build();
    }

    private PsiClass builderClass(Pojo pojo) {
        PsiClass builderClass = generateClass(elementGenerator.builderClass(pojo));
        for (Property property : pojo.getProperties()) {
            includePropertyFieldInClass(property, builderClass);
        }
        addBuilderInnerClass(builderClass);
        createAStaticFactoryMethodForTheBuilderClass(pojo, builderClass);
        for (Property property : pojo.getProperties()) {
            addStepMethod(pojo, builderClass, property);
        }
        addBuildMethod(pojo, builderClass);
        return builderClass;
    }

    private PsiElement addBuildMethod(Pojo pojo, PsiClass builderClass) {
        return builderClass.add(psiElementFactory.createMethodFromText(elementGenerator.buildMethod(pojo), builderClass));
    }

    private PsiElement addStepMethod(Pojo pojo, PsiClass builderClass, Property property) {
        return builderClass.add(psiElementFactory.createMethodFromText(elementGenerator.stepMethod(property, pojo.nextProperty(property)), builderClass));
    }

    private void createAStaticFactoryMethodForTheBuilderClass(Pojo pojo, PsiClass builderClass) {
        builderClass.add(psiElementFactory.createMethodFromText(elementGenerator.builderFactoryMethod(pojo), builderClass));
    }

    private void addBuilderInnerClass(PsiClass builderClass) {
        builderClass.add(psiElementFactory.createMethodFromText(elementGenerator.builderConstructor(), builderClass));
    }

    private void includePropertyFieldInClass(Property property, PsiClass builderClass) {
        builderClass.add(psiElementFactory.createFieldFromText(elementGenerator.fieldDeclaration(property), builderClass));
    }

    private ImmutableList<PsiClass> stepInterfaces(final Pojo pojo) {
        return ImmutableList.<PsiClass>builder()
                .addAll(
                        Lists.transform(pojo.getProperties(), toStepInterface(pojo))
                ).add(generateClass(elementGenerator.buildStepInterface(pojo))
                ).build();
    }

    private Function<Property, PsiClass> toStepInterface(final Pojo pojo) {
        return new Function<Property, PsiClass>() {
            @Override
            public PsiClass apply(Property property) {
                return generateClass(elementGenerator.stepInterface(property, pojo.nextProperty(property)));
            }
        };
    }

    private PsiClass generateClass(String classText) {
        return psiElementFactory.createClassFromText(classText, null).getInnerClasses()[0];
    }
}
