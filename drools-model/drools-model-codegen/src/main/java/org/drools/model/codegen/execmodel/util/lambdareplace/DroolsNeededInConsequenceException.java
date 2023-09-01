package org.drools.model.codegen.execmodel.util.lambdareplace;

class DroolsNeededInConsequenceException extends DoNotConvertLambdaException {

    public DroolsNeededInConsequenceException(String lambda) {
        super(lambda);
    }

    @Override
    public String getMessage() {
        return "Drools parameter needed in consequence " + lambda;
    }
}
