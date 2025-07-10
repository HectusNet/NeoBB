package net.hectus.neobb;

import com.marcpg.libpg.init.PaperLoaderPG;

public class NeoLoader extends PaperLoaderPG {
    @Override
    public String kotlinVersion() {
        return "2.1.20";
    }

    @Override
    public void load() {
        addRepository("https://repo.xenondevs.xyz/releases/", "xenondevs");

        addDependency("kotlin.reflect.full.KCallables", "org.jetbrains.kotlin:kotlin-reflect:" + kotlinVersion());
        addDependency("kotlinx.serialization.json.Json", "org.jetbrains.kotlinx:kotlinx-serialization-json-jvm:1.8.1");
        addDependency("org.postgresql.Driver", "org.postgresql:postgresql:42.7.5");
        addDependency("xyz.xenondevs.invui.InvUI", "xyz.xenondevs.invui:invui:pom:1.44");
    }
}
