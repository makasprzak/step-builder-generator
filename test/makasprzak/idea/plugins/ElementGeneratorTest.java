package makasprzak.idea.plugins;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static java.util.Arrays.asList;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class ElementGeneratorTest {
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private PsiField psiField;

    @InjectMocks private ElementGenerator elementGenerator;

    @Test
    public void shouldGenerateInterfaceName() throws Exception {
        given(psiField.getName()).willReturn("someField");
        assertThat(elementGenerator.interfaceName(psiField)).isEqualTo("SomeFieldStep");

    }

    @Test
    public void shouldGenerateInterfaceDefinition() throws Exception {
        given(psiField.getName()).willReturn("someField");
        given(psiField.getType().getPresentableText()).willReturn("String");
        assertThat(elementGenerator.interfaceDefinition(psiField,"OtherFieldStep")).isEqualTo(
                "public static interface SomeFieldStep {\n" +
                        "\tOtherFieldStep withSomeField(String someField);\n" +
                "}\n"
        );

    }

    @Test
    public void shouldGenerateBuilderMethod() throws Exception {
        given(psiField.getName()).willReturn("someField");
        given(psiField.getType().getPresentableText()).willReturn("String");
        assertThat(elementGenerator.builderMethod(psiField, "OtherFieldStep")).isEqualTo(
                "@Override\n" +
                "public OtherFieldStep withSomeField(String someField) {\n" +
                        "\tthis.someField = someField;\n" +
                        "\treturn this;\n" +
                "}\n"
        );
    }

    @Test
    public void shouldGenerateInjectionStatement() throws Exception {
        given(psiField.getName()).willReturn("someField");
        assertThat(elementGenerator.injection(psiField, "PojoClass")).isEqualTo("pojoClass.setSomeField(this.someField);");
    }

    @Test
    public void shouldGenerateBuilderClass() throws Exception {
        assertThat(elementGenerator.builderClass(asList(
                psiField("name"),
                psiField("lastName"),
                psiField("age")))
        ).isEqualTo(
            "public static class Builder implements NameStep, LastNameStep, AgeStep, BuildStep {}"
        );
    }

    @Test
    public void shouldGenerateBuilderConstructor() throws Exception {
        assertThat(elementGenerator.builderConstructor()).isEqualTo(
                "private Builder() {}"
        );
    }

    @Test
    public void shouldGenerateBuilderFactoryMethod() throws Exception {
        assertThat(elementGenerator.builderFactoryMethod(psiClass("PojoClass"), psiField("firstField"))).isEqualTo(
                "public static FirstFieldStep pojoClass() {\n" +
                "    return new Builder();\n" +
                "}"
        );
    }

    @Test
    public void shouldGenerateBuildMethod() throws Exception {
        assertThat(elementGenerator.buildMethod(psiClass("PojoClass"), asList(psiField("name"), psiField("lastName"), psiField("age")))).isEqualTo(
                "@Override\n" +
                "public PojoClass build() {\n" +
                "    PojoClass pojoClass = new PojoClass();\n" +
                "    pojoClass.setName(this.name);\n" +
                "    pojoClass.setLastName(this.lastName);\n" +
                "    pojoClass.setAge(this.age);\n" +
                "    return pojoClass;\n" +
                "}"
        );
    }

    @Test
    public void shouldGenerateBuildInterface() throws Exception {
        assertThat(elementGenerator.buildStepInterface(psiClass("PojoClass"))).isEqualTo(
                "public static interface BuildStep {\n" +
                "    PojoClass build();\n" +
                "}"
        );

    }

    private PsiClass psiClass(String name) {
        PsiClass psiClass = mock(PsiClass.class);
        given(psiClass.getName()).willReturn(name);
        return psiClass;
    }

    private PsiField psiField(String name) {
        PsiField psiField = mock(PsiField.class);
        given(psiField.getName()).willReturn(name);
        return psiField;
    }

}