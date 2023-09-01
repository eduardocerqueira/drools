package org.drools.scenariosimulation.api.model;

/**
 * <code>Settings</code> to be used inside scenario simulation asset
 */
public class Settings {

    private String dmoSession;

    private String dmnFilePath;

    private ScenarioSimulationModel.Type type;

    private String fileName;

    private String kieSession;

    private String kieBase;

    private String ruleFlowGroup;

    private String dmnNamespace;

    private String dmnName;

    private boolean skipFromBuild = false;
    private boolean stateless = false;

    public String getDmoSession() {
        return dmoSession;
    }

    public void setDmoSession(String ruleSession) {
        this.dmoSession = ruleSession;
    }

    public String getDmnFilePath() {
        return dmnFilePath;
    }

    public void setDmnFilePath(String dmnFilePath) {
        this.dmnFilePath = dmnFilePath;
    }

    public ScenarioSimulationModel.Type getType() {
        return type;
    }

    public void setType(ScenarioSimulationModel.Type type) {
        this.type = type;
    }

    /**
     * @deprecated
     */
    @Deprecated
    public String getFileName() {
        return fileName;
    }

    /**
     * @deprecated
     */
    @Deprecated
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * @deprecated
     */
    @Deprecated
    public String getKieSession() {
        return kieSession;
    }

    /**
     * @deprecated
     */
    @Deprecated
    public void setKieSession(String kieSession) {
        this.kieSession = kieSession;
    }

    /**
     * @deprecated
     */
    @Deprecated
    public String getKieBase() {
        return kieBase;
    }

    /**
     * @deprecated
     */
    @Deprecated
    public void setKieBase(String kieBase) {
        this.kieBase = kieBase;
    }

    public String getRuleFlowGroup() {
        return ruleFlowGroup;
    }

    public void setRuleFlowGroup(String ruleFlowGroup) {
        this.ruleFlowGroup = ruleFlowGroup;
    }

    public String getDmnNamespace() {
        return dmnNamespace;
    }

    public void setDmnNamespace(String dmnNamespace) {
        this.dmnNamespace = dmnNamespace;
    }

    public String getDmnName() {
        return dmnName;
    }

    public void setDmnName(String dmnName) {
        this.dmnName = dmnName;
    }

    public boolean isSkipFromBuild() {
        return skipFromBuild;
    }

    public void setSkipFromBuild(boolean skipFromBuild) {
        this.skipFromBuild = skipFromBuild;
    }

    public boolean isStateless() {
        return stateless;
    }

    public void setStateless(boolean stateless) {
        this.stateless = stateless;
    }

    public Settings cloneSettings() {
        Settings cloned = new Settings();
        cloned.dmoSession = this.dmoSession;
        cloned.dmnFilePath = this.dmnFilePath;
        cloned.type = this.type;
        cloned.fileName = this.fileName;
        cloned.kieSession = this.kieSession;
        cloned.kieBase = this.kieBase;
        cloned.ruleFlowGroup = this.ruleFlowGroup;
        cloned.dmnNamespace = this.dmnNamespace;
        cloned.dmnName = this.dmnName;
        cloned.skipFromBuild = this.skipFromBuild;
        cloned.stateless = this.stateless;
        return cloned;
    }
}
