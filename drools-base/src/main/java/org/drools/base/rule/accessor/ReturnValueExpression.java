package org.drools.base.rule.accessor;

import org.drools.base.base.ValueResolver;
import org.drools.base.reteoo.BaseTuple;
import org.drools.base.rule.Declaration;
import org.kie.api.runtime.rule.FactHandle;

public interface ReturnValueExpression
    extends
    Invoker {
    
    Object createContext();
    
    FieldValue evaluate(FactHandle handle,
                        BaseTuple tuple,
                        Declaration[] previousDeclarations,
                        Declaration[] localDeclarations,
                        ValueResolver valueResolver,
                        Object context ) throws Exception;
    
    void replaceDeclaration(Declaration declaration,
                            Declaration resolved);

}
