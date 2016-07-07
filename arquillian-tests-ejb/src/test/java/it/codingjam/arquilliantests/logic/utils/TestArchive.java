package it.codingjam.arquilliantests.logic.utils;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.MavenFormatStage;

/**
 * Created by pizzo on 06/07/16.
 */
public class TestArchive {

    private TestArchive() {
        // empty constructor to prevent instance
    }

    public static JavaArchive asEjbJar() {
        return ShrinkWrap.create(JavaArchive.class, "arquillian-tests-ejb.jar")
                .addPackages(true, "it.codingjam.arquilliantests.logic")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsResource("META-INF/persistence.xml")
                .addAsResource("META-INF/orm.xml")
                .merge(getMavenDependencies());
    }

    private static JavaArchive getMavenDependencies() {
        return ShrinkWrap.createFromZipFile(JavaArchive.class, resolveMavenDependencies()
                .asSingleFile());
    }

    private static MavenFormatStage resolveMavenDependencies() {
        return Maven.resolver()
                .loadPomFromFile("pom.xml")
                .importRuntimeDependencies()
                .resolve("commons-codec:commons-codec")
                .withTransitivity();
    }
}
