package org.hydev.mcpm.client.models;

import java.util.List;

/**
 * Defines the structure of db.json object exposed by the mcpm server (GET /db).
 *
 * @param plugins A list of plugins that are provided by the server.
 */
public record Database(List<PluginModel> plugins) { }
