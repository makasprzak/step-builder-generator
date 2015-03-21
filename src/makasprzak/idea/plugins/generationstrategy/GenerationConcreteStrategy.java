package makasprzak.idea.plugins.generationstrategy;

import makasprzak.idea.plugins.GeneratorDialogFactory;

public enum GenerationConcreteStrategy {
    FROM_FIELDS {
        @Override
        public GenerationStrategy get() {
            return new GenerateFromFieldsCS(new GeneratorDialogFactory());
        }
    };
    public abstract GenerationStrategy get();
}
