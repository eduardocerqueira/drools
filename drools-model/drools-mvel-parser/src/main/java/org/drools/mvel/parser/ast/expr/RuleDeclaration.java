package org.drools.mvel.parser.ast.expr;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.SimpleName;
import org.drools.mvel.parser.ast.visitor.DrlGenericVisitor;
import org.drools.mvel.parser.ast.visitor.DrlVoidVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;

public class RuleDeclaration extends TypeDeclaration<RuleDeclaration> {

    private final RuleBody ruleBody;

    public RuleDeclaration(TokenRange range, NodeList<AnnotationExpr> annotations, SimpleName name, RuleBody ruleBody ) {
        super( range, NodeList.nodeList(), annotations, name, new NodeList<>() );
        this.ruleBody = ruleBody;
    }

    @Override
    public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
        return ((DrlGenericVisitor<R, A>)v).visit(this, arg);
    }

    @Override
    public <A> void accept(VoidVisitor<A> v, A arg) {
        ((DrlVoidVisitor<A>)v).visit(this, arg);
    }

    public RuleBody getRuleBody() {
        return ruleBody;
    }

    @Override
    public ResolvedReferenceTypeDeclaration resolve() {
        return null;
    }
}
