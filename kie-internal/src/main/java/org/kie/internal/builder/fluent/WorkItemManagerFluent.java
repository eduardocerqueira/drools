package org.kie.internal.builder.fluent;

import java.util.Map;

import org.kie.api.runtime.process.WorkItemHandler;

/**
 * A work item manager is responsible for finding the right
 * work item handler when a work item should be executed and
 * should be notified when this work item has been completed
 * (or aborted).
 */
public interface WorkItemManagerFluent<T,P,U> {

    /**
     * Notifies the work item manager that the work item with the given
     * id has been completed. Results related to the execution of this
     * work item can be passed.
     *
     * @param id the id of the work item that has been completed
     * @param results the results related to this work item, or <code>null</code> if there are no results
     */
    T completeWorkItem(long id, Map<String, Object> results);

    /**
     * Notifies the work item manager that the work item with the given
     * id could not be executed and should be aborted.
     *
     * @param id the id of the work item that should be aborted
     */
    T abortWorkItem(long id);

    /**
     * Register the given handler for all work items of the given
     * type of work
     *
     * @param workItemName the type of work this work item handler can execute
     * @param handler the handler for executing work items
     */
    T registerWorkItemHandler(String workItemName, WorkItemHandler handler);

    KieSessionFluent getKieSession();
}
