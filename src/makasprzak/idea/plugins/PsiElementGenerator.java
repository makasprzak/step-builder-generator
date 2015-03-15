package makasprzak.idea.plugins;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import makasprzak.idea.plugins.model.PsiPojo;

import java.util.*;

import static com.google.common.base.Joiner.on;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Lists.transform;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.apache.commons.lang.StringUtils.capitalize;
import static org.apache.commons.lang.StringUtils.uncapitalize;

/**
 * Created by Maciej Kasprzak on 2014-09-23.
 */
public class PsiElementGenerator {
    public String name(PsiField field) {
        return field.getName();
    }

    public String type(PsiField field) {
        return field.getType().getPresentableText();
    }

    public String field(PsiField field) {
        return "private "+type(field)+" "+name(field);
    }

    public String interfaceName(PsiField field) {
        return capitalize(name(field))+"Step";
    }

    public String with(PsiField field, String nextInterfaceName) {
        return format("%s with%s(%s %s)", nextInterfaceName, capitalize(name(field)), type(field), name(field));
    }

    public String interfaceDefinition(PsiField field, String nextInterfaceName) {
        return format(
                "public static interface %s {\n" +
                    "\t%s;\n" +
                "}\n",
                interfaceName(field), with(field, nextInterfaceName));
    }

    public String builderMethod(PsiField field, String nextInterfaceName) {
        return format(
                "@Override\n" +
                "public %s {\n" +
                        "\tthis.%s = %s;\n" +
                        "\treturn this;\n" +
                "}\n",
                with(field, nextInterfaceName), name(field), name(field)
        );
    }

    public String setterInjection(final PsiField field, final PsiClass psiClass) {
        Iterable<PsiMethod> returnVoid = filter(asList(psiClass.getAllMethods()), returnsVoid());
        Iterable<PsiMethod> takeSingleParameter = filter(returnVoid, takesSingleParameter());
        final Iterable<String> methodNames = Iterables.transform(takeSingleParameter, getName());
        Iterable<String> correspondingSetterNames = Iterables.filter(methodNames, hasCorrespondingSetterName(field));
        Iterable<String> setterInjections = Iterables.transform(correspondingSetterNames, toSetterInjection(field, psiClass));
        ImmutableList<String> setterInjectionsList = ImmutableList.copyOf(setterInjections);
        if (setterInjectionsList.isEmpty()) {
            throw throwError(field);
        } else {
            return setterInjectionsList.get(0);
        }
    }

    public String builderClass(PsiPojo psiPojo) {
        return format(
                "public static class Builder implements %s, BuildStep {}",
                on(", ").join(transform(psiPojo.getFields(), toInterfaceName()))
        );
    }

    public String builderConstructor() {
        return "private Builder() {}";
    }

    public String builderFactoryMethod(PsiClass psiClass, PsiField psiField) {
        return format(
                "public static %s %s() {\n" +
                        "    return new Builder();\n" +
                        "}",
                interfaceName(psiField),
                uncapitalize(psiClass.getName())
        );
    }

    public String buildMethod(PsiClass psiClass, List<PsiField> psiFields) {
        List<PsiMethod> constructors = asList(psiClass.getConstructors());
        if (!constructors.isEmpty()) {
            return constructorInjectionBuildMethod(psiClass, psiFields);
        } else {
            return settersInjectionBuildMethod(psiClass, psiFields);
        }
    }

    public String constructorInjectionBuildMethod(PsiClass psiClass, List<PsiField> psiFields) {
        String className = psiClass.getName();
        return new StringBuilder(
                "@Override\n" +
                        "public ").append(className).append(" build() {\n" +
                "    return new ").append(className).append("(\n" +
                "        ").append(on(",\n        ").join(transform(psiFields, toParameterBinding()))).append("\n" +
                "    );\n" +
                "}").toString();
    }

    private Function<PsiField, Object> toParameterBinding() {
        return new Function<PsiField, Object>() {
            @Override
            public Object apply(PsiField field) {
                return "this." + field.getName();
            }
        };
    }

