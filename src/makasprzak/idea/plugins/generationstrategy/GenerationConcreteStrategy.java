package makasprzak.idea.plugins.generationstrategy;

import makasprzak.idea.plugins.dialog.DialogFactories;

public enum GenerationConcreteStrategy {
    FROM_FIELDS {
        @Override
        public GenerationStrategy get() {
            return new GenerateFromFieldsCS(DialogFactories.FROM_FIELDS.get());
        }
    };
    public abstract GenerationStrategy get();
}
