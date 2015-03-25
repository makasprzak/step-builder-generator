package makasprzak.idea.plugins.element;

import com.google.common.base.Optional;
import makasprzak.idea.plugins.model.Pojo;
import makasprzak.idea.plugins.model.Property;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static makasprzak.idea.plugins.model.Pojo.Builder.pojo;
import static makasprzak.idea.plugins.model.Property.Builder.property;
import static org.fest.assertions.Assertions.assertThat;

public class ElementGeneratorTest {
    private final ElementGenerator generator = new ElementGenerator();

    @Test
    public void shouldGenerateBuilderClass() throws Exception {
        assertThat(generator.builderClass(pojo1)).isEqualTo(
                "public static class Builder implements NameStep, LastNameStep, AgeStep, BuildStep {}"
        );

    }

    @Test
    public void shouldGenerateAnotherBuilderClass() throws Exception {
        assertThat(generator.builderClass(pojo2)).isEqualTo(
                "public static class Builder implements CountryStep, CityStep, ChildrenStep, BuildStep {}"
        );

    }

    @Test
    public void shouldGenerateEmptyBuilderClass() throws Exception {
        assertThat(generator.builderClass(emptyPojo)).isEqualTo(
                "public static class Builder implements BuildStep {}"
        );

    }

    @Test
    public void shouldGenerateFieldDeclaration() throws Exception {
        assertThat(generator.fieldDeclaration(property().withName("name").withType("String").build())).isEqualTo(
          "private String name;"
        );

    }

    @Test
    public void shouldGenerateAnotherFieldDeclaration() throws Exception {
        assertThat(generator.fieldDeclaration(property().withName("age").withType("int").build())).isEqualTo(
          "private int age;"
        );

    }

    @Test
    public void shouldGenerateVarargFieldDeclaration() throws Exception {
        assertThat(generator.fieldDeclaration(property().withName("children").withType("String ...").build())).isEqualTo(
          "private String[] children;"
        );
    }

    @Test
    public void shouldGenerateBuilderConstructor() throws Exception {
        assertThat(generator.builderConstructor()).isEqualTo(
          "private Builder() {}"
        );
    }

    @Test
    public void shouldGenerateBuilderFactoryMethod() throws Exception {
        assertThat(generator.builderFactoryMethod(pojo1)).isEqualTo(
                "public static NameStep somePojo() {\n" +
                        "    return new Builder();\n" +
                        "}"
        );
    }

    @Test
    public void shouldGenerateAnotherBuilderFactoryMethod() throws Exception {
        assertThat(generator.builderFactoryMethod(pojo2)).isEqualTo(
                "public static CountryStep otherPojo() {\n" +
                        "    return new Builder();\n" +
                        "}"
        );
    }

    @Test
    public void edgeCase_shouldInstantlyReturnBuiltEmptyPojo() throws Exception {
        assertThat(generator.builderFactoryMethod(emptyPojo)).isEqualTo(
                "public static EmptyPojo emptyPojo() {\n" +
                        "    return new EmptyPojo();\n" +
                        "}"
        );
    }

    @Test
    public void shouldGenerateStepMethod() throws Exception {
        assertThat(generator.stepMethod(
                property().withName("firstProperty").withType("String").build(),
                Optional.of(property().withName("secondProperty").withType("boolean").build())

        )).isEqualTo(
                "@Override\n" +
                        "public SecondPropertyStep withFirstProperty(String firstProperty) {\n" +
                        "\tthis.firstProperty = firstProperty;\n" +
                        "\treturn this;\n" +
                        "}\n"
        );
    }

    @Test
    public void shouldGenerateAnotherStepMethod() throws Exception {
        assertThat(generator.stepMethod(
                property().withName("secondProperty").withType("boolean").build(),
                Optional.of(property().withName("thirdProperty").withType("String").build())

        )).isEqualTo(
                "@Override\n" +
                        "public ThirdPropertyStep withSecondProperty(boolean secondProperty) {\n" +
                        "\tthis.secondProperty = secondProperty;\n" +
                        "\treturn this;\n" +
                        "}\n"
        );
    }

    @Test
    public void shouldGenerateLastStepMethod() throws Exception {
        assertThat(generator.stepMethod(
                property().withName("secondProperty").withType("boolean").build(),
                Optional.<Property>absent()
        )).isEqualTo(
                "@Override\n" +
                        "public BuildStep withSecondProperty(boolean secondProperty) {\n" +
                        "\tthis.secondProperty = secondProperty;\n" +
                        "\treturn this;\n" +
                        "}\n"
        );
    }

    @Test
    public void shouldGenerateStepInterface() throws Exception {
        assertThat(generator.stepInterface(
                property().withName("firstProperty").withType("String").build(),
                Optional.of(property().withName("secondProperty").withType("boolean").build())
        )).isEqualTo(
                "public static interface FirstPropertyStep {\n" +
                        "\tSecondPropertyStep withFirstProperty(String firstProperty);\n" +
                        "}\n"        );
    }

