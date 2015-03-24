package makasprzak.idea.plugins.propertiesstrategy;

import makasprzak.idea.plugins.dialog.DialogFactories;

public enum PropertiesConcreteStrategy {
    FROM_FIELDS {
        @Override
        public PropertiesStrategy get() {
            return new FromFieldsCS(DialogFactories.FROM_FIELDS.get());
        }
    };
    public abstract PropertiesStrategy get();
}
