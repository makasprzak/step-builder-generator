package makasprzak.idea.plugins.element;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import makasprzak.idea.plugins.model.Pojo;
import makasprzak.idea.plugins.model.Property;


import static com.google.common.base.Joiner.on;
import static com.google.common.collect.Lists.transform;
import static java.lang.String.format;
import static org.apache.commons.lang.StringUtils.capitalize;
import static org.apache.commons.lang.StringUtils.uncapitalize;

public class ElementGenerator {
    public String builderClass(Pojo pojo) {
        String steps = on(", ").join(transform(pojo.getProperties(), new Function<Property, String>() {
            @Override
            public String apply(Property property) {
                return stepName(property);
            }
        }));
        return "public static class Builder implements " + steps + ", BuildStep {}";
    }

    public String fieldDeclaration(Property property) {
        return format("private %s %s;", property.getType().replace(" ...", "[]"), property.getName());
    }

    public String builderConstructor() {
        return "private Builder() {}";
    }

    public String builderFactoryMethod(Pojo pojo) {
        if (pojo.getProperties().isEmpty()) {
            return format("public static %1$s %2$s() {\n" +
                    "    return new %1$s();\n" +
                    "}", pojo.getName(), uncapitalize(pojo.getName()));
        }
        return format("public static %s %s() {\n" +
                "    return new Builder();\n" +
                "}", stepName(pojo.getProperties().get(0)), uncapitalize(pojo.getName()));
    }

    public String stepMethod(Property forProperty, Optional<Property> nextProperty) {
        return  format("@Override\n" +
                        "public %1$s with%4$s(%3$s %2$s) {\n" +
                        "\tthis.%2$s = %2$s;\n" +
                        "\treturn this;\n" +
                        "}\n",
                nextProperty.isPresent() ? stepName(nextProperty.get()) : "BuildStep",
                forProperty.getName(),
                forProperty.getType(),
                capitalize(forProperty.getName()));
    }

    public String buildMethod(final Pojo pojo) {
        if (pojo.isConstructorInjection()) {
            return format("@Override\n" +
                    "public %1$s build() {\n" +
                    "    return new %1$s(\n" +
                    "    %2$s\n" +
                    ");\n" +
                    "}", pojo.getName(), on(",\n    ").join(transform(pojo.getProperties(), new Function<Property, String>() {
                @Override
                public String apply(Property property) {
                    return "this." + property.getName();
                }
            })));
        }
        return format("@Override\n" +
                "public %1$s build() {\n" +
                "    %1$s %2$s = new %1$s();\n" +
                "    %3$s\n" +
                "    return %2$s;\n" +
                "}",pojo.getName(), uncapitalize(pojo.getName()), on("\n    ").join(transform(pojo.getProperties(), new Function<Property, String>() {
            @Override
            public String apply(Property property) {
                return format("%s.set%s(this.%s);",uncapitalize(pojo.getName()),capitalize(property.getName()),property.getName());
            }
        })));
    }

    public String stepInterface(Property forProperty, Optional<Property> nextProperty) {
        return format("public static interface %1$s {\n" +
                        "\t%2$s with%3$s(%4$s %5$s);\n" +
                        "}\n",
                stepName(forProperty),
                nextProperty.isPresent() ? stepName(nextProperty.get()) : "BuildStep",
                capitalize(forProperty.getName()),
                forProperty.getType(),
                forProperty.getName()
        );
    }

    public String buildStepInterface(Pojo pojo) {
        return format("public static interface BuildStep {\n" +
                "    %s build();\n" +
                "}", pojo.getName());
    }

    private String stepName(Property property) {
        return capitalize(property.getName()) + "Step";
    }
}
