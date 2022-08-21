package io.extact.msa.rms.console;

import static com.tngtech.archunit.library.Architectures.*;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import io.extact.msa.rms.console.ui.ScreenController;
import io.extact.msa.rms.console.ui.textio.TextIoUtils;

@AnalyzeClasses(packages = "io.extact.msa.rms.console", importOptions = ImportOption.DoNotIncludeTests.class)
class ConsoleLayerDependencyArchUnitTest {

    // ---------------------------------------------------------------------
    // rms.consoleのアプリケーションアーキテクチャレベルの依存関係の定義
    // ---------------------------------------------------------------------

    /**
     * アプリケーションアーキテクチャのレイヤと依存関係の定義
     * <pre>
     * ・uiレイヤはどのレイヤからも依存されていないこと（uiレイヤは誰も使ってはダメ））
     * ・serviceレイヤはuiレイヤからのみ依存を許可（serviceレイヤを使って良いのはuiレイヤのみ）
     * ・externalレイヤはserviceレイヤからのみ依存を許可（externalレイヤを使って良いのはserviceレイヤのみ）
     * ・modelレイヤは上記3つのレイヤからの依存を許可
     * </pre>
     */
    @ArchTest
    static final ArchRule test_レイヤー間の依存関係の定義 = layeredArchitecture()
            .layer("ui").definedBy("io.extact.msa.rms.console.ui..")
            .layer("service").definedBy("io.extact.msa.rms.console.service..")
            .layer("external").definedBy("io.extact.msa.rms.console.external..")
            .layer("model").definedBy("io.extact.msa.rms.console.model..")

            .whereLayer("ui").mayNotBeAccessedByAnyLayer()
                .ignoreDependency(ConsoleMain.class, ScreenController.class)
                .ignoreDependency(ConsoleMain.class, TextIoUtils.class)
            .whereLayer("service").mayOnlyBeAccessedByLayers("ui")
            .whereLayer("external").mayOnlyBeAccessedByLayers("service")
            .whereLayer("model").mayOnlyBeAccessedByLayers("ui", "service", "external");
}