    private Function<PsiField, Object> toInterfaceName() {
        return new Function<PsiField, Object>() {
            @Override
            public Object apply(PsiField field) {
                return interfaceName(field);
            }
        };
    }

    private Function<String, String> toSetterInjection(final PsiField field, final PsiClass psiClass) {
        return new Function<String, String>() {
            @Override
            public String apply(String s) {
                return toSetterInjection(field,psiClass,s);
            }
        };
    }

    private Predicate<String> hasCorrespondingSetterName(final PsiField field) {
        return new Predicate<String>() {
            @Override
            public boolean apply(String s) {
                return hasCorrespondingSetterName(field, s);
            }
        };
    }

    private Function<PsiMethod, String> getName() {
        return new Function<PsiMethod, String>() {
            @Override
            public String apply(PsiMethod psiMethod) {
                return psiMethod.getName();
            }
        };
    }

    private Predicate<PsiMethod> takesSingleParameter() {
        return new Predicate<PsiMethod>() {
            @Override
            public boolean apply(PsiMethod psiMethod) {
                return takesSingleParameter(psiMethod);
            }
        };
    }

    private Predicate<PsiMethod> returnsVoid() {
        return new Predicate<PsiMethod>() {
            @Override
            public boolean apply(PsiMethod psiMethod) {
                return returnsVoid(psiMethod);
            }
        };
    }

    private RuntimeException throwError(PsiField field) {
        return new RuntimeException("No corresponding setter found for "+field);
    }

    private String toSetterInjection(PsiField field, PsiClass psiClass, String methodName) {
        return format("%s.%s(this.%s);",uncapitalize(psiClass.getName()),methodName,name(field));
    }

    private boolean hasCorrespondingSetterName(PsiField field, String methodName) {
        return methodName.equalsIgnoreCase("set" + name(field));
    }

    private boolean takesSingleParameter(PsiMethod method) {
        return method.getParameterList().getParametersCount() == 1;
    }

    private boolean returnsVoid(PsiMethod method) {
        return method.getReturnType() != null && "void".equals(method.getReturnType().getPresentableText());
    }

    private int compareByParametersCount(PsiMethod left, PsiMethod right) {
        return Integer.valueOf(left.getParameterList().getParametersCount()).compareTo(right.getParameterList().getParametersCount());
    }

    public String buildStepInterface(PsiClass psiClass) {
        return format(
                "public static interface BuildStep {\n" +
                        "    %s build();\n" +
                        "}",
                psiClass.getName()
        );
    }

    public Map<PsiField, String> mapReturnedInterfaces(List<PsiField> psiFields) {
        Map<PsiField,String> returnedInterfaceByField = new HashMap<>();
        for (int i = 0; i < psiFields.size()-1; i++) {
            returnedInterfaceByField.put(psiFields.get(i), interfaceName(psiFields.get(i + 1)));
        }
        if (!psiFields.isEmpty()) {
            returnedInterfaceByField.put(psiFields.get(psiFields.size() - 1), "BuildStep");
        }
        return returnedInterfaceByField;
    }

    public String settersInjectionBuildMethod(final PsiClass psiClass, List<PsiField> psiFields) {
        String className = psiClass.getName();
        return new StringBuilder(
                "@Override\n" +
                        "public ").append(className).append(" build() {\n" +
                "    ").append(className).append(" ").append(uncapitalize(className)).append(" = new ").append(className).append("();\n" +
                "    ").append(on("\n    ").join(transform(psiFields, toSetterInjection(psiClass)))).append("\n" +
                "    return ").append(uncapitalize(className)).append(";\n" +
                "}")
                .toString();
    }

    private Function<PsiField, String> toSetterInjection(final PsiClass psiClass) {
        return new Function<PsiField, String>() {
    @Override
    public String apply(PsiField field) {
        return setterInjection(field, psiClass);
    }
};
    }
}
