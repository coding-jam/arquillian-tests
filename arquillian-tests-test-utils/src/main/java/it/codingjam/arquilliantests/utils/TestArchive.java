package it.codingjam.arquilliantests.utils;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.MavenFormatStage;

/**
 * Created by pizzo on 06/07/16.
 */
public class TestArchive {

    public static final String CONTEXT_PATH = "arquillian-tests-web";

    private TestArchive() {
        // empty constructor to prevent instance
    }

    public static JavaArchive asEjbJar() {
        return ShrinkWrap.create(JavaArchive.class, "arquillian-tests-ejb.jar")
                .addPackages(true, "it.codingjam.arquilliantests.logic", "it.codingjam.arquilliantests.utils")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsManifestResource("META-INF/persistence.xml", "persistence.xml")
                .addAsResource("META-INF/orm.xml")
                .merge(getMavenDependencies());
    }

    public static WebArchive asWar() {
        return ShrinkWrap.create(WebArchive.class, CONTEXT_PATH + ".war")
                .addPackages(true, "it.codingjam.arquilliantests", "it.codingjam.arquilliantests.utils")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsResource("META-INF/persistence.xml")
                .addAsResource("META-INF/orm.xml")
                .addAsLibraries(resolveMavenDependencies().asFile());
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