    @Test
    public void shouldGenerateAnotherStepInterface() throws Exception {
        assertThat(generator.stepInterface(
                property().withName("secondProperty").withType("boolean").build(),
                Optional.of(property().withName("thirdProperty").withType("String").build())
        )).isEqualTo(
                "public static interface SecondPropertyStep {\n" +
                        "\tThirdPropertyStep withSecondProperty(boolean secondProperty);\n" +
                        "}\n"        );
    }

    @Test
    public void shouldGenerateLastStepInterface() throws Exception {
        assertThat(generator.stepInterface(
                property().withName("secondProperty").withType("boolean").build(),
                Optional.<Property>absent()
        )).isEqualTo(
                "public static interface SecondPropertyStep {\n" +
                        "\tBuildStep withSecondProperty(boolean secondProperty);\n" +
                        "}\n"        );
    }

    @Test
    public void shouldGenerateBuildStepInterface() throws Exception {
        assertThat(generator.buildStepInterface(pojo1
        )).isEqualTo(
                "public static interface BuildStep {\n" +
                        "    SomePojo build();\n" +
                        "}"
        );
    }

    @Test
    public void shouldGenerateAnotherBuildStepInterface() throws Exception {
        assertThat(generator.buildStepInterface(pojo2
        )).isEqualTo(
                "public static interface BuildStep {\n" +
                        "    OtherPojo build();\n" +
                        "}"
        );
    }

    @Test
    public void shouldGenerateBuildMethodUsingConstructorInjection() throws Exception {
        assertThat(generator.buildMethod(pojo1
        )).isEqualTo(
                "@Override\n" +
                "public SomePojo build() {\n" +
                        "    return new SomePojo(\n" +
                        "    this.name,\n" +
                        "    this.lastName,\n" +
                        "    this.age\n" +
                        ");\n" +
                        "}"
        );
    }

    @Test
    public void shouldGenerateBuildAnotherMethodUsingConstructorInjection() throws Exception {
        assertThat(generator.buildMethod(pojo2
        )).isEqualTo(
                "@Override\n" +
                "public OtherPojo build() {\n" +
                        "    return new OtherPojo(\n" +
                        "    this.country,\n" +
                        "    this.city,\n" +
                        "    this.children\n" +
                        ");\n" +
                        "}"
        );
    }

    @Test
    public void shouldGenerateBuildMethodUsingSetterInjection() throws Exception {
        assertThat(generator.buildMethod(pojo1SetterInjected
        )).isEqualTo(
                "@Override\n" +
                        "public SomePojo build() {\n" +
                        "    SomePojo somePojo = new SomePojo();\n" +
                        "    somePojo.setName(this.name);\n" +
                        "    somePojo.setLastName(this.lastName);\n" +
                        "    somePojo.setAge(this.age);\n" +
                        "    return somePojo;\n" +
                        "}"
        );
    }

    @Test
    public void shouldGenerateBuildAnotherMethodUsingSetterInjection() throws Exception {
        assertThat(generator.buildMethod(pojo2SetterInjected
        )).isEqualTo(
                "@Override\n" +
                        "public OtherPojo build() {\n" +
                        "    OtherPojo otherPojo = new OtherPojo();\n" +
                        "    otherPojo.setCountry(this.country);\n" +
                        "    otherPojo.setCity(this.city);\n" +
                        "    otherPojo.setChildren(this.children);\n" +
                        "    return otherPojo;\n" +
                        "}"
        );
    }

    private final Pojo pojo1 = pojo().withName("SomePojo").withProperties(Arrays.asList(
            property().withName("name").withType("String").build(),
            property().withName("lastName").withType("String").build(),
            property().withName("age").withType("int").build()
    )).withConstructorInjection(true).build();
    private final Pojo pojo1SetterInjected = pojo().withName("SomePojo").withProperties(Arrays.asList(
            property().withName("name").withType("String").build(),
            property().withName("lastName").withType("String").build(),
            property().withName("age").withType("int").build()
    )).withConstructorInjection(false).build();
    private final Pojo pojo2 = pojo().withName("OtherPojo").withProperties(Arrays.asList(
            property().withName("country").withType("String").build(),
            property().withName("city").withType("String").build(),
            property().withName("children").withType("String ...").build()
    )).withConstructorInjection(true).build();
    private final Pojo pojo2SetterInjected = pojo().withName("OtherPojo").withProperties(Arrays.asList(
            property().withName("country").withType("String").build(),
            property().withName("city").withType("String").build(),
            property().withName("children").withType("String ...").build()
    )).withConstructorInjection(false).build();
    private final Pojo emptyPojo = pojo()
            .withName("EmptyPojo")
            .withProperties(Collections.<Property>emptyList())
            .withConstructorInjection(true)
            .build();

    

}