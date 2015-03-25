package makasprzak.idea.plugins.propertiesstrategy;

import makasprzak.idea.plugins.dialog.DialogFactories;

public enum PropertiesConcreteStrategy {
    FROM_SETTERS(new AskUserPropertiesStrategy(DialogFactories.FROM_METHODS.get())),
    FROM_CONSTRUCTOR_ARGS(new UseConstructorArgsPropertiesStrategy());

    private final PropertiesStrategy propertiesStrategy;

    public PropertiesStrategy get(){
        return this.propertiesStrategy;
    }

    private PropertiesConcreteStrategy(PropertiesStrategy propertiesStrategy) {
        this.propertiesStrategy = propertiesStrategy;
    }
}
