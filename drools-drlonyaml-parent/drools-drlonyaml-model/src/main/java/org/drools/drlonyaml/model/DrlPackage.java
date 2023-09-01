package org.drools.drlonyaml.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.drools.drl.ast.descr.FunctionDescr;
import org.drools.drl.ast.descr.GlobalDescr;
import org.drools.drl.ast.descr.ImportDescr;
import org.drools.drl.ast.descr.PackageDescr;
import org.drools.drl.ast.descr.RuleDescr;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"name", "imports", "globals", "rules", "functions"})
public class DrlPackage {
    @JsonInclude(Include.NON_EMPTY)
    private String name = ""; // default empty, consistent with DRL parser.
    @JsonInclude(Include.NON_EMPTY)
    private List<Import> imports = new ArrayList<>();
    @JsonInclude(Include.NON_EMPTY)
    private List<Global> globals = new ArrayList<>();
    private List<Rule> rules = new ArrayList<>();
    @JsonInclude(Include.NON_EMPTY)
    private List<Function> functions = new ArrayList<>();
    
    public static DrlPackage from(PackageDescr o) {
        Objects.requireNonNull(o);
        DrlPackage result = new DrlPackage();
        result.name = o.getName();
        for (ImportDescr i : o.getImports()) {
            result.imports.add(Import.from(i));
        }
        for (GlobalDescr g : o.getGlobals()) {
            result.globals.add(Global.from(g));
        }
        for (RuleDescr r : o.getRules()) {
            result.rules.add(Rule.from(r));
        }
        for (FunctionDescr f : o.getFunctions()) {
            result.functions.add(Function.from(f));
        }
        return result;
    }

    public String getName() {
        return name;
    }

    public List<Import> getImports() {
        return imports;
    }
    
    public List<Global> getGlobals() {
        return globals;
    }

    public List<Rule> getRules() {
        return rules;
    }
    
    public List<Function> getFunctions() {
        return functions;
    }
}
