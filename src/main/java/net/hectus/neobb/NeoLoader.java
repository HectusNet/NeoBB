package net.hectus.neobb;

import com.marcpg.libpg.storing.Pair;
import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnstableApiUsage")
public class NeoLoader implements PluginLoader {
    @Override
    public void classloader(@NotNull PluginClasspathBuilder classpathBuilder) {
        download(classpathBuilder, "xyz.xenondevs.invui.InvUI", Pair.of("xenondevs", "https://repo.xenondevs.xyz/releases/"), "xyz.xenondevs.invui:invui:pom:1.44");
        download(classpathBuilder, "org.postgresql.Driver", Pair.of("maven-central", "https://repo1.maven.org/maven2/"), "org.postgresql:postgresql:42.7.5");

        download(classpathBuilder, "kotlin.KotlinVersion", Pair.of("maven-central", "https://repo1.maven.org/maven2/"), "org.jetbrains.kotlin:kotlin-stdlib:2.1.10");
        download(classpathBuilder, "kotlin.reflect.full.KCallables", Pair.of("maven-central", "https://repo1.maven.org/maven2/"), "org.jetbrains.kotlin:kotlin-reflect:2.1.10");
        download(classpathBuilder, "kotlinx.serialization.json.Json", Pair.of("maven-central", "https://repo1.maven.org/maven2/"), "org.jetbrains.kotlinx:kotlinx-serialization-json-jvm:1.8.1");
    }

    private void download(PluginClasspathBuilder builder, String className, Pair<String, String> repo, String artifactId) {
        try {
            Class.forName(className);
        } catch (ClassNotFoundException e) {
            MavenLibraryResolver resolver = new MavenLibraryResolver();
            resolver.addRepository(new RemoteRepository.Builder(repo.left(), "default", repo.right()).build());
            resolver.addDependency(new Dependency(new DefaultArtifact(artifactId), null));
            builder.addLibrary(resolver);
        }
    }
}
