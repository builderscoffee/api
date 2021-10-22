package eu.builderscoffee.api.common.configuration;

import eu.builderscoffee.api.bukkit.utils.Serializations;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.val;
import lombok.var;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

@UtilityClass
public class Configuration {

    /**
     * Créer la config avec la structure indiqué dans le dossier du plugin spécifié.
     *
     //* @param plugin    Plugin
     //* @param structure Structure
     */
    @SneakyThrows
    public <T> T readOrCreateConfiguration(String pluginName, Class<T> structure) {
        if (!structure.isAnnotationPresent(eu.builderscoffee.api.common.configuration.annotation.Configuration.class))
            throw new RuntimeException("Tried to load a non-annotated configuration");

        var configName = structure.getAnnotation(eu.builderscoffee.api.common.configuration.annotation.Configuration.class).value();
        if (configName.isEmpty())
            configName = structure.getSimpleName();
        // On veut du YML
        if(!configName.endsWith(".yml"))
            configName = configName + ".yml";

        val pluginPath = Paths.get("plugins", pluginName, configName);
        if(!Files.exists(pluginPath)) {
            Files.createDirectories(pluginPath.getParent());
            Files.createFile(pluginPath);
            Files.write(
                    pluginPath,
                    Serializations.serialize(structure.newInstance()).getBytes(StandardCharsets.UTF_8)
            );
        }

        return Serializations.deserialize(
                new String(Files.readAllBytes(pluginPath), StandardCharsets.UTF_8),
                structure
        );
    }

    /**
     * Ecrit la configuration avec les valeurs contenue dans l'instance spécifiée.
     *
     //* @param plugin Plugin
     * @param configuration Configuration
     * @param <T> Type de la configuration
     */
    @SneakyThrows
    public <T> void writeConfiguration(String pluginName, T configuration) {
        val structure = configuration.getClass();
        if (!structure.isAnnotationPresent(eu.builderscoffee.api.common.configuration.annotation.Configuration.class))
            throw new RuntimeException("Tried to load a non-annotated configuration");

        var configName = structure.getAnnotation(eu.builderscoffee.api.common.configuration.annotation.Configuration.class).value();
        if (configName.isEmpty())
            configName = structure.getSimpleName();
        // On veut du YML
        if(!configName.endsWith(".yml"))
            configName = configName + ".yml";

        val pluginPath = Paths.get("plugins", pluginName, configName);
        Files.write(
                pluginPath,
                Serializations.serialize(configuration).getBytes(StandardCharsets.UTF_8)
        );
    }
}
