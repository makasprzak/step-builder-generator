package makasprzak.idea.plugins;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.intellij.psi.*;
import makasprzak.idea.plugins.element.ElementGenerator;
import makasprzak.idea.plugins.model.Pojo;
import makasprzak.idea.plugins.model.Property;
import makasprzak.idea.plugins.model.PsiPojo;
import makasprzak.idea.plugins.model.StepBuilderPattern;

import java.util.List;
import java.util.Map;

import static makasprzak.idea.plugins.model.StepBuilderPattern.Builder.stepBuilderPattern;


/**
 * @author makasprzak
 */
public class BuilderPatternComposerImpl implements BuilderPatternComposer {

    private final PsiElementGenerator psiElementGenerator;
    private final PsiElementFactory psiElementFactory;
    private final ElementGenerator elementGenerator;

    public BuilderPatternComposerImpl(PsiElementGenerator psiElementGenerator,
                                      PsiElementFactory psiElementFactory, 
                                      ElementGenerator elementGenerator) {
        this.psiElementGenerator = psiElementGenerator;
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
            builderClass.add(psiElementFactory.createFieldFromText(elementGenerator.fieldDeclaration(property), builderClass));
        }
        builderClass.add(psiElementFactory.createMethodFromText(elementGenerator.builderConstructor(), builderClass));
        builderClass.add(psiElementFactory.createMethodFromText(elementGenerator.builderFactoryMethod(pojo), builderClass));
        for (Property property : pojo.getProperties()) {
            builderClass.add(psiElementFactory.createMethodFromText(elementGenerator.stepMethod(property, pojo.nextProperty(property)), builderClass));
        }
        builderClass.add(psiElementFactory.createMethodFromText(elementGenerator.buildMethod(pojo), builderClass));
        return builderClass;
    }

    private ImmutableList<PsiClass> stepInterfaces(final Pojo pojo) {
        return ImmutableList.<PsiClass>builder()
                .addAll(
                        Lists.transform(pojo.getProperties(), new Function<Property, PsiClass>() {
                            @Override
                            public PsiClass apply(Property property) {
                                return generateClass(elementGenerator.stepInterface(property, pojo.nextProperty(property)));
                            }
                        })
                ).add(generateClass(elementGenerator.buildStepInterface(pojo))
                ).build();
    }

    private PsiClass generateClass(String classText) {
        return psiElementFactory.createClassFromText(classText, null).getInnerClasses()[0];
    }
}
