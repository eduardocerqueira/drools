package org.kie.pmml.models.drools.tree.compiler.executor;

import java.io.FileInputStream;
import java.io.Serializable;

import org.dmg.pmml.PMML;
import org.dmg.pmml.tree.TreeModel;
import org.drools.base.util.CloneUtil;
import org.junit.jupiter.api.Test;
import org.kie.pmml.api.enums.PMML_MODEL;
import org.kie.pmml.commons.model.abstracts.AbstractKiePMMLComponent;
import org.kie.pmml.compiler.api.dto.CommonCompilationDTO;
import org.kie.pmml.compiler.commons.mocks.ExternalizableMock;
import org.kie.pmml.compiler.commons.mocks.PMMLCompilationContextMock;
import org.kie.pmml.compiler.commons.utils.KiePMMLUtil;
import org.kie.pmml.models.drools.commons.model.KiePMMLDroolsModelWithSources;
import org.drools.util.FileUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.kie.pmml.commons.Constants.PACKAGE_NAME;

public class TreeModelImplementationProviderTest {

    private static final TreeModelImplementationProvider PROVIDER = new TreeModelImplementationProvider();
    private static final String SOURCE_1 = "TreeSample.pmml";

    @Test
    void getPMMLModelType() {
        assertThat(PROVIDER.getPMMLModelType()).isEqualTo(PMML_MODEL.TREE_MODEL);
    }


    @Test
    void getKiePMMLModelWithSources() throws Exception {
        final PMML pmml = getPMML(SOURCE_1);
        final CommonCompilationDTO<TreeModel> compilationDTO =
                CommonCompilationDTO.fromGeneratedPackageNameAndFields(PACKAGE_NAME,
                                                                       pmml,
                                                                       (TreeModel) pmml.getModels().get(0),
                                                                       new PMMLCompilationContextMock(), "FILENAME");
        final KiePMMLDroolsModelWithSources retrieved = PROVIDER.getKiePMMLModelWithSources(compilationDTO);
        assertThat(retrieved).isNotNull();
        commonVerifyIsDeepCloneable(retrieved);
    }

    private PMML getPMML(String source) throws Exception {
        final FileInputStream fis = FileUtils.getFileInputStream(source);
        final PMML toReturn = KiePMMLUtil.load(fis, source);
        assertThat(toReturn).isNotNull();
        assertThat(toReturn.getModels()).hasSize(1);
        assertThat(toReturn.getModels().get(0)).isInstanceOf(TreeModel.class);
        return toReturn;
    }

    private void commonVerifyIsDeepCloneable(AbstractKiePMMLComponent toVerify) {
        assertThat(toVerify).isInstanceOf(Serializable.class);
        ExternalizableMock externalizableMock = new ExternalizableMock();
        externalizableMock.setKiePMMLComponent(toVerify);
        CloneUtil.deepClone(externalizableMock);
    }
}