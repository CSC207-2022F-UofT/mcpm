package org.hydev.mcpm.client.arguments.parsers

/**
 * Implemented by parsers that define methods to "configure" ArgsParse4j and then "build" a CommandEntry object.
 */
interface CommandParser : CommandHandler, CommandConfigurator
