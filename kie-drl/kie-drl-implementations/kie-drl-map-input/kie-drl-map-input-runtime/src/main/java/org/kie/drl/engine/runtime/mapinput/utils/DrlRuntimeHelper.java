package org.kie.drl.engine.runtime.mapinput.utils;

import java.util.Optional;

import org.kie.api.runtime.KieSession;
import org.kie.drl.api.identifiers.DrlSessionIdFactory;
import org.kie.drl.api.identifiers.KieDrlComponentRoot;
import org.kie.drl.api.identifiers.LocalComponentIdDrlSession;
import org.kie.drl.engine.runtime.mapinput.model.EfestoOutputDrlMap;
import org.kie.efesto.common.api.identifiers.EfestoAppRoot;
import org.kie.efesto.runtimemanager.api.exceptions.KieRuntimeServiceException;
import org.kie.efesto.runtimemanager.api.model.BaseEfestoInput;
import org.kie.efesto.runtimemanager.api.model.EfestoInput;
import org.kie.efesto.runtimemanager.api.model.EfestoMapInputDTO;
import org.kie.efesto.runtimemanager.api.model.EfestoRuntimeContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.kie.drl.engine.runtime.utils.EfestoKieSessionUtil.loadKieSession;
import static org.kie.efesto.runtimemanager.api.utils.GeneratedResourceUtils.getGeneratedExecutableResource;

public class DrlRuntimeHelper {

    private static final Logger logger = LoggerFactory.getLogger(DrlRuntimeHelper.class.getName());

    private DrlRuntimeHelper() {
    }

    public static boolean canManage(EfestoInput toEvaluate, EfestoRuntimeContext context) {
        return getGeneratedExecutableResource(toEvaluate.getModelLocalUriId(), context.getGeneratedResourcesMap()).isPresent();
    }

    public static Optional<EfestoOutputDrlMap> execute(BaseEfestoInput<EfestoMapInputDTO> toEvaluate, EfestoRuntimeContext context) {
        KieSession kieSession;
        try {
            kieSession = loadKieSession(toEvaluate.getModelLocalUriId(), context);
        } catch (Exception e) {
            logger.warn("{} can not execute {}",
                        DrlRuntimeHelper.class.getName(),
                        toEvaluate.getModelLocalUriId());
            return Optional.empty();
        }
        if (kieSession == null) {
            return Optional.empty();
        }
        try {
            MapInputSessionUtils.Builder builder = MapInputSessionUtils.builder(kieSession, toEvaluate.getInputData());
            final MapInputSessionUtils mapInputSessionUtils = builder.build();
            long identifier = kieSession.getIdentifier();
            mapInputSessionUtils.fireAllRules();
            LocalComponentIdDrlSession modelLocalUriId = new EfestoAppRoot()
                    .get(KieDrlComponentRoot.class)
                    .get(DrlSessionIdFactory.class)
                    .get(toEvaluate.getModelLocalUriId().basePath(), identifier);
            return Optional.of(new EfestoOutputDrlMap(modelLocalUriId, null));
        } catch (Exception e) {
            throw new KieRuntimeServiceException(String.format("%s failed to execute %s",
                                                               DrlRuntimeHelper.class.getName(),
                                                               toEvaluate.getModelLocalUriId()), e);
        }
    }

}
