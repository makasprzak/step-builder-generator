package makasprzak.idea.plugins.propertiesstrategy;

import makasprzak.idea.plugins.dialog.DialogFactories;

public enum PropertiesConcreteStrategy {
    FROM_FIELDS(new FromFieldsCS(DialogFactories.FROM_FIELDS.get())),
    FROM_CONSTRUCTOR_ARGS(new FromConstructorArgsCS());

    private final PropertiesStrategy propertiesStrategy;

    public PropertiesStrategy get(){
        return this.propertiesStrategy;
    }

    private PropertiesConcreteStrategy(PropertiesStrategy propertiesStrategy) {
        this.propertiesStrategy = propertiesStrategy;
    }
}
