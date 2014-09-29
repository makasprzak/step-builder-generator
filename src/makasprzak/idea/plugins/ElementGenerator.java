package makasprzak.idea.plugins;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;

import java.util.*;

import static com.google.common.base.Joiner.on;
import static com.google.common.collect.Lists.transform;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.apache.commons.lang.StringUtils.capitalize;
import static org.apache.commons.lang.StringUtils.uncapitalize;

/**
 * Created by Maciej Kasprzak on 2014-09-23.
 */
public class ElementGenerator {
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

    public String setterInjection(PsiField field, PsiClass psiClass) {
        return asList(psiClass.getAllMethods())
                .stream()
                .filter(this::returnsVoid)
                .filter(this::takesSingleParameter)
                .map(PsiMethod::getName)
                .filter(methodName -> hasCorrespondingSetterName(field, methodName))
                .map(methodName -> toSetterInjection(field, psiClass, methodName))
                .findFirst()
                .orElseThrow(() -> throwError(field));
    }

    private RuntimeException throwError(PsiField field) {
        return new RuntimeException("No corresponding setter found for "+field);
    }

    private String toSetterInjection(PsiField field, PsiClass psiClass, String methodName) {
        return format("%s.%s(this.%s);",uncapitalize(psiClass.getName()),methodName,name(field));
    }

    private boolean hasCorrespondingSetterName(PsiField field, String methodName) {
        return methodName.equalsIgnoreCase("set"+name(field));
    }

    private boolean takesSingleParameter(PsiMethod method) {
        return method.getParameterList().getParametersCount() == 1;
    }

    private boolean returnsVoid(PsiMethod method) {
        return method.getReturnType() != null && "void".equals(method.getReturnType().getPresentableText());
    }

    public String builderClass(List<PsiField> fields) {
        return format(
                "public static class Builder implements %s, BuildStep {}",
                on(", ").join(transform(fields, field -> interfaceName(field)))
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
        Optional<PsiMethod> first = constructors
                .stream()
                .sorted((left, right) -> compareByParametersCount(right, left))
                .findFirst();
        if (first.isPresent()) {
            String className = psiClass.getName();
            return new StringBuilder(
                    "@Override\n" +
                            "public ").append(className).append(" build() {\n" +
                    "    return new ").append(className).append("(\n" +
                    "        ").append(on(",\n        ").join(transform(psiFields, field -> "this."+field.getName()))).append("\n" +
                    "    );\n" +
                    "}").toString();

        } else {
            return settersInjectioinBuildMethod(psiClass, psiFields);
        }
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

    private String settersInjectioinBuildMethod(PsiClass psiClass, List<PsiField> psiFields) {
        String className = psiClass.getName();
        return new StringBuilder(
                "@Override\n" +
                        "public ").append(className).append(" build() {\n" +
                "    ").append(className).append(" ").append(uncapitalize(className)).append(" = new ").append(className).append("();\n" +
                "    ").append(on("\n    ").join(transform(psiFields, psiField -> setterInjection(psiField, psiClass)))).append("\n" +
                "    return ").append(uncapitalize(className)).append(";\n" +
                "}")
                .toString();
    }
}
