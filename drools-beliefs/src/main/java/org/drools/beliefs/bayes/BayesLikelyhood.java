package org.drools.beliefs.bayes;

import org.drools.beliefs.graph.Graph;
import org.drools.beliefs.graph.GraphNode;

import java.util.Arrays;

public class BayesLikelyhood {
    private BayesVariable variable;
    private double[]      distribution;
    private double[][]    varPotential;

    private BayesVariable[] vars;
    private int[]           multipliers;
    private int[]           parentVarPos;
    private int[]           parentIndexMultipliers;
    private int             varPos;

    public BayesLikelyhood(Graph graph, JunctionTreeClique jtNode, GraphNode<BayesVariable> varNode, double[] distribution) {
        vars = jtNode.getValues().toArray( new BayesVariable[jtNode.getValues().size()] );

        this.variable = varNode.getContent();
        this.distribution = distribution;

        BayesVariable[] parents = new BayesVariable[varNode.getInEdges().size()];
        for ( int i = 0; i < parents.length; i++ ) {
            parents[i] = (BayesVariable) varNode.getInEdges().get(i).getOutGraphNode().getContent();
        }

        varPotential = new double[ this.variable.getProbabilityTable().length ][];
        Arrays.fill(varPotential, distribution);

        BayesVariable[] vars = jtNode.getValues().toArray( new BayesVariable[jtNode.getValues().size()] );
        int numberOfStates = PotentialMultiplier.createNumberOfStates(vars);
        multipliers = PotentialMultiplier.createIndexMultipliers(vars, numberOfStates);

        parentVarPos = PotentialMultiplier.createSubsetVarPos(vars, parents);
        int parentsNumberOfStates = PotentialMultiplier.createNumberOfStates(parents);
        parentIndexMultipliers = PotentialMultiplier.createIndexMultipliers(parents, parentsNumberOfStates);
//
        varPos = -1;
        for( int i = 0; i < vars.length; i++) {
            if ( vars[i] == variable)  {
                varPos = i;
                break;
            }
        }
        if ( varPos == -1 ) {
            throw new IllegalStateException( "Unable to find Variable in set" );
        }
    }

    public BayesVariable getVariable() {
        return variable;
    }

    public double[] getDistribution() {
        return distribution;
    }

    public void multiplyInto(double[] trgPotential) {
        PotentialMultiplier m = new PotentialMultiplier(varPotential, varPos, parentVarPos, parentIndexMultipliers,
                                                        vars, multipliers, trgPotential);

        m.multiple();

//        int j = 0;
//        int jlength = distribution.length;
//        for ( int i = 0, length = potential.length; i < length; i++ ) {
//            potential[i] = potential[i] * distribution[j++];
//            if ( j == jlength ) {
//                j = 0;
//            }
//        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }

        BayesLikelyhood that = (BayesLikelyhood) o;

        if (!Arrays.equals(distribution, that.distribution)) { return false; }
        if (!variable.equals(that.variable)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        int result = variable.hashCode();
        result = 31 * result + Arrays.hashCode(distribution);
        return result;
    }
}
