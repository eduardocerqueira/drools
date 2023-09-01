package org.drools.core.impl;

import java.io.InputStream;

import org.kie.api.builder.KieModule;
import org.kie.api.builder.ReleaseId;
import org.kie.api.builder.Results;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.StatelessKieSession;

public interface InternalKieContainer extends KieContainer {

    /**
     * Returns an already created defualt KieSession for this KieContainer or creates a new one
     * @throws RuntimeException if this KieContainer doesn't have any defualt KieSession
     * @see org.kie.api.builder.model.KieSessionModel#setDefault(boolean)
     */
    KieSession getKieSession();

    /**
     * Returns an already created KieSession with the given name for this KieContainer or creates a new one
     * @throws RuntimeException if this KieContainer doesn't have any defualt KieSession
     * @see org.kie.api.builder.model.KieSessionModel#setDefault(boolean)
     */
    KieSession getKieSession(String kSessionName);

    StatelessKieSession getStatelessKieSession();

    StatelessKieSession getStatelessKieSession(String kSessionName);

    /**
     * Internal use: returns the RelaseId configured while creating the Kiecontainer, 
     * or alternatively if the RelaseId was NOT configured while creating the Kiecontainer,
     * returns the the ReleaseId of the KieModule wrapped by this KieContainer. 
     * Additionally, please notice this will always gets updated to the parameter passed as updateToVersion(ReleaseId).
     * @see org.kie.api.runtime.KieContainer#getReleaseId()
     * @see org.kie.api.runtime.KieContainer#updateToVersion(ReleaseId)
     */
    ReleaseId getContainerReleaseId();

    long getCreationTimestamp();

    Results updateDependencyToVersion(ReleaseId currentReleaseId, ReleaseId newReleaseId);

    InputStream getPomAsStream();

    /**
     * @return the {@link KieModule} of the {@link #getReleaseId()}
     */
    KieModule getMainKieModule();

    /**
     * Returns the ID assigned to the container.
     * @return the ID assigned to the container.
     */
	String getContainerId();
	
	/**
	 * Returns the RelaseId configured while creating the Kiecontainer.
	 * @return the RelaseId configured while creating the Kiecontainer.
	 */
	ReleaseId getConfiguredReleaseId();
	
	/**
	 * Returns the actual resolved ReleaseId. 
	 * @return the actual resolved ReleaseId. 
	 */
	ReleaseId getResolvedReleaseId();

    void disposeSession(KieSession kieSession);

}
